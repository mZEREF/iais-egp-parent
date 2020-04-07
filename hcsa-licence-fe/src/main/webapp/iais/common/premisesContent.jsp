<c:forEach var="appGrpPremisesDto" items="${AppSubmissionDto.appGrpPremisesDtoList}" varStatus="status">
    <c:set value="${errorMap_premises[premIndexNo]}" var="errMsg"/>
    <div class="row premContent <c:if test="${!status.first}">underLine</c:if>  " id="mainPrem">
        <c:set var="onSite" value="ONSITE" ></c:set>
        <c:set var="conv" value="CONVEYANCE" ></c:set>
        <!--for ph -->
        <input class="premValue" type="hidden" name="premValue" value="${status.index}"/>
        <input class="premisesIndexNo" type="hidden" name="premisesIndexNo" value="${appGrpPremisesDto.premisesIndexNo}"/>
        <c:choose>
            <c:when test="${appGrpPremisesDto.appPremPhOpenPeriodList != null && appGrpPremisesDto.appPremPhOpenPeriodList.size()>0}">
                <input class="phLength" type="hidden" name="phLength" value="${appGrpPremisesDto.appPremPhOpenPeriodList.size()}"/>
            </c:when>
            <c:otherwise>
                <input class="phLength" type="hidden" name="phLength" value="1"/>
            </c:otherwise>
        </c:choose>
        <c:set var="premValue" value="${status.index}"/>
        <div class="col-xs-12">
            <div class="form-horizontal">
                <div><strong class="app-font-size-22 premHeader">Premises ${status.index+1}</strong>
                    <c:if test="${!status.first && requestInformationConfig==null && 'APTY004' !=AppSubmissionDto.appType && 'APTY005' !=AppSubmissionDto.appType}">
                        <div class="row" style="float:right">
                            <div class="form-check removeBtn">
                                <div class="fa fa-times-circle"></div>
                            </div>
                        </div>
                    </c:if>
                </div>
            </div>
            <div class="form-horizontal">
                <div class="form-group premisesTypeDiv" id="premisesType" <c:if test="${'APTY005' ==AppSubmissionDto.appType || 'APTY004'==AppSubmissionDto.appType}">hidden</c:if> >
                    <label class="col-xs-12 col-md-4 control-label error-msg-type" for="premisesType">What is your premises type ? <span class="mandatory">*</span></label>
                    <input class="premTypeValue" type="hidden" name="premType" value="${appGrpPremisesDto.premisesType}"/>
                    <input class="premSelValue" type="hidden" value="${appGrpPremisesDto.premisesSelect}"/>
                    <c:forEach var="premisesType" items="${premisesType}">
                        <c:choose>
                            <c:when test="${'ONSITE' == premisesType}">
                                <c:set var="className" value="onSite"/>
                            </c:when>
                            <c:when test="${'CONVEYANCE' == premisesType}">
                                <c:set var="className" value="conveyance" />
                            </c:when>
                        </c:choose>
                        <div class="col-xs-5 <c:if test="${'onSite'==className}">col-md-3</c:if> <c:if test="${'conveyance'==className}">col-md-4</c:if> ">
                            <div class="form-check">
                                <c:if test="${appGrpPremisesDto.premisesType!=premisesType}">
                                    <input class="form-check-input premTypeRadio ${className}"  type="radio" name="premType${status.index}" value = "${premisesType}" aria-invalid="false">
                                </c:if>
                                <c:if test="${appGrpPremisesDto.premisesType==premisesType}">
                                    <input class="form-check-input premTypeRadio ${className}"  type="radio" name="premType${status.index}" checked="checked" value = "${premisesType}"  aria-invalid="false">
                                </c:if>

                                <label class="form-check-label" ><span class="check-circle"></span>
                                    <c:if test="${premisesType == onSite}">
                                        <c:out value="On-site" /><br/>
                                        <span>(at a fixed address)</span>
                                    </c:if>
                                    <c:if test="${premisesType == conv}">
                                        <c:out value="Conveyance" /><br/>
                                        <span>(in a mobile clinic / ambulance)</span>
                                    </c:if>
                                </label>
                            </div>

                        </div>
                    </c:forEach>

                </div>
                <div class="row">
                    <div class="col-xs-12 col-md-4"></div>
                    <div class="col-xs-6 col-md-5">
                        <span class="error-msg" name="iaisErrorMsg" id="error_premisesType${status.index}" ></span>
                    </div>
                </div>

                <iais:row cssClass="onSiteSelect hidden">
                    <iais:field value="Add or select a premises from the list : " width="12" mandatory="true"/>
                    <iais:value id="onSiteSelect"  cssClass="col-xs-11 col-sm-7 col-md-5">
                        <c:choose>
                            <c:when test="${appGrpPremisesDto.premisesType == onSite}">
                                <iais:select cssClass="premSelect" id="onSiteSel" name="onSiteSelect"  options="premisesSelect" value="${appGrpPremisesDto.premisesSelect}"></iais:select>
                            </c:when>
                            <c:otherwise>
                                <iais:select cssClass="premSelect" id="onSiteSel" name="onSiteSelect"  options="premisesSelect" value=""></iais:select>
                            </c:otherwise>
                        </c:choose>
                    </iais:value>
                </iais:row>

                <iais:row cssClass="conveyanceSelect hidden">
                    <iais:field value="Add or select a premises from the list : " width="12" mandatory="true"/>
                    <iais:value id="conveyanceSelect"  cssClass="col-xs-11 col-sm-7 col-md-5">
                        <c:choose>
                            <c:when test="${appGrpPremisesDto.premisesType == conv}">
                                <iais:select cssClass="premSelect" id="conveyanceSel" name="conveyanceSelect"  options="conveyancePremSel" value="${appGrpPremisesDto.premisesSelect}"></iais:select>
                            </c:when>
                            <c:otherwise>
                                <iais:select cssClass="premSelect" id="conveyanceSel" name="conveyanceSelect"  options="conveyancePremSel" value=""></iais:select>
                            </c:otherwise>
                        </c:choose>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <div class="col-xs-12 col-md-4 "></div>
                    <div class=" col-xs-11 col-sm-7 col-md-5" style="margin-bottom: 2%">
                        <span class="error-msg" id="error_premisesSelect${status.index}"  name="iaisErrorMsg"></span>
                    </div>
                </iais:row>
            </div>
            <div class="prem-summary hidden ">
                <h3 class="without-header-line">Premises Summary</h3>
                <p class="premise-address-gp"> <span class="premise-type"><strong>On-site: </strong></span><span class="premise-address"></span></p>
                <p class="vehicle-txt hidden"><strong>Vehicle No:</strong> <span class="vehicle-info"></span></p>
            </div>

            <div class="new-premise-form-on-site hidden  ">
                <div class="form-horizontal">
                    <iais:row>
                        <iais:field value="Fire Safety Certificate Issued Date" width="12"/>
                        <iais:value cssClass="col-xs-7 col-sm-4 col-md-5">
                            <iais:datePicker cssClass="fireIssuedDate " name="onSiteFireSafetyCertIssuedDate" value="${appGrpPremisesDto.certIssuedDtStr}" />
                        </iais:value>
                    </iais:row>
                    <div class="form-group">
                        <label class="col-xs-12 col-md-4 control-label" for="siteSafefyNo">Fire Safety Shelter Bureau Ref. No. <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" title="&lt;p&gt;This is a xxx digit No. that you can access from the Life Saving Force Portral.&lt;/p&gt;">i</a></label>
                        <div class="col-xs-9 col-sm-5 col-md-5">
                            <input id="siteSafefyNo" maxlength="66" name="onSiteScdfRefNo" type="text" value="${appGrpPremisesDto.scdfRefNo}">
                        </div>
                    </div>
                    <iais:row>
                        <iais:field value="Name of HCI " mandatory="true" width="11"/>
                        <iais:value width="11" cssClass="col-md-5 disabled">
                            <iais:input cssClass="" maxLength="100" type="text" name="onSiteHciName" id="sitePremiseName" value="${appGrpPremisesDto.hciName}"></iais:input>
                            <span id="error_hciName${status.index}" name="iaisErrorMsg" class="error-msg"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row cssClass="postalCodeDiv">
                        <iais:field value="Postal Code " mandatory="true" width="12"/>
                        <iais:value  cssClass="col-md-5">
                            <iais:input cssClass="sitePostalCode" maxLength="6" type="text"  name="onSitePostalCode"  value="${appGrpPremisesDto.postalCode}"></iais:input>
                            <span  id="error_postalCode${status.index}" class="error-msg" name="iaisErrorMsg"></span>
                        </iais:value>
                        <div class="col-xs-7 col-sm-6 col-md-3">
                            <p><a class="retrieveAddr" >Retrieve your address</a></p>
                        </div>

                    </iais:row>
                    <iais:row>
                        <iais:field value="Address Type " mandatory="true" width="12"/>
                        <iais:value id="onSiteAddressType${premValue}" cssClass="col-xs-7 col-sm-4 col-md-5 addressType">
                            <iais:select cssClass="onSiteAddressType" name="onSiteAddressType" id="onSiteAddressType" options="addressType" value="${appGrpPremisesDto.addrType}" ></iais:select>
                            <span class="error-msg" name="iaisErrorMsg" id="error_addrType${status.index}"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Block / House No." width="12"/>
                        <iais:value cssClass="col-md-5">
                            <iais:input cssClass="siteBlockNo" maxLength="10"  type="text" name="onSiteBlkNo" id="siteBlockNo" value="${appGrpPremisesDto.blkNo}"></iais:input>
                            <span class="error-msg" name="iaisErrorMsg" id="error_blkNo${status.index}"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Floor No." width="12"/>
                        <iais:value cssClass="col-xs-7 col-sm-4 col-md-5 ">
                            <iais:input maxLength="3" type="text" name="onSiteFloorNo" id="siteFloorNo" value="${appGrpPremisesDto.floorNo}"></iais:input>
                            <span class="error-msg" name="iaisErrorMsg" id="error_floorNo${status.index}"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Unit No." width="12"/>
                        <iais:value cssClass="col-xs-7 col-sm-4 col-md-5 ">
                            <iais:input maxLength="5" type="text" name="onSiteUnitNo" id="siteUnitNo" value="${appGrpPremisesDto.unitNo}"></iais:input>
                            <span class="error-msg" name="iaisErrorMsg" id="error_unitNo${status.index}"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Street Name " mandatory="true" width="10"/>
                        <iais:value width="10" cssClass="col-md-5">
                            <iais:input cssClass="siteStreetName" maxLength="32" type="text" name="onSiteStreetName" id="siteStreetName" value="${appGrpPremisesDto.streetName}"></iais:input>
                            <span class="error-msg" name="iaisErrorMsg" id="error_streetName${status.index}"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Building Name" width="12"/>
                        <iais:value width="11" cssClass="col-md-5">
                            <iais:input cssClass="siteBuildingName" maxLength="66" type="text" name="onSiteBuildingName" id="siteBuildingName" value="${appGrpPremisesDto.buildingName}"></iais:input>
                            <span class="error-msg" name="iaisErrorMsg" id="error_buildingName"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Office Telephone No " mandatory="true" width="12"/>
                        <iais:value cssClass="col-xs-7 col-sm-4 col-md-5">
                            <iais:input type="text" name="onSiteOffTelNo" maxLength="8" value="${appGrpPremisesDto.offTelNo}" id="onsitOffice" cssClass="onsitOffice" />
                            <span class="error-msg" name="iaisErrorMsg" id="error_offTelNo${status.index}"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row cssClass="other-lic-content">
                        <iais:field value="Are you co-locating with another licensee?" mandatory="true" width="12"/>
                        <iais:value cssClass="col-xs-7 col-sm-4 col-md-6">
                            <input type="hidden" name="onSiteIsOtherLic" value="${appGrpPremisesDto.locateWithOthers}"/>
                            <div class="form-check col-sm-3">
                                <input <c:if test="${'0'==appGrpPremisesDto.locateWithOthers}">checked="checked"</c:if> class="form-check-input other-lic"  type="radio" name="otherLicence${status.index}" value = "1" aria-invalid="false">
                                <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
                            </div>
                            <div class="form-check col-sm-3">
                                <input <c:if test="${'1'==appGrpPremisesDto.locateWithOthers}">checked="checked"</c:if> class="form-check-input other-lic"  type="radio" name="otherLicence${status.index}" value = "0" aria-invalid="false">
                                <label class="form-check-label" ><span class="check-circle"></span>No</label>
                            </div>
                            <span class="error-msg" name="iaisErrorMsg" id="error_isOtherLic${status.index}"></span>
                        </iais:value>
                    </iais:row>
                    <div class="form-group">
                        <label class="col-xs-12 col-md-4 control-label">
                            Operating Hours (Start) <span class="mandatory">*</span>
                        </label>

                        <div class="col-xs-9 col-sm-5 col-md-6">
                            <div class="col-md-3" style="padding-left: unset">
                                <iais:select name="onSiteStartHH" options="premiseHours" value="${appGrpPremisesDto.onsiteStartHH}" firstOption="--"></iais:select>
                            </div>
                            <div class="col-md-1" style="padding-left: unset;padding-top: 3%">(HH):</div>
                            <div class="col-md-3" style="padding-left: unset">
                                <iais:select name="onSiteStartMM" options="premiseMinute" value="${appGrpPremisesDto.onsiteStartMM}" firstOption="--"></iais:select>
                            </div>
                            <div class="col-md-1" style="padding-left: unset;padding-top: 3%">(MM)</div>
                        </div>
                        <div  class="col-xs-12 col-md-4 "></div>
                        <div  class="col-xs-9 col-sm-5 col-md-6">
                            <span class="error-msg" name="iaisErrorMsg" id="error_onsiteStartMM${status.index}"></span>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-xs-12 col-md-4 control-label">
                            Operating Hours (End) <span class="mandatory">*</span>
                        </label>

                        <div class="col-xs-9 col-sm-5 col-md-6">
                            <div class="col-md-3" style="padding-left: unset">
                                <iais:select name="onSiteEndHH" options="premiseHours" value="${appGrpPremisesDto.onsiteEndHH}" firstOption="--"></iais:select>
                            </div>
                            <div class="col-md-1" style="padding-left: unset;padding-top: 3%">(HH):</div>
                            <div class="col-md-3" style="padding-left: unset">
                                <iais:select name="onSiteEndMM" options="premiseMinute" value="${appGrpPremisesDto.onsiteEndMM}" firstOption="--"></iais:select>
                            </div>
                            <div class="col-md-1" style="padding-left: unset;padding-top: 3%">(MM)</div>
                        </div>
                        <div  class="col-xs-12 col-md-4 "></div>
                        <div class="col-xs-9 col-sm-5 col-md-6">
                            <span class="error-msg" name="iaisErrorMsg" id="error_onsiteEndMM${status.index}"></span>
                        </div>
                    </div>
                    <div class="phFormMarkPoint">
                    </div>
                    <c:choose>
                        <c:when test="${appGrpPremisesDto.appPremPhOpenPeriodList.size()>0 && 'ONSITE'== appGrpPremisesDto.premisesType}">
                            <c:forEach var="ph" items="${appGrpPremisesDto.appPremPhOpenPeriodList}" varStatus="phStat" >
                                <div class="pubHolidayContent">
                                    <iais:row>
                                        <iais:field value="Select Public Holiday" width="12"/>
                                        <iais:value cssClass="col-xs-7 col-sm-4 col-md-5">
                                            <iais:select name="${premValue}onSitePubHoliday${phStat.index}" options="publicHolidaySelect" value="${ph.phDateStr}" firstOption="Please Select"></iais:select>
                                            <span class="error-msg" name="iaisErrorMsg" id="error_onsitephDate${premValue}${phStat.index}"></span>
                                        </iais:value>

                                        <c:if test="${!phStat.first}">
                                            <div class=" col-xs-7 col-sm-4 col-md-3">
                                                <div class="form-check removePhBtn">
                                                    <div class="fa fa-times-circle"></div>
                                                </div>
                                            </div>
                                        </c:if>
                                    </iais:row>
                                    <div class="form-group">
                                        <label class="col-xs-12 col-md-4 control-label">
                                            Public Holidays Operating Hours (Start)
                                        </label>
                                        <div class="col-xs-9 col-sm-5 col-md-6">
                                            <div class="col-md-3" style="padding-left: unset">
                                                <iais:select name="${premValue}onSitePbHolDayStartHH${phStat.index}" options="premiseHours" value="${ph.onsiteStartFromHH}" firstOption="--"></iais:select>
                                            </div>
                                            <div class="col-md-1" style="padding-left: unset;padding-top: 3%">(HH):</div>
                                            <div class="col-md-3" style="padding-left: unset">
                                                <iais:select name="${premValue}onSitePbHolDayStartMM${phStat.index}" options="premiseMinute" value="${ph.onsiteStartFromMM}" firstOption="--"></iais:select>
                                            </div>
                                            <div class="col-md-1" style="padding-left: unset;padding-top: 3%">(MM)</div>
                                        </div>
                                        <div class="col-xs-12 col-md-4 "></div>
                                        <div class="col-xs-9 col-sm-5 col-md-6">
                                            <span class="error-msg" name="iaisErrorMsg" id="error_onsiteStartToMM${premValue}${phStat.index}"></span>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-12 col-md-4 control-label">
                                            Public Holidays Operating Hours (End)
                                        </label>
                                        <div class="col-xs-9 col-sm-5 col-md-6">
                                            <div class="col-md-3" style="padding-left: unset">
                                                <iais:select name="${premValue}onSitePbHolDayEndHH${phStat.index}" options="premiseHours" value="${ph.onsiteEndToHH}" firstOption="--"></iais:select>
                                            </div>
                                            <div class="col-md-1" style="padding-left: unset;padding-top: 3%">(HH):</div>
                                            <div class="col-md-3" style="padding-left: unset">
                                                <iais:select name="${premValue}onSitePbHolDayEndMM${phStat.index}" options="premiseMinute" value="${ph.onsiteEndToMM}" firstOption="--"></iais:select>
                                            </div>
                                            <div class="col-md-1" style="padding-left: unset;padding-top: 3%">(MM)</div>
                                        </div>
                                        <div class="col-xs-12 col-md-4 "></div>
                                        <div class="col-xs-9 col-sm-5 col-md-6">
                                            <span class="error-msg" name="iaisErrorMsg" id="error_onsiteEndToMM${premValue}${phStat.index}"></span>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="pubHolidayContent">
                                <iais:row>
                                    <iais:field value="Select Public Holiday" width="12"/>
                                    <iais:value cssClass="col-xs-7 col-sm-4 col-md-5">
                                        <iais:select name="${premValue}onSitePubHoliday0" options="publicHolidaySelect" value="${ph.phDateStr}" cssClass="form_datetime public-holiday" firstOption="Please Select"></iais:select>
                                        <span  class="error-msg"  name="iaisErrorMsg" id="error_onsitephDate${premValue}${phStat.index}"></span>
                                    </iais:value>
                                </iais:row>
                                <div class="form-group">
                                    <label class="col-xs-12 col-md-4 control-label">
                                        Public Holidays Operating Hours (Start)
                                    </label>
                                    <div class="col-xs-9 col-sm-5 col-md-6">
                                        <div class="col-md-3" style="padding-left: unset">
                                            <iais:select name="${premValue}onSitePbHolDayStartHH0" options="premiseHours" value="" firstOption="--"></iais:select>
                                        </div>
                                        <div class="col-md-1" style="padding-left: unset;padding-top: 3%">(HH):</div>
                                        <div class="col-md-3" style="padding-left: unset">
                                            <iais:select name="${premValue}onSitePbHolDayStartMM0" options="premiseMinute" value="" firstOption="--"></iais:select>
                                        </div>
                                        <div class="col-md-1" style="padding-left: unset;padding-top: 3%">(MM)</div>
                                        <div class="col-xs-12 col-md-4 "></div>
                                        <span  class="error-msg"  name="iaisErrorMsg" id="error_onsiteStartToMM${premValue}${phStat.index}"></span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-12 col-md-4 control-label">
                                        Public Holidays Operating Hours (End)
                                    </label>
                                    <div class="col-xs-9 col-sm-5 col-md-6">
                                        <div class="col-md-3" style="padding-left: unset">
                                            <iais:select name="${premValue}onSitePbHolDayEndHH0" options="premiseHours" value="" firstOption="--"></iais:select>
                                        </div>
                                        <div class="col-md-1" style="padding-left: unset;padding-top: 3%">(HH):</div>
                                        <div class="col-md-3" style="padding-left: unset">
                                            <iais:select name="${premValue}onSitePbHolDayEndMM0" options="premiseMinute" value="" firstOption="--"></iais:select>
                                        </div>
                                        <div class="col-md-1" style="padding-left: unset;padding-top: 3%">(MM)</div>
                                    </div>
                                    <div class="col-xs-12 col-md-4 "></div>
                                    <div class="col-xs-9 col-sm-5 col-md-6">
                                        <span class="error-msg" name="iaisErrorMsg" id="error_onsiteEndToMM${premValue}${phStat.index}"></span>
                                    </div>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                    <div class="form-group">
                        <div class="col-xs-9 col-sm-5 col-md-4">
                            <button class="addPubHolDay btn btn-primary" type="button">Add Public Holiday</button>
                        </div>
                    </div>
                </div>
            </div>

            <div class="new-premise-form-conv hidden">
                <div class="form-horizontal">
                    <iais:row>
                        <iais:field value="Vehicle No. " mandatory="true" width="12"/>
                        <iais:value width="11" cssClass="col-md-5">
                            <iais:input maxLength="10" type="text" name="conveyanceVehicleNo" id="vehicleNo" value="${appGrpPremisesDto.conveyanceVehicleNo}"></iais:input>
                            <span  class="error-msg"  name="iaisErrorMsg" id="error_conveyanceVehicleNo${status.index}"></span>
                        </iais:value>
                    </iais:row>

                        <%--<iais:row>
                          <iais:field value="Vehicle Owner's Name. " mandatory="true" width="12"/>
                          <iais:value width="3">
                            <iais:select  name="conveyanceSalutation" codeCategory="CATE_ID_SALUTATION" value="${appGrpPremisesDto.conveyanceSalutation}" firstOption="Please Select"></iais:select>
                          </iais:value>
                          <iais:value width="5">
                            <iais:input maxLength="10" type="text" name="conveyanceVehicleOwnerName"  value="${appGrpPremisesDto.conveyanceVehicleOwnerName}"></iais:input>
                          </iais:value>
                        </iais:row>--%>

                    <iais:row cssClass="postalCodeDiv">
                        <iais:field value="Postal Code " mandatory="true" width="12"/>
                        <iais:value width="11" cssClass="col-md-5">
                            <iais:input maxLength="6" cssClass="sitePostalCode" type="text" name="conveyancePostalCode"  value="${appGrpPremisesDto.conveyancePostalCode}"></iais:input>
                            <span  class="error-msg" name="iaisErrorMsg" id="error_conveyancePostalCode${status.index}"></span>
                        </iais:value>
                        <div class="col-xs-7 col-sm-6 col-md-3">
                            <p><a class="retrieveAddr" id="conveyance">Retrieve your address</a></p>
                        </div>

                    </iais:row>
                    <iais:row>
                        <iais:field value="Address Type " mandatory="true" width="12"/>
                        <iais:value id="conveyanceAddrType${premValue}" cssClass="col-xs-7 col-sm-4 col-md-5 addressType">
                            <iais:select name="conveyanceAddrType" cssClass="conveyanceAddressType" id="siteAddressType" codeCategory="CATE_ID_ADDRESS_TYPE" firstOption="Please Select" value="${appGrpPremisesDto.conveyanceAddressType}"></iais:select>
                            <span  class="error-msg" name="iaisErrorMsg" id="error_conveyanceAddressType${status.index}"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Block / House No." width="12"/>
                        <iais:value width="11" cssClass="col-md-5">
                            <iais:input maxLength="10" cssClass="conveyanceBlockNo" type="text" name="conveyanceBlockNo" id="conveyanceBlockNo" value="${appGrpPremisesDto.conveyanceBlockNo}"></iais:input>
                            <span  class="error-msg" name="iaisErrorMsg" id="error_conveyanceBlockNos${status.index}"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Floor No." width="12"/>
                        <iais:value cssClass="col-xs-7 col-sm-4 col-md-5 ">
                            <iais:input maxLength="3" type="text" name="conveyanceFloorNo" id="conveyanceFloorNo" value="${appGrpPremisesDto.conveyanceFloorNo}"></iais:input>
                            <span  class="error-msg" name="iaisErrorMsg" id="error_conveyanceFloorNo${status.index}"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Unit No." width="12"/>
                        <iais:value cssClass="col-xs-7 col-sm-4 col-md-5 ">
                            <iais:input maxLength="5" type="text" name="conveyanceUnitNo"  value="${appGrpPremisesDto.conveyanceUnitNo}"></iais:input>
                            <span  class="error-msg" name="iaisErrorMsg" id="error_conveyanceUnitNo${status.index}"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Street Name " mandatory="true" width="10"/>
                        <iais:value width="10" cssClass="col-md-5">
                            <iais:input maxLength="32" cssClass="conveyanceStreetName" type="text" name="conveyanceStreetName"  value="${appGrpPremisesDto.conveyanceStreetName}"></iais:input>
                            <span  class="error-msg" name="iaisErrorMsg" id="error_conveyanceStreetName${status.index}"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Building Name " width="12"/>
                        <iais:value cssClass="col-xs-11 col-sm-7 col-md-5 ">
                            <iais:input maxLength="66" cssClass="conveyanceBuildingName" type="text" name="conveyanceBuildingName" id="conveyanceBuildingName" value="${appGrpPremisesDto.conveyanceBuildingName}"></iais:input>
                            <span  class="error-msg"></span>
                        </iais:value>
                    </iais:row>
                    <div class="form-group">
                        <label class="col-xs-12 col-md-4 control-label">
                            Operating Hours (Start) <span class="mandatory">*</span>
                        </label>
                        <div class="col-xs-9 col-sm-5 col-md-6">
                            <div class="col-md-3" style="padding-left: unset">
                                <iais:select name="conveyanceStartHH" options="premiseHours" value="${appGrpPremisesDto.conStartHH}" firstOption="--"></iais:select>
                            </div>
                            <div class="col-md-1" style="padding-left: unset;padding-top: 3%">(HH):</div>
                            <div class="col-md-3" style="padding-left: unset">
                                <iais:select name="conveyanceStartMM" options="premiseMinute" value="${appGrpPremisesDto.conStartMM}" firstOption="--"></iais:select>
                            </div>
                            <div class="col-md-1" style="padding-left: unset;padding-top: 3%">(MM)</div>
                        </div>
                        <div  class="col-xs-12 col-md-4 "></div>
                        <div class="col-xs-9 col-sm-5 col-md-6">
                            <span class="error-msg" name="isaiErrorMsg" id="error_conStartMM${status.index}"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-12 col-md-4 control-label">
                            Operating Hours (End) <span class="mandatory">*</span>
                        </label>
                        <div class="col-xs-9 col-sm-5 col-md-6">
                            <div class="col-md-3" style="padding-left: unset">
                                <iais:select name="conveyanceEndHH" options="premiseHours" value="${appGrpPremisesDto.conEndHH}" firstOption="--"></iais:select>
                            </div>
                            <div class="col-md-1" style="padding-left: unset;padding-top: 3%">(HH):</div>
                            <div class="col-md-3" style="padding-left: unset">
                                <iais:select name="conveyanceEndMM" options="premiseMinute" value="${appGrpPremisesDto.conEndMM}" firstOption="--"></iais:select>
                            </div>
                            <div class="col-md-1" style="padding-left: unset;padding-top: 3%">(MM)</div>

                        </div>
                        <div  class="col-xs-12 col-md-4 "></div>
                        <div class="col-xs-9 col-sm-5 col-md-6">
                            <span class="error-msg" name="iaisErrorMsg" id="error_conEndMM${status.index}"></span>
                        </div>
                    </div>
                    <div class="phFormMarkPoint">
                    </div>
                    <c:choose>
                        <c:when test="${appGrpPremisesDto.appPremPhOpenPeriodList.size()>0 && 'CONVEYANCE'== appGrpPremisesDto.premisesType}">
                            <c:forEach var="ph" items="${appGrpPremisesDto.appPremPhOpenPeriodList}" varStatus="phStat" >
                                <div class="pubHolidayContent">
                                    <iais:row>
                                        <iais:field value="Select Public Holiday" width="12"/>
                                        <iais:value cssClass="col-xs-7 col-sm-4 col-md-5">
                                            <iais:select name="${premValue}conveyancePubHoliday${phStat.index}" options="publicHolidaySelect" value="${ph.phDateStr}" cssClass="form_datetime public-holiday" firstOption="Please Select"></iais:select>
                                            <span  class="error-msg"  name="iaisErrorMsg" id="error_convphDate${premValue}${phStat.index}"></span>
                                        </iais:value>
                                        <c:if test="${!phStat.first}">
                                            <div class=" col-xs-7 col-sm-4 col-md-3">
                                                <div class="form-check removePhBtn">
                                                    <div class="fa fa-times-circle"></div>
                                                </div>
                                            </div>
                                        </c:if>
                                    </iais:row>
                                    <div class="form-group">
                                        <label class="col-xs-12 col-md-4 control-label">
                                            Public Holidays Operating Hours (Start)
                                        </label>
                                        <div class="col-xs-9 col-sm-5 col-md-6">
                                            <div class="col-md-3" style="padding-left: unset">
                                                <iais:select name="${premValue}conveyancePbHolDayStartHH${phStat.index}" options="premiseHours" value="${ph.convStartFromHH}" firstOption="--"></iais:select>
                                            </div>
                                            <div class="col-md-1" style="padding-left: unset;padding-top: 3%">(HH):</div>
                                            <div class="col-md-3" style="padding-left: unset">
                                                <iais:select name="${premValue}conveyancePbHolDayStartMM${phStat.index}" options="premiseMinute" value="${ph.convStartFromMM}" firstOption="--"></iais:select>
                                            </div>
                                            <div class="col-md-1" style="padding-left: unset;padding-top: 3%">(MM)</div>
                                        </div>
                                        <div  class="col-xs-12 col-md-4 "></div>
                                        <div class="col-xs-9 col-sm-5 col-md-6">
                                            <span class="error-msg" name="iaisErrorMsg" id="error_convStartToHH${premValue}${phStat.index}"></span>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-12 col-md-4 control-label">
                                            Public Holidays Operating Hours (End)
                                        </label>
                                        <div class="col-xs-9 col-sm-5 col-md-6">
                                            <div class="col-md-3" style="padding-left: unset">
                                                <iais:select name="${premValue}conveyancePbHolDayEndHH${phStat.index}" options="premiseHours" value="${ph.convEndToHH}" firstOption="--"></iais:select>
                                            </div>
                                            <div class="col-md-1" style="padding-left: unset;padding-top: 3%">(HH):</div>
                                            <div class="col-md-3" style="padding-left: unset">
                                                <iais:select name="${premValue}conveyancePbHolDayEndMM${phStat.index}" options="premiseMinute" value="${ph.convEndToMM}" firstOption="--"></iais:select>
                                            </div>
                                            <div class="col-md-1" style="padding-left: unset;padding-top: 3%">(MM)</div>
                                        </div>
                                        <div  class="col-xs-12 col-md-4 "></div>
                                        <div class="col-xs-9 col-sm-5 col-md-6">
                                            <span class="error-msg" name="iaisErrorMsg" id="error_convEndToHH${premValue}${phStat.index}"></span>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="pubHolidayContent">
                                <iais:row>
                                    <iais:field value="Select Public Holiday" width="12"/>
                                    <iais:value cssClass="col-xs-7 col-sm-4 col-md-5">
                                        <iais:select name="${premValue}conveyancePubHoliday0" options="publicHolidaySelect" value="${ph.phDateStr}" firstOption="Please Select"></iais:select>
                                        <span  class="error-msg"  name="iaisErrorMsg" id="error_convphDate${premValue}${phStat.index}"></span>
                                    </iais:value>
                                </iais:row>
                                <div class="form-group">
                                    <label class="col-xs-12 col-md-4 control-label">
                                        Public Holidays Operating Hours (Start)
                                    </label>
                                    <div class="col-xs-9 col-sm-5 col-md-6">
                                        <div class="col-md-3" style="padding-left: unset">
                                            <iais:select name="${premValue}conveyancePbHolDayStartHH0" options="premiseHours" value="" firstOption="--"></iais:select>
                                        </div>
                                        <div class="col-md-1" style="padding-left: unset;padding-top: 3%">(HH):</div>
                                        <div class="col-md-3" style="padding-left: unset">
                                            <iais:select name="${premValue}conveyancePbHolDayStartMM0" options="premiseMinute" value="" firstOption="--"></iais:select>
                                        </div>
                                        <div class="col-md-1" style="padding-left: unset;padding-top: 3%">(MM)</div>
                                    </div>
                                    <div  class="col-xs-12 col-md-4 "></div>
                                    <div class="col-xs-9 col-sm-5 col-md-6">
                                        <span class="error-msg" name="iaisErrorMsg" id="error_convStartToHH${premValue}${phStat.index}"></span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-12 col-md-4 control-label">
                                        Public Holidays Operating Hours (End)
                                    </label>
                                    <div class="col-xs-9 col-sm-5 col-md-6">
                                        <div class="col-md-3" style="padding-left: unset">
                                            <iais:select name="${premValue}conveyancePbHolDayEndHH0" options="premiseHours" value="" firstOption="--"></iais:select>
                                        </div>
                                        <div class="col-md-1" style="padding-left: unset;padding-top: 3%">(HH):</div>
                                        <div class="col-md-3" style="padding-left: unset">
                                            <iais:select name="${premValue}conveyancePbHolDayEndMM0" options="premiseMinute" value="" firstOption="--"></iais:select>
                                        </div>
                                        <div class="col-md-1" style="padding-left: unset;padding-top: 3%">(MM)</div>
                                    </div>
                                    <div  class="col-xs-12 col-md-4 "></div>
                                    <div class="col-xs-9 col-sm-5 col-md-6">
                                        <span class="error-msg" name="iaisErrorMsg" id="error_convEndToHH${premValue}${phStat.index}"></span>
                                    </div>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                    <div class="form-group">
                        <div class="col-xs-9 col-sm-5 col-md-4">
                            <button class="addPubHolDay btn btn-primary" type="button">Add Public Holiday</button>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>
</c:forEach>


