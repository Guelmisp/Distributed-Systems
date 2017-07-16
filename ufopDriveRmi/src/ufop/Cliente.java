package ufop;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

@SuppressWarnings("serial")
public class Cliente extends UnicastRemoteObject implements ClienteInterface {
	
	private String username;
	private String ip;
	
	public Cliente(String u, String s) throws RemoteException{
		username=u;
		ip=s;
	}

	@Override 
	public String getUsername() throws RemoteException {
		return username;
	}

	@Override
	public String getIp() throws RemoteException {
		return ip;
	}


	

}
