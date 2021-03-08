<div class="panel panel-default">
    <div class="panel-heading" id="headingPremise" role="tab">
        <h4 class="panel-title"><a role="button" class="collapse collapsed" data-toggle="collapse" href="#collapsePremise" aria-expanded="true" aria-controls="collapsePremise">Premises</a></h4>
    </div>
    <div class="panel-collapse collapse" id="collapsePremise" role="tabpanel" aria-labelledby="headingPremise">
        <div class="panel-body">
            <c:forEach var="appGrpPremDto" items="${AppSubmissionDto.appGrpPremisesDtoList}"
                       varStatus="status">
                <div class="panel-main-content amend-preview-info">
                    <div class="row">
                        <p><strong>Premises</strong></p>
                    </div>
                    <div class="preview-info">
                        <div class="row">
                            <div class="col-md-6">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span>Premises Type</span></p>
                            </div>
                            <div class="col-md-6">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span>
                                    <c:if test="${appGrpPremDto.premisesType=='ONSITE'}">On-site</c:if>
                                    <c:if test="${appGrpPremDto.premisesType=='CONVEYANCE'}">Conveyance</c:if>
                                    <c:if test="${appGrpPremDto.premisesType=='OFFSITE'}">Off-site</c:if>
                                    </span>
                                </p>
                            </div>
                        </div>
                        <c:if test="${'ONSITE'==appGrpPremDto.premisesType}">
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
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Fire Safety & Shelter Bureau Ref. No.</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appGrpPremDto.scdfRefNo}</span></p>
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
                                            <c:when test="${appGrpPremDto.locateWithOthers}">
                                                Yes
                                            </c:when>
                                            <c:otherwise>
                                                No
                                            </c:otherwise>
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
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span>All day</span></p>
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

                                <c:if test="${!stat.first}">
                                    <div class="row">
                                        <div class="col-md-12">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span>Weekly</span></p>
                                        </div>
                                    </div>
                                </c:if>


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
                            <c:choose>
                                <c:when test="${!empty appGrpPremDto.appPremPhOpenPeriodList}">
                                    <c:set var="phLength" value="${appGrpPremDto.appPremPhOpenPeriodList.size()-1}"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="phLength" value="0"/>
                                </c:otherwise>
                            </c:choose>
                            <c:forEach begin="0" end="${phLength}" step="1" varStatus="statu">
                                <c:set var="appPremPhOpenPeriod" value="${appGrpPremDto.appPremPhOpenPeriodList[statu.index]}"/>
                                <div class="row">
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span>Select Public Holiday</span></p>
                                    </div>
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span>${appPremPhOpenPeriod.dayName}</span></p>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span>Public Holiday Operating Hours (Start)</span></p>
                                    </div>
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span>
                                             <c:choose>
                                                 <c:when test="${empty appPremPhOpenPeriod}">

                                                 </c:when>
                                                 <c:when test="${appPremPhOpenPeriod.onsiteStartFromHH.length()>1}">
                                                     ${appPremPhOpenPeriod.onsiteStartFromHH}
                                                 </c:when>
                                                 <c:otherwise>
                                                     0${appPremPhOpenPeriod.onsiteStartFromHH}
                                                 </c:otherwise>
                                             </c:choose>
                                            <c:if test="${!empty appPremPhOpenPeriod}">
                                                :
                                            </c:if>
                                            <c:choose>
                                                <c:when test="${empty appPremPhOpenPeriod}">

                                                </c:when>
                                                <c:when test="${appPremPhOpenPeriod.onsiteStartFromMM.length()>1}">
                                                    ${appPremPhOpenPeriod.onsiteStartFromMM}
                                                </c:when>
                                                <c:otherwise>
                                                    0${appPremPhOpenPeriod.onsiteStartFromMM}
                                                </c:otherwise>
                                            </c:choose>
                                        </span></p>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span>Public Holiday Operating Hours (End)</span></p>
                                    </div>
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span>
                                            <c:choose>
                                                <c:when test="${empty appPremPhOpenPeriod}">

                                                </c:when>
                                                <c:when test="${appPremPhOpenPeriod.onsiteEndToHH.length()>1}">
                                                    ${appPremPhOpenPeriod.onsiteEndToHH}
                                                </c:when>
                                                <c:otherwise>
                                                    0${appPremPhOpenPeriod.onsiteEndToHH}
                                                </c:otherwise>
                                            </c:choose>
                                            <c:if test="${!empty appPremPhOpenPeriod}">
                                                :
                                            </c:if>
                                            <c:choose>
                                                <c:when test="${empty appPremPhOpenPeriod}">

                                                </c:when>
                                                <c:when test="${appPremPhOpenPeriod.onsiteEndToMM.length()>1}">
                                                    ${appPremPhOpenPeriod.onsiteEndToMM}
                                                </c:when>
                                                <c:otherwise>
                                                    0${appPremPhOpenPeriod.onsiteEndToMM}
                                                </c:otherwise>
                                            </c:choose>
                                        </span></p>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:if>

                        <c:if test="${'CONVEYANCE'==appGrpPremDto.premisesType}">
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
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Operating Hours (Start)</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>
                                        <c:choose>
                                            <c:when test="${appGrpPremDto.conStartHH.length()>1}">
                                                ${appGrpPremDto.conStartHH}
                                            </c:when>
                                            <c:otherwise>
                                                0${appGrpPremDto.conStartHH}
                                            </c:otherwise>
                                        </c:choose>
                                        :
                                        <c:choose>
                                            <c:when test="${appGrpPremDto.conStartMM.length()>1}">
                                                ${appGrpPremDto.conStartMM}
                                            </c:when>
                                            <c:otherwise>
                                                0${appGrpPremDto.conStartMM}
                                            </c:otherwise>
                                        </c:choose>
                                    </span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Operating Hours (End)</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>
                                        <c:choose>
                                            <c:when test="${appGrpPremDto.conEndHH.length()>1}">
                                                ${appGrpPremDto.conEndHH}
                                            </c:when>
                                            <c:otherwise>
                                                0${appGrpPremDto.conEndHH}
                                            </c:otherwise>
                                        </c:choose>
                                        :
                                        <c:choose>
                                            <c:when test="${appGrpPremDto.conEndMM.length()>1}">
                                                ${appGrpPremDto.conEndMM}
                                            </c:when>
                                            <c:otherwise>
                                                0${appGrpPremDto.conEndMM}
                                            </c:otherwise>
                                        </c:choose>
                                    </span></p>
                                </div>
                            </div>
                            <%--<c:forEach items="${appGrpPremDto.appPremPhOpenPeriodList}" var="appPremPhOpenPeriod" varStatus="statu">--%>
                            <c:choose>
                                <c:when test="${!empty appGrpPremDto.appPremPhOpenPeriodList}">
                                    <c:set var="phLength" value="${appGrpPremDto.appPremPhOpenPeriodList.size()-1}"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="phLength" value="0"/>
                                </c:otherwise>
                            </c:choose>
                            <c:forEach begin="0" end="${phLength}" step="1" varStatus="statu">
                                <c:set var="appPremPhOpenPeriod" value="${appGrpPremDto.appPremPhOpenPeriodList[statu.index]}"/>
                                <div class="row">
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span>Select Public Holiday</span></p>
                                    </div>
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span>${appPremPhOpenPeriod.dayName}</span></p>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span>Public Holiday Operating Hours (Start)</span></p>
                                    </div>
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span>
                                            <c:choose>
                                                <c:when test="${empty appPremPhOpenPeriod}">

                                                </c:when>
                                                <c:when test="${appPremPhOpenPeriod.convStartFromHH.length()>1}">
                                                    ${appPremPhOpenPeriod.convStartFromHH}
                                                </c:when>
                                                <c:otherwise>
                                                    0${appPremPhOpenPeriod.convStartFromHH}
                                                </c:otherwise>
                                            </c:choose>
                                            <c:if test="${!empty appPremPhOpenPeriod}">
                                                :
                                            </c:if>
                                            <c:choose>
                                                <c:when test="${empty appPremPhOpenPeriod}">

                                                </c:when>
                                                <c:when test="${appPremPhOpenPeriod.convStartFromMM.length()>1}">
                                                    ${appPremPhOpenPeriod.convStartFromMM}
                                                </c:when>
                                                <c:otherwise>
                                                    0${appPremPhOpenPeriod.convStartFromMM}
                                                </c:otherwise>
                                            </c:choose>
                                        </span></p>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span>Public Holiday Operating Hours (End)</span></p>
                                    </div>
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span>
                                            <c:choose>
                                                <c:when test="${empty appPremPhOpenPeriod}">

                                                </c:when>
                                                <c:when test="${appPremPhOpenPeriod.convEndToHH.length()>1}">
                                                    ${appPremPhOpenPeriod.convEndToHH}
                                                </c:when>
                                                <c:otherwise>
                                                    0${appPremPhOpenPeriod.convEndToHH}
                                                </c:otherwise>
                                            </c:choose>
                                            <c:if test="${!empty appPremPhOpenPeriod}">
                                                :
                                            </c:if>
                                            <c:choose>
                                                <c:when test="${empty appPremPhOpenPeriod}">

                                                </c:when>
                                                <c:when test="${appPremPhOpenPeriod.convEndToMM.length()>1}">
                                                    ${appPremPhOpenPeriod.convEndToMM}
                                                </c:when>
                                                <c:otherwise>
                                                    0${appPremPhOpenPeriod.convEndToMM}
                                                </c:otherwise>
                                            </c:choose>
                                        </span></p>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:if>

                        <c:if test="${'OFFSITE'==appGrpPremDto.premisesType}">
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
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Operating Hours (Start)</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>
                                        <c:choose>
                                            <c:when test="${appGrpPremDto.offSiteStartHH.length()>1}">
                                                ${appGrpPremDto.offSiteStartHH}
                                            </c:when>
                                            <c:otherwise>
                                                0${appGrpPremDto.offSiteStartHH}
                                            </c:otherwise>
                                        </c:choose>
                                        :
                                        <c:choose>
                                            <c:when test="${appGrpPremDto.offSiteStartMM.length()>1}">
                                                ${appGrpPremDto.offSiteStartMM}
                                            </c:when>
                                            <c:otherwise>
                                                0${appGrpPremDto.offSiteStartMM}
                                            </c:otherwise>
                                        </c:choose>
                                    </span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Operating Hours (End)</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>
                                        <c:choose>
                                            <c:when test="${appGrpPremDto.offSiteEndHH.length()>1}">
                                                ${appGrpPremDto.offSiteEndHH}
                                            </c:when>
                                            <c:otherwise>
                                                0${appGrpPremDto.offSiteEndHH}
                                            </c:otherwise>
                                        </c:choose>
                                        :
                                        <c:choose>
                                            <c:when test="${appGrpPremDto.offSiteEndMM.length()>1}">
                                                ${appGrpPremDto.offSiteEndMM}
                                            </c:when>
                                            <c:otherwise>
                                                0${appGrpPremDto.offSiteEndMM}
                                            </c:otherwise>
                                        </c:choose>
                                    </span></p>
                                </div>
                            </div>
                            <%--<c:forEach items="${appGrpPremDto.appPremPhOpenPeriodList}" var="appPremPhOpenPeriod" varStatus="statu">--%>
                            <c:choose>
                                <c:when test="${!empty appGrpPremDto.appPremPhOpenPeriodList}">
                                    <c:set var="phLength" value="${appGrpPremDto.appPremPhOpenPeriodList.size()-1}"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="phLength" value="0"/>
                                </c:otherwise>
                            </c:choose>
                            <c:forEach begin="0" end="${phLength}" step="1" varStatus="statu">
                                <c:set var="appPremPhOpenPeriod" value="${appGrpPremDto.appPremPhOpenPeriodList[statu.index]}"/>
                                <div class="row">
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span>Select Public Holiday</span></p>
                                    </div>
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span>${appPremPhOpenPeriod.dayName}</span></p>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span>Public Holiday Operating Hours (Start)</span></p>
                                    </div>
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span>
                                            <c:choose>
                                                <c:when test="${empty appPremPhOpenPeriod}">

                                                </c:when>
                                                <c:when test="${appPremPhOpenPeriod.offSiteStartFromHH.length()>1}">
                                                    ${appPremPhOpenPeriod.offSiteStartFromHH}
                                                </c:when>
                                                <c:otherwise>
                                                    0${appPremPhOpenPeriod.offSiteStartFromHH}
                                                </c:otherwise>
                                            </c:choose>
                                            <c:if test="${!empty appPremPhOpenPeriod}">
                                                :
                                            </c:if>
                                            <c:choose>
                                                <c:when test="${empty appPremPhOpenPeriod}">

                                                </c:when>
                                                <c:when test="${appPremPhOpenPeriod.offSiteStartFromMM.length()>1}">
                                                    ${appPremPhOpenPeriod.offSiteStartFromMM}
                                                </c:when>
                                                <c:otherwise>
                                                    0${appPremPhOpenPeriod.offSiteStartFromMM}
                                                </c:otherwise>
                                            </c:choose>
                                        </span></p>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span>Public Holiday Operating Hours (End)</span></p>
                                    </div>
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span>
                                            <c:choose>
                                                <c:when test="${empty appPremPhOpenPeriod}">

                                                </c:when>
                                                <c:when test="${appPremPhOpenPeriod.offSiteEndToHH.length()>1}">
                                                    ${appPremPhOpenPeriod.offSiteEndToHH}
                                                </c:when>
                                                <c:otherwise>
                                                    0${appPremPhOpenPeriod.offSiteEndToHH}
                                                </c:otherwise>
                                            </c:choose>
                                            <c:if test="${!empty appPremPhOpenPeriod}">
                                                :
                                            </c:if>
                                            <c:choose>
                                                <c:when test="${empty appPremPhOpenPeriod}">

                                                </c:when>
                                                <c:when test="${appPremPhOpenPeriod.offSiteEndToMM.length()>1}">
                                                    ${appPremPhOpenPeriod.offSiteEndToMM}
                                                </c:when>
                                                <c:otherwise>
                                                    0${appPremPhOpenPeriod.offSiteEndToMM}
                                                </c:otherwise>
                                            </c:choose>
                                        </span></p>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:if>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>
