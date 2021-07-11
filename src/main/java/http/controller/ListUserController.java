package http.controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;

import java.util.Collection;

public class ListUserController extends AbstractController {

    @Override
    protected void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        listUser(httpRequest, httpResponse);
    }

    @Override
    protected void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        listUser(httpRequest, httpResponse);
    }

    private void listUser(HttpRequest httpRequest, HttpResponse httpResponse) {
        if (!httpRequest.isLogin()) {
            httpResponse.sendRedirect("/user/login.html");
            return;
        }

        Collection<User> users = DataBase.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("<table border='1'>");
        for (User user : users) {
            sb.append("<tr>");
            sb.append("<td>").append(user.getUserId()).append("</td>");
            sb.append("<td>").append(user.getName()).append("</td>");
            sb.append("<td>").append(user.getEmail()).append("</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");
        httpResponse.forwardBody(sb.toString());
    }

}
