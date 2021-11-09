package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.DataSubmissionClient;
import sg.gov.moh.iais.egp.bsb.dto.submission.FacListDto;
import sg.gov.moh.iais.egp.bsb.entity.Biological;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author YiMing
 * @version 2021/11/9 13:49
 **/

@Slf4j
@Delegator("bsbSubmissionCommon")
public class BsbSubmissionCommon {
    private static final String KEY_FACILITY_INFO = "facInfo";
    public static final String KEY_FAC_LIST_DTO = "facListDto";
    public static final String KEY_FAC_ID = "facId";
    private final DataSubmissionClient dataSub;

    public BsbSubmissionCommon(DataSubmissionClient dataSub) {
        this.dataSub = dataSub;
    }


    /**
     * a method to set scheduleType by biological has searched
     * prepareSelectOption
     * */
    public void prepareSelectOption(HttpServletRequest request, String name, List<Biological> b){
        List<SelectOption> selectOptions = null;
        for (Biological b1 : b) {
            selectOptions = new ArrayList<>(b.size());
            selectOptions.add(new SelectOption(b1.getSchedule(),getTextByScheduleType(b1.getSchedule())));
        }
        if(selectOptions != null){
            ParamUtil.setRequestAttr(request,name,selectOptions);
        }else{
            if(log.isInfoEnabled()){
                log.info("The data you passed in caused the query to fail");
            }
        }
    }

    private String getTextByScheduleType(String scheduleType){
        String text = "";
        if(StringUtils.hasLength(scheduleType)){
            switch (scheduleType){
                case "SCHTYPE001":
                    text = "First  Schedule Part I";
                    break;
                case "SCHTYPE002":
                    text = "First Schedule Part II";
                    break;
                case "SCHTYPE003":
                    text = "Second  Schedule";
                    break;
                case "SCHTYPE004":
                    text = "Third  Schedule";
                    break;
                case "SCHTYPE005":
                    text = "Fourth  Schedule";
                    break;
                case "SCHTYPE006":
                    text = "Fifth Schedule";
                    break;
                default:
                    break;
            }
        }else{
            log.error("schedule Type is a null params");
        }
        return text;
    }

    /**
     * a method to FacList by facId,just because facId->FacList is one to one
     * getFacListById
     * @return FacListDto.FacList
     * */
    public FacListDto.FacList getFacListById(HttpServletRequest request, String facId){
        FacListDto facListDto = getFacListDto(request);
        Map<String,FacListDto.FacList> facListMap = facListDto.getFacLists().stream().collect(Collectors.toMap(FacListDto.FacList::getFacId, Function.identity()));
        return facListMap.get(facId);
    }

    public List<Biological> getBiologicalById(HttpServletRequest request){
        String facId = (String) ParamUtil.getSessionAttr(request,KEY_FAC_ID);
        FacListDto.FacList facList = getFacInfo(request);
        if(facList != null && StringUtils.hasLength(facId)){
            return facList.getBioMap().get(facId);
        }else{
            if(log.isInfoEnabled()){
                log.info("facId or facList is null {}", LogUtil.escapeCrlf(facId));
            }
            return Collections.emptyList();
        }
    }

    /**
     * this method is used to get facListDto for facInfo
     * getFacListDto
     * @return FacListDto
     * */
    public FacListDto getFacListDto(HttpServletRequest request){
        FacListDto facListDto = (FacListDto) ParamUtil.getSessionAttr(request,KEY_FAC_LIST_DTO);
        return facListDto == null?getDefaultFacListDto():facListDto;
    }

    private FacListDto getDefaultFacListDto() {
        return dataSub.queryAllApprovalFacList().getEntity();
    }

    public FacListDto.FacList getFacInfo(HttpServletRequest request){
        FacListDto.FacList facList = (FacListDto.FacList) ParamUtil.getSessionAttr(request,KEY_FACILITY_INFO);
        return facList == null?getDefaultFacInfo(request):facList;
    }
    /**
     * get default facInfo by facListDto and facId in session but need facListDto first
     * getDefaultFacInfo
     * @return FacListDto.FacList
     * */
    private FacListDto.FacList getDefaultFacInfo(HttpServletRequest request){
       FacListDto facListDto = getFacListDto(request);
       String facId = (String) ParamUtil.getSessionAttr(request,KEY_FAC_ID);
       if(StringUtils.hasLength(facId) && facListDto != null){
           return getFacListById(request,facId);
       }else{
           if(log.isInfoEnabled()){
               log.info("The facId is empty or the facListDto is empty");
           }
           return null;
       }
    }

}
