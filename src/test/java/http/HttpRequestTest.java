package http;

import enums.HttpMethod;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class HttpRequestTest {

    private final String testDirectory = "./src/test/resources/";

    @Test
    public void request_GET() throws Exception {
        InputStream in = new FileInputStream(new File(testDirectory + "Http_GET.txt"));
        HttpRequest httpRequest = new HttpRequest(in);

        assertEquals(HttpMethod.GET, httpRequest.getMethod());
        assertEquals("/user/create", httpRequest.getPath());
        assertEquals("keep-alive", httpRequest.getHeader("Connection"));
        assertEquals("javajigi", httpRequest.getParameter("userId"));
    }

    @Test
    public void request_POST() throws Exception {
        InputStream in = new FileInputStream(new File(testDirectory + "Http_POST.txt"));
        HttpRequest httpRequest = new HttpRequest(in);

        assertEquals(HttpMethod.POST, httpRequest.getMethod());
        assertEquals("/user/create", httpRequest.getPath());
        assertEquals("keep-alive", httpRequest.getHeader("Connection"));
        assertEquals("javajigi", httpRequest.getParameter("userId"));
    }

}
