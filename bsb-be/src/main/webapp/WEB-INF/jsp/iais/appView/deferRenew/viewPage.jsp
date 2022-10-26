<%@taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="defer" tagdir="/WEB-INF/tags/deferRenew" %>

<%--@elvariable id="needCompare" type="java.lang.Boolean"--%>
<%--@elvariable id="compareDocMap" type="java.util.Map<java.lang.String, java.util.List<gov.moh.iais.egp.bsb.dto.register.facility.CompareDocRecordInfoDto>>"--%>
<%--@elvariable id="savedFiles" type="java.util.Map<java.lang.String, java.util.List<sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo>>"--%>
<%--@elvariable id="compareDeferRenew" type="sg.gov.moh.iais.egp.bsb.dto.compare.CompareWrap"--%>
<%--@elvariable id="deferRenewViewDto" type="sg.gov.moh.iais.egp.bsb.dto.appview.deferrenew.DeferRenewViewDto"--%>
<%--@elvariable id="docTypes" type="java.util.Collection<java.lang.String>"--%>
<%--@elvariable id="pageAppEditSelectDto" type="sg.gov.moh.iais.egp.bsb.dto.rfi.PageAppEditSelectDto"--%>
<%--@elvariable id="viewType" type="java.lang.String"--%>
<c:if test="${needCompare}">
    <defer:compare-preview compareWrap="${compareDeferRenew}">
        <jsp:attribute name="docFrag">
            <c:if test="${not empty docTypes}">
                <div class="form-group">
                    <c:forEach var="type" items="${docTypes}">
                        <c:forEach var="compareWrap" items="${compareDocMap.get(type)}" varStatus="status">
                            <c:set var="oldFile" value="${compareWrap.oldDto}"/>
                            <c:set var="newFile" value="${compareWrap.newDto}"/>
                            <div class="form-group">
                                <c:set var="oldRepoId"><iais:mask name="file" value="${oldFile.repoId}"/></c:set>
                                <div class="col-xs-6"><p data-compare-old="${doc.type}${status.index}" data-val="${oldFile.repoId}"><a href="/bsb-web/ajax/doc/download/repo/${oldRepoId}?filename=${oldFile.filename}">${oldFile.filename}</a>(<fmt:formatNumber value="${oldFile.size/1024.0}" type="number" pattern="0.0"/>KB)</p></div>
                                <c:set var="newRepoId"><iais:mask name="file" value="${newFile.repoId}"/></c:set>
                                <div class="col-xs-6"><p data-compare-new="${doc.type}${status.index}" data-val="${newFile.repoId}" class="compareTdStyle" style="display: none"><a href="/bsb-web/ajax/doc/download/repo/${newRepoId}?filename=${newFile.filename}">${newFile.filename}</a>(<fmt:formatNumber value="${newFile.size/1024.0}" type="number" pattern="0.0"/>KB)</p></div>
                                <div class="clear"></div>
                            </div>
                        </c:forEach>
                    </c:forEach>
                </div>
            </c:if>
        </jsp:attribute>
    </defer:compare-preview>
</c:if>
<c:if test="${!needCompare}">
    <defer:preview deferRenewViewDto="${deferRenewViewDto}" pageAppEditSelectDto="${pageAppEditSelectDto}" viewType="${viewType}">
        <jsp:attribute name="docFrag">
            <c:if test="${not empty docTypes}">
                <div>
                    <c:forEach var="type" items="${docTypes}">
                        <c:set var="savedFileList" value="${savedFiles.get(type)}" />
                        <c:forEach var="file" items="${savedFileList}">
                            <c:set var="repoId"><iais:mask name="file" value="${file.repoId}"/></c:set>
                            <div class="form-group">
                                <div class="col-10"><p><a href="/bsb-web/ajax/doc/download/repo/${repoId}?filename=${file.filename}">${file.filename}</a>(<fmt:formatNumber value="${file.size/1024.0}" type="number" pattern="0.0"/>KB)</p></div>
                                <div class="clear"></div>
                            </div>
                        </c:forEach>
                    </c:forEach>
                </div>
            </c:if>
        </jsp:attribute>
    </defer:preview>
</c:if>