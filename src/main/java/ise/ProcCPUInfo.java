package ise;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Arrays;

public class ProcCPUInfo extends VirtualFileInfo {
    public List<Hashtable<String, Object>> procCPUInfoTables = new ArrayList<Hashtable<String, Object>>();

    private final List<String> parseIntItemsList = Arrays.asList("clflush size", "cache alignment", "processor", "cpu family", "model", "stepping", "physical id", "siblings", "core id", "cpu cores", "apicid", "initial apicid", "cpuid level","cflush size", "cache_alignment");
    private final List<String> parseBooleanFromYesNoItemsList = Arrays.asList("fpu", "fpu_exception", "wp");
    private final List<String> splitOnSpaceItemsList = Arrays.asList("flags", "vmx flags", "bugs");
    private final List<String> parseDoubleItemsList = Arrays.asList("cpu MHz", "bogomips");
    private final List<String> trimOnlyItemsList = Arrays.asList("vendor_id", "model name", "microcode");

    public ProcCPUInfo(String fileLocation) {
        super(fileLocation);
        KVPParser parser = new KVPParser();
        parser.addConversion(parseIntItemsList, KVPParser.premadeConversionOperation.PARSE_INT);
        parser.addConversion(parseBooleanFromYesNoItemsList, KVPParser.premadeConversionOperation.BOOLEAN_FROM_YES_NO);
        parser.addConversion(splitOnSpaceItemsList, KVPParser.premadeConversionOperation.SPLIT_ON_SPACE);
        parser.addConversion(parseDoubleItemsList, KVPParser.premadeConversionOperation.PARSE_DOUBLE);
        parser.addConversion(trimOnlyItemsList, KVPParser.premadeConversionOperation.TRIM_STRING);
    }
}