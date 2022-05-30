package ntou.notesharedevbackend.schedulerModule.service;

import ntou.notesharedevbackend.postModule.entity.Post;
import ntou.notesharedevbackend.postModule.service.PostService;
import ntou.notesharedevbackend.schedulerModule.config.QuartzConfig;
import ntou.notesharedevbackend.schedulerModule.entity.Task;
import ntou.notesharedevbackend.schedulerModule.entity.Vote;
import ntou.notesharedevbackend.schedulerModule.job.TriggerJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;

import static org.quartz.TriggerBuilder.newTrigger;

@Service
public class SchedulingService {
    @Autowired
    private QuartzConfig quartzConfig;

    @Autowired
    @Lazy(value = true)
    private PostService postService;

    public Task newPublishSchedule(String postID, Task request){
        LocalDate currentDate = LocalDate.now();
        int currentDay = currentDate.getDayOfMonth();
        Month month = currentDate.getMonth();
        int currentmonth = month.getValue();
        int currentYear = currentDate.getYear();
        if(request.getYear() < currentYear){//past year
            return null;
        } else if (request.getYear() == currentYear) {//same year
            if(request.getMonth() < currentmonth){//past month
                return null;
            } else if (request.getMonth() == currentmonth) {
                if(request.getDay() <= currentDay){//hour already set , can not set same day
                    return null;
                }
            }
        }
        Task task = postService.schedulerPublishTime(postID,request);
        return task;
    }
    public Vote newVoteSchedule(String postID, Vote request){
        LocalDate currentDate = LocalDate.now();
        int currentDay = currentDate.getDayOfMonth();
        Month month = currentDate.getMonth();
        int currentmonth = month.getValue();
        int currentYear = currentDate.getYear();
        Task task = request.getTask();
        if(task.getYear() < currentYear){//past year
            return null;
        } else if (task.getYear() == currentYear) {//same year
            if(task.getMonth() < currentmonth){//past month
                return null;
            } else if (task.getMonth() == currentmonth) {
                if(task.getDay() <= currentDay){//hour already set , can not set same day
                    return null;
                }
            }
        }
        Vote vote = postService.addVote(postID,request);
        return vote;
    }
    public void addSchedule(Task request){
        //setting scheduleJob
        try{
            JobDetail jobDetail = JobBuilder.newJob(TriggerJob.class).withIdentity(request.getId()).build();
            jobDetail.getJobDataMap().put("taskID", request.getId());
            jobDetail.getJobDataMap().put("type", request.getType());
            jobDetail.getJobDataMap().put("noteIDOrVoteID", request.getNoteIDOrVoteID());
            jobDetail.getJobDataMap().put("postID", request.getPostID());
            //dateOf(hour,minute,second,day,month,year)
            SimpleTrigger trigger = (SimpleTrigger) newTrigger()
                    .withIdentity(request.getId())
                    .startAt(DateBuilder.dateOf(17, 0,0,request.getDay(),request.getMonth(),request.getYear()))
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
    public Task modifyPublishSchedule(String postID ,Task request){
        String oldTaskID = postService.getPostById(postID).getTask().getId();
        cancelSchedule(oldTaskID);
        return postService.replacePublishTime(postID, request);
    }

    public  Vote modifyVoteSchedule(String postID, String voteID, Vote request){
        Post post = postService.getPostById(postID);
        for(Vote v : post.getVote()){
            if(v.getId().equals(voteID)){
                cancelSchedule((v.getTask().getId()));//cancel old task
                break;
            }
        }
        return postService.replaceVote(postID, voteID, request);
    }
}
