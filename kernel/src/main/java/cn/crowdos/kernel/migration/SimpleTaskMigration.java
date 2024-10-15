package cn.crowdos.kernel.migration;

import cn.crowdos.kernel.CrowdKernel;
import cn.crowdos.kernel.resource.Task;
import cn.crowdos.kernel.resource.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimpleTaskMigration implements TaskMigration {
    public CrowdKernel kernel;

    @Autowired
    public SimpleTaskMigration(CrowdKernel kernel) {
        this.kernel = kernel;
    }

    @Override
    public boolean canMigrate(Task task, Participant from, Participant to) {
        return task.canAssignTo(to);
    }

    @Override
    public void migrate(Task task, Participant from, Participant to) {
        if (canMigrate(task, from, to)) {
            if(Participant.ParticipantStatus.AVAILABLE==from.getStatus()){
                from.setStatus(Participant.ParticipantStatus.BUSY);
            }
            List<Participant> participants = kernel.getParticipants();
            if(!participants.contains(to)){
                kernel.registerParticipant(to);
            }
            kernel.getTaskRecommendationScheme(task).add(to);
        }
    }
}