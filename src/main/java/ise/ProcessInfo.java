package ise;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

enum ProcState {
    RUN, SLEEP, UNINTERRUPTABLE_SLEEP, STOPPED, ZOMBIE
}

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

    public void createProcessInfoFromPID(int pid) throws IOException {
        String stringProcessID = Integer.toString(pid);
        VirtualFileInfo statusInfo = new VirtualFileInfo("/proc/" + stringProcessID + "/status");
        Hashtable statusInfoTable = statusInfo.getHashtable();
        this.name = (String) statusInfoTable.get("Name");
        this.pid = Integer.parseInt(statusInfoTable.get("Pid").toString());
        this.ppid = Integer.parseInt(statusInfoTable.get("PPid").toString());
        String state = statusInfoTable.get("State").toString();
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