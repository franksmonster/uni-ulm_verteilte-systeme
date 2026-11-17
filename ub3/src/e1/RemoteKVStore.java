package e1;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteKVStore extends Remote {

	public String readRemote(String key) throws RemoteException;
	
	public void writeRemote(String key, String value) throws RemoteException;
	
	public void removeRemote(String key) throws RemoteException;
}
