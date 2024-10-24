package ise;

import java.io.IOException;
import java.util.*;

public class CPU {
    private int numCPUs;

    public static void main(String[] args) throws IOException {
        ProcStatVF stat = new ProcStatVF("/proc/stat");

        int dummyInt = 4;
    }
}

class ProcStatVF extends VirtualFile<List<Integer>, Map<String, List<Integer>>> {
    public ProcStatVF(String fileLocation) throws IOException {
        super(fileLocation);
    }

    // Gets key-value pairs for each line in the virtual file
    private Map<String, List<Long>> getKVPs() {
        Map<String, List<Long>> KVPs = new HashMap<String, List<Long>>();

        List<String> lines = this.getLines();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            StringTokenizer tokens = new StringTokenizer(line, " ");

            if (tokens.hasMoreTokens()) {
                List<Long> thisLineValues = new ArrayList<Long>();
                String thisLineKey = tokens.nextToken();

                while (tokens.hasMoreTokens()) {
                    String value = tokens.nextToken();
                    if (!value.trim().equals("")) {
                        thisLineValues.add(Long.parseLong(value));
                    }
                }
                KVPs.put(thisLineKey, thisLineValues);
            }
        }
        return KVPs;
    }

    // Get the number of CPUs on the host machine
    public int getNumCPUs() {
        Map<String, List<Long>> KVPs = this.getKVPs();
        int cpuCount = 0;
        for (String key : KVPs.keySet()) {
            if (key.startsWith("cpu")) {
                cpuCount++;
            }
        }
        return cpuCount;
    }

    public void printToStdout() {}

    public void sendToMaster() {}
}