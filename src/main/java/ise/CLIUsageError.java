package ise;

public class CLIUsageError {
    public static void noArgs() {
        System.out.printf("si <command> \n\n" +
                "Usage:\n\n" +
                "si cpu\n" +
                "si mem\n" +
                "si allprocs\n\n" +
                "All commands:\n\n" +
                "cpu, mem, allprocs");
    }
}