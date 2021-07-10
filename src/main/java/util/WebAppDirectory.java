package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WebAppDirectory {

    private static final Logger log = LoggerFactory.getLogger(WebAppDirectory.class);

    private static Set<String> files;

    static {
        try (Stream<Path> paths = Files.walk(Paths.get("./webapp"))) {
            files = paths
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .map(File::getName).collect(Collectors.toSet());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static boolean contains(String filename) {
        return files.contains(filename);
    }

    public static byte[] read(String path) throws IOException {
        return Files.readAllBytes(new File(("./webapp" + path)).toPath());
    }

}
