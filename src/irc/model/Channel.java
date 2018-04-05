/**
 * 
 */
package irc.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * @author chkrr00k;
 *
 */
public class Channel extends Corrispondence{

	//Some "global" constants
	public static final String NEWPERSON = "001";
	public static final String ALLPERSON = "002";
	public static final String PARPERSON = "003";
	public static final String CHAPERSON = "004";
	public static final String OPSPERSON = "005";
	public static final String NEWMESSAGE = "011";
	public static final String NEWNOTICE = "011";
	public static final String NEWTOPIC = "111";
	public static final String JOINREFUSED = "aaa";
	public static final String NEWCHANNEL = "bbb";

	
	//let the class begin
	private String topic;
	private String mode;
	/*
	 * :nickName!realName@hostName PRIVMSG channel :message
	 * XXX can be changed cfr the one on irc.model.Irc
	 * 
	 * 1) realName 		-your name
	 * 2) nickName 		-the name that you display (can be changed)
	 * 3) hostName 		-hash of your IP or VHost
	 * 4) channel  		-the channel in which the message was sent (doesn't apply for PM TODO fix this)
	 * 5) message		-the message
	 * 6) time			-when the message was sent (NOT YET IMPLEMENTED)
	 */
	
	public Channel(String name, User user, PropertyChangeListener pcl) {
		this.name = name.toLowerCase();
		super.persons = new LinkedList<Person>();
		this.topic = "No topic";
		//this.messages = new LinkedList<Map<String, String>>();
		//this.messages = new LinkedList<Message>();
		super.comunications= new LinkedList<Comunication>();
		super.newMessages = new LinkedList<Comunication>();
		super.user = user;
		super.chkChang.addPropertyChangeListener(pcl);
		this.chkChang.firePropertyChange(Channel.NEWCHANNEL, this.name, null);
	}
	
	public void setTopic(String input){
		this.topic = input;
		super.chkChang.firePropertyChange(Channel.NEWTOPIC, this.name, input);
		
	}
	/**
	 * @deprecated
	 */
	/*
	public List<Message> getMessages(){
		return this.messages;
	}
	/**
	 * @deprecated
	 */
	public String getMessagesFormatted() {
		int size = this.comunications.size();
		StringBuilder strBld = new StringBuilder();
		for(int i = 0; i < size; i++){
			strBld.append(this.comunications.get(i).toString());
		}
		return strBld.toString();
	}
	
	public String getNewMessages() {
		if(this.newMessages.isEmpty()){
			return "";
		}
		StringBuilder strBld = new StringBuilder();
		for(Comunication com : this.newMessages){
			strBld.append(com.toString());
		}
		this.comunications.addAll(this.newMessages);
		this.newMessages.clear();
		return strBld.toString();
	}
	public String getAllMessages() {
		StringBuilder strBld = new StringBuilder();
		for(Comunication com : this.comunications){
			strBld.append(com.toString());
		}
		return strBld.toString();
	}
	public void addMessage(Comunication input){		
		this.newMessages.add(input);
		if(input instanceof NoticeMessage){
			this.chkChang.firePropertyChange(Channel.NEWMESSAGE, this.name, input);
			return;
		}
		this.chkChang.firePropertyChange(Channel.NEWMESSAGE, this.name, input);
	}
	
	public void addPerson(List<Person> input){
		this.persons.addAll(input);
		this.sortPerson();
		this.chkChang.firePropertyChange(Channel.ALLPERSON, this.name, input);
	}
	public void addPerson(Person input){
		this.persons.add(input);
		this.sortPerson();
		this.chkChang.firePropertyChange(Channel.NEWPERSON, this.name, input);
	}
	
	void sortPerson(){
		this.persons.sort(new Person());
		/*
		this.persons.sort(new Comparator<Person>() {
			@Override
			public int compare(Person o1, Person o2) {
				return (o1.getNickName().toLowerCase()).compareTo(o2.getNickName().toLowerCase());
			}	
		});
		*/
	}
	
	public List<Person> getPersons(){
		/*
		if(this.persons.isEmpty()){
			try {
				Thread.sleep((long) (100));     // 1 seconds delay
			} catch(InterruptedException ex) {
				;
			}
		}*/
		return this.persons;
	}
	

	public String getMode() {
		return mode;
	}
	public boolean addMode(String mode) throws BadHandlerException {
		if(mode.length() != 2){
			return false;
		}
		String factor = mode.substring(1);
		if(mode.startsWith("+")){
			if(this.mode.contains(factor)){
			}else{
				this.mode += factor;
			}
		}else if(mode.startsWith("-")){
			if(!this.mode.contains(factor)){
			}else{
				this.mode = this.mode.replace(factor, "");
			}
		}else{
			throw new BadHandlerException();
		}
		return true;
	}

	public boolean adjustRank(String nickName, String rank){
		try {
			if(rank.contains("b")){
				this.addMessage(new StatusMessage(nickName, StatusMessage.MODE, rank));
				this.sortPerson();
				super.chkChang.firePropertyChange(Channel.OPSPERSON, this.name, nickName);
				return true;
			}else if(this.getPerson(nickName).adjustRank(rank)){
				this.addMessage(new StatusMessage(nickName, StatusMessage.MODE, rank));
				this.sortPerson();
				super.chkChang.firePropertyChange(Channel.OPSPERSON, this.name, nickName);
				return true;
			}
			return false;
		} catch (BadHandlerException e) {
			return false;
		}
	}
	
	public boolean changePerson(Person oldNick, Person newNick){
		/*String ircopS = "~&@%+";
		boolean found = false;
		try{
			if(!this.persons.remove(oldNick)){
				
				for(char ch : ircopS.toCharArray()){
					try{
						found = this.persons.remove(ch + oldNick);
					}catch(NoSuchElementException e1){
						continue;
					}
					if(found){
						break;
					}
				}
			}else{
				found = true;
			}
		}catch(NoSuchElementException e){
			return false;
		}*/
		if(this.persons.remove(oldNick)){
			this.persons.add(newNick);
			this.sortPerson();
			this.chkChang.firePropertyChange(Channel.CHAPERSON, this.name, newNick);
			return true;
		}		
		return false;
	}
	
	public boolean removePerson(String input){
		for(Person per : this.persons){
			if(per.getNickName().equals(input.trim())){
				this.persons.remove(per);
				this.chkChang.firePropertyChange(Channel.PARPERSON, this.name, input);
				return true;
			}
		}
		return false;
	}
	
	public boolean removePerson(Person input){
		// common IRCop symbols "~&@%+"
		/*
		String ircopS = "~&@%+";
		boolean found = false;
		/*
		if(input.startsWith("~") || input.startsWith("&") || input.startsWith("@") ||
				input.startsWith("%") || input.startsWith("+")){
			input = input.substring(1);
		}
		*
		try{
			if(!this.persons.remove(input)){
				for(char ch : ircopS.toCharArray()){
					try{
						found = this.persons.remove(ch + input);
					}catch(NoSuchElementException e1){
						continue;
					}
					if(found){
						break;
					}
				}
			}else{
				this.chkChang.firePropertyChange(Channel.PARPERSON, this.name, input);
				return true;
			}
		}catch(NoSuchElementException e){
			return false;
		}*/
		if(this.persons.remove(input)){
			this.chkChang.firePropertyChange(Channel.PARPERSON, this.name, input);
			return true;
		}
		return false;
	}
	@Override
	public String toString() {
		return "Channel " + name + "(" + topic + ") , persons=" + persons;
	}

	public String getName(){
		return this.name;
	}
	
	public String send(String message, String myNick) throws PostException{
		this.delay();
		/*
		Map<String,String> result = new HashMap<>();
		result.put("realName", "");
		result.put("nickName", myNick);
		result.put("hostName", "");
		result.put("channel", this.name);
		result.put("message", message);
		result.put("time", LocalDateTime.now().format(DateTimeFormatter.ISO_TIME).toString());
		this.addMessage(result);
		*/
		boolean canPost = false;
		for(Person p : this.persons){
			if(p.equals(this.user)){
				canPost = p.canPost();
			}
		}
		if(canPost){
			/*
			if(message.startsWith("/me")){
				ActionMessage msg = new ActionMessage(message.substring(3), this.user, this.name);
				this.addMessage(msg);
				return msg.send();
				//return "PRIVMSG " + this.name + " " + (char)(0x01) + "ACTION " + message.substring(3) +"\r\n";
			}*/
			Message msg = new Message(message, this.user, this.name);
			this.addMessage(msg);
			//return "PRIVMSG " + this.name + " :" + message +"\r\n";
			return msg.apply();
		}else{
			//System.out.println("Can't post there");
			this.addMessage(new StatusMessage(this.user.getNickName(), StatusMessage.BANN));
			//return "PRIVMSG " + this.name + " :" + message +"\r\n";
			throw new PostException();
		}
	}
	
	public String sendAction(String message, String myNick) throws PostException{
		this.delay();
		/*
		Map<String,String> result = new HashMap<>();
		result.put("realName", "");
		result.put("nickName", myNick);
		result.put("hostName", "");
		result.put("channel", this.name);
		result.put("message", message);
		result.put("time", LocalDateTime.now().format(DateTimeFormatter.ISO_TIME).toString());
		this.addMessage(result);
		*/
		boolean canPost = false;
		for(Person p : this.persons){
			if(p.equals(this.user)){
				canPost = p.canPost();
			}
		}
		if(canPost){
			/*if(message.startsWith("/me")){*/
				ActionMessage msg = new ActionMessage(message/*.substring(3)*/, this.user, this.name);
				this.addMessage(msg);
				return msg.apply();
				//return "PRIVMSG " + this.name + " " + (char)(0x01) + "ACTION " + message.substring(3) +"\r\n";
			/*}
			Message msg = new Message(message, this.user, this.name);
			this.addMessage(msg);
			//return "PRIVMSG " + this.name + " :" + message +"\r\n";
			return msg.send();*/
		}else{
			//System.out.println("Can't post there");
			this.addMessage(new StatusMessage(this.user.getNickName(), StatusMessage.BANN));
			//return "PRIVMSG " + this.name + " :" + message +"\r\n";
			throw new PostException();
		}
	}
	
	/* (non-Javadoc)
	 * @see irc.model.Corrispondence#sendNotice(java.lang.String, java.lang.String)
	 */
	@Override
	public String sendNotice(String message, String myNick) throws PostException {
		NoticeMessage msg = new NoticeMessage(message, this.user, this.name);
		this.addMessage(msg);
		return msg.apply();
	}

	public String join(){
		this.delay();
		return "JOIN " + this.name + "\r\n";
	}
	
	public String part(){
		this.delay();
		return "PART " + this.name + " :Leaving" + "\r\n";
	}
	
	void delay(){
		try {
			Thread.sleep((long) (1000));     // 1 seconds delay
		} catch(InterruptedException ex) {
			return;
		}
	}
}
