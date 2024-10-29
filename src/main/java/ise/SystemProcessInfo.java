package ise;

import java.io.File;
import java.util.ArrayList;

/**
 * This class can create an object with information about currently running processes
 * on the host machine, such as name, pid, ppid, state
 * @author Mikey Fennelly
 * */
public class SystemProcessInfo {
    public static ArrayList<Integer> getAllRunningPIDs() {
        ArrayList<Integer> runningPIDs = new ArrayList<>();

        File procDir = new File("/proc");
        String[] procDirs = procDir.list();

        if (procDirs != null) {
            for (String dir : procDirs) {
                if (dir.matches("\\d+")) {
                    runningPIDs.add(Integer.parseInt(dir));
                }
            }
        }

        return runningPIDs;
    }
}