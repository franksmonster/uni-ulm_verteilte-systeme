package ub2a1;

import org.zeromq.SocketType;

import org.zeromq.ZMQ;
import org.zeromq.ZContext;

public class Hwserver {

    public static void main(String[] args) throws Exception
    {
    	System.out.println("Starten mit try{...}");
        try (ZContext context = new ZContext()) {
            // Socket to talk to clients
            ZMQ.Socket socket = context.createSocket(SocketType.REP);
            socket.bind("tcp://*:5555");
            System.out.println("Gebindet?");

            while (!Thread.currentThread().isInterrupted()) {
                // Block until a message is received
                byte[] reply = socket.recv(0);

                // Print the message
                System.out.println(
                    "Received: [" + new String(reply, ZMQ.CHARSET) + "]"
                );

                // Send a response
                String response = "Hello, world!";
                socket.send(response.getBytes(ZMQ.CHARSET), 0);
                System.out.println("Hallo aus wartendem JeroMQ REP-Socket!");
            }
            System.out.println(":(");
        }
    }
}