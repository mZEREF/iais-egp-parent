<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-cc"/>
<%@ include file="./dashboard.jsp" %>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" id = "premisesTypeValue" value="${appGrpPremisesDto.premisesType}">
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
                                            <p>Premises are your service operation sites that can either be at a fixed address – <b>&#34;on-site&#34;</b>, or in a mobile clinic or ambulance – <b>&#34;conveyance&#34;</b>.</p>
                                        </div>
                                    </div>
                                    <div class="col-xs-12">
                                        <div class="form-horizontal">
                                            <div class="form-group" id="premisesType">
                                                <label class="col-xs-12 col-md-4 control-label" for="premisesType">What is your premises type?</label>
                                                <c:forEach var="premisesType" items="${premisesType}">
                                                    <div class="col-xs-6 col-md-2">
                                                        <div class="form-check">
                                                            <input class="form-check-input premTypeRadio"  type="radio" name="premisesType" value = ${premisesType.premisesType} aria-invalid="false">
                                                            <label class="form-check-label" ><span class="check-circle"></span>${premisesType.premisesType}</label>
                                                        </div>
                                                        <span class="error-msg"><c:out value="${errorMap_premises.premisesType}"></c:out></span>
                                                    </div>
                                                </c:forEach>
                                            </div>
                                            <iais:row cssClass="premiseLocationSelect hidden">
                                                <iais:field value="Add or select a premises from the list" width="12"/>
                                                <iais:value  cssClass="col-xs-11 col-sm-7 col-md-5">
                                                   <iais:select name="premisesSelect" id="premisesSelect" options="premisesSelect" value="${appGrpPremisesDto.premisesSelect}"></iais:select>
                                                </iais:value>
                                            </iais:row>

                                            <div class="form-group vehicleSelectForm hidden">
                                                <label class="col-xs-12 col-md-4 control-label" for="vehicleSelect">Select a vehicle</label>
                                                <div class="col-xs-8 col-sm-5 col-md-3">
                                                    <select id="vehicleSelect">
                                                        <option>Select One</option>
                                                        <option>SBS-1234-A</option>
                                                        <option>SGP-1872-U</option>
                                                    </select>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="premises-summary hidden">
                                            <h3 class="without-header-line">Premises Summary</h3>
                                            <p class="premise-address-gp"> <span class="premise-type"><b>On-site: </b></span><span class="premise-address"></span></p>
                                            <p class="vehicle-txt hidden"><b>Vehicle No:</b> <span class="vehicle-info"></span></p>
                                        </div>
                                        <div class="new-premise-form-on-site hidden">
                                            <div class="form-horizontal">
                                                <iais:row>
                                                    <iais:field value="Name of premises" width="11"/>
                                                    <iais:value width="11">
                                                        <iais:input type="text" name="hciName" id="sitePremiseName" value="${appGrpPremisesDto.hciName}"></iais:input>
                                                    </iais:value>
                                                </iais:row>
                                                <iais:row>
                                                    <iais:field value="Postal Code" width="12"/>
                                                    <iais:value width="5">
                                                        <iais:input type="text"  name="postalCode" id="sitePostalCode" value="${appGrpPremisesDto.postalCode}"></iais:input>
                                                        <span class="error-msg"><c:out value="${errorMap_premises.postalCode}"></c:out></span>
                                                    </iais:value>
                                                        <div class="col-xs-7 col-sm-6 col-md-4">
                                                            <p><a id="retrieveAddr" href="#">Retrieve your address</a></p>
                                                        </div>
                                                </iais:row>
                                                <iais:row>
                                                    <iais:field value="Block / House No." width="12"/>
                                                    <iais:value width="5">
                                                        <iais:input type="text" name="blkNo" id="siteBlockNo" value="${appGrpPremisesDto.blkNo}"></iais:input>
                                                    </iais:value>
                                                </iais:row>
                                                <iais:row>
                                                    <iais:field value="Street Name" width="10"/>
                                                    <iais:value width="10">
                                                        <iais:input type="text" name="streetName" id="siteStreetName" value="${appGrpPremisesDto.streetName}"></iais:input>
                                                      <span class="error-msg"><c:out value="${errorMap_premises.streetName}"></c:out></span>
                                                    </iais:value>
                                                </iais:row>
                                                <iais:row>
                                                    <iais:field value="Floor No." width="12"/>
                                                    <iais:value cssClass="col-xs-7 col-sm-4 col-md-3 input-with-label">
                                                        <iais:input type="text" name="floorNo" id="siteFloorNo" value="${appGrpPremisesDto.floorNo}"></iais:input>
                                                        <p class="small-txt">(Optional)</p>
                                                    </iais:value>
                                                </iais:row>
                                                <iais:row>
                                                    <iais:field value="Unit No." width="12"/>
                                                    <iais:value cssClass="col-xs-7 col-sm-4 col-md-3 input-with-label">
                                                        <iais:input type="text" name="unitNo" id="siteUnitNo" value="${appGrpPremisesDto.unitNo}"></iais:input>
                                                        <p class="small-txt">(Optional)</p>
                                                    </iais:value>
                                                </iais:row>
                                                <iais:row>
                                                    <iais:field value="Building Name" width="12"/>
                                                    <iais:value width="11" cssClass="input-with-label">
                                                        <iais:input type="text" name="buildingName" id="siteBuildingName" value="${appGrpPremisesDto.buildingName}"></iais:input>
                                                        <p class="small-txt">(Optional)</p>
                                                    </iais:value>
                                                </iais:row>
                                                <iais:row>
                                                    <iais:field value="Address Type" width="12"/>
                                                    <iais:value cssClass="col-xs-7 col-sm-4 col-md-3">
                                                        <iais:select name="addrType" id="siteAddressType" codeCategory="CATE_ID_ADDRESS_TYPE" firstOption="Select address type"></iais:select>

                                                    </iais:value>
                                                </iais:row>
                                                <div class="form-group">
                                                    <label class="col-xs-12 col-md-4 control-label" for="siteSafefyNo">Fire Safety Shelter Bureau Ref. No. <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" title="&lt;p&gt;This is a xxx digit No. that you can access from the Life Saving Force Portral.&lt;/p&gt;">i</a></label>
                                                    <div class="col-xs-9 col-sm-5 col-md-4">
                                                        <input id="siteSafefyNo" name="siteSafefyNo" type="text">
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="new-premise-form-conveyance hidden">
                                            <div class="form-horizontal">
                                                <iais:row>
                                                    <iais:field value="Vehicle No." width="12"/>
                                                    <iais:value width="11">
                                                        <iais:input type="text" name="conveyanceVehicleNo" id="vehicleNo" value="${appGrpPremisesDto.conveyanceVehicleNo}"></iais:input>
                                                    </iais:value>
                                                </iais:row>
                                                <iais:row>
                                                    <iais:field value="Vehicle Owner's Name" width="12"/>
                                                    <iais:value cssClass="col-xs-4 col-sm-2 col-md-2">
                                                    <select id="vehicleOwnerNameSalutation" name="conveyanceSalutation" aria-label="vehicleOwnerNameSalutation">
                                                        <option>Mr</option>
                                                        <option>Mrs</option>
                                                        <option>Ms</option>
                                                        <option>Mam</option>
                                                    </select>
                                                    </iais:value>
                                                    <iais:value cssClass="col-xs-7 col-sm-4 col-md-5">
                                                        <iais:input type="text" name = "conveyanceVehicleOwnerName" id="vehicleOwnerName" value="${appGrpPremisesDto.conveyanceVehicleOwnerName}"></iais:input>
                                                    </iais:value>
                                                </iais:row>
                                                <iais:row>
                                                    <iais:field value="Postal Code" width="12"/>
                                                    <iais:value width="5">
                                                        <iais:input type="text" name="conveyancePostalCode" id="conveyancePostalCode" value="${appGrpPremisesDto.conveyancePostalCode}"></iais:input>
                                                    </iais:value>
                                                    <div class="col-xs-7 col-sm-6 col-md-4">
                                                        <p><a href="#">Retrieve your address</a></p>
                                                    </div>
                                                </iais:row>
                                                <iais:row>
                                                    <iais:field value="Block / House No." width="12"/>
                                                    <iais:value width="5">
                                                        <iais:input type="text" name="conveyanceBlockNo" id="conveyanceBlockNo" value="${appGrpPremisesDto.conveyanceBlockNo}"></iais:input>
                                                    </iais:value>
                                                </iais:row>
                                                <iais:row>
                                                    <iais:field value="Street Name" width="10"/>
                                                    <iais:value width="10">
                                                        <iais:input type="text" name="conveyanceStreetName" id="conveyanceStreetName" value="${appGrpPremisesDto.conveyanceStreetName}"></iais:input>
                                                    </iais:value>
                                                </iais:row>
                                                <iais:row>
                                                    <iais:field value="Floor No." width="12"/>
                                                    <iais:value cssClass="col-xs-7 col-sm-4 col-md-3 input-with-label">
                                                        <iais:input type="text" name="conveyanceFloorNo" id="conveyanceFloorNo" value="${appGrpPremisesDto.conveyanceFloorNo}"></iais:input>
                                                        <p class="small-txt">(Optional)</p>
                                                    </iais:value>
                                                </iais:row>
                                                <iais:row>
                                                    <iais:field value="Unit No." width="12"/>
                                                    <iais:value cssClass="col-xs-7 col-sm-4 col-md-3 input-with-label">
                                                        <iais:input type="text" name="conveyanceUnitNo" id="conveyanceUnitNo" value="${appGrpPremisesDto.conveyanceUnitNo}"></iais:input>
                                                        <p class="small-txt">(Optional)</p>
                                                    </iais:value>
                                                </iais:row>
                                                <iais:row>
                                                    <iais:field value="Building Name " width="12"/>
                                                    <iais:value cssClass="col-xs-11 col-sm-7 col-md-6 input-with-label">
                                                        <iais:input type="text" name="conveyanceBuildingName" id="conveyanceBuildingName" value="${appGrpPremisesDto.conveyanceBuildingName}"></iais:input>
                                                        <p class="small-txt">(Optional)</p>
                                                    </iais:value>
                                                </iais:row>
                                                <iais:row>
                                                    <iais:field value="Address Type" width="12"/>
                                                    <iais:value cssClass="col-xs-7 col-sm-4 col-md-3">
                                                        <select id="conveyanceAddressType" name="conveyanceAddressType">
                                                            <option>Select address type</option>
                                                            <option>Apt Blk</option>
                                                            <option>Without Apt Blk</option>
                                                            <option>Without Apt Blk</option>
                                                        </select>
                                                    </iais:value>
                                                </iais:row>
                                                <iais:row>
                                                    <iais:field value="Email Address " width="12"/>
                                                    <iais:value cssClass="col-xs-9 col-sm-5 col-sm-4">
                                                        <iais:input type="text" name="conveyanceEmail" id="conveyanceEmail" value="${appGrpPremisesDto.conveyanceEmail}"></iais:input>
                                                    </iais:value>
                                                </iais:row>
                                                <iais:row>
                                                    <iais:field value="Mobile number " width="12"/>
                                                    <iais:value cssClass="col-xs-7 col-sm-4 col-md-3">
                                                        <iais:input type="text" name="conveyanceMobile" id="conveyanceMobile" value="${appGrpPremisesDto.conveyanceMobile}"></iais:input>
                                                    </iais:value>
                                                </iais:row>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="application-tab-footer">
                                    <div class="row">
                                        <div class="col-xs-12 col-sm-6 ">
                                            <p><a class="back hidden" href="#"><i class="fa fa-angle-left"></i> Back</a></p>
                                        </div>
                                        <div class="col-xs-12 col-sm-6">
                                            <div class="button-group"><a id="premiseSaveDraft" class="btn btn-secondary" >Save as Draft</a><a id="premiseId" class="btn btn-primary next" >Next</a></div>
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
        var premisesTypeValue = $('#premisesTypeValue').val();
        if('<%=ApplicationConsts.PREMISES_TYPE_ON_SITE%>'==premisesTypeValue){
            $('#premise_onsite').attr("checked","checked");
            $('#premise_conveyance').removeAttr("checked");
            $('.premiseLocationSelect').removeClass('hidden');
            $('.premises-summary, .new-premise-form-on-site, .new-premise-form-conveyance, .vehicleSelectForm').addClass('hidden');
        }else if('<%=ApplicationConsts.PREMISES_TYPE_CONVEYANCE%>'==premisesTypeValue){
            $('#premise_conveyance').attr("checked","checked");
            $('#premise_onsite').removeAttr("checked");
            $('.premiseLocationSelect').removeClass('hidden');
            $('.premises-summary, .new-premise-form-on-site, .new-premise-form-conveyance, .vehicleSelectForm').addClass('hidden');
        }
        premisesSelectChange();

        //Binding method
        $('#premiseId').click(function(){
            submit('documents',null,null);
        });
        $('#premiseSaveDraft').click(function(){
            submit('premises','saveDraft',null);
        });
    });

    $('.premTypeRadio').click(function () {
        var checkedType = $(this).val();
        $('#premisesTypeValue').val(checkedType);
    });

    $('#retrieveAddr').click(function(){
        var postCode = $('#sitePostalCode').val();
        var data = {
            'searchField':'postalCode',
            'filterValue':postCode
        };
        $.ajax({
            'url':'/hcsaapplication/eservice/INTERNET/loadPremisesByCode.do',
            'dataType':'json',
            'data':data,
            'type':'Get',
            'success':function (data) {
                console.log("success");

            },
            'error':function () {
                console.log("failed");
            }
        });

    });



</script>



