/**
 * Retrieves and represents information about a process such as memory (virtual and resident)
 * usage, CPU usage, process user, PID, PPID, priority, niceness.
 * @author Mikey Fennelly
 * @version 1.0
 * */

package ise;

import jdk.incubator.vector.VectorOperators;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

enum ProcState {
    RUN, SLEEP, UNINTERRUPTABLE_SLEEP, STOPPED, ZOMBIE
}

/**
 * Provides information about processes similar to that of running the 'top'/'htop' command
 * in bash
 * */
public class Process {
    private final List<String> trimOnlyItemsList = Arrays.asList("Name", "Speculation_Store_Bypass", "SpeculationIndirectBranch", "Cpus_allowed", "untag_mask", "SigQ");
    private final List<String> popThreeCharsReturnIntList = Arrays.asList("VmPeak", "VmSize", "VmLck", "VmPin", "VmHWM", "VmRSS", "RssAnon", "RssFile", "RssShmem", "VmData", "VmStk", "VmExe", "VmLib", "VmPTE", "VmSwap", "HugetlbPages", "untag_mask", "SigPnd", "ShdPnd", "SigBlk", "SigIgn", "SigCgt", "CapInh", "CapPrm", "CapEff", "CapBnd", "CapAmb");
    private final List<String> parseLongList = Arrays.asList("Tgid", "Ngid", "Pid", "PPid", "TracerPid", "FDSize", "NStgid", "NSpid", "NSpgid", "NSsid", "Kthread", "CoreDumping", "THP_enabled", "Threads", "voluntary_ctxt_switches", "nonvoluntary_ctxt_switches");

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


}