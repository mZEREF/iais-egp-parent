<c:set var="permanent" value="PERMANENT" />
<c:set var="conv" value="CONVEYANCE" />
<c:set var="easMts" value="EASMTS" />
<c:set var="mobile" value="MOBILE" />
<c:set var="remote" value="REMOTE" />

<c:set var="premType" value="${appGrpPremDto.premisesType}" />

<div class="preview-info">
    <iais:row>
        <iais:field width="5" value="Mode of Service Delivery"/>
        <iais:value width="7" display="true">
            <c:if test="${premType == permanent}">
                Permanent Premises
            </c:if>
            <c:if test="${premType == conv}">
                Conveyance
            </c:if>
            <c:if test="${premType == easMts}">
                Conveyance (in a mobile clinic / ambulance)
            </c:if>
            <c:if test="${premType == mobile}">
                Mobile Delivery
            </c:if>
            <c:if test="${premType == remote}">
                Remote Delivery
            </c:if>
        </iais:value>
    </iais:row>

    <c:if test="${premType == permanent}">
        <iais:row>
            <iais:field width="5" value="Fire Safety & Shelter Bureau Ref. No."/>
            <iais:value width="7" display="true">
                <c:out value="${appGrpPremDto.scdfRefNo}"/>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field width="5" value="Fire Safety Certificate Issued Date"/>
            <iais:value width="7" display="true">
                <c:out value="${appGrpPremDto.certIssuedDtStr}"/>
            </iais:value>
        </iais:row>
    </c:if>
    <c:if test="${premType == conv}">
        <iais:row>
            <iais:field width="5" value="Vehicle No."/>
            <iais:value width="7" display="true">
                <c:out value="${appGrpPremDto.vehicleNo}"/>
            </iais:value>
        </iais:row>
    </c:if>
    <iais:row>
        <iais:field width="5" value="Business Name"/>
        <iais:value width="7" display="true">
            <c:out value="${appGrpPremDto.hciName}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Postal Code"/>
        <iais:value width="7" display="true">
            <c:out value="${appGrpPremDto.postalCode}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Address Type"/>
        <iais:value width="7" display="true">
            <iais:code code="${appGrpPremDto.addrType}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Block / House No."/>
        <iais:value width="7" display="true">
            <c:out value="${appGrpPremDto.blkNo}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Floor / Unit No."/>
        <iais:value width="7" display="true">
            <c:out value="${appGrpPremDto.floorNo}"/>-<c:out value="${appGrpPremDto.unitNo}"/>
        </iais:value>
    </iais:row>
    <c:forEach var="appPremisesOperationalUnit" items="${appGrpPremDto.appPremisesOperationalUnitDtos}">
        <iais:row>
            <iais:field width="5" value=""/>
            <iais:value width="7" display="true">
                <c:out value="${appPremisesOperationalUnit.floorNo}"/>-<c:out value="${appPremisesOperationalUnit.unitNo}"/>
            </iais:value>
        </iais:row>
    </c:forEach>
    <iais:row>
        <iais:field width="5" value="Street Name"/>
        <iais:value width="7" display="true">
            <c:out value="${appGrpPremDto.streetName}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Building Name"/>
        <iais:value width="7" display="true">
            <c:out value="${appGrpPremDto.buildingName}"/>
        </iais:value>
    </iais:row>
    <c:if test="${premType == easMts}">
        <iais:row>
            <iais:field width="5" value="For public/in-house use only?"/>
            <iais:value width="7" display="true">
                <iais:code code="${appGrpPremDto.easMtsUseOnly}"/>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field width="5" value="Public Email"/>
            <iais:value width="7" display="true">
                <c:out value="${appGrpPremDto.easMtsPubEmail}"/>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field width="5" value="Public Hotline"/>
            <iais:value width="7" display="true">
                <c:out value="${appGrpPremDto.easMtsPubHotline}"/>
            </iais:value>
        </iais:row>
    </c:if>

    <iais:row>
        <iais:field value="Co-Location Service" width="10" />
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Are you co-locating with a service that is licensed under HCSA?"/>
        <iais:value width="7" display="true">
            <c:choose>
                <c:when test="${appGrpPremDto.locateWtihHcsa == '1'}">Yes</c:when>
                <c:when test="${appGrpPremDto.locateWtihHcsa == '0'}">No</c:when>
            </c:choose>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Are you co-locating with a service that is not licensed under HCSA?"/>
        <iais:value width="7" display="true">
            <c:choose>
                <c:when test="${appGrpPremDto.locateWtihNonHcsa == '1'}">Yes</c:when>
                <c:when test="${appGrpPremDto.locateWtihNonHcsa == '0'}">No</c:when>
            </c:choose>
        </iais:value>
    </iais:row>
    <c:if test="${appGrpPremDto.locateWtihNonHcsa == '1'}">
        <iais:row>
            <table aria-describedby="" class="col-xs-12" border="0">
                <thead style="display: none">
                    <tr>
                        <th scope="col" width="50%">Business Name</th>
                        <th scope="col" width="50%">Services Provided</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach var="relatedDto" items="${appGrpPremDto.appPremNonLicRelationDtos}" >
                    <tr>
                        <td><c:out value="${relatedDto.businessName}"/></td>
                        <td><c:out value="${relatedDto.providedService}"/></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </iais:row>
    </c:if>
</div>
