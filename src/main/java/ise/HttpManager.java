package ise;

import io.prometheus.client.Counter;
import io.prometheus.client.Histogram;
import io.prometheus.client.exporter.HTTPServer;
import java.io.IOException;


public class HttpManager {
    // Create a counter metric
    static final Counter requests = Counter.build()
            .name("requests_total").help("Total requests.").register();

    // Create a histogram metric for request duration
    static final Histogram requestLatency = Histogram.build()
            .name("requests_latency_seconds").help("Request latency in seconds.").register();

    public static void main(String[] args) throws IOException {
        // Start an HTTP server to expose metrics on a specific port
        HTTPServer server = new HTTPServer(1234);  // Exposes metrics at http://localhost:1234/metrics

        // Simulating some work
        while (true) {
            // Increment the request counter
            requests.inc();

            // Start the timer for tracking request duration
            Histogram.Timer requestTimer = requestLatency.startTimer();
            try {
                // Simulate work (e.g., processing a request)
                Thread.sleep((long) (Math.random() * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // Stop the timer and record the request duration
                requestTimer.observeDuration();
            }
        }
    }
}
