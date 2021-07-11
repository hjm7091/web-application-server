package http.controller;

import enums.HttpMethod;
import http.HttpRequest;
import http.HttpResponse;

public abstract class AbstractController implements Controller {

    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) {
        HttpMethod method = httpRequest.getMethod();
        if (method.isPost()) {
            doPost(httpRequest, httpResponse);
        } else {
            doGet(httpRequest, httpResponse);
        }
    }

    protected abstract void doGet(HttpRequest httpRequest, HttpResponse httpResponse);

    protected abstract void doPost(HttpRequest httpRequest, HttpResponse httpResponse);
}
