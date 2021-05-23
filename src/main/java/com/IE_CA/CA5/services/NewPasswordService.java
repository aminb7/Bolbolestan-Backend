package com.IE_CA.CA5.services;

import com.IE_CA.CA5.model.BolbolestanApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class NewPasswordService {

	@RequestMapping(value = "/new_pass", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public boolean loginStudent(@RequestAttribute String email, @RequestParam String password) {
		BolbolestanApplication app = BolbolestanApplication.getInstance();
		return app.changePassword(email, password);
	}
}
