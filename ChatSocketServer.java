package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import chat.MultiServerThread;

public class ChatSocketServer implements Runnable{
	private ServerSocket server = null;
	public Socket socket=null;
	//public Thread thread=null;
	public MultiServerThread t=null;
	static ArrayList<MultiServerThread> threads =null;
	private InputStream inStream = null;
	private OutputStream outStream = null;

	public ChatSocketServer(int port) {
		try{
			System.out.println("Binding port "+port);
			server =new ServerSocket(port);
			System.out.println("server started "+server);
			start();
		}catch(IOException e){
			System.out.println(e);
		}
	}

//	public void createSocket() {
//		try {
//			ServerSocket serverSocket = new ServerSocket(3339);
//			while (true) {
//				socket = serverSocket.accept();
//				inStream = socket.getInputStream();
//				outStream = socket.getOutputStream();
//				System.out.println("Connected");
//				createReadThread();
//				createWriteThread();
//
//			}
//		} catch (IOException io) {
//			io.printStackTrace();
//		}
//	}
//
//	public void createReadThread() {
//		Thread readThread = new Thread() {
//			public void run() {
//				while (socket.isConnected()) {
//					try {
//						byte[] readBuffer = new byte[200];
//						int num = inStream.read(readBuffer);
//						if (num > 0) {
//							byte[] arrayBytes = new byte[num];
//							System.arraycopy(readBuffer, 0, arrayBytes, 0, num);
//							String recvedMessage = new String(arrayBytes,
//									"UTF-8");
//							System.out.println("Received message :"
//									+ recvedMessage);
//						} else {
//							notify();
//							//System.out.println("he");
//						}
//						;
//						// System.arraycopy();
//
//					} catch (SocketException se) {
//						System.exit(0);
//
//					} catch (IOException i) {
//						i.printStackTrace();
//					}
//
//				}
//			}
//		};
//		readThread.setPriority(Thread.MAX_PRIORITY);
//		readThread.start();
//	}
//
//	public void createWriteThread() {
//		Thread writeThread = new Thread() {
//			public void run() {
//
//				while (socket.isConnected()) {
//					try {
//						BufferedReader inputReader = new BufferedReader(
//								new InputStreamReader(System.in));
//						sleep(100);
//						String typedMessage = inputReader.readLine();
//						if (typedMessage != null && typedMessage.length() > 0) {
//							synchronized (socket) {
//								outStream.write(typedMessage.getBytes("UTF-8"));
//								sleep(100);
//							}
//						}/*
//						 * else { notify(); }
//						 */
//						;
//						// System.arraycopy();
//
//					} catch (IOException i) {
//						i.printStackTrace();
//					} catch (InterruptedException ie) {
//						ie.printStackTrace();
//					}
//
//				}
//			}
//		};
//		writeThread.setPriority(Thread.MAX_PRIORITY);
//		writeThread.start();
//
//	}
//
//

	@Override
	public void run() {
		// TODO Auto-generated method stub
		t = new MultiServerThread(this, socket);
		threads.add(t);
		while(t!=null){
			try{
				System.out.println("Waiting for a client...");
				addThread(server.accept());
			}catch(IOException e){
				System.out.println("Acceptance error"+e);
			}
		}
	}
	public void addThread(Socket socket){
		System.out.println("Client accepted: "+socket);
		t=new MultiServerThread(this,socket);//// OR thread???
		try{
			t.open();
			t.start();
		}catch(IOException e){
			System.out.print("Error opening threads"+e);			
		}
	}
	public void start(){
		socket =new Socket();
		threads= new ArrayList<MultiServerThread>();
		MultiServerThread t = new MultiServerThread(this, socket);
		
		threads.add(t);
		System.out.println("TEST");
		while (socket.isConnected()) {
			// Î´½øÈëwhileÑ­»·
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
					if (recvedMessage != null && recvedMessage.length() > 0) {
						synchronized (socket) {
							outStream.write(recvedMessage.getBytes("UTF-8"));
						}
				} else {
					notify();
					//System.out.println("he");
				}
				;
				// System.arraycopy();
				}
			} catch (SocketException se) {
				System.exit(0);

			} catch (IOException i) {
				i.printStackTrace();
			}

		}
	}
	public void stop(){}
	public static void main(String[] args) {
		ChatSocketServer chatServer = new ChatSocketServer(5432);
		//chatServer.start();
		//chatServer.stop();
		//chatServer.createSocket();
	}
}
