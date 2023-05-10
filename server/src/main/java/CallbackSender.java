
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.zeroc.Ice.Current;

import Demo.CallbackReceiverPrx;

public class CallbackSender implements Demo.CallbackSender {

    @Override
    public void initiateCallback(CallbackReceiverPrx proxy, String message, Current current) {
        proxy.callback(validationLayer(message));
    }

    /**
     * Method responsible for validating the message
     * @param message 
     * @return response
     */
    public String validationLayer(String message) {
        String response = 0 + "";
        String print = message;
        try {
            int number = Integer.parseInt(message.split(":", 2)[1]);
            if (number > 0) {
                List<Integer> seq = fibonacciSequence(number);
                print = message.split(":", 2)[0] + ":" + seq.toString();
                response = (number != 1 && number != 2) ? fibonacciValue(seq) + "" : 1 + "";
            }
        } catch (Exception e) {}
        System.out.println(print);
        return response;
    }

    /**
     * Method responsible for generating the fibonacci sequence
     * @param number
     * @return fibonacci sequence
     */
    public List<Integer> fibonacciSequence(int number) {
        return Stream.iterate(new int[] { 1, 1 }, t -> new int[] { t[1], t[0] + t[1] }).limit(number).map(n -> n[0])
                .collect(Collectors.toList());
    }

    /**
     * Method responsible for generating the fibonacci value
     * @param seq 
     * @return fibonacci value
     */
    public int fibonacciValue(List<Integer> seq) {
        return seq.subList(seq.size() - 2, seq.size()).stream().mapToInt(Integer::intValue).sum();
    }

}