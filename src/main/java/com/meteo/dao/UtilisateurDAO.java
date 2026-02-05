package com.meteo.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.mindrot.jbcrypt.BCrypt;

import com.meteo.model.Utilisateur;

public class UtilisateurDAO {
    
    private static final String URL = "jdbc:mysql://localhost:3306/meteo_jee?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PWD = "";

    public void inscrire(Utilisateur utilisateur) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connexion = DriverManager.getConnection(URL, USER, PWD);
            
            // Hachage du mot de passe avec BCrypt avant insertion
            String motDePasseHache = BCrypt.hashpw(utilisateur.getMotDePasse(), BCrypt.gensalt());

            String sql = "INSERT INTO utilisateurs (email, mot_de_passe) VALUES (?, ?)";
            PreparedStatement statement = connexion.prepareStatement(sql);
            
            statement.setString(1, utilisateur.getEmail());
            statement.setString(2, motDePasseHache);
            
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
            
            // On cherche l'utilisateur par email UNIQUEMENT
            String sql = "SELECT * FROM utilisateurs WHERE email = ?";
            PreparedStatement statement = connexion.prepareStatement(sql);
            
            statement.setString(1, email);
            
            ResultSet resultat = statement.executeQuery();
            
            if (resultat.next()) {
                String motDePasseStocke = resultat.getString("mot_de_passe");

                // On vérifie le mot de passe candidat avec le hash en base
                if (BCrypt.checkpw(password, motDePasseStocke)) {
                    utilisateur = new Utilisateur();
                    utilisateur.setId(resultat.getInt("id"));
                    utilisateur.setEmail(resultat.getString("email"));
                    utilisateur.setMotDePasse(motDePasseStocke); // On garde le hash dans l'objet
                }
            }
            
            connexion.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return utilisateur; // Renvoie null si pas trouvé ou mdp incorrect
    }
}
