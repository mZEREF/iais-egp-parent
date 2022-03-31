<%@tag description="Preview page of facility registration" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>

<%@attribute name="compProfile" required="true" type="sg.gov.moh.iais.egp.bsb.dto.info.common.OrgAddressInfo" %>
<%@attribute name="facProfile" required="true" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityProfileDto" %>
<%@attribute name="facOperator" required="true" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityOperatorDto" %>
<%@attribute name="facAuth" required="true" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAuthoriserDto" %>
<%@attribute name="facAdminOfficer" required="true" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAdminAndOfficerDto" %>
<%@attribute name="facCommittee" required="true" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityCommitteeDto" %>
<%@attribute name="batList" required="false" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.register.facility.BiologicalAgentToxinDto>" %>
<%@attribute name="afc" required="false" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAfcDto" %>
<%@attribute name="docFrag" fragment="true" %>
<%@attribute name="editFrag" fragment="true" %>
<%@attribute name="containsBatListJudge" type="java.lang.Boolean" %>
<%@attribute name="containsAfcJudge" type="java.lang.Boolean" %>
<%@attribute name="profileEditJudge" type="java.lang.Boolean" %>
<%@attribute name="operatorEditJudge" type="java.lang.Boolean" %>
<%@attribute name="authorisedEditJudge" type="java.lang.Boolean" %>
<%@attribute name="adminEditJudge" type="java.lang.Boolean" %>
<%@attribute name="officerEditJudge" type="java.lang.Boolean" %>
<%@attribute name="committeeEditJudge" type="java.lang.Boolean" %>
<%@attribute name="batListEditJudge" type="java.lang.Boolean" %>
<%@attribute name="otherAppInfoEditJudge" type="java.lang.Boolean" %>
<%@attribute name="docEditJudge" type="java.lang.Boolean" %>
<%@attribute name="afcEditJudge" type="java.lang.Boolean" %>

<jsp:invoke fragment="editFrag" var="editFragString"/>

<div class="preview-gp">
    <div class="row">
        <div class="col-xs-12">
            <div class="panel-group" role="tablist" aria-multiselectable="true">
                <div class="panel panel-default">
                    <div class="panel-heading completed">
                        <h4 class="panel-title">
                            <a class="collapsed" data-toggle="collapse" href="#previewCompInfo">Company Information</a>
                        </h4>
                    </div>
                    <div id="previewCompInfo" class="panel-collapse collapse">
                        <div class="panel-body">
                            <div class="panel-main-content form-horizontal min-row">
                                <div class="form-group">
                                    <div class="col-10"><strong>Company Profile</strong></div>
                                    <div class="clear"></div>
                                </div>
                                <div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">UEN</label>
                                        <div class="col-xs-6"><p>${compProfile.uen}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Company Name</label>
                                        <div class="col-xs-6"><p>${compProfile.compName}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Postal Code</label>
                                        <div class="col-xs-6"><p>${compProfile.postalCode}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Address Type</label>
                                        <div class="col-xs-6"><p><iais:code code="${compProfile.addressType}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Blk / House No.</label>
                                        <div class="col-xs-6"><p>${compProfile.blockNo}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Floor No.</label>
                                        <div class="col-xs-6"><p>${compProfile.floor}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Unit No.</label>
                                        <div class="col-xs-6"><p>${compProfile.unitNo}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Street Name</label>
                                        <div class="col-xs-6"><p>${compProfile.street}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="panel panel-default">
                    <div class="panel-heading completed">
                        <h4 class="panel-title">
                            <a class="collapsed" data-toggle="collapse" href="#previewFacInfo">Facility Information</a>
                        </h4>
                    </div>
                    <div id="previewFacInfo" class="panel-collapse collapse">
                        <div class="panel-body">
                            <c:if test="${profileEditJudge}"><div class="text-right app-font-size-16">${fn:replace(editFragString, "REPLACE-STEP-KEY", "facInfo_facProfile")}</div></c:if>
                            <div class="panel-main-content form-horizontal min-row">
                                <div class="form-group">
                                    <div class="col-10"><strong>Facility Profile</strong></div>
                                    <div class="clear"></div>
                                </div>
                                <div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Facility Name</label>
                                        <div class="col-xs-6"><p>${facProfile.facName}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Is the Facility address the same as the company address?</label>
                                        <div class="col-xs-6"><p>${facProfile.sameAddress}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Postal Code</label>
                                        <div class="col-xs-6"><p>${facProfile.postalCode}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Address Type</label>
                                        <div class="col-xs-6"><p>${facProfile.addressType}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Block / House No.</label>
                                        <div class="col-xs-6"><p>${facProfile.block}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Floor and Unit No.</label>
                                        <div class="col-xs-6"><p>${facProfile.floor}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Street Name</label>
                                        <div class="col-xs-6"><p>${facProfile.streetName}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Is the facility a Protected Place?</label>
                                        <div class="col-xs-6"><p>${facProfile.facilityProtected}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                </div>
                            </div>
                            <c:if test="${operatorEditJudge}"><div class="text-right app-font-size-16">${fn:replace(editFragString, "REPLACE-STEP-KEY", "facInfo_facOperator")}</div></c:if>
                            <div class="panel-main-content form-horizontal min-row">
                                <div class="form-group">
                                    <div class="col-10"><strong>Facility Operator Profile</strong></div>
                                    <div class="clear"></div>
                                </div>
                                <div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Designation of Facility Operator</label>
                                        <div class="col-xs-6"><p>${facOperator.facOperator}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label" style="font-weight: bold">Facility Operator Designee</label>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Salutation</label>
                                        <div class="col-xs-6"><p><iais:code code="${facOperator.salutation}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Name</label>
                                        <div class="col-xs-6"><p>${facOperator.designeeName}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">ID No.</label>
                                        <div class="col-xs-6"><p>${facOperator.idNumber}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Nationality</label>
                                        <div class="col-xs-6"><p><iais:code code="${facOperator.nationality}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Designation</label>
                                        <div class="col-xs-6"><p>${facOperator.designation}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Contect No.</label>
                                        <div class="col-xs-6"><p>${facOperator.contactNo}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Email</label>
                                        <div class="col-xs-6"><p>${facOperator.email}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Employment Start Date</label>
                                        <div class="col-xs-6"><p>${facOperator.employmentStartDt}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                </div>
                            </div>
                            <c:if test="${adminEditJudge}"><div class="text-right app-font-size-16">${fn:replace(editFragString, "REPLACE-STEP-KEY", "facInfo_facAdminOfficer")}</div></c:if>
                            <div class="panel-main-content form-horizontal min-row">
                                <div class="form-group">
                                    <div class="col-10"><strong>Facility Administrator/ Officer</strong></div>
                                    <div class="clear"></div>
                                </div>
                                <div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Main Administrator</label>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Salutation</label>
                                        <div class="col-xs-6"><p><iais:code code="${facAdminOfficer.mainAdmin.salutation}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Name</label>
                                        <div class="col-xs-6"><p>${facAdminOfficer.mainAdmin.name}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Nationality</label>
                                        <div class="col-xs-6"><p><iais:code code="${facAdminOfficer.mainAdmin.nationality}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">NRIC/FIN</label>
                                        <div class="col-xs-6"><p>${facAdminOfficer.mainAdmin.idNumber}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Designation</label>
                                        <div class="col-xs-6"><p>${facAdminOfficer.mainAdmin.designation}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Contact No.</label>
                                        <div class="col-xs-6"><p>${facAdminOfficer.mainAdmin.contactNo}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Email Address</label>
                                        <div class="col-xs-6"><p>${facAdminOfficer.mainAdmin.email}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Employment Start Date</label>
                                        <div class="col-xs-6"><p>${facAdminOfficer.mainAdmin.employmentStartDt}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                </div>
                                <div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Alternative Administrator</label>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Salutation</label>
                                        <div class="col-xs-6"><p><iais:code code="${facAdminOfficer.alternativeAdmin.salutation}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Name</label>
                                        <div class="col-xs-6"><p>${facAdminOfficer.alternativeAdmin.name}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Nationality</label>
                                        <div class="col-xs-6"><p><iais:code code="${facAdminOfficer.alternativeAdmin.nationality}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">NRIC/FIN</label>
                                        <div class="col-xs-6"><p>${facAdminOfficer.alternativeAdmin.idNumber}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Designation</label>
                                        <div class="col-xs-6"><p>${facAdminOfficer.alternativeAdmin.designation}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Contect No.</label>
                                        <div class="col-xs-6"><p>${facAdminOfficer.alternativeAdmin.contactNo}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Email Address</label>
                                        <div class="col-xs-6"><p>${facAdminOfficer.alternativeAdmin.email}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Employment Start Date</label>
                                        <div class="col-xs-6"><p>${facAdminOfficer.alternativeAdmin.employmentStartDt}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                </div>
                            </div>
                            <c:if test="${officerEditJudge}"><div class="text-right app-font-size-16">${fn:replace(editFragString, "REPLACE-STEP-KEY", "facInfo_facAdminOfficer")}</div></c:if>
                            <div class="panel-main-content form-horizontal min-row">
                                <div class="form-group">
                                    <div class="col-10"><strong>Facility Officer</strong></div>
                                    <div class="clear"></div>
                                </div>
                                <div>
                                    <c:forEach var="facOfficer" items="${facAdminOfficer.officerList}">
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Salutation</label>
                                            <div class="col-xs-6"><p><iais:code code="${facOfficer.salutation}"/></p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Name</label>
                                            <div class="col-xs-6"><p>${facOfficer.name}</p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Nationality</label>
                                            <div class="col-xs-6"><p><iais:code code="${facOfficer.nationality}"/></p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">ID No.</label>
                                            <div class="col-xs-6"><p>${facOfficer.idNumber}</p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Designation</label>
                                            <div class="col-xs-6"><p>${facOfficer.designation}</p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Contect No.</label>
                                            <div class="col-xs-6"><p>${facOfficer.contactNo}</p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Email Address</label>
                                            <div class="col-xs-6"><p>${facOfficer.email}</p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Employment Start Date</label>
                                            <div class="col-xs-6"><p>${facOfficer.employmentStartDt}</p></div>
                                            <div class="clear"></div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                            <c:if test="${committeeEditJudge}"><div class="text-right app-font-size-16">${fn:replace(editFragString, "REPLACE-STEP-KEY", "facInfo_facCommittee")}</div></c:if>
                            <div class="panel-main-content form-horizontal min-row">
                                <div class="form-group">
                                    <div class="col-10"><strong>Biosafety Committee</strong></div>
                                    <div class="clear"></div>
                                </div>
                                <div>
                                    <a href="javascript:void(0)" onclick="expandFile('previewSubmit', 'bsbCommittee')">View Biosafety Committee Information</a>
                                </div>
                            </div>
                            <c:if test="${authorisedEditJudge}"><div class="text-right app-font-size-16">${fn:replace(editFragString, "REPLACE-STEP-KEY", "facInfo_facAuth")}</div></c:if>
                            <div class="panel-main-content form-horizontal min-row">
                                <div class="form-group">
                                    <div class="col-10"><strong>Personnel Authorised to Access the Facility</strong></div>
                                    <div class="clear"></div>
                                </div>
                                <div>
                                    <a href="javascript:void(0)" onclick="expandFile('previewSubmit', 'facAuth')">View Authorised Personnel Information</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <c:if test="${containsBatListJudge}">
                <div class="panel panel-default">
                    <div class="panel-heading completed">
                        <h4 class="panel-title">
                            <a class="collapsed" data-toggle="collapse" href="#previewBatInfo">Biological Agents &amp; Toxins</a>
                        </h4>
                    </div>
                    <div id="previewBatInfo" class="panel-collapse collapse">
                        <div class="panel-body">
                            <c:if test="${batListEditJudge}"><div class="text-right app-font-size-16">${fn:replace(editFragString, "REPLACE-STEP-KEY", "batInfo")}</div></c:if>
                            <c:forEach var="bat" items="${batList}">
                                <div class="panel-main-content form-horizontal min-row">
                                    <div class="form-group">
                                        <div class="col-10"><strong><iais:code code="${bat.activityType}"/></strong></div>
                                        <div class="clear"></div>
                                    </div>
                                    <c:forEach var="info" items="${bat.batInfos}">
                                        <div>
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label"><iais:code code="${info.schedule}"/></label>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label">Name of Biological Agent/Toxin</label>
                                                <div class="col-xs-6"><p>${info.batName}</p></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label">Types of samples that will be handled</label>
                                                <div class="col-xs-6"><p>${info.sampleType}</p></div>
                                                <div class="clear"></div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
                </c:if>
                <div class="panel panel-default">
                    <div class="panel-heading completed">
                        <h4 class="panel-title">
                            <a class="collapsed" data-toggle="collapse" href="#previewOtherAppInfo">Other Application & Information</a>
                        </h4>
                    </div>
                    <div id="previewOtherAppInfo" class="panel-collapse collapse">
                        <div class="panel-body">
                            <c:if test="${otherAppInfoEditJudge}"><div class="text-right app-font-size-16">${fn:replace(editFragString, "REPLACE-STEP-KEY", "otherInfo")}</div></c:if>
                            <div class="panel-main-content form-horizontal min-row">
                                <div class="form-group">
                                    <div class="col-10"><strong>Declaration</strong></div>
                                    <div class="clear"></div>
                                </div>
                            </div>
                            <div class="panel-main-content form-horizontal min-row">
                                <div class="form-group">
                                    <div class="col-10"><strong>Other Information</strong></div>
                                    <div class="clear"></div>
                                </div>
                                <div>
                                    <div class="form-group">
                                        <div class="col-xs-12"><p>The following is a non-exhaustive list of supporting documents that the facility is required to provide for the application. If any of these are not available at point of application submission, they must be provided subsequently, when available. Please note that incomplete submission of supporting documents may result in delays to processing or rejection of the application.</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <div class="col-10" style="text-decoration: underline"><strong>Supporting Documents</strong></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <div class="col-xs-12"><p>1. Facility Administrative Oversight Plan.</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <div class="col-xs-12"><p>2. Documentation of successful facility certification.</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <div class="col-xs-12"><p>3. Documentation of successful completion of the required biosafety training for the Biosafety Coordinator.</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <div class="col-xs-12"><p>4. Documentation of approval from relevant ministry or statutory board, where applicable.</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <div class="col-xs-12"><p>5. Gazette Order (if the facility is a Protected Place under the Infrastructure Protection Act).</p></div>
                                        <div class="clear"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="panel panel-default">
                    <div class="panel-heading completed">
                        <h4 class="panel-title">
                            <a class="collapsed" data-toggle="collapse" href="#previewDocs">Supporting Documents</a>
                        </h4>
                    </div>
                    <div id="previewDocs" class="panel-collapse collapse">
                        <div class="panel-body">
                            <c:if test="${docEditJudge}"><div class="text-right app-font-size-16">${fn:replace(editFragString, "REPLACE-STEP-KEY", "primaryDocs")}</div></c:if>
                            <div class="panel-main-content form-horizontal min-row">
                                <jsp:invoke fragment="docFrag"/>
                            </div>
                        </div>
                    </div>
                </div>
                <c:if test="${containsAfcJudge}">
                <div class="panel panel-default">
                    <div class="panel-heading completed">
                        <h4 class="panel-title">
                            <a class="collapsed" data-toggle="collapse" href="#previewAfc">Approved Facility Certifier</a>
                        </h4>
                    </div>
                    <div id="previewAfc" class="panel-collapse collapse">
                        <div class="panel-body">
                            <c:if test="${afcEditJudge}"><div class="text-right app-font-size-16">${fn:replace(editFragString, "REPLACE-STEP-KEY", "afc")}</div></c:if>
                            <div class="panel-main-content form-horizontal min-row">
                                <div class="form-group">
                                    <label class="col-xs-6 control-label">Has the facility appointed an Approved Facility Certifier</label>
                                    <div class="col-xs-6">
                                        Yes <span class="fa <c:choose><c:when test="${afc.appointed eq 'Y'}">fa-dot-circle-o</c:when><c:otherwise>fa-circle-o</c:otherwise></c:choose>"></span>
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                        No <span class="fa <c:choose><c:when test="${afc.appointed eq 'N'}">fa-dot-circle-o</c:when><c:otherwise>fa-circle-o</c:otherwise></c:choose>"></span>
                                    </div>
                                    <div class="clear"></div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-6 control-label">Select Approved Facility Certifier</label>
                                    <div class="col-xs-6"><p><iais:code code="${afc.afc}"/></p></div>
                                    <div class="clear"></div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-6 control-label">Reasons for choosing this AFC</label>
                                    <div class="col-xs-6"><p>${afc.selectReason}</p></div>
                                    <div class="clear"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                </c:if>
            </div>
        </div>
    </div>
</div>