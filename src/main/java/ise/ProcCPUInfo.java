package ise;

import java.io.*;
import java.util.*;

public class ProcCPUInfo extends VirtualFile {
    public static void main(String[] args) throws IOException {
        ProcCPUInfo procCPUInfo = new ProcCPUInfo("/proc/cpuinfo");
        ArrayList<Hashtable<String, Object>> CPUs = procCPUInfo.getCPUs();

        System.out.println(CPUs);
    }

    public ProcCPUInfo(String filePath) throws IOException {
        super(filePath);
    };

    public ArrayList<Hashtable<String, Object>> getCPUs() {
        ArrayList<Hashtable<String, Object>> CPUs = new ArrayList<Hashtable<String, Object>>();
        List<String> lines = getLines();
        Hashtable<String, Object> thisCPU = new Hashtable<String, Object>();

        for (String line : lines) {
            if (line.isEmpty() || line.startsWith("power management")) {
                CPUs.add(thisCPU);
                continue;
            }
            String[] thisLineStringValues = line.split(":");
            String thisLineKey = thisLineStringValues[0].trim();
            String thisLineValueUnconverted = thisLineStringValues[1];
            ConvertToAptType typeConverter = new ConvertToAptType();
            Hashtable<String, Object> convertedKVP = typeConverter.convertKeyAndValue(thisLineKey, thisLineValueUnconverted);
            thisCPU.put((String) convertedKVP.get(0), convertedKVP.get(1));
        }
        return CPUs;
    }

    private  class ConvertToAptType {
        private Set<String> parseIntItems = new HashSet<String>();
        private Set<String> parseBooleanFromYesNoItems = new HashSet<String>();
        private Set<String> splitOnSpaceItems = new HashSet<String>();
        private Set<String> parseDoubleItems = new HashSet<String>();
        private final List<String> parseIntItemsList = Arrays.asList("processor", "cpu family", "model", "stepping", "physical id", "siblings", "core id", "cpu cores", "apicid", "initial apicid", "cpuid level","cflush size", "cache_alignment");
        private final List<String> parseBooleanFromYesNoItemsList = Arrays.asList("fpu", "fpu_exception", "wp");
        private final List<String> splitOnSpaceItemsList = Arrays.asList("flags", "vmx flags", "bugs");
        private final List<String> parseDoubleItemsList = Arrays.asList("cpu MHz", "bogomips");

        ConvertToAptType() {
            for (String item: parseIntItemsList) {
                parseIntItems.add(item);
            }
            for (String item: parseBooleanFromYesNoItemsList) {
                parseBooleanFromYesNoItems.add(item);
            }
            for (String item: splitOnSpaceItemsList) {
                splitOnSpaceItems.add(item);
            }
            for (String item: parseDoubleItemsList) {
                parseDoubleItems.add(item);
            }
        }

        public Hashtable<String, Object> convertKeyAndValue(String key, String value) {
            String trimmedKey = key.trim(); value = value.trim();
            String returnKey = ""; Object returnValue = null;

            if (parseIntItems.contains(trimmedKey)) {
                returnValue = KeyValueOperation.PARSE_INT.apply(value);
            } else if (parseBooleanFromYesNoItems.contains(trimmedKey)) {
                returnValue = KeyValueOperation.PARSE_BOOLEAN_FROM_YES_NO.apply(value);
            } else if (parseDoubleItems.contains(trimmedKey)) {
                returnValue = KeyValueOperation.PARSE_DOUBLE.apply(value);
            } else if (splitOnSpaceItems.contains(trimmedKey)) {
                returnValue = KeyValueOperation.SPLIT_ON_SPACE.apply(value);
            } else if (parseDoubleItems.contains(trimmedKey)) {
                returnValue = KeyValueOperation.PARSE_DOUBLE.apply(value);
            } else {
                System.out.println("Unknown key: " + trimmedKey);
            }

            Hashtable<String, Object> returnTable = new Hashtable<String, Object>();
            returnTable.put(key, returnValue);
            return returnTable;
        }

        @FunctionalInterface
        private interface KeyValueOperationInterface {
            Object apply(String stringToConvert);
        }

        private enum KeyValueOperation implements KeyValueOperationInterface {
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
            UNKNOWN_KEY {
                public Error apply(String stringToConvert) {
                    return new Error("Unknown key: " + stringToConvert);
                }
            }
        }
    }

    public void printToStdout() {}
    public void sendToMaster() {}
}