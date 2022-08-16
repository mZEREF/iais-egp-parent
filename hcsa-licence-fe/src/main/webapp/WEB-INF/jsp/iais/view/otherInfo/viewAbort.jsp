<c:set var="row_total_d" value="0"></c:set>
<c:forEach var="person" items="${currentPreviewSvcInfo.appSvcOtherInfoDto.appSvcOtherInfoAbortDtoList}" varStatus="s">
    <c:set var="row_count_d" value="${row_total_d+1}" />
    <c:if test="${'1' == person.topType}">
        <c:set var="row_total_d" value="${row_count_d}" />
    </c:if>
</c:forEach>
${row_total_d}

<c:set var="row_total_p" value="0"></c:set>
<c:forEach var="person" items="${currentPreviewSvcInfo.appSvcOtherInfoDto.appSvcOtherInfoAbortDtoList}" varStatus="s">
    <c:set var="row_count_p" value="${row_total_p+1}" />
    <c:if test="${'0' == person.topType}">
        <c:set var="row_total_p" value="${row_count_p}" />
    </c:if>
</c:forEach>
${row_total_p}
<c:set var="row_total_a" value="0"></c:set>
<c:forEach var="person" items="${currentPreviewSvcInfo.appSvcOtherInfoDto.appSvcOtherInfoAbortDtoList}" varStatus="s">
    <c:set var="row_count_a" value="${row_total_a+1}" />
    <c:if test="${'-1' == person.topType}">
        <c:set var="row_total_a" value="${row_count_a}" />
    </c:if>
</c:forEach>
<c:forEach var="person" items="${currentPreviewSvcInfo.appSvcOtherInfoDto.appSvcOtherInfoAbortDtoList}" varStatus="status">
    <c:if test="${'0' != currentPreviewSvcInfo.appSvcOtherInfoDto.appSvcOtherInfoTopDto.topType}">
        <c:if test="${'1' == person.topType}">
            <div class="topByDrug <c:if test="${'0' == currentPreviewSvcInfo.appSvcOtherInfoDto.appSvcOtherInfoTopDto.topType}">hidden</c:if>">
                <iais:row>
                    <div class="col-xs-12 col-md-10">
                        <p class="bold">TOP (BY Drug)&nbsp;<c:if test="${row_total_d > 1}">${status.index+1}</c:if></p>
                    </div>
                </iais:row>
                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" mandatory="true" value="Year."/>
                    <iais:value width="6" cssClass="col-md-6" display="true">
                        <c:out value="${person.year}" />
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" mandatory="true" value="No. of abortions"/>
                    <iais:value width="6" cssClass="col-md-6" display="true">
                        <c:out value="${person.abortNum}" />
                    </iais:value>
                </iais:row>
            </div>
        </c:if>
    </c:if>
</c:forEach>

<c:forEach var="person" items="${currentPreviewSvcInfo.appSvcOtherInfoDto.appSvcOtherInfoAbortDtoList}" varStatus="pstatus">
    <c:if test="${'1' != currentPreviewSvcInfo.appSvcOtherInfoDto.appSvcOtherInfoTopDto.topType}">
        <c:if test="${'0' == person.topType}">
            <div class="topBySurgicalProcedure <c:if test="${'1' == currentPreviewSvcInfo.appSvcOtherInfoDto.appSvcOtherInfoTopDto.topType}">hidden</c:if>">
                <iais:row>
                    <div class="col-xs-12 col-md-10">
                        <p class="bold">TOP (By Surgical Procedure)&nbsp;<c:if test="${row_total_p > 1}">${pstatus.index-row_total_d+1}</c:if></p>
                    </div>
                </iais:row>
                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" mandatory="true" value="Year."/>
                    <iais:value width="6" cssClass="col-md-6" display="true">
                        <c:out value="${person.year}" />
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" mandatory="true" value="No. of abortions"/>
                    <iais:value width="6" cssClass="col-md-6" display="true">
                        <c:out value="${person.abortNum}" />
                    </iais:value>
                </iais:row>
            </div>
        </c:if>
    </c:if>
</c:forEach>

<c:forEach var="person" items="${currentPreviewSvcInfo.appSvcOtherInfoDto.appSvcOtherInfoAbortDtoList}" varStatus="astatus">
    <c:if test="${'-1' == currentPreviewSvcInfo.appSvcOtherInfoDto.appSvcOtherInfoTopDto.topType}">
        <c:if test="${'-1' == person.topType}">
            <div class="topByDrugandSurgicalProcedure">
                <iais:row>
                    <div class="col-xs-12 col-md-10">
                        <p class="bold">TOP (By Drug and Surgical Procedure)&nbsp;<c:if test="${row_total_a > 1}">${astatus.index-row_total_d-row_total_p+1}</c:if></p>
                    </div>
                </iais:row>
                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" mandatory="true" value="Year."/>
                    <iais:value width="6" cssClass="col-md-6" display="true">
                        <c:out value="${person.year}" />
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" mandatory="true" value="No. of abortions"/>
                    <iais:value width="6" cssClass="col-md-6" display="true">
                        <c:out value="${person.abortNum}" />
                    </iais:value>
                </iais:row>
            </div>
        </c:if>
    </c:if>
</c:forEach>
