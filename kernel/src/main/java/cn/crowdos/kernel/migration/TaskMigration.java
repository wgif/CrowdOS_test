package cn.crowdos.kernel.migration;

import cn.crowdos.kernel.resource.Task;
import cn.crowdos.kernel.resource.Participant;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskMigration {
    boolean canMigrate(Task task, Participant from, Participant to);
    void migrate(Task task, Participant from, Participant to);
}