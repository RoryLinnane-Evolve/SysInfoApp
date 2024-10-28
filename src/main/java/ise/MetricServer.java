/**
 * This is the file for the server that recieves get requests
 * from prometheus and responds with the data in the gauges set out below.
 * The data is set every 4 seconds
 *
 * @author Rory Linnane
 * @version 1.0
 * */

package ise;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.GaugeMetricFamily;
import io.prometheus.client.exporter.HTTPServer;
import ise.SystemMemoryInfo;
import java.lang.Math;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class MetricServer {

    // Create gauge metrics for system memory properties
    static final Gauge totalMemory = Gauge.build()
            .name("system_memory_total_kilobytes")
            .help("Total system memory in kilobytes.")
            .register();

    static final Gauge freeMemory = Gauge.build()
            .name("system_memory_free_kilobytes")
            .help("Free system memory in kilobytes.")
            .register();

    static final Gauge systemMemoryUsage = Gauge.build()
            .name("system_memory_usage")
            .help("System memory usage percentage.")
            .register();

    static final Gauge systemCPUMHz = Gauge.build()
            .name("system_CPU_MHz")
            .help("CPU clock speed in megahertz.")
            .register();

    static final Gauge processInfo = Gauge.build()
            .name("system_process_info")
            .help("Information about system processes")
            .labelNames("pid", "name", "state")
            .register();


    public static void main(String[] args) throws IOException {
        // Starts a http server
        HTTPServer server = new HTTPServer(8080);


        Thread memAndCPU = new Thread(() -> {
            try {
                systemMemmoryAndCPUUpdater();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Thread procs = new Thread(() -> {
            try {
                processesUpdater();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        procs.start();
        memAndCPU.start();
    }

    public static void systemMemmoryAndCPUUpdater() throws Exception{
        VirtualFileInfo cpuinfo = new VirtualFileInfo("/proc/cpuinfo");
        ProcStatVF coresinfo = new ProcStatVF("/proc/stat");
        while(true){
            try{
                // Get system memory information
                SystemMemoryInfo mem = new SystemMemoryInfo();

                // Update the memory Gauges with the current memory info
                totalMemory.set(mem.total);
                freeMemory.set(mem.free);

                // get the memory usage percentage by dividing the (total memory - available memory) by the total memory and multiplying by 100
                double memusage = (double)(mem.total-mem.available)/mem.total * 100;
                systemMemoryUsage.set(Math.round(memusage));

                //Update cpu mhz
                cpuinfo.setHashtable();
                //retrieve it from Hashtable and update gauge
                double cpuMHz = Double.parseDouble(cpuinfo.fileInfo.get("cpu MHz"));
                systemCPUMHz.set(cpuMHz);

                //Get core info
                //Hashtable<String, Hashtable<String, Long>> cores = coresinfo.getCPUOccupationTablesJiffies();

                Thread.sleep(4000);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void processesUpdater() throws Exception{
        while (true){
            try{
                //Get process
                ArrayList<Integer> pids = SystemProcessInfo.getAllRunningPIDs();
                for (int id : pids) {
                    Process process = new Process(id);

                    processInfo.labels(
                            String.valueOf(process.getPID()),
                            process.getName(),
                            String.valueOf(process.getState())
                    ).set(process.getCpuPercent());
                }
                Thread.sleep(4000);
            } catch (Exception e){
                throw new RuntimeException(e);
            }
        }
    }
}
