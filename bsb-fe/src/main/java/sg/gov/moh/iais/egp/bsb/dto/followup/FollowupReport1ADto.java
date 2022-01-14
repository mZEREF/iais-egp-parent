package sg.gov.moh.iais.egp.bsb.dto.followup;

import lombok.Data;
import org.springframework.util.CollectionUtils;
import sg.gov.moh.iais.egp.bsb.dto.file.CommonDocDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * @author YiMing
 * @version 2022/1/10 17:22
 **/
@Data
public class FollowupReport1ADto implements Serializable {
    private FollowupInfoADto infoADto;
    private Collection<DocRecordInfo> docRecordInfos;


    public static FollowupReport1ADto from(FollowupInfoADto followupInfoADto, CommonDocDto dto){
        //retrieve followupInfoA Data
        FollowupReport1ADto report1ADto = new FollowupReport1ADto();
        report1ADto.setInfoADto(followupInfoADto);
        Map<String, DocRecordInfo> savedDocMap = dto.getSavedDocMap();
        if(CollectionUtils.isEmpty(savedDocMap)){
            return report1ADto;
        }
        report1ADto.setDocRecordInfos(savedDocMap.values());
        return report1ADto;
    }
}
