/**
 * A proof of concept application for distributed metrics slave agent.
 *
 * @version 1.0
 * @author Mikey Fennelly
 */
package ise;

public abstract class Sysinfo {

    private final String name;

    public Sysinfo(String name) {
        this.name = name;
    }

    public void printConsoleHeader() {
        System.out.println("===================================================");
        System.out.println(name);
        System.out.println("===================================================");
    }

    public abstract void printToConsole();

    public abstract void sendToOpenTelemetry();
}