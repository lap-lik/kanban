package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public final class CSVManager {
    private CSVManager() {
    }

    public static String historyToString(HistoryManager historyManager) {

        StringBuilder result = new StringBuilder();
        for (Task task : historyManager.getHistory()) {
            result.append(task.getId().toString()).append(",");
        }
        int index = result.lastIndexOf(",");
        if (index > -1) {
            result.deleteCharAt(index);
        }
        return result.toString();
    }

    public static List<Integer> historyFromString(String value) {
        ArrayList<Integer> result = new ArrayList<>();
        String[] temp = value.split(",");
        for (String str : temp) {
            result.add(Integer.parseInt(str));
        }
        return result;
    }
}
