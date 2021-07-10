package handler;

import db.DataBase;
import dto.*;
import enums.HttpRequestMethod;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.IOException;
import java.util.Map;

public class UserRequestHandler implements Handler {

    private static final Logger log = LoggerFactory.getLogger(UserRequestHandler.class);

    private final FileRequestHandler fileRequestHandler = new FileRequestHandler();

    @Override
    public HttpResponse handle(HttpRequest httpRequest) throws Exception {

        HttpRequestMethod method = httpRequest.getMethod();
        String url = httpRequest.getUrl();

        log.info("request method : {}, request url : {}", method, url);

        if (url.contains("create")) {
            return handleCreateRequest(httpRequest);
        } else if (url.contains("login")) {
            return handleLoginRequest(httpRequest);
        } else if (url.contains("list")) {
            return handleListRequest(httpRequest);
        }

        return new HttpResponse404();
    }

    private HttpResponse handleListRequest(HttpRequest httpRequest) throws IOException {
        String cookie = httpRequest.getHeaders().get("Cookie");
        Map<String, String> cookieMap = HttpRequestUtils.parseCookies(cookie);

        if (cookieMap == null || !cookieMap.containsKey("logined")) {
            return new HttpResponse302("/user/login.html");
        }

        boolean logined = Boolean.parseBoolean(cookieMap.get("logined"));
        if (!logined) {
            return new HttpResponse302("/user/login.html");
        }

        return fileRequestHandler.handle(new HttpRequest("/user/list.html"));
    }

    private HttpResponse handleLoginRequest(HttpRequest httpRequest) throws IOException {

        Map<String, String> paramsMap = HttpRequestUtils.parseQueryString(httpRequest.getBody());

        if (paramsMap != null) {
            String userId = paramsMap.get("userId");
            String password = paramsMap.get("password");

            User userById = DataBase.findUserById(userId);

            if (userById == null || !userById.getPassword().equals(password)) {
                return new HttpResponse302("/user/login_failed.html")
                        .addHeader("Set-Cookie", "logined=false");
            } else {
                return new HttpResponse302("/index.html")
                        .addHeader("Set-Cookie", "logined=true");
            }
        }

        return new HttpResponse404();
    }

    private HttpResponse handleCreateRequest(HttpRequest httpRequest) {

        HttpRequestMethod method = httpRequest.getMethod();
        String url = httpRequest.getUrl();

        Map<String, String> paramsMap = null;

        if (method == HttpRequestMethod.GET) {

            int index = url.indexOf("?");
            String requestPath = url.substring(0, index);
            String params = url.substring(index + 1);
            paramsMap = HttpRequestUtils.parseQueryString(params);

        } else if (method == HttpRequestMethod.POST) {

            paramsMap = HttpRequestUtils.parseQueryString(httpRequest.getBody());

        }

        if (paramsMap != null) {
            String userId = paramsMap.get("userId");
            String password = paramsMap.get("password");
            String name = paramsMap.get("name");
            String email = paramsMap.get("email");

            User userById = DataBase.findUserById(userId);

            if (userById == null) {
                User user = new User(userId, password, name, email);
                log.info("user : {}", user);
                DataBase.addUser(user);
                return new HttpResponse302("/index.html");
            } else {
                return new HttpResponse500("동일한 아이디가 이미 존재합니다.")
                        .addHeader("Content-Type", "text/html;charset=utf-8");
            }
        }

        return new HttpResponse404();
    }
}
