package ise;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class DiskInfo {

    private static final String DF_COMMAND = "df";

    public static void main(String[] args) throws IOException {
        List<String> dfOutput = execCommand(DF_COMMAND);

        int diskCount = getDiskCount(dfOutput);
        System.out.println("Disk Count: " + diskCount);

        String deviceName = getDeviceName(dfOutput);
        System.out.println("Device Name: " + deviceName);

        long diskTotal = getDiskTotal(dfOutput);
        System.out.println("Total Disk(s): " + diskTotal);

        long diskUsed = getDiskUsed(dfOutput);
        System.out.println("Used Disk(s): " + diskUsed);

        long diskAvailable = getDiskAvailable(dfOutput);
        System.out.println("Available Disk(s): " + diskAvailable);

    }

    private static int getDiskCount(List<String> dfOutput) {
        return dfOutput.size() - 1;
    }

    private static String getDeviceName(List<String> dfOutput) {
        return dfOutput.stream()
                .skip(1)
                .findFirst()
                .map(line -> line.split("\\s+")[0])
                .orElse("Not Found");
    }





//    private static int getDiskCount(List<String> dfOutput) {
//        return getCountFromCommand(dfOutput, 1);
//    }
//
//    private static String getDeviceName(List<String> dfOutput) {
//        return getIDFromCommand(dfOutput, 2);
//    }

    private static long getDiskTotal(List<String> dfOutput) {
        return parseDfOutput(dfOutput, 1);
    }

    private static long getDiskUsed(List<String> dfOutput) {
        return parseDfOutput(dfOutput, 2);
    }

    private static long getDiskAvailable(List<String> dfOutput) {
        return parseDfOutput(dfOutput, 3);
    }

    private static long parseDfOutput(List<String> dfOutput, int columnIndex) {
        return dfOutput.stream()
                .skip(1) // Skip header
                .mapToLong(line -> Long.parseLong(line.split("\\s+")[columnIndex]))
                .sum();
    }

    private static List<String> execCommand(String command) throws IOException {
        Process process = Runtime.getRuntime().exec(command);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            return reader.lines().collect(Collectors.toList());
        }
    }
}