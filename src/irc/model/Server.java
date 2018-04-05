/**
 * 
 */
package irc.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * @author chkrr00k;
 *
 */
public class Server {

	protected String serverName;
	protected Socket socket;
	protected BufferedWriter writerObj;
	protected BufferedReader readerObj;
	protected boolean connected;
	
	public Server(String server, int port) throws UnknownHostException, IOException {
		this.socket = new Socket(server, port);
		this.writerObj = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream( )));
		this.readerObj = new BufferedReader(new InputStreamReader(this.socket.getInputStream( )));	
		this.connected = true;
		this.socket.setSoTimeout(0);
		this.serverName = server;
	}
	
	/**
	 * 
	 */
	public Server() {/*Do nothing*/}

	public boolean isConnected(){
		return this.connected;
	}
	
	public void write(String input) throws IOException{
		if(this.connected){
			this.writerObj.write(input);
		}
	}
	
	public String read() throws IOException{
		if(this.connected){
			try{
				return this.readerObj.readLine();

			}catch(SocketTimeoutException ste){				
				this.socket = new Socket(serverName, 6667);
				return "An error during connection has occurred";
			}catch(SocketException e){
				this.readerObj.close();
				return "Socket was closed";
			}
		}
		else{
			return "";
		}
	}
	
	public void flush() throws IOException{
		if(this.connected){
			this.writerObj.flush( );
		}
	}
	
	public void close() throws IOException{
		if(this.connected){
			this.connected = false;
			this.writerObj.close();
			if(!this.socket.isClosed()){
				if(!this.socket.isOutputShutdown()){
					this.socket.shutdownOutput();
				}
				if(!this.socket.isInputShutdown()){
					this.socket.shutdownInput();
				}
			}
			//this.writerObj.close();
			//this.readerObj.close();
			//this.socket.close();
				
		}
	}

}
