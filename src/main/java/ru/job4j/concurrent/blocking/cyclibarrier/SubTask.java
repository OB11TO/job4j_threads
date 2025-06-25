package ru.job4j.concurrent.blocking.cyclibarrier;

import java.util.List;

public class SubTask extends CompositeTask<LeafTask> {

    public SubTask(long id, List<LeafTask> tasks) {
        super(id, tasks);
    }

    @Override
    protected void perform(LeafTask leafTask) {
        leafTask.perform();
    }
}

