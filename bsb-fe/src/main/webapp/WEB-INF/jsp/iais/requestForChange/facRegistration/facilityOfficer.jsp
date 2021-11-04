<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT"%>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-afc-facility-register.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="dashboard.jsp"%>

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
                                <div id="facInfoPanel" role="tabpanel">
                                    <%@include file="subStepNavTab.jsp"%>

                                    <div class="form-horizontal">
                                        <div class="text-right"><a id="edit" href="javascript:void(0)"><em class="fa fa-pencil-square-o"></em>Edit</a></div>
                                        <%--@elvariable id="facOfficer" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityOfficerDto"--%>
                                        <h3 class="col-12" style="border-bottom: 1px solid black">Facility Officer</h3>

                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label for="officerName">Name</label>
                                                    <span class="mandatory otherQualificationSpan">*</span>
                                                </div>
                                                <div class="col-sm-6 col-md-7">
                                                    <input type="text" autocomplete="off" name="officerName" id="officerName" value='<c:out value="${facOfficer.officerName}"/>'/>
                                                    <span data-err-ind="officerName" class="error-msg"></span>
                                                </div>
                                            </div>
                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label for="nationality">Nationality</label>
                                                    <span class="mandatory otherQualificationSpan">*</span>
                                                </div>
                                                <div class="col-sm-6 col-md-7">
                                                    <select name="nationality" id="nationality">
                                                        <c:forEach items="${nationalityOps}" var="na">
                                                            <option value="${na.value}" <c:if test="${facOfficer.nationality eq na.value}">selected="selected"</c:if>>${na.text}</option>
                                                        </c:forEach>
                                                    </select>
                                                    <span data-err-ind="nationality" class="error-msg"></span>
                                                </div>
                                            </div>
                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label for="idNumber">NRIC/FIN</label>
                                                    <span class="mandatory otherQualificationSpan">*</span>
                                                </div>
                                                <div class="col-sm-3">
                                                    <select name="idType" id="idType">
                                                        <option value="IDTYPE001" <c:if test="${facOfficer.idType eq 'IDTYPE001'}">selected="selected"</c:if>>NRIC</option>
                                                        <option value="IDTYPE002" <c:if test="${facOfficer.idType eq 'IDTYPE002'}">selected="selected"</c:if>>FIN</option>
                                                        <option value="IDTYPE003" <c:if test="${facOfficer.idType eq 'IDTYPE003'}">selected="selected"</c:if>>Passport</option>
                                                    </select>
                                                    <span data-err-ind="idType" class="error-msg"></span>
                                                </div>
                                                <div class="col-sm-3 col-md-4">
                                                    <input type="text" autocomplete="off" name="idNumber" id="idNumber" value='<c:out value="${facOfficer.idNumber}"/>'/>
                                                    <span data-err-ind="idNumber" class="error-msg"></span>
                                                </div>
                                            </div>
                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label for="designation">Designation</label>
                                                    <span class="mandatory otherQualificationSpan">*</span>
                                                </div>
                                                <div class="col-sm-6 col-md-7">
                                                    <input type="text" autocomplete="off" name="designation" id="designation" value='<c:out value="${facOfficer.designation}"/>'/>
                                                    <span data-err-ind="designation" class="error-msg"></span>
                                                </div>
                                            </div>
                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label for="contactNo">Contact No.</label>
                                                    <span class="mandatory otherQualificationSpan">*</span>
                                                </div>
                                                <div class="col-sm-6 col-md-7">
                                                    <input type="text" autocomplete="off" name="contactNo" id="contactNo" value='<c:out value="${facOfficer.contactNo}"/>'/>
                                                    <span data-err-ind="contactNo" class="error-msg"></span>
                                                </div>
                                            </div>
                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label for="email">Email</label>
                                                    <span class="mandatory otherQualificationSpan">*</span>
                                                </div>
                                                <div class="col-sm-6 col-md-7">
                                                    <input type="text" autocomplete="off" name="email" id="email" value='<c:out value="${facOfficer.email}"/>'/>
                                                    <span data-err-ind="email" class="error-msg"></span>
                                                </div>
                                            </div>
                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label for="employmentStartDate">Employment Start Date</label>
                                                    <span class="mandatory otherQualificationSpan">*</span>
                                                </div>
                                                <div class="col-sm-6 col-md-7">
                                                    <input type="text" autocomplete="off" name="employmentStartDate" id="employmentStartDate" data-date-start-date="01/01/1900" value="<c:out value="${facOfficer.employmentStartDate}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                                    <span data-err-ind="employmentStartDate" class="error-msg"></span>
                                                </div>
                                            </div>
                                    </div>
                                </div>

                                <%@ include file="InnerFooter.jsp" %>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>