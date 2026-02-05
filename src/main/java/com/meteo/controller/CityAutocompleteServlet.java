package com.meteo.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@WebServlet("/autocomplete")
public class CityAutocompleteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String API_KEY = "349e55d94fd3813fd3c348ab256b645b";
    private static final String GEO_API_URL = "http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=5&appid=%s";

    public CityAutocompleteServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String term = request.getParameter("term");
        List<String> suggestions = new ArrayList<>();

        if (term != null && !term.isEmpty()) {
            try {
                String encodedTerm = URLEncoder.encode(term, StandardCharsets.UTF_8.toString());
                String urlString = String.format(GEO_API_URL, encodedTerm, API_KEY);

                URL url = URI.create(urlString).toURL();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                if (connection.getResponseCode() == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    reader.close();

                    JsonArray jsonArray = JsonParser.parseString(result.toString()).getAsJsonArray();

                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject city = jsonArray.get(i).getAsJsonObject();
                        String name = city.get("name").getAsString();
                        String country = city.get("country").getAsString();
                        String state = city.has("state") ? city.get("state").getAsString() : null;

                        StringBuilder label = new StringBuilder(name);
                        if (state != null && !state.isEmpty()) {
                            label.append(", ").append(state);
                        }
                        label.append(", ").append(country);

                        suggestions.add(label.toString());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Renvoi de la réponse JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        new Gson().toJson(suggestions, response.getWriter());
    }
}
