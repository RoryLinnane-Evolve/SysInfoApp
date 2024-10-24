/**
 * Retrieves and represents information about a process such as memory (virtual and resident)
 * usage, CPU usage, process user, PID, PPID, priority, niceness.
 *
 * @author Mikey Fennelly
 * @version 1.0
 * */

package ise;

import java.io.IOException;

enum ProcState {
    RUN, SLEEP, UNINTERRUPTABLE_SLEEP, STOPPED, ZOMBIE
}

/**
 * Provides information about processes similar to that of running the 'top'/'htop' command
 * in bash
 * */
public class Process {
    private String name;
    private ProcState state;
    private int pid;
    private int ppid;
    private float cpuPercent;
    private int virtualMemoryGB;
    private int sharedMemoryGB;
    private int priority;
    private int niceness;

    /**
     * Sets the properties of the process class based on the values of
     * /proc/&lt;pid&gt;/status
     */
    public Process(int pid) throws IOException {
        setProcessInfoFromPID(pid);
    }

    private void setProcessInfoFromPID(int pid) throws IOException {
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

    public int getPID() {
        return pid;
    }

    public int getPPID() {
        return ppid;
    }

    public float getCpuPercent() {
        return cpuPercent;
    }

    public int getVirtualMemoryGB() {
        return virtualMemoryGB;
    }

    public int getSharedMemoryGB() {
        return sharedMemoryGB;
    }

    public int getPriority() {
        return priority;
    }

    public int getNiceness() {
        return niceness;
    }
}