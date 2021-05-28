package com.ie.bolbolestan.services;

import com.ie.bolbolestan.model.BolbolestanApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TypeSearchService {

	@RequestMapping(value = "/typesearch", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void setSearchFilter(@RequestParam(value = "filter") String filter) {
		BolbolestanApplication app = BolbolestanApplication.getInstance();
		app.setTypeSearchFilter(filter);
	}
}
