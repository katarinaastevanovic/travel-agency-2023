package com.ftn.PrviMavenVebProjekat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
@Controller
public class LanguageController {

    @Autowired
    private LocaleResolver localeResolver;

    @GetMapping("/changeLanguage")
    public String changeLanguage(HttpServletRequest request, HttpServletResponse response, @RequestParam("lang") String lang) {
        Locale locale = new Locale(lang);
        localeResolver.setLocale(request, response, locale);
        return "redirect:/";
    }
}