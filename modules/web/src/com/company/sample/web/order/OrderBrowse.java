package com.company.sample.web.order;

import com.company.sample.service.DataGenerationService;
import com.company.sample.service.DataProcessingService;
import com.haulmont.cuba.gui.backgroundwork.BackgroundWorkWindow;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.executors.BackgroundTask;
import com.haulmont.cuba.gui.executors.TaskLifeCycle;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderBrowse extends AbstractLookup {

    @Inject
    private DataGenerationService dataGenerationService;

    @Inject
    private DataProcessingService dataProcessingService;

    @Inject
    private Logger log;

    public void onGenerateBtnClick() {
        BackgroundTask<Integer, String> task = new BackgroundTask<Integer, String>(600, this) {
            @Override
            public String run(TaskLifeCycle<Integer> taskLifeCycle) {
                long start = System.currentTimeMillis();

                dataGenerationService.generateData();

                return "Done.\nProcessing time, sec: " + ((System.currentTimeMillis() - start) / 1000.0);
            }

            @Override
            public void done(String resultMsg) {
                log.info(resultMsg);
                showMessageDialog("Result", resultMsg, MessageType.WARNING);
            }
        };
        BackgroundWorkWindow.show(task, "Generating data", "See app server log for details", false);
    }

    public void onProcess1BtnClick() {
        BackgroundTask<Integer, String> task = new BackgroundTask<Integer, String>(600, this) {
            @Override
            public String run(TaskLifeCycle<Integer> taskLifeCycle) {
                long start = System.currentTimeMillis();

                Map<String, BigDecimal> result = dataProcessingService.process1();

                return "Processing time, sec: " + ((System.currentTimeMillis() - start) / 1000.0) + "\n"
                        + result.entrySet().stream()
                            .map(entry -> entry.getKey() + ": " + entry.getValue())
                            .collect(Collectors.joining("\n"));
            }

            @Override
            public void done(String resultMsg) {
                log.info(resultMsg);
                showMessageDialog("Result", resultMsg, MessageType.WARNING);
            }
        };
        BackgroundWorkWindow.show(task, "Processing orders 1", "See app server log for details", false);
    }

    public void onProcess2BtnClick() {
        BackgroundTask<Integer, String> task = new BackgroundTask<Integer, String>(600, this) {
            @Override
            public String run(TaskLifeCycle<Integer> taskLifeCycle) {
                long start = System.currentTimeMillis();

                Map<String, BigDecimal> result = dataProcessingService.process2();

                return "Processing time, sec: " + ((System.currentTimeMillis() - start) / 1000.0) + "\n"
                        + result.entrySet().stream()
                            .map(entry -> entry.getKey() + ": " + entry.getValue())
                            .collect(Collectors.joining("\n"));
            }

            @Override
            public void done(String resultMsg) {
                log.info(resultMsg);
                showMessageDialog("Result", resultMsg, MessageType.WARNING);
            }
        };
        BackgroundWorkWindow.show(task, "Processing orders 2", "See app server log for details", false);
    }
}