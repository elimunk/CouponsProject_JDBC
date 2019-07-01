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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elimunk.coupons.beans.PostLoginUserData;
import com.elimunk.coupons.interfaces.ICacheManager;

@Component
@WebFilter("/*")
public class LoginFilter implements Filter{

	@Autowired
	private ICacheManager cacheManager;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		String path = ((HttpServletRequest) request).getRequestURI();
		if (path.startsWith("/users/login")) {
			chain.doFilter(request, response);
			return;
		}
		if (path.startsWith("/customers") && req.getMethod().equalsIgnoreCase("post")) {
			chain.doFilter(request, response);
			return;
		} else {
			
			String token = req.getParameter("token");
			PostLoginUserData userData = (PostLoginUserData) cacheManager.get(token);

			if (userData != null) {
				request.setAttribute("userData", userData);
				chain.doFilter(request, response);
				return;
			}

			HttpServletResponse res = (HttpServletResponse) response;
			res.setStatus(401);
			res.setHeader("ErrorCause", "Couldn't find a login session");
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
	
	
	
}
