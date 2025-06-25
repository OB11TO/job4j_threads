package ru.job4j.concurrent.blocking.cyclibarrier;

import java.util.List;

public abstract class CompositeTask<T extends Task> extends Task {

    private final List<T> subTasks;

    public CompositeTask(long id, List<T> tasks) {
        super(id);
        this.subTasks = tasks;
    }

    @Override
    public void perform() {
        this.subTasks.forEach(this::perform);
    }

    protected abstract void perform(final T subtask);
}
