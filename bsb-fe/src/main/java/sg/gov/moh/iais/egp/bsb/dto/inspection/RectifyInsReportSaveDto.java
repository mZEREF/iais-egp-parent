package sg.gov.moh.iais.egp.bsb.dto.inspection;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YiMing
 * @version 2022/2/11 17:06
 **/
public class RectifyInsReportSaveDto implements Serializable{
    private String appId;
    private String configId;
    private List<DocRecordInfo> attachmentList;
    private List<RectifyItemSaveDto> itemSaveDtoList;

    @Data
    //dto used to save all remarks from jsp
    public static class RectifyItemSaveDto implements Serializable {
        private String itemValue;
        private String remarks;
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

    public void reqObjMapping(HttpServletRequest request,String itemValue){
        //add remark into dto
        RectifyItemSaveDto dto = new RectifyItemSaveDto();
        dto.setItemValue(itemValue);
        dto.setRemarks(ParamUtil.getString(request,"remarks"));
        this.itemSaveDtoList.add(dto);
    }
}
