
<div class="panel panel-default">
  <div class="panel-heading" id="headingPremise" role="tab">
    <h4 class="panel-title"><a  class="collapsed" role="button" data-toggle="collapse" href="#collapsePremise"
                                aria-expanded="true" aria-controls="collapsePremise">Mode of Service Delivery</a>
    </h4>
  </div>
  <div class="panel-collapse collapse" id="collapsePremise" role="tabpanel"
       aria-labelledby="headingPremise">
    <div class="panel-body">
      <p class="text-right">
        <c:if test="${rfi=='rfi'}">
          <c:if test="${(appEdit.premisesEdit || appEdit.premisesListEdit)&& canEidtPremise }">
            <input class="form-check-input" id="premisesCheckbox" type="checkbox"
                   name="editCheckbox" <c:if test="${pageEdit.premisesEdit}">checked</c:if> aria-invalid="false" value="premises">
          </c:if>
        </c:if>
      </p>
      <c:forEach var="appGrpPremDto" items="${appSubmissionDto.appGrpPremisesDtoList}"
                 varStatus="status">
        <c:set var="oldAppGrpPremDto" value="${appSubmissionDto.oldAppSubmissionDto.appGrpPremisesDtoList[status.index]}"/>
        <div class="panel-main-content postion-relative">
          <div class="preview-info">
            <div class="row">
              <div class="col-md-6">
                <label>Mode of Service Delivery ${status.index+1} </label>
              </div>
            </div>

            <div class="row">
              <div class="col-md-6">
                Mode of Service Delivery
              </div>
              <div class="col-md-6">
                <div class="col-md-6">
                  <span class="newVal " attr="${appGrpPremDto.premisesType}">
                    <c:if test="${appGrpPremDto.premisesType=='OFFSITE'}">
                       Off-site
                    </c:if>
                   <c:if test="${appGrpPremDto.premisesType=='ONSITE'}">
                      Premises
                   </c:if>
                    <c:if test="${appGrpPremDto.premisesType=='CONVEYANCE'}">
                       Conveyance
                   </c:if>
                    <c:if test="${appGrpPremDto.premisesType=='EASMTS'}">
                       Conveyance (in a mobile clinic / ambulance)
                    </c:if>
                </div>
                <div class="col-md-6">
                  <span class="oldVal " attr="${oldAppGrpPremDto.premisesType}" style="display: none">
                     <c:if test="${oldAppGrpPremDto.premisesType=='OFFSITE'}">
                       Off-site
                     </c:if>
                   <c:if test="${oldAppGrpPremDto.premisesType=='ONSITE'}">
                     Premises
                   </c:if>
                    <c:if test="${oldAppGrpPremDto.premisesType=='CONVEYANCE'}">
                      Conveyance
                    </c:if>
                    <c:if test="${oldAppGrpPremDto.premisesType=='EASMTS'}">
                      Conveyance (in a mobile clinic / ambulance)
                    </c:if>
                  </span>
                </div>
              </div>
            </div>

            <c:if test="${'ONSITE'==appGrpPremDto.premisesType}">
              <div class="row">
                <div class="col-md-6">
                  Fire Safety & Shelter Bureau Ref. No.
                </div>
                <div class="col-md-6">
                  <div class="col-md-12">
                    <span class="newVal " attr="${appGrpPremDto.scdfRefNo}"><c:out value="${appGrpPremDto.scdfRefNo}"/></span>
                    <br>
                    <span class="oldVal " attr="${oldAppGrpPremDto.scdfRefNo}" style="display: none">
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
                       <span class="newVal " attr="${appGrpPremDto.certIssuedDt}">
                      <fmt:formatDate value="${appGrpPremDto.certIssuedDt}" pattern="dd/MM/yyyy"/>
                    </span>
                  </div>
                  <div class="col-md-6">
                    <c:if test="${appSubmissionDto.oldAppSubmissionDto!=null}">
                       <span class="oldVal " attr="${oldAppGrpPremDto.certIssuedDt}"
                             style="display: none"><fmt:formatDate value="${oldAppGrpPremDto.certIssuedDt}" pattern="dd/MM/yyyy"/>
                          </span>
                    </c:if>
                  </div>
                </div>
              </div>
            </c:if>
            <div class="row">
              <div class="col-md-6">
                Business Name
                <a class="btn-tooltip styleguide-tooltip" id="hciNameClick" <c:if test="${empty appGrpPremDto.applicationViewHciNameDtos}">style="display: none" </c:if> data-toggle="tooltip" data-html="true" title="" data-original-title="">i</a>
              </div>
              <div  class="col-md-7" style="position: absolute;z-index: 100;left: 40%;background-color: #EEEEEE;display: none;margin-top: 2%;overflow-y: scroll" id="hciNameShowOrHidden">
                <p>The  Business Name is currently used by another licensee</p>
                <br>
                <table aria-describedby="" border="1px" style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;text-align: center;background-color: #ffffff;width: 100%">
                  <tr>
                    <th scope="col"  class="col-md-4">Name of Licensee</th>
                    <th scope="col"  class="col-md-4">HCI Name</th>
                    <th scope="col"  class="col-md-4">Service Name</th>
                  </tr>
                  <c:forEach items="${appGrpPremDto.applicationViewHciNameDtos}" var="applicationViewHciNameDtos">
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
                  <span class="newVal " attr="${appGrpPremDto.hciName}"><c:out value="${appGrpPremDto.hciName}"/></span>
                  <br>
                  <span class="oldVal " attr="${oldAppGrpPremDto.hciName}"
                        style="display: none"><c:out value="${oldAppGrpPremDto.hciName}"/></span>
                </div>

              </div>
            </div>
            <c:if test="${'CONVEYANCE'==appGrpPremDto.premisesType}">
              <div class="row">
                <div class="col-md-6">
                  Vehicle No.
                </div>
                <div class="col-md-6">
                  <div  class="col-md-6">
                    <span class="newVal " attr="${appGrpPremDto.conveyanceVehicleNo}"><c:out value="${appGrpPremDto.conveyanceVehicleNo}"/></span>
                  </div>
                  <div  class="col-md-6">
                       <span class="oldVal " attr="${oldAppGrpPremDto.conveyanceVehicleNo}"
                             style="display: none"><c:out value="${oldAppGrpPremDto.conveyanceVehicleNo}"/></span>
                  </div>
                </div>
              </div>
            </c:if>
            <div class="row">
              <div class="col-md-6">
                Postal Code
                <a class="btn-tooltip styleguide-tooltip" id="addressClick" <c:if test="${empty appGrpPremDto.applicationViewAddress}">style="display: none" </c:if> data-toggle="tooltip" data-html="true" title="" data-original-title="">i</a>
              </div>
                <div  class="col-md-7"  style="position: absolute;z-index: 100;left: 40%;background-color: #EEEEEE;margin-top:2%;display: none;overflow-y: scroll;" id="addressShowOrHidden">
                    <p>The address of the mode of service delivery keyed in by applicant is currently used by another licensee</p>
                    <table aria-describedby="" border="1px" style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;text-align: center;background-color: #ffffff;width: 100%">
                      <tr>
                        <th scope="col"  class="col-md-4">Name of Licensee</th>
                        <th scope="col"  class="col-md-4">HCI Name</th>
                        <th scope="col"  class="col-md-4">Service Name</th>
                      </tr>
                      <c:forEach items="${appGrpPremDto.applicationViewAddress}" var="applicationViewAddress">
                      <tr>
                        <td>${applicationViewAddress.licensee}</td>
                        <td>${applicationViewAddress.hciName}</td>
                        <td>${applicationViewAddress.serviceName}</td>
                      </tr>
                      </c:forEach>
                    </table>

                </div>


              <div class="col-md-6">
                <div  class="col-md-6">
                     <span class="newVal " attr="${appGrpPremDto.postalCode}"><c:out value="${appGrpPremDto.postalCode}"/></span>
                </div>
                <div  class="col-md-6">
                     <span class="oldVal " attr="${oldAppGrpPremDto.postalCode}" style="display: none"><c:out value="${oldAppGrpPremDto.postalCode}"/></span>
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
                        <c:if test="${appGrpPremDto.addrType=='ADDTY001'}"> Apt Blk</c:if>
                        <c:if test="${appGrpPremDto.addrType=='ADDTY002'}"> Without Apt Blk</c:if>

                        </span>

                </div>
                <div class="col-md-6">
                       <span class="oldVal " attr="${oldAppGrpPremDto.addrType}" style="display: none">
                            <c:if test="${oldAppGrpPremDto.addrType=='ADDTY001'}"> Apt Blk</c:if>
                            <c:if test="${oldAppGrpPremDto.addrType=='ADDTY002'}"> Without Apt Blk</c:if>
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
                     <span class="newVal " attr="${appGrpPremDto.blkNo}"><c:out value="${appGrpPremDto.blkNo}"/></span>
                </div>
                <div class="col-md-6">
                    <span class="oldVal " attr="${oldAppGrpPremDto.blkNo}" style="display: none"><c:out value="${oldAppGrpPremDto.blkNo}"/></span>
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
                       <span class="oldVal " attr="${oldAppGrpPremDto.floorNo}${oldAppGrpPremDto.unitNo}" style="display: none">
                         <c:out value="${oldAppGrpPremDto.floorNo}-${oldAppGrpPremDto.unitNo}"/>
                       </span>
                </div>
              </div>
            </div>


         <%--   <div class="row">
              <div class="col-md-6">
                Unit No.
              </div>
              <div class="col-md-6">
                <div class="col-md-6"><span class="newVal " attr="${appGrpPremDto.unitNo}"><c:out value="${appGrpPremDto.unitNo}"/></span>

                </div>
                <div class="col-md-6">
                        <span class="oldVal " attr="${oldAppGrpPremDto.unitNo}"
                              style="display: none"><c:out value="${oldAppGrpPremDto.unitNo}"/></span>
                </div>
              </div>
            </div>
--%>
            <c:forEach items="${appGrpPremDto.appPremisesOperationalUnitDtos}" var="appPremisesOperationalUnitDto" varStatus="unitIndex">
              <div class="row">
                <div class="col-md-6">

                </div>
                <div class="col-md-6">
                  <div class="col-md-6">
                    <span class="newVal " attr="${appPremisesOperationalUnitDto.floorNo}${appPremisesOperationalUnitDto.unitNo}">
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
                    <span class="oldVal " attr="${oldAppGrpPremDto.appPremisesOperationalUnitDtos[unitIndex.index].floorNo}${oldAppGrpPremDto.appPremisesOperationalUnitDtos[unitIndex.index].unitNo}" style="display: none">
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
                   <span class="newVal " attr="${appGrpPremDto.streetName}">${appGrpPremDto.streetName}</span>
                   <br>
                   <span class="oldVal " attr="${oldAppGrpPremDto.streetName}" style="display: none">${oldAppGrpPremDto.streetName}</span>
                </div>
              </div>

            </div>
            <div class="row">
              <div class="col-md-6">
                Building Name
              </div>
              <div class="col-md-6">
                <div class="col-md-12">
                  <span class="newVal " attr="${appGrpPremDto.buildingName}">${appGrpPremDto.buildingName}</span>
                  <br>
                  <span class="oldVal " attr="${oldAppGrpPremDto.buildingName}" style="display: none">${oldAppGrpPremDto.buildingName }</span>
                </div>
                <div class="col-md-6">

                </div>
              </div>
            </div>
            <c:if test="${appGrpPremDto.premisesType != 'EASMTS'}">
            <div class="row">
              <div class="col-md-6">
                Email
              </div>
              <div class="col-md-6">
                <div class="col-md-12">
                  <span class="newVal " attr="${appGrpPremDto.easMtsPubEmail}">${appGrpPremDto.easMtsPubEmail}</span>
                  <br>
                  <span class="oldVal " attr="${oldAppGrpPremDto.easMtsPubEmail}" style="display: none">${oldAppGrpPremDto.easMtsPubEmail }</span>
                </div>
                <div class="col-md-6">
                </div>
              </div>
            </div>
            </c:if>
            <c:if test="${'ONSITE'==appGrpPremDto.premisesType}">
              <div class="row">
                <div class="col-md-6">
                  Are you co-locating with another licensee?
                </div>
                <div class="col-md-6">
                  <div class="col-md-6">
                     <span class="newVal" attr="${appGrpPremDto.locateWithOthers}">
                      <c:if test="${appGrpPremDto.locateWithOthers=='0'}">
                        No
                      </c:if>
                       <c:if test="${appGrpPremDto.locateWithOthers=='1'}">
                         Yes
                       </c:if>
                    </span>
                  </div>
                  <div class="col-md-6">
                     <span class="oldVal" style="display: none" attr="${oldAppGrpPremDto.locateWithOthers}">
                      <c:if test="${oldAppGrpPremDto.locateWithOthers=='0'}">
                        No
                      </c:if>
                       <c:if test="${oldAppGrpPremDto.locateWithOthers=='1'}">
                         Yes
                       </c:if>
                    </span>
                  </div>

                </div>
              </div>
              <div class="row">
                <div class="col-md-6">
                  Office Telephone No.
                </div>
                <div class="col-md-6">
                  <div class="col-md-6">
                         <span class="newVal "
                               attr="${appGrpPremDto.offTelNo}"><c:out
                                 value="${appGrpPremDto.offTelNo}"/></span>

                  </div>
                  <div class="col-md-6">
                          <span class="oldVal" attr="${oldAppGrpPremDto.offTelNo}"
                                style="display: none"><c:out value="${oldAppGrpPremDto.offTelNo}"/></span>
                  </div>
                </div>
              </div>
            </c:if>
            <c:if test="${appGrpPremDto.premisesType=='OFFSITE' || appGrpPremDto.premisesType=='ONSITE' || appGrpPremDto.premisesType=='CONVEYANCE'}">
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
            </c:if>
            <c:if test="${appGrpPremDto.premisesType=='EASMTS'}">
              <div class="row">
                <div class="col-md-6">
                  For public/in-house use only?
                </div>
                <div class="col-md-6">
                    <div class="col-md-6">
                      <span class="newVal " attr="${appGrpPremDto.easMtsUseOnly}">
                        <iais:code code="${appGrpPremDto.easMtsUseOnly}"></iais:code>
                      </span>
                    </div>
                    <div class="col-md-6">
                      <span class="oldVal " style="display: none" attr="${oldAppGrpPremDto.easMtsUseOnly}">
                         <iais:code code="${oldAppGrpPremDto.easMtsUseOnly}"></iais:code>
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
                        <c:out value="${appGrpPremDto.easMtsPubEmail}"></c:out>
                      </span>
                      <br>
                      <span class="oldVal " style="display: none" attr="${oldAppGrpPremDto.easMtsPubEmail}">
                           <c:out value="${oldAppGrpPremDto.easMtsPubEmail}"></c:out>
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
                      <c:out value="${appGrpPremDto.easMtsPubHotline}"></c:out>
                    </span>
                  </div>
                  <div class="col-md-6">
                    <span class="oldVal " style="display: none" attr="${oldAppGrpPremDto.easMtsPubHotline}">
                      <c:out value="${oldAppGrpPremDto.easMtsPubHotline}"></c:out>
                    </span>
                  </div>
                </div>
              </div>
            </c:if>
          </div>
        </div>
      </c:forEach>
    </div>
  </div>
</div>
<script>
  $('#hciNameClick').click(function () {
      var jQuery = $('#hciNameShowOrHidden').attr('style');
      if(jQuery.match("display: none")){
          var a = $('#hciNameShowOrHidden').height();
          var b = $('#collapsePremise .panel-body').height();
          if(a>b){
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

  $('#addressClick').click(function () {
      var jQuery = $('#addressShowOrHidden').attr('style');
      if(jQuery.match("display: none")){
        var a = $('#addressShowOrHidden').height();
        var b = $('#collapsePremise .panel-body').height();
        if(a>b){
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
</script>