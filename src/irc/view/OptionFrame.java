/**
 * 
 */
package irc.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import irc.controller.IRCHandler;

/**
 * @author chkrr00k
 *
 */
public class OptionFrame extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	private JTextField serverName;
	private JTextField nickName;
	private JTextField realName;
	private JButton enter;
	private JButton close;
	//private boolean remember;
	
	public OptionFrame(String title, String oldNickName, String oldRealName, String oldServer, boolean remember) throws HeadlessException {
		super(title);
		if(remember){
			this.startIRC(oldNickName, oldRealName, oldServer);
			this.dispose();
		}else{		
			JLabel lblServerName = new JLabel("Server name:");
			this.serverName = new JTextField(oldServer);
			this.serverName.setMaximumSize(new Dimension(2000, 20));
			JPanel server = new JPanel(/*new BorderLayout()*/);
			server.setLayout(new BoxLayout(server, BoxLayout.X_AXIS));
			server.add(lblServerName/*, BorderLayout.WEST*/);
			server.add(this.serverName/*, BorderLayout.EAST*/);
			
			JLabel lblNickName = new JLabel("Nickname:");
			this.nickName = new JTextField(oldNickName);
			this.nickName.setMaximumSize(new Dimension(2000, 20));
			JLabel lblRealName = new JLabel("Realname:");
			this.realName = new JTextField(oldRealName);
			this.realName.setMaximumSize(new Dimension(2000, 20));
			JPanel name = new JPanel(/*new GridLayout(1, 2)*/);
			name.setLayout(new BoxLayout(name, BoxLayout.X_AXIS));
			name.add(lblNickName/*, BorderLayout.WEST*/);
			name.add(this.nickName/*, BorderLayout.EAST*/);
			name.add(lblRealName/*, BorderLayout.WEST*/);
			name.add(this.realName/*, BorderLayout.EAST*/);
			
			this.enter = new JButton("Ok");
			this.close = new JButton("Close");
			this.enter.addActionListener(this);
			this.close.addActionListener(this);
			JPanel buttons = new JPanel();
			buttons.setLayout(new FlowLayout()/*new BoxLayout(buttons, BoxLayout.X_AXIS)*/);
			buttons.add(this.enter);
			buttons.add(this.close);
			
			
			JPanel universe = new JPanel();
			universe.setLayout(new BoxLayout(universe, BoxLayout.Y_AXIS));
			universe.add(server);
			universe.add(name);
			
			universe.add(buttons, BorderLayout.SOUTH);
			
			this.add(universe);
			this.setVisible(true);
			//this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			this.setSize(new Dimension(400, 200));
		}
	}
	private IRCHandler getNewIRC(/*String oldNickName, String oldRealName, String oldServer*/){
		this.setVisible(true);
		return new IRCHandler(this.nickName.getText(), this.realName.getText(), this.serverName.getText(), 6667, false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(this.enter)){
			this.startIRC();
			this.dispose();
		}
		if(e.getSource().equals(this.close)){
			System.exit(0);
		}
	}

	
	/**
	 * 
	 */
	private void startIRC() {
		IRCHandler ircH = this.getNewIRC();
		Frame frame = new Frame("IRC:CID", ircH);
		ircH.setPcl(frame);
		Thread th = new Thread(ircH, "IRC reader");
		th.start();
		frame.setVisible(true);
	}
	
	private void startIRC(String nickName, String realName, String server) {
		IRCHandler ircH = new IRCHandler(nickName, realName, server, 6667, false);
		Frame frame = new Frame("IRC:CID", ircH);
		ircH.setPcl(frame);
		Thread th = new Thread(ircH, "IRC reader");
		th.start();
		

		frame.setVisible(true);
		
	}

	public static void main(String[] args){
		OptionFrame opt = new OptionFrame("test", "ClientInDevelopment", "Chkrr00ksPropriety", "irc.rizon.net", false);
	}
	
}
