/**
 * 
 */
package irc.model;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

/**
 * @author chkrr00k
 *
 */
public abstract class Comunication {
	
	static Pattern channelMess = Pattern.compile(":(.+)\\!(.+)\\@(.+) (?i)(privmsg) (\\#\\w+) \\:?(.*)");
	static Pattern privateMess = Pattern.compile(":(.+)\\!(.+)\\@(.+) (?i)(privmsg) ([^\\#]\\w+) \\:?(.*)");
	static Pattern privmsg = Pattern.compile(":(.+)\\!(.+)\\@(.+) (?i)(privmsg) (\\#?\\w+) \\:?(.*)");
	static Pattern noticeChan = Pattern.compile(":(.+)\\!(.+)\\@(.+) (?i)(notice) (\\#\\w+) \\:?(.*)");
	static Pattern noticePers = Pattern.compile(":(.+)\\!(.+)\\@(.+) (?i)(notice) ([^\\#]\\w+) \\:?(.*)");
	static Pattern notice = Pattern.compile(":(.+)\\!(.+)\\@(.+) (?i)(notice) (\\#?\\w+) \\:?(.*)");
	static Pattern topic = Pattern.compile(":(.+)\\!(.+)\\@(.+) (?i)(topic) (\\#\\w+) \\:?(.*)");
	static Pattern mode = Pattern.compile(":(.+)\\!(.+)\\@(.+) (?i)(mode) (\\S+) :?(\\+|\\-)(\\S+)(\\s(.*))?");
	static Pattern nick = Pattern.compile(":(.+)\\!(.+)\\@(.+) (?i)(nick) \\:?(.*)");
	static Pattern part = Pattern.compile(":(.+)\\!(.+)\\@(.+) (?i)(part) (\\#\\w+)( \\:?(.*))?");
	static Pattern quit = Pattern.compile(":(.+)\\!(.+)\\@(.+) (?i)(quit)( \\:?(.*))?");
	static Pattern join = Pattern.compile(":(.+)\\!(.+)\\@(.+) (?i)(join) \\:?(\\#\\w+)");
	static Pattern kick = Pattern.compile(":(.+)\\!(.+)\\@(.+) (?i)(kick) (\\#\\w+)([^\\#]\\w+)( \\:?(.*))?");
	static Pattern generic = Pattern.compile(":(\\S*)\\s(\\d*)\\s(?i)(\\w+)\\s(.*)");
	static Pattern generic(int number){
		return Pattern.compile(":(\\S*)\\s(" + number + ")\\s(?i)(\\w+)\\s(.*)");
	}
	static Pattern genericEx(int number){
		return Pattern.compile("(\\S*)\\s(\\d*)\\s(?i)(\\w+)\\s(.*)\\s\\:?(.*)");
	}
	
	//protected String nickName;
	protected Person person;
	protected String message;
	protected LocalDateTime time;


	public Comunication() {
		;
	}
	public Comunication(String nickName, String message, LocalDateTime time) {
		this.person = new Person(nickName, "");
		this.message = message;
		this.time = time;
	}
	public Comunication(String nickName, String message) {
		this.person = new Person(nickName, "");
		this.message = message;
		this.time = LocalDateTime.now();
	}
		
	public Person getPerson() {
		return person;
	}
	public String getNickName() {
		return this.person.getNickName();
	}
	public String getMessage() {
		return message;
	}
	public LocalDateTime getTime() {
		return time;
	}

	
}
