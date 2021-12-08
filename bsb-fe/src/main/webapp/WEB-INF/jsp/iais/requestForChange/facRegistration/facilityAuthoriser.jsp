<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT"%>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common-facility-register.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-rfc-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-rfc-facility-register.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="../common/dashboard.jsp"%>

<%--@elvariable id="facAuth" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAuthoriserDto"--%>
<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <input type="hidden" name="sectionIdx" value="${TableDisplayUtil.indexes(facAuth.facAuthPersonnelList.size())}">

    <input type="hidden" id="section_repeat_section_idx_name" value="sectionIdx" readonly disabled>
    <input type="hidden" id="section_repeat_section_id_prefix" value="authSection" readonly disabled>
    <input type="hidden" id="section_repeat_header_title_prefix" value="Authorised Personnel " readonly disabled>
    <input type="hidden" id="section_repeat_section_group_id" value="sectionGroup" readonly disabled>
    <input type="hidden" id="section_repeat_separator" value="--v--" readonly disabled>

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
                                        <h3 class="col-12 pl-0" style="border-bottom: 1px solid black">Personnel Authorised to Access the Facility
                                            <a class="btn-tooltip styleguide-tooltip" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="" data-original-title="<p>The Facility Administrator/Alternate Facility Administrator is responsible to ensure that the list of authorised personnel is always kept up to date i.e. prompt submission of updates to include newly authorized personnel or to remove personnel who are no longer authorized to access the facility.</p>">i</a>
                                        </h3>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>Input Method</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <div class="col-sm-5" style="margin-top: 8px">
                                                    <input type="radio" name="inputMethod" id="inputMethodUpload" value="upload" <c:if test="${facAuth.inputMethod eq 'upload'}">checked="checked"</c:if> />
                                                    <label for="inputMethodUpload">Upload</label>
                                                </div>
                                                <div class="col-sm-5" style="margin-top: 8px">
                                                    <input type="radio" name="inputMethod" id="inputMethodManual" value="manual" <c:if test="${facAuth.inputMethod eq 'manual'}">checked="checked"</c:if> />
                                                    <label for="inputMethodManual">Manual</label>
                                                </div>
                                            </div>
                                        </div>

                                        <div id="sectionGroup">
                                            <c:forEach var="auth" items="${facAuth.facAuthPersonnelList}" varStatus="status">
                                                <section id="authSection--v--${status.index}">
                                                    <c:if test="${facAuth.facAuthPersonnelList.size() > 1}">
                                                        <div class="form-group">
                                                            <h3 class="col-xs-9 col-sm-10 col-md-11" style="border-bottom: 1px solid black">Authorised Personnel ${status.index + 1}</h3>
                                                            <c:if test="${status.index gt 0}">
                                                                <div class="col-sm-1"><h4 class="text-danger"><em data-current-idx="${status.index}" class="fa fa-times-circle del-size-36 cursorPointer removeBtn"></em></h4></div>
                                                            </c:if>
                                                        </div>
                                                    </c:if>
                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="name--v--${status.index}">Name</label>
                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <input maxLength="132" type="text" autocomplete="off" name="name--v--${status.index}" id="name--v--${status.index}" value='<c:out value="${auth.name}"/>'/>
                                                            <span data-err-ind="name--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="idNumber--v--${status.index}">ID No.</label>
                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                        </div>
                                                        <div class="col-sm-3">
                                                            <select name="idType--v--${status.index}" id="idType--v--${status.index}">
                                                                <option value="IDTYPE001" <c:if test="${auth.idType eq 'IDTYPE001'}">selected="selected"</c:if>>NRIC</option>
                                                                <option value="IDTYPE002" <c:if test="${auth.idType eq 'IDTYPE002'}">selected="selected"</c:if>>FIN</option>
                                                                <option value="IDTYPE003" <c:if test="${auth.idType eq 'IDTYPE003'}">selected="selected"</c:if>>Passport</option>
                                                            </select>
                                                            <span data-err-ind="idType--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                        <div class="col-sm-3 col-md-4">
                                                            <input maxLength="9" type="text" autocomplete="off" name="idNumber--v--${status.index}" id="idNumber--v--${status.index}" value='<c:out value="${auth.idNumber}"/>'/>
                                                            <span data-err-ind="idNumber--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="nationality--v--${status.index}">Nationality</label>
                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <select name="nationality--v--${status.index}" id="nationality--v--${status.index}">
                                                                <c:forEach items="${nationalityOps}" var="na">
                                                                    <option value="${na.value}" <c:if test="${auth.nationality eq na.value}">selected="selected"</c:if>>${na.text}</option>
                                                                </c:forEach>
                                                            </select>
                                                            <span data-err-ind="nationality--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="designation--v--${status.index}">Designation</label>
                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <input maxLength="66" type="text" autocomplete="off" name="designation--v--${status.index}" id="designation--v--${status.index}" value='<c:out value="${auth.designation}"/>'/>
                                                            <span data-err-ind="designation--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="contactNo--v--${status.index}">Contact No.</label>
                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <input maxLength="20" type="text" autocomplete="off" name="contactNo--v--${status.index}" id="contactNo--v--${status.index}" value='<c:out value="${auth.contactNo}"/>'/>
                                                            <span data-err-ind="contactNo--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="email--v--${status.index}">Email Address</label>
                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <input maxLength="66" type="text" autocomplete="off" name="email--v--${status.index}" id="email--v--${status.index}" value='<c:out value="${auth.email}"/>'/>
                                                            <span data-err-ind="email--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="employmentStartDate--v--${status.index}">Employment Start Date</label>
                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <input type="text" autocomplete="off" name="employmentStartDate--v--${status.index}" id="employmentStartDate--v--${status.index}" data-date-start-date="01/01/1900" value="<c:out value="${auth.employmentStartDate}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                                            <span data-err-ind="employmentStartDate--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="employmentPeriod--v--${status.index}">Employment Period</label>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <input maxLength="2" type="text" autocomplete="off" name="employmentPeriod--v--${status.index}" id="employmentPeriod--v--${status.index}" value='<c:out value="${auth.employmentPeriod}"/>'/>
                                                            <span data-err-ind="employmentPeriod--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="securityClearanceDate--v--${status.index}">Security Clearance Date</label>
                                                            <a class="btn-tooltip styleguide-tooltip" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="" data-original-title="<p>For Facility that is Protected Place, security clearance date is mandatory.</p>">i</a>
                                                            <c:if test="${facAuth.isProtectedPlace eq 'Y'}">
                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                            </c:if>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <input type="text" autocomplete="off" name="securityClearanceDate--v--${status.index}" id="securityClearanceDate--v--${status.index}" data-date-start-date="01/01/1900" value="<c:out value="${auth.securityClearanceDate}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                                            <span data-err-ind="securityClearanceDate--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="workArea--v--${status.index}">Area of Work</label>
                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                        </div>
                                                        <div class="col-sm-6 col-md-7">
                                                            <input maxLength="100" type="text" autocomplete="off" name="workArea--v--${status.index}" id="workArea--v--${status.index}" value='<c:out value="${auth.workArea}"/>'/>
                                                            <span data-err-ind="workArea--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>
                                                </section>
                                            </c:forEach>
                                        </div>
                                        <div class="form-group">
                                            <div class="col-12">
                                                <a id="addNewSection" style="text-decoration: none" href="javascript:void(0)">+ Add New Authorised Personnel</a>
                                            </div>
                                        </div>
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