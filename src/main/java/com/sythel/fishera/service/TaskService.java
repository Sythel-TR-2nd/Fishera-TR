package com.sythel.fishera.service;

import com.sythel.fishera.repository.TaskRepository;
import com.sythel.fishera.task.TaskData;
import com.sythel.fishera.util.ColorUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TaskService {

    private final TaskRepository taskRepository;

    private final EconomyService economyService;

    private final MessageService messageService;

    private final List<TaskData> tasks;

    public TaskService(
            TaskRepository taskRepository,
            EconomyService economyService,
            MessageService messageService,
            List<TaskData> tasks) {

        this.taskRepository = taskRepository;

        this.economyService = economyService;

        this.messageService = messageService;

        this.tasks =
                new ArrayList<>(
                        tasks
                );

    }

    public void reloadTasks(
            List<TaskData> newTasks) {

        tasks.clear();

        tasks.addAll(
                newTasks
        );

    }

    public TaskData getActiveTask(
            Player player) {

        String uuid =
                player.getUniqueId().toString();

        for (TaskData task : tasks) {

            if (!taskRepository.isClaimed(
                    uuid,
                    task.getId()
            )) {

                return task;

            }

        }

        return null;

    }

    public double getProgress(
            Player player,
            TaskData task) {

        return taskRepository.getProgress(
                player.getUniqueId().toString(),
                task.getId()
        );

    }

    public void handleFishCaught(
            Player player,
            String fishId) {

        TaskData task =
                getActiveTask(
                        player
                );

        if (task == null) {
            return;
        }

        String uuid =
                player.getUniqueId().toString();

        if (taskRepository.isCompleted(
                uuid,
                task.getId()
        )) {

            return;

        }

        if (!task.getType()
                .equalsIgnoreCase(
                        "CATCH_FISH"
                )) {

            return;

        }

        if (task.getFishId() != null
                && !task.getFishId()
                .equalsIgnoreCase(
                        fishId
                )) {

            return;

        }

        double current =
                taskRepository.getProgress(
                        uuid,
                        task.getId()
                );

        double newProgress =
                Math.min(
                        current + 1,
                        task.getAmount()
                );

        taskRepository.setProgress(
                uuid,
                task.getId(),
                newProgress
        );

        if (newProgress >= task.getAmount()) {

            taskRepository.setCompleted(
                    uuid,
                    task.getId(),
                    true
            );

            player.sendMessage("");

            player.sendMessage(
                    messageService.get(
                            "task.completed"
                    )
            );

            player.sendMessage(
                    messageService.get(
                            "task.name",
                            "task",
                            task.getName()
                    )
            );

            player.sendMessage(
                    messageService.get(
                            "task.claim-in-menu"
                    )
            );

            player.sendMessage("");

        }

    }

    public boolean claimReward(
            Player player) {

        TaskData task =
                getActiveTask(
                        player
                );

        if (task == null) {

            player.sendMessage(
                    messageService.get(
                            "task.all-completed"
                    )
            );

            return false;

        }

        String uuid =
                player.getUniqueId().toString();

        if (!taskRepository.isCompleted(
                uuid,
                task.getId()
        )) {

            player.sendMessage(
                    messageService.get(
                            "task.not-completed"
                    )
            );

            return false;

        }

        if (taskRepository.isClaimed(
                uuid,
                task.getId()
        )) {

            return false;

        }

        double reward =
                task.getReward();

        if (reward > 0) {

            economyService.deposit(
                    player,
                    reward
            );

        }

        taskRepository.setClaimed(
                uuid,
                task.getId(),
                true
        );

        player.sendMessage("");

        player.sendMessage(
                messageService.get(
                        "task.reward-received"
                )
        );

        player.sendMessage(
                messageService.get(
                        "task.reward-task",
                        "task",
                        task.getName()
                )
        );

        player.sendMessage(
                messageService.get(
                        "task.reward-amount",
                        "reward",
                        String.format(
                                "%,.2f",
                                reward
                        )
                )
        );

        player.sendMessage("");

        return true;

    }

    public List<TaskData> getTasks() {

        return tasks;

    }

    public boolean isTaskCompleted(
            Player player,
            TaskData task) {

        return taskRepository.isCompleted(
                player.getUniqueId().toString(),
                task.getId()
        );

    }

    public boolean isTaskClaimed(
            Player player,
            TaskData task) {

        return taskRepository.isClaimed(
                player.getUniqueId().toString(),
                task.getId()
        );

    }

}