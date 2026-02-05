package com.meteo.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meteo.dao.UtilisateurDAO;
import com.meteo.model.Utilisateur;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    // Si on accède via l'URL (GET), on affiche simplement le formulaire
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.getServletContext().getRequestDispatcher("/register.jsp").forward(request, response);
    }

    // Si on soumet le formulaire (POST), on traite l'inscription
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. Récupérer les données
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        // 2. Créer l'objet Utilisateur
        Utilisateur nouvelUtilisateur = new Utilisateur(email, password);
        
        // 3. Sauvegarder en base via le DAO
        utilisateurDAO.inscrire(nouvelUtilisateur);
        
        // 4. Rediriger vers la page de connexion (qu'on fera juste après)
        // Pour l'instant, on redirige vers l'accueil pour tester
        response.sendRedirect("register.jsp"); 
    }
}