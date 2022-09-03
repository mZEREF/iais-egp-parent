<c:set var="orgUse" value="${appSvcOtherInfoDto.orgUserDto}"/>
<div class="amended-service-info-gp">
    <iais:row>
        <div class="col-xs-12">
            <p><strong>Yellow Fever Vaccination </strong></p>
        </div>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Professional Regn. No."/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:out value="${appSvcOtherInfoDto.provideYfVs}" />
        </iais:value>
    </iais:row>

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
            <c:out value="${orgUse.email}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Applicant Name"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:out value="${orgUse.displayName}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Designation"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:out value="${orgUse.designation}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Contact number"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:out value="${orgUse.mobileNo}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Date of Commencement"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:out value="${appSvcOtherInfoDto.yfCommencementDateStr}" />
        </iais:value>
    </iais:row>

</div>






