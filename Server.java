import java.net.*;
import java.io.*;
import java.util.*;

/**
 * @author Daniel Tyebkhan
 */
public class Server{ 
    public static final int PORT = 9991;
    public static final String LOCATION = Server.class.getProtectionDomain().getCodeSource().getLocation().getFile() +"users.txt";

    private static ArrayList<ConnectionThread> clients;
    private static HashMap clientMap;

    public static void main(String[] args){
        clients = new ArrayList <>();
        generateHash();
        connectToServer();
    }

    /**
     * Starts the server
     */
    public static void connectToServer(){
        try(ServerSocket serverSocket = new ServerSocket(PORT)){
            System.out.println("Server is now Live on Port " + PORT);
            while(true){
                try{
                    Socket connectionSocket = serverSocket.accept();
                    System.out.println("Someone Has Joined");
                    ConnectionThread client = new ConnectionThread(connectionSocket);
                    client.start();
                    clients.add(client);
                }catch(Exception e){
                    System.out.println(e + "could not connect client to server");
                }
            }
        }catch(Exception e){
            System.out.println(e + "could not connect client to server");
        }
    }

    /**
     * Generates the userbase from the input file
     */
    public static void generateHash(){
        System.out.println("Generating User Hashmap");
        File users = new File(LOCATION);
        try(Scanner ireader = new Scanner(users)){
            int totalLines = 0;
            while(ireader.hasNextLine()){
                ireader.nextLine();
                totalLines++;
            }
            
            clientMap = new HashMap(totalLines);
            try(Scanner sreader = new Scanner(users)){
                while(sreader.hasNextLine()){
                    clientMap.put(sreader.nextLine(), sreader.nextLine());
                }
            }catch(Exception e){
                System.out.println(e + "error putting elements in hash");
            }
        }catch(Exception e){
            System.out.println(e + "error generating hash");
        }
        System.out.println("User Hashmap Generated");
    }

    /**
     * Gets the username from a client
     * @param a The client's ip address
     * @return The username
     */
    public static String getUsername(String a){
        System.out.println("ip is: " + a);
		if(clientMap.containsKey(a)){
			System.out.println(clientMap.keySet());
			System.out.println(clientMap.get(a));
			return (String) clientMap.get(a);
		}else{
			return "unnamed";
		}
    }

    /**
     * Sets a new client's username
     * @param ip The client's ip
     * @param username The username to set
     */
	public static void setUsername(String ip, String username){
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(LOCATION, true));
			writer.write(ip);
			writer.newLine();
			writer.write(username);
			writer.newLine();
			writer.close();
			generateHash();
		}catch(Exception e){
			System.out.println(e + "error setting username");
		}
	}

    /**
     * Sends messages to the clients
     * @param name The name of the client sending the message
     * @param line The message the client is sending
     */
	public static void printMessage(String name, String line){
        for(ConnectionThread c: Server.clients){
            c.showMessage(name, line);
        }
        System.out.println(name + ": " + line);
    }
}
