package com.vargatamas.jobsearchapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InstructionsController {

    @GetMapping({"/", "/home", "/index", "/main", "/instructions"})
    public String showInstructionsHomepage() {
        return "index.html";
    }

}
