package spl.texteditor;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import org.fxmisc.richtext.CodeArea;
import spl.texteditor.storage.LocalFileSystemReadWriteService;
import spl.texteditor.storage.ReadWriteService;
import spl.texteditor.tasks.AutosaveTask;
import spl.texteditor.tasks.ScheduledTask;
import spl.texteditor.tasks.ScheduledTaskExecutorService;
import spl.texteditor.tasks.TaskExecutorService;

import java.util.*;
import java.util.concurrent.Future;

public class MultiTabTextAreaManager {
    private AnchorPane anchorPane;
    private Map<CodeArea, ReadWriteService> codeAreaWithReadWriteServices = new HashMap();
    private Map<CodeArea, Tab> codeAreaWithTab = new HashMap();
    private Map<CodeArea, Future<?>> codeAreaAutoSavingTask = new HashMap();
    private TaskExecutorService taskExecutorService = new ScheduledTaskExecutorService();
    private CodeArea currentActiveCodeArea;
    private TabPane tabPane;

    public MultiTabTextAreaManager(TabPane tabPane) {
        this.tabPane = tabPane;
    }

    public void addTab(String title) {
        // Add first tab
        ReadWriteService readWriteService = new LocalFileSystemReadWriteService();
        CodeArea codeArea = new CodeArea();
        currentActiveCodeArea = codeArea;
        codeAreaWithReadWriteServices.put(currentActiveCodeArea, readWriteService);
        Tab tab = new Tab(title, currentActiveCodeArea);
        codeAreaWithTab.put(currentActiveCodeArea, tab);
        Future<?> future = taskExecutorService.executeRecurringTask(new ScheduledTask(
                new AutosaveTask(readWriteService, new ContentProvider() {
                    private String lastRequestedText;

                    @Override
                    public String getText() {
                        lastRequestedText = codeArea.getText();

                        return codeArea.getText();
                    }

                    @Override
                    public boolean isDirty() {
                        return !Objects.equals(lastRequestedText, codeArea.getText());
                    }
                }),
                5,
                true
        ));
        codeAreaAutoSavingTask.put(currentActiveCodeArea, future);
        tab.setClosable(false);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                        // Set textArea to current tab area
                        currentActiveCodeArea = (CodeArea) tabPane.getSelectionModel().getSelectedItem().getContent();
                    }
                }
        );

    }

    public void removeCurrentTab() {
//        codeAreaAutoSavingTask.get(currentActiveCodeArea).cancel(true);
//        codeAreaAutoSavingTask.remove(currentActiveCodeArea);
//        code
    }

    public CodeAreaWithTabAndReadWriteService getCurrentActiveCodeArea() {
        return new CodeAreaWithTabAndReadWriteService(currentActiveCodeArea, codeAreaWithReadWriteServices.get(currentActiveCodeArea), codeAreaWithTab.get(currentActiveCodeArea));
    }

}
