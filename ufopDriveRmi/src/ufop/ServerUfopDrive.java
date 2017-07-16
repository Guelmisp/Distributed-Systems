package ufop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Set;

import ufop.InterfaceUfopDrive;
import ufop.io.RMIOutputStream;
import ufop.io.RMIOutputStreamImpl;

// Dentro do diretorio bin
//rmiregistry

//servidor
//java -Djave.rmi.server.codebase = file:classdir/ pacote.classe
 
public class ServerUfopDrive implements InterfaceUfopDrive {
	 
	 private String mainPath = "UFOPDrive" + System.getProperty("file.separator");
	 public final Set<ClienteInterface> clientes = new HashSet<ClienteInterface>();

	@Override
	public String teste() throws RemoteException {
		return "Conectado :)"; 
	}
	
	@Override
	public String login(ClienteInterface client) throws RemoteException {
		clientes.add(client);
		return "Bem vindo " + client.getUsername() + ".";
	}
	
	@Override
	public String logout(ClienteInterface client) throws RemoteException {
		String username = client.getUsername();
		clientes.remove(client);
		return "Ate logo " + username + ".";
	}
	
	@Override
	public boolean configDir(ClienteInterface client) throws RemoteException {
		String clientPath = mainPath + client.getUsername();
		if (new File(clientPath).mkdirs()){ 
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean mkdir(ClienteInterface client, String directoryName) throws RemoteException {
		if (new File(mainPath + client.getUsername() + System.getProperty("file.separator") + directoryName).mkdirs()) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public String lsdir(ClienteInterface client) throws RemoteException {
		
		StringBuilder lsdir = new StringBuilder();
		try {
			File dir = new File(mainPath + client.getUsername() + System.getProperty("file.separator"));
			File[] files = dir.listFiles();
		    for (File file : files) {
		        if (file.isDirectory()) {
		        	lsdir.append("\ndiretorio: \n");
		        } else {
		        	lsdir.append("\n     arquivo: \n");
		        }
		        lsdir.append(file.getCanonicalPath());
		    }
		}
		catch(IOException e) {
			System.out.println("readline:"+e.getMessage());
		}
		
		return lsdir.toString();

	}
	
	@Override
	public OutputStream sendfile(ClienteInterface client, String directory, File f) throws IOException {
		
		if (directory.equals("false")){
			String fileName = mainPath + client.getUsername() + System.getProperty("file.separator") + f.getName();
			return new RMIOutputStream(new RMIOutputStreamImpl(new FileOutputStream(fileName)));
		}else {
			String fileName = mainPath + client.getUsername() + System.getProperty("file.separator") + directory + System.getProperty("file.separator") + f.getName();
			return new RMIOutputStream(new RMIOutputStreamImpl(new FileOutputStream(fileName)));
		}

	}
	
	@Override
	public String printClientes() throws RemoteException {
		StringBuilder clientsList = new StringBuilder();
		for (ClienteInterface client : clientes) {
			clientsList.append("User: " + client.getUsername() + "\n");
			clientsList.append("IP: " + client.getIp() + "\n");
			clientsList.append("Object: \n");
		    clientsList.append(client + "\n\n");
		}
		
		return clientsList.toString();
	}
	
	@Override
	public int numberOfClients() throws RemoteException {
		return clientes.size();
	}

	
	public static void main(String[] args){
		try {
			ServerUfopDrive obj = new ServerUfopDrive();
			InterfaceUfopDrive stub = (InterfaceUfopDrive) UnicastRemoteObject.exportObject(obj,0);
			
			Registry registry = LocateRegistry.getRegistry();
			registry.bind("InterfaceUfopDrive", stub);
			
			System.err.println("Servidor pronto.");
						
		} catch (RemoteException r) {
			r.printStackTrace();
		} catch (Exception e) {
			
		}
		
	}

	
	

	

	

	

	

}
