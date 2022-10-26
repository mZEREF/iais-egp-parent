<%--@elvariable id="reviewFollowUpDto" type="sg.gov.moh.iais.egp.bsb.dto.inspection.followup.ReviewInsFollowUpDto"--%>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<div class="alert alert-info" role="alert">
    <strong><h4>Inspection Follow-up Verification</h4></strong>
</div>
<div class="table-gp">
    <table aria-describedby="" class="table" id="reviewSubmitTable">
        <thead>
        <tr>
            <th scope="col" style="width: 12%">Checklist Item</th>
            <th scope="col" style="width: 18%">Item Description</th>
            <th scope="col" style="width: 20%">Observations for Follow-up</th>
            <th scope="col" style="width: 14%">Action Required</th>
            <th scope="col" style="width: 12%">Due Date</th>
            <th scope="col" style="width: 12%">Documents</th>
            <th scope="col" style="width: 12%">Document Type</th>
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
                        <td><c:out value="${item.checklistItem}"/></td>
                        <td><c:out value="${item.itemDescription}"/></td>
                        <td><c:out value="${item.observation}"/></td>
                        <td><c:out value="${item.actionRequired}"/></td>
                        <td><c:out value="${item.dueDt}"/></td>
                        <td>
                            <c:if test="${not empty reviewFollowUpDto.followUpDocDisplayDtoList}">
                                <c:forEach items="${reviewFollowUpDto.followUpDocDisplayDtoList}" var="doc">
                                    <c:set var="maskedRepoId" value="${MaskUtil.maskValue('file', doc.fileRepoId)}"/>
                                    <div class="docDl">
                                        <a href="javascript:void(0)" onclick="downloadSupportDocument('${maskedAppId}', '${maskedRepoId}', '${doc.docName}')">${doc.docName}</a>
                                    </div>
                                </c:forEach>
                            </c:if>
                        </td>
                        <td>
                            <c:if test="${not empty reviewFollowUpDto.followUpDocDisplayDtoList}">
                                <c:forEach items="${reviewFollowUpDto.followUpDocDisplayDtoList}" var="doc">
                                    <div class="docType">
                                        <iais:code code="${doc.docType}"/>
                                    </div>
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
    <c:if test="${requestExtension eq 'Y'}">
        <div id="reasonMandatory">
            <div class="col-xs-4 control-label">
                <label for="reasonForExtension">Reason for extension</label>
            </div>
            <div class="col-xs-6">
                <textarea maxLength="300" class="col-xs-12" name="reasonForExtension" id="reasonForExtension" rows="5"
                          readonly><c:out value="${reviewFollowUpDto.reasonForExtension}"/></textarea>
            </div>
        </div>
    </c:if>
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
