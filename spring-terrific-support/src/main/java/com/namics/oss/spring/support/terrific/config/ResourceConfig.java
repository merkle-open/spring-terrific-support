package com.namics.oss.spring.support.terrific.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.util.Assert.notNull;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * Class for binding json config only!.
 *
 * @author aschaefer, Namics AG
 * @since 2.0 05.04.2013
 */
public class ResourceConfig {

	private static final Logger LOG = LoggerFactory.getLogger(ResourceConfig.class);

	protected String base;
	protected String strippedBase;
	protected ArrayList<String> scan;
	protected Map<String, List<String>> files;

	/**
	 * @param jsonConfigFile must not be null and represent an existing object.
	 */
	public ResourceConfig(Resource jsonConfigFile) {
		notNull(jsonConfigFile);
		try {
			// Unwrapping the Json.
			ObjectMapper mapper = new ObjectMapper();
			Config config = mapper.readValue(jsonConfigFile.getInputStream(), Config.class);

			this.base = config.getBase();
			if (this.base == null) {
				throw new IllegalArgumentException("property \"base\" must be set in json config file");
			}
			if (this.base.endsWith("/")) {
				this.base = this.base.substring(0, this.base.length() - 1);
			}

			// e.g. classpath:/terrific -> /terrific
			this.strippedBase = base;
			if (base.contains(":") && !base.endsWith(":")) {
				this.strippedBase = base.substring(base.indexOf(":") + 1);
			}

			this.scan = new ArrayList<>();
			if (isEmpty(config.getScan())) {
				this.scan.add(this.base);
			} else {
				for (String currentScan : config.getScan()) {
					String scanPath = this.base + (currentScan.startsWith("/") ? currentScan : "/" + currentScan);
					this.scan.add(scanPath);
				}
			}
			this.files = config.getAssets();

		} catch (JsonProcessingException e) {
			String format = String.format("Failed to load terrific json config file %s because of %s message %s. @see location %s ",
			                              jsonConfigFile, e.getClass(), e.getMessage(), e.getLocation());
			throw new IllegalArgumentException(format, e);
		} catch (IOException e) {
			String format = String.format("Failed to load terrific json config file %s because of %s message %s.",
			                              jsonConfigFile, e.getClass(), e.getMessage());
			throw new IllegalArgumentException(format, e);
		}
	}

	public String getBase() {
		return base;
	}

	public String getStrippedBase() {
		return strippedBase;
	}

	public ArrayList<String> getScan() {
		return scan;
	}

	public Map<String, List<String>> getFiles() {
		return files;
	}

	/**
	 * Class for JSON binding.
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public static class Config {
		private String base;
		private List<String> scan;
		private Map<String, List<String>> assets;

		public String getBase() {
			return this.base;
		}

		public void setBase(String base) {
			this.base = base;
		}

		public List<String> getScan() {
			return this.scan;
		}

		public void setScan(List<String> scan) {
			this.scan = scan;
		}

		public Map<String, List<String>> getAssets() {
			return this.assets;
		}

		public void setAssets(Map<String, List<String>> assets) {
			this.assets = assets;
		}
	}
}