package service;

public final class Managers {

    private static TaskManager taskManager;
    private static HistoryManager historyManager = new InMemoryHistoryManager();

    public static TaskManager getDefault() {
        taskManager = new InMemoryTaskManager();
        //historyManager.clearHistory();
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return historyManager;
    }

}
