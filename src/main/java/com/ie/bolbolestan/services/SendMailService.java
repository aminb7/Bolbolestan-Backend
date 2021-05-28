package com.ie.bolbolestan.services;

import com.ie.bolbolestan.model.BolbolestanApplication;
import org.springframework.web.bind.annotation.*;

@RestController
public class SendMailService {

	@RequestMapping(value = "/send_mail", method = RequestMethod.GET)
	public void loginStudent(@RequestParam String email) {
		BolbolestanApplication app = BolbolestanApplication.getInstance();

		if (BolbolestanApplication.getInstance().studentExists(email))
			app.sendMail(email, "http://localhost:80?new_password=" + BolbolestanApplication.getInstance().createForgetURL(email));
	}
}
