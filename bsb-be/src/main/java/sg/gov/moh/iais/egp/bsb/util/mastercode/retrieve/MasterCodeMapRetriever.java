package sg.gov.moh.iais.egp.bsb.util.mastercode.retrieve;

import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeView;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MasterCodeMapRetriever implements MasterCodeRetriever {
    private final Map<String, MasterCodeView> codeViewMap;
    private Map<String, MasterCodeView> valueViewMap;
    private Map<String, MasterCodeView> descViewMap;

    public MasterCodeMapRetriever(List<MasterCodeView> dataList) {
        codeViewMap = Maps.newLinkedHashMapWithExpectedSize(dataList.size());
        for (MasterCodeView data : dataList) {
            codeViewMap.put(data.getCode(), data);
        }
    }


    @Override
    public List<MasterCodeView> retrieveAll() {
        return new ArrayList<>(codeViewMap.values());
    }

    @Override
    public MasterCodeView retrieveByCode(String code) {
        return codeViewMap.get(code);
    }

    @Override
    public MasterCodeView retrieveByValue(String value) {
        if (valueViewMap == null) {
            valueViewMap = Maps.newLinkedHashMapWithExpectedSize(codeViewMap.size());
            for (MasterCodeView data : codeViewMap.values()) {
                valueViewMap.put(data.getCodeValue(), data);
            }
        }
        return valueViewMap.get(value);
    }

    @Override
    public MasterCodeView retrieveByDesc(String desc) {
        if (descViewMap == null) {
            descViewMap = Maps.newLinkedHashMapWithExpectedSize(codeViewMap.size());
            for (MasterCodeView data : codeViewMap.values()) {
                descViewMap.put(data.getDescription(), data);
            }
        }
        return descViewMap.get(desc);
    }
}
