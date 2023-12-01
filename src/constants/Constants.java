package constants;

import java.time.format.DateTimeFormatter;

public class Constants {
    public static final String FILE_PATH = "./src/resources/Kanban.csv";
    public static final DateTimeFormatter LOCAL_DATA_TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm(dd-MM-yyyy)");
    public static final String COLUMNS = "id,type,name,status,description,startTime,duration,epic";
    public static final int INTERVAL_MINUTES = 15;
}
