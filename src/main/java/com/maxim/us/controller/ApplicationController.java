package com.maxim.us.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maxim.us.controller.responses.ConversionResponse;
import com.maxim.us.service.AppService;

@Controller
@RestController
public class ApplicationController {

    @Autowired
    private AppService service;

    @RequestMapping(path = { "/getShort" }, method = RequestMethod.GET)
    public final ConversionResponse getShortUrl(@RequestParam(name = "url", required = true) String longUrl) {

        return new ConversionResponse(service.fetchShortUrl(longUrl));
    }

    @RequestMapping(path = { "/getLong" }, method = RequestMethod.GET)
    public final ConversionResponse getLongUrl(@RequestParam(name = "url", required = true) String shortUrl) {
        return new ConversionResponse(service.fetchLongUrl(shortUrl));
    }
}
