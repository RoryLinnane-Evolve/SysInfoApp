package ise;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;




public class USBInfo {

    private static final String LSUSB_COMMAND = "lsusb";

    public static void main(String[] args) throws IOException {
        execCommand("lsusb");


        int busCount = getBusCount();
        System.out.println("Bus Count: " + busCount);

        int deviceCount = getDeviceCount();
        System.out.println("Device Count: " + deviceCount);

        String vendorID = getVendorID();
        System.out.println("Vendor ID: " + vendorID);

        String productID = getProductID();
        System.out.println("Product ID: " + productID);
    }


    private static int getBusCount() {
        return getCountFromCommand(LSUSB_COMMAND, "Bus");
    }

    private static int getDeviceCount() {
        return getCountFromCommand(LSUSB_COMMAND, "Device");
    }

    private static String getVendorID() {
        return getIDFromCommand(LSUSB_COMMAND, "ID");
    }

    private static String getProductID() {
        return getIDFromCommand(LSUSB_COMMAND, "ID");
    }


    private static int getCountFromCommand(String command, String startsWith) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            long count = reader.lines()
                    .filter(line -> line.contains(startsWith))
                    .count();
            return (int) count;
        } catch (IOException e) {
            System.err.println("Error executing " + command + " command: " + e.getMessage());
            return -1; // Return -1 to indicate an error
        }
    }

    private static String getIDFromCommand(String command, String idType) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return reader.lines()
                    .filter(line -> line.contains(idType))
                    .findFirst()
                    .map(line -> line.split(idType)[1].trim().split("\\s+")[0])
                    .orElse("Not found");
        } catch (IOException e) {
            System.err.println("Error executing " + command + " command: " + e.getMessage());
            return "Error";
        }
    }

    private static void execCommand(String command) throws IOException {

        // ... (rest of the method remains the same)
    }
}