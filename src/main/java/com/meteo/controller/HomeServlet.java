package com.meteo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.meteo.dao.MeteoDAO;
import com.meteo.model.MeteoVille;
import com.meteo.model.Utilisateur;
import com.meteo.service.MeteoClient;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private MeteoClient meteoClient = new MeteoClient();
    private MeteoDAO meteoDAO = new MeteoDAO();

    public HomeServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // --- 1. SÉCURITÉ : Vérifier si l'utilisateur est connecté ---
        HttpSession session = request.getSession();
        Utilisateur utilisateurConnecte = (Utilisateur) session.getAttribute("utilisateur");

        if (utilisateurConnecte == null) {
            // Pas connecté ? Dehors ! Direction le login.
            response.sendRedirect("login.jsp");
            return;
        }

        // --- 2. LOGIQUE MÉTIER ---
        // On utilise l'ID de l'utilisateur connecté pour chercher SES villes
        List<String> nomsDesVilles = meteoDAO.recupererVilles(utilisateurConnecte.getId());
        
        List<MeteoVille> villesAffichees = new ArrayList<>();
        
        for (String nom : nomsDesVilles) {
            MeteoVille villeComplete = meteoClient.recupererMeteo(nom);
            if (villeComplete != null) {
                villesAffichees.add(villeComplete);
            }
        }
        
        request.setAttribute("villes", villesAffichees);
        this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // --- 1. SÉCURITÉ ---
        HttpSession session = request.getSession();
        Utilisateur utilisateurConnecte = (Utilisateur) session.getAttribute("utilisateur");
        
        if (utilisateurConnecte == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        
        if (action != null && action.equals("supprimer")) {
            // Suppression liée à l'utilisateur
            String nomVilleASupprimer = request.getParameter("nomVille");
            meteoDAO.supprimerFavori(utilisateurConnecte.getId(), nomVilleASupprimer);
            
        } else {
            // Ajout lié à l'utilisateur
            String nouvelleVille = request.getParameter("nomVille");
            
            if (nouvelleVille != null && !nouvelleVille.isEmpty()) {
                MeteoVille villeVerifiee = meteoClient.recupererMeteo(nouvelleVille);
                
                if (villeVerifiee != null) {
                    // 1. On s'assure que la ville existe dans le catalogue global
                    meteoDAO.ajouterVille(villeVerifiee);
                    
                    // 2. On récupère son ID
                    int idVille = meteoDAO.trouverIdVille(villeVerifiee.getNomVille());
                    
                    // 3. On crée le lien User <-> Ville
                    if (idVille != -1) {
                        meteoDAO.ajouterFavori(utilisateurConnecte.getId(), idVille);
                    }
                }
            }
        }
        
        // On recharge la page
        doGet(request, response);
    }
}