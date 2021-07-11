package http.controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateUserController extends AbstractController {

    private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);

    @Override
    protected void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        createUser(httpRequest, httpResponse);
    }

    @Override
    protected void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        createUser(httpRequest, httpResponse);
    }

    private void createUser(HttpRequest httpRequest, HttpResponse httpResponse) {
        User user = new User(
                httpRequest.getParameter("userId"),
                httpRequest.getParameter("password"),
                httpRequest.getParameter("name"),
                httpRequest.getParameter("email"));
        log.info("user : {}", user);
        DataBase.addUser(user);
        httpResponse.sendRedirect("/index.html");
    }

}
