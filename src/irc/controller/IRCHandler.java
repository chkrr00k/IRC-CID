/**
 * 
 */
package irc.controller;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Set;

import irc.model.BadHandlerException;
import irc.model.ChannelNotFoundException;
import irc.model.Comunication;
import irc.model.ErrorMessage;
import irc.model.Irc;
import irc.model.Person;
import irc.model.PostException;
import irc.model.ServerAnswerMessage;
import irc.model.UnknownMessage;
import irc.model.User;

/**
 * @author chkrr00k
 *
 */
public class IRCHandler implements Runnable{

	private Irc irc;
	private boolean keep;
	private User user;
	private String server;
	private PropertyChangeListener pcl;
	private boolean ssl;
	private int port;
	private boolean quitted = false;
	
	/*settings*/
	//String server = "chat.freenet.net";
	//String server = "irc.rizon.net";
	// Nick on said server;
	//String nickname = "chkrr00k";
	//String nickName = "ClientInDevelopment";
	// Username on said server;
	//String realName = "IfLostReturnToChkrr00k";
	// Password on said server
	//String password ="";
	// The channel which the bot will join.
	//String channel = "#rizon";
	//String channel3 = "#help";
	
	public synchronized void stop(){
		this.keep = false;
		try {
			irc.disconnect();
		} catch (IOException e) {
			System.err.println("Can't stop the IRCHandler thread");
		}
	}
	public synchronized void joinChannel(String channelName, PropertyChangeListener pcl){
		this.irc.addJoinChannel(channelName, pcl);
	}
	/**
	 * @param channelName
	 * @param frame
	 */
	public synchronized void startPrivateMessage(String nickName, PropertyChangeListener pcl) {
		this.irc.startPrivMsg(nickName, pcl);
		
	}
	public synchronized void partChannel(String channelName){
		this.irc.partChannel(channelName);
	}
	public synchronized void quit(){
		try {
			this.irc.disconnect();
		} catch (IOException e) {
			System.err.println("Unable to quit");
			e.printStackTrace();
		}
	}
	public synchronized String getMotd(){
		return this.irc.getMotd();
	}
	/**
	 * @return
	 */
	public synchronized ErrorMessage getFirstError() {
		return this.irc.getFirstError();
	}
	/**
	 * @return
	 */
	public synchronized ServerAnswerMessage getUnknownAnswer() {
		return this.irc.getFirstUnknownAnswer();
	}
	public synchronized Set<String> getChannels(){
		return this.irc.getCorrispondence();
	}
	public synchronized String getNewMessages(String channelName){
		try {
			return this.irc.getMessagesFormatted(channelName);
		} catch (ChannelNotFoundException e) {
			System.err.println("Unable to get " + channelName + " messages");
			return "ERROR";
		}
	}
	public synchronized String getMessages(String channelName){
		try {
			return this.irc.getMessages(channelName);
		} catch (ChannelNotFoundException e) {
			System.err.println("Unable to get " + channelName + " messages");
			return "ERROR";
		}
	}
	public synchronized String getPeople(String channelName){
		try {
			StringBuilder strBld = new StringBuilder();
			for(Person person : this.irc.getPersons(channelName)){
				strBld.append(person + "\n");
			}
			return strBld.toString();
		} catch (ChannelNotFoundException e) {
			System.err.println("Unable to get " + channelName + " people");
			return "ERROR";
		}
	}
	public synchronized void addMessage(String channelName, Comunication message){
		try {
			this.irc.addMessage(channelName, message);
		} catch (ChannelNotFoundException | IOException e) {
			System.err.println("Unable to send message to " + channelName);
		}
	}
	public synchronized void sendMessage(String channelName, String message){
		try {
			this.irc.sendMessage(channelName, message);
		} catch (ChannelNotFoundException | IOException e) {
			System.err.println("Unable to send message to " + channelName);
		}
	}
	public synchronized void sendActionMessage(String channelName, String message){
		try {
			this.irc.sendActionMessage(channelName, message);
		} catch (ChannelNotFoundException | IOException e) {
			System.err.println("Unable to send message to " + channelName);
		}
	}
	public synchronized void sendNoticeMessage(String channelName, String message){
		try {
			this.irc.sendNoticeMessage(channelName, message);
		} catch (ChannelNotFoundException | IOException e) {
			System.err.println("Unable to send message to " + channelName);
		}
	}
	public synchronized void changeNick(String newNick){
		try {
			this.irc.changeNick(newNick);
		} catch (IOException e) {
			System.err.println("Unable to change Nick");
		}
	}
	/**
	 * 
	 * @param channelName
	 * @param person
	 * @param mode
	 */
	public synchronized void mode(String channelName, String person, String mode){
		try{
			this.irc.mode(channelName, person, mode);
		}catch (ChannelNotFoundException | IOException | IllegalArgumentException | BadHandlerException e) {
			System.err.println("Unable to mode " + person);
		}
	}
	/**
	 * @param channelName
	 * @param mode
	 */
	public synchronized void mode(String channelName, String mode){
		try{
			this.irc.mode(channelName, mode);
		}catch (ChannelNotFoundException | IOException | IllegalArgumentException | BadHandlerException e) {
			System.err.println("Unable to mode " + channelName);
		}
	}
	public synchronized void sendRaw(String rawLine){
		try {
			this.irc.sendRawLine(rawLine);
		} catch (IOException e) {
			System.err.println("Unable to send raw line");
		}
	}
	/**
	 * @deprecated
	 */
	
	public IRCHandler() {
		this.keep = true;
	}

	public IRCHandler(User user, String server) {
		super();
		this.user = user;
		this.server = server;
		this.keep = true;
	}
	public IRCHandler(String nickName, String realName, String server, int port, boolean ssl) {
		super();
		this.user = new User(nickName, realName, "");
		this.server = server;
		this.keep = true;
		this.port = port;
		this.ssl = ssl;
	}


	public void setPcl(PropertyChangeListener pcl) {
		this.pcl = pcl;
	}
	public void run() {
		//this.user = new User(nickName, realName);
		
		try {
			irc = new Irc(server, port, user, pcl, ssl);
			//System.out.println(irc.connect(realName, nickName));
			
			irc.connect(this.user.getRealName(), this.user.getNickName());
			/*
			irc.addJoinChannel(channel);
			irc.addChannel(channel2);
			irc.joinChannel(channel2);
			irc.addJoinChannel(channel3);
			//irc.send(channel, "Will close in 2 minutes");
			//irc.send(channel2, "ready to test");
			//LocalDateTime start = LocalDateTime.now();
			 * */
			while(/*start.plusMinutes(1).isAfter(LocalDateTime.now())*/ this.keep){
				if(irc.isConnect()){
				System.out.println(irc.read());
				}else{
					this.keep = false;
				}
				//irc.read();
			}
			//irc.send(channel, "Closed");
			/*
			try (Writer writer = new BufferedWriter(new OutputStreamWriter(
		              new FileOutputStream("C:\\Users\\chkrr00k\\Desktop\\ircLog.txt"), "utf-8"))) {
		   writer.write(irc.getMessages(channel).toString());
		   System.out.println("End");
		}
			*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
