package spl.texteditor.tasks; 

import java.util.concurrent.Future; 

/**
 * A service that allows you to execute tasks
 */
public  interface  TaskExecutorService {
	
    /**
     * Execute the provided task
     * @param task the task you want to be executed
     */
    Future<?> executeTask(Task task);

	

    /**
     * Execute a task that is recurring
     * @param task
     * @return
     */
    Future<?> executeRecurringTask(Task task);


}
