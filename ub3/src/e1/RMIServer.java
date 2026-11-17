package e1;

import java.rmi.RemoteException;

public class RMIServer {

	public static void main(String[] args) throws RemoteException {
	
		RMIKVStore store = new RMIKVStore();

        System.out.println("RMI Server is running.");
	}

}
