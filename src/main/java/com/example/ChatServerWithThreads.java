package com.example;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This program is a server that takes connection requests on
 * the port specified by the constant LISTENING_PORT.  When a
 * connection is opened, the program should allow the client to send it messages. The messages should then 
 * become visible to all other clients.  The program will continue to receive
 * and process connections until it is killed (by a CONTROL-C,
 * for example). 
 * 
 * This version of the program creates a new thread for
 * every connection request.
 */
public class ChatServerWithThreads {

    public static final int LISTENING_PORT = 9876;
    private ArrayList<String> threads = new ArrayList<String>();


    public static void main(String[] args) 
    {
        ChatServerWithThreads server = new ChatServerWithThreads();
        

    }  // end main()

    public ChatServerWithThreads()
    {
        ServerSocket listener;  // Listens for incoming connections.
        Socket connection;      // For communication with the connecting program.

        /* Accept and process connections forever, or until some error occurs. */

        try {
            listener = new ServerSocket(LISTENING_PORT);
            System.out.println("Listening on port " + LISTENING_PORT);
            
            while (true) 
            {
                // Accept next connection request and handle it.
                connection = listener.accept();
                new ConnectionHandler(connection).start();
               
            }
        }
        catch (Exception e) {
            System.out.println("Sorry, the server has shut down.");
            System.out.println("Error:  " + e);
            return;
        }
    }
    /**
     *  Defines a thread that handles the connection with one
     *  client.
     */
    private static class ConnectionHandler extends Thread 
    {
        Socket client;
        ObjectOutputStream oos;
        ObjectInputStream ios;
        ConnectionHandler(Socket socket) 
        {
            client = socket;
        }
        public void run() 
        {
            String clientAddress = client.getInetAddress().toString();
	        try 
            {
	            //your code to send messages goes here.
                System.out.println("Connecting");
                
                ios = new ObjectInputStream(client.getInputStream());
                oos = new ObjectOutputStream(client.getOutputStream());
                while(true)
                {
                    String messageFromClient = (String) ios.readObject();
                    System.out.println(messageFromClient);
                    oos.writeObject("Client says " + messageFromClient);
                    oos.flush();
                    if (messageFromClient.equalsIgnoreCase("exit"))
                    {
                        break;
                    }
                }
	        }
	        catch (EOFException e)
            {
	            // Client closed the connection; treat this as a normal disconnect.
	            System.out.println("Client disconnected: " + clientAddress);
	        }
	        catch (Exception e)
            {
	            System.out.println("Error on connection with: " 
	                     + clientAddress + ": " + e);
	        }
            //close the client after the message was sent
            finally
            {
                try { 
                    
                    if (ios != null) ios.close();
                    if (oos != null) oos.close(); 
                    client.close();
                } catch (Exception e) {}
            }
            
        }
    }


}
