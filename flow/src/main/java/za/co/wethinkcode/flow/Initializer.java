package za.co.wethinkcode.flow;

import java.io.*;
import java.nio.file.*;

import static za.co.wethinkcode.flow.FileHelpers.*;

public class Initializer {

    Path projectRoot;
    Path gitRoot;

    public Initializer(Path path) {
        projectRoot = path;
        gitRoot = requireGitRoot(projectRoot);

    }

    public Boolean shouldInitialize() {
        Path authorPath = gitRoot.resolve("author.txt");
        if (authorPath.toFile().exists()) return false;
        Path flowFolder = projectRoot.resolve(JLTK_FOLDER);
        if (flowFolder.toFile().exists()) return false;
        return true;
    }

    public void emitJunitFiles() throws IOException {
        projectRoot.resolve("src/test/resources/META-INF/services/").toFile().mkdirs();
        Path propertiesPath = projectRoot.resolve("src/test/resources/junit-platform.properties");
        BufferedWriter writer = Files.newBufferedWriter(propertiesPath);
        writer.write("junit.jupiter.extensions.autodetection.enabled=true\n");
        writer.flush();
        writer.close();
        Path metaPath = projectRoot.resolve("src/test/resources/META-INF/services/org.junit.jupiter.api.extension.Extension");
        writer = Files.newBufferedWriter(metaPath);
        writer.write("za.co.wethinkcode.flow.WtcJunitExtension\n");
        writer.flush();
        writer.close();
    }
}
