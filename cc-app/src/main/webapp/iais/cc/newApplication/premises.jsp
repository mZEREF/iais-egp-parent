<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>

<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-cc"/>
<%@ include file="./dashboard.jsp" %>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
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
                                                <div class="col-xs-6 col-md-2">
                                                    <div class="form-check">
                                                        <input class="form-check-input" id="premise_onsite" type="radio" name="premisesType" value = "On-site" aria-invalid="false">
                                                        <label class="form-check-label" for="premise_onsite"><span class="check-circle"></span>On-site</label>
                                                    </div>
                                                </div>
                                                <div class="col-xs-6 col-md-2">
                                                    <div class="form-check">
                                                        <input class="form-check-input" id="premise_conveyance" type="radio" name="premisesType" value="Conveyance" aria-invalid="false">
                                                        <label class="form-check-label" for="premise_conveyance"><span class="check-circle"></span>Conveyance</label>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group premiseLocationSelect hidden">
                                                <label class="col-xs-12 col-md-4 control-label" for="premisesSelect">Add or select a premises from the list</label>
                                                <div class="col-xs-11 col-sm-7 col-md-5">
                                                    <select id="premisesSelect">
                                                        <option>Select One</option>
                                                        <option value="newPremise">Add a new premises</option>
                                                        <option>111 North Bridge Rd # 07-04, 179098</option>
                                                        <option>514 Chai Chee Lane # 06-03, 65432</option>
                                                        <option>8 Foch Rd, 209786</option>
                                                        <option>400 Orchard Rd, 21-06 Orchard Tower, 23654</option>
                                                    </select>
                                                </div>
                                            </div>
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
                                                    <iais:field value="Name of premises" width="10"/>
                                                    <iais:value width="10">
                                                        <iais:input type="text" name="hciName" id="sitePremiseName"></iais:input>
                                                    </iais:value>
                                                </iais:row>
                                                <div class="form-group">
                                                    <label class="col-xs-10 col-md-4 control-label" for="sitePremiseName">Name of premises</label>
                                                    <div class="col-xs-10 col-sm-7 col-md-6">
                                                        <input id="sitePremiseName" type="text" name ="hciName">
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-12 col-md-4 control-label" for="sitePostalCode">Postal Code</label>
                                                    <div class="col-xs-5 col-sm-4 col-md-2">
                                                        <input id="sitePostalCode" type="text" name = "postalCode">
                                                    </div>
                                                    <div class="col-xs-7 col-sm-6 col-md-4">
                                                        <p><a href="#">Retrieve your address</a></p>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-12 col-md-4 control-label" for="siteBlockNo">Block / House No.</label>
                                                    <div class="col-xs-5 col-sm-4 col-md-2">
                                                        <input id="siteBlockNo" type="text" name="blkNo">
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-10 col-md-4 control-label" for="siteStreetName">Street Name</label>
                                                    <div class="col-xs-10 col-sm-7 col-md-5">
                                                        <input id="siteStreetName" type="text" name = "streetName">
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-12 col-md-4 control-label" for="siteFloorNo">Floor No.</label>
                                                    <div class="col-xs-7 col-sm-4 col-md-3 input-with-label">
                                                        <input id="siteFloorNo" type="text" name = "floorNo">
                                                        <p class="small-txt">(Optional)</p>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-12 col-md-4 control-label" for="siteUnitNo">Unit No.</label>
                                                    <div class="col-xs-7 col-sm-4 col-md-3 input-with-label">
                                                        <input id="siteUnitNo" type="text" name = "unitNo">
                                                        <p class="small-txt">(Optional)</p>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-12 col-md-4 control-label" for="siteBuildingName">Building Name</label>
                                                    <div class="col-xs-11 col-sm-7 col-md-6 input-with-label">
                                                        <input id="siteBuildingName" type="text" name ="buildingName">
                                                        <p class="small-txt">(Optional)</p>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-12 col-md-4 control-label" for="siteAddressType">Address Type</label>
                                                    <div class="col-xs-7 col-sm-4 col-md-3">
                                                        <select id="siteAddressType" name = "addrType">
                                                            <option>Select address type</option>
                                                            <option>Apt Blk</option>
                                                            <option>Without Apt Blk</option>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-12 col-md-4 control-label" for="siteSafefyNo">Fire Safety Shelter Bureau Ref. No. <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" title="&lt;p&gt;This is a xxx digit No. that you can access from the Life Saving Force Portral.&lt;/p&gt;">i</a></label>
                                                    <div class="col-xs-9 col-sm-5 col-md-4">
                                                        <input id="siteSafefyNo" type="text">
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="new-premise-form-conveyance hidden">
                                            <div class="form-horizontal">
                                                <div class="form-group">
                                                    <label class="col-xs-12 col-md-4 control-label" for="vehicleNo">Vehicle No.</label>
                                                    <div class="col-xs-9 col-sm-7 col-md-6">
                                                        <input id="vehicleNo" type="text">
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-12 col-md-4 control-label" for="vehicleOwnerName">Vehicle Owner's Name</label>
                                                    <div class="col-xs-4 col-sm-2 col-md-2">
                                                        <select id="vehicleOwnerNameSalutation" aria-label="vehicleOwnerNameSalutation">
                                                            <option>Mr</option>
                                                            <option>Mrs</option>
                                                            <option>Ms</option>
                                                            <option>Mam</option>
                                                        </select>
                                                    </div>
                                                    <div class="col-xs-7 col-sm-4 col-md-5">
                                                        <input id="vehicleOwnerName" type="text">
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-12 col-md-4 control-label" for="conveyancePostalCode">Postal Code</label>
                                                    <div class="col-xs-5 col-sm-4 col-md-2">
                                                        <input id="conveyancePostalCode" type="text">
                                                    </div>
                                                    <div class="col-xs-7 col-sm-6 col-md-4">
                                                        <p><a href="#">Retrieve your address</a></p>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-12 col-md-4 control-label" for="conveyanceBlockNo">Block / House No.</label>
                                                    <div class="col-xs-5 col-sm-4 col-md-2">
                                                        <input id="conveyanceBlockNo" type="text">
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-10 col-md-4 control-label" for="conveyanceStreetName">Street Name</label>
                                                    <div class="col-xs-10 col-sm-7 col-md-5">
                                                        <input id="conveyanceStreetName" type="text">
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-12 col-md-4 control-label" for="conveyanceFloorNo">Floor No.</label>
                                                    <div class="col-xs-7 col-sm-4 col-md-3 input-with-label">
                                                        <input id="conveyanceFloorNo" type="text">
                                                        <p class="small-txt">(Optional)</p>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-12 col-md-4 control-label" for="conveyanceUnitNo">Unit No.</label>
                                                    <div class="col-xs-7 col-sm-4 col-md-3 input-with-label">
                                                        <input id="conveyanceUnitNo" type="text">
                                                        <p class="small-txt">(Optional)</p>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-12 col-md-4 control-label" for="conveyanceBuildingName">Building Name </label>
                                                    <div class="col-xs-11 col-sm-7 col-md-6 input-with-label">
                                                        <input id="conveyanceBuildingName" type="text">
                                                        <p class="small-txt">(Optional)</p>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-12 col-md-4 control-label" for="conveyanceAddressType">Address Type</label>
                                                    <div class="col-xs-7 col-sm-4 col-md-3">
                                                        <select id="conveyanceAddressType">
                                                            <option>Select address type</option>
                                                            <option>Apt Blk</option>
                                                            <option>Without Apt Blk</option>
                                                            <option>Without Apt Blk</option>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-12 col-md-4 control-label" for="conveyanceEmail">Email Address </label>
                                                    <div class="col-xs-9 col-sm-5 col-sm-4">
                                                        <input id="conveyanceEmail" type="email">
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-xs-12 col-md-4 control-label" for="conveyanceMobile">Mobile number </label>
                                                    <div class="col-xs-7 col-sm-4 col-md-3">
                                                        <input id="conveyanceMobile" type="number">
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="application-tab-footer">
                                    <div class="row">
                                        <div class="col-xs-12 col-sm-6">
                                            <p><a class="back" href="#"><i class="fa fa-angle-left"></i> Back</a></p>
                                        </div>
                                        <div class="col-xs-12 col-sm-6">
                                            <div class="button-group"><a class="btn btn-secondary" href="#">Save as Draft</a><a class="btn btn-primary next" href="application-document.html">Next</a></div>
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
