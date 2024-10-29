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

/*
Java process handling
 */
import java.lang.Process;

/*
USB Info Class
*/
public class USBInfo {
    //  Defines the command to be the Linux command lsusb, to pull the information about the usb from the device.
    private static final String LSUSB_COMMAND = "lsusb";
    //  Start point for the program. Also declares that there might be and IOException.
    public static void main(String[] args) throws IOException {

//      Calls each method i.e. Bus Count and output prints a message.
        int busCount = getBusCount();
        System.out.println("Bus Count: " + busCount);

        int deviceCount = getDeviceCount();
        System.out.println("Device Count: " + deviceCount);

        String vendorID = getVendorID();
        System.out.println("Vendor ID: " + vendorID);

        String productID = getProductID();
        System.out.println("Product ID: " + productID);
    }

    //  Each of these are the methods from the previous block of code.
    private static int getBusCount() {
        return getCountFromCommand(LSUSB_COMMAND, "Bus");
    }
    //  Each method runes the command ``lsusb`` and looks for a keyword that is associated with the correct response.
//  For ,devices it looks fof the line that starts with the keyword Device.
//  For ,IDs it looks for the keyword ``id`` which denotes the function line from the pci info.
    private static int getDeviceCount() {
        return getCountFromCommand(LSUSB_COMMAND, "Device");
    }

    private static String getVendorID() {
        return getIDFromCommand(LSUSB_COMMAND, "ID");
    }

    private static String getProductID() {
        return getIDFromCommand(LSUSB_COMMAND, "ID");
    }

    //  This is the Method(s) execution.
//  Takes in two parameters, the command and the StartWith Word/Keyword.
    private static int getCountFromCommand(String command, String startsWith) {
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
            return -1; // Return -1 to indicate an error
        }
    }
    //  This is the Method(s) execution.
//  Takes in two parameters, the command and the IDType/Keyword.
    private static String getIDFromCommand(String command, String idType) {
//      Runs the command by using Java's Runtime class.
        try {
//          Creates a Buffer Reader to read the output
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//          Generates a stream of strings to represent each lien of the output.
            return reader.lines()
//                   Filters out any redundant lines, only keeping the lines that starts with the keywords.
                    .filter(line -> line.contains(idType))
//                   Finds the first matching line
                    .findFirst()
//                   If the line is found, it splits the at idType
//                   It then removes any whitespace and then splits the line again, leaving the ID.
                    .map(line -> line.split(idType)[1].trim().split("\\s+")[0])
//                   If the line is not found, it produces the output, Not Found thus alerting the user.
                    .orElse("Not found");
        }
//      Catch Block to handle the IOExceptions
        catch (IOException e) {
            System.err.println("Error executing " + command + " command: " + e.getMessage());
            return "Error";
        }
    }
}
