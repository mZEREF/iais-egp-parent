package sg.gov.moh.iais.egp.bsb.util.mastercode.retrieve;

import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeView;

import java.util.List;

public class MasterCodeListRetriever implements MasterCodeRetriever {
    private final List<MasterCodeView> dataList;

    public MasterCodeListRetriever(List<MasterCodeView> dataList) {
        this.dataList = dataList;
    }

    @Override
    public List<MasterCodeView> retrieveAll() {
        return dataList;
    }

    @Override
    public MasterCodeView retrieveByCode(String code) {
        if (code == null) {
            return null;
        }
        MasterCodeView result = null;
        for (MasterCodeView data : dataList) {
            if (code.equals(data.getCode())) {
                result = data;
                break;
            }
        }
        return result;
    }

    @Override
    public MasterCodeView retrieveByValue(String value) {
        if (value == null) {
            return null;
        }
        MasterCodeView result = null;
        for (MasterCodeView data : dataList) {
            if (value.equals(data.getCodeValue())) {
                result = data;
                break;
            }
        }
        return result;
    }

    @Override
    public MasterCodeView retrieveByDesc(String desc) {
        if (desc == null) {
            return null;
        }
        MasterCodeView result = null;
        for (MasterCodeView data : dataList) {
            if (desc.equals(data.getDescription())) {
                result = data;
                break;
            }
        }
        return result;
    }
}
