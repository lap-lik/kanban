package model;

public class Subtask extends Task {
    private Integer epicId;
    public Subtask(String name, String text, Integer epicId) {
        super(name, text);
        this.epicId = epicId;
    }
    public Integer getEpicId() {
        return epicId;
    }
    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", text='" + text + '\'' +
                ", status='" + status + '\'' +
                ", epicId=" + epicId +
                '}';
    }
}
