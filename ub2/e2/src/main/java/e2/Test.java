
package e2;

public class Test {

    public static void main(String[] args) {

        Thread controllerThread = new Thread(() -> {
            Controller.main(new String[]{});
        });

        Thread workerThread = new Thread(() -> {
            Worker.main(new String[]{});
        });

        controllerThread.start();
        workerThread.start();

    }
}
