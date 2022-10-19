<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="amended-service-info-gp">
    <label class="title-font-size">${currStepName}</label>
    <div class="amend-preview-info">
        <div class="form-check-gp">
                <c:set var="appSvcSuplmFormList" value="${currentPreviewSvcInfo.appSvcSuplmFormList}"/>
                 <c:set var="oldAppSvcSuplmFormList" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcSuplmFormList}"/>
                <c:forEach var="appSvcSuplmFormDto" items="${appSvcSuplmFormList}" varStatus="statute">
                <c:set var="oldAppSvcSuplmFormDto" value="${oldAppSvcSuplmFormList[statute.index]}"/>
                <iais:row>
                    <div class="">
                        <div class="app-title">${appSvcSuplmFormDto.premName}</div>
                        <p class="font-18 bold">${appSvcSuplmFormDto.premAddress}</p>
                    </div>
                </iais:row>

            <div class="row">
                <div>
                <c:forEach var="appSvcSuplmGroupDto" items="${appSvcSuplmFormDto.appSvcSuplmGroupDtoList}" varStatus="status">
                    <table class="col-xs-12">
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
                    </table>
                </c:forEach>
                </div>
            </div>
                    </table>
            </c:forEach>
        </div>
    </div>
</div>

<script>
    $(function () {
        $('.mandatory').hide()
    })
</script>