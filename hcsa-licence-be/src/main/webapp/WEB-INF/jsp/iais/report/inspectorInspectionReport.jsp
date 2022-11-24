<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
    String webroot = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;

%>
<webui:setLayout name="iais-intranet"/>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" name="crud_action_type" id="crud_action_type"/>
        <input type="hidden" name="crud_action_value" id="crud_action_value"/>
        <input type="hidden" name="crud_action_additional" id="crud_action_additional"/>
        <input type="hidden" name="readRecom" value="">
        <input type="hidden" name="appType" value="${appType}">
        <input type="hidden" name="rfiCheckErrorMsg" id="rfiCheckErrorMsg" value="<iais:message key="PRF_ERR012" escape="true"/>"/>
        <input type="hidden" name="errorMsgGENERAL_ERR0006" id="errorMsgGENERAL_ERR0006" value="<iais:message key="GENERAL_ERR0006"/>"/>
        <c:set var="isAoRouteBackStatus" value="${applicationViewDto.applicationDto.status == 'APST062' || applicationViewDto.applicationDto.status == 'APST065' || applicationViewDto.applicationDto.status == 'APST066' || applicationViewDto.applicationDto.status == 'APST067'}"/>
        <c:set var="isPsoRouteBackStatus" value="${applicationViewDto.applicationDto.status == 'APST063'}"/>
        <c:set var="isInspectorRouteBackStatus" value="${applicationViewDto.applicationDto.status == 'APST064'}"/>
        <c:set var="isRouteBackStatus" value="${isInspectorRouteBackStatus || isAoRouteBackStatus || isPsoRouteBackStatus}"/>
        <div class="main-content">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="center-content">
                        <div class="intranet-content">
                            <iais:body>
                                <div class="col-xs-12">
                                        <div class="tab-gp dashboard-tab">
                                            <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                                                <li id="info" class="${infoClassTop}" role="presentation"><a
                                                        href="#tabInfo"
                                                        aria-controls="tabInfo"
                                                        role="tab"
                                                        data-toggle="tab">Info</a>
                                                </li>
                                                <li class="complete" role="presentation"><a href="#tabDocuments"
                                                                                            aria-controls="tabDocuments"
                                                                                            role="tab"
                                                                                            data-toggle="tab">Documents</a>
                                                </li>
                                                <li id="inspectionEditCheckList" class="complete" role="presentation" style="display: none">
                                                    <a  onclick="checkInspectionCheckListTab()"
                                                        aria-controls="tabInspectionCheckList" role="tab"
                                                        data-toggle="tab">Checklist</a></li>
                                                <li id="report" class="${reportClassTop}" role="presentation"><a
                                                        id="reportClink" href="#tabInspectionReport"
                                                        aria-controls="tabProcessing" role="tab"
                                                        data-toggle="tab">Inspection Report</a></li>
                                                <li id="process" onclick="changePeriod()" class="${processClassTop}" role="presentation"><a href="#tabProcessing"
                                                                                                                                            aria-controls="tabProcessing"
                                                                                                                                            role="tab"
                                                                                                                                            data-toggle="tab">Processing</a>
                                                </li>

                                            </ul>
                                            <div class="tab-nav-mobile visible-xs visible-sm">
                                                <div class="swiper-wrapper" role="tablist">
                                                    <div class="swiper-slide"><a href="#tabInfo" aria-controls="tabInfo"
                                                                                 role="tab" data-toggle="tab">Info</a>
                                                    </div>
                                                    <div class="swiper-slide"><a href="#tabDocuments"
                                                                                 aria-controls="tabDocuments" role="tab"
                                                                                 data-toggle="tab">Documents</a></div>
                                                    <div class="swiper-slide"><a href="#tabInspectionReport" id="doReport"
                                                                                 aria-controls="tabInspectionReport"
                                                                                 role="tab" data-toggle="tab">Inspection
                                                        Report</a></div>
                                                    <div class="swiper-slide"><a href="#tabProcessing" id="doProcess"
                                                                                 aria-controls="tabProcessing"
                                                                                 role="tab"
                                                                                 data-toggle="tab">Processing</a></div>
                                                </div>
                                                <div class="swiper-button-prev"></div>
                                                <div class="swiper-button-next"></div>
                                            </div>
                                            <div class="tab-content">
                                                <div class="${infoClassBelow}" id="tabInfo" role="tabpanel">
                                                    <%@ include file="../hcsaLicence/applicationInfo.jsp" %>
                                                </div>
                                                <div class="tab-pane" id="tabDocuments" role="tabpanel">
                                                    <%@include
                                                            file="/WEB-INF/jsp/iais/inspectionncList/tabDocuments.jsp" %>
                                                </div>
                                                <div class="tab-pane" id="tabInspectionCheckList" role="tabpanel">

                                                </div>
                                                <div class="${reportClassBelow}" id="tabInspectionReport" role="tabpanel">
                                                    <jsp:include page="/WEB-INF/jsp/iais/report/inspectorReport.jsp"/>
                                                    <div class="row">
                                                        <div class="col-xs-12">
                                                            <a style="float:left;padding-top: 1.1%;" class="back" href="/main-web/eservice/INTRANET/MohHcsaBeDashboard?dashProcessBack=1"><em class="fa fa-angle-left"></em> Back</a>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="${processClassBelow}" id="tabProcessing" role="tabpanel">
                                                    <div class="col-xs-12">
                                                        <div class="table-gp">
                                                            <div class="alert alert-info" role="alert">
                                                                <strong>
                                                                    <h4 style="border-bottom: none">Processing Status
                                                                        Update</h4>
                                                                </strong>
                                                            </div>
                                                            <div class="table-gp">
                                                                <iais:section title="">
                                                                    <iais:row>
                                                                        <iais:field value="Current Status"/>
                                                                        <iais:value width="6">
                                                                            <p><iais:code
                                                                                    code="${insRepDto.currentStatus}"/></p>
                                                                        </iais:value>
                                                                    </iais:row>

                                                                    <iais:row>
                                                                        <label class="col-md-4 control-label">Internal Remarks <span style="color: red" id="internalRemarkStar"> *</span></label>
                                                                        <iais:value width="6">
                                                                            <textarea style="resize:none"
                                                                                      name="processRemarks" cols="65"
                                                                                      rows="6" title="content"
                                                                                      class="internalRemarks"
                                                                                      MAXLENGTH="300"><c:out
                                                                                    value="${appPremisesRecommendationDto.processRemarks}"/></textarea>
                                                                            <br/><span id="error_internalRemarks1" class="error-msg" style="display: none;"><iais:message key="GENERAL_ERR0006"/></span>
                                                                        </iais:value>
                                                                    </iais:row>
                                                                    <iais:row>
                                                                        <iais:field value="Processing Decision"
                                                                                    required="true"/>
                                                                        <iais:value width="6">
                                                                            <iais:select id="processSubmit"
                                                                                         name="processingDecision"
                                                                                         options="processingDe"
                                                                                         cssClass="nice-select nextStage"
                                                                                         firstOption="Please Select"
                                                                                         value="${appPremisesRecommendationDto.processingDecision}"/>
                                                                            <span id="error_submit" class="error-msg"
                                                                                  style="display: none;"><iais:message key="GENERAL_ERR0006"/></span>
                                                                        </iais:value>
                                                                    </iais:row>
                                                                    <c:if test = "${applicationViewDto.applicationDto.status eq 'APST037' || applicationViewDto.applicationDto.status eq 'APST020'}">
                                                                        <iais:row id="ao1SelectRow">
                                                                            <iais:field value="Select Approving Officer" required="false"/>
                                                                            <iais:value width="7" id = "showAoDiv">
                                                                                <iais:select name="aoSelect" firstOption="By System" value="${aoSelectVal}"/>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </c:if>
                                                                    <div id="laterallySelectRow">
                                                                        <c:set var="roleId" value="${taskDto.roleId}"/>
                                                                        <%@include file="../hcsaLicence/laterallySelect.jsp" %>
                                                                    </div>
                                                                    <jsp:include page="/WEB-INF/jsp/iais/inspectionPreTask/rollBackPart.jsp"/>
                                                                    <div id="comments" class="hidden">
                                                                        <%String commentsValue = request.getParameter("comments");%>
                                                                        <iais:row>
                                                                            <iais:field value="Comments to applicant" required="false"  width="12"/>
                                                                            <iais:value width="10">
                                                                                <div class="input-group">
                                                                                    <div class="ax_default text_area">
                                                                                <textarea name="comments" cols="70"
                                                                                          rows="7" maxlength="300"><%=commentsValue == null ? "" : commentsValue%></textarea>
                                                                                    </div>
                                                                                </div>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <c:if test="${applicationViewDto.applicationDto.applicationType=='APTY002'}">
                                                                        <iais:row>
                                                                            <iais:field value="Licence Start Date"
                                                                                        required="false"/>
                                                                            <iais:value width="10">
                                                                                <c:if test="${not empty applicationViewDto.recomLiceStartDate}">
                                                                                    <p><fmt:formatDate
                                                                                            value='${applicationViewDto.recomLiceStartDate}'
                                                                                            pattern='dd/MM/yyyy'/></p>
                                                                                </c:if>
                                                                                <c:if test="${empty applicationViewDto.recomLiceStartDate}">
                                                                                    <p>-</p>
                                                                                </c:if>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </c:if>
                                                                    <c:if test="${appType!='APTY007'&&appType!='APTY009'}">
                                                                        <iais:row>
                                                                            <iais:field value="Recommendation"
                                                                                        required="false"/>
                                                                            <iais:value width="10">
                                                                                <p id="periodValue"></p>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </c:if>
                                                                    <div class="fastTrack">
                                                                        <iais:row>
                                                                            <iais:field value="Fast Tracking?"
                                                                                        required="false"/>
                                                                            <iais:value width="10">
                                                                                <p>
                                                                                    <c:choose>
                                                                                        <c:when test="${applicationViewDto.applicationDto.status=='APST019'}">
                                                                                            <input class="form-check-input"
                                                                                                   id="fastTracking"
                                                                                            <c:if test="${applicationViewDto.applicationDto.fastTracking}">
                                                                                                   checked disabled
                                                                                            </c:if>
                                                                                                   type="checkbox"
                                                                                                   name="fastTracking"
                                                                                                   aria-invalid="false"
                                                                                                   value="Y">
                                                                                            <label class="form-check-label"
                                                                                                   for="fastTracking"><span
                                                                                                    class="check-square"></span></label>
                                                                                        </c:when>
                                                                                        <c:otherwise>
                                                                                            <input class="form-check-input"
                                                                                                   disabled
                                                                                            <c:if test="${applicationViewDto.applicationDto.fastTracking}">
                                                                                                   checked
                                                                                            </c:if>
                                                                                                   id="fastTracking"
                                                                                                   type="checkbox"
                                                                                                   name="fastTracking"
                                                                                                   aria-invalid="false"
                                                                                                   value="Y">
                                                                                            <label class="form-check-label"
                                                                                                   for="fastTracking"><span
                                                                                                    class="check-square"></span></label>
                                                                                        </c:otherwise>
                                                                                    </c:choose>
                                                                                </p>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div id="rfiSelect">
                                                                        <iais:row>
                                                                            <iais:field value="Sections Allowed for Change"
                                                                                        required="false"/>
                                                                            <iais:value width="10">
                                                                                <p id="selectDetail"></p>
                                                                                <input type="hidden" id="rfiSelectValue" name="rfiSelectValue" value="" />
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                </iais:section>
                                                                <iais:action style="text-align:right;">
                                                                    <a style="float:left;padding-top: 1.1%;" class="back" href="/main-web/eservice/INTRANET/MohHcsaBeDashboard?dashProcessBack=1"><em class="fa fa-angle-left"></em> Back</a>
                                                                    <a id="submitButton" class="btn btn-primary"
                                                                       onclick="insSubmit()" name="submitBtn">SUBMIT</a>
                                                                </iais:action>
                                                            </div>
                                                            <%@include file="/WEB-INF/jsp/iais/inspectionncList/processHistory.jsp"%>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                </div>
                            </iais:body>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
    <%@ include file="../inspectionncList/uploadFile.jsp" %>
</div>

<script>
    $(document).ready(function () {
        $('#rfiSelect').hide();
        if(${isRouteBackStatus}){
            $('#inspectionEditCheckList').css('display', 'block');
        }
        if("processing"=='${crud_action_type}'){
            $('#info').removeClass("active");
            $('#doProcess').click();
            $('#process').addClass("active");
        }else if("editInspectorReport" == '${crud_action_type}'){
            $('#info').removeClass("active");
            $('#report').addClass("active");
            $('#doReport').click();
        }
        changeAoSelect();
        showRollBackTo("rollBack");
    });

    $("#processSubmit").change(function () {
        changeAoSelect();
        showRollBackTo("rollBack");
    })

    function changeAoSelect() {
        var fv = $('#processSubmit option:selected').val();
        if (fv == 'submit') {
            showWaiting();
            var data = {
                'verified':'REDECI003'
            };
            $.ajax({
                'url':'${pageContext.request.contextPath}/check-ao',
                'dataType':'json',
                'data':data,
                'type':'POST',
                'success':function (data) {
                    $("#laterallySelectRow").hide();
                    if('<%=AppConsts.AJAX_RES_CODE_SUCCESS%>' == data.resCode){
                        $("#error_aoSelect").html('');
                        $("#showAoDiv").html(data.resultJson + '');
                        $("#aoSelect").val('${aoSelectVal}');
                        $("#aoSelect").niceSelect();
                        $("#ao1SelectRow").show();
                    }else if('<%=AppConsts.AJAX_RES_CODE_VALIDATE_ERROR%>' == data.resCode){
                        $("#error_aoSelect").html(data.resultJson + '');
                        $("#ao1SelectRow").hide();
                    }else if('<%=AppConsts.AJAX_RES_CODE_ERROR%>' == data.resCode){
                        $("#error_aoSelect").html('');
                        $("#ao1SelectRow").hide();
                    }
                    // setValue();
                    dismissWaiting();
                },
                'error':function () {
                    dismissWaiting();
                }
            });
        } else if (fv == 'route'){
            $("#laterallySelectRow").show();
            $("#ao1SelectRow").hide();
        }else {
            $("#ao1SelectRow").hide();
            $("#laterallySelectRow").hide();
        }
    }

    function insSubmit() {
        clearErrorMsg();
        $("#error_rollBackTo1").hide();
        const s = $("#processSubmit").val();
        if (s === "" || s == null) {
            let errorMsg = $("#errorMsgGENERAL_ERR0006").val();
            $('#error_submit').html(errorMsg);
            $("#error_submit").show();
        } else if ("submit" === s) {
            mysubmit();
        } else if ("PROCRFI" === s && rfiValidate()) {
            mysubmit();
        }
        submitRollBack(mysubmit, "rollBack");
    }

    function mysubmit(){
        $("#error_submit").hide();
        showWaiting();
        $('#crud_action_type').val('submit');
        $("#mainForm").submit();
    }

    $("[name='processingDecision']").change(function selectChange() {
        var selectValue = $("[name='processingDecision']").val();
        if (selectValue == "PROCRFI") {
            showPopupWindow('/hcsa-licence-web/eservice/INTRANET/LicenceBEViewService?rfi=rfi');
            $('#comments').removeClass('hidden');
        } else {
            $('#rfiSelect').hide();
            $('#comments').hide();
        }
    });

    function checkInspectionCheckListTab(){
        showWaiting();
        $('#crud_action_type').val('editCheckList');
        document.getElementById('mainForm').submit();
    }

    //request for information validate
    function rfiValidate(){
        //error_nextStage
        var selectValue = $("[name='processingDecision']").val();
        if (selectValue == "PROCRFI" ) {
            var rfiSelectValue = $('#rfiSelectValue').val();
            if(rfiSelectValue == null || rfiSelectValue == ''){
                let rfiCheckErrorMsg = $("#rfiCheckErrorMsg").val();
                $('#error_submit').html(rfiCheckErrorMsg);
                return false;
            }else{
                $('#error_submit').html("");
                return true;
            }
        }else{
            return true;
        }
    }
</script>

