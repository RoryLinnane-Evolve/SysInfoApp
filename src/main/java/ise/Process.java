/**
 * Retrieves and represents information about a process such as memory (virtual and resident)
 * usage, CPU usage, process user, PID, PPID, priority, niceness.
 *
 * @author Mikey Fennelly
 * @version 1.0
 * */

package ise;

public class Process {
    private int pid;
    private int ppid;
    private float cpuPercent;
    private int virtualMemoryGB;
    private int sharedMemoryGB;
    private int priority;
    private int niceness;

    public void setPID(int pid) {
        this.pid = pid;
    }

    public int getPID() {
        return pid;
    }

    public void setPPID(int ppid) {
        this.ppid = ppid;
    }

    public int getPPID() {
        return ppid;
    }

    public void setCpuPercent(float cpuPercent) {
        this.cpuPercent = cpuPercent;
    }

    public float getCpuPercent() {
        return cpuPercent;
    }

    public void setVirtualMemoryGB(int virtualMemoryGB) {
        this.virtualMemoryGB = virtualMemoryGB;
    }

    public int getVirtualMemoryGB() {
        return virtualMemoryGB;
    }

    public void setSharedMemoryGB(int sharedMemoryGB) {
        this.sharedMemoryGB = sharedMemoryGB;
    }

    public int getSharedMemoryGB() {
        return sharedMemoryGB;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public int getNiceness() {
        return niceness;
    }

    public void setNiceness(int niceness) {
        this.niceness = niceness;
    }
}