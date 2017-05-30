package tra1;

import java.net.*;
import java.util.Scanner;
import java.io.*;
public class TCPClient {
	@SuppressWarnings("deprecation")
	public static void main (String args[]) {
		// arguments supply message and host name
		Socket s = null;
		try{
			
			/*initial setup of the server*/
			int port = 7896;
			String address = "127.0.0.1";
			String comando = null;
			String data = null;
			
			/*creating socket and channels of input and output*/
			s = new Socket(address, port);    
			DataInputStream in = new DataInputStream( s.getInputStream());
			
			DataOutputStream out =new DataOutputStream( s.getOutputStream());
			Scanner scanner = new Scanner(System.in);

			while((comando = scanner.nextLine()) != null){
				out.flush();
				out.writeUTF(comando);
				
				data = in.readUTF();
				System.out.println(data);
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