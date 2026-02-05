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
        
        HttpSession session = request.getSession();

        if (user != null) {
            // --- SUCCÈS ---
            session.setAttribute("utilisateur", user);
            session.setAttribute("flashSuccess", "Heureux de vous revoir !");
            
            // 3. Redirection vers la page d'accueil principale
            response.sendRedirect("home");
            
        } else {
            // --- ÉCHEC ---
            // Utilisation des flash attributes via la session au lieu du request forward
            // pour être cohérent avec l'UX des autres pages (Toast)
            session.setAttribute("flashError", "Email ou mot de passe incorrect.");
            response.sendRedirect("login.jsp");
        }
    }
}
