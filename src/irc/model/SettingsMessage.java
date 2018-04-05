/**
 * 
 */
package irc.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author chkrr00k
 *
 */
public class SettingsMessage extends ServerAnswerMessage {

	private Map<String, String> settings;

	public SettingsMessage() {
	}

	/**
	 * @param nickName
	 * @param message
	 * @param time
	 */
	public SettingsMessage(String nickName, String message, LocalDateTime time) {
		super(nickName, message, time);
		this.settings = new HashMap<String, String>();
		this.parser();
	}

	/**
	 * @param number
	 * @param nickName
	 * @param message
	 */
	public SettingsMessage(int number, String nickName, String message) {
		super(number, nickName, message);
		this.settings = new HashMap<String, String>();
		this.parser();
	}

	/**
	 * @param nickName
	 * @param message
	 */
	public SettingsMessage(String nickName, String message) {
		super(nickName, message);
		this.settings = new HashMap<String, String>();
		this.parser();
	}

	private void parser(){
		StringTokenizer tok = new StringTokenizer(this.message.split(" :")[0]);
		String tmp = "";
		String[] pair = null;
		while(tok.hasMoreTokens()){
			tmp = tok.nextToken();
			pair = tmp.split("=");
			this.settings.put(pair[0], pair.length > 1 ? pair[1] : null);
		}
		System.out.println(this.settings);
	}
	
	public Map<String, String> getSettings() {
		return this.settings;
	}
	
	@Override
	public String toString() {
		return "[" + time.format(DateTimeFormatter.ISO_TIME) + "] " + this.message + "\n";
	}
}
