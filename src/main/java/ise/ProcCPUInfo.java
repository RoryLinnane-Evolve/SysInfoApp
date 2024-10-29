package ise;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Object to represent current state of /proc/cpuinfo virtual file when getProcCPUInfoTables() method is called
 * @author Mikey Fennelly
 * @version 2.0
 */
public class ProcCPUInfo extends Sysinfo  {
    private final List<String> parseIntItemsList = Arrays.asList("clflush size", "cache alignment", "processor", "cpu family", "model", "stepping", "physical id", "siblings", "core id", "cpu cores", "apicid", "initial apicid", "cpuid level","cflush size", "cache_alignment");
    private final List<String> parseBooleanFromYesNoItemsList = Arrays.asList("fpu", "fpu_exception", "wp");
    private final List<String> splitOnSpaceItemsList = Arrays.asList("flags", "vmx flags", "bugs");
    private final List<String> parseDoubleItemsList = Arrays.asList("cpu MHz", "bogomips");
    private final List<String> trimOnlyItemsList = Arrays.asList("vendor_id", "model name", "microcode");



    private ConversionOperation processAddressSizes = (unprocessedVal -> {
        String[] splitUnprocessedVal = unprocessedVal.split(", ");
        String unprocessedBitsPhysical = splitUnprocessedVal[0].replace("bits physical", "").trim();
        String unprocessedBitsVirtual = splitUnprocessedVal[1].replace("bits virtual", "").trim();
        Integer bitsPhysical = Integer.parseInt(unprocessedBitsPhysical);
        Integer bitsVirtual = Integer.parseInt(unprocessedBitsVirtual);
        Hashtable<String, Integer> addressSizes = new Hashtable<String, Integer>(2);
        addressSizes.put("bitsPhysical", bitsPhysical);
        addressSizes.put("bitsVirtual", bitsVirtual);
        return addressSizes;
    });

    public ProcCPUInfo() {
        super(ProcCPUInfo.class.getSimpleName());
    }

    public List<Map<String, Object>> getProcCPUInfoTables () throws IOException {
        List<Map<String, Object>> procCPUInfoTables = new ArrayList<Map<String, Object>>();

        KVPParser parser = new KVPParser();
        parser.addConversion(parseIntItemsList, KVPParser.premadeConversionOperation.PARSE_INT);
        parser.addConversion(parseBooleanFromYesNoItemsList, KVPParser.premadeConversionOperation.BOOLEAN_FROM_YES_NO);
        parser.addConversion(splitOnSpaceItemsList, KVPParser.premadeConversionOperation.SPLIT_ON_SPACE);
        parser.addConversion(parseDoubleItemsList, KVPParser.premadeConversionOperation.PARSE_DOUBLE);
        parser.addConversion(trimOnlyItemsList, KVPParser.premadeConversionOperation.TRIM_STRING);
        parser.addConversion(Arrays.asList("cache size"), KVPParser.premadeConversionOperation.POP_3_CHARS_RETURN_INT);
        parser.addConversion(Arrays.asList("address sizes"), processAddressSizes);

        // read file into a String[], each item representing a line
        String[] lines = parser.getLines("/proc/cpuinfo");

        // iterate through each line, and parse each line
        Map<String, Object> thisCPU = new Hashtable<String, Object>();
        for (String line : lines) {
            if (line.contains("power management:")) {
                // if a "power management" line is encountered, append current cpu and start the creation of a new one
                procCPUInfoTables.add(thisCPU);
                thisCPU = new Hashtable<String, Object>();
                continue;
            }
            if (line.trim().isEmpty() || line.substring(line.indexOf(":"), line.length() -1).trim().isEmpty()) {
                continue;
            }

            KVP thisLineKVP = parser.process(":", line);
            thisCPU.put(thisLineKVP.getKey(), thisLineKVP.getValue());
        }

        return procCPUInfoTables;
    }

    @Override
    public void printToConsole() {
        this.printConsoleHeader();
        try {
            List<Map<String, Object>> cpus = this.getProcCPUInfoTables();
            cpus.forEach(cpuMap -> {
                System.out.println("CPU " + cpuMap.get("processor") + ": ");
                cpuMap.entrySet().forEach(entry -> {
                    System.out.println("    " + entry.getKey() + ": " + entry.getValue());
                });
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendToOpenTelemetry() {
        // TODO
    }
}