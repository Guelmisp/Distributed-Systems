package br.ufop;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {
	
	private Client () {
	
	}
	
	public static void main(String[] args){
		String host = (args.length < 1) ? null : args[0];
		
		int opcao = Integer.parseInt(args[1]);
		int ID = Integer.parseInt(args[2]);
		
		Scanner scanner = new Scanner(System.in);
			
		try {
			Registry registry = LocateRegistry.getRegistry(host);
			Eleicao stub = (Eleicao) registry.lookup("Eleicao");
			
			switch (opcao) {
			case 1:
				System.out.println("Sistema de Votacao" + ""
						+ "\n0 - ABC"
						+ "\n1 - BCA"
						+ "\n2 - AAA"
						+ "\n3 - TTT"
						+ "\nSelecione sua opcao: " );
				
				int voto = scanner.nextInt();
				
				boolean resposta = stub.votar(voto, ID);
				if (resposta) {
					System.out.println("Voto Confirmado.");
				} else {
					System.out.println("Voto ja Computado");
				}
				break;
			
			case 2:
				int resultado = stub.resultado();
				System.out.println("Vencedor: " + resultado);
			default:
				break;
			}
			
			
			
		} catch (Exception e){
			
		}
	}

}
