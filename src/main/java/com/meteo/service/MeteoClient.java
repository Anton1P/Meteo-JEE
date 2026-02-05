package com.meteo.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.meteo.model.MeteoVille;

public class MeteoClient {
    
    private static final String API_KEY = "349e55d94fd3813fd3c348ab256b645b";
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric&lang=fr";

    public MeteoVille recupererMeteo(String ville) {
        try {
            // 1. On prépare l'adresse exacte (on remplace les %s par la ville et la clé)
            String adresseComplete = String.format(API_URL, ville, API_KEY);
            URL url = URI.create(adresseComplete).toURL();

            // 2. On ouvre la connexion (comme on décroche le téléphone)
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // 3. On vérifie si ça sonne occupé (Code 200 = OK)
            if (connection.getResponseCode() != 200) {
                // Si la ville n'existe pas ou erreur, on renvoie null
                return null;
            }

            // 4. On lit la réponse du serveur ligne par ligne
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder resultatJson = new StringBuilder();
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                resultatJson.append(ligne);
            }
            reader.close();

            // --- C'est ici que Gson (notre traducteur) entre en jeu ---
            
            // A. On transforme le texte brut en un objet JSON manipulable
            JsonObject jsonComplet = JsonParser.parseString(resultatJson.toString()).getAsJsonObject();

            // B. On va chercher la température : elle est cachée dans le dossier "main"
            // JSON : { "main": { "temp": 15.5 }, ... }
            double temp = jsonComplet.getAsJsonObject("main").get("temp").getAsDouble();

            // C. On va chercher la description : cachée dans la liste "weather" (premier élément)
            // JSON : { "weather": [ { "description": "ciel dégagé" } ], ... }
            String description = jsonComplet.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();

            // 5. On cuisine le plat final : notre objet MeteoVille
            return new MeteoVille(ville, temp, description);

        } catch (Exception e) {
            e.printStackTrace();
            return null; // En cas de pépin technique
        }
    }
}