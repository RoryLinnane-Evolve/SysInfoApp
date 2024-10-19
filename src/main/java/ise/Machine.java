package ise;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Machine {
    private String cpuName;
    private String ram;
    private String disk;

    public static void main(String[] args) throws IOException {
        FileReader cpuinfo = new FileReader("/proc/cpuinfo");
        BufferedReader br = new BufferedReader(cpuinfo);
    }

    private void setCPUname(String cpuName) {
        this.cpuName = cpuName;
    }

    private void setRAM(String ram) {
        this.ram = ram;
    }

    private void setDisk(String disk) {
        this.disk = disk;
    }

    private String getCPUName() {
        return cpuName;
    }

    private String getRAM() {
        return ram;
    }

    private String getDisk() {
        return disk;
    }
}