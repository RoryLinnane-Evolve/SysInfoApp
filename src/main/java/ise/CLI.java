package ise;
import org.apache.commons.cli.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * implements the CLI functionality of the application
 * @author Mikey Fennelly
 * @version 1.0
 */
public class CLI {
    private static List<Sysinfo> sysInfos = Arrays.asList(
      new ProcCPUInfo()
    );

    public static void main(String[] programArgs) {
        sysInfos.forEach(Sysinfo::printToConsole);
    }
}
