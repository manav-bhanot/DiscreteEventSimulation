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
	// current event being processed
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

	// A Uniform Distribution to sample values for the processing time of the
	// messages
	private final UniformRealDistribution sampleProcessingTimeFromUnifDist;

	// A Uniform Distribution to sample values for the processing time of the
	// messages
	private final UniformRealDistribution sampleMessageArrivalTimeFromUnifDist;

	/**
	 * Initializes the member variables. 
	 * Basically initializes all the data distributions from which the values are to be sampled
	 */
	public SimulateWolfCommunication() {
		this.futureEventList = new PriorityQueue<Event>();
		this.sampleGoodWeatherDuration = new NormalDistribution(Constants.GOOD_WEATHER_MEAN, Constants.GOOD_WEATHER_SD);
		this.sampleBadWeatherDuration = new NormalDistribution(Constants.BAD_WEATHER_MEAN, Constants.BAD_WEATHER_SD);
		this.sampleProcessingTimeFromUnifDist = new UniformRealDistribution(Constants.PT_UNIF_LOWER,
				Constants.PT_UNIF_UPPER);
		this.sampleMessageArrivalTimeFromUnifDist = new UniformRealDistribution(Constants.MA_UNIF_LOWER,
				Constants.MA_UNIF_UPPER);
	}

	/**
	 * Initializes the state of the system at the beginning of the simulation 
	 * At T = 0, Good Weather duration starts and it triggers a Bad Weather event
	 * which happens at t = (duration of good weather event). 
	 * Also since we know the number of message arrivals follow a Poisson distribution with lambda = 2
	 * which means the inter-arrival time between messages follows an exponential distribution with lambda = 0.5 
	 * i.e. a new message is arriving after every 30 minutes.
	 */
	public void initiateSystemState() {

		// Initialize an object of the system state
		systemState = new SystemState();
		
		// Set the system to be operating in Good Weather conditions at the beginning of time
		systemState.setGoodWeather(true);
		
		// Sets the bad weather condition to be false in the beginning of time
		systemState.setBadWeather(false);
		
		// Sets the channel 1 to be free in the beginning of time i.e. at T=0
		systemState.setChannelOneBusy(false);
		
		// Sets the channel 2 to be free in the beginning of time i.e. at T=0
		systemState.setChannelTwoBusy(false);
		
		// Sets the channel 3 to be free in the beginning of time i.e. at T=0
		systemState.setChannelThreeBusy(false);

		// Initiate the Future Event List with all the Message Arrival Events

		/**
		 * 1. Initiate GoodWeather 
		 * 2. Creates a BadWeather Event which is scheduled to occur at time T = (duration of good weather event)
		 * 3. Add that BadWeather Event in the priority queue
		 */		
		systemState.setGoodWeatherDuration(sampleGoodWeatherDuration.sample());

		Event badWeatherEvent = new Event(EventCodeEnum.EventCode.getBadWeatherEventCode(),
				systemState.getGoodWeatherDuration(), null);
		futureEventList.add(badWeatherEvent);
	}

	/**
	 * Generates all the message arrivals happening in the time interval [0,200] and put them in FEL
	 * Also generates 'k' copies of the messages sent at or after T = 100 and put those copies in the FEL
	 * 
	 * @param k
	 */
	public void generateMessageArrivals(int k) {

		// Message ID to keep track of the messages
		int messageID = 0;	
		
		// Sample a value from U(0,1) and assign it to U.
		double U = this.sampleMessageArrivalTimeFromUnifDist.sample();
		
		// Use inverse sampling technique to sample values from an Exponential Distribution using lambda = 2
		// and convert the value sampled, which is in hours into minutes
		double nextMessageArrivalTime = ((-1) * (Math.log(1 - U) / Constants.EXP_LAMBDA) * 60);

		// Run the loop till the next message arrival time is <= 12000 minutes
		while (nextMessageArrivalTime <= Constants.END_TIME_200 * 60) {
			
			// Generate the message arrival events and add it to the FEL
			Event messageArrivedEvent = new Event(EventCodeEnum.EventCode.getMessageArrivedEventCode(),
					nextMessageArrivalTime, messageID);
			futureEventList.add(messageArrivedEvent);

			// Increments the total message sent count by 1
			// We should not count this as the statistics for the messages sent during the interval [0,100] should not be counted
			this.setMessageArrivedCount(this.messageArrivedCount + 1);

			//  Generates the multiple copies of the messages arriving after T = 6000 min
			if (nextMessageArrivalTime >= Constants.END_TIME_100 * 60) {
				for (int i = 1; i <= k; i++) {
					
					// Creates a copy of the original message with the same message id but arriving after 60 mins
					messageArrivedEvent = new Event(EventCodeEnum.EventCode.getMessageArrivedEventCode(),
							nextMessageArrivalTime + (k * 60), messageID);
					
					// Add the copy of the message to the FEL
					futureEventList.add(messageArrivedEvent);

					// increments the total messages sent
					this.setMessageArrivedCount(this.messageArrivedCount + 1);
				}
			}

			U = this.sampleMessageArrivalTimeFromUnifDist.sample();
			nextMessageArrivalTime = nextMessageArrivalTime
					+ ((-1) * (Math.log(1 - U) / Constants.EXP_LAMBDA) * 60);
			messageID++;
		}
	}

	/**
	 * Generates all the message arrivals happening in the time interval [0,100]
	 */
	public void generateMessageArrivals() {

		// Message ID to keep track of the messages
		int messageID = 0;

		// Sample a value from U(0,1) and assign it to U.
		double U = this.sampleMessageArrivalTimeFromUnifDist.sample();
		
		// Use inverse sampling technique to sample values from an Exponential Distribution using lambda = 2
		// and convert the value sampled, which is in hours into minutes
		double nextMessageArrivalTime = ((-1) * (Math.log(1 - U) / Constants.EXP_LAMBDA) * 60);

		// Run the loop till the next message arrival time is <= 6000 minutes
		while (nextMessageArrivalTime <= Constants.END_TIME_100 * 60) {

			// Generate the message arrival events and add it to the FEL
			Event messageArrivedEvent = new Event(EventCodeEnum.EventCode.getMessageArrivedEventCode(),
					nextMessageArrivalTime, messageID);
			futureEventList.add(messageArrivedEvent);

			// Counts the total number of messages arriving at Wolf Communications
			// this.setMessageArrivedCount(this.messageArrivedCount + 1);

			// Use inverse sampling technique to sample values from an Exponential Distribution using lambda = 2
			// and convert the value sampled, which is in hours into minutes
			U = this.sampleMessageArrivalTimeFromUnifDist.sample();
			nextMessageArrivalTime = nextMessageArrivalTime
					+ ((-1) * (Math.log(1 - U) / Constants.EXP_LAMBDA) * 60);
			
			// Increments the messsageId for the next message
			messageID++;
		}
	}

	public void runSimulation() {
		
		while (!futureEventList.isEmpty()) {

			// Extracts the first event from the FEL Priority Queue
			Event event = futureEventList.poll();

			// Processes the MessageArrived Event (EventCode : MA)
			if (event.getEventcode().equalsIgnoreCase(EventCodeEnum.EventCode.getMessageArrivedEventCode())) {

				// Increments the total messages arrived count by 1
				this.setMessageArrivedCount(this.messageArrivedCount + 1);
				
				// Check if any of the channels is free
				int channelAssigned = 0;
				if (systemState.isChannelOneBusy() && systemState.isChannelTwoBusy()
						&& systemState.isChannelThreeBusy()) {
					
					this.setMessageLostCount(this.messageLostCount + 1);
					// System.out.println("Message with id <" + event.getId() + "> is lost as no channel is available to process this.");
				} else {
					
					// Sample a value from Uniform Distribution ~ U(0,1)
					// The sampled value if the message processing time in hours
					double messageProcessingTime = sampleProcessingTimeFromUnifDist.sample();
					// System.out.println("Message Processing Time sampled : " + messageProcessingTime);

					// Check if the current weather is a bad weather condition. If not then it is in good weather condition
					if (systemState.isBadWeather()) {
						
						// If the system is in bad weather condition then use inverse sampling and take cube root of the U value sampled
						// to get the message processing time of the message during bad weather conditions
						messageProcessingTime = Math.cbrt(messageProcessingTime);
					}

					// Convert the Message Processing Time Into minutes
					double messageProcessedTime = messageProcessingTime * 60;
					
					// Assigns the message to one of the free channels
					if (!systemState.isChannelOneBusy()) {
						
						channelAssigned = Constants.CHANNEL_ONE;
						
						// Sets the channel state to busy
						systemState.setChannelOneBusy(true);
					} else if (!systemState.isChannelTwoBusy()) {
						
						channelAssigned = Constants.CHANNEL_TWO;
						
						// Sets the channel state to busy
						systemState.setChannelTwoBusy(true);
					} else if (!systemState.isChannelThreeBusy()) {
						
						channelAssigned = Constants.CHANNEL_THREE;
						
						// Sets the channel state to busy
						systemState.setChannelThreeBusy(true);
					}
					
					// System.out.println("Message Processed Time : " + messageProcessedTime);
					
					// Generate a Message Processed Event and add it into the FEL
					Event messageProcessedEvent = new Event(EventCodeEnum.EventCode.getMessageProcessedEventCode(),
							messageProcessedTime + event.getTime(), channelAssigned);
					futureEventList.add(messageProcessedEvent);
				}
			}

			// Process the Message Processed Event (Event Code : MP)
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

				// Sample the duration of the Bad Weather using the Normal Distribution having mean = 60 mins and 20 minutes
				systemState.setBadWeatherDuration(sampleBadWeatherDuration.sample());

				// Generates a Good Weather Event.
				// The time at which the good weather begins = duration of bad weather + time at which bad weather begins
				Event goodWeatherEvent = new Event(EventCodeEnum.EventCode.getGoodWeatherEventCode(),
						systemState.getBadWeatherDuration() + event.getTime(), null);

				// Do not add the Bad Weather event scheduled to happen at T > 6000 min
				if (goodWeatherEvent.getTime() > Constants.END_TIME_100 * 60) {
					continue;
				}
				
				// Adds the Good Weather Event generated into the FEL queue
				futureEventList.add(goodWeatherEvent);
			}

			// Process the Good Weather Event
			if (event.getEventcode().equalsIgnoreCase(EventCodeEnum.EventCode.getGoodWeatherEventCode())) {

				// Sample the duration of the Good Weather using the Normal Distribution having mean = 90 mins and 10 minutes
				systemState.setGoodWeatherDuration(sampleGoodWeatherDuration.sample());

				// Generates a Bad Weather Event.
				// The time at which the bad weather begins = duration of good weather + time at which good weather begins
				Event badWeatherEvent = new Event(EventCodeEnum.EventCode.getBadWeatherEventCode(),
						systemState.getGoodWeatherDuration() + event.getTime(), null);
				
				// Do not add the Good Weather event scheduled to happen at T > 6000 min
				if (badWeatherEvent.getTime() > Constants.END_TIME_100 * 60) {
					continue;
				}
				
				// Adds the Bad Weather Event generated into the FEL queue
				futureEventList.add(badWeatherEvent);
			}
		}
	}
	
	/**
	 * Runs the simulation for strategy applied by Alice in Excercise : 4
	 * 
	 * @param k
	 */
	public void runSimulation(int k) {
		
		// Data structures to keep track of the copies of the messages		
		// isMessageProessed[i] == true implies that the message with messageID has been processed
		boolean[] isMessageProcessed = new boolean[this.getMessageArrivedCount()];
		
		// countLostCopies[i] tells how many message copies with messageID = i has been lost
		// if countLostCopies[i] = k + 1 implies all the copies of the message with messageID = i (including the original) have been lost
		int[] countLostCopies = new int[this.getMessageArrivedCount()]; 
		
		// Resets the total messages sent counter as we do not need to keep track of the messages sent in the duration [0,100]
		this.setMessageArrivedCount(0);

		while (!futureEventList.isEmpty()) {

			// Extracts the first event from the Priority Queue
			Event event = futureEventList.poll();

			if (event.getEventcode().equalsIgnoreCase(EventCodeEnum.EventCode.getMessageArrivedEventCode())) {
				
				// Counts the number of sent messages only for those sent by Alice at time T = 6000 min onwards
				if (event.getTime() >= Constants.END_TIME_100 * 60) {
					this.setMessageArrivedCount(this.messageArrivedCount + 1);
				}				

				// Check if any of the channels is free
				int channelAssigned = 0;
				if (systemState.isChannelOneBusy() && systemState.isChannelTwoBusy()
						&& systemState.isChannelThreeBusy()) {
					
					// If this message arrival happened before the 100th hour then there are no multiple copies of this message waiting to be received
					// Hence this message can be marked as lost
					if (event.getTime() < Constants.END_TIME_100) {
						// this.setMessageLostCount(this.messageLostCount + 1);
					} else {
						if (isMessageProcessed[event.getId()]) {
							// This message has already been processed. Thus this is the redundant copy and hence has to be discarded
							continue;
						} else {
							countLostCopies[event.getId()] += 1;
							if (countLostCopies[event.getId()] == k+1) {
								this.setMessageLostCount(this.messageLostCount + 1);
							}
						}
					}
					// System.out.println("Message with id <" + event.getId() +"> is lost as no channel is available to process this.");
					
				} else {

					// Setting the flag that this message has been processed
					isMessageProcessed[event.getId()] = true;

					// Sample a value from Uniform Distribution ~ U(0,1)
					double messageProcessingTime = sampleProcessingTimeFromUnifDist.sample();
					
					// System.out.println("Message Processing Time sampled : " + messageProcessingTime);

					// Check if the current weather is a bad weather condition
					if (systemState.isBadWeather()) {
						messageProcessingTime = Math.cbrt(messageProcessingTime);
					}

					// Convert the Message Processing Time Into minutes and then assign a channel to it for processing
					double messageProcessedTime = (messageProcessingTime * 60);
					
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

					// System.out.println("Message Processed Time : " + messageProcessedTime);					
					// Generate a Message Processed Event and add it into the FEL
					
					Event messageProcessedEvent = new Event(EventCodeEnum.EventCode.getMessageProcessedEventCode(),
							messageProcessedTime + event.getTime(), channelAssigned);
					futureEventList.add(messageProcessedEvent);
				}
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

				systemState.setBadWeatherDuration(sampleBadWeatherDuration.sample());

				Event goodWeatherEvent = new Event(EventCodeEnum.EventCode.getGoodWeatherEventCode(),
						systemState.getBadWeatherDuration() + event.getTime(), null);

				if (goodWeatherEvent.getTime() > Constants.END_TIME_100 * 60) {
					continue;
				}

				futureEventList.add(goodWeatherEvent);
			}

			// Process the Good Weather Event
			if (event.getEventcode().equalsIgnoreCase(EventCodeEnum.EventCode.getGoodWeatherEventCode())) {

				systemState.setGoodWeatherDuration(sampleGoodWeatherDuration.sample());

				Event badWeatherEvent = new Event(EventCodeEnum.EventCode.getBadWeatherEventCode(),
						systemState.getGoodWeatherDuration() + event.getTime(), null);

				// Break the simulation when the next GW event is scheduled to
				// happen after the 100th hour
				if (badWeatherEvent.getTime() > Constants.END_TIME_100 * 60) {
					continue;
				}
				futureEventList.add(badWeatherEvent);
			}
		}

		// return this;
	}

	/**
	 * @param args
	 */
	/*public static void main(String[] args) {

		SimulateWolfCommunication simulateWolfCommunication = new SimulateWolfCommunication();

		// Initiates the SystemState
		simulateWolfCommunication.initiateSystemState();

		simulateWolfCommunication.runSimulation();

	}*/

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
	 * @param messageArrivedCount
	 *            the messageArrivedCount to set
	 */
	public void setMessageArrivedCount(int messageArrivedCount) {
		this.messageArrivedCount = messageArrivedCount;
	}
}
