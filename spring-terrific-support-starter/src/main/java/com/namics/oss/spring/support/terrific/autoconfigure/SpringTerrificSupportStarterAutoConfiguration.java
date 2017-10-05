package com.namics.oss.spring.support.terrific.autoconfigure;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

/**
 * SpringTerrificSupportStarterAutoConfiguration.
 *
 * @author bhelfenberger, Namics AG
 * @since 02.10.17 11:12
 */
@Configuration
@Import({ SpringTerrificSupportWebAutoConfiguration.class})
public class SpringTerrificSupportStarterAutoConfiguration implements Ordered {

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}
}
