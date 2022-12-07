<div class="row form-horizontal">
    <c:set var="canEdit" value="${AppSubmissionDto.appEditSelectDto.serviceEdit}"/>
    <c:forEach var="appGrpSecondAddrList" items="${AppSubmissionDto.appGrpPremisesDtoList}" varStatus="status">
        <div class="col-xs-12">
            <c:set value="address" var="prefix"/>
            <c:if test="${empty appGrpSecondAddrList.appGrpSecondAddrDtos}">
                <div class="contents">
                    <%@include file="secondAddressDetail.jsp" %>
                </div>
            </c:if>
            <c:forEach var="appGrpSecondAddr" items="${appGrpSecondAddrList.appGrpSecondAddrDtos}" varStatus="statuss">
                <div class="contents">
                    <%@include file="secondAddressDetail.jsp" %>
                </div>
            </c:forEach>
        </div>
    </c:forEach>
</div>
