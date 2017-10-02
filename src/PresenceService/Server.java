package PresenceService;

import Client.RegistrationInfo;
import Presence.PresenceService;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;
import java.util.Vector;

public class Server implements PresenceService {

    Hashtable registeredUsers = new Hashtable();

    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "PresenceService";
            PresenceService service = new Server();
            PresenceService stub = (PresenceService) UnicastRemoteObject.exportObject(service, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("PresenceService bound");
        } catch (Exception e) {
            System.err.println("PresenceService exception:");
            e.printStackTrace();
        }
    }

    public boolean register(RegistrationInfo reg) throws RemoteException {
        //check if user exists in hashtable

        //if not register
        if (!registeredUsers.containsValue(reg.getUserName())) {
            registeredUsers.put(reg.getUserName(),reg);
            return true;
        }
        return false;
    }

    public boolean updateRegistrationInfo(RegistrationInfo reg) throws RemoteException {


        //get user from hash table
        registeredUsers.get(reg.getUserName());
        //change value of
        return false;
    }

    public void unregister(String userName) throws RemoteException {
        registeredUsers.get(userName);
    }

    public RegistrationInfo lookup(String name) throws RemoteException {
        return null;
    }

    public Vector<RegistrationInfo> listRegisteredUsers() throws RemoteException {
        return null;
    }
}
