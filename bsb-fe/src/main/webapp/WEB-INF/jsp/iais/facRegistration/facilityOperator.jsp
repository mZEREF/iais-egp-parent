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
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-facility-register.js"></script>

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
                                        <h3 class="col-12" style="border-bottom: 1px solid black">Facility Operator</h3>
                                        <%--@elvariable id="facOperator" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityOperatorDto"--%>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="facOperator">Facility Operator</label>
                                                <a class="btn-tooltip styleguide-tooltip" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="" data-original-title="<p>The Facility Operator is the person who has overall control and oversight of the management of the facility such as the Chief Executive Officer of the company or a person of equivalent level.</p>">i</a>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input type="text" autocomplete="off" name="facOperator" id="facOperator" value='<c:out value="${facOperator.facOperator}"/>'/>
                                                <span data-err-ind="facOperator" class="error-msg"></span>
                                            </div>
                                        </div>
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label><strong>Facility Operator Designee:</strong></label>
                                                <a class="btn-tooltip styleguide-tooltip" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="" data-original-title="<p>The Facility Operator Designee is the person who is appointed by the Facility Operator to oversee the day to day management and operations at the facility.</p>">i</a>
                                            </div>
                                        </div>
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="operatorName">Name</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input type="text" autocomplete="off" name="operatorName" id="operatorName" value='<c:out value="${facOperator.designeeName}"/>'/>
                                                <span data-err-ind="facName" class="error-msg"></span>
                                            </div>
                                        </div>
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="idNumber">NRIC/FIN</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-3">
                                                <select name="idType" id="idType">
                                                    <option value="IDTYPE001" <c:if test="${facOperator.idType eq 'IDTYPE001'}">selected="selected"</c:if>>NRIC</option>
                                                    <option value="IDTYPE002" <c:if test="${facOperator.idType eq 'IDTYPE002'}">selected="selected"</c:if>>FIN</option>
                                                    <option value="IDTYPE003" <c:if test="${facOperator.idType eq 'IDTYPE003'}">selected="selected"</c:if>>Passport</option>
                                                </select>
                                                <span data-err-ind="idType" class="error-msg"></span>
                                            </div>
                                            <div class="col-sm-3 col-md-4">
                                                <input type="text" autocomplete="off" name="idNumber" id="idNumber" value='<c:out value="${facOperator.idNumber}"/>'/>
                                                <span data-err-ind="idNumber" class="error-msg"></span>
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
                                                        <option value="${na.value}" <c:if test="${facOperator.nationality eq na.value}">selected="selected"</c:if>>${na.text}</option>
                                                    </c:forEach>
                                                </select>
                                                <span data-err-ind="nationality" class="error-msg"></span>
                                            </div>
                                        </div>
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="designation">Designation</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input type="text" autocomplete="off" name="designation" id="designation" value='<c:out value="${facOperator.designation}"/>'/>
                                                <span data-err-ind="designation" class="error-msg"></span>
                                            </div>
                                        </div>
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="contactNo">Contact No.</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input type="text" autocomplete="off" name="contactNo" id="contactNo" value='<c:out value="${facOperator.contactNo}"/>'/>
                                                <span data-err-ind="contactNo" class="error-msg"></span>
                                            </div>
                                        </div>
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="email">Email</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input type="text" autocomplete="off" name="email" id="email" value='<c:out value="${facOperator.email}"/>'/>
                                                <span data-err-ind="email" class="error-msg"></span>
                                            </div>
                                        </div>
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="employmentStartDate">Employment Start Date</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input type="text" autocomplete="off" name="employmentStartDate" id="employmentStartDate" data-date-start-date="01/01/1900" value="<c:out value="${facOperator.employmentStartDate}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
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