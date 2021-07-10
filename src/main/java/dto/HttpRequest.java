package dto;

import com.google.common.collect.Maps;
import enums.HttpRequestMethod;
import matcher.HttpRequestMethodMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private HttpRequestMethod method = null;
    private String url = null;
    private Map<String, String> headers = Maps.newHashMap();
    private String body = null;

    public HttpRequest(InputStream in) throws Exception {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

        try {
            String line = bufferedReader.readLine();

//            log.info("readLine : {}", line);

            String[] tokens = line.split(" ");

            String method = tokens[0], url = tokens[1];

            this.method = HttpRequestMethodMatcher.match(method);
            this.url = url;

            while (!(line = bufferedReader.readLine()).equals("")) {
//                log.info("readLine : {}", line);
                line = line.replaceAll(" ", "");
                tokens = line.split(":");
                this.headers.put(tokens[0], tokens[1]);
            }

            String contentLength = headers.get("Content-Length");
            if (contentLength != null) {
                // log.info("content : {}", content);
                this.body = IOUtils.readData(bufferedReader, Integer.parseInt(contentLength));
            }

        } catch (Exception e) {
            log.error("exception : {}, message : {}", e.getClass().getSimpleName(), e.getMessage());
            throw e;
        }

    }

    public HttpRequest(String url) {
        this.url = url;
    }

    public HttpRequestMethod getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

}
