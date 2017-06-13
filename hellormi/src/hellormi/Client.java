package hellormi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
	
	private Client () {
	
	}
	
	public static void main(String[] args){
		String host = (args.length < 1) ? null : args[0];
		
		int a = Integer.parseInt(args[1]);
		int b = Integer.parseInt(args[2]);
		int opcao = Integer.parseInt(args[3]);
		
		try {
			Registry registry = LocateRegistry.getRegistry(host);
			Hello stub = (Hello) registry.lookup("Hello");
			
			switch (opcao) {
			case 1:
				String response = stub.sayHello();
				System.out.println("reposta " + response);
				break;
				
			case 2:
				int resSoma = stub.soma(a, b);
				System.out.println("Resposta Soma: " + resSoma);
				break;

			default:
				break;
			}
			
			
			
			
		} catch (Exception e){
			
		}
	}

}
