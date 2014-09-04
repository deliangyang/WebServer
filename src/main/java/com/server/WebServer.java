package main.java.com.server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer
{

	public static void main(String[] args)
	{
		boolean isConnect=true;
		try
		{
			ServerSocket serverSocket=new ServerSocket(8090);
			Socket socket=null;
			while(isConnect)
			{
				socket=serverSocket.accept();
				new ServerThread(socket).start();
			}
			socket.close();
			serverSocket.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
