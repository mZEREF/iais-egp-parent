<iais:row>
    <div class="col-xs-12">
        <p><strong>Documentation</strong></p>
    </div>
</iais:row>
<iais:row cssClass="row control control-caption-horizontal">
    <iais:field width="6" cssClass="col-md-6" value="Outcome of procedures are recorded "/>
    <iais:value width="6" cssClass="col-md-6">
        <c:if test="${true == currentPreviewSvcInfo.appSvcOtherInfoDto.appSvcOtherInfoTopDto.isOutcomeProcRecord}">Yes</c:if>
        <c:if test="${false == currentPreviewSvcInfo.appSvcOtherInfoDto.appSvcOtherInfoTopDto.isOutcomeProcRecord}">No</c:if>
    </iais:value>

</iais:row>

<iais:row>
    <iais:field width="6" cssClass="col-md-6" value="Number of cases with complications, if any"/>
    <iais:value width="3" cssClass="col-md-6" display="true">
        <c:out value="${currentPreviewSvcInfo.appSvcOtherInfoDto.appSvcOtherInfoTopDto.compCaseNum}" />
    </iais:value>
</iais:row>

<iais:row>
    <iais:field width="6" cssClass="col-md-12" value="Statistics on abortion (For renewal application only)"/>
</iais:row>

<iais:row>
    <iais:field width="6" cssClass="col-md-12" value="Number of abortions performed during the previous 2 years"/>
</iais:row>