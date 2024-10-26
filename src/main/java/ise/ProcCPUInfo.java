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

//        System.out.println("case \"" + key + "\":");
//        System.out.println("    break;");

        switch (key) {
            case "processor":
                valueReturn = Integer.parseInt(value);
                break;
            case "vendor_id":
                valueReturn = value.trim();
                break;
            case "cpu family":
                valueReturn = Integer.parseInt(value);
                break;
            case "model":
                valueReturn = Integer.parseInt(value);
                break;
            case "model name":
                valueReturn = value.trim();
                break;
            case "stepping":
                valueReturn = Integer.parseInt(value);
                break;
            case "microcode":
                valueReturn = Long.decode(value);
                break;
            case "cpu MHz":
                valueReturn = Double.parseDouble(value);
                break;
            case "cache size":
                valueReturn = Integer.parseInt(value.replace(" KB", ""));
                break;
            case "physical id":
                valueReturn = Integer.parseInt(value);
                break;
            case "siblings":
                valueReturn = Integer.parseInt(value);
                break;
            case "core id":
                valueReturn = Integer.parseInt(value);
                break;
            case "cpu cores":
                valueReturn = Integer.parseInt(value);
                break;
            case "apicid":
                valueReturn = Integer.parseInt(value);
                break;
            case "initial apicid":
                valueReturn = Integer.parseInt(value);
                break;
            case "fpu":
                if (value == "yes"){
                    valueReturn = true;
                } else {
                    valueReturn = false;
                }
                break;
            case "fpu_exception":
                if (value == "yes"){
                    valueReturn = true;
                } else {
                    valueReturn = false;
                }
                break;
            case "cpuid level":
                valueReturn = Integer.parseInt(value);
                break;
            case "wp":
                if (value == "yes"){
                    valueReturn = true;
                } else {
                    valueReturn = false;
                }
                break;
            case "flags":
                valueReturn = value.split(" ");
                break;
            case "vmx flags":
                valueReturn = value.split(" ");
                break;
            case "bugs":
                valueReturn = value.split(" ");
                break;
            case "bogomips":
                valueReturn = Double.parseDouble(value);
                break;
            case "clflush size":
                valueReturn = Integer.parseInt(value);
                break;
            case "cache_alignment":
                valueReturn = Integer.parseInt(value);
                break;
            case "address sizes":
                String[] splitLine = value.split(", ");
                Integer bitsPhysical = Integer.parseInt(splitLine[0].replace(" bits physical", ""));
                Integer bitsVirtual = Integer.parseInt(splitLine[1].replace(" bits virtual", ""));
                valueReturn = new Hashtable<String, Integer>();
                ((Hashtable<String, Integer>) valueReturn).put("bitsPhysical", bitsPhysical);
                ((Hashtable<String, Integer>) valueReturn).put("bitsVirtual", bitsVirtual);
                break;
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