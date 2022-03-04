<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="panel-main-content form-horizontal min-row">
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
                    <c:set var="tmpId"><iais:mask name="file" value="${info.repoId}"/></c:set>
                    <div class="form-group">
                        <div class="col-10"><p><a href="/bsb-fe/ajax/doc/download/commonDoc/repo/${repoId}">${file.filename}</a>(<fmt:formatNumber value="${file.size/1024.0}" type="number" pattern="0.0"/>KB)</p></div>
                        <div class="clear"></div>
                    </div>
                </c:forEach>
                <c:forEach var="file" items="${newFileList}">
                    <c:set var="tmpId"><iais:mask name="file" value="${info.tmpId}"/></c:set>
                    <div class="form-group">
                        <div class="col-10"><p><a href="/bsb-fe/ajax/doc/download/commonDoc/new/${tmpId}">${file.filename}</a>(<fmt:formatNumber value="${file.size/1024.0}" type="number" pattern="0.0"/>KB)</p></div>
                        <div class="clear"></div>
                    </div>
                </c:forEach>
            </div>
        </c:if>
    </c:forEach>
</div>