package ntou.notesharedevbackend.schedulerModule.job;

import ntou.notesharedevbackend.schedulerModule.repository.TaskRepository;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;


public class TriggerJob implements Job {
    @Autowired
    private TaskRepository taskRepository;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //JobExecutionContext include :執行此job的scheduler、觸發執行的trigger、jobDetail對象..
        /* Get message id recorded by scheduler during scheduling */
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String id = dataMap.getString("id");
        String type = dataMap.getString("type");
        if(dataMap.getString("type").equals("publish")){
            publish(dataMap.getString("noteID"));
        }else{
            vote(dataMap.getString("noteID"));
        }
        System.out.println("Type of Executing job is "+type);
    }
    public void publish(String noteID){
        System.out.println(" Publish "+noteID);
    }
    public void vote(String noteID){
        System.out.println("Vote "+noteID);
    }
}
