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
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-withdrawn.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-file.js"></script>
<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp" %>
<div class="dashboard">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
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
                                                <%@include file="facilityInfo.jsp" %>
                                            </div>
                                            <div class="tab-pane" id="tabDocuments" role="tabpanel">
                                                <%@include file="tabDocuments.jsp" %>
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
                                                                    <%--@elvariable id="withdrawnDto" type="sg.gov.moh.iais.egp.bsb.dto.withdrawn.AppSubmitWithdrawnDto"--%>
                                                                <iais:section title="">
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Current Status" required="false"/>
                                                                            <iais:value width="10">
                                                                                <p><iais:code code="${withdrawnDto.currentStatus}"/></p>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="DO Remarks" width="15" required="false"/>
                                                                            <iais:value width="10">
                                                                                <p><c:out value="${withdrawnDto.doRemarks}"/></p>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="DO Decision" width="15" required="false"/>
                                                                            <iais:value width="10">
                                                                                <p><iais:code code="${withdrawnDto.doDecision}"/></p>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="AO Remarks" width="15" required="false"/>
                                                                            <iais:value width="10">
                                                                                <textarea id="aoRemarks"
                                                                                          name="aoRemarks"
                                                                                          cols="70"
                                                                                          rows="5"
                                                                                          maxlength="300">${withdrawnDto.aoRemarks}</textarea>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div id="processingDecision">
                                                                        <iais:row>
                                                                            <iais:field value="Processing Decision" required="true"/>
                                                                            <iais:value width="10">
                                                                                <select name="aoDecision" id="aoDecision">
                                                                                    <option value="">Please Select</option>
                                                                                    <option value="MOHPRO007" <c:if test="${withdrawnDto.aoDecision eq 'MOHPRO007'}">selected = 'selected'</c:if>>Approve</option>
                                                                                    <option value="MOHPRO003" <c:if test="${withdrawnDto.aoDecision eq 'MOHPRO003'}">selected = 'selected'</c:if>>Reject</option>
                                                                                </select>
                                                                                <span data-err-ind="aoDecision" class="error-msg"></span>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                </iais:section>
                                                                <a style="float:left;padding-top: 1.1%;" class="back" href="/bsb-be/eservicecontinue/INTRANET/MohBsbTaskList"><em class="fa fa-angle-left"></em> Back</a>
                                                                <div style="text-align: right">
                                                                    <button name="submitBtn" id="submitBtn" type="button" class="btn btn-primary">Submit</button>
                                                                </div>
                                                                <div>&nbsp;</div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </form>
                                                <%@include file="/WEB-INF/jsp/iais/routingHistory/processHistory.jsp" %>
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