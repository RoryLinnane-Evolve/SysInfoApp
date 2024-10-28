package ise;
import org.apache.commons.cli.*;

import java.util.ArrayList;
import java.util.List;

public class CLI {
    public static void main(String[] args) {
        Options options = new Options();

        Option markMode = Option.builder("m")
                .longOpt("markMode")
                .desc("Submodule used to specify system information and attached devices")
                .hasArgs()
                .valueSeparator(' ')
                .build();

        options.addOption(markMode);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter helpFormatter = new HelpFormatter();
        try {
            CommandLine cmd = parser.parse(options, args);

            Printer printer = new Printer();
            if (cmd.hasOption("m")) {
                String[] markModeVals = cmd.getOptionValues("m");
                for (String markModeVal : markModeVals) {
                    switch (markModeVal) {
                        case "-pci":
                            printer.addPrintItem("PCIItems");
                            break;
                        case "-disk":
                            printer.addPrintItem("DiskItems");
                            break;
                        case "-usb":
                            printer.addPrintItem("USBItems");
                            break;
                        default:
                            System.out.println("Unknown argument value for option: " + markModeVal);
                            helpFormatter.printHelp("cli", options);
                            break;
                    }
                }
            }

            printer.print();
        } catch (ParseException e) {
            System.err.println("Parsing failed. Reason: " + e.getMessage());
        }
    }

    private static void listPCIDevices() {
        System.out.println("PCI Devices");
    }
}

class Printer {
    private List<String> thingsToPrint = new ArrayList<String>();

    public void addPrintItem(String thingToPrint) {
        thingsToPrint.add(thingToPrint);
    }

    public void print() {
        for (String thingToPrint : thingsToPrint) {
            System.out.println(thingToPrint);
        }
    }
}