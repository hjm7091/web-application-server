package dto;

import enums.HttpResponseStatus;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponse302 extends HttpResponse {

    private final String redirectUrl;

    public HttpResponse302(String redirectUrl) {
        super(HttpResponseStatus.REDIRECT);
        this.redirectUrl = redirectUrl;
    }

    @Override
    public void writeOn(DataOutputStream dos) throws IOException {
        super.writeOn(dos);
        dos.writeBytes("Location: " + this.redirectUrl + " \r\n");
    }
}
