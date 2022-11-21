<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>

<c:set var="permanent" value="${ApplicationConsts.PREMISES_TYPE_PERMANENT}" />
<c:set var="conv" value="${ApplicationConsts.PREMISES_TYPE_CONVEYANCE}" />
<c:set var="easMts" value="${ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE}" />
<c:set var="mobile" value="${ApplicationConsts.PREMISES_TYPE_MOBILE}" />
<c:set var="remote" value="${ApplicationConsts.PREMISES_TYPE_REMOTE}" />
<c:set var="permanentShow" value="${ApplicationConsts.PREMISES_TYPE_PERMANENT_SHOW}" />
<c:set var="convShow" value="${ApplicationConsts.PREMISES_TYPE_CONVEYANCE_SHOW}" />
<c:set var="easMtsShow" value="${ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE_SHOW}" />
<c:set var="mobileShow" value="${ApplicationConsts.PREMISES_TYPE_MOBILE_SHOW}" />
<c:set var="remoteShow" value="${ApplicationConsts.PREMISES_TYPE_REMOTE_SHOW}" />

<c:set var="mosdName" value="${ApplicationConsts.MODE_OF_SVC_DELIVERY}" />

<c:set var="premType" value="${appGrpPremDto.premisesType}" />

<div class="preview-info">
    <iais:row>
        <iais:field width="5" value="${mosdName}"/>
        <iais:value width="7" display="true">
            <c:if test="${premType == permanent}">
                ${permanentShow}
            </c:if>
            <c:if test="${premType == conv}">
                ${convShow}
            </c:if>
            <c:if test="${premType == easMts}">
                ${easMtsShow}
            </c:if>
            <c:if test="${premType == mobile}">
                ${mobileShow}
            </c:if>
            <c:if test="${premType == remote}">
                ${remoteShow}
            </c:if>
        </iais:value>
    </iais:row>

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
    <c:if test="${premType == permanent}">
        <iais:row>
            <iais:field width="5" value="Fire Safety & Shelter Bureau Ref No."/>
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

    <c:if test="${premType == permanent || premType == conv}">
        <iais:row>
            <iais:field value="Co-Location Services" width="10" />
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

        <c:if test="${appGrpPremDto.locateWtihNonHcsa == '1'}" var="hasNonHcsa">
            <iais:row>
                <div class="col-xs-12">
                    <table class="table" aria-describedby="" border="0" style="margin:10px 0">
                        <thead>
                            <tr>
                                <td scope="col" class="non-hcsa-1">
                                    Are you co-locating with a service that is not licensed under HCSA?
                                </td>
                                <td scope="col" class="non-hcsa-2">Business Name</td>
                                <td scope="col" class="non-hcsa-3">Services Provided</td>
                            </tr>
                        </thead>
                        <tbody>
                        <c:set var="nonLicSize" value="${appGrpPremDto.appPremNonLicRelationDtos.size()}" />
                        <c:forEach var="relatedDto" items="${appGrpPremDto.appPremNonLicRelationDtos}" varStatus="nonLicVs">
                            <tr style="border-top: ${nonLicVs.first? 'solid silver' : '2px solid black'}">
                                <c:if test="${nonLicVs.first}">
                                <td rowspan="${nonLicSize}" class="non-hcsa-1">Yes</td>
                                </c:if>
                                <td class="non-hcsa-2"><c:out value="${relatedDto.businessName}"/></td>
                                <td class="non-hcsa-3"><c:out value="${relatedDto.providedService}"/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </iais:row>
        </c:if>
        <c:if test="${not hasNonHcsa}">
            <iais:row>
                <iais:field width="5" value="Are you co-locating with a service that is not licensed under HCSA?"/>
                <iais:value width="7" display="true">No</iais:value>
            </iais:row>
        </c:if>
    </c:if>
</div>
