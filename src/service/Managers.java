package service;


import java.io.File;

import static constants.Constants.FILE_PATH;

public final class Managers {
    private Managers() {
    }

    public static TaskManager getDefault() {
        return FileBackedTasksManager.loadFromFile(new File(FILE_PATH));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
