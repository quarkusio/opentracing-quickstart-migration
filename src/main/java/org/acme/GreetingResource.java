package org.acme;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.context.Scope;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.opentelemetry.semconv.trace.attributes.SemanticAttributes;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import static io.opentelemetry.api.trace.StatusCode.*;

@Path("/hello")
@ApplicationScoped
public class GreetingResource {

    @Inject
    io.opentelemetry.api.trace.Tracer otelTracer;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @WithSpan(value = "Not needed, will create a new span, child of the automatic JAX-RS span")
    public String hello() {
        // Add a tag to the active span
        Span incomingSpan = Span.current();
        incomingSpan.setAttribute(SemanticAttributes.CODE_NAMESPACE, "GreetingResource");

        // Create a manual inner span
        Span innerSpan = otelTracer.spanBuilder("Count response chars").startSpan();
        try (Scope scope = innerSpan.makeCurrent()) {
            final String response = "Hello from RESTEasy Reactive";
            innerSpan.setAttribute("response-chars-count", response.length());
            return response;
        } catch (Exception e) {
            innerSpan.setStatus(ERROR);
            innerSpan.recordException(e);
            throw e;
        } finally {
            innerSpan.end();
        }
    }
}
