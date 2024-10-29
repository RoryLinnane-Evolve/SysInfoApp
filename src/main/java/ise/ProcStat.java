package ise;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

class ProcStat {
    public static void main(String[] args) throws IOException {
        parseProcPIDStat(3157);
    }

    private static void parseProcPIDStat(int pid) throws IOException {
        KVPParser parser = new KVPParser();
        String[] lines = parser.getLines("/proc/" + pid + "/stat" );
        String onlyLineInFile = lines[0];
        String[] onlyLineInFileValsSplit = onlyLineInFile.split(" ");

        List<String> onlyLineInFileKeys = Arrays.asList("pid", "comm", "state", "ppid", "pgrp", "session", "tty_nr", "tpgid", "flags", "minflt", "cminflt", "majflt", "cmajflt", "utime", "stime", "cutime", "cstime", "priority", "nice", "num_threads", "itrealvalue", "starttime", "vsize", "rss", "rsslim", "startcode", "endcode", "startstack", "kstkesp", "kstkeip", "signal", "blocked", "sigignore", "sigcatch", "wchan", "nswap", "cnswap", "exit_signal", "processor", "rt_priority", "policy", "delayacct_blkio_ticks", "guest_time", "cguest_time", "start_data", "end_data", "start_brk", "arg_start", "arg_end", "env_start", "env_end", "exit_code");

        for (int i = 0; i < onlyLineInFileKeys.size(); i++) {
            System.out.println(onlyLineInFileKeys.get(i) + " : " + onlyLineInFileValsSplit[i]);
        }
    }
}