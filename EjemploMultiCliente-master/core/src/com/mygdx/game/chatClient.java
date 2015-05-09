package com.mygdx.game;

/**
 * Created by alecorleonis on 05-07-15.
 * from https://javadeveloperszone.wordpress.com/2013/04/20/java-tcp-chat-multiple-client/
 */
import java.net.*;
import java.io.*;

public class chatClient{
    static Socket clientSocket;
    public static void main(String argv[]) throws Exception {
        String sentence;
        BufferedReader inFromUser =
                new BufferedReader(
                        new InputStreamReader(System.in));

        clientSocket = new Socket("localhost", 6789);

        Thread t = new Thread(new MyServerListener());
        t.start();

        while (true) {
            DataOutputStream outToServer =
                    new DataOutputStream(
                            clientSocket.getOutputStream());
            sentence = inFromUser.readLine();
            outToServer.writeBytes(sentence + '\n');
            if (sentence.equals("EXIT")) {
                break;
            }
        }
        clientSocket.close();
    }
}

class MyServerListener implements Runnable {

    String modifiedSentence;
    @Override
    public void run() {
        while (true) {
            try {
                BufferedReader inFromServer  = new BufferedReader(
                        new InputStreamReader(
                                chatClient.clientSocket.getInputStream()));
                modifiedSentence = inFromServer.readLine();

                System.out.println("FROM SERVER: " + modifiedSentence);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}