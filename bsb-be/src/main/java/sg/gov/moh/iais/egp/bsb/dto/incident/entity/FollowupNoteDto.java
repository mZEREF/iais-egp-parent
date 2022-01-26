package sg.gov.moh.iais.egp.bsb.dto.incident.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author YiMing
 * @version 2022/1/19 15:38
 **/
@Data
public class FollowupNoteDto implements Serializable {
    private String addNoteTime;
    private String noteInfo;
}
