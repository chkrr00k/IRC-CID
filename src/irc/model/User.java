/**
 * 
 */
package irc.model;

/**
 * @author chkrr00k
 *
 */
public class User extends Person{
	//private String nickName;
	//private String realName;
	private String password;
	
	



	public User(String nickName, String realName, String hostName, String rank) {
		super(nickName, realName, hostName, rank);
	}
	public User(String nickName, String rank) {
		super(nickName, rank);
	}
	public User() {
	}
	public User(String nickName, String realName, String rank) {
		super(nickName, realName, rank);
	}
	public User(String line) {
		super(line);
	}

	
	
}
