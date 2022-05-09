<%@tag description="Facility committee tag of facility registration" %>
<%@taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@attribute name="committeeSampleFile" required="true" type="sg.gov.moh.iais.egp.bsb.dto.entity.SampleFileDto" %>
<%@attribute name="facCommittee" required="true" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityCommitteeDto" %>
<%@attribute name="dataHasError" required="true" type="java.lang.Boolean" %>
<%@attribute name="dataErrors" required="true" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.validation.ValidationListResultUnit>" %>
<%@attribute name="validFile" required="true" type="java.lang.Boolean" %>
<%@attribute name="specialJsFrag" fragment="true" %>
<%@attribute name="dashboardFrag" fragment="true" %>
<%@attribute name="innerFooterFrag" fragment="true" %>
<%@attribute name="editJudge" type="java.lang.Boolean" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common-node-group.js"></script>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common-data-file.js"></script>
<jsp:invoke fragment="specialJsFrag"/>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp" %>
<jsp:invoke fragment="dashboardFrag"/>
<form method="post" id="mainForm" enctype="multipart/form-data" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">

    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@include file="/WEB-INF/jsp/iais/mainAppCommon/facRegistration/InnerNavTab.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div id="facInfoPanel" role="tabpanel">
                                    <%@include file="/WEB-INF/jsp/iais/mainAppCommon/facRegistration/subStepNavTab.jsp" %>
                                    <div class="form-horizontal">
                                        <c:if test="${editJudge}"><div class="text-right app-font-size-16"><a id="edit" href="javascript:void(0)"><em class="fa fa-pencil-square-o"></em>Edit</a></div></c:if>
                                        <h3 class="col-12 pl-0" style="border-bottom: 1px solid black">Please upload the list of Biosafety Committee Members</h3>
                                        <div class="document-info-list">
                                            <ul>
                                                <li>Click <a href='/bsb-fe/ajax/doc/download/facReg/committee/sample/<iais:mask name="file" value="${committeeSampleFile.fileRepoId}"/>' style="text-decoration: underline">here</a> to download the template for the list of biosafety committee members.</li>
                                                <li>Acceptable file format is XLSX, CSV.</li>
                                                <li>The maximum file size is 10 MB.</li>
                                            </ul>
                                        </div>

                                        <div class="document-content">
                                            <div class="document-upload-gp">
                                                <div class="document-upload-list">
                                                    <h3>Biosafety Committee Information (${facCommittee.amount} records uploaded)</h3>
                                                    <div class="file-upload-gp">
                                                        <span data-err-ind="committeeData" class="error-msg"></span>
                                                        <c:if test="${dataHasError}">
                                                            <span class="error-msg">Unsuccessful upload due to the following invalid record. Please complete rectification before reattempting upload.</span>
                                                            <div class="row">
                                                                <div class="col-xs-12">
                                                                    <div class="table-gp">
                                                                        <table class="table" aria-describedby="">
                                                                            <thead>
                                                                            <tr style="font-weight: bold">
                                                                                <th scope="col" style="width: 15%">S/N</th>
                                                                                <th scope="col" style="width: 35%">Filed Name (Column)</th>
                                                                                <th scope="col" style="width: 50%">Error Message</th>
                                                                            </tr>
                                                                            </thead>
                                                                            <c:forEach var="error" items="${dataErrors}">
                                                                                <tr style="border-top: 1px solid black">
                                                                                    <td>${error.sequence}</td>
                                                                                    <td>${error.field}</td>
                                                                                    <td>${error.message}</td>
                                                                                </tr>
                                                                            </c:forEach>
                                                                        </table>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </c:if>
                                                        <c:if test="${facCommittee.savedFile ne null}">
                                                            <c:set var="repoId"><iais:mask name="file" value="${facCommittee.savedFile.repoId}"/></c:set>
                                                            <div id="${repoId}FileDiv">
                                                                <a href="/bsb-fe/ajax/doc/download/facReg/committee/repo/${repoId}" style="text-decoration: underline"><span id="${repoId}Span">${facCommittee.savedFile.filename}</span></a>(<fmt:formatNumber value="${facCommittee.savedFile.size/1024.0}" type="number" pattern="0.0"/>KB)<button
                                                                    type="button" class="btn btn-secondary btn-sm" onclick="delete1DataFile('${repoId}')">Delete</button>
                                                                <span data-err-ind="${facCommittee.savedFile.repoId}" class="error-msg"></span>
                                                            </div>
                                                        </c:if>
                                                        <c:if test="${facCommittee.newFile ne null}">
                                                            <c:set var="tmpId"><iais:mask name="file" value="${facCommittee.newFile.tmpId}"/></c:set>
                                                            <div id="${tmpId}FileDiv">
                                                                <a href="/bsb-fe/ajax/doc/download/facReg/committee/new/${tmpId}" style="text-decoration: underline"><span id="${tmpId}Span">${facCommittee.newFile.filename}</span></a>(<fmt:formatNumber value="${facCommittee.newFile.size/1024.0}" type="number" pattern="0.0"/>KB)<button
                                                                    type="button" class="btn btn-secondary btn-sm" onclick="delete1DataFile('${tmpId}')">Delete</button>
                                                                <span data-err-ind="${facCommittee.newFile.tmpId}" class="error-msg"></span>
                                                            </div>
                                                        </c:if>
                                                        <div><a class="btn file-upload btn-secondary" data-upload-data-file="committeeData" href="javascript:void(0);">Upload</a></div>
                                                        <input type="file" id="committeeData" name="committeeData" data-data-file-input="committeeData" style="display: none">
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <c:if test="${validFile}">
                                            <div><p>Click <a href="javascript:void(0)" onclick="expandFile('facInfo_facCommittee', 'bsbCommittee')">here</a> to expand all information</p></div>
                                        </c:if>
                                    </div>
                                </div>
                                <jsp:invoke fragment="innerFooterFrag"/>
                            </div>
                        </div>
                        <%@include file="/WEB-INF/jsp/iais/include/jumpAfterDraft.jsp" %>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>