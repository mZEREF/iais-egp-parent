<div class="panel panel-default">
    <div class="panel-heading" id="headingPremise" role="tab">
        <h4 class="panel-title"><a role="button" class="collapse collapsed" data-toggle="collapse" href="#collapsePremise" aria-expanded="true" aria-controls="collapsePremise">Mode of Service Delivery</a></h4>
    </div>
    <div class="panel-collapse collapse" id="collapsePremise" role="tabpanel" aria-labelledby="headingPremise">
        <div class="panel-body">
            <c:forEach var="appGrpPremDto" items="${AppSubmissionDto.appGrpPremisesDtoList}"
                       varStatus="status">
                <div class="panel-main-content amend-preview-info">
                    <div class="row">
                        <p><strong>Mode of Service Delivery ${status.index+1}</strong></p>
                    </div>
                    <div class="preview-info">
                        <div class="row">
                            <div class="col-md-6">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span>Mode of Service Delivery</span></p>
                            </div>
                            <div class="col-md-6">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span>
                                    <c:if test="${appGrpPremDto.premisesType=='ONSITE'}">Premises</c:if>
                                    <c:if test="${appGrpPremDto.premisesType=='CONVEYANCE'}">Conveyance</c:if>
                                    <c:if test="${appGrpPremDto.premisesType=='OFFSITE'}">Off-site</c:if>
                                       <c:if test="${appGrpPremDto.premisesType=='EASMTS'}">
                                           Conveyance (in a mobile clinic / ambulance)
                                       </c:if>
                                    </span>
                                </p>
                            </div>
                        </div>
                        <c:if test="${'ONSITE'==appGrpPremDto.premisesType}">
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Fire Safety & Shelter Bureau Ref. No.</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appGrpPremDto.scdfRefNo}</span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Fire Safety Certificate Issued Date</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appGrpPremDto.certIssuedDtStr}</span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Name of HCI</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appGrpPremDto.hciName}</span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Postal Code</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appGrpPremDto.postalCode}</span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Address Type</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>
                                    <c:if test="${appGrpPremDto.addrType=='ADDTY001'}"> Apt Blk</c:if>
                                        <c:if test="${appGrpPremDto.addrType=='ADDTY002'}"> Without Apt Blk</c:if></span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Block / House No.</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appGrpPremDto.blkNo}</span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Floor / Unit No.</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                        <span>
                                            <c:out value="${appGrpPremDto.floorNo}"/>-<c:out value="${appGrpPremDto.unitNo}"/>
                                        </span>
                                    </p>
                                </div>
                            </div>
                            <c:forEach var="appPremisesOperationalUnit" items="${appGrpPremDto.appPremisesOperationalUnitDtos}">
                                <div class="row">
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span></span></p>
                                    </div>
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology">
                                            <span>
                                                <c:out value="${appPremisesOperationalUnit.floorNo}"/>-<c:out value="${appPremisesOperationalUnit.unitNo}"/>
                                            </span>
                                        </p>
                                    </div>
                                </div>
                            </c:forEach>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Street Name</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appGrpPremDto.streetName}</span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Building Name</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appGrpPremDto.buildingName}</span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Are you co-locating with another licensee?</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>
                                        <c:choose>
                                            <c:when test="${appGrpPremDto.locateWithOthers == '1'}">
                                                Yes
                                            </c:when>
                                            <c:when test="${appGrpPremDto.locateWithOthers == '0'}">
                                                No
                                            </c:when>
                                            <c:otherwise> </c:otherwise>
                                        </c:choose>
                                    </span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Office Telephone No.</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appGrpPremDto.offTelNo}</span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Operating Hours</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span></span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Weekly</span></p>
                                </div>
                                <div class="col-md-6">
                                    <div class="row">
                                        <div class="col-md-4">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span>Start</span></p>
                                        </div>
                                        <div class="col-md-4">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span>End</span></p>
                                        </div>
                                        <div class="col-md-3">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span>24 Hours</span></p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <c:choose>
                                <c:when test="${appGrpPremDto.weeklyDtoList.size()>0}">
                                    <c:set var="weeklySize" value="${appGrpPremDto.weeklyDtoList.size()-1}"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="weeklySize" value="0"/>
                                </c:otherwise>
                            </c:choose>
                            <c:forEach begin="0" end="${weeklySize}" step="1" varStatus="stat">
                                <c:set var="weeklyDto" value="${appGrpPremDto.weeklyDtoList[stat.index]}"/>
                                <div class="row">
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology">
                                            <span>
                                                <c:forEach var="weeklyName" items="${weeklyDto.transferNameForWeekly()}" varStatus="weeklyStat">
                                                    <iais:code code="${weeklyName}"/><c:if test="${!weeklyStat.last}">,</c:if>
                                                </c:forEach>
                                            </span>
                                        </p>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="row">
                                            <div class="col-md-4">
                                                <p class="form-check-label" aria-label="premise-1-cytology">
                                                <span>
                                                    <c:if test="${!weeklyDto.selectAllDay}">
                                                        <c:choose>
                                                            <c:when test="${weeklyDto.startFromHH.length()>1}">
                                                                ${weeklyDto.startFromHH}
                                                            </c:when>
                                                            <c:otherwise>
                                                                0${weeklyDto.startFromHH}
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <c:if test="${weeklyDto.startFromHH != null || weeklyDto.startFromMM != null}">
                                                            :
                                                        </c:if>
                                                        <c:choose>
                                                            <c:when test="${weeklyDto.startFromMM.length()>1}">
                                                                ${weeklyDto.startFromMM}
                                                            </c:when>
                                                            <c:otherwise>
                                                                0${weeklyDto.startFromMM}
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:if>
                                                </span>
                                                </p>
                                            </div>
                                            <div class="col-md-4">
                                                <p class="form-check-label" aria-label="premise-1-cytology">
                                                <span>
                                                    <c:if test="${!weeklyDto.selectAllDay}">
                                                        <c:choose>
                                                            <c:when test="${weeklyDto.endToHH.length()>1}">
                                                                ${weeklyDto.endToHH}
                                                            </c:when>
                                                            <c:otherwise>
                                                                0${weeklyDto.endToHH}
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <c:if test="${!weeklyDto.selectAllDay && (weeklyDto.endToHH != null || weeklyDto.endToMM != null)}">
                                                            :
                                                        </c:if>
                                                        <c:choose>
                                                            <c:when test="${weeklyDto.endToMM.length()>1}">
                                                                ${weeklyDto.endToMM}
                                                            </c:when>
                                                            <c:otherwise>
                                                                0${weeklyDto.endToMM}
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:if>
                                                </span>
                                                </p>
                                            </div>
                                            <div class="col-md-3">
                                                <c:if test="${weeklyDto.selectAllDay}">
                                                    <div class="form-check active">
                                                        <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                            <%--<c:forEach items="${appGrpPremDto.appPremPhOpenPeriodList}" var="appPremPhOpenPeriod" varStatus="statu">--%>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Public Holiday</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appPremPhOpenPeriod.dayName}</span></p>
                                </div>
                            </div>
                            <c:forEach items="${appGrpPremDto.phDtoList}" var="op" varStatus="opSta">
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="col-md-6" style="padding: 0px">
                                            <p class="form-check-label" aria-label="premise-1-cytology">
                                                <span>
                                               <c:forEach items="${op.selectValList}" var="phDto" varStatus="phDtoStat">
                                                   <iais:code code="${phDto}"/><c:if test="${!phDtoStat.last}">,</c:if>
                                               </c:forEach>
                                                </span>
                                            </p>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="row">
                                            <div class="col-md-4" style="padding-right: 0px">
                                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                                    <span>
                                                        <c:if test="${!op.selectAllDay}">
                                                            <fmt:formatDate value="${op.startFrom}" pattern="HH : mm"/>
                                                        </c:if>
                                                    </span>
                                                    </p>
                                            </div>

                                            <div class="col-md-4" style="padding-right: 0px">
                                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                                    <span>
                                                        <c:if test="${!op.selectAllDay}">
                                                            <fmt:formatDate value="${op.endTo}" pattern="HH : mm"/>
                                                        </c:if>
                                                    </span>
                                                    </p>
                                            </div>

                                            <div class="col-md-3" style="padding-right: 0px">
                                                <c:if test="${op.selectAllDay}">
                                                  <div class="form-check active">
                                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                                                  </div>
                                                </c:if>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Event</span></p>
                                </div>
                                <div class="col-md-6">
                                </div>
                            </div>
                            <c:forEach var="eventDto" items="${appGrpPremDto.eventDtoList}" varStatus="eventSta">
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="col-md-6" style="padding: 0px">
                                            <p class="form-check-label" aria-label="premise-1-cytology">
                                                    <span>${eventDto.eventName}</span>
                                            </p>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="row">
                                            <div class="col-md-4">
                                                <p class="form-check-label" aria-label="premise-1-cytology">
                                                    <span>
                                                     <fmt:formatDate value="${eventDto.startDate}" pattern="dd/MM/yyyy"/>
                                                    </span>
                                                </p>
                                            </div>
                                            <div class="col-md-4">
                                                <p class="form-check-label" aria-label="premise-1-cytology">
                                                    <span>
                                                     <fmt:formatDate value="${eventDto.endDate}"  pattern="dd/MM/yyyy"/>
                                                    </span>
                                                </p>
                                            </div>
                                            <div class="col-md-3"></div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:if>

                        <c:if test="${'CONVEYANCE'==appGrpPremDto.premisesType}">
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Name of HCI</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appGrpPremDto.hciName}</span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Vehicle No.</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appGrpPremDto.conveyanceVehicleNo}</span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Postal Code</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appGrpPremDto.conveyancePostalCode}</span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Address Type</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>
                                    <c:if test="${appGrpPremDto.conveyanceAddressType=='ADDTY001'}"> Apt Blk</c:if>
                                    <c:if test="${appGrpPremDto.conveyanceAddressType=='ADDTY002'}"> Without Apt Blk</c:if>
                                    </span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Block / House No.</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appGrpPremDto.conveyanceBlockNo}</span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Floor / Unit No.</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                        <span>
                                            <c:out value="${appGrpPremDto.conveyanceFloorNo}"/>-<c:out value="${appGrpPremDto.conveyanceUnitNo}"/>
                                        </span>
                                    </p>
                                </div>
                            </div>
                            <c:forEach var="appPremisesOperationalUnit" items="${appGrpPremDto.appPremisesOperationalUnitDtos}">
                                <div class="row">
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span></span></p>
                                    </div>
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology">
                                            <span>
                                                <c:out value="${appPremisesOperationalUnit.floorNo}"/>-<c:out value="${appPremisesOperationalUnit.unitNo}"/>
                                            </span>
                                        </p>
                                    </div>
                                </div>
                            </c:forEach>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Street Name</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appGrpPremDto.conveyanceStreetName}</span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Building Name</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appGrpPremDto.conveyanceBuildingName}</span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Email</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appGrpPremDto.conveyanceEmail}</span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Operating Hours</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span></span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Weekly</span></p>
                                </div>
                                <div class="col-md-6">
                                    <div class="row">
                                        <div class="col-md-4">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span>Start</span></p>
                                        </div>
                                        <div class="col-md-4">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span>End</span></p>
                                        </div>
                                        <div class="col-md-3">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span>24 Hours</span></p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <%--<c:forEach items="${appGrpPremDto.appPremPhOpenPeriodList}" var="appPremPhOpenPeriod" varStatus="statu">--%>
                            <c:choose>
                                <c:when test="${appGrpPremDto.weeklyDtoList.size()>0}">
                                    <c:set var="weeklySize" value="${appGrpPremDto.weeklyDtoList.size()-1}"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="weeklySize" value="0"/>
                                </c:otherwise>
                            </c:choose>
                            <c:forEach begin="0" end="${weeklySize}" step="1" varStatus="stat">
                                <c:set var="weeklyDto" value="${appGrpPremDto.weeklyDtoList[stat.index]}"/>

                                <div class="row">
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology">
                                            <span>
                                                <c:forEach var="weeklyName" items="${weeklyDto.transferNameForWeekly()}" varStatus="weeklyStat">
                                                    <iais:code code="${weeklyName}"/><c:if test="${!weeklyStat.last}">,</c:if>
                                                </c:forEach>
                                            </span>
                                        </p>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="row">
                                            <div class="col-md-4">
                                                <p class="form-check-label" aria-label="premise-1-cytology">
                                                <span>
                                                    <c:if test="${!weeklyDto.selectAllDay}">
                                                        <c:choose>
                                                            <c:when test="${weeklyDto.startFromHH.length()>1}">
                                                                ${weeklyDto.startFromHH}
                                                            </c:when>
                                                            <c:otherwise>
                                                                0${weeklyDto.startFromHH}
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <c:if test="${weeklyDto.startFromHH != null || weeklyDto.startFromMM != null}">
                                                            :
                                                        </c:if>
                                                        <c:choose>
                                                            <c:when test="${weeklyDto.startFromMM.length()>1}">
                                                                ${weeklyDto.startFromMM}
                                                            </c:when>
                                                            <c:otherwise>
                                                                0${weeklyDto.startFromMM}
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:if>
                                                </span>
                                                </p>
                                            </div>
                                            <div class="col-md-4">
                                                <p class="form-check-label" aria-label="premise-1-cytology">
                                                <span>
                                                    <c:if test="${!weeklyDto.selectAllDay}">
                                                        <c:choose>
                                                            <c:when test="${weeklyDto.endToHH.length()>1}">
                                                                ${weeklyDto.endToHH}
                                                            </c:when>
                                                            <c:otherwise>
                                                                0${weeklyDto.endToHH}
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <c:if test="${!weeklyDto.selectAllDay && (weeklyDto.endToHH != null || weeklyDto.endToMM != null)}">
                                                            :
                                                        </c:if>
                                                        <c:choose>
                                                            <c:when test="${weeklyDto.endToMM.length()>1}">
                                                                ${weeklyDto.endToMM}
                                                            </c:when>
                                                            <c:otherwise>
                                                                0${weeklyDto.endToMM}
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:if>
                                                </span>
                                                </p>
                                            </div>
                                            <div class="col-md-3">
                                                <c:if test="${weeklyDto.selectAllDay}">
                                                    <div class="form-check active">
                                                        <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                            <%--<c:forEach items="${appGrpPremDto.appPremPhOpenPeriodList}" var="appPremPhOpenPeriod" varStatus="statu">--%>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Public Holiday</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appPremPhOpenPeriod.dayName}</span></p>
                                </div>
                            </div>
                            <c:forEach items="${appGrpPremDto.phDtoList}" var="op" varStatus="opSta">
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="col-md-6" style="padding: 0px">
                                            <p class="form-check-label" aria-label="premise-1-cytology">
                                                <span>
                                               <c:forEach items="${op.selectValList}" var="phDto" varStatus="phDtoStat">
                                                   <iais:code code="${phDto}"/><c:if test="${!phDtoStat.last}">,</c:if>
                                               </c:forEach>
                                                </span>
                                            </p>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="row">
                                            <div class="col-md-4" style="padding-right: 0px">
                                                <p class="form-check-label" aria-label="premise-1-cytology">
                                                    <span>
                                                        <c:if test="${!op.selectAllDay}">
                                                            <fmt:formatDate value="${op.startFrom}" pattern="HH : mm"/>
                                                        </c:if>
                                                    </span>
                                                </p>
                                            </div>

                                            <div class="col-md-4" style="padding-right: 0px">
                                                <p class="form-check-label" aria-label="premise-1-cytology">
                                                    <span>
                                                        <c:if test="${!op.selectAllDay}">
                                                            <fmt:formatDate value="${op.endTo}" pattern="HH : mm"/>
                                                        </c:if>
                                                    </span>
                                                </p>
                                            </div>

                                            <div class="col-md-3" style="padding-right: 0px">
                                                <c:if test="${op.selectAllDay}">
                                                    <div class="form-check active">
                                                        <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Event</span></p>
                                </div>
                                <div class="col-md-6">
                                </div>
                            </div>
                            <c:forEach var="eventDto" items="${appGrpPremDto.eventDtoList}" varStatus="eventSta">
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="col-md-6" style="padding: 0px">
                                            <p class="form-check-label" aria-label="premise-1-cytology">
                                                <span>${eventDto.eventName}</span>
                                            </p>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="row">
                                            <div class="col-md-4">
                                                <p class="form-check-label" aria-label="premise-1-cytology">
                                                    <span>
                                                     <fmt:formatDate value="${eventDto.startDate}" pattern="dd/MM/yyyy"/>
                                                    </span>
                                                </p>
                                            </div>
                                            <div class="col-md-4">
                                                <p class="form-check-label" aria-label="premise-1-cytology">
                                                    <span>
                                                     <fmt:formatDate value="${eventDto.endDate}"  pattern="dd/MM/yyyy"/>
                                                    </span>
                                                </p>
                                            </div>
                                            <div class="col-md-3"></div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:if>

                        <c:if test="${'OFFSITE'==appGrpPremDto.premisesType}">
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Name of HCI</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appGrpPremDto.hciName}</span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Postal Code</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appGrpPremDto.offSitePostalCode}</span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Address Type</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>
                                    <c:if test="${appGrpPremDto.offSiteAddressType=='ADDTY001'}"> Apt Blk</c:if>
                                    <c:if test="${appGrpPremDto.offSiteAddressType=='ADDTY002'}"> Without Apt Blk</c:if>
                                    </span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Block / House No.</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appGrpPremDto.offSiteBlockNo}</span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Floor / Unit No.</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                        <span>
                                            <c:out value="${appGrpPremDto.offSiteFloorNo}"/>-<c:out value="${appGrpPremDto.offSiteUnitNo}"/>
                                        </span>
                                    </p>
                                </div>
                            </div>
                            <c:forEach var="appPremisesOperationalUnit" items="${appGrpPremDto.appPremisesOperationalUnitDtos}">
                                <div class="row">
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span></span></p>
                                    </div>
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology">
                                            <span>
                                                <c:out value="${appPremisesOperationalUnit.floorNo}"/>-<c:out value="${appPremisesOperationalUnit.unitNo}"/>
                                            </span>
                                        </p>
                                    </div>
                                </div>
                            </c:forEach>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Street Name</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appGrpPremDto.offSiteStreetName}</span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Building Name</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appGrpPremDto.offSiteBuildingName}</span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Email</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appGrpPremDto.offSiteEmail}</span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Operating Hours</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span></span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Weekly</span></p>
                                </div>
                                <div class="col-md-6">
                                    <div class="row">
                                        <div class="col-md-4">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span>Start</span></p>
                                        </div>
                                        <div class="col-md-4">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span>End</span></p>
                                        </div>
                                        <div class="col-md-3">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span>24 Hours</span></p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <%--<c:forEach items="${appGrpPremDto.appPremPhOpenPeriodList}" var="appPremPhOpenPeriod" varStatus="statu">--%>
                            <c:choose>
                                <c:when test="${appGrpPremDto.weeklyDtoList.size()>0}">
                                    <c:set var="weeklySize" value="${appGrpPremDto.weeklyDtoList.size()-1}"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="weeklySize" value="0"/>
                                </c:otherwise>
                            </c:choose>
                            <c:forEach begin="0" end="${weeklySize}" step="1" varStatus="stat">
                                <c:set var="weeklyDto" value="${appGrpPremDto.weeklyDtoList[stat.index]}"/>
                                <div class="row">
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology">
                                            <span>
                                                <c:forEach var="weeklyName" items="${weeklyDto.transferNameForWeekly()}" varStatus="weeklyStat">
                                                    <iais:code code="${weeklyName}"/><c:if test="${!weeklyStat.last}">,</c:if>
                                                </c:forEach>
                                            </span>
                                        </p>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="row">
                                            <div class="col-md-4">
                                                <p class="form-check-label" aria-label="premise-1-cytology">
                                                <span>
                                                    <c:if test="${!weeklyDto.selectAllDay}">
                                                        <c:choose>
                                                            <c:when test="${weeklyDto.startFromHH.length()>1}">
                                                                ${weeklyDto.startFromHH}
                                                            </c:when>
                                                            <c:otherwise>
                                                                0${weeklyDto.startFromHH}
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <c:if test="${weeklyDto.startFromHH != null || weeklyDto.startFromMM != null}">
                                                            :
                                                        </c:if>
                                                        <c:choose>
                                                            <c:when test="${weeklyDto.startFromMM.length()>1}">
                                                                ${weeklyDto.startFromMM}
                                                            </c:when>
                                                            <c:otherwise>
                                                                0${weeklyDto.startFromMM}
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:if>
                                                </span>
                                                </p>
                                            </div>
                                            <div class="col-md-4">
                                                <p class="form-check-label" aria-label="premise-1-cytology">
                                                <span>
                                                    <c:if test="${!weeklyDto.selectAllDay}">
                                                        <c:choose>
                                                            <c:when test="${weeklyDto.endToHH.length()>1}">
                                                                ${weeklyDto.endToHH}
                                                            </c:when>
                                                            <c:otherwise>
                                                                0${weeklyDto.endToHH}
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <c:if test="${!weeklyDto.selectAllDay && (weeklyDto.endToHH != null || weeklyDto.endToMM != null)}">
                                                            :
                                                        </c:if>
                                                        <c:choose>
                                                            <c:when test="${weeklyDto.endToMM.length()>1}">
                                                                ${weeklyDto.endToMM}
                                                            </c:when>
                                                            <c:otherwise>
                                                                0${weeklyDto.endToMM}
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:if>
                                                </span>
                                                </p>
                                            </div>
                                            <div class="col-md-3">
                                                <c:if test="${weeklyDto.selectAllDay}">
                                                    <div class="form-check active">
                                                        <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                            <%--<c:forEach items="${appGrpPremDto.appPremPhOpenPeriodList}" var="appPremPhOpenPeriod" varStatus="statu">--%>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Public Holiday</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appPremPhOpenPeriod.dayName}</span></p>
                                </div>
                            </div>
                            <c:forEach items="${appGrpPremDto.phDtoList}" var="op" varStatus="opSta">
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="col-md-6" style="padding: 0px">
                                            <p class="form-check-label" aria-label="premise-1-cytology">
                                                <span>
                                               <c:forEach items="${op.selectValList}" var="phDto" varStatus="phDtoStat">
                                                   <iais:code code="${phDto}"/><c:if test="${!phDtoStat.last}">,</c:if>
                                               </c:forEach>
                                                </span>
                                            </p>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="row">
                                            <div class="col-md-4" style="padding-right: 0px">
                                                <p class="form-check-label" aria-label="premise-1-cytology">
                                                    <span>
                                                        <c:if test="${!op.selectAllDay}">
                                                            <fmt:formatDate value="${op.startFrom}" pattern="HH : mm"/>
                                                        </c:if>
                                                    </span>
                                                </p>
                                            </div>

                                            <div class="col-md-4" style="padding-right: 0px">
                                                <p class="form-check-label" aria-label="premise-1-cytology">
                                                    <span>
                                                        <c:if test="${!op.selectAllDay}">
                                                            <fmt:formatDate value="${op.endTo}" pattern="HH : mm"/>
                                                        </c:if>
                                                    </span>
                                                </p>
                                            </div>

                                            <div class="col-md-3" style="padding-right: 0px">
                                                <c:if test="${op.selectAllDay}">
                                                    <div class="form-check active">
                                                        <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Event</span></p>
                                </div>
                                <div class="col-md-6">
                                </div>
                            </div>
                            <c:forEach var="eventDto" items="${appGrpPremDto.eventDtoList}" varStatus="eventSta">
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="col-md-6" style="padding: 0px">
                                            <p class="form-check-label" aria-label="premise-1-cytology">
                                                <span>${eventDto.eventName}</span>
                                            </p>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="row">
                                            <div class="col-md-4">
                                                <p class="form-check-label" aria-label="premise-1-cytology">
                                                    <span>
                                                     <fmt:formatDate value="${eventDto.startDate}" pattern="dd/MM/yyyy"/>
                                                    </span>
                                                </p>
                                            </div>
                                            <div class="col-md-4">
                                                <p class="form-check-label" aria-label="premise-1-cytology">
                                                    <span>
                                                     <fmt:formatDate value="${eventDto.endDate}"  pattern="dd/MM/yyyy"/>
                                                    </span>
                                                </p>
                                            </div>
                                            <div class="col-md-3"></div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:if>

                        <c:if test="${'EASMTS'==appGrpPremDto.premisesType}">
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Name of HCI</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appGrpPremDto.easMtsHciName}</span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Postal Code</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appGrpPremDto.easMtsPostalCode}</span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Address Type</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>
                                         <c:if test="${appGrpPremDto.easMtsAddressType=='ADDTY001'}"> Apt Blk</c:if>
                                        <c:if test="${appGrpPremDto.easMtsAddressType=='ADDTY002'}"> Without Apt Blk</c:if>
                                        </span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Block / House No.</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appGrpPremDto.easMtsBlockNo}</span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Floor / Unit No.</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span><c:out value="${appGrpPremDto.easMtsFloorNo}">-</c:out><c:out value="${appGrpPremDto.easMtsUnitNo}"></c:out></span></p>
                                </div>
                            </div>
                            <c:forEach var="appPremisesOperationalUnit" items="${appGrpPremDto.appPremisesOperationalUnitDtos}">
                                <div class="row">
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span></span></p>
                                    </div>
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology">
                                            <span>
                                                <c:out value="${appPremisesOperationalUnit.floorNo}"/>-<c:out value="${appPremisesOperationalUnit.unitNo}"/>
                                            </span>
                                        </p>
                                    </div>
                                </div>
                            </c:forEach>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Street Name</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appGrpPremDto.easMtsStreetName}</span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Building Name</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appGrpPremDto.easMtsBuildingName}</span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>For public/in-house use only?</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>
                                            <c:if test="${appGrpPremDto.easMtsUseOnly=='UOT001'}">For Public Use</c:if>
                                            <c:if test="${appGrpPremDto.easMtsUseOnly=='UOT002'}">For Public Use</c:if>
                                            </span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Public Email</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appGrpPremDto.easMtsPubEmail}</span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Public Hotline</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appGrpPremDto.easMtsPubHotline}</span></p>
                                </div>
                            </div>
                        <c:if test="${appGrpPremDto.premisesType !='EASMTS'}">
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Are you co-locating with another licensee?</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                        <span>
                                            <c:if test="${appGrpPremDto.easMtsCoLocation=='0'}">No</c:if>
                                            <c:if test="${appGrpPremDto.easMtsCoLocation=='1'}">Yes</c:if>
                                        </span>
                                    </p>
                                </div>
                            </div>
                        </c:if>
                        </c:if>
                        <c:choose>
                            <c:when test="${'EASMTS'==appGrpPremDto.premisesType}"></c:when>
                            <c:otherwise>
                                <div class="row">
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span>Operating Hours</span></p>
                                    </div>
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span></span></p>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span>Weekly</span></p>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="row">
                                            <div class="col-md-4">
                                                <p class="form-check-label" aria-label="premise-1-cytology"><span>Start</span></p>
                                            </div>
                                            <div class="col-md-4">
                                                <p class="form-check-label" aria-label="premise-1-cytology"><span>End</span></p>
                                            </div>
                                            <div class="col-md-3">
                                                <p class="form-check-label" aria-label="premise-1-cytology"><span>24 Hours</span></p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <!--weekly -->
                                <c:choose>
                                    <c:when test="${appGrpPremDto.weeklyDtoList.size()>0}">
                                        <c:set var="weeklySize" value="${appGrpPremDto.weeklyDtoList.size()-1}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="weeklySize" value="0"/>
                                    </c:otherwise>
                                </c:choose>
                                <c:forEach begin="0" end="${weeklySize}" step="1" varStatus="stat">
                                    <c:set var="weeklyDto" value="${appGrpPremDto.weeklyDtoList[stat.index]}"/>

                                    <%--<c:if test="${!stat.first}">
                                        <div class="row">
                                            <div class="col-md-12">
                                                <p class="form-check-label" aria-label="premise-1-cytology"><span>Weekly</span></p>
                                            </div>
                                        </div>
                                    </c:if>--%>


                                    <div class="row">
                                        <div class="col-md-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology">
                                            <span>
                                                <c:forEach var="weeklyName" items="${weeklyDto.selectValList}" varStatus="weeklyStat">
                                                    <iais:code code="${weeklyName}"/><c:if test="${!weeklyStat.last}">,</c:if>
                                                </c:forEach>
                                            </span>
                                            </p>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="row">
                                                <div class="col-md-4">
                                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                                <span>
                                                    <c:if test="${!weeklyDto.selectAllDay}">
                                                        <c:if test="${weeklyDto.startFromHH != null}">
                                                            <c:choose>
                                                                <c:when test="${weeklyDto.startFromHH.length()>1}">
                                                                    ${weeklyDto.startFromHH}
                                                                </c:when>
                                                                <c:otherwise>
                                                                    0${weeklyDto.startFromHH}
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:if>
                                                        <c:if test="${!weeklyDto.selectAllDay && (weeklyDto.startFromHH != null || weeklyDto.startFromMM != null)}">
                                                            :
                                                        </c:if>
                                                        <c:if test="${weeklyDto.startFromMM != null}">
                                                            <c:choose>
                                                                <c:when test="${weeklyDto.startFromMM.length()>1}">
                                                                    ${weeklyDto.startFromMM}
                                                                </c:when>
                                                                <c:otherwise>
                                                                    0${weeklyDto.startFromMM}
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:if>
                                                    </c:if>
                                                </span>
                                                    </p>
                                                </div>
                                                <div class="col-md-4">
                                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                                <span>
                                                    <c:if test="${!weeklyDto.selectAllDay}">
                                                        <c:if test="${weeklyDto.endToHH != null}">
                                                            <c:choose>
                                                                <c:when test="${weeklyDto.endToHH.length()>1}">
                                                                    ${weeklyDto.endToHH}
                                                                </c:when>
                                                                <c:otherwise>
                                                                    0${weeklyDto.endToHH}
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:if>
                                                        <c:if test="${!weeklyDto.selectAllDay && (weeklyDto.endToHH != null || weeklyDto.endToMM != null)}">
                                                            :
                                                        </c:if>
                                                        <c:if test="${weeklyDto.endToMM != null}">
                                                            <c:choose>
                                                                <c:when test="${weeklyDto.endToMM.length()>1}">
                                                                    ${weeklyDto.endToMM}
                                                                </c:when>
                                                                <c:otherwise>
                                                                    0${weeklyDto.endToMM}
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:if>
                                                    </c:if>
                                                </span>
                                                    </p>
                                                </div>
                                                <div class="col-md-3">
                                                    <c:if test="${weeklyDto.selectAllDay}">
                                                        <div class="form-check active">
                                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                                                        </div>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                                <!--ph -->
                                <c:choose>
                                    <c:when test="${appGrpPremDto.phDtoList.size()>0}">
                                        <c:set var="phSize" value="${appGrpPremDto.phDtoList.size()-1}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="phSize" value="0"/>
                                    </c:otherwise>
                                </c:choose>
                                <c:forEach begin="0" end="${phSize}" step="1" varStatus="stat">
                                    <c:set var="phDto" value="${appGrpPremDto.phDtoList[stat.index]}"/>
                                    <c:if test="${stat.first}">
                                        <div class="row">
                                            <div class="col-md-12">
                                                <p class="form-check-label" aria-label="premise-1-cytology"><span>Public Holiday</span></p>
                                            </div>
                                        </div>
                                    </c:if>



                                    <div class="row">
                                        <div class="col-md-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology">
                                            <span>
                                                <c:forEach var="weeklyName" items="${phDto.selectValList}" varStatus="weeklyStat">
                                                    <iais:code code="${weeklyName}"/><c:if test="${!weeklyStat.last}">,</c:if>
                                                </c:forEach>
                                            </span>
                                            </p>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="row">
                                                <div class="col-md-4">
                                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                                <span>
                                                    <c:if test="${!phDto.selectAllDay}">
                                                        <c:if test="${phDto.startFromHH != null}">
                                                            <c:choose>
                                                                <c:when test="${phDto.startFromHH.length()>1}">
                                                                    ${phDto.startFromHH}
                                                                </c:when>
                                                                <c:otherwise>
                                                                    0${phDto.startFromHH}
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:if>
                                                        <c:if test="${phDto.startFromHH != null || phDto.startFromMM != null}">
                                                            :
                                                        </c:if>
                                                        <c:if test="${phDto.startFromMM != null}">
                                                            <c:choose>
                                                                <c:when test="${phDto.startFromMM.length()>1}">
                                                                    ${phDto.startFromMM}
                                                                </c:when>
                                                                <c:otherwise>
                                                                    0${phDto.startFromMM}
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:if>
                                                    </c:if>
                                                </span>
                                                    </p>
                                                </div>
                                                <div class="col-md-4">
                                                    <p class="form-check-label" aria-label="premise-1-cytology">
                                                <span>
                                                    <c:if test="${!phDto.selectAllDay}">
                                                        <c:if test="${phDto.endToHH != null}">
                                                            <c:choose>
                                                                <c:when test="${phDto.endToHH.length()>1}">
                                                                    ${phDto.endToHH}
                                                                </c:when>
                                                                <c:otherwise>
                                                                    0${phDto.endToHH}
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:if>
                                                        <c:if test="${phDto.endToHH != null || phDto.endToMM != null}">
                                                            :
                                                        </c:if>
                                                        <c:if test="${phDto.endToMM != null}">
                                                            <c:choose>
                                                                <c:when test="${phDto.endToMM.length()>1}">
                                                                    ${phDto.endToMM}
                                                                </c:when>
                                                                <c:otherwise>
                                                                    0${phDto.endToMM}
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:if>
                                                    </c:if>
                                                </span>
                                                    </p>
                                                </div>
                                                <div class="col-md-3">
                                                    <c:if test="${phDto.selectAllDay}">
                                                        <div class="form-check active">
                                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                                                        </div>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>

                                <!--event -->
                                <c:choose>
                                    <c:when test="${appGrpPremDto.eventDtoList.size()>0}">
                                        <c:set var="eventSize" value="${appGrpPremDto.eventDtoList.size()-1}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="eventSize" value="0"/>
                                    </c:otherwise>
                                </c:choose>
                                <c:forEach begin="0" end="${eventSize}" step="1" varStatus="stat">
                                    <c:set var="eventDto" value="${appGrpPremDto.eventDtoList[stat.index]}"/>
                                    <c:if test="${stat.first}">
                                        <div class="row">
                                            <div class="col-md-12">
                                                <p class="form-check-label" aria-label="premise-1-cytology"><span>Event</span></p>
                                            </div>
                                        </div>
                                    </c:if>

                                    <div class="row">
                                        <div class="col-md-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology">
                                            <span>
                                                <c:out value="${eventDto.eventName}"/>
                                            </span>
                                            </p>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="row">
                                                <div class="col-md-4">
                                                    <p class="form-check-label" aria-label="premise-1-cytology"><span><c:out value="${eventDto.startDateStr}"/></span></p>
                                                </div>
                                                <div class="col-md-4">
                                                    <p class="form-check-label" aria-label="premise-1-cytology"><span><c:out value="${eventDto.endDateStr}"/></span></p>
                                                </div>
                                                <div class="col-md-3">
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>
