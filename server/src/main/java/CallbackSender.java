import com.zeroc.Ice.Current;

import Demo.CallbackReceiverPrx;

public class CallbackSender implements Demo.CallbackSender {

    @Override
    public void initiateCallback(CallbackReceiverPrx proxy, String message, Current current) {
        TaskHandler handler = TaskHandler.getInstance();
        Task task = new Task(message, proxy, TaskHandler.getInstance());
        handler.addClient((m) -> m.split(":", 2)[0], message, proxy);
        handler.execute(task);
    }

}