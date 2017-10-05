package Client;

import Presence.PresenceService;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ChatClient {

    public static void main(String args[]) {

        boolean status = true;
        boolean running = true;
        String option;
        String username = args[0];
        String serverHost = "localhost";
        Integer serverPort = 1099;


        BufferedReader is = new BufferedReader(new InputStreamReader(System.in));

        if (args.length == 2) {
            String[] hostAndPort = args[1].split(":");
//            System.out.println(hostAndPort.length);
//            System.out.println("host and port length= "+hostAndPort.length);
            if (hostAndPort.length == 2) {
                serverHost = hostAndPort[0];
//                System.out.println(host.toString());
                serverPort = Integer.parseInt(hostAndPort[1]);
//                System.out.println(port.toString());
            } else if (hostAndPort.length == 1) {
                serverHost = hostAndPort[0];
            }

        }

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            //start listener here
            Thread listener = new Thread(new ClientListener());
            listener.start();


            //look for server
            String name = "PresenceServer";
            Registry registry = LocateRegistry.getRegistry(serverHost, serverPort);
            PresenceService server = (PresenceService) registry.lookup(name);

            //check for duplicate username.  If found exit.
            for (RegistrationInfo reg:server.listRegisteredUsers()) {
                if(reg.getUserName().equals(username)) {
                        System.out.println("!!--User already exists in registry--!!\n");
                        System.exit(0);
                    }

            }


            //gets client's host
            InetAddress clientHost;
            clientHost = InetAddress.getLocalHost();
            String Hostname = clientHost.getHostName();


            //creates new reginfo object for this client
            RegistrationInfo user = new RegistrationInfo(username, Hostname, 9998, status);

            //registers with RMIregistry
            server.register(user);


            //Menu Structure
            while (running) {

                printGreeting();

                option = is.readLine();
                String[] tokens = option.split(" ");

                switch (tokens[0]) {
                    case "friends":

                        for (RegistrationInfo RegInfo : server.listRegisteredUsers()) {
                            System.out.println(RegInfo.getUserName() + " -- Status: " + RegInfo.getStatus());
                        }

                        break;
                    case "talk":
                        if (tokens.length < 3) {
                            System.out.println("must have a message to send");
                            break;
                        }
                        String msg = buildMessage(tokens);

                        String targetName = tokens[1];
                        sendMessage(msg, server, targetName);

                        break;

                    case "broadcast":

                        String message = buildBroadcastMessage(tokens);
                        for (RegistrationInfo info : server.listRegisteredUsers()) {
                            if (!info.getUserName().equals(username)) {
                                sendMessage(message, server, info.getUserName());
                            }

                        }

                        break;

                    case "busy":
                        user.setStatus(false);
                        server.updateRegistrationInfo(user);
                        System.out.println("Status set to Busy\n");
                        break;

                    case "available":
                        user.setStatus(true);
                        server.updateRegistrationInfo(user);
                        System.out.println("Status set to Available\n");
                        break;

                    case "exit":
                        running = false;
                        server.unregister(username);
                        System.out.println("\nLater!");
                        System.exit(0);
                        break;

                    default:
                        System.out.println("Invalid command.");
                        break;
                }


            }
        } catch (Exception e) {
            System.err.println("Client exception:");
            e.printStackTrace();

        }


    }

    private static void printGreeting() {

        System.out.println("\nPlease make a selection from the following items\n" +
                "friends - get list of friends and their availability\n" +
                "talk [username] [message] - Send a message to a user\n" +
                "broadcast\n" +
                "busy: Sets your status to Busy.\n" +
                "available: Sets your status to Available\n" +
                "exit\n" +
                "----------------------------------------------------\n");
    }

    //builds a message from arguements given for Broadcast.
    private static String buildBroadcastMessage(String[] tokens) {

        String message = "";

        //rebuild the message
        for (int i = 1; i < tokens.length; i++) {
            message += tokens[i] + " ";
        }
        String trimmedMessage = message.trim();

        return trimmedMessage;
    }

    //constructs the message the user wants to send from the arguements given
    private static String buildMessage(String[] tokens) {

        String message = "";

        //rebuild the message
        for (int i = 2; i < tokens.length; i++) {
            message += tokens[i] + " ";
        }
        String trimmedMessage = message.trim();

        return trimmedMessage;
    }

    //opens a socket, sends a message anc closes the socket
    private static void sendMessage(String message, PresenceService server, String targetName) {

        try {
            RegistrationInfo talkTarget = server.lookup(targetName);
            if (talkTarget != null) {
                Boolean busyStatus = talkTarget.getStatus();

                if (busyStatus) {

                    String targetHost = talkTarget.getHost();
                    Integer targetPort = talkTarget.getPort();

                    try {
                        Socket clientSocket = new Socket(targetHost, targetPort);
                        DataOutputStream tx = new DataOutputStream(clientSocket.getOutputStream());
                        tx.writeBytes(targetName + ": " + message);


                        tx.close();
                        clientSocket.close();

                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }
            }
        } catch (RemoteException e) {
            System.out.println(e);
        }


    }
}

