<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.StringUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript" src="<%=IaisEGPConstant.CSS_ROOT + IaisEGPConstant.COMMON_CSS_ROOT%>js/file-upload.js"></script>

<c:set var="permanent" value="${ApplicationConsts.PREMISES_TYPE_PERMANENT}" />
<c:set var="conv" value="${ApplicationConsts.PREMISES_TYPE_CONVEYANCE}" />
<c:set var="easMts" value="${ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE}" />
<c:set var="mobile" value="${ApplicationConsts.PREMISES_TYPE_MOBILE}" />
<c:set var="remote" value="${ApplicationConsts.PREMISES_TYPE_REMOTE}" />
<c:set var="mosdName" value="${ApplicationConsts.MODE_OF_SVC_DELIVERY}" />
<c:set var="permanentShow" value="${ApplicationConsts.PREMISES_TYPE_PERMANENT_SHOW}" />
<c:set var="convShow" value="${ApplicationConsts.PREMISES_TYPE_CONVEYANCE_SHOW}" />
<c:set var="easMtsShow" value="${ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE_SHOW}" />
<c:set var="mobileShow" value="${ApplicationConsts.PREMISES_TYPE_MOBILE_SHOW}" />
<c:set var="remoteShow" value="${ApplicationConsts.PREMISES_TYPE_REMOTE_SHOW}" />

<c:set var="premTypeLen" value="${premisesType.size()}"/>

<input class="not-refresh" type="hidden" id="isEditHiddenVal" name="isEdit" value="${!isRfi && AppSubmissionDto.appType == 'APTY002'? '1' : '0'}"/>

<c:forEach var="appGrpPremisesDto" items="${AppSubmissionDto.appGrpPremisesDtoList}" varStatus="status">
    <%--<c:set var="canEdit" value="true"/>--%>
    <div class="row form-horizontal premContent <c:if test="${!status.first}">underLine</c:if>">
        <input class="not-refresh chooseExistData" type="hidden" name="chooseExistData" value="${appGrpPremisesDto.existingData}"/>
        <input class="not-refresh isPartEdit" type="hidden" name="isPartEdit" value="0"/>
        <input class="not-refresh premIndex" type="hidden" name="premIndex" value="${status.index}"/>
        <input class="not-refresh premisesIndexNo" type="hidden" name="premisesIndexNo" value="${appGrpPremisesDto.premisesIndexNo}"/>
        <input class="not-refresh oldPremTypeValue" type="hidden" name="oldPremType" value="${appGrpPremisesDto.premisesType}"/>
        <input class="not-refresh premTypeValue" type="hidden" name="premType" value="${appGrpPremisesDto.premisesType}"/>
        <input class="not-refresh premSelValue" type="hidden" name="premSelValue" value="${appGrpPremisesDto.premisesSelect}"/>

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
            <div class="form-group">
                <div class="col-xs-12 col-md-6">
                    <p class="app-title">${mosdName} <span class="premHeader">${status.index+1}</span></p>
                    <span  id="error_premisesHci${status.index}" class="error-msg" name="iaisErrorMsg"></span>
                </div>
                <div class="col-xs-12 col-md-5 text-right removeEditDiv <c:if test="${status.first}">hidden</c:if>">
                    <c:choose>
                        <c:when test="${!isRfi && !isRfc && !isRenew}">
                            <h4 class="text-danger removeBtn"><em class="fa fa-times-circle del-size-36"></em></h4>
                        </c:when>
                        <c:when test="${(isRfi || isRfc || isRenew)}">
                            <%--<c:set var="canEdit" value="false"/>--%>
                            <c:if test="${AppSubmissionDto.appEditSelectDto.premisesEdit}">
                                <a class="premises-summary-preview premisesEdit app-font-size-16"><em class="fa fa-pencil-square-o"></em><span style="display: inline-block;">&nbsp;</span>Edit</a>
                            </c:if>
                        </c:when>
                    </c:choose>
                </div>
            </div>
            <%--<div class="form-group col-xs-12">
                <span  id="error_premisesHci${status.index}" class="error-msg" name="iaisErrorMsg"></span>
            </div>--%>
            <c:if test="${isRfi || isRenew || isRfc}">
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
            </c:if>

            <c:set var="licenceDtos" value="${appGrpPremisesDto.licenceDtos}" />
            <c:set var="selectedLicences" value="${appGrpPremisesDto.selectedLicences}" />
            <c:set var="premCount" value="${fn:length(licenceDtos)}" />
            <c:if test="${premCount > 0}">
                <div class="form-check-gp">
                    <label>
                        Please confirm if the new change would also apply to other services provided at the existing premises.<br/>
                        If yes, please tick the services that the change would be applied to:
                        <span class="mandatory">*</span>
                    </label>
                    <div class="form-check">
                        <input class="form-check-input" name="selectedLicence" type="checkbox" aria-invalid="false" id="NON" value="NON"
                               <c:if test="${StringUtil.isIn('NON', selectedLicences)}">checked</c:if>/>
                        <label class="form-check-label" for="NON"><span class="check-square"></span> Not Applicable</label>
                    </div>
                    <div class="form-check">
                        <input class="form-check-input" name="selectedLicence" type="checkbox" aria-invalid="false" id="ALL" value="ALL"
                               <c:if test="${StringUtil.isIn('ALL', selectedLicences)}">checked</c:if>/>
                        <label class="form-check-label" for="ALL"><span class="check-square"></span> Select All</label>
                    </div>
                    <c:forEach var="i" begin="0" end="${premCount - 1}">
                        <c:set var="lic" value="${licenceDtos[i]}" />
                        <div class="form-check">
                            <input class="form-check-input" name="selectedLicence" type="checkbox" aria-invalid="false" id="${lic.id}" value="${lic.id}"
                                   <c:if test="${StringUtil.isIn(lic.id, selectedLicences)}">checked</c:if>/>
                            <label class="form-check-label" for="${lic.id}">
                                <span class="check-square"></span>
                                <c:out value="${lic.svcName}" /> licence (Licence No. ${lic.licenceNo})
                            </label>
                        </div>
                    </c:forEach>
                    <div class="form-check">
                        <span class="error-msg" name="iaisErrorMsg" id="error_selectedLicences"></span>
                    </div>
                </div>
            </c:if>

            <div class="form-group premisesTypeDiv" <c:if test="${isRenew || isRfc}">hidden</c:if> >
                <iais:field value="What is your Mode of Service Delivery type" width="5" mandatory="true"/>
                <c:set var="premTypeCss" value="${premTypeLen > 2 ? 'col-md-2' : 'col-md-3'}"/>
                <c:forEach var="premType" items="${premisesType}">
                    <div class="col-xs-12 ${premTypeCss} form-check">
                        <input class="form-check-input ${StringUtil.toLowerCase(premType)} premTypeRadio"  type="radio" name="premType${status.index}" value="${premType}"
                               ${appGrpPremisesDto.premisesType==premType ? 'checked="checked"' : ''}  aria-invalid="false">
                        <label class="form-check-label" >
                            <span class="check-circle"></span>
                            <c:choose>
                                <c:when test="${premType == permanent}">
                                    <span>${permanentShow}</span>
                                    <a class="btn-tooltip styleguide-tooltip" style="z-index: 99;position: absolute; right: -25px;top: 0px" href="javascript:void(0);" data-placement="top"  data-toggle="tooltip" data-html="true" title="&lt;p&gt;<iais:message  key="NEW_ACK019"></iais:message>&lt;/p&gt;">i</a>
                                </c:when>
                                <c:when test="${premType == conv || premType == easMts}">
                                    <span>${premType == conv ? convShow : easMtsShow}</span>
                                    <a class="btn-tooltip styleguide-tooltip" style="z-index: 99;position: absolute; right: -25px;top: 0px" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="&lt;p&gt;<iais:message  key="NEW_ACK021"></iais:message>&lt;/p&gt;">i</a>
                                </c:when>
                                <c:when test="${premType == mobile}">
                                    <span>${mobileShow}</span>
                                    <a class="btn-tooltip styleguide-tooltip"  style="z-index: 99;position: absolute; right: -25px;top: 0px" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="&lt;p&gt;<iais:message  key="NEW_ACK032"></iais:message>&lt;/p&gt;">i</a>
                                </c:when>
                                <c:when test="${premType == remote}">
                                    <span>${remoteShow}</span>
                                    <a class="btn-tooltip styleguide-tooltip"  style="z-index: 99;position: absolute; right: -25px;top: 0px;" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="&lt;p&gt;<iais:message  key="NEW_ACK033"></iais:message>&lt;/p&gt;">i</a>
                                </c:when>
                            </c:choose>
                        </label>
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
                    <iais:field value="Add or select a ${permanentShow} from the list :" width="5" mandatory="true"/>
                    <iais:value id="permanentSelect" cssClass="col-xs-11 col-sm-7 col-md-5">
                        <iais:select cssClass="premSelect permanentSel" name="permanentSel${status.index}" options="permanentSelect" needSort="false" value="${appGrpPremisesDto.premisesSelect}"/>
                    </iais:value>
                </iais:row>
            </c:if>
            <c:if test="${StringUtil.isIn(conv, premisesType)}">
                <iais:row cssClass="conveyanceSelect hidden">
                    <iais:field value="Add or select a ${convShow} from the list :" width="5" mandatory="true"/>
                    <iais:value id="conveyanceSelect"  cssClass="col-xs-11 col-sm-7 col-md-5">
                        <iais:select cssClass="premSelect conveyanceSel" name="conveyanceSel${status.index}"  options="conveyancePremSel" needSort="false"  value="${appGrpPremisesDto.premisesSelect}"/>
                    </iais:value>
                </iais:row>
            </c:if>
            <c:if test="${StringUtil.isIn(easMts, premisesType)}">
                <iais:row cssClass="easMtsSelect hidden">
                    <iais:field value="Add or select a ${convShow} from the list :" width="5" mandatory="true"/>
                    <iais:value id="easMtsSelect"  cssClass="col-xs-11 col-sm-7 col-md-5">
                        <iais:select cssClass="premSelect easMtsSel" name="easMtsSel${status.index}" options="easMtsPremSel" needSort="false"  value="${appGrpPremisesDto.premisesSelect}"/>
                    </iais:value>
                </iais:row>
            </c:if>
            <c:if test="${StringUtil.isIn(mobile, premisesType)}">
                <iais:row cssClass="mobileSelect hidden">
                    <iais:field value="Add or select a ${mobileShow} from the list :" width="5" mandatory="true"/>
                    <iais:value id="mobileSelect" cssClass="col-xs-11 col-sm-7 col-md-5">
                        <iais:select cssClass="premSelect mobileSel" name="mobileSel${status.index}" options="mobilePremSel" needSort="false"  value="${appGrpPremisesDto.premisesSelect}"/>
                    </iais:value>
                </iais:row>
            </c:if>
            <c:if test="${StringUtil.isIn(remote, premisesType)}">
                <iais:row cssClass="remoteSelect hidden">
                    <iais:field value="Add or select a ${remoteShow} from the list :" width="5" mandatory="true"/>
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

            <div class="new-premise-form">
                <iais:row cssClass="vehicleRow">
                    <iais:field value="Vehicle No." mandatory="true" width="5"/>
                    <iais:value width="7" cssClass="col-md-5">
                        <iais:input maxLength="10" type="text" cssClass="vehicleNo" name="vehicleNo${status.index}" value="${appGrpPremisesDto.vehicleNo}"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field value="Business Name" mandatory="true" width="5"/>
                    <iais:value width="7" cssClass="col-xs-10 col-md-5 disabled">
                        <iais:input cssClass="hciName" maxLength="100" type="text" name="hciName${status.index}" value="${appGrpPremisesDto.hciName}"/>
                    </iais:value>
                </iais:row>

                <iais:row cssClass="postalCodeDiv">
                    <iais:field value="Postal Code" mandatory="true" width="5"/>
                    <iais:value cssClass="col-xs-10 col-md-5">
                        <iais:input cssClass="postalCode" maxLength="6" type="text" name="postalCode${status.index}" value="${appGrpPremisesDto.postalCode}"/>
                    </iais:value>
                    <div class="col-xs-7 col-sm-6 col-md-3">
                        <p><a class="retrieveAddr">Retrieve your address</a></p>
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
                        <div class=" col-xs-7 col-sm-4 col-md-1 text-center opDelDiv">
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
                                    <div class=" col-xs-7 col-sm-4 col-md-1 text-center opDelDiv">
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

                <iais:row cssClass="scdfRefNoRow">
                    <c:set var="scdfRefNoInfo"><iais:message key="NEW_ACK006" escape="false"/></c:set>
                    <iais:field value="Fire Safety & Shelter Bureau Ref No." width="5" info="${scdfRefNoInfo}"/>
                    <iais:value cssClass="col-xs-7 col-sm-4 col-md-5 fireIssuedDateDiv">
                        <iais:input maxLength="66" name="scdfRefNo${status.index}" type="text" value="${appGrpPremisesDto.scdfRefNo}"/>
                    </iais:value>
                </iais:row>
                <iais:row cssClass="certIssuedDtRow">
                    <iais:field value="Fire Safety Certificate Issued Date" width="5"/>
                    <iais:value cssClass="col-xs-7 col-sm-4 col-md-5 fireIssuedDateDiv">
                        <iais:datePicker cssClass="certIssuedDt field-date" name="certIssuedDt${status.index}" value="${appGrpPremisesDto.certIssuedDtStr}" />
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
                    <iais:field value="Public Email" mandatory="true" width="5" cssClass="pubEmailLabel"/>
                    <iais:value width="7" cssClass="col-md-5">
                        <iais:input maxLength="320" cssClass="easMtsPubEmail" type="text" name="easMtsPubEmail${status.index}"
                                    value="${appGrpPremisesDto.easMtsPubEmail}"/>
                    </iais:value>
                </iais:row>
                <iais:row cssClass="easMtsAddFields">
                    <iais:field value="Public Hotline" mandatory="true" width="5" cssClass="pubHotlineLabel"/>
                    <iais:value width="7" cssClass="col-md-5">
                        <iais:input maxLength="8" cssClass="easMtsPubHotline" type="text" name="easMtsPubHotline${status.index}"
                                    value="${appGrpPremisesDto.easMtsPubHotline}"/>
                    </iais:value>
                </iais:row>

                <div class="co-location-div">
                    <iais:row>
                        <iais:field value="Co-Location Services" width="10" />
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
                        <iais:field value="Are you co-locating with a service that is not licensed under HCSA?" mandatory="true" width="5" />
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
                            <div class="uploadFileShowDiv" id="uploadFile${status.index}ShowId">
                            </div>
                            <div class="col-xs-12 uploadFileErrorDiv">
                                <span id="error_uploadFile${status.index}Error" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                            <br/>
                            <a class="btn btn-file-upload file-upload btn-secondary">Upload</a>
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
</c:forEach>

<div id="selectFileDiv"></div>
<%@include file="/WEB-INF/jsp/iais/application/common/premFun.jsp" %>

