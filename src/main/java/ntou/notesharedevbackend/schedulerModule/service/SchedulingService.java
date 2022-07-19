package ntou.notesharedevbackend.schedulerModule.service;

import ntou.notesharedevbackend.postModule.entity.Post;
import ntou.notesharedevbackend.postModule.service.PostService;
import ntou.notesharedevbackend.schedulerModule.config.QuartzConfig;
import ntou.notesharedevbackend.schedulerModule.entity.KickVoteRequest;
import ntou.notesharedevbackend.schedulerModule.entity.Task;
import ntou.notesharedevbackend.schedulerModule.entity.Vote;
import ntou.notesharedevbackend.schedulerModule.entity.VoteReturn;
import ntou.notesharedevbackend.schedulerModule.job.TriggerJob;
import ntou.notesharedevbackend.userModule.entity.UserObj;
import ntou.notesharedevbackend.userModule.service.AppUserService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

import static org.quartz.TriggerBuilder.newTrigger;

@Service
public class SchedulingService {
    @Autowired
    private QuartzConfig quartzConfig;

    @Autowired
    @Lazy(value = true)
    private PostService postService;

    @Autowired
    private AppUserService appUserService;

    //    public Task newPublishSchedule(String postID, Task request){
//        if(timeBeforeNow(request)){
//            return null;
//        }
//        Task task = postService.schedulerPublishTime(postID,request);
//        return task;
//    }
    public Vote newVoteSchedule(String postID, KickVoteRequest request) {
        //判斷時間是否過早
        if (timeBeforeNow(request.getYear(), request.getMonth(), request.getDay())) {
            return null;
        }
        Vote vote = postService.addVote(postID, request);
        return vote;
    }

    public void addSchedule(Task request) {
        int i = 0;
        Post post = postService.getPostById(request.getPostID());
        for (Vote vote : post.getVote()) {
            Task task = vote.getTask();
            if (task.getDay() == request.getDay() && task.getMonth() == request.getMonth() && task.getYear() == request.getYear()) {
                i = i + 5;
            }
        }
        System.out.println("second = " + i);
        //setting scheduleJob
        try {
            JobDetail jobDetail = JobBuilder.newJob(TriggerJob.class).withIdentity(request.getId()).build();
            jobDetail.getJobDataMap().put("taskID", request.getId());
            jobDetail.getJobDataMap().put("voteID", request.getVoteID());
            jobDetail.getJobDataMap().put("postID", request.getPostID());
            //dateOf(hour,minute,second,day,month,year)
            SimpleTrigger trigger = (SimpleTrigger) newTrigger()
                    .withIdentity(request.getId())
                    .startAt(DateBuilder.dateOf(0, 0, i, request.getDay(), request.getMonth(), request.getYear()))
                    .build();
            Scheduler scheduler = quartzConfig.schedulerFactoryBean().getScheduler();
            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();
            System.out.println("schedule " + request.getId());
        } catch (IOException | SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void cancelSchedule(String taskID) {
        try {
            Scheduler scheduler = quartzConfig.schedulerFactoryBean().getScheduler();
            scheduler.deleteJob(new JobKey(taskID));
            TriggerKey triggerKey = new TriggerKey(taskID);
            scheduler.unscheduleJob(triggerKey);
            System.out.println("Cancel " + taskID);
        } catch (IOException | SchedulerException e) {
            e.printStackTrace();
        }
    }
//    public Task modifyPublishSchedule(String postID ,Task request){
//        if(timeBeforeNow(request)){
//            return null;
//        }
//        //modify -> cancel old task first then add new task
////        String oldTaskID = postService.getPostById(postID).getTask().getId();
////        cancelSchedule(oldTaskID);
//        return postService.replacePublishTime(postID, request);
//    }

    public Vote modifyVoteSchedule(String postID, String voteID, KickVoteRequest request) {
        //判斷時間是否過早
        if (timeBeforeNow(request.getYear(), request.getMonth(), request.getDay())) {
            return null;
        }
        //cancel old task and add new task
        return postService.replaceVote(postID, voteID, request);
    }

    public boolean timeBeforeNow(int requestYear, int requestMonth, int requestDay) {
        LocalDate currentDate = LocalDate.now();
        int currentDay = currentDate.getDayOfMonth();
        Month month = currentDate.getMonth();
        int currentMonth = month.getValue();
        int currentYear = currentDate.getYear();
        if (requestYear < currentYear) {//past year
            return true;
        } else if (requestYear == currentYear) {//same year
            if (requestMonth < currentMonth) {//past month
                return true;
            } else if (requestMonth == currentMonth) {
                if (requestDay <= currentDay) {//hour already set , can not set same day
                    return true;
                }
            }
        }
        return false;
    }

    public void deleteVote(String postID, String voteID) {
        Post post = postService.getPostById(postID);
        for (Vote vote : post.getVote()) {
            if (vote.getId().equals(voteID)) {
                cancelSchedule(vote.getTask().getId());
                break;
            }
        }
        postService.deleteVote(postID, voteID);
    }

    public VoteReturn getUserInfo(Vote vote) {
        VoteReturn voteReturn = new VoteReturn();
        voteReturn.setId(vote.getId());
        voteReturn.setResult(vote.getResult());
        voteReturn.setKickTargetUserObj(appUserService.getUserInfo(vote.getKickTarget()));
        voteReturn.setTask(vote.getTask());
        voteReturn.setDisagree(vote.getDisagree());
        voteReturn.setAgree(vote.getAgree());
        voteReturn.setKickTarget(vote.getKickTarget());
        ArrayList<UserObj> agreeUserList = new ArrayList<UserObj>();
        for (String agreeUser : vote.getAgree()) {
            UserObj userObj = appUserService.getUserInfo(agreeUser);
            agreeUserList.add(userObj);
        }
        ArrayList<UserObj> disagreeUserList = new ArrayList<UserObj>();
        for (String disagreeUser : vote.getDisagree()) {
            UserObj userObj = appUserService.getUserInfo(disagreeUser);
            disagreeUserList.add(userObj);
        }
        voteReturn.setAgreeUserObj(agreeUserList);
        voteReturn.setDisagreeUserObj(disagreeUserList);
        return voteReturn;
    }
}
