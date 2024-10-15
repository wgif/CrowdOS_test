package cn.crowdos.kernel.manager;

import cn.crowdos.kernel.CrowdKernel;
import cn.crowdos.kernel.migration.TaskMigration;
import cn.crowdos.kernel.resource.Task;
import cn.crowdos.kernel.resource.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
//assign tasks by the kernel
@Service
public class TaskManager {
    private final TaskMigration taskMigration;
    private final CrowdKernel kernel;

    @Autowired
    public TaskManager(TaskMigration taskMigration, CrowdKernel kernel) {
        this.taskMigration = taskMigration;
        this.kernel = kernel;
    }

    public TaskMigration getTaskMigration() {
        return taskMigration;
    }


    public boolean manageTasks(Task task,Participant from,String algo) {
            kernel.algoSelect(algo);
            Participant to = findBestParticipantForTask(task);
            if (taskMigration.canMigrate(task, from, to)) {
                taskMigration.migrate(task, from, to);
                return true;
            }
            else{
                return false;
            }
    }

    private Participant findBestParticipantForTask(Task task) {
        List<Participant> candidates = kernel.getTaskRecommendationScheme(task);
        Participant result = null;
        for(Participant candidate : candidates){
            if(Participant.ParticipantStatus.AVAILABLE == candidate.getStatus()){
                result = candidate;
            }
        }
        return  result;// find the best participant
    }

    public Task getTaskById(Task taskId) {
        List<Task> tasks = kernel.getTasks();
        Task result = null;
        for(Task task : tasks){
            if(task == taskId) {
                result = task;
            }
        }
        return result;   // find task through ID
    }

    public Participant getParticipantById(Participant participantId) {
        List<Participant> participants = kernel.getParticipants();
        Participant result = null;
        for(Participant participant : participants){
            if(participant == participantId) {
                result = participant;
            }
        } // find participant through ID
        return result;
    }


    private List<Task> getAllTasks() {
        return kernel.getTasks(); // get all tasks
    }
}