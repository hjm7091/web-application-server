package http;

import com.google.common.collect.Maps;
import enums.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private final Map<String, String> headers = Maps.newHashMap();
    private Map<String, String> parameters;
    private RequestLine requestLine;

    public HttpRequest(InputStream in) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String line = bufferedReader.readLine();

            if (line == null) return;

            requestLine = new RequestLine(line);

            while (!(line = bufferedReader.readLine().replaceAll(" ", "")).equals("")) {
                String[] tokens = line.split(":");
                headers.put(tokens[0].trim(), tokens[1].trim());
            }

            if (requestLine.getMethod().isPost()) {
                String contentLength = headers.get("Content-Length");
                if (contentLength != null) {
                    String body = IOUtils.readData(bufferedReader, Integer.parseInt(contentLength));
                    parameters = HttpRequestUtils.parseQueryString(body);
                }
            } else {
                parameters = requestLine.getParameters();
            }

        } catch (Exception e) {
            log.error("exception : {}, message : {}", e.getClass().getSimpleName(), e.getMessage());
        }
    }

    public HttpMethod getMethod() {
        return requestLine.getMethod();
    }

    public String getPath() {
        return requestLine.getPath();
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getHeader(String key) { return headers.get(key); }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public String getParameter(String key) { return parameters.get(key); }

    public boolean isLogin() {
        Map<String, String> cookies = HttpRequestUtils.parseCookies(getHeader("Cookie"));
        String value = cookies.get("logined");
        if (value == null) {
            return false;
        }
        return Boolean.parseBoolean(value);
    }
}
