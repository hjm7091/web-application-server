package handler;

import dto.HttpRequest;
import dto.HttpResponse;

@FunctionalInterface
public interface Handler {

    HttpResponse handle(HttpRequest httpRequest) throws Exception;

}
