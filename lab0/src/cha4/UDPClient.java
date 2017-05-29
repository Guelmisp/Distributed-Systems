package cha4;

import java.net.*;
import java.io.*;
public class UDPClient{
    public static void main(String args[]){ 
		// args give message contents and destination hostname
		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket(); 
			
			Aluno a = new Aluno();
			a.setNome("Miguel");
			a.setIdade(24);
			a.setCurso("EC");
			
			String msg = a.getNome()+";"+a.getIdade()+";"+a.getCurso();
			
			byte [] m = msg.getBytes();
			InetAddress aHost = InetAddress.getByName(args[0]);
			int serverPort = 6790;		                                                 
			DatagramPacket request =
			 	new DatagramPacket(m,  msg.length(), aHost, serverPort);
			aSocket.send(request);			                        
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);	
			aSocket.receive(reply);
			System.out.println("Reply: " + new String(reply.getData()));	
		}catch (SocketException e){
			System.out.println("Socket: " + e.getMessage());
		}catch (IOException e){
			System.out.println("IO: " + e.getMessage());
		}finally {
			if(aSocket != null) 
				aSocket.close();
			}
	}		      	
}
