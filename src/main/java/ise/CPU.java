package ise;

public class CPU {
    private int numCPUs;

    public CPU() {
        setCPUInfo();
    }

    private void setCPUInfo() {
        this.numCPUs = Runtime.getRuntime().availableProcessors();
    }

    public int getNumCPUs() {
        return numCPUs;
    }
}