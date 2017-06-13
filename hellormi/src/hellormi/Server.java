package hellormi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class Server implements Hello{
	
	public Server() {}

	public String sayHello() throws RemoteException{
		return "Hello World";
	}
	
	public int soma(int a, int b) throws RemoteException{
		return a+b;
	}
	
	public static void main(String[] args){
		try {
			Server obj = new Server();
			Hello stub = (Hello) UnicastRemoteObject.exportObject(obj,0);
			
			Registry registry = LocateRegistry.getRegistry();
			registry.bind("Hello", stub);
			
			System.err.println("Executando o servidor");
		} catch (RemoteException r) {
			r.printStackTrace();
		} catch (Exception e) {
			
		}
		
	}

}
