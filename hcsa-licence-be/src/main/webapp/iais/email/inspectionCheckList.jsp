<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <br>
    <br>
    <br>
    <br>
    <br>
    <input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.FillupChklistDelegator"/>
    <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.CheckListVadlidateDto"/>
    <input type="hidden" name="valProfiles" id="valProfiles" value=""/>
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp dashboard-tab">

                        <%@ include file="./navTabs.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane" id="tabInfo" role="tabpanel">

                                <div class="panel panel-default">
                                    <!-- Default panel contents -->
                                    <div class="panel-heading"><b>Submission Details</b></div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table table-bordered">
                                                    <tbody>
                                                    <tr>
                                                        <td class="col-xs-6" align="right">Application No. (Overall)</td>
                                                        <td class="col-xs-6">${applicationViewDto.applicationDto.applicationNo}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Application No.</td>
                                                        <td>${applicationViewDto.applicationNoOverAll}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Application Type</td>
                                                        <td>${applicationViewDto.applicationDto.applicationType}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Service Type</td>
                                                        <td>${applicationViewDto.applicationDto.serviceId}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Submission Date</td>
                                                        <td>${applicationViewDto.submissionDate}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Current Status</td>
                                                        <td>${applicationViewDto.currentStatus}</td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div align="center">
                                    <button type="button" class="btn btn-primary">
                                        View Application
                                    </button>
                                </div>
                                <div>&nbsp</div>
                                <div class="panel panel-default">
                                    <div class="panel-heading"><b>Applicant Details</b></div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table table-bordered">
                                                    <tbody>
                                                    <tr>
                                                        <td class="col-xs-6" align="right">HCI Code</td>
                                                        <td class="col-xs-6">-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">HCI Name</td>
                                                        <td>${applicationViewDto.hciName}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">HCI ADDRESS</td>
                                                        <td>${applicationViewDto.hciAddress}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Telephone</td>
                                                        <td>${applicationViewDto.telephone}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Fax</td>
                                                        <td>-</td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="tab-pane" id="tabDocuments" role="tabpanel">
                                <div class="alert alert-info" role="alert"><b>
                                    <h4>Supporting Document</h4>
                                </b></div>
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
                                                    <th>Document</th>
                                                    <th>File</th>
                                                    <th>Size</th>
                                                    <th>Submitted By</th>
                                                    <th>Date Submitted</th>
                                                </tr>
                                                </thead>

                                                <tbody>
                                                <c:forEach items="${applicationViewDto.appSupDocDtoList}" var="appSupDocDto">
                                                    <tr>
                                                        <td>
                                                            <p><c:out value="${appSupDocDto.document}"></c:out></p>
                                                        </td>
                                                        <td>
                                                            <p><a href="#"><c:out value="${appSupDocDto.file}"></c:out></a></p>
                                                        </td>
                                                        <td>
                                                            <p><c:out value="${appSupDocDto.size}"></c:out></p>
                                                        </td>
                                                        <td>
                                                            <p><c:out value="${appSupDocDto.submittedBy}"></c:out></p>
                                                        </td>
                                                        <td>
                                                            <p><c:out value="${appSupDocDto.dateSubmitted}"></c:out></p>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>

                                            </table>
                                            <div class="alert alert-info" role="alert"><strong>
                                                <h4>Internal Document</h4>
                                            </strong></div>
                                            <div  class="text ">
                                                <p><span>These are documents uploaded by an agency officer to support back office processing.</span></p>
                                            </div>
                                            <table class="table">
                                                <thead>
                                                <tr>
                                                    <th>Document</th>
                                                    <th>File</th>
                                                    <th>Size</th>
                                                    <th>Submitted By</th>
                                                    <th>Date Submitted</th>
                                                    <th>Action</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr>
                                                    <td colspan="6" align="center">
                                                        <p>No record found.</p>
                                                    </td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>

                            </div>
                            <div class="tab-pane" id="tabPayment" role="tabpanel">
                                <div class="alert alert-info" role="alert"><strong>
                                    <h4>Payment Details</h4>
                                </strong></div>
                                <div class="row">
                                    <div class="col-xs-12">
                                        <div class="table-gp">
                                            <table class="table">
                                                <thead>
                                                <tr>
                                                    <th>Payment</th>
                                                    <th>Amount</th>
                                                    <th>Date</th>
                                                    <th>Status</th>
                                                    <th>Reference No.</th>
                                                    <th>Payment Type</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr>
                                                    <td>
                                                        <p>New Licence Application</p>
                                                    </td>
                                                    <td>
                                                        <p>S$400.00</p>
                                                    </td>
                                                    <td>
                                                        <p>12-Dec-2018</p>
                                                    </td>
                                                    <td>
                                                        <p>success</p>
                                                    </td>
                                                    <td>
                                                        <p>TRANS-201812000013</p>
                                                    </td>
                                                    <td>
                                                        <p>Credit Card</p>
                                                    </td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="tab-pane" id="tabInspection" role="tabpanel">
                                <div class="alert alert-info" role="alert">
                                    <strong>
                                        <h4>Inspection Findings</h4>
                                    </strong>
                                </div>
                                <div class="text ">
                                    <p><span><strong>Part I: Inspection Checklist</strong></span></p>
                                </div>
                                <div class="row">
                                    <div class="col-xs-12">
                                        <div class="table-gp">
                                            <table class="table">
                                                <thead>
                                                <tr>
                                                    <th class="col-xs-2"><span>Checklist</span></th>
                                                    <th class="col-xs-10"><span>Interviewed</span></th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr>
                                                    <td>
                                                        <p>Radiological Service</p>
                                                    </td>
                                                    <td>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p>Angiography</p>
                                                    </td>
                                                    <td>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p>Mammography</p>
                                                    </td>
                                                    <td>
                                                    </td>
                                                </tr>
                                                </tbody>
                                            </table>
                                            <div class="text ">
                                                <p><span><strong>Part II: Findings</strong></span></p>
                                            </div>
                                            <table class="table">
                                                <tr>
                                                    <td class="col-xs-2">
                                                        <p>Licence Type</p>
                                                    </td>
                                                    <td class="col-xs-10">
                                                        <p>Radiological Service</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p>Findings/Remarks</p>
                                                    </td>
                                                    <td>
                                                        <p>The clinic offers in-house laboratory services</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p>Others</p>
                                                    </td>
                                                    <td>
                                                        <p>Refer to ack. no. 180911007711 for x-ray laboratory licence (ultrasound only). At the time of
                                                            inspection, LIA Br was still awaiting clarification on the deputy manager's qualifications and was
                                                            unable to complete the inspection of the ultrasound facility as the sonographer / radiographer and
                                                            radiologist were not on-site. The ultrasound room, N2 licence (N2/06421/001) and preventive maintenance
                                                            records for the ultrasound machine were in place. The ultrasound procedure is limited to abdominal area
                                                            only</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p>Status</p>
                                                    </td>
                                                    <td>
                                                        <p>Full Compliance</p>
                                                    </td>
                                                </tr>
                                            </table>
                                            <div class="text ">
                                                <p><span><strong>Part III: Inspectors</strong></span></p>
                                            </div>
                                            <table class="table">
                                                <tr>
                                                    <td class="col-xs-2">
                                                        <p>Inspected By</p>
                                                    </td>
                                                    <td class="col-xs-10">
                                                        <p>Jenny</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p>Other Inspection Officer</p>
                                                    </td>
                                                    <td>
                                                        <p>-</p>
                                                    </td>
                                                </tr>
                                            </table>
                                            <div class="text ">
                                                <p><span><strong>Part IV: Report</strong></span></p>
                                            </div>
                                            <table class="table">
                                                <tr>
                                                    <td class="col-xs-2">
                                                        <p>Reported By</p>
                                                    </td>
                                                    <td class="col-xs-10">
                                                        <p>Jenny</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p>Report Noted By</p>
                                                    </td>
                                                    <td>
                                                        <p>Steven</p>
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                                <div class="alert alert-info" role="alert">
                                    <strong>
                                        <h4>Recommendations</h4>
                                    </strong>
                                </div>
                                <div class="row">
                                    <div class="col-xs-12">
                                        <div class="table-gp">
                                            <table class="table">
                                                <tr>
                                                    <td class="col-xs-2">
                                                        <p>Recommendation</p>
                                                    </td>
                                                    <td class="col-xs-10">
                                                        <p>2 Years</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p>Other Remarks</p>
                                                    </td>
                                                    <td>
                                                        <p>-</p>
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                                <div class="alert alert-info" role="alert">
                                    <strong>
                                        <h4>Follow up actions</h4>
                                    </strong>
                                </div>
                                <div class="row">
                                    <div class="col-xs-12">
                                        <div class="table-gp">
                                            <table class="table">
                                                <tr>
                                                    <td class="col-xs-2">
                                                        <p>Follow up actions</p>
                                                    </td>
                                                    <td class="col-xs-10">
                                                        <p>-</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p>Other</p>
                                                    </td>
                                                    <td>
                                                        <p>-</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p>Rectification</p>
                                                    </td>
                                                    <td>
                                                        <p>-</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p>Other</p>
                                                    </td>
                                                    <td>
                                                        <p>-</p>
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                                <div class="alert alert-info" role="alert">
                                    <strong>
                                        <h4>Rectification</h4>
                                    </strong>
                                </div>
                                <div class="row">
                                    <div class="col-xs-12">
                                        <div class="table-gp">
                                            <table class="table">
                                                <tr>
                                                    <td class="col-xs-2">
                                                        <p>Rectifications</p>
                                                    </td>
                                                    <td class="col-xs-10">
                                                        <p>N.A</p>
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="tab-pane active" id="tabCheckList" role="tabpanel">
                                <div class="alert alert-info" role="alert">
                                    <strong>
                                        <h4>Processing Status Update</h4>
                                    </strong>
                                </div>
                                <div class="row">
                                    <div class="col-xs-12">

                                        <h3>Common</h3>
                                        <div class="table-gp">
                                            <c:forEach var ="section" items ="${commonDto.sectionDtoList}">
                                                <br/>
                                                <h4><c:out value="${section.sectionName}"></c:out></h4>
                                                <table class="table">
                                                    <thead>
                                                    <tr>
                                                        <th>No.</th>
                                                        <th>Regulation Clause Number</th>
                                                        <th>Item</th>
                                                        <th>Yes</th>
                                                        <th>No</th>
                                                        <th>N/A</th>
                                                        <th>Remark</th>
                                                        <th>Rectified</th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    <c:forEach var = "item" items = "${section.itemDtoList}" varStatus="status">
                                                        <tr>
                                                            <td class="row_no">${(status.index + 1) }</td>
                                                            <td>${item.incqDto.regClauseNo}</td>
                                                            <td>${item.incqDto.checklistItem}</td>
                                                            <c:set value = "${item.incqDto.sectionName}${item.incqDto.itemId}" var = "ckkId"/>
                                                            <td><input name="<c:out value="${item.incqDto.sectionName}"/><c:out value="${item.incqDto.itemId}"/>comrad" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionName}"/>comitemCheckboxYes" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'Yes'}">checked</c:if> value="Yes" /></td>
                                                            <td>
                                                                <input name="<c:out value="${item.incqDto.sectionName}"/><c:out value="${item.incqDto.itemId}"/>comrad" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionName}"/>comitemCheckboxNo"  onclick="showCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'No'}">checked</c:if> value="No" />
                                                            </td>
                                                            <td><input name="<c:out value="${item.incqDto.sectionName}"/><c:out value="${item.incqDto.itemId}"/>comrad" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionName}"/>comitemCheckboxNa" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'N/A'}">checked</c:if> value="N/A" /></td>
                                                            <td><input name="<c:out value="${item.incqDto.sectionName}"/><c:out value="${item.incqDto.itemId}"/>comremark" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionName}"/>comitemCheckboxRemark" type="text" value="<c:out value="${item.incqDto.remark}"/>" /></td>
                                                            <td>
                                                                <div id="<c:out value="${item.incqDto.sectionName}"/><c:out value="${item.incqDto.itemId}"/>comck"   <c:if test="${item.incqDto.chkanswer != 'No'}">hidden</c:if>>
                                                                    <input name="<c:out value="${item.incqDto.sectionName}"/><c:out value="${item.incqDto.itemId}"/>comrec" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionName}"/>comrec" type="checkbox" <c:if test="${item.incqDto.rectified}">checked</c:if> value="rec"/>
                                                                </div>
                                                                <c:set value = "error_${item.incqDto.sectionName}${item.incqDto.itemId}com" var = "err"/>
                                                                <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                    </tbody>
                                                </table>
                                            </c:forEach>
                                        </div>


                                        <c:forEach var ="cdto" items ="${serListDto.fdtoList}" varStatus="status">
                                            <h3>${cdto.svcName}</h3>
                                            <div class="table-gp">
                                                <c:forEach var ="section" items ="${cdto.sectionDtoList}">
                                                    <br/>
                                                    <h4><c:out value="${section.sectionName}"></c:out></h4>
                                                    <table class="table">
                                                        <thead>
                                                        <tr>
                                                            <th>No.</th>
                                                            <th>Regulation Clause Number</th>
                                                            <th>Item</th>
                                                            <th>Yes</th>
                                                            <th>No</th>
                                                            <th>N/A</th>
                                                            <th>Remark</th>
                                                            <th>Rectified</th>
                                                        </tr>
                                                        </thead>
                                                        <tbody>
                                                        <c:forEach var = "item" items = "${section.itemDtoList}" varStatus="status">
                                                            <tr>
                                                                <td class="row_no">${(status.index + 1) }</td>
                                                                <td>${item.incqDto.regClauseNo}</td>
                                                                <td>${item.incqDto.checklistItem}</td>
                                                                <c:set value = "${cdto.svcCode}${item.incqDto.sectionName}${item.incqDto.itemId}" var = "ckkId"/>
                                                                <td><input name="<c:out value="${cdto.svcCode}"/><c:out value="${item.incqDto.sectionName}"/><c:out value="${item.incqDto.itemId}"/>rad" id="<c:out value="${cdto.svcCode}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionName}"/>itemCheckboxYes" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'Yes'}">checked</c:if> value="Yes" /></td>
                                                                <td>
                                                                    <input name="<c:out value="${cdto.svcCode}"/><c:out value="${item.incqDto.sectionName}"/><c:out value="${item.incqDto.itemId}"/>rad" id="<c:out value="${cdto.svcCode}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionName}"/>itemCheckboxNo"  onclick="showCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'No'}">checked</c:if> value="No" />
                                                                </td>
                                                                <td><input name="<c:out value="${cdto.svcCode}"/><c:out value="${item.incqDto.sectionName}"/><c:out value="${item.incqDto.itemId}"/>rad" id="<c:out value="${cdto.svcCode}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionName}"/>itemCheckboxNa" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'N/A'}">checked</c:if> value="N/A" /></td>
                                                                <td><input name="<c:out value="${cdto.svcCode}"/><c:out value="${item.incqDto.sectionName}"/><c:out value="${item.incqDto.itemId}"/>remark" id="<c:out value="${cdto.svcCode}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionName}"/>itemCheckboxRemark" type="text" value="<c:out value="${item.incqDto.remark}"/>" /></td>
                                                                <td>
                                                                    <div id="<c:out value="${cdto.svcCode}"/><c:out value="${item.incqDto.sectionName}"/><c:out value="${item.incqDto.itemId}"/>ck"   <c:if test="${item.incqDto.chkanswer != 'No'}">hidden</c:if>>
                                                                        <input name="<c:out value="${cdto.svcCode}"/><c:out value="${item.incqDto.sectionName}"/><c:out value="${item.incqDto.itemId}"/>rec" id="<c:out value="${cdto.svcCode}${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionName}"/>rec" type="checkbox" <c:if test="${item.incqDto.rectified}">checked</c:if> value="rec"/>
                                                                    </div>
                                                                    <c:set value = "error_${cdto.svcCode}${item.incqDto.sectionName}${item.incqDto.itemId}" var = "err"/>
                                                                    <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                        </tbody>
                                                    </table>
                                                </c:forEach>
                                            </div>
                                        </c:forEach>
                                        <div class="table-gp">
                                            <h3>Adhoc</h3>
                                            <br/>
                                            <h4></h4>
                                            <table class="table">
                                                <thead>
                                                <tr>
                                                    <th>No.</th>
                                                    <th>Regulation Clause Number</th>
                                                    <th>Item</th>
                                                    <th>Yes</th>
                                                    <th>No</th>
                                                    <th>N/A</th>
                                                    <th>Remark</th>
                                                    <th>Rectified</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach var = "item" items = "${adchklDto.adItemList}" varStatus="status">
                                                    <tr>
                                                        <td class="row_no">${(status.index + 1) }</td>
                                                        <td></td>
                                                        <td><c:out value="${item.question}"/></td>
                                                        <c:set value = "${item.id}" var = "ckkId"/>
                                                        <td><input name="<c:out value="${item.id}"/>adhocrad" id="<c:out value="${item.id}"/>adhocitemCheckboxYes" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.adAnswer eq'Yes'}">checked</c:if> value="Yes" /></td>
                                                        <td>
                                                            <input name="<c:out value="${item.id}"/>adhocrad" id="<c:out value="${item.id}"/>adhocitemCheckboxNo"  onclick="showCheckBox('${ckkId}')" type="radio" <c:if test="${item.adAnswer eq'No'}">checked</c:if> value="No" />
                                                        </td>
                                                        <td><input name="<c:out value="${item.id}"/>adhocrad" id="<c:out value="${item.id}"/>adhocitemCheckboxNa" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.adAnswer eq'N/A'}">checked</c:if> value="N/A" /></td>
                                                        <td><input name="<c:out value="${item.id}"/>adhocremark" id="<c:out value="${item.id}"/>adhocitemCheckboxRemark" type="text" value="<c:out value="${item.remark}"/>" /></td>
                                                        <td>
                                                            <div id="<c:out value="${item.id}"/>ck"<c:if test="${item.adAnswer != 'No'}">hidden</c:if>>
                                                                <input name="<c:out value="${item.id}"/>adhocrec" id="<c:out value="${item.id}"/>adhocrec" type="checkbox" <c:if test="${item.rectified}">checked</c:if> value="rec"/>
                                                            </div>
                                                            <c:set value = "error_${item.id}adhoc" var = "err"/>
                                                            <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>


                                        <div class="col-xs-12">
                                            <div class="input-group">
                                                <div class="ax_default text_area">
                                                    <h4>Best Practice</h4>
                                                    <textarea cols="70" rows="7" name="bestpractice" id="bestpractice"><c:out value="${serListDto.bestPractice}"></c:out></textarea>
                                                    <span class="error-msg" id="error_bestPractice" name="iaisErrorMsg"></span>
                                                </div>
                                            </div>
                                            <div class="input-group">
                                                <div class="ax_default text_area">
                                                    <h4>TCU Date</h4> &nbsp;<iais:datePicker id = "tuc" name = "tuc" value="${serListDto.tuc}"></iais:datePicker><br>
                                                    <span class="error-msg" id="error_tcuDate" name="iaisErrorMsg"></span>
                                                </div>
                                            </div>
                                            <div class="input-group">
                                                <div class="ax_default text_area">
                                                    <h4>Remark</h4> <textarea cols="70" rows="7" name="tcuRemark" id="tcuRemark"><c:out value="${serListDto.tcuRemark}"></c:out></textarea>
                                                </div>
                                            </div>
                                        </div>

                                        <div align="right">
                                            <button type="button" class="btn btn-primary" onclick="javascript: doNext();">
                                                Save
                                            </button>
                                        </div>
                                        <input type="hidden" name = "saveflag" id="saveflag"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<%@ include file="/include/validation.jsp" %>

<script type="text/javascript">
    function doNext(){
        $("#saveflag").val("save");
        SOP.Crud.cfxSubmit("mainForm", "checkList");
    }
    function showCheckBox(str){
        var name = str;
        var divId = str+'ck';
        var comId = str+'comck'
        var comdivck =document.getElementById(divId);
        var divck =document.getElementById(comId);
        $("#"+divId).show();
        $("#"+comId).show();
    }
    function hideCheckBox(str){
        var name = str;
        var divId = str+'ck';
        var comdivId = str+'comck';
        var divck =document.getElementById(divId);
        var comdivck =document.getElementById(comdivId);
        $("#"+divId).hide();
        $("#"+comdivId).hide();

    }
</script>