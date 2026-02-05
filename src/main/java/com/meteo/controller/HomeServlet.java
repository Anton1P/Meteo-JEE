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
        HttpSession session = request.getSession();

        // --- 1. GESTION DE LA GÉOLOCALISATION (Paramètres URL) ---
        String latParam = request.getParameter("lat");
        String lonParam = request.getParameter("lon");

        if (latParam != null && lonParam != null) {
            try {
                double lat = Double.parseDouble(latParam);
                double lon = Double.parseDouble(lonParam);

                // On récupère la météo de la position
                MeteoVille guestWeather = meteoClient.recupererMeteoByCoordinates(lat, lon);

                if (guestWeather != null) {
                    // On marque cette ville comme étant la "Position actuelle" pour l'affichage
                    // (Optionnel, mais sympa pour l'UX)
                    guestWeather.setDescription(guestWeather.getDescription() + " (Position actuelle)");

                    // On stocke en session pour ne pas redemander à chaque F5
                    session.setAttribute("guestWeather", guestWeather);

                    // On nettoie l'indicateur d'échec s'il existait
                    session.removeAttribute("geoFailed");

                    // On redirige pour nettoyer l'URL
                    response.sendRedirect("home");
                    return;
                } else {
                    // Si l'API renvoie null (ex: service indisponible ou coords invalides)
                    // On ne redirige PAS vers 'home' pour éviter la boucle infinie
                    // On note l'échec en session pour ne plus demander
                    session.setAttribute("geoFailed", true);
                    session.setAttribute("flashError", "Impossible de récupérer la météo locale.");
                }
            } catch (NumberFormatException e) {
                // Ignorer si paramètres invalides
                session.setAttribute("geoFailed", true);
            }

            // Si on est ici, c'est que ça a échoué. On continue le flux normal pour afficher la page avec l'erreur.
            // On ne redirige pas, on laisse le flux descendre pour afficher la JSP.
        }

        // --- 2. LOGIQUE PRINCIPALE ---
        Utilisateur utilisateurConnecte = (Utilisateur) session.getAttribute("utilisateur");
        List<MeteoVille> villesAffichees = new ArrayList<>();
        
        boolean isUserLoggedIn = (utilisateurConnecte != null);
        boolean hasFavorites = false;

        if (isUserLoggedIn) {
            // L'utilisateur est connecté, on récupère ses favoris (avec préférences)
            List<MeteoVille> favoris = meteoDAO.recupererVilles(utilisateurConnecte.getId());

            if (!favoris.isEmpty()) {
                hasFavorites = true;
                for (MeteoVille favori : favoris) {
                    MeteoVille villeComplete = meteoClient.recupererMeteo(favori.getNomVille());

                    if (villeComplete != null) {
                        villeComplete.setPreferences(favori.getPreferences());
                        villesAffichees.add(villeComplete);
                    }
                }
            }
        }

        // --- 3. FALLBACK / MODE INVITÉ ---
        // Si pas connecté OU (connecté mais zéro favori)
        if (!isUserLoggedIn || !hasFavorites) {

            // A-t-on déjà la position en session ?
            MeteoVille guestWeather = (MeteoVille) session.getAttribute("guestWeather");
            Boolean geoFailed = (Boolean) session.getAttribute("geoFailed");

            if (guestWeather != null) {
                villesAffichees.add(guestWeather);
            } else if (geoFailed == null || !geoFailed) {
                // Seulement si on n'a pas déjà échoué
                request.setAttribute("askForGeolocation", true);
            }
        }
        
        request.setAttribute("villes", villesAffichees);
        this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); // Important pour les accents
        HttpSession session = request.getSession();
        Utilisateur utilisateurConnecte = (Utilisateur) session.getAttribute("utilisateur");
        
        String action = request.getParameter("action");

        // Certaines actions nécessitent d'être connecté
        if (utilisateurConnecte == null) {
            // Si un invité essaie de poster (ex: ajouter ville), on le redirige vers le login
            response.sendRedirect("login.jsp");
            return;
        }

        if (action != null) {
            if (action.equals("supprimer")) {
                String nomVilleASupprimer = request.getParameter("nomVille");
                meteoDAO.supprimerFavori(utilisateurConnecte.getId(), nomVilleASupprimer);
                session.setAttribute("flashSuccess", "Ville supprimée.");

            } else if (action.equals("updatePreferences")) {
                String nomVille = request.getParameter("nomVille");

                // On récupère les cases cochées. Ex: ["WIND", "HUMIDITY"]
                String[] prefsArray = request.getParameterValues("prefs");
                String prefsString = "";

                if (prefsArray != null && prefsArray.length > 0) {
                    prefsString = String.join(",", prefsArray);
                }

                meteoDAO.updatePreferences(utilisateurConnecte.getId(), nomVille, prefsString);
                session.setAttribute("flashSuccess", "Préférences sauvegardées.");
            }
        } else {
            // Ajout classique
            String nouvelleVille = request.getParameter("nomVille");
            
            if (nouvelleVille != null && !nouvelleVille.isEmpty()) {
                MeteoVille villeVerifiee = meteoClient.recupererMeteo(nouvelleVille);
                
                if (villeVerifiee != null) {
                    meteoDAO.ajouterVille(villeVerifiee);
                    int idVille = meteoDAO.trouverIdVille(villeVerifiee.getNomVille());
                    
                    if (idVille != -1) {
                        meteoDAO.ajouterFavori(utilisateurConnecte.getId(), idVille);
                        session.setAttribute("flashSuccess", "Ville ajoutée avec succès !");
                    } else {
                        session.setAttribute("flashError", "Erreur technique lors de l'ajout.");
                    }
                } else {
                    session.setAttribute("flashError", "Ville introuvable : " + nouvelleVille);
                }
            }
        }
        
        // On recharge la page (Pattern PRG: Post-Redirect-Get)
        response.sendRedirect("home");
    }
}
