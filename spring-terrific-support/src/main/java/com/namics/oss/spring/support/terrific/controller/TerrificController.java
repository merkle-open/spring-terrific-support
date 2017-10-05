package com.namics.oss.spring.support.terrific.controller;

import com.namics.oss.spring.support.terrific.service.ContentService;
import com.namics.oss.spring.support.terrific.util.CacheControlUtil;
import com.namics.oss.spring.support.terrific.util.MimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * TerrificController.
 *
 * @author aschaefer, Namics AG
 * @since 2.0 05.04.2013
 */
public class TerrificController {
	private static final Logger LOG = LoggerFactory.getLogger(TerrificController.class);
	protected ContentService contentService;
	protected String mapping;
	protected String debugParamName = "debug";
	protected boolean debug = false;
	protected boolean checkLastModified = true;
	protected long startDate = new Date().getTime();
	protected CacheControlUtil cacheControlUtil = new CacheControlUtil();
	protected String terrificVersion;

	/**
	 * Serves requested files.
	 *
	 * @param request request of file
	 * @return response entity with file data and response metadata
	 */
	@RequestMapping("/terrific/**")
	public ResponseEntity<byte[]> getFile(HttpServletRequest request, HttpServletResponse servletResponse) throws ServletException {
		String file = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		file = file.replace(this.mapping, ""); // remove controller mapping path

		String mimeType = MimeUtils.detectMimeType(file);
		boolean debug = this.debug || ServletRequestUtils.getStringParameter(request, debugParamName) != null;

		LOG.info("Requested file: {} required mime type: {}", file, mimeType);
		try {
			if (!debug && checkLastModified(request, servletResponse, file)) {
				return null;
			}

			byte[] result = this.contentService.getContent(file, debug);
			ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(result, HttpStatus.OK);
			if (terrificVersion != null && debug) {
				response.getHeaders().set("X-Terrific-Version", terrificVersion);
			}
			return response;
		} catch (Exception e) {
			final Object[] args = new Object[] { file, e };
			LOG.warn("Exception while resolving file {} : {}", args);
			LOG.debug("Exception while resolving file {} : {}", args, e);
		}

		return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
	}

	protected boolean checkLastModified(HttpServletRequest request, HttpServletResponse response, String file) {
		long modified = startDate;
		if (this.checkLastModified) {
			Long lastModified = this.contentService.lastModified(file);
			modified = startDate > lastModified ? startDate : lastModified;
		}
		ServletWebRequest webRequest = new ServletWebRequest(request, response);
		if (webRequest.checkNotModified(modified)) {
			return true;
		}
		this.cacheControlUtil.cacheControl(response, modified);
		return false;
	}

	/**
	 * Setter for contentService. @param contentService the contentService to set
	 */
	public void setContentService(ContentService contentService) {
		this.contentService = contentService;
	}

	/**
	 * mapping, default configured by property ${terrific.mapping:/terrific/}.
	 *
	 * @param mapping the mapping to set
	 */
	public void setMapping(String mapping) {
		this.mapping = mapping;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setDebugParamName(String debugParamName) {
		this.debugParamName = debugParamName;
	}

	public void setCheckLastModified(boolean checkLastModified) {
		this.checkLastModified = checkLastModified;
	}

	public void setCacheControlAge(int maxAgeDays) {
		this.cacheControlUtil.setMaxAgeDays(maxAgeDays);
	}

	public void setTerrificVersion(String terrificVersion) {
		this.terrificVersion = terrificVersion;
	}
}