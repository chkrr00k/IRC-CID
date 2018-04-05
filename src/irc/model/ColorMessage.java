/**
 * 
 */
package irc.model;

/**
 * @author chkrr00k
 *
 */
public class ColorMessage extends Message {

	/**
	 * @param line
	 * @throws IllegalArgumentException
	 */
	public ColorMessage(String line) throws IllegalArgumentException {
		super(line);
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param message
	 * @param user
	 * @param channel
	 */
	public ColorMessage(String message, User user, String channel) {
		super(message, user, channel);
		// TODO Auto-generated constructor stub
	}

}
