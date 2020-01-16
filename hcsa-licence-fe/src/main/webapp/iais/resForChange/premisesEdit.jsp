<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<br/>
<form method="post" id="menuListForm" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
  <input type="hidden" name="crud_action_type_form_value" value="">
  <%@include file="dashboard.jsp" %>
  <%--Validation fields Start--%>
  <input type="hidden" name="paramController" id="paramController"
         value="com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator"/>
  <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.ApplicationValidateDto"/>
  <input type="hidden" name="valProfiles" id="valProfiles" value=""/>
  <%--Validation fields End--%>

  <div class="main-content">
    <div class="container">
      <div class="row">
        <div class="col-xs-12">
          <div class="tab-gp steps-tab">
            <div class="tab-content">
              <div class="tab-pane active" id="premisesTab" role="tabpanel">
                <c:forEach var="appGrpPremisesDto" items="${ReloadPremises}" varStatus="status">
                <c:set value="${errorMap_premises}" var="errMsg"/>
                <input type="hidden" id="premTypeVal" value="${appGrpPremisesDto.premisesType}"/>
                <div class="row premContent" id="mainPrem">
                  <div class="col-xs-12">
                    <div>
                      <h1 style="margin-top: 10px;">Premises Amendment</h1>
                    </div>
                    <div>
                      <h2>${PremisesListQueryDto.svcId}:${PremisesListQueryDto.premisesType}:${PremisesListQueryDto.address}</h2>
                    </div>
                    <div class="new-premise-prem hidden">
                      <div class="form-horizontal">
                        <iais:row>
                          <iais:field value="Name of HCI " mandatory="true" width="11"/>
                          <iais:value width="11">
                            <iais:input maxLength="100" needDisabled="true" type="text" name="hciName"
                                        id="sitePremiseName" value="${appGrpPremisesDto.hciName}"></iais:input>
                            <span id="error_hciName" name="iaisErrorMsg" class="error-msg"></span>
                          </iais:value>
                        </iais:row>
                        <iais:row cssClass="postalCodeDiv">
                          <iais:field value="Postal Code" mandatory="true" width="12"/>
                          <iais:value width="30">
                            <iais:row>
                              <iais:value width="15">
                                <iais:input cssClass="sitePostalCode" maxLength="6" type="text" name="postalCode"
                                            value="${appGrpPremisesDto.postalCode}"></iais:input>
                                <span id="error_postalCode" class="error-msg" name="iaisErrorMsg"></span>
                              </iais:value>
                              <div class="col-xs-7 col-sm-6 col-md-6">
                                <p><a class="retrieveAddr">Retrieve your address</a></p>
                              </div>
                            </iais:row>

                          </iais:value>
                        </iais:row>
                        <iais:row>
                          <iais:field value="Address Type " mandatory="true" width="12"/>
                          <iais:value cssClass="col-xs-7 col-sm-4 col-md-3">
                            <iais:select cssClass="siteAddressType" name="siteAddressType" id="siteAddressType"
                                         codeCategory="CATE_ID_ADDRESS_TYPE" firstOption="Select address type"
                                         value="${appGrpPremisesDto.addrType}"></iais:select>
                            <span class="error-msg" name="iaisErrorMsg" id="error_addrType"></span>
                          </iais:value>
                        </iais:row>
                        <iais:row>
                          <iais:field value="Block / House No." width="12"/>
                          <iais:value width="5">
                            <iais:input cssClass="siteBlockNo" maxLength="10" type="text" name="blkNo" id="siteBlockNo"
                                        value="${appGrpPremisesDto.blkNo}"></iais:input>
                            <span class="error-msg" name="iFire Safety Shelter Bureau Ref. No.aisErrorMsg"
                                  id="error_blkNo"></span>
                          </iais:value>
                        </iais:row>
                        <iais:row>
                          <iais:field value="Floor No." width="12"/>
                          <iais:value cssClass="col-xs-7 col-sm-4 col-md-3 input-with-label">
                            <iais:input maxLength="3" type="text" name="floorNo" id="siteFloorNo"
                                        value="${appGrpPremisesDto.floorNo}"></iais:input>
                            <p class="small-txt">(Optional)</p>
                            <div class="col-xs-12">
                              <span class="error-msg " name="iaisErrorMsg" id="error_floorNo"></span>
                            </div>
                          </iais:value>
                        </iais:row>
                        <iais:row>
                          <iais:field value="Unit No." width="12"/>
                          <iais:value cssClass="col-xs-7 col-sm-4 col-md-3 input-with-label">
                            <iais:input maxLength="5" type="text" name="unitNo" id="siteUnitNo"
                                        value="${appGrpPremisesDto.unitNo}"></iais:input>
                            <p class="small-txt">(Optional)</p>
                            <div class="col-xs-12">
                              <span class="error-msg" name="iaisErrorMsg" id="error_unitNo"></span>
                            </div>
                          </iais:value>
                        </iais:row>
                        <iais:row>
                          <iais:field value="Building Name" width="12"/>
                          <iais:value width="11" cssClass="input-with-label">
                            <iais:input cssClass="siteBuildingName" maxLength="45" type="text" name="buildingName"
                                        id="siteBuildingName" value="${appGrpPremisesDto.buildingName}"></iais:input>
                            <p class="small-txt">(Optional)</p>
                            <span class="error-msg" name="iaisErrorMsg" id="error_buildingName"></span>
                          </iais:value>
                        </iais:row>
                        <iais:row>
                          <iais:field value="Street Name " mandatory="true" width="10"/>
                          <iais:value width="10">
                            <iais:input cssClass="siteStreetName" maxLength="32" type="text" name="streetName"
                                        id="siteStreetName" value="${appGrpPremisesDto.streetName}"></iais:input>
                            <span class="error-msg" name="iaisErrorMsg" id="error_streetName"></span>
                          </iais:value>
                        </iais:row>
                        <iais:row>
                          <iais:field value="Fire Safety Shelter Bureau Ref. No. " mandatory="true" width="10"/>
                          <iais:value width="10">
                            <iais:input cssClass="scdfRefNo" maxLength="66" type="text" name="scdfRefNo"
                                        id="siteStreetName" value="${appGrpPremisesDto.scdfRefNo}"></iais:input>
                            <span class="error-msg" name="iaisErrorMsg" id="error_scdfRefNo"></span>
                          </iais:value>
                        </iais:row>
                      </div>
                    </div>
                    <div class="new-premise-conv hidden">
                      <div class="form-horizontal">
                        <iais:row>
                          <iais:field value="Vehicle No. " mandatory="true" width="12"/>
                          <iais:value width="11">
                            <iais:input maxLength="10" type="text" name="conveyanceVehicleNo" id="vehicleNo"
                                        value="${appGrpPremisesDto.conveyanceVehicleNo}"></iais:input>
                            <span class="error-msg" name="iaisErrorMsg" id="error_conveyanceVehicleNo"></span>
                          </iais:value>
                        </iais:row>
                        <iais:row cssClass="postalCodeDiv">
                          <iais:field value="Postal Code " mandatory="true" width="12"/>
                          <iais:value width="5">
                            <iais:input maxLength="6" cssClass="sitePostalCode" type="text" name="conveyancePostalCode"
                                        value="${appGrpPremisesDto.conveyancePostalCode}"></iais:input>
                            <span class="error-msg" name="iaisErrorMsg" id="error_conveyancePostalCode"></span>
                          </iais:value>
                          <div class="col-xs-7 col-sm-6 col-md-4">
                            <p><a class="retrieveAddr" id="conveyance">Retrieve your address</a></p>
                          </div>

                        </iais:row>
                        <iais:row>
                          <iais:field value="Address Type " mandatory="true" width="12"/>
                          <iais:value cssClass="col-xs-7 col-sm-4 col-md-3">
                            <iais:select name="conveyanceAddrType" cssClass="conveyanceAddressType" id="siteAddressType"
                                         codeCategory="CATE_ID_ADDRESS_TYPE" firstOption="Select address type"
                                         value="${appGrpPremisesDto.conveyanceAddressType}"></iais:select>
                            <span class="error-msg" name="iaisErrorMsg" id="error_conveyanceAddressType"></span>
                          </iais:value>
                        </iais:row>
                        <iais:row>
                          <iais:field value="Block / House No." width="12"/>
                          <iais:value width="5">
                            <iais:input maxLength="10" cssClass="conveyanceBlockNo" type="text" name="conveyanceBlockNo"
                                        id="conveyanceBlockNo"
                                        value="${appGrpPremisesDto.conveyanceBlockNo}"></iais:input>
                            <span class="postalCodeMsg error-msg"></span>
                          </iais:value>
                        </iais:row>
                        <iais:row>
                          <iais:field value="Floor No." width="12"/>
                          <iais:value cssClass="col-xs-7 col-sm-4 col-md-3 input-with-label">
                            <iais:input maxLength="3" type="text" name="conveyanceFloorNo" id="conveyanceFloorNo"
                                        value="${appGrpPremisesDto.conveyanceFloorNo}"></iais:input>
                            <p class="small-txt">(Optional)</p>
                            <span class="error-msg" name="iaisErrorMsg" id="error_conveyanceFloorNo"></span>
                          </iais:value>
                        </iais:row>
                        <iais:row>
                          <iais:field value="Unit No." width="12"/>
                          <iais:value cssClass="col-xs-7 col-sm-4 col-md-3 input-with-label">
                            <iais:input maxLength="5" type="text" name="conveyanceUnitNo"
                                        value="${appGrpPremisesDto.conveyanceUnitNo}"></iais:input>
                            <p class="small-txt">(Optional)</p>
                            <span class="error-msg" name="iaisErrorMsg" id="error_conveyanceUnitNo"></span>
                          </iais:value>
                        </iais:row>
                        <iais:row>
                          <iais:field value="Building Name " width="12"/>
                          <iais:value cssClass="col-xs-11 col-sm-7 col-md-6 input-with-label">
                            <iais:input maxLength="45" cssClass="conveyanceBuildingName" type="text"
                                        name="conveyanceBuildingName" id="conveyanceBuildingName"
                                        value="${appGrpPremisesDto.conveyanceBuildingName}"></iais:input>
                            <p class="small-txt">(Optional)</p>
                            <span class="error-msg"></span>
                          </iais:value>
                        </iais:row>
                        <iais:row>
                          <iais:field value="Street Name " mandatory="true" width="10"/>
                          <iais:value width="10">
                            <iais:input maxLength="32" cssClass="conveyanceStreetName" type="text"
                                        name="conveyanceStreetName"
                                        value="${appGrpPremisesDto.conveyanceStreetName}"></iais:input>
                            <span class="error-msg" name="iaisErrorMsg" id="error_conveyanceStreetName"></span>
                          </iais:value>
                        </iais:row>
                      </div>
                    </div>
                  </div>
                  </c:forEach>
                  <div class="application-tab-footer">
                    <div class="row">
                      <div class="col-xs-12 col-sm-6 ">
                        <p><a class="back" id="back"><em class="fa fa-angle-left"></em> Back</a></p>
                      </div>
                      <div class="col-xs-12 col-sm-6">
                        <div class="button-group">
                          <a class="btn btn-primary next" id="previewAndSub">Preview and Submit</a>
                        </div>
                        <%--<div class="color-small-block" style="border: 0.5px solid rgb(25, 137, 191); border-image: none; background-color: rgb(25, 137, 191);">
                          <p style="color: rgb(255, 255, 255);">Dark blue #1989BF</p>
                        </div>--%>
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
    </div>
  </div>
</form>


<script>
    $(document).ready(function () {
        var premType = $('#premTypeVal').val();
        if ("ONSITE" == premType) {
            $('.new-premise-prem').removeClass('hidden');
            $('.new-premise-conv').addClass('hidden');
        } else if ("CONVEYANCE" == premType) {
            $('.new-premise-prem').addClass('hidden');
            $('.new-premise-conv').removeClass('hidden');
        }

        retrieveAddr();

    });

    $('#previewAndSub').click(function () {
        doSubmitForm('', 'premisesList', 'prepareEdit');
    });

    $('#back').click(function () {
        menuListForBack('premisesList', 'prepareSwitch');
    });


    var retrieveAddr = function () {
        $('.retrieveAddr').click(function () {
            $postalCodeEle = $(this).closest('div.postalCodeDiv');
            $premContent = $(this).closest('div.premContent');
            var postalCode = $postalCodeEle.find('.sitePostalCode').val();
            var thisId = $(this).attr('id');
//alert(postalCode);
            var re = new RegExp('^[0-9]*$');
            var errMsg = '';
            if ('' == postalCode) {
                errMsg = 'the postal code could not be null';
            } else if (postalCode.length != 6) {
                errMsg = 'the postal code length must be 6';
            } else if (!re.test(postalCode)) {
                errMsg = 'the postal code must be numbers';
            }
            if ("" != errMsg) {
                $postalCodeEle.find('.postalCodeMsg').html(errMsg);
                return;
            }
            var data = {
                'postalCode': postalCode
            };
            $.ajax({
                'url': '${pageContext.request.contextPath}/retrieve-address',
                'dataType': 'json',
                'data': data,
                'type': 'GET',
                'success': function (data) {
                    if (data == null) {
                        $postalCodeEle.find('.postalCodeMsg').html("the postal code information could not be found");
                        return;
                    }
                    if ("conveyance" == thisId) {
                        $premContent.find('.conveyanceBlockNo').val(data.blkHseNo);
                        $premContent.find('.conveyanceStreetName').val(data.streetName);
                        $premContent.find('.conveyanceBuildingName').val(data.buildingName);
                    } else {
                        $premContent.find('.siteBlockNo').val(data.blkHseNo);
                        $premContent.find('.siteStreetName').val(data.streetName);
                        $premContent.find('.siteBuildingName').val(data.buildingName);
                        if (null == data.addressType || '' == data.addressType) {


                        } else {


                        }

                        $premContent.find('.siteBlockNo').prop('readonly', true);
                        $premContent.find('.siteStreetName').prop('readonly', true);
                        $premContent.find('.siteBuildingName').prop('readonly', true);
                    }

                },
                'error': function () {
                    $postalCodeEle.find('.postalCodeMsg').html("the postal code information could not be found");
                }
            });

        });
    }

</script>