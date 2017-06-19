package tra1;

import java.net.*;
import java.io.*;
public class TCPServer {
	public static void main (String args[]) {
		try{
			int serverPort = 7897; // the server port
			@SuppressWarnings("resource")
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
		String mainPath = "UFOPDrive" + System.getProperty("file.separator");;
		String clientPath = mainPath + clientSocket.getPort() + System.getProperty("file.separator");;
		new File(clientPath).mkdirs();
		
		/*setup to receive file*/
		BufferedOutputStream bos = null;
		OutputStream fos = null;
		int bytesRead;
		
		try {
			while(true){
				
				System.out.println("Esperando Cliente...");
				String data = in.readUTF();	  
				
				if(data.contains("mkdir")){
					System.out.println("Cliente: " + clientSocket + " | Tarefa: mkdir");
					String directoryName = data.replaceAll("mkdir ", "");
					
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
					out.writeUTF(resposta.toString() + "\n");
			        out.flush();
				}
				else if (data.contains("sendfile")){
					try {
						System.out.println("Cliente: " + clientSocket + " | Tarefa: sendfile");
						/*get file name*/
						String filePath =  data.replaceAll("sendfile", "");
						String[] parts = filePath.split(System.getProperty("file.separator"));
						String fileName = clientPath + parts[parts.length-1];
						
						/*tell client to send the file*/
						out.writeUTF("ready!");
						
						/*gets file size from Client*/
						long fileSize = in.readLong();
							
						/*recreate the file and save it in Client directory*/
						byte [] buffer = new byte [1024];
						fos = new FileOutputStream(fileName);
	
						while (fileSize > 0 && (bytesRead = in.read(buffer, 0, (int)Math.min(buffer.length, fileSize))) != -1) {
							fos.write(buffer, 0, bytesRead);
							fileSize -= bytesRead;
						}
						
						out.writeUTF("Arquivo salvo no servidor.");
						
					} finally {
						if (bos != null) bos.close();
						if (fos != null) fos.close();
					}
				}else {
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