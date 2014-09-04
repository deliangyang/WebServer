package main.java.com.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

public class ServerThread extends Thread
{
	private Socket socket;
	private InputStream is=null;
	private PrintStream ps=null;
	
	private static final String ROOT_DIR = "c:users/lenovo/desktop";
	
	public ServerThread(Socket socket)
	{
		this.socket=socket;
		try
		{
			is=socket.getInputStream();
			ps=new PrintStream(socket.getOutputStream());
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	
	@Override
	public void run()
	{
		String fileName=parse(is);
		
		sendFile(fileName);
	}


	private void sendFile(String fileName)
	{
		File file=new File(ROOT_DIR + fileName);
		if(!file.exists())
		{
			sendError(404, "Con't Find this file");
			return;
		}
		
		try
		{
			DataInputStream dis=new DataInputStream(new FileInputStream(file));
			int len=(int)file.length();
			byte[] buffer=new byte[len];
			dis.readFully(buffer);
			ps.println("http/1.0 200 OK");
			ps.println("Content-type: text/html");
			ps.println();
			ps.println("<html>");
			ps.println("<title>Error Message");
			ps.println("</title>");
			ps.println("<body>");
			
			ps.write(buffer, 0, len);
			
			ps.println("</body>");
			ps.println("</html>");
			ps.flush();
			ps.close();
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}


	private String parse(InputStream is)
	{
		BufferedReader br=new BufferedReader(new InputStreamReader(is));
		String fileName=null;
		try
		{
			String httpMessage=br.readLine();
			String[] content=httpMessage.split(" ");
			
			if(content.length!=3)
			{
				sendError(400, "Request Error");
				return null;
			}
			System.out.println("Code: " + content[0] 
					+ " File Name: " + content[1] + " Http Version: " + content[2]);
			fileName=content[1];
		} catch (Exception e)
		{
			// TODO: handle exception
		}
		return fileName;
	}


	private void sendError(int ErrorCode, String ErrorMsg)
	{
		ps.println("http:/1.0"+ErrorCode+" "+ErrorMsg);
		ps.println("content-type:text/html");
		ps.println();
		ps.println("<html>");
		ps.println("<title>Error Message");
		ps.println("</title>");
		ps.println("<body>");
		ps.println("<h1>ErrorCode:"+ErrorCode+",Message:"+ErrorMsg+"</h1>");
		ps.println("</body>");
		ps.println("</html>");
		ps.flush();
		ps.close();
		try
		{
			is.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
}
