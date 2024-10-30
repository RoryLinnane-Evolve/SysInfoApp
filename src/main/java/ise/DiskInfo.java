/*
Package for the ISE Final Project of Block 1 of First Year.
*/
package ise;



/*
Java Imports
 */
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.Process;
import java.util.List;
import java.util.stream.Collectors;

/*
Disk Info Class
 */
public class    DiskInfo {
    //  Defines the command to be the Linux command df, to pull the information about the disk from the device.
    private static final String DF_COMMAND = "df";
    //  Start point for the program. Also declares that there might be and IOException.
    public static void main(String[] args) throws IOException {
        List<String> dfOutput = execCommand(DF_COMMAND);
//      Calls each method i.e. Bus Count and output prints a message.
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
  
    public static String[] getAllDiskInformation() throws IOException {
        String[] Data = new String[5];
        List<String> dfOutput = execCommand(DF_COMMAND);

        int diskCount = getDiskCount(dfOutput);
        Data[0] = "Disk Count: " + diskCount;

        String deviceName = getDeviceName(dfOutput);
        Data[1] = "Device Name: " + deviceName;

        long diskTotal = getDiskTotal(dfOutput);
        Data[2] = "Total Disk space: " + diskTotal;

        long diskUsed = getDiskUsed(dfOutput);
        Data[3] = "Used Disk space: " + diskUsed;

        long diskAvailable = getDiskAvailable(dfOutput);
        Data[4] = "Available Disk space: " + diskAvailable;
        return Data;
    }

  private static int getDiskCount(List<String> dfOutput) {
        return dfOutput.size() - 1;
    }

    private static String getDeviceName(List<String> dfOutput) {
//      Converts the list to a stream
        return dfOutput.stream()
//               Skips the first line, due to it typically being a header
                .skip(1)
//               Finds the first lien after the header
                .findFirst()
//               Line Split
                .map(line -> line.split("\\s+")[0])
//               If result is not found it produces an error to notify the user
                .orElse("Not Found");
    }

    private static long getDiskTotal(List<String> dfOutput) {
        return parseDfOutput(dfOutput, 1);
    }

    private static long getDiskUsed(List<String> dfOutput) {
        return parseDfOutput(dfOutput, 2);
    }

    private static long getDiskAvailable(List<String> dfOutput) {
        return parseDfOutput(dfOutput, 3);
    }
    
    /**
    * Parses output of 'df' command on linux
    * @param List<String> - lines from output of df command
    * @param int - column index
    * @return long - total disk space in KB
    */
    private static long parseDfOutput(List<String> dfOutput, int columnIndex) {
        return dfOutput.stream()
                .skip(1)
//               maps it line to a long value
//               Splits the line of any whitespace
//               Selects specific column from the line
//               Converts the string/column to a long value
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