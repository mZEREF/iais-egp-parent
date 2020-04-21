<div class="panel panel-default">
    <div class="panel-heading
        <c:if test="${!FirstView}">
            <c:if test="${Msg.premiss==null}">completed </c:if> <c:if test="${Msg.premiss!=null}">incompleted </c:if>
        </c:if>" id="headingPremise" role="tab">
        <h4 class="panel-title"><a role="button" class="collapse collapsed" data-toggle="collapse" href="#collapsePremise${documentIndex}" aria-expanded="true" aria-controls="collapsePremise">Premises</a></h4>
    </div>
    <div class="panel-collapse collapse" id="collapsePremise${documentIndex}" role="tabpanel" aria-labelledby="headingPremise">
        <div class="panel-body">
            <c:if test="${AppSubmissionDto.appEditSelectDto==null||AppSubmissionDto.appEditSelectDto.premisesEdit}">
                <p class="text-right"><a href="#" id="premisesEdit"><em class="fa fa-pencil-square-o"></em>Edit</a></p>
            </c:if>
            <c:forEach var="appGrpPremDto" items="${AppSubmissionDto.appGrpPremisesDtoList}"
                       varStatus="status">
                <div class="panel-main-content amend-preview-info">
                    <div class="row">
                        <p><strong>Premises ${status.index+1}</strong></p>
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
                                    <c:if test="${appGrpPremDto.premisesType=='OFFSIET'}">Off-site</c:if>
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
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Fire Safety Shelter Bureau Ref. No.</span></p>
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
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Floor No.</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appGrpPremDto.floorNo}</span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Unit No.</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appGrpPremDto.unitNo}</span></p>
                                </div>
                            </div>
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
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Are you co-locating with anothe licensee?</span></p>
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
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Operating Hours (Start)</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>
                                        <c:choose>
                                            <c:when test="${appGrpPremDto.onsiteStartHH.length()>1}">
                                                ${appGrpPremDto.onsiteStartHH}
                                            </c:when>
                                            <c:otherwise>
                                                0${appGrpPremDto.onsiteStartHH}
                                            </c:otherwise>
                                        </c:choose>
                                        :
                                        <c:choose>
                                            <c:when test="${appGrpPremDto.onsiteStartMM.length()>1}">
                                                ${appGrpPremDto.onsiteStartMM}
                                            </c:when>
                                            <c:otherwise>
                                                0${appGrpPremDto.onsiteStartMM}
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
                                            <c:when test="${appGrpPremDto.onsiteEndHH.length()>1}">
                                                ${appGrpPremDto.onsiteEndHH}
                                            </c:when>
                                            <c:otherwise>
                                                0${appGrpPremDto.onsiteEndHH}
                                            </c:otherwise>
                                        </c:choose>
                                        :
                                        <c:choose>
                                            <c:when test="${appGrpPremDto.onsiteEndMM.length()>1}">
                                                ${appGrpPremDto.onsiteEndMM}
                                            </c:when>
                                            <c:otherwise>
                                                0${appGrpPremDto.onsiteEndMM}
                                            </c:otherwise>
                                        </c:choose>
                                    </span></p>
                                </div>
                            </div>
                            <c:forEach items="${appGrpPremDto.appPremPhOpenPeriodList}" var="appPremPhOpenPeriod" varStatus="statu">
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
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span>Public Holidays Operating Hours (Start)</span></p>
                                    </div>
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span>
                                             <c:choose>
                                                 <c:when test="${appPremPhOpenPeriod.onsiteStartFromHH.length()>1}">
                                                     ${appPremPhOpenPeriod.onsiteStartFromHH}
                                                 </c:when>
                                                 <c:otherwise>
                                                     0${appPremPhOpenPeriod.onsiteStartFromHH}
                                                 </c:otherwise>
                                             </c:choose>
                                            :
                                            <c:choose>
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
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span>Public Holidays Operating Hours (End)</span></p>
                                    </div>
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span>
                                            <c:choose>
                                                <c:when test="${appPremPhOpenPeriod.onsiteEndToHH.length()>1}">
                                                    ${appPremPhOpenPeriod.onsiteEndToHH}
                                                </c:when>
                                                <c:otherwise>
                                                    0${appPremPhOpenPeriod.onsiteEndToHH}
                                                </c:otherwise>
                                            </c:choose>
                                            :
                                            <c:choose>
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
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Floor No.</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appGrpPremDto.conveyanceFloorNo}</span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Unit No.</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appGrpPremDto.conveyanceUnitNo}</span></p>
                                </div>
                            </div>
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
                            <c:forEach items="${appGrpPremDto.appPremPhOpenPeriodList}" var="appPremPhOpenPeriod" varStatus="statu">
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
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span>Public Holidays Operating Hours (Start)</span></p>
                                    </div>
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span>
                                            <c:choose>
                                                <c:when test="${appPremPhOpenPeriod.convStartFromHH.length()>1}">
                                                    ${appPremPhOpenPeriod.convStartFromHH}
                                                </c:when>
                                                <c:otherwise>
                                                    0${appPremPhOpenPeriod.convStartFromHH}
                                                </c:otherwise>
                                            </c:choose>
                                            :
                                            <c:choose>
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
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span>Public Holidays Operating Hours (End)</span></p>
                                    </div>
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span>
                                            <c:choose>
                                                <c:when test="${appPremPhOpenPeriod.convEndToHH.length()>1}">
                                                    ${appPremPhOpenPeriod.convEndToHH}
                                                </c:when>
                                                <c:otherwise>
                                                    0${appPremPhOpenPeriod.convEndToHH}
                                                </c:otherwise>
                                            </c:choose>
                                            :
                                            <c:choose>
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

                        <c:if test="${'OFFSIET'==appGrpPremDto.premisesType}">
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
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Floor No.</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appGrpPremDto.offSiteFloorNo}</span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>Unit No.</span></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span>${appGrpPremDto.offSiteUnitNo}</span></p>
                                </div>
                            </div>
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
                            <c:forEach items="${appGrpPremDto.appPremPhOpenPeriodList}" var="appPremPhOpenPeriod" varStatus="statu">
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
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span>Public Holidays Operating Hours (Start)</span></p>
                                    </div>
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span>
                                            <c:choose>
                                                <c:when test="${appPremPhOpenPeriod.offSiteStartFromHH.length()>1}">
                                                    ${appPremPhOpenPeriod.offSiteStartFromHH}
                                                </c:when>
                                                <c:otherwise>
                                                    0${appPremPhOpenPeriod.offSiteStartFromHH}
                                                </c:otherwise>
                                            </c:choose>
                                            :
                                            <c:choose>
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
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span>Public Holidays Operating Hours (End)</span></p>
                                    </div>
                                    <div class="col-md-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span>
                                            <c:choose>
                                                <c:when test="${appPremPhOpenPeriod.offSiteEndToHH.length()>1}">
                                                    ${appPremPhOpenPeriod.offSiteEndToHH}
                                                </c:when>
                                                <c:otherwise>
                                                    0${appPremPhOpenPeriod.offSiteEndToHH}
                                                </c:otherwise>
                                            </c:choose>
                                            :
                                            <c:choose>
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
            <c:if test="${FirstView}">
                <br/>
                <p class="font-size-14">Please note that you will not be able to add  or remove any premises here.</p>
                <p class="font-size-14">If you wish to do so, please click <a href="#">here</a>.</p>
            </c:if>
        </div>
    </div>
</div>
