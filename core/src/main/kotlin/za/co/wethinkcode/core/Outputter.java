package za.co.wethinkcode.core;

import java.nio.file.*;

public interface Outputter {

    void add(Message message);

    void saveResults(Path results);

    void saveGrade(Path results);
}
