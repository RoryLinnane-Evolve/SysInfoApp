package ise;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Method 'getProcPIDStatInfo()' can be used to retrieve information in KVP format from /proc/<pid>/stat file - given PID for that process.
 * Docs for /proc/<pid>/stat file on Linux can be found at url: https://manpages.ubuntu.com/manpages/noble/man5/proc_pid_stat.5.html#:~:text=DESCRIPTION,file%20fs%2Fproc%2Farray.
 * see the following gist for example usage and docs: https://gist.github.com/mikeyfennelly1/0698dc6435a8547373270fca7971c65b
 * @author Mikey Fennelly
 * @version 1.0
 * */
class ProcPIDStat extends Sysinfo {
    ProcPIDStat() {
        super("ProcPIDStat");
    }

    public Map<String, Object> getProcPIDStatInfo(int pid) throws IOException {
        KVPParser parser = new KVPParser();
        String[] lines = parser.getLines("/proc/" + pid + "/stat" );
        String onlyLineInFile = lines[0];
        String[] onlyLineInFileValsSplit = onlyLineInFile.split(" ");

        List<String> onlyLineInFileKeys = Arrays.asList("pid", "comm", "state", "ppid", "pgrp", "session", "tty_nr", "tpgid", "flags", "minflt", "cminflt", "majflt", "cmajflt", "utime", "stime", "cutime", "cstime", "priority", "nice", "num_threads", "itrealvalue", "starttime", "vsize", "rss", "rsslim", "startcode", "endcode", "startstack", "kstkesp", "kstkeip", "signal", "blocked", "sigignore", "sigcatch", "wchan", "nswap", "cnswap", "exit_signal", "processor", "rt_priority", "policy", "delayacct_blkio_ticks", "guest_time", "cguest_time", "start_data", "end_data", "start_brk", "arg_start", "arg_end", "env_start", "env_end", "exit_code");

        Map<String, Object> returnMap = new HashMap<>();

        for (int i = 0; i < onlyLineInFileKeys.size(); i++) {
            String thisLineKey = onlyLineInFileKeys.get(i);
            String thisLineValueString = onlyLineInFileValsSplit[i];

            try {
                if (thisLineKey == "comm" || thisLineKey == "state") {
                    String thisLineVal = thisLineValueString;
                    returnMap.put(thisLineKey, thisLineVal);
                } else {
                    BigInteger thisLineVal = new BigInteger(thisLineValueString);
                    returnMap.put(thisLineKey, thisLineVal);
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }

        return returnMap;
    }

    @Override
    public void printToConsole() {
        this.printConsoleHeader();
        SystemProcessInfo systemProcessInfo = new SystemProcessInfo();
        List<Integer> allRunningPids = systemProcessInfo.getAllRunningPIDs();
        allRunningPids.forEach(pid -> {
            try {
                Map<String, Object> processInfo = this.getProcPIDStatInfo(pid);
                System.out.println("Process " + processInfo.get("pid") + ": ");
                processInfo.entrySet().forEach(entry -> {
                    System.out.println("    " + entry.getKey() + ": " + entry.getValue());
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void sendToOpenTelemetry() {

    }
}