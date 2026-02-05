<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Inscription Météo</title>
    <style>
        body { font-family: sans-serif; display: flex; justify-content: center; align-items: center; height: 100vh; background-color: #f0f2f5; }
        .card { background: white; padding: 2rem; border-radius: 8px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); width: 300px; }
        h2 { text-align: center; color: #333; }
        input { width: 100%; padding: 10px; margin: 10px 0; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }
        button { width: 100%; padding: 10px; background-color: #28a745; color: white; border: none; border-radius: 4px; cursor: pointer; }
        button:hover { background-color: #218838; }
        .link { text-align: center; margin-top: 10px; display: block; font-size: 0.9rem; }
    </style>
</head>
<body>

    <div class="card">
        <h2>Créer un compte</h2>
        
        <form action="register" method="post">
            <label>Email :</label>
            <input type="email" name="email" required placeholder="votre@email.com">
            
            <label>Mot de passe :</label>
            <input type="password" name="password" required placeholder="********">
            
            <button type="submit">S'inscrire</button>
        </form>
        
        <a href="login.jsp" class="link">Déjà un compte ? Se connecter</a>
    </div>

</body>
</html>