package com.mygdx.game;

/**
 * Created by alecorleonis on 05-07-15.
 * from https://javadeveloperszone.wordpress.com/2013/04/20/java-tcp-chat-multiple-client/
 */
import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class chatServer {

    static ArrayList<Socket> client_sockets;

    public static void main(String argv[]) throws Exception {

        client_sockets = new ArrayList<Socket>();

        ServerSocket welcomeSocket = new ServerSocket(6789);

        Responder h = new Responder();
        // server runs for infinite time and
        // wait for clients to connect
        while (true) {
            // waiting..
            Socket connectionSocket = welcomeSocket.accept();
            client_sockets.add(connectionSocket);

// on connection establishment start a new thread for each client
            // each thread shares a common responder object
            // which will be used to respond every client request
            // need to synchronize method of common object not to have unexpected behaviour
            Thread t = new Thread(new MyServer(h, connectionSocket));

// start thread
            t.start();

        }
    }
}

class MyServer implements Runnable {

    Responder h;
    Socket connectionSocket;

    public MyServer(Responder h, Socket connectionSocket) {
        this.h = h;
        this.connectionSocket = connectionSocket;
    }

    @Override
    public void run() {

        while (h.responderMethod(connectionSocket)) {
            try {
                // once an conversation with one client done,
                // give chance to other threads
                // so make this thread sleep
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        try {
            connectionSocket.close();
        } catch (IOException ex) {
        }

    }

}

class Responder {

    String serverSentence;
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    // on client process termination or
    // client sends EXIT then to return false to close connection
    // else return true to keep connection alive
    // and continue conversation
    synchronized public boolean responderMethod(Socket connectionSocket) {
        try {
            BufferedReader inFromClient =
                    new BufferedReader(
                            new InputStreamReader(
                                    connectionSocket.getInputStream()));
            DataOutputStream outToClient =
                    new DataOutputStream(
                            connectionSocket.getOutputStream());
            String clientSentence = inFromClient.readLine();
// if client process terminates it get null, so close connection
            if (clientSentence == null || clientSentence.equals("EXIT")) {
                return false;
            }

            if (clientSentence != null) {
                System.out.println("client : " + clientSentence);
            }

            serverSentence = clientSentence +"\n";
            //serverSentence = br.readLine() + clientSentence +" \n";

            for(int i=0; i<chatServer.client_sockets.size();i++)
            {
                DataOutputStream data_output_stream =
                        new DataOutputStream(
                                chatServer.client_sockets.get(i).getOutputStream());
                data_output_stream.writeBytes(serverSentence);
            }

            //outToClient.writeBytes(serverSentence);

            return true;

        } catch (SocketException e) {
            System.out.println("Disconnected");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}