package cha4;

import java.net.*;
import java.io.*;
public class UDPServer{
    public static void main(String args[]){ 
    	DatagramSocket aSocket = null;
		try{
	    	aSocket = new DatagramSocket(6790);
					// create socket at agreed port
			byte[] buffer = new byte[1000];
 			while(true){
 				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
  				aSocket.receive(request);
  				
  				//Pega msg e Cria objeto no servidor
  				String str = new String(request.getData());
  				String[] results = str.split(";");
  				Aluno a = new Aluno();
  				a.setNome(results[0]);
  				a.setIdade(Integer.parseInt(results[1]));
  				a.setCurso(results[2]);
  				
    			DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(), 
    				request.getAddress(), request.getPort());
    			System.out.println("Recebeu: " + a.getNome() + " " + a.getIdade() +" "+ a.getCurso());
    			aSocket.send(reply);
    		}
		}catch (SocketException e){System.out.println("Socket: " + e.getMessage());
		}catch (IOException e) {System.out.println("IO: " + e.getMessage());
		}finally {if(aSocket != null) aSocket.close();}
    }
}
