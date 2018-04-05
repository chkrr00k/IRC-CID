/**
 * 
 */
package irc.model;

/**
 * @author chkrr00k
 *
 */
public class ChannelModality extends Modality {

	
	public ChannelModality(String mode, String channel) {
		this.mode = mode;
		this.channel = channel;
	}


	@Override
	public String apply() {
		return "MODE " + this.channel + " " + this.mode + "\r\n";
	}

}
