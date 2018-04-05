/**
 * 
 */
package irc.model;

import java.util.Comparator;

/**
 * @author chkrr00k
 *
 */
public class Person implements Comparator<Person>{

	private String nickName;
	private String realName;
	private String hostName;
	//private String rank;
	private PersonMode rank;
	
	/*
	 *:chkrr00k!KiwiIRC@c.plus.plus MODE #can +o ClientInDevelopment 
	 *:chkrr00k!KiwiIRC@c.plus.plus MODE #can -o ClientInDevelopment
	 *:chkrr00k!KiwiIRC@c.plus.plus MODE #can +v ClientInDevelopment
	 *:chkrr00k!KiwiIRC@c.plus.plus MODE #can -v ClientInDevelopment
	 *:chkrr00k!KiwiIRC@c.plus.plus MODE #can +h ClientInDevelopment
	 *:chkrr00k!KiwiIRC@c.plus.plus MODE #can -h ClientInDevelopment
	 *
	 */
	
	public Person() {
		this.nickName = "";
		this.realName = "";
		this.hostName = "";
		this.rank = new PersonMode();
	}
	public Person(String nickName, String rank) {
		this.nickName = nickName;
		this.rank = new PersonMode(rank);
		this.hostName = "";
		this.realName = "";
	}
	public Person(String nickName, String realName, String rank) {
		this.nickName = nickName;
		this.realName = realName;
		this.hostName = "";
		this.rank =  new PersonMode(rank);
	}
	public Person(String nickName, String realName, String hostName, String rank) {
		this.nickName = nickName;
		this.realName = realName;
		this.hostName = hostName;
		this.rank =  new PersonMode(rank);
	}
	public Person(String nickName, String realName, String hostName, PersonMode rank) {
		this.nickName = nickName;
		this.realName = realName;
		this.hostName = hostName;
		this.rank = rank;
	}

	
	public Person(String nickName) {
		//this.nickName = nickName;
		this.rank =  new PersonMode();
		/*
		if(nickName.startsWith("@")){
			this.nickName = nickName.substring(1);
			this.rank = "o";
		}else if(nickName.startsWith("+")){
			this.nickName = nickName.substring(1);
			this.rank = "v";
		}else if(nickName.startsWith("%")){
			this.nickName = nickName.substring(1);
			this.rank = "h";
		}else if(nickName.startsWith("~")){
			this.nickName = nickName.substring(1);
			this.rank = "q";
		}else if(nickName.startsWith("&")){
			this.nickName = nickName.substring(1);;
			this.rank = "a";
		}else{
			this.nickName = nickName;
		}*/
		if(rank.setPersonMode(nickName)){
			this.nickName = nickName.substring(1);
		}else{
			this.nickName = nickName;
		}
		this.realName = "";
		this.hostName = "";
		//this.rank = "";
	}

	public String getNickName() {
		return nickName;
	}
	public String getRealName() {
		return realName;
	}
	public String getHostName() {
		return hostName;
	}
	public PersonMode getRank() {
		return rank;
	}
	
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public boolean adjustRank(String rank) throws BadHandlerException{
		return this.rank.adjustMode(rank);
		/*
		if(rank.length() != 2){
			return false;
		}
		String factor = rank.substring(1);
		if(rank.startsWith("+")){
			if(this.rank.contains(factor)){
			}else{
				this.rank += factor;
			}
		}else if(rank.startsWith("-")){
			if(!this.rank.contains(factor)){
			}else{
				this.rank = this.rank.replace(factor, "");
			}
		}else{
			throw new BadHandlerException();
		}
		return true;
		*/
	}
	/**
	 * @param md
	 * @throws BadHandlerException 
	 */
	public void adjustRank(PersonModality md) throws BadHandlerException {
		this.adjustRank(md.getMode());
		
	}
	/*
	public void setRank(String rank) {
		this.rank = rank;
	}*/
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	/*
	public int getStatus(){
		int status = 0;
		if(this.rank.contains("v")){
			status = 1;
		}
		if(this.rank.contains("h")){
			status = 2;
		}
		if(this.rank.contains("o")){
			status = 3;
		}
		if(this.rank.contains("a")){
			status = 4;
		}
		if(this.rank.contains("q")){
			status = 5;
		}
		return status;
	}*/
	
	@Override
	public int compare(Person o1, Person o2) {
		if(o1.rank.compareTo(o2.rank) == 0){
			return (o1.getNickName().toLowerCase()).compareTo(o2.getNickName().toLowerCase());
		}else{
			return o1.rank.compareTo(o2.rank);
		}
	}
	
	public boolean canPost(){
		return this.rank.canPost();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (nickName == null) {
			if (other.nickName != null)
				return false;
		} else if (!nickName.equals(other.nickName))
			return false;
		return true;
	}
	public boolean equals(User obj) {
			if (!nickName.equals(obj.getNickName()))
			return false;
		return true;
	}
	@Override
	public String toString() {
		String symbol = this.rank.toString();
		/*
		if(this.rank.contains("v")){
			symbol = "+";
		}
		if(this.rank.contains("h")){
			symbol = "%";
		}
		if(this.rank.contains("o")){
			symbol = "@";
		}
		if(this.rank.contains("a")){
			symbol = "&";
		}
		if(this.rank.contains("q")){
			symbol = "~";
		}*/
		return symbol + nickName;
	}


	
	
	
}
