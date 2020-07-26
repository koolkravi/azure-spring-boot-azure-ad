package com.example.demo2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController
{
    @Autowired
    @PreAuthorize("hasRole('users')")
    @RequestMapping("/")
    public String helloWorld()
    {
        return "Hello World!";
    }
}
