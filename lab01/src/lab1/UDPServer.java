package lab1;

import java.net.*;
import java.io.*;
public class UDPServer{
    public static void main(String args[]){ 
    	DatagramSocket aSocket = null;
    	int count = 0;
		try{
			count = 0;
	    	aSocket = new DatagramSocket(6789);
					// create socket at agreed port
			byte[] buffer = new byte[10000];
 			while(true){
 				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
  				aSocket.receive(request); 
  				count += 1;
  				System.out.println("Recebido: " + count);
  				
  				/*
    			DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(), 
    				request.getAddress(), request.getPort());
    			System.out.println("Recebeu: " + new String(request.getData()));
    			aSocket.send(reply);
    			*/
    		}
		}
		catch (SocketException e){
			System.out.println("Socket: " + e.getMessage());
		}
		catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		}
		finally {
			if(aSocket != null) aSocket.close();
		}
    }
}
