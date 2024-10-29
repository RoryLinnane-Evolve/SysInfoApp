package ise;

import java.io.*;
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
}

class KVP {
    private Object value = null;
    private String key = null;
    public KVP(Object value, String key) {
        this.value = value;
        this.key = key;
    }
    public Object getValue() {return value;}
    public String getKey() {return key;}
    public void setValue(Object value) {this.value = value;}
    public void setKey(String key) {this.key = key;}

}

/**
 * KVPParser class can be used to create a parser for virtual files that exhibit key-value pair format.
 * */
class KVPParser {
    private HashMap<String, ConversionOperation> keyValueConversionOperationMap = new HashMap<String, ConversionOperation>();
    private ArrayList<String> lines = new ArrayList<String>();

    /**
     * Used to configure KVPParser based on the behaviour of a virtual file.
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
     * @throws KVPLineParsingException
     * */

    public KVP process(String split, String line) throws KVPLineParsingException {
        KVP processedKVP = new KVP(null, null);
        try {
                int keyIndex = line.indexOf(split);

                if (keyIndex == -1) {
                    throw new KVPLineParsingException(line + "\n split substring: " + split + "not found");
                }

                String key = line.substring(0, keyIndex).trim();
                String value = line.substring(keyIndex + 1, line.length()).trim();
                ConversionOperation conversionOperation = keyValueConversionOperationMap.get(key); // get appropriate ConversionOperation for key's corresponding values
                if (conversionOperation != null) {
                    Object returnedValue = conversionOperation.apply(value);
                    processedKVP.setKey(key);
                    processedKVP.setValue(returnedValue);
                }
            } catch (Exception e) {
                throw new KVPLineParsingException(line);
            }
        return processedKVP;
    }

    public String[] getLines(String filePath) throws IOException {
        try (Reader file = new FileReader(filePath);
            BufferedReader reader = new BufferedReader(file)) {
            Stream<String> linesStream = reader.lines();
            return reader.lines().toArray(String[]::new);
        }
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
        },
        PARSE_INT_ARRAY_SPLIT_ON_SPACE {
            public Integer[] apply(String unprocessedVal) {
                unprocessedVal = unprocessedVal.trim().replace("\t", " ");
                String[] unprocessedValSplit = unprocessedVal.split(" ");
                List<Integer> processedVal = new ArrayList<Integer>();
                for (int i = 0; i < unprocessedValSplit.length; i++) {
                    processedVal.add(Integer.parseInt(unprocessedValSplit[i]));
                }
                return processedVal.toArray(new Integer[processedVal.size()]);
            }
        },
        PARSE_LONG {
            public Long apply(String unprocessedVal) {
                return Long.parseLong(unprocessedVal);
            }
        }
    }

    class KVPLineParsingException extends RuntimeException {
        public KVPLineParsingException(String line) {
            super("KVPParsingException: Unable to parse line: " + line + ". " );
        }
    }
}

@FunctionalInterface
interface ConversionOperation {
    Object apply(String unprocessedVal);
}


abstract class VirtualFile {
    private ArrayList<String> lines;

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
}