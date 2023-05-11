import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Util;

import Demo.CallbackReceiverPrx;
import Demo.CallbackSenderPrx;

public class Client {
    // Communicator
    static com.zeroc.Ice.Communicator communicator;

    /**
     * Main method
     * @param args 
     */
    public static void main(String[] args) {
        java.util.List<String> extraArgs = new java.util.ArrayList<>();
        communicator = com.zeroc.Ice.Util.initialize(args, "config.client", extraArgs);
        Demo.CallbackSenderPrx server = serverConfiguration();
        Demo.CallbackReceiverPrx client = clientConfiguration();
        if (server == null || client == null)
            throw new Error("Invalid proxy");
        runProgram(server, client);
        communicator.shutdown();
        communicator.destroy();
    }

    /**
     * Method responsible for running the program
     * @param server 
     * @param client 
     */
    public static void runProgram(CallbackSenderPrx server, CallbackReceiverPrx client) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            String hostname = InetAddress.getLocalHost().getHostName();
            System.out.println("Welcome please type a  \n 1. number [Fibonacci] \n 2. list clients [List of all host avaible] \n 3. bc <<msg>> or to <<host:msg>> [Send a particular msg] \n 4. exit ");
            String message = br.readLine();
            while (!message.equalsIgnoreCase("exit")) {
                long startTime = System.nanoTime();
                server.initiateCallback(client, hostname + ":" + message);
                showTime(System.nanoTime() - startTime);
                message =  br.readLine();
            }
            server.initiateCallback(client, hostname + ":" + message); // exit
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Method responsible for showing time
     * @param elapsed time
     */
    public static void showTime(long elapsed) {
        long elapsedMillis = TimeUnit.MILLISECONDS.convert(elapsed, TimeUnit.NANOSECONDS);
        long elapsedSecs = TimeUnit.SECONDS.convert(elapsed, TimeUnit.NANOSECONDS);
        System.out.println("Time: " + (elapsedMillis) + " ms, " + (elapsedSecs) + " s");
    }

    /**
     * Method responsible for client callback creation
     * @return client callback
     */
    public static Demo.CallbackReceiverPrx clientConfiguration() {
        ObjectAdapter adapter = communicator.createObjectAdapter("Callback.Client");
        com.zeroc.Ice.Object obj = new CallbackReceiver();
        ObjectPrx objectPrx = adapter.add(obj, Util.stringToIdentity("callbackReceiver"));
        adapter.activate();
        return Demo.CallbackReceiverPrx.uncheckedCast(objectPrx);
    }

    /**
     * Method responsible for server callback creation
     * @return server callback
     */
    public static Demo.CallbackSenderPrx serverConfiguration() {
        Demo.CallbackSenderPrx twoway = Demo.CallbackSenderPrx
                .checkedCast(communicator.propertyToProxy("Printer.Proxy")).ice_twoway().ice_secure(false);
        return twoway.ice_twoway();
    }

}