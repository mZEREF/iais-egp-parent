<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="authPersonnelIds" required="true" type="java.util.HashSet<java.lang.String>" %>
<%@attribute name="facilityAuthIdMap" required="true" type="java.util.HashMap<java.lang.String,sg.gov.moh.iais.egp.bsb.dto.entity.FacilityAuthoriserDto>" %>
<%@attribute name="authPersonnelOps" required="true" type="java.util.ArrayList<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>" %>
<%@attribute name="specialJsFrag" fragment="true" %>
<%@attribute name="dashboardFrag" fragment="true" %>
<%@attribute name="innerFooterFrag" fragment="true" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common-node-group.js"></script>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common-add-section.js"></script>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-approval-special.js"></script>
<jsp:invoke fragment="specialJsFrag"/>

<script>
    <% String jsonStr = (String) request.getAttribute("authPersonnelDetailMapJson");
       if (jsonStr == null || "".equals(jsonStr)) {
           jsonStr = "undefined";
       }
    %>
    var authPersonnelDetailDataJson = <%=jsonStr%>;
</script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>
<jsp:invoke fragment="dashboardFrag"/>
<form method="post" id="mainForm" enctype="multipart/form-data" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">

    <input type="hidden" id="section_repeat_section_idx_name" value="sectionIdx" readonly disabled>
    <input type="hidden" id="section_repeat_section_id_prefix" value="facAuthorisedPersonnel" readonly disabled>
    <input type="hidden" id="section_repeat_header_title_prefix" value="Authorised Personnel " readonly disabled>
    <input type="hidden" id="section_repeat_section_group_id" value="sectionGroup" readonly disabled>
    <input type="hidden" id="section_repeat_separator" value="--v--" readonly disabled>
    <input type="hidden" name="sectionIdx" value="<%=sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil.indexes(authPersonnelIds.size())%>">

    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@ include file="/WEB-INF/jsp/iais/approvalBatAndActivity/InnerNavTab.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div id="appInfoPanel" role="tabpanel">
                                    <%@include file="/WEB-INF/jsp/iais/approvalBatAndActivity/subStepNavTab.jsp"%>
                                    <div class="form-horizontal">
                                        <div id="sectionGroup">
                                            <%--@elvariable id="authPersonnelIds" type="java.util.HashSet<java.lang.String>"--%>
                                            <c:forEach  var="personnel" items="${authPersonnelIds}" varStatus="status">
                                                <%--@elvariable id="facilityAuthIdMap" type="java.util.HashMap<java.lang.String,sg.gov.moh.iais.egp.bsb.dto.entity.FacilityAuthoriserDto>"--%>
                                                <c:set var="auth" value="${facilityAuthIdMap.get(personnel)}" />
                                                <%--@elvariable id="auth" type="sg.gov.moh.iais.egp.bsb.dto.entity.FacilityAuthoriserDto"--%>
                                                <%--@elvariable id="authPersonnelOps" type="java.util.ArrayList<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
                                                <section id="facAuthorisedPersonnel--v--${status.index}">
                                                    <div class="form-group">
                                                        <c:choose>
                                                            <c:when test="${authPersonnelIds.size() eq 1}">
                                                                <h3 class="col-xs-9 col-sm-10 col-md-11" style="border-bottom: 1px solid black">Authorised Personnel<a class="btn-tooltip styleguide-tooltip" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="" data-original-title="<p>Note: Please indicate the personnel who are identified to work with/handle the Second Schedule biological agent.</p>">i</a></h3>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <h3 class="col-xs-9 col-sm-10 col-md-11" style="border-bottom: 1px solid black">Authorised Personnel ${status.index+1}<a class="btn-tooltip styleguide-tooltip" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="" data-original-title="<p>Note: Please indicate the personnel who are identified to work with/handle the Second Schedule biological agent.</p>">i</a></h3>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </div>
                                                    <div class="form-group ">
                                                        <div class="col-sm-5 control-label">
                                                            <label for="personnel--v--${status.index}">Select Personnel </label>
                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                        </div>
                                                        <div class="col-sm-6" style="z-index: 20;">
                                                            <select name="personnel--v--${status.index}" id="personnel--v--${status.index}" data-cascade-dropdown="authPersonnel-detail">
                                                                <option value="">Please Select</option>
                                                                <c:forEach items="${authPersonnelOps}" var="item">
                                                                    <option value="${item.value}" <c:if test="${auth.idNumber eq item.value}">selected="selected"</c:if>>${item.text}</option>
                                                                </c:forEach>
                                                            </select>
                                                            <span data-err-ind="personnel--v--${status.index}" class="error-msg"></span>
                                                        </div>
                                                    </div>

                                                    <div id="authPersonnelInfo--v--${status.index}" <c:if test="${auth eq null}">style="display: none"</c:if>>
                                                        <div class="form-group ">
                                                            <div class="col-sm-5 control-label">
                                                                <label for="name--v--${status.index}">Name</label>
                                                            </div>
                                                            <div class="col-sm-6">
                                                                <label id="name--v--${status.index}" data-cascade-label = "authPersonnelInfo${status.index}">${auth.name}</label>
                                                            </div>
                                                        </div>
                                                        <div class="form-group ">
                                                            <div class="col-sm-5 control-label">
                                                                <label for="idType--v--${status.index}">ID Type</label>
                                                            </div>
                                                            <div class="col-sm-6">
                                                                <label id="idType--v--${status.index}" data-cascade-label = "authPersonnelInfo${status.index}">${auth.idType}</label>
                                                            </div>
                                                        </div>
                                                        <div class="form-group ">
                                                            <div class="col-sm-5 control-label">
                                                                <label for="idNumber--v--${status.index}">ID No</label>
                                                            </div>
                                                            <div class="col-sm-6">
                                                                <label id="idNumber--v--${status.index}" data-cascade-label = "authPersonnelInfo${status.index}">${auth.idNumber}</label>
                                                            </div>
                                                        </div>
                                                        <div class="form-group ">
                                                            <div class="col-sm-5 control-label">
                                                                <label for="nationality--v--${status.index}">Nationality</label>
                                                            </div>
                                                            <div class="col-sm-6">
                                                                <label id="nationality--v--${status.index}" data-cascade-label = "authPersonnelInfo${status.index}">${auth.nationality}</label>
                                                            </div>
                                                        </div>
                                                        <div class="form-group ">
                                                            <div class="col-sm-5 control-label">
                                                                <label for="designation--v--${status.index}">Designation</label>
                                                            </div>
                                                            <div class="col-sm-6">
                                                                <label id="designation--v--${status.index}" data-cascade-label = "authPersonnelInfo${status.index}">${auth.designation}</label>
                                                            </div>
                                                        </div>
                                                        <div class="form-group ">
                                                            <div class="col-sm-5 control-label">
                                                                <label for="contactNo--v--${status.index}">Contact No</label>
                                                            </div>
                                                            <div class="col-sm-6">
                                                                <label id="contactNo--v--${status.index}" data-cascade-label = "authPersonnelInfo${status.index}">${auth.contactNo}</label>
                                                            </div>
                                                        </div>
                                                        <div class="form-group ">
                                                            <div class="col-sm-5 control-label">
                                                                <label for="email--v--${status.index}">Email Address</label>
                                                            </div>
                                                            <div class="col-sm-6">
                                                                <label id="email--v--${status.index}" data-cascade-label = "authPersonnelInfo${status.index}">${auth.email}</label>
                                                            </div>
                                                        </div>
                                                        <div class="form-group ">
                                                            <div class="col-sm-5 control-label">
                                                                <label for="employmentStartDate--v--${status.index}">Employment Start Date</label>
                                                            </div>
                                                            <div class="col-sm-6">
                                                                <label id="employmentStartDate--v--${status.index}" data-cascade-label = "authPersonnelInfo${status.index}">${auth.employmentStartDate}</label>
                                                            </div>
                                                        </div>
                                                        <div class="form-group ">
                                                            <div class="col-sm-5 control-label">
                                                                <label for="securityClearanceDate--v--${status.index}">Security Clearance Date</label>
                                                            </div>
                                                            <div class="col-sm-6">
                                                                <label id="securityClearanceDate--v--${status.index}" data-cascade-label = "authPersonnelInfo${status.index}">${auth.securityClearanceDate}</label>
                                                            </div>
                                                        </div>
                                                        <div class="form-group ">
                                                            <div class="col-sm-5 control-label">
                                                                <label for="workArea--v--${status.index}">Area of Work</label>
                                                            </div>
                                                            <div class="col-sm-6">
                                                                <label id="workArea--v--${status.index}" data-cascade-label = "authPersonnelInfo${status.index}">${auth.workArea}</label>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </section>
                                            </c:forEach>
                                        </div>
                                        <div class="form-group">
                                            <div class="col-12">
                                                <a id="addNewSection"  style="text-decoration: none" href="javascript:void(0)">+ Add Authorised Personnel</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <jsp:invoke fragment="innerFooterFrag"/>
                            </div>
                        </div>
                        <%@include file="/WEB-INF/jsp/iais/include/jumpAfterDraft.jsp"%>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>