package com.namics.oss.spring.support.terrific.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.Resource;

import javax.inject.Named;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/**
 * VersionConfig.
 *
 * @author bhelfenberger, Namics AG
 * @since 03.10.17 10:55
 */
@Configuration
public class VersionConfig {
	private static final Logger LOG = LoggerFactory.getLogger(VersionConfig.class);

	@Bean(name = "versionProperties")
	public PropertiesFactoryBean versionProperties(@Value("classpath:META-INF/build.properties") Resource source) {
		PropertiesFactoryBean properties = new PropertiesFactoryBean();
		properties.setLocation(source);
		properties.setIgnoreResourceNotFound(true);
		return properties;
	}

	@Bean(name = "assetVersion")
	public String assetVersion(@Named("versionInfo") VersionInfoMap versionInfo) {
		if (versionInfo.containsKey("git.commit.id.abbrev")) {
			return versionInfo.get("git.commit.id.abbrev");
		}
		return "current";
	}

	@Bean(name = "versionInfo")
	public VersionInfoMap versionInfo(ConfigurableEnvironment environment, @Named("versionProperties") Properties properties) {
		VersionInfoMap mapped = new VersionInfoMap();
		for (Map.Entry<Object, Object> entry : properties.entrySet()) {
			if (entry.getKey() instanceof String) {
				if (entry.getValue() instanceof String && !((String) entry.getValue()).startsWith("${") && !((String) entry.getValue()).startsWith("@")) {
					mapped.put((String) entry.getKey(), (String) entry.getValue());
				}
			}
		}
		mapped.put("spring.active.profiles", Arrays.toString(environment.getActiveProfiles()));
		mapped.put("serverStartupTime", LocalDateTime.now().toString());
		LOG.info("===== Application Information");
		for (Map.Entry<String, String> entry : mapped.entrySet()) {
			LOG.info(String.format("%-30s | %s", entry.getKey(), entry.getValue()));
		}

		environment.getPropertySources().addFirst(new PropertiesPropertySource("versionInfo", properties));
		return mapped;
	}

	public static class VersionInfoMap extends TreeMap<String, String> {

	}
}
