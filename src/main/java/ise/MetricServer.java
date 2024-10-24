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
import io.prometheus.client.exporter.HTTPServer;
import ise.SystemMemoryInfo;
import java.lang.Math;

import java.io.IOException;

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

    static final Gauge systemCPUUsage = Gauge.build()
            .name("system_CPU_usage")
            .help("CPU usage percentage.")
            .register();

    public static void main(String[] args) throws IOException {
        // Starts a http server
        HTTPServer server = new HTTPServer(8080);

        while (true) {
            try {
                // Get system memory information
                SystemMemoryInfo mem = new SystemMemoryInfo();

                // Update the Gauges with the current memory info
                totalMemory.set(mem.total);
                freeMemory.set(mem.free);

                // get the memory usage percentage by dividing the (total memory - available memory) by the total memory and multiplying by 100
                double memusage = (double)(mem.total-mem.available)/mem.total * 100;
                systemMemoryUsage.set(Math.round(memusage));

                //TODO: find cpu usage
                double cpuUsage = 25;
                systemCPUUsage.set(cpuUsage);

                // delay 1 second
                Thread.sleep(4);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
