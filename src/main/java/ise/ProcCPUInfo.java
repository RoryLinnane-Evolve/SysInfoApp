/**
 * Object to represent current state of /proc/cpuinfo virtual file when getProcCPUInfoTables() method is called
 * @author Mikey Fennelly
 * @version 2.0
 * */

package ise;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Stream;

public class ProcCPUInfo  {
    private static final List<String> parseIntItemsList = Arrays.asList("clflush size", "cache alignment", "processor", "cpu family", "model", "stepping", "physical id", "siblings", "core id", "cpu cores", "apicid", "initial apicid", "cpuid level","cflush size", "cache_alignment");
    private static final List<String> parseBooleanFromYesNoItemsList = Arrays.asList("fpu", "fpu_exception", "wp");
    private static final List<String> splitOnSpaceItemsList = Arrays.asList("flags", "vmx flags", "bugs");
    private static final List<String> parseDoubleItemsList = Arrays.asList("cpu MHz", "bogomips");
    private static final List<String> trimOnlyItemsList = Arrays.asList("vendor_id", "model name", "microcode");

    private static ConversionOperation processAddressSizes = (unprocessedVal -> {
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

    public List<Hashtable<String, Object>> getProcCPUInfoTables () throws FileNotFoundException {
        List<Hashtable<String, Object>> procCPUInfoTables = new ArrayList<Hashtable<String, Object>>();

        KVPParser parser = new KVPParser();
        parser.addConversion(parseIntItemsList, KVPParser.premadeConversionOperation.PARSE_INT);
        parser.addConversion(parseBooleanFromYesNoItemsList, KVPParser.premadeConversionOperation.BOOLEAN_FROM_YES_NO);
        parser.addConversion(splitOnSpaceItemsList, KVPParser.premadeConversionOperation.SPLIT_ON_SPACE);
        parser.addConversion(parseDoubleItemsList, KVPParser.premadeConversionOperation.PARSE_DOUBLE);
        parser.addConversion(trimOnlyItemsList, KVPParser.premadeConversionOperation.TRIM_STRING);
        parser.addConversion(Arrays.asList("cache size"), KVPParser.premadeConversionOperation.POP_3_CHARS_RETURN_INT);
        parser.addConversion(Arrays.asList("address sizes"), processAddressSizes);

        // read file into a String[], each item representing a line
        FileReader file = new FileReader("/proc/cpuinfo");
        BufferedReader reader = new BufferedReader(file);
        Stream<String> linesStream = reader.lines();
        String[] lines = linesStream.toArray(String[]::new);

        // iterate through each line, and parse each line
        Hashtable<String, Object> thisCPU = new Hashtable<String, Object>();
        for (String line : lines) {
            String lineToOperateOn = line;
            if (lineToOperateOn.trim().length() == 0) { // if a blank line is encountered, append current cpu and start the creation of a new one
                procCPUInfoTables.add(thisCPU);
            } else if (lineToOperateOn.contains("power management")) {
                // Do nothing with this line (ingnore it)
            } else { // otherwise, attempt to parse the line and add to the CPU
                try {
                    KVP thisLineKVP = parser.process(":", lineToOperateOn);
                    thisCPU.put(thisLineKVP.getKey(), thisLineKVP.getValue());
                } catch (NullPointerException e) {
                    System.out.println();
                    System.out.println(lineToOperateOn);
                    e.printStackTrace();
                }
            }
        }

        return procCPUInfoTables;
    }
}