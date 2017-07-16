package ufop;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

public interface InterfaceUfopDrive extends Remote{
	
	static Set<ClienteInterface> clientes = new HashSet<ClienteInterface>();;
	
	/**
	 * Verifica se o servidor esta disponivel
	 * 
	 * @return String Working
	 * @throws RemoteException  
	 */
	public String teste() throws RemoteException;
	
	/**
	 * adiciona cliente na hashSet do servidor
	 * 
	 * @return String Welcome
	 * @throws RemoteException
	 */
	public String login (ClienteInterface client) throws RemoteException;
	
	/**
	 * remove cliente na hashSet do servidor
	 * 
	 * @return String
	 * @throws RemoteException
	 */
	public String logout(ClienteInterface client) throws RemoteException;
	
	/**
	 * Cria diretorio para o Cliente dentro da pasta UFOPDriv
	 * 
	 * @return
	 * @throws RemoteException
	 */
	public boolean configDir (ClienteInterface client) throws RemoteException;
	
	/**
	 * Criar um diretorio no servidor para o cliente
	 * 
	 * @return true or false
	 * @throws RemoteException
	 */
	public boolean mkdir (ClienteInterface client, String directoryName) throws RemoteException;
	
	/**
	 * Lista todos os arquivos no diretorio
	 * 
	 * @return true or false
	 * @throws RemoteException
	 */
	public String lsdir(ClienteInterface client) throws RemoteException;
	
	/**
	 * Retorna o OutputStream executavel de forma distrivuida
	 * 
	 * @return OutputStream
	 * @throws IOException
	 */
	public OutputStream sendfile(ClienteInterface client, String directory, File fileInputStream) throws IOException;
	
	/**
	 * Retorna a lista de Clientes no Servidor
	 * Username: [name] IP: [0.0.0.0]
	 * Admin Only
	 * @return String
	 * @throws RemoteException
	 */
	public String printClientes() throws RemoteException;
	
	/**
	 * Retorna o numero de Clientes no Servidor
	 * Admin Only
	 * @return Int
	 * @throws RemoteException
	 */
	public int numberOfClients() throws RemoteException;
	
	
}