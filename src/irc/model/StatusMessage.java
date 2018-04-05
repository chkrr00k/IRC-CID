/**
 * 
 */
package irc.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author chkrr00k
 *
 */
public class StatusMessage extends Comunication {

	// some "global" constants
	public static final String QUIT = " has quit";
	public static final String PART = " has left";
	public static final String JOIN = " has joined";
	public static final String NICK = " has changed nick to";
	public static final String MODE = " has been changed modes to";
	public static final String KICK = " has been kicked by ";
	public static final String BANN = " can't post here";
	public static final String CHMO = "channel has been changed mode to";
	
	// a useful constant (never used tbh)
	private static final String EMPTY = "";
	
	
	//let the class begin
	private String event;
	
	public StatusMessage(String nickName, String event, String message, LocalDateTime time) {
		super(nickName, message, time);
		this.event = event;
	}
	/**
	 * @param nickName
	 * @param message
	 */
	public StatusMessage(String nickName, String event, String message) {
		super(nickName, message);
		this.event = event;
	}
	/**
	 * @param nickName
	 * @param part2
	 */
	public StatusMessage(String nickName, String event) {
		super(nickName, StatusMessage.EMPTY);
		this.event = event;
	}
	@Override
	public String toString() {
		if(super.message.isEmpty()){
			return "[" + time.format(DateTimeFormatter.ISO_TIME) + "] " + this.person.getNickName() + this.event + "\n";
		}
		if(this.event.equals(StatusMessage.NICK) || this.event.equals(StatusMessage.MODE)){
			return "[" + time.format(DateTimeFormatter.ISO_TIME) + "] " + this.person.getNickName() + this.event + " " + message + "\n";
		}
		if(this.event.equals(StatusMessage.KICK)){
			return "[" + time.format(DateTimeFormatter.ISO_TIME) + "] " + this.person.getNickName() + this.event + " " + message + "\n";
		}
		if(this.event.equals(StatusMessage.CHMO)){
			return "[" + time.format(DateTimeFormatter.ISO_TIME) + "] " + this.event + " " + message + "\n";
		}
		return "[" + time.format(DateTimeFormatter.ISO_TIME) + "] " + this.person.getNickName() + this.event + " [" + message + "]\n";
	}
	/* (non-Javadoc)
	 * @see irc.model.Comunication#send()
	 */
	public String apply() {
		return "";
	}

	
	
}
