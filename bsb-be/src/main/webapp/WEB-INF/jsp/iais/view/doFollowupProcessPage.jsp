<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-report-event.js"></script>

<div class="dashboard">
    <form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
        <%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp" %>
        <input type="hidden" name="action_type" value="">
        <input type="hidden" name="action_value" value="">
        <input type="hidden" name="action_additional" value="">
        <div class="main-content">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="center-content">
                        <div class="intranet-content">
                            <iais:body>
                                <div class="col-md-12">
                                    <div class="tab-gp dashboard-tab">
                                        <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                                            <li class="active" id="info" role="presentation">
                                                <a href="#tabInfo" id="doInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Info</a>
                                            </li>
                                            <li id="personnel" role="presentation">
                                                <a href="#tabPersonnel" id="doPersonnel" aria-controls="tabPersonnel" role="tab" data-toggle="tab">Biosafety Personnel</a>
                                            </li>
                                            <li id="bat" role="presentation">
                                                <a href="#tabBat" id="doBat" aria-controls="tabBat" role="tab" data-toggle="tab">BA/T</a>
                                            </li>
                                            <li id="documents" role="presentation">
                                                <a href="#tabDocuments" id="doDocument" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a>
                                            </li>
                                            <li id="process" role="presentation">
                                                <a href="#tabProcessing" id="doProcess" aria-controls="tabProcessing" role="tab" data-toggle="tab">Processing</a>
                                            </li>
                                        </ul>
                                        <div class="tab-nav-mobile visible-xs visible-sm">
                                            <div class="swiper-wrapper" role="tablist">
                                                <div class="swiper-slide">
                                                    <a href="#tabInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Info</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#tabPersonnel" aria-controls="tabPersonnel" role="tab" data-toggle="tab">Biosafety Personnel</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#tabBat" aria-controls="tabBat" role="tab" data-toggle="tab">BA/T</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#tabDocuments" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a>
                                                </div>
                                                <div class="swiper-slide">
                                                    <a href="#tabProcessing" aria-controls="tabProcessing" role="tab" data-toggle="tab">Processing</a>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="tab-content">
                                            <div class="tab-pane active" id="tabInfo" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/view/common/applicationInfo.jsp" %>
                                            </div>
                                            <div class="tab-pane" id="tabPersonnel" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/view/common/biosafetyPersonnel.jsp"%>
                                            </div>
                                            <div class="tab-pane" id="tabBat" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/view/common/batPage.jsp"%>
                                            </div>
                                            <div class="tab-pane" id="tabDocuments" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/doDocument/tabDocuments.jsp"%>
                                            </div>
                                            <div class="tab-pane" id="tabProcessing" role="tabpanel">
                                                <div class="alert alert-info" role="alert" style="margin-top: 15px">
                                                    <h4>Processing Status Update</h4>
                                                </div>
                                                <div class="row" style="margin: 20px 0">
                                                    <div class="col-xs-12">
                                                        <div class="form-horizontal">
                                                            <c:forEach var="item" items="${processDto.followupNoteDtoList}">
                                                                <div class="form-group ">
                                                                    <div class="col-sm-5 control-label">
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <label class="label-normal"><c:out value="${item.addNoteTime}"/>&nbsp;&nbsp;&nbsp;&nbsp;<c:out value="${item.noteInfo}"/></label>
                                                                    </div>
                                                                </div>
                                                            </c:forEach>

                                                            <c:forEach var="item" items="${followupDto.newFollowupNotes}">
                                                                <div class="form-group ">
                                                                    <div class="col-sm-5 control-label">
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <label class="label-normal"><c:out value="${item.addNoteTime}"/>&nbsp;&nbsp;&nbsp;&nbsp;<c:out value="${item.newNote}"/></label>
                                                                    </div>
                                                                </div>
                                                            </c:forEach>

                                                            <div class="form-group ">
                                                                <div class="col-sm-5 control-label">
                                                                    <label for="note">Remarks from MOH</label>
                                                                </div>
                                                                <div class="col-sm-6 col-md-7">
                                                                    <textarea autocomplete="off" class="col-xs-12" name="note" id="note" maxlength="1000" style="width: 100%"><c:out value="${dto.remarks}"/></textarea>
                                                                </div>
                                                            </div>
                                                            <div class = "row">
                                                                <a style=" float:left;padding-top: 1.1%;text-decoration:none;" id="back" href="/bsb-be/eservicecontinue/INTRANET/MohBsbTaskList"><em class="fa fa-angle-left"> </em> Back</a>
                                                                <button class="btn btn-primary" type="button" id="addNote" style="float: right">Add Note</button>
                                                                <button class="btn btn-primary" type="button" id="close" style="float: right">Close Report</button>
                                                            </div>
                                                            <div class="modal fade" id="submitCloseModal" role="dialog" aria-labelledby="myModalLabel">
                                                                <div class="modal-dialog modal-dialog-centered" role="document">
                                                                    <div class="modal-content">
                                                                        <div class="modal-body">
                                                                            <div class="row">
                                                                                <div class="col-md-12"><span style="font-size: 2rem">Are you sure all the follow-ups have been satisfactorily completed</span></div>
                                                                            </div>
                                                                        </div>
                                                                        <div class="modal-footer">
                                                                            <button type="button" class="btn btn-secondary btn-md" data-dismiss="modal">No</button>
                                                                            <button type="button" class="btn btn-secondary btn-md" onclick="closeNote()">Yes</button>
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
                            </iais:body>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
    <%@include file="/WEB-INF/jsp/iais/doDocument/internalFileUploadModal.jsp"%>
</div>