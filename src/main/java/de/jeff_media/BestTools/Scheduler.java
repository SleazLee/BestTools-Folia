package de.jeff_media.BestTools;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.TimeUnit;

import static org.bukkit.Bukkit.getLogger;

/**
 * Utility class for scheduling tasks with optional Folia support.
 */
public final class Scheduler {

    private static final boolean isFolia;
    static {
        boolean folia;
        try {
            Class.forName("io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler");
            folia = true;
            getLogger().warning("Scheduler You are running FOLIA");
        } catch (ClassNotFoundException e) {
            folia = false;
            getLogger().warning("Scheduler You are running BUKKIT");
        }
        isFolia = folia;
    }

    /**
     * Runs a task on the global scheduler.
     *
     * @param runnable task to run
     */
    public static void run(Runnable runnable) {
        if (isFolia) {
            Bukkit.getGlobalRegionScheduler().execute(Main.getInstance(), runnable);
        } else {
            Bukkit.getScheduler().runTask(Main.getInstance(), runnable);
        }
    }

    /**
     * Runs a task after a delay using the global scheduler.
     *
     * @param runnable   task to run
     * @param delayTicks delay in ticks
     * @return scheduled task wrapper
     */
    public static Task runLater(Runnable runnable, long delayTicks) {
        if (delayTicks <= 0) {
            run(runnable);
            return new Task(null);
        }
        if (isFolia) {
            return new Task(Bukkit.getGlobalRegionScheduler().runDelayed(Main.getInstance(), t -> runnable.run(), delayTicks));
        }
        return new Task(Bukkit.getScheduler().runTaskLater(Main.getInstance(), runnable, delayTicks));
    }

    /**
     * Runs a repeating task on the global scheduler.
     *
     * @param runnable    task to run
     * @param delayTicks  delay before first run
     * @param periodTicks period between runs
     * @return scheduled task wrapper
     */
    public static Task runTimer(Runnable runnable, long delayTicks, long periodTicks) {
        if (isFolia) {
            return new Task(Bukkit.getGlobalRegionScheduler().runAtFixedRate(Main.getInstance(), t -> runnable.run(), delayTicks < 1 ? 1 : delayTicks, periodTicks));
        }
        return new Task(Bukkit.getScheduler().runTaskTimer(Main.getInstance(), runnable, delayTicks, periodTicks));
    }

    // -------------------------------------------------------------------
    // ASYNC METHODS
    // -------------------------------------------------------------------

    /**
     * Runs a task asynchronously.
     *
     * @param runnable task to run
     */
    public static void runAsync(Runnable runnable) {
        if (isFolia) {
            Bukkit.getAsyncScheduler().runNow(Main.getInstance(), t -> runnable.run());
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), runnable);
        }
    }

    /**
     * Runs a task asynchronously after a delay.
     *
     * @param runnable   task to run
     * @param delayTicks delay in ticks
     * @return scheduled task wrapper
     */
    public static Task runAsyncLater(Runnable runnable, long delayTicks) {
        if (delayTicks <= 0) {
            runAsync(runnable);
            return new Task(null);
        }
        if (isFolia) {
            return new Task(Bukkit.getAsyncScheduler().runDelayed(Main.getInstance(), t -> runnable.run(), delayTicks * 50L, TimeUnit.MILLISECONDS));
        }
        return new Task(Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getInstance(), runnable, delayTicks));
    }

    /**
     * Runs a repeating asynchronous task.
     *
     * @param runnable    task to run
     * @param delayTicks  delay before first run
     * @param periodTicks period between runs
     * @return scheduled task wrapper
     */
    public static Task runAsyncTimer(Runnable runnable, long delayTicks, long periodTicks) {
        if (isFolia) {
            return new Task(Bukkit.getAsyncScheduler().runAtFixedRate(Main.getInstance(), t -> runnable.run(), (delayTicks < 1 ? 1 : delayTicks) * 50L, periodTicks * 50L, TimeUnit.MILLISECONDS));
        }
        return new Task(Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), runnable, delayTicks, periodTicks));
    }

    // -------------------------------------------------------------------
    // REGION METHODS
    // -------------------------------------------------------------------

    /**
     * Runs a task in a region context.
     *
     * @param location world location
     * @param runnable task to run
     */
    public static void run(Location location, Runnable runnable) {
        if (isFolia) {
            Bukkit.getRegionScheduler().execute(Main.getInstance(), location, runnable);
        } else {
            Bukkit.getScheduler().runTask(Main.getInstance(), runnable);
        }
    }

    /**
     * Runs a task after a delay in a region context.
     *
     * @param location   world location
     * @param runnable   task to run
     * @param delayTicks delay in ticks
     * @return scheduled task wrapper
     */
    public static Task runLater(Location location, Runnable runnable, long delayTicks) {
        if (delayTicks <= 0) {
            run(location, runnable);
            return new Task(null);
        }
        if (isFolia) {
            return new Task(Bukkit.getRegionScheduler().runDelayed(Main.getInstance(), location, t -> runnable.run(), delayTicks));
        }
        return new Task(Bukkit.getScheduler().runTaskLater(Main.getInstance(), runnable, delayTicks));
    }

    /**
     * Runs a repeating task in a region context.
     *
     * @param location    world location
     * @param runnable    task to run
     * @param delayTicks  delay before first run
     * @param periodTicks period between runs
     * @return scheduled task wrapper
     */
    public static Task runTimer(Location location, Runnable runnable, long delayTicks, long periodTicks) {
        if (isFolia) {
            return new Task(Bukkit.getRegionScheduler().runAtFixedRate(Main.getInstance(), location, t -> runnable.run(), delayTicks < 1 ? 1 : delayTicks, periodTicks));
        }
        return new Task(Bukkit.getScheduler().runTaskTimer(Main.getInstance(), runnable, delayTicks, periodTicks));
    }

    /**
     * @return true if the server runs Folia
     */
    public static boolean isFolia() {
        return isFolia;
    }

    /**
     * Placeholder method for cancelling current task.
     */
    public static void cancelCurrentTask() {
    }

    /**
     * Wrapper for scheduled tasks.
     */
    public static class Task {

        private final Object foliaTask;
        private final BukkitTask bukkitTask;

        Task(Object foliaTask) {
            this.foliaTask = foliaTask;
            this.bukkitTask = null;
        }

        Task(BukkitTask bukkitTask) {
            this.bukkitTask = bukkitTask;
            this.foliaTask = null;
        }

        /**
         * Cancel the task.
         */
        public void cancel() {
            if (foliaTask != null) {
                ((ScheduledTask) foliaTask).cancel();
            } else if (bukkitTask != null) {
                bukkitTask.cancel();
            }
        }
    }
}
