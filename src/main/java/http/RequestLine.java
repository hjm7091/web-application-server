package http;

import enums.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.util.HashMap;
import java.util.Map;

public class RequestLine {

    private static final Logger log = LoggerFactory.getLogger(RequestLine.class);
    private final HttpMethod method;
    private final String path;
    private Map<String, String> parameters = new HashMap<>();

    public RequestLine(String line) {
        String[] tokens = line.trim().split(" ");
        method = HttpMethod.valueOf(tokens[0]);

        if (method.isPost()) {
            path = tokens[1];
            return;
        }

        int index = tokens[1].indexOf("?");
        if (index == -1) {
            path = tokens[1];
        } else {
            path = tokens[1].substring(0, index);
            parameters = HttpRequestUtils.parseQueryString(tokens[1].substring(index + 1));
        }
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
}
