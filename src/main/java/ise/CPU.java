package ise;

import java.io.IOException;
import java.util.*;

public class CPU {
    private int numCPUs;

    public static void main(String[] args) throws IOException {
        ProcStatVF stat = new ProcStatVF("/proc/stat");
        Map<String, List<Integer>> statData = stat.getKVPs();

        int dummyInt = 4;
    }
}

class ProcStatVF extends VirtualFile<List<Integer>, Map<String, List<Integer>>> {
    public ProcStatVF(String fileLocation) throws IOException {
        super(fileLocation);
    }

    // Gets key-value pairs for each line in the virtual file
    public Map<String, List<Integer>> getKVPs() {
        Map<String, List<Integer>> KVPs = new HashMap<String, List<Integer>>();

        List<String> lines = this.getLines();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            StringTokenizer tokens = new StringTokenizer(line, " ");

            if (tokens.hasMoreTokens()) {
                List<Integer> thisLineValues = new ArrayList<Integer>();
                String thisLineKey = tokens.nextToken();

                while (tokens.hasMoreTokens()) {
                    String value = tokens.nextToken();
                    if (!value.trim().equals("")) {
                        thisLineValues.add(Integer.parseInt(value));
                    }
                }
                KVPs.put(thisLineKey, thisLineValues);
            }
        }
        return KVPs;
    }

    public void printToStdout() {}

    public void sendToMaster() {}
}