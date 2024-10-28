package ise;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * implements the CLI functionality of the application
 * @author Mikey Fennelly
 * @version 1.0
 */
public class CLI {
    private static Map<Option, ArrayList<String>> optionsAndValidValues = new HashMap<Option, ArrayList<String>>();

    static Option markMode = Option.builder("m")
            .longOpt("markMode")
            .desc("Submodule used to specify system information and attached devices")
            .hasArgs()
            .valueSeparator(' ')
            .hasArg(true)
            .build();

    public static void main(String[] programArgs) {
        Options validOptions = new Options();
        HelpFormatter helpFormatter = new HelpFormatter();
        CommandLineParser parser = new DefaultParser();

        validOptions.addOption(markMode);
        try {
            CommandLine cmd = parser.parse(validOptions, programArgs);
            processOptions(cmd);
        } catch (ParseException e) {
            System.err.println("Parsing failed. Reason: " + e.getMessage());
        }
    }

    private static void listPCIDevices() {
        System.out.println("PCI Devices");
    }

    private static void processOptions(CommandLine cmd) throws ParseException {
        // iterate through all options parser parsed
        for (Option option : cmd.getOptions()) {
            String[] optionVals = option.getValues();
        }
        // iterate through options
        // check if the option values are valid
        // check validity of optionValues for the arg
    }
}

class BasicCLI {
    public static void main(String[] args) throws IOException {
        ProcCPUInfo procCpuInfo = new ProcCPUInfo();
        List<Map<String, Object>> procCPUInfoTables = procCpuInfo.getProcCPUInfoTables();
        for (Map<String, Object> procCPUInfoTable : procCPUInfoTables) {
            KVPPrinter(procCPUInfoTable);
        }
    }

    private static void KVPPrinter(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().toString();
            System.out.println(key + ": " + value);
        };
    }
}