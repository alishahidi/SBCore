package net.alishahidi.sbcore.core.util;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@UtilityClass
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExecutorUtils {

    static Integer size;

    public Executor fixedThreadPool() {
        return Executors.newFixedThreadPool(size);
    }

    public ScheduledExecutorService scheduledThreadPool(){
        return Executors.newScheduledThreadPool(size);
    }

    public ScheduledExecutorService scheduledThreadPool(int size_){
        return Executors.newScheduledThreadPool(size_);
    }
}
