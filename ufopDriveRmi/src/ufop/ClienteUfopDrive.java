package ufop;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import ufop.InterfaceUfopDrive;

 public class ClienteUfopDrive {
	 
	 final public static int BUFFER = 1024 * 64;
	
	private ClienteUfopDrive () { 
		
	}
	
	public static void copiar(InputStream in, OutputStream out) throws IOException {
    	byte[] b = new byte[BUFFER];
        int len;
        while ((len = in.read(b)) >= 0) {
        	out.write(b, 0, len);
        }
        in.close();
        out.close();
    }
	
	public static void main(String[] args) throws RemoteException, UnknownHostException{
		
		if (args.length >= 2) {
			boolean clienteConectado = true;
			
			String server = args[0];
			String username = args[1];
			String clienteIp = Inet4Address.getLocalHost().getHostAddress();
			
			Cliente client = new Cliente(username, clienteIp);
			Scanner scanner = new Scanner(System.in);
				
			try {
				Registry registry = LocateRegistry.getRegistry(server);
				InterfaceUfopDrive ufopDrive = (InterfaceUfopDrive) registry.lookup("InterfaceUfopDrive");
				
				System.out.println(ufopDrive.login(client));
				ufopDrive.configDir(client);
				
				while(clienteConectado){
					System.out.printf("\n> ");
					String comando = scanner.nextLine();
					String[] comandos = comando.split(" ");
					
					switch(comandos[0]) {
						case "teste":
							System.out.println("teste");
							System.out.println(ufopDrive.teste());
							break;
						
						case "lsdir":
							System.out.println(ufopDrive.lsdir(client));
							break;
						
						case "sendfile":
							String srcFile = comandos[1];
							if (srcFile != null && srcFile.length() > 0) {
								if (comandos.length == 2) {
									String notDefined = "false";
									long startTime = System.nanoTime();
									copiar (new FileInputStream(srcFile), ufopDrive.sendfile(client, notDefined, new File(srcFile)));
									long endTime = System.nanoTime();
									System.out.println("Arquivo Enviado. Tempo de envio: " + (endTime - startTime)/1000000 + " ms");
								}else {
									long startTime = System.nanoTime();
									copiar (new FileInputStream(srcFile), ufopDrive.sendfile(client, comandos[2], new File(srcFile)));
									long endTime = System.nanoTime();
									System.out.println("Arquivo Enviado. Tempo de envio: " + (endTime - startTime)/1000000 + " ms");
								}
								
							}
							break;
						
						case "mkdir":
							String directoryName = comandos[1];
							ufopDrive.mkdir(client, directoryName);
							break;
							
						case "logout":
							clienteConectado = false;
							System.out.println(ufopDrive.logout(client));
							System.out.println("Encerrando apliacacao...");
							System.exit(0);
							break;
						
						case "help":
							System.out.println("Comandos disponiveis: ");
							System.out.println("> teste");
							System.out.println("> lsdir");
							System.out.println("> mkdir nome_do_diretorio");
							System.out.println("> sendfile srcfile.txt [directory]");
							System.out.println("> logout");
							break;
							
						default: 
							System.out.println("Comando nao reconhecido pelo servidor. Tente Novamente");
							break;
					}
					
				}			
				
			} catch (Exception e){
				e.printStackTrace();
			}
			
			finally{
				scanner.close();
			}
		} 
		else {
			System.out.println("\nErro!\n\nExemplo: java [-Djava.security.policy=rmi.policy] ufop.ClienteUfopDrive [127.0.0.1] [username]");
		}

	}
}
