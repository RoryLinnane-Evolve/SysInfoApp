/**
 * Retrieves and represents information about a process such as memory (virtual and resident)
 * usage, CPU usage, process user, PID, PPID, priority, niceness.
 *
 * @author Mikey Fennelly
 * @version 1.0
 * */

package ise;

import java.io.IOException;

enum ProcState {
    RUN, SLEEP, UNINTERRUPTABLE_SLEEP, STOPPED, ZOMBIE
}

/**
 * Provides information about processes similar to that of running the 'top'/'htop' command
 * in bash
 * */
public class Process {

}