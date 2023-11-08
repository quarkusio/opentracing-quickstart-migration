package org.acme;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.opentelemetry.opentracingshim.OpenTracingShim;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.tag.Tags;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
@ApplicationScoped
public class GreetingResource {

    @Inject
    io.opentelemetry.api.OpenTelemetry openTelemetry;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @WithSpan(value = "Not needed, will create a new span, child of the automatic JAX-RS span")
    public String hello() {
        // Add a tag to the active span
        Tracer legacyTracer = OpenTracingShim.createTracerShim(openTelemetry);
        legacyTracer.activeSpan().setTag(Tags.COMPONENT, "GreetingResource");

        // Create a manual inner span
        Span innerSpan = legacyTracer.buildSpan("Count response chars").start();

        try (Scope dbScope = legacyTracer.scopeManager().activate(innerSpan)) {
            String response = "Hello from RESTEasy Reactive";
            innerSpan.setTag("response-chars-count", response.length());
            return response;
        } catch (Exception e) {
            innerSpan.setTag("error", true);
            innerSpan.setTag("error.message", e.getMessage());
            throw e;
        } finally {
            innerSpan.finish();
        }
    }
}