package ntou.notesharedevbackend.schedulerModule.service;

import ntou.notesharedevbackend.schedulerModule.config.QuartzConfig;
import ntou.notesharedevbackend.schedulerModule.entity.Task;
import ntou.notesharedevbackend.schedulerModule.job.TriggerJob;
import ntou.notesharedevbackend.schedulerModule.repository.TaskRepository;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static org.quartz.TriggerBuilder.newTrigger;

@Service
public class SchedulingService {
    @Autowired
    private QuartzConfig quartzConfig;

    @Autowired
    private TaskRepository taskRepository;

    public Task addSchedule(Task request){
        Task task = new Task();
        task.setDay(request.getDay());
        task.setHour(request.getHour());
        task.setMinute(request.getMinute());
        task.setYear(request.getYear());
        task.setMonth(request.getMonth());
        task.setSecond(request.getSecond());
        task.setNoteID(request.getNoteID());
        task.setType(request.getType());
        taskRepository.insert(task);
        try{
            JobDetail jobDetail = JobBuilder.newJob(TriggerJob.class).withIdentity(task.getId()).build();
            jobDetail.getJobDataMap().put("id",request.getId());
            jobDetail.getJobDataMap().put("type",request.getType());
            jobDetail.getJobDataMap().put("noteID",request.getNoteID());
            SimpleTrigger trigger = (SimpleTrigger) newTrigger()
                    .withIdentity(request.getId())
                    .startAt(DateBuilder.dateOf(request.getHour(),request.getMinute(),request.getSecond(),request.getDay(),request.getMonth(),request.getYear()))
                    .build();
            Scheduler scheduler = quartzConfig.schedulerFactoryBean().getScheduler();
            scheduler.scheduleJob(jobDetail,trigger);
            scheduler.start();
        }catch(IOException | SchedulerException e){
            e.printStackTrace();
        }
        return task;
    }

    public void cancelSchedule(String id){
        try{
            Scheduler scheduler = quartzConfig.schedulerFactoryBean().getScheduler();
            scheduler.deleteJob(new JobKey(id));
            TriggerKey triggerKey = new TriggerKey(id);
            scheduler.unscheduleJob(triggerKey);
        }catch(IOException | SchedulerException e){
            e.printStackTrace();
        }
    }
    public void editSchedule(String id,Task request){
        cancelSchedule(id);
        addSchedule(request);
    }
}
