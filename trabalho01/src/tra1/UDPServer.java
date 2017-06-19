package tra1;

import java.net.*;
import java.io.*;
public class UDPServer{
    public static void main(String args[]){ 
    	DatagramSocket aSocket = null;
    	
    	/*setup to receive file*/
		BufferedOutputStream bos = null;
		OutputStream fos = null;

		try{
			// create socket at agreed port
	    	aSocket = new DatagramSocket(9876);	
 			while(true){ 
 				byte[] buffer = new byte[1024];
 				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
 				System.out.println("Esperando Cliente...");
 				aSocket.receive(request);
 				String comando = new String(request.getData(), 0, request.getLength());
 				/*Configuration UFOPServer for new Client*/
 				String mainPath = "UFOPDrive" + System.getProperty("file.separator");
 				String clientPath = mainPath +  String.valueOf(request.getPort()) + System.getProperty("file.separator");
 				File f = new File(clientPath);
 				f.mkdirs(); 				 				
  				
  				if (comando.contains("mkdir")){
  					/*Server log*/
  					System.out.println("Cliente: " + String.valueOf(request.getPort()) + " | Tarefa: mkdir");
  					String directoryName = comando.replaceAll("mkdir ", "");
  					File file = new File(clientPath + directoryName);
  					if (!file.isDirectory())
  						file.mkdirs();
  					if (file.exists()){
  					    file.mkdir();
  					}
  	  				/*Return message to Client*/
  	  				String mensagem = System.getProperty("file.separator") + clientPath + directoryName + System.getProperty("file.separator");
					byte [] m = mensagem.getBytes();
					DatagramPacket reply = new DatagramPacket(m, mensagem.length(), request.getAddress(), request.getPort());
			    	aSocket.send(reply);										
  					
				} else if (comando.contains("lsdir")) {
					System.out.println("Cliente: " + String.valueOf(request.getPort()) + " | Tarefa: lsdir");
					
					StringBuilder resposta = new StringBuilder();
					
					/*get data from current directory and create string*/
					File dir = new File(clientPath);
					File[] files = dir.listFiles();
				    for (File file : files) {
				        if (file.isDirectory()) {
				            resposta.append("\ndiretorio: \n");
				        } else {
				        	resposta.append("\n     arquivo: \n");
				        }
				        resposta.append(file.getCanonicalPath());
				    }
				    String mensagem = resposta.toString();
				    /*Send answer to client*/
				    byte [] m = mensagem.getBytes();	
				    DatagramPacket reply = new DatagramPacket(m, mensagem.length(), request.getAddress(), request.getPort());
		    		aSocket.send(reply);
		    		
					
				} else if (comando.contains("sendfile")) {
					try {
						System.out.println("Cliente: " + String.valueOf(request.getPort()) + " | Tarefa: sendfile");
						
						/*get file name*/
						String filePath =  comando.replaceAll("sendfile", "");
						String[] parts = filePath.split(System.getProperty("file.separator"));
						String fileName = clientPath + parts[parts.length-1];												
		 				
		 				/*recreate the file and save it in Client directory*/
		 				fos = new FileOutputStream(fileName);
		 				bos = new BufferedOutputStream(fos);
			            double nOfPackets=Math.ceil(((int) (new File(filePath)).length())/1024);
			            
			            /*tell client to send the file*/
						String ready = "ready!";
						DatagramPacket handshake = new DatagramPacket(ready.getBytes(), ready.length(), request.getAddress(), request.getPort());
			    		aSocket.send(handshake);
			            
			    		/*Receive file*/
			            byte [] bufferFile = new byte [1024];
			            DatagramPacket filePacket = new DatagramPacket(bufferFile, bufferFile.length);
			            
			            System.out.println("Remontando arquivo.");
			            for (double i = 0; i< nOfPackets+1; i++){
			        	   aSocket.receive(filePacket);
			        	   byte fileData[] = filePacket.getData();
			        	   System.out.println("Pacote: "+(i+1));
			        	   bos.write(fileData, 0, fileData.length);
			            }
			            
					} finally {
						if (bos != null) bos.close();
						if (fos != null) fos.close();
					}
					
				} else {
					System.out.println("Comando nao reconhecido pelo servidor.");
				}
   				
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
