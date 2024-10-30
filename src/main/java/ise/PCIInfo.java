/*
Package for the ISE Final Project of Block 1 of First Year.
 */
package ise;



/*
Java Imports
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/*
Java process handling
 */
import java.lang.Process;
import java.util.List;

/*
PCI Info Class
 */
public class PCIInfo {
    //  Defines the command to be the Linux command lspci, to pull the information about the pci from the device.
    private static final String LSPCI_COMMAND = "lspci";

    //  Start point for the program. Also declares that there might be and IOException.
    public static void main(String[] args) throws IOException {

//      Calls each method i.e. Bus Count and output prints a message.
        int busCount = getBusCount();
        System.out.println("Bus Count: " + busCount);

        int deviceCount = getDeviceCount();
        System.out.println("Device Count: " + deviceCount);

        int functionCount = getFunctionCount();
        System.out.println("Function Count: " + functionCount);

        int functionPresent = getFunctionPresent();
        System.out.println("Function Present: " + functionPresent);

//        information taken from Mark Burkley's Library but is redundant in this code as it producing a not found response.
//        String vendorID = getVendorID();
//        System.out.println("Vendor ID: " + vendorID);
//
//        String productID = getProductID();
//        System.out.println("Product ID: " + productID);
    }


    // Each of these are the methods from the previous block of code.
    static int getBusCount() {
        return getCountFromCommand(LSPCI_COMMAND, "Bus");
    }

    static List<String> getBusList() {
        return getListFromCommand(LSPCI_COMMAND, "Bus");
    }

    // Each method runes the command ``lspci`` and looks for a keyword that is associated with the correct response.
// For ,devices it looks fof the line that starts with the keyword Device.
// For ,functions it looks for the keyword ``:`` which denotes the function line from the pci info.
    static int getDeviceCount() {
        return getCountFromCommand(LSPCI_COMMAND, "Device");
    }

    static List<String> getDeviceList() {
        return getListFromCommand(LSPCI_COMMAND, "Device");
    }

    static int getFunctionCount() {
        return getCountFromCommand(LSPCI_COMMAND, ":");
    }

    static List<String> getFunctionList() {
        return getListFromCommand(LSPCI_COMMAND, ":");
    }

    static int getFunctionPresent() {
        return getFunctionCount();
    }
//    information taken from Mark Burkley's Library but is redundant in this code as it producing a not found response.
//    private static String getVendorID() {
//        return getIDFromCommand(LSPCI_COMMAND, "ID");
//    }
//
//    private static String getProductID() {
//        return getIDFromCommand(LSPCI_COMMAND, "ID");
//    }


    //  This is the Method(s) execution.
//  Takes in two parameters, the command and the StartWith Word/Keyword.
    private static int getCountFromCommand(String command, String startsWith) {
//      Runs the command by using Java's Runtime class.
        try {
//          Creates a Buffer Reader to read the output
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//          Generates a stream of strings to represent each lien of the output.
            long count = reader.lines()
//                   Filters out any redundant lines, only keeping the lines that starts with the keywords.
                    .filter(line -> line.contains(startsWith))
//                   Counts the number of lines that pass through the filter.
                    .count();
//          Returns the count value in the form of an Integer.
            return (int) count;
        }
//      If an IOException does occur it produces an output notifying the user.
        catch (IOException e) {
            System.err.println("Error executing " + command + " command: " + e.getMessage());
            return -1;
        }
    }

    private static List<String> getListFromCommand(String command, String startsWith) {
        try {
            // Start the process with the provided command
            Process process = Runtime.getRuntime().exec(command);

            // Create a BufferedReader to read the output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            // Collect matching lines into a list
            List<String> List = new ArrayList<>();

            reader.lines()
                    .filter(line -> line.startsWith(startsWith)) // Filter lines that start with the specified keyword
                    .forEach(List::add);
            return List;

        } catch (IOException e) {
            // Log the error and return an empty list if an exception occurs
            System.err.println("An error occurred while executing the command: " + e.getMessage());
            return Collections.emptyList(); // Return an empty list on error

        }
    /*
    //  This is the Method(s) execution.
//  Takes in two parameters, the command and the IDType/Keyword.
    private static String getIDFromCommand(String command, String idType) {
//      Runs the command by using Java's Runtime class.
        try {
//          Creates a Buffer Reader to read the output
            java.lang.Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//          Generates a stream of strings to represent each lien of the output.
            return reader.lines()
//                  Filters out any redundant lines, only keeping the lines that starts with the keywords.
                    .filter(line -> line.contains(idType))
//                  Finds the first matching line
                    .findFirst()
//                  If the line is found, it splits the at idType
//                  It then removes any whitespace and then splits the line again, leaving the ID.
                    .map(line -> line.split(idType)[1].trim().split("\\s+")[0])
//                  If the line is not found, it produces the output, Not Found thus alerting the user.
                    .orElse("Not found");
//
        }
//      Catch Block to handle the IOExceptions
        catch (IOException e) {
//
            System.err.println("Error executing " + command + " command: " + e.getMessage());
            return "Error";
        }
    }
     */
    }
}