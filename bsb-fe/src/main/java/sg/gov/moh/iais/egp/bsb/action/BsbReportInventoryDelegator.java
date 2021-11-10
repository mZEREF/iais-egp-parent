package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.entity.DocSetting;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YiMing
 * @version 2021/11/10 9:37
 **/
@Delegator(value = "reportInventoryDelegator")
public class BsbReportInventoryDelegator {

    /**
     * step1
     * */
    public void step1(BaseProcessClass bpc){

    }

    /**
     * prepare
     * */
    public void prepare(BaseProcessClass bpc){

    }

    /**
     * preSubmitFile
     * */
    public void preSubmitFile(BaseProcessClass bpc){

    }

    /**
     * doSubmit
     * */
    public void doSubmit(BaseProcessClass bpc){

    }

    /* Will be removed in future, will get this from config mechanism */
    private List<DocSetting> getRepInventoryDocSettings () {
        List<DocSetting> docSettings = new ArrayList<>(3);
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_BIO_SAFETY_COORDINATOR_CERTIFICATES, "BioSafety Coordinator Certificates", true));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_INVENTORY_FILE, "Inventory File", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_GMAC_ENDORSEMENT, "GMAC Endorsement", false));
        return docSettings;
    }
}
