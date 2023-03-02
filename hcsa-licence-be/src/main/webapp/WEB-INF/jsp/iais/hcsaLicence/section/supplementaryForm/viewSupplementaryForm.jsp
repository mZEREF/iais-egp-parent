<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="amended-service-info-gp">
    <label class="title-font-size">${currStepName}</label>
    <div class="amend-preview-info form-horizontal min-row">
        <div class="form-check-gp">
            <c:set var="appSvcSuplmFormList" value="${currentPreviewSvcInfo.appSvcSuplmFormList}"/>
            <c:set var="oldAppSvcSuplmFormList" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcSuplmFormList}"/>
            <%--<table class="col-xs-12">--%>
            <c:forEach var="appSvcSuplmFormDto" items="${appSvcSuplmFormList}" varStatus="statute">
            <c:set var="oldAppSvcSuplmFormDto" value="${oldAppSvcSuplmFormList[statute.index]}"/>
                <iais:row>
                    <div class="col-xs-12">
                        <div class="newVal "
                             attr="${appSvcSuplmFormDto.premName}<c:out value="${appSvcSuplmFormDto.premAddress}"/>">
                            <c:if test="${not empty appSvcSuplmFormDto.premAddress}">
                                <div class="app-title"><c:out value="${appSvcSuplmFormDto.premName}"/></div>
                                <div class="font-18 bold">Address: <c:out
                                        value="${appSvcSuplmFormDto.premAddress}"/></div>
                            </c:if>
                        </div>
                    </div>
                    <div class="col-xs-12">
                        <div class="oldVal"
                             attr="${oldAppSvcSuplmFormDto.premName}<c:out value="${oldAppSvcSuplmFormDto.premAddress}"/>">
                            <c:if test="${not empty oldAppSvcSuplmFormDto.premAddress}">
                                <div class="app-title"><c:out value="${oldAppSvcSuplmFormDto.premName}"/></div>
                                <div class="font-18 bold">Address: <c:out
                                        value="${oldAppSvcSuplmFormDto.premAddress}"/></div>
                            </c:if>
                        </div>
                    </div>
                </iais:row>
            <div class="row">
                <div>
                <c:forEach var="appSvcSuplmGroupDto" items="${appSvcSuplmFormDto.appSvcSuplmGroupDtoList}" varStatus="status">
                    <c:set var="oldAppSvcSuplmGroupDto" value="${oldAppSvcSuplmFormDto.appSvcSuplmGroupDtoList[status.index]}"/>
                    <c:set var="batchSize" value="${appSvcSuplmGroupDto.count}"/>
                    <c:if test="${batchSize > 0}">
                        <c:forEach var="item" items="${appSvcSuplmGroupDto.appSvcSuplmItemDtoList}" varStatus="statuss">
                            <c:set var="oldItem" value="${oldAppSvcSuplmGroupDto.appSvcSuplmItemDtoList[statuss.index]}"/>
                            <c:if test="${item.display}">
                                <%@ include file="viewItem.jsp" %>
                            </c:if>
                        </c:forEach>
                    </c:if>
                </c:forEach>
                </div>
            </div>
            </c:forEach>
            <%--</table>--%>
        </div>
    </div>
</div>

<script>
    $(function () {
        $('.mandatory').hide()
    })
</script>