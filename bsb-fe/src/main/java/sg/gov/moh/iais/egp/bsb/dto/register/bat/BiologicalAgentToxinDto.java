package sg.gov.moh.iais.egp.bsb.dto.register.bat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.RequestObjectMappingUtil;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class BiologicalAgentToxinDto extends ValidatableNodeValue {
    private String activityEntityId;
    private String activityType;

    private List<BATInfo> batInfos;


    @JsonIgnore
    private ValidationResultDto validationResultDto;


    public BiologicalAgentToxinDto() {
        batInfos = new ArrayList<>();
        batInfos.add(new BATInfo());
    }

    public BiologicalAgentToxinDto(String activityType) {
        this();
        this.activityType = activityType;
    }


    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("facRegFeignClient", "validateFacilityBiologicalAgentToxin", new Object[]{this});
        return validationResultDto.isPass();
    }

    @Override
    public String retrieveValidationResult() {
        if (this.validationResultDto == null) {
            throw new IllegalStateException("This DTO is not validated");
        }
        return this.validationResultDto.toErrorMsg();
    }

    @Override
    public void clearValidationResult() {
        this.validationResultDto = null;
    }



    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityEntityId() {
        return activityEntityId;
    }

    public void setActivityEntityId(String activityEntityId) {
        this.activityEntityId = activityEntityId;
    }

    public List<BATInfo> getBatInfos() {
        return new ArrayList<>(batInfos);
    }

    public void clearBatInfos() {
        this.batInfos.clear();
    }

    public void addBatInfo(BATInfo info) {
        this.batInfos.add(info);
    }

    public void setBatInfos(List<BATInfo> batInfos) {
        this.batInfos = new ArrayList<>(batInfos);
    }



//    ---------------------------- request -> object ----------------------------------------------

    private static final String KEY_SECTION_IDXES                       = "sectionIdx";
    private static final String KEY_DELETED_SECTION_IDXES               = "deletedSectionIdx";


    @SneakyThrows({InstantiationException.class, IllegalAccessException.class})
    public void reqObjMapping(HttpServletRequest request) {
        String idxes = request.getParameter(KEY_SECTION_IDXES);
        Set<Integer> idxSet = StringUtils.hasLength(idxes) ? Arrays.stream(idxes.trim().split(" +")).map(Integer::valueOf).collect(Collectors.toSet()) : Collections.emptySet();
        String deletedIdxes = request.getParameter(KEY_DELETED_SECTION_IDXES);
        Set<Integer> deletedIdxSet = StringUtils.hasLength(deletedIdxes) ? Arrays.stream(deletedIdxes.trim().split(" +")).map(Integer::valueOf).collect(Collectors.toSet()) : Collections.emptySet();

        if (idxSet.isEmpty()) {
            clearBatInfos();
            batInfos.add(new BATInfo());
        } else {
            Map<Integer, BATInfo> map = RequestObjectMappingUtil.readAndReuseSectionDto(BATInfo.class, batInfos, idxSet, deletedIdxSet);
            map.forEach((k, v) -> v.reqObjMapping(request, k.toString()));
            this.batInfos = new ArrayList<>(map.values());
        }
    }
}
