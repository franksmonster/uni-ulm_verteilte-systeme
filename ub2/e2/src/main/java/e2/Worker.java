
package e2;

import java.math.BigInteger;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

public class Worker {

    public static void main(String[] args) {

        try (ZContext context = new ZContext()) {

            ZMQ.Socket receiver =
                context.createSocket(SocketType.PULL);

            receiver.connect("tcp://localhost:5555");

            ZMQ.Socket sender =
                context.createSocket(SocketType.PUSH);

            sender.connect("tcp://localhost:5556");

            System.out.println("Worker wartet auf Challenges...");

            while (true) {

                String message = receiver.recvStr();

                System.out.println("Empfangen: " + message);

                BigInteger n = new BigInteger(message);

                BigInteger[] factors =
                    Fermat.fermatFactorization(n);

                if (factors.length == 2) {

                    String result = n + ":" +
                                    factors[0] + ":" +
                                    factors[1];

                    sender.send(result);

                    System.out.println("Ergebnis: " + result);
                }
            }
        }
    }
}
