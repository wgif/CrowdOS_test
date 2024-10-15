package cn.crowdos.kernel;

import cn.crowdos.kernel.common.TimeParticipant;
import cn.crowdos.kernel.constraint.InvalidConstraintException;
import cn.crowdos.kernel.constraint.SimpleTimeConstraint;
import cn.crowdos.kernel.controller.TaskMigrationController;
import cn.crowdos.kernel.manager.TaskManager;
import cn.crowdos.kernel.migration.SimpleTaskMigration;
import cn.crowdos.kernel.migration.TaskMigration;
import cn.crowdos.kernel.resource.Participant;
import cn.crowdos.kernel.resource.SimpleTask;
import cn.crowdos.kernel.resource.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

public class KernelTest {
    CrowdKernel kernel;
    @BeforeEach
    void setUp() {
        kernel = Kernel.getKernel();
        kernel.initial();
        TimeParticipant p1 = new TimeParticipant("2022.6.1");
        TimeParticipant p2 = new TimeParticipant("2022.6.2");
        TimeParticipant p3 = new TimeParticipant("2022.6.3");
        TimeParticipant p4 = new TimeParticipant("2022.6.4");
        TimeParticipant p5 = new TimeParticipant("2022.6.5");
        TimeParticipant p6 = new TimeParticipant("2022.6.6");
        kernel.registerParticipant(p1);
        kernel.registerParticipant(p2);
        kernel.registerParticipant(p3);
        kernel.registerParticipant(p4);
        kernel.registerParticipant(p5);
        kernel.registerParticipant(p6);
        try {
            SimpleTimeConstraint timeConst = new SimpleTimeConstraint("2022.6.1", "2022.6.5");
            SimpleTask t1 = new SimpleTask(Collections.singletonList(timeConst), Task.TaskDistributionType.RECOMMENDATION);
            timeConst = new SimpleTimeConstraint("2022.6.2", "2022.6.4");
            SimpleTask t2 = new SimpleTask(Collections.singletonList(timeConst), Task.TaskDistributionType.ASSIGNMENT );
            kernel.submitTask(t1);
            kernel.submitTask(t2);
            kernel.algoSelect("T_Weight");
        } catch (InvalidConstraintException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown() {
        Kernel.shutdown();
    }
    @Test
    void getTasks() {
        System.out.println(kernel.getTasks());
    }

    @Test
    void getParticipants() {
        System.out.println(kernel.getParticipants());
    }

    @Test
    void getTaskAssignmentScheme() {
        for (Task task : kernel.getTasks()) {
            System.out.println(kernel.getTaskAssignmentScheme(task));
        }
    }

    @Test
    void getTaskRecommendationScheme() {
        for (Task task : kernel.getTasks()) {
            System.out.println(kernel.getTaskRecommendationScheme(task));
        }
    }

    @Test
    void getTaskParticipantSelectionResult() {
        for (Task task : kernel.getTasks()) {
            System.out.println(kernel.getTaskParticipantSelectionResult(task));
        }
    }
    @Test
    void migrateTask() {
        SimpleTaskMigration taskMigration = new SimpleTaskMigration(kernel);
        TaskMigrationController taskMigrationController = new TaskMigrationController(new TaskManager(taskMigration,kernel));
        List<Participant> p = kernel.getParticipants();
        List<Task> tasks = kernel.getTasks();
        System.out.println(kernel.getTaskRecommendationScheme(tasks.get(1)));
        System.out.println(taskMigrationController.migrateTask(tasks.get(1), p.get(1)));
        System.out.println(kernel.getTaskRecommendationScheme(tasks.get(1)));
    }
}