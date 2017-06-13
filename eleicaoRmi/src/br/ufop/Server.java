package br.ufop;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;

// rmiregistry

// servidor
// java -Djave.rmi.server.codebase = file:classdir/ pacote.classe

public class Server implements Eleicao{
	
	ArrayList<Integer> candidatos = new ArrayList<Integer>();
	ArrayList<Integer> votantes = new ArrayList<Integer>();
	
	public Server() {
		candidatos.add(0, 0);
		candidatos.add(1, 0);
		candidatos.add(2, 0);
		candidatos.add(3, 0);

	}
	
	public int resultado() throws RemoteException{
		return Collections.max(candidatos);
	}
	
	public boolean votar(int numCandidato, int numVotante) throws RemoteException{
		
		int voto = candidatos.get(numCandidato);
		
		if (!votantes.contains(numVotante)){
			voto = voto + 1;
			candidatos.add(numCandidato, voto);
			votantes.add(numVotante);
			return true;
		} else {
			return false;
		}
		
	}
	
	public static void main(String[] args){
		try {
			Server obj = new Server();
			Eleicao stub = (Eleicao) UnicastRemoteObject.exportObject(obj,0);
			
			Registry registry = LocateRegistry.getRegistry();
			registry.bind("Eleicao", stub);
			
			
			System.err.println("Executando o servidor");
		} catch (RemoteException r) {
			r.printStackTrace();
		} catch (Exception e) {
			
		}
		
	}

}
