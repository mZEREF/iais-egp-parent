<%@tag description="Preview page of facility registration" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@attribute name="facProfile" required="true" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityProfileDto" %>
<%@attribute name="facOperator" required="true" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityOperatorDto" %>
<%@attribute name="facAuth" required="true" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAuthoriserDto" %>
<%@attribute name="facAdmin" required="true" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAdministratorDto" %>
<%@attribute name="facOfficer" required="true" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityOfficerDto" %>
<%@attribute name="facCommittee" required="true" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityCommitteeDto" %>
<%@attribute name="batList" required="true" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.register.facility.BiologicalAgentToxinDto>" %>
<%@attribute name="docFrag" fragment="true" %>
<%@attribute name="editFrag" fragment="true" %>

<jsp:invoke fragment="editFrag" var="editFragString"/>

<div class="preview-gp">
    <div class="row">
        <div class="col-xs-12">
            <div class="panel-group" role="tablist" aria-multiselectable="true">
                <div class="panel panel-default">
                    <div class="panel-heading completed">
                        <h4 class="panel-title">
                            <a class="collapsed" data-toggle="collapse" href="#previewFacInfo">Facility Informations</a>
                        </h4>
                    </div>
                    <div id="previewFacInfo" class="panel-collapse collapse">
                        <div class="panel-body">
                            <div class="text-right app-font-size-16">${fn:replace(editFragString, "REPLACE-STEP-KEY", "facInfo_facProfile")}</div>
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
                                        <label class="col-xs-6 control-label">Facility Address</label>
                                        <div class="col-xs-6"><p><iais-bsb:address block="${facProfile.block}" street="${facProfile.streetName}" floor="${facProfile.floor}" unitNo="${facProfile.unitNo}" postalCode="${facProfile.postalCode}"/></p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Is the facility a Protected Place?</label>
                                        <div class="col-xs-6"><p>${facProfile.isFacilityProtected}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="panel-main-content form-horizontal min-row">
                                <div class="form-group">
                                    <div class="col-10"><strong>Facility Operator</strong></div>
                                    <div class="clear"></div>
                                </div>
                                <div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Facility Operator</label>
                                        <div class="col-xs-6"><p>${facOperator.facOperator}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Facility Operator Designee Name</label>
                                        <div class="col-xs-6"><p>${facOperator.designeeName}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">NRIC/FIN</label>
                                        <div class="col-xs-6"><p>${facOperator.idNumber}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Nationality</label>
                                        <div class="col-xs-6"><p>${facOperator.nationality}</p></div>
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
                            <div class="panel-main-content form-horizontal min-row">
                                <div class="form-group">
                                    <div class="col-10"><strong>Personnel Authorised to Access the Facility</strong></div>
                                    <div class="clear"></div>
                                </div>
                                <c:forEach var="personnel" items="${facAuth.facAuthPersonnelList}" varStatus="status">
                                    <div>
                                        <c:if test="${facAuth.facAuthPersonnelList.size() > 1}">
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label">Personnel ${status.index + 1}</label>
                                                <div class="clear"></div>
                                            </div>
                                        </c:if>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Name</label>
                                            <div class="col-xs-6"><p>${personnel.name}</p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">NRIC/FIN</label>
                                            <div class="col-xs-6"><p>${personnel.idNumber}</p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Nationality</label>
                                            <div class="col-xs-6"><p>${personnel.nationality}</p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Designation</label>
                                            <div class="col-xs-6"><p>${personnel.designation}</p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Contect No.</label>
                                            <div class="col-xs-6"><p>${personnel.contactNo}</p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Email Address</label>
                                            <div class="col-xs-6"><p>${personnel.email}</p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Employment Start Date</label>
                                            <div class="col-xs-6"><p>${personnel.employmentStartDt}</p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Employment Period</label>
                                            <div class="col-xs-6"><p>${personnel.employmentPeriod}</p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Security Clearance Date</label>
                                            <div class="col-xs-6"><p>${personnel.securityClearanceDt}</p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Area of Work</label>
                                            <div class="col-xs-6"><p>${personnel.workArea}</p></div>
                                            <div class="clear"></div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                            <div class="panel-main-content form-horizontal min-row">
                                <div class="form-group">
                                    <div class="col-10"><strong>Facility Administrator</strong></div>
                                    <div class="clear"></div>
                                </div>
                                <div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Main Administrator</label>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Name</label>
                                        <div class="col-xs-6"><p>${facAdmin.mainAdmin.adminName}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">NRIC/FIN</label>
                                        <div class="col-xs-6"><p>${facAdmin.mainAdmin.idNumber}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Nationality</label>
                                        <div class="col-xs-6"><p>${facAdmin.mainAdmin.nationality}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Designation</label>
                                        <div class="col-xs-6"><p>${facAdmin.mainAdmin.designation}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Contect No.</label>
                                        <div class="col-xs-6"><p>${facAdmin.mainAdmin.contactNo}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Email Address</label>
                                        <div class="col-xs-6"><p>${facAdmin.mainAdmin.email}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Employment Start Date</label>
                                        <div class="col-xs-6"><p>${facAdmin.mainAdmin.employmentStartDt}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                </div>
                                <div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Alternative Administrator</label>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Name</label>
                                        <div class="col-xs-6"><p>${facAdmin.alternativeAdmin.adminName}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">NRIC/FIN</label>
                                        <div class="col-xs-6"><p>${facAdmin.alternativeAdmin.idNumber}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Nationality</label>
                                        <div class="col-xs-6"><p>${facAdmin.alternativeAdmin.nationality}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Designation</label>
                                        <div class="col-xs-6"><p>${facAdmin.alternativeAdmin.designation}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Contect No.</label>
                                        <div class="col-xs-6"><p>${facAdmin.alternativeAdmin.contactNo}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Email Address</label>
                                        <div class="col-xs-6"><p>${facAdmin.alternativeAdmin.email}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Employment Start Date</label>
                                        <div class="col-xs-6"><p>${facAdmin.alternativeAdmin.employmentStartDt}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="panel-main-content form-horizontal min-row">
                                <div class="form-group">
                                    <div class="col-10"><strong>Facility Officer</strong></div>
                                    <div class="clear"></div>
                                </div>
                                <div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Name</label>
                                        <div class="col-xs-6"><p>${facOfficer.officerName}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">NRIC/FIN</label>
                                        <div class="col-xs-6"><p>${facOfficer.idNumber}</p></div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-6 control-label">Nationality</label>
                                        <div class="col-xs-6"><p>${facOfficer.nationality}</p></div>
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
                                </div>
                            </div>
                            <div class="panel-main-content form-horizontal min-row">
                                <div class="form-group">
                                    <div class="col-10"><strong>Biosafety Committee</strong></div>
                                    <div class="clear"></div>
                                </div>
                                <c:forEach var="personnel" items="${facCommittee.facCommitteePersonnelList}" varStatus="status">
                                    <div>
                                        <c:if test="${facCommittee.facCommitteePersonnelList.size() > 1}">
                                            <div class="form-group">
                                                <label class="col-xs-6 control-label">Committee ${status.index + 1}</label>
                                                <div class="clear"></div>
                                            </div>
                                        </c:if>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Name</label>
                                            <div class="col-xs-6"><p>${personnel.name}</p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">NRIC/FIN</label>
                                            <div class="col-xs-6"><p>${personnel.idNumber}</p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Nationality</label>
                                            <div class="col-xs-6"><p>${personnel.nationality}</p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Designation</label>
                                            <div class="col-xs-6"><p>${personnel.designation}</p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Contect No.</label>
                                            <div class="col-xs-6"><p>${personnel.contactNo}</p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Email Address</label>
                                            <div class="col-xs-6"><p>${personnel.email}</p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Employment Start Date</label>
                                            <div class="col-xs-6"><p>${personnel.employmentStartDt}</p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Area of Expertise</label>
                                            <div class="col-xs-6"><p>${personnel.expertiseArea}</p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Role</label>
                                            <div class="col-xs-6"><p>${personnel.role}</p></div>
                                            <div class="clear"></div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-6 control-label">Is this person is Employee of the Company?</label>
                                            <div class="col-xs-6"><p>${personnel.employee}</p></div>
                                            <div class="clear"></div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="panel panel-default">
                    <div class="panel-heading completed">
                        <h4 class="panel-title">
                            <a class="collapsed" data-toggle="collapse" href="#previewBatInfo">Biological Agents &amp; Toxins</a>
                        </h4>
                    </div>
                    <div id="previewBatInfo" class="panel-collapse collapse">
                        <div class="panel-body">
                            <div class="text-right app-font-size-16">${fn:replace(editFragString, "REPLACE-STEP-KEY", "batInfo")}</div>
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
                <div class="panel panel-default">
                    <div class="panel-heading completed">
                        <h4 class="panel-title">
                            <a class="collapsed" data-toggle="collapse" href="#previewDocs">Primary Documents</a>
                        </h4>
                    </div>
                    <div id="previewDocs" class="panel-collapse collapse">
                        <div class="panel-body">
                            <div class="text-right app-font-size-16">${fn:replace(editFragString, "REPLACE-STEP-KEY", "primaryDocs")}</div>
                            <div class="panel-main-content form-horizontal min-row">
                                <jsp:invoke fragment="docFrag"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>