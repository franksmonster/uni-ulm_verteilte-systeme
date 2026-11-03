
package e2;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

public class Controller {

    public static void main(String[] args) {

        try (ZContext context = new ZContext()) {

            ZMQ.Socket subscriber =
                context.createSocket(SocketType.SUB);

            subscriber.connect("tcp://vs.lxd-vs.uni-ulm.de:27378");
            subscriber.subscribe("");

            ZMQ.Socket sender =
                context.createSocket(SocketType.PUSH);

            sender.bind("tcp://*:5555");

            ZMQ.Socket receiver =
                context.createSocket(SocketType.PULL);

            receiver.bind("tcp://*:5556");

            ZMQ.Socket requester =
                context.createSocket(SocketType.REQ);

            requester.connect("tcp://vs.lxd-vs.uni-ulm.de:27379");

            System.out.println("Controller gestartet!");

            while (true) {

                String message = subscriber.recvStr();

                System.out.println("Empfangene Aufgabe: " + message);

                sender.send(message);

                String result = receiver.recvStr();

                System.out.println("Ergebnis: " + result);

                requester.send(result);

                String response = requester.recvStr();

                System.out.println("Publisher Antwort: " + response);
            }
        }
    }
}
