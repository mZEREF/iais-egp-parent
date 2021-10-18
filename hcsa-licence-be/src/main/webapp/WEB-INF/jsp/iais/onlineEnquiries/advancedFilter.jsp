<c:if test="${count=='3'||count=='2'}">
    <h4>
                        <span >
                            Search By Application
                            <strong>&nbsp;
                                <input  id="applicationChk" type="radio"
                                        <c:if test="${count=='3'||count=='2'}">checked</c:if>   value="3"   name="searchChk"  />
                            </strong>
                        </span>
    </h4>
    <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
        <div class="panel-body">
            <div class="panel-main-content">
                <iais:section title="" id = "supPoolList">
                    <iais:row>
                        <iais:field value="Application No."/>
                        <div class="col-sm-7 col-md-4 col-xs-10">

                            <input type="text" maxlength="20" style=" font-weight:normal;" name="application_no" value="${SearchParam.filters['appNo']}" />

                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Application Type"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <div id="application_type">
                                <iais:select name="application_type" options="appTypeOption" firstOption="Please Select" value="${SearchParam.filters['appType']}" ></iais:select>
                            </div>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Application Status"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <div id="application_status">
                                <iais:select name="application_status" options="appStatusOption" firstOption="Please Select" value="${SearchParam.filters['appStatus']}" ></iais:select>
                            </div>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Application Submitted Date From"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:datePicker id = "sub_date" name = "sub_date" value="${SearchParam.filters['subDate']}" ></iais:datePicker>
                            <span style="font-weight:normal;" id="error_to_date" name="iaisErrorMsg" class="error-msg" ></span>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Application Submitted Date To"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:datePicker id = "to_date" name = "to_date" value="${SearchParam.filters['toDate']}"></iais:datePicker>
                        </div >
                    </iais:row>

                    <iais:row>
                        <iais:field value="Licence No."/>
                        <div class="col-sm-7 col-md-4 col-xs-10">

                            <input type="text" maxlength="24" style=" font-weight:normal;" name="licence_no" value="${SearchParam.filters['licence_no']}" />

                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="UEN No."/>
                        <div class="col-sm-7 col-md-4 col-xs-10">

                            <input type="text" maxlength="10" style=" font-weight:normal;" name="uen_no" value="${SearchParam.filters['uen_no']}" />

                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Service Type"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <div id="service_licence_type">
                                <iais:select name="service_licence_type" options="licSvcTypeOption" firstOption="Please Select" value="${SearchParam.filters['svc_name']}" ></iais:select>
                            </div>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Service Sub-Type"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <div id="service_sub_type">
                                <iais:select name="service_sub_type" options="licSvcSubTypeOption" firstOption="Please Select" value="${SearchParam.filters['serviceSubTypeName']}" ></iais:select>
                            </div>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Licence Status"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <div id="licence_status">
                                <iais:select name="licence_status" options="licStatusOption" firstOption="Please Select" value="${SearchParam.filters['licence_status']}" ></iais:select>
                            </div>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Licence Start Date From"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:datePicker id = "start_date" name = "start_date" value="${SearchParam.filters['start_date']}" ></iais:datePicker>
                            <span style="font-weight:normal;" id="error_start_to_date" name="iaisErrorMsg" class="error-msg" ></span>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Licence Start Date To"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:datePicker id = "start_to_date" name = "start_to_date" value="${SearchParam.filters['start_to_date']}"></iais:datePicker>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Licence Expiry Date From"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:datePicker id = "expiry_start_date" name = "expiry_start_date" value="${SearchParam.filters['expiry_start_date']}" ></iais:datePicker>
                            <span style="font-weight:normal;" id="error_expiry_date" name="iaisErrorMsg" class="error-msg" ></span>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Licence Expiry Date To"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:datePicker id = "expiry_date" name = "expiry_date" value="${SearchParam.filters['expiry_date']}"></iais:datePicker>
                        </div >
                    </iais:row>
                </iais:section>
            </div>
        </div>
    </div>
</c:if>
<c:if test="${count=='1'}">
    <h4>
                        <span >Search By HCI
                            <strong >&nbsp;
                                <input  id="hciChk" type="radio"
                                        name="searchChk" <c:if test="${count=='1'}">checked</c:if>   value="1"  />
                            </strong>
                        </span>
    </h4>

    <div class="panel-collapse collapse in" id="collapseTwo" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
        <div class="panel-body">
            <div class="panel-main-content">
                <iais:section title="" id = "supPoolList">
                    <iais:row>
                        <iais:field value="HCI Code"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">

                            <input type="text" maxlength="7" style=" font-weight:normal;" name="hci_code" value="${SearchParam.filters['hciCode']}" />

                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="HCI Name"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">

                            <input type="text" maxlength="100" style=" font-weight:normal;" name="hci_name" value="${SearchParam.filters['hciName']}" />

                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="HCI Street Name"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">

                            <input type="text" maxlength="32" style=" font-weight:normal;" name="hci_street_name" value="${SearchParam.filters['hciStreetName']}" />

                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="HCI Postal Code"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">

                            <input type="text" maxlength="6" onpaste="return false" oncontextmenu="return false" onkeypress="keyPress()" oninput = "value=value.replace(/[^\d]/g,'')"  style=" font-weight:normal;" name="hci_postal_code" value="${SearchParam.filters['hciPostalCode']}" />

                        </div >
                    </iais:row>

                </iais:section>
            </div>
        </div>
    </div>
</c:if>
<c:if test="${count=='4'||count=='6'}">
    <h4>
                        <span >Search By Licensee
                            <strong >&nbsp;
                                <input  id="licenseeChk" type="radio" <c:if test="${count=='4'||count=='6'}">checked</c:if>   value="4"   name="searchChk"  />
                            </strong>
                        </span>
    </h4>

    <div class="panel-collapse collapse in" id="collapseThree" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
        <div class="panel-body">
            <div class="panel-main-content">
                <iais:section title="" id = "supPoolList">
                    <iais:row>
                        <iais:field value="Licensee ID"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">

                            <input type="text" maxlength="10" style=" font-weight:normal;" name="licensee_idNo" value="${SearchParam.filters['licenseeIdNo']}" />

                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Licensee Name"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">

                            <input type="text" maxlength="50" style=" font-weight:normal;" name="licensee_name" value="${SearchParam.filters['licenseeName']}" />

                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Licensee Professional Regn. No."/>
                        <div class="col-sm-7 col-md-4 col-xs-10">

                            <input type="text" maxlength="20" style=" font-weight:normal;" name="licensee_regn_no" value="${SearchParam.filters['licenseeRegnNo']}" />

                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="UEN"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">

                            <input type="text" maxlength="10" style=" font-weight:normal;" name="uen_no" value="${SearchParam.filters['uen_no']}" />

                        </div >
                    </iais:row>
                </iais:section>
            </div>
        </div>
    </div>
</c:if>
<c:if test="${count=='5'}">
    <h4>
                        <span>Search By Service Personnel Details (CGO, PO, DPO & MedAlert)
                            <strong >&nbsp;
                                <input  id="servicePersonnelChk" type="radio" <c:if test="${count=='5'}">checked</c:if>   value="5"  name="searchChk"  />
                            </strong>
                        </span>
    </h4>

    <div class="panel-collapse collapse in" id="collapseFour" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
        <div class="panel-body">
            <div class="panel-main-content">
                <iais:section title="" id = "supPoolList">
                    <iais:row>
                        <iais:field value="Service Personnel ID"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">

                            <input type="text" maxlength="10" style=" font-weight:normal;" name="personnelId" value="${SearchParam.filters['personnelId']}" />

                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Service Personnel Name"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">

                            <input type="text" maxlength="50" style=" font-weight:normal;" name="personnelName" value="${SearchParam.filters['personnelName']}" />

                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Service Personnel Role"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <div id="personnelRoleOption">
                                <iais:select name="personnelRole" options="servicePersonnelRoleOption" firstOption="Please Select" value="${SearchParam.filters['personnelRole']}" ></iais:select>
                            </div>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Service Personnel Professional Regn. No."/>
                        <div class="col-sm-7 col-md-4 col-xs-10">

                            <input type="text" maxlength="20" style=" font-weight:normal;" name="personnelRegnNo" value="${SearchParam.filters['personnelRegnNo']}" />

                        </div >
                    </iais:row>
                </iais:section>
            </div>
        </div>
    </div>
</c:if>
<div class="col-xs-12 col-md-12">
    <iais:action style="text-align:right;">
        <a style=" float:left;padding-top: 1.1%;text-decoration:none;" href="#" onclick="javascript:doLicBack()"><em class="fa fa-angle-left"> </em> Back</a>
        <button class="btn btn-secondary" type="button"  onclick="javascript:doLicClear()">Clear</button>
        <button class="btn btn-primary" type="button"  onclick="javascript:doLicSearch()">Search</button>
    </iais:action>
</div>
