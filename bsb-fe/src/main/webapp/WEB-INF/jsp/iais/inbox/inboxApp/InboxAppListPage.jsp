<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.ecq.com/iais-bsb" prefix="iais-bsb" %>
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

                    <div class="tab-content">
                        <form class="" method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
                            <div id="searchPanel" class="tab-search">

                                <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
                                <input type="hidden" name="action_type" value="">
                                <input type="hidden" name="action_value" value="">
                                <input type="hidden" name="action_additional" value="">


                                <%--@elvariable id="inboxAppSearchDto" type="sg.gov.moh.iais.egp.bsb.dto.inbox.InboxAppSearchDto"--%>
                                <div class="row">
                                    <label for="searchAppNo" class="col-sm-3 col-md-2 control-label">Application No.:</label>
                                    <div class="col-sm-7 col-md-5">
                                        <input type="text" id="searchAppNo" name="searchAppNo" value="${inboxAppSearchDto.searchAppNo}"/>
                                        <span data-err-ind="searchAppNo" class="error-msg"></span>
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 15px">
                                    <label for="searchProcessType" class="col-sm-3 col-md-2 control-label">Process Type:</label>
                                    <div class="col-sm-7 col-md-5">
                                        <select id="searchProcessType" name="searchProcessType">
                                            <option value='<c:out value=""/>' <c:if test="${inboxAppSearchDto.searchProcessType eq ''}">selected="selected"</c:if>>All</option>
                                            <%--@elvariable id="processTypeOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
                                            <c:forEach var="appStatusItem" items="${processTypeOps}">
                                                <option value='<c:out value="${appStatusItem.value}"/>' <c:if test="${inboxAppSearchDto.searchProcessType eq appStatusItem.value}">selected="selected"</c:if> ><c:out value="${appStatusItem.text}"/></option>
                                            </c:forEach>
                                        </select>
                                        <span data-err-ind="searchProcessType" class="error-msg"></span>
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 15px">
                                    <label for="searchStatus" class="col-sm-3 col-md-2 control-label">Status:</label>
                                    <div class="col-sm-7 col-md-5">
                                        <select id="searchStatus" name="searchStatus">
                                            <option value='<c:out value=""/>' <c:if test="${inboxAppSearchDto.searchStatus eq ''}">selected="selected"</c:if>>All</option>
                                            <%--@elvariable id="appStatusOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
                                            <c:forEach var="appStatusItem" items="${appStatusOps}">
                                                <option value='<c:out value="${appStatusItem.value}"/>' <c:if test="${inboxAppSearchDto.searchStatus eq appStatusItem.value}">selected="selected"</c:if> ><c:out value="${appStatusItem.text}"/></option>
                                            </c:forEach>
                                        </select>
                                        <span data-err-ind="searchStatus" class="error-msg"></span>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-xs-12 col-sm-6">
                                        <label class="col-xs-12 col-sm-4 control-label" style="padding-left: 0">Application Date</label>
                                        <div class="col-xs-12 col-sm-8">
                                            <iais:datePicker id="searchSubmissionDateFrom" name="searchSubmissionDateFrom" value="${inboxAppSearchDto.searchSubmissionDateFrom}"/>
                                        </div>
                                        <span data-err-ind="searchSubmissionDateFrom" class="error-msg"></span>
                                    </div>
                                    <div class="col-xs-12 col-sm-6">
                                        <label class="col-xs-2 col-sm-2 control-label" style="padding-left: 0">To</label>
                                        <div class="col-xs-12 col-sm-8">
                                            <iais:datePicker id="searchSubmissionDateTo" name="searchSubmissionDateTo" value="${inboxAppSearchDto.searchSubmissionDateTo}"/>
                                        </div>
                                        <span data-err-ind="searchSubmissionDateTo" class="error-msg"></span>
                                    </div>
                                </div>
                                <div class="row text-right text-center-mobile">
                                    <button class="btn btn-secondary" type="reset" id="clearBtn" name="clearBtn">Clear</button>
                                    <button class="btn btn-primary" type="button" id="searchBtn" name="searchBtn">Search</button>
                                </div>
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
                                                <iais:sortableHeader needSort="true" field="appType" value="Application Type" isFE="true" style="width:18%"/>
                                                <iais:sortableHeader needSort="true" field="processType" value="Process Type" isFE="true"/>
                                                <iais:sortableHeader needSort="true" field="status" value="Status" isFE="true"/>
                                                <iais:sortableHeader needSort="true" field="applicationDt" value="Date Submitted" isFE="true" style="width:18%"/>
                                                <iais:sortableHeader needSort="false" field="" value="Actions" isFE="true" style="width:17%"/>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:choose>
                                                <%--@elvariable id="dataList" type="java.util.List<sg.gov.moh.iais.egp.bsb.entity.Application>"--%>
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
                                                                    <c:when test="${app.appType eq 'BSBAPTY001' and app.processType eq 'PROTYPE001'}">
                                                                        <a href="/bsb-fe/eservice/INTERNET/MohBsbViewFacRegApplication?appId=<iais:mask name='id' value='${app.id}'/><c:if test="${app.status eq 'BSBAPST001'}">&editId=<iais:mask name='editId' value='${app.id}'/></c:if>"><c:out value="${app.applicationNo}"/></a>
                                                                    </c:when>
                                                                    <c:when test="${app.appType eq 'BSBAPTY001' and (app.processType eq 'PROTYPE002' or app.processType eq 'PROTYPE003' or app.processType eq 'PROTYPE004')}">
                                                                        <a href="/bsb-fe/eservice/INTERNET/MohViewApprovalPossessApplication?appId=<iais:mask name='id' value='${app.id}'/>&processType=${app.processType}<c:if test="${app.status eq 'BSBAPST001'}">&editId=<iais:mask name='editId' value='${app.id}'/></c:if>"><c:out value="${app.applicationNo}"/></a>
                                                                    </c:when>
                                                                    <c:when test="${app.appType eq 'BSBAPTY001' and app.processType eq 'PROTYPE005'}">
                                                                        <a href="/bsb-fe/eservice/INTERNET/MohBsbViewCertRegApplication?appId=<iais:mask name='id' value='${app.id}'/><c:if test="${app.status eq 'BSBAPST001'}">&editId=<iais:mask name='editId' value='${app.id}'/></c:if>"><c:out value="${app.applicationNo}"/></a>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <c:out value="${app.applicationNo}"/>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Application Type</p>
                                                                <p style="text-align: center"><iais:code code="${app.appType}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Process Type</p>
                                                                <p><iais:code code="${app.processType}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Status</p>
                                                                <p><iais:code code="${app.status}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Date Submitted</p>
                                                                <p><fmt:formatDate value="${app.applicationDt}" pattern="dd/MM/yyyy HH:mm:ss"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Actions</p>
                                                                <select id="appAction${status.index}" name="appAction${status.index}" data-action-select="">
                                                                    <option value="#" selected="selected">Select</option>
                                                                    <c:choose>
                                                                        <c:when test="${app.appType eq 'BSBAPTY001' and app.processType eq 'PROTYPE001' and app.status eq 'BSBAPST001'}">
                                                                            <option value="/bsb-fe/eservice/INTERNET/MohBsbFacilityRegistration?editId=<iais:mask name='editId' value='${app.id}'/>">Edit</option>
                                                                        </c:when>
                                                                        <c:when test="${app.appType eq 'BSBAPTY001' and app.processType eq 'PROTYPE001' and app.status eq 'BSBAPST011'}">
                                                                            <option value="/bsb-fe/eservice/INTERNET/MohBsbFacilityRegistration?editId=<iais:mask name='editId' value='${app.id}'/>">Continue</option>
                                                                        </c:when>
                                                                        <c:when test="${app.appType eq 'BSBAPTY005' and app.processType eq 'PROTYPE001' and app.status eq 'BSBAPST011'}">
                                                                            <option value="/bsb-fe/eservice/INTERNET/ApplicantDeRegistrationFacility?editId=<iais:mask name='editId' value='${app.id}'/>">Continue</option>
                                                                        </c:when>
                                                                        <c:when test="${app.appType eq 'BSBAPTY004' and (app.processType eq 'PROTYPE002' or app.processType eq 'PROTYPE003' or app.processType eq 'PROTYPE004') and app.status eq 'BSBAPST011'}">
                                                                            <option value="/bsb-fe/eservice/INTERNET/ApplicantCancellationApproval?editId=<iais:mask name='editId' value='${app.id}'/>">Continue</option>
                                                                        </c:when>
                                                                        <c:when test="${app.appType eq 'BSBAPTY005' and app.processType eq 'PROTYPE005' and app.status eq 'BSBAPST011'}">
                                                                            <option value="/bsb-fe/eservice/INTERNET/ApplicantDeRegistrationAFC?editId=<iais:mask name='editId' value='${app.id}'/>">Continue</option>
                                                                        </c:when>
                                                                        <c:when test="${app.appType eq 'BSBAPTY001' and (app.processType eq 'PROTYPE002' or app.processType eq 'PROTYPE003' or app.processType eq 'PROTYPE004') and app.status eq 'BSBAPST001'}">
                                                                            <option value="/bsb-fe/eservice/INTERNET/MohApprovalApplication?editId=<iais:mask name='editId' value='${app.id}'/>&processType=${app.processType}">Edit</option>
                                                                        </c:when>
                                                                        <c:when test="${app.appType eq 'BSBAPTY001' and app.processType eq 'PROTYPE005' and app.status eq 'BSBAPST001'}">
                                                                            <option value="/bsb-fe/eservice/INTERNET/MohFacilityCertifierRegistration?editId=<iais:mask name='editId' value='${app.id}'/>">Edit</option>
                                                                        </c:when>
                                                                        <c:when test="${app.processType eq 'PROTYPE006' and app.status eq 'BSBAPST011'}">
                                                                            <option value="/bsb-fe/eservice/INTERNET/JudgeDataSubmissionType?editId=<iais:mask name='editId' value='${app.id}'/>">Continue</option>
                                                                        </c:when>
                                                                        <c:when test="${app.processType eq 'PROTYPE008' and app.status eq 'BSBAPST011'}">
                                                                            <option value="/bsb-fe/eservice/INTERNET/IncidentCheckProcess?editId=<iais:mask name='editId' value='${app.id}'/>">Continue</option>
                                                                        </c:when>
                                                                    </c:choose>

                                                                    <c:if test="${app.status eq 'BSBAPST011'}">
                                                                        <option value="deleteDraft<iais:mask name='deleteId' value='${app.id}'/>">Delete</option>
                                                                    </c:if>

                                                                    <c:if test="${app.processType eq 'PROTYPE001' and app.status eq 'BSBAPST022'}">
                                                                        <option value="/bsb-fe/eservice/INTERNET/MohBsbSubmitSelfAssessment?appId=<iais:mask name='selfAssessAppId' value='${app.id}'/>">Self-Assessment</option>
                                                                    </c:if>

                                                                    <%-- This is actually an option of RFI, we need to use a standalone process to handle it and diapatch request to diffirent processes.
                                                                         Currently, we just route to comment inspection report page for test --%>
                                                                    <c:if test="${app.processType eq 'PROTYPE001' and app.status eq 'BSBAPST004'}">
                                                                        <option value="/bsb-fe/eservice/INTERNET/MohBsbCommentInspectionReport?appId=<iais:mask name='commentInsReportAppId' value='${app.id}'/>">Comment Inspection Report</option>
                                                                    </c:if>

                                                                        <%--The application will be in a “non-approved” or “non-rejected” stage and may be one of the following: New / Renewal / Request for Change / Deregistration / Cancellation / Notification/ Data Submission.--%>
                                                                    <c:if test="${(app.appType eq 'BSBAPTY001' or app.appType eq 'BSBAPTY002' or app.appType eq 'BSBAPTY003' or app.appType eq 'BSBAPTY004' or app.appType eq 'BSBAPTY005' or app.appType eq 'BSBAPTY010') and (app.status ne 'BSBAPST007' and app.status ne 'BSBAPST008' and app.status ne 'BSBAPST009' and app.status ne 'BSBAPST011' and app.status ne 'BSBAPST012')}">
                                                                        <option value="/bsb-fe/eservice/INTERNET/BsbWithDrawn?withdrawnAppId=<iais:mask name='id' value='${app.id}'/>&from=application">Withdraw</option>
                                                                    </c:if>
                                                                </select>
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