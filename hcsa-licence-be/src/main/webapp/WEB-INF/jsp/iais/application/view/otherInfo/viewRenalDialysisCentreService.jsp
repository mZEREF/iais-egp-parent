<div class="amend-preview-info form-horizontal min-row">
    <iais:row>
        <iais:field width="5" value="Nurses per Shift"/>
        <iais:value width="3" cssClass="col-md-7">
            <c:out value="${appSvcOtherInfoDto.appSvcOtherInfoNurseDto.perShiftNum}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Total number of dialysis stations"/>
        <iais:value width="3" cssClass="col-md-7">
            <c:out value="${appSvcOtherInfoDto.appSvcOtherInfoNurseDto.dialysisStationsNum}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Number of Hep B stations"/>
        <iais:value width="3" cssClass="col-md-7">
            <c:out value="${appSvcOtherInfoDto.appSvcOtherInfoNurseDto.helpBStationNum}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Is the clinic open to general public?"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:if test="${'1' == appSvcOtherInfoDto.appSvcOtherInfoNurseDto.isOpenToPublic}">Yes</c:if>
            <c:if test="${'0' == appSvcOtherInfoDto.appSvcOtherInfoNurseDto.isOpenToPublic}">No</c:if>
        </iais:value>
    </iais:row>
</div>