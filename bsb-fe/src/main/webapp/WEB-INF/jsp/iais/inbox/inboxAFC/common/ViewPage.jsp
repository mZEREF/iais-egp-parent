<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%--@elvariable id="reviewAFCReportDto" type="sg.gov.moh.iais.egp.bsb.dto.inspection.afc.ReviewAFCReportDto"--%>
<%--@elvariable id="reportDto" type="sg.gov.moh.iais.egp.bsb.dto.inspection.ReportDto"--%>
<div class="dashboard">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <div class="main-content">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="center-content">
                        <div class="internet-content">
                            <iais:body>
                                <div class="col-xs-12">
                                    <div class="tab-gp dashboard-tab">
                                        <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                                            <c:if test="${reportDto ne null}">
                                                <li id="InspectionReport" role="presentation" class="active">
                                                    <a href="#tabInspectionReport" id="doInspectionReport" aria-controls="tabInspectionReport" role="tab" data-toggle="tab">Inspection Report</a>
                                                </li>
                                            </c:if>
                                            <c:if test="${reviewAFCReportDto ne null}">
                                                <li id="CertificationReport" role="presentation">
                                                    <a href="#tabCertificationReport" id="doCertificationReport" aria-controls="tabCertificationReport" role="tab" data-toggle="tab">Certification Documents</a>
                                                </li>
                                            </c:if>
                                        </ul>
                                        <div class="tab-nav-mobile visible-xs visible-sm">
                                            <div class="swiper-wrapper" role="tablist">
                                                <c:if test="${reportDto ne null}">
                                                    <div class="swiper-slide active">
                                                        <a href="#tabInspectionReport" aria-controls="tabInspectionReport" role="tab" data-toggle="tab">Inspection Report</a>
                                                    </div>
                                                </c:if>
                                                <c:if test="${reviewAFCReportDto ne null}">
                                                    <div class="swiper-slide">
                                                        <a href="#tabCertificationReport" aria-controls="tabCertificationReport" role="tab" data-toggle="tab">Certification Documents</a>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </div>
                                        <div class="tab-content">
                                            <c:if test="${reportDto ne null}">
                                                <div class="tab-pane active" id="tabInspectionReport" role="tabpanel">
                                                    <%@include file="inspectionReport.jsp"%>
                                                </div>
                                            </c:if>
                                            <c:if test="${reviewAFCReportDto ne null}">
                                                <div class="tab-pane" id="tabCertificationReport" role="tabpanel">
                                                    <%@include file="certificationDocumentsPage.jsp"%>
                                                </div>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                            </iais:body>
                            <div style="text-align: right">
                                <a class="back" href="/bsb-web/eservice/INTERNET/MohBSBInboxMsg" style="float:left"><em class="fa fa-angle-left"></em> Previous</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>