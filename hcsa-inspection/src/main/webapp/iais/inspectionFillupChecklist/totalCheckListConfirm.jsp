<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp dashboard-tab">
                        <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                            <li class="active" role="presentation"><a href="#tabInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Info</a></li>
                            <li class="complete" role="presentation"><a href="#tabDocuments" aria-controls="tabDocuments" role="tab"
                                                                        data-toggle="tab">Documents</a></li>
                            <li class="complete" role="presentation"><a href="#tabPayment" aria-controls="tabPayment" role="tab"
                                                                        data-toggle="tab">Payment</a></li>
                        </ul>
                        <div class="tab-nav-mobile visible-xs visible-sm">
                            <div class="swiper-wrapper" role="tablist">
                                <div class="swiper-slide"><a href="#tabInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Info</a></div>
                                <div class="swiper-slide"><a href="#tabDocuments" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a></div>
                                <div class="swiper-slide"><a href="#tabPayment" aria-controls="tabPayment" role="tab" data-toggle="tab">Payment</a></div>
                            </div>
                            <div class="swiper-button-prev"></div>
                            <div class="swiper-button-next"></div>
                        </div>

                        <div class="tab-content">
                            <div class="tab-pane active" id="tabInfo" role="tabpanel">

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
                                                        <td>${applicationViewDto.applicationDto.status}</td>
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
                                                    <th>No.</th>
                                                    <th>Regulation Clause Number</th>
                                                    <th>Item</th>
                                                    <th>Yes</th>
                                                    <th>No</th>
                                                    <th>N/A</th>
                                                    <th>Remark</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach var = "item" items = "${fillCheckListDto.checkList}" >
                                                    <tr>
                                                        <td>${item.regClauseNo}</td>
                                                        <td>${item.checklistItem}</td>
                                                        <td><input name="itemCheckboxYes" id="itemCheckboxYes" type="checkbox" value="Yes" /></td>
                                                        <td><input name="itemCheckboxNo" id="itemCheckboxNo" type="checkbox" value="No" /></td>
                                                        <td><input name="itemCheckboxNa" id="itemCheckboxNa" type="checkbox" value="N/A" /></td>
                                                        <td><input name="itemCheckboxRemark" id="itemCheckboxRemark" type="" value="" /></td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                            <div class="input-group">
                                                <div class="ax_default text_area">
                                                    <textarea cols="70" rows="7"></textarea>
                                                </div>
                                            </div>
                                            <select>
                                                <c:forEach items="${applicationViewDto.hcsaSvcRoutingStageDtoList}" var="hcsaSvcRoutingStageDtoList">
                                                    <option value="${hcsaSvcRoutingStageDtoList.stageCode}">${hcsaSvcRoutingStageDtoList.stageName}</option>
                                                </c:forEach>
                                            </select>
                                            <div align="center">
                                                <button type="button" class="btn btn-primary" onclick="javascript: doSubmit();">
                                                    Submit
                                                </button>
                                            </div>
                                            <div align="center">
                                                <button type="button" class="btn btn-primary" onclick="javascript: doBack();">
                                                    Back
                                                </button>
                                            </div>
                                        </div>
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

<script type="text/javascript">
    function doSubmit(){
        SOP.Crud.cfxSubmit("mainForm", "submit");
    }
    function doBack(){
        SOP.Crud.cfxSubmit("mainForm", "back");
    }

</script>



