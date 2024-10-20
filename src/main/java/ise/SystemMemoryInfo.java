package ise;

import java.io.IOException;
import java.util.Set;

/**
 * Object represents memory information on the host, and can be set to current
 * host values by calling non-static method SystemMemoryInfo.setMemInfo();
 * */
public class SystemMemoryInfo {
    int total;
    int free;
    int available;
    int buffers;
    int cached;

    public void setMemInfo() throws IOException {
        Set<String> memoryAttributes = Set.of("MemTotal", "MemFree", "MemAvailable", "Buffers", "Cached");
        VirtualFileInfo meminfo = new VirtualFileInfo("/proc/meminfo");
        meminfo.setHashtable();
        this.total = trimAndCastToInt(meminfo.fileInfo.get("MemTotal"), "kB");
        this.free = trimAndCastToInt(meminfo.fileInfo.get("MemFree"), "kB");
        this.available = trimAndCastToInt(meminfo.fileInfo.get("MemAvailable"), "kB");
        this.buffers = trimAndCastToInt(meminfo.fileInfo.get("Buffers"), "kB");
        this.cached = trimAndCastToInt(meminfo.fileInfo.get("Cached"), "kB");
    }

    /**
     * Takes in a string value 'raw', with an undesirable (non int) substring
     * (for example "20 kB") and trims the metric, and returns an integer.
     * @return int
     * */
    private static int trimAndCastToInt(String raw, String metric) {
        return Integer.parseInt(raw.replace(metric, "").trim());
    }
}