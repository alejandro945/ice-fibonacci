import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import Demo.CallbackReceiverPrx;

public class Task implements Runnable{
    private String message;
    private CallbackReceiverPrx proxy;
    private TaskHandler handler;
    private Semaphore sem;

    public Task(String message, CallbackReceiverPrx proxy, TaskHandler handler) {
        this.message = message;
        this.proxy = proxy;
        this.handler = handler;
        this.sem = handler.getSemaphore();
    }

    public String getMessage() {
        return this.message;
    }

    public CallbackReceiverPrx getProxy() {
        return this.proxy;
    }

    public TaskHandler getHandler() {
        return this.handler;
    }

    @Override
    public void run() {
        String host = this.message.split(":", 2)[0];
        String message = this.message.split(":", 2)[1];

    }

    /**
     * Method responsible for returning the hosts
     * 
     * @return hosts
     * @throws InterruptedException
     */
    public ArrayList<String> getHosts() throws InterruptedException {
        sem.acquire();
        ArrayList<String> hosts = new ArrayList<String>();
        for (String host : this.handler.getClients().keySet()) {
            hosts.add(host);
        }
        sem.release();
        return hosts;
    }

    /**
     * Method responsible for replying the hosts
     * 
     * @param hosts
     */
    public void replyHosts(ArrayList<String> hosts) {
        String response = "Hosts:\n";
        for (String host : hosts) {
            response += "\t" + host + "\n";
        }
        this.proxy.callback(response);
    }

    /**
     * Method responsible for emitting a broadcast
     * 
     * @param source
     * @param message
     * @throws InterruptedException
     */
    public void emitBroadcast(String source, String message) throws InterruptedException{
        System.out.println("Broadcasting message from " + source + " to all clients");
        sem.acquire();
        for (String host : this.handler.getClients().keySet()) {
            if(!host.equals(source)){
                this.handler.getClients().get(host).callback(source + ": " + message);
            }
        }
        sem.release();
    }

    /**
     * Method responsible for sending a message
     * 
     * @param from
     * @param to
     * @param message
     * @throws InterruptedException
     */
    public void send(String from, String to, String message) throws InterruptedException{
        System.out.println("Sending message from " + from + " to " + to);
        sem.acquire();
        if(this.handler.getClients().containsKey(to)){
            this.handler.getClients().get(to).callback(from + ": " + message);
        }
        sem.release();
    }

}
