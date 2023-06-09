/**
 * Generated by SIT
 *
 * Feel free to add  methods  or comments. The content of this 
 * file will be kept as-is when committed.
 *
 * Extending this  class is not recommended , since the class-
 * name will change together with the version. Calling methods
 * from external code is not recommended as well , for similar
 * reasons.
 */
package sop.webflow.rt.java.code;
import com.ecquaria.cloud.helper.EngineHelper;
import sop.webflow.rt.api.BaseProcessClass;

public class INTERNET___MohApptUserChooseDate___1 extends BaseProcessClass {

	private static final long serialVersionUID = 4944373852088223808L;
	private static final String DELEGATOR ="apptConfirmReSchDateDelegator";

	public void apptUserChooseDateStart_OnStepProcess_0() throws Exception {
	// 		apptUserChooseDateStart->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "apptUserChooseDateStart", this);
	}

	public void apptUserChooseDateInit_OnStepProcess_0() throws Exception {
	// 		apptUserChooseDateInit->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "apptUserChooseDateInit", this);
	}

	public void apptUserChooseDatePre_OnStepProcess_0() throws Exception {
	// 		apptUserChooseDatePre->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "apptUserChooseDatePre", this);
	}

	public void apptUserChooseDateVali_OnStepProcess_0() throws Exception {
	// 		apptUserChooseDateVali->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "apptUserChooseDateVali", this);
	}

	public void apptUserChooseDateStep1_OnStepProcess_0() throws Exception {
	// 		apptUserChooseDateStep1->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "apptUserChooseDateStep1", this);
	}

	public void apptUserChooseDateSuccess_OnStepProcess_0() throws Exception {
	// 		apptUserChooseDateSuccess->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "apptUserChooseDateSuccess", this);
	}

	public void apptUserChooseDateReject_OnStepProcess_0() throws Exception {
	// 		apptUserChooseDateReject->OnStepProcess
		EngineHelper.delegate(DELEGATOR, "apptUserChooseDateReject", this);
	}

}
