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
                <div class="form-group premisesTypeDiv" id="premisesType" <c:if test="${'APTY005' ==AppSubmissionDto.appType || 'APTY004'==AppSubmissionDto.appType}">hidden</c:if> >
                    <label class="col-xs-12 col-md-4 control-label error-msg-type" for="premisesType">What is your premises type ? <span class="mandatory">*</span></label><br>
                    <span class="error-msg"></span>
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

                    <c:if test="${!status.first && requestInformationConfig==null && 'APTY004' !=AppSubmissionDto.appType && 'APTY005' !=AppSubmissionDto.appType}">
                        <div class="col-xs-5 col-md-1">
                            <div class="form-check">
                                <strong class="removeBtn">X</strong>
                            </div>
                        </div>
                    </c:if>
                </div>
                <div class="row">
                    <div class="col-xs-12 col-md-4"></div>
                    <div class="col-xs-6 col-md-5">
                        <span class="error-msg" name="iaisErrorMsg" id="error_premisesType${status.index}" style="margin-left: 35%"></span>
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
                        <iais:field value="Name of HCI " mandatory="true" width="11"/>
                        <iais:value width="11" cssClass="col-md-5 disabled">
                            <iais:input cssClass="disabled" maxLength="100" type="text" name="onSiteHciName" id="sitePremiseName" value="${appGrpPremisesDto.hciName}"></iais:input>
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
                            <iais:select cssClass="siteAddressType" name="onSiteAddressType" id="siteAddressType" options="addressType" value="${appGrpPremisesDto.addrType}"></iais:select>
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
                        <iais:field value="Building Name" width="12"/>
                        <iais:value width="11" cssClass="col-md-5">
                            <iais:input cssClass="siteBuildingName" maxLength="45" type="text" name="onSiteBuildingName" id="siteBuildingName" value="${appGrpPremisesDto.buildingName}"></iais:input>
                            <span class="error-msg" name="iaisErrorMsg" id="error_buildingName"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Street Name " mandatory="true" width="10"/>
                        <iais:value width="10" cssClass="col-md-5">
                            <iais:input cssClass="siteStreetName" maxLength="32" type="text" name="onSiteStreetName" id="siteStreetName" value="${appGrpPremisesDto.streetName}"></iais:input>
                            <span class="error-msg" name="iaisErrorMsg" id="error_streetName${status.index}"></span>
                        </iais:value>
                    </iais:row>
                    <div class="form-group">
                        <label class="col-xs-12 col-md-4 control-label" for="siteSafefyNo">Fire Safety Shelter Bureau Ref. No. <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" title="&lt;p&gt;This is a xxx digit No. that you can access from the Life Saving Force Portral.&lt;/p&gt;">i</a></label>
                        <div class="col-xs-9 col-sm-5 col-md-5">
                            <input id="siteSafefyNo" maxlength="66" name="onSiteScdfRefNo" type="text" value="${appGrpPremisesDto.scdfRefNo}">
                        </div>
                    </div>
                    <iais:row>
                        <iais:field value="Fire Safety Certificate Issued Date" width="12"/>
                        <iais:value cssClass="col-xs-7 col-sm-4 col-md-5">
                            <iais:datePicker cssClass="fireIssuedDate" name="onSiteFireSafetyCertIssuedDate" value="${appGrpPremisesDto.certIssuedDtStr}" />
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
                                <input <c:if test="${'0'==appGrpPremisesDto.locateWithOthers}">checked="checked"</c:if> class="form-check-input other-lic"  type="radio" name="otherLicence${status.index}" value = "0" aria-invalid="false">
                                <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
                            </div>
                            <div class="form-check col-sm-3">
                                <input <c:if test="${'1'==appGrpPremisesDto.locateWithOthers}">checked="checked"</c:if> class="form-check-input other-lic"  type="radio" name="otherLicence${status.index}" value = "1" aria-invalid="false">
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
                            <input type="text" value="${appGrpPremisesDto.onsiteStartHH}" maxlength="2" style="width: 60px;margin-right: 2%" name="onSiteStartHH"/>(HH)
                            :
                            <input type="text" value="${appGrpPremisesDto.onsiteStartMM}" maxlength="2" style="width: 60px;margin-right: 2%;margin-left: 2%"  name="onSiteStartMM"/>(MM)
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
                            <input type="text" value="${appGrpPremisesDto.onsiteEndHH}" maxlength="2" style="width: 60px;margin-right: 2%" name="onSiteEndHH"/>(HH)
                            :
                            <input type="text" value="${appGrpPremisesDto.onsiteEndMM}" maxlength="2" style="width: 60px;margin-right: 2%;margin-left: 2%" name="onSiteEndMM"/>(MM)

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
                                            <iais:datePicker cssClass="form_datetime public-holiday" value="${ph.phDateStr}"  name="${premValue}onSitePubHoliday${phStat.index}"/>
                                        </iais:value>
                                        <c:if test="${!phStat.first}">
                                            <div class=" col-xs-7 col-sm-4 col-md-3">
                                                <div class="form-check">
                                                    <strong class="removePhBtn">X</strong>
                                                </div>
                                            </div>
                                        </c:if>
                                    </iais:row>
                                    <div class="form-group">
                                        <label class="col-xs-12 col-md-4 control-label">
                                            Public Holidays Operating Hours (Start)
                                        </label>
                                        <div class="col-xs-9 col-sm-5 col-md-6">
                                            <input class="PbHolDayStartHH" type="text" name="${premValue}onSitePbHolDayStartHH${phStat.index}" value="${ph.onsiteStartFromHH}" maxlength="2" style="width: 60px;margin-right: 2%"/>(HH)
                                            :
                                            <input class="PbHolDayStartMM" type="text" name="${premValue}onSitePbHolDayStartMM${phStat.index}" value="${ph.onsiteStartFromMM}" maxlength="2" style="width: 60px;;margin-right: 2%;margin-left: 2%"/>(MM)
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-12 col-md-4 control-label">
                                            Public Holidays Operating Hours (End)
                                        </label>
                                        <div class="col-xs-9 col-sm-5 col-md-6">
                                            <input class="PbHolDayEndHH" type="text" name="${premValue}onSitePbHolDayEndHH${phStat.index}" value="${ph.onsiteEndToHH}" maxlength="2" style="width: 60px;margin-right: 2%"/>(HH)
                                            :
                                            <input class="PbHolDayEndMM" type="text" name="${premValue}onSitePbHolDayEndMM${phStat.index}" value="${ph.onsiteEndToMM}" maxlength="2" style="width: 60px;margin-right: 2%;margin-left: 2%"/>(MM)
                                        </div>
                                        <div class="col-xs-12 col-md-4"></div>
                                        <div class="col-xs-9 col-sm-5 col-md-6">
                                            <span class="error-msg" name="iaisErrorMsg" id="error_onsiteEndToMM${status.index}"></span>
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
                                        <iais:datePicker cssClass="form_datetime public-holiday"  name="${premValue}onSitePubHoliday0"/>
                                    </iais:value>
                                </iais:row>
                                <div class="form-group">
                                    <label class="col-xs-12 col-md-4 control-label">
                                        Public Holidays Operating Hours (Start)
                                    </label>
                                    <div class="col-xs-9 col-sm-5 col-md-4">
                                        <input class="PbHolDayStartHH" type="text" name="${premValue}onSitePbHolDayStartHH0" value="" maxlength="2" style="width: 60px;margin-right: 2%"/>(HH)
                                        :
                                        <input class="PbHolDayStartMM" type="text" name="${premValue}onSitePbHolDayStartMM0" value="" maxlength="2" style="width: 60px;margin-right: 2%;margin-left: 2%"/>(MM)
                                        <span  class="error-msg"  name="iaisErrorMsg" id="error_"></span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-12 col-md-4 control-label">
                                        Public Holidays Operating Hours (End)
                                    </label>
                                    <div class="col-xs-9 col-sm-5 col-md-4">
                                        <input class="PbHolDayEndHH" type="text" name="${premValue}onSitePbHolDayEndHH0" value="" maxlength="2" style="width: 60px;margin-right: 2%"/>(HH)
                                        :
                                        <input class="PbHolDayEndMM" type="text" name="${premValue}onSitePbHolDayEndMM0" value="" maxlength="2" style="width: 60px;margin-right: 2%;margin-left: 2%"/>(MM)
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
            <div class="new-premise-form-conv hidden ">
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
                            <span  class="postalCodeMsg error-msg"></span>
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
                        <iais:field value="Building Name " width="12"/>
                        <iais:value cssClass="col-xs-11 col-sm-7 col-md-5 ">
                            <iais:input maxLength="45" cssClass="conveyanceBuildingName" type="text" name="conveyanceBuildingName" id="conveyanceBuildingName" value="${appGrpPremisesDto.conveyanceBuildingName}"></iais:input>
                            <span  class="error-msg"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Street Name " mandatory="true" width="10"/>
                        <iais:value width="10" cssClass="col-md-5">
                            <iais:input maxLength="32" cssClass="conveyanceStreetName" type="text" name="conveyanceStreetName"  value="${appGrpPremisesDto.conveyanceStreetName}"></iais:input>
                            <span  class="error-msg" name="iaisErrorMsg" id="error_conveyanceStreetName${status.index}"></span>
                        </iais:value>
                    </iais:row>
                    <div class="form-group">
                        <label class="col-xs-12 col-md-4 control-label">
                            Operating Hours (Start) <span class="mandatory">*</span>
                        </label>
                        <div class="col-xs-9 col-sm-5 col-md-6">
                            <input type="text" value="${appGrpPremisesDto.conStartHH}" maxlength="2" style="width: 60px;margin-right: 2%" name="conveyanceStartHH"/>(HH)
                            :
                            <input type="text" value="${appGrpPremisesDto.conStartMM}" maxlength="2" style="width: 60px;margin-right: 2%;margin-left: 2%" name="conveyanceStartMM"/>(MM)

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
                            <input type="text" value="${appGrpPremisesDto.conEndHH}" maxlength="2" style="width: 60px;margin-right: 2%" name="conveyanceEndHH"/>(HH)
                            :
                            <input type="text" value="${appGrpPremisesDto.conEndMM}" maxlength="2" style="width: 60px;margin-right: 2%;margin-left: 2%" name="conveyanceEndMM"/>(MM)

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
                                            <iais:datePicker cssClass="form_datetime public-holiday" value="${ph.phDateStr}"  name="${premValue}conveyancePubHoliday${phStat.index}"/>
                                        </iais:value>
                                        <c:if test="${!phStat.first}">
                                            <div class=" col-xs-7 col-sm-4 col-md-3">
                                                <div class="form-check">
                                                    <strong class="removePhBtn">X</strong>
                                                </div>
                                            </div>
                                        </c:if>
                                    </iais:row>
                                    <div class="form-group">
                                        <label class="col-xs-12 col-md-4 control-label">
                                            Public Holidays Operating Hours (Start)
                                        </label>
                                        <div class="col-xs-9 col-sm-5 col-md-4">
                                            <input class="PbHolDayStartHH" type="text" name="${premValue}conveyancePbHolDayStartHH${phStat.index}" value="${ph.convStartFromHH}" maxlength="2" style="width: 60px;margin-right: 2%"/>(HH)
                                            :
                                            <input class="PbHolDayStartMM" type="text" name="${premValue}conveyancePbHolDayStartMM${phStat.index}" value="${ph.convStartFromMM}" maxlength="2" style="width: 60px;margin-left: 2%;margin-right: 2%"/>(MM)
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-12 col-md-4 control-label">
                                            Public Holidays Operating Hours (End)
                                        </label>
                                        <div class="col-xs-9 col-sm-5 col-md-6">
                                            <input class="PbHolDayEndHH" type="text" name="${premValue}conveyancePbHolDayEndHH${phStat.index}" value="${ph.convEndToHH}" maxlength="2" style="width: 60px;margin-right: 2%"/>(HH)
                                            :
                                            <input class="PbHolDayEndMM" type="text" name="${premValue}conveyancePbHolDayEndMM${phStat.index}" value="${ph.convEndToMM}" maxlength="2" style="width: 60px;margin-right: 2%;margin-left: 2%"/>(MM)

                                        </div>

                                        <div  class="col-xs-12 col-md-4 "></div>
                                        <div class="col-xs-9 col-sm-5 col-md-6">
                                            <span class="error-msg" name="iaisErrorMsg" id="error_convEndToHH${status.index}"></span>
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
                                        <iais:datePicker cssClass="form_datetime public-holiday"  name="${premValue}conveyancePubHoliday0"/>
                                    </iais:value>
                                </iais:row>
                                <div class="form-group">
                                    <label class="col-xs-12 col-md-4 control-label">
                                        Public Holidays Operating Hours (Start)
                                    </label>
                                    <div class="col-xs-9 col-sm-5 col-md-4">
                                        <input class="PbHolDayStartHH" type="text" name="${premValue}conveyancePbHolDayStartHH0" value="" maxlength="2" style="width: 60px;margin-right: 2%"/>(HH)
                                        :
                                        <input class="PbHolDayStartMM" type="text" name="${premValue}conveyancePbHolDayStartMM0" value="" maxlength="2" style="width: 60px;margin-right: 2%;margin-left: 2%"/>(MM)
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-12 col-md-4 control-label">
                                        Public Holidays Operating Hours (End)
                                    </label>
                                    <div class="col-xs-9 col-sm-5 col-md-4">
                                        <input class="PbHolDayEndHH" type="text" name="${premValue}conveyancePbHolDayEndHH0" value="" maxlength="2" style="width: 60px;margin-right: 2%"/>(HH)
                                        :
                                        <input class="PbHolDayEndMM" type="text" name="${premValue}conveyancePbHolDayEndMM0" value="" maxlength="2" style="width: 60px;margin-left: 2%;margin-right: 2%"/>(MM)
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


