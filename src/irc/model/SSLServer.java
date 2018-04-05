/**
 * 
 */
package irc.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * @author chkrr00k
 *
 */
public class SSLServer extends Server {

	/**
	 * @param server
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public SSLServer(String server, int port) throws UnknownHostException, IOException {
		super();
	    SSLSocketFactory ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();
		super.socket = ssf.createSocket(server, port);
		((SSLSocket) super.socket).startHandshake();
		super.writerObj = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream( )));
		super.readerObj = new BufferedReader(new InputStreamReader(this.socket.getInputStream( )));	
		super.connected = true;
		super.socket.setSoTimeout(0);
		super.serverName = server;

	}

}
