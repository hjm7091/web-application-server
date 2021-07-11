package http.controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;

public class LoginController extends AbstractController {

    @Override
    protected void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        login(httpRequest, httpResponse);
    }

    @Override
    protected void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        login(httpRequest, httpResponse);
    }

    private void login(HttpRequest httpRequest, HttpResponse httpResponse) {
        User user = DataBase.findUserById(httpRequest.getParameter("userId"));
        if (user != null) {
            if (user.login(httpRequest.getParameter("password"))) {
                httpResponse.addHeader("Set-Cookie", "logined=true; Path=/");
                httpResponse.sendRedirect("/index.html");
            } else {
                httpResponse.sendRedirect("/user/login_failed.html");
            }
        } else {
            httpResponse.sendRedirect("/user/login_failed.html");
        }
    }

}
