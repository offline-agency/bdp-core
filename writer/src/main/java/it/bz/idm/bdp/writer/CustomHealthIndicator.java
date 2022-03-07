package it.bz.idm.bdp.writer;

import com.google.common.collect.ImmutableMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.v;


@Component
public class CustomHealthIndicator implements HealthIndicator {

	private static final Logger LOG = LoggerFactory.getLogger(CustomHealthIndicator.class);

    @Override
    public Health health() {
		LOG.info("Health check", v("api_health_info", ImmutableMap.of("status", "HEALTHY")));
        return Health.up().build();
    }
}