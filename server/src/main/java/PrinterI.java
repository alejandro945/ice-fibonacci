public class PrinterI implements Demo.Printer
{
    public long printAnswer(long l, String hostname, com.zeroc.Ice.Current current)
    {
        
        String result = fibonacciSerieNumbers(l);
        System.out.println("Cliente: " + hostname + ": " + result);
        return fib(l);
    }

    private long fib(long n){
        if (n == 0) return 0;
        if (n == 1) return 1;
        return fib(n - 1) + fib(n - 2);
    }

    private String fibonacciSerieNumbers (long n){
        String serie = "";
        for (int i = 0; i <= n; i++) {
            serie += fib(i) + " ";
        }
        return serie;
    }
}