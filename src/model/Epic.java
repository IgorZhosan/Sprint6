package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Epic extends Task {
    private final Set<Integer> subTaskIds = new HashSet<>();

    private LocalDateTime endTime;
    private Duration duration;

    public Epic(String title, String desc) {
        super(title, desc, Status.NEW, TaskType.EPIC);
        this.duration = Duration.ZERO;
        this.setStartTime(null);
        this.setEndTime(null);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Epic(int id, String title, String desc) {
        super(id, title, desc, Status.NEW, TaskType.EPIC);
        this.duration = Duration.ZERO;
        this.setStartTime(null);
        this.setEndTime(null);
    }

    @Override
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void calculateEpicDurationAndTime(Set<SubTask> subTasks) {
        if (subTasks.isEmpty()) {
            this.duration = Duration.ZERO;
            this.setStartTime(null);
            this.setEndTime(null);
            return;
        }

        LocalDateTime earliestStart = subTasks.stream()
                .map(SubTask::getStartTime)
                .filter(startTime -> startTime != null)
                .min(LocalDateTime::compareTo)
                .orElse(null);

        LocalDateTime latestEnd = subTasks.stream()
                .map(subTask -> subTask.getStartTime().plus(subTask.getDuration()))
                .filter(endTime -> endTime != null)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        Duration calculatedDuration = (earliestStart != null && latestEnd != null)
                ? Duration.between(earliestStart, latestEnd)
                : Duration.ZERO;

        this.setStartTime(earliestStart);
        this.setEndTime(latestEnd);
        this.setDuration(calculatedDuration);
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public Set<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public void addingSubTasks(int id) {
        subTaskIds.add(id);
    }

    public void deleteAllSubTasks() {
        subTaskIds.clear();
    }

    public void deleteSubTaskById(int id) {
        subTaskIds.remove(id);
    }


    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subTaskIds=" + getSubTaskIds() +
                ", startTime=" + getStartTime() +
                ", endTime=" + getEndTime() +
                ", duration=" + getDuration() +
                '}';
    }
}
