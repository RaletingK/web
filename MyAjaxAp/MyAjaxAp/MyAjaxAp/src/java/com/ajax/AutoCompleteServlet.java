/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.ajax;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "AutoCompleteServlet", urlPatterns = {"/autocomplete"})
public class AutoCompleteServlet extends HttpServlet {
    private final ComposerData compData = new ComposerData();
    private final HashMap composers;

    public AutoCompleteServlet() {
        this.composers = compData.getComposers();
    }
 @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    String action = request.getParameter("action");
    String targetId = request.getParameter("id");
    StringBuilder sb = new StringBuilder();

    if (targetId != null) {
        targetId = targetId.trim().toLowerCase();
    } else {
        request.getRequestDispatcher("/error.jsp").forward(request, response);
        return;
    }

    boolean namesAdded = false;
    if ("complete".equals(action)) {
        if (!"".equals(targetId)) {
            Iterator it = composers.keySet().iterator();

            while (it.hasNext()) {
                String id = (String) it.next();
                Composer composer = (Composer) composers.get(id);

                if (composer.getFirstName().toLowerCase().startsWith(targetId) ||
                    composer.getLastName().toLowerCase().startsWith(targetId) ||
                    (composer.getFirstName() + " " + composer.getLastName()).toLowerCase().startsWith(targetId)) {

                    sb.append("<composer>");
                    sb.append("<id>").append(composer.getId()).append("</id>");
                    sb.append("<firstName>").append(composer.getFirstName()).append("</firstName>");
                    sb.append("<lastName>").append(composer.getLastName()).append("</lastName>");
                    sb.append("</composer>");
                    namesAdded = true;
                }
            }
        }

        if (namesAdded) {
            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache");
            response.getWriter().write("<composers>" + sb.toString() + "</composers>");
        } else {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }
    if ("lookup".equals(action)) {
        if (composers.containsKey(targetId)) {
            request.setAttribute("composer", composers.get(targetId));
            request.getRequestDispatcher("/composer.jsp").forward(request, response);
        }
    }
}
}
