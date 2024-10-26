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
import java.lang.reflect.Type;
import java.sql.Array;
import java.util.*;
import java.util.stream.Stream;

class VirtualFileInfo {
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

    class KVPParser {
        private HashMap<String, VFStringValueOperation> conversionMap = new HashMap<String, VFStringValueOperation>();
        private ArrayList<String> lines = new ArrayList<String>();

        public void addConversion(List<String> keys, VFStringValueOperation conversionOperation) {
            for (String key : keys) {
                conversionMap.put(key, conversionOperation);
            }
        }

        public void run(String delimiter) {
            for (String line: lines) {
                Integer keyIndex = line.indexOf(delimiter);
                String key = line.substring(0, keyIndex);
                String value = line.substring(delimiter.length() - keyIndex);
                VFStringValueOperation conversionOperation = conversionMap.get(key);
                if (conversionOperation != null) {
                    conversionOperation.apply(value);
                }
            }
        }

        @FunctionalInterface
        public interface VFStringValueOperationsInterface {
            Object apply(String stringToConvert);
        }

        public enum VFStringValueOperation implements VFStringValueOperationsInterface  {
            PARSE_INT {
                public Integer apply(String stringToConvert) {
                    return Integer.parseInt(stringToConvert);
                }
            },
            PARSE_BOOLEAN_FROM_YES_NO {
                public Boolean apply(String stringToConvert) {
                    if (stringToConvert.equalsIgnoreCase("yes")) {
                        return true;
                    } else {
                        return false;
                    }
                }
            },
            SPLIT_ON_SPACE {
                public String[] apply(String stringToConvert) {
                    return stringToConvert.split(" ");
                }
            },
            PARSE_DOUBLE {
                public Double apply(String stringToConvert) {
                    return Double.parseDouble(stringToConvert);
                }
            },
            TRIM_STRING {
                public String apply(String stringToConvert) {
                    return stringToConvert.trim();
                }
            },
            POP_3_CHARS_RETURN_INT {
                public Integer apply(String stringToConvert) {
                    String stringInt = stringToConvert.substring(0, stringToConvert.length() -3);
                    return Integer.parseInt(stringInt);
                }
            }
        }
    }
}

abstract class VirtualFile<T, K> {
    private ArrayList<String> lines;
    private K data;

    public VirtualFile(String fileLocation) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileLocation));
        this.lines = new ArrayList<String>();
        Stream<String> readerLines = reader.lines();
        readerLines.forEach(line -> {
            this.lines.add(line);
        });
    }

    // Returns the lines of the virtual file as List<String>
    protected List<String> getLines() {
        return this.lines;
    }

    // Returns the data of the file in generic type K
    protected K getData() {
        return this.data;
    }

    public abstract void printToStdout();

    public abstract void sendToMaster(); // can remove this after, here as an idea, doesn't need to be implemented

}