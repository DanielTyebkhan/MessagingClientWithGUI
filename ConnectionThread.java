import java.io.*;
import java.util.*;
import java.net.*;

/**
 * @author Daniel Tyebkhan
 */
public class ConnectionThread extends Thread{
    private InputStream inputToServer;
    private OutputStream outputFromServer;
    private Scanner scanner;
    private PrintWriter serverPrintOut;
    private Socket connectionSocket;
    private String IP;
    private String name;
    private int colonIndex;

    /**
     * Constructor
     * @param connectionSocket The socket to connect to
     */
    public ConnectionThread(Socket connectionSocket){
        try{
            IP = connectionSocket.getRemoteSocketAddress().toString();
            colonIndex = IP.indexOf(":");
            IP = IP.substring(1, colonIndex);
            name = Server.getUsername(IP);
            this.connectionSocket = connectionSocket;
            inputToServer = connectionSocket.getInputStream();
            outputFromServer = connectionSocket.getOutputStream();
            scanner = new Scanner(inputToServer, "UTF-8");
            serverPrintOut = new PrintWriter(new OutputStreamWriter(outputFromServer, "UTF-8"), true);
			if(name.equals("unnamed")){
                pickUsername();
				name = Server.getUsername(IP);
            }
            serverPrintOut.println("Hello " + name + ", Welcome to the Server! Type 'exit' to leave");
        }catch(Exception e){
            System.out.println(e);
        }
    }

    /**
     * Gets and sends input to the server
     */
    @Override 
    public void run(){
        boolean done = false;
        while(!done && scanner.hasNextLine()){
            String line = scanner.nextLine();
            Server.printMessage(name, line);
            if(line.toLowerCase().trim().equals("exit")){
                serverPrintOut.println("Terminating Connection");
                try{connectionSocket.close();}catch(Exception e){System.out.println(e);}
                done = true;
            }
        }
    }

    /**
     * Allows new users to pick a username
     */
    public void pickUsername(){
        serverPrintOut.println("Please Choose a Username:");
        Scanner usernameScanner = new Scanner(inputToServer, "UTF-8");
		Server.setUsername(IP, usernameScanner.nextLine());
    }
}
