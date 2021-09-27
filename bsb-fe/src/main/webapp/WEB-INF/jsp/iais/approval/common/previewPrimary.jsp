<div class="panel panel-default">
    <div class="panel-heading" id="headingOne" role="tab">
        <h4 class="panel-title"><a class="collapsed a-panel-collapse" role="button" data-toggle="collapse" href="#collapseOne${documentIndex}" aria-expanded="true" aria-controls="collapseOne" name="printControlNameForApp">Primary Documents</a></h4>
    </div>
    <div class="panel-collapse collapse <c:if test="${!empty printFlag}">in</c:if>" id="collapseOne${documentIndex}" role="tabpanel" aria-labelledby="headingOne">
        <div class="panel-body">
            <%--<c:set var="reloadMap" value="${AppSubmissionDto.multipleGrpPrimaryDoc}"/>--%>
            <c:forEach var="config" items="${primaryDocConfig}" varStatus="configStat">
                <c:choose>
                    <c:when test="${'1' == config.index}">
                        <c:set var="fileList" value="${docReloadMap[config.id]}"/>
                        <%@include file="previewPrimaryContent.jsp"%>
                    </c:when>
                    <c:when test="${'2' == config.index}">
                        <c:set var="fileList" value="${docReloadMap[config.id]}"/>
                        <%--<%@include file="previewPrimaryContent.jsp"%>--%>
                    </c:when>
                    <c:when test="${'3' == config.index}">
                        <c:set var="fileList" value="${docReloadMap[config.id]}"/>
                        <%--<%@include file="previewPrimaryContent.jsp"%>--%>
                    </c:when>
                    <c:when test="${'4' == config.index}">
                        <c:set var="fileList" value="${docReloadMap[config.id]}"/>
                        <%--<%@include file="previewPrimaryContent.jsp"%>--%>
                    </c:when>
                    <c:when test="${'5' == config.index}">
                        <c:set var="fileList" value="${docReloadMap[config.id]}"/>
                        <%--<%@include file="previewPrimaryContent.jsp"%>--%>
                    </c:when>
                    <c:when test="${'6' == config.index}">
                        <c:set var="fileList" value="${docReloadMap[config.id]}"/>
                        <%--<%@include file="previewPrimaryContent.jsp"%>--%>
                    </c:when>
                    <c:when test="${'7' == config.index}">
                        <c:set var="fileList" value="${docReloadMap[config.id]}"/>
                        <%--<%@include file="previewPrimaryContent.jsp"%>--%>
                    </c:when>
                    <c:when test="${'8' == config.index}">
                        <c:set var="fileList" value="${docReloadMap[config.id]}"/>
                        <%--<%@include file="previewPrimaryContent.jsp"%>--%>
                    </c:when>
                </c:choose>
            </c:forEach>
        </div>
    </div>
</div>
