package spl.texteditor.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * A service that allows you to schedule the execution of tasks
 */
public class ScheduledTaskExecutorService implements TaskExecutorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTaskExecutorService.class);

    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    @Override
    public Future<?> executeTask(Task task) {
        if (task instanceof ScheduledTask) {
            ScheduledTask scheduledTask = (ScheduledTask) task;

            if (!scheduledTask.isRecurring()) {
                LOGGER.info("Scheduling non-recurring task");
                return executorService.schedule(scheduledTask.getTask(), 0L, TimeUnit.SECONDS);
            } else {
                throw new IllegalArgumentException("A recurring task was provided to a non recurring task call");
            }
        } else {
            throw new IllegalArgumentException("Provided task is not a scheduled task");
        }
    }

    @Override
    public Future<?> executeRecurringTask(Task task) {
        if (task instanceof ScheduledTask) {
            ScheduledTask scheduledTask = (ScheduledTask) task;

            if (scheduledTask.isRecurring()) {
                LOGGER.info("Scheduling recurring task with period of {} seconds", scheduledTask.getPeriod());
                return executorService.scheduleWithFixedDelay(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            scheduledTask.getTask().call();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, scheduledTask.getPeriod(), scheduledTask.getPeriod(), TimeUnit.SECONDS);
            } else {
                throw new IllegalArgumentException("A non recurring task was provided to a recurring task call");
            }
        } else {
            throw new IllegalArgumentException("Provided task is not a scheduled task");
        }
    }
}
