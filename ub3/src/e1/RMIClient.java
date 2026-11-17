package e1;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            // Cast to the remote interface...
            RemoteKVStore service =
                    (RemoteKVStore) registry.lookup("RMIKVStore");

            service.writeRemote("Nashornkaefer", "Dynastes tityus");

            String value = service.readRemote("Nashornkaefer");

            System.out.println("Value: " + value);
            
            service.removeRemote("Nashornkaefer");

        } catch (Exception e) {
            System.err.println("Client exception: " + e);
            e.printStackTrace();
        }
    }
}
