package stud.ntnu.idatt2104.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import stud.ntnu.idatt2104.backend.service.CompilerService;

import java.io.IOException;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
public class MainController {

    @Autowired
    private CompilerService compilerService;

    @PostMapping("/compile")
    public String compile(@RequestBody String code) throws IOException {
        return compilerService.compile(code);
    }

}
