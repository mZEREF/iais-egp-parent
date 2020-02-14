<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<webui:setLayout name="iais-internet"/>
<%@ include file="./dashboard.jsp" %>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
  <%@ include file="/include/formHidden.jsp" %>

  <%--Validation fields Start--%>
  <input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator"/>
  <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.ApplicationValidateDto"/>
  <input type="hidden" name="valProfiles" id="valProfiles" value=""/>
  <%--Validation fields End--%>
  <div class="main-content">
    <div class="container">
      <div class="row">
        <div class="col-xs-12">
          <div class="tab-gp steps-tab">
            <%@ include file="./navTabs.jsp" %>
            
            <div class="tab-content  ">
              <div class="tab-pane active" id="premisesTab" role="tabpanel">
                <c:if test="${'APTY005' ==AppSubmissionDto.appType}">
                  <c:forEach var="clickEditPage" items="${AppSubmissionDto.clickEditPage}">
                    <c:if test="${'APPPN01' == clickEditPage}">
                      <c:set var="isClickEdit" value="true"/>
                    </c:if>
                  </c:forEach>
                  <c:choose>
                    <c:when test="${'true' != isClickEdit}">
                      <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
                    </c:when>
                    <c:otherwise>
                      <input id="isEditHiddenVal" type="hidden" name="isEdit" value="1"/>
                    </c:otherwise>
                  </c:choose>
                  <c:if test="${'true' != isClickEdit}">
                    <c:set var="showPreview" value="true"/>
                    <c:forEach var="amendType"  items="${AppSubmissionDto.amendTypes}">
                      <c:if test="${amendType =='RFCATYPE01'}">
                        <c:set var="canEdit" value="1"/>
                      </c:if>
                    </c:forEach>
                    <div class="premises-summary-preview <c:if test="${'true' != showPreview}">hidden</c:if>">
                      <c:choose>
                        <c:when test="${'1' == canEdit}">
                          <p class="text-right"><a id="edit"><em class="fa fa-pencil-square-o"></em>Edit</a></p>
                        </c:when>
                        <c:otherwise>
                          <p class="text-right" style="color: gray"><em class="fa fa-pencil-square-o"></em>Edit</p>
                        </c:otherwise>
                      </c:choose>
                      <c:forEach var="appGrpPremDto" items="${AppSubmissionDto.appGrpPremisesDtoList}" varStatus="stat">
                        <h3 class="without-header-line">Premises ${stat.index+1}</h3>
                        <p class="premise-address-gp"> 
                          <span class="premise-type">
                            <strong>
                              <c:if test="${'ONSITE' == appGrpPremDto.premisesType}">
                                <c:out value="On-site"/>
                              </c:if>
                              <c:if test="${'CONVEYANCE' == appGrpPremDto.premisesType}">
                                <c:out value="Conveyance"/>
                              </c:if>
                              :
                            </strong>
                          </span>
                          <span class="premise-address">
                            <c:out value="${appGrpPremDto.address}"/>
                          </span>
                        </p>
                        <c:if test="${'CONVEYANCE' == appGrpPremDto.premisesType}">
                          <p class="vehicle-txt hidden"><strong>Vehicle No:</strong> <span class="vehicle-info">${appGrpPremDto.conveyanceVehicleNo}</span></p>
                        </c:if>
                      </c:forEach>
                    </div>
                  </c:if>
                </c:if>
                <div class="premises-content <c:if test="${'true' == showPreview}">hidden</c:if>" >
                <div class="row ">
                  <div class="col-xs-12">
                    <div class="premises-txt">
                      <p>Premises are your service operation sites that can either be at a fixed address<strong> - &#34;on-site&#34;</strong>, or in a mobile clinic or ambulance<strong> - &#34;conveyance&#34;</strong>.</p>
                    </div>
                  </div>
                </div>
                <c:forEach var="appGrpPremisesDto" items="${AppSubmissionDto.appGrpPremisesDtoList}" varStatus="status">
                  <c:set value="${errorMap_premises[premIndexNo]}" var="errMsg"/>
                  <div class="row premContent <c:if test="${!status.first}">underLine</c:if>  " id="mainPrem">
                    <c:set var="onSite" value="ONSITE" ></c:set>
                    <c:set var="conv" value="CONVEYANCE" ></c:set>
                    <input class="premValue" type="hidden" name="premValue" value="${status.index}"/>
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
                        <div class="form-group premisesTypeDiv" id="premisesType" <c:if test="${'APTY005' ==AppSubmissionDto.appType}">hidden</c:if> >
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

                          <c:if test="${!status.first && requestInformationConfig==null}">
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
                            <span class="error-msg" name="iaisErrorMsg" id="error_premisesType${status.index}"></span>
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
                                <iais:select cssClass="premSelect" id="conveyanceSel" name="conveyanceSelect"  options="conveyancePremSel" value="${appGrpPremisesDto.premisesSelect}"></iais:select>
                              </c:otherwise>
                            </c:choose>
                          </iais:value>
                        </iais:row>
                        <span class="error-msg" id="error_premisesSelect${status.index}" name="iaisErrorMsg"></span>
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
                            <iais:value width="11">
                              <iais:input maxLength="100" type="text" name="onSiteHciName" id="sitePremiseName" value="${appGrpPremisesDto.hciName}"></iais:input>
                              <span id="error_hciName${status.index}" name="iaisErrorMsg" class="error-msg"></span>
                            </iais:value>
                          </iais:row>
                          <iais:row cssClass="postalCodeDiv">
                            <iais:field value="Postal Code " mandatory="true" width="12"/>
                            <iais:value width="5">
                              <iais:input cssClass="sitePostalCode" maxLength="6" type="text"  name="onSitePostalCode"  value="${appGrpPremisesDto.postalCode}"></iais:input>
                              <span  id="error_postalCode${status.index}" class="error-msg" name="iaisErrorMsg"></span>
                            </iais:value>
                            <div class="col-xs-7 col-sm-6 col-md-4">
                              <p><a class="retrieveAddr" >Retrieve your address</a></p>
                            </div>

                          </iais:row>
                          <iais:row>
                            <iais:field value="Address Type " mandatory="true" width="12"/>
                            <iais:value id="onSiteAddressType${premValue}" cssClass="col-xs-7 col-sm-4 col-md-3">
                              <iais:select cssClass="siteAddressType" name="onSiteAddressType" id="siteAddressType" codeCategory="CATE_ID_ADDRESS_TYPE" firstOption="Please Select" value="${appGrpPremisesDto.addrType}"></iais:select>
                              <span class="error-msg" name="iaisErrorMsg" id="error_addrType${status.index}"></span>
                            </iais:value>
                          </iais:row>
                          <iais:row>
                            <iais:field value="Block / House No." width="12"/>
                            <iais:value width="5">
                              <iais:input cssClass="siteBlockNo" maxLength="10"  type="text" name="onSiteBlkNo" id="siteBlockNo" value="${appGrpPremisesDto.blkNo}"></iais:input>
                              <span class="error-msg" name="iaisErrorMsg" id="error_blkNo${status.index}"></span>
                            </iais:value>
                          </iais:row>
                          <iais:row>
                            <iais:field value="Floor No." width="12"/>
                            <iais:value cssClass="col-xs-7 col-sm-4 col-md-3 input-with-label">
                              <iais:input maxLength="3" type="text" name="onSiteFloorNo" id="siteFloorNo" value="${appGrpPremisesDto.floorNo}"></iais:input>
                              <span class="error-msg" name="iaisErrorMsg" id="error_floorNo${status.index}"></span>
                            </iais:value>
                          </iais:row>
                          <iais:row>
                            <iais:field value="Unit No." width="12"/>
                            <iais:value cssClass="col-xs-7 col-sm-4 col-md-3 input-with-label">
                              <iais:input maxLength="5" type="text" name="onSiteUnitNo" id="siteUnitNo" value="${appGrpPremisesDto.unitNo}"></iais:input>
                              <span class="error-msg" name="iaisErrorMsg" id="error_unitNo${status.index}"></span>
                            </iais:value>
                          </iais:row>
                          <iais:row>
                            <iais:field value="Building Name" width="12"/>
                            <iais:value width="11" cssClass="input-with-label">
                              <iais:input cssClass="siteBuildingName" maxLength="45" type="text" name="onSiteBuildingName" id="siteBuildingName" value="${appGrpPremisesDto.buildingName}"></iais:input>
                              <span class="error-msg" name="iaisErrorMsg" id="error_buildingName"></span>
                            </iais:value>
                          </iais:row>
                          <iais:row>
                            <iais:field value="Street Name " mandatory="true" width="10"/>
                            <iais:value width="10">
                              <iais:input cssClass="siteStreetName" maxLength="32" type="text" name="onSiteStreetName" id="siteStreetName" value="${appGrpPremisesDto.streetName}"></iais:input>
                              <span class="error-msg" name="iaisErrorMsg" id="error_streetName${status.index}"></span>
                            </iais:value>
                          </iais:row>
                          <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="siteSafefyNo">Fire Safety Shelter Bureau Ref. No. <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" title="&lt;p&gt;This is a xxx digit No. that you can access from the Life Saving Force Portral.&lt;/p&gt;">i</a></label>
                            <div class="col-xs-9 col-sm-5 col-md-4">
                              <input id="siteSafefyNo" name="onSiteScdfRefNo" type="text" value="${appGrpPremisesDto.scdfRefNo}">
                            </div>
                          </div>
                          <iais:row>
                            <iais:field value="Fire Safety Certificate Issued Date" width="12"/>
                            <iais:value cssClass="col-xs-7 col-sm-4 col-md-3">
                              <iais:datePicker cssClass="fireIssuedDate" name="onSiteFireSafetyCertIssuedDate" value="${appGrpPremisesDto.certIssuedDtStr}" />
                            </iais:value>
                          </iais:row>
                          <iais:row>
                            <iais:field value="Office Telephone No " mandatory="true" width="12"/>
                            <iais:value cssClass="col-xs-7 col-sm-4 col-md-3">
                              <iais:input type="text" name="onSiteOffTelNo" maxLength="8" value="${appGrpPremisesDto.offTelNo}" id="onsitOffice" cssClass="onsitOffice" />
                              <span class="error-msg" name="iaisErrorMsg" id="error_offTelNo${status.index}"></span>
                            </iais:value>
                          </iais:row>
                          <iais:row cssClass="other-lic-content">
                            <iais:field value="Are you co-locating with another licensee?" mandatory="true" width="12"/>
                            <iais:value cssClass="col-xs-7 col-sm-4 col-md-6">
                              <input type="hidden" name="onSiteIsOtherLic" value="${appGrpPremisesDto.isOtherLic}"/>
                              <div class="form-check col-sm-3">
                                <input <c:if test="${'0'==appGrpPremisesDto.isOtherLic}">checked="checked"</c:if> class="form-check-input other-lic"  type="radio" name="otherLicence${status.index}" value = "0" aria-invalid="false">
                                <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
                              </div>
                              <div class="form-check col-sm-3">
                                <input <c:if test="${'1'==appGrpPremisesDto.isOtherLic}">checked="checked"</c:if> class="form-check-input other-lic"  type="radio" name="otherLicence${status.index}" value = "1" aria-invalid="false">
                                <label class="form-check-label" ><span class="check-circle"></span>No</label>
                              </div>
                              <span class="error-msg" name="iaisErrorMsg" id="error_isOtherLic${status.index}"></span>
                            </iais:value>
                          </iais:row>
                          <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label">
                              Operating Hours (Start) <span class="mandatory">*</span>
                            </label>

                            <div class="col-xs-9 col-sm-5 col-md-4">
                              <input type="text" value="${appGrpPremisesDto.onsiteStartHH}" maxlength="2" style="width: 60px" name="onSiteStartHH"/>(HH)
                              :
                              <input type="text" value="${appGrpPremisesDto.onsiteStartMM}" maxlength="2" style="width: 60px"  name="onSiteStartMM"/>(MM)
                              <span class="error-msg" name="iaisErrorMsg" id="error_onsiteStartMM${status.index}"></span>
                            </div>
                          </div>
                          <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label">
                              Operating Hours (End) <span class="mandatory">*</span>
                            </label>

                            <div class="col-xs-9 col-sm-5 col-md-4">
                              <input type="text" value="${appGrpPremisesDto.onsiteEndHH}" maxlength="2" style="width: 60px" name="onSiteEndHH"/>(HH)
                              :
                              <input type="text" value="${appGrpPremisesDto.onsiteEndMM}" maxlength="2" style="width: 60px" name="onSiteEndMM"/>(MM)
                              <span class="error-msg" name="iaisErrorMsg" id="error_onsiteEndMM${status.index}"></span>
                            </div>
                          </div>
                          <c:choose>
                            <c:when test="${appGrpPremisesDto.appPremPhOpenPeriodList.size()>0 && 'ONSITE'== appGrpPremisesDto.premisesType}">
                              <c:forEach var="ph" items="${appGrpPremisesDto.appPremPhOpenPeriodList}" varStatus="phStat" >
                                <div class="pubHolidayContent">
                                  <iais:row>
                                    <iais:field value="Select Public Holiday" width="12"/>
                                    <iais:value cssClass="col-xs-7 col-sm-4 col-md-3">
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
                                    <div class="col-xs-9 col-sm-5 col-md-4">
                                      <input class="PbHolDayStartHH" type="text" name="${premValue}onSitePbHolDayStartHH${phStat.index}" value="${ph.onsiteStartFromHH}" maxlength="2" style="width: 60px"/>(HH)
                                      :
                                      <input class="PbHolDayStartMM" type="text" name="${premValue}onSitePbHolDayStartMM${phStat.index}" value="${ph.onsiteStartFromMM}" maxlength="2" style="width: 60px"/>(MM)
                                    </div>
                                  </div>
                                  <div class="form-group">
                                    <label class="col-xs-12 col-md-4 control-label">
                                      Public Holidays Operating Hours (End)
                                    </label>
                                    <div class="col-xs-9 col-sm-5 col-md-4">
                                      <input class="PbHolDayEndHH" type="text" name="${premValue}onSitePbHolDayEndHH${phStat.index}" value="${ph.onsiteEndToHH}" maxlength="2" style="width: 60px"/>(HH)
                                      :
                                      <input class="PbHolDayEndMM" type="text" name="${premValue}onSitePbHolDayEndMM${phStat.index}" value="${ph.onsiteEndToMM}" maxlength="2" style="width: 60px"/>(MM)
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
                                  <iais:value cssClass="col-xs-7 col-sm-4 col-md-3">
                                    <iais:datePicker cssClass="form_datetime public-holiday"  name="${premValue}onSitePubHoliday0"/>
                                  </iais:value>
                                </iais:row>
                                <div class="form-group">
                                  <label class="col-xs-12 col-md-4 control-label">
                                    Public Holidays Operating Hours (Start)
                                  </label>
                                  <div class="col-xs-9 col-sm-5 col-md-4">
                                    <input class="PbHolDayStartHH" type="text" name="${premValue}onSitePbHolDayStartHH0" value="" maxlength="2" style="width: 60px"/>(HH)
                                    :
                                    <input class="PbHolDayStartMM" type="text" name="${premValue}onSitePbHolDayStartMM0" value="" maxlength="2" style="width: 60px"/>(MM)
                                    <span  class="error-msg"  name="iaisErrorMsg" id="error_"></span>
                                  </div>
                                </div>
                                <div class="form-group">
                                  <label class="col-xs-12 col-md-4 control-label">
                                    Public Holidays Operating Hours (End)
                                  </label>
                                  <div class="col-xs-9 col-sm-5 col-md-4">
                                    <input class="PbHolDayEndHH" type="text" name="${premValue}onSitePbHolDayEndHH0" value="" maxlength="2" style="width: 60px"/>(HH)
                                    :
                                    <input class="PbHolDayEndMM" type="text" name="${premValue}onSitePbHolDayEndMM0" value="" maxlength="2" style="width: 60px"/>(MM)
                                  </div>
                                </div>

                              </div>
                            </c:otherwise>
                          </c:choose>
                          <div class="form-group">
                            <div class="col-xs-9 col-sm-5 col-md-4">
                              <button class="addPubHolDay" type="button">add Public Holiday</button>
                            </div>
                          </div> 
                        </div>
                      </div>
                      <div class="new-premise-form-conv hidden ">
                        <div class="form-horizontal">
                          <iais:row>
                            <iais:field value="Vehicle No. " mandatory="true" width="12"/>
                            <iais:value width="11">
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
                            <iais:value width="5">
                              <iais:input maxLength="6" cssClass="sitePostalCode" type="text" name="conveyancePostalCode"  value="${appGrpPremisesDto.conveyancePostalCode}"></iais:input>
                              <span  class="error-msg" name="iaisErrorMsg" id="error_conveyancePostalCode${status.index}"></span>
                            </iais:value>
                            <div class="col-xs-7 col-sm-6 col-md-4">
                              <p><a class="retrieveAddr" id="conveyance">Retrieve your address</a></p>
                            </div>

                          </iais:row>
                          <iais:row>
                            <iais:field value="Address Type " mandatory="true" width="12"/>
                            <iais:value id="conveyanceAddrType${premValue}" cssClass="col-xs-7 col-sm-4 col-md-3">
                              <iais:select name="conveyanceAddrType" cssClass="conveyanceAddressType" id="siteAddressType" codeCategory="CATE_ID_ADDRESS_TYPE" firstOption="Please Select" value="${appGrpPremisesDto.conveyanceAddressType}"></iais:select>
                              <span  class="error-msg" name="iaisErrorMsg" id="error_conveyanceAddressType${status.index}"></span>
                            </iais:value>
                          </iais:row>
                          <iais:row>
                            <iais:field value="Block / House No." width="12"/>
                            <iais:value width="5">
                              <iais:input maxLength="10" cssClass="conveyanceBlockNo" type="text" name="conveyanceBlockNo" id="conveyanceBlockNo" value="${appGrpPremisesDto.conveyanceBlockNo}"></iais:input>
                              <span  class="postalCodeMsg error-msg"></span>
                            </iais:value>
                          </iais:row>
                          <iais:row>
                            <iais:field value="Floor No." width="12"/>
                            <iais:value cssClass="col-xs-7 col-sm-4 col-md-3 input-with-label">
                              <iais:input maxLength="3" type="text" name="conveyanceFloorNo" id="conveyanceFloorNo" value="${appGrpPremisesDto.conveyanceFloorNo}"></iais:input>
                              <span  class="error-msg" name="iaisErrorMsg" id="error_conveyanceFloorNo${status.index}"></span>
                            </iais:value>
                          </iais:row>
                          <iais:row>
                            <iais:field value="Unit No." width="12"/>
                            <iais:value cssClass="col-xs-7 col-sm-4 col-md-3 input-with-label">
                              <iais:input maxLength="5" type="text" name="conveyanceUnitNo"  value="${appGrpPremisesDto.conveyanceUnitNo}"></iais:input>
                              <span  class="error-msg" name="iaisErrorMsg" id="error_conveyanceUnitNo${status.index}"></span>
                            </iais:value>
                          </iais:row>
                          <iais:row>
                            <iais:field value="Building Name " width="12"/>
                            <iais:value cssClass="col-xs-11 col-sm-7 col-md-6 input-with-label">
                              <iais:input maxLength="45" cssClass="conveyanceBuildingName" type="text" name="conveyanceBuildingName" id="conveyanceBuildingName" value="${appGrpPremisesDto.conveyanceBuildingName}"></iais:input>
                              <span  class="error-msg"></span>
                            </iais:value>
                          </iais:row>
                          <iais:row>
                            <iais:field value="Street Name " mandatory="true" width="10"/>
                            <iais:value width="10">
                              <iais:input maxLength="32" cssClass="conveyanceStreetName" type="text" name="conveyanceStreetName"  value="${appGrpPremisesDto.conveyanceStreetName}"></iais:input>
                              <span  class="error-msg" name="iaisErrorMsg" id="error_conveyanceStreetName${status.index}"></span>
                            </iais:value>
                          </iais:row>
                          <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label">
                              Operating Hours (Start) <span class="mandatory">*</span>
                            </label>
                            <div class="col-xs-9 col-sm-5 col-md-4">
                              <input type="text" value="${appGrpPremisesDto.conStartHH}" maxlength="2" style="width: 60px" name="conveyanceStartHH"/>(HH)
                              :
                              <input type="text" value="${appGrpPremisesDto.conStartMM}" maxlength="2" style="width: 60px" name="conveyanceStartMM"/>(MM)
                              <span class="error-msg" name="isaiErrorMsg" id="error_conStartMM${status.index}"></span>
                            </div>

                        </div>
                          <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label">
                              Operating Hours (End) <span class="mandatory">*</span>
                            </label>
                            <div class="col-xs-9 col-sm-5 col-md-4">
                              <input type="text" value="${appGrpPremisesDto.conEndHH}" maxlength="2" style="width: 60px" name="conveyanceEndHH"/>(HH)
                              :
                              <input type="text" value="${appGrpPremisesDto.conEndMM}" maxlength="2" style="width: 60px" name="conveyanceEndMM"/>(MM)
                              <span class="error-msg" name="iaisErrorMsg" id="error_conEndMM${status.index}"></span>
                            </div>
                          </div>
                          <c:choose>
                            <c:when test="${appGrpPremisesDto.appPremPhOpenPeriodList.size()>0 && 'CONVEYANCE'== appGrpPremisesDto.premisesType}">
                              <c:forEach var="ph" items="${appGrpPremisesDto.appPremPhOpenPeriodList}" varStatus="phStat" >
                                <div class="pubHolidayContent">
                                  <iais:row>
                                    <iais:field value="Select Public Holiday" width="12"/>
                                    <iais:value cssClass="col-xs-7 col-sm-4 col-md-3">
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
                                      <input class="PbHolDayStartHH" type="text" name="${premValue}conveyancePbHolDayStartHH${phStat.index}" value="${ph.convStartFromHH}" maxlength="2" style="width: 60px"/>(HH)
                                      :
                                      <input class="PbHolDayStartMM" type="text" name="${premValue}conveyancePbHolDayStartMM${phStat.index}" value="${ph.convStartFromMM}" maxlength="2" style="width: 60px"/>(MM)
                                    </div>
                                  </div>
                                  <div class="form-group">
                                    <label class="col-xs-12 col-md-4 control-label">
                                      Public Holidays Operating Hours (End)
                                    </label>
                                    <div class="col-xs-9 col-sm-5 col-md-4">
                                      <input class="PbHolDayEndHH" type="text" name="${premValue}conveyancePbHolDayEndHH${phStat.index}" value="${ph.convEndToHH}" maxlength="2" style="width: 60px"/>(HH)
                                      :
                                      <input class="PbHolDayEndMM" type="text" name="${premValue}conveyancePbHolDayEndMM${phStat.index}" value="${ph.convEndToMM}" maxlength="2" style="width: 60px"/>(MM)
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
                                  <iais:value cssClass="col-xs-7 col-sm-4 col-md-3">
                                    <iais:datePicker cssClass="form_datetime public-holiday"  name="${premValue}conveyancePubHoliday0"/>
                                  </iais:value>
                                </iais:row>
                                <div class="form-group">
                                  <label class="col-xs-12 col-md-4 control-label">
                                    Public Holidays Operating Hours (Start)
                                  </label>
                                  <div class="col-xs-9 col-sm-5 col-md-4">
                                    <input class="PbHolDayStartHH" type="text" name="${premValue}conveyancePbHolDayStartHH0" value="" maxlength="2" style="width: 60px"/>(HH)
                                    :
                                    <input class="PbHolDayStartMM" type="text" name="${premValue}conveyancePbHolDayStartMM0" value="" maxlength="2" style="width: 60px"/>(MM)
                                  </div>
                                </div>
                                <div class="form-group">
                                  <label class="col-xs-12 col-md-4 control-label">
                                    Public Holidays Operating Hours (End)
                                  </label>
                                  <div class="col-xs-9 col-sm-5 col-md-4">
                                    <input class="PbHolDayEndHH" type="text" name="${premValue}conveyancePbHolDayEndHH0" value="" maxlength="2" style="width: 60px"/>(HH)
                                    :
                                    <input class="PbHolDayEndMM" type="text" name="${premValue}conveyancePbHolDayEndMM0" value="" maxlength="2" style="width: 60px"/>(MM)
                                  </div>
                                </div>
                              </div>
                            </c:otherwise>
                          </c:choose>
                          <div class="form-group">
                            <div class="col-xs-9 col-sm-5 col-md-4">
                              <button class="addPubHolDay" type="button">Add Public Holiday</button>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </c:forEach>
                </div>
                <div class="row">
                  <div class="col-xs-12">
                    <c:if test="${requestInformationConfig==null && 'APTY005' !=AppSubmissionDto.appType && !multiBase}">
                      <button id="addPremBtn" type="button">Add Premises</button>
                    </c:if>
                  </div>
                </div>
                <div class="application-tab-footer">
                  <div class="row">
                    <div class="col-xs-12 col-sm-6 ">
                      <p><a class="back hidden" href="#"><em class="fa fa-angle-left"></em> Back</a></p>
                    </div>
                    <div class="col-xs-12 col-sm-6">
                      <div class="button-group">
                        <c:if test="${requestInformationConfig==null}">
                        <a class="btn btn-secondary premiseSaveDraft" id="SaveDraft" >Save as Draft</a>
                        </c:if>
                        <a class="btn btn-primary next premiseId" id="Next" >Next</a></div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <%--Validation Field--%>
  <%@ include file="/include/validation.jsp" %>
  <input type="hidden" name="pageCon" value="valPremiseList" >
</form>
<script type="text/javascript">

    $(document).ready(function() {
        var checkedType = "";

        $('.prem-summary').addClass('hidden');

        $('.table-condensed').css("background-color","#d9edf7");


        $('.premTypeValue').each(function (k,v) {
            checkedType = $(this).val();
            $premCountEle = $(this).closest('div.premContent');
            if('ONSITE'==checkedType){
                $premCountEle.find('.onSiteSelect').removeClass('hidden');
                $premCountEle.find('.conveyanceSelect').addClass('hidden');
                $premCountEle.find('.new-premise-form-conv').addClass('hidden');
                var premSelValue =  $premCountEle.find('.onSiteSelect .premSelect').val();
                if(premSelValue == "newPremise"){
                    $premCountEle.find('.new-premise-form-on-site').removeClass('hidden');
                }
            }else if('CONVEYANCE' == checkedType){
                $premCountEle.find('.conveyanceSelect').removeClass('hidden');
                $premCountEle.find('.onSiteSelect').addClass('hidden');
                $premCountEle.find('.new-premise-form-on-site').addClass('hidden');
                var premSelValue =  $premCountEle.find('.conveyanceSelect .premSelect').val();
                if(premSelValue =="newPremise"){
                    $premCountEle.find('.new-premise-form-conv').removeClass('hidden');
                }
            }
        });

        premType();

        premSelect();

        retrieveAddr();

        removePremises();

        doEdit();

        otherLic();

        addPubHolDay();

        removePH();
        //Binding method
        $('#Next').click(function(){
          submit('documents',null,null);
        });
        $('#SaveDraft').click(function(){
          submit('premises','saveDraft',null);
        });

        if(<c:out value="${errorMap_premises eq null}"/>){
          $('#premisesli').removeClass('incomplete');
        }else{
          $('#premisesli').addClass('incomplete');
        }

        //request for information
        // if($('#isPremisesEdit').val() == 'true'){
        //     disabledPage();
        // }

        <c:if test="${AppSubmissionDto.appEditSelectDto!=null && !AppSubmissionDto.appEditSelectDto.premisesEdit}">
        disabledPage();
        </c:if>

        <%--<c:if test="${'APTY005' ==AppSubmissionDto.appType}">
          $('input[name="onSiteHciName"]').prop('disabled',true);
        </c:if>--%>
    });


    var premType = function () {
        $('.premTypeRadio').click(function () {
            var checkedType = $(this).val();
            $premSelect = $(this).closest('div.premContent');
            $premSelctDivEle = $(this).closest('div.premisesTypeDiv');
            if('ONSITE'==checkedType){
                $premSelect.find('.onSiteSelect').removeClass('hidden');
                $premSelect.find('.conveyanceSelect').addClass('hidden');
                $premSelect.find('.new-premise-form-conv').addClass('hidden');
                $premSelctDivEle.find('.premTypeValue').val(checkedType);
                <!--change hidden length value -->
                var length =  $premSelect.find('.new-premise-form-on-site div.pubHolidayContent').length;
                $premSelect.find('.phLength').val(length);
                
            }else if('CONVEYANCE' == checkedType){
                $premSelect.find('.conveyanceSelect').removeClass('hidden');
                $premSelect.find('.onSiteSelect').addClass('hidden');
                $premSelect.find('.new-premise-form-on-site').addClass('hidden');
                $premSelctDivEle.find('.premTypeValue').val(checkedType);
                <!--change hidden length value -->
                var length =  $premSelect.find('.new-premise-form-conv div.pubHolidayContent').length;
                $premSelect.find('.phLength').val(length);
            }

        });
    }


    var premSelect = function(){
        $('.premSelect').change(function () {
            var premSelectVal = $(this).val();
            $premSelect = $(this).closest('div.premContent');
            var thisId = $(this).attr('id');
            if("newPremise" == premSelectVal){
                $premSelect.find('.new-premise-form-on-site').removeClass('hidden');
                $premSelect.find('.new-premise-form-conv').addClass('hidden');
                if("onSiteSel" == thisId){
                    $premSelect.find('.new-premise-form-on-site').removeClass('hidden');
                    $premSelect.find('.new-premise-form-conv').addClass('hidden');
                }else if ("conveyanceSel" == thisId) {
                    $premSelect.find('.new-premise-form-conv').removeClass('hidden');
                    $premSelect.find('.new-premise-form-on-site').addClass('hidden');
                }
            }else if("-1" == premSelectVal){
                $premSelect.find('.new-premise-form-conv').addClass('hidden');
                $premSelect.find('.new-premise-form-on-site').addClass('hidden');
            }


        });
    }

var retrieveAddr = function(){
    $('.retrieveAddr').click(function(){
        $postalCodeEle = $(this).closest('div.postalCodeDiv');
        $premContent = $(this).closest('div.premContent');
        var postalCode = $postalCodeEle.find('.sitePostalCode').val();
        var thisId = $(this).attr('id');
       //alert(postalCode);
        var re=new RegExp('^[0-9]*$');
        var errMsg = '';
        if(''== postalCode ){
            errMsg = 'the postal code could not be null';
        }else if(postalCode.length != 6){
            errMsg = 'the postal code length must be 6';
        }else if(!re.test(postalCode)){
            errMsg = 'the postal code must be numbers';
        }
        if("" != errMsg){
            $postalCodeEle.find('.postalCodeMsg').html(errMsg);
            return;
        }
        var data = {
            'postalCode':postalCode
        };
        $.ajax({
            'url':'${pageContext.request.contextPath}/retrieve-address',
            'dataType':'json',
            'data':data,
            'type':'GET',
            'success':function (data) {
                if(data == null){
                    $postalCodeEle.find('.postalCodeMsg').html("the postal code information could not be found");
                    return;
                }
                if("conveyance" == thisId){
                    $premContent.find('.conveyanceBlockNo').val(data.blkHseNo);
                    $premContent.find('.conveyanceStreetName').val(data.streetName);
                    $premContent.find('.conveyanceBuildingName').val(data.buildingName);
                }else{
                    $premContent.find('.siteBlockNo').val(data.blkHseNo);
                    $premContent.find('.siteStreetName').val(data.streetName);
                    $premContent.find('.siteBuildingName').val(data.buildingName);
                    if(null == data.addressType || ''== data.addressType){


                    }else{


                    }

                    $premContent.find('.siteBlockNo').prop('readonly',true);
                    $premContent.find('.siteStreetName').prop('readonly',true);
                    $premContent.find('.siteBuildingName').prop('readonly',true);
                }

            },
            'error':function () {
                $postalCodeEle.find('.postalCodeMsg').html("the postal code information could not be found");
            }
        });

    });
}





    //add premises testing.......
    $('#addPremBtn').click(function () {
        //console.log($('.premContent').html());
        var data = {
            'currentLength':$('.premContent').length
        };
        $.ajax({
            'url':'${pageContext.request.contextPath}/premises-html',
            'data':data,
            'dataType':'text',
            'type':'GET',
            'success':function (data) {
                $('div.premContent:last').after(data);
                premType();

                premSelect();

                removePremises();

                retrieveAddr();
                
                $('.addPubHolDay').unbind('click');
                addPubHolDay();
                
                removePH();

                otherLic();
                
                $('.date_picker').datepicker({
                    format:"dd/mm/yyyy"
                });

            },
            'error':function (data) {

            }
        });
    });

  var removePremises = function () {
      $('.removeBtn').click(function () {
          $removeEle= $(this).closest('div.premContent');
          $removeEle.remove();
      });

  }
  
  var doEdit = function () {
      $('#edit').click(function () {
          $('.premises-summary-preview').addClass('hidden');
          $('.premises-content').removeClass('hidden');
          $('#isEditHiddenVal').val('1');
      });
  }
  
  var otherLic = function () {
      $('.other-lic').click(function () {
          var val = $(this).val();
          $otherLicEle = $(this).closest('div.other-lic-content');
          $otherLicEle.find('input[name="onSiteIsOtherLic"]').val(val);
      });
      
  }
  
  var addPubHolDay = function () {
      $('.addPubHolDay').click(function () {
         var name = $(this).closest('div.premContent').find('.premTypeValue').val();
         if('ONSITE' == name){
             name = 'onSite';
         }else if('CONVEYANCE' == name){
             name = "conveyance";
         }
         //alert("name:"+name);
          //onsite conv
         var pubHolDayHtml = "<div class=\"pubHolidayContent\">" 
           + " <div class=\"form-group\">"
           + "<label class=\"col-xs-12 col-md-4 control-label\">Select Public Holiday</label>"
           + "<div class=\" col-xs-7 col-sm-4 col-md-3\">"
           + "<input type=\"text\" autocomplete=\"off\" class=\"date_picker form-control form_datetime public-holiday \" name=\""+name+"PubHoliday1\" data-date-start-date=\"01/01/1900\" placeholder=\"dd/mm/yyyy\" maxlength=\"10\"><span id=\"error_onsitePubHoliday\" name=\"iaisErrorMsg\" class=\"error-msg\"></span>"
           + "</div>" 
           + "<div class=\" col-xs-7 col-sm-4 col-md-3\">"
           + "<div class=\"form-check\">"  
           + "<strong class=\"removePhBtn\">X</strong>"
           + "</div>"
           + "</div>"
           + "<div class=\"clear\"></div></div>"
           + "<div class=\"form-group\">"
           + "<label class=\"col-xs-12 col-md-4 control-label\">"
           + " Public Holidays Operating Hours (Start)"
           + "</label>"
           + "<div class=\"col-xs-9 col-sm-5 col-md-4\">"
           + "<input type=\"text\" class=\"PbHolDayStartHH\" name=\""+name+"PbHolDayStartHH1\" value=\"\" maxlength=\"2\" style=\"width: 60px\">(HH) :"
           + "<input type=\"text\" class=\"PbHolDayStartMM\" name=\""+name+"PbHolDayStartMM1\" value=\"\" maxlength=\"2\" style=\"width: 60px\">(MM)"
           + " </div>"
           + "</div>"
           + "<div class=\"form-group\">"
           + "<label class=\"col-xs-12 col-md-4 control-label\">"
           + "Public Holidays Operating Hours (End)"
           + "</label>"
           + "<div class=\"col-xs-9 col-sm-5 col-md-4\">"
           + "<input type=\"text\" class=\"PbHolDayEndHH\" name=\""+name+"PbHolDayEndHH1\" value=\"\" maxlength=\"2\" style=\"width: 60px\">(HH) :"
           + "<input type=\"text\" class=\"PbHolDayEndMM\" name=\""+name+"PbHolDayEndMM1\" value=\"\" maxlength=\"2\" style=\"width: 60px\">(MM)"
           + "</div>"
           + "</div>"
           + "</div>";
          $contentDivEle = $(this).closest('div.form-horizontal');
          $contentDivEle.find('div.pubHolidayContent:last').after(pubHolDayHtml);

          $('.date_picker').datepicker({
              format:"dd/mm/yyyy"
          });

          <!--change hidden length value -->
          var length = $contentDivEle.find('div.pubHolidayContent').length;
          $(this).closest('div.premContent').find('.phLength').val(length);
          
          //Prevent duplicate binding
          $('.removePhBtn').unbind('click');
          removePH();
      });
  }
  
  
  var removePH = function () {
      $('.removePhBtn').click(function () {
         $pubHolidayContentEle = $(this).closest('div.pubHolidayContent');
         $contentDivEle = $(this).closest('div.form-horizontal');
         $premContentEle = $(this).closest('div.premContent');
         $pubHolidayContentEle.remove();
         <!--change hidden length value -->
         var length =  $contentDivEle.find('div.pubHolidayContent').length;
         console.log("length"+length);
         $premContentEle.find('.phLength').val(length);
          
         <!-- get current premValue-->
         var premValue = $premContentEle.find('.premValue').val();

         var nameVal = $premContentEle.find('.premTypeValue').val();
         if('ONSITE' == nameVal){
             nameVal = 'onSite';
         }else if('CONVEYANCE' == nameVal){
             nameVal = 'conveyance';
         }
          
         $contentDivEle.find('div.pubHolidayContent').each(function (k,v) {
             var publicHoliday = nameVal + 'PubHoliday';
             var PbHolDayStartHH = nameVal + 'PbHolDayStartHH';
             var PbHolDayStartMM = nameVal + 'PbHolDayStartMM';
             var PbHolDayEndHH = nameVal + 'PbHolDayEndHH';
             var PbHolDayEndMM = nameVal + 'PbHolDayEndMM';
             
             $(this).find('.public-holiday').attr("name",premValue+publicHoliday+k);
             $(this).find('.PbHolDayStartHH').attr("name",premValue+PbHolDayStartHH+k);
             $(this).find('.PbHolDayStartMM').attr("name",premValue+PbHolDayStartMM+k);
             $(this).find('.PbHolDayEndHH').attr("name",premValue+PbHolDayEndHH+k);
             $(this).find('.PbHolDayEndMM').attr("name",premValue+PbHolDayEndMM+k);
         });
      });
  }
  
</script>



