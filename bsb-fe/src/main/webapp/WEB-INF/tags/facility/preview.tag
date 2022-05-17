<%@tag description="Preview page of facility registration" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@taglib prefix="fac" tagdir="/WEB-INF/tags/facility" %>

<%@attribute name="isCfJudge" required="true" type="java.lang.Boolean" %>
<%@attribute name="isUcfJudge" required="true" type="java.lang.Boolean" %>
<%@attribute name="isRfJudge" required="true" type="java.lang.Boolean" %>

<%@attribute name="classification" required="true" type="java.lang.String" %>
<%@attribute name="activities" required="false" type="java.util.List<java.lang.String>" %>

<%@attribute name="compProfile" required="true" type="sg.gov.moh.iais.egp.bsb.dto.info.common.OrgAddressInfo" %>
<%@attribute name="facProfile" required="true" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityProfileDto" %>
<%@attribute name="facOperator" required="false" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityOperatorDto" %>
<%@attribute name="facAdminOfficer" required="true" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAdminAndOfficerDto" %>
<%@attribute name="facCommittee" required="false" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityCommitteeDto" %>
<%@attribute name="facAuth" required="false" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAuthoriserDto" %>
<%@attribute name="batList" required="false" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.register.bat.BiologicalAgentToxinDto>" %>
<%@attribute name="declarationConfigList" required="true" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.declaration.DeclarationItemMainInfo>" %>
<%@attribute name="declarationAnswerMap" required="true" type="java.util.Map<java.lang.String, java.lang.String>" %>
<%@attribute name="afc" required="false" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAfcDto" %>
<%@attribute name="docFrag" fragment="true" %>

<%@attribute name="editFrag" fragment="true" %>
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


<iais-bsb:global-constants classFullName="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" attributeKey="masterCodeConstants"/>
<%--@elvariable id="masterCodeConstants" type="java.util.Map<java.lang.String, java.lang.Object>"--%>
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
                                        <div class="col-xs-6"><p><iais:code code="${facProfile.addressType}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Block / House No.</label>
                                        <div class="col-xs-6"><p>${facProfile.block}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Floor and Unit No.</label>
                                        <div class="col-xs-6"><p>${facProfile.floor} - ${facProfile.unitNo}</p></div>
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
                            <c:if test="${not isRfJudge}">
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
                                        <label class="col-xs-6 control-label">Contact No.</label>
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
                            </c:if>
                            <c:if test="${adminEditJudge}"><div class="text-right app-font-size-16">${fn:replace(editFragString, "REPLACE-STEP-KEY", "facInfo_facAdminOfficer")}</div></c:if>
                            <div class="panel-main-content form-horizontal min-row">
                                <div class="form-group">
                                    <div class="col-10"><strong>Facility Administrator/ Officer</strong></div>
                                    <div class="clear"></div>
                                </div>
                                <div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label"><strong>Main Administrator</strong></label>
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
                                        <label class="col-xs-6 control-label"><strong>AlternateAdministrator</strong></label>
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
                                        <label class="col-xs-6 control-label">Contact No.</label>
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
                            <c:if test="${facAdminOfficer.officerList.size() > 0}">
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
                                            <label class="col-xs-6 control-label">Contact No.</label>
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
                            </c:if>
                            <c:if test="${not isRfJudge}">
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
                            </c:if>
                        </div>
                    </div>
                </div>
                <c:if test="${isUcfJudge or (isRfJudge and activities.get(0) eq masterCodeConstants.ACTIVITY_SP_HANDLE_FIFTH_SCHEDULE_EXEMPTED)}">
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
                                    <div class="form-group" style="margin-top: 10px">
                                        <div class="col-10"><strong><iais:code code="${bat.activityType}"/></strong></div>
                                        <div class="clear"></div>
                                    </div>
                                    <c:forEach var="info" items="${bat.batInfos}">
                                        <c:set var="BAorT" value='${masterCodeConstants.FIFTH_SCHEDULE eq info.schedule ? "Toxin" : "Biological Agent"}'/>
                                        <div>
                                            <c:if test="${masterCodeConstants.FIFTH_SCHEDULE ne info.schedule}">
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label">Schedule</label>
                                                <div class="col-xs-6"><p><iais:code code="${info.schedule}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                            </c:if>
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label">Name of ${BAorT}</label>
                                                <div class="col-xs-6"><p><iais-bsb:bat-code code="${info.batName}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label">Types of samples that will be handled</label>
                                                <div class="col-xs-6">
                                                    <c:forEach var="oneSampleType" items="${info.sampleType}">
                                                    <p><iais:code code="${oneSampleType}"/></p>
                                                    </c:forEach>
                                                </div>
                                                <div class="clear"></div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label">Type of work that will be carried out involving the ${fn:toLowerCase(BAorT)}</label>
                                                <div class="col-xs-6">
                                                    <c:forEach var="oneWorkType" items="${info.workType}">
                                                        <p><iais:code code="${oneWorkType}"/></p>
                                                    </c:forEach>
                                                </div>
                                                <div class="clear"></div>
                                            </div>
                                            <c:if test='${info.sampleType.contains(masterCodeConstants.SAMPLE_NATURE_OTHER) or info.workType.contains(masterCodeConstants.WORK_TYPE_BIOMANUFACTURING_INVOLVING_BAT) or info.workType.contains(masterCodeConstants.WORK_TYPE_OTHERS)}'>
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label">Details regarding the type of samples that will be handled and the intended work</label>
                                                <div class="col-xs-6"><p>${info.sampleWorkDetail}</p></div>
                                                <div class="clear"></div>
                                            </div>
                                            </c:if>
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label">Mode of Procurement</label>
                                                <div class="col-xs-6"><p><iais:code code="${info.details.procurementMode}"/></p></div>
                                                <div class="clear"></div>
                                            </div>
                                            <c:if test="${'BMOP001' eq info.details.procurementMode}">
                                            <div id="transferringFacilityDetailsInfo">
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label" style="font-weight: bold">Details of Transferring Facility</label>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Block No.</label>
                                                    <div class="col-xs-6"><p>${info.details.blockNoT}</p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Floor & Unit</label>
                                                    <div class="col-xs-6"><p>${info.details.floorNoT}-${info.details.unitNoT}</p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Street</label>
                                                    <div class="col-xs-6"><p>${info.details.streetNameT}</p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Postal Code</label>
                                                    <div class="col-xs-6"><p>${info.details.postalCodeT}</p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label" style="font-weight: bold">Details of Contact Person from Transferring Facility</label>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Name</label>
                                                    <div class="col-xs-6"><p>${info.details.contactPersonNameT}</p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Email address</label>
                                                    <div class="col-xs-6"><p>${info.details.emailAddressT}</p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Contact No.</label>
                                                    <div class="col-xs-6"><p>${info.details.contactNoT}</p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Expected Date of Transfer</label>
                                                    <div class="col-xs-6"><p>${info.details.expectedDateT}</p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Name of Courier Service Provider</label>
                                                    <div class="col-xs-6"><p>${info.details.courierServiceProviderNameT}</p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Remarks</label>
                                                    <div class="col-xs-6"><p>${info.details.remarksT}</p></div>
                                                    <div class="clear"></div>
                                                </div>
                                            </div>
                                            </c:if>
                                            <c:if test="${'BMOP002' eq info.details.procurementMode}">
                                            <div id="exportingFacilityDetailsInfo">
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label" style="font-weight: bold">Details of Exporting Facility</label>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Facility Name</label>
                                                    <div class="col-xs-6"><p>${info.details.facNameE}</p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Block No.</label>
                                                    <div class="col-xs-6"><p>${info.details.blockNoE}</p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Floor & Unit</label>
                                                    <div class="col-xs-6"><p>${info.details.floorNoE}-${info.details.unitNoE}</p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Street</label>
                                                    <div class="col-xs-6"><p>${info.details.streetNameE}</p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Postal Code</label>
                                                    <div class="col-xs-6"><p>${info.details.postalCodeE}</p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label" style="font-weight: bold">Details of Contact Person from Exporting Facility</label>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Name</label>
                                                    <div class="col-xs-6"><p>${info.details.contactPersonNameE}</p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Email address</label>
                                                    <div class="col-xs-6"><p>${info.details.emailAddressE}</p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Contact No.</label>
                                                    <div class="col-xs-6"><p>${info.details.contactNoE}</p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Expected Date of Export</label>
                                                    <div class="col-xs-6"><p>${info.details.expectedDateE}</p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Name of Courier Service Provider</label>
                                                    <div class="col-xs-6"><p>${info.details.courierServiceProviderNameE}</p></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-6 control-label">Remarks</label>
                                                    <div class="col-xs-6"><p>${info.details.remarksE}</p></div>
                                                    <div class="clear"></div>
                                                </div>
                                            </div>
                                            </c:if>
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
                                    <div class="col-10" style="padding-bottom: 15px;"><strong>Declaration</strong></div>
                                    <div class="clear"></div>
                                </div>
                                <div class="col-xs-12 form-group">
                                    <h4 style="font-size: 16px">I, hereby declare the following:</h4>
                                    <br/>
                                    <ol style="padding-left: 16px">
                                        <c:forEach var="item" items="${declarationConfigList}">
                                            <li class="col-xs-12">
                                                <div class="col-xs-9 form-group" style="padding-left: 0">${item.statement}</div>
                                                <div class="form-check col-xs-2">
                                                    <span class="fa <c:choose><c:when test="${'Y' eq declarationAnswerMap.get(item.id)}">fa-dot-circle-o</c:when><c:otherwise>fa-circle-o</c:otherwise></c:choose>"></span> Yes
                                                </div>
                                                <div class="form-check col-xs-1">
                                                    <span class="fa <c:choose><c:when test="${'N' eq declarationAnswerMap.get(item.id)}">fa-dot-circle-o</c:when><c:otherwise>fa-circle-o</c:otherwise></c:choose>"></span> No
                                                </div>
                                            </li>
                                        </c:forEach>
                                    </ol>
                                </div>
                            </div>
                            <div class="panel-main-content form-horizontal min-row">
                                <div class="form-group">
                                    <div class="col-10"><strong>Other Information</strong></div>
                                    <div class="clear"></div>
                                </div>
                                <div class="col-xs-12 form-group">
                                    <fac:supportingDocInfo classification="${classification}" activities="${activities}" masterCodeConstants="${masterCodeConstants}"/>
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
                                <div class="form-group">
                                    <div class="col-10" style="padding-bottom: 15px;"><strong>Uploaded Documents</strong></div>
                                    <div class="clear"></div>
                                </div>
                                <jsp:invoke fragment="docFrag"/>
                            </div>
                        </div>
                    </div>
                </div>
                <c:if test="${isCfJudge}">
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
                                <c:if test="${afc.appointed eq 'Y'}">
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
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
                </c:if>
            </div>
        </div>
    </div>
</div>