/**
 * 
 */
package irc.model;

import java.beans.PropertyChangeSupport;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author chkrr00k
 *
 */
public abstract class Corrispondence {

	protected List<Comunication> comunications;
	protected List<Comunication> newMessages;
	protected List<Person> persons;
	protected User user;
	protected PropertyChangeSupport chkChang = new PropertyChangeSupport(this);
	protected String name;

	/**
	 * 
	 */
	public Corrispondence() {
		super();
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
	/**
	 * @return
	 */
	public List<Comunication> getListMessages() {
		this.comunications.addAll(this.newMessages);
		List<Comunication> tmp = new LinkedList<Comunication>();
		tmp.addAll(this.newMessages);
		this.newMessages.clear();
		return tmp;
	}

	public abstract List<Person> getPersons();
	
	public abstract void addMessage(Comunication input);

	public abstract boolean changePerson(Person oldNick, Person newNick);

	public abstract boolean removePerson(Person input);
	
	public abstract boolean removePerson(String input);

	public abstract String getName();

	public abstract String send(String message, String myNick) throws PostException;
	
	public abstract String sendAction(String message, String myNick) throws PostException;
	
	public abstract String sendNotice(String message, String myNick) throws PostException;
	
	public abstract String join();
	
	public abstract boolean adjustRank(String nickName, String rank);
	

	/**
	 * @param nickName
	 */
	public Person getPerson(String nickName) {
		Person tmp = new Person(nickName);
		for(Person p : this.persons){
			if(p.equals(tmp)){
				return p;
			}
		}
		throw new NoSuchElementException();
	}

	/**
	 * @param mode
	 */
	public void changeMode(String mode) {
		this.addMessage(new StatusMessage("", StatusMessage.CHMO, mode));
		
	}

	/**
	 * @param md
	 * @return
	 * @throws BadHandlerException 
	 *//*
	public String addMode(Modality md) throws IllegalArgumentException, BadHandlerException {
			for(Person p : this.persons){
				if(p.equals(md.getPerson())){
					p.adjustRank(md);
					return md.apply();
			}
			
		}
			throw new IllegalArgumentException();
	}*/

}