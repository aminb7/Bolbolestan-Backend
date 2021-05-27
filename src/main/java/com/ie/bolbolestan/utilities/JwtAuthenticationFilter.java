package com.ie.bolbolestan.utilities;

import static com.ie.bolbolestan.model.BolbolestanApplication.decodeJWT;

import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@WebFilter("/*")
public class JwtAuthenticationFilter implements Filter {

	@Override
	public void init(FilterConfig fc) throws ServletException {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String uri = httpRequest.getRequestURI();

		if(!(uri.contains("loggedin_student") || uri.contains("add_course") || uri.contains("remove_course")
				|| uri.contains("finalize_courses") || uri.contains("reset") || uri.contains("new_pass"))) {
			chain.doFilter(request, response);
			return;
		}

		String header = httpRequest.getHeader("Authorization");

		if (header == null || !header.startsWith("Bearer ")) {
			httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
			return;
		}

		String token = header.substring(7);
		Claims claims = null;

		try {
			claims = decodeJWT(token);
		} catch (Exception e) {
			httpResponse.setStatus(HttpStatus.FORBIDDEN.value());
			return;
		}

		request.setAttribute("id", claims.getId());
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {}
}
