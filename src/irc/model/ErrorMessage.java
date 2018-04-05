/**
 * 
 */
package irc.model;

import java.time.LocalDateTime;
import java.util.StringTokenizer;

/**
 * @author chkrr00k
 *
 */
public class ErrorMessage extends ServerAnswerMessage {

	/**
	 * @param nickName
	 * @param message
	 * @param time
	 */
	public ErrorMessage(int number, String nickName, String message) {
		super(nickName, message, LocalDateTime.now());
		this.number = number;
	}

	/**
	 * @param nickName
	 * @param message
	 */
	public ErrorMessage(String nickName, String message) {
		super(nickName, message);
	}
	/*
	public ErrorMessage(String line) throws IllegalArgumentException{
		this.person = new Person();
		this.digester(line);
		this.time = LocalDateTime.now();
	}
*/
	/*
	 * @param line
	 *
	private void digester(String line) {
		StringTokenizer tok = new StringTokenizer(line);
		//boolean chk = false;
		String serverName = tok.nextToken(" ").trim().substring(1);
		int number = Integer.parseInt(tok.nextToken(" ").trim());
		tok.nextToken(" ");
		String message = tok.nextToken("\n").trim();
		if(message.startsWith(":")){
			message = message.substring(1);
		}
		this.message = message;
		this.number = number;
	}
	*/
	public String guessChannel(){
		if(this.message.contains("#")){
			String tmp = this.message.substring(this.message.indexOf("#")).trim();
			tmp = tmp.substring(0, tmp.indexOf(":"));
			return tmp.trim();
		}
		return "";
	}

}
