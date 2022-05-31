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
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <input type="hidden" name="crud_action_additional" value="">
        <input type="hidden" name="readRecom" value="">
        <input type="hidden" name="appType" value="${appType}">
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
                                                <li id="report" class="${reportClassTop}" role="presentation"><a
                                                        id="reportClink" href="#tabInspectionReport"
                                                        aria-controls="tabProcessing" role="tab"
                                                        data-toggle="tab">Inspection Report</a></li>
                                                <li onclick="changePeriod()" class="complete" role="presentation"><a href="#tabProcessing"
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
                                                    <div class="swiper-slide"><a href="#tabInspectionReport"
                                                                                 aria-controls="tabInspectionReport"
                                                                                 role="tab" data-toggle="tab">Inspection
                                                        Report</a></div>
                                                    <div class="swiper-slide"><a href="#tabProcessing"
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
                                                <div class="${reportClassBelow}" id="tabInspectionReport" role="tabpanel">
                                                    <jsp:include
                                                            page="/WEB-INF/jsp/iais/report/inspectorReport.jsp"></jsp:include>
                                                </div>
                                                <div class="tab-pane" id="tabProcessing" role="tabpanel">
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
                                                                        <iais:field value="Internal Remarks"/>
                                                                        <iais:value width="6">
                                                                        <textarea style="resize:none"
                                                                                  name="processRemarks" cols="65"
                                                                                  rows="6" title="content"
                                                                                  MAXLENGTH="300"><c:out
                                                                                value="${appPremisesRecommendationDto.processRemarks}"/></textarea>
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
                                                                                  style="display: none;"><iais:message key="GENERAL_ERR0006"></iais:message></span>
                                                                        </iais:value>
                                                                    </iais:row>
                                                                  <c:if test = "${applicationViewDto.applicationDto.status eq 'APST037'}">
                                                                    <iais:row id="ao1SelectRow">
                                                                        <iais:field value="Officer" required="false"/>
                                                                        <iais:value width="7" id = "showAoDiv">
                                                                            <iais:select name="aoSelect" firstOption="By System" value="${aoSelectVal}"/>
                                                                        </iais:value>
                                                                    </iais:row>
                                                                  </c:if>
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
                                                                </iais:section>
                                                                <iais:action style="text-align:right;">
                                                                    <a style="float:left;padding-top: 1.1%;" class="back" href="/main-web/eservice/INTRANET/MohHcsaBeDashboard?dashProcessBack=1"><em class="fa fa-angle-left"></em> Back</a>
                                                                    <a id="submitButton" class="btn btn-primary"
                                                                       onclick="insSubmit()" name="submitBtn">SUBMIT</a>
                                                                </iais:action>
                                                            </div>
                                                            <br/>
                                                            <div class="alert alert-info" role="alert">
                                                                <strong>
                                                                    <h4>Processing History</h4>
                                                                </strong>
                                                            </div>
                                                            <div class="row">
                                                                <div class="col-xs-12">
                                                                    <div class="table-gp">
                                                                        <table aria-describedby="" class="table">
                                                                            <thead>
                                                                            <tr>
                                                                                <th scope="col" >Username</th>
                                                                                <th scope="col" >Working Group</th>
                                                                                <th scope="col" >Status Update</th>
                                                                                <th scope="col" >Remarks</th>
                                                                                <th scope="col" >Last Updated</th>
                                                                            </tr>
                                                                            </thead>
                                                                            <tbody>
                                                                            <c:forEach
                                                                                    items="${applicationViewDto.appPremisesRoutingHistoryDtoList}"
                                                                                    var="appPremisesRoutingHistoryDto">
                                                                                <tr>
                                                                                    <td>
                                                                                        <p><c:out
                                                                                                value="${appPremisesRoutingHistoryDto.actionby}"></c:out></p>
                                                                                    </td>
                                                                                    <td>
                                                                                        <p><c:out
                                                                                                value="${appPremisesRoutingHistoryDto.workingGroup}"></c:out></p>
                                                                                    </td>
                                                                                    <td>
                                                                                        <p><c:out
                                                                                                value="${appPremisesRoutingHistoryDto.processDecision}"></c:out></p>
                                                                                    </td>
                                                                                    <td>
                                                                                        <p><c:out
                                                                                                value="${appPremisesRoutingHistoryDto.internalRemarks}"></c:out></p>
                                                                                    </td>
                                                                                    <td>
                                                                                        <p><c:out
                                                                                                value="${appPremisesRoutingHistoryDto.updatedDt}"></c:out></p>
                                                                                    </td>
                                                                                </tr>
                                                                            </c:forEach>
                                                                            </tbody>
                                                                        </table>
                                                                    </div>
                                                                </div>
                                                            </div>
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
<c:if test = "${applicationViewDto.applicationDto.status eq 'APST037'}">
    $(document).ready(function () {
        changeAoSelect();
        $("#processSubmit").change(function () {
            changeAoSelect();
        })
    });

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
                },
                'error':function () {

                }
            });
            dismissWaiting();
        } else {
            $("#ao1SelectRow").hide();
        }
    }

</c:if>
    function insSubmit() {
        const s = $("#processSubmit").val();
        if (s == "" || s == null) {
            $("#error_submit").show();
        } else if ("submit" == s) {
            showWaiting();
            $("#mainForm").submit();
            $("#error_submit").hide();
        }
    }

</script>

