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
public class MotdMessage extends ServerAnswerMessage {

	public MotdMessage() {
		super();
	}
	public MotdMessage(String nickName, String message, LocalDateTime time) {
		super(nickName, message, time);
	}
	
	public MotdMessage(int number, String nickName, String message) {
		super(number, nickName, message);
	}
	public MotdMessage(String nickName, String message) {
		super(nickName, message);
	}
	
	public boolean isLast(){
		return this.number == 376;
	}
	public boolean isFirst(){
		return this.number == 375;
	}
	@Override
	public String toString() {
		return "[" + time.format(DateTimeFormatter.ISO_TIME) + "] " + this.message + "\n";
	}
}
