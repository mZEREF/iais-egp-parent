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
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-audit.js"></script>
<div class="dashboard">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input type="hidden" name="action_type" value="">
        <input type="hidden" name="action_value" value="">
        <input type="hidden" name="action_additional" value="">
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
                                                   data-toggle="tab">Info</a>
                                            </li>
                                            <li class="incomplete" id="process" role="presentation">
                                                <a href="#tabProcessing" id="processa"
                                                   aria-controls="tabProcessing" role="tab"
                                                   data-toggle="tab">Processing</a></li>
                                        </ul>
                                        <div class="tab-nav-mobile visible-xs visible-sm">
                                            <div class="swiper-wrapper" role="tablist">
                                                <div class="swiper-slide"><a href="#tabInfo" aria-controls="tabInfo"
                                                                             role="tab"
                                                                             data-toggle="tab">Info</a></div>
                                                <div class="swiper-slide"><a href="#tabProcessing" id="doProcess"
                                                                             aria-controls="tabProcessing"
                                                                             role="tab" data-toggle="tab">Processing</a>
                                                </div>
                                            </div>
                                            <div class="swiper-button-prev"></div>
                                            <div class="swiper-button-next"></div>
                                        </div>
                                        <div class="tab-content">
                                            <div class="tab-pane active" id="tabInfo" role="tabpanel">
                                                <%@include file="facilityInfo.jsp" %>
                                            </div>
                                            <div class="tab-pane" id="tabProcessing" role="tabpanel">
                                                <span id="error_document" name="iaisErrorMsg" class="error-msg"></span>
                                                <br/><br/>
                                                <div class="alert alert-info" role="alert">
                                                    <strong>
                                                        <h4>Processing Audit Date</h4>
                                                    </strong>
                                                </div>
                                                <form method="post" action=<%=process.runtime.continueURL()%>>
                                                    <input type="hidden" name="sopEngineTabRef"
                                                           value="<%=process.rtStatus.getTabRef()%>">
                                                    <div class="row">
                                                        <div class="col-xs-12">
                                                            <div class="table-gp">
                                                                    <%--@elvariable id="processData" type="sg.gov.moh.iais.egp.bsb.dto.audit.OfficerProcessAuditDto"--%>
                                                                <iais:section title="">
                                                                    <div>
                                                                        <iais:row>
                                                                            <iais:field value="Request Audit Date" required="false"/>
                                                                            <iais:value width="10">
                                                                                <p><fmt:formatDate value='${processData.requestAuditDate}' pattern='dd/MM/yyyy'/></p>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <c:if test="${(processData.changeReason!=null && processData.changeReason!='')}">
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Reason for Change Audit Date" required="false" width="12"/>
                                                                                <iais:value width="10">
                                                                                    <p><c:out value="${processData.changeReason}"/></p>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                    </c:if>
                                                                    <c:if test="${(processData.remarks!=null && processData.remarks!='')}">
                                                                        <div>
                                                                            <iais:row>
                                                                                <iais:field value="Facility Admin's Remark" required="false"/>
                                                                                <iais:value width="10">
                                                                                    <p><c:out value="${processData.remarks}"/></p>
                                                                                </iais:value>
                                                                            </iais:row>
                                                                        </div>
                                                                    </c:if>
                                                                    <div id="rejectReason">
                                                                        <iais:row>
                                                                            <%--Required if decision is reject--%>
                                                                            <iais:field value="Reason for rejection" required="true" width="12"/>
                                                                            <iais:value width="10">
                                                                                <div class="input-group">
                                                                                    <div class="ax_default text_area">
                                                                                        <textarea id="reason"
                                                                                                  name="reason"
                                                                                                  cols="70"
                                                                                                  rows="7"
                                                                                                  maxlength="300"></textarea>
                                                                                        <span id="error_reason"
                                                                                              name="iaisErrorMsg"
                                                                                              class="error-msg"></span>
                                                                                    </div>
                                                                                </div>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div>
                                                                        <iais:row>
                                                                            <div><iais:field value="Remarks" required="false" width="12"/></div>
                                                                            <iais:value width="10">
                                                                                <div class="input-group">
                                                                                    <div class="ax_default text_area">
                                                                                        <textarea id="remark"
                                                                                                  name="remark"
                                                                                                  cols="70"
                                                                                                  rows="7"
                                                                                                  maxlength="300"></textarea>
                                                                                    </div>
                                                                                </div>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                    <div id="processingDecision">
                                                                        <iais:row>
                                                                            <iais:field value="Processing Decision" required="true"/>
                                                                            <iais:value width="10">
                                                                                <iais:select name="decision"
                                                                                             id="decision"
                                                                                             codeCategory="CATE_ID_BSB_CHANGE_DATE_DO"
                                                                                             firstOption="Please Select"/>
                                                                                <span id="error_decision"
                                                                                      name="iaisErrorMsg"
                                                                                      class="error-msg"></span>
                                                                            </iais:value>
                                                                        </iais:row>
                                                                    </div>
                                                                </iais:section>
                                                                <a style="float:left;padding-top: 1.1%;" class="back" href="/bsb-be/eservicecontinue/INTRANET/MohBsbTaskList"><em class="fa fa-angle-left"></em> Back</a>
                                                                <div align="right">
                                                                    <button name="submitBtn" id="submitButton" type="button" class="btn btn-primary">Submit</button>
                                                                </div>
                                                                <div>&nbsp;</div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </form>
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
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>