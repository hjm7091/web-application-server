package handler;

import dto.HttpRequest;
import dto.HttpResponse;
import dto.HttpResponse200;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileRequestHandler implements Handler {

    private static final Logger log = LoggerFactory.getLogger(FileRequestHandler.class);

    @Override
    public HttpResponse handle(HttpRequest httpRequest) throws IOException {

        log.info("request method : {}, request url : {}", httpRequest.getMethod(), httpRequest.getUrl());

        String url = httpRequest.getUrl();

        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
        String contentType = url.substring(url.lastIndexOf(".") + 1);

        log.info("url : {}, body.length : {}, contentType : {}", url, body.length, contentType);

        return new HttpResponse200(body).addHeader("Content-Type", "text/" + contentType + ";charset=utf-8");
    }

}
