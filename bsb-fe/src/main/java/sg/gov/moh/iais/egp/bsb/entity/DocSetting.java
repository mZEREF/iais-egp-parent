package sg.gov.moh.iais.egp.bsb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocSetting implements Serializable {
    private String type;
    private String typeDisplay;
    private boolean mandatory;



    public Map<String,DocSetting> getDocScheduleMap(){
        Map<String,DocSetting> settingMap = new HashMap<>();
        MasterCodeConstants.BIOLOGICAL_AGENT_SCHEDULE_TYPE_ALL.forEach(i->{
            if(!MasterCodeConstants.FIFTH_SCHEDULE.equals(i)){
                DocSetting setting = new DocSetting(DocConstants.DOC_TYPE_INVENTORY_AGENT,"Inventory: Biological Agents",true);
                settingMap.put(i,setting);
            }else{
                DocSetting setting = new DocSetting(DocConstants.DOC_TYPE_INVENTORY_TOXIN,"Inventory: Toxins",true);
                settingMap.put(i,setting);
            }
        });
        return settingMap;
    }
}
