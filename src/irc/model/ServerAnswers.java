/**
 * 
 */
package irc.model;

import java.beans.PropertyChangeSupport;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author chkrr00k
 *
 */
public class ServerAnswers {
	private Queue<ErrorMessage> errors;
	private Queue<MotdMessage> motd;
	
	private Queue<ServerAnswerMessage> unknown;
	private PropertyChangeSupport pcs;
	
	public ServerAnswers(PropertyChangeSupport pcs) {
		this.errors = new LinkedList<ErrorMessage>();
		this.motd = new LinkedList<MotdMessage>();
		this.unknown = new LinkedList<ServerAnswerMessage>();
		this.pcs = pcs;
	}
	
	public void addError(ErrorMessage error){
		this.errors.add(error);
		this.pcs.firePropertyChange(ServerAnswerMessage.ERROR, null, null);
	}
	public void addModtLine(MotdMessage motd){
		//this.motd.add(motd);
		if(motd.isFirst()){
		}else if(motd.isLast()){
			this.pcs.firePropertyChange(ServerAnswerMessage.MOTD, null, null);
		}else{
			this.motd.add(motd);
		}
	}
	public void addUnknownMessage(ServerAnswerMessage unk){
		this.unknown.add(unk);
		this.pcs.firePropertyChange(ServerAnswerMessage.UNKNOWN, null, null);
	}
	
	public ErrorMessage getFirstError(){
		return this.errors.poll();
	}/*
	public ErrorMessage peekFirstError(){
		return this.errors.peek();
	}*/
	public Queue<MotdMessage> getMotd(){
		return this.motd;
	}
	public ServerAnswerMessage getUnknownAnswer(){
		return this.unknown.poll();
	}
	
}
