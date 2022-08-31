<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
    String continueURL = "";
    if (process != null && process.runtime != null && process.runtime.getBaseProcessClass() != null) {
        continueURL = process.runtime.continueURL();
    }
%>
<webui:setLayout name="iais-internet"/>

<c:set var="title" value="New Data Submission"/>
<c:set var="smallTitle" value="You are submitting for Assisted Reproduction"/>

<%@ include file="common/header.jsp" %>
<%--@elvariable id="premisesOpts" type=" java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
<%--@elvariable id="arSuperDataSubmissionDto" type="com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto"--%>
<form method="post" id="mainForm" action=<%=continueURL%>>
    <div class="main-content">
        <div class="container center-content">
            <div class="col-xs-12">
                <div class="row form-group" style="border-bottom: 1px solid #D1D1D1;">
                    <div class="col-xs-12 col-md-10">
                        <strong style="font-size: 2rem;">Please key in the information below.</strong>
                    </div>

                    <div class="col-xs-12 col-md-2 text-right">
                        <p class="print" style="font-size: 16px;">
                            <label class="fa fa-print" style="color: #147aab;" onclick="printData()"></label> <a onclick="printData()" href="javascript:void(0);">Print</a>
                        </p>
                    </div>

                    <br>
                </div>

                <div class="row form-group">
                    <div class="col-xs-12 col-md-12"><span>You may submit data for Assisted Reproduction (AR) and Intrauterine Insemination (IUI) cycles in this module.</span>
                    </div>
                </div>

                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <strong>Submit Data</strong>
                            </h4>
                        </div>
                        <div id="arDataSubmission" class="panel-collapse collapse in">
                            <div class="panel-body">
                                <div class="panel-main-content form-horizontal">

                                    <iais:row cssClass="form-check-gp">
                                        <p class="form-check-title">AR Centre that is performing this
                                            submission</p>

                                        <c:choose>
                                            <c:when test="${premisesOpts.size() > 1}">
                                                <iais:select name="centreSel" options="premisesOpts"
                                                             id="centreSel"
                                                             needErrorSpan="false"
                                                             cssClass="centreSel"
                                                             value="${arSuperDataSubmissionDto.centreSel}"/>
                                            </c:when>

                                            <c:otherwise>
                                                <c:out value="${premisesOpts[0].text}"/>
                                            </c:otherwise>
                                        </c:choose>

                                        <span class="error-msg" name="iaisErrorMsg"
                                              id="error_noArLicences"></span>
                                        <span class="error-msg" name="iaisErrorMsg"
                                              id="error_noSelectCenter"><iais:message key="GENERAL_ACK006"/> </span>
                                    </iais:row>

                                    <iais:row cssClass="form-check-gp">
                                        <p class="form-check-title">Please select the mode of submission</p>

                                        <div class="form-check" style="padding-left:0">
                                            <input class="form-check-input" id="DS_MTD001" type="radio"
                                                   name="submissionMethod" value="DS_MTD001"
                                                   <c:if test="${arSuperDataSubmissionDto.submissionMethod eq 'DS_MTD001'}">checked</c:if> />

                                            <label class="form-check-label" for="DS_MTD001">
                                                <span class="check-circle"></span>Form Entry (Submit Single Record)
                                            </label>
                                        </div>

                                        <div class="form-check" style="padding-left:0">
                                            <input class="form-check-input" id="DS_MTD002" type="radio"
                                                   name="submissionMethod" value="DS_MTD002"
                                                   <c:if test="${arSuperDataSubmissionDto.submissionMethod eq 'DS_MTD002'}">checked</c:if> />

                                            <label class="form-check-label" for="DS_MTD002">
                                                <span class="check-circle"></span>Batch Upload (Submit Multiple
                                                Records via File Upload)
                                            </label>
                                        </div>

                                        <span class="error-msg" name="iaisErrorMsg"
                                              id="error_submissionMethod"></span>
                                    </iais:row>

                                    <%--this section display when checkboxs choose Form Entry--%>
                                    <div id="formEntrySection" style="display: none">
                                        <iais:row cssClass="form-check-gp">
                                            <p class="form-check-title">Do you want to register a Donor Sample
                                                Only?</p>

                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input triggerObj" id="donorSampleN"
                                                       type="radio" name="submissionType" value="AR_TP001"
                                                       <c:if test="${arSuperDataSubmissionDto.submissionType eq 'AR_TP001'}">checked</c:if>/>

                                                <label class="form-check-label" for="donorSampleN">
                                                    <span class="check-circle"></span>No
                                                </label>
                                            </div>

                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input triggerObj" id="donorSampleY"
                                                       type="radio" name="submissionType" value="AR_TP003"
                                                       <c:if test="${arSuperDataSubmissionDto.submissionType eq 'AR_TP003'}">checked</c:if>/>

                                                <label class="form-check-label" for="donorSampleY">
                                                    <span class="check-circle"></span>Yes
                                                </label>
                                            </div>

                                            <span class="error-msg" name="iaisErrorMsg"
                                                  id="error_submissionType"></span>
                                        </iais:row>

                                        <div id="donorSampleSection" style="display: none">
                                            <%@include file="section/donorSample.jsp" %>
                                        </div>

                                        <div id="patientSection" style="display: none">
                                            <%@include file="section/arPatient.jsp" %>
                                        </div>
                                    </div>

                                    <%--this section display when checkboxs choose Batch Upload--%>
                                    <div id="batchUpload" style="display: none">
                                        <h1>TODO</h1>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <%@include file="common/footer.jsp" %>
            </div>
        </div>
    </div>
</form>
<%--@elvariable id="hasDraft" type="java.lang.Boolean"--%>
<c:if test="${hasDraft && arSuperDataSubmissionDto.submissionType eq 'AR_TP001'}">
    <iais:confirm msg="DS_MSG001" callBack="submit('page', 'resume');" popupOrder="_draftModal"
                  yesBtnDesc="Resume from draft"
                  cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" needFungDuoJi="false"
                  cancelBtnDesc="Continue" cancelFunc="submit('page', 'delete')"/>
</c:if>

<%--@elvariable id="needCycle" type="java.lang.Boolean"--%>
<c:if test="${needCycle}">
    <iais:confirm msg="DS_MSG036" callBack="submit('page', 'needCycle');" popupOrder="_draftModal" yesBtnDesc="Yes"
                  cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" needFungDuoJi="false"
                  cancelBtnDesc="No" cancelFunc="submit('confirm', 'noCycle')"/>
</c:if>