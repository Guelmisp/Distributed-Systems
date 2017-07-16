package ufop;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import ufop.InterfaceUfopDrive;

 public class Admin {
	 
	 
	private Admin () {
		
	}
	
	
	public static void main(String[] args) throws RemoteException, UnknownHostException{
		
		if (args.length >= 2 && args[1].equals("admin")) {
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
				
				while(clienteConectado){
					System.out.printf("\n> ");
					String comando = scanner.nextLine();
					String[] comandos = comando.split(" ");
					
					switch(comandos[0]) {
						case "print":
							System.out.println("Log de Clientes Ativos no Servidor");
							System.out.println(ufopDrive.printClientes());
							break;
						
						case "number":
							System.out.println("Numero de Clientes Ativos: " + ufopDrive.numberOfClients());
							break;
						
						case "logout":
							clienteConectado = false;
							System.out.println(ufopDrive.logout(client));
							System.out.println("Encerrando apliacacao...");
							System.exit(0);
							break;
						
						case "help":
							System.out.println("Comandos disponiveis: ");
							System.out.println("> print");
							System.out.println("> number");
							System.out.println("> logout");
							System.out.println("> help");
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
			System.out.println("\nErro!\n\nExemplo: java [-Djava.security.policy=rmi.policy] ufop.ClienteUfopDrive [127.0.0.1] [admin]");
		}

	}
}
