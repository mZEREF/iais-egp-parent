<div class="amend-preview-info form-horizontal min-row">
    <iais:row>
        <div class="col-xs-12 col-md-6">
            <p class="bold">AmBulatorySurgicalCentreService Other Information</p>
        </div>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="GFA Value (in sqm)"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:out value="${appSvcOtherInfoDto.appSvcOtherInfoMedDto1.gfaValue}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="I declare that I have met URA's requirements for gross floor area"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            Yes
        </iais:value>
    </iais:row>
</div>
