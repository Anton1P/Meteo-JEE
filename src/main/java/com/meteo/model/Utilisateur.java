package com.meteo.model;

public class Utilisateur {
    
    private int id;
    private String email;
    private String motDePasse;

    // Constructeur vide (nécessaire pour certains frameworks et pratique)
    public Utilisateur() {
    }

    // Constructeur complet
    public Utilisateur(int id, String email, String motDePasse) {
        this.id = id;
        this.email = email;
        this.motDePasse = motDePasse;
    }
    
    // Constructeur sans ID (pour la création d'un nouvel utilisateur avant insertion en BDD)
    public Utilisateur(String email, String motDePasse) {
        this.email = email;
        this.motDePasse = motDePasse;
    }

    // --- Getters et Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
}