/**
 * 
 */
package irc.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;


/**
 * @author chkrr00k;
 * 
 *https://www.alien.net.au/irc/irc2numerics.html
 */
public class Irc {
	
	boolean debug = true;
	
	private Server server;
	private boolean connected;
	private Map<String, Corrispondence> corrispondence;
	private ServerAnswers answers;
	private User user;
	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	private PropertyChangeListener pcl;

	private Map<String, String> settings;

	
	public Irc(Server server, User user, PropertyChangeListener pcl) throws IOException {
		if(server.isConnected()){
			this.server = server;
			this.connected = false;
			this.user = user;
			this.pcs.addPropertyChangeListener(pcl);
			this.pcl = pcl;
			this.corrispondence = new HashMap<String, Corrispondence>();
			this.answers = new ServerAnswers(this.pcs);
			this.settings = new HashMap<String, String>();

			
		}
		else{
			throw new IOException();
		}
	}
	
	public Irc(String server, int port, User user, PropertyChangeListener pcl, boolean ssl) throws IOException {
		if(ssl){
			this.server = new SSLServer(server, port);
		}else{
			this.server = new Server(server, port);
		}
		if(this.server.isConnected()){
			this.connected = false;
			this.user = user;
			this.pcs.addPropertyChangeListener(pcl);
			this.pcl = pcl;
			this.corrispondence = new HashMap<String, Corrispondence>();
			this.answers = new ServerAnswers(this.pcs);
			this.settings = new HashMap<String, String>();
		}
		else{
			throw new IOException();
		}
	}
	
	//irc Getting
	
	/**
	 * @return
	 */
	public ServerAnswerMessage getFirstUnknownAnswer() {
		return this.answers.getUnknownAnswer();
	}

	public ErrorMessage getFirstError(){
		Comunication result = this.answers.getFirstError();
			return (ErrorMessage) result;
	}
	
	public String getMotd(){
		StringBuilder strbld = new StringBuilder();
		Queue<MotdMessage> motd = this.answers.getMotd();
		for(MotdMessage s : motd){
			strbld.append(s.toString());
		}
		return strbld.toString();
	}
	
	public String getMessages(String channel) throws ChannelNotFoundException{
		channel = channel.toLowerCase();
		if(this.corrispondence.containsKey(channel.toLowerCase())){
			return this.corrispondence.get(channel.toLowerCase()).getAllMessages();
		}else{
			throw new ChannelNotFoundException();
		}
	}
	public List<Comunication> getMessagesList(String channel) throws ChannelNotFoundException{
		channel = channel.toLowerCase();
		if(this.corrispondence.containsKey(channel.toLowerCase())){
			return this.corrispondence.get(channel.toLowerCase()).getListMessages();
		}else{
			throw new ChannelNotFoundException();
		}
	}
	public String getMessagesFormatted(String channel) throws ChannelNotFoundException{
		channel = channel.toLowerCase();
		if(this.corrispondence.containsKey(channel.toLowerCase())){
			return this.corrispondence.get(channel.toLowerCase()).getNewMessages();
		}else{
			throw new ChannelNotFoundException();
		}
	}
	public List<Person> getPersons(String channel) throws ChannelNotFoundException{
		channel = channel.toLowerCase();
		if(this.corrispondence.containsKey(channel.toLowerCase())){
			Corrispondence tmp = this.corrispondence.get(channel.toLowerCase());
			if(tmp instanceof Channel){
				return tmp.getPersons();
			}else if(tmp instanceof PrivateMessage){
				return tmp.getPersons();
			}else{
				System.err.println(channel + " was not a valid channel name");
				throw new ChannelNotFoundException();
			}
		}else{
			throw new ChannelNotFoundException();
		}
	}
	
	public Set<String> getCorrispondence(){
		return this.corrispondence.keySet();
	}
	
	//irc controller's function
	public void addMessage(String channel, Comunication message) throws ChannelNotFoundException, IOException{
		channel = channel.toLowerCase();
		if(this.corrispondence.containsKey(channel.toLowerCase())){
			this.corrispondence.get(channel.toLowerCase()).addMessage(message);
		}else{
			throw new ChannelNotFoundException();
		}
	}
	
	public void changeNick(String newNick) throws IOException{
		String oldNick = this.user.getNickName();
		try{
		this.write("NICK " + newNick + "\r\n");
		}catch(IOException e){
			throw e;
		}
		Corrispondence tmp = null;
		for(String key : this.corrispondence.keySet()){
			tmp = this.corrispondence.get(key);
			if(tmp.changePerson(new Person(oldNick), new Person(newNick))){
				tmp.addMessage(new StatusMessage(oldNick, StatusMessage.NICK, newNick));
			}
		}
		this.user.setNickName(newNick);
	}
	
	//irc comunication out
	public void sendMessage(String channel, String message) throws ChannelNotFoundException, IOException{
		channel = channel.toLowerCase();
		if(this.corrispondence.containsKey(channel.toLowerCase())){
			try {
				this.write(this.corrispondence.get(channel.toLowerCase()).send(message, this.user.getNickName()));
			} catch (PostException e) {
				System.err.println("Cant post on " + channel);
			}
		}else{
			throw new ChannelNotFoundException();
		}
	}
	public void sendActionMessage(String channel, String message) throws ChannelNotFoundException, IOException{
		channel = channel.toLowerCase();
		if(this.corrispondence.containsKey(channel.toLowerCase())){
			try {
				this.write(this.corrispondence.get(channel.toLowerCase()).sendAction(message, this.user.getNickName()));
			} catch (PostException e) {
				System.err.println("Cant post on " + channel);
			}
		}else{
			throw new ChannelNotFoundException();
		}
	}
	public void sendNoticeMessage(String channel, String message) throws ChannelNotFoundException, IOException{
		channel = channel.toLowerCase();
		if(this.corrispondence.containsKey(channel.toLowerCase())){
			try {
				this.write(this.corrispondence.get(channel.toLowerCase()).sendNotice(message, this.user.getNickName()));
			} catch (PostException e) {
				System.err.println("Cant post on " + channel);
			}
		}else{
			throw new ChannelNotFoundException();
		}
	}
	
	//irc Parsing
	
	/**
	 * @param rawLine
	 * @throws IOException 
	 */
	public void sendRawLine(String rawLine) throws IOException {
		this.write(rawLine);
		
	}

	/**
	 * @param channelName
	 * @param person
	 * @throws IOException 
	 * @throws ChannelNotFoundException 
	 * @throws BadHandlerException 
	 * @throws IllegalArgumentException
	 * @deprecated
	 */
	public void mode(String channelName, String person, String mode) throws IOException, ChannelNotFoundException, IllegalArgumentException, BadHandlerException {
		String channel = channelName.toLowerCase();
		PersonModality md = new PersonModality(mode, channel, person.toLowerCase());
		this.write(md.apply());
		
	}
	/**
	 * @param channelName
	 * @param mode
	 * @throws IOException
	 * @throws ChannelNotFoundException
	 * @throws IllegalArgumentException
	 * @throws BadHandlerException
	 * @deprecated
	 */
	public void mode(String channelName, String mode) throws IOException, ChannelNotFoundException, IllegalArgumentException, BadHandlerException {
		String channel = channelName.toLowerCase();
		ChannelModality md = new ChannelModality(mode, channel);
		this.write(md.apply());
	}

	/**
	 * @param nickName
	 * @param pcl2
	 */
	public void startPrivMsg(String nickName, PropertyChangeListener pcl) {
		PrivateMessage newPrivMsg = null;
		if(!this.corrispondence.containsKey(nickName.toLowerCase())){
			newPrivMsg = new PrivateMessage(nickName, this.user, pcl);
			this.corrispondence.put(nickName.toLowerCase(), newPrivMsg);
		}
		
	}

	public boolean addJoinChannel(String name, PropertyChangeListener pcl){
		Channel newChan = null;
		if(!this.corrispondence.containsKey(name.toLowerCase())){
			newChan = new Channel(name.toLowerCase(), this.user, pcl);
			this.corrispondence.put(name.toLowerCase(), newChan);
			try {
				this.write(newChan.join());
			} catch (IOException e) {
				return false;
			}
			return true;
		}
		return false;
		
	}

	public void addChannel(String name, PropertyChangeListener pcl){
		if(this.corrispondence.containsKey(name.toLowerCase())){
			return;
		}
		Channel newChan = new Channel(name.toLowerCase(), user, pcl);
		this.corrispondence.put(name.toLowerCase(), newChan);
	}
	
	public boolean joinChannel(String name){
		try {
			this.write((this.corrispondence.get(name.toLowerCase())).join());
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * @param channelName
	 */
	public void partChannel(String channelName) {
		if(this.corrispondence.containsKey(channelName.toLowerCase())){
			try {
				Corrispondence tmp = this.corrispondence.get(channelName.toLowerCase());
				if(tmp instanceof Channel){
					this.write(((Channel) tmp).part());
					this.corrispondence.remove(channelName.toLowerCase());
				}else{
					System.err.println("Unable to part! The parameter was not a channel! " + channelName);
				}
			} catch (IOException e) {
				System.err.println("Error parting");
			}
			this.corrispondence.remove(channelName.toLowerCase());
		}
		
	}
	
	
	public String read() throws IOException {
		if(!this.connected){
			return "Disconnected";
		}
		String result = this.server.read();
		if(result.startsWith("PING")){
			this.pingHandler(result);
		}else if(result.matches(Comunication.privmsg.pattern())){
			try {
				return this.messageHandler(result);
			} catch (BadHandlerException e) {
				return "privmsg[ERROR IN PARSING MESSAGE]";
			} catch (ChannelNotFoundException e) {
				return "privmsg[CHANNEL NOT FOUND]";
			}
		}else if(result.matches(Comunication.notice.pattern())){
			try {
				return this.noticeHandler(result);
			} catch (BadHandlerException e) {
				return "notice[ERROR IN PARSING MESSAGE]";
			}
		}else if(result.matches(Comunication.generic(353).pattern())){
			List<String> tmp;
			try {
				tmp = this.personListHandler(result);
			} catch (BadHandlerException e) {
				return "353[ERROR IN PARSING MESSAGE]";
			}
			Corrispondence temp = this.corrispondence.get(tmp.get(0).trim().toLowerCase());
			if(temp instanceof Channel){
				List<String> userNameList = tmp.subList(1, tmp.size());
				((Channel) temp).addPerson(this.toPersonList(userNameList));
			}else{
				System.err.println("Error in adding person: was not a channel§ " + temp);
			}
			if(this.debug){
				System.out.println(this.corrispondence.toString());
			}
			return tmp.toString();
		}else if(result.matches(Comunication.generic(366).pattern())){
			return "End names";
		}else if(result.matches(Comunication.generic(477).pattern()) || result.matches(Comunication.generic(473).pattern()) || result.matches(Comunication.generic(474).pattern())){
			try {
				return this.joinRefusedHandler(result);
			} catch (BadHandlerException e) {
				return "473[ERROR IN PARSING MESSAGE]";
			}
		}else if(result.matches(Comunication.generic(333).pattern())){
			try {
				return this.topicHandler(result);
			} catch (BadHandlerException | ChannelNotFoundException e) {
				return "333[ERROR IN PARSING MESSAGE]";
			}
		}else if(result.matches(Comunication.generic(332).pattern())){
			try {
				return "Topic: " + this.topicHandler(result);
			} catch (BadHandlerException | ChannelNotFoundException e) {
				return "332[ERROR IN PARSING MESSAGE]";
			}
		}else if(result.matches(Comunication.join.pattern()) && !result.contains(user.getNickName())){
			try {
				return this.joinHandler(result);
			} catch (BadHandlerException | ChannelNotFoundException e) {
				return "join[ERROR IN PARSING MESSAGE]";
			}
		}else if(result.matches(Comunication.join.pattern()) && result.contains(user.getNickName())){
			try {
				this.joinMeHandler(result);
				return "Forced join happened";
			} catch (BadHandlerException e) {
				return "forcedJoin[ERROR IN PARSING MESSAGE]";
			}
		}else if(result.matches(Comunication.part.pattern()) && !result.contains(user.getNickName())){
			try {
				return this.partHandler(result);
			} catch (BadHandlerException e) {
				return "part[ERROR IN PARSING MESSAGE]";
			}
		}else if(result.matches(Comunication.quit.pattern()) && !result.contains(user.getNickName())){
			try {
				return this.quitHandler(result);
			} catch (BadHandlerException e) {
				return "quit[ERROR IN PARSING MESSAGE]";
			}
		}else if(result.matches(Comunication.kick.pattern()) && !result.contains(user.getNickName())){
			try {
				return this.kickHandler(result);
			} catch (BadHandlerException e) {
				return "kick[ERROR IN PARSING MESSAGE]";
			}
		}else if(result.matches(Comunication.kick.pattern()) && result.contains(user.getNickName())){
			try {
				return this.kickMeHandler(result);
			} catch (BadHandlerException e) {
				return "kick[ERROR IN PARSING MESSAGE]";
			}
		}else if(result.matches(Comunication.nick.pattern()) && !result.contains(user.getNickName())){
			//:chkrr00k!KiwiIRC@c.plus.plus KICK #can pablinho :Bye!
			try {
				return this.nickHandler(result);
			} catch (BadHandlerException e) {
				return "nick[ERROR IN PARSING MESSAGE]";
			}
		}else if(result.matches(Comunication.topic.pattern())){
				try {
					return this.topicChangeHandler(result);
				} catch (BadHandlerException e) {
					return "topic[ERROR IN PARSING MESSAGE]";
				}
		}else if(result.matches(Comunication.mode.pattern())){
			//:chkrr00k!KiwiIRC@c.plus.plus MODE #can +o ClientInDevelopment 
			try {
				return this.modeHandler(result);
			} catch (BadHandlerException e) {
				return "mode[ERROR IN PARSING MESSAGE]";
			}
		}else if(result.matches(Comunication.generic.pattern())){
			return this.serverMessageHandler(result);
		}
		//:irc.x2x.cc 376 ClientInDevelopment :End of /MOTD command.
		//:irc.x2x.cc 042 ClientInDevelopment 39CAA1MHA :your unique ID
		//:irc.losslessone.com 401 ClientInDevelopment master3 :No such nick/channel
		//:irc.losslessone.com 404 ClientInDevelopment #can :Cannot send to channel
		
		return result;
	}
	
	private void write(String line) throws IOException{
		this.server.write(line);
		this.server.flush();
	}

	/**
	 * @param result
	 * @return
	 */	
	/*
	 * :cadance.canternet.org 251 ClientInDevelopment :There are 8 users and 759 invisible on 7 servers
	 * :cadance.canternet.org 252 ClientInDevelopment 42 :operator(s) online
	 * :cadance.canternet.org 254 ClientInDevelopment 714 :channels formed
	 * :cadance.canternet.org 255 ClientInDevelopment :I have 82 clients and 1 servers
	 * :cadance.canternet.org 265 ClientInDevelopment :Current Local Users: 82  Max: 104
	 * :cadance.canternet.org 266 ClientInDevelopment :Current Global Users: 767  Max: 782
	 * :cadance.canternet.org 396 ClientInDevelopment Pony-egk4at.ip49.fastwebnet.it :is now your displayed host
	*/
	/**
	 * @param userNameList
	 * @return
	 */
	private List<Person> toPersonList(List<String> input) {
			List<Person> result = new LinkedList<Person>();
			for(String str : input){
				result.add(new Person(str));
			}
		return result;
	}

	private String serverMessageHandler(String result){
		Comunication answer = ServerAnswerMessage.of(result);
		if(answer instanceof ErrorMessage){
			this.answers.addError((ErrorMessage)answer);
		}else if(answer instanceof MotdMessage){
			MotdMessage tmp = (MotdMessage) answer;
			this.answers.addModtLine(tmp);
		}else if(answer instanceof ServerAnswerMessage){
			this.answers.addUnknownMessage((ServerAnswerMessage) answer);
			if(answer instanceof SettingsMessage){
				this.settings.putAll(((SettingsMessage) answer).getSettings());
			}
		}
		return result;
	}

	private String topicChangeHandler(String line) throws BadHandlerException{
		//:nickName!realName@hostName TOPIC channel :newTopic
		if(!line.contains(" TOPIC")){
			throw new BadHandlerException();
		}
		Matcher m = Comunication.topic.matcher(line);
		m.find();
		String channelName = m.group(5).trim();
		String newTopic = m.group(6).trim();
		try{
			Corrispondence tmp = this.corrispondence.get(channelName);
			if(tmp instanceof Channel){
				((Channel) tmp).setTopic(newTopic);
			}else{
				throw new ChannelNotFoundException();
			}
		}catch(Exception e){
			return "Error in setting new Topic";
		}
		return "Changed topic in " + channelName + " into " + newTopic;
	}
	
	
	private String modeHandler(String line) throws BadHandlerException {
		//:chkrr00k!KiwiIRC@c.plus.plus MODE #can +o ClientInDevelopment 
		//:ClientInDevelopment!~Chkrr00ks@Rizon-E2679A21.ip96.fastwebnet.it MODE ClientInDevelopment :+ix
		//:chkrr00k!KiwiIRC@c.plus.plus MODE #can -b master!*@*
		//:OctaviaPone!WebChat@Pony-egk4at.ip49.fastwebnet.it MODE #rgb +vvh Rainb Pony_1 OctaviaPone
		//:OctaviaPone!WebChat@Pony-egk4at.ip49.fastwebnet.it MODE #rgb -n
		//:ClientInDevelopment!~Chkrr00ks@Rizon-480D1A65.ip49.fastwebnet.it MODE ClientInDevelopment :+ix
 		if(!line.contains(" MODE")){
			throw new BadHandlerException();
		}
 		Matcher m = Comunication.mode.matcher(line);
		m.find();
		String channel = m.group(5).trim();
		String mode = m.group(7).trim();
		String operation = m.group(6).trim();
		if(m.group(9) != null){
			String[] modedArray = m.group(9).trim().split(" ");
			int i = 0;
			for(String nickName : modedArray){
				if(!(this.corrispondence.size() == 0)){
					String currentMode = "" + mode.charAt(i);
					i++;
					if(PersonMode.isInModes(currentMode)){
						try{
							this.corrispondence.get(channel.toLowerCase()).adjustRank(nickName, operation+currentMode);
						}
						catch(NullPointerException nllp){
							System.err.println("ERROR IN FINDING CHANNEL " + channel + " additional info " + nickName+operation+currentMode);
						}
					}
				}else{
					System.out.println(line);
				}
			}
			return line;
		}else{
			if(this.corrispondence.containsKey(channel)){
				this.corrispondence.get(channel).changeMode(mode);
			}
			return line;
		}
	}

	@SuppressWarnings("deprecation")
	private String noticeHandler(String line) throws BadHandlerException{
		//:nickName!realName@hostName NOTICE yourNickName :message
		//:serverName NOTICE channel :message
		if(!line.contains(" NOTICE")){
			throw new BadHandlerException();
		}
		
		NoticeMessage message = new NoticeMessage(line);
		if(!message.isPrivateMessage()){
			if(this.corrispondence.containsKey(message.getChannel())){
				this.corrispondence.get(message.getChannel()).addMessage(message);
			}	
		}else{
			if(this.corrispondence.containsKey(message.getNickName().toLowerCase())){
				this.corrispondence.get(message.getNickName().toLowerCase()).addMessage(message);
			}else{
				PrivateMessage newPrivMsg = new PrivateMessage(message, this.user, this.pcl);
				this.corrispondence.put(newPrivMsg.getName().toLowerCase(), newPrivMsg);
				((PrivateMessage) this.corrispondence.get(newPrivMsg.getName())).newNoticeMessage();
			}
		}
		return message.toString();
	}
	
	private String nickHandler(String line) throws BadHandlerException{
		//:nickName!realName@hostName NICK newNickName
		
		if(!line.contains(" NICK")){
			throw new BadHandlerException();
		}
		if(line.startsWith(":")){
			Matcher m = Comunication.nick.matcher(line);
			m.find();
			String nickName = m.group(1).trim();
			String newNickName = m.group(5).trim();
			// XXX Fix this;
			Corrispondence tmp = null;
			for(String key : this.corrispondence.keySet()){
				tmp = this.corrispondence.get(key);
				if(tmp.changePerson(new Person(nickName), new Person(newNickName))){
					tmp.addMessage(new StatusMessage(nickName, StatusMessage.NICK, newNickName));
				}
			}
			if(this.debug){
				System.out.println("changed from all list " + nickName + " into " + newNickName);
			}
		}
		return line;
	}
	
	private String quitHandler(String line) throws BadHandlerException{
		//:nickName!realName@hostName QUIT :Ping timeout: 121 seconds
		if(!line.contains(" QUIT")){
			throw new BadHandlerException();
		}
		Matcher m = Comunication.quit.matcher(line);
		m.find();
		String nickName = "";
		String motivation = "";
		if(line.startsWith(":")){
			line = line.substring(1);
			nickName = m.group(1).trim();
			motivation = m.group(6).trim();
			// Fix this;
			for(String key : this.corrispondence.keySet()){
				try{
					if(this.corrispondence.get(key).removePerson(new Person(nickName))){
						this.corrispondence.get(key).addMessage(new StatusMessage(nickName, StatusMessage.QUIT, motivation));
					}
				}catch(Exception e){
					System.err.println("Error quit handler");
				}
			}
			if(this.debug){
				System.out.println("removed from all list " + nickName);
			}
		}
		return "";
	}
	
	/**
	 * @param result
	 * @return
	 */
	private String kickHandler(String line) throws BadHandlerException{
		//:chkrr00k!KiwiIRC@c.plus.plus KICK #can pablinho :Bye!
		if(!line.contains(" KICK")){
			throw new BadHandlerException();
		}
		if(line.startsWith(":")){
			Matcher m = Comunication.kick.matcher(line);
			m.find();
			String kickerNickName = m.group(1).trim();
			String channelName = "";
			String nickName = "";
			String motivation = "";
			channelName = m.group(5).trim();
			nickName = m.group(6).trim();
			if(m.group(8) != null){
				motivation = m.group(8).trim();
			}

			try{
				//FIXME this is ugly
				this.corrispondence.get(channelName.toLowerCase()).removePerson(new Person(nickName));
				//XXX externalize this;
				this.corrispondence.get(channelName.toLowerCase()).addMessage(new StatusMessage(nickName, StatusMessage.KICK, kickerNickName + " [" + motivation + "]"));
			}catch(Exception e){
				System.out.println("ERROR " + line);
			}
			if(this.debug){
				System.out.println(this.corrispondence.get(channelName.toLowerCase()).toString());
			}
		}
		return line;
	}

	/**
	 * @param result
	 * @return
	 * @throws BadHandlerException 
	 */
	//XXX remove this is a duplicate
	private String kickMeHandler(String line) throws BadHandlerException {
		//:chkrr00k!KiwiIRC@c.plus.plus KICK #can pablinho :Bye!
		if(!line.contains(" KICK")){
			throw new BadHandlerException();
		}
		if(line.startsWith(":")){
			Matcher m = Comunication.kick.matcher(line);
			m.find();
			String kickerNickName = m.group(1).trim();
			String channelName = "";
			String nickName = "";
			String motivation = "";
			channelName = m.group(5).trim();
			nickName = m.group(6).trim();
			if(m.group(8) != null){
				motivation = m.group(8).trim();
			}
			try{
				this.corrispondence.get(channelName.toLowerCase()).removePerson(nickName);
				//XXX externalize this;
				this.corrispondence.get(channelName.toLowerCase()).addMessage(new StatusMessage(nickName, StatusMessage.KICK, kickerNickName + " [" + motivation + "]"));
			}catch(Exception e){
				System.out.println("ERROR " + line);
			}
			if(this.debug){
				System.out.println(this.corrispondence.get(channelName.toLowerCase()).toString());
			}
			
		}
		return line;
	}

	private String partHandler(String line) throws BadHandlerException{
		//:nickName!realName@hostName PART channel :""
		//:nickName!realName@hostName PART channel
		if(!line.contains(" PART")){
			throw new BadHandlerException();
		}
		if(line.startsWith(":")){
			Matcher m = Comunication.part.matcher(line);
			m.find();
			String nickName = m.group(1).trim();
			String channelName = m.group(5).trim();
			String message = m.group(7);
			try{
				this.corrispondence.get(channelName.toLowerCase()).removePerson(nickName);
				if(message != null){
					this.corrispondence.get(channelName.toLowerCase()).addMessage(new StatusMessage(nickName, StatusMessage.PART, message.trim()));
				}else{
					this.corrispondence.get(channelName.toLowerCase()).addMessage(new StatusMessage(nickName, StatusMessage.PART));

				}
			}catch(Exception e){
				System.out.println("ERROR " + line);
			}
			if(this.debug){
				System.out.println(this.corrispondence.get(channelName.toLowerCase()).toString());
			}
		}
		return line;
	}

	private String joinHandler(String line) throws BadHandlerException, ChannelNotFoundException{
		//:nickName!realName@hostName JOIN :channel
		if(!line.contains(" JOIN")){
			throw new BadHandlerException();
		}
		if(line.startsWith(":")){
			Matcher m = Comunication.join.matcher(line);
			m.find();
			String nickName = m.group(1).trim();
			String channelName =  m.group(5).trim();
			Corrispondence tmp = this.corrispondence.get(channelName.toLowerCase());
			if(tmp instanceof Channel){
				((Channel) tmp).addPerson(new Person(nickName));
				//XXX externalize this;
				this.corrispondence.get(channelName.toLowerCase()).addMessage(new StatusMessage(nickName, StatusMessage.JOIN));
				if(this.debug){
					System.out.println(this.corrispondence.get(channelName.toLowerCase()).toString());
				}
			}else{
				throw new ChannelNotFoundException();
			}
		}
		return line;
		
	}
	
	private String joinRefusedHandler(String line) throws BadHandlerException{
		//:irc.losslessone.com 473 OctaviaPonesClient #tester :Cannot join channel (+i)
		//:irc.rizon.no 474 ClientInDevelopment #help :Cannot join channel (+b)
		//:irc.losslessone.com 477 ClientInDevelopment #4chan :You need to identify to a registered nick to join that channel. Check /msg nickserv help register
		
		if(!line.contains(" 473") && !line.contains(" 474") && !line.contains(" 477")){
			throw new BadHandlerException();
		}
		//XXX correct this
		String channelName = line.substring(line.indexOf("#"), line.indexOf(" :"));
		if(this.corrispondence.containsKey(channelName)){
			this.pcs.firePropertyChange(Channel.JOINREFUSED, channelName, "");
			this.corrispondence.remove(channelName);
		}
		if(this.debug){
			System.out.println("Refused channel");
		}
		return line;
	}
	
	private List<String> personListHandler(String line) throws BadHandlerException{
		
		/*
		:nickName!realName@hostName JOIN :channel
		:server 353 nickName = channel :userList
		:server 366 nickName channel :End of /NAMES list.
		*/

		List<String> result = new LinkedList<String>();
		if(line.contains(" 353")){
			String channelName = line.substring(line.indexOf("#"), line.indexOf(" :"));
			result.add(channelName.toLowerCase());
			//XXX i have no clue why this works
			line = line.substring(line.indexOf(":") + 1);
			line = line.substring(line.indexOf(":") + 1);
			StringTokenizer tok = new StringTokenizer(line, " ");
			String tmp = "";
			int users = tok.countTokens();
			for(int i = 0; i < users; i++){
				tmp = tok.nextToken().trim();
				result.add(tmp);
			}
		}else if(!line.contains(" 366")){
			throw new BadHandlerException();
		}
		return result;
	}

	/**
	 * @param result
	 * @throws BadHandlerException 
	 */
	private String joinMeHandler(String line) throws BadHandlerException {
		//:nickName!realName@hostName JOIN :channel
		
		if(!line.contains(" JOIN")){
			throw new BadHandlerException();
		}
		if(line.startsWith(":")){
			Matcher m = Comunication.join.matcher(line);
			m.find();
			String channelName =  m.group(5).trim();
			Channel newChan = new Channel(channelName.toLowerCase(), user, pcl);
			if(!this.corrispondence.containsKey(channelName.toLowerCase())){
				this.corrispondence.put(channelName.toLowerCase(), newChan);
			}
		}
		return line;
		
	}

	private String topicHandler(String line) throws BadHandlerException, ChannelNotFoundException{
		//server 332 nickName channel :messageOfTheDay
		//TODO server 333 nickName channel setterNickName setTime
		String topic = "";
		if(line.contains(" 332")){
			Matcher m = Comunication.genericEx(332).matcher(line);
			m.find();
			String channelName =  m.group(4).trim();
			if(m.group(5) != null){
				topic = m.group(5).trim();
			}
			Corrispondence tmp = this.corrispondence.get(channelName.toLowerCase());
			if(tmp instanceof Channel){
				((Channel) tmp).setTopic(topic);
			}else{
				throw new ChannelNotFoundException();
			}
		}else if(line.contains(" 333")){
			return "";
		}else{
			throw new BadHandlerException();
		}
		return topic;
	}
	
	private String messageHandler(String line) throws BadHandlerException, ChannelNotFoundException{
		/*
		 * :nickName!realName@hostName PRIVMSG channel :message
		 * 
		 * 1) realName 		-your name
		 * 2) nickName 		-the name that you display (can be changed)
		 * 3) hostName 		-hash of your IP or VHost
		 * 4) channel  		-the channel in which the message was sent (doesn't apply for PM TODO fix this)
		 * 4 bis) sender	-the one that sent you the message (NOT YET IMPLEMENTED) TODO implement 
		 * 5) message		-the message
		 * 6) time			-when the message was sent (NOT YET IMPLEMENTED)
		 */
		if(!line.contains("PRIVMSG")){
			throw new BadHandlerException();
		}
		Message message = null;
		if(line.contains("" + (char)(0x01))){
			System.out.println("CTCP request/action");			
			if(line.contains((char)(0x01) + "ACTION ")){
				message = new ActionMessage(line);
			}else{
				if(line.contains((char)(0x01) + "VERSION")){
					message = new CTCPVersionMessage(line);
				}else if(line.contains((char)(0x01) + "PING")){
					message = new CTCPPingMessage(line);
				}else if(line.contains((char)(0x01) + "TIME")){
					message = new CTCPTimeMessage(line);
				}else{
					System.err.println("Error on parsing CTCP request");
					return "";
				}
				try {
					this.write(((CTCPMessage) message).answer());
					return "";
				} catch (IOException e) {
					e.printStackTrace();
				}
			}			
		}else{
			message = new Message(line);
		}
		if(!message.isPrivateMessage()){
			if(this.corrispondence.containsKey(message.getChannel())){
				this.corrispondence.get(message.getChannel()).addMessage(message);
			}		
		}else{
			if(this.corrispondence.containsKey(message.getNickName())){
				this.corrispondence.get(message.getNickName()).addMessage(message);
			}else{
				PrivateMessage newPrivMsg = new PrivateMessage(message, this.user, this.pcl);
				this.corrispondence.put(newPrivMsg.getName(), newPrivMsg);
				((PrivateMessage) this.corrispondence.get(newPrivMsg.getName())).newPrivateMessage();
			}
		}
		return message.toString();
	}
	
	private StringBuilder connectionErrorHandler() throws Exception{
		String line = null;
		StringBuilder strBld = new StringBuilder();
	
			while ((line = server.read()) != null) {
				
				strBld.append(line + "\n");
				
				if (line.indexOf("004") >= 0) {				
					break;
				}
				else if(line.startsWith("PING")){
					this.pingHandler(line);
				}
				else if (line.indexOf("433") >= 0) {
					throw new Exception("Nickname is already in use.");
				}
				else if (line.indexOf("401") >= 0) {
					throw new Exception("No nickname and/or channel");
				}
				else if (line.indexOf("402") >= 0) {
					throw new Exception("No such server");
				}
				else if (line.indexOf("403") >= 0) {
					throw new Exception("No channel");
				}
				else if (line.indexOf("404") >= 0) {
					throw new Exception("Cannot send to channel");
				}
				else if (line.indexOf("405") >= 0) {
					throw new Exception("Too many channel");
				}
				else if (line.indexOf("412") >= 0) {
					throw new Exception("No text sent");
				}
				else if (line.indexOf("421") >= 0) {
					throw new Exception("Unknow command");
				}
				else if (line.indexOf("431") >= 0) {
					throw new Exception("No nickname given");
				}
				else if (line.indexOf("403") >= 0) {
					throw new Exception("No channel");
				}
				else if (line.indexOf("436") >= 0) {
					throw new Exception("Nickname collision");
				}
				else if (line.indexOf("442") >= 0) {
					throw new Exception("Not on that channel");
				}
				else if (line.indexOf("451") >= 0) {
					throw new Exception("Not registered");
				}
				else if (line.indexOf("461") >= 0) {
					throw new Exception("Not enough parameters");
				}
				else if (line.indexOf("464") >= 0) {
					throw new Exception("Password incorrect");
				}
			}
			return strBld;
	
	}

	public String pingHandler(String line) throws IOException{		
		// :93-44-93-103.ip96.fastwebnet.it PONG 93-44-93-103.ip96.fastwebnet.it :weber.freenode.net
		server.write("PONG " + line.substring(6).trim() + "\r\n");
		server.flush();
		return "PONG " + line.substring(6).trim() + "\r\n";

	}
	
	public String connect(String realName, String nickName) throws Exception{
		
		String identity = realName;
		try{
			server.write("NICK " + nickName + "\r\n");
			server.write("USER " + identity + " 0 ayy.lmao :" + realName + "\r\n");
			server.flush();
		}catch(Exception e){
			throw new IOException("Unable to connecting to the server");
		}
		//this.myNickName = nickName;
		this.connected = true;
		StringBuilder strBld = this.connectionErrorHandler();
		
		return strBld.toString();
		
	}
	
	public void disconnect() throws IOException{
		if(this.connected){
			server.write("QUIT :\"developing an IRC client is hard AF\"\r\n");
			server.flush();
			server.close();
			this.connected = false;
		} 
	}

	/**
	 * @return
	 */
	public boolean isConnect() {
		return this.connected;
	}



}
