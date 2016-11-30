/**
 * 
 */
package edu.csulb.des;

/**
 * @author Manav
 *
 */
public class Event implements Comparable<Event>{

	/**
	 *  Stores the Event Code. The various event codes are :
	 *  MA : MessageArrived
	 *  MP : MessageProcessed
	 *  ML : MessageLost
	 *  GW : GoodWeather
	 *  BW : BadWeather
	 */	
	private String eventcode;
	
	// Store the time at which this event is scheduled to execute/occur/happen
	private Double time;

	/**
	 * If eventCode = MA, id = msgId 
	 * If eventCode = MP, id = channelId which just finished processing the message
	 */
	private Integer id;

	/**
	 * 
	 */
	public Event() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param eventcode
	 * @param time
	 * @param messageId
	 */
	public Event(String eventcode, Double time, Integer id) {
		super();
		this.eventcode = eventcode;
		this.time = time;
		this.id = id;
	}

	/**
	 * @return the eventcode
	 */
	public String getEventcode() {
		return eventcode;
	}

	/**
	 * @param eventcode
	 *            the eventcode to set
	 */
	public void setEventcode(String eventcode) {
		this.eventcode = eventcode;
	}

	/**
	 * @return the time
	 */
	public Double getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(Double time) {
		this.time = time;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public int compareTo(Event o) {
		return this.getTime().compareTo(o.getTime());
	}

}
