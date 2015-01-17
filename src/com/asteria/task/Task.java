package com.asteria.task;

import java.util.Objects;

import com.google.common.base.Preconditions;

/**
 * The various services that are executed on the main game thread.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public abstract class Task {

    /**
     * The default attachment key for all tasks.
     */
    public static final Object DEFAULT_KEY = new Object();

    /**
     * The delay for this task.
     */
    private int delay;

    /**
     * The counter used for determining when this task executes.
     */
    private int counter;

    /**
     * Determines if this task executes when submitted.
     */
    private final boolean instant;

    /**
     * The attachment key that will be used by this task.
     */
    private Object key;

    /**
     * Determines if this task is running.
     */
    private boolean running;

    /**
     * Creates a new {@link Task}.
     * 
     * @param delay
     *            the delay for this task.
     * @param instant
     *            if this task executes when submitted.
     */
    public Task(int delay, boolean instant) {
        Preconditions.checkArgument(delay >= 0);
        this.delay = delay;
        this.instant = instant;
        this.running = true;
        attach(DEFAULT_KEY);
    }

    /**
     * The code that will be executed by this task.
     */
    public abstract void execute();

    /**
     * The method executed when this task is submitted to the task manager.
     */
    public void onSubmit() {

    }

    /**
     * The method executed when this task is cancelled using
     * {@link Task#cancel()}. This may be overridden so more code can be
     * executed dynamically when a task is cancelled.
     */
    public void onCancel() {

    }

    /**
     * The method executed when {@link Task#execute()} throws an error. This may
     * be overridden so more code can be executed dynamically when a task is
     * cancelled.
     * 
     * @param t
     *            the error thrown by execution of the task.
     */
    public void onThrowable(Throwable t) {

    }

    /**
     * Determines if this task needs to be executed.
     * 
     * @return {@code true} if this task needs to be executed, {@code false}
     *         otherwise.
     */
    public final boolean needsExecute() {
        if (++counter >= delay && running) {
            counter = 0;
            return true;
        }
        return false;
    }

    /**
     * Cancels this task and executes the {@link Task#onCancel()} method only if
     * this task is running.
     */
    public final void cancel() {
        if (running) {
            running = false;
            onCancel();
        }
    }

    /**
     * Attaches {@code key} to this task that can later be retrieved with
     * {@link Task#getKey()}. This is a very useful feature because similar or
     * related tasks can be bound with the same key, and then be retrieved or
     * cancelled later on. All player related tasks should be bound with the
     * player's instance so all tasks are automatically stopped on logout.
     * Please note that keys with a value of {@code null} are not permitted, the
     * default value for all keys is {@link Task#DEFAULT_KEY}.
     * 
     * @param key
     *            the key to bind to this task, cannot be {@code null}.
     * @return an instance of this task.
     */
    public final Task attach(Object key) {
        this.key = Objects.requireNonNull(key);
        return this;
    }

    /**
     * Sets the new delay for this task in a dynamic fashion.
     * 
     * @param delay
     *            the new delay to set for this task.
     */
    public final void newDelay(int delay) {
        Preconditions.checkArgument(delay >= 0);
        this.delay = delay;
    }

    /**
     * Determines if this task executes when submitted.
     * 
     * @return {@code true} if this task executes when submitted, {@code false}
     *         otherwise.
     */
    public final boolean isInstant() {
        return instant;
    }

    /**
     * Gets the attachment key that will be used by this task.
     * 
     * @return the attachment key.
     */
    public final Object getKey() {
        return key;
    }

    /**
     * Determines if this task is running.
     * 
     * @return {@code true} if this task is running, {@code false} otherwise.
     */
    public final boolean isRunning() {
        return running;
    }
}