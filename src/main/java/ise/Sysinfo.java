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
    public static void main(String[] args ) throws IOException {

        String firstArg;
        try {
            firstArg = args[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            firstArg = null;
        }

        if (firstArg == null) {
            new CLIUsageError().noArgs();
        } else {
            switch (firstArg) {
                case "cpu":
                    VirtualFileInfo cpuinfo = new VirtualFileInfo("/proc/cpuinfo");
                    break;
                case "mem":
                    VirtualFileInfo meminfo = new VirtualFileInfo("/proc/meminfo");
                    break;
                case "allprocs":
                    ProcessInfo processInfo = new ProcessInfo();
                    ArrayList<Integer> currentProcs = processInfo.getAllRunningPIDs();

                ProcessInfo testProc = new ProcessInfo();
                testProc.setProcessInfoFromPID(3159);
            }
        }
    }
}