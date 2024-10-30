package ise;

import org.apache.commons.cli.*;
import org.apache.commons.io.*;
import org.apache.commons.lang3.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.*;
import java.net.ServerSocket;
import java.rmi.MarshalException;
import java.sql.SQLOutput;
import java.util.*;

public class CLI {
    // set up the options
    private static final Option cpu = new Option("c","cpu",false,"display  cpu information");
    private static final Option usb = new Option("u","usb",false,"display usb information");
    private static final Option pci = new Option("p","pci",false,"display pci information");
    private static final Option disk = new Option("d","disk",false,"display disk information");
    private static final Option memory = new Option("m","memory",false,"display  memory information");
    private static final Option process = new Option("P","Process",false, "display process information");
    private static final Option server = new Option("s","server",false,"start server features");

    private static final Option help = new Option("h","help",false,"Show available commands");

    static CommandLineParser clp;
    static Options options;

    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        PrintWriter pw = new PrintWriter(System.out);
        pw.println();
        formatter.printUsage(pw, 100, "java -jar host-info-agent-1.0-SNAPSHOT.jar [options] [extra arguments]");
        formatter.printOptions(pw, 100, options, 2, 5);
        pw.close();
    }

    // Method for the  display bar
    private static String  displayBar(int numerator, int denominator) {
        int barLength = 50; // Length of the progress bar
        int completed = (int) ((numerator / (float) denominator) * barLength);

        StringBuilder progressBar = new StringBuilder("[");
        for (int i = 0; i < barLength; i++) {
            if (i < completed) {
                progressBar.append("=");
            } else {
                progressBar.append(" ");
            }
        }
        progressBar.append("] ");

        // Calculate and append the percentage completion
        int percentComplete = (int) ((numerator / (float) denominator) * 100);
        progressBar.append(percentComplete).append("%");

        // return the display bar and percentage
        return progressBar.toString();
    }

    private static String findIp(){
        String address = "";
        try {
            // Get the local host address
            InetAddress inetAddress = InetAddress.getLocalHost();

            // Print IP Address
            address = inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            System.out.println("Could not get IP address: " + e.getMessage());
        }
        return address;
    }

    private static int findFreePort(){
        try (ServerSocket socket = new ServerSocket(0)) {
            // Bind the socket to a free port and return it
            return socket.getLocalPort();
        } catch (IOException e) {
            System.out.println("Could not find a free port: " + e.getMessage());
            return -1; // Return -1 to indicate failure
        }
    }
    private static void memoryInfo(String unit) throws IOException {
        SystemMemoryInfo memInfo = new SystemMemoryInfo();
        int ratio = 1;
        switch (unit) {
            case "kb":
                break;
            case "mb":
                ratio = 1000;
                break;
            case "gb":
                ratio = 1000000;
                break;
            default:
                printHelp(options);
                break;
        }
        System.out.printf("Total: %d %s%n%n",memInfo.total/ratio, unit);
        System.out.printf("%s Free%n%d %s%n",displayBar(memInfo.free, memInfo.total),memInfo.free/ratio, unit);
        System.out.printf("%s Available%n%d %s%n",displayBar(memInfo.available, memInfo.total),memInfo.available/ratio, unit);
        System.out.printf("%s Cached%n%d %s%n",displayBar(memInfo.cached, memInfo.total),memInfo.cached/ratio, unit);
        System.out.printf("%s Available%n%d %s%n",displayBar(memInfo.available, memInfo.total),memInfo.available/ratio, unit);
        System.out.printf("%s Buffers%n%d %s%n",displayBar(memInfo.buffers, memInfo.total),memInfo.buffers/ratio, unit);

    }
  public static void cpuInfo(String cpuNo) throws IOException {
      // Create an instance of ProcCPUInfo
      ProcCPUInfo cpuInfo = new ProcCPUInfo();

      // Call getProcCPUInfoTables to retrieve the CPU information
      List<Map<String, Object>> cpuInfoTables = cpuInfo.getProcCPUInfoTables();
      System.out.println();
      // Print the CPU information
      System.out.println("CPU Information:");
      int cpuNumber = 1;
      for (Map<String, Object> cpuTable : cpuInfoTables) {
          if (cpuNo.equals("") || cpuNumber == Integer.parseInt(cpuNo.substring(3))){
              System.out.println("CPU " + cpuNumber + ":");
              for (Map.Entry<String, Object> entry : cpuTable.entrySet()) {
                  System.out.printf("  %s: %s%n", entry.getKey(), entry.getValue());
              }
          }
          cpuNumber++;
          System.out.println();
      }
    }

    public static void usbInfo(){
        // Calls each method from USBInfo and prints the output
        int busCount = USBInfo.getBusCount();
        System.out.println("Bus Count: " + busCount);

        int deviceCount = USBInfo.getDeviceCount();
        System.out.println("Device Count: " + deviceCount);

        String vendorID = USBInfo.getVendorID();
        System.out.println("Vendor ID: " + vendorID);

        String productID = USBInfo.getProductID();
        System.out.println("Product ID: " + productID);
    }

    public static void pciInfo(){
        // Calls each method from PCIInfo and prints the output
        int busCount = PCIInfo.getBusCount();
        System.out.println("Bus Count: " + busCount);

        int deviceCount = PCIInfo.getDeviceCount();
        System.out.println("Device Count: " + deviceCount);

        int functionCount = PCIInfo.getFunctionCount();
        System.out.println("Function Count: " + functionCount);

        int functionPresent = PCIInfo.getFunctionPresent();
        System.out.println("Function Present: " + functionPresent);
    }

    public static void pciDetailed(String choice){
        switch (choice) {
            case "Bus" -> {
                List<String> BusList = PCIInfo.getBusList();
                if (BusList.isEmpty()){
                    System.out.println("empty");
                }
                System.out.println("Bus Info:");
                for (String item : BusList) {
                    System.out.println("    " + item);
                }
            }
            case "Device" -> {
                List<String> DeviceList = PCIInfo.getDeviceList();
                System.out.println("Device Info:");
                for (String item : DeviceList) {
                    System.out.println("    " + item);
                }
            }
            case "Function" -> {
                List<String> FunctionList = PCIInfo.getFunctionList();
                System.out.println("Function Info:");
                for (String item : FunctionList) {
                    System.out.println("    " + item);
                }
            }
            default -> printHelp(options);
        }
    }
    public static void diskInfo(){
        // Capture the output of DiskInfo's main method
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);

        // Redirect System.out to our print stream
        PrintStream originalOut = System.out;
        System.setOut(printStream);

        try {
            // Call the main method of DiskInfo
            DiskInfo.main(new String[]{});
        } catch (IOException e) {
            System.err.println("Error executing DiskInfo: " + e.getMessage());
        } finally {
            // Restore the original System.out
            System.setOut(originalOut);
        }

        // Get the output as a string
        String output = outputStream.toString();

        // Print the output
        System.out.println(output);
    }
    public static void processInfo(String procNo){
        // Instantiate ProcPIDStatus and ProcPIDStat
        ProcPIDStatus procPIDStatus = new ProcPIDStatus();
        ProcPIDStat procPIDStat = new ProcPIDStat();


        try {
            // Retrieve a list of all active PIDs using the `ps` command
            List<Integer> activePIDs = getActivePIDs();

            // Loop through each PID to retrieve status and stat information
            boolean empty = true;
            for (int pid : activePIDs) {
                if (procNo.equals("") || pid == Integer.parseInt(procNo.substring(3))){
                    System.out.println("Process Information for PID: " + pid);
                    empty = false;
                    // Retrieve process status information for the current PID
                    Map<String, Object> statusInfo = procPIDStatus.getProcessInfo(pid);
                    System.out.println("Status Information:");
                    for (Map.Entry<String, Object> entry : statusInfo.entrySet()) {
                        System.out.println(entry.getKey() + " : " + entry.getValue());
                    }

                    // Retrieve process stat information for the current PID
                    Map<String, Object> statInfo = procPIDStat.getProcPIDStatInfo(pid);
                    System.out.println("Stat Information:");
                    for (Map.Entry<String, Object> entry : statInfo.entrySet()) {
                        System.out.println(entry.getKey() + " : " + entry.getValue());
                    }

                    System.out.println("--------------------------------------------------");
                }
            }
            if (empty){
                System.out.println("That was not a pid.");
            }
        } catch (IOException e) {
            System.err.println("An error occurred while retrieving process information: " + e.getMessage());
        }
    }

    private static List<Integer> getActivePIDs() throws IOException {
        List<Integer> pidList = new ArrayList<>();
        ProcessBuilder processBuilder = new ProcessBuilder("ps", "-e", "-o", "pid=");
        Process process = processBuilder.start();

        // Read the output of the `ps` command
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    // Convert PID from string to integer and add to list
                    int pid = Integer.parseInt(line.trim());
                    pidList.add(pid);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid PID: " + line);
                }
            }
        }
        return pidList;
    }

    public static void serverRun(String ip, int port) throws IOException {
        if (port != -1) {
            System.out.println("Server started at " + ip + " : " + port);
            MetricServer server = new MetricServer(ip, port);
            server.start();
        } else {
            System.out.println("No free port found.");
        }
    }
    public static void main(String[] args){
        clp = new DefaultParser();

        options = new Options();
        options.addOption(cpu);
        options.addOption(usb);
        options.addOption(pci);
        options.addOption(disk);
        options.addOption(memory);
        options.addOption(process);
        options.addOption(server);
        options.addOption(help);
        try {
            CommandLine cl = clp.parse(options, args);

            // check to see that only one argument has been parsed
            int argCount = 0;
            if (cl.hasOption(cpu.getLongOpt())) {argCount++;}
            if (cl.hasOption(usb.getLongOpt())) {argCount++;}
            if (cl.hasOption(pci.getLongOpt())) {argCount++;}
            if (cl.hasOption(disk.getLongOpt())) {argCount++;}
            if (cl.hasOption(memory.getLongOpt())) {argCount++;}
            if (cl.hasOption(process.getLongOpt())) {argCount++;}
            if (cl.hasOption(server.getLongOpt())) {argCount++;}

            if (argCount != 1) { // if there is anything other than 1 argument call help
                printHelp(options);
            }
            if (cl.hasOption(cpu.getLongOpt())) {
                if (args.length == 1) {
                    cpuInfo("");
                } else {
                    cpuInfo(args[1]);
                }
            } else if (cl.hasOption(usb.getLongOpt())) {
                usbInfo();                                                                     //see about vendor and product id
            } else if(cl.hasOption(pci.getLongOpt())){
                if (args.length == 1) {
                    pciInfo();
                } else {
                    pciDetailed(args[1]);
                }
            } else if (cl.hasOption(disk.getLongOpt())) {
                diskInfo();
            } else if (cl.hasOption(memory.getLongOpt())) {
                if (args.length == 1){
                    memoryInfo("mb");
                } else if (args[1].equals("kb")){
                    memoryInfo("kb");
                } else if (args[1].equals("mb")){
                    memoryInfo("mb");
                } else if (args[1].equals("gb")) {
                    memoryInfo("gb");
                } else {
                    printHelp(options);
                }
            } else if (cl.hasOption(process.getLongOpt())){
                if (args.length == 1){
                    processInfo("");
                } else {
                    processInfo(args[1]);
                }
            } else if (cl.hasOption(server.getLongOpt())){
                serverRun(findIp(),findFreePort());
            }else if (cl.hasOption(help.getLongOpt())) {
                printHelp(options);
            } else {
                printHelp(options);
            }
        }catch (Exception e){
            e.printStackTrace();// error if the program doesn't run right
        }

    }
}
