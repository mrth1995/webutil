package io.mrth.webutil.rest;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class CommonResponseWrapper extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream byteArrayOutputStream;

    public CommonResponseWrapper(HttpServletResponse response) {
        super(response);
        byteArrayOutputStream = new ByteArrayOutputStream();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new ByteArrayOutputStreamWrapper(byteArrayOutputStream);
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(byteArrayOutputStream);
    }

    @Override
    public void sendError(int sc) throws IOException {
        setStatus(sc);
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
        setStatus(sc);
    }

    public void sendErrorX(int sc) throws IOException {
        setStatus(sc);
        super.sendError(sc);
    }

    @Override
    public void flushBuffer() throws IOException {
        // Tidak melakukan apapun, sebagai gantinya dapat menggunakan flushBufferX
    }

    public void flushBufferX() throws IOException {
        super.flushBuffer();
    }

    public ByteArrayOutputStream getByteArrayOutputStream() {
        return byteArrayOutputStream;
    }
	
    public static class ByteArrayOutputStreamWrapper extends ServletOutputStream {

        private final ByteArrayOutputStream output;

        public ByteArrayOutputStreamWrapper(ByteArrayOutputStream output) {
            this.output = output;
        }

        @Override
        public void write(int b) throws IOException {
            output.write(b);
        }

		@Override
		public boolean isReady() {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		@Override
		public void setWriteListener(WriteListener writeListener) {
			throw new UnsupportedOperationException("Not supported yet.");
		}
    }
}
