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
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Stream;

public class VirtualFileInfo {
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
                System.out.println(e.getMessage());
            }
        });

        this.fileInfo = table;
    }
}

abstract class VirtualFile<T> {
    private ArrayList<String> lines;

    public VirtualFile(String fileLocation) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileLocation));
        this.lines = new ArrayList<String>();
    }

    protected List<String> getLines() {
        return this.lines;
    }

    public abstract void printToStdout();

    public abstract void sendToMaster(); // can remove this after, here as an idea, doesn't need to be implemented

}