package ise;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

public class ProcCPUInfo extends VirtualFile {
    public static void main(String[] args) throws IOException {
        ProcCPUInfo procCPUInfo = new ProcCPUInfo("/proc/cpuinfo");
        ArrayList<Hashtable<String, Object>> KVPs = procCPUInfo.getStringKVPs();

        System.out.println(KVPs);
    }

    public ProcCPUInfo(String filePath) throws IOException {
        super(filePath);
    };

    public ArrayList<Hashtable<String, Object>> getStringKVPs() {
        ArrayList<Hashtable<String, Object>> KVPs = new ArrayList<Hashtable<String, Object>>();
        List<String> lines = getLines();
        Hashtable<String, Object> thisCPU = new Hashtable<String, Object>();

        for (String line : lines) {
            if (line.isEmpty() || line.startsWith("power management")) {
                KVPs.add(thisCPU);
                continue;
            }
            String[] thisLineStringValues = line.split(":");
            String thisLineKey = thisLineStringValues[0];
            String thisLineValueUnconverted = thisLineStringValues[1];
            Object thisLineValueConverted = convertToAptType(thisLineKey, thisLineValueUnconverted);

            thisCPU.put(thisLineKey, thisLineValueConverted);
        }
        return KVPs;
    }

    private Object convertToAptType(String keyUnconverted, String valueUnconverted) {
        String key = keyUnconverted.trim();
        String value = valueUnconverted.trim();
        Object valueReturn = value;



        return valueReturn;
    }

    /**
     * Creates a CPU from the lines of a /proc/cpuinfo file
     * @returns CPU
     * */
    private Hashtable<String, Object> parseCPUDataFromLines() {
        List<String> lines = this.getLines();
        Hashtable<String, Object> thisCPU = new Hashtable<>();
        for (String line : lines) {
            System.out.println(line);
        }

        return thisCPU;
    }

    public void printToStdout() {}
    public void sendToMaster() {}
}