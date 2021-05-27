package com.ie.bolbolestan.services;

import com.ie.bolbolestan.model.BolbolestanApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class LoginService {

    @RequestMapping(value = "/login", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String loginStudent(@RequestParam String email, @RequestParam String password) {
        BolbolestanApplication app = BolbolestanApplication.getInstance();
        if (app.studentExists(email, password))
            return "{\"key\": \"" + app.createJWT(email) + "\"}";
        else
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized");
    }
}
