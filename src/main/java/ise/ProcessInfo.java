package ise;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

enum ProcState {
    RUN, SLEEP, UNINTERRUPTABLE_SLEEP, STOPPED, ZOMBIE
}

/**
 * This class can create an object with information about currently running processes
 * on the host machine, such as name, pid, ppid, state
 * @author Mikey Fennelly
 * */
public class ProcessInfo {
    String name;
    int pid;
    int ppid;
    ProcState state;

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

    /**
     * Sets the properties of the process class based on the values of
     * /proc/&lt;pid&gt;/status
     * */
    public void setProcessInfoFromPID(int pid) throws IOException {
        String stringProcessID = Integer.toString(pid);
        VirtualFileInfo statusInfo = new VirtualFileInfo("/proc/" + stringProcessID + "/status");
        statusInfo.setHashtable();
        this.name = (String) statusInfo.fileInfo.get("Name");
        this.pid = Integer.parseInt(statusInfo.fileInfo.get("Pid").toString());
        this.ppid = Integer.parseInt(statusInfo.fileInfo.get("PPid").toString());
        String state = statusInfo.fileInfo.get("State").toString();
        switch (state) {
            case "S (sleeping)":
                this.state = ProcState.SLEEP;
                break;
            case "R (running)":
                this.state = ProcState.RUN;
                break;
            case "T (stopped)":
                this.state = ProcState.STOPPED;
                break;
            case "Z (zombie)":
                this.state = ProcState.ZOMBIE;
                break;
            case "D (uninterruptable sleep)":
                this.state = ProcState.UNINTERRUPTABLE_SLEEP;
                break;
        }
    }
}