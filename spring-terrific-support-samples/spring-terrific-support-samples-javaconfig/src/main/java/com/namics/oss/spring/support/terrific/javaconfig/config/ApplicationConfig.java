package com.namics.oss.spring.support.terrific.javaconfig.config;

import com.namics.oss.spring.support.terrific.config.TerrificVersionConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

/**
 * ApplicationConfig.
 *
 * @author bhelfenberger, Namics AG
 * @since 03.10.17 18:00
 */
@Configuration
@PropertySource(value= {"classpath:application.properties"})
@Import(TerrificVersionConfig.class)
public class ApplicationConfig {
}
