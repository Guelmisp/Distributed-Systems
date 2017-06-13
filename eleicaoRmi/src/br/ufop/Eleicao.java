package br.ufop;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Eleicao extends Remote{
	
	boolean votar(int numCandidato, int numVotante)  throws RemoteException;
	int resultado() throws RemoteException;
	
	
}