package com.javawiz.controller;

import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javawiz.service.MyService;

@RestController
public class MyRestController {
	
	@Autowired
	MyService myService;
	  // Exercise using curl http://localhost:8080/async?input=oxmore
	  @RequestMapping(path = "async", method = RequestMethod.GET)
	  public Future<String> get(@RequestParam String input) throws Exception {
	    return myService.concatenateAsync(input);
	  }
}
