package spl.texteditor.tasks;

public abstract class Task {
    private Runnable task;

    public Task(Runnable task) {
        this.task = task;
    }

    public Runnable getTask() {
        return task;
    }
}
