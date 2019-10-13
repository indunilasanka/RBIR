package aztec.rbir_backend;

import aztec.rbir_backend.globals.Global;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        new Global();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                System.out.println("In shutdown hook");
                Global.writeToFile();
            }
        }, "Shutdown-thread"));


    }
}
