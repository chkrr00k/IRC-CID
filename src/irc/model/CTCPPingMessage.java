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
public class CTCPPingMessage extends CTCPMessage {

	public CTCPPingMessage(/*String message,*/ User user, String channel) {
		super("", user, channel);
		
	}

	public CTCPPingMessage(String line) throws IllegalArgumentException {
		super.person = new Person();
		this.digester(line);
		super.time = LocalDateTime.now();
		
	}

	private void digester(String line) throws IllegalArgumentException{
		if(line.startsWith(":") && line.contains("" +(char)(0x01))){
			line = line.substring(1);
			this.person.setNickName(line.substring(0, line.indexOf("!")).trim());
			this.person.setRealName(line.substring(line.indexOf("!") + 1, line.indexOf("@")).trim());
			this.person.setHostName(line.substring(line.indexOf("@") + 1, line.indexOf(" ")).trim());
			try{
			this.channel = line.substring(line.indexOf(" PRIVMSG ") + 8, line.indexOf(" :")).trim().toLowerCase();
			}catch(Exception e){
				System.err.println(line);
			}
			this.message = line.substring(line.indexOf(":" + (char)(0x01) + "PING") + 6).trim();
		}
		else{
			throw new IllegalArgumentException("Unable to parse message " + line);
		}
	}
	
	@Override
	public String toString() {
		return "[" + time.format(DateTimeFormatter.ISO_TIME) + "] * " + this.person.getNickName().trim() + " " + message + "\n";
	}
	
	@Override
	public String apply(){
		return  "PRIVMSG " + this.channel + " :" + (char)(0x01) + "PING" + (char)(0x01) +"\r\n";
		
	}
	
	public String answer(){
		return "NOTICE " + this.person.getNickName() + " :" + (char)(0x01) + "PING " + this.message + (char)(0x01) +"\r\n";
	}
}
