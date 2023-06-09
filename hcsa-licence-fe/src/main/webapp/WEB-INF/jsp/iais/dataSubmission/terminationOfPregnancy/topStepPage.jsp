<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ page import="com.ecquaria.cloud.moh.iais.helper.MessageUtil" %>

<webui:setLayout name="iais-internet"/>

<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process2 =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<%@ include file="common/topHeader.jsp" %>

<c:set var="canEdit" value="${TOP_CURRENT_STEP.showEdit}" scope="request"/>

<%--@elvariable id="topSuperDataSubmissionDto" type="com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TopSuperDataSubmissionDto"--%>
<c:set var="terminationOfPregnancyDto" value="${topSuperDataSubmissionDto.terminationOfPregnancyDto}"/>
<c:set var="preTerminationDto" value="${terminationOfPregnancyDto.preTerminationDto}"/>
<form method="post" id="mainForm" action="<%=process2.runtime.continueURL()%>">
    <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@ include file="common/navTabs.jsp" %>
                        <span id="error_topErrorMsg" name="iaisErrorMsg" class="error-msg"></span>
                        <div class="tab-content">
                            <div class="tab-pane in active">
                                <c:set var="currCode" value="${TOP_CURRENT_STEP.code}" scope="request"/>
                                <%--------------------${currCode}----------------%>
                                <c:choose>
                                    <c:when test="${currCode == 'TOPT001'}">
                                        <%@ include file="section/patientDetails.jsp" %>
                                    </c:when>
                                    <c:when test="${currCode == 'TOPT002'}">
                                        <%@ include file="section/familyPlanning.jsp" %>
                                    </c:when>
                                    <c:when test="${currCode == 'TOPT003'}">
                                        <%@ include file="section/preTerminationOfPregnancyCounselling.jsp" %>
                                    </c:when>
                                    <c:when test="${currCode == 'TOPT004'}">
                                        <%@ include file="section/presentTerminationOfPregnancy.jsp" %>
                                    </c:when>
                                    <c:when test="${currCode == 'TOPT005'}">
                                        <%@ include file="section/postTerminationOfPregnancyCounselling.jsp" %>
                                    </c:when>
                                    <c:when test="${currCode == 'TOPT010'}">
                                        <div class="row form-group" style="border-bottom: 1px solid #D1D1D1;">
                                            <div class="col-xs-12 col-md-10">
                                            </div>
                                            <div class="col-xs-12 col-md-2 text-right">
                                                <p class="print" style="font-size: 16px;">
                                                    <label class="fa fa-print" style="color: #147aab;" onclick="printData()"></label> <a onclick="printData()" href="javascript:void(0);">Print</a>
                                                </p>
                                            </div>
                                        </div>
                                        <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                                            <%@ include file="section/previewPatientDetails.jsp" %>
                                            <%@ include file="section/previewFamilyPlanning.jsp" %>
                                            <%@ include file="section/previewPreTermination.jsp" %>
                                            <%@ include file="section/previewPresentTermination.jsp" %>
                                            <%@ include file="section/previewPostTermination.jsp" %>
                                            <%@ include file="common/topDsAmendment.jsp" %>
                                            <%@ include file="common/topDeclaration.jsp" %>
                                        </div>
                                    </c:when>
                                </c:choose>
                                <%@ include file="common/topFooter.jsp" %>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<c:if test="${hasDrafts}">
    <iais:confirm msg="DS_MSG023" callBack="submit('resume');" popupOrder="_draftModal"  yesBtnDesc="Resume from draft"
                  cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary"
                  cancelBtnDesc="Continue" cancelFunc="submit('delete');" />
</c:if>
<iais:confirm msg="DS_MSG024" callBack="$('#noFoundDiv').modal('hide');" popupOrder="noFoundDiv"  yesBtnDesc="Close" yesBtnCls="btn btn-secondary"
              needFungDuoJi="false" needCancel="false"/>
<iais:confirm msg="DS_MSG007" callBack="$('#validatePT').modal('hide');" popupOrder="validatePT" yesBtnDesc="Close"
              yesBtnCls="btn btn-secondary" needCancel="false" needFungDuoJi="false"/>
<input type="hidden" id="showValidatePT" name="showValidatePT" value="${showValidatePT}"/>