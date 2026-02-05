package com.meteo.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.meteo.model.MeteoVille;

public class MeteoClient {
    
    private static final String API_KEY = "349e55d94fd3813fd3c348ab256b645b";
    private static final String API_URL_VILLE = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric&lang=fr";
    private static final String API_URL_COORD = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s&units=metric&lang=fr";

    public MeteoVille recupererMeteo(String ville) {
        try {
            String villeEncodee = URLEncoder.encode(ville, StandardCharsets.UTF_8.toString());
            String adresseComplete = String.format(API_URL_VILLE, villeEncodee, API_KEY);
            return appelerApi(adresseComplete, ville);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public MeteoVille recupererMeteoByCoordinates(double lat, double lon) {
        String adresseComplete = String.format(API_URL_COORD, lat, lon, API_KEY);
        return appelerApi(adresseComplete, null); // Ville inconnue au départ, l'API nous le dira
    }

    private MeteoVille appelerApi(String urlString, String nomVilleParDefaut) {
        try {
            URL url = URI.create(urlString).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() != 200) {
                return null;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder resultatJson = new StringBuilder();
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                resultatJson.append(ligne);
            }
            reader.close();

            JsonObject jsonComplet = JsonParser.parseString(resultatJson.toString()).getAsJsonObject();

            // Extraction des données principales
            JsonObject main = jsonComplet.getAsJsonObject("main");
            double temp = main.get("temp").getAsDouble();
            double pressure = main.has("pressure") ? main.get("pressure").getAsDouble() : 0.0;
            double humidity = main.has("humidity") ? main.get("humidity").getAsDouble() : 0.0;
            double feelsLike = main.has("feels_like") ? main.get("feels_like").getAsDouble() : 0.0;

            // Extraction du vent
            double windSpeed = 0.0;
            if (jsonComplet.has("wind")) {
                windSpeed = jsonComplet.getAsJsonObject("wind").get("speed").getAsDouble();
            }

            // Description
            String description = jsonComplet.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();

            // Nom de la ville (Si l'API le donne, on l'utilise, sinon on prend le défaut)
            String nomVille = nomVilleParDefaut;
            if (jsonComplet.has("name") && !jsonComplet.get("name").getAsString().isEmpty()) {
                nomVille = jsonComplet.get("name").getAsString();
            }

            return new MeteoVille(nomVille, temp, description, humidity, pressure, feelsLike, windSpeed);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
