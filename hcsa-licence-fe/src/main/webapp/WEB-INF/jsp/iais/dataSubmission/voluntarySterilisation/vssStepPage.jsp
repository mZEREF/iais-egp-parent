<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<webui:setLayout name="iais-internet"/>

<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process2 =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<%@ include file="common/vssHeader.jsp" %>
<c:set var="canEdit" value="${VSS_CURRENT_STEP.showEdit}" scope="request"/>
<c:set var="headingSign" value="completed"/>
<%--<c:set var="headingSign" value="${headingStatus = 1 ? 'completed' : 'incompleted' }"/>--%>
<form method="post" id="mainForm" action="<%=process2.runtime.continueURL()%>">
    <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@ include file="common/navTabs.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane in active">
                                <c:set var="currCode" value="${VSS_CURRENT_STEP.code}" scope="request"/>
                               <%-- ------------------${currCode}----------------%>
                                <c:choose>
                                    <c:when test="${currCode == 'VSST001'}">
                                        <%@ include file="section/treatmentDetails.jsp" %>
                                    </c:when>
                                    <c:when test="${currCode == 'VSST002'}">
                                        <%@ include file="section/particularsConsentOrCourtOrder.jsp" %>
                                    </c:when>
                                    <c:when test="${currCode == 'VSST003'}">
                                        <%@ include file="section/particularsSterilizationSection.jsp" %>
                                    </c:when>
                                    <c:when test="${currCode == 'VSST010'}">
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
                                            <%@ include file="section/previewTreatmentDetails.jsp" %>
                                            <%@ include file="section/previewParticularsConsentOrCourtOrder.jsp" %>
                                            <%@ include file="section/previewParticularsSterilizationSection.jsp" %>
                                            <%@ include file="common/vssDeclaration.jsp" %>
                                        </div>
                                    </c:when>
                                </c:choose>
                                <%@ include file="common/vssFooter.jsp" %>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<c:if test="${hasDraft}">
    <iais:confirm msg="DS_MSG022" callBack="submit('resume');" popupOrder="_draftModal"  yesBtnDesc="Resume from draft"
                  cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary"
                  cancelBtnDesc="Continue" cancelFunc="submit('delete');" />
</c:if>
<iais:confirm msg="DS_MSG011" callBack="$('#validateVD').modal('hide');" popupOrder="validateVD" yesBtnDesc="Close"
              yesBtnCls="btn btn-secondary" needCancel="false" needFungDuoJi="false"/>
<input type="hidden" id="showValidateVD" name="showValidateVD" value="${showValidateVD}"/>