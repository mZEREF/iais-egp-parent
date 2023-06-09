<c:if test="${'1' == appSvcOtherInfoDto.provideTop}">
    <iais:row>
        <div class="col-xs-12">
            <p><strong>Documentation</strong></p>
        </div>
    </iais:row>
    <div class="amend-preview-info form-horizontal min-row">
        <iais:row cssClass="row control control-caption-horizontal">
            <iais:field width="5" value="Outcome of procedures are recorded "/>
            <iais:value width="3" cssClass="col-md-7">
                <c:if test="${true == appSvcOtherInfoDto.appSvcOtherInfoTopDto.outcomeProcRecord}">Yes</c:if>
                <c:if test="${false == appSvcOtherInfoDto.appSvcOtherInfoTopDto.outcomeProcRecord}">No</c:if>
            </iais:value>

        </iais:row>

        <iais:row>
            <iais:field width="5" value="Number of cases with complications, if any"/>
            <iais:value width="3" cssClass="col-md-7" display="true">
                <c:out value="${appSvcOtherInfoDto.appSvcOtherInfoTopDto.compCaseNum}" />
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" value="Statistics on abortion (For renewal application only)"/>
        </iais:row>

        <iais:row>
            <iais:field width="5" value="Number of abortions performed during the previous 2 years"/>
        </iais:row>
    </div>
</c:if>
