package com.meteo.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. On récupère la session actuelle
        HttpSession session = request.getSession(false); // false = ne pas en créer une si elle n'existe pas
        
        // 2. Si elle existe, on la détruit (invalidation)
        if (session != null) {
            session.invalidate();
        }
        
        // 3. On redirige vers la page de connexion
        response.sendRedirect("login.jsp");
    }
}