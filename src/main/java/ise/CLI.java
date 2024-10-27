package ise;

import org.apache.commons.cli.*;
import org.apache.commons.io.*;
import org.apache.commons.lang3.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLOutput;
import java.util.*;

public class CLI {
    // set up the options
    private static final Option cpu = new Option("c","cpu",false,"display relevant cpu information");
    private static final Option cpuSearch = new Option("core",false,"search for data for a specific core");

    private static final Option busses = new Option("b","busses",false,"display relevant bus information");

    private static final Option memory = new Option("m","memory",false,"display relevant memory information");
    private static final Option kb = new Option("kb",false,"convert unit to kb");
    private static final Option mb = new Option("mb",false,"convert unit to mb");
    private static final Option gb = new Option("gb",false,"convert unit to gb");

    private static final Option process = new Option("p","process",false, "display process information");

    private static final Option help = new Option("h","help",false,"Show available commands");

    static CommandLineParser clp;
    static Options options;

    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        PrintWriter pw = new PrintWriter(System.out);
        pw.println();
        if (options.hasOption(cpuSearch.getOpt())){
            formatter.printUsage(pw,100, "java -jar RCHM.jar [options] [choice]");
        }else {
            formatter.printUsage(pw, 100, "java -jar RCHM.jar [options]");
        }
        formatter.printOptions(pw, 100, options, 2, 5);
        pw.close();
    }

    private static void resetOptions(Option... optionsToRetain) {
        Set<Option> resetTo = new HashSet<>(Arrays.asList(optionsToRetain));

        // Create a new Options object and add only the options in retainSet
        Options retainedOptions = new Options();
        for (Option option : resetTo) {
            retainedOptions.addOption(option);
        }

        // Replace the original options with retainedOptions
        CLI.options = retainedOptions;
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
  public static void cpuInfo(String choice) throws IOException {
        ProcStatVF cpu = new ProcStatVF("/proc/stat");

        // Get CPU occupation tables in jiffies
        Hashtable<String, Hashtable<String, Long>> cpuOccupationTables = cpu.getCPUOccupationTablesJiffies();
        if (choice.isEmpty()) {
            // Print the detailed CPU occupation times for each CPU
            for (String cpuKey : cpuOccupationTables.keySet()) {
                System.out.println("CPU: " + cpuKey);
                Hashtable<String, Long> occupationTable = cpuOccupationTables.get(cpuKey);
                for (String task : occupationTable.keySet()) {
                    System.out.println("  " + task + ": " + occupationTable.get(task) + " jiffies");
                }
            }
        }else{
            boolean choicePresent = false;
            for (String cpuKey : cpuOccupationTables.keySet()){
                if (cpuKey.equals(choice)){
                    System.out.println("CPU: " + cpuKey);
                    Hashtable<String, Long> occupationTable = cpuOccupationTables.get(cpuKey);
                    for (String task : occupationTable.keySet()) {
                        System.out.println("  " + task + ": " + occupationTable.get(task) + " jiffies");
                    }
                    choicePresent = true;
                }
            }
            if (!choicePresent){
                printHelp(options);// if the entered string is not a cpuKey run help command
            }
        }
    }

    public static void processInfo() throws IOException {
        System.out.println("I don't know how process info classes work :(");
    }

    public static void main(String[] args){
        clp = new DefaultParser();

        options = new Options();
        options.addOption(cpu);
        options.addOption(busses);
        options.addOption(memory);
        options.addOption(process);
        options.addOption(help);
        try {
            CommandLine cl = clp.parse(options, args);

            // check to see that only one argument has been parsed
            int argCount = 0;
            if (cl.hasOption(cpu.getLongOpt())) {argCount++;}
            if (cl.hasOption(busses.getLongOpt())) {argCount++;}
            if (cl.hasOption(memory.getLongOpt())) {argCount++;}

            if (argCount != 1) { // if there is anything other than 1 argument call help
                printHelp(options);
            }
            if (cl.hasOption(cpu.getLongOpt())) {
                resetOptions(cpu, busses, memory, process, help);
                options.addOption(cpuSearch);
                cpuInfo("");// no input to give all cpu data
            } else if (cl.hasOption(cpuSearch.getOpt())){
                cpuInfo(cl.getArgList().get(0));// argument gives data on certain core
            } else if (cl.hasOption(busses.getLongOpt())) {
                resetOptions(cpu, busses, memory, process, help);
                System.out.println("busses");
            } else if (cl.hasOption(memory.getLongOpt())) {
                resetOptions(cpu, busses, memory, process, help);
                options.addOption(kb);
                options.addOption(mb);
                options.addOption(gb);
                memoryInfo("mb");
            } else if (cl.hasOption(kb.getOpt())) {
                memoryInfo("kb");
            } else if (cl.hasOption(mb.getOpt())) {
                memoryInfo("mb");
            } else if (cl.hasOption(gb.getOpt())) {
                memoryInfo("gb");
            } else if (cl.hasOption(process.getLongOpt())){
                resetOptions(cpu, busses, memory, process, help);
                processInfo();
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
