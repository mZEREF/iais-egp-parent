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

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common-facility-register.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-display-or-not.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-renewal-facility-register.js"></script>

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
                                    <%@include file="../../mainAppCommon/facRegistration/subStepNavTab.jsp"%>

                                    <div class="form-horizontal">
                                        <div class="text-right"><a id="edit" href="javascript:void(0)"><em class="fa fa-pencil-square-o"></em>Edit</a></div>
                                        <%--@elvariable id="facAdmin" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAdministratorDto"--%>
                                        <h3 class="col-12 pl-0" style="border-bottom: 1px solid black">Main Adminstrator</h3>

                                        <section id="mainAdmin">
                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label for="adminNameM">Name</label>
                                                    <span class="mandatory otherQualificationSpan">*</span>
                                                </div>
                                                <div class="col-sm-6 col-md-7">
                                                    <input maxLength="60" type="text" autocomplete="off" name="adminNameM" id="adminNameM" value='<c:out value="${facAdmin.mainAdmin.adminName}"/>'/>
                                                    <span data-err-ind="adminNameM" class="error-msg"></span>
                                                </div>
                                            </div>
                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label for="nationalityM">Nationality</label>
                                                    <span class="mandatory otherQualificationSpan">*</span>
                                                </div>
                                                <div class="col-sm-6 col-md-7">
                                                    <select name="nationalityM" id="nationalityM">
                                                        <c:forEach items="${nationalityOps}" var="na">
                                                            <option value="${na.value}" <c:if test="${facAdmin.mainAdmin.nationality eq na.value}">selected="selected"</c:if>>${na.text}</option>
                                                        </c:forEach>
                                                    </select>
                                                    <span data-err-ind="nationalityM" class="error-msg"></span>
                                                </div>
                                            </div>
                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label for="idNumberM">NRIC/FIN</label>
                                                    <span class="mandatory otherQualificationSpan">*</span>
                                                </div>
                                                <div class="col-sm-3">
                                                    <select name="idTypeM" id="idTypeM">
                                                        <option value="IDTYPE001" <c:if test="${facAdmin.mainAdmin.idType eq 'IDTYPE001'}">selected="selected"</c:if>>NRIC</option>
                                                        <option value="IDTYPE002" <c:if test="${facAdmin.mainAdmin.idType eq 'IDTYPE002'}">selected="selected"</c:if>>FIN</option>
                                                        <option value="IDTYPE003" <c:if test="${facAdmin.mainAdmin.idType eq 'IDTYPE003'}">selected="selected"</c:if>>Passport</option>
                                                    </select>
                                                    <span data-err-ind="idTypeM" class="error-msg"></span>
                                                </div>
                                                <div class="col-sm-3 col-md-4">
                                                    <input maxLength="9" type="text" autocomplete="off" name="idNumberM" id="idNumberM" value='<c:out value="${facAdmin.mainAdmin.idNumber}"/>'/>
                                                    <span data-err-ind="idNumberM" class="error-msg"></span>
                                                </div>
                                            </div>
                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label for="designationM">Designation</label>
                                                    <span class="mandatory otherQualificationSpan">*</span>
                                                </div>
                                                <div class="col-sm-6 col-md-7">
                                                    <input maxLength="66" type="text" autocomplete="off" name="designationM" id="designationM" value='<c:out value="${facAdmin.mainAdmin.designation}"/>'/>
                                                    <span data-err-ind="designationM" class="error-msg"></span>
                                                </div>
                                            </div>
                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label for="contactNoM">Contact No.</label>
                                                    <span class="mandatory otherQualificationSpan">*</span>
                                                </div>
                                                <div class="col-sm-6 col-md-7">
                                                    <input maxLength="20" type="text" autocomplete="off" name="contactNoM" id="contactNoM" value='<c:out value="${facAdmin.mainAdmin.contactNo}"/>'/>
                                                    <span data-err-ind="contactNoM" class="error-msg"></span>
                                                </div>
                                            </div>
                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label for="emailM">Email Address</label>
                                                    <span class="mandatory otherQualificationSpan">*</span>
                                                </div>
                                                <div class="col-sm-6 col-md-7">
                                                    <input maxLength="66" type="text" autocomplete="off" name="emailM" id="emailM" value='<c:out value="${facAdmin.mainAdmin.email}"/>'/>
                                                    <span data-err-ind="emailM" class="error-msg"></span>
                                                </div>
                                            </div>
                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label for="employmentStartDateM">Employment Start Date</label>
                                                    <span class="mandatory otherQualificationSpan">*</span>
                                                </div>
                                                <div class="col-sm-6 col-md-7">
                                                    <input type="text" autocomplete="off" name="employmentStartDateM" id="employmentStartDateM" data-date-start-date="01/01/1900" value="<c:out value="${facAdmin.mainAdmin.employmentStartDate}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                                    <span data-err-ind="employmentStartDateM" class="error-msg"></span>
                                                </div>
                                            </div>
                                        </section>

                                        <h3 class="col-12 pl-0" style="border-bottom: 1px solid black">Alternative Adminstrator</h3>
                                        <section id="alternativeAdmin">
                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label for="adminNameA">Name</label>
                                                    <span class="mandatory otherQualificationSpan">*</span>
                                                </div>
                                                <div class="col-sm-6 col-md-7">
                                                    <input maxLength="60" type="text" autocomplete="off" name="adminNameA" id="adminNameA" value='<c:out value="${facAdmin.alternativeAdmin.adminName}"/>'/>
                                                    <span data-err-ind="adminNameA" class="error-msg"></span>
                                                </div>
                                            </div>
                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label for="nationalityA">Nationality</label>
                                                    <span class="mandatory otherQualificationSpan">*</span>
                                                </div>
                                                <div class="col-sm-6 col-md-7">
                                                    <select name="nationalityA" id="nationalityA">
                                                        <c:forEach items="${nationalityOps}" var="na">
                                                            <option value="${na.value}" <c:if test="${facAdmin.alternativeAdmin.nationality eq na.value}">selected="selected"</c:if>>${na.text}</option>
                                                        </c:forEach>
                                                    </select>
                                                    <span data-err-ind="nationalityA" class="error-msg"></span>
                                                </div>
                                            </div>
                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label for="idNumberA">NRIC/FIN</label>
                                                    <span class="mandatory otherQualificationSpan">*</span>
                                                </div>
                                                <div class="col-sm-3">
                                                    <select name="idTypeA" id="idTypeA">
                                                        <option value="IDTYPE001" <c:if test="${facAdmin.alternativeAdmin.idType eq 'IDTYPE001'}">selected="selected"</c:if>>NRIC</option>
                                                        <option value="IDTYPE002" <c:if test="${facAdmin.alternativeAdmin.idType eq 'IDTYPE002'}">selected="selected"</c:if>>FIN</option>
                                                        <option value="IDTYPE003" <c:if test="${facAdmin.alternativeAdmin.idType eq 'IDTYPE003'}">selected="selected"</c:if>>Passport</option>
                                                    </select>
                                                    <span data-err-ind="idTypeA" class="error-msg"></span>
                                                </div>
                                                <div class="col-sm-3 col-md-4">
                                                    <input maxLength="9" type="text" autocomplete="off" name="idNumberA" id="idNumberA" value='<c:out value="${facAdmin.alternativeAdmin.idNumber}"/>'/>
                                                    <span data-err-ind="idNumberA" class="error-msg"></span>
                                                </div>
                                            </div>
                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label for="designationA">Designation</label>
                                                    <span class="mandatory otherQualificationSpan">*</span>
                                                </div>
                                                <div class="col-sm-6 col-md-7">
                                                    <input maxLength="66" type="text" autocomplete="off" name="designationA" id="designationA" value='<c:out value="${facAdmin.alternativeAdmin.designation}"/>'/>
                                                    <span data-err-ind="designationA" class="error-msg"></span>
                                                </div>
                                            </div>
                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label for="contactNoA">Contact No.</label>
                                                    <span class="mandatory otherQualificationSpan">*</span>
                                                </div>
                                                <div class="col-sm-6 col-md-7">
                                                    <input maxLength="20" type="text" autocomplete="off" name="contactNoA" id="contactNoA" value='<c:out value="${facAdmin.alternativeAdmin.contactNo}"/>'/>
                                                    <span data-err-ind="contactNoA" class="error-msg"></span>
                                                </div>
                                            </div>
                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label for="emailA">Email Address</label>
                                                    <span class="mandatory otherQualificationSpan">*</span>
                                                </div>
                                                <div class="col-sm-6 col-md-7">
                                                    <input maxLength="66" type="text" autocomplete="off" name="emailA" id="emailA" value='<c:out value="${facAdmin.alternativeAdmin.email}"/>'/>
                                                    <span data-err-ind="emailA" class="error-msg"></span>
                                                </div>
                                            </div>
                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label for="employmentStartDateA">Employment Start Date</label>
                                                    <span class="mandatory otherQualificationSpan">*</span>
                                                </div>
                                                <div class="col-sm-6 col-md-7">
                                                    <input type="text" autocomplete="off" name="employmentStartDateA" id="employmentStartDateA" data-date-start-date="01/01/1900" value="<c:out value="${facAdmin.alternativeAdmin.employmentStartDate}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                                    <span data-err-ind="employmentStartDateA" class="error-msg"></span>
                                                </div>
                                            </div>
                                        </section>
                                    </div>
                                </div>

                                <%@ include file="../common/InnerFooter.jsp" %>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>