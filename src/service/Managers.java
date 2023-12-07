package service;


import java.io.File;

import static constants.Constants.*;

public final class Managers {
    private Managers() {
    }

    public static TaskManager getDefaultFileBackedTasksManager() {
        return FileBackedTasksManager.loadFromFile(new File(FILE_PATH));
    }

    public static HttpTaskManager getDefault() {
        return new HttpTaskManager(URL_KV_SERVER + PORT_KV_SERVER);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
