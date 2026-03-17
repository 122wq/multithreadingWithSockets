package com.example;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class SocketClientExample {
	private InetAddress host;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ios;
	
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
            host = InetAddress.getLocalHost();
            socket = new Socket(host.getHostName(), 9876);
            //write to socket using ObjectOutputStream
            oos = new ObjectOutputStream(socket.getOutputStream());
            ios = new ObjectInputStream(socket.getInputStream());
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

    private class InputThread extends Thread
    {
        Scanner myObj;
        String message;
        public InputThread()
        {
            myObj =  new Scanner(System.in);
        }
        public void run()
        {
            try {
                while(true)
                {
                    System.out.println("Enter your message: ");
                    message = myObj.nextLine();
                    oos.writeObject(message);
                }
               
            } catch (IOException e) {
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
                    String messageFromServer = (String) ios.readObject();
                    System.out.println(messageFromServer);
                    //add something else (like a button) later
                    if (messageFromServer.equalsIgnoreCase("exit"))
                    {
                        break;
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{
        //get the localhost IP address, if server is running on some other IP, you need to use that
        InetAddress host = InetAddress.getLocalHost();
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        /* 
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
       SocketClientExample inputTest = new SocketClientExample();
        
    }
}
