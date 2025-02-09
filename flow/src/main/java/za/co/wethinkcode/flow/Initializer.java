package za.co.wethinkcode.flow;

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
}
