package com.sythel.fishera.bootstrap;

import com.sythel.fishera.database.DatabaseManager;
import com.sythel.fishera.repository.CollectionRepository;
import com.sythel.fishera.repository.FishRepository;
import com.sythel.fishera.repository.TaskRepository;

public class RepositorySetup {

    private final DatabaseManager databaseManager;

    private TaskRepository taskRepository;
    private FishRepository fishRepository;
    private CollectionRepository collectionRepository;

    public RepositorySetup(
            DatabaseManager databaseManager) {

        this.databaseManager = databaseManager;
    }

    public void initialize() {

        taskRepository =
                new TaskRepository(
                        databaseManager
                );

        fishRepository =
                new FishRepository(
                        databaseManager
                );

        collectionRepository =
                new CollectionRepository(
                        databaseManager
                );
    }

    public TaskRepository getTaskRepository() {
        return taskRepository;
    }

    public FishRepository getFishRepository() {
        return fishRepository;
    }

    public CollectionRepository getCollectionRepository() {
        return collectionRepository;
    }
}