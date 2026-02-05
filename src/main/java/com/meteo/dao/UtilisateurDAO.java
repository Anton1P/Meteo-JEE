package com.meteo.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.meteo.model.Utilisateur;

public class UtilisateurDAO {
    
    private static final String URL = "jdbc:mysql://localhost:3306/meteo_jee?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PWD = "";

    public void inscrire(Utilisateur utilisateur) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connexion = DriverManager.getConnection(URL, USER, PWD);
            
            // La requête pour insérer un nouvel utilisateur
            String sql = "INSERT INTO utilisateurs (email, mot_de_passe) VALUES (?, ?)";
            PreparedStatement statement = connexion.prepareStatement(sql);
            
            statement.setString(1, utilisateur.getEmail());
            statement.setString(2, utilisateur.getMotDePasse());
            
            statement.executeUpdate();
            connexion.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Utilisateur seConnecter(String email, String password) {
        Utilisateur utilisateur = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connexion = DriverManager.getConnection(URL, USER, PWD);
            
            // On cherche l'utilisateur qui a CET email ET CE mot de passe
            String sql = "SELECT * FROM utilisateurs WHERE email = ? AND mot_de_passe = ?";
            PreparedStatement statement = connexion.prepareStatement(sql);
            
            statement.setString(1, email);
            statement.setString(2, password);
            
            ResultSet resultat = statement.executeQuery();
            
            if (resultat.next()) {
                // Bingo ! On a trouvé. On crée l'objet Java correspondant.
                utilisateur = new Utilisateur();
                utilisateur.setId(resultat.getInt("id"));
                utilisateur.setEmail(resultat.getString("email"));
                utilisateur.setMotDePasse(resultat.getString("mot_de_passe"));
            }
            
            connexion.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return utilisateur; // Renvoie null si pas trouvé, ou l'objet User si trouvé
    }
}