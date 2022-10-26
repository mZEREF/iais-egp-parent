package sg.gov.moh.iais.egp.bsb.dto.inspection;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author YiMing
 * @version 2022/2/11 17:06
 **/
public class RectifyInsReportSaveDto implements Serializable{
    private String appId;
    private String configId;
    private List<DocRecordInfo> attachmentList;
    private List<RectifyItemSaveDto> itemSaveDtoList;
    private Set<String> toBeDeletedDocIds;

    @Data
    //dto used to save all remarks from jsp
    public static class RectifyItemSaveDto implements Serializable {
        private String itemValue;
        private String remarks;
        private String rectified;
    }

    public RectifyInsReportSaveDto() {
        this.attachmentList = new ArrayList<>();
        this.itemSaveDtoList = new ArrayList<>();
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public List<DocRecordInfo> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<DocRecordInfo> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public List<RectifyItemSaveDto> getItemSaveDtoList() {
        return itemSaveDtoList;
    }

    public void setItemSaveDtoList(List<RectifyItemSaveDto> itemSaveDtoList) {
        this.itemSaveDtoList = itemSaveDtoList;
    }

    public Set<String> getToBeDeletedDocIds() {
        return toBeDeletedDocIds;
    }

    public void setToBeDeletedDocIds(Set<String> toBeDeletedDocIds) {
        this.toBeDeletedDocIds = toBeDeletedDocIds;
    }
}
