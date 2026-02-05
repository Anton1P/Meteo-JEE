package com.meteo.model;

public class MeteoVille {
    
    private String nomVille;
    private double temperature;
    private String description; // Exemple: "Ensoleillé", "Pluvieux"

    // Constructeur vide (Obligatoire en JEE !)
    public MeteoVille() {
    }

    // Constructeur pour remplir facilement
    public MeteoVille(String nomVille, double temperature, String description) {
        this.nomVille = nomVille;
        this.temperature = temperature;
        this.description = description;
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
    
    // ICI, IL NOUS MANQUE LES GETTERS ET SETTERS
}