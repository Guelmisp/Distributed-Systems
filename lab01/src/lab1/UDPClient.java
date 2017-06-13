package lab1;

import java.net.*;
import java.io.*;
public class UDPClient{
    public static void main(String args[]) throws InterruptedException{ 
		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket();    
			aSocket.setSoTimeout(10);
			
			String ipServer = args[0];
			int porta = Integer.parseInt(args[1]);
			int numPacotes = Integer.parseInt(args[2]);
			int tamanho = Integer.parseInt(args[3]);
			
			byte [] m = new byte [tamanho];
			
			
			InetAddress aHost = InetAddress.getByName(ipServer);	                                                 
			
			/*Send*/
			DatagramPacket request =
				 	new DatagramPacket(m,  args[0].length(), aHost, porta);
			for (int i = 0; i<=numPacotes; i++){	
				aSocket.send(request);

			}
			
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);	
			aSocket.receive(reply);
			System.out.println("Reply: " + new String(reply.getData()));	
		}
		catch (SocketException e){
			System.out.println("Socket: " + e.getMessage());
		}
		catch (IOException e){
			System.out.println("IO: " + e.getMessage());
		}
		finally {
			if(aSocket != null) 
				aSocket.close();
			}
	}		      	
}
