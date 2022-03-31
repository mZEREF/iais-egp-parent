package sg.gov.moh.iais.egp.bsb.dto.inbox;

import lombok.*;
import sg.gov.moh.iais.egp.bsb.entity.MsgMaskParam;


import java.io.Serializable;
import java.util.List;


@Data
public class InboxMsgContentDto implements Serializable {
    private String content;
    private List<MsgMaskParam> inboxMaskParamDtoList;

}
