<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<div class="main-content" style="padding-top: 1%">
    <div class="container">
        <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>

            <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
            <input type="hidden" name="iaisErrorFlag" id="iaisErrorFlag"/>
            <input type="hidden" name="crud_action_additional" id="crud_action_additional"/>
            <input type="hidden" name="interalFileId" id="interalFileId"/>
            <input type="hidden" name="dateTimeShow" value="${recomInDateOnlyShow}"/>
            <input type="hidden" name="recommendationShow" value="${recommendationOnlyShow}"/>
            <input type="hidden" id="isOtherAppealType" value="${isOtherAppealType}"/>
            <input type="hidden" id="isChangePeriodAppealType" value="${isChangePeriodAppealType}"/>
            <input type="hidden" id="isLateFeeAppealType" value="${isLateFeeAppealType}"/>
            <input type="hidden" id="appealRecommendationOtherOnlyShow" value="${appealRecommendationOtherOnlyShow}"/>
            <input type="hidden" id="returnFeeOnlyShow" value="${returnFeeOnlyShow}"/>
            <c:set var="isAoRouteBackStatus" value="${applicationViewDto.applicationDto.status == 'APST062' || applicationViewDto.applicationDto.status == 'APST065' || applicationViewDto.applicationDto.status == 'APST066' || applicationViewDto.applicationDto.status == 'APST067'}"/>
            <c:set var="isPsoRouteBackStatus" value="${applicationViewDto.applicationDto.status == 'APST063'}"/>
            <c:set var="isInspectorRouteBackStatus" value="${applicationViewDto.applicationDto.status == 'APST064'}"/>
            <c:set var="isRouteBackStatus" value="${isInspectorRouteBackStatus || isAoRouteBackStatus || isPsoRouteBackStatus}"/>
            <c:set var="isBroadcastStatus" value="${applicationViewDto.applicationDto.status == 'APST013'}"/>
            <c:set var="isBroacastAsoPso" value="${broadcastAsoPso}"/>
            <c:set var="isBroacastAso" value="${broadcastAso}"/>
            <c:set var="isAppealType" value="${applicationViewDto.applicationDto.applicationType == 'APTY001'}"/>
            <c:set var="isAso" value="${taskDto.taskKey == '12848A70-820B-EA11-BE7D-000C29F371DC'}"/>
            <c:set var="isPso" value="${taskDto.taskKey == '13848A70-820B-EA11-BE7D-000C29F371DC'}"/>
            <input type="hidden" id="isAppealType" value="${isAppealType}"/>
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp dashboard-tab">
                        <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                            <li class="active" id="info" role="presentation"><a href="#tabInfo" aria-controls="tabInfo" role="tab"
                                                                      data-toggle="tab">Info</a></li>
                            <li class="complete" id="document" role="presentation"><a href="#tabDocuments"
                                                                        aria-controls="tabDocuments" role="tab"
                                                                        data-toggle="tab">Documents</a></li>
                            <li id="ApplicationViewInspection" class="complete" role="presentation"
                                style="display: block"><a href="#tabInspection"
                                                          aria-controls="tabInspection" role="tab"
                                                          data-toggle="tab">Inspection</a></li>
                            <li class="incomplete" id="process" role="presentation"><a href="#tabProcessing"
                                                                          aria-controls="tabProcessing" role="tab"
                                                                          data-toggle="tab">Processing</a></li>
                        </ul>
                        <div class="tab-content">
                        <%--info page--%>
                            <div class="tab-pane active" id="tabInfo" role="tabpanel">
                                <%@include file="applicationInfo.jsp" %>
                            </div>
                            <div class="tab-pane" id="tabDocuments" role="tabpanel">
                                <%@include file="/WEB-INF/jsp/iais/inspectionncList/tabDocuments.jsp"%>
                            </div>
                            <%--         Inspection start                       --%>
                            <div class="tab-pane" id="tabInspection" role="tabpanel">
                                <%@include file="applicationInspection.jsp" %>
                            </div>
                            <%--         Inspection end                       --%>
                            <div class="tab-pane" id="tabProcessing" role="tabpanel">
                                <span id="error_document" name="iaisErrorMsg" class="error-msg"></span>
                                <br/><br/>
                                <div class="alert alert-info" role="alert">
                                    <strong>
                                        <h4>Processing Status Update</h4>
                                    </strong>
                                </div>
                                <form method="post" action=<%=process.runtime.continueURL()%>>
                                    <input type="hidden" name="sopEngineTabRef"
                                           value="<%=process.rtStatus.getTabRef()%>">
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <iais:section title="">
                                                    <div>
                                                        <iais:row>
                                                            <iais:field value="Current Status" required="false"/>
                                                            <iais:value width="10"><p>${applicationViewDto.currentStatus}</p></iais:value>
                                                        </iais:row>
                                                    </div>
                                                    <%--<div>--%>
                                                    <div>
                                                        <iais:row>
                                                            <iais:field value="Internal Remarks" required="false"  width="12"/>
                                                            <iais:value width="10">
                                                                <div class="input-group">
                                                                    <div class="ax_default text_area">
                                                                            <textarea id="internalRemarksId"
                                                                                      name="internalRemarks" cols="70"
                                                                                      rows="7" maxlength="300">${internalRemarks}</textarea>
                                                                        <span id="error_internalRemarks" name="iaisErrorMsg" class="error-msg"></span>
                                                                    </div>
                                                                </div>
                                                            </iais:value>
                                                        </iais:row>
                                                    </div>
                                                    <%--</div>--%>
                                                    <div id="processingDecision">
                                                        <iais:row>
                                                            <iais:field value="Processing Decision" required="true"/>
                                                            <%String nextStage = request.getParameter("nextStage");%>
                                                            <iais:value width="10">
                                                                <iais:select cssClass="nextStage" name="nextStage" id="nextStage"
                                                                             options="nextStages"
                                                                             value="<%=nextStage%>"></iais:select>
                                                            </iais:value>
                                                        </iais:row>
                                                    </div>
                                                    <c:if test="${isRouteBackStatus || applicationViewDto.applicationDto.status == 'APST014' || applicationViewDto.applicationDto.status == 'APST013'}">
                                                        <div id="replytr" class="hidden">
                                                            <iais:row>
                                                                <iais:field value="Processing Decision" required="true"/>
                                                                <%--                                                                <%String selectNextStageReply = request.getParameter("selectNextStageReply");%>--%>
                                                                <iais:value width="10">
                                                                    <iais:select cssClass="nextStageReplys" name="nextStageReplys" id="nextStageReply"
                                                                                 options="nextStageReply"
                                                                                 value="${selectNextStageReply}"></iais:select>
                                                                </iais:value>
                                                            </iais:row>
                                                        </div>
                                                    </c:if>

                                                    <%-- DMS approval and reject --%>
                                                    <c:if test="${applicationViewDto.applicationDto.status == 'APST014'}">
                                                        <div id="decision">
                                                            <iais:row>
                                                                <iais:field value="Processing Decision" required="true"/>
                                                                <iais:value width="10">
                                                                    <iais:select cssClass="decisionValues" name="decisionValues" id="decisionValues"
                                                                                 firstOption="Please Select"
                                                                                 options="decisionValues"
                                                                                 value="${selectDecisionValue}"></iais:select>
                                                                </iais:value>
                                                            </iais:row>
                                                        </div>
                                                    </c:if>
                                                    <c:if test="${applicationViewDto.applicationDto.applicationType == 'APTY006' && applicationViewDto.applicationDto.status == 'APST007'}">
                                                        <div id="withdrawalDecision">
                                                            <iais:row>
                                                                <iais:field value="Processing Decision" required="true"/>
                                                                <iais:value width="10">
                                                                    <iais:select cssClass="withdrawalDecisionValues" name="withdrawalDecisionValues" id="withdrawalDecisionValues"
                                                                                 firstOption="Please Select"
                                                                                 options="decisionValues"
                                                                                 value="${selectDecisionValue}"></iais:select>
                                                                </iais:value>
                                                            </iais:row>
                                                        </div>
                                                    </c:if>
                                                    <div id="rollBackDropdown" class="hidden">
                                                        <iais:row>
                                                            <iais:field value="Route Back To" required="true"/>
                                                            <iais:value width="10">
                                                                <select name="rollBack" class="nice-select input-large rollBack">
                                                                    <option value="">Please Select</option>
                                                                    <c:forEach items="${applicationViewDto.rollBack}"
                                                                               var="rollBack">
                                                                        <option value="<iais:mask name="rollBack" value="${rollBack.value}"/>" <c:if test="${rollBack.value == selectRollBack}">selected</c:if>>${rollBack.key}</option>
                                                                    </c:forEach>
                                                                </select>
                                                                <span id="error_rollBack" name="iaisErrorMsg" class="error-msg"></span>
                                                            </iais:value>
                                                        </iais:row>
                                                    </div>
                                                    <div id="verifiedDropdown" class="hidden">
                                                        <iais:row>
                                                            <iais:field value="Verified" required="false"/>
                                                            <iais:value width="10">
                                                                <iais:select cssClass="verified" name="verified"
                                                                             options="verifiedValues"
                                                                             value="${selectVerified}"></iais:select>
                                                                <span id="error_verified" name="iaisErrorMsg" class="error-msg"></span>
                                                            </iais:value>
                                                        </iais:row>
                                                    </div>

                                                    <div id="comments" class="hidden">
                                                        <%String commentsValue = request.getParameter("comments");%>
                                                        <iais:row>
                                                            <iais:field value="Comments" required="false"  width="12"/>
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
                                                    <%--application type == new application --%>
                                                    <c:if test="${applicationViewDto.applicationDto.applicationType == 'APTY002'}">
                                                        <div id="licenceStartDate">
                                                            <iais:row>
                                                                <iais:field value="Licence Start Date" required="false"/>
                                                                <iais:value width="10">
                                                                    <c:choose>
                                                                        <c:when test="${applicationViewDto.applicationDto.status=='APST007' || (isRouteBackStatus && taskDto.taskKey == '12848A70-820B-EA11-BE7D-000C29F371DC') || isBroacastAso}">
                                                                            <iais:datePicker id="licenceStartDate" name="tuc"
                                                                                             value="${date}"></iais:datePicker>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <p>${(recomInDateOnlyShow == "" || recomInDateOnlyShow == null) ? "-" : recomInDateOnlyShow}</p>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </iais:value>
                                                            </iais:row>
                                                        </div>
                                                    </c:if>

                                                    <%--application type == appeal --%>
                                                    <c:if test="${isAppealType}">
                                                        <div id="appealRecommendation">
                                                            <iais:row>
                                                                <iais:field value="Recommendation" required="true"/>
                                                                <iais:value width="10">
                                                                    <c:choose>
                                                                        <c:when test="${isAso || isPso}">
                                                                                <iais:select cssClass="appealRecommendationValues" name="appealRecommendationValues" id="appealRecommendationValues"
                                                                                             firstOption="Please Select"
                                                                                             options="appealRecommendationValues"
                                                                                             value="${selectAppealRecommendationValue}"></iais:select>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <p>${(appealRecommendationValueOnlyShow == "" || appealRecommendationValueOnlyShow == null) ? "-" : appealRecommendationValueOnlyShow}</p>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </iais:value>
                                                            </iais:row>
                                                        </div>

                                                        <%-- Appeal against late application renewal fee --%>
                                                        <div id="appealReturnFee" class="hidden">
                                                            <iais:row>
                                                                <iais:field value="Amount to be returned" required="true"/>
                                                                <iais:value width="10">
                                                                    <c:choose>
                                                                        <c:when test="${isAso || isPso}">
                                                                            <input id="returnFee" type="text" name="returnFee" maxlength="6" value="${returnFee}" onkeypress="if(!this.value.match(/^[\+\-]?\d*?\.?\d*?$/))this.value=this.t_value;else this.t_value=this.value;if(this.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?)?$/))this.o_value=this.value" onkeyup="if(!this.value.match(/^[\+\-]?\d*?\.?\d*?$/))this.value=this.t_value;else this.t_value=this.value;if(this.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?)?$/))this.o_value=this.value" onblur="if(!this.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?|\.\d*?)?$/))this.value=this.o_value;else{if(this.value.match(/^\.\d+$/))this.value=0+this.value;if(this.value.match(/^\.$/))this.value=0;this.o_value=this.value}">
                                                                            <span id="error_returnFee" name="iaisErrorMsg" class="error-msg"></span>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <p>${returnFeeOnlyShow}</p>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </iais:value>
                                                            </iais:row>
                                                        </div>
                                                    </c:if>

                                                    <div id="recommendationDropdown">
                                                        <iais:row>
                                                            <div id="recommendationFieldTrue" class="hidden"><iais:field value="${recommendationShowName}" required="true"/></div>
                                                            <div id="recommendationFieldFalse"><iais:field value="${recommendationShowName}" required="false"/></div>
                                                            <iais:value width="10">
                                                                <c:choose>
                                                                    <c:when test="${applicationViewDto.applicationDto.status=='APST007' || applicationViewDto.applicationDto.status=='APST012' || applicationViewDto.applicationDto.status=='APST014' || (isRouteBackStatus && isAso) || (isRouteBackStatus && isPso) || isBroacastAsoPso}">
                                                                        <iais:select cssClass="recommendation" name="recommendation"
                                                                                     options="recommendationDropdown"
                                                                                     firstOption="Please Select"
                                                                                     value="${recommendationStr}"></iais:select>
                                                                        <span id="error_recommendation" name="iaisErrorMsg" class="error-msg"></span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <p>${(recommendationOnlyShow == "" || recommendationOnlyShow == null) ? "-" : recommendationOnlyShow}</p>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </iais:value>
                                                        </iais:row>
                                                    </div>
                                                    <div id="recommendationOtherDropdown">
                                                        <iais:row>
                                                            <iais:field value="${isAppealType ? 'Recommended Licence Period' : 'Other Period'}" required="true"/>
                                                            <iais:value width="10">
                                                                <c:choose>
                                                                    <c:when test="${!isAppealType || (isAppealType && (isAso || isPso))}">
                                                                        <input onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" id=recomInNumber type="text" name="number" maxlength="2" value="${otherNumber}">
                                                                        <span id="error_recomInNumber" name="iaisErrorMsg" class="error-msg"></span>
                                                                        <iais:select cssClass="chrono" id="chronoUnit" name="chrono" options="recommendationOtherDropdown" value="${otherChrono}"/>
                                                                        <span id="error_chronoUnit" name="iaisErrorMsg" class="error-msg"></span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <p>${appealRecommendationOtherOnlyShow}</p>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </iais:value>
                                                        </iais:row>
                                                    </div>

                                                    <%--application type != appeal --%>
                                                    <c:if test="${!isAppealType}">
                                                        <div class="fastTrack">
                                                            <iais:row>
                                                                <iais:field value="Fast Tracking?" required="false"/>
                                                                <iais:value width="10">
                                                                    <p>
                                                                        <c:choose>
                                                                            <c:when test="${applicationViewDto.applicationDto.status=='APST007' || applicationViewDto.applicationDto.status=='APST012' || (isRouteBackStatus && taskDto.taskKey == '12848A70-820B-EA11-BE7D-000C29F371DC') || (isRouteBackStatus && taskDto.taskKey == '13848A70-820B-EA11-BE7D-000C29F371DC')}">
                                                                                <input class="form-check-input" id="fastTracking"
                                                                                <c:if test="${applicationViewDto.applicationDto.fastTracking}">
                                                                                       checked disabled
                                                                                </c:if>
                                                                                       type="checkbox" name="fastTracking" aria-invalid="false" value="Y">
                                                                                <label class="form-check-label" for="fastTracking"><span class="check-square"></span></label>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <input class="form-check-input" disabled
                                                                                <c:if test="${applicationViewDto.applicationDto.fastTracking}">
                                                                                       checked
                                                                                </c:if>
                                                                                       id="fastTracking" type="checkbox" name="fastTracking" aria-invalid="false" value="Y">
                                                                                <label class="form-check-label" for="fastTracking"><span class="check-square"></span></label>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </p>
                                                                </iais:value>
                                                            </iais:row>
                                                        </div>
                                                    </c:if>

                                                    <div id="rfiSelect">
                                                        <iais:row>
                                                            <iais:field value="Sections Allowed for Change"
                                                                        required="false"/>
                                                            <iais:value width="10">
                                                                <p id="selectDetail"></p>
                                                            </iais:value>
                                                        </iais:row>
                                                    </div>
                                                    <%--</table>--%>
                                                </iais:section>
                                                <div align="right">
                                                    <button id="submitButton" type="button" class="btn btn-primary">
                                                        Submit
                                                    </button>
                                                </div>
                                                <div>&nbsp</div>
                                            </div>
                                        </div>
                                    </div>
                                </form>
                                <%@include file="/WEB-INF/jsp/iais/inspectionncList/processHistory.jsp"%>
                            </div>
                        </div>
                        <div class="tab-nav-mobile visible-xs visible-sm">
                            <div class="swiper-wrapper" role="tablist">
                                <div class="swiper-slide"><a href="#tabInfo" aria-controls="tabInfo" role="tab"
                                                             data-toggle="tab">Info</a></div>
                                <div class="swiper-slide"><a href="#tabDocuments" id="doDocument" aria-controls="tabDocuments"
                                                             role="tab" data-toggle="tab">Documents</a></div>

                                <div class="swiper-slide"><a href="#tabInspection" aria-controls="tabInspection"
                                                             role="tab" data-toggle="tab">Inspection</a></div>
                                <div class="swiper-slide"><a href="#tabProcessing" id="doProcess" aria-controls="tabProcessing"
                                                             role="tab" data-toggle="tab">Processing</a></div>
                            </div>
                            <div class="swiper-button-prev"></div>
                            <div class="swiper-button-next"></div>
                        </div>
                    </div>
                </div>
            </div>
        </form>
        <%@include file="/WEB-INF/jsp/include/validation.jsp" %>
        <%@include file="/WEB-INF/jsp/iais/inspectionncList/uploadFile.jsp" %>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        $("input[name='fastTracking']").each(function(){
            if('${applicationViewDto.applicationDto.fastTracking}' == 'true'){
                $(this).prop("checked",true);
            }
        });
        if ('${taskDto.taskKey}' == '12848A70-820B-EA11-BE7D-000C29F371DC' || '${taskDto.taskKey}' == '13848A70-820B-EA11-BE7D-000C29F371DC') {
            // $('#ApplicationViewInspection').css('display', 'none');
            // $('#recommendationDropdown').removeClass('hidden');
        }
        if ('${applicationViewDto.applicationDto.status}' == 'APST062' ||  '${applicationViewDto.applicationDto.status}' == 'APST013' ||  '${applicationViewDto.applicationDto.status}' == 'APST063' ||  '${applicationViewDto.applicationDto.status}' == 'APST064' || '${applicationViewDto.applicationDto.status}' == 'APST065' || '${applicationViewDto.applicationDto.status}' == 'APST066' || '${applicationViewDto.applicationDto.status}' == 'APST067') {
            $('#processingDecision').addClass('hidden');
            // $('#recommendationDropdown').addClass('hidden');
            $('#replytr').removeClass('hidden');
            // $('#licenceStartDate').addClass('hidden');
        }
        <%-- DMS approval and reject --%>
        if ('${applicationViewDto.applicationDto.status}' == 'APST014'){
            $('#processingDecision').addClass('hidden');
        }
        //cessation
        if ('${applicationViewDto.applicationDto.applicationType}' == 'APTY008'){
            $('#recommendationDropdown').addClass('hidden');
            $('#licenceStartDate').addClass('hidden');
        }
        //broadcast
        if('${applicationViewDto.applicationDto.status}' == 'APST013'){
            $('#recommendationFieldTrue').removeClass('hidden');
            $('#recommendationFieldFalse').addClass('hidden');
        }
        //appeal
        if (${isAppealType}){
            $('#recommendationDropdown').addClass('hidden');
            checkAppealRecommendation();

        }
        $('#rfiSelect').hide();
        check();
        validate();
        checkVerifiedField();
        //check DMS
        DMSCheck();
        checkRecommendationOtherDropdown();
        checkInspectionShow();
        //route back
        routeBackCheck();
        if('APTY006' == '${applicationViewDto.applicationDto.applicationType}' && 'APST007' == '${applicationViewDto.applicationDto.status}'){
            $('#recommendationDropdown').addClass('hidden');
            $('#replytr').removeClass('hidden');
            $('#licenceStartDate').addClass('hidden');
            $('#processingDecision').addClass('hidden');
            $('.fastTrack').addClass('hidden');
        }
        appealAoFillBack();
    });

    function checkInspectionShow(){
        if('${isShowInspection}' == 'N'){
            $('#ApplicationViewInspection').css('display', 'none');
        }
    }

    //check DMS decision value
    $("[name='decisionValues']").change(function selectChange() {
        checkRecommendationDMS();
    });

    function checkRecommendationDMS(){
        var selectValue = $("[name='decisionValues']").val();
        if(selectValue == "decisionReject"){
            $('#recommendationDropdown').addClass('hidden');
            $('#recommendationOtherDropdown').addClass('hidden');
        }else if(selectValue == "decisionApproval"){
            $('#recommendationDropdown').removeClass('hidden');
            checkRecommendationOtherDropdown();
        }
    }

    //appeal
        $("[name='appealRecommendationValues']").change(function selectChange() {
            if (${isAppealType}) {
                checkAppealRecommendation();
            }
        });


    function checkAppealRecommendation(){
        var selectValue = $("[name='appealRecommendationValues']").val();
        var isOtherAppealType = $('#isOtherAppealType').val();
        var isChangePeriodAppealType = $('#isChangePeriodAppealType').val();
        var isLateFeeAppealType = $('#isLateFeeAppealType').val();
        if(selectValue == "appealApprove"){
            if(isLateFeeAppealType == 'true'){
                $('#appealReturnFee').removeClass('hidden');
                $('#recommendationOtherDropdown').addClass('hidden');
            }else if(isChangePeriodAppealType == 'true'){
                $('#recommendationOtherDropdown').removeClass('hidden');
                $('#appealReturnFee').addClass('hidden');
            }else if(isOtherAppealType == 'true'){
                $('#appealReturnFee').addClass('hidden');
                $('#recommendationOtherDropdown').addClass('hidden');
            }
        }else{
            $('#recommendationOtherDropdown').addClass('hidden');
            $('#appealReturnFee').addClass('hidden');
        }
    }

    function appealAoFillBack(){
        var appealRecommendationOtherOnlyShow = $('#appealRecommendationOtherOnlyShow').val();
        if((appealRecommendationOtherOnlyShow != null) && (appealRecommendationOtherOnlyShow.length > 0)){
            $('#recommendationOtherDropdown').removeClass('hidden');
        }
        var returnFeeOnlyShow = $('#returnFeeOnlyShow').val();
        if((returnFeeOnlyShow != null) && (returnFeeOnlyShow.length > 0)){
            $('#appealReturnFee').removeClass('hidden');
        }
    }

    function DMSCheck(){
        if('${applicationViewDto.applicationDto.status}' == 'APST014'){
                $('#recommendationFieldTrue').removeClass('hidden');
                $('#recommendationFieldFalse').addClass('hidden');
            checkRecommendationDMS();
        }
    }


    $("#submitButton").click(function () {
        showWaiting();
        document.getElementById("mainForm").submit();
        $("#submitButton").attr("disabled", true);
    });

    function check(){
        var selectValue = $("[name='nextStage']").val();
        if (selectValue == "PROCVER") {
            $('#verifiedDropdown').removeClass('hidden');
            $('#rollBackDropdown').addClass('hidden');
            $('#comments').addClass('hidden');
        } else if (selectValue == "PROCRB") {
            $('#rollBackDropdown').removeClass('hidden');
            $('#verifiedDropdown').addClass('hidden');
            $('#comments').addClass('hidden');
        } else if (selectValue == "PROCRFI") {
            $('#verifiedDropdown').addClass('hidden');
            $('#rollBackDropdown').addClass('hidden');
            $('#comments').removeClass('hidden');
            // showPopupWindow('/hcsa-licence-web/eservice/INTRANET/LicenceBEViewService?rfi=rfi');
        } else {
            $('#rollBackDropdown').addClass('hidden');
            $('#verifiedDropdown').addClass('hidden');
            $('#comments').addClass('hidden');
        }
    }

    //recommendation
    $("[name='recommendation']").change(function selectChange() {
        var recommendation = $("[name='recommendation']").val();
        if('other' == recommendation){
            $('#recommendationOtherDropdown').removeClass('hidden');
        }else{
            $('#recommendationOtherDropdown').addClass('hidden');
        }
    });

    function checkRecommendationOtherDropdown(){
        var recommendation = $("[name='recommendation']").val();
        var appealRecommendation = $("[name='appealRecommendationValues']").val();
        var isChangePeriodAppealType = $('#isChangePeriodAppealType').val();
        if('other' == recommendation || (isChangePeriodAppealType == 'true' && 'appealApprove' == appealRecommendation)){
            $('#recommendationOtherDropdown').removeClass('hidden');
        }else{
            $('#recommendationOtherDropdown').addClass('hidden');
        }
    }



    $("[name='nextStage']").change(function selectChange() {
        var selectValue = $("[name='nextStage']").val();
        if (selectValue == "PROCVER") {
            $('#verifiedDropdown').removeClass('hidden');
            $('#rollBackDropdown').addClass('hidden');
            $('#comments').addClass('hidden');
        } else if (selectValue == "PROCRB") {
            $('#rollBackDropdown').removeClass('hidden');
            $('#verifiedDropdown').addClass('hidden');
            $('#comments').addClass('hidden');
        } else if (selectValue == "PROCRFI") {
            $('#verifiedDropdown').addClass('hidden');
            $('#rollBackDropdown').addClass('hidden');
            $('#comments').removeClass('hidden');
            showPopupWindow('/hcsa-licence-web/eservice/INTRANET/LicenceBEViewService?rfi=rfi');
        } else {
            $('#comments').addClass('hidden');
            $('#rollBackDropdown').addClass('hidden');
            $('#verifiedDropdown').addClass('hidden');

        }
    });

    function checkVerifiedField(){
        var selectValue = $("[name='verified']").val();
        //pso aso
        if('${applicationViewDto.applicationDto.status}' == 'APST012' || '${applicationViewDto.applicationDto.status}' == 'APST007'){
            if('AO1' == selectValue|| 'AO2'==selectValue || 'AO3'==selectValue){
                $('#recommendationFieldTrue').removeClass('hidden');
                $('#recommendationFieldFalse').addClass('hidden');
            }else{
                $('#recommendationFieldTrue').addClass('hidden');
                $('#recommendationFieldFalse').removeClass('hidden');
            }
        }
    }

    $("[name='verified']").change(function selectChange() {
        var selectValue = $("[name='verified']").val();
        checkVerifiedField();
        if (selectValue == "PROCRFI") {
            showPopupWindow('/hcsa-licence-web/eservice/INTRANET/LicenceBEViewService?rfi=rfi');
        } else {
            $('#rfiSelect').hide();
        }
    });

    //route back status verify
    function routeBackCheck(){
        //AO route back to
        if(${isAoRouteBackStatus}){
            $('#recommendationFieldTrue').removeClass('hidden');
            $('#recommendationFieldFalse').addClass('hidden');
        }
    }




    $('#verifiedDropdown').change(function verifiedChange() {
        //var verified= $("[name='verified']").val();
        // if(verified=="PROCLSD") {
        //     $('#licenceStartDate').removeClass('hidden');
        // }else{
        //     $('#licenceStartDate').addClass('hidden');
        // }
    });

    function showWaiting() {
        $.blockUI({
            message: '<div style="padding:3px;">We are processing your request now, please do not click the Back or Refresh buttons in the browser.</div>',
            css: {width: '25%', border: '1px solid #aaa'},
            overlayCSS: {opacity: 0.2}
        });
    }

    function dismissWaiting() {
        $.unblockUI();
    }

    function validate(){
        if("Y"=='${doProcess}'){
            $('#info').removeClass("active");
            $('#process').addClass("active");
            $('#doProcess').click();
        }else if("Y" == '${doDocument}'){
            $('#info').removeClass("active");
            $('#document').addClass("active");
            $('#doDocument').click();
        }

        if("Y"=='${uploadFileValidate}'){
            $('#uploadButton').click();
        }
    }


</script>



