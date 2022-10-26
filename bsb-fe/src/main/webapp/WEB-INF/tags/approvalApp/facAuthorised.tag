<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="authPersonList" required="true" type="java.util.ArrayList<sg.gov.moh.iais.egp.bsb.dto.entity.FacilityAuthoriserDto>" %>
<%@attribute name="authorisedSelectionMap" required="true" type="java.util.HashMap<java.lang.String,sg.gov.moh.iais.egp.bsb.dto.register.approval.AuthorisedSelection>" %>
<%@attribute name="specialJsFrag" fragment="true" %>
<%@attribute name="dashboardFrag" fragment="true" %>
<%@attribute name="innerFooterFrag" fragment="true" %>
<%@attribute name="editJudge" type="java.lang.Boolean" %>
<%@attribute name="editableFieldSet" type="java.lang.String" %>
<%@attribute name="hasError" type="java.lang.Boolean" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common-node-group.js"></script>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common-add-section.js"></script>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common-edit.js"></script>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/edit/bsb-approval-edit-bat.js"></script>
<jsp:invoke fragment="specialJsFrag"/>


<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>
<jsp:invoke fragment="dashboardFrag"/>
<form method="post" id="mainForm" enctype="multipart/form-data" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <input type="hidden" id="editJudge" value="${editJudge}" readonly disabled>
    <input type="hidden" id="editableFieldSet" value="${editableFieldSet}" readonly disabled>
    <input type="hidden" id="hasError" value="${hasError}" readonly disabled>

    <input type="hidden" name="sectionIdx" value="<%=sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil.indexes(authPersonList.size())%>">
    <input type="hidden" name="authPersonListSize" value="${authPersonList.size()}">
    <input type="hidden" id="section_repeat_section_idx_name" value="sectionIdx" readonly disabled>
    <input type="hidden" id="section_repeat_section_id_prefix" value="workActivitySection" readonly disabled>
    <input type="hidden" id="section_repeat_section_group_id" value="sectionGroup" readonly disabled>
    <input type="hidden" id="section_repeat_separator" value="--v--" readonly disabled>


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
                                        <div class="row">
                                            <c:if test="${editJudge and not hasError}"><div class="text-right app-font-size-16"><a id="edit" href="javascript:void(0)"><em class="fa fa-pencil-square-o"></em>Edit</a></div></c:if>
                                            <h3 class="col-xs-9 col-sm-10 col-md-11" style="border-bottom: 1px solid black">Authorised Personnel   <a class="btn-tooltip styleguide-tooltip" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="" data-original-title="<p>Note: Please indicate the personnel who are identified to work with/handle the Second Schedule biological agent.</p>">i</a></h3>
                                            <table class="table">
                                                <tr>
                                                    <th>S/N</th>
                                                    <th>Name</th>
                                                    <th>ID Number</th>
                                                    <th style="width: 13%">Is this person an employee of this Company</th>
                                                    <th style="width: 10%">Company Name</th>
                                                    <th style="width: 45%">Work involving Second Schedule Biological Agent   <a class="btn-tooltip styleguide-tooltip" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="" data-original-title="<p>Please specify in detail the nature of the work involving the Second Schedule biological agent that the identified personnel is expected to perform.</p>">i</a></th>
                                                    <th>Performing for Second Schedule Biological Agent</th>
                                                </tr>
                                                <c:choose>
                                                    <c:when test="${empty authPersonList}">
                                                        <tr>
                                                            <td colspan="7">No Authorised Personnel work with/handle the Second Schedule biological agent to be selected</td>
                                                        </tr>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:forEach var="item" items="${authPersonList}" varStatus="status">
                                                            <c:set var="authSelection" value="${authorisedSelectionMap.get(item.id)}" />
                                                            <%--@elvariable id="authSelection" type="sg.gov.moh.iais.egp.bsb.dto.register.approval.AuthorisedSelection"--%>

                                                            <%--according to this part,can judge this data is submitted or not--%>
                                                            <c:if test="${not empty authSelection}">
                                                                <c:set var="maskedSpHandleAuthId"><iais:mask name="spAuthId" value="${authSelection.spHandleAuthId}" /></c:set>
                                                                <input type="hidden" name="maskedSpHandleAuthId" value="${maskedSpHandleAuthId}--v--${status.index}">
                                                            </c:if>

                                                            <c:set var="maskedAuthId"><iais:mask name="authId" value="${item.id}" /></c:set>
                                                            <tr>
                                                                <td><c:out value="${status.index+1}"/><input type="hidden" value="${maskedAuthId}" name="authorisedPerId--v--${status.index}"></td>
                                                                <td><c:out value="${item.name}"/></td>
                                                                <td><c:out value="${item.idNumber}"/></td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${'Y' == item.employeeOfComp}"><c:out value="Yes"/></c:when>
                                                                        <c:when test="${'N' == item.employeeOfComp}"><c:out value="No"/></c:when>
                                                                    </c:choose>
                                                                </td>
                                                                <td>
                                                                    <c:if test="${'N' eq item.employeeOfComp}">
                                                                        <c:out value="${item.externalCompName}" />
                                                                    </c:if>
                                                                </td>
                                                                <td><label for="involvedWork--v--${status.index}"></label><textarea maxlength="500" cols="60" rows="4" id="involvedWork--v--${status.index}" name="involvedWork--v--${status.index}" style="resize: none">${authSelection.involvedWork}</textarea>
                                                                    <span data-err-ind="involvedWork--v--${status.index}" class="error-msg"></span>
                                                                </td>
                                                                <td><label><input type="checkbox" id="isPerformed--v--${status.index}" name="isPerformed--v--${status.index}" value="Y" <c:if test="${'Y' eq authSelection.isPerformed}">checked="checked"</c:if> ></label>
                                                                    <span data-err-ind="isPerformed--v--${status.index}" class="error-msg"></span>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td colspan="7"><span data-err-ind="authPersonList" class="error-msg"></span></td>
                                                            </tr>
                                                        </c:forEach>
                                                    </c:otherwise>
                                                </c:choose>
                                            </table>
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