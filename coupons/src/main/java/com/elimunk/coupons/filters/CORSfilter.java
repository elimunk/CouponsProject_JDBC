package com.elimunk.coupons.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
@WebFilter("/*")
public class CORSfilter implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

		// Authorize (allow) all domains to consume the content
		((HttpServletResponse) response).addHeader("Access-Control-Allow-Credentials", "true");
		((HttpServletResponse) response).addHeader("Access-Control-Allow-Origin", "http://localhost:4200");
		((HttpServletResponse) response).addHeader("Access-Control-Allow-Methods", "GET, DELETE, OPTIONS, HEAD, PUT, POST");
		((HttpServletResponse) response).addHeader("Access-Control-Allow-Headers",
				"Origin, Accept, x-auth-token, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");

		((HttpServletResponse) response).setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
		((HttpServletResponse) response).setHeader("Access-Control-Allow-Credentials", "true");
		((HttpServletResponse) response).setHeader("Access-Control-Allow-Methods", "POST, DELETE, PUT, GET, HEAD, OPTIONS");
		((HttpServletResponse) response).setHeader("Access-Control-Allow-Headers",
				"Origin, Accept, x-auth-token, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");

		if (httpServletRequest.getMethod().equals("OPTIONS")) {

			httpServletResponse.setStatus(HttpServletResponse.SC_ACCEPTED);
			return;
		}
		chain.doFilter(request, response);
	}

	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
}
