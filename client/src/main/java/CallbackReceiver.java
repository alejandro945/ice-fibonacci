import com.zeroc.Ice.Current;

public class CallbackReceiver implements Demo.CallbackReceiver {
    @Override
    public void callback(String message, Current current) {
        System.out.println("Server response: " + message);
    }
}
