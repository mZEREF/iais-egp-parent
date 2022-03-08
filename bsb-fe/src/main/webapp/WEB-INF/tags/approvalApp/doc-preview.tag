<%@tag description="Preview documents, this tag should be used by preview.tag" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@attribute name="docSettings" required="true" type="java.util.List<sg.gov.moh.iais.egp.bsb.entity.DocSetting>" %>
<%@attribute name="savedFiles" required="true" type="java.util.Map<java.lang.String, java.util.List<sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo>>" %>
<%@attribute name="newFiles" required="true" type="java.util.Map<java.lang.String, java.util.List<sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo>>" %>

<c:forEach var="doc" items="${docSettings}">
    <c:set var="savedFileList" value="${savedFiles.get(doc.type)}" />
    <c:set var="newFileList" value="${newFiles.get(doc.type)}" />
    <c:if test="${not empty savedFileList or not empty newFileList}">
        <div class="form-group">
            <div class="col-10"><strong>${doc.typeDisplay}</strong></div>
            <div class="clear"></div>
        </div>
        <div>
            <c:forEach var="file" items="${savedFileList}">
                <c:set var="repoId"><iais:mask name="file" value="${file.repoId}"/></c:set>
                <div class="form-group">
                    <div class="col-10"><p><a href="/bsb-fe/ajax/doc/download/approvalApp/repo/${repoId}">${file.filename}</a>(<fmt:formatNumber value="${file.size/1024.0}" type="number" pattern="0.0"/>KB)</p></div>
                    <div class="clear"></div>
                </div>
            </c:forEach>
            <c:forEach var="file" items="${newFileList}">
                <c:set var="tmpId"><iais:mask name="file" value="${file.tmpId}"/></c:set>
                <div class="form-group">
                    <div class="col-10"><p><a href="/bsb-fe/ajax/doc/download/approvalApp/new/${tmpId}">${file.filename}</a>(<fmt:formatNumber value="${file.size/1024.0}" type="number" pattern="0.0"/>KB)</p></div>
                    <div class="clear"></div>
                </div>
            </c:forEach>
        </div>
    </c:if>
</c:forEach>