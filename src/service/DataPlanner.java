package service;

import exception.DataPlannerException;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static constants.Constants.INTERVAL_MINUTES;
import static constants.Constants.LOCAL_DATA_TIME_FORMAT;

public class DataPlanner {
    private final Map<LocalDateTime, Boolean> intervalGrid;
    private final LocalDateTime startYear;
    private final LocalDateTime endYear;

    public DataPlanner() {
        this.intervalGrid = new HashMap<>();
        this.startYear = LocalDateTime.now();
        this.endYear = startYear.plusYears(1);
    }

    public boolean fillCells(Task task) {
        LocalDateTime startTime = task.getStartTime();
        if (startTime == null) {
            return true;
        }
        int minutes = checkMinutes(startTime.getMinute());
        if (minutes == 60) {
            startTime = startTime.plusHours(1);
            startTime = startTime.withMinute(0);
        } else {
            startTime = startTime.withMinute(minutes);
        }
        int duration = checkMinutes((int) task.getDuration().toMinutes());
        LocalDateTime endTime = startTime.plus(Duration.ofMinutes(duration));

        try {
            isWithinNextYear(startTime, endTime);
            isCellsFree(startTime, endTime);
            while (startTime.isBefore(endTime)) {
                intervalGrid.put(startTime, false);
                startTime = startTime.plusMinutes(INTERVAL_MINUTES);
            }
        } catch (DataPlannerException e) {
            System.err.println("Ошибка: " + task.getName() + " - " + e.getMessage());
            return false;
        }
        return true;
    }

    public void clearCells(Task task) {
        LocalDateTime startTime = task.getStartTime();
        int minutes = checkMinutes(startTime.getMinute());
        startTime = startTime.withMinute(minutes);
        int duration = checkMinutes((int) task.getDuration().toMinutes());
        LocalDateTime endTime = startTime.plus(Duration.ofMinutes(duration));

        while (startTime.isBefore(endTime)) {
            intervalGrid.remove(startTime);
            startTime = startTime.plusMinutes(INTERVAL_MINUTES);
        }
    }

    private Integer checkMinutes(Integer minutes) {
        int div = minutes % INTERVAL_MINUTES;
        if (div != 0) {
            div = INTERVAL_MINUTES - div;
        }
        return minutes + div;
    }

    private void isWithinNextYear(LocalDateTime startTime, LocalDateTime endTime) {
        if (startYear.isAfter(startTime) || endYear.isBefore(endTime)) {
            throw new DataPlannerException("Задача не добавлена, планирование возможно только на год вперед.");
        }
    }

    private void isCellsFree(LocalDateTime startTime, LocalDateTime endTime) {
        while (startTime.isBefore(endTime)) {
            if (intervalGrid.containsKey(startTime)) {
                throw new DataPlannerException("Задача не добавлена, время " +
                        startTime.format(LOCAL_DATA_TIME_FORMAT) + " занято.");
            }
            startTime = startTime.plusMinutes(INTERVAL_MINUTES);
        }
    }

    public Map<LocalDateTime, Boolean> getIntervalGrid() {
        return new HashMap<>(intervalGrid);
    }
}
