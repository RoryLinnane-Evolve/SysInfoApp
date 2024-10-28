package ise;

import java.io.IOException;
import java.util.*;

/**
 * ProcStatVF is a class that represents the /proc/stat virtual file on many Linux/Unix based systems.
 * Mostly for CPU/core occupation information and time spent related to interrupts etc.
 * */
public class ProcStatVF extends VirtualFile {
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

    // Get the number of CPUs/cores on the host machine
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

    // Returns a table of items that represents time a CPU/core spent at specific tasks in jiffies (1/100th of sec)
    // for each CPU listed on host machine /proc/stat file there is a table that represents time spent in
    // userMode, niceMode, systemMode, idleTask, IOWait, IRQ (interrupt request), IOWait (cpu waiting for I/O
    // operations to complete) and softIRQ.
    public Hashtable<String, Hashtable<String, Long>> getCPUOccupationTablesJiffies() {
        Map<String, List<Long>> KVPs = this.getKVPs();
        Set<Map.Entry<String, List<Long>>> entrySet = KVPs.entrySet();

        Hashtable<String, Hashtable<String, Long>> CPUOccupationTablesJiffies = new Hashtable<>();

        for (Map.Entry<String, List<Long>> entry : entrySet) {
            String key = entry.getKey();
            if (key.startsWith("cpu")) {
                List<Long> thisKeyValues = KVPs.get(key);
                Hashtable<String, Long> thisCPUOccupationTableJiffies = new Hashtable<>(10);
                List<String> occupations = new ArrayList<>(Arrays.asList("userMode", "niceMode", "systemMode", "idleTask", "IOWait", "IRQ", "softIRQ"));

                for (int i = 0; i < occupations.size(); i++) {
                    thisCPUOccupationTableJiffies.put(occupations.get(i), entry.getValue().get(i));
                }

                CPUOccupationTablesJiffies.put(key, thisCPUOccupationTableJiffies);

            }
        }

        return CPUOccupationTablesJiffies;
    }
}