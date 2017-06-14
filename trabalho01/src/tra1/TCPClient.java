package tra1;

import java.net.*;
import java.util.Scanner;
import java.io.*;
public class TCPClient {
	public static void main (String args[]) {
		// arguments supply message and host name
		Socket s = null;
		try{
			
			/*initial setup of the server*/
			int port = 7897;
			String address = "127.0.0.1";
			String comando = null;
			String data = null;
			
			/*Setup to transfer files*/
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			OutputStream os = null;
			
			/*creating socket and channels of input and output*/
			s = new Socket(address, port);    
			DataInputStream in = new DataInputStream( s.getInputStream());
			
			DataOutputStream out = new DataOutputStream( s.getOutputStream());
			Scanner scanner = new Scanner(System.in);

			while(true){
				comando = scanner.nextLine();
				/*close client*/
				if (comando.contains("exit")){
					System.out.println("Encerrando cliente...");
					s.close();
				} else if (comando.contains("sendfile")){
					try {
						/*tell the server to receive a file*/
						out.writeUTF(comando);
						data = in.readUTF();
						System.out.println(data);
						if (data.contains("ready!")){
							String FILE_PATH = comando.replaceAll("sendfile ", "");
							
							/*creating a new file to send*/
							File clientFile = new File (FILE_PATH);
							byte [] clientArray = new byte[(int) clientFile.length()];
							fis = new FileInputStream(clientFile);
							bis = new BufferedInputStream(fis);
							bis.read(clientArray,0,clientArray.length);
							
							/*Send file size*/
							out.writeLong(clientArray.length);
							
							/*Send file*/
							out.write(clientArray, 0, clientArray.length);
							out.flush();
							
							/*verify*/
							System.out.println(in.readUTF());
							
						} else {
							System.out.println("Servidor nao esta pronto para receber arquivo.");
						}
						
					} catch (IOException ex){
						System.out.println(ex.getMessage());
					} finally {
						if (bis != null) bis.close();
						if (os != null) os.close();
						if (fis != null) fis.close();
					}
					
				} else {
					out.writeUTF(comando);
					data = in.readUTF();
					System.out.println(data);
				}
				
			}
		}
		catch (UnknownHostException e){
			System.out.println("Socket:"+e.getMessage());
		}
		catch (EOFException e){
			System.out.println("EOF:"+e.getMessage());
		}
		catch (IOException e){
			System.out.println("readline:"+e.getMessage());
		}
		finally {
			if(s!=null) {
				try {
					s.close();
					}
				catch (IOException e){
					System.out.println("close:"+e.getMessage());
					}
				}
			}
     }
}