import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import Demo.CallbackReceiverPrx;

public class Task implements Runnable {
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

    /**
     * Method responsible for returning the message
     * 
     * @return message
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Method responsible for returning the proxy
     * 
     * @return
     */
    public CallbackReceiverPrx getProxy() {
        return this.proxy;
    }

    /**
     * Method responsible for returning the handler
     * 
     * @return
     */
    public TaskHandler getHandler() {
        return this.handler;
    }

    /**
     * Method responsible for running the task
     */
    @Override
    public void run() {
        String host = this.message.split(":", 2)[0]; // host: message
        String message = this.message.split(":", 2)[1].toLowerCase(); // host: message
        try {
            if (message.startsWith("exit")) {
                this.handler.removeClient(host);
            } else if (message.startsWith("list clients")) { // list clients
                replyHosts(getHosts());
            } else if (message.startsWith("bc")) { // bc message
                emitBroadcast(host, message);
            } else if (message.startsWith("to")) { // to host:message
                String to = message.replace("to", "").trim().split(":", 2)[0];
                String msg = message.replace("to", "").trim().split(":", 2)[1];
                send(host, to, msg);
            } else { // Fibonacci Calculation
                this.proxy.callback(validationLayer(this.message));
            }
        } catch (InterruptedException e) {
            sem.release();
            e.printStackTrace();
        }
    }

    /**
     * Method responsible for validating the message
     * 
     * @param message
     * @return response
     */
    public String validationLayer(String message) {
        String response = 0 + "";
        String print = message;
        try {
            int number = Integer.parseInt(message.trim().split(":", 2)[1]);
            if (number > 0) {
                List<Integer> seq = this.fibonacciSequence(number);
                print = message.split(":", 2)[0] + ":" + seq.toString();
                response = (number != 1 && number != 2) ? this.fibonacciValue(seq) + "" : 1 + "";
            }
        } catch (Exception e) {}
        System.out.println(print);
        return response;
    }

    /**
     * Method responsible for generating the fibonacci sequence
     * 
     * @param number
     * @return fibonacci sequence
     */
    public List<Integer> fibonacciSequence(int number) {
        return Stream.iterate(new int[] { 1, 1 }, t -> new int[] { t[1], t[0] + t[1] }).limit(number).map(n -> n[0])
                .collect(Collectors.toList());
    }

    /**
     * Method responsible for generating the fibonacci value
     * 
     * @param seq
     * @return fibonacci value
     */
    public int fibonacciValue(List<Integer> seq) {
        return seq.subList(seq.size() - 2, seq.size()).stream().mapToInt(Integer::intValue).sum();
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
        String response = "\n Hosts: \n";
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
    public void emitBroadcast(String source, String message) throws InterruptedException {
        System.out.println("Broadcasting message from " + source + " to all clients");
        sem.acquire();
        for (String host : this.handler.getClients().keySet()) {
            if (!host.equals(source)) {
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
    public void send(String from, String to, String message) throws InterruptedException {
        System.out.println("Sending message from " + from + " to " + to);
        sem.acquire();
        if (this.handler.getClients().containsKey(to)) {
            this.handler.getClients().get(to).callback(from + ": " + message);
        } else {
            this.handler.getClients().get(from).callback("Host " + to + " not found");
        }
        sem.release();
    }

}
