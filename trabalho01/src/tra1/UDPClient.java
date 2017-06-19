package tra1;

import java.net.*;
import java.util.Scanner;
import java.io.*;
public class UDPClient{
    public static void main(String args[]) throws InterruptedException{ 
		// args[] give message contents and destination host name
		DatagramSocket aSocket = null;
		/*Setup to transfer files*/
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		OutputStream os = null;
		try {	
			/*initial setup*/
			aSocket = new DatagramSocket();    
			InetAddress aHost = InetAddress.getByName("localhost");
			int serverPort = 9876;
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(System.in);
			String comando = null;
			byte[] buffer = new byte[1024];
			
			while(true) {
				System.out.printf("> ");
				comando = scanner.nextLine();
				byte [] m = comando.getBytes();	
				
				if (comando.contains("mkdir") || comando.contains("lsdir")){
					DatagramPacket request = new DatagramPacket(m,  comando.length(), aHost, serverPort);
					aSocket.send(request);	
					/*reply from the system*/
					DatagramPacket reply = new DatagramPacket(buffer, buffer.length);	
					aSocket.receive(reply);
					System.out.println("Retorno: " + new String(reply.getData(), 0, reply.getLength()));
					
				} else if (comando.contains("sendfile")) {
					try {
						/*handshake*/
						DatagramPacket request = new DatagramPacket(m,  comando.length(), aHost, serverPort);
						aSocket.send(request);
						DatagramPacket reply = new DatagramPacket(buffer, buffer.length);	
						aSocket.receive(reply);
						String resposta = new String(reply.getData(), 0, reply.getLength());
						System.out.println(resposta);
						
						if (resposta.contains("ready!")){
							String FILE_PATH = comando.replaceAll("sendfile ", "");
							
							/*creating a new file to send*/
							File clientFile = new File (FILE_PATH);;
							fis = new FileInputStream(clientFile);
							bis = new BufferedInputStream(fis);
							
							/*Send file*/
							double nOfPackets = Math.ceil(((int) clientFile.length()) / 1024);
							
							for (double i = 0; i < nOfPackets + 1; i++) {
								byte [] clientArray = new byte[(int) clientFile.length()];
				                bis.read(clientArray, 0, clientArray.length);
				                System.out.println("Pacote:" + (i + 1));
				                DatagramPacket sendFile = new DatagramPacket(clientArray,  clientArray.length, aHost, serverPort);
								aSocket.send(sendFile);
				            }
							
							System.out.println("Arquivo enviado.");
							
						} else {
							System.out.println("Servidor nao esta pronto! Impossivel enviar");
						}
						
					} catch (IOException e){
						e.printStackTrace();
					}finally {
						if (bis != null) bis.close();
						if (os != null) os.close();
						if (fis != null) fis.close();
					}					
				} else if (comando.contains("exit")){
					System.out.println("Encerrando Cliente");
					aSocket.close();
				} else {
					System.out.println("Comando nao reconhecido pelo servidor. Tente novamente.");
				}									
			}					
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
