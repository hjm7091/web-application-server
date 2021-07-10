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

    public void forward(String path) throws IOException {
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
    }

    public void forwardBody(String body) throws IOException {
        byte[] contents = body.getBytes(StandardCharsets.UTF_8);
        addHeader("Content-Type", "text/html;charset=utf-8");
        addHeader("Content-Length", String.valueOf(contents.length));
        response200Header(contents.length);
        responseBody(contents);
    }

    private void response200Header(int length) throws IOException {
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        processHeaders();
        dos.writeBytes("\r\n");
    }

    private void response404Header() throws IOException {
        dos.writeBytes("HTTP/1.1 404 Not Found \r\n");
    }

    private void responseBody(byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.writeBytes("\r\n");
        dos.flush();
    }

    public void sendRedirect(String redirectUrl) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Found \r\n");
        processHeaders();
        dos.writeBytes("Location: " + redirectUrl + " \r\n");
        dos.writeBytes("\r\n");
    }

    private void processHeaders() throws IOException {
        for(Map.Entry<String, String> entry : headers.entrySet()) {
            dos.writeBytes(entry.getKey() + ": " + entry.getValue() + " \r\n");
        }
    }

    public HttpResponse addHeader(String key, String value) {
        this.headers.put(key, value);
        return this;
    }
}
