package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;


public class ChatSocketClient {

	private static Socket socket = null;
	private InputStream inStream = null;
	private OutputStream outStream = null;

	public ChatSocketClient() {

	}

	public void createSocket() {
		try {
			
			inStream = socket.getInputStream();
			outStream = socket.getOutputStream();
			createReadThread();
			createWriteThread();
		} catch (UnknownHostException u) {
			u.printStackTrace();
		} catch (IOException io) {
			io.printStackTrace();
		}
	}

	public void createReadThread() {
		Thread readThread = new Thread() {
			public void run() {
				while (socket.isConnected()) {

					try {
						byte[] readBuffer = new byte[200];
						int num = inStream.read(readBuffer);

						if (num > 0) {
							byte[] arrayBytes = new byte[num];
							System.arraycopy(readBuffer, 0, arrayBytes, 0, num);
							String recvedMessage = new String(arrayBytes,
									"UTF-8");
							System.out.println("Received message :"
									+ recvedMessage);
							
						}/*
						 * else { // notify(); }
						 */
						;
						// System.arraycopy();
					} catch (SocketException se) {
						System.exit(0);

					} catch (IOException i) {
						i.printStackTrace();
					}

				}
			}
		};
		readThread.setPriority(Thread.MAX_PRIORITY);
		readThread.start();
	}

	public void createWriteThread() {
		Thread writeThread = new Thread() {
			public void run() {
				while (socket.isConnected()) {

					try {
						BufferedReader inputReader = new BufferedReader(
								new InputStreamReader(System.in));
						sleep(100);
						String typedMessage = inputReader.readLine();
						if (typedMessage != null && typedMessage.length() > 0) {
							if(typedMessage.equals("stop")){
								System.out.println("stop?");
								socket.close();
							}
							synchronized (socket) {
								outStream.write(typedMessage.getBytes("UTF-8"));			
								sleep(100);
							}
						}
						;
						
						// System.arraycopy();

					} catch (IOException i) {
						i.printStackTrace();
					} catch (InterruptedException ie) {
						ie.printStackTrace();
					}

				}
			}
		};
		writeThread.setPriority(Thread.MAX_PRIORITY);
		writeThread.start();
	}

	public static void main(String[] args) throws Exception {
		ChatSocketClient myChatClient = new ChatSocketClient();
		//myChatClient.createSocket();
		socket = new Socket("127.0.0.1", 5432);
		System.out.println("Connected");
		/*
		 * myChatClient.createReadThread(); myChatClient.createWriteThread();
		 */
	}
}
