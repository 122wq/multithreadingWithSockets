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
    


    public static void main(String[] args) 
    {
        new ChatServerWithThreads();
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
                
                ConnectionHandler c = new ConnectionHandler(connection);
                c.start();
               
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
        private static ArrayList<ConnectionHandler> handlers;
        Socket client;
        ObjectOutputStream oos;
        ObjectInputStream ois;
        ConnectionHandler(Socket socket) 
        {
            client = socket;
            if (handlers == null)
            {
                handlers = new ArrayList<ConnectionHandler>();
            }
            handlers.add(this);
        }
        public void run() 
        {
            String clientAddress = client.getInetAddress().toString();
	        try 
            {
	            //your code to send messages goes here.
                System.out.println("Connecting");
                String messageFromClient;
                ois = new ObjectInputStream(client.getInputStream());
                oos = new ObjectOutputStream(client.getOutputStream());
                while(true)
                { 
                    messageFromClient = (String) ois.readObject();
                    System.out.println(messageFromClient);
                    if (messageFromClient.equalsIgnoreCase("exit"))
                    {
                        System.out.println("Client " + clientAddress + " disconnects normally");
                        break;
                    }
                    
                    for (int i = 0; i < handlers.size(); i++)
                    {
                        handlers.get(i).oos.writeObject("Someone says " + messageFromClient);
                        handlers.get(i).oos.flush();
                    }
                        
                }
	        }
	        catch (EOFException e)
            {
	            // Client closed the connection; treat this as a abnormal disconnect.
	            System.out.println("Client disconnected unexpectedly: " + clientAddress);
	        }
	        catch (Exception e)
            {
	            System.out.println("Error on connection with: " 
	                     + clientAddress + ": " + e);
	        }
            //closes the client 
            finally
            {
                try {
                    if (ois != null) ois.close();
                    if (oos != null) oos.close(); 
                    client.close();
                } catch (Exception e) {
                }
                handlers.remove(this);
            }
        
        }
    }


}
