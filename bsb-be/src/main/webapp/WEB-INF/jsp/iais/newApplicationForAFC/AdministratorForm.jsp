<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
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
                                                                Facility Admin
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
                                                                        <label class="control-label control-set-font control-font-label">Admin
                                                                            Name</label>
                                                                        <span class="mandatory">*</span>
                                                                        <span class="upload_controls"></span>
                                                                    </div>
                                                                    <div class="col-sm-4 col-md-7">
                                                                        <div class="">
                                                                            <iais:input maxLength="60" type="text"
                                                                                        id="adminName"
                                                                                        name=""
                                                                                        value=""/>
                                                                            <span class="error-msg" name="iaisErrorMsg"
                                                                                  id="adminNameError"></span>
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
                                                                        <label class="control-label control-set-font control-font-label">Type
                                                                            admin</label>
                                                                        <span class="mandatory">*</span>
                                                                        <span class="upload_controls"></span>
                                                                    </div>
                                                                    <div class="col-sm-4 col-md-7">
                                                                        <div class="">
                                                                            <select id="typeAdmin">
                                                                                <option>Please Select</option>
                                                                                <option>Main</option>
                                                                                <option>Alternate</option>
                                                                            </select>
                                                                            <span class="error-msg"
                                                                                  name="iaisErrorMsg"
                                                                                  id="typeAdminError"></span>
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
                                                                        <label class="control-label control-set-font control-font-label">ID
                                                                            Type</label>
                                                                        <span class="mandatory">*</span>
                                                                        <span class="upload_controls"></span>
                                                                    </div>
                                                                    <div class="col-sm-5 col-md-7">
                                                                        <div class="">
                                                                            <select id="idType">
                                                                                <option>Please Select</option>
                                                                                <option>NRIC</option>
                                                                                <option>FIN</option>
                                                                                <option>Passport No.</option>
                                                                            </select>
                                                                            <span class="error-msg"
                                                                                  name="idTypeError"
                                                                                  id="idTypeError"></span>
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
                                                                        <label class="control-label control-set-font control-font-label">ID
                                                                            No</label>
                                                                        <span class="mandatory">*</span>
                                                                        <span class="upload_controls"></span>
                                                                    </div>
                                                                    <div class="col-sm-5 col-md-7">
                                                                        <div class="">
                                                                            <iais:input maxLength="9" type="text"
                                                                                        id="idNo"
                                                                                        name=""
                                                                                        value=""/>
                                                                            <span class="error-msg"
                                                                                  name="idNoError"
                                                                                  id="idNoError"></span>
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
                                                                        <label class="control-label control-set-font control-font-label">Designation
                                                                            in the
                                                                            Facility/Organisation/Institution</label>
                                                                        <span class="mandatory">*</span>
                                                                        <span class="upload_controls"></span>
                                                                    </div>
                                                                    <div class="col-sm-5 col-md-7">
                                                                        <div class="professionRegoType">
                                                                            <iais:input maxLength="66" type="text"
                                                                                        id="designation"
                                                                                        name=""
                                                                                        value=""/>
                                                                            <span class="error-msg"
                                                                                  name="designationError"
                                                                                  id="designationError"></span>
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
                                                                            Contact No
                                                                        </label>
                                                                        <span class="mandatory">*</span>
                                                                        <span class="upload_controls"></span>
                                                                    </div>
                                                                    <div class="col-sm-5 col-md-7">
                                                                        <div class="">
                                                                            <iais:input maxLength="20" type="text"
                                                                                        id="contactNo"
                                                                                        name=""
                                                                                        value=""/>
                                                                            <span class="error-msg"
                                                                                  name="iaisErrorMsg"
                                                                                  id="contactNoError"></span>
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
                                                                               class="control-label control-set-font control-font-label">Email
                                                                            Address</label>
                                                                        <span class="mandatory">*</span>
                                                                    </div>
                                                                    <div class="col-sm-5 col-md-7">
                                                                        <div class="">
                                                                            <iais:input maxLength="66" type="text"
                                                                                        id="emailAddress"
                                                                                        name=""
                                                                                        value=""/>
                                                                            <span class="error-msg"
                                                                                  name="iaisErrorMsg"
                                                                                  id="emailAddressError"></span>
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
                                                                        <label class="control-label control-set-font control-font-label">Employment
                                                                            Start Date</label>
                                                                        <span class="mandatory">*</span>
                                                                    </div>
                                                                    <div class="col-sm-5 col-md-7">
                                                                        <div class="">
                                                                            <iais:datePicker value="dd/mm/yyyy" id="startDate"></iais:datePicker>
                                                                            <span class="error-msg"
                                                                                  name="iaisErrorMsg"
                                                                                  id="startDateError"></span>
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
    function validateAdministrator(num) {
        //date
        var startDate = $("#startDate").val();
        //textarea/input
        var adminName = $("#adminName").val();
        var idNo = $("#idNo").val();
        var emailAddress = $("#emailAddress").val();
        var designation = $("#designation").val();
        var contactNo = $("#contactNo").val();
        //select
        var idType = $("#idType").val();
        var typeAdmin = $("#typeAdmin").val();

        if (startDate == "dd/mm/yyyy") {
            $("#startDateError").html("This field is Mandatory");
            num++;
        } else {
            $("#startDateError").html("");
        }

        if (adminName == null || adminName == "") {
            $("#adminNameError").html("This field is Mandatory");
            num++;
        } else {
            $("#adminNameError").html("");
        }

        if (idNo == null || idNo == "") {
            $("#idNoError").html("This field is Mandatory");
            num++;
        } else {
            $("#idNoError").html("");
        }

        if (emailAddress == null || emailAddress == "") {
            $("#emailAddressError").html("This field is Mandatory");
            num++;
        } else {
            $("#emailAddressError").html("");
        }

        if (designation == null || designation == "") {
            $("#designationError").html("This field is Mandatory");
            num++;
        } else {
            $("#designationError").html("");
        }

        if (contactNo == null || contactNo == "") {
            $("#contactNoError").html("This field is Mandatory");
            num++;
        } else {
            $("#contactNoError").html("");
        }

        //
        if (idType == "Please Select") {
            $("#idTypeError").html("Please select a valid option");
            num++;
        } else {
            $("#idTypeError").html("");
        }

        if (typeAdmin == "Please Select") {
            $("#typeAdminError").html("Please select a valid option");
            num++;
        } else {
            $("#typeAdminError").html("");
        }
        return num;
    }
</script>
