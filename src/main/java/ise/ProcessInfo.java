package ise;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ProcessInfo {
    int fd;
    int cgroup;


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

    public static void createProcessInfoFromPID(int pid) throws IOException {
        String stringProcessID = Integer.toString(pid);
        File procDir = new File("/proc/" + stringProcessID);

        File status = new File(procDir, "status");
        VirtualFileInfo statusInfo = new VirtualFileInfo("/proc/" + stringProcessID + "/status");
        statusInfo.getHashtable().forEach((key, value) -> {
            if (key == "cgroup") {
                System.out.println(key + " : " + value );
            }
        }
    }
}