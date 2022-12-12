package spl.texteditor.plugin.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spl.texteditor.plugin.core.pf4j.Pf4JPluginManager;
import spl.texteditor.tasks.ScheduledTask;
import spl.texteditor.tasks.ScheduledTaskExecutorService;
import spl.texteditor.tasks.TaskExecutorService;
/**
 * This class allows you to bootstrap the plugin system
 */
public class PluginSystemBootstrapper {
    private Logger LOGGER = LoggerFactory.getLogger(PluginSystemBootstrapper.class);
    private TaskExecutorService scheduledTaskExecutorService = new ScheduledTaskExecutorService();
    private PluginManager pluginManager;
    private boolean started = false;

    public PluginSystemBootstrapper() {
        this.pluginManager = Pf4JPluginManager.getInstance();
    }

    public void start() {
        if (started) {
            throw new IllegalStateException("Can not start the plugin system as it's already running.");
        }

        LOGGER.info("Plugin system starting up...");
        scheduledTaskExecutorService.executeTask(new ScheduledTask(new DiscoverAndLoadPluginTask(pluginManager, "plugins"), 10, true));
        started = true;
        LOGGER.info("Plugin system successfully started!");
    }
}
