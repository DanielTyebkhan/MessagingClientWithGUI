import java.awt.*;
import java.io.BufferedReader;
import java.io.*;
import javax.swing.*;
import java.net.*;
import java.util.*;
import java.awt.event.*;

class UserGUI{
    String IP;
    int port;
    PrintWriter out;
    BufferedReader in;
    JFrame frame;
    JTextField messagefield;
    JPanel spanel;
    JTextArea feed;
    JButton sendbutton;
    Socket socket;
    InputStream inputToServer;
    OutputStream outputFromServer;
    Scanner scanner;
    PrintWriter serverPrintOut;
    Boolean firstprint;

    public UserGUI(){
        firstprint = true;
        requestConnection();
        connectToServer();
        initializeGUI();
        runMessaging();
    }

    public void runMessaging(){
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            if(firstprint){
                feed.setText(line);
                firstprint = false;
            }else{
                feed.setText(feed.getText()+ "\n" + line);
            }
        }
    }

    public void requestConnection(){
        IP = JOptionPane.showInputDialog(null, "IP Address");
        port = Integer.parseInt(JOptionPane.showInputDialog(null, "Port"));
    }

    public void connectToServer(){
        try{
            socket = new Socket(IP, port);
            inputToServer = socket.getInputStream();
            outputFromServer = socket.getOutputStream();
            scanner = new Scanner(inputToServer, "UTF-8");
            serverPrintOut = new PrintWriter(new OutputStreamWriter(outputFromServer, "UTF-8"), true);
        }catch(Exception e){
            JOptionPane.showMessageDialog(frame, "There was an error in connection. Please Try Again", "Connection Error", JOptionPane.ERROR_MESSAGE);
            requestConnection();
            connectToServer();
        }
    }

    public void initializeGUI(){
        frame = new JFrame("Messaging Client");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        feed = new JTextArea();
        feed.setEditable(false);
        frame.add(feed, BorderLayout.CENTER);
        
        spanel = new JPanel();
        spanel.setLayout(new BoxLayout(spanel, BoxLayout.PAGE_AXIS));
        
        messagefield = new JTextField("Enter Message Here");
        messagefield.addKeyListener(new EnterListener());
        spanel.add(messagefield);

        sendbutton = new JButton("Send");
        sendbutton.addActionListener(new ButtonListener());
        spanel.add(sendbutton);

        frame.add(spanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
    public static void main(String[] args){
        UserGUI u = new UserGUI();
    }

    public class ButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            serverPrintOut.println(messagefield.getText());
            messagefield.setText("");
        }
    }
    
    public class EnterListener implements KeyListener{
        @Override
        public void keyPressed(KeyEvent e){
            if(e.getKeyCode() == KeyEvent.VK_ENTER){
                serverPrintOut.println(messagefield.getText());
                messagefield.setText("");
            }
        }
        @Override
        public void keyReleased(KeyEvent e){

        }
        @Override
        public void keyTyped(KeyEvent e){

        }
    }
}