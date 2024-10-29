package cn.crowdos.kernel.resource;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import cn.crowdos.kernel.constraint.Condition;
import java.util.List;

public class TaskContext {

    private final Task taskId;  // 任务标识符
    private Task.TaskStatus status;  // 任务状态
    private List<Participant> participantList;
    private final Map<Participant, Condition> participantData;  // 参与者信息（如ID，能力等）
    private final Map<String, Object> progressData;  // 任务进度（如已处理的数据量、进度百分比等）
    private final Map<String, Object> environmentData;  // 感知环境数据（如传感器信息、位置等）
    private final Map<String, Object> resourceData;  // 资源状态（如设备状态、剩余电量等）


    // 构造函数
    public TaskContext(Task taskId) {
        this.taskId = taskId;
        this.status = taskId.getTaskStatus();
        this.participantList = new ArrayList<>();
        this.participantData = new HashMap<>();
        this.progressData = new HashMap<>();
        this.environmentData = new HashMap<>();
        this.resourceData = new HashMap<>();
    }

    // 获取和设置任务标识符
    public Task getTaskId() {
        return taskId;
    }


    // 获取和设置任务状态

    public Task.TaskStatus getStatus() {
        return status;
    }

    public void setStatus(Task.TaskStatus status) {
        this.status = status;
        this.taskId.setTaskStatus(status);
    }


    // 设置参与者相关数据
    public void setParticipantList(List<Participant> participants) {
        this.participantList = participants;
    }

    public List<Participant> getParticipantList() {
        return this.participantList;
    }

    public void setParticipantData(Participant key, Condition value) {
        participantData.put(key, value);
    }

    public Object getParticipantData(Participant key) {
        return participantData.get(key);
    }


    // 设置任务进度相关数据
    public void setProgressData(String key, Object value) {
        progressData.put(key, value);
    }

    public Object getProgressData(String key) {
        return progressData.get(key);
    }

    // 设置环境数据
    public void setEnvironmentData(String key, Object value) {
        environmentData.put(key, value);
    }

    public Object getEnvironmentData(String key) {
        return environmentData.get(key);
    }

    // 设置资源状态
    public void setResourceData(String key, Object value) {
        resourceData.put(key, value);
    }

    public Object getResourceData(String key) {
        return resourceData.get(key);
    }
}

