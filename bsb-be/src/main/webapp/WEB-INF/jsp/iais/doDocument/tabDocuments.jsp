<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<%@ page import="java.lang.String" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-officer-doc-tab.js"></script>

<div class="alert alert-info" role="alert">
    <h4>Supporting Document</h4>
</div>
<div id="u8522_text" class="text ">
    <p><span>These are documents uploaded by the applicant or an officer on behalf of the applicant. Listed
												documents are those defined for this digital service only.</span></p>
</div>
<div class="row">
    <div class="col-xs-12">
        <div class="table-gp">
            <table aria-describedby="" class="table">
                <thead>
                <tr>
                    <th scope="col" style="width: 20%">Type</th>
                    <th scope="col" style="width: 20%">File</th>
                    <th scope="col" style="width: 15%">Size</th>
                    <th scope="col" style="width: 20%">Submitted By</th>
                    <th scope="col" style="width: 25%">Date Submitted</th>
                </tr>
                </thead>
                <c:set var="maskedAppId" value="${MaskUtil.maskValue('file', appId)}"/>
                <tbody id="supportDocBody">
                <%--@elvariable id="supportDocDisplayDtoList" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto>"--%>
                <c:choose>
                    <c:when test="${supportDocDisplayDtoList eq null or empty supportDocDisplayDtoList}">
                        <tr><td colspan="5" style="text-align: left"><iais:message key="GENERAL_ACK018" escape="true"/></td></tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="doc" items="${supportDocDisplayDtoList}" varStatus="status">
                            <c:set var="maskedRepoId" value="${MaskUtil.maskValue('file', doc.fileRepoId)}"/>
                            <tr>
                                <td>${doc.docTypeDesc}</td>
                                <%-- This method downloadSupportDocument should be implemented by each module iteslf --%>
                                <td><a href="javascript:void(0)" onclick="downloadSupportDocument('${maskedAppId}', '${maskedRepoId}', '${doc.docName}')">${doc.docName}</a></td>
                                <td>${String.format("%.1f", doc.docSize/1024.0)}KB</td>
                                <td>${doc.submitByName}</td>
                                <td><fmt:formatDate value='${doc.submitDt}' pattern='dd/MM/yyyy HH:mm'/></td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>

            <div class="alert alert-info" role="alert">
                <h4>Internal Document</h4>
            </div>
            <div class="text ">
                <p><span>These are documents uploaded by an agency officer to support back office processing.</span>
                </p>
            </div>
            <table aria-describedby="" class="table">
                <thead>
                <tr>
                    <th scope="col" style="width: 20%">Type</th>
                    <th scope="col" style="width: 20%">File</th>
                    <th scope="col" style="width: 10%">Size</th>
                    <th scope="col" style="width: 20%">Submitted By</th>
                    <th scope="col" style="width: 20%">Date Submitted</th>
                    <th scope="col" style="width: 10%">Action</th>
                </tr>
                </thead>
                <tbody id="internalDocBody">
                <%--@elvariable id="internalDocDisplayDtoList" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto>"--%>
                <%--@elvariable id="appId" type="java.lang.String"--%>
                <c:choose>
                    <c:when test="${internalDocDisplayDtoList eq null or empty internalDocDisplayDtoList}">
                        <tr id="noRecordTr"><td colspan="6" style="text-align: left"><iais:message key="GENERAL_ACK018" escape="true"/></td></tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="doc" items="${internalDocDisplayDtoList}" varStatus="status">
                            <c:set var="maskedRepoId" value="${MaskUtil.maskValue('file', doc.fileRepoId)}"/>
                            <tr>
                                <td>${doc.docTypeDesc}</td>
                                <td><a href="javascript:void(0)" onclick="downloadInternalDocument('${maskedRepoId}', '${doc.docName}')">${doc.docName}</a></td>
                                <td>${String.format("%.1f", doc.docSize/1024.0)}KB</td>
                                <td>${doc.submitByName}</td>
                                <td><fmt:formatDate value='${doc.submitDt}' pattern='dd/MM/yyyy HH:mm'/></td>
                                <td><button type="button" class="btn btn-secondary-del btn-sm" onclick="deleteInternalDocument(this, '${maskedAppId}', '${maskedRepoId}')">DELETE</button></td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>


            <iais:action>
                <c:choose>
                    <%--@elvariable id="goBackUrl" type="java.lang.String"--%>
                    <c:when test="${goBackUrl ne null}">
                        <a class="back" href="${goBackUrl}" style="float:left"><em class="fa fa-angle-left"></em> Previous</a>
                    </c:when>
                    <c:otherwise>
                        <a class="back" href="/bsb-be/eservice/INTRANET/MohBsbTaskList" style="float:left"><em class="fa fa-angle-left"></em> Previous</a>
                    </c:otherwise>
                </c:choose>

                <%--@elvariable id="canNotUploadInternalDoc" type="java.lang.Boolean"--%>
                <c:if test="${canNotUploadInternalDoc eq null or !canNotUploadInternalDoc}">
                    <button type="button" style="float:right" class="btn btn-primary" data-toggle="modal" data-target="#uploadDoc">
                        Upload Document
                    </button>
                </c:if>
            </iais:action>
        </div>
    </div>
</div>



