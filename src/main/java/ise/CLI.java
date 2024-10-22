package ise;

import org.apache.commons.cli.*;
import org.apache.commons.io.*;
import org.apache.commons.lang3.*;

import java.io.PrintWriter;

public class CLI {

    private static final Option cpu = new Option("c","cpu",false,"display cpu relevant information");
    private static final Option busses = new Option("b","busses",false,"display relevant bus information");
    private static final Option devices = new Option("d","devices",false,"display relevant bus information");

    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        PrintWriter pw = new PrintWriter(System.out);
        pw.println("RCHM "+ Math.class.getPackage().getImplementationVersion());
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
        options.addOption(devices);
        try{
            CommandLine cl = clp.parse(options,args);

            if (cl.getArgList().size() != 1){
                printHelp(options);
                System.exit(-1);
            }


        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
