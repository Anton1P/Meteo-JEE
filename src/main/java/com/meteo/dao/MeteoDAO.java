package com.meteo.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.meteo.model.MeteoVille;

public class MeteoDAO {
    
    // Les informations de connexion
    private static final String URL = "jdbc:mysql://localhost:3306/meteo_jee?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PWD = "";

    // --- 1. AJOUTER UNE VILLE (Dans la bibliothèque globale) ---
    public void ajouterVille(MeteoVille ville) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connexion = DriverManager.getConnection(URL, USER, PWD);
            
            // On vérifie d'abord si la ville existe déjà pour éviter les doublons
            if (trouverIdVille(ville.getNomVille()) == -1) {
                String sql = "INSERT INTO villes (nom_ville) VALUES (?)";
                PreparedStatement statement = connexion.prepareStatement(sql);
                statement.setString(1, ville.getNomVille());
                statement.executeUpdate();
            }
            
            connexion.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- 2. RECUPERER LES VILLES D'UN UTILISATEUR ---
    public List<MeteoVille> recupererVilles(int idUtilisateur) {
        List<MeteoVille> listeVilles = new ArrayList<>();
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connexion = DriverManager.getConnection(URL, USER, PWD);
            
            // On récupère aussi les préférences
            String sql = "SELECT v.nom_ville, f.preferences " +
                         "FROM villes v " +
                         "JOIN favoris f ON v.id = f.id_ville " +
                         "WHERE f.id_utilisateur = ?";
                         
            PreparedStatement statement = connexion.prepareStatement(sql);
            statement.setInt(1, idUtilisateur);
            
            ResultSet resultat = statement.executeQuery();
            
            while (resultat.next()) {
                MeteoVille ville = new MeteoVille();
                ville.setNomVille(resultat.getString("nom_ville"));
                ville.setPreferences(resultat.getString("preferences"));
                listeVilles.add(ville);
            }
            connexion.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listeVilles;
    }

    // --- 3. AJOUTER UN FAVORI (Le lien entre User et Ville) ---
    public void ajouterFavori(int idUtilisateur, int idVille) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connexion = DriverManager.getConnection(URL, USER, PWD);
            
            String sql = "INSERT INTO favoris (id_utilisateur, id_ville) VALUES (?, ?)";
            PreparedStatement statement = connexion.prepareStatement(sql);
            
            statement.setInt(1, idUtilisateur);
            statement.setInt(2, idVille);
            
            statement.executeUpdate();
            connexion.close();
        } catch (Exception e) {
            // Si le favori existe déjà, MySQL renverra une erreur (doublon), on l'ignore.
             System.out.println("Ce favori existe déjà ou erreur SQL : " + e.getMessage());
        }
    }

    // --- 4. SUPPRIMER UN FAVORI ---
    public void supprimerFavori(int idUtilisateur, String nomVille) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connexion = DriverManager.getConnection(URL, USER, PWD);
            
            // On utilise une sous-requête pour trouver l'ID de la ville directement
            String sql = "DELETE FROM favoris " +
                         "WHERE id_utilisateur = ? " +
                         "AND id_ville = (SELECT id FROM villes WHERE nom_ville = ?)";
            
            PreparedStatement statement = connexion.prepareStatement(sql);
            statement.setInt(1, idUtilisateur);
            statement.setString(2, nomVille);
            
            statement.executeUpdate();
            connexion.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- 5. UTILITAIRE : TROUVER L'ID D'UNE VILLE ---
    public int trouverIdVille(String nomVille) {
        int id = -1; // -1 signifie "pas trouvé"
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connexion = DriverManager.getConnection(URL, USER, PWD);
            
            String sql = "SELECT id FROM villes WHERE nom_ville = ?";
            PreparedStatement statement = connexion.prepareStatement(sql);
            statement.setString(1, nomVille);
            
            ResultSet resultat = statement.executeQuery();
            if (resultat.next()) {
                id = resultat.getInt("id");
            }
            connexion.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    // --- 6. METTRE A JOUR LES PREFERENCES ---
    public void updatePreferences(int idUtilisateur, String nomVille, String preferences) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connexion = DriverManager.getConnection(URL, USER, PWD);

            String sql = "UPDATE favoris SET preferences = ? " +
                         "WHERE id_utilisateur = ? " +
                         "AND id_ville = (SELECT id FROM villes WHERE nom_ville = ?)";

            PreparedStatement statement = connexion.prepareStatement(sql);
            statement.setString(1, preferences);
            statement.setInt(2, idUtilisateur);
            statement.setString(3, nomVille);

            statement.executeUpdate();
            connexion.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
