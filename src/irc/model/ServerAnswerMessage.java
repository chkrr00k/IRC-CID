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
public abstract class ServerAnswerMessage extends Comunication {

	public static final String MOTD = "motd";
	public static final String ERROR = "error";
	public static final String UNKNOWN = "unknown";
	
	protected int number;

	public ServerAnswerMessage() {
		super();
	}
	/**
	 * @param nickName
	 * @param message
	 * @param time
	 */
	public ServerAnswerMessage(String nickName, String message, LocalDateTime time) {
		super(nickName, message, time);
	}
	public ServerAnswerMessage(int number, String nickName, String message) {
		super(nickName, message, LocalDateTime.now());
		this.number = number;
	}

	/**
	 * @param nickName
	 * @param message
	 */
	public ServerAnswerMessage(String nickName, String message) {
		super(nickName, message);
	}

	@Override
	public String toString() {
		return "[" + time.format(DateTimeFormatter.ISO_TIME) + "] " + " -" + this.number + "- " + this.message + "\n";
	}
	
	public static Comunication of(String input){

		StringTokenizer tok = new StringTokenizer(input);
		String serverName = tok.nextToken(" ").trim().substring(1);
		int number = Integer.parseInt(tok.nextToken(" ").trim());
		tok.nextToken(" ");
		String message = tok.nextToken("\n").trim();
		if(message.startsWith(":")){
			message = message.substring(1);
		}
		if(number >= 400 && number < 600){
			return new ErrorMessage(number, serverName, message);
		}else if(number == 376 || number == 375 || number == 372){
			return new MotdMessage(number, serverName, message);
		}else if(number == 311 || number == 319){
			return new WhoisMessage(number, serverName, message);
		}else if(number == 005){
			return new SettingsMessage(number, serverName, message);
		}else{
			return new UnknownMessage(number, serverName, message);
		}

	}

	/*
	 * :magnet.rizon.net 311 ClientInDevelopment ClientInDevelopment ~Chkrr00ks Rizon-480D1A65.ip49.fastwebnet.it * :Chkrr00ksPropriety
	 * :magnet.rizon.net 319 ClientInDevelopment ClientInDevelopment :@#rgb
	 * :magnet.rizon.net 312 ClientInDevelopment ClientInDevelopment magnet.rizon.net :Rizon Client Server
	 * :magnet.rizon.net 310 ClientInDevelopment ClientInDevelopment :is using modes +ix authflags: [none]
	 * :magnet.rizon.net 338 ClientInDevelopment ClientInDevelopment :is actually ~Chkrr00ks@93-34-113-103.ip49.fastwebnet.it [93.34.113.103]
	 * :magnet.rizon.net 317 ClientInDevelopment ClientInDevelopment 25 1478281450 :seconds idle, signon time
	 * :magnet.rizon.net 318 ClientInDevelopment ClientInDevelopment :End of /WHOIS list.
	 */
	
}