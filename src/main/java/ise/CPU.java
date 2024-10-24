package ise;

import java.io.IOException;
import java.util.HashMap;

public class CPU {
    private int numCPUs;

    public static void main(String[] args) throws IOException {
        HashMap<String, String[]> procStatHMap = stat();
    }

    public static HashMap<String, String[]> stat() throws IOException {
        VirtualFileInfo procstat = new VirtualFileInfo("/proc/stat");
        return procstat.getGenericHashMap();
    }
}