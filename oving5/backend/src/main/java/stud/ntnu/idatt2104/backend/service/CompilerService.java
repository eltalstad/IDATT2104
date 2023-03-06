package stud.ntnu.idatt2104.backend.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Scanner;

@Service
public class CompilerService {
    public String compile(String code) throws IOException {
        Process process = Runtime.getRuntime().exec(new String[]{"docker", "run", "--rm", "python:latest", "python", "-c", code});
        Scanner scanner = new Scanner(process.getInputStream());
        StringBuilder output = new StringBuilder();
        while (scanner.hasNextLine()) {
            output.append(scanner.nextLine()).append("\n");
        }
        return output.toString();
    }
}