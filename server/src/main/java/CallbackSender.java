import com.zeroc.Ice.Current;

import Demo.CallbackReceiverPrx;

public class CallbackSender implements Demo.CallbackSender {

    @Override
    public void initiateCallback(CallbackReceiverPrx proxy, String message, Current current) {
        proxy.callback(validationLayer(message));
    }

    public String validationLayer(String message) {
        String response = "";
        try {
            int number = Integer.parseInt(message.split(":", 2)[1]);
            if (number > 0) {
                response = fibonacci(number) + "";
            }
        } catch (Exception e) {
            System.out.println(message + "Error you must send a number");
            response = 0 + "";
        }
        return response;
    }

    public Integer fibonacci(int number) {
        return (number > 2) ? fibonacci(number - 1) + fibonacci(number - 2) : 1;
    }

}