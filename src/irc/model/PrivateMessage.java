/**
 * 
 */
package irc.model;

import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;

/**
 * @author chkrr00k
 *
 */
public class PrivateMessage extends Corrispondence {

	//some "global" constants;
	public static final String NEWMESSAGE = "011";
	public static final String NEWPRIVATEMESSAGE = "012";
	public static final String NEWNOTICEMESSAGE = "015";
	public static final String NEWNOTICE = "014";
	public static final String PARPERSON = "003";
	public static final String CHAPERSON = "004b";
	
	
	//let the class begin
	//private String person;
	//private User user;
	//private PropertyChangeSupport chkChang = new PropertyChangeSupport(this);
	
	/*
	 * :nickName!realName@hostName PRIVMSG sender :message
	 * XXX can be changed cfr the one on irc.model.Irc
	 * 
	 * 1) realName 		-your name
	 * 2) nickName 		-the name that you display (can be changed)
	 * 3) hostName 		-hash of your IP or VHost
	 * 4) channel  		-the channel in which the message was sent (doesn't apply for PM)
	 * 4  sender		-the one that sent you the message (NOT YET IMPLEMENTED)
	 * 5) message		-the message
	 * 6) time			-when the message was sent (NOT YET IMPLEMENTED)
	 */
	
	public PrivateMessage(Comunication message, User user, PropertyChangeListener pcl) {
		//this.person = message.getNickName();
		super.persons = new LinkedList<Person>();
		persons.add(message.getPerson());
		super.name = message.getNickName().toLowerCase();
		super.comunications= new LinkedList<Comunication>();
		super.newMessages = new LinkedList<Comunication>();
		this.user = user;
		this.chkChang.addPropertyChangeListener(pcl);
		this.newMessages.add(message);
		//this.addMessage(message);
	}
	public PrivateMessage(String nickName, User user, PropertyChangeListener pcl) {
		//this.person = nickName;
		super.persons = new LinkedList<Person>();
		persons.add(new Person(nickName));
		super.name = nickName.toLowerCase();
		super.comunications= new LinkedList<Comunication>();
		super.newMessages = new LinkedList<Comunication>();
		this.user = user;
		this.chkChang.addPropertyChangeListener(pcl);
		//this.chkChang.firePropertyChange(PrivateMessage.NEWMESSAGE, this.name, null);
	}
	
	public void newPrivateMessage(){
		this.chkChang.firePropertyChange(PrivateMessage.NEWPRIVATEMESSAGE, this.name, null);
	}
	public void newNoticeMessage(){
		this.chkChang.firePropertyChange(PrivateMessage.NEWNOTICEMESSAGE, this.name, null);
	}
	//FIXME ???
	public String getPerson() {
		return super.persons.toString();
	}
	/* (non-Javadoc)
	 * @see irc.model.Corrispondence#getPersons()
	 */
	@Override
	public List<Person> getPersons() {
		return super.persons;
		
	}
	public String getAllMessages() {
		StringBuilder strBld = new StringBuilder();
		for(Comunication com : this.comunications){
			strBld.append(com.toString());
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
	public boolean isNotice(){
		for(Comunication c : this.comunications){
			if(c instanceof Message){
				return false;
			}
		}
		for(Comunication c : this.newMessages){
			if(c instanceof Message){
				return false;
			}
		}
		return true;
	}
	public void addMessage(Comunication input){
		this.newMessages.add(input);
		if(input instanceof NoticeMessage){
			this.chkChang.firePropertyChange(PrivateMessage.NEWNOTICE, this.name, input);
			return;
		}
		this.chkChang.firePropertyChange(PrivateMessage.NEWMESSAGE, this.name, input);
	}
	public String send(String message, String myNick){
		this.delay();/*
		if(message.startsWith("/me")){
			ActionMessage msg = new ActionMessage(message.substring(3), this.user, this.name);
			this.addMessage(msg);
			return msg.send();
			//return "PRIVMSG " + this.name + " " + (char)(0x01) + "ACTION " + message.substring(3) +"\r\n";
		}*/
		Message msg = new Message(message, this.user, this.name);
		this.addMessage(msg);
		return "PRIVMSG " + this.name + " :" + message +"\r\n";
	}
	public String sendAction(String message, String myNick){
		this.delay();/*
		if(message.startsWith("/me")){*/
			ActionMessage msg = new ActionMessage(message/*.substring(3)*/, this.user, this.name);
			this.addMessage(msg);
			return msg.apply();
			//return "PRIVMSG " + this.name + " " + (char)(0x01) + "ACTION " + message.substring(3) +"\r\n";
		/*}
		Message msg = new Message(message, this.user, this.name);
		this.addMessage(msg);
		return "PRIVMSG " + this.name + " :" + message +"\r\n";*/
	}
	
	@Override
	public String sendNotice(String message, String myNick) throws PostException {
		NoticeMessage msg = new NoticeMessage(message, this.user, this.name);
		this.addMessage(msg);
		return msg.apply();
	}
	
	@Override
	public String toString() {
		return "PRIVMSG " + this.name + this.newMessages;
	}
	
	private void delay(){
		try {
			Thread.sleep((long) (1000));     // 1 seconds delay
		} catch(InterruptedException ex) {
			return;
		}
	}

	@Override
	//FIXME it doesn't work
	public boolean changePerson(Person oldNick, Person newNick) {
		if(super.persons.contains(oldNick)){
			if(super.persons.remove(oldNick)){
				super.persons.add(newNick);
				this.chkChang.firePropertyChange(PrivateMessage.CHAPERSON, this.name, newNick);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean removePerson(Person input) {
		this.chkChang.firePropertyChange(PrivateMessage.PARPERSON, this.name, input);
		return false;
	}
	@Override
	public boolean removePerson(String input) {
		this.chkChang.firePropertyChange(PrivateMessage.PARPERSON, this.name, input);
		return false;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String join() {
		return "";
	}
	/* (non-Javadoc)
	 * @see irc.model.Corrispondence#adjustRank(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean adjustRank(String nickName, String rank) {
		return false;
	}
}
