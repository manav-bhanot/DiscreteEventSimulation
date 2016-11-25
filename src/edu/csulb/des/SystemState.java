/**
 * 
 */
package edu.csulb.des;

/**
 * @author Manav
 *
 */
public class SystemState {

	private boolean isGoodWeather;
	private boolean isBadWeather;
	private boolean isChannelOneBusy;
	private boolean isChannelTwoBusy;
	private boolean isChannelThreeBusy;
	private int goodWeatherDuration;
	private int badWeatherDuration;
	private int processingTime;

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
	public int getGoodWeatherDuration() {
		return goodWeatherDuration;
	}

	/**
	 * @param goodWeatherDuration
	 *            the goodWeatherDuration to set
	 */
	public void setGoodWeatherDuration(int goodWeatherDuration) {
		this.goodWeatherDuration = goodWeatherDuration;
	}

	/**
	 * @return the badWeatherDuration
	 */
	public int getBadWeatherDuration() {
		return badWeatherDuration;
	}

	/**
	 * @param badWeatherDuration
	 *            the badWeatherDuration to set
	 */
	public void setBadWeatherDuration(int badWeatherDuration) {
		this.badWeatherDuration = badWeatherDuration;
	}

	/**
	 * @return the processingTime
	 */
	public int getProcessingTime() {
		return processingTime;
	}

	/**
	 * @param processingTime
	 *            the processingTime to set
	 */
	public void setProcessingTime(int processingTime) {
		this.processingTime = processingTime;
	}

}
