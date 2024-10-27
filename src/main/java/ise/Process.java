/**
 * Retrieves and represents information about a process such as memory (virtual and resident)
 * usage, CPU usage, process user, PID, PPID, priority, niceness.
 *
 * @author Mikey Fennelly
 * @version 1.0
 * */

package ise;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

enum ProcState {
    RUN, SLEEP, UNINTERRUPTABLE_SLEEP, STOPPED, ZOMBIE
}

/**
 * Provides information about processes similar to that of running the 'top'/'htop' command
 * in bash
 * */
public class Process {
    private static final List<String> trimOnlyItemsList = Arrays.asList("Name", "Speculation_Store_Bypass", "SpeculationIndirectBranch", "Cpus_allowed");
    private static final List<String> popThreeCharsReturnIntList = Arrays.asList("VmPeak", "VmSize", "VmLck", "VmPin", "VmHWM", "VmRSS", "RssAnon", "RssFile", "RssShmem", "VmData", "VmStk", "VmExe", "VmLib", "VmPTE", "VmSwap", "HugetlbPages");


    private static ConversionOperation processProcState = (state -> {
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