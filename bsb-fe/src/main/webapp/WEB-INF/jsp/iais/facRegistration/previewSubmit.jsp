<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT"%>
<%@ page import="sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil"%>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.lang.String" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common-facility-register.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-facility-register.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="dashboard.jsp"%>


<%--@elvariable id="facProfile" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityProfileDto"--%>
<%--@elvariable id="facOperator" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityOperatorDto"--%>
<%--@elvariable id="facAuth" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAuthoriserDto"--%>
<%--@elvariable id="facAdmin" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAdministratorDto"--%>
<%--@elvariable id="facOfficer" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityOfficerDto"--%>
<%--@elvariable id="facCommittee" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityCommitteeDto"--%>
<%--@elvariable id="batList" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.register.facility.BiologicalAgentToxinDto>"--%>
<%--@elvariable id="docSettings" type="java.util.List<sg.gov.moh.iais.egp.bsb.entity.DocSetting>"--%>
<%--@elvariable id="savedFiles" type="java.util.Map<java.lang.String, java.util.List<sg.gov.moh.iais.egp.bsb.dto.register.facility.PrimaryDocDto$DocRecordInfo>>"--%>
<%--@elvariable id="newFiles" type="java.util.Map<java.lang.String, java.util.List<sg.gov.moh.iais.egp.bsb.dto.register.facility.PrimaryDocDto$NewDocInfo>>"--%>
<%--@elvariable id="previewSubmit" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.PreviewSubmitDto"--%>
<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@ include file="InnerNavTab.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div id="previewSubmitPanel" role="tabpanel">
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
                                                                <div class="text-right app-font-size-16"><a href="#" data-step-key="facInfo_facProfile"><em class="fa fa-pencil-square-o"></em>Edit</a></div>
                                                                <div class="panel-main-content form-horizontal min-row">
                                                                    <div class="form-group">
                                                                        <div class="col-10"><strong>Facility Profile</strong></div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Facility Name</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facProfile.facName}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Facility Address</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${TableDisplayUtil.getOneLineAddress(facProfile.block, facProfile.streetName, facProfile.floor, facProfile.unitNo, facProfile.postalCode)}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Is the facility a Protected Place?</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facProfile.isFacilityProtected}</p></div>
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
                                                                            <label class="col-xs-5 col-md-4 control-label">Facility Operator</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facOperator.facOperator}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Facility Operator Designee Name</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facOperator.designeeName}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">NRIC/FIN</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facOperator.idNumber}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Nationality</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facOperator.nationality}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Designation</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facOperator.designation}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Contect No.</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facOperator.contactNo}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Email</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facOperator.email}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Employment Start Date</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facOperator.employmentStartDate}</p></div>
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
                                                                                <label class="col-xs-5 col-md-4 control-label">Personnel ${status.index + 1}</label>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            </c:if>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Name</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${personnel.name}</p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">NRIC/FIN</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${personnel.idNumber}</p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Nationality</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${personnel.nationality}</p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Designation</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${personnel.designation}</p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Contect No.</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${personnel.contactNo}</p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Email Address</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${personnel.email}</p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Employment Start Date</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${personnel.employmentStartDate}</p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Employment Period</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${personnel.employmentPeriod}</p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Security Clearance Date</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${personnel.securityClearanceDate}</p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Area of Work</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${personnel.workArea}</p></div>
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
                                                                            <label class="col-xs-5 col-md-4 control-label">Main Administrator</label>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Name</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facAdmin.mainAdmin.adminName}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">NRIC/FIN</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facAdmin.mainAdmin.idNumber}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Nationality</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facAdmin.mainAdmin.nationality}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Designation</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facAdmin.mainAdmin.designation}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Contect No.</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facAdmin.mainAdmin.contactNo}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Email Address</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facAdmin.mainAdmin.email}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Employment Start Date</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facAdmin.mainAdmin.employmentStartDate}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                    </div>
                                                                    <div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Alternative Administrator</label>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Name</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facAdmin.alternativeAdmin.adminName}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">NRIC/FIN</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facAdmin.alternativeAdmin.idNumber}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Nationality</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facAdmin.alternativeAdmin.nationality}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Designation</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facAdmin.alternativeAdmin.designation}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Contect No.</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facAdmin.alternativeAdmin.contactNo}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Email Address</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facAdmin.alternativeAdmin.email}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Employment Start Date</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facAdmin.alternativeAdmin.employmentStartDate}</p></div>
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
                                                                            <label class="col-xs-5 col-md-4 control-label">Name</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facOfficer.officerName}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">NRIC/FIN</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facOfficer.idNumber}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Nationality</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facOfficer.nationality}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Designation</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facOfficer.designation}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Contect No.</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facOfficer.contactNo}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Email Address</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facOfficer.email}</p></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Employment Start Date</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facOfficer.employmentStartDate}</p></div>
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
                                                                                    <label class="col-xs-5 col-md-4 control-label">Committee ${status.index + 1}</label>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                            </c:if>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Name</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${personnel.name}</p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">NRIC/FIN</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${personnel.idNumber}</p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Nationality</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${personnel.nationality}</p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Designation</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${personnel.designation}</p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Contect No.</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${personnel.contactNo}</p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Email Address</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${personnel.email}</p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Employment Start Date</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${personnel.employmentStartDate}</p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Area of Expertise</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${personnel.expertiseArea}</p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Role</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${personnel.role}</p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Is this person is Employee of the Company?</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p>${personnel.employee}</p></div>
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
                                                                <div class="text-right app-font-size-16"><a href="#" data-step-key="batInfo"><em class="fa fa-pencil-square-o"></em>Edit</a></div>
                                                                <c:forEach var="bat" items="${batList}">
                                                                    <div class="panel-main-content form-horizontal min-row">
                                                                        <div class="form-group">
                                                                            <div class="col-10"><strong><iais:code code="${bat.activityType}"/></strong></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <c:forEach var="info" items="${bat.batInfos}">
                                                                            <div>
                                                                                <div class="form-group">
                                                                                    <label class="col-xs-5 col-md-4 control-label"><iais:code code="${info.schedule}"/></label>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                                <div class="form-group">
                                                                                    <label class="col-xs-5 col-md-4 control-label">Name of Biological Agent/Toxin</label>
                                                                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.batName}</p></div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                                <div class="form-group">
                                                                                    <label class="col-xs-5 col-md-4 control-label">Types of samples that will be handled</label>
                                                                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.sampleType}</p></div>
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
                                                                <div class="text-right app-font-size-16"><a href="#" data-step-key="primaryDocs"><em class="fa fa-pencil-square-o"></em>Edit</a></div>
                                                                <div class="panel-main-content form-horizontal min-row">
                                                                    <c:forEach var="doc" items="${docSettings}">
                                                                        <c:set var="maskDocType" value="${MaskUtil.maskValue('file', doc.type)}"/>
                                                                        <c:set var="savedFileList" value="${savedFiles.get(doc.type)}" />
                                                                        <c:set var="newFileList" value="${newFiles.get(doc.type)}" />
                                                                        <c:if test="${not empty savedFileList or not empty newFileList}">
                                                                            <div class="form-group">
                                                                                <div class="col-10"><strong>${doc.typeDisplay}</strong></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div>
                                                                                <c:forEach var="file" items="${savedFileList}">
                                                                                    <c:set var="tmpId" value="${MaskUtil.maskValue('file', file.repoId)}"/>
                                                                                    <div class="form-group">
                                                                                        <div class="col-10"><p><a href="javascript:void(0)" onclick="downloadFile('saved', '${tmpId}')">${file.filename}</a>(${String.format("%.1f", file.size/1024.0)}KB)</p></div>
                                                                                        <div class="clear"></div>
                                                                                    </div>
                                                                                </c:forEach>
                                                                                <c:forEach var="file" items="${newFileList}">
                                                                                    <c:set var="tmpId" value="${MaskUtil.maskValue('file', file.tmpId)}"/>
                                                                                    <div class="form-group">
                                                                                        <div class="col-10"><p><a href="javascript:void(0)" onclick="downloadFile('new', '${tmpId}')">${file.filename}</a>(${String.format("%.1f", file.size/1024.0)}KB)</p></div>
                                                                                        <div class="clear"></div>
                                                                                    </div>
                                                                                </c:forEach>
                                                                            </div>
                                                                        </c:if>
                                                                    </c:forEach>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-horizontal" style="padding: 30px 20px 10px;">
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="remarks">Remarks</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <textarea maxLength="1000" class="col-xs-12" name="remarks" id="remarks" rows="5"><c:out value="${previewSubmit.remarks}"/></textarea>
                                                <span data-err-ind="remarks" class="error-msg"></span>
                                            </div>
                                        </div>
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="approvedFacCertifier">Select Approved Facility Certifier</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7" style="z-index: 20">
                                                <select name="approvedFacCertifier" id="approvedFacCertifier">
                                                    <c:forEach items="${approvedFacCertifierOps}" var="certifier">
                                                        <option value="${certifier.value}" <c:if test="${previewSubmit.approvedFacCertifier eq certifier.value}">selected="selected"</c:if>>${certifier.text}</option>
                                                    </c:forEach>
                                                </select>
                                                <span data-err-ind="approvedFacCertifier" class="error-msg"></span>
                                            </div>
                                        </div>
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="reason">Reasons to Choose This AFC</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <textarea maxLength="500" class="col-xs-12" name="reason" id="reason" rows="2"><c:out value="${previewSubmit.reason}"/></textarea>
                                                <span data-err-ind="reason" class="error-msg"></span>
                                            </div>
                                        </div>
                                        <div class="form-group " style="z-index: 10">
                                            <div class="col-xs-1" style="padding: 30px 0 20px 30px;">
                                                <input type="checkbox" name="declare" id="declare" value="Y" <c:if test="${previewSubmit.declare eq 'Y'}">checked="checked"</c:if> />
                                            </div>
                                            <div class="col-xs-10 control-label">
                                                <label for="declare">I, hereby declare that all the information I have provided here is true and accurate. If any of the information given herein changes or becomes inaccurate in any way, I shall immediately notify MOH Biosafety Branch of such change or inaccuracy.</label>
                                                <span data-err-ind="declare" class="error-msg"></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="application-tab-footer">
                                    <div class="row">
                                        <div class="col-xs-12 col-sm-6 ">
                                            <a class="back" id="back" href="#"><em class="fa fa-angle-left"></em> Back</a>
                                        </div>
                                        <div class="col-xs-12 col-sm-6">
                                            <div class="button-group">
                                                <a class="btn btn-secondary" id="saveDraft" >Save as Draft</a>
                                                <a class="btn btn-primary next" id="submit" >Next</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="modal fade" id="submitDeclareModal" role="dialog" aria-labelledby="myModalLabel">
                            <div class="modal-dialog modal-dialog-centered" role="document">
                                <div class="modal-content">
                                    <div class="modal-body">
                                        <div class="row">
                                            <div class="col-md-12"><span style="font-size: 2rem">Please check the declaration box</span></div>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary btn-md" data-dismiss="modal">Close</button>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>
</form>