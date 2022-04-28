<%@tag description="Facility operator tag of facility registration" %>
<%@taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>

<%@attribute name="facOperator" required="true" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityOperatorDto" %>
<%@attribute name="salutationOps" required="true" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>" %>
<%@attribute name="nationalityOps" required="true" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common-node-group.js"></script>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-facility-register.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp" %>
<%@include file="/WEB-INF/jsp/iais/facRegistration/dashboard.jsp" %>
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
                        <%@include file="/WEB-INF/jsp/iais/mainAppCommon/facRegistration/InnerNavTab.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div id="facInfoPanel" role="tabpanel">
                                    <%@include file="/WEB-INF/jsp/iais/mainAppCommon/facRegistration/subStepNavTab.jsp" %>
                                    <div class="form-horizontal">
                                        <h3 class="col-12 pl-0" style="border-bottom: 1px solid black">Facility Operator Profile</h3>
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="facOperator">Designation of Facility Operator</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input maxLength="60" type="text" autocomplete="off" name="facOperator" id="facOperator" value='<c:out value="${facOperator.facOperator}"/>'/>
                                                <span data-err-ind="facOperator" class="error-msg"></span>
                                            </div>
                                            <div class="col-sm-12 control-label" style="font-size: 14px">
                                                Note: The Facility Operator is the person who has overall control and oversight of the management of the facility such as the Chief Executive Officer of the company or a person of equivalent level.
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
                                                <label for="salutation">Salutation</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <select name="salutation"  class="salutationdd" id="salutation">
                                                    <option value="">Please Select</option>
                                                    <c:forEach var="item" items="${salutationOps}">
                                                        <option value="${item.value}" <c:if test="${facOperator.salutation eq item.value}">selected="selected"</c:if>>${item.text}</option>
                                                    </c:forEach>
                                                </select>
                                                <span data-err-ind="salutation" class="error-msg"></span>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="operatorName">Name</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input maxLength="60" type="text" autocomplete="off" name="operatorName" id="operatorName" value='<c:out value="${facOperator.designeeName}"/>'/>
                                                <span data-err-ind="designeeName" class="error-msg"></span>
                                            </div>
                                        </div>
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="idNumber">ID No</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-3">
                                                <select name="idType" class="idTypedd" id="idType">
                                                    <option value="IDTYPE001" <c:if test="${facOperator.idType eq 'IDTYPE001'}">selected="selected"</c:if>>NRIC</option>
                                                    <option value="IDTYPE002" <c:if test="${facOperator.idType eq 'IDTYPE002'}">selected="selected"</c:if>>FIN</option>
                                                </select>
                                                <span data-err-ind="idType" class="error-msg"></span>
                                            </div>
                                            <div class="col-sm-3 col-md-4">
                                                <input maxLength="9" type="text" autocomplete="off" name="idNumber" id="idNumber" value='<c:out value="${facOperator.idNumber}"/>'/>
                                                <span data-err-ind="idNumber" class="error-msg"></span>
                                            </div>
                                        </div>
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="nationality">Nationality</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <select name="nationality" class="nationalitydd" id="nationality">
                                                    <option value="">Please Select</option>
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
                                                <input maxLength="66" type="text" autocomplete="off" name="designation" id="designation" value='<c:out value="${facOperator.designation}"/>'/>
                                                <span data-err-ind="designation" class="error-msg"></span>
                                            </div>
                                        </div>
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="contactNo">Contact No.</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input maxLength="20" type="text" autocomplete="off" name="contactNo" id="contactNo" value='<c:out value="${facOperator.contactNo}"/>'/>
                                                <span data-err-ind="contactNo" class="error-msg"></span>
                                            </div>
                                        </div>
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="email">Email</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input maxLength="66" type="text" autocomplete="off" name="email" id="email" value='<c:out value="${facOperator.email}"/>'/>
                                                <span data-err-ind="email" class="error-msg"></span>
                                            </div>
                                        </div>
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="employmentStartDt">Employment Start Date</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input type="text" autocomplete="off" name="employmentStartDt" id="employmentStartDt" data-date-start-date="01/01/1900" value="<c:out value="${facOperator.employmentStartDt}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                                <span data-err-ind="employmentStartDt" class="error-msg"></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <%@include file="/WEB-INF/jsp/iais/facRegistration/InnerFooter.jsp" %>
                            </div>
                        </div>
                        <%@include file="/WEB-INF/jsp/iais/include/jumpAfterDraft.jsp" %>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>