package spl.texteditor.tasks;

import java.util.concurrent.Callable;

public class ScheduledTask<T> extends Task<T> {
    private final int period;
    private final boolean recurring;

    public ScheduledTask(Callable<T> task, int period, boolean recurring) {
        super(task);

        if (period != 0 && !recurring) {
            throw new IllegalArgumentException("Can not provide period with a task that is not recurring");
        }

        if (period < 0) {
            throw new IllegalArgumentException("Period can not be a negative number");
        }

        this.period = period;
        this.recurring = recurring;
    }

    public int getPeriod() {
        return period;
    }

    public boolean isRecurring() {
        return recurring;
    }
}