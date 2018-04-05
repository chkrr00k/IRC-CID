/**
 * 
 */
package irc.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;

/**
 * @author chkrr00k
 *
 */
public class WhoisMessage extends ServerAnswerMessage {

	/**
	 * 
	 */
	public WhoisMessage() {
	}
	/**
	 * @param nickName
	 * @param message
	 * @param time
	 */
	public WhoisMessage(String nickName, String message, LocalDateTime time) {
		super(nickName, message.trim(), time);
	}
	/**
	 * @param number
	 * @param nickName
	 * @param message
	 */
	public WhoisMessage(int number, String nickName, String message) {
		super(number, nickName.trim(), message);
	}
	/**
	 * @param nickName
	 * @param message
	 */
	public WhoisMessage(String nickName, String message) {
		super(nickName, message.trim());
	}
	@Override
	public String toString() {
		if(this.number == 311){
			return "[" + time.format(DateTimeFormatter.ISO_TIME) + "] " + " -" + this.number + "- " + this.parse311() + "\n";
		}else if(this.number == 319){
			return "[" + time.format(DateTimeFormatter.ISO_TIME) + "] " + " -" + this.number + "- Channels: " + this.parse319() + "\n";
		}else{
			return "[" + time.format(DateTimeFormatter.ISO_TIME) + "] " + " -" + this.number + "- " + this.message + "\n";
		}
	}
	private String parse311(){
		StringTokenizer tok = new StringTokenizer(this.message);
		if(tok.countTokens() >= 4){
			return "" + "[" + tok.nextToken().trim() + "!" + tok.nextToken().trim() + "@" +
					"" + tok.nextToken().trim() + "] " + tok.nextToken("\n").trim();
		}
		return "" + tok.nextToken("\n");
	}
	private String parse319(){
		return "" + this.message.substring(this.message.indexOf(" :") + 2);
		
	}
	
	
	/*
	 * :magnet.rizon.net 311 ClientInDevelopment ClientInDevelopment ~Chkrr00ks Rizon-480D1A65.ip49.fastwebnet.it * :Chkrr00ksPropriety
	 * :magnet.rizon.net 319 ClientInDevelopment ClientInDevelopment :@#rgb
	 * :magnet.rizon.net 312 ClientInDevelopment ClientInDevelopment magnet.rizon.net :Rizon Client Server
	 * :magnet.rizon.net 310 ClientInDevelopment ClientInDevelopment :is using modes +ix authflags: [none]
	 * :magnet.rizon.net 338 ClientInDevelopment ClientInDevelopment :is actually ~Chkrr00ks@93-34-113-103.ip49.fastwebnet.it [93.34.113.xxx]
	 * :magnet.rizon.net 317 ClientInDevelopment ClientInDevelopment 25 1478281450 :seconds idle, signon time
	 * :magnet.rizon.net 318 ClientInDevelopment ClientInDevelopment :End of /WHOIS list.
	 */
	
}
