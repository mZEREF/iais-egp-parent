package sg.gov.moh.iais.egp.bsb.dto.inspection.followup;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsProcessDto;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class InsNCProcessDto extends InsProcessDto {
    private Map<String, String> ncMohRemarksMap;

    public InsNCProcessDto() {
        ncMohRemarksMap = new HashMap<>();
    }


    private static final String MOH_REMARK = "mohRemark";
    public void reqObjMapping(HttpServletRequest request, List<String> itemList){
        super.reqObjMapping(request);

        for (String item : itemList) {
            String mohRemark = ParamUtil.getString(request, MOH_REMARK+item);
            ncMohRemarksMap.put(item,mohRemark);
        }
    }
}
