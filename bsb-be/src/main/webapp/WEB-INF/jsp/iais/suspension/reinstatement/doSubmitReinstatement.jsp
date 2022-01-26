<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-suspension.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-file.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp" %>
<div class="dashboard">
    <form method="post" id="mainForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <div class="main-content">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="center-content">
                        <div class="intranet-content">
                            <iais:body>
                                <div class="col-xs-12">
                                    <div class="tab-gp dashboard-tab">
                                        <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                                            <li class="active" id="info" role="presentation">
                                                <a href="#tabInfo"
                                                   id="infoa"
                                                   aria-controls="tabInfo"
                                                   role="tab"
                                                   data-toggle="tab">Info</a></li>
                                            <li class="complete" id="document" role="presentation">
                                                <a href="#tabDocuments"
                                                   id="documenta"
                                                   aria-controls="tabDocuments" role="tab"
                                                   data-toggle="tab">Documents</a></li>
                                            <li class="incomplete" id="process" role="presentation">
                                                <a href="#tabProcessing"
                                                   id="processa"
                                                   aria-controls="tabProcessing" role="tab"
                                                   data-toggle="tab">Processing</a></li>
                                        </ul>
                                        <div class="tab-nav-mobile visible-xs visible-sm">
                                            <div class="swiper-wrapper" role="tablist">
                                                <div class="swiper-slide"><a href="#tabInfo" aria-controls="tabInfo"
                                                                             role="tab"
                                                                             data-toggle="tab">Info</a></div>
                                                <div class="swiper-slide"><a href="#tabDocuments" id="doDocument"
                                                                             aria-controls="tabDocuments"
                                                                             role="tab" data-toggle="tab">Documents</a></div>
                                                <div class="swiper-slide"><a href="#tabProcessing" id="doProcess"
                                                                             aria-controls="tabProcessing"
                                                                             role="tab" data-toggle="tab">Processing</a></div>
                                            </div>
                                            <div class="swiper-button-prev"></div>
                                            <div class="swiper-button-next"></div>
                                        </div>
                                        <div class="tab-content">
                                            <div class="tab-pane active" id="tabInfo" role="tabpanel">
                                                <%@include file="../facilityInfo.jsp" %>
                                            </div>
                                            <div class="tab-pane" id="tabDocuments" role="tabpanel">
                                                <%@include file="../tabDocuments.jsp" %>
                                            </div>
                                            <div class="tab-pane" id="tabProcessing" role="tabpanel">
                                                <br/><br/>
                                                <div class="alert alert-info" role="alert">
                                                    <h4>Process Status Update</h4>
                                                </div>
                                                <form method="post" action=<%=process.runtime.continueURL()%>>
                                                    <div class="row">
                                                        <div class="col-xs-12">
                                                            <div class="table-gp">
                                                                    <%--@elvariable id="suspensionReinstatementDto" type="sg.gov.moh.iais.egp.bsb.dto.suspension.SuspensionReinstatementDto"--%>
                                                                <iais:section title="">
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Active Approval No." required="false"/>
                                                                            <iais:value width="10">
                                                                                <p>${suspensionReinstatementDto.approvalNo}</p>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Facility Name" required="false"/>
                                                                            <iais:value width="10">
                                                                                <p>${suspensionReinstatementDto.facName}</p>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Facility Address" required="false"/>
                                                                            <iais:value width="10">
                                                                                <p>${suspensionReinstatementDto.facAddress}</p>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Facility Classification (Type)" required="false"/>
                                                                            <iais:value width="10">
                                                                                <iais:code code="${suspensionReinstatementDto.facClassification}"/>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Effective Date of Reinstatement" required="true"/>
                                                                            <iais:value width="10">
                                                                                <input type="text" autocomplete="off" name="effectiveDate" id="effectiveDate" data-date-start-date="01/01/1900" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control" value="${suspensionReinstatementDto.effectiveDate}"/>
                                                                                <span data-err-ind="effectiveDate" class="error-msg"></span>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Additional Comments (for Facility/AFC)" required="false"/>
                                                                            <iais:value width="10">
                                                                                <textarea id="additionalComments"
                                                                                          name="additionalComments"
                                                                                          cols="70"
                                                                                          rows="5"
                                                                                          maxlength="300">${suspensionReinstatementDto.additionalComments}</textarea>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Reason for Reinstatement (MOH Internal info)" required="true"/>
                                                                            <iais:value width="10">
                                                                                <textarea id="reinstatementReason"
                                                                                          name="reinstatementReason"
                                                                                          cols="70"
                                                                                          rows="5"
                                                                                          maxlength="1000">${suspensionReinstatementDto.reinstatementReason}</textarea>
                                                                                <span data-err-ind="reinstatementReason" class="error-msg"></span>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Remarks" width="15" required="false"/>
                                                                            <iais:value width="10">
                                                                                <textarea id="doRemarks"
                                                                                          name="doRemarks"
                                                                                          cols="70"
                                                                                          rows="5"
                                                                                          maxlength="300">${suspensionReinstatementDto.doRemarks}</textarea>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                </iais:section>
                                                                        <a class="back" href="${backUrl}"><em class="fa fa-angle-left"></em>Back</a>
                                                                <div style="text-align: right">
                                                                    <a href="/bsb-be/eservice/INTRANET/MohBsbTaskList" class="btn btn-secondary">Cancel</a>
                                                                    <button id="submitBtn" class="btn btn-primary">Submit</button>
                                                                </div>
                                                                <div>&nbsp;</div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </form>
                                                <%@include file="/WEB-INF/jsp/iais/common/processHistory.jsp" %>
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
</div>