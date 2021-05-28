package com.ie.bolbolestan.utilities;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@WebFilter(filterName = "CORSFilter")
public class CORSFilter implements Filter {
	public void init(FilterConfig config) throws ServletException {
	}

	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;

		// Authorize (allow) all domains to consume the content
		((HttpServletResponse) response).addHeader("Access-Control-Allow-Origin", "*");
		((HttpServletResponse) response).addHeader("Access-Control-Allow-Methods","GET, OPTIONS, HEAD, PUT, POST, DELETE");
		((HttpServletResponse) response).addHeader("Access-Control-Allow-Headers","content-type");
		((HttpServletResponse) response).addHeader("Access-Control-Allow-Headers","Authorization");

		HttpServletResponse resp = (HttpServletResponse) response;

		// For HTTP OPTIONS verb/method reply with ACCEPTED status code -- per CORS handshake
		if (req.getMethod().equals("OPTIONS")) {
			resp.setStatus(HttpServletResponse.SC_ACCEPTED);
			return;
		}

		// pass the request along the filter chain
		chain.doFilter(request, response);
	}
}
