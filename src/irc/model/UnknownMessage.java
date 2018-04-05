/**
 * 
 */
package irc.model;

import java.time.LocalDateTime;

/**
 * @author chkrr00k
 *
 */
public class UnknownMessage extends ServerAnswerMessage {

	/**
	 * 
	 */
	public UnknownMessage() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param nickName
	 * @param message
	 * @param time
	 */
	public UnknownMessage(String nickName, String message, LocalDateTime time) {
		super(nickName, message, time);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param number
	 * @param nickName
	 * @param message
	 */
	public UnknownMessage(int number, String nickName, String message) {
		super(number, nickName, message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param nickName
	 * @param message
	 */
	public UnknownMessage(String nickName, String message) {
		super(nickName, message);
		// TODO Auto-generated constructor stub
	}

}
