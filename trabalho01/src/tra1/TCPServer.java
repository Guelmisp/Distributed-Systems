package tra1;

import java.net.*;
import java.util.Scanner;
import java.io.*;
public class TCPServer {
	public static void main (String args[]) {
		try{
			int serverPort = 7896; // the server port
			ServerSocket listenSocket = new ServerSocket(serverPort);
			while(true) {
				Socket clientSocket = listenSocket.accept();
				Connection c = new Connection(clientSocket);
			}
		} 
		catch(IOException e) {
			System.out.println("Listen socket:"+e.getMessage());
		}
	}
}

class Connection extends Thread {
	DataInputStream in;
	DataOutputStream out;
	Socket clientSocket;
	public Connection (Socket aClientSocket) {
		try {
			clientSocket = aClientSocket;
			in = new DataInputStream( clientSocket.getInputStream());
			out =new DataOutputStream( clientSocket.getOutputStream());
			
			this.start();
		} 
		catch(IOException e) {
			System.out.println("Connection:"+e.getMessage());
		}
	}
	public void run(){
		
		/*Configuration UFOPServer for new Client*/
		String mainPath = "UFOPDrive/";
		String clientPath = mainPath + clientSocket.getPort() + "/";
		new File(clientPath).mkdirs();
		
		try {
			while(true){
				String data = in.readUTF();	  
				
				if(data.contains("mkdir")){
					System.out.println("Cliente: " + clientSocket + " | Tarefa: mkdir");
					String directoryName = data.replaceAll("mkdir", "");
					
					new File(clientPath + directoryName).mkdir();
					out.writeUTF("Diretorio:" + directoryName + " criado");
					out.flush();
					
				}
				else if (data.contains("lsdir")){
					System.out.println("Cliente: " + clientSocket + " | Tarefa: lsdir");
					
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
				    
				    /*Send answer to client*/
					out.writeUTF(resposta.toString() + "/n");
			        out.flush();
				}
				else if (data.contains("sendfile")){
					out.writeUTF("Enviar arquivo para o servidor");
				}
				else if (data.contains("exit")){
					out.writeUTF("terminando execucao com servidor...");
					clientSocket.close();
				}
				else {
					out.writeUTF("Comando nao reconhecido pelo servidor");
				}
			}
			
		}
		catch (EOFException e){
			System.out.println("EOF:"+e.getMessage());
		} 
		catch(IOException e) {
			System.out.println("readline:"+e.getMessage());
		} 
		finally{ 
			try {
				clientSocket.close();
				}
			catch (IOException e){
				/*close failed*/
				}
			}
	}
}