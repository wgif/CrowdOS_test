package cn.crowdos.kernel.manager;

import java.util.*;
import java.util.Map;
import java.util.HashMap;
import cn.crowdos.kernel.resource.Task;
import cn.crowdos.kernel.resource.TaskContext;

import java.util.*;

public class PriorityScheduler {

    private Queue<Task> taskQueue;  // 任务的优先级队列
    private Task currentTask;  // 当前正在执行的任务
    private Map<Task, TaskContext> interruptedTasks;  // 保存被中断任务的上下文
    private Timer timer;  // 用于定期检查任务状态
    private Map<Task, Integer> taskPriorityMap;  // 保存任务的优先级数值

    // 优先级数值范围
    private static final int LOW_PRIORITY_START = 0;
    private static final int MEDIUM_PRIORITY_START = 100;
    private static final int HIGH_PRIORITY_START = 200;
    private static final int URGENT_PRIORITY_START = 300;

    // 每单位时间任务优先级增加的数值
    private static final int PRIORITY_INCREMENT_RATE = 15;

    /**
     * 构造函数，初始化调度器
     */
    public PriorityScheduler() {
        // 使用优先队列并根据优先级数值进行排序
        taskQueue = new PriorityQueue<>(Comparator.comparingInt(task -> taskPriorityMap.get(task)).reversed());
        interruptedTasks = new HashMap<>();
        taskPriorityMap = new HashMap<>();
        timer = new Timer(true);  // 后台线程定期检查任务状态
        startPriorityAdjustment();  // 开始定时调整任务优先级
    }

    /**
     * 启动定时任务，每隔一定时间调整被中断任务的优先级
     */
    private void startPriorityAdjustment() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                adjustPriorities();
            }
        }, 0, 5000);  // 每5秒调整一次优先级
    }

    /**
     * 动态调整被中断任务的优先级
     */
    private void adjustPriorities() {
        for (Task task : interruptedTasks.keySet()) {
            int currentPriorityValue = taskPriorityMap.get(task);
            taskPriorityMap.put(task, currentPriorityValue + PRIORITY_INCREMENT_RATE);  // 增加优先级数值
            System.out.println("任务 " + task + " 的优先级已提升到: " + taskPriorityMap.get(task));
        }
    }

    /**
     * 根据任务的初始优先级将其映射为数值
     * @param task 任务
     * @return 映射后的优先级数值
     */
    private int mapPriorityValue(Task task) {
        switch (task.getTaskPriority()) {
            case LOW:
                return LOW_PRIORITY_START;
            case MEDIUM:
                return MEDIUM_PRIORITY_START;
            case HIGH:
                return HIGH_PRIORITY_START;
            case URGENT:
                return URGENT_PRIORITY_START;
            default:
                throw new IllegalArgumentException("未知的任务优先级: " + task.getTaskPriority());
        }
    }

    /**
     * 向调度器添加任务
     * @param task 要添加的任务
     */
    public void addTask(Task task) {
        if (currentTask != null && taskPriorityMap.get(task) > taskPriorityMap.get(currentTask)) {
            // 如果新任务的优先级数值大于当前任务，执行中断逻辑
            interruptCurrentTask();
            taskQueue.offer(task);  // 将新任务加入队列
            taskPriorityMap.put(task, mapPriorityValue(task));  // 为任务分配初始优先级数值
            executeNextTask();  // 执行新任务
        } else {
            taskQueue.offer(task);  // 将任务加入队列
            taskPriorityMap.put(task, mapPriorityValue(task));  // 分配初始优先级
            if (currentTask == null) {
                executeNextTask();  // 如果当前没有正在执行的任务，则执行下一个任务
            }
        }
    }

    /**
     * 执行下一个任务
     */
    private void executeNextTask() {
        if (!taskQueue.isEmpty()) {
            currentTask = taskQueue.poll();  // 从队列中取出优先级最高的任务
            System.out.println("开始执行任务: " + currentTask + ", 当前优先级: " + taskPriorityMap.get(currentTask));

            // 模拟任务执行
            performTask(currentTask);
        }
    }

    /**
     * 模拟任务执行
     * @param task 当前任务
     */
    private void performTask(Task task) {
        // 使用线程模拟任务执行
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // 任务完成后执行下一个任务
                completeCurrentTask();
            }
        }, 3000);  // 模拟任务执行3秒后完成
    }

    /**
     * 完成当前任务
     */
    private void completeCurrentTask() {
        System.out.println("任务 " + currentTask + " 已完成");
        currentTask = null;  // 当前任务完成
        if (!taskQueue.isEmpty()) {
            executeNextTask();  // 执行下一个任务
        }
    }

    /**
     * 中断当前任务并保存其上下文
     */
    private void interruptCurrentTask() {
        System.out.println("中断任务: " + currentTask + ", 当前优先级: " + taskPriorityMap.get(currentTask));
        TaskContext context = new TaskContext(currentTask);  // 保存任务的上下文
        interruptedTasks.put(currentTask, context);
        currentTask = null;  // 当前任务被中断
    }

    /**
     * 恢复中断的任务
     * @param task 被中断的任务
     */
    private void resumeInterruptedTask(Task task) {
        if (interruptedTasks.containsKey(task)) {
            System.out.println("恢复任务: " + task + ", 当前优先级: " + taskPriorityMap.get(task));
            interruptedTasks.remove(task);  // 恢复任务执行
            currentTask = task;
            performTask(task);
        }
    }
}

