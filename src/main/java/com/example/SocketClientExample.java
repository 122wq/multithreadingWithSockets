package com.example;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.*;
import java.awt.*;

public class SocketClientExample {
	private InetAddress host;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private JFrame gui;
    private JTextField textBox;

	private JTextArea textArea;
    private JButton submitButton;
	/*
	 * Modify this example so that it opens a dialogue window using java swing, 
	 * takes in a user message and sends it
	 * to the server. The server should output the message back to all connected clients
	 * (you should see your own message pop up in your client as well when you send it!).
	 *  We will build on this project in the future to make a full fledged server based game,
	 *  so make sure you can read your code later! Use good programming practices.
	 *  ****HINT**** you may wish to have a thread be in charge of sending information 
	 *  and another thread in charge of receiving information.
	*/
    
    public SocketClientExample()
    {
        //get the localhost IP address, if server is running on some other IP, you need to use that
        try {
            GuiSetUp();
            host = InetAddress.getLocalHost();
            socket = new Socket(host.getHostName(), 9876);
            //write to socket using ObjectOutputStream
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            InputThread i = new InputThread();
            OutputThread o = new OutputThread();
            i.start();
            o.start();
            // wait for the send to complete before closing
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
    }
    private void GuiSetUp()
    {
        gui = new JFrame("Chat Client");
        gui.setLayout(new GridLayout(3,1));
        
        textArea = new JTextArea("Enter Stuff Below: \n");
        textArea.setEditable(false);
        JScrollPane scroll = new JScrollPane (textArea);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        textBox = new JTextField();
        submitButton = new JButton("Submit");
        
        gui.add(scroll);
        gui.add(textBox);
        gui.add(submitButton);
  
        gui.setSize(400,400);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setVisible(true);
    }
    private class InputThread extends Thread
    {
        String message;
        boolean pressed;
        public InputThread()
        {
            pressed = false;
            submitButton.addActionListener(e ->
            {
                pressed = true;
            });
        }
        public synchronized void run()
        {
            try 
            {
                while(true)
                {
                    message = "";
                    if (pressed)
                    {
                        message = textBox.getText();
                        oos.writeObject(message);
                        oos.flush();
                        this.wait(100);
                        pressed = false;
                    }
                }
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private class OutputThread extends Thread
    {
        public void run()
        {
            try {
                while(true)
                {
                    String messageFromServer = (String) ois.readObject();
                    textArea.append(messageFromServer + "\n");
                    System.out.println("" + messageFromServer);
                    //add something else (like a button) later
                    
                }
            }catch (EOFException e){
                System.out.println("Disconnected from Server");
                gui.dispose();
                System.exit(0);
            } 
             catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{
        //get the localhost IP address, if server is running on some other IP, you need to use that
        /* 
        InetAddress host = InetAddress.getLocalHost();
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        
        for(int i=0; i<5;i++){
            //establish socket connection to server
            socket = new Socket(host.getHostName(), 9876);
            //write to socket using ObjectOutputStream
            oos = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Sending request to Socket Server");
            if(i==4)oos.writeObject("exit");
            else oos.writeObject(""+i);
            //read the server response message
            ois = new ObjectInputStream(socket.getInputStream());
            String message = (String) ois.readObject();
            System.out.println("Message: " + message);
            //close resources
            ois.close();
            oos.close();
            Thread.sleep(100);
        }
        */
       new SocketClientExample();
        
    }
}
