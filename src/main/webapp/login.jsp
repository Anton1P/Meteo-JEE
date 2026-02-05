<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Connexion Météo</title>
    <style>
        body { font-family: sans-serif; display: flex; justify-content: center; align-items: center; height: 100vh; background-color: #f0f2f5; }
        .card { background: white; padding: 2rem; border-radius: 8px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); width: 300px; }
        h2 { text-align: center; color: #333; }
        input { width: 100%; padding: 10px; margin: 10px 0; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }
        button { width: 100%; padding: 10px; background-color: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; }
        button:hover { background-color: #0056b3; }
        .error { color: red; text-align: center; font-size: 0.9em; }
        .link { text-align: center; margin-top: 10px; display: block; font-size: 0.9rem; }
    </style>
</head>
<body>

    <div class="card">
        <h2>Connexion</h2>
        
        <% if (request.getAttribute("erreur") != null) { %>
            <p class="error"><%= request.getAttribute("erreur") %></p>
        <% } %>
        
        <form action="login" method="post">
            <label>Email :</label>
            <input type="email" name="email" required>
            
            <label>Mot de passe :</label>
            <input type="password" name="password" required>
            
            <button type="submit">Se connecter</button>
        </form>
        
        <a href="register.jsp" class="link">Pas de compte ? S'inscrire</a>
    </div>

</body>
</html>