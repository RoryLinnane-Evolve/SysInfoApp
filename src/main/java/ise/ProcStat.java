/**
 * Retrieves and represents information about a process such as memory (virtual and resident)
 * usage, CPU usage, process user, PID, PPID, priority, niceness.
 * @author Mikey Fennelly
 * @version 1.0
 * */

package main.java.ise;

import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

enum ProcState {
    RUN, SLEEP, UNINTERRUPTABLE_SLEEP, STOPPED, ZOMBIE
}

/**
 * Provides information about processes similar to that of running the 'top'/'htop' command
 * in bash
 * */
public class ProcStat {
    private final List<String> trimOnlyItemsList = Arrays.asList("Umask", "Name", "Speculation_Store_Bypass", "SpeculationIndirectBranch", "Cpus_allowed", "untag_mask", "SigQ", "CapBnd");
    private final List<String> popThreeCharsReturnIntList = Arrays.asList("VmPeak", "VmSize", "VmLck", "VmPin", "VmHWM", "VmRSS", "RssAnon", "RssFile", "RssShmem", "VmData", "VmStk", "VmExe", "VmLib", "VmPTE", "VmSwap", "HugetlbPages", "SigPnd", "ShdPnd", "SigBlk", "SigIgn", "SigCgt", "CapInh", "CapPrm", "CapEff", "CapAmb");
    private final List<String> parseLongList = Arrays.asList("Seccomp","NoNewPrivs", "Tgid", "Ngid", "Pid", "PPid", "TracerPid", "FDSize", "NStgid", "NSpid", "NSpgid", "NSsid", "Kthread", "CoreDumping", "THP_enabled", "Threads", "voluntary_ctxt_switches", "nonvoluntary_ctxt_switches", "Seccomp_filters", "Mems_allowed_list");
    private final List<String> processMemsAllowedList = Arrays.asList("Mems_allowed");
    private final List<String> processCpusAllowedListList = Arrays.asList("Cpus_allowed_list");
    private final List<String> processProcStateList = Arrays.asList("State");
    private final List<String> parseIntArraySplitOnSpaceList = Arrays.asList("Groups");
    private final List<String> parseIDsList = Arrays.asList("Uid", "Gid");

    private ConversionOperation processIDs = (idString -> {
        Integer[] idsIntArray = (Integer[]) KVPParser.premadeConversionOperation.PARSE_INT_ARRAY_SPLIT_ON_SPACE.apply(idString);
        List<String> idKeys = Arrays.asList("real", "effective", "saved_set", "filesystem");
        Map<String, Integer> idTable = new HashMap<String, Integer>();

        for (int i = 0; i < idsIntArray.length; i++) {
            idTable.put(idKeys.get(i), idsIntArray[i]);
        }

        return idTable;
    });

    private ConversionOperation processMemsAllowed = (memsAllowedString -> {
        String[] memsAllowed = memsAllowedString.trim().split(",");
        return memsAllowed;
    });

    private ConversionOperation processCpusAllowedList = (rangeString -> {
        String[] splitRangeString = rangeString.trim().split("-");
        int[] range = IntStream.rangeClosed(Integer.parseInt(splitRangeString[0]), Integer.parseInt(splitRangeString[1])).toArray();
        return range;
    });

    private ConversionOperation processProcState = (state -> {
        ProcState stateToReturn = null;
        switch (state) {
            case "S (sleeping)":
                stateToReturn = ProcState.SLEEP;
                break;
            case "R (running)":
                stateToReturn = ProcState.RUN;
                break;
            case "T (stopped)":
                stateToReturn = ProcState.STOPPED;
                break;
            case "Z (zombie)":
                stateToReturn = ProcState.ZOMBIE;
                break;
            case "D (uninterruptable sleep)":
                stateToReturn = ProcState.UNINTERRUPTABLE_SLEEP;
                break;
        }
        return stateToReturn;
    });

    public Map<String, Object> getProcessInfo(int pid) throws IOException {
        List<Map<String, Object>> procCPUInfoTables = new ArrayList<Map<String, Object>>();

        KVPParser parser = new KVPParser();
        parser.addConversion(trimOnlyItemsList, KVPParser.premadeConversionOperation.TRIM_STRING);
        parser.addConversion(popThreeCharsReturnIntList, KVPParser.premadeConversionOperation.POP_3_CHARS_RETURN_INT);
        parser.addConversion(parseLongList, KVPParser.premadeConversionOperation.PARSE_LONG);
        parser.addConversion(processMemsAllowedList, processMemsAllowed);
        parser.addConversion(processCpusAllowedListList, processCpusAllowedList);
        parser.addConversion(processProcStateList, processProcState);
        parser.addConversion(parseIntArraySplitOnSpaceList, KVPParser.premadeConversionOperation.PARSE_INT_ARRAY_SPLIT_ON_SPACE);
        parser.addConversion(parseIDsList, processIDs);

        String[] lines = parser.getLines("/proc/" + pid + "/status");

        Map<String, Object> thisProcess = new Hashtable<String, Object>();
        for (String line : lines) {
            if (line.contains("x86_Thread_features")) {
                continue;
            }

            KVP kvp = parser.process(":", line);
            if (kvp.getValue()==null || kvp.getKey()==null) {
                System.out.println("unable to parse line: " + line);
            } else {
                thisProcess.put(kvp.getKey(), kvp.getValue());
            }
        }

        return thisProcess;
    }
}