package service;

public final class Managers {

    public static TaskManager taskManager = new InMemoryTaskManager();

    public static HistoryManager historyManager = new InMemoryHistoryManager();

    public static TaskManager getDefault() {
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return historyManager;
    }

}
