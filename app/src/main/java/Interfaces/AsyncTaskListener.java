package Interfaces;

import Enum.TaskType;
/**
 * Created by kuush on 6/17/2016.
 */
public interface AsyncTaskListener {

    public void onTaskCompleted(String result, TaskType taskType);
}
