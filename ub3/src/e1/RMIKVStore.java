package e1;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class RMIKVStore extends UnicastRemoteObject 
						implements RemoteKVStore {
	
	private static final long serialVersionUID = 1L; // HMMMM
	
	// The key-value-store.
	HashMap<String, String> store = new HashMap<>();

	protected RMIKVStore() throws RemoteException {
		super();
        try {
        	
            // 1099 Standard-RMI-Port.
            Registry registry = LocateRegistry.createRegistry(1099);

            // Bind the remote object's stub in the registry.
            registry.rebind("RMIKVStore", this);
            
        } catch (Exception e) {
        	
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
            
        }
	}	
	
    /**
     * Checks, if the store contains a given key. 
     * If so, it returns the associated value, else it throws a RemoteException.
     *
     * @param String key
     * @return Associated String value
     */
	@Override
	public String readRemote(String key) throws RemoteException {
		if(store.containsKey(key)) {
			return store.get(key);
		} else {
			throw new RemoteException();
		}
	}

	/**
	 * Inserts a key-value-pair into the store, if the key is already in the store, 
	 * the old value associated with the key gets overwritten by the new given value.
	 * 
	 * @param String key
	 * @param String value 
	 */
	@Override
	public void writeRemote(String key, String value) {
		store.put(key, value);
	}

	/**
	 * Removes a key-value-pair from the store, if the store does not contain the
	 * key, a RemoteExceptiom is thrown.
	 * 
	 * @param String key
	 */
	@Override
	public void removeRemote(String key) throws RemoteException {
		if(store.containsKey(key)) {
			store.remove(key);
		} else {
			throw new RemoteException();
		}
		
	}

}
