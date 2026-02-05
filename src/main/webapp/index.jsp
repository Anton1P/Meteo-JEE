<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.meteo.model.Utilisateur" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Ma Météo - Accueil</title>
    <style>
        body { font-family: 'Segoe UI', sans-serif; background-color: #eef2f3; margin: 0; padding: 20px; }
        .container { max-width: 800px; margin: 0 auto; }
        
        /* En-tête avec info utilisateur et déconnexion */
        .header { display: flex; justify-content: space-between; align-items: center; background: white; padding: 15px 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); margin-bottom: 20px; }
        .user-info { font-weight: bold; color: #555; }
        .btn-logout { text-decoration: none; color: white; background-color: #dc3545; padding: 8px 15px; border-radius: 4px; font-size: 0.9em; }
        .btn-logout:hover { background-color: #c82333; }

        /* Formulaire d'ajout */
        .add-form { display: flex; gap: 10px; margin-bottom: 30px; }
        .input-ville { flex-grow: 1; padding: 12px; border: 1px solid #ddd; border-radius: 6px; font-size: 16px; }
        .btn-add { padding: 12px 25px; background-color: #28a745; color: white; border: none; border-radius: 6px; cursor: pointer; font-size: 16px; }
        .btn-add:hover { background-color: #218838; }

        /* Cartes Météo */
        .grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(200px, 1fr)); gap: 20px; }
        .card { background: white; padding: 20px; border-radius: 12px; box-shadow: 0 4px 6px rgba(0,0,0,0.05); text-align: center; position: relative; transition: transform 0.2s; }
        .card:hover { transform: translateY(-5px); }
        .temp { font-size: 2.5rem; font-weight: bold; color: #333; margin: 10px 0; }
        .desc { color: #666; text-transform: capitalize; }
        
        /* Bouton supprimer (croix) */
        .btn-delete { position: absolute; top: 10px; right: 10px; background: none; border: none; color: #999; font-size: 1.2rem; cursor: pointer; }
        .btn-delete:hover { color: #dc3545; }
    </style>
</head>
<body>

<div class="container">

    <div class="header">
        <div class="user-info">
            <% 
                Utilisateur u = (Utilisateur) session.getAttribute("utilisateur");
                if(u != null) { out.print("Bonjour, " + u.getEmail()); }
            %>
        </div>
        <a href="logout" class="btn-logout">Se déconnecter</a>
    </div>

    <form action="home" method="post" class="add-form">
        <input type="text" name="nomVille" class="input-ville" placeholder="Entrez une ville (ex: Paris, Tokyo...)" required>
        <button type="submit" class="btn-add">Ajouter +</button>
    </form>

    <div class="grid">
        <c:forEach items="${villes}" var="ville">
            <div class="card">
                <form action="home" method="post">
                    <input type="hidden" name="action" value="supprimer">
                    <input type="hidden" name="nomVille" value="${ville.nomVille}">
                    <button type="submit" class="btn-delete" title="Supprimer">×</button>
                </form>
                
                <h3>${ville.nomVille}</h3>
                <div class="temp">${ville.temperature}°C</div>
                <div class="desc">${ville.description}</div>
            </div>
        </c:forEach>
    </div>

</div>

</body>
</html>