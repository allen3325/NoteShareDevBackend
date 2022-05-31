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
        if(timeBeforeNow(request)){
            return null;
        }
        Task task = postService.schedulerPublishTime(postID,request);
        return task;
    }
    public Vote newVoteSchedule(String postID, Vote request){
        if(timeBeforeNow(request.getTask())){
            return null;
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
                    .startAt(DateBuilder.dateOf(0, 35,0,request.getDay(),request.getMonth(),request.getYear()))
                    .build();
            Scheduler scheduler = quartzConfig.schedulerFactoryBean().getScheduler();
            scheduler.scheduleJob(jobDetail,trigger);
            scheduler.start();
            System.out.println("schedule "+request.getId());
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
            System.out.println("Cancel "+ taskID);
        }catch(IOException | SchedulerException e){
            e.printStackTrace();
        }
    }
    public Task modifyPublishSchedule(String postID ,Task request){
        if(timeBeforeNow(request)){
            return null;
        }
        //modify -> cancel old task first then add new task
        String oldTaskID = postService.getPostById(postID).getTask().getId();
        cancelSchedule(oldTaskID);
        return postService.replacePublishTime(postID, request);
    }

    public  Vote modifyVoteSchedule(String postID, String voteID, Vote request){
        Post post = postService.getPostById(postID);
//        for(Vote v : post.getVote()){
//            if(v.getId().equals(voteID)){
//                cancelSchedule((v.getTask().getId()));//cancel old task
//                break;
//            }
//        }
        return postService.replaceVote(postID, voteID, request);
    }

    public boolean timeBeforeNow(Task request){
        LocalDate currentDate = LocalDate.now();
        int currentDay = currentDate.getDayOfMonth();
        Month month = currentDate.getMonth();
        int currentMonth = month.getValue();
        int currentYear = currentDate.getYear();
        if(request.getYear() < currentYear){//past year
            return true;
        } else if (request.getYear() == currentYear) {//same year
            if(request.getMonth() < currentMonth){//past month
                return true;
            } else if (request.getMonth() == currentMonth) {
                if(request.getDay() <= currentDay){//hour already set , can not set same day
                    return true;
                }
            }
        }
        return false;
    }
}
