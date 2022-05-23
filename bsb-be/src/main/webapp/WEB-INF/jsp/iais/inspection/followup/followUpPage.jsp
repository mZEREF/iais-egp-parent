<%--@elvariable id="reviewFollowUpDto" type="sg.gov.moh.iais.egp.bsb.dto.inspection.followup.ReviewInsFollowUpDto"--%>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<div class="alert alert-info" role="alert">
    <strong><h4>Inspection Follow-up Verification</h4></strong>
</div>
<div class="table-gp">
    <table aria-describedby="" class="table" id="reviewSubmitTable" style="display: none">
        <thead>
        <tr>
            <th scope="col" class="col-md-1">S/N</th>
            <th scope="col" class="col-md-2">Item Description</th>
            <th scope="col" class="col-md-3">Observations for Follow-up</th>
            <th scope="col" class="col-md-2">Due Date</th>
            <th scope="col" class="col-md-2">Applicant Remarks</th>
            <th scope="col" class="col-md-2">Documents</th>
        </tr>
        </thead>
        <c:set var="maskedAppId" value="${MaskUtil.maskValue('file', appId)}"/>
        <tbody>
        <c:choose>
            <c:when test="${empty reviewFollowUpDto.followUpDisplayDtos}">
                <iais:message key="GENERAL_ACK018" escape="true"/>
            </c:when>
            <c:otherwise>
                <c:forEach items="${reviewFollowUpDto.followUpDisplayDtos}" var="item" varStatus="status">
                    <tr>
                        <td class="col-md-1"><c:out value="${status.index+1}"/></td>
                        <td class="col-md-2"><c:out value="${item.itemDescription}"/></td>
                        <td class="col-md-3"><c:out value="${item.observation}"/></td>
                        <td class="col-md-2"><c:out value="${item.dueDate}"/></td>
                        <td class="col-md-2"><c:out value="${item.applicantRemarks}"/></td>
                        <td class="col-md-2">
                            <c:if test="${not empty reviewFollowUpDto.followUpDocDisplayDtoList}">
                                <c:forEach items="${reviewFollowUpDto.followUpDocDisplayDtoList}" var="doc">
                                    <c:set var="maskedRepoId" value="${MaskUtil.maskValue('file', doc.fileRepoId)}"/>
                                    <a href="javascript:void(0)" onclick="downloadSupportDocument('${maskedAppId}', '${maskedRepoId}', '${doc.docName}')">${doc.docName}</a>
                                    &nbsp;
                                </c:forEach>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>

    <table aria-describedby="" class="table" id="reviewExtensionTable" style="display: none">
        <thead>
        <tr>
            <th scope="col" class="col-md-3">Observations for Follow-up</th>
            <th scope="col" class="col-md-2">Due Date</th>
            <th scope="col" class="col-md-2">Applicant Remarks</th>
            <th scope="col" class="col-md-2">Document</th>
        </tr>
        </thead>
        <c:set var="maskedAppId" value="${MaskUtil.maskValue('file', appId)}"/>
        <tbody>
        <c:choose>
            <c:when test="${empty reviewFollowUpDto.followUpDisplayDtos}">
                <iais:message key="GENERAL_ACK018" escape="true"/>
            </c:when>
            <c:otherwise>
                <c:forEach items="${reviewFollowUpDto.followUpDisplayDtos}" var="item">
                    <tr>
                        <td class="col-md-3"><c:out value="${item.observation}"/></td>
                        <td class="col-md-2"><c:out value="${item.dueDate}"/></td>
                        <td class="col-md-2"><c:out value="${item.applicantRemarks}"/></td>
                        <td class="col-md-2">
                            <c:if test="${not empty reviewFollowUpDto.followUpDocDisplayDtoList}">
                                <c:forEach items="${reviewFollowUpDto.followUpDocDisplayDtoList}" var="doc">
                                    <c:set var="maskedRepoId" value="${MaskUtil.maskValue('file', doc.fileRepoId)}"/>
                                    <a href="javascript:void(0)" onclick="downloadSupportDocument('${maskedAppId}', '${maskedRepoId}', '${doc.docName}')">${doc.docName}</a>
                                </c:forEach>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>
</div>
<br>
<div class="row">
    <div id="reasonMandatory" style="display: none">
        <div class="col-xs-4 control-label">
            <label for="reasonForExtension">Reason for extension</label>
        </div>
        <div class="col-xs-6">
            <textarea maxLength="300" class="col-xs-12" name="reasonForExtension" id="reasonForExtension" rows="5"
                      readonly><c:out value="${reviewFollowUpDto.reasonForExtension}"/></textarea>
        </div>
    </div>
</div>
<br>
<%@ include file="history.jsp" %>
<c:choose>
    <%--@elvariable id="goBackUrl" type="java.lang.String"--%>
    <c:when test="${goBackUrl ne null}">
        <a class="back" href="${goBackUrl}" style="float:left"><em class="fa fa-angle-left"></em> Previous</a>
    </c:when>
    <c:otherwise>
        <a class="back" href="/bsb-web/eservice/INTRANET/MohBsbTaskList" style="float:left"><em class="fa fa-angle-left"></em> Previous</a>
    </c:otherwise>
</c:choose>
