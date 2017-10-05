/*
 * Copyright 2000-2014 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * TerrificConfig.
 *
 * @author aschaefer
 * @since 04.02.14 11:19
 */
@Configuration
@Import(value = VersionConfig.class)
public class TerrificVersionConfig extends TerrificConfig {

	@Inject
	@Named("assetVersion")
	protected String assetVersion;

	@Override
	protected String mapping() {
		return super.mapping() + assetVersion + "/";
	}

}
