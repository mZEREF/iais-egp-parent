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
<div class="panel panel-default">

    <div class="panel-heading" id="headingPremise" role="tab">
        <h4 class="panel-title"><a class="collapsed" role="button" data-toggle="collapse" href="#collapsePremise"
                                   aria-expanded="true" aria-controls="collapsePremise"><c:out
                value="${mosdName}"/> </a>
        </h4>
    </div>

    <div class="panel-collapse collapse" id="collapsePremise" role="tabpanel" aria-labelledby="headingPremise">

        <div class="panel-body">


            <p class="text-right">
                <c:if test="${rfi=='rfi'}">
                    <c:if test="${(appEdit.premisesEdit || appEdit.premisesListEdit)&& canEidtPremise }">
                        <input class="form-check-input" id="premisesCheckbox" type="checkbox"
                               name="editCheckbox"
                               <c:if test="${pageEdit.premisesEdit}">checked</c:if> aria-invalid="false"
                               value="premises">
                    </c:if>
                </c:if>
            </p>


            <c:forEach var="appGrpPremDto" items="${appSubmissionDto.appGrpPremisesDtoList}" varStatus="status">
                <c:set var="oldAppGrpPremDto"
                       value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index]}"/>
                <%--        begin--%>
                <div class="panel-main-content postion-relative">

                    <div class="preview-info">
                        <div class="row">
                            <div class="col-md-6">
                                <label>${mosdName} ${status.index+1}</label>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6">
                                    ${mosdName}
                            </div>
                            <div class="col-md-6">
                                <div class="col-md-6">
                  <span class="newVal " attr="${appGrpPremDto.premisesType}">
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
                  <span class="oldVal " attr="${oldAppGrpPremDto.premisesType}" style="display: none">
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
                        <c:if test="${permanent == appGrpPremDto.premisesType || permanent == oldAppGrpPremDto.premisesType}">
                            <div class="row">
                                <div class="col-md-6">
                                    Fire Safety & Shelter Bureau Ref. No.
                                </div>
                                <div class="col-md-6">
                                    <div class="col-md-12">
                                    <span class="newVal " attr="${appGrpPremDto.scdfRefNo}"><c:out
                                            value="${appGrpPremDto.scdfRefNo}"/></span>
                                        <br>
                                        <span class="oldVal " attr="${oldAppGrpPremDto.scdfRefNo}"
                                              style="display: none">
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
                    <span class="newVal " attr="${appGrpPremDto.certIssuedDtStr}">
                      <c:out value="${appGrpPremDto.certIssuedDtStr}"/>
                    </span>
                                    </div>
                                    <div class="col-md-6">
                    <span class="oldVal " attr="${oldAppGrpPremDto.certIssuedDtStr}" style="display: none">
                      <c:out value="${oldAppGrpPremDto.certIssuedDtStr}"/>
                      </span>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${conv == appGrpPremDto.premisesType || conv == oldAppGrpPremDto.premisesType}">
                            <div class="row">
                                <div class="col-md-6"> Vehicle No.</div>
                                <div class="col-md-6">
                                    <div class="col-md-6">
                    <span class="newVal " attr="<c:out value="${appGrpPremDto.vehicleNo}"/>">
                      <c:out value="${appGrpPremDto.vehicleNo}"/>
                    </span>
                                    </div>
                                    <div class="col-md-6">
                    <span class="oldVal " attr="<c:out value="${oldAppGrpPremDto.vehicleNo}"/>" style="display: none">
                      <c:out value="${appGrpPremDto.vehicleNo}"/>
                    </span>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                        <div class="row">
                            <div class="col-md-6">
                                Business Name
                                <a class="btn-tooltip styleguide-tooltip" id="hciNameClick"
                                   <c:if test="${empty appGrpPremDto.applicationViewHciNameDtos}">style="display: none" </c:if>
                                   data-toggle="tooltip" data-html="true" title="" data-original-title="">i</a>
                            </div>
                            <div class="col-md-7"
                                 style="position: absolute;z-index: 100;left: 40%;background-color: #EEEEEE;display: none;margin-top: 2%;overflow-y: scroll"
                                 id="hciNameShowOrHidden">
                                <p>The Business Name is currently used by another licensee</p>
                                <br>
                                <table aria-describedby="" border="1px"
                                       style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;text-align: center;background-color: #ffffff;width: 100%">
                                    <tr>
                                        <th scope="col" class="col-md-4">Name of Licensee</th>
                                        <th scope="col" class="col-md-4">HCI Name</th>
                                        <th scope="col" class="col-md-4">Service Name</th>
                                    </tr>
                                    <c:forEach items="${appGrpPremDto.applicationViewHciNameDtos}"
                                               var="applicationViewHciNameDtos">
                                        <tr>
                                            <td>${applicationViewHciNameDtos.licensee}</td>
                                            <td>${applicationViewHciNameDtos.hciName}</td>
                                            <td>${applicationViewHciNameDtos.serviceName}</td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </div>
                            <div class="col-md-6">
                                <div class="col-md-12">
                                <span class="newVal " attr="${appGrpPremDto.hciName}"><c:out
                                        value="${appGrpPremDto.hciName}"/></span>
                                    <br>
                                    <span class="oldVal " attr="${oldAppGrpPremDto.hciName}" style="display: none">
                    <c:out value="${oldAppGrpPremDto.hciName}"/>
                  </span>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                Postal Code
                                <a class="btn-tooltip styleguide-tooltip" id="addressClick"
                                   <c:if test="${empty appGrpPremDto.applicationViewAddress}">style="display: none" </c:if>
                                   data-toggle="tooltip" data-html="true" title="" data-original-title="">i</a>
                            </div>


                            <div class="col-md-7"
                                 style="position: absolute;z-index: 100;left: 40%;background-color: #EEEEEE;margin-top:2%;display: none;overflow-y: scroll;"
                                 id="addressShowOrHidden">
                                <p>The address of the mode of service delivery keyed in by applicant is currently used
                                    by
                                    another licensee</p>
                                <table aria-describedby="" border="1px"
                                       style="border-collapse: collapse;border-top: 0 solid #000000;padding: 8px;text-align: center;background-color: #ffffff;width: 100%">
                                    <tr>
                                        <th scope="col" class="col-md-4">Name of Licensee</th>
                                        <th scope="col" class="col-md-4">HCI Name</th>
                                        <th scope="col" class="col-md-4">Service Name</th>
                                    </tr>
                                    <c:forEach items="${appGrpPremDto.applicationViewAddress}"
                                               var="applicationViewAddress">
                                        <tr>
                                            <td>${applicationViewAddress.licensee}</td>
                                            <td>${applicationViewAddress.hciName}</td>
                                            <td>${applicationViewAddress.serviceName}</td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </div>


                            <div class="col-md-6">
                                <div class="col-md-6">
                                <span class="newVal " attr="${appGrpPremDto.postalCode}"><c:out
                                        value="${appGrpPremDto.postalCode}"/></span>
                                </div>
                                <div class="col-md-6">
                                <span class="oldVal " attr="${oldAppGrpPremDto.postalCode}" style="display: none"><c:out
                                        value="${oldAppGrpPremDto.postalCode}"/></span>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6">
                                Address Type
                            </div>
                            <div class="col-md-6">
                                <div class="col-md-6">
                  <span class="newVal " attr="${appGrpPremDto.addrType}">
                    <iais:code code="${appGrpPremDto.addrType}"/>
                  </span>
                                </div>
                                <div class="col-md-6">
                  <span class="oldVal " attr="${oldAppGrpPremDto.addrType}" style="display: none">
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
                                <span class="newVal " attr="${appGrpPremDto.blkNo}"><c:out
                                        value="${appGrpPremDto.blkNo}"/></span>
                                </div>
                                <div class="col-md-6">
                                <span class="oldVal " attr="${oldAppGrpPremDto.blkNo}" style="display: none"><c:out
                                        value="${oldAppGrpPremDto.blkNo}"/></span>
                                </div>
                            </div>
                        </div>


                        <div class="row">
                            <div class="col-md-6">
                                Floor / Unit No.
                            </div>
                            <div class="col-md-6">
                                <div class="col-md-6">
                   <span class="newVal " attr="${appGrpPremDto.floorNo}${appGrpPremDto.unitNo}">
                     <c:out value="${appGrpPremDto.floorNo}-${appGrpPremDto.unitNo}"/>
                   </span>
                                </div>
                                <div class="col-md-6">
                   <span class="oldVal " attr="${oldAppGrpPremDto.floorNo}${oldAppGrpPremDto.unitNo}"
                         style="display: none">
                     <c:out value="${oldAppGrpPremDto.floorNo}-${oldAppGrpPremDto.unitNo}"/>
                   </span>
                                </div>
                            </div>
                        </div>


                        <c:forEach items="${appGrpPremDto.appPremisesOperationalUnitDtos}"
                                   var="appPremisesOperationalUnitDto" varStatus="unitIndex">
                            <div class="row">
                                <div class="col-md-6">
                                </div>
                                <div class="col-md-6">
                                    <div class="col-md-6">
                    <span class="newVal "
                          attr="${appPremisesOperationalUnitDto.floorNo}${appPremisesOperationalUnitDto.unitNo}">
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
                    <span class="oldVal "
                          attr="${oldAppGrpPremDto.appPremisesOperationalUnitDtos[unitIndex.index].floorNo}${oldAppGrpPremDto.appPremisesOperationalUnitDtos[unitIndex.index].unitNo}"
                          style="display: none">
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
                   <span class="newVal " attr="<c:out value="${appGrpPremDto.streetName}"/>">
                       <c:out value="${appGrpPremDto.streetName}"/>
                   </span>
                                    <br>
                                    <span class="oldVal " attr="<c:out value="${oldAppGrpPremDto.streetName}"/>"
                                          style="display: none">
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
                  <span class="newVal " attr="<c:out value="${appGrpPremDto.buildingName}"/>">
                    <c:out value="${appGrpPremDto.buildingName}"/>
                  </span>
                                    <br>
                                    <span class="oldVal " attr="<c:out value="${oldAppGrpPremDto.buildingName}"/>"
                                          style="display: none">
                    <c:out value="${oldAppGrpPremDto.buildingName}"/>
                  </span>
                                </div>
                            </div>
                        </div>

                        <c:if test="${appGrpPremDto.premisesType == permanent || appGrpPremDto.premisesType == conv
                || oldAppGrpPremDto.premisesType == permanent || oldAppGrpPremDto.premisesType == conv}">
                            <div class="row">
                                <div class="col-md-12">
                                    Co-Location Service
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
                            <c:if test="${appGrpPremDto.locateWtihNonHcsa == '1' || oldAppGrpPremDto.locateWtihNonHcsa == '1'}"
                                  var="hasNonHcsa">
                                <div class="row">
                                    <table class="table" aria-describedby="" border="0" style="margin:10px 0">
                                        <thead>
                                        <tr>
                                            <td scope="col" class="col-xs-12 col-md-3"
                                                style="padding-left: 0; padding-right: 15px;">
                                                Are you co-locating with a service that is not licensed under HCSA?
                                            </td>
                                            <td scope="col" class="col-xs-12 col-md-3" style="padding-left:20px;">
                                                Business
                                                Name
                                            </td>
                                            <td scope="col" class="col-xs-12 col-md-3" style="padding-left:20px;">
                                                Services
                                                Provided
                                            </td>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:set var="nonLicSize"
                                               value="${appGrpPremDto.appPremNonLicRelationDtos.size()}"/>
                                        <c:forEach var="relatedDto" items="${appGrpPremDto.appPremNonLicRelationDtos}"
                                                   varStatus="nonLicVs">
                                            <c:set var="oldRelatedDto"
                                                   value="${oldAppGrpPremDto.appPremNonLicRelationDtos[nonLicVs.index]}"/>
                                            <tr style="border-top: ${nonLicVs.first? 'solid silver' : '2px solid black'}">
                                                <c:if test="${nonLicVs.first}">
                                                    <td rowspan="${nonLicSize}"
                                                        style="padding-left: 0; padding-right: 15px;">
                                                        <div class="">
                              <span class="newVal " attr="${appGrpPremDto.locateWtihNonHcsa}">
                                <c:choose>
                                    <c:when test="${appGrpPremDto.locateWtihNonHcsa == '1'}">Yes</c:when>
                                    <c:when test="${appGrpPremDto.locateWtihNonHcsa == '0'}">No</c:when>
                                </c:choose>
                              </span>
                                                            <br>
                                                            <span class="oldVal"
                                                                  attr="${oldAppGrpPremDto.locateWtihNonHcsa}"
                                                                  style="display: none">
                                  <c:choose>
                                      <c:when test="${oldAppGrpPremDto.locateWtihNonHcsa == '1'}">Yes</c:when>
                                      <c:when test="${oldAppGrpPremDto.locateWtihNonHcsa == '0'}">No</c:when>
                                  </c:choose>
                              </span>
                                                        </div>
                                                    </td>
                                                </c:if>
                                                <td style="padding: 5px 20px">
                          <span class="newVal " attr="<c:out value="${relatedDto.businessName}"/>">
                            <c:out value="${relatedDto.businessName}"/>
                          </span>
                                                    <br>
                                                    <span class="oldVal"
                                                          attr="<c:out value="${oldRelatedDto.businessName}"/>"
                                                          style="display: none">
                            <c:out value="${oldRelatedDto.businessName}"/>
                          </span>
                                                </td>
                                                <td style="padding: 5px 20px">
                          <span class="newVal " attr="<c:out value="${relatedDto.providedService}"/>">
                            <c:out value="${relatedDto.providedService}"/>
                          </span>
                                                    <br>
                                                    <span class="oldVal"
                                                          attr="<c:out value="${oldRelatedDto.providedService}"/>"
                                                          style="display: none">
                            <c:out value="${oldRelatedDto.providedService}"/>
                          </span>
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
                            <%--<c:if test="${appGrpPremDto.premisesType=='OFFSITE' || appGrpPremDto.premisesType=='ONSITE' || appGrpPremDto.premisesType=='CONVEYANCE'}">
                              <div class="row">
                                <div class="col-md-6">
                                  Operating Hours
                                </div>
                                <div class="col-md-6">
                                  <span></span>
                                </div>
                              </div>
                              <div class="row">
                                <div class="col-md-6">Weekly</div>
                                <div class="col-md-6">
                                  <div class="col-md-4"><span>Start</span></div>
                                  <div class="col-md-4"><span>End</span></div>
                                  <div class="col-md-4"><span>24 Hours</span></div>
                                </div>
                              </div>
                              <c:forEach items="${appGrpPremDto.weeklyDtoList}" var="weeklyDto" varStatus="weekSta">
                                <c:set var="oldWeeklyDto" value="${oldAppGrpPremDto.weeklyDtoList[weekSta.index]}"/>
                                <div class="row">
                                  <div class="col-md-6">
                                    <div class="col-md-12" style="padding: 0px">
                                    <span class="newVal" attr="${weeklyDto.selectValList}">
                                       <c:forEach items="${weeklyDto.selectValList}" var="selectVal" varStatus="in">
                                         <iais:code code="${selectVal}"/><c:if test="${!in.last}">,</c:if>
                                       </c:forEach>
                                    </span>
                                      <br>
                                      <span class="oldVal" style="display: none" attr="${oldWeeklyDto.selectValList}">
                                      <c:forEach items="${oldWeeklyDto.selectValList}" var="oldSelectValList" varStatus="in">
                                        <iais:code code="${oldSelectValList}"/><c:if test="${!in.last}">,</c:if>
                                      </c:forEach>
                                    </span>
                                    </div>

                                  </div>
                                  <div class="col-md-6">
                                    <div class="col-md-4" style="padding-right: 0px">
                                      <c:if test="${weeklyDto.selectAllDay}">
                                      <span class="newVal" attr=""></span>
                                      </c:if>
                                      <c:if test="${!weeklyDto.selectAllDay}">
                                      <span class="newVal" attr="${weeklyDto.startFrom}">
                                        <fmt:formatDate value="${weeklyDto.startFrom}" pattern="HH : mm"/>
                                      </span>
                                      </c:if>
                                      <c:if test="${oldWeeklyDto.selectAllDay}">
                                        <span class="oldVal" attr=""></span>
                                      </c:if>
                                      <c:if test="${!oldWeeklyDto.selectAllDay}">
                                      <span class="oldVal" attr="${oldWeeklyDto.startFrom}">
                                        <fmt:formatDate value="${oldWeeklyDto.startFrom}" pattern="HH : mm"/>
                                      </span>
                                      </c:if>
                                    </div>
                                    <div class="col-md-4" style="padding-right: 0px">
                                      <c:if test="${weeklyDto.selectAllDay}">
                                        <span class="newVal" attr=""></span>
                                      </c:if>
                                      <c:if test="${!weeklyDto.selectAllDay}">
                                        <span class="newVal" attr="${weeklyDto.endTo}">
                                            <fmt:formatDate value="${weeklyDto.endTo}" pattern="HH : mm"/>
                                        </span>
                                      </c:if>
                                      <c:if test="${oldWeeklyDto.selectAllDay}">
                                        <span class="oldVal" attr=""></span>
                                      </c:if>
                                      <c:if test="${!oldWeeklyDto.selectAllDay}">
                                        <span class="oldVal" attr="${oldWeeklyDto.endTo}">
                                            <fmt:formatDate value="${oldWeeklyDto.endTo}" pattern="HH : mm"/>
                                        </span>
                                      </c:if>
                                    </div>
                                    <div class="col-md-4" style="padding-right: 0px">
                                      <div class="col-md-6" style="padding: 0px">
                                      <span class="newVal" attr="${weeklyDto.selectAllDay && not empty weeklyDto.selectValList}">
                                        <c:if test="${weeklyDto.selectAllDay && not empty weeklyDto.selectValList}">
                                          <div class="form-check active">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                                          </div>
                                        </c:if>
                                      </span>
                                      </div>
                                      <div class="col-md-6" style="padding: 0px">
                                      <span class="oldVal" style="display: none" attr="${oldWeeklyDto.selectAllDay && not empty oldWeeklyDto.selectValList}">
                                        <c:if test="${oldWeeklyDto.selectAllDay && not empty oldWeeklyDto.selectValList}">
                                          <div class="form-check active">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                                          </div>
                                        </c:if>
                                      </span>
                                      </div>
                                    </div>
                                  </div>
                                </div>
                              </c:forEach>
                              <div class="row">
                                <div class="col-md-6">Public Holiday</div>
                                <div class="col-md-6">
                                  <div class="col-md-4"></div>
                                  <div class="col-md-4"></div>
                                  <div class="col-md-4"></div>
                                </div>
                              </div>
                              <c:forEach items="${appGrpPremDto.phDtoList}" var="op" varStatus="opSta">
                                <c:set var="oldOp" value="${oldAppGrpPremDto.phDtoList[opSta.index]}" />
                                <div class="row">
                                  <div class="col-md-6">
                                    <div class="col-md-12" style="padding: 0px">
                                    <span class="newVal" attr="${op.selectValList}">
                                         <c:forEach items="${op.selectValList}" var="phDto" varStatus="in">
                                           <iais:code code="${phDto}"></iais:code><c:if test="${!in.last}">,</c:if>
                                         </c:forEach>
                                    </span>
                                      <br>
                                      <span class="oldVal" style="display: none" attr="${oldOp.selectValList}">
                                        <c:forEach items="${oldOp.selectValList}" var="oldPhDtoList" varStatus="in">
                                          <iais:code code="${oldPhDtoList}"/><c:if test="${!in.last}">,</c:if>
                                        </c:forEach>
                                    </span>
                                    </div>

                                  </div>
                                  <div class="col-md-6">
                                    <div class="col-md-4" style="padding-right: 0px">
                                      <c:if test="${op.selectAllDay}">
                                        <span class="newVal" attr=""></span>
                                      </c:if>
                                      <c:if test="${!op.selectAllDay}">
                                        <span class="newVal" attr="${op.startFrom}">
                                          <fmt:formatDate value="${op.startFrom}" pattern="HH : mm"/>
                                        </span>
                                      </c:if>
                                      <c:if test="${oldOp.selectAllDay}">
                                        <span class="oldVal" attr=""></span>
                                      </c:if>
                                      <c:if test="${!oldOp.selectAllDay}">
                                        <span class="oldVal" attr="${oldOp.startFrom}">
                                          <fmt:formatDate value="${oldOp.startFrom}" pattern="HH : mm"/>
                                        </span>
                                      </c:if>
                                    </div>

                                    <div class="col-md-4" style="padding-right: 0px">
                                      <c:if test="${op.selectAllDay}">
                                        <span class="newVal" attr=""></span>
                                      </c:if>
                                      <c:if test="${!op.selectAllDay}">
                                        <span class="newVal" attr="${op.endTo}">
                                         <fmt:formatDate value="${op.endTo}" pattern="HH : mm"/>
                                        </span>
                                      </c:if>
                                      <c:if test="${oldOp.selectAllDay}">
                                        <span class="oldVal" attr=""></span>
                                      </c:if>
                                      <c:if test="${!oldOp.selectAllDay}">
                                        <span class="oldVal" attr="${oldOp.endTo}">
                                         <fmt:formatDate value="${oldOp.endTo}" pattern="HH : mm"/>
                                        </span>
                                      </c:if>
                                    </div>

                                    <div class="col-md-4" style="padding-right: 0px">
                                      <div class="col-md-6" style="padding: 0px" >
                                        <span class="newVal" attr="${op.selectAllDay && not empty op.selectValList}">
                                          <c:if test="${op.selectAllDay && not empty op.selectValList}">
                                            <div class="form-check active">
                                              <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                                            </div>
                                          </c:if>
                                        </span>
                                      </div>
                                      <div class="col-md-6" style="padding: 0px">
                                        <span class="oldVal" style="display: none" attr="${oldOp.selectAllDay && not empty oldOp.selectValList}">
                                           <c:if test="${oldOp.selectAllDay && not empty oldOp.selectValList}">
                                             <div class="form-check active">
                                              <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                                            </div>
                                           </c:if>
                                        </span>
                                      </div>
                                    </div>
                                  </div>

                                </div>
                              </c:forEach>
                              <div class="row">
                                <div class="col-md-6">Event</div>
                                <div class="col-md-6">
                                  <div class="col-md-4"></div>
                                  <div class="col-md-4"></div>
                                  <div class="col-md-4"></div>
                                </div>
                              </div>
                              <c:forEach var="eventDto" items="${appGrpPremDto.eventDtoList}" varStatus="eventSta">
                                <div class="row">
                                  <div class="col-md-6">
                                    <div class="col-md-12" style="padding: 0px">
                                      <span class="newVal" attr="${eventDto.eventName}">${eventDto.eventName}</span>
                                      <br>
                                      <span class="oldVal" style="display: none" attr="${oldAppGrpPremDto.eventDtoList[eventSta.index].eventName}">
                                          ${oldAppGrpPremDto.eventDtoList[eventSta.index].eventName}
                                      </span>
                                    </div>


                                  </div>
                                  <div class="col-md-6">
                                    <div class="col-md-6">
                                      <div class="col-md-6" style="padding: 0px">
                                      <span class="newVal" attr="${eventDto.startDate}">
                                        <fmt:formatDate value="${eventDto.startDate}" pattern="dd/MM/yyyy"/>
                                      </span>
                                        <span class="oldVal" style="display: none" attr="${oldAppGrpPremDto.eventDtoList[eventSta.index].startDate}">
                                        <fmt:formatDate value="${oldAppGrpPremDto.eventDtoList[eventSta.index].startDate}" pattern="dd/MM/yyyy"/>
                                      </span>
                                      </div>

                                    </div>
                                    <div class="col-md-6">
                                      <div class="col-md-6" style="padding: 0px">
                                      <span class="newVal" attr="${eventDto.endDate}">
                                          <fmt:formatDate value="${eventDto.endDate}"  pattern="dd/MM/yyyy"/>
                                      </span>
                                        <span class="oldVal" style="display: none" attr="${oldAppGrpPremDto.eventDtoList[eventSta.index].endDate}">
                                        <fmt:formatDate value="${oldAppGrpPremDto.eventDtoList[eventSta.index].endDate}" pattern="dd/MM/yyyy"/>
                                      </span>
                                      </div>
                                    </div>
                                  </div>
                                </div>
                              </c:forEach>
                            </c:if>--%>


                        <c:if test="${appGrpPremDto.premisesType == easMts}">
                            <div class="row">
                                <div class="col-md-6">
                                    For public/in-house use only?
                                </div>
                                <div class="col-md-6">
                                    <div class="col-md-6">
                      <span class="newVal " attr="${appGrpPremDto.easMtsUseOnly}">
                        <iais:code code="${appGrpPremDto.easMtsUseOnly}"/>
                      </span>
                                    </div>
                                    <div class="col-md-6">
                      <span class="oldVal " style="display: none" attr="${oldAppGrpPremDto.easMtsUseOnly}">
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
                      <span class="newVal " attr="${appGrpPremDto.easMtsPubEmail}">
                        <c:out value="${appGrpPremDto.easMtsPubEmail}"/>
                      </span>
                                        <br>
                                        <span class="oldVal " style="display: none"
                                              attr="${oldAppGrpPremDto.easMtsPubEmail}">
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
                    <span class="newVal " attr="${appGrpPremDto.easMtsPubHotline}">
                      <c:out value="${appGrpPremDto.easMtsPubHotline}"/>
                    </span>
                                    </div>
                                    <div class="col-md-6">
                    <span class="oldVal " style="display: none" attr="${oldAppGrpPremDto.easMtsPubHotline}">
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
                <div class="panel-main-content postion-relative">
                    <c:if test="${empty appGrpSecondAddrList.appGrpSecondAddrDtos}">
                        <div class="contents">
                            <%@include file="viewPremisesDetils.jsp" %>
                        </div>
                    </c:if>
                    <c:forEach var="appGrpSecondAddr" items="${appGrpSecondAddrList.appGrpSecondAddrDtos}"
                               varStatus="statuss">
                        <div class="contents">
                            <%@include file="viewPremisesDetils.jsp" %>
                        </div>
                    </c:forEach>
                            <c:set var="MMM" value="logo"></c:set>
                    <%@include file="viewPremisesDetilEdit.jsp" %>
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


        </div>
    </div>
</div>
<%@include file="/WEB-INF/jsp/include/validation.jsp"%>
<script type="text/javascript">
    $(function () {
        init()
    })

    function init() {
        addrTypeEvent()
        addOperationalEvnet();
        delOperationEvent();
        addPremEvent();
        removeBtnEvent();
        retrieveAddrEvent();
        editEvent()
        changeCount()
    }


    let retrieveAddrEvent = function () {
        $('.retrieveAddr').click(function () {
            let $postalCodeEle = $(this).closest('div.postalCodeDiv');
            let postalCode = $postalCodeEle.find('.postalCode').val();
            retrieveAddr(postalCode, $(this).closest('div.viewPrem').find('div.address'));
        })
    }

    let saveEvent = function ($target){
        $('#Save').unbind('click')
        $('#Save').click(function () {
        let list = new Array();
        clearFields($target);
        $('.viewPrem').each(function (k, v) {
            let appPremisesOperationalUnitDtos = new Array();
            let postalCode = $(v).find('.postalCode').val()
            let addrType = $(v).find('.addressType').find("option:selected").val()
            addrType = addrType=='ADDTY002' ? 'Without Apt Blk' : (addrType == 'ADDTY001' ? 'Apt Blk' : '')
            let blkNo = $(v).find('.blkNo').val()
            let floorNo = $(v).find('.operationDiv:first').find('.floorNo').val()
            let unitNo = $(v).find('.operationDiv:first').find('.unitNo').val()
            $(v).find('.operationDivGroup .operationDiv').each(function (index, item) {
                let floorNo = $(item).find('.floorNo').val()
                let unitNo = $(item).find('.unitNo').val()
                let others = {
                    'floorNo' : floorNo,
                    'unitNo' : unitNo
                }
                appPremisesOperationalUnitDtos.push(others)
            })
            let streetName = $(v).find('.streetName').val()
            let buildingName = $(v).find('.buildingName').val()
            let appGrpSecondAddrDto = {
                'postalCode': postalCode,
                'addrType': addrType,
                'blkNo': blkNo,
                'floorNo': floorNo,
                'unitNo': unitNo,
                'appPremisesOperationalUnitDtos': appPremisesOperationalUnitDtos,
                'streetName': streetName,
                'buildingName': buildingName
            }
            list.push(appGrpSecondAddrDto)
        })

        <%--    var opt = {--%>
        <%--        url: '${pageContext.request.contextPath}/save-second-address',--%>
        <%--        type: 'post',--%>
        <%--        data: JSON.stringify(list),--%>
        <%--    };--%>
        <%--console.log(JSON.stringify(list),"=====d>")--%>
        <%--    console.log(list,"=====v>")--%>
        <%--    callCommonAjax(opt, "premSelectCallback", $target);--%>

            $.ajax({
                url: '${pageContext.request.contextPath}/save-second-address',
                data: JSON.stringify(list),
                dataType:"json",
                contentType: "application/json;charset=utf-8",
                type: 'post',
                success: function(result) {
                    if (result.code == "ok"){
                        premSelectCallback(result.data,$target)
                    }
                    if (result.code == "error"){
                        clearErrorMsg($('.viewPrem'))
                        doValidationParse(result.data)
                    }
                }
            });
    })
    }

    function premSelectCallback(data, $target) {
        if (data == null || isEmptyNode($target)) {
            dismissWaiting();
            return;
        }
        fillInfoMation(data[0],$target)
        let length = data.length;
        // TODO
        if (length > 1){
            $.each(data.splice(1,1),function (index,items){
                console.log(data,'data=====>')
                let $targets = $target.clone();
                clearFields($targets)
                $('.premisesContent').last().after($targets)
                editEvent()
                fillInfoMation(items,$('.premisesContent').last())
            })
        }
    }

    function fillInfoMation(data,$target,flag){
        $('.contents').removeClass('hidden')
        $('.viewPrem').addClass('hidden')
        $('.btns').addClass('hidden')
        $target.find('.postalCode').text(data.postalCode)
        $target.find('.addrType').text(data.addrType)
        $target.find('.blkNo').text(data.blkNo)
        let mm = data.floorNo + "-" + data.unitNo
        $target.find('.floorNo-unitNo').text(mm)
        $target.find('.streetName').text(data.streetName)
        $target.find('.buildingName').text(data.buildingName)
        let content  = `<div class="row addmore">
            <div class="col-md-6">
            </div>
        <div class="col-md-6">
            <div class="col-md-6">
                   <span class="newVal addmorecontent" attr="">
                   </span>
            </div>
        </div>
    </div>`
        $('.addmore').remove()
        $.each(data.appPremisesOperationalUnitDtos,function (index,value){
            if (index == 0){
                $target.find('.appendContent').after(content)
            }else {
                $('.addmore').last().after(content)
            }
            let numbere = value.floorNo + "-" + value.unitNo
            $('.addmore').eq(index).find('.addmorecontent').text(numbere)
        })
        changeCount()
        dismissWaiting();
    }


    function retrieveAddr(postalCode, target) {
        var $addressSelectors = $(target);
        var data = {
            'postalCode': postalCode
        };
        showWaiting();
        $.ajax({
            'url': '${pageContext.request.contextPath}/retrieve-address',
            'dataType': 'json',
            'data': data,
            'type': 'GET',
            'success': function (data) {
                if (data == null) {
                    $('#postalCodePop').modal('show');
                    clearFields($addressSelectors.find(':input'));
                    unReadlyContent($addressSelectors);
                } else {
                    fillValue($addressSelectors.find('.blkNo'), data.blkHseNo);
                    fillValue($addressSelectors.find('.streetName'), data.streetName);
                    fillValue($addressSelectors.find('.buildingName'), data.buildingName);
                }
                dismissWaiting();
            },
            'error': function () {
                $('#postalCodePop').modal('show');
                clearFields($addressSelectors.find(':input'));
                unReadlyContent($addressSelectors);
                dismissWaiting();
            }
        });
    }

    let addrTypeEvent = function (target) {
        var $target = getJqueryNode(target);
        if (isEmptyNode($target)) {
            $target = $(document);
        }
        $target.find('.addrType').unbind('change');
        $target.find('.addrType').on('change', function () {
            var $premContent = $(this).closest('div.viewPrem');
            checkAddressMandatory($premContent);
        });
    }

    function checkAddressMandatory($premContent) {
        var addrType = $premContent.find('.addrType').val();
        $premContent.find('.blkNoLabel .mandatory').remove();
        $premContent.find('.floorUnitLabel .mandatory').remove();
        if ('ADDTY001' == addrType) {
            $premContent.find('.blkNoLabel').append('<span class="mandatory">*</span>');
            $premContent.find('.floorUnitLabel:first').append('<span class="mandatory">*</span>');
        }
    }

    let addOperationalEvnet = function (target) {
        let $target = getJqueryNode(target);
        if (isEmptyNode($target)) {
            $target = $(document);
        }
        $target.find('.addOperational').unbind('click');
        $target.find('.addOperational').on('click', function () {
            addFloorUnit(this);
        });
    }

    function addFloorUnit(ele,flag,value) {
        let $premContent
        if (flag){
            $premContent = $(ele)
        }else {
            $premContent =  $(ele).closest('div.viewPrem');
        }
        var src = $premContent.find('div.operationDiv').first().clone();
        clearFields(src);
        $premContent.find('div.addOpDiv').before(src);
        refreshFloorUnit($premContent, $('div.viewPrem').index($premContent));
        delOperationEvent($premContent);
        if (flag){
            fillValue($premContent.find('.operationDivGroup .operationDiv').last().find('.floorNo'),value.floorNo)
            fillValue($premContent.find('.operationDivGroup .operationDiv').last().find('.unitNo'),value.unitNo)
            console.log("enter====>")
        }
    }

    let delOperationEvent = function (target) {
        let $target = getJqueryNode(target);
        if (isEmptyNode($target)) {
            $target = $(document);
        }
        $target.find('.opDel').unbind('click');
        $target.find('div.operationDivGroup').find('.opDel').on('click', function () {
            let $premContent = $(this).closest('div.viewPrem');
            $(this).closest('div.operationDiv').remove();
            refreshFloorUnit($premContent, $('div.viewPrem').index($premContent));
        });
    }

    function refreshFloorUnit(target, prefix) {
        var $target = getJqueryNode(target);
        if (isEmptyNode($target)) {
            return;
        }
        $target.find('.operationDiv').each(function (i, ele) {
            if (i == 0) {
                hideTag($(ele).find('.operationAdlDiv'));
            } else {
                showTag($(ele).find('.operationAdlDiv'));
                $(ele).find('.floorUnitLabel').html('');
            }
            resetField(ele, i, prefix);
        });
    }

    function addPremEvent() {
        $('#addPremBtn').unbind('click');
        $('#addPremBtn').on('click', addPremEventFun);
    }

    function addPremEventFun() {
        showWaiting();
        var $target = $('div.viewPrem:last');
        var src = $target.clone();
        $('div.adds').before(src);
        var $premContent = $('div.viewPrem').last();
        clearFields($premContent);
        removeAdditional($premContent);
        refreshPremise($premContent, $('div.viewPrem').length - 1);
        $('div.viewPrem:first').find('.premHeader').html('1');
        init();
        dismissWaiting();
    }

    function removeAdditional($premContent) {
        $premContent.find('div.operationDivGroup .operationDiv').remove();
    }


    function refreshPremise($premContent, k) {
        var $target = getJqueryNode($premContent);
        if (isEmptyNode($target)) {
            return;
        }
        toggleTag($target.find('.removeEditDiv'), k != 0);
        $target.find('.premHeader').html(k + 1);
        resetIndex($target, k);
        refreshFloorUnit($target, k);
    }

    let removeBtnEvent = function () {
        $('.removeBtn').click(function () {
            var $psnContentEle = $(this).closest('.viewPrem');
            $psnContentEle.remove();
            $('div.viewPrem').each(function (k, v) {
                refreshPremise($(v), k);
            });
            if ($('div.viewPrem').length == 1) {
                $('div.viewPrem').find('.premHeader').html('');
            }
            dismissWaiting();
        });
    }

    $('#addressClick').click(function () {
        var jQuery = $('#addressShowOrHidden').attr('style');
        if (jQuery.match("display: none")) {
            var a = $('#addressShowOrHidden').height();
            var b = $('#collapsePremise .panel-body').height();
            if (a > b) {
                $('#addressShowOrHidden').height(b)
            } else {
                $('#addressShowOrHidden').height(a)
            }
            $('#addressShowOrHidden').show();
            $('#hciNameShowOrHidden').hide();
        } else {
            $('#addressShowOrHidden').hide();
        }
    });

    //edit
    let editEvent = function () {
        $('.viewPremisesEdit').unbind('click')
    $('.viewPremisesEdit').click(function () {
        let target = $(this).closest('div.premisesContent');
        $('.viewPrem').removeClass('hidden');
        $('.btns').removeClass('hidden')
        $('.save').removeClass('hidden')
        //all hide
        $('.contents').addClass('hidden');
        $('.viewPrem:not(:first)').remove()
        clearFields($('.viewPrem'))
        $('.viewPrem').find('.premHeader').html('')
        getEditInformationAndFill(target)
        saveEvent(target)
    })
    }

    let changeCount = function (){
        let length  =  $('.premisesContent').length
        $('.premisesContent').each(function (k,v){
            if (length == 1){
                $(v).find('.assign-psn-item').html('')
            }else {
                $(v).find('.assign-psn-item').html(k+1)
            }
        })
    }
    let getEditInformationAndFill = function (target) {
        let postalCode = target.find('.postalCode').text()
        let addrType = target.find('.addrType').text()
        let blkNo = target.find('.blkNo').text()
        let others = target.find('.floorNo-unitNo').text().split("-")
        let floorNo = others[0]
        let unitNo = others[1]
        let streetName = target.find('.streetName').text()
        let buildingName = target.find('.buildingName').text()
        let appPremisesOperationalUnitDtos = new Array()
        $('.addmore').each(function (index, item) {
            let others =  $(item).find('.addmorecontent').text().split("-")
            let floorNo = others[0]
            let unitNo = others[1]
            appPremisesOperationalUnitDtos.push({"floorNo":floorNo,"unitNo":unitNo})
        })
        let data = {
            'postalCode': postalCode,
            'addrType': addrType,
            'blkNo': blkNo,
            'floorNo': floorNo,
            'unitNo': unitNo,
            'appPremisesOperationalUnitDtos': appPremisesOperationalUnitDtos,
            'streetName': streetName,
            'buildingName': buildingName
        }
        let $premContent = $('.viewPrem')
        fillValue($premContent.find('.postalCode'), data.postalCode.trim());
        fillValue($premContent.find('.blkNo'), data.blkNo.trim());
        fillValue($premContent.find('.operationDiv:first').find('.floorNo'), data.floorNo.trim());
        fillValue($premContent.find('.operationDiv:first').find('.unitNo'), data.unitNo.trim());
        fillValue($premContent.find('.streetName'), data.streetName.trim());
        fillValue($premContent.find('.buildingName'), data.buildingName.trim());
        let code = data.addrType.trim() == 'Without Apt Blk' ? 'ADDTY002' : (data.addrType == 'Apt Blk' ? 'ADDTY001' : '')
        fillValue($premContent.find('.addrType'), code);
        $premContent.find('.addrType').trigger('change')
        let ele = $('.viewPrem')
        let flag = true

        console.log(appPremisesOperationalUnitDtos.length,"length=====>")
        ele.find('div.operationDivGroup .operationDiv').remove();
        $.each(appPremisesOperationalUnitDtos,function (index, value) {
            addFloorUnit(ele,flag,value)
        })
    }
    $('#hciNameClick').click(function () {
        var jQuery = $('#hciNameShowOrHidden').attr('style');
        if (jQuery.match("display: none")) {
            var a = $('#hciNameShowOrHidden').height();
            var b = $('#collapsePremise .panel-body').height();
            if (a > b) {
                $('#hciNameShowOrHidden').height(b)
            } else {
                $('#hciNameShowOrHidden').height(a)
            }
            $('#hciNameShowOrHidden').show();
            $('#addressShowOrHidden').hide();
        } else {
            $('#hciNameShowOrHidden').hide();
        }
    });


</script>