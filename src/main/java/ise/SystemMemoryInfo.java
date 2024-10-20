package ise;

import java.io.IOException;

public class SystemMemoryInfo {
    int total;
    int free;
    int available;
    int buffers;
    int cached;

    public static void main(String[] args) throws IOException {
        setMemInfo();
    }

    public static void setMemInfo() throws IOException {
        VirtualFileInfo meminfo = new VirtualFileInfo("/proc/meminfo");
        meminfo.setHashtable();
        meminfo.fileInfo.forEach((key, value) -> {

        });
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