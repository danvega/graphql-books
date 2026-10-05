package dev.danvega.books;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * Connects the Logback OTEL appender to Spring's OpenTelemetry bean so logs reach Grafana.
 * <p>
 * Logback starts first and creates the appender from logback-spring.xml. Spring creates the
 * OpenTelemetry bean later, and Logback can't inject Spring beans. Until install() runs, the
 * appender has nowhere to send logs. Logs written before that are held and sent once it's installed.
 * <p>
 * Spring Boot doesn't do this step for you. Traces and metrics come from the starter and don't need it.
 */
@Component
class InstallOpenTelemetryAppender implements InitializingBean {

    private final OpenTelemetry openTelemetry;

    InstallOpenTelemetryAppender(OpenTelemetry openTelemetry) {
        this.openTelemetry = openTelemetry;
    }

    @Override
    public void afterPropertiesSet() {
        OpenTelemetryAppender.install(this.openTelemetry);
    }

}
