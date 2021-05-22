package com.IE_CA.CA5.services;

import com.IE_CA.CA5.model.BolbolestanApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchService {

    @RequestMapping(value = "/search", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void setSearchFilter(@RequestParam(value = "filter") String filter) {
        BolbolestanApplication app = BolbolestanApplication.getInstance();
        app.setSearchFilter(filter);
    }
}
