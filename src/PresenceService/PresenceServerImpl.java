package PresenceService;

import Client.RegistrationInfo;
import Presence.PresenceService;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;
import java.util.Vector;

public class PresenceServerImpl implements PresenceService {

    Hashtable<String, RegistrationInfo> registeredUsers = new Hashtable<>();

    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "PresenceServer";
            PresenceService service = new PresenceServerImpl();
            PresenceService stub = (PresenceService) UnicastRemoteObject.exportObject(service, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("PresenceServer bound");
        } catch (Exception e) {
            System.err.println("PresenceServer exception:");
            e.printStackTrace();
        }
    }

    public boolean register(RegistrationInfo reg) throws RemoteException {

        if (!registeredUsers.containsKey(reg.getUserName())) {
            registeredUsers.put(reg.getUserName(), reg);
            System.out.println("User " + reg.getUserName() + " registered!!!");
            return true;
        }
        System.out.println("Duplicate Entry!");
        return false;
    }

    public boolean updateRegistrationInfo(RegistrationInfo reg) throws RemoteException {


        if (registeredUsers.containsKey(reg.getUserName())) {
            registeredUsers.put(reg.getUserName(), reg);
            System.out.println("User " + reg.getUserName() + "Updated!!!");
            return true;
        }
        System.out.println("User " + reg.getUserName() + "does not exist!!!");
        return false;
    }

    public void unregister(String userName) throws RemoteException {
        registeredUsers.remove(userName);
        System.out.println("Removed " + userName + " from registry!");
    }

    public RegistrationInfo lookup(String name) throws RemoteException {
        return registeredUsers.get(name);

    }

    public Vector<RegistrationInfo> listRegisteredUsers() throws RemoteException {
       Vector<RegistrationInfo> userList = new Vector<RegistrationInfo>();
       userList.addAll(registeredUsers.values());
        return userList;
    }
}
