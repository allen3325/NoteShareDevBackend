package ntou.notesharedevbackend.schedulerModule.service;

import ntou.notesharedevbackend.postModule.entity.Post;
import ntou.notesharedevbackend.repository.PostRepository;
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


    public void addSchedule(String id, Task request){
        //setting scheduleJob
        try{
            JobDetail jobDetail = JobBuilder.newJob(TriggerJob.class).withIdentity(request.getId()).build();
            jobDetail.getJobDataMap().put("taskID", request.getId());
            jobDetail.getJobDataMap().put("type", request.getType());
            jobDetail.getJobDataMap().put("noteID", request.getNoteID());
            jobDetail.getJobDataMap().put("postID", id);
            SimpleTrigger trigger = (SimpleTrigger) newTrigger()
                    .withIdentity(request.getId())
                    .startAt(DateBuilder.dateOf(request.getHour(), request.getMinute(),request.getSecond(),request.getDay(),request.getMonth(),request.getYear()))
                    .build();
            Scheduler scheduler = quartzConfig.schedulerFactoryBean().getScheduler();
            scheduler.scheduleJob(jobDetail,trigger);
            scheduler.start();
        }catch(IOException | SchedulerException e){
            e.printStackTrace();
        }
    }

    public void cancelSchedule(String taskID){
        try{
            Scheduler scheduler = quartzConfig.schedulerFactoryBean().getScheduler();
            scheduler.deleteJob(new JobKey(taskID));
            TriggerKey triggerKey = new TriggerKey(taskID);
            scheduler.unscheduleJob(triggerKey);
        }catch(IOException | SchedulerException e){
            e.printStackTrace();
        }
    }
    public void modifySchedule(String postID,Task request){
        cancelSchedule(request.getId());
        addSchedule(postID,request);
        //TODO postrepositiry save modify task
    }
}
