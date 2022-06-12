<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/css/bsb/bsb-withdrawn.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-file.js"></script>

<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <%--@elvariable id="viewWithdrawnDto" type="sg.gov.moh.iais.egp.bsb.dto.withdrawn.ViewWithdrawnDto"--%>
    <%--@elvariable id="supportDocDisplayDtoList" type="sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto"--%>
    <%--@elvariable id="appId" type="java.lang.String"--%>
    <div class="navigation-gp">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="intranet-content">
                    <div class="center-content">
                        <p style="font-size: 5rem">Withdrawal Form</p>
                        <h2>You are withdrawing for</h2>
                        <div class="row">
                            <div class="col-lg-8 col-xs-12">
                                <div class="withdraw-content-box">
                                    <div class="withdraw-info-gp">
                                        <div class="withdraw-info-row">
                                            <div class="withdraw-info">
                                                <p><c:out value="${viewWithdrawnDto.appNo}"/></p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="center-content">
                        <h3>Reason for Withdrawal<span style="color: #ff0000"> *</span></h3>
                        <div class="row">
                            <div class="col-md-7">
                                <input type="text" value="<iais:code code="${viewWithdrawnDto.reason}"/>" readonly>
                            </div>
                        </div>
                    </div>
                    <div id="remarksDiv" <c:if test="${viewWithdrawnDto.reason ne 'WDREASN007' || viewWithdrawnDto.reason == null}">style="display: none"</c:if>>
                        <div class="row">
                            <div class="center-content">
                                <label class="col-md-4" style="font-size:2rem">Supporting Remarks<span style="color: #ff0000"> *</span></label>
                            </div>
                        </div>
                        <div class="row">
                            <div class="center-content">
                                <div class="col-md-6">
                                    <div class="file-upload-gp">
                                        <textarea name="remarks" id="remarks" readonly maxlength="1000" cols="100" rows="10"><c:out value="${viewWithdrawnDto.remarks}"/></textarea>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="center-content">
                        <div class="row">
                            <div class="col-xs-12">
                                <div class="tab-gp steps-tab">
                                    <div class="document-content">
                                        <div class="document-upload-gp">
                                            <div class="document-upload-list">
                                                <h2>Supporting Documents</h2>
                                                <c:set var="maskedAppId"><iais:mask name="file" value="${appId}"/></c:set>
                                                <div id="upload-other-doc-gp" class="file-upload-gp">
                                                    <c:if test="${supportDocDisplayDtoList ne null}">
                                                        <c:forEach var="doc" items="${supportDocDisplayDtoList}">
                                                            <c:set var="maskedRepoId"><iais:mask name="file" value="${doc.fileRepoId}"/></c:set>
                                                            <div id="${maskedRepoId}FileDiv">
                                                                <a href="javascript:void(0)" onclick="downloadSupportDocument('${maskedAppId}', '${maskedRepoId}', '${doc.docName}')"><span id="${maskedRepoId}Span"><c:out value="${doc.docName}"/></span></a>
                                                            </div>
                                                        </c:forEach>
                                                    </c:if>
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
    </div>
</form>