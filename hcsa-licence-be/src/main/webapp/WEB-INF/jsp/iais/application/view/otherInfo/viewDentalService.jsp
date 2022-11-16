<div class="">
    <iais:row>
        <iais:field width="5" value="Type of medical records"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:if test="${appSvcOtherInfoDto.appSvcOtherInfoMedDto.medicalTypeIt eq true}">
                IT System
            </c:if>
            <c:if test="${appSvcOtherInfoDto.appSvcOtherInfoMedDto.medicalTypePaper eq true}">
                Paper cards
            </c:if>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="List of options for IT system and paper cards / IT system only"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:if test="${appSvcOtherInfoDto.appSvcOtherInfoMedDto.systemOption == 'MED01'}">Clinic Assist</c:if>
            <c:if test="${appSvcOtherInfoDto.appSvcOtherInfoMedDto.systemOption == 'MED02'}">Gloco</c:if>
            <c:if test="${appSvcOtherInfoDto.appSvcOtherInfoMedDto.systemOption == 'MED03'}">GPConnect</c:if>
            <c:if test="${appSvcOtherInfoDto.appSvcOtherInfoMedDto.systemOption == 'MED04'}">Medi2000</c:if>
            <c:if test="${appSvcOtherInfoDto.appSvcOtherInfoMedDto.systemOption == 'MED05'}"> Freeware/ Shareware/ GP Self-developed Software</c:if>
            <c:if test="${appSvcOtherInfoDto.appSvcOtherInfoMedDto.systemOption == 'MED06'}">Others (please specify)</c:if>
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
            <c:if test="${true eq appSvcOtherInfoDto.appSvcOtherInfoMedDto.openToPublic}">Yes</c:if>
            <c:if test="${false eq appSvcOtherInfoDto.appSvcOtherInfoMedDto.openToPublic}">No</c:if>
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
