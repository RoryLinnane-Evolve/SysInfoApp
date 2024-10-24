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

    static final Gauge availableMemory = Gauge.build()
            .name("system_memory_available_kilobytes")
            .help("Available system memory in kilobytes.")
            .register();

    static final Gauge buffersMemory = Gauge.build()
            .name("system_memory_buffers_kilobytes")
            .help("Buffered system memory in kilobytes.")
            .register();

    static final Gauge cachedMemory = Gauge.build()
            .name("system_memory_cached_kilobytes")
            .help("Cached system memory in kilobytes.")
            .register();

    static final Gauge systemMemoryUsage = Gauge.build()
            .name("system_memory_usage")
            .help("System memory usage percentage.")
            .register();



    public static void main(String[] args) throws IOException {
        // Expose metrics at localhost:8080/metrics
        HTTPServer server = new HTTPServer(8080);

        while (true) {
            try {
                // Get system memory information
                SystemMemoryInfo mem = new SystemMemoryInfo();

                // Update the Gauges with the current memory info
                totalMemory.set(mem.total);
                freeMemory.set(mem.free);
                availableMemory.set(mem.available);
                buffersMemory.set(mem.buffers);
                cachedMemory.set(mem.cached);
                double memusage = (double)(mem.total-mem.available)/mem.total * 100;
                systemMemoryUsage.set(Math.round(memusage));
                Thread.sleep(1000);  // delay 1 second

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
