<div class="preview-info">
    <iais:row>
        <div class="col-xs-12">
            <p><strong>Secondary Address <c:if test="${appGrpPremDto.appGrpSecondAddrDtos.size() > 1}"> ${statuss.index+1}</c:if>:</strong></p>
        </div>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Postal Code"/>
        <iais:value width="7" display="true">
            <c:out value="${appGrpSecondAddrDto.postalCode}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Address Type"/>
        <iais:value width="7" display="true">
            <iais:code code="${appGrpSecondAddrDto.addrType}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Block / House No."/>
        <iais:value width="7" display="true">
            <c:out value="${appGrpSecondAddrDto.blkNo}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Floor / Unit No."/>
        <iais:value width="7" display="true">
            <c:out value="${appGrpSecondAddrDto.floorNo}"/>-<c:out value="${appGrpSecondAddrDto.unitNo}"/>
        </iais:value>
    </iais:row>
    <c:forEach var="appPremisesOperationalUnits" items="${appGrpSecondAddrDto.appPremisesOperationalUnitDtos}">
        <iais:row>
            <iais:field width="5" value=""/>
            <iais:value width="7" display="true">
                <c:out value="${appPremisesOperationalUnits.floorNo}"/>-<c:out value="${appPremisesOperationalUnits.unitNo}"/>
            </iais:value>
        </iais:row>
    </c:forEach>
    <iais:row>
        <iais:field width="5" value="Street Name"/>
        <iais:value width="7" display="true">
            <c:out value="${appGrpSecondAddrDto.streetName}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Building Name"/>
        <iais:value width="7" display="true">
            <c:out value="${appGrpSecondAddrDto.buildingName}"/>
        </iais:value>
    </iais:row>
</div>