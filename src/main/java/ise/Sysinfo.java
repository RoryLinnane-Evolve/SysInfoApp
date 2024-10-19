package ise;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A proof of concept application for distributed metrics slave agent.
 *
 * @version 1.0
 * @author Mikey Fennelly
 */
public class Sysinfo {
    public static void main(String[] args ) throws IOException {

        String firstArg = "";
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
                    cpuinfo.printInfoTable();
                    break;
                case "mem":
                    VirtualFileInfo meminfo = new VirtualFileInfo("/proc/meminfo");
                    meminfo.printInfoTable();
                    break;
                case "allprocs":
                    ProcessInfo processInfo = new ProcessInfo();
                    ArrayList<Integer> currentProcs = processInfo.getAllRunningPIDs();


                    for (int i = 0; i < currentProcs.toArray().length; i++) {
                        new ProcessInfo().createProcessInfoFromPID(3260);
                        return;
                    }
            }
        }
    }
}