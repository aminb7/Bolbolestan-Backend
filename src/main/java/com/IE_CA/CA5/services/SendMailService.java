package com.IE_CA.CA5.services;

import com.IE_CA.CA5.model.BolbolestanApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class SendMailService {

	@RequestMapping(value = "/send_mail", method = RequestMethod.GET)
	public void loginStudent(@RequestAttribute String email, @RequestParam String password) {
		BolbolestanApplication app = BolbolestanApplication.getInstance();

		if (BolbolestanApplication.getInstance().studentExists(email))
			app.sendMail(email, BolbolestanApplication.getInstance().createForgetURL(email));
	}
}
