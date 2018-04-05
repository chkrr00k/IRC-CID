/**
 * 
 */
package irc.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;

import irc.controller.IRCHandler;
import irc.model.Channel;
import irc.model.Comunication;
import irc.model.Irc;
import irc.model.Message;
import irc.model.PrivateMessage;
import irc.model.ServerAnswerMessage;
import irc.model.StatusMessage;

/**
 * @author chkrr00k
 *
 */
public class Frame extends JFrame implements ActionListener, PropertyChangeListener{

	private static final long serialVersionUID = 1L;
	
	private JTextArea chatPrs;
	private JTextArea chatMsg;
	private JTextField inputMsg;
	private JTextField inputCmd;
	private JComboBox<String> chanSelector;
	private IRCHandler irc;
	private StatusBar statusBar;
	private JButton option;
	private JButton part;
	private JButton quit;
	private StringBuilder status = new StringBuilder();
	private boolean inizialized;
	
	public Frame(String title, IRCHandler irc) throws HeadlessException {
		super(title);
		this.statusBar = new StatusBar();
		this.irc = irc;
		this.inizialized = false;

		JPanel upper = new JPanel(new GridLayout(1, 2));
		JPanel lefter = new JPanel(new BorderLayout());
		JPanel downer = new JPanel(new BorderLayout());	
		JPanel stater = new JPanel(new BorderLayout());
		
		JPanel menu = new JPanel(/*new FlowLayout(FlowLayout.LEFT)*/);
		menu.setLayout(new BoxLayout(menu, BoxLayout.X_AXIS));
		
		this.chatMsg = new JTextArea();
		this.chatMsg.setEditable(false);
		this.chatMsg.setBackground(Color.cyan);
		this.chatMsg.setLineWrap(true);
		this.chatMsg.setFocusable(false);
		this.chatMsg.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
		this.chatMsg.setWrapStyleWord(true);
		
		DefaultCaret caret = (DefaultCaret)this.chatMsg.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		this.chatPrs = new JTextArea();
		this.chatPrs.setEditable(false);
		this.chatPrs.setBackground(Color.cyan);
		this.chatPrs.setLineWrap(true);
		this.chatPrs.setFocusable(false);
		this.chatPrs.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		//this.chatPrs.setWrapStyleWord(true);
		
		DefaultCaret caret2 = (DefaultCaret)this.chatPrs.getCaret();
		caret2.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		
		this.inputMsg = new JTextField();
		this.inputMsg.addActionListener(this);
		this.inputCmd = new JTextField();
		this.inputCmd.setEnabled(true);
		this.inputCmd.setEditable(false);
		//this.inputCmd.addActionListener(this);
		this.quit = new JButton("Quit");
		this.quit.addActionListener(this);
		this.option = new JButton("Option");
		this.option.addActionListener(this);
		
		this.part = new JButton("Part");
		this.part.addActionListener(this);
		menu.add(option);
		menu.add(quit);
		
		JScrollPane chatMsgSP = new JScrollPane(this.chatMsg);
		JScrollPane chatPrsSP = new JScrollPane(this.chatPrs);
		chatPrsSP.setAutoscrolls(true);
		chatMsgSP.setAutoscrolls(true);
		chatPrsSP.setPreferredSize(new Dimension(150, HEIGHT));
		
		this.chanSelector = new JComboBox<String>();
		this.chanSelector.addActionListener(this);
		this.chanSelector.setEditable(true);
		this.chanSelector.addItem("Status");
		/*
		for(String ch : this.irc.irc.getChannels()){
		 this.chanSelector.addItem(ch);
		}*/
		
		// chat quit and text input
		downer.add(this.part, BorderLayout.EAST);
		downer.add(this.inputMsg);
		downer.add(this.chanSelector, BorderLayout.WEST);
		
		// downer and cmd
		stater.add(downer, BorderLayout.NORTH);
		stater.add(inputCmd);
		
		// chat and input
		lefter.add(chatMsgSP, BorderLayout.CENTER);
		lefter.add(chatPrsSP, BorderLayout.EAST);
		lefter.add(stater, BorderLayout.SOUTH);
		
		// chat and status windows
		upper.add(lefter);
		//upper.add(chatPrsSP);
		
		// all the rest
		this.add(menu, BorderLayout.NORTH);
		this.add(upper);
		this.setSize(800, 600);
		//this.pack();
		//this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}


	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	
	@SuppressWarnings("unused")
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Object toBeChannelName = this.chanSelector.getSelectedItem();
		if(toBeChannelName == null){
			toBeChannelName = "";
		}
		String channelName = ((String)toBeChannelName).trim();
		if(evt.getPropertyName().equals(Channel.NEWMESSAGE) || evt.getPropertyName().equals(PrivateMessage.NEWMESSAGE)){
			if(((String)(evt.getOldValue())).equals(channelName)){
				//this.chatMsg.append(((Comunication)(evt.getNewValue())).toString());	
				this.chatMsg.append(this.irc.getNewMessages(channelName));
			}/*else{
				String name = (String)(evt.getOldValue());
				boolean found = false;
				for (int i = 0; i < this.chanSelector.getItemCount(); i++) {
					if(this.chanSelector.getItemAt(i).equals(name)){
						found = true;
					}
				}
				if(!found){
					this.chanSelector.addItem(name);
				}
			}*/else{
				if(evt.getNewValue() instanceof Message){
					this.statusBar.addMessage((String)evt.getOldValue());
					this.inputCmd.setText(this.statusBar.toString());
				}
			}
		}
		if(evt.getPropertyName().equals(Channel.ALLPERSON)){
			if(((String)(evt.getOldValue())).equals(channelName)){
				this.showPeople((String)evt.getOldValue());
			}
		}
		if(evt.getPropertyName().equals(Channel.NEWPERSON)){
			if(((String)(evt.getOldValue())).equals(channelName)){
				this.showPeople((String)evt.getOldValue());
				//this.chatMsg.append((String)evt.getNewValue() + " has joined\n");
			}
		}
		if(evt.getPropertyName().equals(Channel.PARPERSON) || evt.getPropertyName().equals(PrivateMessage.PARPERSON)){
			if(((String)(evt.getOldValue())).equals(channelName)){
				this.showPeople((String)evt.getOldValue());
				//this.chatMsg.append((String)evt.getNewValue() + " has quit\n");
			}
		}
		if(evt.getPropertyName().equals(Channel.NEWTOPIC)){
			if(((String)(evt.getOldValue())).equals(channelName)){
				;
			}
		}
		if(evt.getPropertyName().equals(Channel.CHAPERSON)){
			if(((String)(evt.getOldValue())).equals(channelName)){
				this.showPeople((String)evt.getOldValue());
				//this.chatMsg.append((String)evt.getNewValue() + " has changed nick\n");
			}
		}/*
		if(evt.getPropertyName().equals(PrivateMessage.CHAPERSON)){
			try{
				this.chanSelector.removeItem((String)(evt.getOldValue()));
				this.chanSelector.addItem((String)(evt.getNewValue()));
			}catch(NoSuchElementException e){
				System.err.println(("CHAPERSON failed to remove " + ((String)(evt.getOldValue())).equals(channelName)));
			}
			
		}*/
		if(evt.getPropertyName().equals(PrivateMessage.NEWPRIVATEMESSAGE)){
			if(true){
				this.chanSelector.addItem((String) evt.getOldValue());
				//TODO using status bar for this
				if(this.inputCmd.getText().contains("New private message form")){
					this.inputCmd.setText(this.inputCmd.getText() + " and " + evt.getOldValue());
				}else{
					this.inputCmd.setText("New private message form " + evt.getOldValue());
				}
				//here
				/*this.statusBar.addChannel((String)evt.getOldValue());
				this.inputCmd.setText(this.statusBar.toString());*/
				System.out.println("New PRIVMSG");
			}else{
				//TODO avoid PRIVMSG;
			}
		}
		if(evt.getPropertyName().equals(Channel.OPSPERSON)){
			if(((String)(evt.getOldValue())).equals(channelName)){
				this.showPeople((String)evt.getOldValue());
			}
		}
		if(evt.getPropertyName().equals(Channel.JOINREFUSED)){
			this.chanSelector.removeItem(evt.getOldValue());
			this.inputCmd.setText("Join to " + evt.getOldValue() + " refused (" + evt.getNewValue() + ")");
		}
		if(evt.getPropertyName().equals(ServerAnswerMessage.MOTD)){
			this.status.append(this.irc.getMotd());
			if(this.chanSelector.getSelectedItem().equals("Status")){
				this.chatMsg.append(this.status.toString());
			}
			this.inizialized = true;
		}
		if(evt.getPropertyName().equals(ServerAnswerMessage.ERROR)){
			if(/*this.inizialized*/ true){/*
				this.status.append(this.irc.getLastError());
				if(this.chanSelector.getSelectedItem().equals("Status")){
					this.chatMsg.append(this.status.toString());
				}*/
				if(this.chanSelector.getSelectedItem().equals("Status")){
					this.status.append(this.irc.getFirstError());
					//this.chatMsg.append(this.status.toString());
				}else{
					this.irc.addMessage((String)this.chanSelector.getSelectedItem(), this.irc.getFirstError());
				}
			}else{
				System.err.println(this.irc.getFirstError());
			}
		}
		if(evt.getPropertyName().equals(ServerAnswerMessage.UNKNOWN)){
			
			if(this.chanSelector.getSelectedItem().equals("Status")){
				this.status.append(this.irc.getUnknownAnswer());
				//this.chatMsg.append(this.status.toString());
			}else{
				this.irc.addMessage((String)this.chanSelector.getSelectedItem(), this.irc.getUnknownAnswer());
			}
		}
			
		if(evt.getPropertyName().equals(PrivateMessage.NEWNOTICEMESSAGE)){
			if(true){
				System.out.println("New NOTICE");
			}else{
				//TODO avoid PRIVMSG;
			}
		}
		if(evt.getPropertyName().equals(PrivateMessage.NEWNOTICE)){
			this.status.append(this.irc.getNewMessages((String)evt.getOldValue()));
			if(this.chanSelector.getSelectedItem().equals("Status")){
				this.chatMsg.setText(this.status.toString());
			}
		}
		if(evt.getPropertyName().equals(Channel.NEWCHANNEL)){
			String newChannelName = (String) evt.getOldValue();
			boolean found = false;
			for (int i = 0; i < this.chanSelector.getItemCount(); i++) {
				if(this.chanSelector.getItemAt(i).equals(newChannelName.toLowerCase())){
					found = true;
				}
			}
			if(!found){
				this.chatMsg.setText("");
				this.chanSelector.addItem(newChannelName);
				this.showPeople(newChannelName);
				this.statusBar.addChannel(newChannelName);
				this.inputCmd.setText(this.statusBar.toString());
			}
		}
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(this.chanSelector)){
			String channelName = (String)this.chanSelector.getSelectedItem();
			if(channelName == null){
				this.chatMsg.setText("");
				this.chatPrs.setText("");
			}else if(channelName.startsWith("#")){
				if(this.chanSelector.getItemCount() == 0){
					this.chatMsg.setText("");
					this.irc.joinChannel(channelName, this);
					this.chanSelector.addItem(channelName);
					this.showPeople(channelName);
					
					
					this.statusBar.addChannel(channelName);
					this.inputCmd.setText(this.statusBar.toString());
				}else{
					boolean found = false;
					for (int i = 0; i < this.chanSelector.getItemCount(); i++) {
						if(this.chanSelector.getItemAt(i).equals(channelName)){
							found = true;
						}
					}
					if(found){
						this.chatMsg.setText("");
						this.showMessages(channelName);
						this.showPeople(channelName);
						
						this.statusBar.readMessage(channelName);
						this.inputCmd.setText(this.statusBar.toString());
					}else{
						/*this.chatMsg.setText("");*/
						this.irc.joinChannel(channelName, this);
						/*this.chanSelector.addItem(channelName);
						this.showPeople(channelName);
						
						
						this.statusBar.addChannel(channelName);
						this.inputCmd.setText(this.statusBar.toString());*/
					}
				}
			}else{
				if(this.chanSelector.getItemCount() == 0){
					this.chatMsg.setText("");
					this.irc.startPrivateMessage(channelName, this);
					this.chanSelector.addItem(channelName);
					this.showPeople(channelName);
					

					this.statusBar.addChannel(channelName);
					this.inputCmd.setText(this.statusBar.toString());
				}else{
					boolean found = false;
					for (int i = 0; i < this.chanSelector.getItemCount(); i++) {
						if(this.chanSelector.getItemAt(i).equals(channelName)){
							found = true;
						}
					}
					if(found && !channelName.equals("Status")){
						this.chatMsg.setText("");
						this.showMessages(channelName);
						this.showPeople(channelName);
						
						this.statusBar.readMessage(channelName);
						this.inputCmd.setText(this.statusBar.toString());
					}else if(!channelName.equals("Status")){
						this.chatMsg.setText("");
						this.irc.startPrivateMessage(channelName, this);
						this.chanSelector.addItem(channelName);
						this.showPeople(channelName);
						
						this.statusBar.addChannel(channelName);
						this.inputCmd.setText(this.statusBar.toString());
					}else if(channelName.equals("Status")){
						this.chatMsg.setText(this.status.toString());
						this.chatPrs.setText("");
					}
				}
			}
		}// COMMAND SECTION
		//FIXME check null messages
		if(e.getSource().equals(this.inputMsg)){
			String message = this.inputMsg.getText();
			String channelName = (String) this.chanSelector.getSelectedItem();
			if(!message.startsWith("/")){
				this.inputMsg.setText("");
				this.irc.sendMessage(channelName, message.trim());
			}else{
				this.inputMsg.setText("");
				if(message.startsWith("/me ")){
					this.irc.sendActionMessage(channelName, message.substring(3).trim());
				}else if(message.startsWith("/notice ")){
					this.irc.sendNoticeMessage(channelName, message.substring(7).trim());
				}else if(message.startsWith("/mode ")){
					message = message.substring(6);
					StringTokenizer tok = new StringTokenizer(message);
					if(tok.countTokens() >= 3){
						String channel = tok.nextToken().trim();
						String mode = tok.nextToken().trim();
						String person = tok.nextToken("\n").trim();
						this.irc.mode(channel, person, mode);
					}else if(tok.countTokens() == 2){
						String channel = tok.nextToken().trim();
						String mode = tok.nextToken().trim();
						this.irc.mode(channel, mode);
					}else{
						this.inputCmd.setText("MODE syntax is wrong");
					}
				}else if(message.startsWith("/raw ")){
					this.irc.sendRaw(message.substring(4).trim() + "\r\n");
				}else if(message.startsWith("/nick ")){
					this.irc.changeNick(message.substring(5).trim());
					
				}else if(message.startsWith("/ns ")){
					this.irc.sendMessage("NickServ", message.substring(3).trim());
				}else{			
				}
			}
		}
		if(e.getSource().equals(this.quit)){
			this.irc.quit();
		}
		if(e.getSource().equals(this.part)){
			String channelName1 = (String) this.chanSelector.getSelectedItem();
			if(channelName1 != "Status"){
				this.irc.partChannel(channelName1);
				this.chanSelector.removeItem(channelName1);
				System.gc();
			}
		}
		if(e.getSource().equals(this.option)){}
	}



	private void showMessages(String channelName){
		//this.chatMsg.append(this.irc.getMessages(channelName));
		//this.chatMsg.append(this.irc.getNewMessages(channelName));
	}
	private void showPeople(String channelName){
		this.chatPrs.setText(this.irc.getPeople(channelName));
	}
	
	
	
	
	public static void main(String[] args){
		IRCHandler ircH = new IRCHandler("ClientInDevelopment", "Chkrr00ksPropriety", "irc.rizon.net", 6667, false);

		Frame frame = new Frame("IRC:CID", ircH);
		ircH.setPcl(frame);
		Thread th = new Thread(ircH, "IRC reader");
		th.start();
		

		frame.setVisible(true);
	}

}
