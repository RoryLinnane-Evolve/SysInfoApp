package ise;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PCIInfo {

    public static void main(String[] args) throws IOException {
        execCommand("lscpu");
    }

    /**
     * executes shell commands
     * @return
    */
     private static String execCommand(String command) throws IOException {
        // execute the command in process called 'process'
        Process process = Runtime.getRuntime().exec(command);
        // Read input&error streams of process
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        reader.lines().forEach(System.out::println);
    }
}

interface PCIInfoInterface {
    int busCount();
}