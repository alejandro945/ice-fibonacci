import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;

import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Util;

import Demo.CallbackReceiverPrx;
import Demo.CallbackSenderPrx;

public class Client {
    static com.zeroc.Ice.Communicator communicator;

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
     * 
     * @param server
     * @param client
     */
    public static void runProgram(CallbackSenderPrx server, CallbackReceiverPrx client) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            String hostname = InetAddress.getLocalHost().getHostName();
            System.out.print("Welcome please type a number 🫰: ");
            String message = br.readLine();
            while (!message.equalsIgnoreCase("exit")) {
                server.initiateCallback(client, hostname + ":" + message);
                System.out.print("Enter another number to calculate ⭐️: ");
                message =  br.readLine();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Method responsible for client callback creation
     * 
     * @return
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
     * 
     * @return
     */
    public static Demo.CallbackSenderPrx serverConfiguration() {
        Demo.CallbackSenderPrx twoway = Demo.CallbackSenderPrx
                .checkedCast(communicator.propertyToProxy("Printer.Proxy")).ice_twoway().ice_secure(false);
        return twoway.ice_twoway();
    }

}