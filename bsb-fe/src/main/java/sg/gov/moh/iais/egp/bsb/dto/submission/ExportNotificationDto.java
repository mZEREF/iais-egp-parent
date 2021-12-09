package sg.gov.moh.iais.egp.bsb.dto.submission;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocMeta;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;
import sop.servlet.webflow.HttpHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static sg.gov.moh.iais.egp.bsb.constant.DataSubmissionConstants.*;

/**
 * @author Zhu Tangtang
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExportNotificationDto implements Serializable{
    @Data
    public static class ExportNot implements Serializable {
        private String scheduleType;
        private String bat;
        private String transferType;
        private String transferQty;
        private String meaUnit;

        @JsonIgnore
        private List<PrimaryDocDto.NewDocInfo> newDocInfos;
        @JsonIgnore
        private String docType;
        @JsonIgnore
        private String repoIdNewString;

        public ExportNot() {
            this.newDocInfos = new ArrayList<>();
        }

        public List<PrimaryDocDto.NewDocInfo> getNewInfos(){
            return new ArrayList<>(this.newDocInfos);
        }

        public void setNewInfos(List<PrimaryDocDto.NewDocInfo> newDocInfos){
            this.newDocInfos = new ArrayList<>(newDocInfos);
        }
    }

    private String facId;
    private String receivedFacility;
    private String receivedCountry;
    private String exportDate;
    private String provider;
    private String flightNo;
    private String remarks;
    private String ensure;

    private List<ExportNot> exportNotList;
    private List<PrimaryDocDto.NewDocInfo> otherNewInfos;
    private Map<String, PrimaryDocDto.NewDocInfo> allNewDocInfos;
    private Map<String,PrimaryDocDto.DocRecordInfo> savedDocInfos;
    private List<DocMeta> docMetaInfos;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public ExportNotificationDto() {
        exportNotList = new ArrayList<>();
        exportNotList.add(new ExportNot());
        docMetaInfos = new ArrayList<>();
        otherNewInfos = new ArrayList<>();
        allNewDocInfos = new LinkedHashMap<>();
        savedDocInfos = new LinkedHashMap<>();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExportNotNeed{
        private String scheduleType;
        private String bat;
        private String transferType;
        private String transferQty;
        private String meaUnit;
    }

    @Data
    @NoArgsConstructor
    public static class ExportNotNeedR{
        private List<ExportNotNeed> needList;
        private String facId;
        private String receivedFacility;
        private String receivedCountry;
        private String exportDate;
        private String provider;
        private String flightNo;
        private String remarks;
        private String ensure;
        private List<PrimaryDocDto.DocRecordInfo> docInfos;
        private List<DocMeta> docMetas;
    }

    public String getFacId() {
        return facId;
    }

    public void setFacId(String facId) {
        this.facId = facId;
    }

    public String getReceivedFacility() {
        return receivedFacility;
    }

    public void setReceivedFacility(String receivedFacility) {
        this.receivedFacility = receivedFacility;
    }

    public String getReceivedCountry() {
        return receivedCountry;
    }

    public void setReceivedCountry(String receivedCountry) {
        this.receivedCountry = receivedCountry;
    }

    public String getExportDate() {
        return exportDate;
    }

    public void setExportDate(String exportDate) {
        this.exportDate = exportDate;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getEnsure() {
        return ensure;
    }

    public void setEnsure(String ensure) {
        this.ensure = ensure;
    }

    public List<ExportNot> getExportNotList() {
        return exportNotList;
    }

    public void clearExportLists(){
        this.exportNotList.clear();
    }

    public void addExportLists(ExportNot exportNot){
        this.exportNotList.add(exportNot);
    }

    public void setExportNotList(List<ExportNot> exportNotList) {
        this.exportNotList = exportNotList;
    }

    public List<PrimaryDocDto.NewDocInfo> getOtherNewInfos() {
        return new ArrayList<>(this.otherNewInfos);
    }

    public void setOtherNewInfos(List<PrimaryDocDto.NewDocInfo> otherNewInfos) {
        this.otherNewInfos = new ArrayList<>(otherNewInfos);
    }

    public Map<String, PrimaryDocDto.NewDocInfo> getAllNewDocInfos() {
        return allNewDocInfos;
    }

    public void setAllNewDocInfos(Map<String, PrimaryDocDto.NewDocInfo> allNewDocInfos) {
        this.allNewDocInfos = allNewDocInfos;
    }

    public List<DocMeta> getDocMetaInfos() {
        return docMetaInfos;
    }

    public void setDocMetaInfos(List<DocMeta> docMetaInfos) {
        this.docMetaInfos = docMetaInfos;
    }

    public void addDocMetaInfos(DocMeta docMeta){
        this.docMetaInfos.add(docMeta);
    }

    public Map<String, PrimaryDocDto.DocRecordInfo> getSavedDocInfos() {
        return savedDocInfos;
    }

    public void setSavedDocInfos(Map<String, PrimaryDocDto.DocRecordInfo> savedDocInfos) {
        this.savedDocInfos = savedDocInfos;
    }

    /**
     * This method is for downloading and contains all file information sorted by tmpId
     *getAllNewDocInfo
     * */
    public void fillAllNewDocInfo(){
        if(!CollectionUtils.isEmpty(this.exportNotList)){
            List<PrimaryDocDto.NewDocInfo> newDocInfos = exportNotList.stream().flatMap(i->i.getNewDocInfos().stream()).collect(Collectors.toList());
            newDocInfos.addAll(this.otherNewInfos);
            this.allNewDocInfos = newDocInfos.stream().collect(Collectors.toMap(PrimaryDocDto.NewDocInfo::getTmpId, Function.identity()));
        }
    }

    public void getDocMetaInfoFromNew(){
        this.allNewDocInfos.values().forEach(i -> {
            DocMeta docMeta = new DocMeta(i.getTmpId(), i.getDocType(), i.getFilename(), i.getSize(), "dataSub");
            addDocMetaInfos(docMeta);
        });
    }

    /**
     * this method is used to consume useful data by feign
     * setExportNotNeedR
     * @return ExportNotNeedR
     * */
    public ExportNotNeedR getExportNotNeedR(){
        List<ExportNotNeed> exportNotNeeds = exportNotList.stream().map(t->{
            ExportNotNeed exportNotNeed = new ExportNotNeed();
            exportNotNeed.setScheduleType(t.getScheduleType());
            exportNotNeed.setBat(t.getBat());
            exportNotNeed.setTransferType(t.getTransferType());
            exportNotNeed.setTransferQty(t.getTransferQty());
            exportNotNeed.setMeaUnit(t.getMeaUnit());
            return exportNotNeed; }).collect(Collectors.toList());
        ExportNotNeedR exportNotNeedR = new ExportNotNeedR();
        exportNotNeedR.setNeedList(exportNotNeeds);
        exportNotNeedR.setReceivedFacility(this.receivedFacility);
        exportNotNeedR.setReceivedCountry(this.receivedCountry);
        exportNotNeedR.setExportDate(this.exportDate);
        exportNotNeedR.setProvider(this.provider);
        exportNotNeedR.setFlightNo(this.flightNo);
        exportNotNeedR.setRemarks(this.remarks);
        exportNotNeedR.setFacId(this.facId);
        exportNotNeedR.setEnsure(this.ensure);
        exportNotNeedR.setDocInfos(new ArrayList<>(savedDocInfos.values()));
        exportNotNeedR.setDocMetas(this.docMetaInfos);
        return exportNotNeedR;
    }

    /**
     * This method will put new added files to the important data structure which is used to update the FacilityDoc.
     * This file is called when new uploaded files are saved and we get the repo Ids.
     * ATTENTION!!!
     * This method is dangerous! The relationship between the ids and the files in this dto is fragile!
     * We rely on the order is not changed! So we use a LinkedHashMap to save our data.
     * <p>
     * This method will generate id-bytes pairs at the same time, the result will be used to sync files to BE.
     * @return a list of file data to be synchronized to BE
     */
    public List<NewFileSyncDto> newFileSaved(List<String> repoIds) {
        Iterator<String> repoIdIt = repoIds.iterator();
        Iterator<PrimaryDocDto.NewDocInfo> newDocIt = allNewDocInfos.values().iterator();

        List<NewFileSyncDto> newFileSyncDtoList = new ArrayList<>(repoIds.size());
        while (repoIdIt.hasNext() && newDocIt.hasNext()) {
            String repoId = repoIdIt.next();
            PrimaryDocDto.NewDocInfo newDocInfo = newDocIt.next();
            PrimaryDocDto.DocRecordInfo docRecordInfo = new PrimaryDocDto.DocRecordInfo();
            docRecordInfo.setDocType(newDocInfo.getDocType());
            docRecordInfo.setFilename(newDocInfo.getFilename());
            docRecordInfo.setSize(newDocInfo.getSize());
            docRecordInfo.setRepoId(repoId);
            docRecordInfo.setSubmitBy(newDocInfo.getSubmitBy());
            docRecordInfo.setSubmitDate(newDocInfo.getSubmitDate());
            savedDocInfos.put(repoId, docRecordInfo);

            NewFileSyncDto newFileSyncDto = new NewFileSyncDto();
            newFileSyncDto.setId(repoId);
            newFileSyncDto.setData(newDocInfo.getMultipartFile().getBytes());
            newFileSyncDtoList.add(newFileSyncDto);
        }
        return newFileSyncDtoList;
    }


    //------------------------------------------Validation---------------------------------------------

    public boolean doValidation() {
        ExportNotNeedR exportNotNeedR =getExportNotNeedR();
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("dataSubmissionFeignClient", "validateExportNot", new Object[]{exportNotNeedR});
        return validationResultDto.isPass();
    }

    public String retrieveValidationResult() {
        if (this.validationResultDto == null) {
            throw new IllegalStateException("This DTO is not validated");
        }
        return this.validationResultDto.toErrorMsg();
    }

    public void clearValidationResult() {
        this.validationResultDto = null;
    }

    /**
     * This method is for JSP shows and contains all file information sorted by type
     * getAllDocMetaByDocType
     * @return Map<String,List<DocMeta>>
     * */
    public Map<String,List<DocMeta>> getAllDocMetaByDocType(){
        return sg.gov.moh.iais.egp.bsb.util.CollectionUtils.groupCollectionToMap(docMetaInfos,DocMeta::getDocType);
    }

    /**
     * reqObjectMapping
     * get value from request
     * */
    public void reqObjectMapping(HttpServletRequest request){
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String idxes = ParamUtil.getString(request, KEY_SECTION_IDXES);
        clearExportLists();
        String[] idxArr = idxes.trim().split(" +");
        for (String idx : idxArr) {
            ExportNot exportNot = new ExportNot();
            String scheduleType = ParamUtil.getString(request, KEY_PREFIX_SCHEDULE_TYPE + SEPARATOR +idx);
            exportNot.setScheduleType(scheduleType);
            exportNot.setBat(ParamUtil.getString(request,KEY_PREFIX_BAT+ SEPARATOR+idx));
            exportNot.setTransferType(ParamUtil.getString(request,KEY_PREFIX_TRANSFER_TYPE+ SEPARATOR+idx));
            exportNot.setTransferQty(ParamUtil.getString(request,KEY_PREFIX_TRANSFER_QTY + SEPARATOR+idx));
            exportNot.setMeaUnit(ParamUtil.getString(request, KEY_PREFIX_MEASUREMENT_UNIT+ SEPARATOR+idx));

            PrimaryDocDto primaryDocDto = new PrimaryDocDto();
            primaryDocDto.reqObjMapping(mulReq,request,getDocType(scheduleType),String.valueOf(idx));
            exportNot.setDocType(getDocType(scheduleType));
            //joint repoId exist
            String newRepoId = String.join(",", primaryDocDto.getNewDocMap().keySet());
            exportNot.setRepoIdNewString(newRepoId);
            //set newDocFiles
            exportNot.setNewDocInfos(primaryDocDto.getNewDocTypeList());
            //set need Validation value
            addExportLists(exportNot);
        }
        PrimaryDocDto primaryDocDto = new PrimaryDocDto();
        primaryDocDto.reqOtherMapping(mulReq,request,"others");
        this.setOtherNewInfos(primaryDocDto.getNewDocTypeList());
        //get all new doc
        fillAllNewDocInfo();
        //get all
        getDocMetaInfoFromNew();
        this.setReceivedFacility(ParamUtil.getString(request,KEY_PREFIX_RECEIVED_FACILITY));
        this.setReceivedCountry(ParamUtil.getString(request,KEY_PREFIX_RECEIVED_COUNTRY));
        this.setExportDate(ParamUtil.getString(request,KEY_PREFIX_EXPORT_DATE));
        this.setProvider(ParamUtil.getString(request,KEY_PREFIX_PROVIDER));
        this.setFlightNo(ParamUtil.getString(request,KEY_PREFIX_FLIGHT_NO));
        this.setRemarks(ParamUtil.getString(request,KEY_PREFIX_REMARKS));
        this.setFacId((String) ParamUtil.getSessionAttr(request,KEY_FAC_ID));
    }

    public String getDocType(String scheduleType){
        String docType = "";
        if(StringUtils.hasLength(scheduleType)){
            switch (scheduleType){
                case MasterCodeConstants.FIRST_SCHEDULE_PART_I:
                case MasterCodeConstants.FIRST_SCHEDULE_PART_II:
                case MasterCodeConstants.SECOND_SCHEDULE:
                case MasterCodeConstants.THIRD_SCHEDULE:
                case MasterCodeConstants.FOURTH_SCHEDULE:
                    docType = KEY_DOC_TYPE_INVENTORY_BAT;
                    break;
                case MasterCodeConstants.FIFTH_SCHEDULE:
                    docType = KEY_DOC_TYPE_INVENTORY_TOXIN;
                    break;
                default:
                    break;
            }
        }
        return docType;
    }
}
