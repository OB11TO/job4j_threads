package ru.job4j.concurrent.blocking.cyclibarrier;

import java.util.List;

public class MainTask extends CompositeTask<SubTask> {

    public MainTask(long id, List<SubTask> tasks) {
        super(id, tasks);
    }

    @Override
    protected void perform(SubTask subtask) {
        new Thread(subtask::perform).start();
    }
}
