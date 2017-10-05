package com.namics.oss.spring.support.terrific.autoconfigure;

import com.namics.oss.spring.support.terrific.config.TerrificVersionConfig;
import com.namics.oss.spring.support.terrific.filter.TerrificFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * SpringTerrificSupportWebAutoConfiguration.
 *
 * @author bhelfenberger, Namics AG
 * @since 02.10.17 11:13
 */
@Import({ TerrificVersionConfig.class })
public class SpringTerrificSupportWebAutoConfiguration {
	private static final Logger LOG = LoggerFactory.getLogger(SpringTerrificSupportWebAutoConfiguration.class);

	@Inject
	@Named("terrificMappingString")
	String terrificMappingString;

	@Inject
	TerrificFilter terrificFilter;

	@Bean
	public FilterRegistrationBean terrificFilterRegistrationBean() {
		String path = terrificMappingString + "*";
		LOG.info("Register terrific filter {}", path);
		FilterRegistrationBean registration = new FilterRegistrationBean(terrificFilter);
		registration.addUrlPatterns(path);
		return registration;
	}
}
