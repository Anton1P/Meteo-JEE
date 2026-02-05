package com.meteo.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.meteo.dao.UtilisateurDAO;
import com.meteo.model.Utilisateur;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    // GET : Affiche juste le formulaire
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
    }

    // POST : Traite la connexion
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        // 1. On interroge la base de données
        Utilisateur user = utilisateurDAO.seConnecter(email, password);
        
        if (user != null) {
            // --- SUCCÈS ---
            
            // 2. CRÉATION DE LA SESSION (Le moment le plus important)
            // On récupère la session de l'utilisateur (ou on en crée une nouvelle)
            HttpSession session = request.getSession();
            
            // On stocke l'objet "user" dedans. C'est son badge d'identité.
            session.setAttribute("utilisateur", user);
            
            // 3. Redirection vers la page d'accueil principale
            response.sendRedirect("home");
            
        } else {
            // --- ÉCHEC ---
            
            // On prépare un message d'erreur
            request.setAttribute("erreur", "Email ou mot de passe incorrect");
            
            // On renvoie vers le formulaire de login
            this.getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}