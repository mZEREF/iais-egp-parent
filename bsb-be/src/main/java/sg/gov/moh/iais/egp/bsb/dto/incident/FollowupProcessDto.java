package sg.gov.moh.iais.egp.bsb.dto.incident;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.util.DateUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author YiMing
 * @version 2022/1/19 15:37
 **/

public class FollowupProcessDto implements Serializable {
    private String applicationId;
    private String taskId;
    private List<NewFollowupNote> newFollowupNotes;

    public FollowupProcessDto() {
        newFollowupNotes = new ArrayList<>();
    }

    @Data
    public static class NewFollowupNote implements Serializable{
        private String newNote;
        private String addNoteTime;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public List<NewFollowupNote> getNewFollowupNotes() {
        return newFollowupNotes;
    }

    public void setNewFollowupNotes(List<NewFollowupNote> newFollowupNotes) {
        this.newFollowupNotes = newFollowupNotes;
    }

    public void reqObjMapping(HttpServletRequest request){
        NewFollowupNote newFollowupNote = new NewFollowupNote();
        String newNote = ParamUtil.getString(request,"note");
        newFollowupNote.setNewNote(newNote);
        newFollowupNote.setAddNoteTime(DateUtil.toString(new Date()));
        this.newFollowupNotes.add(newFollowupNote);
    }
}
