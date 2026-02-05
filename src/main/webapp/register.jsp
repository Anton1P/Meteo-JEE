<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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

    <%-- TOAST NOTIFICATIONS --%>
    <c:if test="${not empty sessionScope.flashSuccess}">
        <div id="toast" class="toast success"><c:out value="${sessionScope.flashSuccess}" /></div>
        <c:remove var="flashSuccess" scope="session" />
    </c:if>

    <c:if test="${not empty sessionScope.flashError}">
        <div id="toast" class="toast error"><c:out value="${sessionScope.flashError}" /></div>
        <c:remove var="flashError" scope="session" />
    </c:if>

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
        <a href="home" class="link" style="color: #6c757d; margin-top: 15px;">← Retour à l'accueil / Mode Invité</a>
    </div>

    <!-- SCRIPT JS -->
    <script>
        // TOAST ANIMATION
        var toast = document.getElementById("toast");
        if (toast) {
            toast.className += " show";
            setTimeout(function(){
                toast.className = toast.className.replace(" show", "");
            }, 3000);
        }
    </script>

</body>
</html>
