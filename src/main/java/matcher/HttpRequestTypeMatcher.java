package matcher;

import enums.HttpRequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HttpRequestTypeMatcher {

    private static final Logger log = LoggerFactory.getLogger(HttpRequestTypeMatcher.class);

    private static Set<String> contentTypes;

    static {
        try (Stream<Path> paths = Files.walk(Paths.get("./webapp"))) {
            contentTypes = paths
                            .filter(Files::isRegularFile)
                            .map(Path::toFile)
                            .map(file -> {
                                String fileName = file.getName();
                                return fileName.substring(fileName.lastIndexOf(".") + 1);
                            }).collect(Collectors.toSet());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static HttpRequestType match(String url) {
        if (url.equals("/")) {
            return HttpRequestType.ROOT;
        } else if (isFileRequest(url) && existInFileSystem(url)) {
            return HttpRequestType.FILE;
        } else if (url.contains("/user")) {
            return HttpRequestType.USER;
        }
        return HttpRequestType.UNKNOWN;
    }

    private static boolean isFileRequest(String url) {
        return url.contains(".");
    }

    private static boolean existInFileSystem(String url) {
        String contentType = url.substring(url.lastIndexOf(".") + 1);
        return contentTypes.contains(contentType);
    }
}
