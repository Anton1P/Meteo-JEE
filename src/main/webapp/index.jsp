<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.meteo.model.Utilisateur" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Ma Météo - Accueil</title>

    <!-- jQuery UI & jQuery -->
    <link rel="stylesheet" href="https://code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://code.jquery.com/ui/1.13.2/jquery-ui.min.js"></script>

    <style>
        body { font-family: 'Segoe UI', sans-serif; background-color: #eef2f3; margin: 0; padding: 20px; }
        .container { max-width: 900px; margin: 0 auto; }
        
        /* En-tête */
        .header { display: flex; justify-content: space-between; align-items: center; background: white; padding: 15px 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); margin-bottom: 20px; }
        .user-info { font-weight: bold; color: #555; }
        .btn-logout { text-decoration: none; color: white; background-color: #dc3545; padding: 8px 15px; border-radius: 4px; font-size: 0.9em; transition: 0.3s;}
        .btn-logout:hover { background-color: #c82333; }
        .btn-login { text-decoration: none; color: white; background-color: #007bff; padding: 8px 15px; border-radius: 4px; font-size: 0.9em; transition: 0.3s;}
        .btn-login:hover { background-color: #0056b3; }

        /* Message d'erreur géo */
        .geo-message { background: #fff3cd; color: #856404; padding: 15px; border-radius: 8px; margin-bottom: 20px; text-align: center; border: 1px solid #ffeeba; display: none; }

        /* Formulaire d'ajout */
        .add-form { display: flex; gap: 10px; margin-bottom: 30px; justify-content: center; }
        .input-ville { width: 60%; padding: 12px; border: 1px solid #ddd; border-radius: 6px; font-size: 16px; transition: 0.3s; }
        .input-ville:focus { border-color: #007bff; outline: none; box-shadow: 0 0 5px rgba(0,123,255,0.2); }
        .btn-add { padding: 12px 25px; background-color: #28a745; color: white; border: none; border-radius: 6px; cursor: pointer; font-size: 16px; transition: 0.3s; }
        .btn-add:hover { background-color: #218838; }

        /* Grille Cartes */
        .grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(250px, 1fr)); gap: 20px; }
        .card { background: white; padding: 20px; border-radius: 12px; box-shadow: 0 4px 6px rgba(0,0,0,0.05); text-align: center; position: relative; transition: transform 0.2s; }
        .card:hover { transform: translateY(-5px); }

        .temp { font-size: 2.5rem; font-weight: bold; color: #333; margin: 10px 0; }
        .desc { color: #666; text-transform: capitalize; font-size: 1.1em; margin-bottom: 15px; }
        
        /* Boutons Card */
        .btn-delete { position: absolute; top: 10px; right: 10px; background: none; border: none; color: #aaa; font-size: 1.2rem; cursor: pointer; transition: 0.2s; }
        .btn-delete:hover { color: #dc3545; }

        .btn-settings { position: absolute; top: 10px; left: 10px; background: none; border: none; color: #aaa; font-size: 1.2rem; cursor: pointer; transition: 0.2s; }
        .btn-settings:hover { color: #007bff; transform: rotate(90deg); }

        /* Panel Settings */
        .settings-panel { display: none; background: #f8f9fa; padding: 10px; border-radius: 8px; margin-bottom: 10px; text-align: left; font-size: 0.9em; border: 1px solid #eee; }
        .settings-panel label { display: block; margin: 5px 0; cursor: pointer; }
        .btn-save-prefs { width: 100%; margin-top: 5px; background: #6c757d; color: white; border: none; padding: 5px; border-radius: 4px; cursor: pointer; }
        .btn-save-prefs:hover { background: #5a6268; }

        /* Données supplémentaires */
        .extras { border-top: 1px solid #eee; margin-top: 15px; padding-top: 15px; display: grid; grid-template-columns: 1fr 1fr; gap: 10px; text-align: left; font-size: 0.9em; color: #555; }
        .extra-item { display: flex; align-items: center; gap: 5px; }

        /* Toast Notifications */
        .toast {
            visibility: hidden;
            min-width: 250px;
            margin-left: -125px;
            background-color: #333;
            color: #fff;
            text-align: center;
            border-radius: 4px;
            padding: 16px;
            position: fixed;
            z-index: 1;
            left: 50%;
            bottom: 30px;
            font-size: 17px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.2);
            opacity: 0;
            transition: opacity 0.5s, bottom 0.5s;
        }

        .toast.show {
            visibility: visible;
            opacity: 1;
            bottom: 50px;
        }

        .toast.success { background-color: #28a745; }
        .toast.error { background-color: #dc3545; }
    </style>
</head>
<body>

<div class="container">

    <%-- TOAST NOTIFICATIONS (Flash Attributes) --%>
    <c:if test="${not empty sessionScope.flashSuccess}">
        <div id="toast" class="toast success"><c:out value="${sessionScope.flashSuccess}" /></div>
        <c:remove var="flashSuccess" scope="session" />
    </c:if>

    <c:if test="${not empty sessionScope.flashError}">
        <div id="toast" class="toast error"><c:out value="${sessionScope.flashError}" /></div>
        <c:remove var="flashError" scope="session" />
    </c:if>

    <!-- HEADER -->
    <div class="header">
        <div class="user-info">
            <c:choose>
                <c:when test="${sessionScope.utilisateur != null}">
                    Bonjour, ${sessionScope.utilisateur.email}
                </c:when>
                <c:otherwise>
                    Mode Invité
                </c:otherwise>
            </c:choose>
        </div>

        <c:choose>
            <c:when test="${sessionScope.utilisateur != null}">
                <a href="logout" class="btn-logout">Se déconnecter</a>
            </c:when>
            <c:otherwise>
                <a href="login.jsp" class="btn-login">Se connecter / S'inscrire</a>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- GEOLOCATION FEEDBACK -->
    <div id="geo-message" class="geo-message">
        Impossible de vous localiser. Veuillez rechercher une ville ou vous connecter.
    </div>

    <!-- FORMULAIRE AJOUT -->
    <form action="home" method="post" class="add-form">
        <input type="text" name="nomVille" id="search-input" class="input-ville" placeholder="Entrez une ville (ex: Paris, Tokyo...)" required>
        <button type="submit" class="btn-add">Ajouter +</button>
    </form>

    <!-- GRILLE METEO -->
    <div class="grid">
        <c:forEach items="${villes}" var="ville" varStatus="status">
            <div class="card">
                <!-- ACTIONS (Suppression & Settings) : Seulement pour les utilisateurs connectés -->
                <c:if test="${sessionScope.utilisateur != null}">
                    <form action="home" method="post">
                        <input type="hidden" name="action" value="supprimer">
                        <input type="hidden" name="nomVille" value="${ville.nomVille}">
                        <button type="submit" class="btn-delete" title="Supprimer">×</button>
                    </form>

                    <button class="btn-settings" onclick="toggleSettings(${status.index})" title="Personnaliser">⚙️</button>

                    <!-- FORMULAIRE REGLAGES -->
                    <div id="settings-${status.index}" class="settings-panel">
                        <form action="home" method="post">
                            <input type="hidden" name="action" value="updatePreferences">
                            <input type="hidden" name="nomVille" value="${ville.nomVille}">

                            <strong>Afficher :</strong>
                            <label><input type="checkbox" name="prefs" value="WIND" ${fn:contains(ville.preferences, 'WIND') ? 'checked' : ''}> Vent</label>
                            <label><input type="checkbox" name="prefs" value="HUMIDITY" ${fn:contains(ville.preferences, 'HUMIDITY') ? 'checked' : ''}> Humidité</label>
                            <label><input type="checkbox" name="prefs" value="PRESSURE" ${fn:contains(ville.preferences, 'PRESSURE') ? 'checked' : ''}> Pression</label>
                            <label><input type="checkbox" name="prefs" value="FEELS_LIKE" ${fn:contains(ville.preferences, 'FEELS_LIKE') ? 'checked' : ''}> Ressenti</label>

                            <button type="submit" class="btn-save-prefs">Enregistrer</button>
                        </form>
                    </div>
                </c:if>
                
                <h3>${ville.nomVille}</h3>
                <div class="temp">${ville.temperature}°C</div>
                <div class="desc">${ville.description}</div>

                <!-- WIDGETS PERSONNALISABLES -->
                <!-- Pour les invités, on affiche tout par défaut ? Ou rien ?
                     Restons logique : si pas de préférences (null), on n'affiche rien de plus, ou alors une sélection par défaut.
                     Ici, le code affiche si présent dans 'preferences'. -->

                <c:if test="${not empty ville.preferences or sessionScope.utilisateur == null}">
                   <div class="extras">
                       <%-- Si Invité, on affiche Humidité et Vent par défaut pour montrer que ça existe --%>
                       <c:if test="${fn:contains(ville.preferences, 'WIND') or sessionScope.utilisateur == null}">
                           <div class="extra-item">🌬️ ${ville.windSpeed} km/h</div>
                       </c:if>

                       <c:if test="${fn:contains(ville.preferences, 'HUMIDITY') or sessionScope.utilisateur == null}">
                           <div class="extra-item">💧 ${ville.humidity}%</div>
                       </c:if>

                       <c:if test="${fn:contains(ville.preferences, 'PRESSURE')}">
                           <div class="extra-item">⏲️ ${ville.pressure} hPa</div>
                       </c:if>

                       <c:if test="${fn:contains(ville.preferences, 'FEELS_LIKE')}">
                           <div class="extra-item">🌡️ ${ville.feelsLike}°C</div>
                       </c:if>
                   </div>
                </c:if>
            </div>
        </c:forEach>
    </div>

</div>

<!-- SCRIPT JS -->
<script>
    // TOAST ANIMATION
    var toast = document.getElementById("toast");
    if (toast) {
        toast.className += " show";
        setTimeout(function(){
            toast.className = toast.className.replace(" show", "");
            // Petit hack pour le retirer du flux après l'anim si besoin, mais visibility:hidden suffit
        }, 3000);
    }

    // Fonction pour afficher/cacher les réglages
    function toggleSettings(index) {
        var panel = document.getElementById('settings-' + index);
        if (panel.style.display === "block") {
            panel.style.display = "none";
        } else {
            panel.style.display = "block";
        }
    }

    // AUTOCOMPLETION (jQuery UI)
    $(document).ready(function() {
        $("#search-input").autocomplete({
            source: function(request, response) {
                $.ajax({
                    url: "autocomplete",
                    dataType: "json",
                    data: {
                        term: request.term
                    },
                    success: function(data) {
                        response(data);
                    }
                });
            },
            minLength: 2, // Se déclenche après 2 caractères
            select: function(event, ui) {
                // Quand on clique sur une suggestion, on remplit le champ
                // On garde "Ville, Pays" pour l'API météo
                // On pourrait nettoyer si besoin, mais l'API météo gère "Paris, FR"
                $("#search-input").val(ui.item.value);
            }
        });
    });

    // GÉOLOCALISATION AUTOMATIQUE
    <c:if test="${askForGeolocation}">
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(
                function(position) {
                    // Succès : Redirection vers le Servlet avec les coordonnées
                    window.location.href = "home?lat=" + position.coords.latitude + "&lon=" + position.coords.longitude;
                },
                function(error) {
                    // Erreur ou Refus
                    document.getElementById('geo-message').style.display = 'block';
                    document.getElementById('search-input').focus();
                }
            );
        } else {
            // Pas de support
            document.getElementById('geo-message').style.display = 'block';
        }
    </c:if>
</script>

</body>
</html>
