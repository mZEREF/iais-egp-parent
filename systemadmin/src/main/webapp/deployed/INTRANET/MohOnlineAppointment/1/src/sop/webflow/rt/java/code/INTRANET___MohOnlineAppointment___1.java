package sop.webflow.rt.java.code;
import com.ecquaria.cloud.helper.EngineHelper;
import sop.webflow.rt.api.BaseProcessClass;

public class INTRANET___MohOnlineAppointment___1 extends BaseProcessClass {

	private static final String DELEGATOR ="appointmentDelegator";

	public void start_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "doStart", this);
	}

	public void prepareDate_OnStepProcess_0() throws Exception {
		EngineHelper.delegate(DELEGATOR, "prepareDate", this);
	}
}
