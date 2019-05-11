package io.mrth.webutil.rest;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

public class MultireadServletFilter implements Filter {

	public static final Set<String> MULTI_READ_HTTP_METHODS = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER) {
		{
			add("PUT");
			add("POST");
		}
	};

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		ServletRequest requestChain = request;
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		if (MULTI_READ_HTTP_METHODS.contains(httpServletRequest.getMethod())) {
			requestChain = new MultiReadRequestWrapper(httpServletRequest);
		}
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		chain.doFilter(requestChain, httpServletResponse);
	}

	@Override
	public void destroy() {

	}

}
