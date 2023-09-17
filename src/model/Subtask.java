package model;

public class Subtask extends Task {
    private Integer epicId;
    public Subtask(String title, String text, Integer epicId) {
        super(title, text);
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
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", text='" + getText() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", epicId=" + epicId +
                '}';
    }
}
