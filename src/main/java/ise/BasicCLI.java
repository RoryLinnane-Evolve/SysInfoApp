package ise;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Implements CLI functionality in most basic fashion.
 * @author Mikey Fennelly
 * @version 1.0
 * */
public class BasicCLI {
    public static void main(String[] args) throws IOException {
        MetricServer server = new MetricServer("172.27.254.4", 8080);
        server.start();
        dumpAllVFs();
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
        // Print /proc/cpuinfo
        System.out.println();
        System.out.println();
        System.out.println("/proc/cpuinfo");
        System.out.println();
        basicReadAndPrint("/proc/cpuinfo");
        // Print /proc/stat
        System.out.println();
        System.out.println();
        System.out.println("/proc/stat");
        System.out.println();
        basicReadAndPrint("/proc/stat");
        // print /proc/meminfo
        System.out.println();
        System.out.println();
        System.out.println("/proc/meminfo");
        System.out.println();
        basicReadAndPrint("/proc/meminfo");
    }
}