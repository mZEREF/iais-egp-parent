<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.ecq.com/iais-bsb" prefix="iais-bsb" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-inbox.js"></script>


<%@include file="../dashboard/dashboard.jsp"%>


<div class="main-content">
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div class="tab-gp dashboard-tab" style="margin-left: 6px;margin-right: -8px;">
                    <%@ include file="../InnerNavBar.jsp"%>

                    <div style="padding: 50px 0">
                        <form class="" method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
                            <div id="searchPanel" class="tab-search" style="padding: 0 90px">

                                <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
                                <input type="hidden" name="action_type" value="">
                                <input type="hidden" name="action_value" value="">
                                <input type="hidden" name="action_additional" value="">


                                <%--@elvariable id="inboxAppSearchDto" type="sg.gov.moh.iais.egp.bsb.dto.inbox.InboxAppSearchDto"--%>
                                <div class="row">
                                    <div class="col-xs-12 col-sm-6">
                                        <label class="col-xs-12 col-sm-5 control-label" FOR="searchAppNo">Application No.:</label>
                                        <div class="col-xs-12 col-sm-7">
                                            <input type="text" id="searchAppNo" name="searchAppNo" value="${inboxAppSearchDto.searchAppNo}"/>
                                            <span data-err-ind="searchAppNo" class="error-msg"></span>
                                        </div>

                                    </div>
                                    <div class="col-xs-12 col-sm-6">
                                        <label class="col-xs-12 col-sm-5 control-label" FOR="searchProcessType">Application Sub-Type:</label>
                                        <div class="col-xs-12 col-sm-7">
                                            <select id="searchProcessType" class="searchProcessTypeDropdown" name="searchProcessType">
                                                <option value='<c:out value=""/>' <c:if test="${inboxAppSearchDto.searchProcessType eq ''}">selected="selected"</c:if>>All</option>
                                                <%--@elvariable id="processTypeOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
                                                <c:forEach var="appStatusItem" items="${processTypeOps}">
                                                    <option value='<c:out value="${appStatusItem.value}"/>' <c:if test="${inboxAppSearchDto.searchProcessType eq appStatusItem.value}">selected="selected"</c:if> ><c:out value="${appStatusItem.text}"/></option>
                                                </c:forEach>
                                            </select>
                                            <span data-err-ind="searchProcessType" class="error-msg"></span>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-xs-12 col-sm-6">
                                        <label class="col-xs-12 col-sm-5 control-label" for="searchFacilityName">Facility Name:</label>
                                        <div class="col-xs-12 col-sm-7">
                                            <input type="text" id="searchFacilityName" name="searchFacilityName" value="${inboxAppSearchDto.searchFacilityName}"/>
                                            <span data-err-ind="searchFacilityName" class="error-msg"></span>
                                        </div>

                                    </div>
                                    <div class="col-xs-12 col-sm-6">
                                        <label class="col-xs-12 col-sm-5 control-label" FOR="searchStatus">Application Status:</label>
                                        <div class="col-xs-12 col-sm-7">
                                            <select id="searchStatus" class="searchStatusDropdown" name="searchStatus">
                                                <option value='<c:out value=""/>' <c:if test="${inboxAppSearchDto.searchStatus eq ''}">selected="selected"</c:if>>All</option>
                                                <%--@elvariable id="appStatusOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
                                                <c:forEach var="appStatusItem" items="${appStatusOps}">
                                                    <option value='<c:out value="${appStatusItem.value}"/>' <c:if test="${inboxAppSearchDto.searchStatus eq appStatusItem.value}">selected="selected"</c:if> ><c:out value="${appStatusItem.text}"/></option>
                                                </c:forEach>
                                            </select>
                                            <span data-err-ind="searchStatus" class="error-msg"></span>
                                        </div>
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 20px">
                                    <div class="col-xs-12 col-sm-6">
                                        <label for="searchAppType" class="col-xs-12 col-sm-5 control-label">Application Type:</label>
                                        <div class="col-xs-12 col-sm-7">
                                            <select id="searchAppType" class="searchAppTypeDropdown" name="searchAppType">
                                                <option value='<c:out value=""/>' <c:if test="${inboxAppSearchDto.searchAppType eq ''}">selected="selected"</c:if>>All</option>
                                                <%--@elvariable id="appTypeOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
                                                <c:forEach var="appTypeItem" items="${appTypeOps}">
                                                    <option value='<c:out value="${appTypeItem.value}"/>' <c:if test="${inboxAppSearchDto.searchAppType eq appTypeItem.value}">selected="selected"</c:if> ><c:out value="${appTypeItem.text}"/></option>
                                                </c:forEach>
                                            </select>
                                            <span data-err-ind="searchAppType" class="error-msg"></span>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-xs-12 col-sm-6">
                                        <label class="col-xs-12 col-sm-5 control-label" style="padding-left: 0">Date Saved/Submitted From:</label>
                                        <div class="col-xs-12 col-sm-7">
                                            <iais:datePicker id="searchSubmissionDateFrom" name="searchSubmissionDateFrom" value="${inboxAppSearchDto.searchSubmissionDateFrom}"/>
                                        </div>
                                        <span data-err-ind="searchSubmissionDateFrom" class="error-msg"></span>
                                    </div>
                                    <div class="col-xs-12 col-sm-6">
                                        <label class="col-xs-12 col-sm-5 control-label" style="padding-left: 0">Date Saved/Submitted To:</label>
                                        <div class="col-xs-12 col-sm-7">
                                            <iais:datePicker id="searchSubmissionDateTo" name="searchSubmissionDateTo" value="${inboxAppSearchDto.searchSubmissionDateTo}"/>
                                        </div>
                                        <span data-err-ind="searchSubmissionDateTo" class="error-msg"></span>
                                    </div>
                                </div>
                            </div>
                            <div class="row text-right text-center-mobile">
                                <button class="btn btn-secondary" type="reset" id="clearBtn" name="clearBtn">Clear</button>
                                <button class="btn btn-primary" type="button" id="searchBtn" name="searchBtn">Search</button>
                            </div>

                            <%--@elvariable id="pageInfo" type="sg.gov.moh.iais.egp.bsb.dto.PageInfo"--%>
                            <iais-bsb:Pagination size="${pageInfo.size}" pageNo="${pageInfo.pageNo + 1}" pageAmt="${pageInfo.totalPages}" totalElements="${pageInfo.totalElements}"/>

                            <div class="row">
                                <div class="col-xs-12">
                                    <div class="table-gp">
                                        <table aria-describedby="" class="table">
                                            <thead>
                                            <tr>
                                                <%-- need to use new tag in future --%>
                                                <th scope="col" style="display: none"></th>
                                                <iais:sortableHeader needSort="true" field="applicationNo" value="Application No." isFE="true" style="width:15%"/>
                                                <iais:sortableHeader needSort="true" field="facilityName" value="Facility Name" isFE="true" style="width:13%"/>
                                                <iais:sortableHeader needSort="true" field="appType" value="Application Type" isFE="true" style="width:14%"/>
                                                <iais:sortableHeader needSort="true" field="processType" value="Application Sub-Type" style="width:13%" isFE="true"/>
                                                <iais:sortableHeader needSort="true" field="status" value="Application Status" style="width:13%" isFE="true"/>
                                                <iais:sortableHeader needSort="true" field="applicationDt" value="Date Saved/Submitted" isFE="true" style="width:15%"/>
                                                <iais:sortableHeader needSort="false" field="" value="Actions" isFE="true" style="width:17%"/>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:choose>
                                                <%--@elvariable id="dataList" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.inbox.InboxAppResultDto>"--%>
                                                <c:when test="${empty dataList}">
                                                    <tr>
                                                        <td colspan="6">
                                                            <iais:message key="GENERAL_ACK018" escape="true"/>
                                                        </td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="app" items="${dataList}" varStatus="status">
                                                        <tr>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Application No.</p>
                                                                <c:choose>
                                                                    <c:when test="${app.appType eq 'BSBAPTY001' and app.appSubType eq 'PROTYPE001'}">
                                                                        <a href="/bsb-web/eservice/INTERNET/MohBsbViewFacRegApplication?appId=<iais:mask name='id' value='${app.appId}'/><c:if test="${app.appSubType eq 'BSBAPST001'}">&editId=<iais:mask name='editId' value='${app.appId}'/></c:if>"><c:out value="${app.appNo}"/></a>
                                                                    </c:when>
                                                                    <c:when test="${app.appType eq 'BSBAPTY001' and (app.appSubType eq 'PROTYPE002' or app.appSubType eq 'PROTYPE003' or app.appSubType eq 'PROTYPE004')}">
                                                                        <a href="/bsb-web/eservice/INTERNET/MohViewApprovalPossessApplication?appId=<iais:mask name='id' value='${app.appId}'/>&processType=${app.appSubType}<c:if test="${app.appSubType eq 'BSBAPST001'}">&editId=<iais:mask name='editId' value='${app.appId}'/></c:if>"><c:out value="${app.appNo}"/></a>
                                                                    </c:when>
                                                                    <c:when test="${app.appType eq 'BSBAPTY001' and app.appSubType eq 'PROTYPE005'}">
                                                                        <a href="/bsb-web/eservice/INTERNET/MohBsbViewCertRegApplication?appId=<iais:mask name='id' value='${app.appId}'/><c:if test="${app.appSubType eq 'BSBAPST001'}">&editId=<iais:mask name='editId' value='${app.appId}'/></c:if>"><c:out value="${app.appNo}"/></a>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <c:out value="${app.appNo}"/>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Facility Name</p>
                                                                <p style="text-align: center"><c:out value="${app.facilityName}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Application Type</p>
                                                                <p style="text-align: center"><iais:code code="${app.appType}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Application Sub-Type</p>
                                                                <p><iais:code code="${app.appSubType}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Application Status</p>
                                                                <p><iais:code code="${app.appStatus}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Date Saved/Submitted</p>
                                                                <p><c:out value="${app.submittedDt}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Actions</p>
                                                                <iais-bsb:app-action info="${app}" attributeKey="actionAvailable"/>
                                                                <c:choose>
                                                                    <c:when test="${not actionAvailable}">
                                                                        <select  class="naDropdown" disabled="disabled">
                                                                            <option>N/A</option>
                                                                        </select>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <select id="appAction${status.index}" class="appActionDropdown${status.index}" name="appAction${status.index}" data-action-select="">
                                                                            <option value="#" selected="selected">Select</option>
                                                                            <c:if test="${AppEditableJudge}">
                                                                                <c:choose>
                                                                                    <c:when test="${app.appType eq 'BSBAPTY001' and app.appSubType eq 'PROTYPE001'}">
                                                                                        <option value="/bsb-web/eservice/INTERNET/MohBsbFacilityRegistration?editId=<iais:mask name='editId' value='${app.appId}'/>">Edit</option>
                                                                                    </c:when>
                                                                                    <c:when test="${app.appType eq 'BSBAPTY001' and (app.appSubType eq 'PROTYPE002' or app.appSubType eq 'PROTYPE003' or app.appSubType eq 'PROTYPE004')}">
                                                                                        <option value="/bsb-web/eservice/INTERNET/MohApprovalApplication?editId=<iais:mask name='editId' value='${app.appId}'/>&processType=${app.appSubType}">Edit</option>
                                                                                    </c:when>
                                                                                    <c:when test="${app.appType eq 'BSBAPTY001' and app.appSubType eq 'PROTYPE005'}">
                                                                                        <option value="/bsb-web/eservice/INTERNET/MohBsbFacilityCertifierRegistration?editId=<iais:mask name='editId' value='${app.appId}'/>">Edit</option>
                                                                                    </c:when>
                                                                                </c:choose>
                                                                            </c:if>
                                                                            <c:if test="${DraftAppJudge}">
                                                                                <c:choose>
                                                                                    <c:when test="${app.appType eq 'BSBAPTY001' and app.appSubType eq 'PROTYPE001'}">
                                                                                        <option value="/bsb-web/eservice/INTERNET/MohBsbFacilityRegistration?editId=<iais:mask name='editId' value='${app.appId}'/>">Continue</option>
                                                                                    </c:when>
                                                                                    <c:when test="${app.appType eq 'BSBAPTY001' and (app.appSubType eq 'PROTYPE002' or app.appSubType eq 'PROTYPE003' or app.appSubType eq 'PROTYPE004' or app.appSubType eq 'PROTYPE012')}">
                                                                                        <option value="/bsb-web/eservice/INTERNET/MohApprovalBatAndActivity?editId=<iais:mask name='editId' value='${app.appId}'/>&processType=${app.appSubType}">Continue</option>
                                                                                    </c:when>
                                                                                    <c:when test="${app.appType eq 'BSBAPTY001' and app.appSubType eq 'PROTYPE005'}">
                                                                                        <option value="/bsb-web/eservice/INTERNET/MohBsbFacilityCertifierRegistration?editId=<iais:mask name='editId' value='${app.appId}'/>">Continue</option>
                                                                                    </c:when>
                                                                                    <c:when test="${app.appType eq 'BSBAPTY005' and app.appSubType eq 'PROTYPE001'}">
                                                                                        <option value="/bsb-web/eservice/INTERNET/ApplicantDeRegistrationFacility?editId=<iais:mask name='editId' value='${app.appId}'/>">Continue</option>
                                                                                    </c:when>
                                                                                    <c:when test="${app.appType eq 'BSBAPTY004' and (app.appSubType eq 'PROTYPE002' or app.appSubType eq 'PROTYPE003' or app.appSubType eq 'PROTYPE004')}">
                                                                                        <option value="/bsb-web/eservice/INTERNET/ApplicantCancellationApproval?editId=<iais:mask name='editId' value='${app.appId}'/>">Continue</option>
                                                                                    </c:when>
                                                                                    <c:when test="${app.appType eq 'BSBAPTY005' and app.appSubType eq 'PROTYPE005'}">
                                                                                        <option value="/bsb-web/eservice/INTERNET/ApplicantDeRegistrationAFC?editId=<iais:mask name='editId' value='${app.appId}'/>">Continue</option>
                                                                                    </c:when>
                                                                                    <c:when test="${app.appSubType eq 'PROTYPE006'}">
                                                                                        <option value="/bsb-web/eservice/INTERNET/JudgeDataSubmissionType?editId=<iais:mask name='editId' value='${app.appId}'/>">Continue</option>
                                                                                    </c:when>
                                                                                    <c:when test="${app.appSubType eq 'PROTYPE008'}">
                                                                                        <option value="/bsb-web/eservice/INTERNET/IncidentCheckProcess?editId=<iais:mask name='editId' value='${app.appId}'/>">Continue</option>
                                                                                    </c:when>
                                                                                </c:choose>

                                                                                <option value="deleteDraft<iais:mask name='deleteId' value='${app.appId}'/>">Delete</option>
                                                                            </c:if>
                                                                            <c:if test="${app.appStatus eq MasterCodeConstants.APP_STATUS_PEND_SUBMIT_SELF_ASSESSMENT}">
                                                                                <option value="/bsb-web/eservice/INTERNET/MohBsbSubmitSelfAssessment?appId=<iais:mask name='selfAssessAppId' value='${app.appId}'/>">Self-Assessment</option>
                                                                            </c:if>
                                                                            <c:if test="${AppWithdrawableJudge}">
                                                                                <option value="/bsb-web/eservice/INTERNET/BsbWithDrawn?withdrawnAppId=<iais:mask name='id' value='${app.appId}'/>&from=application">Withdraw</option>
                                                                            </c:if>
                                                                            <c:if test="${InsAppointmentJudge}">
                                                                                <option value="/bsb-web/eservice/INTERNET/ApplicantSubmitInspectionDate?appId=<iais:mask name='indicateInsDateAppId' value='${app.appId}'/>">Indicate Preferred Inspection Date</option>
                                                                            </c:if>
                                                                            <c:if test="${app.appType eq MasterCodeConstants.APP_TYPE_NEW and app.appStatus eq MasterCodeConstants.APP_STATUS_PEND_NC_RECTIFICATION}">
                                                                                <option value="/bsb-web/eservice/INTERNET/MohBsbRectifiesNCs?appId=<iais:mask name='ncAppId' value='${app.appId}'/>">Submit non-compliance action</option>
                                                                            </c:if>
                                                                            <c:if test="${InspectionFollowUpJudge}">
                                                                                <option value="/bsb-web/eservice/INTERNET/InspectionFollowUpItemsFE?appId=<iais:mask name='followUpAppId' value='${app.appId}'/>">Submit follow-up action</option>
                                                                            </c:if>
                                                                            <c:if test="${app.appStatus eq MasterCodeConstants.APP_STATUS_PEND_INPUT or app.appStatus eq MasterCodeConstants.APP_STATUS_PEND_CLARIFICATION}">
                                                                                <option value="/bsb-web/eservice/INTERNET/MohBsbRfi?appId=<iais:mask name='rfiAppId' value='${app.appId}'/>">Request For Information</option>
                                                                            </c:if>
                                                                            <c:if test="${AFCUploadReportJudge}">
                                                                                <option value="/bsb-web/eservice/INTERNET/InsAfcCertification?appId=<iais:mask name='afcCertReportAppId' value='${app.appId}'/>">Upload Certification Report</option>
                                                                            </c:if>
                                                                            <c:if test="${ApplicantUploadCertReportJudge}">
                                                                                <option value="/bsb-web/eservice/INTERNET/InsApplicantCertification?appId=<iais:mask name='applicantCertReportAppId' value='${app.appId}'/>">Upload Certification Report</option>
                                                                            </c:if>

                                                                        </select>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </c:otherwise>
                                            </c:choose>
                                            </tbody>
                                        </table>
                                        <!-- Modal -->
                                        <div class="modal fade" id="archiveModal" role="dialog" aria-labelledby="myModalLabel" style="left: 50%;top: 50%;transform: translate(-50%,-50%);min-width:80%; overflow: visible;bottom: inherit;right: inherit;">
                                            <div class="modal-dialog" role="document">
                                                <div class="modal-content">
                                                    <div class="modal-body">
                                                        <div class="row">
                                                            <div class="col-md-12"><span style="font-size: 2rem">Please select at least one record</span></div>
                                                        </div>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="button" class="btn btn-secondary btn-md" data-dismiss="modal">Close</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <iais:confirm msg="Are you sure you want to delete?" needFungDuoJi="false" popupOrder="deleteDraftModal" callBack="delDraftCancelBtn()" title=" " cancelFunc="delDraftYesBtn()" cancelBtnDesc="OK" yesBtnDesc="Cancel" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary"  />
                                        <iais:confirm msg="The draft application is deleted" needFungDuoJi="false" popupOrder="deleteDraftMessage"  title=" " callBack="delDraftMsgYesBtn()"  needCancel="false" />
                                        <input type="hidden" id="afterDeleteDraftApp" name="afterDeleteDraftApp" value="${AFTER_DELETE_DRAFT_APP}" readonly disabled/>
                                        <!--Modal End-->
                                        <div class="row" style="margin-top: 1.5%">
                                            <div class="col-md-12">
                                                <div class="col-md-6 pull-right">
                                                    <button type="button" class="btn btn-primary" id="doArchive" style="margin-right: 10px; display: none">Archive</button>
                                                    <button style="display: none" type="button" class="btn btn-primary pull-right" onclick="toArchiveView()">Access Archive</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>

                    </div>
                </div>
            </div>
        </div>
    </div>
</div>