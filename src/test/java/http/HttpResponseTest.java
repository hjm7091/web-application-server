package http;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class HttpResponseTest {

    @Test
    public void responseForward() throws Exception {
        // Http_Forward.txt 결과는 응답 body 에 index.html 이 포함되어 있어야 한다.
        HttpResponse httpResponse = new HttpResponse(createOutputStream("Http_Forward.txt"));
        httpResponse.forward("/index.html");
    }

    @Test
    public void responseRedirect() throws Exception {
        // Http_Redirect.txt 결과는 응답 header 에
        // Location 정보가 /index.html 로 포함되어 있어야 한다.
        HttpResponse httpResponse = new HttpResponse(createOutputStream("Http_Redirect.txt"));
        httpResponse.sendRedirect("/index.html");
    }

    @Test
    public void responseCookies() throws Exception {
        // Http_Cookie.txt 결과는 응답 header 에
        // Set-Cookie 값으로 logined=true 값이 포함되어 있어야 한다.
        HttpResponse httpResponse = new HttpResponse(createOutputStream("Http_Cookie.txt"));
        httpResponse.addHeader("Set-Cookie", "logined=true");
        httpResponse.sendRedirect("/index.html");
    }

    private OutputStream createOutputStream(String filename) throws FileNotFoundException {
        String testDirectory = "./src/test/resources/";
        return new FileOutputStream(new File(testDirectory + filename));
    }
}
