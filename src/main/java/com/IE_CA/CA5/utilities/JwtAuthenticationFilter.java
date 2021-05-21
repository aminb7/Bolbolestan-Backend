package com.IE_CA.CA5.utilities;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(servletNames = {"GetLoggedInStudentService, MainService"})
public class JwtAuthenticationFilter implements Filter {

	@Override
	public void init(FilterConfig fc) throws ServletException {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
	                     FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String header = httpRequest.getHeader("Authorization");
		System.out.println("Hiiiiiiiiiiiiiii");

		if (header == null || !header.startsWith("Bearer ")) {
			return;
		}

		String authToken = header.substring(7);

//		JwtAuthenticationToken authRequest = new JwtAuthenticationToken(authToken);
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {}
}
