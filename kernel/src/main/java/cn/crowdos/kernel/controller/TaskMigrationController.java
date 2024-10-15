package cn.crowdos.kernel.controller;

import cn.crowdos.kernel.manager.TaskManager;
import cn.crowdos.kernel.resource.Task;
import cn.crowdos.kernel.resource.Participant;
import org.springframework.beans.factory.annotation.Autowired;


public class TaskMigrationController {

    @Autowired
    private final TaskManager taskManager;

    public TaskMigrationController(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    //migrate according to IDs
    public String migrateTask(Task taskId,  Participant fromParticipantId, Participant toParticipantId) {
        Task task = taskManager.getTaskById(taskId);
        Participant from = taskManager.getParticipantById(fromParticipantId);
        Participant to = taskManager.getParticipantById(toParticipantId);

        if (taskManager.getTaskMigration().canMigrate(task, from, to)) {
            taskManager.getTaskMigration().migrate(task, from, to);
            return "Task migrated successfully.";
        } else {
            return "Task migration failed.";
        }
    }
    //migrate but not sure the target, algorithm default
    public String migrateTask(Task taskId,  Participant fromParticipantId){
        Task task = taskManager.getTaskById(taskId);
        Participant from = taskManager.getParticipantById(fromParticipantId);
        String algo = "PTMost";
        if (taskManager.manageTasks(task,from,algo)) {
            return "Task migrated successfully.";
        } else {
            return "Task migration failed.";
        }
    }
   //migrate but not sure the target, by pointing the selecting algorithm
    public String migrateTask(Task taskId,  Participant fromParticipantId,String algo){
        Task task = taskManager.getTaskById(taskId);
        Participant from = taskManager.getParticipantById(fromParticipantId);

        if (taskManager.manageTasks(task,from,algo)) {
            return "Task migrated successfully.";
        } else {
            return "Task migration failed.";
        }
    }
}