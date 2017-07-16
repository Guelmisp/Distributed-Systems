package ufop;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClienteInterface extends Remote {
	
	/**
	 * Retorna o usarname do Cliente
	 * 
	 * @return String 
	 * @throws RemoteException
	 */
	public String getUsername() throws RemoteException;
	 
	/**
	 * Retorna o IP da maquina cliente
	 * 
	 * @return String
	 * @throws RemoteException
	 */
	public String getIp() throws RemoteException;

}
