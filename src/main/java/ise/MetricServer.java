package ise;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.HTTPServer;
import ise.SystemMemoryInfo;

import java.io.IOException;

public class MetricServer {

    // Create a counter metric for requests
    static final Counter requests = Counter.build()
            .name("requests_total").help("Total requests to this service.").register();

    // Create gauge metrics for system memory properties
    static final Gauge totalMemory = Gauge.build()
            .name("system_memory_total_bytes")
            .help("Total system memory in bytes.")
            .register();

    static final Gauge freeMemory = Gauge.build()
            .name("system_memory_free_bytes")
            .help("Free system memory in bytes.")
            .register();

    static final Gauge availableMemory = Gauge.build()
            .name("system_memory_available_bytes")
            .help("Available system memory in bytes.")
            .register();

    static final Gauge buffersMemory = Gauge.build()
            .name("system_memory_buffers_bytes")
            .help("Buffered system memory in bytes.")
            .register();

    static final Gauge cachedMemory = Gauge.build()
            .name("system_memory_cached_bytes")
            .help("Cached system memory in bytes.")
            .register();

    public static void main(String[] args) throws IOException {
        // Expose metrics at localhost:8080/metrics
        HTTPServer server = new HTTPServer(8080);

        // Simulate some work: increment the counter and update memory gauges
        while (true) {
            requests.inc();  // Increment the request counter

            try {
                // Get system memory information
                SystemMemoryInfo mem = new SystemMemoryInfo();

                // Update the Gauges with the current memory info
                totalMemory.set(mem.total);
                freeMemory.set(mem.free);
                availableMemory.set(mem.available);
                buffersMemory.set(mem.buffers);
                cachedMemory.set(mem.cached);

                Thread.sleep(1000);  // Simulate a delay (e.g., each second)

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
