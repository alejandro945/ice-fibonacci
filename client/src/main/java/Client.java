public class Client
{
    public static void main(String[] args)
    {

        long start = 0;
        long end = 0;
        start = System.currentTimeMillis();

        if (args.length == 0) {
            System.out.println("Debe proporcionar el número de la serie Fibonacci como argumento");
            return;
        }
        
        java.util.List<Integer> numbers = new java.util.ArrayList<>();

        try {
            for (String arg : args) {
                numbers.add(Integer.parseInt(arg));
            }
        } catch (NumberFormatException e) {
            System.out.println("El valor ingresado no es un numero");
            return;
        }

        java.util.List<String> extraArgs = new java.util.ArrayList<>();

        try(com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args,"config.client",extraArgs))
        {
            com.zeroc.Ice.ObjectPrx base = communicator.propertyToProxy("Printer.Proxy");
            Demo.PrinterPrx printer = Demo.PrinterPrx.checkedCast(base);

            String hostname = java.net.InetAddress.getLocalHost().getHostName();

            if(printer == null)
            {
                throw new Error("Invalid proxy");
            }
            


            for(int number : numbers){
                if (number < 0)
                    System.out.println("Respuesta: " + 0);
                else
                    System.out.println(printer.printAnswer(number, hostname));
                    end = System.currentTimeMillis();
                    System.out.println("Se calculo el fibonacci: " + hostname + ": " + number + " con un Tiempo de respuesta: " + (end - start) + " ms");
            }
            System.exit(0);

        }catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}