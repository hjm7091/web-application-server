package matcher;

import enums.HttpRequestMethod;

public class HttpRequestMethodMatcher {

    public static HttpRequestMethod match(String method) {
        if (method.equals("GET")) {
            return HttpRequestMethod.GET;
        } else if (method.equals("POST")) {
            return HttpRequestMethod.POST;
        }
        return HttpRequestMethod.UNKNOWN;
    }

}
