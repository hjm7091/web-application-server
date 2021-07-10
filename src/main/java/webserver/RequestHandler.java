package webserver;

import dto.*;
import enums.HttpRequestType;
import handler.FileRequestHandler;
import handler.UserRequestHandler;
import matcher.HttpRequestTypeMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final FileRequestHandler fileRequestHandler = new FileRequestHandler();

    private final UserRequestHandler userRequestHandler = new UserRequestHandler();

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpResponse httpResponse = processRequest(in);
            DataOutputStream dos = new DataOutputStream(out);
            httpResponse.writeOn(dos);
            httpResponse.flush(dos);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private HttpResponse processRequest(InputStream in) {
        try {

            HttpRequest httpRequest = new HttpRequest(in);

            String url = httpRequest.getUrl();

            HttpRequestType requestType = HttpRequestTypeMatcher.match(url);

            if (requestType == HttpRequestType.ROOT) {
                return HttpResponse.helloWorld();
            }

            if (requestType == HttpRequestType.FILE) {
                return fileRequestHandler.handle(httpRequest);
            }

            if (requestType == HttpRequestType.USER) {
                return userRequestHandler.handle(httpRequest);
            }

            return new HttpResponse404();
        } catch (Exception e) {
            String format = String.format("exception : %s, message : %s", e.getClass().getSimpleName(), e.getMessage());
            log.error(format);
            return new HttpResponse500(format);
        }
    }
}
