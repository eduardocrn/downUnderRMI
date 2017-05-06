package downUnderRMI;

import java.rmi.Naming;
import java.rmi.RemoteException;

public class DownUnderServer {

    public static void main(String[] args) {
        try {
			java.rmi.registry.LocateRegistry.createRegistry(1099);
			System.out.println("RMI registry ready.");
		} catch (RemoteException e) {
			System.out.println("RMI registry already running.");
		}
		try {
			Naming.rebind ("DownUnder", new DownUnderImplementation());
			System.out.println ("DownUnder is ready.");
		} catch (Exception e) {
			System.out.println ("DownUnder failed:");
			e.printStackTrace();
		}
    }

}
