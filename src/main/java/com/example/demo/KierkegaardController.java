package com.example.demo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class KierkegaardController {

  @RequestMapping("/")
  public String helloWorld(){
    return "<h1>Kierkegaard Quote Finder</h1><p>Welcome ! Please GET your request from /SearchForQuote</p>";
  }

  @RequestMapping("/SearchForQuote")
  public String SearchForQuote(
    @RequestParam(value = "token", defaultValue = "World") String token){

      try{
        File file = new ClassPathResource("static/quotes.json").getFile();
        String json = new String(Files.readAllBytes(file.toPath()));

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(json);
        JsonNode array = rootNode.get("quotes");

        if (array.isArray()) {
          for (JsonNode quote : array) {
            var text = quote.get("quote");
            if (text.asText().contains(token))
              return text.asText();
          }
        }
        
      } catch (IOException ex) {
        return "Had an IO exception. Oops.";
      }

    return "Did not find that quote. Sorry bub.";
  }
}