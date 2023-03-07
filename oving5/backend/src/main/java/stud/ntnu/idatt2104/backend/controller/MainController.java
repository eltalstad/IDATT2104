package stud.ntnu.idatt2104.backend.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.springframework.web.bind.annotation.*;
import stud.ntnu.idatt2104.backend.model.Code;


import java.io.IOException;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class MainController {

    @PostMapping("/compile")
    public String compileCode(@RequestBody Code code) throws IOException, InterruptedException {
        return compile(code);
    }

    public String compile(Code code) throws IOException, InterruptedException {
        if(code.getCode() == null){
            return "No code to compile";
        }
        String output = "";
        String[] command = {
                "docker", "run", "--rm", "python:latest", "python", "-c", code.getCode()
        };

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            Process p = pb.start();

            p.getOutputStream().write(code.getCode().getBytes());
            p.getOutputStream().close();

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while((line = br.readLine()) != null){
                output += line + "\n";
            }

            int exit = p.waitFor();
            if(exit != 0){
                output += "Compilation failed with exit code " + exit;
            }
        return output;
    }
}
