package dto;

import enums.HttpResponseStatus;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponse200 extends HttpResponse {

    private byte[] body;

    public HttpResponse200() {
        super(HttpResponseStatus.OK);
    }

    public HttpResponse200(byte[] body) {
        this();
        this.body = body;
    }

    @Override
    public void writeOn(DataOutputStream dos) throws IOException {
        super.writeOn(dos);
        if (body != null && body.length > 0) {
            dos.writeBytes("Content-Length: " + this.body.length + " \r\n");
            dos.writeBytes("\r\n");
            dos.write(this.body, 0, this.body.length);
        }
    }
}
