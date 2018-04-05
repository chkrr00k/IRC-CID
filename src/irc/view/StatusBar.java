/**
 * 
 */
package irc.view;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chkrr00k
 *
 */
public class StatusBar {
	private Map<String, Integer> messages;

	public StatusBar() {
		this.messages = new HashMap<String, Integer>();
	}
	
	public void addMessage(String channelName){
		if(this.messages.containsKey(channelName)){
			int tmp = this.messages.get(channelName).intValue() + 1;
			this.messages.remove(channelName);
			this.messages.put(channelName, tmp);
		}
	}
	public void addChannel(String channelName){
		this.messages.put(channelName, 0);
	}
	public void readMessage(String channelName){
		this.messages.remove(channelName);
		this.messages.put(channelName, 0);
	}
	public void removeChannel(String channelName){
		this.messages.remove(channelName);
	}

	@Override
	public String toString() {
		String result = "";
		for(String k : this.messages.keySet()){
			if(this.messages.get(k) > 0){
				result += (result.isEmpty()? "" : ", ") + k + "(" + this.messages.get(k) + ")";
			}
		}
		return result.isEmpty() ? "" : "New messages " + result;
	}
	
	
}
