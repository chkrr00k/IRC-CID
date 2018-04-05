/**
 * 
 */
package irc.model;

import java.util.Comparator;

/**
 * @author chkrr00k
 *
 */
public class PersonMode extends Mode implements Comparator<PersonMode>, Comparable<PersonMode>{

	public final static String MODES ="qaohvb";
	
	public PersonMode(PersonModality mode) {
		super(mode.getMode());
	}
	public PersonMode(String mode) {
		super(mode);
	}
	public PersonMode() {
	}

	public boolean setPersonMode(String nickName){
		if(nickName.startsWith("@")){
			this.mode = "o";
		}else if(nickName.startsWith("+")){
			this.mode = "v";
		}else if(nickName.startsWith("%")){
			this.mode = "h";
		}else if(nickName.startsWith("~")){
			this.mode = "q";
		}else if(nickName.startsWith("&")){
			this.mode = "a";
		}else{
			return false;
		}
		return true;
	}

	public int getStatus(){
		int status = 0;
		if(this.mode.contains("v")){
			status = 1;
		}
		if(this.mode.contains("h")){
			status = 2;
		}
		if(this.mode.contains("o")){
			status = 3;
		}
		if(this.mode.contains("a")){
			status = 4;
		}
		if(this.mode.contains("q")){
			status = 5;
		}
		return status;
	}
	
	public boolean canPost(){
		return !super.mode.contains("b");
	}

	/* (non-Javadoc)
	 * @see irc.model.Mode#apply()
	 */

	@Override
	public int compare(PersonMode o1, PersonMode o2) {
		int result = 0;
		if(o1.getStatus() < o2.getStatus()){
			result = 1;
		}else if(o1.getStatus() > o2.getStatus()){
			result = -1;
		}
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(PersonMode o) {
		int result = 0;
		if(this.getStatus() < o.getStatus()){
			result = 1;
		}else if(this.getStatus() > o.getStatus()){
			result = -1;
		}
		return result;
	}
	@Override
	public String toString() {
		String symbol = "";
		if(this.mode.contains("v")){
			symbol = "+";
		}
		if(this.mode.contains("h")){
			symbol = "%";
		}
		if(this.mode.contains("o")){
			symbol = "@";
		}
		if(this.mode.contains("a")){
			symbol = "&";
		}
		if(this.mode.contains("q")){
			symbol = "~";
		}
		return symbol;
	}

	public static boolean isInModes(String mode){
		return PersonMode.MODES.contains(mode);
	}

}
