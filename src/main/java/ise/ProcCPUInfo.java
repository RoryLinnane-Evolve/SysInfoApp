package ise;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
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
            try {
                String thisLineKey = thisLineStringValues[0].trim();
                Object thisLineValue = thisLineStringValues[1].trim();

                switch (thisLineStringValues[1].trim()) {
                    case "yes":
                        thisLineValue = true;
                        break;
                    case "no":
                        thisLineValue = false;
                        break;
                    default:
                        if (thisLineStringValues[1].trim().endsWith(" KB")) {
                            thisLineValue = thisLineStringValues[1].trim().substring(0, thisLineStringValues[1].trim().length() - 3);
                        } else if (thisLineKey == "flags" | thisLineKey == "bugs" | thisLineKey == "vmx flags") {
                            thisLineValue = thisLineStringValues[1].trim().split("\\s+");
                        } else {
                            try {
                                thisLineValue = Integer.parseInt(thisLineStringValues[1].trim());
                            } catch (NumberFormatException e) {
                                thisLineValue = thisLineStringValues[1].trim();
                            }
                        }
                }
                thisCPU.put(thisLineKey, thisLineValue);
            } catch (IndexOutOfBoundsException e) {
                thisCPU.put(thisLineStringValues[0].trim(), "");
            }
        }
        return KVPs;
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