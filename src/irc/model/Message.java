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
/*
 * :nickName!realName@hostName PRIVMSG channel :message
 * XXX can be changed cfr the one on irc.model.Irc
 * 
 * 1) realName 		-your name
 * 2) nickName 		-the name that you display (can be changed)
 * 3) hostName 		-hash of your IP or VHost
 * 4) channel  		-the channel in which the message was sent (doesn't apply for PM TODO fix this)
 * 4 bis) sender	-the one that sent you the message (NOT YET IMPLEMENTED) TODO implement 
 * 5) message		-the message
 * 6) time			-when the message was sent (NOT YET IMPLEMENTED)
 */
public class Message extends Comunication implements Commands{
	
	//private String realName;
	//private String nickName;
	//private String hostName;
	protected String channel;
	//private String message;
	//private LocalDateTime time;
	
	
	
	public Message(String line) throws IllegalArgumentException{
		this.person = new Person();
		this.digester(line);
		this.time = LocalDateTime.now();
	}
	public Message() {
		super();
	}
	public Message(String message, User user, String channel){
		super.person = new Person(user.getNickName(), user.getRealName(), "", user.getRank());
		super.message = message;
		this.channel = channel;
		super.time = LocalDateTime.now();
	}
	
	
	//getters;
	public String getRealName() {
		return this.person.getRealName();
	}/*
	public String getNickName() {
		return nickName;
	}*/
	public String getHostName() {
		return this.person.getHostName();
	}
	public String getChannel() {
		return channel;
	}/*
	public String getMessage() {
		return message;
	}
	public LocalDateTime getTime() {
		return time;
	}*/
	
	//line parser;
	//dakidaki!~dakine@8D7FCA66:822F5F4C:41012FFC:IP PRIVMSG #chat :great p-x , go on and do lewd stuff with cute people regardless of gender
	//dakidaki!~dakine@8D7FCA66:822F5F4C:41012FFC:IP PRIVMSG #chat :IF IT'S CUTE, FUCK IT; THERE IS NO GAY
	private void digester(String line) throws IllegalArgumentException{
		if(line.startsWith(":")){
			line = line.substring(1);
			this.person.setNickName(line.substring(0, line.indexOf("!")).trim());
			this.person.setRealName(line.substring(line.indexOf("!") + 1, line.indexOf("@")).trim());
			this.person.setHostName(line.substring(line.indexOf("@") + 1, line.indexOf(" ")).trim());
			try{
				String channel = line.substring(line.indexOf(" PRIVMSG") + 8, line.indexOf(" :")).trim().toLowerCase();
				this.channel = channel.startsWith("#") ? channel : this.person.getNickName().toLowerCase();
			}catch(Exception e){
				System.err.println(line);
				throw new IllegalArgumentException("Unable to parse message " + line);
			}
			this.message = line.substring(line.indexOf(" :") + 2).trim();
		}
		else{
			throw new IllegalArgumentException("Unable to parse message " + line);
		}
	}

	public String apply(){
		return "PRIVMSG " + this.channel + " :" + this.message +"\r\n";
	}

	@Override
	public String toString() {
		return "[" + time.format(DateTimeFormatter.ISO_LOCAL_TIME) + "] <" + this.person.getNickName() + "> " + message + "\n";
	}
	/**
	 * @return
	 * @deprecated
	 */
	public boolean isPrivateMessage() {
		return !this.channel.trim().startsWith("#");
	}
	
	
}
