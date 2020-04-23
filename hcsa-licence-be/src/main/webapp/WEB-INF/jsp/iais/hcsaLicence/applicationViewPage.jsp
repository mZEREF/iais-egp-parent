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
                                <div class="alert alert-info" role="alert"><strong>
                                    <h4>Supporting Document</h4>
                                </strong></div>
                                <div id="u8522_text" class="text ">
                                    <p><span>These are documents uploaded by the applicant or an officer on behalf of the applicant. Listed
												documents are those defined for this digital service only.</span></p>
                                </div>
                                <div class="row">
                                    <div class="col-xs-12">
                                        <div class="table-gp">
                                            <table class="table">
                                                <thead>
                                                <tr>
                                                    <th width="30%">Document</th>
                                                    <th width="20%">File</th>
                                                    <th width="10%">Size</th>
                                                    <th width="20%">Submitted By</th>
                                                    <th width="20%">Date Submitted</th>
                                                </tr>
                                                </thead>

                                                <tbody>
                                                <c:forEach items="${applicationViewDto.appSupDocDtoList}" var="appSupDocDto" varStatus="status">
                                                    <tr>
                                                        <td width="30%">
                                                            <p><c:out value="${appSupDocDto.file}"></c:out></p>
                                                        </td>
                                                        <td width="20%">
                                                            <p>
                                                                <a href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}"  value="${appSupDocDto.fileRepoId}"/>&fileRepoName=${appSupDocDto.document}" title="Download" class="downloadFile">
                                                                    <c:out value="${appSupDocDto.document}"></c:out>
                                                                </a>
                                                            </p>
                                                        </td>
                                                        <td width="10%">
                                                            <p><c:out value="${appSupDocDto.size}"></c:out></p>
                                                        </td>
                                                        <td width="20%">
                                                            <p><c:out value="${appSupDocDto.submittedBy}"></c:out></p>
                                                        </td>
                                                        <td width="20%">
                                                            <p><c:out value="${appSupDocDto.dateSubmitted}"></c:out></p>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                                <c:if test="${appSupDocDtoListNull == 'Y'}">
                                                    <tr>
                                                        <td colspan="5" align="center">
                                                            <iais:message key="ACK018" escape="true"></iais:message>
                                                        </td>
                                                    </tr>
                                                </c:if>
                                                </tbody>

                                            </table>
                                            <div class="alert alert-info" role="alert"><strong>
                                                <h4>Internal Document</h4>
                                            </strong></div>
                                            <div class="text ">
                                                <p><span>These are documents uploaded by an agency officer to support back office processing.</span>
                                                </p>
                                            </div>
                                            <table class="table">
                                                <thead>
                                                <tr>
                                                    <th width="30%">Document</th>
                                                    <th width="20%">File</th>
                                                    <th width="10%">Size</th>
                                                    <th width="20%">Submitted By</th>
                                                    <th width="15%">Date Submitted</th>
                                                    <th width="5%">Action</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <%--                                                <tr>--%>
                                                <%--                                                    <td colspan="7" align="center">--%>
                                                <%--                                                        <iais:message key="ACK018" escape="true"></iais:message>--%>
                                                <%--                                                    </td>--%>
                                                <%--                                                </tr>--%>

                                                <c:choose>
                                                    <c:when test="${empty applicationViewDto.appIntranetDocDtoList}">
                                                        <tr>
                                                            <td colspan="6"  align="center">
                                                                <iais:message key="ACK018" escape="true"></iais:message>
                                                            </td>
                                                        </tr>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:forEach var="interalFile" items="${applicationViewDto.appIntranetDocDtoList}" varStatus="status">
                                                            <tr>
                                                                <td width="30%">
                                                                    <p><c:out value="${interalFile.docDesc}"></c:out></p>
                                                                </td>
                                                                <td width="20%">
                                                                        <%--                                                                        <p><a href="#"><c:out--%>
                                                                        <%--                                                                                value="${interalFile.docName}.${interalFile.docType}"></c:out></a></p>--%>
                                                                    <p><a href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}"  value="${interalFile.fileRepoId}"/>&fileRepoName=${interalFile.docName}" title="Download" class="downloadFile">
                                                                        <c:out value="${interalFile.docName}.${interalFile.docType}"></c:out>
                                                                    </a></p>
                                                                </td>
                                                                <td width="10%">
                                                                    <p><c:out value="${interalFile.docSize}"></c:out></p>
                                                                </td>
                                                                <td width="20%">
                                                                    <p><c:out value="${interalFile.submitByName}"></c:out></p>
                                                                </td>
                                                                <td width="15%">
                                                                    <p><fmt:formatDate value='${interalFile.submitDt}' pattern='dd/MM/yyyy HH:mm:ss'/></p>
                                                                </td>
                                                                <td width="5%">
                                                                    <a href="javascript:deleteFile('<iais:mask name="interalFileId" value="${interalFile.id}"/>');"><label style="color: #D22727; font-size: 2rem; cursor:pointer;">X</label></a>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </c:otherwise>
                                                </c:choose>



                                                </tbody>
                                            </table>
                                            <%--upload file--%>
                                            <div align="right">
                                                <button type="button" id="uploadButton" class="btn btn-primary" data-toggle="modal" data-target="#uploadDoc">
                                                    Upload Document
                                                </button>
                                            </div>

                                        </div>
                                    </div>
                                </div>

                            </div>
                            <%--         Inspection start                       --%>
                            <div class="tab-pane" id="tabInspection" role="tabpanel">
                                <%@include file="applicationInspection.jsp" %>
                            </div>
                            <%--         Inspection end                       --%>
                            <div class="tab-pane" id="tabProcessing" role="tabpanel">
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
                                                    <iais:row>
                                                        <iais:field value="Current Status" required="false"/>
                                                        <iais:value width="10"><p>${applicationViewDto.currentStatus}</p></iais:value>
                                                    </iais:row>
                                                    <%--<div>--%>
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
                                                    <%--</div>--%>
                                                    <div id="processingDecision">
                                                        <iais:row>
                                                            <iais:field value="Processing Decision" required="true"/>
                                                            <%String nextStage = request.getParameter("nextStage");%>
                                                            <iais:value width="10">
                                                                <iais:select name="nextStage" id="nextStage"
                                                                             options="nextStages"
                                                                             value="<%=nextStage%>"></iais:select>
                                                            </iais:value>
                                                        </iais:row>
                                                    </div>
                                                    <c:if test="${applicationViewDto.applicationDto.status == 'APST057' || applicationViewDto.applicationDto.status == 'APST014' || applicationViewDto.applicationDto.status == 'APST013'}">
                                                        <div id="replytr" class="hidden">
                                                            <iais:row>
                                                                <iais:field value="Processing Decision" required="true"/>
                                                                <%--                                                                <%String selectNextStageReply = request.getParameter("selectNextStageReply");%>--%>
                                                                <iais:value width="10">
                                                                    <iais:select name="nextStageReplys" id="nextStageReply"
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
                                                                <iais:field value="Decision" required="true"/>
                                                                <iais:value width="10">
                                                                    <iais:select name="decisionValues" id="decisionValues"
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
                                                                <select name="rollBack" class="nice-select input-large">
                                                                    <option value="">Please Select</option>
                                                                    <c:forEach items="${applicationViewDto.rollBack}"
                                                                               var="rollBack">
                                                                        <option value="${rollBack.value}" <c:if test="${rollBack.value == selectRollBack}">selected</c:if>>${rollBack.key}</option>
                                                                    </c:forEach>
                                                                </select>
                                                                <span id="error_rollBack" name="iaisErrorMsg" class="error-msg"></span>
                                                            </iais:value>
                                                        </iais:row>
                                                    </div>
                                                    <div id="verifiedDropdown" class="hidden">
                                                        <iais:row>
                                                            <iais:field value="Verified" required="true"/>
                                                            <iais:value width="10">
                                                                <%--                                                                <select name="verified" class="nice-select input-large">--%>
                                                                <%--                                                                    <option value="">Please Select</option>--%>
                                                                <%--                                                                    <c:forEach items="${applicationViewDto.verified}"--%>
                                                                <%--                                                                               var="verified">--%>
                                                                <%--                                                                        <option value="${verified.key}" <c:if test="${verified.key == selectVerified}">selected</c:if>>${verified.value}</option>--%>
                                                                <%--                                                                    </c:forEach>--%>
                                                                <%--                                                                </select>--%>
                                                                <iais:select name="verified"
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

                                                    <div id="licenceStartDate">
                                                        <iais:row>
                                                            <iais:field value="Licence Start Date" required="false"/>
                                                            <iais:value width="10">
                                                                <c:choose>
                                                                    <c:when test="${applicationViewDto.applicationDto.status=='APST007'}">
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
                                                    <div id="recommendationDropdown">
                                                        <iais:row>
                                                            <div id="recommendationFieldTrue" class="hidden"><iais:field value="Recommendation" required="true"/></div>
                                                            <div id="recommendationFieldFalse"><iais:field value="Recommendation" required="false"/></div>
                                                            <iais:value width="10">
                                                                <c:choose>
                                                                    <c:when test="${applicationViewDto.applicationDto.status=='APST007' || applicationViewDto.applicationDto.status=='APST012' || applicationViewDto.applicationDto.status=='APST014'}">
                                                                        <iais:select name="recommendation"
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
                                                            <iais:field value="Other Period" required="true"/>
                                                            <iais:value width="10">
                                                                <%--                                                                <%String otherNumber = request.getParameter("number");%>--%>
                                                                <%--                                                                <%String otherChrono = request.getParameter("chrono");%>--%>
                                                                <input onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" id=recomInNumber type="text" name="number" maxlength="2" value="${otherNumber}">
                                                                <span id="error_recomInNumber" name="iaisErrorMsg" class="error-msg"></span>
                                                                <iais:select id="chronoUnit" name="chrono" options="recommendationOtherDropdown" value="${otherChrono}"/>
                                                                <span id="error_chronoUnit" name="iaisErrorMsg" class="error-msg"></span>
                                                            </iais:value>
                                                        </iais:row>
                                                    </div>

                                                    <div class="fastTrack">
                                                        <iais:row>
                                                            <iais:field value="Fast Tracking?" required="false"/>
                                                            <iais:value width="10">
                                                                <p>
                                                                    <c:choose>
                                                                        <c:when test="${applicationViewDto.applicationDto.status=='APST007' || applicationViewDto.applicationDto.status=='APST012'}">
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
                                <div class="alert alert-info" role="alert">
                                    <strong>
                                        <h4>Processing History</h4>
                                    </strong>
                                </div>
                                <div class="row">
                                    <div class="col-xs-12">
                                        <div class="table-gp">
                                            <table class="table">
                                                <thead>
                                                <tr>
                                                    <th>Username</th>
                                                    <th>Working Group</th>
                                                    <th>Status Update</th>
                                                    <th>Remarks</th>
                                                    <th>Last Updated</th>
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
        <%@include file="uploadFile.jsp" %>
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
        if ('${applicationViewDto.applicationDto.status}' == 'APST057' || '${applicationViewDto.applicationDto.status}' == 'APST014' || '${applicationViewDto.applicationDto.status}' == 'APST013') {
            $('#processingDecision').addClass('hidden');
            // $('#recommendationDropdown').addClass('hidden');
            $('#replytr').removeClass('hidden');
            $('#licenceStartDate').addClass('hidden');
        }
        //cessation
        if ('${applicationViewDto.applicationDto.applicationType}' == 'APTY008'){
            $('#recommendationDropdown').addClass('hidden');
            $('#licenceStartDate').addClass('hidden');
        }
        $('#rfiSelect').hide();
        check();
        validate();
        checkVerifiedField();
        //check DMS
        DMSCheck();
        checkRecommendationOtherDropdown();
        checkInspectionShow();

        if('APTY006' == '${applicationViewDto.applicationDto.applicationType}' && 'APST007' == '${applicationViewDto.applicationDto.status}'){
            $('#recommendationDropdown').addClass('hidden');
            $('#replytr').removeClass('hidden');
            $('#licenceStartDate').addClass('hidden');
            $('.fastTrack').addClass('hidden');
        }
    });

    <%--function DMSCheck(){--%>
    <%--    var decisionValue = $("[name='decisionValues']").val();--%>
    <%--    if('${applicationViewDto.applicationDto.status}' == 'APST014'){--%>
    <%--        if(decisionValue == 'decisionApproval'){--%>
    <%--            $('#recommendationFieldTrue').removeClass('hidden');--%>
    <%--            $('#recommendationFieldFalse').addClass('hidden');--%>
    <%--        }else{--%>
    <%--            $('#recommendationFieldTrue').addClass('hidden');--%>
    <%--            $('#recommendationFieldFalse').removeClass('hidden');--%>
    <%--        }--%>
    <%--    }--%>
    <%--}--%>

    function checkInspectionShow(){
        if('${isShowInspection}' == 'N'){
            $('#ApplicationViewInspection').css('display', 'none');
        }
    }

    function DMSCheck(){
        if('${applicationViewDto.applicationDto.status}' == 'APST014'){
                $('#recommendationFieldTrue').removeClass('hidden');
                $('#recommendationFieldFalse').addClass('hidden');
        }
    }


    $("#submitButton").click(function () {
        showWaiting();
        document.getElementById("mainForm").submit();
        $("#submitButton").attr("disabled", true);
    });

    function check(){
        var selectValue = $("[name='nextStage']").val();
        if (selectValue == "APST016") {
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
        if('other' == recommendation){
            $('#recommendationOtherDropdown').removeClass('hidden');
        }else{
            $('#recommendationOtherDropdown').addClass('hidden');
        }
    }



    $("[name='nextStage']").change(function selectChange() {
        var selectValue = $("[name='nextStage']").val();
        if (selectValue == "APST016") {
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

    //check decision value
    // $("[name='decisionValues']").change(function selectChange() {
    //     //var selectValue = $("[name='decisionValues']").val();
    //     DMSCheck();
    // });


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



