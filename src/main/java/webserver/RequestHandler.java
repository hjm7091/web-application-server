package webserver;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collection;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = new HttpRequest(in);
            HttpResponse httpResponse = new HttpResponse(out);
            String path = getDefaultPath(httpRequest.getPath());

            switch (path) {
                case "/user/create": {
                    User user = new User(
                            httpRequest.getParameter("userId"),
                            httpRequest.getParameter("password"),
                            httpRequest.getParameter("name"),
                            httpRequest.getParameter("email"));
                    log.info("user : {}", user);
                    DataBase.addUser(user);
                    httpResponse.sendRedirect("/index.html");
                    break;
                }
                case "/user/login": {
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
                    break;
                }
                case "/user/list":
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
                    break;
                default:
                    httpResponse.forward(path);
                    break;
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String getDefaultPath(String path) {
        if (path.equals("/")) {
            return "/index.html";
        }
        return path;
    }
}
