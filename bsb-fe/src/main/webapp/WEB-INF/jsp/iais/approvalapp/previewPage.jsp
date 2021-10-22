<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%--
<%@ include file="common/dashboard.jsp" %>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" id="page_id" name="page_id" value="preview_page">
    <input type="hidden" id="actionType" name="actionType" value="">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@ include file="common/navTabs.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane active" id="previewTab" role="tabpanel">
                                <div class="preview-gp">
                                    <div class="row">
                                        <div class="col-xs-12 col-md-10">
                                            <p></p>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                                                <%@include file="common/previewApprovalInfo.jsp"%>
                                                <%@include file="common/previewPrimary.jsp"%>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="application-tab-footer">
                                    <div class="row">
                                        <div class="col-xs-12 col-sm-6"><a class="back" id="Back"><em class="fa fa-angle-left"></em>Back</a></div>
                                        <div class="col-xs-12 col-sm-6">
                                            <div class="button-group">
                                                <a class="btn btn-secondary" id="SaveDraft">Save as Draft</a>
                                                <a class="next btn btn-primary" id="Submit" href="#">Submit</a>
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
</form>--%>
