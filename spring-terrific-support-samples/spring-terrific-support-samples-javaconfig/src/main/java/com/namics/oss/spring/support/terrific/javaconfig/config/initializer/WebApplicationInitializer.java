package com.namics.oss.spring.support.terrific.javaconfig.config.initializer;

import com.namics.oss.spring.support.terrific.javaconfig.config.ApplicationConfig;
import com.namics.oss.spring.support.terrific.javaconfig.config.servlet.MvcServletConfig;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;

/**
 * WebApplicationInitializer.
 *
 * @author bhelfenberger, Namics AG
 * @since 03.10.17 17:56
 */
public class WebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer implements org.springframework.web.WebApplicationInitializer {

	@Override
	protected String getServletName() {
		return "mvc";
	}


	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { ApplicationConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { MvcServletConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

	@Override
	protected Filter[] getServletFilters() {
		return new Filter[] { new HiddenHttpMethodFilter(), new CharacterEncodingFilter(), new DelegatingFilterProxy("terrificFilter") };
	}

}
