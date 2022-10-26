package sg.gov.moh.iais.egp.bsb.dto.register.approval;

import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.common.edit.FieldEditableJudger;
import sg.gov.moh.iais.egp.bsb.common.node.simple.BranchedValidationNodeValue;
import sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants;;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FacAuthorisedDto implements BranchedValidationNodeValue {
    private List<AuthorisedSelection> facAuthorisedSelections;

    //when user do sp rfi,turn checkbox to unChecked and this data is stored in database
    /*condition first step,this data is selected and recorded into database
    second step,user turn this checkboxes to unchecked
    finally, submit data and record into database*/
    private Set<String> spHandleAuthDeleteIdSet;

    @JsonIgnore
    //this map is used to prepare echo data for page which clicked as 'is performing second schedule bat'
    //key is authorisedPerId
    private  Map<String, AuthorisedSelection> authorisedSelectionMap;


    @JsonIgnore
    private ValidationResultDto validationResultDto;
    @JsonIgnore
    private String validationProfile;

    public FacAuthorisedDto() {
        //original authorisedSelection size is 1;
        facAuthorisedSelections = new ArrayList<>();
        AuthorisedSelection authorisedSelection = new AuthorisedSelection();
        facAuthorisedSelections.add(authorisedSelection);

        //begin to originate authorisedSelectionMap
        //linkHashMap contain sequence
        authorisedSelectionMap = new LinkedHashMap<>();
    }
    @Override
    public String getValidationProfile() {
        return validationProfile;
    }

    @Override
    public void setValidationProfile(String profile) {
        this.validationProfile = profile;
    }

    public void clearFacAuthorisedSelections(){
        this.facAuthorisedSelections.clear();
    }

    public Map<String, AuthorisedSelection> getAuthorisedSelectionMap(){
        return CollectionUtils.uniqueIndexMap(AuthorisedSelection::getAuthorisedPerId, facAuthorisedSelections);
    }

    public List<String> getAuthorisedPersonIdList(){
        return facAuthorisedSelections.stream().map(AuthorisedSelection::getAuthorisedPerId).collect(Collectors.toList());
    }

    @Override
    public boolean doValidation() {
        //TO DO
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod(ApprovalBatAndActivityConstants.FEIGN_CLIENT, "validateFacilityAuthoriser", new Object[]{this});
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

    private static final String SEPARATOR                                     = "--v--";
    private static final String ALL_INDEXES                                   = "authPersonListSize";
    private static final String FAC_AUTHORISED_PERSON_ID                      = "authorisedPerId";
    private static final String AUTH_ID_NUMBER                                = "authIdNumber";
    private static final String SP_HANDLE_AUTH_ID                             = "maskedSpHandleAuthId";
    private static final String WORK_INVOLVED_SECOND_SCHEDULE_BAT             = "involvedWork";
    private static final String PERFORMING_FOR_SECOND_SCHEDULE_BAT            = "isPerformed";
    private static final String KEY_UNMASK_FAC_AUTHORISED_PERSON_ID           = "authId";
    private static final String KEY_UNMASK_SP_HANDLE_AUTH_ID                  = "spAuthId";



    public void reqObjMapping(HttpServletRequest request, FieldEditableJudger editableJudger) {
        //reset fac authorised person list
        clearFacAuthorisedSelections();

        //get all authorised person list size - indexes
        int indexes = ParamUtil.getInt(request,ALL_INDEXES);

        //dismiss Null Pointer due to authorised person should not be empty
        for (int i = 0; i < indexes; i++) {
            //choose or not
            String isPerformed = ParamUtil.getString(request, PERFORMING_FOR_SECOND_SCHEDULE_BAT + SEPARATOR + i);
            //old data or not
            String maskedSpHandleAuthId = ParamUtil.getString(request, SP_HANDLE_AUTH_ID+SEPARATOR+i);
            String spHandleAuthId = MaskUtil.unMaskValue(KEY_UNMASK_SP_HANDLE_AUTH_ID,maskedSpHandleAuthId);
            if(StringUtils.hasLength(spHandleAuthId) && !maskedSpHandleAuthId.equals(spHandleAuthId)) {
                if("Y".equals(isPerformed)){
                   this.facAuthorisedSelections.add(retrieveAuthorisedSelection(true,spHandleAuthId,i,request,editableJudger));
                } else {
                    this.spHandleAuthDeleteIdSet.add(spHandleAuthId);
                }
            } else {
                // do new
                if("Y".equals(isPerformed)){
                    this.facAuthorisedSelections.add(retrieveAuthorisedSelection(false,null,i,request,editableJudger));
                }
            }

        }
    }

    /**
     * this method is used to prepare Dto {@link AuthorisedSelection}
     * @param isOld true: data is loading from database;false: data is storing first time
     * @param spHandleAuthId The ID of Dto AuthorisedSelection{@link AuthorisedSelection},when it is recorded into database
     * @param index the index of {@code List}<{@link sg.gov.moh.iais.egp.bsb.dto.entity.FacilityAuthoriserDto}>
     */
    private AuthorisedSelection retrieveAuthorisedSelection(boolean isOld, String spHandleAuthId, int index, HttpServletRequest request, FieldEditableJudger editableJudger){
        //prepare all key need to save

        //prepare authorised person id- facility authorised person id seeked by application id
        //begin------------------------
        String maskedAuthorisedPerId  = ParamUtil.getString(request, FAC_AUTHORISED_PERSON_ID+SEPARATOR+index);
        String authorisedPerId = "";
        if(StringUtils.hasLength(maskedAuthorisedPerId)){
           String unMaskId = MaskUtil.unMaskValue(KEY_UNMASK_FAC_AUTHORISED_PERSON_ID, maskedAuthorisedPerId);
           if(StringUtils.hasLength(unMaskId) && !maskedAuthorisedPerId.equals(unMaskId)){
               authorisedPerId = unMaskId;
           }
        }
        //end---------------------------

        String involvedWork = "";
        String isPerformed = "";
        if (editableJudger.editable(PERFORMING_FOR_SECOND_SCHEDULE_BAT)) {
            involvedWork = ParamUtil.getString(request, WORK_INVOLVED_SECOND_SCHEDULE_BAT + SEPARATOR + index);
        }
        if (editableJudger.editable(PERFORMING_FOR_SECOND_SCHEDULE_BAT)) {
            isPerformed = ParamUtil.getString(request, PERFORMING_FOR_SECOND_SCHEDULE_BAT + SEPARATOR + index);
        }

        AuthorisedSelection  authorisedSelection = new AuthorisedSelection();
        authorisedSelection.setAuthorisedPerId(authorisedPerId);
        authorisedSelection.setIsPerformed(isPerformed);
        authorisedSelection.setInvolvedWork(involvedWork);
        authorisedSelection.setCurrentIndex(String.valueOf(index));
        if(isOld){
            authorisedSelection.setSpHandleAuthId(spHandleAuthId);
        }
        return authorisedSelection;
    }

}
