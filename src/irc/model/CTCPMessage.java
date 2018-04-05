/**
 * 
 */
package irc.model;

/**
 * @author chkrr00k
 *
 */
public abstract class CTCPMessage extends Message {

	/**
	 * @param message
	 * @param user
	 * @param channel
	 */
	public CTCPMessage(String message, User user, String channel) {
		super(message, user, channel);
	}
	public CTCPMessage(String line) throws IllegalArgumentException{
		
	}
	public CTCPMessage() throws IllegalArgumentException{
		
	}
	
	public abstract String apply();
	
	public abstract String answer();
	
}
