import java.net.*;
import java.io.*;
import java.util.*;
public class Server{ 
    public static final int PORT = 9991;
    public static ArrayList<ConnectionThread> clients;
    public static HashMap clientMap;
    public static void main(String[] args){
        clients = new ArrayList <ConnectionThread>();
        generateHash();
        connectToServer();
    }

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
                    System.out.println(e);
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    public static void generateHash(){
        System.out.println("Generating User Hashmap");
        File users = new File("users.txt");
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
                System.out.println(e);
            }
        }catch(Exception e){
            System.out.println(e);
        }
        System.out.println("User Hashmap Generated");
    }
    
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
	
	public static void setUsername(String ip, String username){
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(Server.class.getProtectionDomain().getCodeSource().getLocation().getFile() +"users.txt", true));
			writer.write(ip);
			writer.newLine();
			writer.write(username);
			writer.newLine();
			writer.close();
			generateHash();
		}catch(Exception e){
			System.out.println(e);
		}
	}
}
