package ntou.notesharedevbackend.schedulerModule.job;

import ntou.notesharedevbackend.noteNodule.service.NoteService;
import ntou.notesharedevbackend.postModule.entity.Post;
import ntou.notesharedevbackend.postModule.service.PostService;
import ntou.notesharedevbackend.repository.NoteRepository;
import ntou.notesharedevbackend.schedulerModule.entity.Task;
import ntou.notesharedevbackend.schedulerModule.repository.TaskRepository;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;


public class TriggerJob implements Job {

    @Autowired
    private NoteService noteService;
    @Autowired
    private PostService postService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //JobExecutionContext include :執行此job的scheduler、觸發執行的trigger、jobDetail對象..
        /* Get message id recorded by scheduler during scheduling */
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String taskID = dataMap.getString("taskID");
        String type = dataMap.getString("type");

        if(dataMap.getString("type").equals("publish")){
            publish(dataMap.getString("noteID"));
        }else{

            vote(taskID,type,dataMap.getString("postID"));
        }
        System.out.println("Type of Executing job is "+type);
    }
    public void publish(String noteID){
        noteService.publishOrSubmit(noteID);
        System.out.println(" Publish "+noteID);
    }
    public void vote(String taskID, String type, String postID){
        Post post = postService.getPostById(postID);
        if(post.getVote().getType().equals("kick")){//踢人
            if(post.getVote().getAgree().size()>post.getVote().getDisagree().size()){//agree kick
                noteService.kickUserFromCollaboration(post.getAnswers().get(0),post.getVote().getKickTarget());
            }
        }else{//共筆
            if(post.getVote().getAgree().size()>post.getVote().getDisagree().size()){//agree publish
                noteService.publishOrSubmit(post.getAnswers().get(0));
            }else{//add a week
                Task task = new Task();
                task.setId(taskID);
                task.setType(type);
                //TODO 判斷加七天的月日年 接著call
                //modifySchedule(postID, task);
            }
        }
        System.out.println("Vote "+postID);
    }
}
