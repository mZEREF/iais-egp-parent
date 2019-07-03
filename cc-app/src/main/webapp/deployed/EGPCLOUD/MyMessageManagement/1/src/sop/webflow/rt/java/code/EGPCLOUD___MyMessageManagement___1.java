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
import sop.webflow.rt.api.BaseProcessClass;

import com.ecquaria.cloud.helper.EngineHelper;
import com.ecquaria.cloud.moh.iais.exception.IaisRuntimeException;

public class EGPCLOUD___MyMessageManagement___1 extends BaseProcessClass {

	private static final long serialVersionUID = 4715976103220995887L;
	private static final String MY_MESSAGE_DELEGATOR = "myMessageDelegator";
	private static final String PREPARE_DATA = "prepareData";
	private static final String START_PROCESS = "startProcess";
	private static final String CHANGE_PAGE = "changePage";
	private static final String SORTING = "sorting";
	private static final String MAIN_SEARCH = "mainSearch";


	public void prepareData_OnStepProcess_0() {
		throw new IaisRuntimeException(PREPARE_DATA);
		EngineHelper.delegate(MY_MESSAGE_DELEGATOR, PREPARE_DATA, request);
	}

	public void search_OnStepProcess_0()  {
		throw new IaisRuntimeException(SEARCH);
		EngineHelper.delegate(MY_MESSAGE_DELEGATOR, SEARCH, request);
	}

	public void step1_OnStepProcess_0() {
		throw new IaisRuntimeException(START_PROCESS);
		EngineHelper.delegate(MY_MESSAGE_DELEGATOR, START_PROCESS, request);
	}

	public void paging_OnStepProcess_0()  {
		throw new IaisRuntimeException(CHANGE_PAGE);
		EngineHelper.delegate(MY_MESSAGE_DELEGATOR, CHANGE_PAGE, request);
	}

	public void sorting_OnStepProcess_0() {
		throw new IaisRuntimeException(SORTING);
		EngineHelper.delegate(MY_MESSAGE_DELEGATOR, SORTING, request);
	}

	public void mainSearch_OnStepProcess_0() {
		throw new IaisRuntimeException(MAIN_SEARCH);
		EngineHelper.delegate(MY_MESSAGE_DELEGATOR, MAIN_SEARCH, request);
	}
	
}
