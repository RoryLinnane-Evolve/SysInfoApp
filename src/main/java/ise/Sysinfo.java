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
        SystemMemoryInfo smi = new SystemMemoryInfo();
    }
}

interface SysinfoInterface {
    ArrayList<Integer> getRunningPIDs();
    ProcessInfo getProcessInfo(int pid);
    SystemMemoryInfo getSystemMemoryInfo();
}