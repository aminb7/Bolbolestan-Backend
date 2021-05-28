package com.ie.bolbolestan.services;

import com.ie.bolbolestan.model.BolbolestanApplication;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ForgetService {

	@RequestMapping(value = "/forget/*", method = RequestMethod.GET)
	public String loginStudent(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getRequestURI().substring(8);

		try {
			BolbolestanApplication.decodeJWT(token);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "URI is not correct");
		}

		return "/pages/new_password/index.html";
	}
}
