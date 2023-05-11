import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;

import Demo.CallbackReceiverPrx;

public class TaskHandler {
    // Constants
    private final int MAX_THREADS = Runtime.getRuntime().availableProcessors();
    // Instance
    private static TaskHandler instance = null;
    // Attributes
    private ExecutorService pool;
    private HashMap<String, CallbackReceiverPrx> clients;
    private Semaphore sem;

    /**
     * Method responsible for returning the instance
     * 
     * @return instance
     */
    private TaskHandler() {
        this.pool = java.util.concurrent.Executors.newFixedThreadPool(MAX_THREADS);
        this.clients = new HashMap<>();
        this.sem = new Semaphore(1);
    }

    /**
     * Method responsible for returning the instance
     * 
     * @return instance
     */
    public static TaskHandler getInstance() {
        if (instance == null)
            instance = new TaskHandler();
        return instance;
    }

    /**
     * Method responsible for returning the pool
     * 
     * @return pool
     */
    public ExecutorService getPool() {
        return this.pool;
    }

    /**
     * Method responsible for returning the clients
     * 
     * @return clients
     */
    public HashMap<String, CallbackReceiverPrx> getClients() {
        return this.clients;
    }

    /**
     * Method responsible for returning the semaphore
     * 
     * @return semaphore
     */
    public Semaphore getSemaphore() {
        return this.sem;
    }

    /**
     * Method responsible for executing a task
     * 
     * @param task
     */
    public void execute(Task task) {
        this.pool.execute(task);
    }

    /**
     * Method responsible for adding a client
     * 
     * @param lambda  (a) -> a.split(":", 2)[0] || null
     * @param message
     * @param client
     */
    public void addClient(Transform lambda, String message, CallbackReceiverPrx client) {
        String hostname = lambda.transform(message);
        try {
            this.sem.acquire();
            if (!this.clients.containsKey(hostname)) {
                this.clients.put(hostname, client);
                System.out.println(hostname + " joined. \n"); // Debug Porpuses
            }
        } catch (InterruptedException e) {
            System.out.println("[ERROR]" + hostname + " failed to join. \n");
            e.printStackTrace();
        } finally {
            this.sem.release();
        }
    }

    /**
     * Method responsible for removing a client
     * 
     * @param host
     */
    public void removeClient(String host) {
        try {
            this.sem.acquire();
            if (this.clients.containsKey(host)) {
                this.clients.remove(host);
                System.out.println(host + " left. \n"); // Debug Porpuses
            }
        } catch (InterruptedException e) {
            System.out.println("[ERROR]" + host + " failed to remove. \n");
            e.printStackTrace();
        } finally {
            this.sem.release();
        }
    }
}
