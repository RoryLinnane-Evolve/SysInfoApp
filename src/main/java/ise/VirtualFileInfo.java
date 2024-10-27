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
import java.util.*;
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

    /**
     * KVPParser class can be used to create a parser for virtual files that exhibit key-value pair format.
     * */
    class KVPParser {
        private HashMap<String, ConversionOperation> keyValueConversionOperationMap = new HashMap<String, ConversionOperation>();
        private ArrayList<String> lines = new ArrayList<String>();
        private Hashtable<String, Object> processedKVPs = new Hashtable<String, Object>();

        /**
         * Method is used to configure KVPParser based on the behaviour of a virtual file.
         * @param conversionOperation - the operation that is to be applied to all corresponding values of the keys.
         * @param keys - list of keys whose values require a conversionOperation.
         * @return void
         * */
        public void addConversion(List<String> keys, ConversionOperation conversionOperation) {
            for (String key : keys) {
                keyValueConversionOperationMap.put(key, conversionOperation);
            }
        }

        /**
         * Once parser conversions are configured, run the 'process()' method on specified file location.
         * @param split: This is the substring that marks the separation of keys and values.
         * @return The processed key-value pairs in form Hashtable<String, Object>
         * */
        public Hashtable<String, Object> process(String split) {
            for (String line: lines) { // process every line in file
                Integer keyIndex = line.indexOf(split);
                String key = line.substring(0, keyIndex);
                String value = line.substring(line.length() - keyIndex);
                ConversionOperation conversionOperation = keyValueConversionOperationMap.get(key); // get appropriate ConversionOperation for key's corresponding values
                if (conversionOperation != null) {
                    Object returnedValue = conversionOperation.apply(value);
                    processedKVPs.put(key, returnedValue); // add key and processed values to processedKVPs Hashtable
                }
            }
            return processedKVPs;
        }

        @FunctionalInterface
        public interface ConversionOperation {
            Object apply(String unprocessedVal);
        }


        public enum premadeConversionOperation implements ConversionOperation {
            /**
             * Given a String 'unprocessedVal', will parse the unprocessedVal to integer.
             * @implements ConversionOperation
             * @throws NumberFormatException
             * @returns parsedInt
             * */
            PARSE_INT {
                public Integer apply(String unprocessedVal) throws NumberFormatException {
                    return Integer.parseInt(unprocessedVal);
                }
            },
            /**
             * Given String 'unprocessedVal' will return boolean value 'true' if unprocessedVal="yes" and boolean val 'false' otherwise.
             * @param unprocessedVal
             * @returns Boolean
             * @throws InputMismatchException - if unprocessedVal is neither yes nor no.
             * */
            BOOLEAN_FROM_YES_NO {
                public Boolean apply(String unprocessedVal) throws InputMismatchException {
                    if (unprocessedVal.equalsIgnoreCase("yes")) {
                        return true;
                    } else if (unprocessedVal.equalsIgnoreCase("no")) {
                        return false;
                    } else {
                        throw new InputMismatchException("The inputted value " + unprocessedVal + " is neither 'yes' nor 'no'");
                    }
                }
            },
            /**
             * Takes in string unprocessedVal and returns a String[] of the words in the String unprocessedVal.
             * Splits unprocessedVal based on spaces.
             * @param unprocessedVal
             * @returns String[]
             * */
            SPLIT_ON_SPACE {
                public String[] apply(String unprocessedVal) {
                    return unprocessedVal.split(" ");
                }
            },
            /**
             * Parses unprocessedVal to Double and returns processed value.
             * @param unprocessedVal
             * @returns Double
             * @throws NumberFormatException
             * */
            PARSE_DOUBLE {
                public Double apply(String unprocessedVal) throws NumberFormatException {
                    try {
                        return Double.parseDouble(unprocessedVal);
                    } catch (NumberFormatException e) {
                        throw new NumberFormatException("The inputted value " + unprocessedVal + " can not be parsed to double");
                    }
                }
            },
            /**
             * Accepts param unprocessedVal of type String, applies String.trim() method and returns
             * @param unprocessedVal
             * @returns String
             * */
            TRIM_STRING {
                public String apply(String unprocessedVal) {
                    return unprocessedVal.trim();
                }
            },
            /**
             * Pops last 3 chars from String unprocessedVal, then casts to Integer and returns processed value.
             * @param unprocessedVal
             * @return Integer
             * @throws NumberFormatException
             * */
            POP_3_CHARS_RETURN_INT {
                public Integer apply(String unprocessedVal) throws NumberFormatException {
                    String stringInt = unprocessedVal.substring(0, unprocessedVal.length() -3);
                    try {
                        return Integer.parseInt(stringInt);
                    } catch (NumberFormatException e) {
                        throw new NumberFormatException("The inputted value " + unprocessedVal + " can not be parsed to int");
                    }
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