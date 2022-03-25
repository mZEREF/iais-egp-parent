package sg.gov.moh.iais.egp.bsb.util.mastercode.retrieve;

import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeView;

import java.util.List;

public interface MasterCodeRetriever {
    List<MasterCodeView> retrieveAll();

    MasterCodeView retrieveByCode(String code);
    MasterCodeView retrieveByValue(String value);
    MasterCodeView retrieveByDesc(String desc);
}
