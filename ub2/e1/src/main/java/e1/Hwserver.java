
package e1;

import java.util.concurrent.ThreadLocalRandom;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

public class Hwserver {

    // Connection Info.
    static String url = "tcp://vs.lxd-vs.uni-ulm.de:27401";
    static String gameID;

    // Binary Search limits.
    static long low = 1;
    static long high = Long.MAX_VALUE;

    public static void main(String[] args) {

        try (ZContext context = new ZContext()) {

            ZMQ.Socket socket = context.createSocket(SocketType.REQ);

            try {
                socket.connect(url);

                // Generate random initial gameID.
                gameID = Long.toString(
                    ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE)
                );

                // Initial request.
                String req = gameID + ":1";

                System.out.println("Sending initial request: " + req);

                socket.send(req.getBytes(ZMQ.CHARSET), 0);

                // Receive initial reply.
                byte[] rep = socket.recv(0);
                String stringRep = new String(rep, ZMQ.CHARSET);

                System.out.println("Received: " + stringRep);

                // Extract current gameID.
                gameID = extractGameID(stringRep);

                System.out.println("Current GameID: " + gameID);

                // Binary Search.
                while (true) {

                    // Calculate median.
                    long guess = low + (high - low) / 2;

                    // Create request.
                    req = gameID + ":" + guess;

                    System.out.println("Sending: " + req);

                    socket.send(req.getBytes(ZMQ.CHARSET), 0);

                    // Receive reply.
                    rep = socket.recv(0);

                    stringRep = new String(rep, ZMQ.CHARSET);

                    System.out.println("Received: " + stringRep);

                    // Correct guess.
                    if (stringRep.startsWith("Correct guess")) {

                        System.out.println("GEWONNEN!");

                        // Extract new gameID.
                        gameID = extractGameID(stringRep);

                        break;
                    }

                    // Game already guessed or unknown ID.
                    if (stringRep.startsWith(
                            "This number has been guessed already.")
                            || stringRep.startsWith("GameID unknown!")) {

                        gameID = extractGameID(stringRep);

                        System.out.println("New GameID: " + gameID);

                        // Reset search limits.
                        low = 1;
                        high = Long.MAX_VALUE;

                        continue;
                    }

                    // Guess too small.
                    if (stringRep.contains("too small")) {

                        if (guess > Long.MAX_VALUE - 2) {
                            System.out.println("Search limit reached.");
                            break;
                        }

                        low = guess + 2;

                        // Increase upper limit if possible.
                        if (high < Long.MAX_VALUE) {
                            high++;
                        }

                    }

                    // Guess too large.
                    else if (stringRep.contains("too large")) {

                        low++;

                        high = guess;

                    }

                    else {

                        System.out.println("Unknown server reply!");
                        break;
                    }

                    // Check whether search interval is still valid.
                    // Note: Has not happened yet.
                    if (low > high) {

                        System.out.println("Search interval exhausted!");
                        break;
                    }
                }

            } finally {
                socket.close();
            }
        }
    }
    
    /**
     * Extracts the game ID from a server response.
     *
     * @param response the response received from the server
     * @return the extracted game ID
     */
    static String extractGameID(String response) {

        String[] words = response.trim().split("\\s+");

        return words[words.length - 1];
    }
}
