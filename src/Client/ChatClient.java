package Client;

import Presence.PresenceService;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ChatClient {

    public static void main(String args[]) {

        boolean status = true;
        boolean running = true;
        String option;
        String username = args[0];
        String host = "localhost";
        Integer port = 1099;
        BufferedReader is = new BufferedReader(new InputStreamReader(System.in));

        if (args.length == 2) {
            String[] hostAndPort = args[1].split(":");

            if (hostAndPort.length == 2) {
                host = hostAndPort[0];
                port = Integer.parseInt(hostAndPort[1]);
            } else if (hostAndPort.length == 1) {
                host = hostAndPort[0];
            }

        }
//        System.out.println(host + " " + port);

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            //start listener here
            Thread listener = new Thread(new ClientListener());
            listener.start();

            String name = "PresenceServer";
            Registry registry = LocateRegistry.getRegistry(args[1]);
            PresenceService server = (PresenceService) registry.lookup(name);

            //creates new reginfo object for this client
            RegistrationInfo user = new RegistrationInfo(username, host, port, status);
            //registers with RMIregistry
            server.register(user);



            //Menu Structure
            while (running) {

                printGreeting();

                option = is.readLine();
                String[] tokens = option.split(" ");

                switch (tokens[0]) {
                    case "friends":

                        for(RegistrationInfo RegInfo: server.listRegisteredUsers()){
                            System.out.println(RegInfo.getUserName()+", ");
                        }

                        break;
                    case "talk":
                        if (tokens.length < 3) {
                            System.out.println("must have a message to send");
                            break;
                        }

                        String targetName = tokens[1];
                        String message = "";
                        for (int i = 2; i < tokens.length; i++) {
                            message += tokens[i] + " ";
                        }
                        String trimmedMessage = message.trim();
                        System.out.println(trimmedMessage);

                        RegistrationInfo talkTarget = server.lookup(targetName);

                        if (talkTarget != null) {
                            System.out.println("\ntarget Ain't Null");

                            Boolean busyStatus = talkTarget.getStatus();
                            if (busyStatus) {
                                System.out.println("\nNot so busy either!");
                                String targetHost = talkTarget.getHost();
                                Integer targetPort = talkTarget.getPort();

                                Socket clientSocket = new Socket(targetHost, 9999);
                                Writer out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                                out.write(trimmedMessage);
                                clientSocket.close();

                            }
                        }

                        break;

                    case "broadcast":
                        break;

                    case "busy":

                        break;
                    case "available":

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
                "friends - get list of friends available\n" +
                "talk [username] [message] - Send a message to a user\n" +
                "broadcast\n" +
                "busy\n" +
                "available\n" +
                "exit\n");
    }

}

