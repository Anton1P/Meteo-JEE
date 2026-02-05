package com.meteo.model;

public class MeteoVille {
    
    private String nomVille;
    private double temperature;
    private String description; // Exemple: "Ensoleillé", "Pluvieux"

    // Nouveaux champs pour les widgets
    private double humidity;
    private double pressure;
    private double feelsLike;
    private double windSpeed;

    // Préférences d'affichage (provenant de la base de données)
    // Format attendu: "WIND,HUMIDITY,PRESSURE,FEELS_LIKE"
    private String preferences;

    // Constructeur vide (Obligatoire en JEE !)
    public MeteoVille() {
    }

    // Constructeur complet
    public MeteoVille(String nomVille, double temperature, String description,
                      double humidity, double pressure, double feelsLike, double windSpeed) {
        this.nomVille = nomVille;
        this.temperature = temperature;
        this.description = description;
        this.humidity = humidity;
        this.pressure = pressure;
        this.feelsLike = feelsLike;
        this.windSpeed = windSpeed;
    }

    // Constructeur simplifié (rétrocompatibilité)
    public MeteoVille(String nomVille, double temperature, String description) {
        this(nomVille, temperature, description, 0, 0, 0, 0);
    }

	public String getNomVille() {
		return nomVille;
	}

	public void setNomVille(String nomVille) {
		this.nomVille = nomVille;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(double feelsLike) {
        this.feelsLike = feelsLike;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getPreferences() {
        return preferences == null ? "" : preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }
}
