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

        int dummyInt = 3;
    }

    public ProcCPUInfo(String filePath) throws IOException {
        super(filePath);
    };

    public ArrayList<Hashtable<String, Object>> getStringKVPs() {
        ArrayList<Hashtable<String, Object>> KVPs = new ArrayList<Hashtable<String, Object>>();
        List<String> lines = getLines();
        Hashtable<String, Object> thisCPU = new Hashtable<String, Object>();

        for (String line : lines) {
            if (line.isEmpty()) {
                KVPs.add(thisCPU);
                continue;
            }
            String[] thisLineStringValues = line.split(":");
            String thisLineKey = thisLineStringValues[0].trim();
            String thisLineValueUnconverted = thisLineStringValues[1].trim();

        }
        return KVPs;
    }

    private Object convertToAptType(String key, String value) {
        key.trim(); value.trim();
        Object valueReturn = value;

        if (value == "yes") {
            valueReturn = true;
        } else if (value == "no") {
            valueReturn = false;
        } else if (key == "microcode") {
            valueReturn = Long.parseLong(value);
        } else if (key == "flags" || key == "vmx flags" || key == "bugs") {
            valueReturn = (String[]) value.split(" ");
        } else if (key == "address sizes") {
            String[] splitVal = value.split(", ");
            Integer bitsPhysical = Integer.parseInt(splitVal[0].replace(" bits physical", ""));
            Integer bitsVirtual = Integer.parseInt(splitVal[1].replace(" bits virtual", ""));
            valueReturn = new Hashtable<String, Integer>(2);
            ((Hashtable<String, Integer>) valueReturn).put("bitsPhysical", bitsPhysical);
            ((Hashtable<String, Integer>) valueReturn).put("bitsVirtual", bitsVirtual);
        } else {
            valueReturn = Integer.parseInt(value);
        }

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