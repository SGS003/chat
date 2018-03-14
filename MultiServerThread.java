package chat;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import server.ChatSocketServer;

public class MultiServerThread extends Thread{
	private Socket socket=null;
	private ChatSocketServer server=null;
	private int ID= -1;
	private DataInputStream stream=null;
	
	public MultiServerThread(ChatSocketServer server,Socket socket){
		ID=socket.getPort();
		this.server=server;
		this.socket=socket;
	}
	public void run(){
		System.out.println("server thread " +ID+" running");
		while(true){
			try{
				System.out.println(stream.readUTF());
			}catch(IOException e){
				
			}
		}
	}
	public void open()throws IOException{
		stream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
	}
	public void close() throws IOException{
		if(socket!=null){
			socket.close();
		}if(stream!=null){
			stream.close();
		}
	}
}
