<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<%
    String webroot=IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
%>
<webui:setLayout name="iais-intranet"/>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
        <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
            <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
            <input type="hidden" name="iaisErrorFlag" id="iaisErrorFlag"/>
            <input type="hidden" name="rfiCheckErrorMsg" id="rfiCheckErrorMsg" value="<iais:message key="PRF_ERR012" escape="true"></iais:message>"/>
            <input type="hidden" name="crud_action_additional" id="crud_action_additional"/>
            <input type="hidden" name="interalFileId" id="interalFileId"/>
            <input type="hidden" name="dateTimeShow" value="${recomInDateOnlyShow}"/>
            <input type="hidden" name="recommendationShow" value="${recommendationOnlyShow}"/>
            <input type="hidden" id="isOtherAppealType" value="${isOtherAppealType}"/>
            <input type="hidden" id="isChangePeriodAppealType" value="${isChangePeriodAppealType}"/>
            <input type="hidden" id="isLateFeeAppealType" value="${isLateFeeAppealType}"/>
            <input type="hidden" id="appealRecommendationOtherOnlyShow" value="${appealRecommendationOtherOnlyShow}"/>
            <input type="hidden" id="returnFeeOnlyShow" value="${returnFeeOnlyShow}"/>
            <input type="hidden" id="isRequestForChange" value="${isRequestForChange}"/>
            <c:set var="isAoRouteBackStatus" value="${applicationViewDto.applicationDto.status == 'APST062' || applicationViewDto.applicationDto.status == 'APST065' || applicationViewDto.applicationDto.status == 'APST066' || applicationViewDto.applicationDto.status == 'APST067'}"/>
            <c:set var="isPsoRouteBackStatus" value="${applicationViewDto.applicationDto.status == 'APST063'}"/>
            <c:set var="isInspectorRouteBackStatus" value="${applicationViewDto.applicationDto.status == 'APST064'}"/>
            <c:set var="isRouteBackStatus" value="${isInspectorRouteBackStatus || isAoRouteBackStatus || isPsoRouteBackStatus}"/>
            <c:set var="isBroadcastStatus" value="${applicationViewDto.applicationDto.status == 'APST013'}"/>
            <c:set var="isBroacastAsoPso" value="${broadcastAsoPso}"/>
            <c:set var="isBroacastAso" value="${broadcastAso}"/>
            <c:set var="isAppealType" value="${applicationViewDto.applicationDto.applicationType == 'APTY001'}"/>
            <c:set var="isWithDrawal" value="${applicationViewDto.applicationDto.applicationType == 'APTY006'}"/>
            <c:set var="isAso" value="${taskDto.taskKey == '12848A70-820B-EA11-BE7D-000C29F371DC'}"/>
            <c:set var="isPso" value="${taskDto.taskKey == '13848A70-820B-EA11-BE7D-000C29F371DC'}"/>
            <c:set var="isCessation" value="${applicationViewDto.applicationDto.applicationType == 'APTY008'}"/>
            <input type="hidden" id="isAppealType" value="${isAppealType}"/>
            <input type="hidden" id="isWithDrawal" value="${isWithDrawal}"/>
            <input type="hidden" id="isCessation" value="${isCessation}"/>

            <div class="main-content">
                <div class="row">
                    <div class="col-lg-12 col-xs-12">
                        <div class="center-content">
                            <div class="intranet-content">
                                <iais:body >
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
                                                                              data-toggle="tab">Inspection${!isAso && !isPso || isAoRouteBackStatus || isBroadcastStatus ? ' Report' : ''}</a></li>
                                                <li class="incomplete" id="process" role="presentation"><a href="#tabProcessing"
                                                                                                           aria-controls="tabProcessing" role="tab"
                                                                                                           data-toggle="tab">Processing</a></li>
                                            </ul>
                                            <div class="tab-nav-mobile visible-xs visible-sm">
                                                <div class="swiper-wrapper" role="tablist">
                                                    <div class="swiper-slide"><a href="#tabInfo" aria-controls="tabInfo" role="tab"
                                                                                 data-toggle="tab">Info</a></div>
                                                    <div class="swiper-slide"><a href="#tabDocuments" id="doDocument" aria-controls="tabDocuments"
                                                                                 role="tab" data-toggle="tab">Documents</a></div>
<%--                                                    <div class="swiper-slide"><a href="#tabInspection" aria-controls="tabProcessing"--%>
<%--                                                                                 role="tab" data-toggle="tab">Inspection${!isAso && !isPso || isAoRouteBackStatus || isBroadcastStatus ? ' Report' : ''}</a></div>--%>
                                                    <div class="swiper-slide"><a href="#tabProcessing" id="doProcess" aria-controls="tabProcessing"
                                                                                 role="tab" data-toggle="tab">Processing</a></div>
                                                </div>
                                                <div class="swiper-button-prev"></div>
                                                <div class="swiper-button-next"></div>
                                            </div>
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
                                                    <%@include file="/WEB-INF/jsp/iais/report/ao1Report.jsp" %>
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
                                                                                <div id="internalRemarksFalse"><iais:field value="Internal Remarks" required="false"  width="12"/></div>
                                                                                <div id="internalRemarksTrue" class="hidden"><iais:field value="Internal Remarks" required="true"  width="12"/></div>
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
                                                                        <%--Choose to inspect for 6 months--%>
                                                                        <c:if test="${(applicationViewDto.applicationDto.status == 'APST007' || applicationViewDto.applicationDto.status == 'APST012') && isChooseInspection}">
                                                                            <div id="chooseInspectionBox">
                                                                                <iais:row>
                                                                                    <label class="col-xs-0 col-md-4 control-label">
                                                                                        To use recent inspection report?<a href="javascript:void(0);" onclick="doVerifyFileGo('${AppLastInsGroup.fileReportIdForViewLastReport}')">(View Report)</a>
                                                                                    </label>
                                                                                    <iais:value width="10">
                                                                                        <p>
                                                                                            <input class="form-check-input" id="chooseInspection"
                                                                                                   type="checkbox" name="chooseInspection" aria-invalid="false" <c:if test="${chooseInspectionChecked == 'Y'}">checked</c:if> value="Y">
                                                                                            <label class="form-check-label" for="chooseInspection"><span class="check-square"></span></label>
                                                                                            <c:if test="${!empty AppLastInsGroup.fileReportIdForViewLastReport}">
                                                                                                <a hidden href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo&fileRo=<iais:mask name="fileRo"  value="${AppLastInsGroup.fileReportIdForViewLastReport}"/>&fileRepoName=${AppLastInsGroup.reportName}"
                                                                                                   title="Download" class="downloadFile"><span id="${AppLastInsGroup.fileReportIdForViewLastReport}Down">trueDown</span></a>
                                                                                            </c:if>
                                                                                        </p>
                                                                                    </iais:value>
                                                                                </iais:row>
                                                                            </div>
                                                                        </c:if>
                                                                        <div id="processingDecision">
                                                                            <iais:row>
                                                                                <iais:field value="Processing Decision" required="true"/>
                                                                                <%String nextStage = request.getParameter("nextStage");%>
                                                                                <iais:value width="10">
                                                                                    <iais:select cssClass="nextStage" name="nextStage" id="nextStage" firstOption="Please Select"
                                                                                                 options="nextStages" needSort="true"
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
                                                                        <div id="rollBackDropdown" class="hidden">
                                                                            <iais:row>
                                                                                <iais:field value="Route Back To" required="true"/>
                                                                                <iais:value width="10">
                                                                                    <iais:select cssClass="rollBack" name="rollBack" id="rollBack"
                                                                                                 firstOption="Please Select"
                                                                                                 options="routeBackValues"
                                                                                                 value="${selectRollBack}"></iais:select>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                        <c:if test="${ 'canRouteBackReview' == routeBackReview}">
                                                                            <div id="routeBackReviewBox" class="hidden">
                                                                                <iais:row>
                                                                                    <iais:field value="To review application after internal user has clarified?" required="false"/>
                                                                                    <iais:value width="10">
                                                                                        <p>
                                                                                            <input class="form-check-input" id="routeBackReview"
                                                                                                   type="checkbox" name="routeBackReview" aria-invalid="false" checked value="Y">
                                                                                            <label class="form-check-label" for="routeBackReview"><span class="check-square"></span></label>
                                                                                        </p>
                                                                                    </iais:value>
                                                                                </iais:row>
                                                                            </div>
                                                                        </c:if>
                                                                        <div id="verifiedDropdown" class="hidden">
                                                                            <iais:row>
                                                                                <iais:field value="Assign To" required="false"/>
                                                                                <iais:value width="10" id="verifyCallAjaxDropDown">
                                                                                    <iais:select cssClass="verified" name="verified" id="verified"
                                                                                                 options="verifiedValues"
                                                                                                 value="${selectVerified}"></iais:select>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                          <%@include file="aoSelect.jsp" %>
                                                                        </div>

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
                                                                        <div id="appealRecommendationDiv">
                                                                            <c:if test="${isAppealType || ((isWithDrawal || isCessation) && (!finalStage || hasRollBackHistoryList && (appealRecommendationValueOnlyShow != '' && appealRecommendationValueOnlyShow != null)))}">
                                                                            <div id="appealRecommendation">
                                                                                <iais:row>
                                                                                    <div id="appealRecommendationTrue"><iais:field value="Recommendation" required="true"/></div>
                                                                                    <div id="appealRecommendationFalse" class="hidden"><iais:field value="Recommendation" required="false"/></div>
                                                                                    <iais:value width="10">
                                                                                        <c:choose>
                                                                                            <c:when test="${isAso || isPso}">
                                                                                                <iais:select cssClass="appealRecommendationValues" name="appealRecommendationValues" id="appealRecommendationValues"
                                                                                                             firstOption="Please Select"
                                                                                                             options="appealRecommendationValues"
                                                                                                             value="${selectAppealRecommendationValue}"></iais:select>
                                                                                            </c:when>
                                                                                            <c:otherwise>
                                                                                                <p id = "appealRecommenValueShow">${(appealRecommendationValueOnlyShow == "" || appealRecommendationValueOnlyShow == null) ? "-" : appealRecommendationValueOnlyShow}</p>
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
                                                                                                <input id="returnFee" type="text" name="returnFee" maxlength="5" value="${returnFee}" onkeypress="if(!this.value.match(/^[\+\-]?\d*?\.?\d*?$/))this.value=this.t_value;else this.t_value=this.value;if(this.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?)?$/))this.o_value=this.value" onkeyup="if(!this.value.match(/^[\+\-]?\d*?\.?\d*?$/))this.value=this.t_value;else this.t_value=this.value;if(this.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?)?$/))this.o_value=this.value" onblur="if(!this.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?|\.\d*?)?$/))this.value=this.o_value;else{if(this.value.match(/^\.\d+$/))this.value=0+this.value;if(this.value.match(/^\.$/))this.value=0;this.o_value=this.value}">
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
                                                                        </div>

                                                                        <div id="normalRecommendationDiv">
                                                                            <div id="recommendationDropdown" ${applicationViewDto.applicationDto.applicationType == 'APTY007' ? 'hidden' : ''}>
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
                                                                        </div>

                                                                        <div id="recommendationOtherDropdown">
                                                                            <iais:row>
                                                                                <iais:field value="${isAppealType ? 'Recommended Licence Period' : 'Other Period'}" required="true"/>
                                                                                <iais:value width="10">
                                                                                    <c:choose>
                                                                                        <c:when test="${!isAppealType || (isAppealType && (isAso || isPso))}">
                                                                                            <input onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" id=recomInNumber type="text" name="number" maxlength="${isAppealType ? 1 : 2}" value="${otherNumber}">
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
                                                                        <%@include file="/WEB-INF/jsp/iais/hcsaLicence/appFlowSvcVehicleShow.jsp"%>
                                                                        <%--application type != appeal --%>
                                                                        <c:if test="${!isAppealType && !isCessation && !isWithDrawal}">
                                                                            <div class="fastTrack">
                                                                                <iais:row>
                                                                                    <iais:field value="Fast Tracking?" required="false"/>
                                                                                    <iais:value width="10">
                                                                                        <p>
                                                                                            <c:choose>
                                                                                                <c:when test="${applicationViewDto.applicationDto.status=='APST007' || applicationViewDto.applicationDto.status=='APST012'}">
                                                                                                    <input class="" id="fastTracking"
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
                                                                                                           ao2staff03                                                                                       id="fastTracking" type="checkbox" name="fastTracking" aria-invalid="false" value="Y">
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
                                                                                    <input type="hidden" id="rfiSelectValue" name="rfiSelectValue" value="" />
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                    <c:if test="${applicationViewDto.showTcu && applicationViewDto.editTcu}">
                                                                        <div class="form-group">
                                                                            <label class="col-xs-12 col-md-4 control-label">TCU</label>
                                                                            <div class="col-xs-8 col-sm-6 col-md-5">
                                                                                <p><input type="checkbox" id="tcuType"  value="tcuType"  <c:if test="${applicationViewDto.tcuFlag}">checked</c:if>  name="tcuType" onclick="javascript: showTcuLabel(this);">
                                                                                    <label class="form-check-label" for="tcuType" ><span class="check-square"></span></label>
                                                                                </p>
                                                                            </div>
                                                                        </div>

                                                                        <div class="form-group" id="tcuLabel" >
                                                                            <label class="col-xs-12 col-md-4 control-label">TCU Date<span style="color: red"> *</span></label>
                                                                            <div class="col-xs-8 col-sm-6 col-md-5">
                                                                                <iais:datePicker id = "tucDate" name = "tucDate" value="${applicationViewDto.tuc}"></iais:datePicker>
                                                                                <span class="error-msg" id="error_tcuDate" name="iaisErrorMsg"></span>
                                                                            </div>
                                                                        </div>
                                                                    </c:if>
                                                                        <c:if test="${applicationViewDto.showTcu && !applicationViewDto.editTcu}">
                                                                            <div class="form-group">
                                                                                <label class="col-xs-12 col-md-4 control-label">TCU</label>
                                                                                <div class="col-xs-8 col-sm-6 col-md-5">
                                                                                    <p><input type="checkbox" id="tcuTypeShow"  value="tcuTypeShow"  <c:if test="${applicationViewDto.tcuFlag}">checked</c:if>  name="tcuTypeShow"  disabled>
                                                                                        <label class="form-check-label" for="tcuTypeShow" ><span class="check-square"></span></label>
                                                                                    </p>
                                                                                </div>
                                                                            </div>
                                                                           <c:if test="${applicationViewDto.tcuFlag}">
                                                                            <div class="form-group" id="tcuLabel" >
                                                                                <label class="col-xs-12 col-md-4 control-label">TCU Date</label>
                                                                                <div class="col-xs-8 col-sm-6 col-md-5">
                                                                                    <p>${applicationViewDto.tuc}</p>
                                                                                </div>
                                                                            </div>
                                                                          </c:if>
                                                                        </c:if>
                                                                        <%--</table>--%>
                                                                    </iais:section>
                                                                    <a style="float:left;padding-top: 1.1%;" class="back" href="/main-web/eservice/INTRANET/MohHcsaBeDashboard?dashProcessBack=1"><em class="fa fa-angle-left"></em> Back</a>
                                                                    <div align="right">
                                                                        <button name="submitBtn" id="submitButton" type="button" class="btn btn-primary">
                                                                            Submit
                                                                        </button>
                                                                    </div>
                                                                    <div>&nbsp;</div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </form>
                                                    <%@include file="/WEB-INF/jsp/iais/inspectionncList/processHistory.jsp"%>
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
        <iais:confirm msg="GENERAL_ACK018"  needCancel="false" callBack="tagConfirmCallbacksupportReport()" popupOrder="supportReport" ></iais:confirm>
</div>
<%@include file="/WEB-INF/jsp/iais/inspectionncList/uploadFile.jsp" %>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
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
        //audit
        if ('${applicationViewDto.applicationDto.applicationType}' == 'APTY007'){
            $('#recommendationDropdown').addClass('hidden');
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
        checkRecomendationOtherShow();
        //route back
        routeBackCheck();
        if('APTY006' == '${applicationViewDto.applicationDto.applicationType}'){
            $('#recommendationDropdown').addClass('hidden');
            $('#licenceStartDate').addClass('hidden');
            $('.fastTrack').addClass('hidden');
        }
        appealAoFillBack();
        checkDms();
        recommendationRemoveRequired();

        loadTcuFunction();
        //vehicle
        var recommendation = $("[name='recommendation']").val();
        appFlowVehicleShowRadio(recommendation);
    });
    function recommendationRemoveRequired() {
        if ('${applicationViewDto.applicationDto.status}' == 'APST013' ||  '${applicationViewDto.applicationDto.status}' == 'APST062' ||  '${applicationViewDto.applicationDto.status}' == 'APST065' ||  '${applicationViewDto.applicationDto.status}' == 'APST066' || '${applicationViewDto.applicationDto.status}' == 'APST067') {
        $('#recommendationFieldTrue').addClass('hidden');
        $('#recommendationFieldFalse').removeClass('hidden');
        }
    }
    function checkInspectionShow(){
        if('${isShowInspection}' == 'N'){
            $('#ApplicationViewInspection').css('display', 'none');
<%--            ${'#applicationSlidInspection'}.css('display', 'none');--%>
        }
    }

    $("[name='chooseInspection']").click(function () {
        callAjaxDropDown();
    });

    //chooseInspection
    //verifyCallAjaxDropDown
    function callAjaxDropDown(){
        showWaiting();
        var isChecked = $("[name='chooseInspection']").is(':checked');
        var verified = $("[name='verified']").val();
        var chooseInspection;
        if(isChecked){
            chooseInspection = "Y";
        }else{
            chooseInspection = "N";
        }
        $.ajax({
            type: "post",
            url:  "${pageContext.request.contextPath}/callInspectionForSixth",
            data: {verified:verified, chooseInspection:chooseInspection},
            dataType: "text",
            success: function (data) {
                $('#verifyCallAjaxDropDown').html(data);
                dismissWaiting();
            },
            error: function (msg) {
            }
        });
    }

    //check DMS decision value
    $("[name='decisionValues']").change(function selectChange() {
        checkDms();
    });

    function checkDms() {
        var isRequestForChange = $('#isRequestForChange').val();
        var decisionValues = $("[name='decisionValues']").val();
        if(isRequestForChange != 'Y'){
            checkRecommendationDMS();
        }
        if('decisionReject' == decisionValues) {
            $('.vehicle-approve').attr("disabled","disabled");
            $('.vehicle-reject').attr("disabled","disabled");
            $('.vehicle-approve').prop('checked', false);
            $('.vehicle-reject').prop('checked', true);
        } else {
            $('.vehicle-approve').removeAttr("disabled","disabled");
            $('.vehicle-reject').removeAttr("disabled","disabled");
        }
    }

    function checkRecommendationDMS(){
        if ('${applicationViewDto.applicationDto.status}' == 'APST014'){
            var selectValue = $("[name='decisionValues']").val();
            appFlowVehicleShowRadio(selectValue);
            if(${isAppealType || isWithDrawal || isCessation}){
                $('#appealRecommendation').addClass('hidden');
                if("decisionApproval" == selectValue){
                    var isChangePeriodAppealType = $('#isChangePeriodAppealType').val();
                    var isLateFeeAppealType = $('#isLateFeeAppealType').val();
                    if(isLateFeeAppealType == 'true'){
                        $('#appealReturnFee').removeClass('hidden');
                        $('#recommendationOtherDropdown').addClass('hidden');
                    }else if(isChangePeriodAppealType == 'true'){
                        $('#recommendationOtherDropdown').removeClass('hidden');
                        $('#appealReturnFee').addClass('hidden');
                    }
                }else{
                    $('#recommendationOtherDropdown').addClass('hidden');
                    $('#appealReturnFee').addClass('hidden');
                }
            }
            if("decisionApproval" == selectValue){
                if(${!(isAppealType || isWithDrawal || isCessation)}){
                    $('#recommendationDropdown').removeClass('hidden');
                    checkRecommendationOtherDropdown();
                }
            }else{
                if(${!(isAppealType || isWithDrawal || isCessation)}){
                    $('#recommendationDropdown').addClass('hidden');
                    $('#recommendationOtherDropdown').addClass('hidden');
                }
            }
        }
    }

    //request for information validate
    function rfiValidate(){
        //error_nextStage
        var selectValue = $("[name='nextStage']").val();
        if (selectValue == "PROCRFI" && ${!isAppealType && !isWithDrawal &&!isCessation}) {
            var rfiSelectValue = $('#rfiSelectValue').val();
            if(rfiSelectValue == null || rfiSelectValue == ''){
                let rfiCheckErrorMsg = $("#rfiCheckErrorMsg").val();
                $('#error_nextStage').html(rfiCheckErrorMsg);
                return false;
            }else{
                $('#error_nextStage').html("");
                return true;
            }
        }else{
            return true;
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
        if ('${applicationViewDto.applicationDto.status}' != 'APST014'){
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
            var isRequestForChange = $('#isRequestForChange').val();
            if(isRequestForChange != 'Y') {
                checkRecommendationDMS();
            }else{
                $('#recommendationDropdown').addClass('hidden');
                $('#recommendationOtherDropdown').addClass('hidden');
            }
        }
    }


    $("#submitButton").click(function () {
        var selectDetail = $('#selectDetail').html();
        if(selectDetail != null && selectDetail != ''){
            $('#rfiSelectValue').val(selectDetail);
        }
        if(rfiValidate()){
            showWaiting();
            document.getElementById("mainForm").submit();
            $("#submitButton").attr("disabled", true);
        }else{
            return;
        }
    });

    function check(){
        var selectValue = $("[name='nextStage']").val();
        var isChangePeriodAppealType = $('#isChangePeriodAppealType').val();
        var appealRecommenValueShow = $('#appealRecommenValueShow').text();
        if (selectValue == "PROCVER") {
            $("#chooseInspectionBox").removeClass('hidden');
            $('#verifiedDropdown').removeClass('hidden');
            $('#rollBackDropdown').addClass('hidden');
            $('#routeBackReviewBox').addClass('hidden');
            $('#comments').addClass('hidden');
            $('#appealRecommendationDiv').removeClass('hidden');
            $('#normalRecommendationDiv').removeClass('hidden');
            let recommenVal = $('#recommendation').val();
            let appealRecommenVal = $('#appealRecommendationValues').val();
            if('other' == recommenVal || (('appealApprove' == appealRecommenVal || 'Approve' == appealRecommenValueShow) && isChangePeriodAppealType == 'true')){
                $('#recommendationOtherDropdown').removeClass('hidden');
            } else {
                $('#recommendationOtherDropdown').addClass('hidden');
            }
        } else if (selectValue == "PROCRB") {
            $("#chooseInspectionBox").addClass('hidden');
            $('#rollBackDropdown').removeClass('hidden');
            $('#routeBackReviewBox').removeClass('hidden');
            $('#verifiedDropdown').addClass('hidden');
            $('#comments').addClass('hidden');
            $('#appealRecommendationDiv').removeClass('hidden');
            $('#normalRecommendationDiv').removeClass('hidden');
            checkAppealPso();
        } else if (selectValue == "PROCRFI") {
            $("#chooseInspectionBox").addClass('hidden');
            $('#verifiedDropdown').addClass('hidden');
            $('#rollBackDropdown').addClass('hidden');
            $('#routeBackReviewBox').addClass('hidden');
            $('#comments').removeClass('hidden');
            $('#appealRecommendationDiv').addClass('hidden');
            $('#normalRecommendationDiv').addClass('hidden');
            $('#recommendationOtherDropdown').addClass('hidden');
            // showPopupWindow('/hcsa-licence-web/eservice/INTRANET/LicenceBEViewService?rfi=rfi');
        } else {
            $("#chooseInspectionBox").addClass('hidden');
            $('#rollBackDropdown').addClass('hidden');
            $('#verifiedDropdown').addClass('hidden');
            $('#routeBackReviewBox').addClass('hidden');
            $('#comments').addClass('hidden');
            $('#appealRecommendationDiv').removeClass('hidden');
            $('#normalRecommendationDiv').removeClass('hidden');
        }
    }

    //recommendation
    $("[name='recommendation']").change(function selectChange() {
        checkRecomendationOtherShow();
    });

    function checkRecomendationOtherShow(){
        var recommendation = $("[name='recommendation']").val();
        if('other' == recommendation){
            $('#recommendationOtherDropdown').removeClass('hidden');
            $('.vehicle-approve').removeAttr("disabled","disabled");
            $('.vehicle-reject').removeAttr("disabled","disabled");
        } else if('reject' == recommendation) {
            $('.vehicle-approve').attr("disabled","disabled");
            $('.vehicle-reject').attr("disabled","disabled");
            $('.vehicle-approve').prop('checked', false);
            $('.vehicle-reject').prop('checked', true);
            $('#recommendationOtherDropdown').addClass('hidden');
        }else{
            $('.vehicle-approve').removeAttr("disabled","disabled");
            $('.vehicle-reject').removeAttr("disabled","disabled");
            $('#recommendationOtherDropdown').addClass('hidden');
        }
    }

    function checkRecommendationOtherDropdown(){
        var recommendation = $("[name='recommendation']").val();
        var appealRecommendation = $("[name='appealRecommendationValues']").val();
        var isChangePeriodAppealType = $('#isChangePeriodAppealType').val();
        var isAppealType = $('#isAppealType').val();
        if((('other' == recommendation) && ('false' == isAppealType)) || (isChangePeriodAppealType == 'true' && 'appealApprove' == appealRecommendation)){
            if('${applicationViewDto.applicationDto.status}' != 'APST014'){
                $('#recommendationOtherDropdown').removeClass('hidden');
            }
        }else{
            $('#recommendationOtherDropdown').addClass('hidden');
        }
    }




    $("[name='nextStage']").change(function selectChange() {
        var selectValue = $("[name='nextStage']").val();
        var isChangePeriodAppealType = $('#isChangePeriodAppealType').val();
        var appealRecommenValueShow = $('#appealRecommenValueShow').text();
        if (selectValue == "PROCVER") {
            $("#chooseInspectionBox").removeClass('hidden');
            $('#verifiedDropdown').removeClass('hidden');
            $('#rollBackDropdown').addClass('hidden');
            $('#routeBackReviewBox').addClass('hidden');
            $('#comments').addClass('hidden');
            $('#appealRecommendationFalse').addClass('hidden');
            $('#appealRecommendationTrue').removeClass('hidden');
            $('#appealRecommendationDiv').removeClass('hidden');
            $('#normalRecommendationDiv').removeClass('hidden');
            let recommenVal = $('#recommendation').val();
            let appealRecommenVal = $('#appealRecommendationValues').val();
            if('other' == recommenVal || (('appealApprove' == appealRecommenVal || 'Approve' == appealRecommenValueShow) && isChangePeriodAppealType == 'true')){
                $('#recommendationOtherDropdown').removeClass('hidden');
            } else {
                $('#recommendationOtherDropdown').addClass('hidden');
            }
        } else if (selectValue == "PROCRB") {
            $("#chooseInspectionBox").addClass('hidden');
            $('#rollBackDropdown').removeClass('hidden');
            $('#verifiedDropdown').addClass('hidden');
            $('#routeBackReviewBox').removeClass('hidden');
            $('#comments').addClass('hidden');
            $('#appealRecommendationFalse').addClass('hidden');
            $('#appealRecommendationTrue').removeClass('hidden');
            checkAppealPso();
            $('#appealRecommendationDiv').removeClass('hidden');
            $('#normalRecommendationDiv').removeClass('hidden');
        } else if (selectValue == "PROCRFI") {
            $("#chooseInspectionBox").addClass('hidden');
            $('#verifiedDropdown').addClass('hidden');
            $('#rollBackDropdown').addClass('hidden');
            $('#routeBackReviewBox').addClass('hidden');
            $('#comments').removeClass('hidden');
            showPopupWindow('/hcsa-licence-web/eservice/INTRANET/LicenceBEViewService?rfi=rfi');
            $('#appealRecommendationTrue').addClass('hidden');
            $('#appealRecommendationFalse').removeClass('hidden');
            $('#recommendationFieldTrue').addClass('hidden');
            $('#recommendationFieldFalse').removeClass('hidden');
            $('#appealRecommendationDiv').addClass('hidden');
            $('#normalRecommendationDiv').addClass('hidden');
            $('#recommendationOtherDropdown').addClass('hidden');
        } else {
            $("#chooseInspectionBox").addClass('hidden');
            $('#comments').addClass('hidden');
            $('#rollBackDropdown').addClass('hidden');
            $('#verifiedDropdown').addClass('hidden');
            $('#routeBackReviewBox').addClass('hidden');
            $('#appealRecommendationFalse').addClass('hidden');
            $('#appealRecommendationTrue').removeClass('hidden');
            $('#rfiSelect').hide();
            $('#appealRecommendationDiv').removeClass('hidden');
            $('#normalRecommendationDiv').removeClass('hidden');
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
        //PROCVER nextStage
        <%--var nextStage = $("[name='nextStage']").val();--%>
        <%--if('PROCVER' == nextStage && selectValue != '${RecommendValue}'){--%>
        <%--    $('#internalRemarksTrue').removeClass('hidden');--%>
        <%--    $('#internalRemarksFalse').addClass('hidden');--%>
        <%--}else if('PROCVER' == nextStage && selectValue ==  '${RecommendValue}'){--%>
        <%--    $('#internalRemarksTrue').addClass('hidden');--%>
        <%--    $('#internalRemarksFalse').removeClass('hidden');--%>
        <%--}--%>
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

    $('#rollBack').change(function selectRollBackChange() {
        checkAppealPso();
    });

    function checkAppealPso(){
        if('${applicationViewDto.applicationDto.status}' == 'APST012'){
            $('#appealRecommendationTrue').addClass('hidden');
            $('#appealRecommendationFalse').removeClass('hidden');
        }else{
            $('#appealRecommendationTrue').removeClass('hidden');
            $('#appealRecommendationFalse').addClass('hidden');
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
            message: '<div style="padding:3px;">We are processing your request now; please do not click the Back or Refresh button in the browser.</div>',
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
            $('#doProcess').click();
            $('#process').addClass("active");
        }else if("Y" == '${doDocument}'){
            $('#info').removeClass("active");
            $('#document').addClass("active");
            $('#doDocument').click();
        }

        if("Y"=='${uploadFileValidate}'){
            $('#uploadButton').click();
        }
    }

    function doVerifyFileGo(verify) {
        showWaiting();
        var data = {"repoId":verify};
        $.post(
            "${pageContext.request.contextPath}/verifyFileExist",
            data,
            function (data) {
                if(data != null ){
                    if(data.verify == 'N'){
                        $('#supportReport').modal('show');
                    }else {
                        $("#"+verify+"Down").click();
                    }
                    dismissWaiting();
                }
            }
        )
    }

    function tagConfirmCallbacksupportReport(){
        $('#supportReport').modal('hide');
    }

    function loadTcuFunction() {
        if(${applicationViewDto.showTcu && applicationViewDto.editTcu}){
            if($("#tcuType").is(":checked")){
                $("#tcuLabel").show()
            }else{
                $("#tcuLabel").hide();
                $("#tucDate").val("");
            }
            if(${applicationViewDto.tuc!= null && applicationViewDto.tuc != ""}){
                $("#tcuType").attr('checked',true)
                $("#tcuLabel").show();
            }
        }
    }
    function showTcuLabel(checkbox){
        if(checkbox.checked == true){
            $("#tcuLabel").show()
        }else{
            $("#tcuLabel").hide();
            $("#tucDate").val("");
        }
    }


</script>



