package ise;

public class ProcCPUInfo extends VirtualFileInfo {

    public ProcCPUInfo(String fileLocation) {
        super(fileLocation);
        KVPParser parser = new KVPParser();
        parser.addConversion("");
    }

}