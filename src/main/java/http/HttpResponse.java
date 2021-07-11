package http;

import enums.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.WebAppDirectory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    protected static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    protected HttpResponseStatus status;
    protected Map<String, String> headers = new HashMap<>();
    protected DataOutputStream dos;

    public HttpResponse(OutputStream os) {
        this.dos = new DataOutputStream(os);
    }

    public void forward(String path) {
        try {
            String filename = path.substring(path.lastIndexOf("/") + 1);
            if (WebAppDirectory.contains(filename)) {
                if (filename.endsWith(".css")) {
                    addHeader("Content-Type", "text/css");
                } else if (filename.endsWith(".js")) {
                    addHeader("Content-Type", "application/javascript");
                } else {
                    addHeader("Content-Type", "text/html;charset=utf-8");
                }
                byte[] body = WebAppDirectory.read(path);
                addHeader("Content-Length", String.valueOf(body.length));
                response200Header(body.length);
                responseBody(body);
            } else {
                response404Header();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void forwardBody(String body) {
        byte[] contents = body.getBytes(StandardCharsets.UTF_8);
        addHeader("Content-Type", "text/html;charset=utf-8");
        addHeader("Content-Length", String.valueOf(contents.length));
        response200Header(contents.length);
        responseBody(contents);
    }

    private void response200Header(int length) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            processHeaders();
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response404Header() {
        try {
            dos.writeBytes("HTTP/1.1 404 Not Found \r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void sendRedirect(String redirectUrl) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            processHeaders();
            dos.writeBytes("Location: " + redirectUrl + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void processHeaders() {
        try {
            for(Map.Entry<String, String> entry : headers.entrySet()) {
                dos.writeBytes(entry.getKey() + ": " + entry.getValue() + " \r\n");
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public HttpResponse addHeader(String key, String value) {
        this.headers.put(key, value);
        return this;
    }
}
