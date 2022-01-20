package sg.gov.moh.iais.egp.bsb.dto.followup;

import lombok.Data;
import org.springframework.util.CollectionUtils;
import sg.gov.moh.iais.egp.bsb.dto.file.CommonDocDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocMeta;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author YiMing
 * @version 2022/1/11 14:16
 **/
@Data
public class Followup1BMetaDto {
    private FollowupInfoBDto infoBDto;
    private CommonDocDto.DocsMetaDto docsMetaDto;


    public static Followup1BMetaDto retrieve1BMetaData(FollowupInfoBDto followupInfoBDto,CommonDocDto followupDoc){
        Followup1BMetaDto followup1BMetaDto = new Followup1BMetaDto();
        followup1BMetaDto.setInfoBDto(followupInfoBDto);
        Map<String, NewDocInfo> newDocInfoMap = followupDoc.getNewDocMap();
        Map<String, DocRecordInfo> savedDocInfoMap = followupDoc.getSavedDocMap();
        if(CollectionUtils.isEmpty(newDocInfoMap) && CollectionUtils.isEmpty(savedDocInfoMap)){
            return followup1BMetaDto;
        }
        List<DocMeta> metaDtoList = new ArrayList<>(newDocInfoMap.size()+savedDocInfoMap.size());
        savedDocInfoMap.values().forEach(i -> {
            DocMeta docMeta = new DocMeta(i.getRepoId(), i.getDocType(), i.getFilename(), i.getSize(), "facReg");
            metaDtoList.add(docMeta);
        });
        newDocInfoMap.values().forEach(i -> {
            DocMeta docMeta = new DocMeta(i.getTmpId(), i.getDocType(), i.getFilename(), i.getSize(), "followup");
            metaDtoList.add(docMeta);
        });
        Map<String, List<DocMeta>> metaDtoMap = sg.gov.moh.iais.egp.bsb.util.CollectionUtils.groupCollectionToMap(metaDtoList, DocMeta::getDocType);
        CommonDocDto.DocsMetaDto newDocsMetaDto = new CommonDocDto.DocsMetaDto();
        newDocsMetaDto.setMetaDtoMap(metaDtoMap);
        followup1BMetaDto.setDocsMetaDto(newDocsMetaDto);
        return followup1BMetaDto;
    }
}
