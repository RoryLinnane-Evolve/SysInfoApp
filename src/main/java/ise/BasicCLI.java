package ise;

import ise.ProcCPUInfo;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class BasicCLI {
    public static void main(String[] args) throws IOException {
        ProcCPUInfo procCpuInfo = new ProcCPUInfo();
        List<Map<String, Object>> procCPUInfoTables = procCpuInfo.getProcCPUInfoTables();
        for (Map<String, Object> procCPUInfoTable : procCPUInfoTables) {
            KVPPrinter(procCPUInfoTable);
        }
    }

    private static void KVPPrinter(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().toString();
            System.out.println(key + ": " + value);
        };
    }

    private static void basicReadAndPrint(String filePath) throws FileNotFoundException {
        try (Reader file = new FileReader(filePath);
             BufferedReader reader = new BufferedReader(file)) {
            Stream<String> linesStream = reader.lines();
            String[] fileLines = reader.lines().toArray(String[]::new);
            for (String line: fileLines) {
                System.out.println(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void dumpAllVFs() throws FileNotFoundException {
        basicReadAndPrint("/proc/cpuinfo");
        basicReadAndPrint("/proc/stat");
        basicReadAndPrint("/proc/meminfo");
    }
}