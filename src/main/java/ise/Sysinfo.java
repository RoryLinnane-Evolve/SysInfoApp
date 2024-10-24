/**
 * A proof of concept application for distributed metrics slave agent.
 *
 * @version 1.0
 * @author Mikey Fennelly
 */
package ise;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Sysinfo {
    public static void main(String[] args) throws IOException {
//        ArrayList<Integer> pIds = SystemProcessInfo.getAllRunningPIDs();
//
//        for (Map.Entry<String, String[]> entry : hashMap.entrySet()) {
//            String key = entry.getKey();
//            String[] values = entry.getValue();
//            System.out.println("Key: " + key + " | Values: " + Arrays.toString(values));
//        }
    }
}

interface SysinfoInterface {
    ArrayList<Integer> getRunningPIDs();
    SystemProcessInfo getProcessInfo(int pid);
    SystemMemoryInfo getSystemMemoryInfo();
}