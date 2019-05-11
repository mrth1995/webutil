package io.mrth.webutil.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class RestErrorFilter implements Filter {

	private static final Logger LOG = LoggerFactory.getLogger(RestErrorFilter.class);
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			chain.doFilter(request, response);
		} catch (IOException | ServletException | RuntimeException e) {
			LOG.error("Map error in filter: {}", e.getMessage(), e);
			HttpServletResponse httpServletResponse = (HttpServletResponse) response;
			httpServletResponse.setStatus(400);
			httpServletResponse.setContentType("text/plain");
			httpServletResponse.setHeader("RC", "99");
			try (PrintWriter writer = new PrintWriter(httpServletResponse.getOutputStream())) {
				writer.print(e.getMessage());
			}
		}
	}

	@Override
	public void destroy() {
	
	}
	
}
