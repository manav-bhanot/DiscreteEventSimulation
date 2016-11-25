/**
 * 
 */
package edu.csulb.des;

/**
 * @author Manav
 *
 */
public enum EventCodeEnum {

	EventCode("MA", "MP", "BW", "GW", "ML");

	private String messageArrivedEventCode;
	private String messageProcessedEventCode;
	private String badWeatherEventCode;
	private String goodWeatherEventCode;
	private String messageLostEventCode;

	private EventCodeEnum() {
		// TODO Auto-generated constructor stub
	}

	private EventCodeEnum(String messageArrivedEventCode, String messageProcessedEventCode, String badWeatherEventCode,
			String goodWeatherEventCode, String messageLostEventCode) {
		this.messageArrivedEventCode = messageArrivedEventCode;
		this.messageProcessedEventCode = messageProcessedEventCode;
		this.badWeatherEventCode = badWeatherEventCode;
		this.goodWeatherEventCode = goodWeatherEventCode;
		this.messageLostEventCode = messageLostEventCode;
	}

	/**
	 * @return the messageArrivedEventCode
	 */
	public String getMessageArrivedEventCode() {
		return messageArrivedEventCode;
	}

	/**
	 * @param messageArrivedEventCode
	 *            the messageArrivedEventCode to set
	 */
	public void setMessageArrivedEventCode(String messageArrivedEventCode) {
		this.messageArrivedEventCode = messageArrivedEventCode;
	}

	/**
	 * @return the messageProcessedEventCode
	 */
	public String getMessageProcessedEventCode() {
		return messageProcessedEventCode;
	}

	/**
	 * @param messageProcessedEventCode
	 *            the messageProcessedEventCode to set
	 */
	public void setMessageProcessedEventCode(String messageProcessedEventCode) {
		this.messageProcessedEventCode = messageProcessedEventCode;
	}

	/**
	 * @return the badWeatherEventCode
	 */
	public String getBadWeatherEventCode() {
		return badWeatherEventCode;
	}

	/**
	 * @param badWeatherEventCode
	 *            the badWeatherEventCode to set
	 */
	public void setBadWeatherEventCode(String badWeatherEventCode) {
		this.badWeatherEventCode = badWeatherEventCode;
	}

	/**
	 * @return the goodWeatherEventCode
	 */
	public String getGoodWeatherEventCode() {
		return goodWeatherEventCode;
	}

	/**
	 * @param goodWeatherEventCode
	 *            the goodWeatherEventCode to set
	 */
	public void setGoodWeatherEventCode(String goodWeatherEventCode) {
		this.goodWeatherEventCode = goodWeatherEventCode;
	}

	/**
	 * @return the messageLostEventCode
	 */
	public String getMessageLostEventCode() {
		return messageLostEventCode;
	}

	/**
	 * @param messageLostEventCode
	 *            the messageLostEventCode to set
	 */
	public void setMessageLostEventCode(String messageLostEventCode) {
		this.messageLostEventCode = messageLostEventCode;
	}

}
