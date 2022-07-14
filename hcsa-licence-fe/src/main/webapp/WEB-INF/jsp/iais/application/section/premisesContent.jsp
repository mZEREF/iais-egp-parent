<%--<style>
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

</style>--%>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.StringUtil" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%
    String webroot2 = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.COMMON_CSS_ROOT;
%>
<script type="text/javascript" src="<%=webroot2%>js/file-upload.js"></script>

<c:set var="permanent" value="PERMANENT" />
<c:set var="conv" value="CONVEYANCE" />
<c:set var="easMts" value="EASMTS" />
<c:set var="mobile" value="MOBILE" />
<c:set var="remote" value="REMOTE" />

<c:forEach var="appGrpPremisesDto" items="${AppSubmissionDto.appGrpPremisesDtoList}" varStatus="status">
    <c:set value="${errorMap_premises[premIndexNo]}" var="errMsg"/>
    <c:set var="canEdit" value="true"/>
    <div class="row premContent <c:if test="${!status.first}">underLine</c:if>">
        <input class="not-refresh" type="hidden" name="chooseExistData" value="${appGrpPremisesDto.existingData}"/>
        <input class="not-refresh" type="hidden" name="isPartEdit" value="0"/>
        <%--<input class="not-refresh not-clear" type="hidden" name="rfiCanEdit" value="${appGrpPremisesDto.rfiCanEdit}"/>--%>
        <%--<input class="not-refresh not-clear premValue" type="hidden" name="premValue" value="${status.index}"/>--%>
        <input class="not-refresh not-clear premIndex" type="hidden" name="premIndex" value="${status.index}"/>
        <input class="not-refresh premisesIndexNo" type="hidden" name="premisesIndexNo" value="${appGrpPremisesDto.premisesIndexNo}"/>
        <input class="not-refresh premTypeValue" type="hidden" name="premType" value="${appGrpPremisesDto.premisesType}"/>
        <input class="not-refresh premSelValue" type="hidden" value="${appGrpPremisesDto.premisesSelect}"/>

        <c:set var="premValue" value="${status.index}"/>
        <%--<input hidden class="premiseIndex" value="${premValue}">--%>
        <c:choose>
            <c:when test="${appGrpPremisesDto.appPremisesOperationalUnitDtos.size() > 0}">
                <input class="not-refresh opLength" type="hidden" name="opLength" value="${appGrpPremisesDto.appPremisesOperationalUnitDtos.size() + 1}"/>
            </c:when>
            <c:otherwise>
                <input class="not-refresh opLength" type="hidden" name="opLength" value="1"/>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${appGrpPremisesDto.appPremNonLicRelationDtos.size() > 0}">
                <input class="not-refresh nonHcsaLength" type="hidden" name="nonHcsaLength" value="${appGrpPremisesDto.appPremNonLicRelationDtos.size()}"/>
            </c:when>
            <c:otherwise>
                <input class="not-refresh nonHcsaLength" type="hidden" name="nonHcsaLength" value="1"/>
            </c:otherwise>
        </c:choose>

        <c:choose>
            <c:when test="${appGrpPremisesDto.clickRetrieve}">
                <input class="not-refresh retrieveflag" type="hidden" name="retrieveflag" value="1"/>
            </c:when>
            <c:otherwise>
                <input class="not-refresh retrieveflag" type="hidden" name="retrieveflag" value="0"/>
            </c:otherwise>
        </c:choose>

        <div class="col-xs-12">
            <div class="form-horizontal">
                <div class="form-group">
                    <div class="col-xs-12 col-md-6">
                        <p class="app-title">Mode of Service Delivery <span class="premHeader">${status.index+1}</span></p>
                    </div>
                    <div class="col-xs-12 col-md-4 text-right removeEditDiv <c:if test="${status.first}">hidden</c:if>">
                        <c:choose>
                            <c:when test="${!isRFI && !isRFC && !isRenew}">
                                <h4 class="text-danger"><em class="fa fa-times-circle del-size-36 removeBtn"></em></h4>
                            </c:when>
                            <c:when test="${((isRFI && appGrpPremisesDto.rfiCanEdit) || isRFC || isRenew)}">
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
            <c:if test="${isRFI || isRenew || isRFC}">
            <div class="form-horizontal">
                <div class="form-group">
                    <div class="col-xs-12">
                        <span class="premise-type ack-font-16">
                            <strong>
                                <c:out value="Address: "/>
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
                <div class="form-group premisesTypeDiv"<c:if test="${isRenew || isRFC}">hidden</c:if> >
                    <%--<label class="col-xs-12 col-md-4 control-label error-msg-type">What is your mode of service delivery ? <span class="mandatory">*</span></label>--%>
                    <iais:field value="What is your mode of service delivery ?" width="5" mandatory="true"/>

                    <c:set var="premTypeLen" value="${premisesType.size()}"/>
                    <c:set var="premTypeCss" value="${premTypeLen > 2 ? 'col-md-2' : 'col-md-3'}"/>
                    <c:forEach var="premType" items="${premisesType}">
                        <div class="col-xs-12 ${premTypeCss} form-check">
                            <c:if test="${appGrpPremisesDto.premisesType!=premType}">
                                <input class="form-check-input premTypeRadio"  type="radio" name="premType${status.index}" value="${premType}" aria-invalid="false">
                            </c:if>
                            <c:if test="${appGrpPremisesDto.premisesType==premType}">
                                <input class="form-check-input premTypeRadio"  type="radio" name="premType${status.index}" checked="checked" value="${premType}" aria-invalid="false">
                            </c:if>
                            <label class="form-check-label" ><span class="check-circle"></span>
                                <c:if test="${premType == permanent}">
                                    Permanent Premises
                                </c:if>
                                <c:if test="${premType == conv}">
                                    Conveyance
                                </c:if>
                                <c:if test="${premType == easMts}">
                                    Conveyance<br/>(in a mobile clinic / ambulance)
                                </c:if>
                                <c:if test="${premType == mobile}">
                                    Mobile Delivery
                                </c:if>
                                <c:if test="${premType == remote}">
                                    Remote Delivery
                                </c:if>
                                &nbsp;
                            </label>
                            <c:choose>
                                <c:when test="${premType == permanent}">
                                    <a class="btn-tooltip styleguide-tooltip" style="z-index: 99;position: absolute;" href="javascript:void(0);" data-placement="top"  data-toggle="tooltip" data-html="true" title="&lt;p&gt;<iais:message  key="NEW_ACK019"></iais:message>&lt;/p&gt;">i</a>
                                </c:when>
                                <c:when test="${premType == conv || premType == easMts}">
                                    <a class="btn-tooltip styleguide-tooltip" style="z-index: 99;position: absolute;" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="&lt;p&gt;<iais:message  key="NEW_ACK021"></iais:message>&lt;/p&gt;">i</a>
                                </c:when>
                                <c:when test="${premType == mobile}">
                                    <a class="btn-tooltip styleguide-tooltip"  style="z-index: 99;position: absolute;" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="&lt;p&gt;<iais:message  key="NEW_ACK032"></iais:message>&lt;/p&gt;">i</a>
                                </c:when>
                                <c:when test="${premType == remote}">
                                    <a class="btn-tooltip styleguide-tooltip"  style="z-index: 99;position: absolute;" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="&lt;p&gt;<iais:message  key="NEW_ACK033"></iais:message>&lt;/p&gt;">i</a>
                                </c:when>
                            </c:choose>
                        </div>
                    </c:forEach>
                </div>
                <iais:row>
                    <iais:field value="" width="5"/>
                    <div class="col-xs-12 col-md-5">
                        <span class="error-msg" name="iaisErrorMsg" id="error_premisesType${status.index}"></span>
                    </div>
                </iais:row>

                <c:if test="${StringUtil.isIn(permanent, premisesType)}">
                <iais:row cssClass="permanentSelect hidden">
                    <iais:field value="Add or select a Permanent Premises from the list :" width="5" mandatory="true"/>
                    <iais:value id="permanentSelect" cssClass="col-xs-11 col-sm-7 col-md-5">
                        <iais:select cssClass="premSelect permanentSel" name="permanentSel${status.index}" options="permanentSelect" needSort="false" value="${appGrpPremisesDto.premisesSelect}"/>
                    </iais:value>
                </iais:row>
                </c:if>
                <c:if test="${StringUtil.isIn(conv, premisesType)}">
                <iais:row cssClass="conveyanceSelect hidden">
                    <iais:field value="Add or select a Conveyance from the list :" width="5" mandatory="true"/>
                    <iais:value id="conveyanceSelect"  cssClass="col-xs-11 col-sm-7 col-md-5">
                        <iais:select cssClass="premSelect conveyanceSel" name="conveyanceSel${status.index}"  options="conveyancePremSel" needSort="false"  value="${appGrpPremisesDto.premisesSelect}"/>
                    </iais:value>
                </iais:row>
                </c:if>
                <c:if test="${StringUtil.isIn(easMts, premisesType)}">
                <iais:row cssClass="easMtsSelect hidden">
                    <iais:field value="Add or select a Conveyance from the list :" width="5" mandatory="true"/>
                    <iais:value id="easMtsSelect"  cssClass="col-xs-11 col-sm-7 col-md-5">
                        <iais:select cssClass="premSelect easMtsSel" name="easMtsSel${status.index}" options="easMtsPremSel" needSort="false"  value="${appGrpPremisesDto.premisesSelect}"/>
                    </iais:value>
                </iais:row>
                </c:if>
                <c:if test="${StringUtil.isIn(mobile, premisesType)}">
                <iais:row cssClass="mobileSelect hidden">
                    <iais:field value="Add or select a Mobile Delivery from the list :" width="5" mandatory="true"/>
                    <iais:value id="mobileSelect" cssClass="col-xs-11 col-sm-7 col-md-5">
                        <iais:select cssClass="premSelect mobileSel" name="mobileSel${status.index}" options="mobilePremSel" needSort="false"  value="${appGrpPremisesDto.premisesSelect}"/>
                    </iais:value>
                </iais:row>
                </c:if>
                <c:if test="${StringUtil.isIn(remote, premisesType)}">
                <iais:row cssClass="remoteSelect hidden">
                    <iais:field value="Add or select a Remote Delivery from the list :" width="5" mandatory="true"/>
                    <iais:value id="remoteSelect"  cssClass="col-xs-11 col-sm-7 col-md-5">
                        <iais:select cssClass="premSelect remoteSel" name="remoteSel${status.index}" options="remotePremSel" needSort="false"  value="${appGrpPremisesDto.premisesSelect}"/>
                    </iais:value>
                </iais:row>
                </c:if>
                <iais:row>
                    <div class="col-xs-12 col-md-4 "></div>
                    <div class=" col-xs-11 col-sm-7 col-md-5" style="margin-bottom: 2%">
                        <span class="error-msg" id="error_premisesSelect${status.index}" name="iaisErrorMsg"></span>
                    </div>
                </iais:row>
            </div>

            <div class="new-premise-form">
                <div class="form-horizontal">
                    <iais:row cssClass="scdfRefNoRow">
                        <c:set var="scdfRefNoInfo"><iais:message key="NEW_ACK006"></iais:message></c:set>
                        <iais:field value="Fire Safety & Shelter Bureau Ref. No." width="5" info="${scdfRefNoInfo}"/>
                        <iais:value cssClass="col-xs-7 col-sm-4 col-md-5 fireIssuedDateDiv">
                            <iais:input maxLength="66" name="scdfRefNo${status.index}" type="text" value="${appGrpPremisesDto.scdfRefNo}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row cssClass="certIssuedDtRow">
                        <iais:field value="Fire Safety Certificate Issued Date" width="5"/>
                        <iais:value cssClass="col-xs-7 col-sm-4 col-md-5 fireIssuedDateDiv">
                            <iais:datePicker cssClass="certIssuedDt" name="certIssuedDt${status.index}" value="${appGrpPremisesDto.certIssuedDtStr}" />
                        </iais:value>
                    </iais:row>

                    <iais:row cssClass="vehicleRow">
                        <iais:field value="Vehicle No." mandatory="true" width="5"/>
                        <iais:value width="7" cssClass="col-md-5">
                            <iais:input maxLength="10" type="text" name="vehicleNo${status.index}" value="${appGrpPremisesDto.vehicleNo}"/>
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field value="Business Name" mandatory="true" width="5"/>
                        <iais:value width="7" cssClass="col-xs-10 col-md-5 disabled">
                            <iais:input cssClass="" maxLength="100" type="text" name="hciName${status.index}" value="${appGrpPremisesDto.hciName}"/>
                        </iais:value>
                    </iais:row>

                    <iais:row cssClass="postalCodeDiv">
                        <iais:field value="Postal Code" mandatory="true" width="5"/>
                        <iais:value cssClass="col-xs-10 col-md-5">
                            <iais:input cssClass="postalCode" maxLength="6" type="text" name="postalCode${status.index}" value="${appGrpPremisesDto.postalCode}"/>
                        </iais:value>
                        <div class="col-xs-7 col-sm-6 col-md-3">
                            <p><a class="retrieveAddr <c:if test="${!canEdit || readOnly}">hidden</c:if>">Retrieve your address</a></p>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Address Type" mandatory="true" width="5"/>
                        <iais:value width="7" cssClass="addressType">
                            <iais:select cssClass="addrType" name="addrType${status.index}" codeCategory="CATE_ID_ADDRESS_TYPE" needSort="false"
                                         firstOption="Please Select" value="${appGrpPremisesDto.addrType}" />
                        </iais:value>
                    </iais:row>
                    <iais:row cssClass="address">
                        <iais:field value="Block / House No." width="5" cssClass="blkNoLabel"/>
                        <iais:value width="7">
                            <iais:input cssClass="blkNo" maxLength="10" type="text" name="blkNo${status.index}" value="${appGrpPremisesDto.blkNo}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row cssClass="operationDiv">
                        <iais:field value="Floor / Unit No." width="5" cssClass="floorUnitLabel"/>
                        <iais:value cssClass="col-xs-7 col-sm-4 col-md-5 ">
                            <div class="row">
                                <iais:value cssClass="col-xs-12 col-md-5 ">
                                    <input class="floorNo" maxlength="3" type="text" data-base="FloorNo" name="${status.index}FloorNo0" value="${appGrpPremisesDto.floorNo}"/>
                                    <span class="error-msg" name="iaisErrorMsg" id="error_${status.index}FloorNo0"></span>
                                </iais:value>
                                <div class="col-xs-12 col-md-2 text-center"><p>-</p></div>
                                <iais:value cssClass="col-xs-12 col-md-5 ">
                                    <input class="unitNo" maxlength="5" type="text" data-base="UnitNo" name="${status.index}UnitNo0" value="${appGrpPremisesDto.unitNo}"/>
                                    <span class="error-msg" name="iaisErrorMsg" id="error_${status.index}UnitNo0"></span>
                                </iais:value>
                            </div>
                        </iais:value>
                        <div class="operationAdlDiv hidden">
                            <div class=" col-xs-7 col-sm-4 col-md-2 ">
                                <p>(Additional)&nbsp;&nbsp;&nbsp;&nbsp;</p>
                            </div>
                            <div class=" col-xs-7 col-sm-4 col-md-1 text-center">
                                <p class="text-danger opDel"><em class="fa fa-times-circle del-size-36"></em></p>
                            </div>
                        </div>
                    </iais:row>
                    <c:set var="hasAddFU" value="${appGrpPremisesDto.appPremisesOperationalUnitDtos.size()>0}" />
                    <div class="operationDivGroup">
                        <c:if test="${hasAddFU}">
                            <c:forEach var="operationDto" items="${appGrpPremisesDto.appPremisesOperationalUnitDtos}" varStatus="opStat">
                                <c:set var="opIndex" value="${opStat.index + 1}" />
                                <iais:row cssClass="operationDiv">
                                    <iais:field value="" width="5"/>
                                    <iais:value cssClass="col-xs-7 col-sm-4 col-md-5 ">
                                        <div class="row">
                                            <iais:value cssClass="col-xs-12 col-md-5 ">
                                                <input class="floorNo" maxlength="3" type="text" data-base="FloorNo" name="${premValue}FloorNo${opIndex}" value="${operationDto.floorNo}" />
                                                <span class="error-msg" name="iaisErrorMsg" id="error_${premValue}FloorNo${opIndex}"></span>
                                            </iais:value>
                                            <div class="col-xs-12 col-md-2 text-center"><p>-</p></div>
                                            <iais:value cssClass="col-xs-12 col-md-5 ">
                                                <input class="unitNo" maxlength="5" type="text" data-base="UnitNo" name="${premValue}UnitNo${opIndex}" value="${operationDto.unitNo}"/>
                                                <span class="error-msg" name="iaisErrorMsg" id="error_${premValue}UnitNo${opIndex}"></span>
                                            </iais:value>
                                        </div>
                                        <span class="error-msg" name="iaisErrorMsg" id="error_${premValue}FloorUnit${opIndex}"></span>
                                    </iais:value>
                                    <div class="operationAdlDiv">
                                        <div class=" col-xs-7 col-sm-4 col-md-2 ">
                                            <p>(Additional)&nbsp;&nbsp;&nbsp;&nbsp;</p>
                                        </div>
                                        <div class=" col-xs-7 col-sm-4 col-md-1 text-center">
                                            <p class="text-danger opDel"><em class="fa fa-times-circle del-size-36"></em></p>
                                        </div>
                                    </div>
                                </iais:row>
                            </c:forEach>
                        </c:if>
                        <!--prem operational -->
                        <iais:row cssClass="addOpDiv">
                            <iais:field value="" width="5"/>
                            <iais:value cssClass="col-xs-7 col-sm-4 col-md-5 ">
                                <span class="addOperational"><a style="text-decoration:none;">+ Add Additional Floor/Unit No.</a></span>
                            </iais:value>
                        </iais:row>
                    </div>
                    <iais:row cssClass="address">
                        <iais:field value="Street Name" mandatory="true" width="5"/>
                        <iais:value width="5" cssClass="col-md-5">
                            <iais:input cssClass="streetName" maxLength="32" type="text" name="streetName${status.index}" value="${appGrpPremisesDto.streetName}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row cssClass="address">
                        <iais:field value="Building Name" width="5"/>
                        <iais:value width="5" cssClass="col-md-5">
                            <iais:input cssClass="buildingName" maxLength="66" type="text" name="buildingName${status.index}" value="${appGrpPremisesDto.buildingName}"/>
                        </iais:value>
                    </iais:row>

                    <iais:row cssClass="easMtsAddFields">
                        <iais:field value="For public/in-house use only?" mandatory="true" width="5"/>
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
                    <iais:row cssClass="easMtsAddFields">
                        <iais:field value="Public Email" mandatory="true" width="5"/>
                        <iais:value width="7" cssClass="col-md-5">
                            <iais:input maxLength="320" cssClass="easMtsPubEmail" type="text" name="easMtsPubEmail${status.index}"
                                        value="${appGrpPremisesDto.easMtsPubEmail}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row cssClass="easMtsAddFields">
                        <iais:field value="Public Hotline" mandatory="true" width="5"/>
                        <iais:value width="7" cssClass="col-md-5">
                            <iais:input maxLength="8" cssClass="easMtsPubHotline" type="text" name="easMtsPubHotline"  value="${appGrpPremisesDto.easMtsPubHotline}"/>
                            <span  class="error-msg" name="iaisErrorMsg" id="error_easMtsPubHotline${status.index}"></span>
                        </iais:value>
                    </iais:row>

                    <div class="co-location-div">
                        <iais:row>
                            <iais:field value="Co-Location Service" width="10" />
                            <iais:value/>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Are you co-locating with a service that is licensed under HCSA?" mandatory="true" width="5" />
                            <iais:value width="3" cssClass="col-md-3 form-check">
                                <input <c:if test="${'1'==appGrpPremisesDto.locateWtihHcsa}">checked="checked"</c:if> class="form-check-input" type="radio" name="locateWtihHcsa${status.index}" value = "1" aria-invalid="false">
                                <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
                            </iais:value>
                            <iais:value width="4" cssClass="col-md-4 form-check">
                                <input <c:if test="${'0'==appGrpPremisesDto.locateWtihHcsa}">checked="checked"</c:if> class="form-check-input" type="radio" name="locateWtihHcsa${status.index}" value = "0" aria-invalid="false">
                                <label class="form-check-label" ><span class="check-circle"></span>No</label>
                            </iais:value>
                            <iais:value cssClass="col-md-offset-4 col-md-8 col-xs-12">
                                <span class="error-msg " name="iaisErrorMsg" id="error_locateWtihHcsa${status.index}"></span>
                            </iais:value>
                        </iais:row>
                        <iais:row cssClass="locateWtihNonHcsaRow">
                            <iais:field value="Are you co-locating with a service that is licensed under HCSA?" mandatory="true" width="5" />
                            <iais:value width="3" cssClass="col-md-3 form-check">
                                <input <c:if test="${'1'==appGrpPremisesDto.locateWtihNonHcsa}">checked="checked"</c:if> class="form-check-input locateWtihNonHcsa" type="radio" name="locateWtihNonHcsa${status.index}" value = "1" aria-invalid="false">
                                <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
                            </iais:value>
                            <iais:value width="4" cssClass="col-md-4 form-check">
                                <input <c:if test="${'0'==appGrpPremisesDto.locateWtihNonHcsa}">checked="checked"</c:if> class="form-check-input locateWtihNonHcsa" type="radio" name="locateWtihNonHcsa${status.index}" value = "0" aria-invalid="false">
                                <label class="form-check-label" ><span class="check-circle"></span>No</label>
                            </iais:value>
                            <iais:value cssClass="col-md-offset-4 col-md-8 col-xs-12">
                                <span class="error-msg " name="iaisErrorMsg" id="error_locateWtihNonHcsa${status.index}"></span>
                            </iais:value>
                        </iais:row>
                        <div class="nonHcsaRowDiv">
                            <div class="file-upload-gp" style="background-color: rgba(242, 242, 242, 1);padding: 20px;">
                                <p>Please list down all services not licensed under HCSA in the tabs below. Alternatively, you may also submit using the
                                    <a href="${pageContext.request.contextPath}/co-non-hcsa-template">Excel Template</a>
                                </p>
                                <div id="uploadFileShowId">
                                </div>
                                <div class="col-xs-12">
                                    <span id="error_uploadFileError" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                                <br/>
                                <input id="uploadFile" name="selectedFile"
                                       class="uploadFile commDoc"
                                       type="file" style="display: none;"
                                       aria-label="selectedFile1"
                                       onclick="fileClicked(event)"
                                       onchange="ajaxCallUpload('mainForm','uploadFile');"/>
                                <a class="btn btn-file-upload btn-secondary" onclick="clearFlagValueFEFile()">Upload</a>
                            </div>
                            <iais:row>
                                <label class="col-xs-12 col-md-4 control-label">Business Name</label>
                                <label class="col-xs-12 col-md-4 control-label">Services Provided</label>
                            </iais:row>

                            <c:set var="hasNonHcsa" value="${appGrpPremisesDto.appPremNonLicRelationDtos.size() > 0}" />
                            <c:if test="${hasNonHcsa}">
                            <c:forEach var="relatedDto" items="${appGrpPremisesDto.appPremNonLicRelationDtos}" varStatus="relatedStatus">
                                <iais:row cssClass="nonHcsaRow">
                                    <div class="col-xs-12 col-md-4">
                                        <input maxlength="100" class="coBusinessName" type="text" data-base="CoBusinessName" name="${premValue}CoBusinessName${relatedStatus.index}" value="${relatedDto.businessName}" />
                                        <span  class="error-msg" name="iaisErrorMsg" id="error_${premValue}CoBusinessName${relatedStatus.index}"></span>
                                    </div>
                                    <div class="col-xs-12 col-md-4">
                                        <input maxlength="100" class="coSvcName" type="text" data-base="CoSvcName" name="${premValue}CoSvcName${relatedStatus.index}" value="${relatedDto.providedService}" />
                                        <span  class="error-msg" name="iaisErrorMsg" id="error_${premValue}CoSvcName${relatedStatus.index}"></span>
                                    </div>
                                    <div class="col-xs-12 col-md-2 delNonHcsaSvcRow">
                                        <div class="text-center">
                                            <p class="text-danger nonHcsaSvcDel"><em class="fa fa-times-circle del-size-36"></em></p>
                                        </div>
                                    </div>
                                </iais:row>
                            </c:forEach>
                            </c:if>
                            <c:if test="${not hasNonHcsa}">
                            <iais:row cssClass="nonHcsaRow">
                                <div class="col-xs-12 col-md-4">
                                    <input maxlength="100" class="coBusinessName" type="text" data-base="CoBusinessName" name="${premValue}CoBusinessName0" value="" />
                                    <span class="error-msg" name="iaisErrorMsg" id="error_${premValue}CoBusinessName0"></span>
                                </div>
                                <div class="col-xs-12 col-md-4">
                                    <input maxlength="100" class="coSvcName" type="text" data-base="CoSvcName" name="${premValue}CoSvcName0" value="" />
                                    <span class="error-msg" name="iaisErrorMsg" id="error_${premValue}CoSvcName0"></span>
                                </div>
                                <div class="col-xs-12 col-md-2 delNonHcsaSvcRow hiden">
                                    <div class="text-center">
                                        <p class="text-danger nonHcsaSvcDel"><em class="fa fa-times-circle del-size-36"></em></p>
                                    </div>
                                </div>
                            </iais:row>
                            </c:if>
                            <iais:row cssClass="addNonHcsaSvcRow">
                                <div class="col-xs-12">
                                    <span class="addNonHcsaSvc"><a style="text-decoration:none;">+ Add Non-Licensable Service</a></span>
                                </div>
                            </iais:row>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</c:forEach>
<script type="text/javascript">
    $(document).ready(function() {
        initPremiseEvent();
        initPremisePage($('div.premContent'));
    });

    function doEditPremise(premContent, isPartEdit) {

    }

    function initPremiseEvent() {
        addOperationalEvnet();
        delOperationEvent();

        locateWtihNonHcsaEvent();
        addNonHcsaEvent();
        delNonHcsaEvent();

        premTypeEvent();
        premSelectEvent();

        addrTypeEvent();
        retrieveAddrEvent();

        addPremEvent();
        removeBtnEvent();
    }

    function initPremisePage($premContent) {
        checkAddressMandatory($premContent);
        checkLocateWtihNonHcsa($premContent);

        var premType = $premContent.find('.premTypeValue').val();
        var premSelectVal = $premContent.find('.premSelValue').val();
        checkPremSelect($premContent, premSelectVal, true);
        console.info("Prem: " + premType + ' | ' + premSelectVal);
        if ('PERMANENT' == premType) {
            showTag($premContent.find('.permanentSelect'));
            hideTag($premContent.find('.conveyanceSelect'));
            hideTag($premContent.find('.easMtsSelect'));
            hideTag($premContent.find('.mobileSelect'));
            hideTag($premContent.find('.remoteSelect'));
            hideTag($premContent.find('.vehicleRow'));
            hideTag($premContent.find('.easMtsAddFields'));
            showTag($premContent.find('.scdfRefNoRow'));
            showTag($premContent.find('.certIssuedDtRow'));
            showTag($premContent.find('.co-location-div'));
        } else if ('CONVEYANCE' == premType) {
            hideTag($premContent.find('.permanentSelect'));
            showTag($premContent.find('.conveyanceSelect'));
            hideTag($premContent.find('.easMtsSelect'));
            hideTag($premContent.find('.mobileSelect'));
            hideTag($premContent.find('.remoteSelect'));
            hideTag($premContent.find('.scdfRefNoRow'));
            hideTag($premContent.find('.certIssuedDtRow'));
            hideTag($premContent.find('.vehicleRow'));
            hideTag($premContent.find('.easMtsAddFields'));
            hideTag($premContent.find('.co-location-div'));
            showTag($premContent.find('.vehicleRow'));
            showTag($premContent.find('.co-location-div'));
        } else if ('EASMTS' == premType) {
            hideTag($premContent.find('.permanentSelect'));
            hideTag($premContent.find('.conveyanceSelect'));
            showTag($premContent.find('.easMtsSelect'));
            hideTag($premContent.find('.mobileSelect'));
            hideTag($premContent.find('.remoteSelect'));
            hideTag($premContent.find('.scdfRefNoRow'));
            hideTag($premContent.find('.certIssuedDtRow'));
            hideTag($premContent.find('.vehicleRow'));
            hideTag($premContent.find('.easMtsAddFields'));
            hideTag($premContent.find('.co-location-div'));
            showTag($premContent.find('.easMtsAddFields'));
        } else if ('MOBILE' == premType) {
            hideTag($premContent.find('.permanentSelect'));
            hideTag($premContent.find('.conveyanceSelect'));
            hideTag($premContent.find('.easMtsSelect'));
            showTag($premContent.find('.mobileSelect'));
            hideTag($premContent.find('.remoteSelect'));
            hideTag($premContent.find('.scdfRefNoRow'));
            hideTag($premContent.find('.certIssuedDtRow'));
            hideTag($premContent.find('.vehicleRow'));
            hideTag($premContent.find('.easMtsAddFields'));
            hideTag($premContent.find('.co-location-div'));
        } else if ('REMOTE' == premType) {
            hideTag($premContent.find('.permanentSelect'));
            hideTag($premContent.find('.conveyanceSelect'));
            hideTag($premContent.find('.easMtsSelect'));
            hideTag($premContent.find('.mobileSelect'));
            showTag($premContent.find('.remoteSelect'));
            hideTag($premContent.find('.scdfRefNoRow'));
            hideTag($premContent.find('.certIssuedDtRow'));
            hideTag($premContent.find('.vehicleRow'));
            hideTag($premContent.find('.easMtsAddFields'));
            hideTag($premContent.find('.co-location-div'));
        } else {
            hideTag($premContent.find('.permanentSelect'));
            hideTag($premContent.find('.conveyanceSelect'));
            hideTag($premContent.find('.easMtsSelect'));
            hideTag($premContent.find('.mobileSelect'));
            hideTag($premContent.find('.remoteSelect'));
            hideTag($premContent.find(".new-premise-form"));
        }
    }

    function initPremiseData($premContent) {
        $premContent.find('.premSelValue').val('-1');
        $premContent.find('.premTypeValue').val('');
        $premContent.find('.premisesIndexNo').val('');
        $premContent.find('.chooseExistData').val('0');
        $premContent.find('.isPartEdit').val('0');
    }

    function addPremEvent() {
        $('#addPremBtn').on('click', function () {
            var $target = $('div.premContent:last');
            var src = $target.clone();
            $('div.premises-content').append(src)
            $target.html(src.html());// for preventing the radio auto cleared
            var $premContent = $('div.premContent').last();
            clearFields($premContent);
            //initPremiseData($premContent);
            removeAdditional($premContent);
            refreshPremise($premContent, $('div.premContent').length - 1);
            initPremiseEvent();
        });
    }

    var removeBtnEvent = function () {
        var $target = $(document);
        $target.find('.removeBtn').unbind('click');
        $target.find('.removeBtn').not(':first').on('click', function () {
            $(this).closest('div.premContent').remove();
            refreshAllPremises();
        });
    }

    function refreshPremise($premContent, k) {
        var $target = getJqueryNode($premContent);
        if (isEmptyNode($target)) {
            return;
        }
        //premIndex
        $target.find('.premIndex').val(k);
        if (k == 0) {
            $target.find('.premHeader').html('');
            hideTag($target.find('.removeEditDiv'));
        } else {
            $target.find('.premHeader').html(k + 1);
            showTag($target.find('.removeEditDiv'));
        }
        resetIndex($target, k);
        refreshFloorUnit($target, k);
        refreshNonHcsa($target, k);
        initPremisePage($target);
        premTypeEvent();
        premSelectEvent();
        removeBtnEvent();
    }

    function refreshAllPremises() {
        $('div.premContent').each(function(k, v){
            var $target = $(v);
            if (k == 0) {
                $target.find('.premHeader').html('');
                hideTag($target.find('.removeEditDiv'));
            } else {
                $target.find('.premHeader').html(k + 1);
                showTag($target.find('.removeEditDiv'));
            }
            $target.find('.premIndex').val(k);
            resetIndex($target, k);
            refreshFloorUnit($target, k);
            refreshNonHcsa($target, k);
            initPremisePage($target);
        });
        premTypeEvent();
        premSelectEvent();
        removeBtnEvent();
    }

    function removeAdditional($premContent) {
        $premContent.find('div.operationDivGroup .operationDiv').remove();
        $premContent.find('div.locateWtihNonHcsaRowDiv .locateWtihNonHcsaRow:not(:first)').remove();
    }

    var premTypeEvent = function () {
        $('.premTypeRadio').on('click', function () {
            clearErrorMsg();
            var $premContent = $(this).closest('div.premContent');
            var premType = $premContent.find('.premTypeRadio:checked').val();
            if (isEmpty(premType)) {
                premType = "";
            }
            $premContent.find('.premTypeValue').val(premType);
            $premContent.find('.premSelValue').val('-1');
            initPremisePage($premContent);
        });
    }

    var premSelectEvent = function() {
        $('.premSelect').change(function () {
            showWaiting();
            clearErrorMsg();
            var premSelectVal = $(this).val();
            var $premContent = $(this).closest('div.premContent');
            $premContent.find('.premSelValue').val(premSelectVal);
            checkPremSelect($premContent, premSelectVal, false);
        });
    }

    function checkPremSelect($premContent, premSelectVal, onlyInit) {
        var $premMainContent = $premContent.find(".new-premise-form");
        if ("-1" == premSelectVal || isEmpty(premSelectVal)) {
            hideTag($premMainContent);
            dismissWaiting();
        } else if ("newPremise" == premSelectVal) {
            if (!onlyInit) {
                removeAdditional($premContent);
                clearFields($premMainContent);
                checkAddressMandatory($premContent);
                checkLocateWtihNonHcsa($premContent);
            }
            showTag($premMainContent);
            dismissWaiting();
        } else {
            showTag($premMainContent);
            if (onlyInit) {
                dismissWaiting();
                return;
            }
            clearFields($premMainContent);
            var premType = $premContent.find('.premTypeValue').val();
            var premisesIndexNo = $premContent.find(".premisesIndexNo").val();
            var jsonData = {
                'premIndexNo': premisesIndexNo,
                'premSelectVal': premSelectVal,
                'premisesType': premType
            };
            var opt = {
                url: '${pageContext.request.contextPath}/lic-premises',
                type: 'GET',
                data: jsonData
            };
            callCommonAjax(opt, "premSelectCallback", $premContent);
        }
    }

    function premSelectCallback(data, $premContent) {
        if (data == null || isEmptyNode($premContent)) {
            dismissWaiting();
            return;
        }
        console.info("premSelectCallback");
        removeAdditional($premContent);
        fillForm($premContent, data, "", $('div.premContent').index($premContent));
        fillFloorUnit($premContent, data);
        fillNonHcsa($premContent, data.appPremNonLicRelationDtos);
        // date
        fillValue($premContent.find('.certIssuedDt'), data.certIssuedDtStr);
        checkAddressMandatory($premContent);
        checkLocateWtihNonHcsa($premContent);
        if (eqHciCode=='true') {
            $premContent.find('input[name="chooseExistData"]').val('0');
        } else {
            $premContent.find('input[name="chooseExistData"]').val('1');
        }
        $premContent.find('.premSelValue').val(data.premisesSelect);
        $premContent.find('.premTypeValue').val(data.premisesType);
        $premContent.find('.premisesIndexNo').val(data.premisesIndexNo);
        $premContent.find('.isPartEdit').val('0');
        dismissWaiting();
    }

    function checkLocateWtihNonHcsa($premContent) {
        var $row = $premContent.find('.locateWtihNonHcsaRow');
        if ($row.find('input.locateWtihNonHcsa[value="1"]').is(':checked')) {
            showTag($row.next('.nonHcsaRowDiv'));
            hideTag($premContent.find('.delNonHcsaSvcRow:first'));
        } else {
            var $nonHcsaRowDiv = $row.next('.nonHcsaRowDiv');
            hideTag($nonHcsaRowDiv);
            $nonHcsaRowDiv.find('.locateWtihNonHcsaRow:not(:first)').remove();
        }
    }

    var locateWtihNonHcsaEvent = function (target) {
        var $target = getJqueryNode(target);
        if (isEmptyNode($target)) {
            $target = $(document);
        }
        $target.find('.locateWtihNonHcsa').unbind('click');
        $target.find('.locateWtihNonHcsa').on('click', function () {
            checkLocateWtihNonHcsa($(this).closest('.premContent'));
        });
    }

    function fillNonHcsa($premContent, data) {
        if (isEmpty(data) || !$.isArray(data)) {
            return;
        }
        var $parent = $premContent.find('div.nonHcsaRowDiv');
        var len = data.length;
        for(var i = 0; i < len; i++) {
            var $target = $parent.find('.nonHcsaRow').eq(i);
            if ($target.length == 0) {
                addNonHcsa($parent);
                $target = $parent.find('.nonHcsaRow').eq(i);
            }
            fillValue($target.find('input.coBusinessName'), data[i].businessName);
            fillValue($target.find('input.coSvcName'), data[i].providedService);
        }
    }

    function refreshNonHcsa(target, prefix) {
        var $target = getJqueryNode(target);
        if (isEmptyNode($target)) {
            return;
        }
        $target.find('.nonHcsaRow').each(function(k, v) {
            console.log("refreshNonHcsa---" + prefix + " - " + k);
            toggleTag($(v).find('.delNonHcsaSvcRow'), k != 0);
            resetField(v, k, prefix);
        });
        var length = $target.find('div.nonHcsaRow').length;
        $target.closest('div.premContent').find('.nonHcsaLength').val(length);
        console.log('Non Hcsa: ' + length);
    }

    function addNonHcsa(ele) {
        showWaiting();
        console.log("addNonHcsaSvc2");
        var $premContent = $(ele).closest('div.premContent');
        var src = $premContent.find('div.nonHcsaRow:first').clone();
        $premContent.find('div.addNonHcsaSvcRow').before(src);
        var $target = $premContent.find('div.nonHcsaRow:last');
        clearFields($target);
        refreshNonHcsa($premContent.find('div.nonHcsaRowDiv'), $('div.premContent').index($premContent));
        delOperationEvent($premContent);
        dismissWaiting();
    }

    var addNonHcsaEvent = function (target) {
        var $target = getJqueryNode(target);
        if (isEmptyNode($target)) {
            $target = $(document);
        }
        console.log("addNonHcsaEvent");
        $target.find('.addNonHcsaSvc').unbind('click');
        $target.find('.addNonHcsaSvc').on('click', function () {
            console.log("addNonHcsaSvc");
            addNonHcsa(this);
        });
    }

    var delNonHcsaEvent = function (target) {
        var $target = getJqueryNode(target);
        if (isEmptyNode($target)) {
            $target = $(document);
        }
        $target.find('.nonHcsaSvcDel').unbind('click');
        $target.find('.nonHcsaSvcDel').not(':first').on('click', function () {
            var $premContent = $(this).closest('div.premContent');
            var $nonHcsaRowDiv = $(this).closest('div.nonHcsaRowDiv');
            $(this).closest('div.nonHcsaRow').remove();
            refreshNonHcsa($nonHcsaRowDiv, $('div.premContent').index($premContent));
        });
    }

    function fillFloorUnit($premContent, data) {
        if (isEmpty(data)) {
            return;
        }
        // base
        var $firstFU = $premContent.find('.operationDiv:first');
        fillValue($firstFU.find('input.floorNo'), data.floorNo);
        fillValue($firstFU.find('input.unitNo'), data.unitNo);
        // additional
        var floorUnits = data.appPremisesOperationalUnitDtos;
        var $parent = $premContent.find('div.operationDivGroup');
        var len = floorUnits.length;
        for(var i = 0; i < len; i++) {
            var $target = $parent.find('.operationDiv').eq(i);
            if ($target.length == 0) {
                addFloorUnit($parent);
                $target = $parent.find('.operationDiv').eq(i);
            }
            fillValue($target.find('input.floorNo'), floorUnits[i].floorNo);
            fillValue($target.find('input.unitNo'), floorUnits[i].unitNo);
        }
    }

    function refreshFloorUnit(target, prefix) {
        var $target = getJqueryNode(target);
        if (isEmptyNode($target)) {
            return;
        }
        $target.find('.operationDiv').each(function(i, ele){
            console.log("refreshFloorUnit---" + prefix + " - " + i);
            if (i == 0) {
                hideTag($(ele).find('.operationAdlDiv'));
            } else {
                showTag($(ele).find('.operationAdlDiv'));
                $(ele).find('.floorUnitLabel').html('');
            }
            resetField(ele, i, prefix);
        });
        var length = $target.find('.operationDiv').length;
        $target.find('.opLength').val(length);
    }

    function addFloorUnit(ele) {
        var $premContent = $(ele).closest('div.premContent');
        var src = $premContent.find('div.operationDiv:first').clone();
        clearFields(src);
        $premContent.find('div.addOpDiv').before(src);
        refreshFloorUnit($premContent, $('div.premContent').index($premContent));
        delOperationEvent($premContent);
    }

    var addOperationalEvnet = function (target) {
        var $target = getJqueryNode(target);
        if (isEmptyNode($target)) {
            $target = $(document);
        }
        $target.find('.addOperational').unbind('click');
        $target.find('.addOperational').on('click', function () {
            addFloorUnit(this);
        });
    }

    var delOperationEvent = function (target) {
        var $target = getJqueryNode(target);
        if (isEmptyNode($target)) {
            $target = $(document);
        }
        $target.find('.opDel').unbind('click');
        $target.find('div.operationDivGroup').find('.opDel').on('click', function () {
            var $premContent = $(this).closest('div.premContent');
            $(this).closest('div.operationDiv').remove();
            refreshFloorUnit($premContent, $('div.premContent').index($premContent));
        });
    }

    var addrTypeEvent = function(target) {
        var $target = getJqueryNode(target);
        if (isEmptyNode($target)) {
            $target = $(document);
        }
        $target.find('.addrType').unbind('change');
        $target.find('.addrType').on('change', function(){
            var $premContent = $(this).closest('div.premContent');
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

    var retrieveAddrEvent = function(target) {
        var $target = getJqueryNode(target);
        if (isEmptyNode($target)) {
            $target = $(document);
        }
        $target.find('.retrieveAddr').unbind('click');
        $target.find('.retrieveAddr').on('click', function(){
            var $postalCodeEle = $(this).closest('div.postalCodeDiv');
            var postalCode = $postalCodeEle.find('.postalCode').val();
            retrieveAddr(postalCode, $(this).closest('div.licenseeContent').find('div.address'));
        });
    }

    function retrieveAddr(postalCode, target) {
        var $addressSelectors = $(target);
        var data = {
            'postalCode':postalCode
        };
        showWaiting();
        $.ajax({
            'url':'${pageContext.request.contextPath}/retrieve-address',
            'dataType':'json',
            'data':data,
            'type':'GET',
            'success':function (data) {
                if(data == null){
                    // $postalCodeEle.find('.postalCodeMsg').html("the postal code information could not be found");
                    //show pop
                    $('#postalCodePop').modal('show');
                    //handleVal($addressSelectors.find(':input'), '', false);
                    clearFields($addressSelectors.find(':input'));
                    unReadlyContent($addressSelectors);
                    //$premContent.find('input[name="retrieveflag"]').val('0');
                } else {
                    fillValue($addressSelectors.find('.blkNo'), data.blkHseNo);
                    fillValue($addressSelectors.find('.streetName'), data.streetName);
                    fillValue($addressSelectors.find('.buildingName'), data.buildingName);
                    readonlyContent($addressSelectors);
                }
                dismissWaiting();
            },
            'error':function () {
                //show pop
                $('#postalCodePop').modal('show');
                clearFields($addressSelectors.find(':input'));
                unReadlyContent($addressSelectors);
                dismissWaiting();
            }
        });
    }

    function ajaxCallUpload(idForm, fileAppendId) {
        showWaiting();
        $("#fileAppendId").val(fileAppendId);
        $("#error_" + fileAppendId + "Error").html("");
        $("#uploadFormId").val(idForm);
        var form = new FormData($("#" + idForm)[0]);
        var maxFileSize = $("#fileMaxSize").val();
        var rslt = validateFileSizeMaxOrEmpty(maxFileSize);
        //alert('rslt:'+rslt);
        if (rslt == 'N') {
            $("#error_" + fileAppendId + "Error").html($("#fileMaxMBMessage").val());
            clearFlagValueFEFile();
        } else if (rslt == 'E') {
            clearFlagValueFEFile();
        } else {
            $.ajax({
                type: "post",
                url: BASE_CONTEXT_PATH + "/co-non-hcsa-file?stamp=" + new Date().getTime(),
                data: form,
                async: true,
                dataType: "json",
                processData: false,
                contentType: false,
                success: function (data) {
                    if (data != null && data.description != null) {
                        if (data.msgType == "Y") {
                            if (reloadIndex != -1) {
                                $("#" + fileAppendId + "Div" + reloadIndex).after("<Div id = '" + fileAppendId + "Div" + reloadIndex + "Copy' ></Div>");
                                deleteFileFeDiv(fileAppendId + "Div" + reloadIndex);
                                $("#reloadIndex").val(-1);
                                $("#" + fileAppendId + "Div" + reloadIndex + "Copy").after(data.description);
                                deleteFileFeDiv(fileAppendId + "Div" + reloadIndex + "Copy");
                            } else {
                                $("#" + fileAppendId + "ShowId").append(data.description);
                            }
                            $("#error_" + fileAppendId + "Error").html("");
                            cloneUploadFile();
                        } else {
                            $("#error_" + fileAppendId + "Error").html(data.description);
                        }
                    }
                    if (typeof doActionAfterUploading === 'function') {
                        doActionAfterUploading(data, fileAppendId);
                    }
                    dismissWaiting();
                },
                error: function (msg) {
                    //alert("error");
                    dismissWaiting();
                }
            });
        }
    }

    function initUploadFileData() {
        $('#_needReUpload').val(0);
        $('#_fileType').val("XLSX");
        $('#_singleUpload').val("1");
    }


    /*function fillForm(ele, data, prefix, suffix) {
        var $selector = getJqueryNode(ele);
        if (isEmptyNode($selector)) {
            return;
        }
        if (isEmpty(data)) {
            clearFields($selector);
            return;
        }
        if (isEmpty(prefix)) {
            prefix = "";
        }
        if (isEmpty(suffix)) {
            suffix = "";
        }
        for (var i in data) {
            var val = data[i];
            if (Object.prototype.toString.call(val) === "[object Object]") {
                fillValue(ele, val, prefix, suffix);
            }
            var name = prefix + i + suffix;
            var $input = $selector.find('[name="' + name + '"]');
            if ($input.length == 0) {
                name = prefix + capitalize(i) + suffix;
                $input = $selector.find('[name="' + name + '"]');
            }
            if ($input.length == 0) {
                continue;
            }
            fillValue($input, val, true);
        }
    }

    function readonlyContent(targetSelector) {
        var $selector = getJqueryNode(targetSelector);
        if (isEmptyNode($selector)) {
            return;
        }
        if (!$selector.is(":input")) {
            $selector = $selector.find(':input');
        }
        if ($selector.length <= 0) {
            return;
        }
        $selector.each(function (i, ele) {
            var type = ele.type, tag = ele.tagName.toLowerCase(), $input = $(ele);
            if (type == 'hidden') {
                return;
            }
            $input.prop('readonly', true);
            if (tag == 'select') {
                updateSelectTag($input);
            }
        });
    }

    function unReadlyContent(targetSelector) {
        var $selector = getJqueryNode(targetSelector);
        if (isEmptyNode($selector)) {
            return;
        }
        if (!$selector.is(":input")) {
            $selector = $selector.find(':input');
        }
        if ($selector.length <= 0) {
            return;
        }
        $selector.each(function (i, ele) {
            var type = ele.type, tag = ele.tagName.toLowerCase(), $input = $(ele);
            if (type == 'hidden') {
                return;
            }
            $input.prop('readonly', false);
            if (tag == 'select') {
                updateSelectTag($input);
            }
        });
    }

    function premisesFunc(){}*/

/*
    function isEmptyNode(ele) {
        return ele == null || typeof ele !== "object" || !(ele instanceof jQuery) || ele.length == 0;
    }

    function resetField(targetTag, index, prefix) {
        var $target = getJqueryNode(targetTag);
        if (isEmptyNode($target) || $target.hasClass('not-refresh')) {
            return;
        }
        var tag = $target[0].tagName.toLowerCase();
        if ($target.is(':input')) {
            var orgName = $target.attr('name');
            var orgId = $target.attr('id');
            if (isEmpty(orgName)) {
                orgName = orgId;
            }
            if (isEmpty(orgName)) {
                return;
            }
            if (isEmpty(prefix)) {
                prefix = "";
            }
            var base = $target.data('base');
            if (isEmpty(base)) {
                var result;
                if (isEmpty(prefix)) {
                    prefix = "";
                    result = /(.*\D+)/g.exec(orgName);
                } else {
                    result = /(\D+.*\D+)/g.exec(orgName);
                }
                base = !isEmpty(result) && result.length > 0 ? result[0] : orgName;
            }
            var newName = prefix + base + index;
            $target.prop('name', newName);
            if (orgName == orgId || base == orgId || !isEmpty(orgId) && $('#' + orgId).length > 1) {
                $target.prop('id', newName);
            }
            var $errorSpan = $target.closest('.form-group').find('span[name="iaisErrorMsg"][id="error_'+ orgName +'"]');
            if ($errorSpan.length > 0) {
                $errorSpan.prop('id', 'error_' + newName);
            }
            if (tag == 'select') {
                updateSelectTag($target);
            }
        } else {
            $target.find(':input').each(function() {
                resetField(this, index, prefix);
            });
        }
    }

    function isEmpty(str) {
        return typeof str === 'undefined' || str == null || (typeof str !== 'number' && str == '') || str == 'undefined';
    }
    function clearFields(targetSelector, withoutClearError) {
        var $selector = getJqueryNode(targetSelector);
        if (isEmptyNode($selector)) {
            return;
        }
        if (!$selector.is(":input")) {
            if (isEmpty(withoutClearError) || !withoutClearError) {
                $selector.find("span[name='iaisErrorMsg']").each(function () {
                    $(this).html("");
                });
            }
            $selector = $selector.find(':input[class!="not-clear"]');
        }
        if ($selector.length <= 0) {
            return;
        }
        $selector.each(function() {
            var type = this.type, tag = this.tagName.toLowerCase();
            if (!$(this).hasClass('not-clear')) {
                if (type == 'text' || type == 'password' || type == 'hidden' || tag == 'textarea') {
                    this.value = '';
                } else if (type == 'checkbox') {
                    this.checked = false;
                } else if (type == 'radio') {
                    this.checked = false;
                } else if (tag == 'select') {
                    this.selectedIndex = 0;
                    updateSelectTag($(this));
                }
            }
        });
    }

    function capitalize(str){
        if (isEmpty(str) || Object.prototype.toString.call(str) !== "[object String]") {
            return str;
        }
        return str.charAt(0).toUpperCase() + str.slice(1);
    }

    function uncapitalize(str){
        if (isEmpty(str) || Object.prototype.toString.call(str) !== "[object String]") {
            return str;
        }
        return str.charAt(0).toLowerCase() + str.slice(1);
    }*/
    /*function hideTag(ele) {
        var $ele = getJqueryNode(ele);
        if (isEmpty($ele)) {
            return;
        }
        $ele.hide();
        $ele.addClass('hidden');
        clearFields($ele);
    }*/
    /*$(document).ready(function() {

    });

    function ajaxCallUpload(idForm, fileAppendId) {
        showWaiting();
        $("#fileAppendId").val(fileAppendId);
        $("#error_" + fileAppendId + "Error").html("");
        $("#uploadFormId").val(idForm);
        var form = new FormData($("#" + idForm)[0]);
        var maxFileSize = $("#fileMaxSize").val();
        var rslt = validateFileSizeMaxOrEmpty(maxFileSize);
        //alert('rslt:'+rslt);
        if (rslt == 'N') {
            $("#error_" + fileAppendId + "Error").html($("#fileMaxMBMessage").val());
            clearFlagValueFEFile();
        } else if (rslt == 'E') {
            clearFlagValueFEFile();
        } else {
            $.ajax({
                type: "post",
                url: BASE_CONTEXT_PATH + "/co-non-hcsa-file?stamp=" + new Date().getTime(),
                data: form,
                async: true,
                dataType: "json",
                processData: false,
                contentType: false,
                success: function (data) {
                    if (data != null && data.description != null) {
                        if (data.msgType == "Y") {
                            if (reloadIndex != -1) {
                                $("#" + fileAppendId + "Div" + reloadIndex).after("<Div id = '" + fileAppendId + "Div" + reloadIndex + "Copy' ></Div>");
                                deleteFileFeDiv(fileAppendId + "Div" + reloadIndex);
                                $("#reloadIndex").val(-1);
                                $("#" + fileAppendId + "Div" + reloadIndex + "Copy").after(data.description);
                                deleteFileFeDiv(fileAppendId + "Div" + reloadIndex + "Copy");
                            } else {
                                $("#" + fileAppendId + "ShowId").append(data.description);
                            }
                            $("#error_" + fileAppendId + "Error").html("");
                            cloneUploadFile();
                        } else {
                            $("#error_" + fileAppendId + "Error").html(data.description);
                        }
                    }
                    if (typeof doActionAfterUploading === 'function') {
                        doActionAfterUploading(data, fileAppendId);
                    }
                    dismissWaiting();
                },
                error: function (msg) {
                    //alert("error");
                    dismissWaiting();
                }
            });
        }
    }

    function initUploadFileData() {
        $('#_needReUpload').val(0);
        $('#_fileType').val("XLSX");
        $('#_singleUpload').val("1");
    }

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
    });*/

</script>

