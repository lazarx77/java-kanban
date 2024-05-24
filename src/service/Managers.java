package service;

public final class Managers {

    private static TaskManager taskManager;
    private static HistoryManager historyManager = new InMemoryHistoryManager();
    private static FileBackedTaskManager fileManager = new FileBackedTaskManager();

    public static TaskManager getDefault() {
        taskManager = new InMemoryTaskManager();
        historyManager.clearHistory();
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return historyManager;
    }

    public static FileBackedTaskManager getDefaultFileManager() {
        return fileManager;
    }

}
