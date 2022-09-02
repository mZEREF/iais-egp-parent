<div class="amend-preview-info form-horizontal min-row">
    <iais:row>
        <div class="col-xs-12 col-md-6">
            <p class="bold">DentalService Other Information</p>
        </div>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Type of medical records"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:if test="${appSvcOtherInfoDto.appSvcOtherInfoMedDto.isMedicalTypeIt != null}">
                <c:out value="${appSvcOtherInfoDto.appSvcOtherInfoMedDto.isMedicalTypeIt}" />&nbsp;&nbsp;
            </c:if>
            <c:if test="${appSvcOtherInfoDto.appSvcOtherInfoMedDto.isMedicalTypePaper != null}">
                <c:out value="${appSvcOtherInfoDto.appSvcOtherInfoMedDto.isMedicalTypePaper}" />
            </c:if>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="List of options for IT system and paper cards / IT system only"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:out value="${appSvcOtherInfoDto.appSvcOtherInfoMedDto.systemOption}" />
        </iais:value>
    </iais:row>

    <c:if test="${appSvcOtherInfoDto.appSvcOtherInfoMedDto.systemOption == 'MED06'}">
        <iais:row>
            <iais:field width="5" value="Please specify"/>
            <iais:value width="3" cssClass="col-md-7" display="true">
                <c:out value="${appSvcOtherInfoDto.appSvcOtherInfoMedDto.otherSystemOption}" />
            </iais:value>
        </iais:row>
    </c:if>


    <iais:row>
        <iais:field width="5" value="Is clinic open to general public?"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:if test="${'1' == appSvcOtherInfoDto.appSvcOtherInfoMedDto.isOpenToPublic}">Yes</c:if>
            <c:if test="${'0' == appSvcOtherInfoDto.appSvcOtherInfoMedDto.isOpenToPublic}">No</c:if>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="GFA Value (in sqm)"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:out value="${appSvcOtherInfoDto.appSvcOtherInfoMedDto.gfaValue}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="I declare that I have met URA's requirements for gross floor area"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            Yes
        </iais:value>
    </iais:row>
</div>
