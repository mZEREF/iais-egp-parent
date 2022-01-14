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
public class FollowupReport1BDto implements Serializable {
    private FollowupInfoBDto infoBDto;
    private Collection<DocRecordInfo> docRecordInfos;


    public static FollowupReport1BDto from(FollowupInfoBDto followupInfoBDto, CommonDocDto dto){
        //retrieve followupInfoA Data
        FollowupReport1BDto report1BDto = new FollowupReport1BDto();
        report1BDto.setInfoBDto(followupInfoBDto);
        Map<String, DocRecordInfo> savedDocMap = dto.getSavedDocMap();
        if(CollectionUtils.isEmpty(savedDocMap)){
            return report1BDto;
        }
        report1BDto.setDocRecordInfos(savedDocMap.values());
        return report1BDto;
    }
}
