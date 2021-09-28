<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT"%>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-facility-register.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="dashboard.jsp"%>

<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@ include file="InnerNavTab.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div id="PrimaryDocsPanel" role="tabpanel">
                                    <div class="document-content">
                                        <div class="document-info-list">
                                            <ul>
                                                <li><p>The maximum file size for each upload is 5MB</p></li>
                                                <li><p>Acceptable file formats are PDF, word, JPG, Excel and PNG</p></li>
                                            </ul>
                                        </div>
                                        <div class="document-upload-gp">
                                            <h2>PRIMARY DOCUMENTS</h2>

                                            <div class="document-upload-list">
                                                <h3>Company Information <span class="mandatory otherQualificationSpan">*</span></h3>
                                                <div class="file-upload-gp">
                                                    <a class="btn file-upload btn-secondary" href="javascript:void(0);">Upload</a>
                                                </div>
                                            </div>

                                            <div class="document-upload-list">
                                                <h3>SOP for Certification <span class="mandatory otherQualificationSpan">*</span></h3>
                                                <div class="file-upload-gp">
                                                    <a class="btn file-upload btn-secondary" href="javascript:void(0);">Upload</a>
                                                </div>
                                            </div>

                                            <div class="document-upload-list">
                                                <h3>Others</h3>
                                                <div class="file-upload-gp">
                                                    <a class="btn file-upload btn-secondary" href="javascript:void(0);">Upload</a>
                                                </div>
                                            </div>

                                            <h2 style="margin: 10px 0;border-bottom: 1px solid black">Team Member</h2>

                                            <div class="document-upload-list">
                                                <h3>Testimonials <span class="mandatory otherQualificationSpan">*</span></h3>
                                                <div class="file-upload-gp">
                                                    <a class="btn file-upload btn-secondary" href="javascript:void(0);">Upload</a>
                                                </div>
                                            </div>

                                            <div class="document-upload-list">
                                                <h3>Curriculum Vitae <span class="mandatory otherQualificationSpan">*</span></h3>
                                                <div class="file-upload-gp">
                                                    <a class="btn file-upload btn-secondary" href="javascript:void(0);">Upload</a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <%@ include file="InnerFooter.jsp" %>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>