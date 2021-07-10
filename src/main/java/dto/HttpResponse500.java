package dto;

import enums.HttpResponseStatus;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponse500 extends HttpResponse {

    private String message;

    public HttpResponse500() {
        super(HttpResponseStatus.SERVER_ERROR);
    }

    public HttpResponse500(String message) {
        this();
        this.message = message;
    }

    @Override
    public void writeOn(DataOutputStream dos) throws IOException {
        super.writeOn(dos);
        if (this.message != null) {
            dos.writeBytes("\r\n");
            byte[] body = this.message.getBytes();
            dos.write(body, 0, body.length);
        }
    }

}
