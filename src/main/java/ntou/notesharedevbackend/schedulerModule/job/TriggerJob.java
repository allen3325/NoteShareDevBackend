package ntou.notesharedevbackend.schedulerModule.job;

import ntou.notesharedevbackend.noteModule.service.NoteService;
import ntou.notesharedevbackend.notificationModule.entity.*;
import ntou.notesharedevbackend.notificationModule.service.*;
import ntou.notesharedevbackend.postModule.entity.Post;
import ntou.notesharedevbackend.postModule.service.PostService;
import ntou.notesharedevbackend.repository.PostRepository;
import ntou.notesharedevbackend.schedulerModule.entity.Task;
import ntou.notesharedevbackend.schedulerModule.entity.Vote;
import ntou.notesharedevbackend.schedulerModule.service.SchedulingService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.*;

import java.text.*;
import java.util.*;
import java.util.Calendar;


public class TriggerJob implements Job {

    @Autowired
    private NoteService noteService;
    @Autowired
    private PostService postService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private PostRepository postRepository;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //JobExecutionContext include :執行此job的scheduler、觸發執行的trigger、jobDetail對象
        // Get message id recorded by scheduler during scheduling
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String taskID = dataMap.getString("taskID");
//        String type = dataMap.getString("type");
//        if(dataMap.getString("type").equals("publish")){//publish noteID
//            publish(dataMap.getString("noteIDOrVoteID"));
//        }else{
//
//        }
        vote(dataMap.getString("voteID"), dataMap.getString("postID"));//vote result
        System.out.println("job execution taskID = " + taskID);
    }

    //    public void publish(String noteID){
//        noteService.publishOrSubmit(noteID);
//        System.out.println(" Publish "+noteID);
//    }
    public void vote(String voteID, String postID) {//need post -> vote -> type ->result
        Post post = postService.getPostById(postID);
        int i = 0;
        for (Vote v : post.getVote()) {//find target vote
            if (v.getId().equals(voteID)) {
                //總投票人數
                int totalVote = v.getAgree().size() + v.getDisagree().size();
                //共筆總人數
                int totalPerson = post.getEmail().size();
                //有效投票->總投票人數要大於共筆總人數
                if (totalVote > (totalPerson / 2)) {
                    //需要同意大於不同意才算同意
                    if (v.getAgree().size() > v.getDisagree().size()) {//agree kick
                        noteService.triggerKickUserFromCollaboration(post.getAnswers().get(0), v.getKickTarget());
                        v.setResult("agree kick");
                    } else {
                        v.setResult("disagree kick");
                    }
                } else {
                    //無效投票
                    v.setResult("invalid");
                }
                //傳送投票結果通知
                for (String author : post.getEmail()) {
                    boolean isKickTarget = author.equals(v.getKickTarget());
                    MessageReturn messageReturn = notificationService.getMessageReturnFromVotes(v.getResult(), postID, isKickTarget, v.getKickTarget());
                    messagingTemplate.convertAndSendToUser(author, "/topic/private-messages", messageReturn);
                    notificationService.saveNotificationPrivate(author, messageReturn);
                }
                //更新投票結果
                System.out.println("kickTarget " + v.getKickTarget() + " result " + v.getResult());
                post = postService.getPostById(postID);
                post.getVote().get(i).setResult(v.getResult());
                postRepository.save(post);
                //踢人
                if (v.getResult().equals("agree kick")) {
                    post = postService.getPostById(postID);
                    post.getEmail().remove(v.getKickTarget());
                    postRepository.save(post);
                }
                break;
            }
            i++;
        }
        System.out.println("Vote's post ID " + postID);
    }

    public Task postponeTask(Task request, int postponeDay) {
        Task task = new Task();
//        task.setType(request.getType());
        task.setVoteID(request.getVoteID());
        task.setPostID(request.getPostID());
        // input 改為 DataBase 讀取或是前端輸入
        int year = request.getYear();
        int month = request.getMonth();
        int day = request.getDay();
        int modify = postponeDay;

        // 格式 dd-MM-yyyy
        DateFormat parser = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = "";
        // 處理字串格式並 parse 成 Date 格式
        if (day < 10) {
            dateString += "0" + Integer.toString(day);
        } else {
            dateString += Integer.toString(day);
        }
        dateString += "-";
        if (month < 10) {
            dateString += "0" + Integer.toString(month);
        } else {
            dateString += Integer.toString(month);
        }
        dateString += "-" + Integer.toString(year);

        try {
            Date date = (Date) parser.parse(dateString);
            Calendar publishDate = Calendar.getInstance();
            publishDate.setTime(date); // 將 publishDate 設為 date (DataBase 原本的日期或是前端傳進來的)
            publishDate.add(Calendar.DAY_OF_MONTH, modify); // 加減天數
            task.setYear(publishDate.get(Calendar.YEAR));
            System.out.println(publishDate.get(Calendar.YEAR)); // publishDate.get(Calendar.YEAR) -> int -> 存進DataBase
            task.setMonth(publishDate.get(Calendar.MONTH));
            System.out.println(publishDate.get(Calendar.MONTH)); // publishDate.get(Calendar.MONTH) -> int -> 存進DataBase
            task.setDay(publishDate.get(Calendar.DAY_OF_MONTH));
            System.out.println(publishDate.get(Calendar.DAY_OF_MONTH)); // publishDate.get(Calendar.DAY_OF_MONTH) -> int -> 存進DataBase
            System.out.println(publishDate.getTime().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return task;
    }
}
