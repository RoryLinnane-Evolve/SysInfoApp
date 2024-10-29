package ise;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ProcStat {
    public static void main(String[] args) throws IOException {
        Map<String, Object> procPidStat = parseProcPIDStat(3157);

        int d = 2;
    }

    private static Map<String, Object> parseProcPIDStat(int pid) throws IOException {
        KVPParser parser = new KVPParser();
        String[] lines = parser.getLines("/proc/" + pid + "/stat" );
        String onlyLineInFile = lines[0];
        String[] onlyLineInFileValsSplit = onlyLineInFile.split(" ");

        List<String> onlyLineInFileKeys = Arrays.asList("pid", "comm", "state", "ppid", "pgrp", "session", "tty_nr", "tpgid", "flags", "minflt", "cminflt", "majflt", "cmajflt", "utime", "stime", "cutime", "cstime", "priority", "nice", "num_threads", "itrealvalue", "starttime", "vsize", "rss", "rsslim", "startcode", "endcode", "startstack", "kstkesp", "kstkeip", "signal", "blocked", "sigignore", "sigcatch", "wchan", "nswap", "cnswap", "exit_signal", "processor", "rt_priority", "policy", "delayacct_blkio_ticks", "guest_time", "cguest_time", "start_data", "end_data", "start_brk", "arg_start", "arg_end", "env_start", "env_end", "exit_code");

        Map<String, Object> returnMap = new HashMap<>();

        for (int i = 0; i < onlyLineInFileKeys.size(); i++) {
            String thisLineKey = onlyLineInFileKeys.get(i);
            String thisLineValueString = onlyLineInFileValsSplit[i];
            System.out.println(thisLineKey + " : " + thisLineValueString);

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
}