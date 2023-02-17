<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<c:set var="permanent" value="${ApplicationConsts.PREMISES_TYPE_PERMANENT}"/>
<c:set var="conv" value="${ApplicationConsts.PREMISES_TYPE_CONVEYANCE}"/>
<c:set var="easMts" value="${ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE}"/>
<c:set var="mobile" value="${ApplicationConsts.PREMISES_TYPE_MOBILE}"/>
<c:set var="remote" value="${ApplicationConsts.PREMISES_TYPE_REMOTE}"/>
<c:set var="permanentShow" value="${ApplicationConsts.PREMISES_TYPE_PERMANENT_SHOW}"/>
<c:set var="convShow" value="${ApplicationConsts.PREMISES_TYPE_CONVEYANCE_SHOW}"/>
<c:set var="easMtsShow" value="${ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE_SHOW}"/>
<c:set var="mobileShow" value="${ApplicationConsts.PREMISES_TYPE_MOBILE_SHOW}"/>
<c:set var="remoteShow" value="${ApplicationConsts.PREMISES_TYPE_REMOTE_SHOW}"/>

<c:set var="mosdName" value="${ApplicationConsts.MODE_OF_SVC_DELIVERY}"/>

<style>
  .wrap-line{
    word-break: keep-all;
    word-wrap: break-word;
    white-space: pre-wrap;
  }
</style>

<div class="panel panel-default">
  <div class="panel-heading" id="headingPremise" role="tab">
    <h4 class="panel-title">
        <a class="collapsed" role="button" data-toggle="collapse" href="#collapsePremise"
                                aria-expanded="true" aria-controls="collapsePremise">
            <c:out value="${mosdName}" />
        </a>
    </h4>
  </div>
  <div class="panel-collapse collapse" id="collapsePremise" role="tabpanel"
       aria-labelledby="headingPremise">
    <div class="panel-body">
      <p class="text-right">
        <c:if test="${rfi=='rfi' && (appEdit.premisesEdit || appEdit.premisesListEdit)&& canEidtPremise }">
          <input class="form-check-input" id="premisesCheckbox" type="checkbox"
                 name="editCheckbox" <c:if test="${pageEdit.premisesEdit}">checked</c:if> aria-invalid="false" value="premises">
        </c:if>
      </p>
      <c:forEach var="appGrpPremDto" items="${appSubmissionDto.appGrpPremisesDtoList}"
                 varStatus="status">
        <c:set var="oldAppGrpPremDto" value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index]}"/>
        <div class="panel-main-content postion-relative">
          <div class="preview-info">
            <div class="row">
              <div class="col-md-6">
                <div class="app-title">${mosdName} ${appGrpPremDto.seqNum}</div>
              </div>
            </div>

            <div class="row">
              <div class="col-md-6">
                ${mosdName}
              </div>
              <div class="col-md-6">
                <div class="col-md-6">
                  <span class="newVal" attr="${appGrpPremDto.premisesType}">
                    <c:if test="${appGrpPremDto.premisesType == permanent}">
                        ${permanentShow}
                    </c:if>
                    <c:if test="${appGrpPremDto.premisesType == conv}">
                        ${convShow}
                    </c:if>
                    <c:if test="${appGrpPremDto.premisesType == easMts}">
                        ${easMtsShow}
                    </c:if>
                    <c:if test="${appGrpPremDto.premisesType == mobile}">
                        ${mobileShow}
                    </c:if>
                    <c:if test="${appGrpPremDto.premisesType == remote}">
                        ${remoteShow}
                    </c:if>
                </div>
                <div class="col-md-6">
                  <span class="oldVal" attr="${oldAppGrpPremDto.premisesType}" style="display: none">
                     <c:if test="${oldAppGrpPremDto.premisesType == permanent}">
                       ${permanentShow}
                     </c:if>
                    <c:if test="${oldAppGrpPremDto.premisesType == conv}">
                      ${convShow}
                    </c:if>
                    <c:if test="${oldAppGrpPremDto.premisesType == easMts}">
                      ${easMtsShow}
                    </c:if>
                    <c:if test="${oldAppGrpPremDto.premisesType == mobile}">
                      ${mobileShow}
                    </c:if>
                    <c:if test="${oldAppGrpPremDto.premisesType == remote}">
                      ${remoteShow}
                    </c:if>
                  </span>
                </div>
              </div>
            </div>
            <c:if test="${conv == appGrpPremDto.premisesType || conv == oldAppGrpPremDto.premisesType}">
              <div class="row">
                <div class="col-md-6"> Vehicle No.</div>
                <div class="col-md-6">
                  <div  class="col-md-6">
                    <span class="newVal" attr="<c:out value="${appGrpPremDto.vehicleNo}"/>">
                      <c:out value="${appGrpPremDto.vehicleNo}"/>
                    </span>
                  </div>
                  <div  class="col-md-6">
                    <span class="oldVal" attr="<c:out value="${oldAppGrpPremDto.vehicleNo}"/>" style="display: none">
                      <c:out value="${appGrpPremDto.vehicleNo}"/>
                    </span>
                  </div>
                </div>
              </div>
            </c:if>
            <div class="row">
              <div class="col-md-6">
                Business Name
                <a class="btn-tooltip styleguide-tooltip" id="hciNameClick" <c:if test="${empty appGrpPremDto.applicationViewHciNameDtos}">style="display: none" </c:if> data-toggle="tooltip" data-html="true" title="" data-original-title="">i</a>
              </div>
              <div class="col-md-7" style="position: absolute;z-index: 100;left: 40%;background-color: #EEEEEE;display: none;margin-top: 2%;overflow-y: scroll" id="hciNameShowOrHidden">
                <p>The  Business Name is currently used by another licensee</p>
                <br>
                <table aria-describedby="" border="1px" style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;text-align: center;background-color: #ffffff;width: 100%">
                  <tr>
                    <th scope="col" class="col-md-4">Name of<br>Licensee</th>
                    <th scope="col" class="col-md-4">Business Name</th>
                    <th scope="col" class="col-md-4">Service Name</th>
                  </tr>
                  <c:forEach items="${appGrpPremDto.applicationViewHciNameDtos}" var="applicationViewHciNameDtos">
                    <tr>
                      <td class="wrap-line">${applicationViewHciNameDtos.licensee}</td>
                      <td class="wrap-line">${applicationViewHciNameDtos.hciName}</td>
                      <td class="wrap-line">${applicationViewHciNameDtos.serviceName}</td>
                    </tr>
                  </c:forEach>
                </table>
              </div>

              <div class="col-md-6">
                <div class="col-md-12">
                  <span class="newVal" attr="${appGrpPremDto.hciName}"><c:out value="${appGrpPremDto.hciName}"/></span>
                  <br>
                  <span class="oldVal" attr="${oldAppGrpPremDto.hciName}" style="display: none">
                    <c:out value="${oldAppGrpPremDto.hciName}"/>
                  </span>
                </div>
              </div>
            </div>
            <div class="row">
              <div class="col-md-6">
                Postal Code
                <a class="btn-tooltip styleguide-tooltip" id="addressClick" <c:if test="${empty appGrpPremDto.applicationViewAddress}">style="display: none" </c:if> data-toggle="tooltip" data-html="true" title="" data-original-title="">i</a>
              </div>
                <div class="col-md-7"  style="position: absolute;z-index: 100;left: 40%;background-color: #EEEEEE;margin-top:2%;display: none;overflow-y: scroll;" id="addressShowOrHidden">
                    <p>The address of the mode of service delivery keyed in by applicant is currently used by another licensee</p>
                    <table aria-describedby="" border="1px" style="border-collapse: collapse;border-top: 0 solid #000000;padding: 8px;text-align: center;background-color: #ffffff;width: 100%">
                      <tr>
                        <th scope="col"  class="col-md-4">Name of<br>Licensee</th>
                        <th scope="col"  class="col-md-4">Business Name</th>
                        <th scope="col"  class="col-md-4">Service Name</th>
                      </tr>
                      <c:forEach items="${appGrpPremDto.applicationViewAddress}" var="applicationViewAddress">
                      <tr>
                        <td class="wrap-line">${applicationViewAddress.licensee}</td>
                        <td class="wrap-line">${applicationViewAddress.hciName}</td>
                        <td class="wrap-line">${applicationViewAddress.serviceName}</td>
                      </tr>
                      </c:forEach>
                    </table>
                </div>
              <div class="col-md-6">
                <div  class="col-md-6">
                  <span class="newVal" attr="${appGrpPremDto.postalCode}"><c:out value="${appGrpPremDto.postalCode}"/></span>
                </div>
                <div  class="col-md-6">
                  <span class="oldVal" attr="${oldAppGrpPremDto.postalCode}" style="display: none"><c:out value="${oldAppGrpPremDto.postalCode}"/></span>
                </div>
              </div>
            </div>

            <div class="row">
              <div class="col-md-6">
                Address Type
              </div>
              <div class="col-md-6">
                <div class="col-md-6">
                  <span class="newVal" attr="${appGrpPremDto.addrType}">
                    <iais:code code="${appGrpPremDto.addrType}"/>
                  </span>
                </div>
                <div class="col-md-6">
                  <span class="oldVal" attr="${oldAppGrpPremDto.addrType}" style="display: none">
                    <iais:code code="${oldAppGrpPremDto.addrType}"/>
                  </span>
                </div>
              </div>
            </div>
            <div class="row">
              <div class="col-md-6">
                Block / House No.
              </div>
              <div class="col-md-6">
                <div class="col-md-6">
                  <span class="newVal" attr="${appGrpPremDto.blkNo}"><c:out value="${appGrpPremDto.blkNo}"/></span>
                </div>
                <div class="col-md-6">
                  <span class="oldVal" attr="${oldAppGrpPremDto.blkNo}" style="display: none"><c:out value="${oldAppGrpPremDto.blkNo}"/></span>
                </div>
              </div>
            </div>

            <div class="row">
              <div class="col-md-6">
                Floor / Unit No.
              </div>
              <div class="col-md-6">
                <div class="col-md-6">
                   <span class="newVal" attr="${appGrpPremDto.floorNo}${appGrpPremDto.unitNo}">
                     <c:out value="${appGrpPremDto.floorNo}-${appGrpPremDto.unitNo}"/>
                   </span>
                </div>
                <div class="col-md-6">
                   <span class="oldVal" attr="${oldAppGrpPremDto.floorNo}${oldAppGrpPremDto.unitNo}" style="display: none">
                     <c:out value="${oldAppGrpPremDto.floorNo}-${oldAppGrpPremDto.unitNo}"/>
                   </span>
                </div>
              </div>
            </div>

            <c:forEach items="${appGrpPremDto.appPremisesOperationalUnitDtos}" var="appPremisesOperationalUnitDto" varStatus="unitIndex">
              <div class="row">
                <div class="col-md-6">
                </div>
                <div class="col-md-6">
                  <div class="col-md-6">
                    <span class="newVal" attr="${appPremisesOperationalUnitDto.floorNo}${appPremisesOperationalUnitDto.unitNo}">
                      <c:choose>
                        <c:when test="${appPremisesOperationalUnitDto.floorNo!=null &&  appPremisesOperationalUnitDto.unitNo!=null}">
                          <c:out value="${appPremisesOperationalUnitDto.floorNo}-${appPremisesOperationalUnitDto.unitNo}"/>
                        </c:when>
                        <c:otherwise>
                          <c:out value="${appPremisesOperationalUnitDto.floorNo}${appPremisesOperationalUnitDto.unitNo}"/>
                        </c:otherwise>
                      </c:choose>
                    </span>
                  </div>
                  <div class="col-md-6">
                    <span class="oldVal" attr="${oldAppGrpPremDto.appPremisesOperationalUnitDtos[unitIndex.index].floorNo}${oldAppGrpPremDto.appPremisesOperationalUnitDtos[unitIndex.index].unitNo}" style="display: none">
                      <c:choose>
                        <c:when test="${oldAppGrpPremDto.appPremisesOperationalUnitDtos[unitIndex.index].floorNo!=null && oldAppGrpPremDto.appPremisesOperationalUnitDtos[unitIndex.index].unitNo!=null}">
                          <c:out value="${oldAppGrpPremDto.appPremisesOperationalUnitDtos[unitIndex.index].floorNo}-${oldAppGrpPremDto.appPremisesOperationalUnitDtos[unitIndex.index].unitNo}"/>
                        </c:when>
                        <c:otherwise>
                          <c:out value="${oldAppGrpPremDto.appPremisesOperationalUnitDtos[unitIndex.index].floorNo}${oldAppGrpPremDto.appPremisesOperationalUnitDtos[unitIndex.index].unitNo}"/>
                        </c:otherwise>
                      </c:choose>
                    </span>
                  </div>
                </div>
              </div>
            </c:forEach>


            <div class="row">
              <div class="col-md-6">
                Street Name
              </div>
              <div class="col-md-6">
                <div class="col-md-12">
                   <span class="newVal" attr="<c:out value="${appGrpPremDto.streetName}"/>">
                       <c:out value="${appGrpPremDto.streetName}"/>
                   </span>
                   <br>
                   <span class="oldVal" attr="<c:out value="${oldAppGrpPremDto.streetName}"/>" style="display: none">
                       <c:out value="${oldAppGrpPremDto.streetName}"/>
                   </span>
                </div>
              </div>

            </div>
            <div class="row">
              <div class="col-md-6">
                Building Name
              </div>
              <div class="col-md-6">
                <div class="col-md-12">
                  <span class="newVal" attr="<c:out value="${appGrpPremDto.buildingName}"/>">
                    <c:out value="${appGrpPremDto.buildingName}"/>
                  </span>
                  <br>
                  <span class="oldVal" attr="<c:out value="${oldAppGrpPremDto.buildingName}"/>" style="display: none">
                    <c:out value="${oldAppGrpPremDto.buildingName}"/>
                  </span>
                </div>
                <div class="col-md-6">

                </div>
              </div>
            </div>
            <c:if test="${permanent == appGrpPremDto.premisesType || permanent == oldAppGrpPremDto.premisesType}">
              <div class="row">
                <div class="col-md-6">
                  Fire Safety & Shelter Bureau Ref No.
                </div>
                <div class="col-md-6">
                  <div class="col-md-12">
                    <span class="newVal" attr="${appGrpPremDto.scdfRefNo}"><c:out value="${appGrpPremDto.scdfRefNo}"/></span>
                    <br>
                    <span class="oldVal" attr="${oldAppGrpPremDto.scdfRefNo}" style="display: none">
                      <c:out value="${oldAppGrpPremDto.scdfRefNo}"/>
                    </span>
                  </div>

                </div>
              </div>
              <div class="row">
                <div class="col-md-6">
                  Fire Safety Certificate Issued Date
                </div>
                <div class="col-md-6">
                  <div class="col-md-6">
                    <span class="newVal" attr="${appGrpPremDto.certIssuedDtStr}">
                      <c:out value="${appGrpPremDto.certIssuedDtStr}"/>
                    </span>
                  </div>
                  <div class="col-md-6">
                    <span class="oldVal" attr="${oldAppGrpPremDto.certIssuedDtStr}" style="display: none">
                      <c:out value="${oldAppGrpPremDto.certIssuedDtStr}"/>
                      </span>
                  </div>
                </div>
              </div>
            </c:if>
            <c:if test="${appGrpPremDto.premisesType == permanent || appGrpPremDto.premisesType == conv
                || oldAppGrpPremDto.premisesType == permanent || oldAppGrpPremDto.premisesType == conv}">
              <div class="row">
                <div class="col-md-12 bold">
                  Co-Location Services
                </div>
              </div>
              <div class="row">
                <div class="col-md-6">
                  Are you co-locating with a service that is licensed under HCSA?
                </div>
                <div class="col-md-6">
                  <div class="col-md-6">
                    <span class="newVal" attr="${appGrpPremDto.locateWtihHcsa}">
                      <c:choose>
                        <c:when test="${appGrpPremDto.locateWtihHcsa == '1'}">Yes</c:when>
                        <c:when test="${appGrpPremDto.locateWtihHcsa == '0'}">No</c:when>
                      </c:choose>
                    </span>
                  </div>
                  <div class="col-md-6">
                    <span class="oldVal" style="display: none" attr="${oldAppGrpPremDto.locateWtihHcsa}">
                      <c:choose>
                        <c:when test="${oldAppGrpPremDto.locateWtihHcsa == '1'}">Yes</c:when>
                        <c:when test="${oldAppGrpPremDto.locateWtihHcsa == '0'}">No</c:when>
                      </c:choose>
                    </span>
                  </div>
                </div>
              </div>

              <c:if test="${appGrpPremDto.locateWtihNonHcsa == '1' || oldAppGrpPremDto.locateWtihNonHcsa == '1'}" var="hasNonHcsa">
                <div class="row">
                  <table class="table" aria-describedby="" border="0" style="margin:10px 0">
                    <thead>
                    <tr>
                      <td scope="col" class="col-xs-12 col-md-3" style="padding-left: 0; padding-right: 15px;">
                        Are you co-locating with a service that is not licensed under HCSA?
                      </td>
                      <td scope="col" class="col-xs-12 col-md-3" style="padding-left:20px;">Business Name</td>
                      <td scope="col" class="col-xs-12 col-md-3" style="padding-left:20px;">Services Provided</td>
                    </tr>
                    </thead>
                    <tbody>
                    <c:set var="nonLicSize" value="${appGrpPremDto.appPremNonLicRelationDtos.size()}" />
                    <c:forEach var="relatedDto" items="${appGrpPremDto.appPremNonLicRelationDtos}" varStatus="nonLicVs">
                      <c:set var="oldRelatedDto" value="${oldAppGrpPremDto.appPremNonLicRelationDtos[nonLicVs.index]}" />
                      <tr style="border-top: ${nonLicVs.first? 'solid silver' : '2px solid black'}">
                        <c:if test="${nonLicVs.first}">
                          <td rowspan="${nonLicSize}" style="padding-left: 0; padding-right: 15px;">
                            <div class="">
                              <span class="newVal" attr="${appGrpPremDto.locateWtihNonHcsa}">
                                <c:choose>
                                  <c:when test="${appGrpPremDto.locateWtihNonHcsa == '1'}">Yes</c:when>
                                  <c:when test="${appGrpPremDto.locateWtihNonHcsa == '0'}">No</c:when>
                                </c:choose>
                              </span>
                              <br>
                              <span class="oldVal" attr="${oldAppGrpPremDto.locateWtihNonHcsa}" style="display: none">
                                  <c:choose>
                                    <c:when test="${oldAppGrpPremDto.locateWtihNonHcsa == '1'}">Yes</c:when>
                                    <c:when test="${oldAppGrpPremDto.locateWtihNonHcsa == '0'}">No</c:when>
                                  </c:choose>
                              </span>
                            </div>
                          </td>
                        </c:if>
                        <td style="padding: 5px 20px">
                          <div class="newVal" attr="<c:out value="${relatedDto.businessName}"/>">
                            <c:out value="${relatedDto.businessName}"/>
                          </div>
                          <div class="oldVal" attr="<c:out value="${oldRelatedDto.businessName}"/>" style="display: none">
                            <c:out value="${oldRelatedDto.businessName}"/>
                          </div>
                        </td>
                        <td style="padding: 5px 20px">
                          <div class="newVal" attr="<c:out value="${relatedDto.providedService}"/>">
                            <c:out value="${relatedDto.providedService}"/>
                          </div>
                          <div class="oldVal" attr="<c:out value="${oldRelatedDto.providedService}"/>" style="display: none">
                            <c:out value="${oldRelatedDto.providedService}"/>
                          </div>
                        </td>
                      </tr>
                    </c:forEach>
                    </tbody>
                  </table>
                </div>
              </c:if>
              <c:if test="${not hasNonHcsa}">
                <div class="row">
                  <div class="col-md-6">
                    Are you co-locating with a service that is not licensed under HCSA?
                  </div>
                  <div class="col-md-6">
                    <div class="col-md-6">
                    <span class="newVal" attr="${appGrpPremDto.locateWtihNonHcsa}">
                      <c:choose>
                        <c:when test="${appGrpPremDto.locateWtihNonHcsa == '1'}">Yes</c:when>
                        <c:when test="${appGrpPremDto.locateWtihNonHcsa == '0'}">No</c:when>
                      </c:choose>
                    </span>
                    </div>
                    <div class="col-md-6">
                    <span class="oldVal" style="display: none" attr="${oldAppGrpPremDto.locateWtihNonHcsa}">
                      <c:choose>
                        <c:when test="${oldAppGrpPremDto.locateWtihNonHcsa == '1'}">Yes</c:when>
                        <c:when test="${oldAppGrpPremDto.locateWtihNonHcsa == '0'}">No</c:when>
                      </c:choose>
                    </span>
                    </div>
                  </div>
                </div>
              </c:if>
            </c:if>

            <c:if test="${appGrpPremDto.premisesType == easMts}">
              <div class="row">
                <div class="col-md-6">
                  For public/in-house use only?
                </div>
                <div class="col-md-6">
                    <div class="col-md-6">
                      <span class="newVal" attr="${appGrpPremDto.easMtsUseOnly}">
                        <iais:code code="${appGrpPremDto.easMtsUseOnly}"/>
                      </span>
                    </div>
                    <div class="col-md-6">
                      <span class="oldVal" style="display: none" attr="${oldAppGrpPremDto.easMtsUseOnly}">
                         <iais:code code="${oldAppGrpPremDto.easMtsUseOnly}"/>
                      </span>
                    </div>
                </div>
              </div>
              <div class="row">
                <div class="col-md-6">
                  Public Email
                </div>
                <div class="col-md-6">
                    <div class="col-md-12">
                      <span class="newVal" attr="${appGrpPremDto.easMtsPubEmail}">
                        <c:out value="${appGrpPremDto.easMtsPubEmail}"/>
                      </span>
                      <br>
                      <span class="oldVal" style="display: none" attr="${oldAppGrpPremDto.easMtsPubEmail}">
                           <c:out value="${oldAppGrpPremDto.easMtsPubEmail}"/>
                      </span>
                    </div>
                </div>
              </div>
              <div class="row">
                <div class="col-md-6">
                  Public Hotline
                </div>
                <div class="col-md-6">
                  <div class="col-md-6">
                    <span class="newVal" attr="${appGrpPremDto.easMtsPubHotline}">
                      <c:out value="${appGrpPremDto.easMtsPubHotline}"/>
                    </span>
                  </div>
                  <div class="col-md-6">
                    <span class="oldVal" style="display: none" attr="${oldAppGrpPremDto.easMtsPubHotline}">
                      <c:out value="${oldAppGrpPremDto.easMtsPubHotline}"/>
                    </span>
                  </div>
                </div>
              </div>
            </c:if>
          </div>
        </div>
      </c:forEach>

      <c:forEach var="appGrpSecondAddrList" items="${appSubmissionDto.appGrpPremisesDtoList}" varStatus="status">
        <c:set var="oldAppGrpPremDto" value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index]}"/>
        <div class="panel-main-content postion-relative">
            <c:if test="${empty appGrpSecondAddrList.appGrpSecondAddrDtos}">
                <div class="contents">
                    <%@include file="viewAddressDetils.jsp" %>
                </div>
            </c:if>
            <c:forEach var="appGrpSecondAddr" items="${appGrpSecondAddrList.appGrpSecondAddrDtos}" varStatus="statuss">
                <div class="contents">
                  <c:set var="oldAppGrpSecondAddr" value="${oldAppGrpPremDto.appGrpSecondAddrDtos[status.index]}"/>
                    <%@include file="viewAddressDetils.jsp" %>
                </div>
            </c:forEach>
            <%@include file="viewAddresslEdit.jsp" %>
            <div class="adds"></div>
        </div>
      </c:forEach>

      <div class="btns hidden">
        <div class="row">
            <div class="col-xs-12 col-md-8">
                <button id="addPremBtn" class="btn btn-primary" type="button">ADD SECONDARY ADDRESS</button>
            </div>
            <div class="col-xs-12 col-sm-4">
                <a class="btn btn-primary premiseSave" id="Save">SAVE</a>
            </div>
        </div>
      </div>
      <iais:confirm msg="NEW_ACK016" needCancel="false" callBack="$('#postalCodePop').modal('hide');" popupOrder="postalCodePop"
                    yesBtnDesc="" needEscapHtml="false" needFungDuoJi="false"/>
    </div>
  </div>
</div>
<%@include file="/WEB-INF/jsp/include/validation.jsp"%>
<%@include file="viewAddressFun.jsp"%>
