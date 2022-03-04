<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT"%>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common-facility-certifier-register.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-display-or-not.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-renewal-facility-certifier-register.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common-file.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="dashboard.jsp"%>

<form method="post" id="mainForm" enctype="multipart/form-data" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <input type="hidden" id="deleteExistFiles" name="deleteExistFiles" value="">
    <input type="hidden" id="deleteNewFiles" name="deleteNewFiles" value="">
    <div id="fileUploadInputDiv" style="display: none"></div>
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
                                        <div class="text-right"><a id="edit" href="javascript:void(0)"><em class="fa fa-pencil-square-o"></em>Edit</a></div>
                                        <%@ include file="../../mainAppCommon/facCertifierRegistration/orgPrimaryDocs.jsp" %>
                                    </div>
                                </div>
                                <div class="application-tab-footer">
                                    <div class="row">
                                        <div class="col-xs-12 col-sm-6 ">
                                            <a class="back" id="back" href="#"><em class="fa fa-angle-left"></em> Previous</a>
                                        </div>
                                        <div class="col-xs-12 col-sm-6">
                                            <div class="button-group">
                                                <a class="btn btn-secondary" id="saveDraft" >Save as Draft</a>
                                                <a class="btn btn-primary" id="previewButton" >Preview</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <%@include file="/WEB-INF/jsp/iais/include/jumpAfterDraft.jsp"%>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>

