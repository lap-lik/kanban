package constants;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import utils.DurationTimeAdapter;
import utils.LocalDateTimeAdapter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Constants {
    public static final String FILE_PATH = "./src/resources/Kanban.csv";
    public static final DateTimeFormatter LOCAL_DATA_TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm(dd-MM-yyyy)");

    public static final String COLUMNS = "id,type,name,status,description,startTime,duration,epic";
    public static final int INTERVAL_MINUTES = 15;
    public static final String URL_KV_SERVER = "http://localhost:";
    public static final String URL_CLIENT = "http://localhost:";
    public static final int PORT_CLIENT = 8080;
    public static final int PORT_KV_SERVER = 8078;
    public static GsonBuilder gsonBuilder = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationTimeAdapter())
            .setPrettyPrinting();
    public static final Gson GSON = gsonBuilder.create();

}
