/**
 * This will query a virtual file (basing the directory structure on standard Ubuntu directory
 * structure) and return the results found as a hashtable, where the key value pairs are
 * derived using a colon as a delimiter.
 *
 * @author Mikey Fennelly
 * @version 1.0
 * */

package ise;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.stream.Stream;

public class VirtualFileInfo<T, K> {
    public String fileLocation;
    public VirtualFileInfo(String fileLocation) {
        this.fileLocation = fileLocation;
    }
    public Hashtable<String, String> fileInfo;

    /**
     * Sets this.fileInfo, provided the file is a colon seperated
     * stream of key value pairs.
     * @return void
     * */
    public void setHashtable() throws IOException {
        FileReader file = new FileReader(this.fileLocation);
        BufferedReader reader = new BufferedReader(file);
        Stream<String> lines = reader.lines();

        Hashtable<String, String> table = new Hashtable<>();
        lines.forEach(line -> {
            String[] thisLineItems = line.split(":");
            try {
                table.put(thisLineItems[0].trim(), thisLineItems[1].trim());
            } catch (ArrayIndexOutOfBoundsException e) {
//                ignoring this exception for now
            } catch (Exception e) {
                System.out.println(e.getMessage());;
            }
        });

        this.fileInfo = table;
    }

    public  <T, K> HashMap<?, ?[]> getGenericHashMap() throws IOException {
        FileReader file = new FileReader(this.fileLocation);
        BufferedReader reader = new BufferedReader(file);
        Stream<String> lines = reader.lines();

        HashMap<T, K[]> table = new HashMap<>();
        lines.forEach(line -> {
            String[] splitLine = line.split(" ");
            T key = (T) splitLine[0];
            K[] value = (K[]) Arrays.copyOfRange(splitLine, 1, splitLine.length);
            table.put(key, value);
        });

        return table;
    }
}