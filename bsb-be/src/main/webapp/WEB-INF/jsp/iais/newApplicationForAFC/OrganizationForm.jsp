<style>
    table.control-grid.columns1 > tbody > tr > td > .section.control input[type=text], table.control-grid.columns1 > tbody > tr > td > .section.control input[type=email], table.control-grid.columns1 > tbody > tr > td > .section.control input[type=number], table.control-grid.columns1 > tbody > tr > td > .section.control .nice-select {
        margin-bottom: 15px;
        margin-top: 25px;
    }

    .control-font-label {
        margin-top: 19px;
    }
</style>
<div id="formPanel" class="sopform ui-tabs ui-widget ui-widget-content ui-corner-all" style="display: block;">
    <div class="form-tab-panel ui-tabs-panel ui-widget-content ui-corner-bottom" id="tab_page_0">
        <div id="control--runtime--0" class="page control control-area  container-p-1">
            <div id="control--runtime--0--errorMsg_page_top" class="error_placements"></div>
            <table class="control-grid columns1 " style="width: 100%;">
                <tbody>
                <tr height="1">
                    <td class="first last" style="width: 100%;">
                        <div id="control--runtime--85" class="control control-caption-horizontal">
                        </div>
                    </td>
                </tr>
                <tr height="1">
                    <td class="first last" style="width: 100%;">
                        <div id="control--runtime--1" class="section control  container-s-1">
                            <div class="control-set-font control-font-header section-header"></div>
                            <div class="cgo-content">
                                <table class="assignContent control-grid" style="width:100%;">
                                    <tbody>
                                    <tr height="1">
                                        <td class="first last" style="width: 100%;">

                                            <div id="control--runtime--" class="control control-caption-horizontal">
                                                <div class=" form-group form-horizontal formgap">
                                                    <div class="col-sm-8 control-label formtext control">
                                                        <div class="cgo-header">
                                                            <strong>
                                                                Organisation Info
                                                                <label class="assign-psn-item"></label>
                                                            </strong>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div id="newOfficer" class="new-officer-form">
                                                <hr/>
                                                <table class="control-grid">
                                                    <tbody>
                                                    <tr height="1">
                                                        <td class="first last" style="">
                                                            <div class="control control-caption-horizontal">
                                                                <div class=" form-group form-horizontal formgap">
                                                                    <div class="col-sm-4 control-label formtext ">
                                                                        <label class="control-label control-set-font control-font-label">Organisation
                                                                            Name</label>
                                                                        <span class="mandatory">*</span>
                                                                        <span class="upload_controls"></span>
                                                                    </div>
                                                                    <div class="col-sm-4 col-md-7">
                                                                        <div class="">
                                                                            <textarea id="organisationName"
                                                                                      name="organisationName"
                                                                                      cols="62"
                                                                                      rows="4"
                                                                                      maxlength="250"></textarea>
                                                                            <span class="error-msg" name="iaisErrorMsg"
                                                                                  id="organisationNameError"></span>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </td>
                                                    </tr>

                                                    <tr height="1">
                                                        <td class="first last" style="">
                                                            <div class="control control-caption-horizontal">
                                                                <div class=" form-group form-horizontal formgap">
                                                                    <div class="col-sm-4 control-label formtext ">
                                                                        <label class="control-label control-set-font control-font-label">Address
                                                                            Type</label>
                                                                        <span class="mandatory">*</span>
                                                                        <span class="upload_controls"></span>
                                                                    </div>
                                                                    <div class="col-sm-4 col-md-7">
                                                                        <div class="">
                                                                            <select id="addressType">
                                                                                <option>Please Select</option>
                                                                                <option>Local</option>
                                                                                <option>Overseas</option>
                                                                            </select>
                                                                            <span class="error-msg"
                                                                                  name="iaisErrorMsg"
                                                                                  id="addressTypeError"></span>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </td>
                                                    </tr>

                                                    <tr height="1">
                                                        <td class="first last" style="width: 100%;">
                                                            <div class="control control-caption-horizontal">
                                                                <div class="form-group form-horizontal formgap">
                                                                    <div class="col-sm-4 control-label formtext">
                                                                        <label class="control-label control-set-font control-font-label">Floor</label>
                                                                        <span class="mandatory">*</span>
                                                                        <span class="upload_controls"></span>
                                                                    </div>
                                                                    <div class="col-sm-5 col-md-7"
                                                                         id="Floor">
                                                                        <div class="">
                                                                            <iais:input maxLength="4" type="text"
                                                                                        id="floor"
                                                                                        name=""
                                                                                        value=""/>
                                                                            <span class="error-msg"
                                                                                  name="iaisErrorMsg"
                                                                                  id="floorError"></span>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </td>
                                                    </tr>

                                                    <tr height="1">
                                                        <td class="first last" style="width: 100%;">
                                                            <div class="control control-caption-horizontal">
                                                                <div class="form-group form-horizontal formgap">
                                                                    <div class="col-sm-4 control-label formtext">
                                                                        <label class="control-label control-set-font control-font-label">Unit</label>
                                                                        <span class="mandatory">*</span>
                                                                        <span class="upload_controls"></span>
                                                                    </div>
                                                                    <div class="col-sm-5 col-md-7"
                                                                         id="designation">
                                                                        <div class="">
                                                                            <iais:input maxLength="4" type="text"
                                                                                        id="unit"
                                                                                        name=""
                                                                                        value=""/>
                                                                            <span class="error-msg"
                                                                                  name="iaisErrorMsg"
                                                                                  id="unitError"></span>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </td>
                                                    </tr>

                                                    <tr height="1">
                                                        <td class="first last" style="width: 100%;">
                                                            <div class="control control-caption-horizontal">
                                                                <div class="form-group form-horizontal formgap">
                                                                    <div class="col-sm-4 control-label formtext">
                                                                        <label class="control-label control-set-font control-font-label">Building</label>
                                                                        <span class="mandatory">*</span>
                                                                        <span class="upload_controls"></span>
                                                                    </div>
                                                                    <div class="col-sm-5 col-md-7"
                                                                         id="professionType">
                                                                        <div class="professionRegoType">
                                                                            <iais:input maxLength="4" type="text"
                                                                                        id="building"
                                                                                        name=""/>
                                                                            <span class="error-msg"
                                                                                  name="iaisErrorMsg"
                                                                                  id="buildingError"></span>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </td>
                                                    </tr>

                                                    <tr height="1">
                                                        <td class="first last" style="width: 100%;">
                                                            <div id="control--runtime--31"
                                                                 class="control control-caption-horizontal">
                                                                <div class=" form-group form-horizontal formgap">
                                                                    <div class="col-sm-4 control-label formtext ">
                                                                        <label id="control--runtime--31--label"
                                                                               class="control-label control-set-font control-font-label">
                                                                            Street Name
                                                                        </label>
                                                                        <span class="mandatory">*</span>
                                                                        <span class="upload_controls"></span>
                                                                    </div>
                                                                    <div class="col-sm-5 col-md-7">
                                                                        <div class="">
                                                                            <iais:input maxLength="32" type="text"
                                                                                        id="street"
                                                                                        name=""/>
                                                                            <span class="error-msg"
                                                                                  name="iaisErrorMsg"
                                                                                  id="streetError"></span>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </td>
                                                    </tr>

                                                    <tr height="1">
                                                        <td class="first last" style="width: 100%;">
                                                            <div id="control--runtime--29"
                                                                 class="control control-caption-horizontal">
                                                                <div class="form-group form-horizontal formgap">
                                                                    <div class="col-sm-4 control-label formtext">
                                                                        <label id="control--runtime--29--label"
                                                                               class="control-label control-set-font control-font-label">Address
                                                                            1</label>
                                                                    </div>
                                                                    <div class="col-sm-5 col-md-7">
                                                                        <div class="">
                                                                            <iais:input maxLength="35" type="text"
                                                                                        name=""/>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </td>
                                                    </tr>

                                                    <tr height="1">
                                                        <td class="first last" style="width: 100%;">
                                                            <div class="control control-caption-horizontal">
                                                                <div class="form-group form-horizontal formgap">
                                                                    <div class="col-sm-4 control-label formtext">
                                                                        <label class="control-label control-set-font control-font-label">Address
                                                                            2</label>
                                                                    </div>
                                                                    <div class="col-sm-5 col-md-7">
                                                                        <div class="">
                                                                            <iais:input maxLength="35" type="text"
                                                                                        name=""/>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </td>
                                                    </tr>

                                                    <tr height="1">
                                                        <td class="first last" style="width: 100%;">
                                                            <div class="control control-caption-horizontal">
                                                                <div class="form-group form-horizontal formgap">
                                                                    <div class="col-sm-4 control-label formtext">
                                                                        <label class="control-label control-set-font control-font-label">Address
                                                                            3</label>
                                                                    </div>
                                                                    <div class="col-sm-5 col-md-7">
                                                                        <div class="">
                                                                            <iais:input maxLength="35" type="text"
                                                                                        name=""/>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </td>
                                                    </tr>

                                                    <tr height="1">
                                                        <td class="first last" style="width: 100%;">
                                                            <div class="control control-caption-horizontal">
                                                                <div class="form-group form-horizontal formgap">
                                                                    <div class="col-sm-4 control-label formtext">
                                                                        <label class="control-label control-set-font control-font-label">Postal
                                                                            Code</label>
                                                                        <span class="mandatory">*</span>
                                                                        <span class="mandatory otherQualificationSpan"></span>
                                                                    </div>
                                                                    <div class="col-sm-5 col-md-7">
                                                                        <div class="">
                                                                            <iais:input maxLength="15" type="text"
                                                                                        id="postalCode"
                                                                                        name=""/>
                                                                            <span class="error-msg" name="iaisErrorMsg"
                                                                                  id="postalCodeError"></span>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </td>
                                                    </tr>

                                                    <tr height="1">
                                                        <td class="first last" style="width: 100%;">
                                                            <div class="control control-caption-horizontal">
                                                                <div class=" form-group form-horizontal formgap">
                                                                    <div class="col-sm-4 control-label formtext ">
                                                                        <label class="control-label control-set-font control-font-label">City</label>
                                                                        <span class="mandatory">*</span>
                                                                        <span class="upload_controls"></span>
                                                                    </div>
                                                                    <div class="col-sm-5 col-md-7">
                                                                        <div class="">
                                                                            <iais:input maxLength="30" id="city"
                                                                                        type="text"
                                                                                        name=""
                                                                                        value=""/>
                                                                            <span class="error-msg"
                                                                                  name="iaisErrorMsg"
                                                                                  id="cityError"></span>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </td>
                                                    </tr>

                                                    <tr height="1">
                                                        <td class="first last" style="">
                                                            <div class="control control-caption-horizontal">
                                                                <div class=" form-group form-horizontal formgap">
                                                                    <div class="col-sm-4 control-label formtext ">
                                                                        <label
                                                                                class="control-label control-set-font control-font-label">State</label>
                                                                        <span class="mandatory">*</span>
                                                                        <span class="upload_controls"></span>
                                                                    </div>
                                                                    <div class="col-sm-4 col-md-7">
                                                                        <div class="">
                                                                            <iais:input maxLength="66" id="state"
                                                                                        type="text"
                                                                                        name="emailAddress"/>
                                                                            <span class="error-msg"
                                                                                  name="iaisErrorMsg"
                                                                                  id="stateError"></span>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </td>
                                                    </tr>

                                                    <tr height="1">
                                                        <td class="first last" style="">
                                                            <div class="control control-caption-horizontal">
                                                                <div class=" form-group form-horizontal formgap">
                                                                    <div class="col-sm-4 control-label formtext ">
                                                                        <label class="control-label control-set-font control-font-label">Country</label>
                                                                        <span class="mandatory">*</span>
                                                                        <span class="upload_controls"></span>
                                                                    </div>
                                                                    <div class="col-sm-4 col-md-7">
                                                                        <div class="">
                                                                            <select id="country">
                                                                                <option>Please Select</option>
                                                                                <option>Singapore</option>
                                                                            </select>
                                                                            <span class="error-msg"
                                                                                  name="iaisErrorMsg"
                                                                                  id="countryError"></span>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </td>
                                                    </tr>

                                                    <tr height="1">
                                                        <td class="first last" style="">
                                                            <div
                                                                    class="control control-caption-horizontal">
                                                                <div class=" form-group form-horizontal formgap">
                                                                    <div class="col-sm-4 control-label formtext ">
                                                                        <label class="control-label control-set-font control-font-label">Year
                                                                            Established</label>
                                                                        <span class="mandatory">*</span>
                                                                        <span class="upload_controls"></span>
                                                                    </div>
                                                                    <div class="col-sm-4 col-md-7">
                                                                        <div class="">
                                                                            <iais:input maxLength="4"
                                                                                        id="yearEstablished"
                                                                                        type="number"
                                                                                        name=""/>
                                                                            <span class="error-msg"
                                                                                  name="iaisErrorMsg"
                                                                                  id="yearEstablishedError"></span>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </td>
                                                    </tr>

                                                    <tr height="1">
                                                        <td class="first last" style="">
                                                            <div
                                                                    class="control control-caption-horizontal">
                                                                <div class=" form-group form-horizontal formgap">
                                                                    <div class="col-sm-4 control-label formtext ">
                                                                        <label class="control-label control-set-font control-font-label">Tel
                                                                            No</label>
                                                                        <span class="mandatory">*</span>
                                                                        <span class="upload_controls"></span>
                                                                    </div>
                                                                    <div class="col-sm-4 col-md-7">
                                                                        <div class="">
                                                                            <iais:input maxLength="20" id="telNo"
                                                                                        type="text"
                                                                                        name=""/>
                                                                            <span class="error-msg"
                                                                                  name="iaisErrorMsg"
                                                                                  id="telNoError"></span>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </td>
                                                    </tr>

                                                    <tr height="1">
                                                        <td class="first last" style="">
                                                            <div
                                                                    class="control control-caption-horizontal">
                                                                <div class=" form-group form-horizontal formgap">
                                                                    <div class="col-sm-4 control-label formtext ">
                                                                        <label
                                                                                class="control-label control-set-font control-font-label">Email</label>
                                                                        <span class="mandatory">*</span>
                                                                        <span class="upload_controls"></span>
                                                                    </div>
                                                                    <div class="col-sm-4 col-md-7">
                                                                        <div class="">
                                                                            <iais:input maxLength="66" id="email"
                                                                                        type="text"
                                                                                        name=""/>
                                                                            <span class="error-msg"
                                                                                  name="iaisErrorMsg"
                                                                                  id="emailError"></span>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </td>
                                                    </tr>

                                                    <tr height="1">
                                                        <td class="first last" style="">
                                                            <div class="control control-caption-horizontal">
                                                                <div class=" form-group form-horizontal formgap">
                                                                    <div class="col-sm-4 control-label formtext ">
                                                                        <label
                                                                                class="control-label control-set-font control-font-label">Contact
                                                                            Person</label>
                                                                        <span class="mandatory">*</span>
                                                                        <span class="upload_controls"></span>
                                                                    </div>
                                                                    <div class="col-sm-4 col-md-7">
                                                                        <div class="">
                                                                            <textarea id="contactPerson"
                                                                                      name="contactPerson"
                                                                                      cols="62"
                                                                                      rows="4"
                                                                                      maxlength="132"></textarea>
                                                                            <span class="error-msg"
                                                                                  name="iaisErrorMsg"
                                                                                  id="contactPersonError"></span>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div class="cgo-content-point"></div>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
<script>
    function validateOrganization(num) {
        //textarea/input
        var organisationName = $("#organisationName").val();
        var floor = $("#floor").val();
        var unit = $("#unit").val();
        var building = $("#building").val();
        var street = $("#street").val();
        var postalCode = $("#postalCode").val();
        var city = $("#city").val();
        var state = $("#state").val();
        var yearEstablished = $("#yearEstablished").val();
        var telNo = $("#telNo").val();
        var email = $("#email").val();
        var contactPerson = $("#contactPerson").val();
        //select
        var country = $("#country").val();
        var addressType = $("#addressType").val();

        if (organisationName=="" || organisationName == null) {
            $("#organisationNameError").html("This field is Mandatory");
            num++;
        } else {
            $("#organisationNameError").html("");
        }

        if (floor == null || floor == "") {
            $("#floorError").html("This field is Mandatory");
            num++;
        } else {
            $("#floorError").html("");
        }

        if (unit == null || unit == "") {
            $("#unitError").html("This field is Mandatory");
            num++;
        } else {
            $("#unitError").html("");
        }

        if (building == null || building == "") {
            $("#buildingError").html("This field is Mandatory");
            num++;
        } else {
            $("#buildingError").html("");
        }

        if (street == null || street == "") {
            $("#streetError").html("This field is Mandatory");
            num++;
        } else {
            $("#streetError").html("");
        }

        if (postalCode == null || postalCode == "") {
            $("#postalCodeError").html("This field is Mandatory");
            num++;
        } else {
            $("#postalCodeError").html("");
        }

        if (city == null || city == "") {
            $("#cityError").html("This field is Mandatory");
            num++;
        } else {
            $("#cityError").html("");
        }

        if (state == null || state == "") {
            $("#stateError").html("This field is Mandatory");
            num++;
        } else {
            $("#stateError").html("");
        }

        if (yearEstablished == null || yearEstablished == "") {
            $("#yearEstablishedError").html("This field is Mandatory");
            num++;
        } else {
            $("#yearEstablishedError").html("");
        }

        if (telNo == null || telNo == "") {
            $("#telNoError").html("This field is Mandatory");
            num++;
        } else {
            $("#telNoError").html("");
        }

        if (email == null || email == "") {
            $("#emailError").html("This field is Mandatory");
            num++;
        } else {
            $("#emailError").html("");
        }

        if (contactPerson == null || contactPerson == "") {
            $("#contactPersonError").html("This field is Mandatory");
            num++;
        } else {
            $("#contactPersonError").html("");
        }

        //
        if (country == "Please Select") {
            $("#countryError").html("Please select a valid option");
            num++;
        } else {
            $("#countryError").html("");
        }

        if (addressType == "Please Select") {
            $("#addressTypeError").html("Please select a valid option");
            num++;
        } else {
            $("#addressTypeError").html("");
        }
        return num;
    }
</script>
