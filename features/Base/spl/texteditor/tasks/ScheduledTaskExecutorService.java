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
        	ScheduledTask scheduledTask = (ScheduledTask)task;

            if (scheduledTask.isRecurring()) {
                LOGGER.info("Scheduling recurring task with period of {} seconds", scheduledTask.getPeriod());
                return executorService.schedule(scheduledTask.getTask(), scheduledTask.getPeriod(), TimeUnit.SECONDS);
            } else {
                LOGGER.info("Scheduling non-recurring task");
                return executorService.schedule(scheduledTask.getTask(), 0L, TimeUnit.SECONDS);
            }
        } else {
            throw new IllegalArgumentException("Provided task is not a scheduled task");
        }
    }
}
