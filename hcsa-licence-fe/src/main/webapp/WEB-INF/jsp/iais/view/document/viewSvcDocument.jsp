<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="amended-service-info-gp">
    <iais:row>
        <label class="app-title">${currStepName}</label>
    </iais:row>
    <div class="amend-preview-info form-horizontal min-row">
        <c:forEach var="docShowDto" items="${currSvcInfoDto.documentShowDtoList}" varStatus="stat">
            <c:if test="${docShowDto.existDoc}">
                <iais:row>
                    <div class="col-xs-12 app-title">
                        <p><c:out value="${docShowDto.premName}"/></p>
                        <p>Address: <c:out value="${docShowDto.premAddress}"/></p>
                    </div>
                </iais:row>
                <c:forEach var="secDto" items="${docShowDto.docSectionList}" varStatus="secStat">
                    <c:if test="${secDto.existDoc}">
                        <c:forEach var="doc" items="${secDto.docSecDetailList}" varStatus="docStat">
                            <c:if test="${doc.existDoc}">
                                <%@include file="viewSvcDocContent.jsp"%>
                            </c:if>
                        </c:forEach>
                    </c:if>
                </c:forEach>
            </c:if>
        </c:forEach>
    </div>
</div>
