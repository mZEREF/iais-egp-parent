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
import com.ecquaria.egp.core.application.controller.ApplicationViewController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sop.webflow.rt.api.BaseProcessClass;


public class EGPCLOUD___DownloadSupportDocument_MC___1 extends BaseProcessClass {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public void prepareSupport_OnStepProcess_0 () throws Exception {
		logger.debug("Down");
		ApplicationViewController.downSupportDocInMC(request, response);;
		logger.debug("Load");
	}

}
