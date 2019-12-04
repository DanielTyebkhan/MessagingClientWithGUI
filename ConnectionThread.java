import java.io.*;
import java.util.*;
import java.net.*;
public class ConnectionThread extends Thread{
    InputStream inputToServer;
    OutputStream outputFromServer;
    Scanner scanner;
    PrintWriter serverPrintOut;
    Socket connectionSocket;
    String IP;
    String name;
    int colonIndex;
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

    @Override 
    public void run(){
        boolean done = false;
        while(!done && scanner.hasNextLine()){
            String line = scanner.nextLine();
            for(ConnectionThread c: Server.clients){
                c.serverPrintOut.println(name + ": " + line);
            }
            System.out.println(name + ": " + line);
            if(line.toLowerCase().trim().equals("exit")){
                serverPrintOut.println("Terminating Connection");
                try{connectionSocket.close();}catch(Exception e){System.out.println(e);}
                done = true;
            }
        }
    }

    public void pickUsername(){
        serverPrintOut.println("Please Choose a Username:");
        Scanner usernameScanner = new Scanner(inputToServer, "UTF-8");
		Server.setUsername(IP, usernameScanner.nextLine());
    }
}
