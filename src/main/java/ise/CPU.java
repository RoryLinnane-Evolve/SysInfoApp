package ise;

import java.io.IOException;
import java.util.HashMap;

public class CPU {
    private int numCPUs;

    public static void main(String[] args) throws IOException {
        stat();
    }

    private static void stat() throws IOException {
        VirtualFileInfo procstat = new VirtualFileInfo("/proc/stat");
        procstat.setHashtable();
    }
}