/**
 * A proof of concept application for distributed metrics slave agent.
 *
 * @version 1.0
 * @author Mikey Fennelly
 */
package ise;

import java.io.IOException;
import java.util.ArrayList;

public class Sysinfo {
    public static void main(String[] args) throws IOException {
        CPU cpu = new CPU();
        System.out.println(cpu.getNumCPUs());
    }
}

interface SysinfoInterface {
    ArrayList<Integer> getRunningPIDs();
    SystemProcessInfo getProcessInfo(int pid);
    SystemMemoryInfo getSystemMemoryInfo();
}