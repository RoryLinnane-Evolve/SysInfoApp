/**
 * This is the file for the server that recieves get requests
 * from prometheus and responds with the data in the gauges set out below.
 * The data is set every 4 seconds. There is 2 threads running simultaneously
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
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class MetricServer {

    // Create gauge metrics for system memory properties
    final Gauge totalMemory = Gauge.build()
            .name("system_memory_total_kilobytes")
            .help("Total system memory in kilobytes.")
            .register();

    final Gauge freeMemory = Gauge.build()
            .name("system_memory_free_kilobytes")
            .help("Free system memory in kilobytes.")
            .register();

    final Gauge systemMemoryUsage = Gauge.build()
            .name("system_memory_usage")
            .help("System memory usage percentage.")
            .register();

    final Gauge systemCPUMHz = Gauge.build()
            .name("system_CPU_MHz")
            .help("CPU clock speed in megahertz.")
            .register();

    final Gauge processInfo = Gauge.build()
            .name("system_process_info")
            .help("Information about system processes")
            .labelNames("pid", "name", "state")
            .register();

    private String IPAddress ;
    private int port;

    public MetricServer(String _IPAddress, int _port){
        IPAddress = _IPAddress;
        port = _port;
    }


    public void start() throws IOException{
        // Starts a http server with the specified port and IP
        // metrics are exposed at <localIp>:port/metrics
        InetSocketAddress address = new InetSocketAddress(IPAddress, port);
        HTTPServer server = new HTTPServer.Builder().withInetSocketAddress(address).build();

        //creating the thread for the memory and cpu metrics to be updated
        Thread memAndCPU = new Thread(() -> {
            try {
                systemMemmoryAndCPUUpdater();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        //creating the thread for the process metrics to be updated
        Thread procs = new Thread(() -> {
            try {
                processesUpdater();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        //starting the threads
        procs.start();
        memAndCPU.start();
    }

    private void systemMemmoryAndCPUUpdater() throws Exception{

        //creating the object for cpu information
        VirtualFileInfo cpuinfo = new VirtualFileInfo("/proc/cpuinfo");

        while(true){
            try{
                // Get system memory information
                SystemMemoryInfo mem = new SystemMemoryInfo();

                // Update the memory Gauges with the current memory info
                totalMemory.set(mem.total);
                freeMemory.set(mem.free);

                // get the memory usage percentage by dividing the (total memory - free memory) by the total memory and multiplying by 100
                double memusage = (double)(mem.total-mem.free)/(mem.total) * 100;
                systemMemoryUsage.set(Math.round(memusage));

                //Update cpu mhz in the hashtable
                cpuinfo.setHashtable();

                //TODO: Calculate CPU Usage

                //TODO: Collect PCI, USB and disk information


                //retrieve it from Hashtable and update gauge
                double cpuMHz = Double.parseDouble(cpuinfo.fileInfo.get("cpu MHz"));
                systemCPUMHz.set(cpuMHz);

                //Delay for 4 seconds
                Thread.sleep(4000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void processesUpdater() throws Exception{

        ProcPIDStat proc = new ProcPIDStat();
        while (true){
            try{
                //Get process
                ArrayList<Integer> pids = SystemProcessInfo.getAllRunningPIDs();

                //Loop through the process ids
                for (int id : pids) {
                    //gets the process infromation for the current id

                    Map<String, Object> procInfo = proc.getProcPIDStatInfo(id);

                    //updates the gauge list for the current process
                    processInfo.labels(
                            String.valueOf(procInfo.get("Pid")),
                            String.valueOf(procInfo.get("Name")),
                            String.valueOf(procInfo.get("State"))
                    ).set(0);
                }

                //Delay 4 seconds
                Thread.sleep(4000);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
