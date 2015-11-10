package pl.java.scalatech.service;

import java.util.Random;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import pl.java.scalatech.domain.Task;
import pl.java.scalatech.respository.TaskRepository;

@Service
@Transactional
@Slf4j
public class TaskService implements Job {
    Random r = new Random();
    @Autowired
    private TaskRepository taskRepository;

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        log.info("++++++++++++++++++  task invoke..");
        Task task = new Task();
        task.setName("task " + r.nextInt(100));
        taskRepository.save(task);
    }

}