package sop.webflow.rt.java.code;
import com.ecquaria.cloud.helper.EngineHelper;
import sop.webflow.rt.api.BaseProcessClass;

public class INTRANET___MohCleanUserCalendar___1 extends BaseProcessClass {

	private static final String DELEGATOR ="cleanCalandarDelegator";
	
	public void doCleanCalandarStep_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doStart", this);
	}

}
