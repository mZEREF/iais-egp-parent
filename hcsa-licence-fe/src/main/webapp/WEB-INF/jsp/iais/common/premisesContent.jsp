<style>
    input.disabled-placeHolder::-webkit-input-placeholder { /* WebKit, Blink, Edge */
        color:#999999 !important;
    }
    .disabled-placeHolder:-moz-placeholder { /* Mozilla Firefox 4 to 18 */
        color:#999999!important;
    }
    .disabled-placeHolder::-moz-placeholder { /* Mozilla Firefox 19+ */
        color:#999999!important;
    }
    input.disabled-placeHolder:-ms-input-placeholder { /* Internet Explorer 10-11 */
        color:#999999!important;
    }
    input.disabled-placeHolder::-ms-input-placeholder { /* Microsoft Edge */
        color:#999999!important;
    }
    .radio-disabled::before{
        background-color: #999999 !important;
        /*border: 1px solid #999999 !important;*/
    }
    .radio-disabled{
        border-color: #999999 !important;
    }
    input.allDay {
        -ms-transform: scale(2,2); /* IE */
        -moz-transform: scale(2,2); /* FireFox */
        -webkit-transform: scale(2,2); /* Safari and Chrome */
        -o-transform: scale(2,2); /* Opera */
    }

    .input-padding {
        padding-right:5px;
        padding-left: 15px;
    }

    .label-padding {
        padding-left: 0px;
        padding-top: 14px;
        margin-left: -3px;
    }

    .multi-sel-padding {
        padding-bottom: 15px;
    }

    .all-day-position {
        margin: 14px 20px 20px 5px;
        padding: 0;
    }

</style>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:forEach var="appGrpPremisesDto" items="${AppSubmissionDto.appGrpPremisesDtoList}" varStatus="status">
    <c:set value="${errorMap_premises[premIndexNo]}" var="errMsg"/>
    <c:set var="canEdit" value="true"/>
    <div class="row premContent <c:if test="${!status.first}">underLine</c:if>  " id="mainPrem">
        <c:set var="onSite" value="ONSITE" ></c:set>
        <c:set var="conv" value="CONVEYANCE" ></c:set>
        <c:set var="offSite" value="OFFSITE" ></c:set>
        <c:set var="easMts" value="EASMTS" ></c:set>
        <input type="hidden" name="chooseExistData" value="0"/>
        <input type="hidden" name="isPartEdit" value="0"/>
        <input type="hidden" name="rfiCanEdit" value="${appGrpPremisesDto.rfiCanEdit}"/>
        <!--for ph -->
        <input class="premValue" type="hidden" name="premValue" value="${status.index}"/>
        <input class="premisesIndexNo" type="hidden" name="premisesIndexNo" value="${appGrpPremisesDto.premisesIndexNo}"/>

        <c:choose>
            <c:when test="${appGrpPremisesDto.phDtoList != null && appGrpPremisesDto.phDtoList.size()>1}">
                <input class="phLength" type="hidden" name="phLength" value="${appGrpPremisesDto.phDtoList.size()}"/>
            </c:when>
            <c:otherwise>
                <input class="phLength" type="hidden" name="phLength" value="1"/>
            </c:otherwise>
        </c:choose>
        <c:set var="premValue" value="${status.index}"/>
        <input hidden class="premiseIndex" value="${premValue}">
        <c:choose>
            <c:when test="${appGrpPremisesDto.appPremisesOperationalUnitDtos.size() > 0}">
                <input class="opLength" type="hidden" name="opLength" value="${appGrpPremisesDto.appPremisesOperationalUnitDtos.size()}"/>
            </c:when>
            <c:otherwise>
                <input class="opLength" type="hidden" name="opLength" value="0"/>
            </c:otherwise>
        </c:choose>

        <c:choose>
            <c:when test="${appGrpPremisesDto.clickRetrieve}">
                <input class="retrieveflag" type="hidden" name="retrieveflag" value="1"/>
            </c:when>
            <c:otherwise>
                <input class="retrieveflag" type="hidden" name="retrieveflag" value="0"/>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${appGrpPremisesDto.weeklyDtoList != null && appGrpPremisesDto.weeklyDtoList.size()>1}">
                <input class="weeklyLength" type="hidden" name="weeklyLength" value="${appGrpPremisesDto.weeklyDtoList.size()}"/>
            </c:when>
            <c:otherwise>
                <input class="weeklyLength" type="hidden" name="weeklyLength" value="1"/>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${appGrpPremisesDto.eventDtoList != null && appGrpPremisesDto.eventDtoList.size()>1}">
                <input class="eventLength" type="hidden" name="eventLength" value="${appGrpPremisesDto.eventDtoList.size()}"/>
            </c:when>
            <c:otherwise>
                <input class="eventLength" type="hidden" name="eventLength" value="1"/>
            </c:otherwise>
        </c:choose>
        <div class="col-xs-12">
            <c:if test="${AppSubmissionDto.appType=='APTY005'||AppSubmissionDto.appType=='APTY004'}">
                <c:if test="${fn:length(appGrpPremisesDto.licenceDtos)>0}">
                    <div class="form-check col-sm-12" >
                        <table aria-describedby="" class="impactedLic">
                            <tr>
                                <th scope="col" style="font-size: 18px;font-weight: 700" class="form-check col-sm-3">Licence  </th>
                                <th scope="col" style="font-size: 18px;font-weight: 700" class="form-check col-sm-3">Licence No.</th>
                                <span id="error_selectLicence" class="error-msg"></span>
                            </tr>
                            <c:forEach items="${appGrpPremisesDto.licenceDtos}" var="licence">
                                <tr>
                                    <td >
                                        <div class="col-xs-12 col-md-12 form-check" style="padding:0px 15px;margin-top:15px">
                                            <img src="/hcsa-licence-web/img/20210124131101.png" alt="Confirm the icon for dec 20210124131101.png">
                                            <label style="font-weight: normal"><span class="check-square"></span>${licence.svcName}</label>
                                        </div></td>
                                    <td class="form-check col-sm-3">${licence.licenceNo}</td>
                                </tr>
                            </c:forEach>
                        </table>
                    </div>
                </c:if>
            </c:if>

            <div class="form-horizontal">
                <div class="form-group">
                    <div class="col-xs-12 col-md-6">
                        <strong class="app-font-size-22 premHeader">Mode of Service Delivery ${status.index+1}</strong>
                    </div>
                    <div class="col-xs-12 col-md-4 text-right">
                        <c:choose>
                            <c:when test="${!status.first && requestInformationConfig==null && 'APTY004' !=AppSubmissionDto.appType && 'APTY005' !=AppSubmissionDto.appType}">
                                <h4 class="text-danger"><em class="fa fa-times-circle del-size-36 removeBtn"></em></h4>
                                <c:set var="canEdit" value="false"/>
                            </c:when>
                            <c:when test="${((requestInformationConfig != null && appGrpPremisesDto.rfiCanEdit) || 'APTY004' ==AppSubmissionDto.appType || 'APTY005' ==AppSubmissionDto.appType) && '1' != appGrpPremisesDto.existingData }">
                                <c:set var="canEdit" value="false"/>
                                <c:if test="${AppSubmissionDto.appEditSelectDto.premisesEdit}">
                                    <a class="premises-summary-preview premisesEdit app-font-size-16"><em class="fa fa-pencil-square-o"></em><span style="display: inline-block;">&nbsp;</span>Edit</a>
                                </c:if>
                            </c:when>
                        </c:choose>
                    </div>
                </div>
            </div>
            <div class="form-horizaontal">
                <div class="form-group">
                    <span  id="error_premisesHci${status.index}" class="error-msg" name="iaisErrorMsg"></span>
                </div>
            </div>
            <c:if test="${requestInformationConfig != null || 'APTY004' ==AppSubmissionDto.appType || 'APTY005' ==AppSubmissionDto.appType}">
            <div class="form-horizontal">
                <div class="form-group">
                    <div class="col-xs-12">
                        <span class="premise-type ack-font-16">
                            <strong>
                                <c:if test="${'ONSITE' == appGrpPremisesDto.premisesType}">
                                    <c:out value="Address: "/>
                                </c:if>
                                <c:if test="${'CONVEYANCE' == appGrpPremisesDto.premisesType}">
                                    <c:out value="Address: "/>
                                </c:if>
                                <c:if test="${'OFFSITE' == appGrpPremisesDto.premisesType}">
                                    <c:out value="Address: "/>
                                </c:if>
                                <c:if test="${'EASMTS' == appGrpPremisesDto.premisesType}">
                                    <c:out value="Address: "/>
                                </c:if>
                            </strong>
                        </span>
                            <span class="premise-address ack-font-16">
                            <c:out value="${appGrpPremisesDto.address}"/>
                        </span>
                    </div>
                    <div class="col-xs-12 ack-font-16">
                        <c:if test="${'CONVEYANCE' == appGrpPremDto.premisesType}">
                            <strong>Vehicle No:</strong> <span class="vehicle-info">${appGrpPremDto.conveyanceVehicleNo}</span>
                        </c:if>
                    </div>
                </div>
            </div>
            </c:if>
            <div class="form-horizontal">
                <div class="form-group premisesTypeDiv" id="premisesType" <c:if test="${'APTY005' ==AppSubmissionDto.appType || 'APTY004'==AppSubmissionDto.appType }">hidden</c:if> >
                    <label class="col-xs-12 col-md-4 control-label error-msg-type" for="premisesType">What is your mode of service delivery ? <span class="mandatory">*</span></label>
                    <input class="premTypeValue" type="hidden" name="premType" value="${appGrpPremisesDto.premisesType}"/>
                    <input class="premSelValue" type="hidden" value="${appGrpPremisesDto.premisesSelect}"/>

                    <c:set var="premTypeLen" value="${premisesType.size()}"/>
                    <c:forEach var="premType" items="${premisesType}">
                        <c:set var="premTypeCss" value="col-md-3"/>
                        <c:choose>
                            <c:when test="${'ONSITE' == premType}">
                                <c:set var="className" value="onSite"/>
                                <c:set var="premTypeCss" value="${premTypeLen > 2 ? 'col-md-2' : 'col-md-3'}"/>
                            </c:when>
                            <c:when test="${'CONVEYANCE' == premType}">
                                <c:set var="className" value="conveyance" />
                            </c:when>
                            <c:when test="${'OFFSITE' == premType }">
                                <c:set var="className" value="offSite" />
                            </c:when>
                            <c:when test="${'EASMTS' == premType }">
                                <c:set var="className" value="easMts" />
                            </c:when>
                        </c:choose>
                        <div class="col-xs-12 ${premTypeCss}">
                            <c:choose>
                                <c:when test="${premType == onSite}">
                                    <a class="btn-tooltip styleguide-tooltip" style="z-index: 999;position: absolute; right: 30px; top: 12px;" href="javascript:void(0);" data-placement="top"  data-toggle="tooltip" data-html="true" title="&lt;p&gt;<iais:message  key="NEW_ACK019"></iais:message>&lt;/p&gt;">i</a>
                                </c:when>
                                <c:when test="${premType == conv || premType == easMts}">
                                    <a class="btn-tooltip styleguide-tooltip" style="z-index: 999;position: absolute; right: 30px; top: 12px;" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="&lt;p&gt;<iais:message  key="NEW_ACK021"></iais:message>&lt;/p&gt;">i</a>
                                </c:when>
                                <c:when test="${premType == offSite}">
                                    <a class="btn-tooltip styleguide-tooltip"  style="z-index: 999;position: absolute; right: 30px; top: 12px;" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="&lt;p&gt;<iais:message  key="NEW_ACK020"></iais:message>&lt;/p&gt;">i</a>
                                </c:when>
                            </c:choose>

                            <div class="form-check">
                                <c:if test="${appGrpPremisesDto.premisesType!=premType}">
                                    <input class="form-check-input premTypeRadio ${className}"  type="radio" name="premType${status.index}" value = "${premType}" aria-invalid="false">
                                </c:if>
                                <c:if test="${appGrpPremisesDto.premisesType==premType}">
                                    <input class="form-check-input premTypeRadio ${className}"  type="radio" name="premType${status.index}" checked="checked" value = "${premType}"  aria-invalid="false">
                                </c:if>
                                <label class="form-check-label" ><span class="check-circle"></span>
                                    <c:if test="${premType == onSite}">
                                        <c:out value="Premises" />
                                        <br/>
                                        <span>(at fixed address)</span>
                                    </c:if>
                                    <c:if test="${premType == conv}">
                                        <c:out value="Conveyance" />
                                        <br/>
                                        <span>(registered vehicle, aircraft, vessel or train)</span>
                                    </c:if>
                                    <c:if test="${premType == offSite}">
                                        <c:out value="Off-site" />
                                        <br/>
                                        <span>(remotely/non-fixed location)</span>
                                    </c:if>
                                    <c:if test="${premType == easMts}">
                                        <c:out value="Conveyance" />
                                        <br/>
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
                    <iais:field value="Add or select a mode of service delivery from the list :" width="4" mandatory="true"/>
                    <iais:value id="onSiteSelect"  cssClass="col-xs-11 col-sm-7 col-md-5">
                        <c:choose>
                            <c:when test="${appGrpPremisesDto.premisesType == onSite}">
                                <iais:select cssClass="premSelect" id="onSiteSel" name="onSiteSelect"  options="premisesSelect" needSort="false"  value="${appGrpPremisesDto.premisesSelect}"></iais:select>
                            </c:when>
                            <c:otherwise>
                                <iais:select cssClass="premSelect" id="onSiteSel" name="onSiteSelect"  options="premisesSelect" needSort="false"  value=""></iais:select>
                            </c:otherwise>
                        </c:choose>
                    </iais:value>
                </iais:row>
                <iais:row cssClass="conveyanceSelect hidden">
                    <iais:field value="Add or select a mode of service delivery from the list :" width="4" mandatory="true"/>
                    <iais:value id="conveyanceSelect"  cssClass="col-xs-11 col-sm-7 col-md-5">
                        <c:choose>
                            <c:when test="${appGrpPremisesDto.premisesType == conv}">
                                <iais:select cssClass="premSelect" id="conveyanceSel" name="conveyanceSelect"  options="conveyancePremSel" needSort="false"  value="${appGrpPremisesDto.premisesSelect}"></iais:select>
                            </c:when>
                            <c:otherwise>
                                <iais:select cssClass="premSelect" id="conveyanceSel" name="conveyanceSelect"  options="conveyancePremSel" needSort="false"  value=""></iais:select>
                            </c:otherwise>
                        </c:choose>
                    </iais:value>
                </iais:row>
                <iais:row cssClass="offSiteSelect hidden">
                    <iais:field value="Add or select a mode of service delivery from the list :" width="4" mandatory="true"/>
                    <iais:value id="offSiteSelect"  cssClass="col-xs-11 col-sm-7 col-md-5">
                        <c:choose>
                            <c:when test="${appGrpPremisesDto.premisesType == offSite}">
                                <iais:select cssClass="premSelect" id="offSiteSel" name="offSiteSelect"  options="offSitePremSel" needSort="false"  value="${appGrpPremisesDto.premisesSelect}"></iais:select>
                            </c:when>
                            <c:otherwise>
                                <iais:select cssClass="premSelect" id="offSiteSel" name="offSiteSelect"  options="offSitePremSel" needSort="false"  value=""></iais:select>
                            </c:otherwise>
                        </c:choose>
                    </iais:value>
                </iais:row>
                <iais:row cssClass="easMtsSelect hidden">
                    <iais:field value="Add or select a mode of service delivery from the list :" width="4" mandatory="true"/>
                    <iais:value id="easMtsSelect"  cssClass="col-xs-11 col-sm-7 col-md-5">
                        <c:choose>
                            <c:when test="${appGrpPremisesDto.premisesType == easMts}">
                                <iais:select cssClass="premSelect" id="easMtsSel" name="easMtsSelect"  options="easMtsPremSel" needSort="false"  value="${appGrpPremisesDto.premisesSelect}"></iais:select>
                            </c:when>
                            <c:otherwise>
                                <iais:select cssClass="premSelect" id="easMtsSel" name="easMtsSelect"  options="easMtsPremSel" needSort="false"  value=""></iais:select>
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
                    <div class="form-group">
                        <label class="col-xs-12 col-md-4 control-label" for="siteSafefyNo">Fire Safety & Shelter Bureau Ref. No. <a class="btn-tooltip styleguide-tooltip" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="&lt;p&gt;<iais:message key="NEW_ACK006"></iais:message>&lt;/p&gt;">i</a></label>
                        <div class="col-xs-9 col-sm-5 col-md-5">
                            <input id="siteSafefyNo" maxlength="66" name="onSiteScdfRefNo" type="text" value="${appGrpPremisesDto.scdfRefNo}">
                        </div>
                        <label class="col-xs-12 col-md-4 control-label"></label>
                        <div class="col-xs-9 col-sm-5 col-md-5">
                            <span id="error_ScdfRefNo${status.index}" name="iaisErrorMsg" class="error-msg"></span>
                        </div>
                    </div>
                    <iais:row>
                        <iais:field value="Fire Safety Certificate Issued Date" width="4"/>
                        <iais:value cssClass="col-xs-7 col-sm-4 col-md-5 fireIssuedDateDiv">
                            <iais:datePicker cssClass="fireIssuedDate" name="${premValue}onSiteFireSafetyCertIssuedDate" value="${appGrpPremisesDto.certIssuedDtStr}" />
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Business Name" mandatory="true" width="4"/>
                        <iais:value width="7" cssClass="col-xs-10 col-md-5 disabled">
                            <iais:input cssClass="" maxLength="100" type="text" name="onSiteHciName" id="sitePremiseName" value="${appGrpPremisesDto.hciName}"></iais:input>
                            <span id="error_hciName${status.index}" name="iaisErrorMsg" class="error-msg"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row cssClass="postalCodeDiv">
                        <iais:field value="Postal Code" mandatory="true" width="4"/>
                        <iais:value cssClass="col-xs-10 col-md-5">
                            <iais:input cssClass="postalCode" maxLength="6" type="text"  name="onSitePostalCode"  value="${appGrpPremisesDto.postalCode}"></iais:input>
                            <span  id="error_postalCode${status.index}" class="error-msg" name="iaisErrorMsg"></span>
                        </iais:value>
                        <div class="col-xs-7 col-sm-6 col-md-3">
                            <p><a class="retrieveAddr <c:if test="${!canEdit || readOnly}">hidden</c:if>" id="onSite" >Retrieve your address</a></p>
                        </div>

                    </iais:row>
                    <iais:row>
                        <iais:field value="Address Type" mandatory="true" width="4"/>
                        <iais:value id="onSiteAddressType${premValue}" cssClass="col-xs-7 col-sm-4 col-md-5 addressType">
                            <iais:select cssClass="onSiteAddressType" name="onSiteAddressType" id="onSiteAddressType" codeCategory="CATE_ID_ADDRESS_TYPE" needSort="false" firstOption="Please Select" value="${appGrpPremisesDto.addrType}" ></iais:select>
                            <span class="error-msg" name="iaisErrorMsg" id="error_addrType${status.index}"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Block / House No." width="4"/>
                        <iais:value cssClass="col-xs-7 col-md-5">
                            <iais:input cssClass="siteBlkNo" maxLength="10"  type="text" name="onSiteBlkNo" id="siteBlkNo" value="${appGrpPremisesDto.blkNo}"></iais:input>
                            <span class="error-msg" name="iaisErrorMsg" id="error_blkNo${status.index}"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Floor / Unit No." width="4"/>
                        <iais:value cssClass="col-xs-7 col-sm-4 col-md-5 ">
                            <div class="row">
                                <iais:value cssClass="col-xs-12 col-md-5 ">
                                    <iais:input maxLength="3" type="text" name="onSiteFloorNo" id="siteFloorNo" value="${appGrpPremisesDto.floorNo}"></iais:input>
                                    <span class="error-msg" name="iaisErrorMsg" id="error_floorNo${status.index}"></span>
                                </iais:value>
                                <div class="col-xs-12 col-md-2 text-center"><p>-</p></div>
                                <iais:value cssClass="col-xs-12 col-md-5 ">
                                    <iais:input maxLength="5" type="text" name="onSiteUnitNo" id="siteUnitNo" value="${appGrpPremisesDto.unitNo}"></iais:input>
                                    <span class="error-msg" name="iaisErrorMsg" id="error_unitNo${status.index}"></span>
                                </iais:value>
                            </div>
                        </iais:value>
                    </iais:row>
                    <div class="operationDivGroup">
                        <c:if test="${appGrpPremisesDto.appPremisesOperationalUnitDtos.size()>0}">
                            <c:forEach var="operationDto" items="${appGrpPremisesDto.appPremisesOperationalUnitDtos}" varStatus="opStat">
                                <div class="operationDiv">
                                    <iais:row>
                                        <iais:field value="" width="4"/>
                                        <iais:value cssClass="col-xs-7 col-sm-4 col-md-5 ">
                                            <div class="row">
                                                <iais:value cssClass="col-xs-12 col-md-5 ">
                                                    <iais:input cssClass="floorNo" maxLength="3" type="text" name="${premValue}onSiteFloorNo${opStat.index}" value="${operationDto.floorNo}"></iais:input>
                                                    <span class="error-msg" name="iaisErrorMsg" id="error_opFloorNo${premValue}${opStat.index}"></span>
                                                </iais:value>
                                                <div class="col-xs-12 col-md-2 text-center"><p>-</p></div>
                                                <iais:value cssClass="col-xs-12 col-md-5 ">
                                                    <iais:input cssClass="unitNo" maxLength="5" type="text" name="${premValue}onSiteUnitNo${opStat.index}" value="${operationDto.unitNo}"></iais:input>
                                                    <span class="error-msg" name="iaisErrorMsg" id="error_opUnitNo${premValue}${opStat.index}"></span>
                                                </iais:value>
                                            </div>
                                            <span class="error-msg" name="iaisErrorMsg" id="error_floorUnit${premValue}${opStat.index}"></span>
                                        </iais:value>
                                        <div class=" col-xs-7 col-sm-4 col-md-2 ">
                                            <p>(Additional)&nbsp;&nbsp;&nbsp;&nbsp;</p>
                                        </div>
                                        <div class=" col-xs-7 col-sm-4 col-md-1 text-center">
                                            <p class="text-danger opDel"><em class="fa fa-times-circle del-size-36"></em></p>
                                        </div>
                                    </iais:row>
                                </div>
                            </c:forEach>
                        </c:if>
                        <!--prem operational -->
                        <iais:row cssClass="addOpDiv">
                            <iais:field value="" width="4"/>
                            <iais:value cssClass="col-xs-7 col-sm-4 col-md-5 ">
                                <span class="addOperational"><a style="text-decoration:none;">+ Add Additional Floor/Unit No.</a></span>
                            </iais:value>
                        </iais:row>
                    </div>
                    <iais:row>
                        <iais:field value="Street Name" mandatory="true" width="4"/>
                        <iais:value width="5" cssClass="col-md-5">
                            <iais:input cssClass="siteStreetName" maxLength="32" type="text" name="onSiteStreetName" id="siteStreetName" value="${appGrpPremisesDto.streetName}"></iais:input>
                            <span class="error-msg" name="iaisErrorMsg" id="error_streetName${status.index}"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Building Name" width="4"/>
                        <iais:value width="5" cssClass="col-md-5">
                            <iais:input cssClass="siteBuildingName" maxLength="66" type="text" name="onSiteBuildingName" id="siteBuildingName" value="${appGrpPremisesDto.buildingName}"></iais:input>
                            <span class="error-msg" name="iaisErrorMsg" id="error_buildingName${status.index}"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Email" mandatory="true" width="4"/>
                        <iais:value width="5" cssClass="col-md-5">
                            <iais:input maxLength="320" cssClass="easMtsPubEmail" type="text" name="onSiteEmail"  value="${appGrpPremisesDto.easMtsPubEmail}"></iais:input>
                            <span  class="error-msg" name="iaisErrorMsg" id="error_onSiteEmail${status.index}"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Office Telephone No." mandatory="true" width="4"/>
                        <iais:value cssClass="col-xs-7 col-sm-4 col-md-5">
                            <iais:input type="text" name="onSiteOffTelNo" maxLength="8" value="${appGrpPremisesDto.offTelNo}" id="onsitOffice" cssClass="onsitOffice" />
                            <span class="error-msg" name="iaisErrorMsg" id="error_offTelNo${status.index}"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row cssClass="other-lic-content co-location-div">
                        <iais:field value="Are you co-locating with<br/>another licensee?" mandatory="true" width="4" />
                        <iais:value cssClass="col-xs-7 col-sm-4 col-md-6" style="margin-left:-1%;">
                            <input type="hidden" class="co-location-val" name="onSiteIsOtherLic" value="${appGrpPremisesDto.locateWithOthers}"/>
                            <div class="form-check col-xs-6 col-md-3 col-5">
                                <input <c:if test="${'1'==appGrpPremisesDto.locateWithOthers}">checked="checked"</c:if> class="form-check-input other-lic co-location"  type="radio" name="otherLicence${status.index}" value = "1" aria-invalid="false">
                                <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
                            </div>
                            <div class="form-check col-xs-6 col-md-3 col-5">
                                <input <c:if test="${'0'==appGrpPremisesDto.locateWithOthers}">checked="checked"</c:if> class="form-check-input other-lic co-location"  type="radio" name="otherLicence${status.index}" value = "0" aria-invalid="false">
                                <label class="form-check-label" ><span class="check-circle"></span>No</label>
                            </div>
                            <div class="col-sm-12">
                                <span class="error-msg " name="iaisErrorMsg" id="error_isOtherLic${status.index}"></span>
                            </div>
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <div class="col-md-12 col-xs-12">
                            <label class="control-label">Operating Hours</label>
                        </div>
                        <div class="col-md-4 col-xs-4  hidden-xs hidden-sm">
                            <label class="control-label">Weekly <span class="mandatory">*</span></label>
                        </div>
                        <div class="col-md-3 col-xs-3 input-padding hidden-xs hidden-sm">
                            <label class="control-label">Start</label>
                        </div>
                        <div class="col-md-3 col-xs-3 input-padding hidden-xs hidden-sm">
                            <label class="control-label">End</label>
                        </div>
                        <div class="col-md-2 col-xs-2 hidden-xs hidden-sm">
                            <label class="control-label">24 Hours</label>
                        </div>
                    </iais:row>
                    <c:set var="weeklyList" value="${appGrpPremisesDto.weeklyDtoList}"/>
                    <div class="weeklyContent">
                        <c:choose>
                            <c:when test="${weeklyList.size()>0 && 'ONSITE' == appGrpPremisesDto.premisesType}">
                                <c:forEach begin="0" end="${weeklyList.size()-1}" step="1" varStatus="weeklyStat">
                                    <c:set var="weekly" value="${weeklyList[weeklyStat.index]}"/>
                                    <iais:row cssClass="weeklyDiv">
                                        <c:if test="${weeklyStat.index>0}">
                                            <div class="col-md-12 col-xs-12">
                                                <label class="control-label">Weekly <span class="mandatory">*</span></label>
                                            </div>
                                        </c:if>
                                        <div>
                                            <div class="col-md-4 col-xs-4 multi-sel-padding">
                                                <div class="row d-flex">
                                                    <div class="col-xs-12 visible-xs visible-sm">
                                                        <label class="control-label">Weekly <span class="mandatory">*</span></label>
                                                    </div>
                                                    <div class="col-md-12 multi-select col-xs-12">
                                                        <iais:select name="${premValue}onSiteWeekly${weeklyStat.index}" multiValues="${weekly.selectValList}" options="weeklyOpList"  multiSelect="true"/>
                                                    </div>
                                                    <div class="col-md-12 col-xs-12">
                                                        <span class="error-msg " name="iaisErrorMsg" id="error_onSiteWeekly${status.index}${weeklyStat.index}"></span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-3 col-xs-3 start-div">
                                                <div class="row d-flex">
                                                    <div class="col-sm-12 visible-xs visible-sm">
                                                        <label class="control-label">Start</label>
                                                    </div>
                                                    <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                        <iais:select cssClass="WeeklyStartHH" name="${premValue}onSiteWeeklyStartHH${weeklyStat.index}" options="premiseHours" value="${weekly.startFromHH}" firstOption="--"></iais:select>
                                                    </div>
                                                    <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                        (HH)
                                                    </div>
                                                    <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                        <iais:select cssClass="WeeklyStartMM" name="${premValue}onSiteWeeklyStartMM${weeklyStat.index}" options="premiseMinute" value="${weekly.startFromMM}" firstOption="--"></iais:select>
                                                    </div>
                                                    <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                        (MM)
                                                    </div>
                                                    <div class="col-md-12 col-xs-12">
                                                        <span class="error-msg " name="iaisErrorMsg" id="error_onSiteWeeklyStart${status.index}${weeklyStat.index}"></span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-3 col-xs-3 end-div">
                                                <div class="row d-flex">
                                                    <div class="col-xs-12 visible-xs visible-sm">
                                                        <label class="control-label">End</label>
                                                    </div>
                                                    <div class="col-md-10 col-lg-5 col-9 input-padding" >
                                                        <iais:select cssClass="WeeklyEndHH" name="${premValue}onSiteWeeklyEndHH${weeklyStat.index}" options="premiseHours" value="${weekly.endToHH}" firstOption="--"></iais:select>
                                                    </div>
                                                    <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                        (HH)
                                                    </div>
                                                    <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                        <iais:select cssClass="WeeklyEndMM" name="${premValue}onSiteWeeklyEndMM${weeklyStat.index}" options="premiseMinute" value="${weekly.endToMM}" firstOption="--"></iais:select>
                                                    </div>
                                                    <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                        (MM)
                                                    </div>
                                                    <div class="col-md-12 col-xs-12">
                                                        <span class="error-msg " name="iaisErrorMsg" id="error_onSiteWeeklyEnd${status.index}${weeklyStat.index}"></span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-2 col-xs-2 all-day-div">
                                                <div class="row d-flex">
                                                    <div class="col-xs-12 visible-xs visible-sm">
                                                        <label class="control-label">24 Hours</label>
                                                    </div>
                                                    <div class="col-md-5 text-center col-xs-5 all-day-position">
                                                        <input class="form-check-input allDay" name="${premValue}onSiteWeeklyAllDay${weeklyStat.index}"  type="checkbox" aria-invalid="false" value="true" <c:if test="${weekly.selectAllDay}">checked="checked"</c:if> >
                                                    </div>
                                                    <div class="col-md-5 col-xs-5">
                                                        <c:if test="${weeklyStat.index>0}">
                                                            <div class="fa fa-times-circle del-size-36 text-danger weeklyDel"></div>
                                                        </c:if>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-4 col-xs-4">
                                            </div>
                                            <div class="col-md-8 col-xs-8">
                                                <span class="error-msg " name="iaisErrorMsg" id="error_onSiteWeeklyTime${status.index}${weeklyStat.index}"></span>
                                            </div>
                                        </div>
                                    </iais:row>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <c:set var="suffix" value="0"/>
                                <iais:row cssClass="weeklyDiv">
                                    <div>
                                        <div class="col-md-4 col-xs-4 multi-sel-padding">
                                            <div class="row d-flex">
                                                <div class="col-xs-12 visible-xs visible-sm">
                                                    <label class="control-label">Weekly <span class="mandatory">*</span></label>
                                                </div>
                                                <div class="col-md-12 multi-select col-xs-12">
                                                    <iais:select name="${premValue}onSiteWeekly${suffix}"  options="weeklyOpList" multiSelect="true" multiValues="" />
                                                </div>
                                                <div class="col-md-12 col-xs-12">
                                                    <span class="error-msg " name="iaisErrorMsg" id="error_onSiteWeekly${status.index}${suffix}"></span>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-3 col-xs-3 start-div">
                                            <div class="row d-flex">
                                                <div class="col-sm-12 visible-xs visible-sm">
                                                    <label class="control-label">Start</label>
                                                </div>
                                                <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                    <iais:select cssClass="WeeklyStartHH" name="${premValue}onSiteWeeklyStartHH${suffix}" options="premiseHours" value="" firstOption="--"></iais:select>
                                                </div>
                                                <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                    (HH)
                                                </div>
                                                <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                    <iais:select cssClass="WeeklyStartMM" name="${premValue}onSiteWeeklyStartMM${suffix}" options="premiseMinute" value="" firstOption="--"></iais:select>
                                                </div>
                                                <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                    (MM)
                                                </div>
                                                <div class="col-md-12 col-xs-12">
                                                    <span class="error-msg " name="iaisErrorMsg" id="error_onSiteWeeklyStart${status.index}${suffix}"></span>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-3 col-xs-3 end-div">
                                            <div class="row d-flex">
                                                <div class="col-sm-12 visible-xs visible-sm">
                                                    <label class="control-label">End</label>
                                                </div>
                                                <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                    <iais:select cssClass="WeeklyEndHH" name="${premValue}onSiteWeeklyEndHH${suffix}" options="premiseHours" value="" firstOption="--"></iais:select>
                                                </div>
                                                <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                    (HH)
                                                </div>
                                                <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                    <iais:select cssClass="WeeklyEndMM" name="${premValue}onSiteWeeklyEndMM${suffix}" options="premiseMinute" value="" firstOption="--"></iais:select>
                                                </div>
                                                <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                    (MM)
                                                </div>
                                                <div class="col-md-12 col-xs-12">
                                                    <span class="error-msg " name="iaisErrorMsg" id="error_onSiteWeeklyEnd${status.index}${suffix}"></span>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-2 col-xs-2 all-day-div">
                                            <div class="row d-flex">
                                                <div class="col-xs-12 visible-xs visible-sm">
                                                    <label class="control-label">24 Hours</label>
                                                </div>
                                                <div class="col-md-5 col-xs-5 text-center all-day-position">
                                                    <input class="form-check-input allDay" name="${premValue}onSiteWeeklyAllDay${suffix}"  type="checkbox" aria-invalid="false" value="true"  >
                                                </div>
                                                <div class="col-md-5 col-xs-5">
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </iais:row>
                            </c:otherwise>
                        </c:choose>

                        <div class="form-group addWeeklyDiv <c:if test="${weeklyList.size() >= weeklyCount}">hidden</c:if>">
                            <iais:value cssClass="col-xs-4 col-sm-4 col-md-4">
                                <a class="addWeekly" style="text-decoration:none;">+ Add</a>
                            </iais:value>
                            <iais:value cssClass="col-xs-8 col-sm-4 col-md-8">

                            </iais:value>
                        </div>
                    </div>

                    <c:set var="phList" value="${appGrpPremisesDto.phDtoList}"/>
                    <div class="pubHolDayContent">
                        <c:choose>
                            <c:when test="${phList.size()>0 && 'ONSITE' == appGrpPremisesDto.premisesType}">
                                <c:forEach begin="0" end="${phList.size()-1}" step="1" varStatus="phyStat">
                                    <c:set var="ph" value="${phList[phyStat.index]}"/>
                                    <iais:row cssClass="pubHolidayDiv">
                                        <div class="col-md-12">
                                            <label class="control-label">Public Holiday</label>
                                        </div>
                                        <div>
                                            <div class="col-md-4 multi-sel-padding">
                                                <div class="row d-flex">
                                                    <div class="col-md-12 multi-select col-xs-12">
                                                        <iais:select name="${premValue}onSitePubHoliday${phyStat.index}" multiValues="${ph.selectValList}" options="phOpList" multiSelect="true"/>
                                                    </div>
                                                    <div class="col-md-12 col-xs-12">
                                                        <span class="error-msg " name="iaisErrorMsg" id="error_onSitePubHoliday${status.index}${phyStat.index}"></span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-3 col-xs-3 start-div">
                                                <div class="row d-flex">
                                                    <div class="col-sm-12 visible-xs visible-sm">
                                                        <label class="control-label">Start</label>
                                                    </div>
                                                    <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                        <iais:select cssClass="PhStartHH" name="${premValue}onSitePhStartHH${phyStat.index}" options="premiseHours" value="${ph.startFromHH}" firstOption="--"></iais:select>
                                                    </div>
                                                    <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                        (HH)
                                                    </div>
                                                    <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                        <iais:select cssClass="PhStartMM" name="${premValue}onSitePhStartMM${phyStat.index}" options="premiseMinute" value="${ph.startFromMM}" firstOption="--"></iais:select>
                                                    </div>
                                                    <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                        (MM)
                                                    </div>
                                                    <div class="col-md-12 col-xs-12">
                                                        <span class="error-msg " name="iaisErrorMsg" id="error_onSitePhStart${status.index}${phyStat.index}"></span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-3 col-xs-3 end-div">
                                                <div class="row d-flex">
                                                    <div class="col-sm-12 visible-xs visible-sm">
                                                        <label class="control-label">End</label>
                                                    </div>
                                                    <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                        <iais:select cssClass="PhEndHH" name="${premValue}onSitePhEndHH${phyStat.index}" options="premiseHours" value="${ph.endToHH}" firstOption="--"></iais:select>
                                                    </div>
                                                    <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                        (HH)
                                                    </div>
                                                    <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                        <iais:select cssClass="PhEndMM" name="${premValue}onSitePhEndMM${phyStat.index}" options="premiseMinute" value="${ph.endToMM}" firstOption="--"></iais:select>
                                                    </div>
                                                    <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                        (MM)
                                                    </div>
                                                    <div class="col-md-12 col-xs-12">
                                                        <span class="error-msg " name="iaisErrorMsg" id="error_onSitePhEnd${status.index}${phyStat.index}"></span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-2 col-xs-2 all-day-div">
                                                <div class="row d-flex">
                                                    <div class="col-sm-12 visible-xs visible-sm">
                                                        <label class="control-label">24 Hours</label>
                                                    </div>
                                                    <div class="col-md-5 col-xs-5 text-center all-day-position">
                                                        <input class="form-check-input allDay" name="${premValue}onSitePhAllDay${phyStat.index}"  type="checkbox" aria-invalid="false" value="true" <c:if test="${ph.selectAllDay}">checked="checked"</c:if> >
                                                    </div>
                                                    <div class="col-md-5 col-xs-5">
                                                        <c:if test="${phyStat.index>0}">
                                                            <div class="fa fa-times-circle del-size-36 text-danger pubHolidayDel"></div>
                                                        </c:if>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-4 col-xs-4">
                                            </div>
                                            <div class="col-md-8 col-xs-8">
                                                <span class="error-msg " name="iaisErrorMsg" id="error_onSitePhTime${status.index}${phyStat.index}"></span>
                                            </div>
                                        </div>
                                    </iais:row>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <c:set var="suffix" value="0"/>
                                <iais:row cssClass="pubHolidayDiv">
                                    <div class="col-md-12 col-xs-12">
                                        <label class="control-label">Public Holiday</label>
                                    </div>
                                    <div>
                                        <div class="col-md-4 col-xs-4 multi-sel-padding">
                                            <div class="row d-flex">
                                                <div class="col-md-12 multi-select col-xs-12">
                                                    <iais:select name="${premValue}onSitePubHoliday${suffix}" options="phOpList"  multiSelect="true"/>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-3 col-xs-3 start-div">
                                            <div class="row d-flex">
                                                <div class="col-sm-12 visible-xs visible-sm">
                                                    <label class="control-label">Start</label>
                                                </div>
                                                <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                    <iais:select cssClass="PhStartHH" name="${premValue}onSitePhStartHH${suffix}" options="premiseHours" value="" firstOption="--"></iais:select>
                                                </div>
                                                <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                    (HH)
                                                </div>
                                                <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                    <iais:select cssClass="PhStartMM" name="${premValue}onSitePhStartMM${suffix}" options="premiseMinute" value="" firstOption="--"></iais:select>
                                                </div>
                                                <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                    (MM)
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-3 col-xs-3 end-div">
                                            <div class="row d-flex">
                                                <div class="col-sm-12 visible-xs visible-sm">
                                                    <label class="control-label">End</label>
                                                </div>
                                                <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                    <iais:select cssClass="PhEndHH" name="${premValue}onSitePhEndHH${suffix}" options="premiseHours" value="" firstOption="--"></iais:select>
                                                </div>
                                                <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                    (HH)
                                                </div>
                                                <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                    <iais:select cssClass="PhEndMM" name="${premValue}onSitePhEndMM${suffix}" options="premiseMinute" value="" firstOption="--"></iais:select>
                                                </div>
                                                <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                    (MM)
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-2 col-xs-2 all-day-div">
                                            <div class="row d-flex">
                                                <div class="col-sm-12 visible-xs visible-sm">
                                                    <label class="control-label">24 Hours</label>
                                                </div>
                                                <div class="col-md-5 col-xs-5 text-center all-day-position">
                                                    <input class="form-check-input allDay" name="${premValue}onSitePhAllDay${suffix}"  type="checkbox" aria-invalid="false" value="true" >
                                                </div>
                                                <div class="col-md-5 col-xs-5">
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </iais:row>
                            </c:otherwise>
                        </c:choose>

                        <div class="form-group addPhDiv <c:if test="${phList.size() >= phCount}">hidden</c:if>">
                            <iais:value cssClass="col-xs-4 col-sm-4 col-md-4">
                                <a class="addPubHolDay" style="text-decoration:none;">+ Add</a>
                            </iais:value>
                            <iais:value cssClass="col-xs-8 col-sm-4 col-md-8">

                            </iais:value>
                        </div>
                    </div>

                    <c:set var="eventList" value="${appGrpPremisesDto.eventDtoList}"/>
                    <div class="eventContent">
                        <c:choose>
                            <c:when test="${eventList.size()>0 && 'ONSITE' == appGrpPremisesDto.premisesType}">
                                <c:forEach begin="0" end="${eventList.size()-1}" step="1" varStatus="eventStat">
                                    <c:set var="event" value="${eventList[eventStat.index]}"/>
                                    <iais:row cssClass="eventDiv">
                                        <div class="col-md-12 col-xs-12">
                                            <label class="control-label">Event</label>
                                        </div>
                                        <div>
                                            <div class="col-md-4 col-xs-4">
                                                <div class="row">
                                                    <div class="col-md-12 col-xs-12">
                                                        <iais:input type="text" maxLength="100" cssClass="Event" name="${premValue}onSiteEvent${eventStat.index}" value="${event.eventName}" />
                                                    </div>
                                                    <div class="col-md-12 col-xs-12">
                                                        <span class="error-msg " name="iaisErrorMsg" id="error_onSiteEvent${status.index}${eventStat.index}"></span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-3 col-xs-3">
                                                <iais:datePicker cssClass="EventStart" name="${premValue}onSiteEventStart${eventStat.index}" value="${event.startDateStr}" />
                                                <span class="error-msg " name="iaisErrorMsg" id="error_onSiteEventStart${status.index}${eventStat.index}"></span>
                                            </div>
                                            <div class="col-md-3 col-xs-3">
                                                <iais:datePicker cssClass="EventEnd" name="${premValue}onSiteEventEnd${eventStat.index}" value="${event.endDateStr}" />
                                                <span class="error-msg " name="iaisErrorMsg" id="error_onSiteEventEnd${status.index}${eventStat.index}"></span>
                                            </div>
                                            <div class="col-md-2 col-xs-2">
                                                <div class="row">
                                                    <div class="col-md-6 text-center col-xs-6">
                                                    </div>
                                                    <div class="col-md-6 col-xs-6">
                                                        <c:if test="${eventStat.index>0}">
                                                            <div class="fa fa-times-circle del-size-36 text-danger eventDel"></div>
                                                        </c:if>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="">
                                            </div>
                                            <div class="col-md-8 col-xs-8">
                                                <span class="error-msg " name="iaisErrorMsg" id="error_onSiteEventDate${status.index}${eventStat.index}"></span>
                                            </div>
                                        </div>
                                    </iais:row>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <c:set var="suffix" value="0"/>
                                <iais:row cssClass="eventDiv">
                                    <div class="col-md-12 col-xs-12">
                                        <label class="control-label">Event</label>
                                    </div>
                                    <div>
                                        <div class="col-md-4 col-xs-4">
                                            <div class="row">
                                                <div class="col-md-12 col-xs-12">
                                                    <iais:input maxLength="100" type="text" cssClass="Event" name="${premValue}onSiteEvent${suffix}" value="" />
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-3 col-xs-3">
                                            <iais:datePicker cssClass="EventStart" name="${premValue}onSiteEventStart${suffix}" value="" />
                                        </div>
                                        <div class="col-md-3 col-xs-3">
                                            <iais:datePicker cssClass="EventEnd" name="${premValue}onSiteEventEnd${suffix}" value="" />
                                        </div>
                                        <div class="col-md-2 col-xs-2">
                                            <div class="row">
                                                <div class="col-md-6 text-center col-xs-6">
                                                </div>
                                                <div class="col-md-6 col-xs-6">
                                                    <c:if test="${eventStat.index>0}">
                                                        <div class="fa fa-times-circle del-size-36 text-danger eventDel"></div>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </iais:row>
                            </c:otherwise>
                        </c:choose>

                        <div class="form-group addEventDiv <c:if test="${eventList.size() >= eventCount}">hidden</c:if>">
                            <iais:value cssClass="col-xs-4 col-sm-4 col-md-4">
                                <a class="addEvent" style="text-decoration:none;">+ Add</a>
                            </iais:value>
                            <iais:value cssClass="col-xs-8 col-sm-4 col-md-8">

                            </iais:value>
                        </div>
                    </div>
                </div>
            </div>

            <div class="new-premise-form-conv hidden">
                <div class="form-horizontal">
                    <iais:row>
                        <iais:field value="Business Name" mandatory="true" width="4"/>
                        <iais:value width="7" cssClass="col-xs-10 col-md-5">
                            <iais:input cssClass="hciName" maxLength="100" type="text" name="conveyanceHciName" value="${appGrpPremisesDto.conveyanceHciName}"></iais:input>
                            <span  class="error-msg"  name="iaisErrorMsg" id="error_conveyanceHciName${status.index}"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Vehicle No." mandatory="true" width="4"/>
                        <iais:value width="7" cssClass="col-md-5">
                            <iais:input maxLength="10" type="text" name="conveyanceVehicleNo" id="vehicleNo" value="${appGrpPremisesDto.conveyanceVehicleNo}"></iais:input>
                            <span  class="error-msg"  name="iaisErrorMsg" id="error_conveyanceVehicleNo${status.index}"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row cssClass="postalCodeDiv">
                        <iais:field value="Postal Code" mandatory="true" width="4"/>
                        <iais:value cssClass="col-xs-10 col-md-5">
                            <iais:input maxLength="6" cssClass="postalCode" type="text" name="conveyancePostalCode"  value="${appGrpPremisesDto.conveyancePostalCode}"></iais:input>
                            <span  class="error-msg" name="iaisErrorMsg" id="error_conveyancePostalCode${status.index}"></span>
                        </iais:value>
                        <div class="col-xs-7 col-sm-6 col-md-3">
                            <p><a class="retrieveAddr <c:if test="${!canEdit || readOnly}">hidden</c:if>" id="conveyance">Retrieve your address</a></p>
                        </div>

                    </iais:row>
                    <iais:row>
                        <iais:field value="Address Type" mandatory="true" width="4"/>
                        <iais:value id="conveyanceAddrType${premValue}" cssClass="col-xs-7 col-sm-4 col-md-5 addressType">
                            <iais:select name="conveyanceAddrType" cssClass="conveyanceAddressType" id="siteAddressType" codeCategory="CATE_ID_ADDRESS_TYPE" needSort="false" firstOption="Please Select"  value="${appGrpPremisesDto.conveyanceAddressType}"></iais:select>
                            <span  class="error-msg" name="iaisErrorMsg" id="error_conveyanceAddressType${status.index}"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Block / House No." width="4"/>
                        <iais:value width="7" cssClass="col-xs-7 col-md-5">
                            <iais:input maxLength="10" cssClass="conveyanceBlkNo" type="text" name="conveyanceBlkNo" id="conveyanceBlkNo" value="${appGrpPremisesDto.conveyanceBlockNo}"></iais:input>
                            <span  class="error-msg" name="iaisErrorMsg" id="error_conveyanceBlockNos${status.index}"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Floor / Unit No." width="4"/>
                        <iais:value cssClass="col-xs-7 col-sm-4 col-md-5 ">
                            <div class="row">
                                <iais:value cssClass="col-xs-12 col-md-5 ">
                                    <iais:input maxLength="3" type="text" name="conveyanceFloorNo" id="conveyanceFloorNo" value="${appGrpPremisesDto.conveyanceFloorNo}"></iais:input>
                                    <span  class="error-msg" name="iaisErrorMsg" id="error_conveyanceFloorNo${status.index}"></span>
                                </iais:value>
                                <div class="col-xs-12 col-md-2 text-center"><p>-</p></div>
                                <iais:value cssClass="col-xs-12 col-md-5 ">
                                    <iais:input maxLength="5" type="text" name="conveyanceUnitNo"  value="${appGrpPremisesDto.conveyanceUnitNo}"></iais:input>
                                    <span  class="error-msg" name="iaisErrorMsg" id="error_conveyanceUnitNo${status.index}"></span>
                                </iais:value>
                            </div>
                        </iais:value>
                    </iais:row>
                    <div class="operationDivGroup">
                    <c:if test="${appGrpPremisesDto.appPremisesOperationalUnitDtos.size()>0}">
                        <c:forEach var="operationDto" items="${appGrpPremisesDto.appPremisesOperationalUnitDtos}" varStatus="opStat">
                            <div class="operationDiv">
                                <iais:row>
                                    <iais:field value="" width="4"/>
                                    <iais:value cssClass="col-xs-7 col-sm-4 col-md-5 ">
                                        <div class="row">
                                            <iais:value cssClass="col-xs-12 col-md-5 ">
                                                <iais:input cssClass="floorNo" maxLength="3" type="text" name="${premValue}conveyanceFloorNo${opStat.index}" value="${operationDto.floorNo}"></iais:input>
                                                <span class="error-msg" name="iaisErrorMsg" id="error_opConvFloorNo${premValue}${opStat.index}"></span>
                                            </iais:value>
                                            <div class="col-xs-12 col-md-2 text-center"><p>-</p></div>
                                            <iais:value cssClass="col-xs-12 col-md-5 ">
                                                <iais:input cssClass="unitNo" maxLength="5" type="text" name="${premValue}conveyanceUnitNo${opStat.index}" value="${operationDto.unitNo}"></iais:input>
                                                <span class="error-msg" name="iaisErrorMsg" id="error_opConvUnitNo${premValue}${opStat.index}"></span>
                                            </iais:value>
                                        </div>
                                        <span class="error-msg" name="iaisErrorMsg" id="error_ConvFloorUnit${premValue}${opStat.index}"></span>
                                    </iais:value>
                                    <div class=" col-xs-7 col-sm-4 col-md-2 ">
                                        <p>(Additional)&nbsp;&nbsp;&nbsp;&nbsp;</p>
                                    </div>
                                    <div class=" col-xs-7 col-sm-4 col-md-1 text-center">
                                        <p class="text-danger opDel"><em class="fa fa-times-circle del-size-36"></em></p>
                                    </div>
                                </iais:row>
                            </div>
                        </c:forEach>
                    </c:if>
                    <!--prem operational -->
                    <iais:row cssClass="addOpDiv">
                        <iais:field value="" width="4"/>
                        <iais:value cssClass="col-xs-7 col-sm-4 col-md-5 ">
                            <span class="addOperational"><a style="text-decoration:none;">+ Add Additional Floor/Unit No.</a></span>
                        </iais:value>
                    </iais:row>
                    </div>
                    <iais:row>
                        <iais:field value="Street Name" mandatory="true" width="4"/>
                        <iais:value width="5" cssClass="col-md-5">
                            <iais:input maxLength="32" cssClass="conveyanceStreetName" type="text" name="conveyanceStreetName"  value="${appGrpPremisesDto.conveyanceStreetName}"></iais:input>
                            <span  class="error-msg" name="iaisErrorMsg" id="error_conveyanceStreetName${status.index}"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Building Name" width="4"/>
                        <iais:value cssClass="col-xs-5 col-sm-7 col-md-5 ">
                            <iais:input maxLength="66" cssClass="conveyanceBuildingName" type="text" name="conveyanceBuildingName" id="conveyanceBuildingName" value="${appGrpPremisesDto.conveyanceBuildingName}"></iais:input>
                            <span  class="error-msg" name="iaisErrorMsg" id="error_conveyanceBuildingName${status.index}"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Email" mandatory="true" width="4"/>
                        <iais:value cssClass="col-xs-11 col-sm-7 col-md-5 ">
                            <iais:input maxLength="320" cssClass="conveyanceEmail" type="text" name="conveyanceEmail" id="conveyanceEmail" value="${appGrpPremisesDto.conveyanceEmail}"></iais:input>
                            <span  class="error-msg" name="iaisErrorMsg" id="error_conveyanceEmail${status.index}"></span>
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <div class="col-md-12 col-xs-12">
                            <label class="control-label">Operating Hours</label>
                        </div>
                        <div class="col-md-4 col-xs-4 hidden-xs hidden-sm">
                            <label class="control-label">Weekly <span class="mandatory">*</span></label>
                        </div>
                        <div class="col-md-3 col-xs-3 hidden-xs hidden-sm">
                            <label class="control-label">Start</label>
                        </div>
                        <div class="col-md-3 col-xs-3 hidden-xs hidden-sm">
                            <label class="control-label">End</label>
                        </div>
                        <div class="col-md-2 col-xs-2 hidden-xs hidden-sm">
                            <label class="control-label">24 Hours</label>
                        </div>
                    </iais:row>

                    <c:set var="weeklyList" value="${appGrpPremisesDto.weeklyDtoList}"/>
                    <div class="weeklyContent">
                        <c:choose>
                            <c:when test="${weeklyList.size()>0 && 'CONVEYANCE' == appGrpPremisesDto.premisesType}">
                                <c:forEach begin="0" end="${weeklyList.size()-1}" step="1" varStatus="weeklyStat">
                                    <c:set var="weekly" value="${weeklyList[weeklyStat.index]}"/>
                                    <iais:row cssClass="weeklyDiv">
                                        <c:if test="${weeklyStat.index>0}">
                                            <div class="col-md-12 col-xs-12 hidden-xs hidden-sm">
                                                <label class="control-label">Weekly <span class="mandatory">*</span></label>
                                            </div>
                                        </c:if>
                                        <div>
                                            <div class="col-md-4 col-xs-4 multi-sel-padding">
                                                <div class="row d-flex">
                                                    <div class="col-xs-12 visible-xs visible-sm">
                                                        <label class="control-label">Weekly <span class="mandatory">*</span></label>
                                                    </div>
                                                    <div class="col-md-12 multi-select col-xs-12">
                                                        <iais:select  name="${premValue}conveyanceWeekly${weeklyStat.index}" multiValues="${weekly.selectValList}" options="weeklyOpList"  multiSelect="true"/>
                                                    </div>
                                                    <div class="col-md-12 col-xs-12">
                                                        <span class="error-msg " name="iaisErrorMsg" id="error_conveyanceWeekly${status.index}${weeklyStat.index}"></span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-3 col-xs-3 start-div">
                                                <div class="row d-flex">
                                                    <div class="col-xs-12 visible-xs visible-sm">
                                                        <label class="control-label">Start</label>
                                                    </div>
                                                    <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                        <iais:select cssClass="WeeklyStartHH" name="${premValue}conveyanceWeeklyStartHH${weeklyStat.index}" options="premiseHours" value="${weekly.startFromHH}" firstOption="--"></iais:select>
                                                    </div>
                                                    <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                        (HH)
                                                    </div>
                                                    <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                        <iais:select cssClass="WeeklyStartMM" name="${premValue}conveyanceWeeklyStartMM${weeklyStat.index}" options="premiseMinute" value="${weekly.startFromMM}" firstOption="--"></iais:select>
                                                    </div>
                                                    <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                        (MM)
                                                    </div>
                                                    <div class="col-md-12 col-xs-12">
                                                        <span class="error-msg " name="iaisErrorMsg" id="error_conveyanceWeeklyStart${status.index}${weeklyStat.index}"></span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-3 col-xs-3 end-div">
                                                <div class="row d-flex">
                                                    <div class="col-xs-12 visible-xs visible-sm">
                                                        <label class="control-label">End</label>
                                                    </div>
                                                    <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                        <iais:select cssClass="WeeklyEndHH" name="${premValue}conveyanceWeeklyEndHH${weeklyStat.index}" options="premiseHours" value="${weekly.endToHH}" firstOption="--"></iais:select>
                                                    </div>
                                                    <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                        (HH)
                                                    </div>
                                                    <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                        <iais:select cssClass="WeeklyEndMM" name="${premValue}conveyanceWeeklyEndMM${weeklyStat.index}" options="premiseMinute" value="${weekly.endToMM}" firstOption="--"></iais:select>
                                                    </div>
                                                    <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                        (MM)
                                                    </div>
                                                    <div class="col-md-12 col-xs-12">
                                                        <span class="error-msg " name="iaisErrorMsg" id="error_conveyanceWeeklyEnd${status.index}${weeklyStat.index}"></span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-2 col-xs-2 all-day-div">
                                                <div class="row d-flex">
                                                    <div class="col-xs-12 visible-xs visible-sm">
                                                        <label class="control-label">24 Hours</label>
                                                    </div>
                                                    <div class="col-md-5 col-xs-5 text-center all-day-position">
                                                        <input class="form-check-input allDay" name="${premValue}conveyanceWeeklyAllDay${weeklyStat.index}"  type="checkbox" aria-invalid="false" value="true" <c:if test="${weekly.selectAllDay}">checked="checked"</c:if> >
                                                    </div>
                                                    <div class="col-md-5 col-xs-5">
                                                        <c:if test="${weeklyStat.index>0}">
                                                            <div class="fa fa-times-circle del-size-36 text-danger weeklyDel"></div>
                                                        </c:if>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-4 col-xs-4">
                                            </div>
                                            <div class="col-md-8 col-xs-8">
                                                <span class="error-msg " name="iaisErrorMsg" id="error_conveyanceWeeklyTime${status.index}${weeklyStat.index}"></span>
                                            </div>
                                        </div>
                                    </iais:row>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <c:set var="suffix" value="0"/>
                                <iais:row cssClass="weeklyDiv">
                                    <div>
                                        <div class="col-md-4 col-xs-4 multi-sel-padding">
                                            <div class="row d-flex">
                                                <div class="col-xs-12 visible-xs visible-sm">
                                                    <label class="control-label">Weekly <span class="mandatory">*</span></label>
                                                </div>
                                                <div class="col-md-12 multi-select col-xs-12">
                                                    <iais:select  name="${premValue}conveyanceWeekly${suffix}"  options="weeklyOpList"  multiSelect="true"/>
                                                </div>
                                                <div class="col-md-12 col-xs-12">
                                                    <span class="error-msg " name="iaisErrorMsg" id="error_conveyanceWeekly${status.index}${suffix}"></span>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-3 col-xs-3 start-div">
                                            <div class="row d-flex">
                                                <div class="col-xs-12 visible-xs visible-sm">
                                                    <label class="control-label">Start</label>
                                                </div>
                                                <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                    <iais:select cssClass="WeeklyStartHH" name="${premValue}conveyanceWeeklyStartHH${suffix}" options="premiseHours" value="" firstOption="--"></iais:select>
                                                </div>
                                                <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                    (HH)
                                                </div>
                                                <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                    <iais:select cssClass="WeeklyStartMM" name="${premValue}conveyanceWeeklyStartMM${suffix}" options="premiseMinute" value="" firstOption="--"></iais:select>
                                                </div>
                                                <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                    (MM)
                                                </div>
                                                <div class="col-md-12 col-xs-12">
                                                    <span class="error-msg " name="iaisErrorMsg" id="error_conveyanceWeeklyStart${status.index}${suffix}"></span>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-3 col-xs-3 end-div">
                                            <div class="row d-flex">
                                                <div class="col-xs-12 visible-xs visible-sm">
                                                    <label class="control-label">End</label>
                                                </div>
                                                <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                    <iais:select cssClass="WeeklyEndHH" name="${premValue}conveyanceWeeklyEndHH${suffix}" options="premiseHours" value="" firstOption="--"></iais:select>
                                                </div>
                                                <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                    (HH)
                                                </div>
                                                <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                    <iais:select cssClass="WeeklyEndMM" name="${premValue}conveyanceWeeklyEndMM${suffix}" options="premiseMinute" value="" firstOption="--"></iais:select>
                                                </div>
                                                <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                    (MM)
                                                </div>
                                                <div class="col-md-12 col-xs-12">
                                                    <span class="error-msg " name="iaisErrorMsg" id="error_conveyanceWeeklyEnd${status.index}${suffix}"></span>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-2 col-xs-2 all-day-div">
                                            <div class="row d-flex">
                                                <div class="col-xs-12 visible-xs visible-sm">
                                                    <label class="control-label">24 Hours</label>
                                                </div>
                                                <div class="col-md-5 col-xs-5 text-center all-day-position">
                                                    <input class="form-check-input allDay" name="${premValue}conveyanceWeeklyAllDay${suffix}"  type="checkbox" aria-invalid="false" value="true"  >
                                                </div>
                                                <div class="col-md-5 col-xs-5">
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </iais:row>
                            </c:otherwise>
                        </c:choose>

                        <div class="form-group addWeeklyDiv <c:if test="${weeklyList.size() >= weeklyCount}">hidden</c:if>">
                            <iais:value cssClass="col-xs-4 col-sm-4 col-md-4">
                                <a class="addWeekly" style="text-decoration:none;">+ Add</a>
                            </iais:value>
                            <iais:value cssClass="col-xs-8 col-sm-4 col-md-8">

                            </iais:value>
                        </div>
                    </div>

                    <c:set var="phList" value="${appGrpPremisesDto.phDtoList}"/>
                    <div class="pubHolDayContent">
                        <c:choose>
                            <c:when test="${phList.size()>0 && 'CONVEYANCE' == appGrpPremisesDto.premisesType}">
                                <c:forEach begin="0" end="${phList.size()-1}" step="1" varStatus="phyStat">
                                    <c:set var="ph" value="${phList[phyStat.index]}"/>
                                    <iais:row cssClass="pubHolidayDiv">
                                        <div class="col-md-12">
                                            <label class="control-label">Public Holiday</label>
                                        </div>
                                        <div>
                                            <div class="col-md-4 multi-sel-padding">
                                                <div class="row d-flex">
                                                    <div class="col-md-12 multi-select col-xs-12">
                                                        <iais:select name="${premValue}conveyancePubHoliday${phyStat.index}" multiValues="${ph.selectValList}" options="phOpList" multiSelect="true"  />
                                                    </div>
                                                    <div class="col-md-12 col-xs-12">
                                                        <span class="error-msg " name="iaisErrorMsg" id="error_conveyancePubHoliday${status.index}${phyStat.index}"></span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-3 col-xs-3 start-div">
                                                <div class="row d-flex">
                                                    <div class="col-sm-12 visible-xs visible-sm">
                                                        <label class="control-label">Start</label>
                                                    </div>
                                                    <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                        <iais:select cssClass="PhStartHH" name="${premValue}conveyancePhStartHH${phyStat.index}" options="premiseHours" value="${ph.startFromHH}" firstOption="--"></iais:select>
                                                    </div>
                                                    <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                        (HH)
                                                    </div>
                                                    <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                        <iais:select cssClass="PhStartMM" name="${premValue}conveyancePhStartMM${phyStat.index}" options="premiseMinute" value="${ph.startFromMM}" firstOption="--"></iais:select>
                                                    </div>
                                                    <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                        (MM)
                                                    </div>
                                                    <div class="col-md-12 col-xs-12">
                                                        <span class="error-msg " name="iaisErrorMsg" id="error_conveyancePhStart${status.index}${phyStat.index}"></span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-3 col-xs-3 end-div">
                                                <div class="row d-flex">
                                                    <div class="col-sm-12 visible-xs visible-sm">
                                                        <label class="control-label">End</label>
                                                    </div>
                                                    <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                        <iais:select cssClass="PhEndHH" name="${premValue}conveyancePhEndHH${phyStat.index}" options="premiseHours" value="${ph.endToHH}" firstOption="--"></iais:select>
                                                    </div>
                                                    <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                        (HH)
                                                    </div>
                                                    <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                        <iais:select cssClass="PhEndMM" name="${premValue}conveyancePhEndMM${phyStat.index}" options="premiseMinute" value="${ph.endToMM}" firstOption="--"></iais:select>
                                                    </div>
                                                    <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                        (MM)
                                                    </div>
                                                    <div class="col-md-12 col-xs-12">
                                                        <span class="error-msg " name="iaisErrorMsg" id="error_conveyancePhEnd${status.index}${phyStat.index}"></span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-2 col-xs-2 all-day-div">
                                                <div class="row d-flex">
                                                    <div class="col-sm-12 visible-xs visible-sm">
                                                        <label class="control-label">24 Hours</label>
                                                    </div>
                                                    <div class="col-md-5 col-xs-5 text-center all-day-position">
                                                        <input class="form-check-input allDay" name="${premValue}conveyancePhAllDay${phyStat.index}"  type="checkbox" aria-invalid="false" value="true" <c:if test="${ph.selectAllDay}">checked="checked"</c:if> >
                                                    </div>
                                                    <div class="col-md-5 col-xs-5">
                                                        <c:if test="${phyStat.index>0}">
                                                            <div class="fa fa-times-circle del-size-36 text-danger pubHolidayDel"></div>
                                                        </c:if>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-4 col-xs-4">
                                            </div>
                                            <div class="col-md-8 col-xs-8">
                                                <span class="error-msg " name="iaisErrorMsg" id="error_conveyancePhTime${status.index}${phyStat.index}"></span>
                                            </div>
                                        </div>
                                    </iais:row>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <c:set var="suffix" value="0"/>
                                <iais:row cssClass="pubHolidayDiv">
                                    <div class="col-md-12 col-xs-12">
                                        <label class="control-label">Public Holiday</label>
                                    </div>
                                    <div>
                                        <div class="col-md-4 col-xs-4 multi-sel-padding">
                                            <div class="row d-flex">
                                                <div class="col-md-12 multi-select col-xs-12">
                                                    <iais:select name="${premValue}conveyancePubHoliday${suffix}" options="phOpList" multiSelect="true"  />
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-3 col-xs-3 start-div">
                                            <div class="row d-flex">
                                                <div class="col-sm-12 visible-xs visible-sm">
                                                    <label class="control-label">Start</label>
                                                </div>
                                                <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                    <iais:select cssClass="PhStartHH" name="${premValue}conveyancePhStartHH${suffix}" options="premiseHours" value="" firstOption="--"></iais:select>
                                                </div>
                                                <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                    (HH)
                                                </div>
                                                <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                    <iais:select cssClass="PhStartMM" name="${premValue}conveyancePhStartMM${suffix}" options="premiseMinute" value="" firstOption="--"></iais:select>
                                                </div>
                                                <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                    (MM)
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-3 col-xs-3 end-div">
                                            <div class="row d-flex">
                                                <div class="col-sm-12 visible-xs visible-sm">
                                                    <label class="control-label">End</label>
                                                </div>
                                                <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                    <iais:select cssClass="PhEndHH" name="${premValue}conveyancePhEndHH${suffix}" options="premiseHours" value="" firstOption="--"></iais:select>
                                                </div>
                                                <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                    (HH)
                                                </div>
                                                <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                    <iais:select cssClass="PhEndMM" name="${premValue}conveyancePhEndMM${suffix}" options="premiseMinute" value="" firstOption="--"></iais:select>
                                                </div>
                                                <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                    (MM)
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-2 col-xs-2 all-day-div">
                                            <div class="row d-flex">
                                                <div class="col-sm-12 visible-xs visible-sm">
                                                    <label class="control-label">24 Hours</label>
                                                </div>
                                                <div class="col-md-5 col-xs-5 text-center all-day-position">
                                                    <input class="form-check-input allDay" name="${premValue}conveyancePhAllDay${suffix}"  type="checkbox" aria-invalid="false" value="true" >
                                                </div>
                                                <div class="col-md-5 col-xs-5">
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </iais:row>
                            </c:otherwise>
                        </c:choose>

                        <div class="form-group addPhDiv <c:if test="${phList.size() >= phCount}">hidden</c:if>">
                            <iais:value cssClass="col-xs-4 col-sm-4 col-md-4">
                                <a class="addPubHolDay" style="text-decoration:none;">+ Add</a>
                            </iais:value>
                            <iais:value cssClass="col-xs-8 col-sm-4 col-md-8">

                            </iais:value>
                        </div>
                    </div>

                    <c:set var="eventList" value="${appGrpPremisesDto.eventDtoList}"/>
                    <div class="eventContent">
                        <c:choose>
                            <c:when test="${eventList.size()>0 && 'CONVEYANCE' == appGrpPremisesDto.premisesType}">
                                <c:forEach begin="0" end="${eventList.size()-1}" step="1" varStatus="eventStat">
                                    <c:set var="event" value="${eventList[eventStat.index]}"/>
                                    <iais:row cssClass="eventDiv">
                                        <div class="col-md-12 col-xs-12">
                                            <label class="control-label">Event</label>
                                        </div>
                                        <div>
                                            <div class="col-md-4 col-xs-4">
                                                <div class="row">
                                                    <div class="col-md-12 col-xs-12">
                                                        <iais:input type="text" maxLength="100" cssClass="Event" name="${premValue}conveyanceEvent${eventStat.index}" value="${event.eventName}" />
                                                    </div>
                                                    <div class="col-md-12 col-xs-12">
                                                        <span class="error-msg " name="iaisErrorMsg" id="error_conveyanceEvent${status.index}${eventStat.index}"></span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-3 col-xs-3">
                                                <iais:datePicker cssClass="EventStart" name="${premValue}conveyanceEventStart${eventStat.index}" value="${event.startDateStr}" />
                                                <span class="error-msg " name="iaisErrorMsg" id="error_conveyanceEventStart${status.index}${eventStat.index}"></span>
                                            </div>
                                            <div class="col-md-3 col-xs-3">
                                                <iais:datePicker cssClass="EventEnd" name="${premValue}conveyanceEventEnd${eventStat.index}" value="${event.endDateStr}" />
                                                <span class="error-msg " name="iaisErrorMsg" id="error_conveyanceEventEnd${status.index}${eventStat.index}"></span>
                                            </div>
                                            <div class="col-md-2 col-xs-2">
                                                <div class="row">
                                                    <div class="col-md-6 text-center col-xs-6">
                                                    </div>
                                                    <div class="col-md-6 col-xs-6">
                                                        <c:if test="${eventStat.index>0}">
                                                            <div class="fa fa-times-circle del-size-36 text-danger eventDel"></div>
                                                        </c:if>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="">
                                            </div>
                                            <div class="col-md-8 col-xs-8">
                                                <span class="error-msg " name="iaisErrorMsg" id="error_conveyanceEventDate${status.index}${eventStat.index}"></span>
                                            </div>
                                        </div>
                                    </iais:row>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <c:set var="suffix" value="0"/>
                                <iais:row cssClass="eventDiv">
                                    <div class="col-md-12 col-xs-12">
                                        <label class="control-label">Event</label>
                                    </div>
                                    <div>
                                        <div class="col-md-4 col-xs-4">
                                            <div class="row">
                                                <div class="col-md-12 col-xs-12">
                                                    <iais:input maxLength="100" type="text" cssClass="Event" name="${premValue}conveyanceEvent${suffix}" value="" />
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-3 col-xs-3">
                                            <iais:datePicker cssClass="EventStart" name="${premValue}conveyanceEventStart${suffix}" value="" />
                                        </div>
                                        <div class="col-md-3 col-xs-3">
                                            <iais:datePicker cssClass="EventEnd" name="${premValue}conveyanceEventEnd${suffix}" value="" />
                                        </div>
                                        <div class="col-md-2 col-xs-2">
                                            <div class="row">
                                                <div class="col-md-6 text-center col-xs-6">
                                                </div>
                                                <div class="col-md-6 col-xs-6">
                                                    <c:if test="${eventStat.index>0}">
                                                        <div class="fa fa-times-circle del-size-36 text-danger eventDel"></div>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </iais:row>
                            </c:otherwise>
                        </c:choose>

                        <div class="form-group addEventDiv <c:if test="${eventList.size() >= eventCount}">hidden</c:if>">
                            <iais:value cssClass="col-xs-4 col-sm-4 col-md-4">
                                <a class="addEvent" style="text-decoration:none;">+ Add</a>
                            </iais:value>
                            <iais:value cssClass="col-xs-8 col-sm-4 col-md-8">

                            </iais:value>
                        </div>
                    </div>

                </div>
            </div>

            <div class="new-premise-form-off-site hidden">
                <div class="form-horizontal">
                    <iais:row>
                        <iais:field value="Business Name" mandatory="true" width="4"/>
                        <iais:value width="7" cssClass="col-xs-10 col-md-5">
                            <iais:input cssClass="hciName" maxLength="100" type="text" name="offSiteHciName" value="${appGrpPremisesDto.offSiteHciName}"></iais:input>
                            <span  class="error-msg"  name="iaisErrorMsg" id="error_offSiteHciName${status.index}"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row cssClass="postalCodeDiv">
                        <iais:field value="Postal Code" mandatory="true" width="4"/>
                        <iais:value cssClass="col-md-5">
                            <iais:input maxLength="6" cssClass="postalCode" type="text" name="offSitePostalCode"  value="${appGrpPremisesDto.offSitePostalCode}"></iais:input>
                            <span  class="error-msg" name="iaisErrorMsg" id="error_offSitePostalCode${status.index}"></span>
                        </iais:value>
                        <div class="col-xs-7 col-sm-6 col-md-3">
                            <p><a class="retrieveAddr <c:if test="${!canEdit || readOnly}">hidden</c:if>" id="offSite">Retrieve your address</a></p>
                        </div>

                    </iais:row>
                    <iais:row>
                        <iais:field value="Address Type" mandatory="true" width="4"/>
                        <iais:value id="offSiteAddrType${premValue}" cssClass="col-xs-7 col-sm-4 col-md-5 addressType">
                            <iais:select name="offSiteAddrType" cssClass="offSiteAddressType" id="offSiteAddressType" codeCategory="CATE_ID_ADDRESS_TYPE" needSort="false" firstOption="Please Select" value="${appGrpPremisesDto.offSiteAddressType}"></iais:select>
                            <span  class="error-msg" name="iaisErrorMsg" id="error_offSiteAddressType${status.index}"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Block / House No." width="4"/>
                        <iais:value width="7" cssClass="col-xs-7 col-md-5">
                            <iais:input maxLength="10" cssClass="offSiteBlkNo" type="text" name="offSiteBlkNo" id="offSiteBlkNo" value="${appGrpPremisesDto.offSiteBlockNo}"></iais:input>
                            <span  class="error-msg" name="iaisErrorMsg" id="error_offSiteBlockNo${status.index}"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Floor / Unit No." width="4"/>
                        <iais:value cssClass="col-xs-7 col-md-5">
                            <div class="row">
                                <iais:value cssClass="col-xs-12 col-md-5 ">
                                    <iais:input maxLength="3" type="text" name="offSiteFloorNo" id="offSiteFloorNo" value="${appGrpPremisesDto.offSiteFloorNo}"></iais:input>
                                    <span  class="error-msg" name="iaisErrorMsg" id="error_offSiteFloorNo${status.index}"></span>
                                </iais:value>
                                <div class="col-xs-12 col-md-2 text-center"><p>-</p></div>
                                <iais:value cssClass="col-xs-12 col-md-5 ">
                                    <iais:input maxLength="5" type="text" name="offSiteUnitNo"  value="${appGrpPremisesDto.offSiteUnitNo}"></iais:input>
                                    <span  class="error-msg" name="iaisErrorMsg" id="error_offSiteUnitNo${status.index}"></span>
                                </iais:value>
                            </div>
                        </iais:value>
                    </iais:row>
                    <div class="operationDivGroup">
                    <c:if test="${appGrpPremisesDto.appPremisesOperationalUnitDtos.size()>0}">
                        <c:forEach var="operationDto" items="${appGrpPremisesDto.appPremisesOperationalUnitDtos}" varStatus="opStat">
                            <div class="operationDiv">
                                <iais:row>
                                    <iais:field value="" width="4"/>
                                    <iais:value cssClass="col-xs-7 col-sm-4 col-md-5 ">
                                        <div class="row">
                                            <iais:value cssClass="col-xs-12 col-md-5 ">
                                                <iais:input cssClass="floorNo" maxLength="3" type="text" name="${premValue}offSiteFloorNo${opStat.index}" value="${operationDto.floorNo}"></iais:input>
                                                <span class="error-msg" name="iaisErrorMsg" id="error_opOffFloorNo${premValue}${opStat.index}"></span>
                                            </iais:value>
                                            <div class="col-xs-12 col-md-2 text-center"><p>-</p></div>
                                            <iais:value cssClass="col-xs-12 col-md-5 ">
                                                <iais:input cssClass="unitNo" maxLength="5" type="text" name="${premValue}offSiteUnitNo${opStat.index}" value="${operationDto.unitNo}"></iais:input>
                                                <span class="error-msg" name="iaisErrorMsg" id="error_opOffUnitNo${premValue}${opStat.index}"></span>
                                            </iais:value>
                                        </div>
                                        <span class="error-msg" name="iaisErrorMsg" id="error_offFloorUnit${premValue}${opStat.index}"></span>
                                    </iais:value>
                                    <div class=" col-xs-7 col-sm-4 col-md-2 ">
                                        <p>(Additional)&nbsp;&nbsp;&nbsp;&nbsp;</p>
                                    </div>
                                    <div class=" col-xs-7 col-sm-4 col-md-1 text-center">
                                        <p class="text-danger opDel"><em class="fa fa-times-circle del-size-36"></em></p>
                                    </div>
                                </iais:row>
                            </div>
                        </c:forEach>
                    </c:if>
                    <!--prem operational -->
                    <iais:row cssClass="addOpDiv">
                        <iais:field value="" width="4"/>
                        <iais:value cssClass="col-xs-7 col-sm-4 col-md-5 ">
                            <span class="addOperational"><a style="text-decoration:none;">+ Add Additional Floor/Unit No.</a></span>
                        </iais:value>
                    </iais:row>
                    </div>
                    <iais:row>
                        <iais:field value="Street Name" mandatory="true" width="4"/>
                        <iais:value width="5" cssClass="col-md-5">
                            <iais:input maxLength="32" cssClass="offSiteStreetName" type="text" name="offSiteStreetName"  value="${appGrpPremisesDto.offSiteStreetName}"></iais:input>
                            <span  class="error-msg" name="iaisErrorMsg" id="error_offSiteStreetName${status.index}"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Building Name" width="4"/>
                        <iais:value cssClass="col-xs-5 col-sm-7 col-md-5 ">
                            <iais:input maxLength="66" cssClass="offSiteBuildingName" type="text" name="offSiteBuildingName" id="offSiteBuildingName" value="${appGrpPremisesDto.offSiteBuildingName}"></iais:input>
                            <span  class="error-msg" name="iaisErrorMsg" id="error_offSiteBuildingName${status.index}"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Email" mandatory="true" width="4"/>
                        <iais:value cssClass="col-xs-11 col-sm-7 col-md-5 ">
                            <iais:input maxLength="320" cssClass="offSiteEmail" type="text" name="offSiteEmail" id="offSiteEmail" value="${appGrpPremisesDto.offSiteEmail}"></iais:input>
                            <span  class="error-msg" name="iaisErrorMsg" id="error_offSiteEmail${status.index}"></span>
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <div class="col-md-12 col-xs-12">
                            <label class="control-label">Operating Hours</label>
                        </div>
                        <div class="col-md-4 col-xs-4 hidden-xs hidden-sm">
                            <label class="control-label">Weekly <span class="mandatory">*</span></label>
                        </div>
                        <div class="col-md-3 col-xs-3 hidden-xs hidden-sm">
                            <label class="control-label">Start</label>
                        </div>
                        <div class="col-md-3 col-xs-3 hidden-xs hidden-sm">
                            <label class="control-label">End</label>
                        </div>
                        <div class="col-md-2 col-xs-2 hidden-xs hidden-sm">
                            <label class="control-label">24 Hours</label>
                        </div>
                    </iais:row>

                    <c:set var="weeklyList" value="${appGrpPremisesDto.weeklyDtoList}"/>
                    <div class="weeklyContent">
                        <c:choose>
                            <c:when test="${weeklyList.size()>0 && 'OFFSITE' == appGrpPremisesDto.premisesType}">
                                <c:forEach begin="0" end="${weeklyList.size()-1}" step="1" varStatus="weeklyStat">
                                    <c:set var="weekly" value="${weeklyList[weeklyStat.index]}"/>
                                    <iais:row cssClass="weeklyDiv">
                                        <c:if test="${weeklyStat.index>0}">
                                            <div class="col-md-12 col-xs-12 hidden-xs hidden-sm">
                                                <label class="control-label">Weekly <span class="mandatory">*</span></label>
                                            </div>
                                        </c:if>
                                        <div>
                                            <div class="col-md-4 col-xs-4 multi-sel-padding">
                                                <div class="row d-flex">
                                                    <div class="col-xs-12 visible-xs visible-sm">
                                                        <label class="control-label">Weekly <span class="mandatory">*</span></label>
                                                    </div>
                                                    <div class="col-md-12 multi-select col-xs-12">
                                                        <iais:select name="${premValue}offSiteWeekly${weeklyStat.index}" multiValues="${weekly.selectValList}" options="weeklyOpList" multiSelect="true"  />
                                                    </div>
                                                    <div class="col-md-12 col-xs-12">
                                                        <span class="error-msg " name="iaisErrorMsg" id="error_offSiteWeekly${status.index}${weeklyStat.index}"></span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-3 col-xs-3 start-div">
                                                <div class="row d-flex">
                                                    <div class="col-xs-12 visible-xs visible-sm">
                                                        <label class="control-label">Start</label>
                                                    </div>
                                                    <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                        <iais:select cssClass="WeeklyStartHH" name="${premValue}offSiteWeeklyStartHH${weeklyStat.index}" options="premiseHours" value="${weekly.startFromHH}" firstOption="--"></iais:select>
                                                    </div>
                                                    <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                        (HH)
                                                    </div>
                                                    <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                        <iais:select cssClass="WeeklyStartMM" name="${premValue}offSiteWeeklyStartMM${weeklyStat.index}" options="premiseMinute" value="${weekly.startFromMM}" firstOption="--"></iais:select>
                                                    </div>
                                                    <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                        (MM)
                                                    </div>
                                                    <div class="col-md-12 col-xs-12">
                                                        <span class="error-msg " name="iaisErrorMsg" id="error_offSiteWeeklyStart${status.index}${weeklyStat.index}"></span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-3 col-xs-3 end-div">
                                                <div class="row d-flex">
                                                    <div class="col-xs-12 visible-xs visible-sm">
                                                        <label class="control-label">End</label>
                                                    </div>
                                                    <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                        <iais:select cssClass="WeeklyEndHH" name="${premValue}offSiteWeeklyEndHH${weeklyStat.index}" options="premiseHours" value="${weekly.endToHH}" firstOption="--"></iais:select>
                                                    </div>
                                                    <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                        (HH)
                                                    </div>
                                                    <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                        <iais:select cssClass="WeeklyEndMM" name="${premValue}offSiteWeeklyEndMM${weeklyStat.index}" options="premiseMinute" value="${weekly.endToMM}" firstOption="--"></iais:select>
                                                    </div>
                                                    <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                        (MM)
                                                    </div>
                                                    <div class="col-md-12 col-xs-12">
                                                        <span class="error-msg " name="iaisErrorMsg" id="error_offSiteWeeklyEnd${status.index}${weeklyStat.index}"></span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-2 col-xs-2 all-day-div">
                                                <div class="row d-flex">
                                                    <div class="col-xs-12 visible-xs visible-sm">
                                                        <label class="control-label">24 Hours</label>
                                                    </div>
                                                    <div class="col-md-5 col-xs-5 text-center all-day-position">
                                                        <input class="form-check-input allDay" name="${premValue}offSiteWeeklyAllDay${weeklyStat.index}"  type="checkbox" aria-invalid="false" value="true" <c:if test="${weekly.selectAllDay}">checked="checked"</c:if> >
                                                    </div>
                                                    <div class="col-md-5 col-xs-5">
                                                        <c:if test="${weeklyStat.index>0}">
                                                            <div class="fa fa-times-circle del-size-36 text-danger weeklyDel"></div>
                                                        </c:if>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-4 col-xs-4">
                                            </div>
                                            <div class="col-md-8 col-xs-8">
                                                <span class="error-msg " name="iaisErrorMsg" id="error_offSiteWeeklyTime${status.index}${weeklyStat.index}"></span>
                                            </div>
                                        </div>
                                    </iais:row>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <c:set var="suffix" value="0"/>
                                <iais:row cssClass="weeklyDiv">
                                    <div>
                                        <div class="col-md-4 col-xs-4 multi-sel-padding">
                                            <div class="row d-flex">
                                                <div class="col-xs-12 visible-xs visible-sm">
                                                    <label class="control-label">Weekly <span class="mandatory">*</span></label>
                                                </div>
                                                <div class="col-md-12 multi-select col-xs-12">
                                                    <iais:select name="${premValue}offSiteWeekly${suffix}" options="weeklyOpList" multiSelect="true"  />
                                                </div>
                                                <div class="col-md-12 col-xs-12">
                                                    <span class="error-msg " name="iaisErrorMsg" id="error_offSiteWeekly${status.index}${suffix}"></span>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-3 col-xs-3 start-div">
                                            <div class="row d-flex">
                                                <div class="col-xs-12 visible-xs visible-sm">
                                                    <label class="control-label">Start</label>
                                                </div>
                                                <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                    <iais:select cssClass="WeeklyStartHH" name="${premValue}offSiteWeeklyStartHH${suffix}" options="premiseHours" value="" firstOption="--"></iais:select>
                                                </div>
                                                <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                    (HH)
                                                </div>
                                                <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                    <iais:select cssClass="WeeklyStartMM" name="${premValue}offSiteWeeklyStartMM${suffix}" options="premiseMinute" value="" firstOption="--"></iais:select>
                                                </div>
                                                <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                    (MM)
                                                </div>
                                                <div class="col-md-12 col-xs-12">
                                                    <span class="error-msg " name="iaisErrorMsg" id="error_offSiteWeeklyStart${status.index}${suffix}"></span>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-3 col-xs-3 end-div">
                                            <div class="row d-flex">
                                                <div class="col-xs-12 visible-xs visible-sm">
                                                    <label class="control-label">End</label>
                                                </div>
                                                <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                    <iais:select cssClass="WeeklyEndHH" name="${premValue}offSiteWeeklyEndHH${suffix}" options="premiseHours" value="" firstOption="--"></iais:select>
                                                </div>
                                                <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                    (HH)
                                                </div>
                                                <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                    <iais:select cssClass="WeeklyEndMM" name="${premValue}offSiteWeeklyEndMM${suffix}" options="premiseMinute" value="" firstOption="--"></iais:select>
                                                </div>
                                                <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                    (MM)
                                                </div>
                                                <div class="col-md-12 col-xs-12">
                                                    <span class="error-msg " name="iaisErrorMsg" id="error_offSiteWeeklyEnd${status.index}${suffix}"></span>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-2 col-xs-2 all-day-div">
                                            <div class="row">
                                                <div class="col-xs-12 visible-xs visible-sm">
                                                    <label class="control-label">24 Hours</label>
                                                </div>
                                                <div class="col-md-5 col-xs-5 text-center all-day-position">
                                                    <input class="form-check-input allDay" name="${premValue}offSiteWeeklyAllDay${suffix}"  type="checkbox" aria-invalid="false" value="true"  >
                                                </div>
                                                <div class="col-md-5 col-xs-5">
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </iais:row>
                            </c:otherwise>
                        </c:choose>

                        <div class="form-group addWeeklyDiv <c:if test="${weeklyList.size() >= weeklyCount}">hidden</c:if>">
                            <iais:value cssClass="col-xs-4 col-sm-4 col-md-4">
                                <a class="addWeekly" style="text-decoration:none;">+ Add</a>
                            </iais:value>
                            <iais:value cssClass="col-xs-8 col-sm-4 col-md-8">

                            </iais:value>
                        </div>
                    </div>

                    <c:set var="phList" value="${appGrpPremisesDto.phDtoList}"/>
                    <div class="pubHolDayContent">
                        <c:choose>
                            <c:when test="${phList.size()>0 && 'OFFSITE' == appGrpPremisesDto.premisesType}">
                                <c:forEach begin="0" end="${phList.size()-1}" step="1" varStatus="phyStat">
                                    <c:set var="ph" value="${phList[phyStat.index]}"/>
                                    <iais:row cssClass="pubHolidayDiv">
                                        <div class="col-md-12">
                                            <label class="control-label">Public Holiday</label>
                                        </div>
                                        <div>
                                            <div class="col-md-4 multi-sel-padding">
                                                <div class="row d-flex">
                                                    <div class="col-md-12 multi-select col-xs-12">
                                                        <iais:select name="${premValue}offSitePubHoliday${phyStat.index}" multiValues="${ph.selectValList}" options="phOpList" multiSelect="true"  />
                                                    </div>
                                                    <div class="col-md-12 col-xs-12">
                                                        <span class="error-msg " name="iaisErrorMsg" id="error_offSitePubHoliday${status.index}${phyStat.index}"></span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-3 col-xs-3 start-div">
                                                <div class="row d-flex">
                                                    <div class="col-sm-12 visible-xs visible-sm">
                                                        <label class="control-label">Start</label>
                                                    </div>
                                                    <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                        <iais:select cssClass="PhStartHH" name="${premValue}offSitePhStartHH${phyStat.index}" options="premiseHours" value="${ph.startFromHH}" firstOption="--"></iais:select>
                                                    </div>
                                                    <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                        (HH)
                                                    </div>
                                                    <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                        <iais:select cssClass="PhStartMM" name="${premValue}offSitePhStartMM${phyStat.index}" options="premiseMinute" value="${ph.startFromMM}" firstOption="--"></iais:select>
                                                    </div>
                                                    <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                        (MM)
                                                    </div>
                                                    <div class="col-md-12 col-xs-12">
                                                        <span class="error-msg " name="iaisErrorMsg" id="error_offSitePhStart${status.index}${phyStat.index}"></span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-3 col-xs-3 end-div">
                                                <div class="row d-flex">
                                                    <div class="col-sm-12 visible-xs visible-sm">
                                                        <label class="control-label">End</label>
                                                    </div>
                                                    <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                        <iais:select cssClass="PhEndHH" name="${premValue}offSitePhEndHH${phyStat.index}" options="premiseHours" value="${ph.endToHH}" firstOption="--"></iais:select>
                                                    </div>
                                                    <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                        (HH)
                                                    </div>
                                                    <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                        <iais:select cssClass="PhEndMM" name="${premValue}offSitePhEndMM${phyStat.index}" options="premiseMinute" value="${ph.endToMM}" firstOption="--"></iais:select>
                                                    </div>
                                                    <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                        (MM)
                                                    </div>
                                                    <div class="col-md-12 col-xs-12">
                                                        <span class="error-msg " name="iaisErrorMsg" id="error_offSitePhEnd${status.index}${phyStat.index}"></span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-2 col-xs-2 all-day-div">
                                                <div class="row d-flex">
                                                    <div class="col-sm-12 visible-xs visible-sm">
                                                        <label class="control-label">24 Hours</label>
                                                    </div>
                                                    <div class="col-md-5 col-xs-5 text-center all-day-position">
                                                        <input class="form-check-input allDay" name="${premValue}offSitePhAllDay${phyStat.index}"  type="checkbox" aria-invalid="false" value="true" <c:if test="${ph.selectAllDay}">checked="checked"</c:if> >
                                                    </div>
                                                    <div class="col-md-5 col-xs-5">
                                                        <c:if test="${phyStat.index>0}">
                                                            <div class="fa fa-times-circle del-size-36 text-danger pubHolidayDel"></div>
                                                        </c:if>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-4 col-xs-4">
                                            </div>
                                            <div class="col-md-8 col-xs-8">
                                                <span class="error-msg " name="iaisErrorMsg" id="error_offSitePhTime${status.index}${phyStat.index}"></span>
                                            </div>
                                        </div>
                                    </iais:row>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <c:set var="suffix" value="0"/>
                                <iais:row cssClass="pubHolidayDiv">
                                    <div class="col-md-12 col-xs-12">
                                        <label class="control-label">Public Holiday</label>
                                    </div>
                                    <div>
                                        <div class="col-md-4 col-xs-4 multi-sel-padding">
                                            <div class="row d-flex">
                                                <div class="col-md-12 multi-select col-xs-12">
                                                    <iais:select name="${premValue}offSitePubHoliday${suffix}"  options="phOpList" multiSelect="true"  />
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-3 col-xs-3 start-div">
                                            <div class="row d-flex">
                                                <div class="col-sm-12 visible-xs visible-sm">
                                                    <label class="control-label">Start</label>
                                                </div>
                                                <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                    <iais:select cssClass="PhStartHH" name="${premValue}offSitePhStartHH${suffix}" options="premiseHours" value="" firstOption="--"></iais:select>
                                                </div>
                                                <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                    (HH)
                                                </div>
                                                <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                    <iais:select cssClass="PhStartMM" name="${premValue}offSitePhStartMM${suffix}" options="premiseMinute" value="" firstOption="--"></iais:select>
                                                </div>
                                                <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                    (MM)
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-3 col-xs-3 end-div">
                                            <div class="row d-flex">
                                                <div class="col-sm-12 visible-xs visible-sm">
                                                    <label class="control-label">End</label>
                                                </div>
                                                <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                    <iais:select cssClass="PhEndHH" name="${premValue}offSitePhEndHH${suffix}" options="premiseHours" value="" firstOption="--"></iais:select>
                                                </div>
                                                <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                    (HH)
                                                </div>
                                                <div class="col-md-10 col-lg-5 col-9 input-padding">
                                                    <iais:select cssClass="PhEndMM" name="${premValue}offSitePhEndMM${suffix}" options="premiseMinute" value="" firstOption="--"></iais:select>
                                                </div>
                                                <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                    (MM)
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-2 col-xs-2 all-day-div">
                                            <div class="row d-flex">
                                                <div class="col-sm-12 visible-xs visible-sm">
                                                    <label class="control-label">24 Hours</label>
                                                </div>
                                                <div class="col-md-5 col-xs-5 text-center all-day-position">
                                                    <input class="form-check-input allDay" name="${premValue}offSitePhAllDay${suffix}"  type="checkbox" aria-invalid="false" value="true" >
                                                </div>
                                                <div class="col-md-5 col-xs-5">
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </iais:row>
                            </c:otherwise>
                        </c:choose>

                        <div class="form-group addPhDiv <c:if test="${phList.size() >= phCount}">hidden</c:if>">
                            <iais:value cssClass="col-xs-4 col-sm-4 col-md-4">
                                <a class="addPubHolDay" style="text-decoration:none;">+ Add</a>
                            </iais:value>
                            <iais:value cssClass="col-xs-8 col-sm-4 col-md-8">

                            </iais:value>
                        </div>
                    </div>

                    <c:set var="eventList" value="${appGrpPremisesDto.eventDtoList}"/>
                    <div class="eventContent">
                        <c:choose>
                            <c:when test="${eventList.size()>0 && 'OFFSITE' == appGrpPremisesDto.premisesType}">
                                <c:forEach begin="0" end="${eventList.size()-1}" step="1" varStatus="eventStat">
                                    <c:set var="event" value="${eventList[eventStat.index]}"/>
                                    <iais:row cssClass="eventDiv">
                                        <div class="col-md-12 col-xs-12">
                                            <label class="control-label">Event</label>
                                        </div>
                                        <div>
                                            <div class="col-md-4 col-xs-4">
                                                <div class="row">
                                                    <div class="col-md-12 col-xs-12">
                                                        <iais:input type="text" maxLength="100" cssClass="Event" name="${premValue}offSiteEvent${eventStat.index}" value="${event.eventName}" />
                                                    </div>
                                                    <div class="col-md-12 col-xs-12">
                                                        <span class="error-msg " name="iaisErrorMsg" id="error_offSiteEvent${status.index}${eventStat.index}"></span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-3 col-xs-3">
                                                <iais:datePicker cssClass="EventStart" name="${premValue}offSiteEventStart${eventStat.index}" value="${event.startDateStr}" />
                                                <span class="error-msg " name="iaisErrorMsg" id="error_offSiteEventStart${status.index}${eventStat.index}"></span>
                                            </div>
                                            <div class="col-md-3 col-xs-3">
                                                <iais:datePicker cssClass="EventEnd" name="${premValue}offSiteEventEnd${eventStat.index}" value="${event.endDateStr}" />
                                                <span class="error-msg " name="iaisErrorMsg" id="error_offSiteEventEnd${status.index}${eventStat.index}"></span>
                                            </div>
                                            <div class="col-md-2 col-xs-2">
                                                <div class="row">
                                                    <div class="col-md-6 text-center col-xs-6">
                                                    </div>
                                                    <div class="col-md-6 col-xs-6">
                                                        <c:if test="${eventStat.index>0}">
                                                            <div class="fa fa-times-circle del-size-36 text-danger eventDel"></div>
                                                        </c:if>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="">
                                            </div>
                                            <div class="col-md-8 col-xs-8">
                                                <span class="error-msg " name="iaisErrorMsg" id="error_offSiteEventDate${status.index}${eventStat.index}"></span>
                                            </div>
                                        </div>
                                    </iais:row>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <c:set var="suffix" value="0"/>
                                <iais:row cssClass="eventDiv">
                                    <div class="col-md-12 col-xs-12">
                                        <label class="control-label">Event</label>
                                    </div>
                                    <div>
                                        <div class="col-md-4 col-xs-4">
                                            <div class="row">
                                                <div class="col-md-12 col-xs-12">
                                                    <iais:input maxLength="100" type="text" cssClass="Event" name="${premValue}offSiteEvent${suffix}" value="" />
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-3 col-xs-3">
                                            <iais:datePicker cssClass="EventStart" name="${premValue}offSiteEventStart${suffix}" value="" />
                                        </div>
                                        <div class="col-md-3 col-xs-3">
                                            <iais:datePicker cssClass="EventEnd" name="${premValue}offSiteEventEnd${suffix}" value="" />
                                        </div>
                                        <div class="col-md-2 col-xs-2">
                                            <div class="row">
                                                <div class="col-md-6 text-center col-xs-6">
                                                </div>
                                                <div class="col-md-6 col-xs-6">
                                                    <c:if test="${eventStat.index>0}">
                                                        <div class="fa fa-times-circle del-size-36 text-danger eventDel"></div>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </iais:row>
                            </c:otherwise>
                        </c:choose>

                        <div class="form-group addEventDiv <c:if test="${eventList.size() >= eventCount}">hidden</c:if>">
                            <iais:value cssClass="col-xs-4 col-sm-4 col-md-4">
                                <a class="addEvent" style="text-decoration:none;">+ Add</a>
                            </iais:value>
                            <iais:value cssClass="col-xs-8 col-sm-4 col-md-8">

                            </iais:value>
                        </div>
                    </div>

                </div>
            </div>
            <c:set var="easOrMts" value="true" />
            <c:forEach var="service" items="${hcsaServiceDtoList}">
                <c:if test="${'true' == easOrMts && 'EAS' != service.svcCode && 'MTS' != service.svcCode}">
                    <c:set var="easOrMts" value="false" />
                </c:if>
            </c:forEach>
            <c:if test="${'true' == easOrMts}">
                <div class="new-premise-form-eas-mts hidden">
                    <div class="form-horizontal">
                        <iais:row>
                            <iais:field value="Business Name" mandatory="true" width="4"/>
                            <iais:value width="7" cssClass="col-xs-10 col-md-5">
                                <iais:input cssClass="hciName" maxLength="100" type="text" name="easMtsHciName" value="${appGrpPremisesDto.easMtsHciName}"></iais:input>
                                <span  class="error-msg"  name="iaisErrorMsg" id="error_easMtsHciName${status.index}"></span>
                            </iais:value>
                        </iais:row>
                        <iais:row cssClass="postalCodeDiv">
                            <iais:field value="Postal Code" mandatory="true" width="4"/>
                            <iais:value cssClass="col-xs-10 col-md-5">
                                <iais:input maxLength="6" cssClass="postalCode" type="text" name="easMtsPostalCode"  value="${appGrpPremisesDto.easMtsPostalCode}"></iais:input>
                                <span  class="error-msg" name="iaisErrorMsg" id="error_easMtsPostalCode${status.index}"></span>
                            </iais:value>
                            <div class="col-xs-7 col-sm-6 col-md-3">
                                <p><a class="retrieveAddr <c:if test="${!canEdit || readOnly}">hidden</c:if>" id="easMts">Retrieve your address</a></p>
                            </div>

                        </iais:row>
                        <iais:row>
                            <iais:field value="Address Type" mandatory="true" width="4"/>
                            <iais:value id="easMtsAddrType${premValue}" width="7" cssClass="col-md-5">
                                <iais:select name="easMtsAddrType" cssClass="easMtsAddressType" id="easMtsAddressType" codeCategory="CATE_ID_ADDRESS_TYPE" needSort="false" firstOption="Please Select" value="${appGrpPremisesDto.easMtsAddressType}"></iais:select>
                                <span  class="error-msg" name="iaisErrorMsg" id="error_easMtsAddressType${status.index}"></span>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Block / House No." width="4"/>
                            <iais:value width="7" cssClass="col-xs-7 col-md-5">
                                <iais:input maxLength="10" cssClass="easMtsBlkNo" type="text" name="easMtsBlkNo" id="easMtsBlkNo" value="${appGrpPremisesDto.easMtsBlockNo}"></iais:input>
                                <span  class="error-msg" name="iaisErrorMsg" id="error_easMtsBlockNo${status.index}"></span>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Floor / Unit No." width="4"/>
                            <iais:value cssClass="col-xs-7 col-sm-4 col-md-5 ">
                                <div class="row">
                                    <iais:value cssClass="col-xs-12 col-md-5 ">
                                        <iais:input maxLength="3" type="text" name="easMtsFloorNo" id="easMtsFloorNo" value="${appGrpPremisesDto.easMtsFloorNo}"></iais:input>
                                        <span  class="error-msg" name="iaisErrorMsg" id="error_easMtsFloorNo${status.index}"></span>
                                    </iais:value>
                                    <div class="col-xs-12 col-md-2 text-center"><p>-</p></div>
                                    <iais:value cssClass="col-xs-12 col-md-5 ">
                                        <iais:input maxLength="5" type="text" name="easMtsUnitNo"  value="${appGrpPremisesDto.easMtsUnitNo}"></iais:input>
                                        <span  class="error-msg" name="iaisErrorMsg" id="error_easMtsUnitNo${status.index}"></span>
                                    </iais:value>
                                </div>
                            </iais:value>
                        </iais:row>
                        <div class="operationDivGroup">
                            <c:if test="${appGrpPremisesDto.appPremisesOperationalUnitDtos.size()>0}">
                                <c:forEach var="operationDto" items="${appGrpPremisesDto.appPremisesOperationalUnitDtos}" varStatus="opStat">
                                    <div class="operationDiv">
                                        <iais:row>
                                            <iais:field value="" width="4"/>
                                            <iais:value cssClass="col-xs-7 col-sm-4 col-md-5 ">
                                                <div class="row">
                                                    <iais:value cssClass="col-xs-12 col-md-5 ">
                                                        <iais:input cssClass="floorNo" maxLength="3" type="text" name="${premValue}easMtsFloorNo${opStat.index}" value="${operationDto.floorNo}"></iais:input>
                                                        <span class="error-msg" name="iaisErrorMsg" id="error_opEasMtsFloorNo${premValue}${opStat.index}"></span>
                                                    </iais:value>
                                                    <div class="col-xs-12 col-md-2 text-center"><p>-</p></div>
                                                    <iais:value cssClass="col-xs-12 col-md-5 ">
                                                        <iais:input cssClass="unitNo" maxLength="5" type="text" name="${premValue}easMtsUnitNo${opStat.index}" value="${operationDto.unitNo}"></iais:input>
                                                        <span class="error-msg" name="iaisErrorMsg" id="error_opEasMtsUnitNo${premValue}${opStat.index}"></span>
                                                    </iais:value>
                                                </div>
                                                <span class="error-msg" name="iaisErrorMsg" id="error_EasMtsFloorUnit${premValue}${opStat.index}"></span>
                                            </iais:value>
                                            <div class=" col-xs-7 col-sm-4 col-md-2 ">
                                                <p>(Additional)&nbsp;&nbsp;&nbsp;&nbsp;</p>
                                            </div>
                                            <div class=" col-xs-7 col-sm-4 col-md-1 text-center">
                                                <p class="text-danger opDel"><em class="fa fa-times-circle del-size-36"></em></p>
                                            </div>
                                        </iais:row>
                                    </div>
                                </c:forEach>
                            </c:if>
                            <!--prem operational -->
                            <iais:row cssClass="addOpDiv">
                                <iais:field value="" width="4"/>
                                <iais:value cssClass="col-xs-7 col-sm-4 col-md-5 ">
                                    <span class="addOperational"><a style="text-decoration:none;">+ Add Additional Floor/Unit No.</a></span>
                                </iais:value>
                            </iais:row>
                        </div>
                        <iais:row>
                            <iais:field value="Street Name" mandatory="true" width="4"/>
                            <iais:value width="5" cssClass="col-xs-7 col-sm-7 col-md-5 ">
                                <iais:input maxLength="32" cssClass="easMtsStreetName" type="text" name="easMtsStreetName"  value="${appGrpPremisesDto.easMtsStreetName}"></iais:input>
                                <span  class="error-msg" name="iaisErrorMsg" id="error_easMtsStreetName${status.index}"></span>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Building Name" width="4"/>
                            <iais:value width="5" cssClass="col-md-5">
                                <iais:input maxLength="66" cssClass="easMtsBuildingName" type="text" name="easMtsBuildingName" id="easMtsBuildingName" value="${appGrpPremisesDto.easMtsBuildingName}"></iais:input>
                                <span  class="error-msg" name="iaisErrorMsg" id="error_easMtsBuildingName${status.index}"></span>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="For public/in-house use only?" mandatory="true" width="4"/>
                            <iais:value width="11" cssClass="col-md-7" style="margin-left:-1%;">
                                <input type="hidden" name="easMtsUseOnlyVal" value="${appGrpPremisesDto.easMtsUseOnly}"/>
                                <div class="form-check col-sm-4">
                                    <input <c:if test="${'UOT001'==appGrpPremisesDto.easMtsUseOnly}">checked="checked"</c:if> class="form-check-input useType public-use"  type="radio" name="easMtsUseOnly${status.index}" value = "UOT001" aria-invalid="false">
                                    <label class="form-check-label" ><span class="check-circle"></span><iais:code code="UOT001"/></label>
                                </div>
                                <div class="form-check col-sm-6">
                                    <input <c:if test="${'UOT002'==appGrpPremisesDto.easMtsUseOnly}">checked="checked"</c:if> class="form-check-input useType in-house-use"  type="radio" name="easMtsUseOnly${status.index}" value = "UOT002" aria-invalid="false">
                                    <label class="form-check-label" ><span class="check-circle"></span><iais:code code="UOT002"/></label>
                                </div>
                                <div class="col-sm-12">
                                    <span  class="error-msg" name="iaisErrorMsg" id="error_easMtsUseOnly${status.index}"></span>
                                </div>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Public Email" mandatory="true" width="4"/>
                            <iais:value width="7" cssClass="col-md-5">
                                <iais:input maxLength="66" cssClass="easMtsPubEmail" type="text" name="easMtsPubEmail"  value="${appGrpPremisesDto.easMtsPubEmail}"></iais:input>
                                <span  class="error-msg" name="iaisErrorMsg" id="error_easMtsPubEmail${status.index}"></span>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Public Hotline" mandatory="true" width="4"/>
                            <iais:value width="7" cssClass="col-md-5">
                                <iais:input maxLength="8" cssClass="easMtsPubHotline" type="text" name="easMtsPubHotline"  value="${appGrpPremisesDto.easMtsPubHotline}"></iais:input>
                                <span  class="error-msg" name="iaisErrorMsg" id="error_easMtsPubHotline${status.index}"></span>
                            </iais:value>
                        </iais:row>
                    </div>
                </div>
            </c:if>
        </div>
    </div>
</c:forEach>
<script >

    var cl = function(){
        $("select[name='onSiteAddressType']").change(function () {
            if('ADDTY001'==$(this).val()){
                if( $(this).parent().parent().next().children("label").children().length<1){
                    $(this).parent().parent().next().children("label").append("<span class=\"mandatory\">*</span>");
                    $(this).parent().parent().next().next().children("label").append("<span class=\"mandatory\">*</span>");
                    $(this).parent().parent().next().next().next().children("label").append("<span class=\"mandatory\">*</span>");
                }
            }else if('ADDTY002'==$( this).val()) {
                $(this).parent().parent().next().children("label").children().remove();
                $(this).parent().parent().next().next().children("label").children().remove();
                $(this).parent().parent().next().next().next().children("label").children().remove();
            }
        });

        $("select[name='conveyanceAddrType']").change(function () {
            if('ADDTY001'==$( this).val()){
                if( $(this).parent().parent().next().children("label").children().length<1){
                    $(this).parent().parent().next().children("label").append("<span class=\"mandatory\">*</span>");
                    $(this).parent().parent().next().next().children("label").append("<span class=\"mandatory\">*</span>");
                    $(this).parent().parent().next().next().next().children("label").append("<span class=\"mandatory\">*</span>");
                }
            }else if('ADDTY002'==$( this).val()) {
                $(this).parent().parent().next().children("label").children().remove();
                $(this).parent().parent().next().next().children("label").children().remove();
                $(this).parent().parent().next().next().next().children("label").children().remove();
            }
        });

        $("select[name='offSiteAddrType']").change(function () {
            if('ADDTY001'==$( this).val()){
                if( $(this).parent().parent().next().children("label").children().length<1){
                    $(this).parent().parent().next().children("label").append("<span class=\"mandatory\">*</span>");
                    $(this).parent().parent().next().next().children("label").append("<span class=\"mandatory\">*</span>");
                    $(this).parent().parent().next().next().next().children("label").append("<span class=\"mandatory\">*</span>");
                }
            }else if('ADDTY002'==$( this).val()) {
                $(this).parent().parent().next().children("label").children().remove();
                $(this).parent().parent().next().next().children("label").children().remove();
                $(this).parent().parent().next().next().next().children("label").children().remove();
            }
        });
        $("select[name='easMtsAddrType']").change(function (){
            if('ADDTY001'==$( this).val()){
                if( $(this).parent().parent().next().children("label").children().length<1){
                    $(this).parent().parent().next().children("label").append("<span class=\"mandatory\">*</span>");
                    $(this).parent().parent().next().next().children("label").append("<span class=\"mandatory\">*</span>");
                    $(this).parent().parent().next().next().next().children("label").append("<span class=\"mandatory\">*</span>");
                }
            }else if('ADDTY002'==$( this).val()) {
                $(this).parent().parent().next().children("label").children().remove();
                $(this).parent().parent().next().next().children("label").children().remove();
                $(this).parent().parent().next().next().next().children("label").children().remove();
            }
        });
    }

  var preperChange =  function(){
        if($("select[name='onSiteAddressType']").val()=='ADDTY001'){
            if($(this).parent().parent().next().children("label").children().length<1){
                $(this).parent().parent().next().children("label").append("<span class=\"mandatory\">*</span>");
                $(this).parent().parent().next().next().children("label").append("<span class=\"mandatory\">*</span>");
                $(this).parent().parent().next().next().next().children("label").append("<span class=\"mandatory\">*</span>");
            }
        }else if($("select[name='onSiteAddressType']").val()=='ADDTY002'){
            $(this).parent().parent().next().children("label").children().remove();
            $(this).parent().parent().next().next().children("label").children().remove();
            $(this).parent().parent().next().next().next().children("label").children().remove();
        };

        if($("select[name='conveyanceAddrType']").val()=='ADDTY001'){
            if( $(this).parent().parent().next().children("label").children().length<1){
                $(this).parent().parent().next().children("label").append("<span class=\"mandatory\">*</span>");
                $(this).parent().parent().next().next().children("label").append("<span class=\"mandatory\">*</span>");
                $(this).parent().parent().next().next().next().children("label").append("<span class=\"mandatory\">*</span>");
            }
        }else if($("select[name='conveyanceAddrType']").val()=='ADDTY002'){
            $(this).parent().parent().next().children("label").children().remove();
            $(this).parent().parent().next().next().children("label").children().remove();
            $(this).parent().parent().next().next().next().children("label").children().remove();
        };
        if($("select[name='offSiteAddrType']").val()=='ADDTY001'){
            if($(this).parent().parent().next().children("label").children().length<1){
                $(this).parent().parent().next().children("label").append("<span class=\"mandatory\">*</span>");
                $(this).parent().parent().next().next().children("label").append("<span class=\"mandatory\">*</span>");
                $(this).parent().parent().next().next().next().children("label").append("<span class=\"mandatory\">*</span>");
            }
        }else if($("select[name='offSiteAddrType']").val()=='ADDTY002'){
            $(this).parent().parent().next().children("label").children().remove();
            $(this).parent().parent().next().next().children("label").children().remove();
            $(this).parent().parent().next().next().next().children("label").children().remove();
        };
    }

    $("select[name='onSiteAddressType']").change(function () {

        if('ADDTY001'==$(this).val()){
            if( $(this).parent().parent().next().children("label").children().length<1){
                $(this).parent().parent().next().children("label").append("<span class=\"mandatory\">*</span>");
                $(this).parent().parent().next().next().children("label").append("<span class=\"mandatory\">*</span>");
                $(this).parent().parent().next().next().next().children("label").append("<span class=\"mandatory\">*</span>");
            }
        }else if('ADDTY002'==$( this).val()) {
            $(this).parent().parent().next().children("label").children().remove();
            $(this).parent().parent().next().next().children("label").children().remove();
            $(this).parent().parent().next().next().next().children("label").children().remove();
        }

    });

    $("select[name='conveyanceAddrType']").change(function () {
        if('ADDTY001'==$( this).val()){
            if( $(this).parent().parent().next().children("label").children().length<1){
                $(this).parent().parent().next().children("label").append("<span class=\"mandatory\">*</span>");
                $(this).parent().parent().next().next().children("label").append("<span class=\"mandatory\">*</span>");
                $(this).parent().parent().next().next().next().children("label").append("<span class=\"mandatory\">*</span>");
            }
        }else if('ADDTY002'==$( this).val()) {
            $(this).parent().parent().next().children("label").children().remove();
            $(this).parent().parent().next().next().children("label").children().remove();
            $(this).parent().parent().next().next().next().children("label").children().remove();
        }
    });

    $("select[name='offSiteAddrType']").change(function () {

        if('ADDTY001'==$( this).val()){
            if( $(this).parent().parent().next().children("label").children().length<1){
                $(this).parent().parent().next().children("label").append("<span class=\"mandatory\">*</span>");
                $(this).parent().parent().next().next().children("label").append("<span class=\"mandatory\">*</span>");
                $(this).parent().parent().next().next().next().children("label").append("<span class=\"mandatory\">*</span>");
            }
        }else if('ADDTY002'==$( this).val()) {
            $(this).parent().parent().next().children("label").children().remove();
            $(this).parent().parent().next().next().children("label").children().remove();
            $(this).parent().parent().next().next().next().children("label").children().remove();
        }
    });
    $("input[name='easMtsUseOnly0']").change(function (){
        if($("input[name='easMtsUseOnlyVal']").val()=='UOT001'){
            if($("input[name='easMtsUseOnlyVal']").closest('div.form-group').next().children('label').children().length<1){
                $("input[name='easMtsUseOnlyVal']").closest('div.form-group').next().children('label').append("<span class=\"mandatory\">*</span>");
            }
            if($("input[name='easMtsUseOnlyVal']").closest('div.form-group').next().next().children('label').children().length<1){
                $("input[name='easMtsUseOnlyVal']").closest('div.form-group').next().next().children('label').append("<span class=\"mandatory\">*</span>");
            }
        }else if($("input[name='easMtsUseOnlyVal']").val()=='UOT002'){
            $("input[name='easMtsUseOnlyVal']").closest('div.form-group').next().children('label').children().remove();
            $("input[name='easMtsUseOnlyVal']").closest('div.form-group').next().next().children('label').children().remove();
        }
    });

</script>

