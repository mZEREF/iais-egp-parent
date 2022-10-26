package sg.gov.moh.iais.egp.bsb.dto.file;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PrimaryDocDto implements Serializable{
    /* docs already saved in DB, key is repoId */
    private Map<String, DocRecordInfo> savedDocMap;
    @Getter
    @Setter
    @JsonIgnore
    private boolean pvRf;
    private Map<Integer, String> facilityNameIndexMap;

    public PrimaryDocDto() {
        savedDocMap = new LinkedHashMap<>();
    }

    /**
     * get a structure used to display the already saved docs
     * we have not retrieve data of these docs yet, if user wants to download it, we call API to retrieve the data
     * @return a map, the key is the doc type, the value is the exist doc info list
     */
    public Map<String, List<DocRecordInfo>> getExistDocTypeMap() {
        if(pvRf){
            return getPvRfExistDocTypeMap();
        } else {
            return CollectionUtils.groupCollectionToMap(DocRecordInfo::getDocType, this.savedDocMap.values());
        }
    }

    private Map<String, List<DocRecordInfo>> getPvRfExistDocTypeMap(){
        Map<String, Integer> exchangeMap = Maps.newLinkedHashMapWithExpectedSize(this.facilityNameIndexMap.size());
        for (Map.Entry<Integer, String> entry : this.facilityNameIndexMap.entrySet()) {
            exchangeMap.put(entry.getValue(), entry.getKey());
        }
        Map<String, List<DocRecordInfo>> docRecordInfoKeyMap = Maps.newLinkedHashMapWithExpectedSize(this.savedDocMap.size());
        List<DocRecordInfo> docRecordInfos = new ArrayList<>(this.savedDocMap.values());
        for (DocRecordInfo recordInfo : docRecordInfos) {
            String key;
            if(DocConstants.DOC_TYPE_PV_INVENTORY_REPORTING_FORM.equals(recordInfo.getDocType())){
                Integer index = exchangeMap.get(recordInfo.getDocSubType());
                key = DocConstants.DOC_TYPE_PV_INVENTORY_REPORTING_FORM + "--v--" + index;
                List<DocRecordInfo> reportFormDoc = docRecordInfoKeyMap.get(key);
                if(reportFormDoc == null || reportFormDoc.isEmpty()){
                    reportFormDoc = new ArrayList<>();
                }
                reportFormDoc.add(recordInfo);
                docRecordInfoKeyMap.put(key, reportFormDoc);
            } else {
                key = recordInfo.getDocType();
                List<DocRecordInfo> otherDoc = docRecordInfoKeyMap.get(key);
                if(otherDoc == null || otherDoc.isEmpty()){
                    otherDoc = new ArrayList<>();
                }
                otherDoc.add(recordInfo);
                docRecordInfoKeyMap.put(key, otherDoc);
            }
        }
        return docRecordInfoKeyMap;
    }

    public Map<String, DocRecordInfo> getSavedDocMap() {
        return savedDocMap;
    }

    public void setSavedDocMap(Map<String, DocRecordInfo> savedDocMap) {
        this.savedDocMap = savedDocMap;
    }


    public Map<Integer, String> getFacilityNameIndexMap() {
        return facilityNameIndexMap;
    }

    public void setFacilityNameIndexMap(Map<Integer, String> facilityNameIndexMap) {
        this.facilityNameIndexMap = facilityNameIndexMap;
    }
}
