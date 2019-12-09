<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ page import="com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@ include file="./dashboard.jsp" %>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
  <div class="main-content">
    <div class="container">
      <div class="row">
        <div class="col-xs-12">
          <div class="tab-gp steps-tab">
            <%@ include file="./navTabs.jsp" %>
            <div class="tab-content">
              <div class="tab-pane active" id="premisesTab" role="tabpanel">
                <div class="row">
                  <div class="col-xs-12">
                    <div class="premises-txt">
                      <p>Premises are your service operation sites that can either be at a fixed address<strong> - &#34;on-site&#34;</strong>, or in a mobile clinic or ambulance<strong> - &#34;conveyance&#34;</strong>.</p>
                    </div>
                  </div>
                  <button id="addPremBtn" type="button">Add Premises</button>
                </div>
                <c:forEach var="appGrpPremisesDto" items="${AppSubmissionDto.appGrpPremisesDtoList}" varStatus="status">
                  <c:set var="premIndexNo" value="prem${status.index}"/>
                  <c:set value="${errorMap_premises[premIndexNo]}" var="errMsg"/>
                  <div class="row premContent" id="mainPrem">
                    <c:set var="onSite" value="<%=ApplicationConsts.PREMISES_TYPE_ON_SITE%>" ></c:set>
                    <c:set var="conv" value="<%=ApplicationConsts.PREMISES_TYPE_CONVEYANCE%>" ></c:set>
                    <div class="col-xs-12">
                      <div class="form-horizontal">
                        <div class="form-group premisesTypeDiv" id="${premIndexNo}premisesType">
                          <label class="col-xs-12 col-md-4 control-label" for="${premIndexNo}premisesType">What is your premises type?</label>
                          <input class="premTypeValue" type="hidden" name="${premIndexNo}premType" value="${appGrpPremisesDto.premisesType}"/>
                          <c:forEach var="premisesType" items="${premisesType}">
                            <div class="col-xs-6 col-md-2">
                              <div class="form-check">
                                <input class="form-check-input premTypeRadio ${premisesType}"  type="radio" name="premType" value = "${premisesType}" aria-invalid="false">
                                <label class="form-check-label" ><span class="check-circle"></span>
                                  <c:if test="${premisesType == onSite}">
                                    <c:out value="On-site" />
                                  </c:if>
                                  <c:if test="${premisesType == conv}">
                                    <c:out value="Conveyance" />
                                  </c:if>
                                </label>
                              </div>
                            </div>
                          </c:forEach>
                        </div>
                        <div class="row">
                          <div class="col-xs-12 col-md-4"></div>
                          <div class="col-xs-6 col-md-5">
                            <span class="error-msg"><c:out value="${errorMap_premises.premisesType}"></c:out></span>
                          </div>
                        </div>
                        <iais:row cssClass="premiseOnSiteSelect hidden">
                          <iais:field value="Add or select a premises from the list" width="12"/>
                          <iais:value  cssClass="col-xs-11 col-sm-7 col-md-5">
                            <iais:select cssClass="premSelect" id="premOnsiteSel" name="${premIndexNo}premOnSiteSelect"  options="premisesSelect" value="${appGrpPremisesDto.premisesSelect}"></iais:select>
                            <span class="error-msg"><c:out value="${errMsg.premisesSelect}"></c:out></span>
                          </iais:value>
                        </iais:row>
                        <iais:row cssClass="premiseConSelect hidden">
                          <iais:field value="Add or select a premises from the list" width="12"/>
                          <iais:value  cssClass="col-xs-11 col-sm-7 col-md-5">
                            <iais:select cssClass="premSelect" id="premConSel" name="${premIndexNo}premConSelect"  options="conveyancePremSel" value=""></iais:select>
                            <span class="error-msg"><c:out value="${errMsg.premisesSelect}"></c:out></span>
                          </iais:value>
                        </iais:row>
                      </div>
                      <div class="prem-summary hidden">
                        <h3 class="without-header-line">Premises Summary</h3>
                        <p class="premise-address-gp"> <span class="premise-type"><strong>On-site: </strong></span><span class="premise-address"></span></p>
                        <p class="vehicle-txt hidden"><strong>Vehicle No:</strong> <span class="vehicle-info"></span></p>
                      </div>
                      <div class="new-premise-form-on-site hidden">
                        <div class="form-horizontal">
                          <iais:row>
                            <iais:field value="Name of premises" width="11"/>
                            <iais:value width="11">
                              <iais:input maxLength="100" type="text" name="${premIndexNo}hciName" id="sitePremiseName" value="${appGrpPremisesDto.hciName}"></iais:input>
                              <span class="error-msg"><c:out value="${errMsg.hciName}"></c:out></span>
                            </iais:value>
                          </iais:row>
                          <iais:row cssClass="postalCodeDiv">
                            <iais:field value="Postal Code" width="12"/>
                            <iais:value width="30">
                              <iais:row>
                                <iais:value width="15">
                                  <iais:input cssClass="sitePostalCode" maxLength="6" type="text"  name="${premIndexNo}postalCode" id="sitePostalCode" value="${appGrpPremisesDto.postalCode}"></iais:input>
                                </iais:value>
                                <div class="col-xs-7 col-sm-6 col-md-6">
                                  <p><a class="retrieveAddr" >Retrieve your address</a></p>
                                </div>
                              </iais:row>
                              <span  class="postalCodeMsg error-msg"><c:out value="${errMsg.postalCode}"></c:out></span>
                            </iais:value>
                          </iais:row>
                          <iais:row>
                            <iais:field value="Address Type" width="12"/>
                            <iais:value cssClass="col-xs-7 col-sm-4 col-md-3">
                              <iais:select name="${premIndexNo}addrType" id="siteAddressType" codeCategory="CATE_ID_ADDRESS_TYPE" firstOption="Select address type" value="${appGrpPremisesDto.addrType}"></iais:select>
                              <span class="error-msg"><c:out value="${errMsg.addrType}"></c:out></span>
                            </iais:value>
                          </iais:row>
                          <iais:row>
                            <iais:field value="Block / House No." width="12"/>
                            <iais:value width="5">
                              <iais:input cssClass="siteBlockNo" maxLength="10"  type="text" name="${premIndexNo}blkNo" id="siteBlockNo" value="${appGrpPremisesDto.blkNo}"></iais:input>
                              <span class="error-msg"><c:out value="${errMsg.blkNo}"></c:out></span>
                            </iais:value>
                          </iais:row>
                          <iais:row>
                            <iais:field value="Floor No." width="12"/>
                            <iais:value cssClass="col-xs-7 col-sm-4 col-md-3 input-with-label">
                              <iais:input maxLength="3" type="text" name="${premIndexNo}floorNo" id="siteFloorNo" value="${appGrpPremisesDto.floorNo}"></iais:input>
                              <p class="small-txt">(Optional)</p>
                              <span class="error-msg"><c:out value="${errMsg.floorNo}"></c:out></span>
                            </iais:value>
                          </iais:row>
                          <iais:row>
                            <iais:field value="Unit No." width="12"/>
                            <iais:value cssClass="col-xs-7 col-sm-4 col-md-3 input-with-label">
                              <iais:input maxLength="5" type="text" name="${premIndexNo}unitNo" id="siteUnitNo" value="${appGrpPremisesDto.unitNo}"></iais:input>
                              <p class="small-txt">(Optional)</p>
                              <span class="error-msg"><c:out value="${errMsg.unitNo}"></c:out></span>
                            </iais:value>
                          </iais:row>
                          <iais:row>
                            <iais:field value="Building Name" width="12"/>
                            <iais:value width="11" cssClass="input-with-label">
                              <iais:input cssClass="siteBuildingName" maxLength="45" type="text" name="${premIndexNo}buildingName" id="siteBuildingName" value="${appGrpPremisesDto.buildingName}"></iais:input>
                              <p class="small-txt">(Optional)</p>
                              <span class="error-msg"><c:out value="${errMsg.buildingName}"></c:out></span>
                            </iais:value>
                          </iais:row>
                          <iais:row>
                            <iais:field value="Street Name" width="10"/>
                            <iais:value width="10">
                              <iais:input cssClass="siteStreetName" maxLength="32" type="text" name="${premIndexNo}streetName" id="siteStreetName" value="${appGrpPremisesDto.streetName}"></iais:input>
                              <span class="error-msg"><c:out value="${errMsg.streetName}"></c:out></span>
                            </iais:value>
                          </iais:row>
                          <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label" for="siteSafefyNo">Fire Safety Shelter Bureau Ref. No. <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" title="&lt;p&gt;This is a xxx digit No. that you can access from the Life Saving Force Portral.&lt;/p&gt;">i</a></label>
                            <div class="col-xs-9 col-sm-5 col-md-4">
                              <input id="siteSafefyNo" name="${premIndexNo}siteSafefyNo" type="text" value="${appGrpPremisesDto.siteSafefyNo}">
                            </div>
                          </div>
                          <iais:row>
                            <iais:field value="Fire Safety Certificate Issued Date" width="12"/>
                            <iais:value cssClass="col-xs-7 col-sm-4 col-md-3">
                              <iais:datePicker cssClass="fireIssuedDate" />
                            </iais:value>
                          </iais:row>
                          <iais:row>
                            <iais:field value="Office Telephone No" width="12"/>
                            <iais:value cssClass="col-xs-7 col-sm-4 col-md-3">
                              <iais:input type="text" name="${premIndexNo}offTelNo" maxLength="8" value="" />
                            </iais:value>
                          </iais:row>
                          <iais:row>
                            <iais:field value="Are you co-locating with another licensee?" width="12"/>
                            <iais:value cssClass="col-xs-7 col-sm-4 col-md-3">
                            </iais:value>
                          </iais:row>
                          <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label">
                              Operating Hours (Start)
                            </label>
                            <div class="col-xs-9 col-sm-5 col-md-4">
                              <input type="text" value="" maxlength="2" style="width: 60px"/>(HH)
                              :
                              <input type="text" value="" maxlength="2" style="width: 60px"/>(MM)
                            </div>
                          </div>
                          <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label">
                              Operating Hours (End)
                            </label>
                            <div class="col-xs-9 col-sm-5 col-md-4">
                              <input type="text" value="" maxlength="2" style="width: 60px"/>(HH)
                              :
                              <input type="text" value="" maxlength="2" style="width: 60px"/>(MM)
                            </div>
                          </div>
                          <iais:row>
                            <iais:field value="Select Public Holiday" width="12"/>
                            <iais:value cssClass="col-xs-7 col-sm-4 col-md-3">
                              <iais:datePicker cssClass="form_datetime" />
                            </iais:value>
                          </iais:row>
                          <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label">
                              Public Holidays Operating Hours (Start)
                            </label>
                            <div class="col-xs-9 col-sm-5 col-md-4">
                              <input type="text" value="" maxlength="2" style="width: 60px"/>(HH)
                              :
                              <input type="text" value="" maxlength="2" style="width: 60px"/>(MM)
                            </div>
                          </div>
                          <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label">
                              Public Holidays Operating Hours (End)
                            </label>
                            <div class="col-xs-9 col-sm-5 col-md-4">
                              <input type="text" value="" maxlength="2" style="width: 60px"/>(HH)
                              :
                              <input type="text" value="" maxlength="2" style="width: 60px"/>(MM)
                            </div>
                          </div>
                        </div>
                      </div>
                      <div class="new-premise-form-conveyance hidden">
                        <div class="form-horizontal">
                          <iais:row>
                            <iais:field value="Vehicle No." width="12"/>
                            <iais:value width="11">
                              <iais:input maxLength="10" type="text" name="${premIndexNo}conveyanceVehicleNo" id="vehicleNo" value="${appGrpPremisesDto.conveyanceVehicleNo}"></iais:input>
                              <span  class="postalCodeMsg error-msg"><c:out value="${errMsg.conveyancePostalCode}"></c:out></span>
                            </iais:value>
                          </iais:row>
                          <iais:row cssClass="postalCodeDiv">
                            <iais:field value="Postal Code" width="12"/>
                            <iais:value width="5">
                              <iais:input maxLength="6" cssClass="sitePostalCode" type="text" name="${premIndexNo}conveyancePostalCode" id="conveyancePostalCode" value="${appGrpPremisesDto.conveyancePostalCode}"></iais:input>
                              <span  class="postalCodeMsg error-msg"><c:out value="${errMsg.postalCode}"></c:out></span>
                            </iais:value>
                            <div class="col-xs-7 col-sm-6 col-md-4">
                              <p><a class="retrieveAddr" id="conveyance">Retrieve your address</a></p>
                            </div>
                          </iais:row>
                          <iais:row>
                            <iais:field value="Address Type" width="12"/>
                            <iais:value cssClass="col-xs-7 col-sm-4 col-md-3">
                              <iais:select name="${premIndexNo}conveyanceAddrType" id="siteAddressType" codeCategory="CATE_ID_ADDRESS_TYPE" firstOption="Select address type" value="${appGrpPremisesDto.addrType}"></iais:select>
                              <span  class="postalCodeMsg error-msg"><c:out value="${errMsg.conveyanceAddressType}"></c:out></span>
                            </iais:value>
                          </iais:row>
                          <iais:row>
                            <iais:field value="Block / House No." width="12"/>
                            <iais:value width="5">
                              <iais:input maxLength="10" cssClass="conveyanceBlockNo" type="text" name="${premIndexNo}conveyanceBlockNo" id="conveyanceBlockNo" value="${appGrpPremisesDto.conveyanceBlockNo}"></iais:input>
                              <span  class="postalCodeMsg error-msg"><c:out value="${errMsg.conveyanceBlockNo}"></c:out></span>
                            </iais:value>
                          </iais:row>
                          <iais:row>
                            <iais:field value="Floor No." width="12"/>
                            <iais:value cssClass="col-xs-7 col-sm-4 col-md-3 input-with-label">
                              <iais:input maxLength="3" type="text" name="${premIndexNo}conveyanceFloorNo" id="conveyanceFloorNo" value="${appGrpPremisesDto.conveyanceFloorNo}"></iais:input>
                              <p class="small-txt">(Optional)</p>
                              <span  class="postalCodeMsg error-msg"><c:out value="${errMsg.conveyanceFloorNo}"></c:out></span>
                            </iais:value>
                          </iais:row>
                          <iais:row>
                            <iais:field value="Unit No." width="12"/>
                            <iais:value cssClass="col-xs-7 col-sm-4 col-md-3 input-with-label">
                              <iais:input maxLength="5" type="text" name="${premIndexNo}conveyanceUnitNo" id="conveyanceUnitNo" value="${appGrpPremisesDto.conveyanceUnitNo}"></iais:input>
                              <p class="small-txt">(Optional)</p>
                              <span  class="postalCodeMsg error-msg"><c:out value="${errMsg.conveyanceUnitNo}"></c:out></span>
                            </iais:value>
                          </iais:row>
                          <iais:row>
                            <iais:field value="Street Name" width="10"/>
                            <iais:value width="10">
                              <iais:input maxLength="32" cssClass="conveyanceStreetName" type="text" name="${premIndexNo}conveyanceStreetName" id="conveyanceStreetName" value="${appGrpPremisesDto.conveyanceStreetName}"></iais:input>
                              <span  class="postalCodeMsg error-msg"><c:out value="${errMsg.conveyanceStreetName}"></c:out></span>
                            </iais:value>
                          </iais:row>
                          <iais:row>
                            <iais:field value="Building Name " width="12"/>
                            <iais:value cssClass="col-xs-11 col-sm-7 col-md-6 input-with-label">
                              <iais:input maxLength="45" cssClass="conveyanceBuildingName" type="text" name="${premIndexNo}conveyanceBuildingName" id="conveyanceBuildingName" value="${appGrpPremisesDto.conveyanceBuildingName}"></iais:input>
                              <p class="small-txt">(Optional)</p>
                              <span  class="postalCodeMsg error-msg"><c:out value="${errMsg.conveyanceBuildingName}"></c:out></span>
                            </iais:value>
                          </iais:row>
                          <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label">
                              Operating Hours (Start)
                            </label>
                            <div class="col-xs-9 col-sm-5 col-md-4">
                              <input type="text" value="" maxlength="2" style="width: 60px"/>(HH)
                              :
                              <input type="text" value="" maxlength="2" style="width: 60px"/>(MM)
                            </div>
                          </div>
                          <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label">
                              Operating Hours (End)
                            </label>
                            <div class="col-xs-9 col-sm-5 col-md-4">
                              <input type="text" value="" maxlength="2" style="width: 60px"/>(HH)
                              :
                              <input type="text" value="" maxlength="2" style="width: 60px"/>(MM)
                            </div>
                          </div>
                          <iais:row>
                            <iais:field value="Select Public Holiday" width="12"/>
                            <iais:value cssClass="col-xs-7 col-sm-4 col-md-3">
                            </iais:value>
                          </iais:row>
                          <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label">
                              Public Holidays Operating Hours (Start)
                            </label>
                            <div class="col-xs-9 col-sm-5 col-md-4">
                              <input type="text" value="" maxlength="2" style="width: 60px"/>(HH)
                              :
                              <input type="text" value="" maxlength="2" style="width: 60px"/>(MM)
                            </div>
                          </div>
                          <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label">
                              Public Holidays Operating Hours (End)
                            </label>
                            <div class="col-xs-9 col-sm-5 col-md-4">
                              <input type="text" value="" maxlength="2" style="width: 60px"/>(HH)
                              :
                              <input type="text" value="" maxlength="2" style="width: 60px"/>(MM)
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </c:forEach>
                <div class="application-tab-footer">
                  <div class="row">
                    <div class="col-xs-12 col-sm-6 ">
                      <p><a class="back hidden" href="#"><em class="fa fa-angle-left"></em> Back</a></p>
                    </div>
                    <div class="col-xs-12 col-sm-6">
                      <div class="button-group"><a class="btn btn-secondary premiseSaveDraft" >Save as Draft</a><a class="btn btn-primary next premiseId" >Next</a></div>
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
</form>
<script type="text/javascript">

    $(document).ready(function() {

        $('.prem-summary').addClass('hidden');

        $('.table-condensed').css("background-color","#d9edf7");
        // premisesSelectChange();

        // $('.ONSITE[name="prem0premType"]').attr("checked","checked");

        <%--<c:forEach var="appGrpPremises" items="${AppSubmissionDto.appGrpPremisesDtoList}" varStatus="status">
        <c:set var="premIndexNo2" value="prem${status.index}"/>
        <c:if test="${!empty appGrpPremises.premisesType }">
        console.log('not null');
            if('<%=ApplicationConsts.PREMISES_TYPE_ON_SITE%>'== '${appGrpPremises.premisesType}'){
                console.log('on site');
                $('input.ONSITE[name="${premIndexNo2}premType"]').attr("checked","checked");

                $premSelect.find('.premiseOnSiteSelect').removeClass('hidden');
                if
                //$premSelect.find('.premiseConSelect').addClass('hidden');
                $premSelect.find('.new-premise-form-conveyance').addClass('hidden');

                var premSelect = function(){
                    $('.premSelect').change(function () {
                        var premSelectVal = $(this).val();
                        $premSelect = $(this).closest('div.premContent');
                        var thisId = $(this).attr('id');
                        if("newPremise" == premSelectVal){
                            $premSelect.find('.new-premise-form-on-site').removeClass('hidden');
                            $premSelect.find('.new-premise-form-conveyance').addClass('hidden');
                            if("premOnsiteSel" == thisId){
                                $premSelect.find('.new-premise-form-on-site').removeClass('hidden');
                                $premSelect.find('.new-premise-form-conveyance').addClass('hidden');
                            }else if ("premConSel" == thisId) {
                                $premSelect.find('.new-premise-form-conveyance').removeClass('hidden');
                                $premSelect.find('.new-premise-form-on-site').addClass('hidden');
                            }
                        }else if("-1" == newPremise){
                            $premSelect.find('.new-premise-form-conveyance').addClass('hidden');
                            $premSelect.find('.new-premise-form-on-site').addClass('hidden');
                        }


                    });
                }

                //$('.premiseLocationSelect').removeClass('hidden');
            }else if('<%=ApplicationConsts.PREMISES_TYPE_CONVEYANCE%>'==premisesTypeValue){
                $('input.premTypeRadio.CONVEYANCE[name="name"]').attr("checked","checked");
                /*$('.CONVEYANCE').attr("checked","checked");
                $('#premise_conveyance').attr("checked","checked");
                $('#premise_onsite').removeAttr("checked");
                $('.premiseLocationSelect').removeClass('hidden');
                $('.premises-summary, .new-premise-form-on-site, .new-premise-form-conveyance, .vehicleSelectForm').addClass('hidden');*/
            }
        </c:if>
        </c:forEach>--%>



        premType();

        premSelect();

        retrieveAddr();

        //Binding method
        $('.premiseId').click(function(){
            submit('documents',null,null);
        });
        $('.premiseSaveDraft').click(function(){
            submit('premises','saveDraft',null);
        });

        if(<c:out value="${errorMap_premises eq null}"/>){
            $('#premisesli').removeClass('incomplete');
        }else{
            $('#premisesli').addClass('incomplete');
        }





    });


    var premType = function () {
        $('.premTypeRadio').click(function () {
            var checkedType = $(this).val();
            $premSelect = $(this).closest('div.premContent');
            $premSelctDivEle = $(this).closest('div.premisesTypeDiv');
            if('<%=ApplicationConsts.PREMISES_TYPE_ON_SITE%>'==checkedType){
                $premSelect.find('.premiseOnSiteSelect').removeClass('hidden');
                $premSelect.find('.premiseConSelect').addClass('hidden');
                $premSelect.find('.new-premise-form-conveyance').addClass('hidden');
                $premSelctDivEle.find('.premTypeValue').val(checkedType);
            }else if('<%=ApplicationConsts.PREMISES_TYPE_CONVEYANCE%>' == checkedType){
                $premSelect.find('.premiseConSelect').removeClass('hidden');
                $premSelect.find('.premiseOnSiteSelect').addClass('hidden');
                $premSelect.find('.new-premise-form-on-site').addClass('hidden');
                $premSelctDivEle.find('.premTypeValue').val(checkedType);
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
                $premSelect.find('.new-premise-form-conveyance').addClass('hidden');
                if("premOnsiteSel" == thisId){
                    $premSelect.find('.new-premise-form-on-site').removeClass('hidden');
                    $premSelect.find('.new-premise-form-conveyance').addClass('hidden');
                }else if ("premConSel" == thisId) {
                    $premSelect.find('.new-premise-form-conveyance').removeClass('hidden');
                    $premSelect.find('.new-premise-form-on-site').addClass('hidden');
                }
            }else if("-1" == newPremise){
                $premSelect.find('.new-premise-form-conveyance').addClass('hidden');
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
                console.log(data);
                if(data == null){
                    console.log("data is null");
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
            'dataType':'json',
            'type':'GET',
            'success':function (data) {
                // $('div.premContent').(data);

            },
            'error':function (data) {
                $('div.premContent:last').after(data.responseText);
                premType();

                premSelect();

                retrieveAddr();
            }
        });
    });


    $('.fireIssuedDate').datetimepicker({
        format:"yyyy-mm-dd",
        weekStart: 0,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        forceParse: 0,
        showMeridian: 1,
        //0=>min , 1=>hour , 2=>day , 3=>mon
        minView:2
    });

    $('.form_datetime').datetimepicker({
        format:"yyyy-mm-dd",
        weekStart: 0,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        forceParse: 0,
        showMeridian: 1,
        //0=>min , 1=>hour , 2=>day , 3=>mon
        minView:2
    });





</script>



