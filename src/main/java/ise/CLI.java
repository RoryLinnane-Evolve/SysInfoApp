package ise;

import org.apache.commons.cli.*;
import org.apache.commons.io.*;
import org.apache.commons.lang3.*;

import java.io.PrintWriter;
import java.sql.SQLOutput;
import java.util.Hashtable;

public class CLI {
    // set up the options
    private static final Option cpu = new Option("c","cpu",false,"display relevant cpu information");
    private static final Option busses = new Option("b","busses",false,"display relevant bus information");
    private static final Option memory = new Option("m","memory",false,"display relevant memory information");


    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        PrintWriter pw = new PrintWriter(System.out);
        pw.println();
        formatter.printUsage(pw,100, "java -jar RCHM.jar [options]");
        formatter.printOptions(pw, 100, options, 2, 5);
        pw.close();
    }


    public static void main(String[] args){
        CommandLineParser clp = new DefaultParser();

        Options options = new Options();
        options.addOption(cpu);
        options.addOption(busses);
        options.addOption(memory);
        try{
            CommandLine cl = clp.parse(options,args);

            // check to see that only one argument has been parsed
            int argCount = 0;
            if (cl.hasOption(cpu.getLongOpt())){ argCount++;}
            if (cl.hasOption(busses.getLongOpt())){ argCount++;}
            if (cl.hasOption(memory.getLongOpt())){ argCount++;}

            if (argCount != 1){ // if there is anything other than 1 argument call help
                printHelp(options);
            }

            //show data for the parsed input
            if(cl.hasOption(cpu.getLongOpt())){
                ProcStatVF cpu = new ProcStatVF("/proc/stat");

                // Get CPU occupation tables in jiffies
                Hashtable<String, Hashtable<String, Long>> cpuOccupationTables = cpu.getCPUOccupationTablesJiffies();

                // Print the detailed CPU occupation times for each CPU
                for (String cpuKey : cpuOccupationTables.keySet()) {
                    System.out.println("CPU: " + cpuKey);
                    Hashtable<String, Long> occupationTable = cpuOccupationTables.get(cpuKey);
                    for (String task : occupationTable.keySet()) {
                        System.out.println("  " + task + ": " + occupationTable.get(task) + " jiffies");
                    }
                }
            }else if(cl.hasOption(busses.getLongOpt())){
                System.out.println("busses");
            }else if(cl.hasOption(memory.getLongOpt())){
                SystemMemoryInfo memInfo = new SystemMemoryInfo();

                System.out.printf("Total: %d%n",memInfo.total);
                System.out.printf("Free: %d%n",memInfo.free);
                System.out.printf("Available: %d%n",memInfo.available);
                System.out.printf("Cached: %d%n",memInfo.cached);
                System.out.printf("Buffers: %d%n",memInfo.buffers);

            }else{
                printHelp(options); // if no input is given, give the options available
            }

        }catch (Exception e){
            e.printStackTrace();// error if the program doesn't run right
        }

    }
}
