<c:set var="orgUse" value="${appSvcOtherInfoDto.orgUserDto}"/>
<div class="">
<%--    <iais:row>--%>
<%--        <div class="col-xs-12">--%>
<%--            <p><strong>Yellow Fever Vaccination </strong></p>--%>
<%--        </div>--%>
<%--    </iais:row>--%>

    <iais:row>
        <iais:field width="5" value="Do you provide Yellow Fever Vaccination Service"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:if test="${appSvcOtherInfoDto.provideYfVs == 1}">Yes</c:if>
            <c:if test="${appSvcOtherInfoDto.provideYfVs == 0}">No</c:if>
        </iais:value>
    </iais:row>

    <c:if test="${appSvcOtherInfoDto.provideYfVs == 1}">
        <iais:row>
            <iais:field width="5" value="Business Name"/>
            <iais:value width="7" cssClass="col-md-7" display="true">
                <c:forEach var="docShowDto" items="${currentPreviewSvcInfo.appSvcBusinessDtoList}" varStatus="stat">
                    <c:if test="${stat.index != 0}">
                        <c:if test="${stat.index != stat.index-1}">,</c:if>
                    </c:if>
                    <c:out value="${docShowDto.businessName}"/>
                </c:forEach>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" value="Address"/>
            <iais:value width="3" cssClass="col-md-7" display="true">
                <c:out value="${appSvcOtherInfoDto.premAddress}"/>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" value="Applicant Name"/>
            <iais:value width="3" cssClass="col-md-7" display="true">
<%--                <c:out value="${orgUse.displayName}"/>--%>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" value="Designation"/>
            <iais:value width="3" cssClass="col-md-7" display="true">
<%--                <iais:code code="${orgUse.designation}"/>--%>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" value="Contact number"/>
            <iais:value width="3" cssClass="col-md-7" display="true">
<%--                <c:out value="${orgUse.mobileNo}"/>--%>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" value="Date of Commencement"/>
            <iais:value width="3" cssClass="col-md-7" display="true">
                <c:out value="${appSvcOtherInfoDto.yfCommencementDateStr}" />
            </iais:value>
        </iais:row>
    </c:if>
</div>






