/**
 * 
 */
package edu.csulb.des;

import cc.mallet.util.StatFunctions;
import cc.mallet.util.Univariate;

/**
 * @author Manav
 *
 */
public class PerformIMCSimulation {

	

	private SimulateWolfCommunication simulateWolfCommunication;
	
	private int totalMessagesLost = 0;
	private int totalMessagesSent = 0;

	private double averageMessagesLost = 0.0;

	private double probabilityAMessageIsLost = 0.0;
	
	// k copies of the original message to be sent to Wolf Communications
	int k;
	
	// Stores the ci_left value of the 0.90 confidence interval
	double p2;

	public PerformIMCSimulation() {
		// TODO Auto-generated constructor stub
	}

	private void performIMCSimulation(int n, double delta, boolean isStrategyApplied) {
		
		/*int totalMessagesLost = 0;
		int totalMessagesSent = 0;

		double averageMessagesLost = 0.0;

		double probabilityAMessageIsLost = 0.0;*/
		
		double X[] = new double[n];

		try {
			
			if (isStrategyApplied) {
				
				// Run this simulation till a least value of k is determined as per the condition given in Exercise 4.
				int k = 0;
				do {
					k++;
					
					// Perform 10000 simulation until the condition in while is true
					for (int i = 0; i < n; i++) {
						simulateWolfCommunication = new SimulateWolfCommunication();
						simulateWolfCommunication.initiateSystemState();
						simulateWolfCommunication.generateMessageArrivals(k);
						simulateWolfCommunication.runSimulation(k);
						
						X[i] = simulateWolfCommunication.getMessageLostCount();

						totalMessagesLost = totalMessagesLost + simulateWolfCommunication.getMessageLostCount();
						totalMessagesSent = totalMessagesSent + simulateWolfCommunication.getMessageArrivedCount();
					}
					System.out.println((((double) totalMessagesLost / (double) totalMessagesSent)) + " : " + this.p2 / 2.0);
				} while ((((double) totalMessagesLost / (double) totalMessagesSent)) > this.p2 / 2.0);
				
				System.out.println("The least value of k required to satisfy the given condition is : " + k + "\n");
			} else {
				for (int i = 0; i < n; i++) {
					simulateWolfCommunication = new SimulateWolfCommunication();

					simulateWolfCommunication.initiateSystemState();
					// Generate the message arrivals in the interval [0,100] using the Poisson Distribution having lambda = 2 messages/hr
					// Thus the inter arrival times will follow an exponential distribution with lambda = 0.5 hrs between the arrival of 2 messages
					
					simulateWolfCommunication.generateMessageArrivals();
					simulateWolfCommunication.runSimulation();					
					
					X[i] = simulateWolfCommunication.getMessageLostCount();

					totalMessagesLost = totalMessagesLost + simulateWolfCommunication.getMessageLostCount();
					totalMessagesSent = totalMessagesSent + simulateWolfCommunication.getMessageArrivedCount();
				}
				
				System.out.println("Total Messages lost in " + n + " simulations are : " + totalMessagesLost);
				System.out.println("Total Messages sent in " + n + " simulations are : " + totalMessagesSent);

				averageMessagesLost = (double) totalMessagesLost / (double) n;
				probabilityAMessageIsLost = (double) totalMessagesLost / (double) totalMessagesSent;

				System.out.println("Average Messages Lost : " + averageMessagesLost);
				System.out.println("Probability that a sent message gets lost : " + probabilityAMessageIsLost);

				/*double variance = (totalMessagesLost + (averageMessagesLost * averageMessagesLost * n)) / (double) (n - 1);
				double standardError = Math.sqrt(variance / (double) n);*/
				
				Univariate x = new Univariate(X);
				double variance = x.variance();
				double standardError = x.SE();

				double qnormUpper = StatFunctions.qnorm((1 + delta) / 2, true);
				double qnormLower = StatFunctions.qnorm((1 + delta) / 2, false);

				double confidenceIntervalLeft = averageMessagesLost - qnormLower * standardError;
				double confidenceIntervalRight = averageMessagesLost + qnormLower * standardError;
				
				this.p2 = probabilityAMessageIsLost;

				System.out.println("Number of Samples : " + n);
				System.out.println("Sample Mean (Average Messages Lost) : " + averageMessagesLost);
				System.out.println("Sample Variance (sigma2) : " + variance);
				System.out.println("Standard Error : " + standardError);

				System.out.println("qnorm (upper=true) : " + qnormUpper);
				System.out.println("qnorm (upper=false) : " + qnormLower);

				System.out
						.println(delta + " confindence interval : " + confidenceIntervalLeft + " , " + confidenceIntervalRight);
			}			
		} finally  {
			this.totalMessagesLost = 0;
			this.totalMessagesSent = 0;
			this.averageMessagesLost = 0.0;
			this.probabilityAMessageIsLost = 0.0;			
			this.simulateWolfCommunication = null;
		}
	}

	/**
	 * Performs the IMC simulation using the variance reduction technique The
	 * Variance Reduction Technique used is the Method of Control Variables.
	 * 
	 * @param n
	 * @param delta
	 */
	public void performIMCSimultionUsingVarianceReduction(int n, double delta) {

		/**
		 * PREPROCESSING Step to calculate the mu and c for the control variable
		 * M M = the number of sent messages during the interval [0,100] Control
		 * Constant c = - (Cov(X,M) / Var(M)) where X = number of lost messages
		 * Clearly M and X are positively correlated.
		 */
		
		try {
			
			// PRE-PROCESSING STEP :: BEGINS
			simulateWolfCommunication = new SimulateWolfCommunication();

			double X[] = new double[Constants.CONTROL_VARIABLE_SAMPLES];
			double M[] = new double[Constants.CONTROL_VARIABLE_SAMPLES];

			for (int i = 0; i < Constants.CONTROL_VARIABLE_SAMPLES; i++) {

				simulateWolfCommunication.initiateSystemState();
				simulateWolfCommunication.generateMessageArrivals();
				simulateWolfCommunication.runSimulation();

				X[i] = simulateWolfCommunication.getMessageLostCount();
				M[i] = simulateWolfCommunication.getMessageArrivedCount();
			}

			Univariate x = new Univariate(X);
			Univariate m = new Univariate(M);

			double cov_x_m = StatFunctions.cov(x, m);
			double var_m = m.variance();

			// Calculate mu
			double mu = m.mean();

			// Calcuate the control constant c
			double c = (-1) * (cov_x_m / var_m);
			
			System.out.println("cov_x_m : " + cov_x_m);
			System.out.println("var_m : " + var_m);
			System.out.println("mu : " + mu);
			System.out.println("c : " + c);

			// PRE-PROCESSING STEP :: ENDS
			
			
			// Now the actual samples will be taken from the set Z.
			// Z[i] = X[i] + c * (M[i] - mu)

			double Z[] = new double[n];
			
			/*int totalMessagesLost = 0;
			int totalMessagesSent = 0;

			double averageMessagesLost = 0.0;

			double probabilityAMessageIsLost = 0.0;*/

			for (int i = 0; i < n; i++) {
				simulateWolfCommunication = new SimulateWolfCommunication();

				simulateWolfCommunication.initiateSystemState();
				simulateWolfCommunication.generateMessageArrivals();
				simulateWolfCommunication.runSimulation();

				Z[i] = simulateWolfCommunication.getMessageLostCount()
						+ c * (simulateWolfCommunication.getMessageArrivedCount() - mu);
				
				totalMessagesLost = totalMessagesLost + simulateWolfCommunication.getMessageLostCount();
				totalMessagesSent = totalMessagesSent + simulateWolfCommunication.getMessageArrivedCount();				
			}

			System.out.println("Total Messages lost in " + n + " simulations are : " + totalMessagesLost);
			System.out.println("Total Messages sent in " + n + " simulations are : " + totalMessagesSent);

			averageMessagesLost = (double) totalMessagesLost / (double) n;
			probabilityAMessageIsLost = (double) totalMessagesLost / (double) totalMessagesSent;

			System.out.println("Average Messages Lost : " + averageMessagesLost);
			System.out.println("Probability that a sent message gets lost : " + probabilityAMessageIsLost);
			
			Univariate z = new Univariate(Z);

			double variance = z.variance();
			double standardError = z.SE();

			double qnormUpper = StatFunctions.qnorm((1 + delta) / 2, true);
			double qnormLower = StatFunctions.qnorm((1 + delta) / 2, false);

			double confidenceIntervalLeft = (averageMessagesLost + qnormUpper) * standardError;
			double confidenceIntervalRight = (averageMessagesLost - qnormUpper) * standardError;

			// System.out.println("Control Constant c = " + c);
			// System.out.println("E[M] = " + mu);
			System.out.println("Number of Samples : " + n);
			System.out.println("Sample Mean (Average Messages Lost) : " + averageMessagesLost);
			System.out.println("Sample Variance (sigma2) : " + variance);
			System.out.println("Standard Error : " + standardError);

			System.out.println("qnorm (upper=true) : " + qnormUpper);
			System.out.println("qnorm (upper=false) : " + qnormLower);

			System.out
					.println(delta + " confindence interval : " + confidenceIntervalLeft + " , " + confidenceIntervalRight);
		} finally {
			
			// Clears up the state for this simulation
			
			this.totalMessagesLost = 0;
			this.totalMessagesSent = 0;

			this.averageMessagesLost = 0.0;

			this.probabilityAMessageIsLost = 0.0;
			
			this.simulateWolfCommunication = null;
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		PerformIMCSimulation imcSimulation = new PerformIMCSimulation();

		System.out.println("EXCERCISE 2");
		System.out.println(
				"Printing the statistics for IMC simulation without using any Variance Reduction Techniques \n");
		imcSimulation.performIMCSimulation(Constants.NO_OF_SAMPLES, Constants.DELTA_CI, false);

		System.out.println("\n\nEXCERCISE 3");
		System.out.println(
				"Printing the statistics for IMC simulation using the \"Method of Control Variables\" as the Variance Reduction Technique \n");
		imcSimulation.performIMCSimultionUsingVarianceReduction(Constants.NO_OF_SAMPLES, Constants.DELTA_CI);
		
		System.out.println("\n\nEXCERCISE 4");
		System.out.println(
				"Printing the strategic IMC simulation as per the strategy given in EXERCISE 4 \n");
		imcSimulation.performIMCSimulation(Constants.NO_OF_SAMPLES, Constants.DELTA_CI, true);
		
		
		
	}

}
