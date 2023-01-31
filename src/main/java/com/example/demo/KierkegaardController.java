package com.example.demo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class KierkegaardController {

  private final String INVALID_SEARCH_TOKEN = "an invalid search token";

  public List<Quote> _quotes = new ArrayList<Quote>();

  @RequestMapping("/")
  public String helloWorld(){
    return "<h1>Kierkegaard Quote Finder</h1><p>Welcome ! Please GET your request from /SearchForQuote</p>";
  }

  // TODO: break this out into a service
  public boolean LoadQuotes() {
    if (_quotes.size() != 0) {
      return true;
    }

    try{
      File file = new ClassPathResource("static/quotes.json").getFile();
      String json = new String(Files.readAllBytes(file.toPath()));

      ObjectMapper mapper = new ObjectMapper();
      JsonNode rootNode = mapper.readTree(json);
      JsonNode array = rootNode.get("quotes");

      if (array.isArray()) {
        for (JsonNode quote : array) {
          var text = quote.get("quote").asText();
          var id = Integer.parseInt(quote.get("id").asText());
          var citation = quote.get("citation").asText();

          var newQuote = new Quote();
          newQuote.Id = id;
          newQuote.Text = (String)text;
          newQuote.Citation = (String)citation;
          _quotes.add(newQuote);
        }
      }
      
    } catch (IOException ex) {
      return false;
    }
    return true;
  }

  // e.g.   http://localhost:8080/SearchForQuote?token=Immortality
  @RequestMapping("/SearchForQuote")
  public String SearchForQuote(
    @RequestParam(value = "token", defaultValue = INVALID_SEARCH_TOKEN) String token){

      if (!LoadQuotes()) {
        return "Failed to load the quotes. Sorry bub.";
      }

      for (Quote quote : _quotes) {
        if (quote.Text.contains(token)) {
          return "<p>\"" + quote.Text + "\"</p>" + "<p>" + quote.Citation + "</p>";
        }
      }

    return "Did not find that quote. Sorry bub.";
  }
}