package dto;

import enums.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    protected static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    protected HttpResponseStatus status;
    protected Map<String, Object> header = new HashMap<>();

    public HttpResponse(HttpResponseStatus status) {
        this.status = status;
    }

    public HttpResponse(HttpResponseStatus status, Map<String, Object> header) {
        this(status);
        this.header = header;
    }

    public void writeOn(DataOutputStream dos) throws IOException {
        dos.writeBytes("HTTP/1.1 " + this.status + " \r\n");
        this.header.forEach((k, v) -> {
            try {
                dos.writeBytes(k + ": " + v + " \r\n");
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        });
    }

    public void flush(DataOutputStream dos) throws IOException {
        dos.flush();
    }

    public HttpResponse addHeader(String key, Object value) {
        this.header.put(key, value);
        return this;
    }

    public boolean removeHeader(String key) {
        return this.header.remove(key) != null;
    }

    public static HttpResponse helloWorld() {
        return new HttpResponse200("Hello World".getBytes())
                .addHeader("Content-Type", "text/html;charset=utf-8");
    }
}
