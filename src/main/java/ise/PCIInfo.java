/*
Package for the ISE Final Project of Block 1 of First Year.
 */
package ise;



/*
Java Imports
 */
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.lang.Process;
import java.sql.Array;
import java.util.*;

public class PCIInfo {
    private static final String LSPCI_COMMAND = "lspci";

    public static void main(String[] args) throws IOException {

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


    private static int getBusCount() {
        return getCountFromCommand(LSPCI_COMMAND, "Bus");
    }
    private static int getDeviceCount() {
        return getCountFromCommand(LSPCI_COMMAND, "Device");
    }

    private static int getFunctionCount() {
        return getCountFromCommand(LSPCI_COMMAND, ":");
    }

    private static int getFunctionPresent() {
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


    private static int getCountFromCommand(String command, String startsWith) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            long count = reader.lines()
                    .filter(line -> line.contains(startsWith))
                    .count();
            return (int) count;
        }
        catch (IOException e) {
            System.err.println("Error executing " + command + " command: " + e.getMessage());
            return -1;
        }
    }

    private static String getIDFromCommand(String command, String idType) {
        try {
            java.lang.Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return reader.lines()
                    .filter(line -> line.contains(idType))
                    .findFirst()
                    .map(line -> line.split(idType)[1].trim().split("\\s+")[0])
                    .orElse("Not found");
        }
        catch (IOException e) {
            System.err.println("Error executing " + command + " command: " + e.getMessage());
            return "Error";
        }
    }

    // get all names of all pci buses by reading subdirectory names of /sys/bus/pci/devices
    private List<String> getPCIBuses() {
        List<String> buses = new ArrayList<String>();
        String SysBusPCIDevicesPath = "/sys/bus/pci/devices";
        File SysBusPCIDevices = new File(SysBusPCIDevicesPath);

        String[] PCIBusDirs = SysBusPCIDevices.list();

        if (SysBusPCIDevices != null) {
            for (String dirName: PCIBusDirs) {
                buses.add(dirName);
            }
        }

        return buses;
    }

    // get info for a single PCI bus
    public Map<String, String> getPCIBusInfo(String busName) {
        List<String> keys = Arrays.asList("");
        List<String> keyInfoLocations = Arrays.asList("");
        Map<String, String>  keyInfoLocationMap = new HashMap<>();

        // add keys and locations to map


        // get data and add to info map
        Map<String, String> info = new HashMap<String, String>();
        for (int i = 0; i < keys.size(); i++) {
            String thisKey = keys.get(i);
            info.put(thisKey, keyInfoLocationMap.get(thisKey));
        }

        return info;
    }
}