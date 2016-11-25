/**
 * 
 */
package edu.csulb.des;

import java.util.PriorityQueue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;

/**
 * @author Manav
 *
 */
public class SimulateWolfCommunication {

	// A FEL (Future Event List) that stores the Future Events triggered by the
	// current event being proessed
	private PriorityQueue<Event> futureEventList;

	// A System state saving the modified state of the system after a event is
	// processed.
	private SystemState systemState;

	// Counts the total number of messages being lost
	private int messageLostCount;
	
	// Counts the total number of messages arriving
	private int messageArrivedCount;

	// A Normal Distribution used to sample the duration of Good Weather
	private final NormalDistribution sampleGoodWeatherDuration;

	// A Normal Distribution used to sample the duration of Bad Weather
	private final NormalDistribution sampleBadWeatherDuration;
	
	// A Uniform Distribution to sample values for the processing time of the messages
	private final UniformRealDistribution sampleProcessingTimeFromUnifDist;
	
	// A Uniform Distribution to sample values for the processing time of the messages
	private final UniformRealDistribution sampleMessageArrivalTimeFromUnifDist;

	public SimulateWolfCommunication() {
		this.futureEventList = new PriorityQueue<Event>();
		this.sampleGoodWeatherDuration = new NormalDistribution(Constants.GOOD_WEATHER_MEAN, Constants.GOOD_WEATHER_SD);
		this.sampleBadWeatherDuration = new NormalDistribution(Constants.BAD_WEATHER_MEAN, Constants.BAD_WEATHER_SD);
		this.sampleProcessingTimeFromUnifDist = new UniformRealDistribution(Constants.PT_UNIF_LOWER, Constants.PT_UNIF_UPPER);
		this.sampleMessageArrivalTimeFromUnifDist = new UniformRealDistribution(Constants.MA_UNIF_LOWER, Constants.MA_UNIF_UPPER);
	}

	/**
	 * Initializes the state of the system at the beginning of the simulation At
	 * T = 0, Good Weather duration starts and it triggers a Bad Weather event
	 * which happens at t = duration of good weather event Also since we know
	 * the number of message arrivals follow a Poisson distribution with lambda
	 * = 2 which means the inter-arrival time between messages follows an
	 * exponential distribution with lambda = 0.5 i.e. a new message is arriving
	 * after every 30 minutes.
	 */
	private void initiateSystemState() {
		
		systemState = new SystemState();
		systemState.setGoodWeather(true);
		systemState.setBadWeather(false);
		systemState.setChannelOneBusy(false);
		systemState.setChannelTwoBusy(false);
		systemState.setChannelThreeBusy(false);

		// Initiate the Future Event List with all the Message Arrival Events
		
		/**
		 * 1. Initiate GoodWeather 2. Creates a BadWeather Event which is
		 * scheduled to occur at time T = duration of good weather event 3. Add
		 * that BadWeather Event in the priority queue
		 */
		systemState.setGoodWeatherDuration((int) sampleGoodWeatherDuration.sample());

		Event badWeatherEvent = new Event(EventCodeEnum.EventCode.getBadWeatherEventCode(),
				systemState.getGoodWeatherDuration(), null);
		futureEventList.add(badWeatherEvent);

		int messageID = 1;
		
		double U = this.sampleMessageArrivalTimeFromUnifDist.sample();
		int nextMessageArrivalTime = (int) ((-1) * (Math.log(1 - U) / Constants.EXP_LAMBDA) * 60);
		//int nextMessageArrivalTime = 0;
		
		while (nextMessageArrivalTime <= Constants.END_TIME * 60) {
			
			Event messageArrivedEvent = new Event(EventCodeEnum.EventCode.getMessageArrivedEventCode(), nextMessageArrivalTime, messageID);
			futureEventList.add(messageArrivedEvent);
			
			U = this.sampleMessageArrivalTimeFromUnifDist.sample();
			nextMessageArrivalTime = nextMessageArrivalTime + (int) ((-1) * (Math.log(1 - U) / Constants.EXP_LAMBDA) * 60);
			messageID++;
		}
	}

	private void runSimulation() {

		while (!futureEventList.isEmpty()) {

			Event event = futureEventList.poll();

			System.out.println("<" + event.getEventcode() + ", " + event.getTime() + ", " + event.getId() + ">");
			
			// Break the simulation when the next GW event is scheduled to
			// happen after the 100th hour
			
			/*if (event.getTime() > Constants.END_TIME * 60) {
				break;
			}*/

			if (event.getEventcode().equalsIgnoreCase(EventCodeEnum.EventCode.getMessageArrivedEventCode())) {

				// Sample a value from Uniform Distribution ~ U(0,1)
				double messageProcessingTime = sampleProcessingTimeFromUnifDist.sample();
				//System.out.println("Message Processing Time sampled : " + messageProcessingTime);

				// Check if the current weather is a bad weather condition
				if (systemState.isBadWeather()) {
					messageProcessingTime = Math.cbrt(messageProcessingTime);
				}

				// Convert the Message Processing Time Into minutes
				int messageProcessedTime = (int) (messageProcessingTime * 60);

				// Check if any of the channels is free
				int channelAssigned = 0;
				if (systemState.isChannelOneBusy() && systemState.isChannelTwoBusy()
						&& systemState.isChannelThreeBusy()) {
					this.setMessageLostCount(this.getMessageLostCount() + 1);
					
					System.out.println("Message with id <" + event.getId() + "> is lost as no channel is available to process this.");
					
					continue;
				} else {
					if (!systemState.isChannelOneBusy()) {
						channelAssigned = Constants.CHANNEL_ONE;
						systemState.setChannelOneBusy(true);
					} else if (!systemState.isChannelTwoBusy()) {
						channelAssigned = Constants.CHANNEL_TWO;
						systemState.setChannelTwoBusy(true);
					} else if (!systemState.isChannelThreeBusy()) {
						channelAssigned = Constants.CHANNEL_THREE;
						systemState.setChannelThreeBusy(true);
					}
				}

				// System.out.println("Message Processed Time : " + messageProcessedTime);
				// Generate a Message Processed Event and add it into the FEL
				Event messageProcessedEvent = new Event(EventCodeEnum.EventCode.getMessageProcessedEventCode(),
						messageProcessedTime + event.getTime(), channelAssigned);
				futureEventList.add(messageProcessedEvent);
			}

			// Process the Message Processed Event
			if (event.getEventcode().equalsIgnoreCase(EventCodeEnum.EventCode.getMessageProcessedEventCode())) {

				// Frees up the particular channel
				if (event.getId() == Constants.CHANNEL_ONE) {
					systemState.setChannelOneBusy(false);
				} else if (event.getId() == Constants.CHANNEL_TWO) {
					systemState.setChannelTwoBusy(false);
				} else if (event.getId() == Constants.CHANNEL_THREE) {
					systemState.setChannelThreeBusy(false);
				}
			}

			// Process the Bad Weather Event
			if (event.getEventcode().equalsIgnoreCase(EventCodeEnum.EventCode.getBadWeatherEventCode())) {

				systemState.setBadWeatherDuration((int) sampleBadWeatherDuration.sample());
				
				Event goodWeatherEvent = new Event(EventCodeEnum.EventCode.getGoodWeatherEventCode(),
						systemState.getBadWeatherDuration() + event.getTime(), null);
				
				
				if (goodWeatherEvent.getTime() > Constants.END_TIME * 60) {
					continue;
				}
				
				futureEventList.add(goodWeatherEvent);
			}

			// Process the Good Weather Event
			if (event.getEventcode().equalsIgnoreCase(EventCodeEnum.EventCode.getGoodWeatherEventCode())) {

				systemState.setGoodWeatherDuration((int) sampleBadWeatherDuration.sample());

				Event badWeatherEvent = new Event(EventCodeEnum.EventCode.getBadWeatherEventCode(),
						systemState.getGoodWeatherDuration() + event.getTime(), null);
				
				// Break the simulation when the next GW event is scheduled to
				// happen after the 100th hour
				if (badWeatherEvent.getTime() > Constants.END_TIME * 60) {
					continue;
				}
				futureEventList.add(badWeatherEvent);
			}
		}
		
		//return this;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		SimulateWolfCommunication simulateWolfCommunication = new SimulateWolfCommunication();

		// Initiates the SystemState
		simulateWolfCommunication.initiateSystemState();

		simulateWolfCommunication.runSimulation();

	}

	/**
	 * @return the messageLostCount
	 */
	public int getMessageLostCount() {
		return messageLostCount;
	}

	/**
	 * @param messageLostCount
	 *            the messageLostCount to set
	 */
	public void setMessageLostCount(int messageLostCount) {
		this.messageLostCount = messageLostCount;
	}

	/**
	 * @return the messageArrivedCount
	 */
	public int getMessageArrivedCount() {
		return messageArrivedCount;
	}

	/**
	 * @param messageArrivedCount the messageArrivedCount to set
	 */
	public void setMessageArrivedCount(int messageArrivedCount) {
		this.messageArrivedCount = messageArrivedCount;
	}
}
