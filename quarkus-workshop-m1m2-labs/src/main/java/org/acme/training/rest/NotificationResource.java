package org.acme.training.rest;

import java.util.Random;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.jboss.logging.Logger;

@Path("/notification")
public class NotificationResource {
    private static final String RESPONSE_STRING_FORMAT = "notification v1 from '%s': %d\n";

    private static final String RESPONSE_STRING_FALLBACK_FORMAT = "notification fallback from '%s': %d\n";

    private final Logger logger = Logger.getLogger(getClass());

    // Counter to help us see the lifecycle
    private int count = 0;

    @ConfigProperty(name = "notification.misbehave")
    Boolean misbehave;

    @ConfigProperty(name = "notification.timouot")
    Boolean timeout;

    @ConfigProperty(name = "notification.crash")
    Boolean crash;

    private static final String HOSTNAME = System.getenv().getOrDefault("HOSTNAME", "unknown");

    @GET
    @Retry(maxRetries = 10)
    @CircuitBreaker(requestVolumeThreshold = 2, failureRatio = 1, delay = 10000)
    @Fallback(fallbackMethod = "getFallbackNotifications")
    @Timeout(500)
    public Response getNotifications() {
        count++;
        logger.info(String.format("notification request from %s: %d", HOSTNAME, count));

        if (new Random().nextBoolean()) {

            if (timeout) {
                timeout();
            }

            if (crash) {
                crash();
            }
        }

        logger.debug("notification service ready to return");
        if (misbehave) {
            return doMisbehavior();
        }
        return Response.ok(String.format(RESPONSE_STRING_FORMAT, HOSTNAME, count)).build();
    }

    public Response getFallbackNotifications() {
        count++;
        logger.info(String.format("notification request failback from: %s: %d", HOSTNAME, count));
        return Response.ok(String.format(RESPONSE_STRING_FALLBACK_FORMAT, HOSTNAME, count)).build();
    }

    private void timeout() {
        try {
            int delay = 1000;
            logger.info("This request will be delayed " + delay + "ms");
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            logger.info("Thread interrupted");
        }
    }

    private Response doMisbehavior() {
        logger.info(String.format("Misbehaving %d", count));
        return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity(String.format("notification misbehavior from '%s'\n", HOSTNAME)).build();
    }

    private void crash() {
        logger.error("Crashing!");
        throw new RuntimeException("Resource failure.");
    }

    @GET
    @Path("/reset")
    public String clearAllFlags() {
        logger.info("Clearing all flags");
        this.count = 0;
        this.misbehave = false;
        this.timeout = false;
        this.crash = false;
        return "Service reset.\n";
    }

    @GET
    @Path("/misbehave")
    public Response flagMisbehave() {
        this.misbehave = !this.misbehave;
        logger.debug("'misbehave' has been set to " + misbehave);
        return Response.ok("'misbehave' has been set to " + misbehave + "\n").build();
    }

    @GET
    @Path("/timeout")
    public Response flagTimeout() {
        this.timeout = !this.timeout;
        logger.debug("'timeout' has been set to " + timeout);
        return Response.ok("'timeout' has been set to " + timeout + "\n").build();
    }

    @GET
    @Path("/crash")
    public Response flagCrash() {
        this.crash = !this.crash;
        logger.debug("'crash' has been set to " + crash);
        return Response.ok("'crash' has been set to " + crash + "\n").build();
    }

}
