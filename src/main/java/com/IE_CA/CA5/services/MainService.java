package com.IE_CA.CA5.services;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainService {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getPage() {
        return "index.html";
    }
}
