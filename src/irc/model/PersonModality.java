/**
 * 
 */
package irc.model;

/**
 * @author chkrr00k
 *
 */
public class PersonModality extends Modality{

	private String person = "";
	private String host = "";

	public PersonModality(String mode, String channel, String person) {
		this.channel = channel;
		this.mode = mode;
		if(mode.contains("b")){
			this.host = person;
		}else{
			this.person = person;
		}
	}

	public String getChannel() {
		return channel;
	}
	public String getMode() {
		return mode;
	}/*
	public Person getPerson() {
		return person;
	}*/

	public String apply() {
		if(person == ""){
			return "MODE " + this.channel + " " + this.mode + " " + this.host + "\r\n";
		}else{
			return "MODE " + this.channel + " " + this.mode + " " + this.person + "\r\n";
		}
	}
}
