/*
 * Copyright 2000-2013 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;

public class FileOutputResponseWrapper extends HttpServletResponseWrapper {
	private static final Logger LOG = LoggerFactory.getLogger(FileOutputResponseWrapper.class);

	private final OutputStream content;

	private final ServletOutputStream outputStream = new ResponseServletOutputStream();

	private PrintWriter writer;

	private int statusCode = HttpServletResponse.SC_OK;

	public FileOutputResponseWrapper(HttpServletResponse response, OutputStream stream) throws Exception {
		super(response);
		content = stream;
	}

	@Override
	public void setStatus(int sc) {
		super.setStatus(sc);
		this.statusCode = sc;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setStatus(int sc,
	                      String sm) {
		super.setStatus(sc, sm);
		this.statusCode = sc;
	}

	@Override
	public void sendError(int sc) throws IOException {
		super.sendError(sc);
		this.statusCode = sc;
	}

	@Override
	public void sendError(int sc,
	                      String msg) throws IOException {
		super.sendError(sc, msg);
		this.statusCode = sc;
	}

	@Override
	public void setContentLength(int len) {
	}

	@Override
	public ServletOutputStream getOutputStream() {
		return this.outputStream;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		if (this.writer == null) {
			String characterEncoding = getCharacterEncoding();
			this.writer = (characterEncoding != null ? new ResponsePrintWriter(characterEncoding) : new ResponsePrintWriter(
					WebUtils.DEFAULT_CHARACTER_ENCODING));
		}
		return this.writer;
	}

	@Override
	public void resetBuffer() {

	}

	@Override
	public void reset() {
		super.reset();
		resetBuffer();
	}

	public int getStatusCode() {
		return statusCode;
	}

	private class ResponseServletOutputStream extends ServletOutputStream {

		@Override
		public void write(int b) throws IOException {
			content.write(b);
		}

		@Override
		public void write(byte[] b,
		                  int off,
		                  int len) throws IOException {
			content.write(b, off, len);
		}

		@Override
		public boolean isReady() {
			//TODO: Implement Method
			LOG.error("Method isReady() is not implemented yet!");
			return false;
		}

		@Override
		public void setWriteListener(WriteListener writeListener) {
			//TODO: Implement Method
			LOG.error("Method setWriteListener() is not implemented yet!");
		}
	}

	private class ResponsePrintWriter extends PrintWriter {

		private ResponsePrintWriter(String characterEncoding) throws UnsupportedEncodingException {
			super(new OutputStreamWriter(content, characterEncoding));
		}

		@Override
		public void write(char buf[],
		                  int off,
		                  int len) {
			super.write(buf, off, len);
			super.flush();
		}

		@Override
		public void write(String s,
		                  int off,
		                  int len) {
			super.write(s, off, len);
			super.flush();
		}

		@Override
		public void write(int c) {
			super.write(c);
			super.flush();
		}
	}
}
