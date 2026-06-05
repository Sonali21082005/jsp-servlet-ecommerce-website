package com.ecommerce.control;

import com.ecommerce.dao.AccountDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;

@WebServlet(name = "RegisterControl", value = "/register")
@MultipartConfig
public class RegisterControl extends HttpServlet {

    AccountDao accountDao = new AccountDao();

    // HANDLE GET REQUEST
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("register.jsp")
               .forward(request, response);
    }

    // HANDLE POST REQUEST
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String repeatPassword = request.getParameter("repeat-password");

        Part part = request.getPart("profile-image");
        InputStream inputStream = null;

        if (part != null) {
            inputStream = part.getInputStream();
        }

        if (!password.equals(repeatPassword)) {

            String alert =
                    "<div class=\"alert alert-danger\">" +
                    "<p>Incorrect password!</p>" +
                    "</div>";

            request.setAttribute("alert", alert);
            request.getRequestDispatcher("register.jsp")
                    .forward(request, response);

        } else if (accountDao.checkUsernameExists(username)) {

            String alert =
                    "<div class=\"alert alert-danger\">" +
                    "<p>Username already exists!</p>" +
                    "</div>";

            request.setAttribute("alert", alert);
            request.getRequestDispatcher("register.jsp")
                    .forward(request, response);

        } else {

            accountDao.createAccount(
                    username,
                    password,
                    inputStream
            );

            String alert =
                    "<div class=\"alert alert-success\">" +
                    "<p>Account created successfully!</p>" +
                    "</div>";

            request.setAttribute("alert", alert);
            request.getRequestDispatcher("login.jsp")
                    .forward(request, response);
        }
    }
}