import java.io.*;
import java.net.*;
import java.util.*;
class userconsole{
    public static void main(String[] args){
        try{
            Socket socket = new Socket("192.168.86.44", 9991);
            InputStream inputToServer = socket.getInputStream();
            OutputStream outputFromServer = socket.getOutputStream();
            Scanner scanner = new Scanner(inputToServer, "UTF-8");
            Scanner inputscanner = new Scanner(System.in);
            PrintWriter serverPrintOut = new PrintWriter(new OutputStreamWriter(outputFromServer, "UTF-8"), true);
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                System.out.println(line);
                //this line gets input but prevents from receiving server messages(just implemented to test)
                serverPrintOut.println(inputscanner.nextLine());
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
}