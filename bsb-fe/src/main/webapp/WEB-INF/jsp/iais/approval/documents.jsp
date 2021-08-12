<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@ include file="common/dashboard.jsp" %>
<form method="post" id="mainForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@ include file="common/navTabs.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane active" id="documentsTab" role="tabpanel">
                            </div>
                            <div class="document-content ">
                                <input type="hidden" name="uploadKey" value=""/>
                                <div id="selectFileDiv">
                                    <input id="selectedFile" class="selectedFile"  name="selectedFile" type="file" style="display: none;" onclick="fileClicked(event)" onchange="fileChangedLocal(this,event)" aria-label="selectedFile1">
                                </div>
                                <div class="document-info-list">
                                    <ul>
                                        <li>
                                            <p>The maximum file size for each upload is <c:out value="${sysFileSize}"/>MB. </p>
                                        </li>
                                        <li>
                                            <p>Acceptable file formats are<c:out value="${sysFileType}"/></p>
                                        </li>
                                    </ul>
                                </div>
                                <div class="document-upload-gp">
                                    <h2>PRIMARY DOCUMENTS</h2>
                                </div>
                                <div class="application-tab-footer">
                                    <div class="row">
                                        <div class="col-xs-12 col-sm-6"><a class="back" id="Back2"><em class="fa fa-angle-left"></em> Back</a></div>
                                        <div class="col-xs-12 col-sm-6">
                                            <div class="button-group">
                                                <a class="btn btn-secondary" id = "SaveDraft"  href="javascript:void(0);">Save as Draft</a>
                                                <a class="btn btn-primary" id="Next" href="javascript:void(0);">Next</a>
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
    <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
</form>