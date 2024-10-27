package ise;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

class DiskInfo {

    private List<String> execCommand(String... command) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        Process fdiskCommandProc = pb.start();

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(fdiskCommandProc.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(fdiskCommandProc.getErrorStream()));

        List<String> lines = new ArrayList<String>();

        String line;
        while ((line = stdInput.readLine()) != null) {
            lines.add(line);
        }

        return lines;
    }
}