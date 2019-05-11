package io.mrth.webutil.rest;

import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class MultiReadRequestWrapper extends HttpServletRequestWrapper {

	private byte[] body;
	private final AtomicBoolean bodyRead = new AtomicBoolean(false);

	public MultiReadRequestWrapper(HttpServletRequest httpServletRequest) {
		super(httpServletRequest);
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		if (!bodyRead.getAndSet(true)) {
			// Baca dan simpan sebagai array of byte
			InputStream is = super.getInputStream();
			body = IOUtils.toByteArray(is);
		}
		return new ServletInputStreamImpl(new ByteArrayInputStream(body));
	}

	@Override
	public BufferedReader getReader() throws IOException {
		String enc = getCharacterEncoding();
		if (enc == null) {
			enc = "UTF-8";
		}
		return new BufferedReader(new InputStreamReader(getInputStream(), enc));
	}

	private class ServletInputStreamImpl extends ServletInputStream {

		private InputStream is;

		public ServletInputStreamImpl(InputStream is) {
			this.is = is;
		}

		@Override
		public int read() throws IOException {
			return is.read();
		}

		@Override
		public boolean markSupported() {
			return false;
		}

		@Override
		public synchronized void mark(int i) {
			throw new RuntimeException(new IOException("mark/reset not supported"));
		}

		@Override
		public synchronized void reset() throws IOException {
			throw new IOException("mark/reset not supported");
		}

		@Override
		public boolean isFinished() {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		@Override
		public boolean isReady() {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		@Override
		public void setReadListener(ReadListener readListener) {
			throw new UnsupportedOperationException("Not supported yet."); 
		}
	}
}
