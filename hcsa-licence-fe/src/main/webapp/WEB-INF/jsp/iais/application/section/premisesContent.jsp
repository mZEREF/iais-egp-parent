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

<input type="hidden" name="premCount" value="0"/>


<c:forEach var="appGrpPremisesDto" items="${AppSubmissionDto.appGrpPremisesDtoList}" varStatus="status">
    <c:set value="${errorMap_premises[premIndexNo]}" var="errMsg"/>
    <c:set var="canEdit" value="true"/>
    <div class="row premContent <c:if test="${!status.first}">underLine</c:if>">
        <input type="hidden" name="chooseExistData" value="${appGrpPremisesDto.existingData}"/>
        <input type="hidden" name="isPartEdit" value="0"/>
        <input type="hidden" name="rfiCanEdit" value="${appGrpPremisesDto.rfiCanEdit}"/>
        <input class="premValue" type="hidden" name="premValue" value="${status.index}"/>
        <input class="premisesIndexNo" type="hidden" name="premisesIndexNo" value="${appGrpPremisesDto.premisesIndexNo}"/>

        <c:set var="premValue" value="${status.index}"/>
        <%--<input hidden class="premiseIndex" value="${premValue}">--%>
        <c:choose>
            <c:when test="${appGrpPremisesDto.appPremisesOperationalUnitDtos.size() > 0}">
                <input class="opLength" type="hidden" name="opLength" value="${appGrpPremisesDto.appPremisesOperationalUnitDtos.size() + 1}"/>
            </c:when>
            <c:otherwise>
                <input class="opLength" type="hidden" name="opLength" value="1"/>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${appGrpPremisesDto.appPremNonLicRelationDtos.size() > 0}">
                <input class="nonHcsaLength" type="hidden" name="nonHcsaLength" value="${appGrpPremisesDto.appPremNonLicRelationDtos.size()}"/>
            </c:when>
            <c:otherwise>
                <input class="nonHcsaLength" type="hidden" name="nonHcsaLength" value="1"/>
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

        <div class="col-xs-12">
            <div class="form-horizontal">
                <div class="form-group">
                    <div class="col-xs-12 col-md-6">
                        <p class="app-title">Mode of Service Delivery <span class="premHeader">${status.index+1}</span></p>
                    </div>
                    <div class="col-xs-12 col-md-4 text-right">
                        <c:choose>
                            <c:when test="${!isRFI && !isRFC && !isRenew}">
                                <h4 class="text-danger"><em class="fa fa-times-circle del-size-36 removeBtn"></em></h4>
                                <c:set var="canEdit" value="false"/>
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
                    <label class="col-xs-12 col-md-4 control-label error-msg-type">What is your mode of service delivery ? <span class="mandatory">*</span></label>
                    <input class="premTypeValue" type="hidden" name="premType" value="${appGrpPremisesDto.premisesType}"/>
                    <input class="premSelValue" type="hidden" value="${appGrpPremisesDto.premisesSelect}"/>

                    <c:set var="premTypeLen" value="${premisesType.size()}"/>
                    <c:set var="premTypeCss" value="${premTypeLen > 2 ? 'col-md-2' : 'col-md-3'}"/>
                    <c:forEach var="premType" items="${premisesType}">
                        <div class="col-xs-12 ${premTypeCss}">
                            <c:choose>
                                <c:when test="${premType == permanent}">
                                    <a class="btn-tooltip styleguide-tooltip" style="z-index: 999;position: absolute; right: 30px; top: 12px;" href="javascript:void(0);" data-placement="top"  data-toggle="tooltip" data-html="true" title="&lt;p&gt;<iais:message  key="NEW_ACK019"></iais:message>&lt;/p&gt;">i</a>
                                </c:when>
                                <c:when test="${premType == conv || premType == easMts}">
                                    <a class="btn-tooltip styleguide-tooltip" style="z-index: 999;position: absolute; right: 30px; top: 12px;" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="&lt;p&gt;<iais:message  key="NEW_ACK021"></iais:message>&lt;/p&gt;">i</a>
                                </c:when>
                                <c:when test="${premType == mobile}">
                                    <a class="btn-tooltip styleguide-tooltip"  style="z-index: 999;position: absolute; right: 30px; top: 12px;" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="&lt;p&gt;<iais:message  key="NEW_ACK020"></iais:message>&lt;/p&gt;">i</a>
                                </c:when>
                                <c:when test="${premType == remote}">
                                    <a class="btn-tooltip styleguide-tooltip"  style="z-index: 999;position: absolute; right: 30px; top: 12px;" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="&lt;p&gt;<iais:message  key="NEW_ACK020"></iais:message>&lt;/p&gt;">i</a>
                                </c:when>
                            </c:choose>

                            <div class="form-check">
                                <c:if test="${appGrpPremisesDto.premisesType!=premType}">
                                    <input class="form-check-input premTypeRadio"  type="radio" name="premType${status.index}" value = "${premType}" aria-invalid="false">
                                </c:if>
                                <c:if test="${appGrpPremisesDto.premisesType==premType}">
                                    <input class="form-check-input premTypeRadio"  type="radio" name="premType${status.index}" checked="checked" value = "${premType}"  aria-invalid="false">
                                </c:if>
                                <label class="form-check-label" ><span class="check-circle"></span>
                                    <c:if test="${premType == permanent}">
                                        Permanent Premises
                                    </c:if>
                                    <c:if test="${premType == conv}">
                                        Conveyance
                                    </c:if>
                                    <c:if test="${premType == easMts}">
                                        Conveyance (in a mobile clinic / ambulance)
                                    </c:if>
                                    <c:if test="${premType == mobile}">
                                        Mobile Delivery
                                    </c:if>
                                    <c:if test="${premType == remote}">
                                        Remote Delivery
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
                <c:if test="${StringUtil.isIn(permanent, premisesType)}">
                <iais:row cssClass="permanentSelect hidden">
                    <iais:field value="Add or select a mode of service delivery from the list :" width="5" mandatory="true"/>
                    <iais:value id="permanentSelect" cssClass="col-xs-11 col-sm-7 col-md-5">
                        <iais:select cssClass="premSelect permanentSel" name="permanentSel${status.index}" options="permanentSelect" needSort="false" value="${appGrpPremisesDto.premisesSelect}"/>
                    </iais:value>
                </iais:row>
                </c:if>
                <c:if test="${StringUtil.isIn(conv, premisesType)}">
                <iais:row cssClass="conveyanceSelect hidden">
                    <iais:field value="Add or select a mode of service delivery from the list :" width="5" mandatory="true"/>
                    <iais:value id="conveyanceSelect"  cssClass="col-xs-11 col-sm-7 col-md-5">
                        <iais:select cssClass="premSelect conveyanceSel" name="conveyanceSel${status.index}"  options="conveyancePremSel" needSort="false"  value="${appGrpPremisesDto.premisesSelect}"/>
                    </iais:value>
                </iais:row>
                </c:if>
                <c:if test="${StringUtil.isIn(easMts, premisesType)}">
                <iais:row cssClass="easMtsSelect hidden">
                    <iais:field value="Add or select a mode of service delivery from the list :" width="5" mandatory="true"/>
                    <iais:value id="easMtsSelect"  cssClass="col-xs-11 col-sm-7 col-md-5">
                        <iais:select cssClass="premSelect easMtsSel" name="easMtsSel${status.index}" options="easMtsPremSel" needSort="false"  value="${appGrpPremisesDto.premisesSelect}"/>
                    </iais:value>
                </iais:row>
                </c:if>
                <c:if test="${StringUtil.isIn(mobile, premisesType)}">
                <iais:row cssClass="mobileSelect hidden">
                    <iais:field value="Add or select a mode of service delivery from the list :" width="5" mandatory="true"/>
                    <iais:value id="mobileSelect" cssClass="col-xs-11 col-sm-7 col-md-5">
                        <iais:select cssClass="premSelect mobileSel" name="mobileSel${status.index}" options="mobilePremSel" needSort="false"  value="${appGrpPremisesDto.premisesSelect}"/>
                    </iais:value>
                </iais:row>
                </c:if>
                <c:if test="${StringUtil.isIn(remote, premisesType)}">
                <iais:row cssClass="remoteSelect hidden">
                    <iais:field value="Add or select a mode of service delivery from the list :" width="5" mandatory="true"/>
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
                            <iais:datePicker cssClass="fireIssuedDate" name="certIssuedDt${status.index}" value="${appGrpPremisesDto.certIssuedDtStr}" />
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Business Name" mandatory="true" width="5"/>
                        <iais:value width="7" cssClass="col-xs-10 col-md-5 disabled">
                            <iais:input cssClass="" maxLength="100" type="text" name="hciName${status.index}" value="${appGrpPremisesDto.hciName}"/>
                        </iais:value>
                    </iais:row>

                    <iais:row cssClass="vehicleRow">
                        <iais:field value="Vehicle No." mandatory="true" width="5"/>
                        <iais:value width="7" cssClass="col-md-5">
                            <iais:input maxLength="10" type="text" name="vehicleNo${status.index}" value="${appGrpPremisesDto.vehicleNo}"/>
                        </iais:value>
                    </iais:row>

                    <iais:row cssClass="postalCodeDiv">
                        <iais:field value="Postal Code" mandatory="true" width="5"/>
                        <iais:value cssClass="col-xs-10 col-md-5">
                            <iais:input cssClass="postalCode" maxLength="6" type="text" name="postalCode${status.index}" value="${appGrpPremisesDto.postalCode}"/>
                        </iais:value>
                        <div class="col-xs-7 col-sm-6 col-md-3">
                            <p><a class="retrieveAddr <c:if test="${!canEdit || readOnly}">hidden</c:if>" id="onSite" >Retrieve your address</a></p>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Address Type" mandatory="true" width="5"/>
                        <iais:value width="7" cssClass="addressType">
                            <iais:select cssClass="addrType" name="addrType${status.index}" codeCategory="CATE_ID_ADDRESS_TYPE" needSort="false"
                                         firstOption="Please Select" value="${appGrpPremisesDto.addrType}" />
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Block / House No." width="5" cssClass="blkNoLabel"/>
                        <iais:value width="7">
                            <iais:input cssClass="siteBlkNo" maxLength="10" type="text" name="blkNo${status.index}" value="${appGrpPremisesDto.blkNo}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row cssClass="operationDiv">
                        <iais:field value="Floor / Unit No." width="5" cssClass="floorUnitLabel"/>
                        <iais:value cssClass="col-xs-7 col-sm-4 col-md-5 ">
                            <div class="row">
                                <iais:value cssClass="col-xs-12 col-md-5 ">
                                    <iais:input maxLength="3" type="text" name="${status.index}floorNo0" value="${appGrpPremisesDto.floorNo}"/>
                                </iais:value>
                                <div class="col-xs-12 col-md-2 text-center"><p>-</p></div>
                                <iais:value cssClass="col-xs-12 col-md-5 ">
                                    <iais:input maxLength="5" type="text" name="${status.index}unitNo0" value="${appGrpPremisesDto.unitNo}"/>
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
                                                <iais:input cssClass="floorNo" maxLength="3" type="text" name="${premValue}FloorNo${opIndex}" value="${operationDto.floorNo}" />
                                            </iais:value>
                                            <div class="col-xs-12 col-md-2 text-center"><p>-</p></div>
                                            <iais:value cssClass="col-xs-12 col-md-5 ">
                                                <iais:input cssClass="unitNo" maxLength="5" type="text" name="${premValue}UnitNo${opIndex}" value="${operationDto.unitNo}"/>
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
                    <iais:row>
                        <iais:field value="Street Name" mandatory="true" width="5"/>
                        <iais:value width="5" cssClass="col-md-5">
                            <iais:input cssClass="streetName" maxLength="32" type="text" name="streetName${status.index}" value="${appGrpPremisesDto.streetName}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
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
                            <iais:value width="3" cssClass="col-md-3 form-check" style="padding-left: 0px;">
                                <input <c:if test="${'1'==appGrpPremisesDto.locateWtihHcsa}">checked="checked"</c:if> class="form-check-input" type="radio" name="locateWtihHcsa${status.index}" value = "1" aria-invalid="false">
                                <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
                            </iais:value>
                            <iais:value width="4" cssClass="col-md-4 form-check" style="padding-left: 0px;">
                                <input <c:if test="${'0'==appGrpPremisesDto.locateWtihHcsa}">checked="checked"</c:if> class="form-check-input" type="radio" name="locateWtihHcsa${status.index}" value = "0" aria-invalid="false">
                                <label class="form-check-label" ><span class="check-circle"></span>No</label>
                            </iais:value>
                            <iais:value cssClass="col-xs-12">
                                <span class="error-msg " name="iaisErrorMsg" id="error_locateWtihHcsa${status.index}"></span>
                            </iais:value>
                        </iais:row>
                        <iais:row cssClass="locateWtihNonHcsaRow">
                            <iais:field value="Are you co-locating with a service that is licensed under HCSA?" mandatory="true" width="5" />
                            <iais:value width="3" cssClass="col-md-3 form-check" style="padding-left: 0px;">
                                <input <c:if test="${'1'==appGrpPremisesDto.locateWtihNonHcsa}">checked="checked"</c:if> class="form-check-input locateWtihNonHcsa"  type="radio" name="locateWtihNonHcsa${status.index}" value = "1" aria-invalid="false">
                                <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
                            </iais:value>
                            <iais:value width="4" cssClass="col-md-4 form-check" style="padding-left: 0px;">
                                <input <c:if test="${'0'==appGrpPremisesDto.locateWtihNonHcsa}">checked="checked"</c:if> class="form-check-input locateWtihNonHcsa"  type="radio" name="locateWtihNonHcsa${status.index}" value = "0" aria-invalid="false">
                                <label class="form-check-label" ><span class="check-circle"></span>No</label>
                            </iais:value>
                            <iais:value cssClass="col-xs-12">
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

                            <c:set var="hasNonHcsa" value="${appGrpPremisesDto.appPremisesOperationalUnitDtos.size()>0}" />
                            <c:if test="${hasNonHcsa}">
                            <c:forEach var="relatedDto" items="${appGrpPremisesDto.appPremNonLicRelationDtos}" varStatus="relatedStatus">
                                <iais:row cssClass="nonHcsaRow">
                                    <div class="col-xs-12 col-md-4">
                                        <iais:input maxLength="100" cssClass="coBusinessName" type="text" name="${premValue}coBusinessName${relatedStatus.index}" value="${relatedDto.busninessName}" />
                                    </div>
                                    <div class="col-xs-12 col-md-4">
                                        <iais:input maxLength="100" cssClass="coSvcName" type="text" name="${premValue}coSvcName${relatedStatus.index}" value="${relatedDto.busninessName}" />
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
                                    <iais:input maxLength="100" cssClass="coBusinessName" type="text" name="${premValue}coBusinessName0" value="" />
                                </div>
                                <div class="col-xs-12 col-md-4">
                                    <iais:input maxLength="100" cssClass="coSvcName" type="text" name="${premValue}coSvcName0" value="" />
                                </div>
                                <div class="col-xs-12 col-md-2 delNonHcsaSvcRow hiden">
                                    <div class="text-center">
                                        <p class="text-danger nonHcsaSvcDel"><em class="fa fa-times-circle del-size-36"></em></p>
                                    </div>
                                </div>
                            </iais:row>
                            </c:if>
                            <iais:row cssClass="addNonHcsaSvcRow">
                                <iais:field value="" width="5"/>
                                <iais:value cssClass="col-xs-7 col-sm-4 col-md-5 ">
                                    <span class="addNonHcsaSvc"><a style="text-decoration:none;">+ Add Non-Licensable Service</a></span>
                                </iais:value>
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
        initPremisePage($('div.premContent'));
    });

    function initPremisePage($premContent) {
        addOperationalEvnet();
        delOperationEvent();

        locateWtihNonHcsaEvent();
        addNonHcsaEvent;
        delNonHcsaEvent();

        checkAddressManatory($premContent);
        checkLocateWtihNonHcsa($premContent);
    }

    function removeAdditional($premContent) {
        $premContent.find('div.operationDivGroup .operationDiv').remove();
        $premContent.find('div.locateWtihNonHcsaRowDiv .locateWtihNonHcsaRow:not(:first)').remove();
    }

    var premSelect = function(){
        $('.premSelect').change(function () {
            showWaiting();
            clearErrorMsg();
            var premSelectVal = $(this).val();
            var $premContent = $(this).closest('div.premContent');
            //var thisId = $(this).attr('id');
            var premisesIndexNo = $premContent.find(".premisesIndexNo").val();
            clearFields($premContent);
            if ("-1" == premSelectVal) {
                hideTag($premContent.find(".new-premise-form"));
            } else if ("newPremise" == premSelectVal) {
                showTag($premContent.find(".new-premise-form"));
                removeAdditional($premContent);
                initPremisePage($premContent);
            } else {
                showTag($premContent.find(".new-premise-form"));
                var jsonData = {
                    'premIndexNo': premisesIndexNo,
                    'premSelectVal': premSelectVal,
                    'premisesType': premisesType,
                    'premiseIndex': premiseIndex
                };
                var opt = {
                    url: '${pageContext.request.contextPath}/lic-premises',
                    type: 'GET',
                    data: jsonData
                };
                opt.data = jsonData;
                callCommonAjax(opt, "premSelectCallback", $premContent);
            }
        });
    }

    function premSelectCallback(data, $premContent) {
        if (data == null) {
            dismissWaiting();
            return;
        }
        removeAdditional($premContent);
        fillForm($premContent, data);
        fillFloorUnit($premContent, data.appPremisesOperationalUnitDtos);
        fillNonHcsa($premContent, data.appPremNonLicRelationDtos);
        initPremisePage($premContent);
    }

    function checkAddressManatory($premContent) {
        var addrType = $premContent.find('.addrType').val();
        $premContent.find('.blkNoLabel .mandatory').remove();
        $premContent.find('.floorUnitLabel .mandatory').remove();
        if ('ADDTY001' == addrType) {
            $premContent.find('.blkNoLabel').append('<span class="mandatory">*</span>');
            $premContent.find('.floorUnitLabel').append('<span class="mandatory">*</span>');
        }
    }

    function checkLocateWtihNonHcsa($premContent) {
        var $row = $premContent.find('.locateWtihNonHcsaRow');
        if ($row.find('input.locateWtihNonHcsa[value="1"]').is(':checked')) {
            showTag($row.next('.nonHcsaRowDiv'));
        } else {
            hideTag($row.next('.nonHcsaRowDiv'));
        }
    }

    var locateWtihNonHcsaEvent = function (target) {
        var $target = getJqueryNode(target);
        if (isEmpty($target)) {
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
        var $parent = $premContent.find('div.nonHcsaRow');
        var len = data.length;
        for(var i = 1; i <= len; i++) {
            var $target = $parent.find('.operationDiv').eq(i - 1);
            if ($target.length == 0) {
                addFloorUnit($parent, 1);
                $target = $parent.find('.operationDiv').eq(i - 1)
            }
            fillValue($target.find('input.floorNo'), data.floorNo);
            fillValue($target.find('input.unitNo'), data.unitNo);
        }
    }

    function refreshNonHcsa(target) {
        var $target = getJqueryNode(target);
        if (isEmpty($target)) {
            return;
        }
        $target.find('.nonHcsaRow').each(function(k, v) {
            toggleTag($(this).find('.delNonHcsaSvcRow'), k != 0);
            resetIndex(v, k);
        });
        var length = $nonHcsaRowDiv.find('div.nonHcsaRow').length;
        $nonHcsaRowDiv.closest('div.premContent').find('.nonHcsaLength').val(length);
        console.log('Non Hcsa: ' + length);
    }

    function addNonHcsa(ele) {
        var $premContent = $(ele).closest('div.premContent');
        var src = $premContent.find('div.nonHcsaRow:first').clone();
        clearFields(src);
        $premContent.find('div.addNonHcsaSvcRow').before(src);
        refreshNonHcsa($premSelect.find('div.nonHcsaRowDiv'));
        delOperationEvent($premContent);
    }

    var addNonHcsaEvent = function (target) {
        var $target = getJqueryNode(target);
        if (isEmpty($target)) {
            $target = $(document);
        }
        $target.find('.addNonHcsaSvc').unbind('click');
        $target.find('.addNonHcsaSvc').on('click', function () {
            addNonHcsa(this);
        });
    }

    var delNonHcsaEvent = function (target) {
        var $target = getJqueryNode(target);
        if (isEmpty($target)) {
            $target = $(document);
        }
        $target.find('.nonHcsaSvcDel').unbind('click');
        $target.find('.nonHcsaSvcDel').not(':first').on('click', function () {
            $(this).closest('div.nonHcsaRow').remove();
            var $nonHcsaRowDiv = $(this).closest('div.nonHcsaRowDiv');
            refreshNonHcsa($nonHcsaRowDiv);
        });
    }

    function fillFloorUnit($premContent, data) {
        if (isEmpty(data) || !$.isArray(data)) {
            return;
        }
        var $parent = $premContent.find('div.operationDivGroup');
        var len = data.length;
        for(var i = 1; i <= len; i++) {
            var $target = $parent.find('.operationDiv').eq(i - 1);
            if ($target.length == 0) {
                addFloorUnit($parent, 1);
                $target = $parent.find('.operationDiv').eq(i - 1)
            }
            fillValue($target.find('input.floorNo'), data.floorNo);
            fillValue($target.find('input.unitNo'), data.unitNo);
        }
    }

    function refreshFloorUnit(operationDivGroup) {
        var $operationDivGroup = getJqueryNode(operationDivGroup);
        if (isEmpty($operationDivGroup)) {
            return;
        }
        refreshIndex($operationDivGroup);
        /*$operationDivGroup.find('.operationDiv').each(function(k, v) {
            showTag(v);
            resetIndex(v, k);
        });*/
        var length = $operationDivGroup.find('div.operationDiv').length;
        $operationDivGroup.closest('div.premContent').find('.opLength').val(length);
        console.log('Floor and Unit: ' + length);
    }

    function addFloorUnit(ele, count) {
        var $premContent = $(ele).closest('div.premContent');
        var src = $premContent.find('div.operationDiv:first').clone();
        clearFields(src);
        for (var i = 0 ; i < count; i++) {
            $premContent.find('div.addOpDiv').before(src);
        }
        refreshFloorUnit($premSelect.find('div.operationDivGroup'));
        delOperationEvent($premContent);
    }

    var addOperationalEvnet = function (target) {
        var $target = getJqueryNode(target);
        if (isEmpty($target)) {
            $target = $(document);
        }
        $target.find('.addOperational').unbind('click');
        $target.find('.addOperational').on('click', function () {
            addFloorUnit(this, 1);
        });
    }

    var delOperationEvent = function (target) {
        var $target = getJqueryNode(target);
        if (isEmpty($target)) {
            $target = $(document);
        }
        $target.find('.opDel').unbind('click');
        $target.find('div.operationDivGroup').find('.opDel').on('click', function () {
            var $operationDivGroup = $(this).closest('div.operationDivGroup');
            //var $premContent = $(this).closest('div.premContent');
            $(this).closest('div.operationDiv').remove();
            refreshFloorUnit($operationDivGroup);
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

