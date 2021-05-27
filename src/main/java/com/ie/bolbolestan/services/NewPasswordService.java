package com.ie.bolbolestan.services;

import com.ie.bolbolestan.model.BolbolestanApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class NewPasswordService {

	@RequestMapping(value = "/forget/new_pass", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public boolean loginStudent(@RequestAttribute String id, @RequestParam String password) {
		BolbolestanApplication app = BolbolestanApplication.getInstance();
		return app.changePassword(id, password);
	}
}
