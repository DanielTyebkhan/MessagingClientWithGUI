import java.awt.*;
import java.io.BufferedReader;
import java.io.*;
import javax.swing.*;
import java.net.*;
import java.util.*;
import java.awt.event.*;

class UserGUI{
    private String IP;
    private int port;
    private JFrame frame;
    private JTextField messagefield;
    private JPanel spanel;
    private JPanel sbspanel;
    private JPanel npanel;
    private JTextArea feed;
    private JButton sendbutton;
    private JButton connectbutton;
    private Socket socket;
    private InputStream inputToServer;
    private OutputStream outputFromServer;
    private Scanner scanner;
    private PrintWriter serverPrintOut;
    private Boolean firstprint;

    /**
     * Constructor
     */
    public UserGUI(){
        firstprint = true;
        while(!requestConnection()){
            requestConnection();
        }
        connectToServer();
        initializeGUI();
        runMessaging();
    }

    /**
     * Sets the text in the window
     */
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

    /**
     * Attempts to connect to the inputted ip and port
     * @return true if the connection is successful, else false
     */
    public boolean requestConnection(){
        IP = JOptionPane.showInputDialog(null, "IP Address");
        if(IP==null){
            if(firstprint){
                System.exit(0);
                return false;
            }else{
                return false;
            }
        }
        String iport = JOptionPane.showInputDialog(null, "Port");
        if(iport == null){
            if(firstprint){
                System.exit(0);
                return false;
            }else{
                return false;
            }
        }else{
            port = Integer.parseInt(iport);
        }
        return true;
    }

    /**
     * Connects the user to the server
     */
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

    /**
     * Creates the GUI
     */
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

        sbspanel = new JPanel();

        messagefield = new JTextField("Enter Message Here");
        messagefield.addKeyListener(new EnterListener());
        spanel.add(messagefield);

        sendbutton = new JButton("Send");
        sendbutton.setSize(frame.getWidth(), 50);
        sendbutton.addActionListener(new SendButtonListener());
        sbspanel.add(sendbutton);
        spanel.add(sbspanel);

        npanel = new JPanel();

        connectbutton = new JButton("Change Connection");
        connectbutton.addActionListener(new ConnectButtonListener());
        connectbutton.setSize(frame.getWidth(), 50);
        npanel.add(connectbutton);

        frame.add(npanel, BorderLayout.NORTH);
        frame.add(spanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    /**
     * Sends messages when the send button is clicked
     */
    public class SendButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            serverPrintOut.println(messagefield.getText());
            messagefield.setText("");
        }
    }

    /**
     * Creates a new connection when the connect button is clicked
     */
    public class ConnectButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            if(requestConnection()){
                connectToServer();
            }
        }
    }

    /**
     * Sends a message when 'enter' is pressed
     */
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

    public static void main(String[] args){
        UserGUI u = new UserGUI();
    }
}