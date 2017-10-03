package Client;

import Presence.PresenceService;

import java.io.*;
import java.math.BigDecimal;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;

public class ChatClient {

    public static void main(String args[]) {

        boolean status = true;
        boolean running = true;
        String option;
        String username = args[0];
        String host = "localhost";
        Integer port = 1099;

        if (args.length == 2) {
            String[] hostAndPort = args[1].split(":");

            if (hostAndPort.length == 2) {
                host = hostAndPort[0];
                port = Integer.parseInt(hostAndPort[1]);
            }
            else if (hostAndPort.length == 1) {
                host = hostAndPort[0];
            }


        }
        System.out.println(host +" "+ port);

//            if (System.getSecurityManager() == null) {
//                System.setSecurityManager(new SecurityManager());
//            }
//
//            BufferedReader is = new BufferedReader(new InputStreamReader(System.in));
//
//            try {
//                String name = "PresenceServer";
//                Registry registry = LocateRegistry.getRegistry(args[1]);
//                PresenceService server = (PresenceService) registry.lookup(name);
//
//                //creates new reginfo object for this client
//                RegistrationInfo user = new RegistrationInfo(username, host, port, status);
//                //registers with RMIregistry
//                server.register(user);
//
//
//                //Menu Structure
//                while (running) {
//
//                    printGreeting();
//
//                    option = is.readLine();
//                    String[] tokens = option.split(" ");
//
//                    switch (tokens[0]) {
//                        case "friends":
//
//
//                            break;
//                        case "talk":
//
//                            break;
//
//                        case "broadcast":
//                            break;
//
//                        case "busy":
//
//                            break;
//                        case "available":
//
//                            break;
//                        case "exit":
//
//                            break;
//
//                        default:
//                            System.out.println("Invalid command.");
//                            break;
//                    }
//                }
//            } catch (Exception e) {
//                System.err.println("Client exception:");
//                e.printStackTrace();
        /*
        Register name with Arg[1]
        display user interface
        1.) Friends
        prints out users names and if they are available or not.

        2.) talk [username] [message]
        checks to see if present and available
        NEW THREAD
        connection to target client is established
        send message to connection target
        print message to console

        3.) Broadcast message
        get all users
        send message to all users

        4.)busy
        busy = true;

        5.)available
        busy = false;

        6.)Exit
         */
    }

}
//    }
//
//    private static void printGreeting() {
//
//        System.out.println("Please make a selection from the following items\n" +
//                "friends - get list of friends available\n" +
//                "talk.) Compute Prime Numbers\n" +
//                "broadcast\n" +
//                "busy\n" +
//                "available\n" +
//                "exit\n");
//    }
//
//}

