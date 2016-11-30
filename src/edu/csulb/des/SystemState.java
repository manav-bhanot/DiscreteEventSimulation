/**
 * 
 */
package edu.csulb.des;

/**
 * @author Manav
 * 
 * Class used to store the state of the system at a particular time T = t
 */
public class SystemState {

	// Stores whether the system is in a Good Weather State.
	// true value indicates that the system is currently operating in good weather conditions
	private boolean isGoodWeather;
	
	// Stores whether the system is in a Bad Weather State.
	// true value indicates that the system is currently operating in bad weather conditions
	private boolean isBadWeather;
	
	// Checks if Channel 1 is busy
	// true value indicates that channel 1 is busy
	private boolean isChannelOneBusy;
	
	// Checks if Channel 2 is busy
	// true value indicates that channel 2 is busy
	private boolean isChannelTwoBusy;
	
	// Checks if Channel 3 is busy
	// true value indicates that channel 3 is busy
	private boolean isChannelThreeBusy;
	
	// Stores the duration of the Good Weather in minutes
	private double goodWeatherDuration;
	
	// Stores the duration if the bad weather in minutes
	private double badWeatherDuration;
	
	// Stores the processing time which depends on whether the system is in good weather state or bad weather state
	private double processingTime;

	/**
	 * @return the isGoodWeather
	 */
	public boolean isGoodWeather() {
		return isGoodWeather;
	}

	/**
	 * @param isGoodWeather
	 *            the isGoodWeather to set
	 */
	public void setGoodWeather(boolean isGoodWeather) {
		this.isGoodWeather = isGoodWeather;
	}

	/**
	 * @return the isBadWeather
	 */
	public boolean isBadWeather() {
		return isBadWeather;
	}

	/**
	 * @param isBadWeather
	 *            the isBadWeather to set
	 */
	public void setBadWeather(boolean isBadWeather) {
		this.isBadWeather = isBadWeather;
	}

	/**
	 * @return the isChannelOneBusy
	 */
	public boolean isChannelOneBusy() {
		return isChannelOneBusy;
	}

	/**
	 * @param isChannelOneBusy
	 *            the isChannelOneBusy to set
	 */
	public void setChannelOneBusy(boolean isChannelOneBusy) {
		this.isChannelOneBusy = isChannelOneBusy;
	}

	/**
	 * @return the isChannelTwoBusy
	 */
	public boolean isChannelTwoBusy() {
		return isChannelTwoBusy;
	}

	/**
	 * @param isChannelTwoBusy
	 *            the isChannelTwoBusy to set
	 */
	public void setChannelTwoBusy(boolean isChannelTwoBusy) {
		this.isChannelTwoBusy = isChannelTwoBusy;
	}

	/**
	 * @return the isChannelThreeBusy
	 */
	public boolean isChannelThreeBusy() {
		return isChannelThreeBusy;
	}

	/**
	 * @param isChannelThreeBusy
	 *            the isChannelThreeBusy to set
	 */
	public void setChannelThreeBusy(boolean isChannelThreeBusy) {
		this.isChannelThreeBusy = isChannelThreeBusy;
	}

	/**
	 * @return the goodWeatherDuration
	 */
	public double getGoodWeatherDuration() {
		return goodWeatherDuration;
	}

	/**
	 * @param goodWeatherDuration
	 *            the goodWeatherDuration to set
	 */
	public void setGoodWeatherDuration(double goodWeatherDuration) {
		this.goodWeatherDuration = goodWeatherDuration;
	}

	/**
	 * @return the badWeatherDuration
	 */
	public double getBadWeatherDuration() {
		return badWeatherDuration;
	}

	/**
	 * @param badWeatherDuration
	 *            the badWeatherDuration to set
	 */
	public void setBadWeatherDuration(double badWeatherDuration) {
		this.badWeatherDuration = badWeatherDuration;
	}

	/**
	 * @return the processingTime
	 */
	public double getProcessingTime() {
		return processingTime;
	}

	/**
	 * @param processingTime
	 *            the processingTime to set
	 */
	public void setProcessingTime(double processingTime) {
		this.processingTime = processingTime;
	}

}
