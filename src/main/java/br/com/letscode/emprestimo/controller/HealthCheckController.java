package br.com.letscode.emprestimo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/health-check")
public class HealthCheckController {

    @GetMapping()
    @ResponseBody
    public String healthCheck(){
        return "Server Running!";
    }

}