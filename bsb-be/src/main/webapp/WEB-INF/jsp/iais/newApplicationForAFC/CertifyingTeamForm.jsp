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
                                            <div id="control--runtime--2" class="control control-caption-horizontal">
                                                <div class=" form-group form-horizontal formgap">
                                                    <div class="col-sm-5 control-label formtext ">
                                                        <label id="control--runtime--2--label"
                                                               class="control-label control-set-font control-font-label">Name</label>
                                                        <span class="mandatory">*</span>
                                                        <span class="upload_controls"></span>
                                                    </div>
                                                    <div class="col-sm-5 col-md-7"
                                                         id="assignSelect">
                                                        <div class="ax_default text_area">
                                                            <textarea id="nameId"
                                                                      name="name"
                                                                      cols="62"
                                                                      rows="4"
                                                                      maxlength="132"></textarea>
                                                            <span class="error-msg" name="iaisErrorMsg"
                                                                  id="nameIdError"></span>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="profile-info-gp hidden"></div>
                                            <div id="newOfficer" class="new-officer-form">
                                                <table class="control-grid">
                                                    <tbody>
                                                    <tr height="1">
                                                        <td class="first last" style="">
                                                            <div class="control control-caption-horizontal">
                                                                <div class=" form-group form-horizontal formgap">
                                                                    <div class="col-sm-4 control-label formtext ">
                                                                        <label class="control-label control-set-font control-font-label">ID
                                                                            Type</label>
                                                                        <span class="mandatory">*</span>
                                                                        <span class="upload_controls"></span>
                                                                    </div>
                                                                    <div class="col-sm-4 col-md-7">
                                                                        <div class="">
                                                                            <select id="idType">
                                                                                <option>Please Select</option>
                                                                                <option>NRIC</option>
                                                                                <option>FIN</option>
                                                                                <option>Passport No</option>
                                                                            </select>
                                                                            <span class="error-msg"
                                                                                  name="iaisErrorMsg"
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
                                                                    <div class="col-sm-5 col-md-7"
                                                                         id="Floor">
                                                                        <div class="">
                                                                            <iais:input maxLength="10" type="text"
                                                                                        id="idNo"
                                                                                        name="idNo"/>
                                                                            <span class="error-msg"
                                                                                  name="iaisErrorMsg"
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
                                                                        <label class="control-label control-set-font control-font-label">Date
                                                                            of Birth</label>
                                                                        <span class="mandatory">*</span>
                                                                        <span class="upload_controls"></span>
                                                                    </div>
                                                                    <div class="col-sm-5 col-md-7"
                                                                         id="designation">
                                                                        <div class="">
                                                                            <iais:datePicker value="dd/mm/yyyy"
                                                                                             id="birthDay"></iais:datePicker>
                                                                            <span class="error-msg"
                                                                                  name="iaisErrorMsg"
                                                                                  id="birthDayError"></span>
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
                                                                        <label class="control-label control-set-font control-font-label">Sex</label>
                                                                        <span class="mandatory">*</span>
                                                                        <span class="upload_controls"></span>
                                                                    </div>
                                                                    <div class="col-sm-5 col-md-7"
                                                                         id="professionType">
                                                                        <div class="professionRegoType">
                                                                            <select id="sex">
                                                                                <option>Please Select</option>
                                                                                <option>Male</option>
                                                                                <option>Female</option>
                                                                            </select>
                                                                            <span class="error-msg"
                                                                                  name="iaisErrorMsg"
                                                                                  id="sexError"></span>
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
                                                                            Nationality
                                                                        </label>
                                                                        <span class="mandatory">*</span>
                                                                        <span class="upload_controls"></span>
                                                                    </div>
                                                                    <div class="col-sm-5 col-md-7">
                                                                        <div class="">
                                                                            <select id="nationality">
                                                                                <option>Please Select</option>
                                                                                <option>Singapore</option>
                                                                            </select>
                                                                            <span class="error-msg"
                                                                                  name="iaisErrorMsg"
                                                                                  id="nationalityError"></span>
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
                                                                               class="control-label control-set-font control-font-label">Tel
                                                                            No</label>
                                                                        <span class="mandatory">*</span>
                                                                    </div>
                                                                    <div class="col-sm-5 col-md-7">
                                                                        <div class="">
                                                                            <iais:input maxLength="20" type="text"
                                                                                        id="telNo"
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
                                                        <td class="first last" style="width: 100%;">
                                                            <div class="control control-caption-horizontal">
                                                                <div class="form-group form-horizontal formgap">
                                                                    <div class="col-sm-4 control-label formtext">
                                                                        <label class="control-label control-set-font control-font-label">Job
                                                                            Designation</label>
                                                                        <span class="mandatory">*</span>
                                                                    </div>
                                                                    <div class="col-sm-5 col-md-7">
                                                                        <div class="">
                                                                            <iais:input maxLength="66" type="text"
                                                                                        id="jobDesignation"
                                                                                        name=""/>
                                                                            <span class="error-msg"
                                                                                  name="iaisErrorMsg"
                                                                                  id="jobDesignationError"></span>
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
                                                                        <label class="control-label control-set-font control-font-label">Lead
                                                                            Certifier</label>
                                                                        <span class="mandatory">*</span>
                                                                    </div>
                                                                    <div class="col-sm-5 col-md-7">
                                                                        <div class="">
                                                                            <select id="leadCertifier">
                                                                                <option>Please Select</option>
                                                                                <option>Yes</option>
                                                                                <option>No</option>
                                                                            </select>
                                                                            <span class="error-msg"
                                                                                  name="iaisErrorMsg"
                                                                                  id="leadCertifierError"></span>
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
                                                                        <label class="control-label control-set-font control-font-label">Area
                                                                            of Expertise (Position)</label>
                                                                        <span class="mandatory">*</span>
                                                                        <span class="mandatory otherQualificationSpan"></span>
                                                                    </div>
                                                                    <div class="col-sm-5 col-md-7">
                                                                        <div class="">
                                                                            <select id="areaId">
                                                                                <option>Please Select</option>
                                                                                <option>Biosafety Certifier</option>
                                                                                <option>Engineering Certifier</option>
                                                                                <option id="s1" style="display: none">Assistant Biosafety Certifier</option>
                                                                                <option id="s2">Assistant Engineering Certifier</option>
                                                                            </select>
                                                                            <span class="error-msg" name="iaisErrorMsg"
                                                                                  id="areaIdError"></span>
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
                                                                        <label class="control-label control-set-font control-font-label">Experience
                                                                            in certification of a BSL-3 Facility</label>
                                                                        <span class="mandatory">*</span>
                                                                        <span class="upload_controls"></span>
                                                                    </div>
                                                                    <div class="col-sm-5 col-md-7">
                                                                        <div class="">
                                                                            <textarea id="BSL1"
                                                                                      name="BSL1"
                                                                                      cols="62"
                                                                                      rows="4"
                                                                                      maxlength="1000"></textarea>
                                                                            <span class="error-msg"
                                                                                  name="iaisErrorMsg"
                                                                                  id="BSL1Error"></span>
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
                                                                        <label class="control-label control-set-font control-font-label">Experience
                                                                            in commissioning of a BSL-3/4
                                                                            facility</label>
                                                                        <span class="mandatory">*</span>
                                                                        <span class="upload_controls"></span>
                                                                    </div>
                                                                    <div class="col-sm-4 col-md-7">
                                                                        <div class="">
                                                                            <textarea id="BSL2"
                                                                                      name="BSL2"
                                                                                      cols="62"
                                                                                      rows="4"
                                                                                      maxlength="1000"></textarea>
                                                                            <span class="error-msg"
                                                                                  name="iaisErrorMsg"
                                                                                  id="BSL2Error"></span>
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
                                                                        <label class="control-label control-set-font control-font-label">Experience
                                                                            in other BSL-3/4 related activities</label>
                                                                        <span class="mandatory">*</span>
                                                                        <span class="upload_controls"></span>
                                                                    </div>
                                                                    <div class="col-sm-4 col-md-7">
                                                                        <div class="">
                                                                            <textarea id="activities"
                                                                                      name="activities"
                                                                                      cols="62"
                                                                                      rows="4"
                                                                                      maxlength="1000"></textarea>
                                                                            <span class="error-msg"
                                                                                  name="iaisErrorMsg"
                                                                                  id="activitiesError"></span>
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
                                                                        <label class="control-label control-set-font control-font-label">Education
                                                                            Background</label>
                                                                        <span class="mandatory">*</span>
                                                                        <span class="upload_controls"></span>
                                                                    </div>
                                                                    <div class="col-sm-4 col-md-7">
                                                                        <div class="">
                                                                            <textarea id="educationBackground"
                                                                                      name="educationBackground"
                                                                                      cols="62"
                                                                                      rows="4"
                                                                                      maxlength="500"></textarea>
                                                                            <span class="error-msg"
                                                                                  name="iaisErrorMsg"
                                                                                  id="educationBackgroundError"></span>
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
                                                                        <label class="control-label control-set-font control-font-label">Position/Professional
                                                                            activities (facility related)</label>
                                                                        <span class="mandatory">*</span>
                                                                        <span class="upload_controls"></span>
                                                                    </div>
                                                                    <div class="col-sm-4 col-md-7">
                                                                        <div class="">
                                                                            <textarea id="positionActivities"
                                                                                      name="positionActivities"
                                                                                      cols="62"
                                                                                      rows="4"
                                                                                      maxlength="500"></textarea>
                                                                            <span class="error-msg"
                                                                                  name="iaisErrorMsg"
                                                                                  id="positionActivitiesError"></span>
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
                                                                        <label class="control-label control-set-font control-font-label">Relevant
                                                                            professional registration &
                                                                            certificates</label>
                                                                        <span class="mandatory">*</span>
                                                                        <span class="upload_controls"></span>
                                                                    </div>
                                                                    <div class="col-sm-4 col-md-7">
                                                                        <div class="">
                                                                            <textarea id="certificates"
                                                                                      name="certificates"
                                                                                      cols="62"
                                                                                      rows="4"
                                                                                      maxlength="500"></textarea>
                                                                            <span class="error-msg"
                                                                                  name="iaisErrorMsg"
                                                                                  id="certificatesError"></span>
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
                                                                        <label class="control-label control-set-font control-font-label">Facility
                                                                            related publications/researches</label>
                                                                        <span class="mandatory">*</span>
                                                                        <span class="upload_controls"></span>
                                                                    </div>
                                                                    <div class="col-sm-4 col-md-7">
                                                                        <div class="">
                                                                            <textarea id="researches"
                                                                                      name="researches"
                                                                                      cols="62"
                                                                                      rows="4"
                                                                                      maxlength="500"></textarea>
                                                                            <span class="error-msg"
                                                                                  name="iaisErrorMsg"
                                                                                  id="researchesError"></span>
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
    function validateCertifyingTeam(num) {
        //date
        var birthDay = $("#birthDay").val();
        //textarea/input
        var nameId = $("#nameId").val();
        var idNo = $("#idNo").val();
        var telNo = $("#telNo").val();
        var jobDesignation = $("#jobDesignation").val();
        var BSL1 = $("#BSL1").val();
        var BSL2 = $("#BSL2").val();
        var activities = $("#activities").val();
        var educationBackground = $("#educationBackground").val();
        var positionActivities = $("#positionActivities").val();
        var certificates = $("#certificates").val();
        var researches = $("#researches").val();
        //select
        var idType = $("#idType").val();
        var sex = $("#sex").val();
        var nationality = $("#nationality").val();
        var areaId = $("#areaId").val();
        var leadCertifier = $("#leadCertifier").val();

        if (birthDay == "dd/mm/yyyy") {
            $("#birthDayError").html("This field is Mandatory");
            num++;
        } else {
            $("#birthDayError").html("");
        }

        if (nameId == null || nameId == "") {
            $("#nameIdError").html("This field is Mandatory");
            num++;
        } else {
            $("#nameIdError").html("");
        }

        if (idNo == null || idNo == "") {
            $("#idNoError").html("This field is Mandatory");
            num++;
        } else {
            $("#idNoError").html("");
        }

        if (telNo == null || telNo == "") {
            $("#telNoError").html("This field is Mandatory");
            num++;
        } else {
            $("#telNoError").html("");
        }

        if (jobDesignation == null || jobDesignation == "") {
            $("#jobDesignationError").html("This field is Mandatory");
            num++;
        } else {
            $("#jobDesignationError").html("");
        }

        if (BSL1 == null || BSL1 == "") {
            $("#BSL1Error").html("This field is Mandatory");
            num++;
        } else {
            $("#BSL1Error").html("");
        }

        if (BSL2 == null || BSL2 == "") {
            $("#BSL2Error").html("This field is Mandatory");
            num++;
        } else {
            $("#BSL2Error").html("");
        }

        if (activities == null || activities == "") {
            $("#activitiesError").html("This field is Mandatory");
            num++;
        } else {
            $("#activitiesError").html("");
        }

        if (educationBackground == null || educationBackground == "") {
            $("#educationBackgroundError").html("This field is Mandatory");
            num++;
        } else {
            $("#educationBackgroundError").html("");
        }

        if (positionActivities == null || positionActivities == "") {
            $("#positionActivitiesError").html("This field is Mandatory");
            num++;
        } else {
            $("#positionActivitiesError").html("");
        }

        if (certificates == null || certificates == "") {
            $("#certificatesError").html("This field is Mandatory");
            num++;
        } else {
            $("#certificatesError").html("");
        }

        if (researches == null || researches == "") {
            $("#researchesError").html("This field is Mandatory");
            num++;
        } else {
            $("#researchesError").html("");
        }

        //
        if (idType == "Please Select") {
            $("#idTypeError").html("Please select a valid option");
            num++;
        } else {
            $("#idTypeError").html("");
        }

        if (sex == "Please Select") {
            $("#sexError").html("Please select a valid option");
            num++;
        } else {
            $("#sexError").html("");
        }

        if (nationality == "Please Select") {
            $("#nationalityError").html("Please select a valid option");
            num++;
        } else {
            $("#nationalityError").html("");
        }

        if (leadCertifier == "Please Select") {
            $("#leadCertifierError").html("Please select a valid option");
            num++;
        } else {
            $("#leadCertifierError").html("");
        }

        if (areaId == "Please Select") {
            $("#areaIdError").html("Please select a valid option");
            num++;
        } else {
            $("#areaIdError").html("");
        }
        return num;
    }

    $("#leadCertifier").change(function () {
        var lead = $("#leadCertifier").val();
        if (lead == "Yes") {
            console.log("222:" + lead);
            $("#areaId").html("<option>Please Select</option> <option>Biosafety Certifier</option> <option>Engineering Certifier</option>");
            // $("#s1").css('display','none');
            // $("#s2").css('display','none');
        }
        if (lead == "No" || lead == "Please Select") {
            console.log("333:" + lead);
            $("#areaId").html("<option>Please Select</option> <option>Biosafety Certifier</option> <option>Engineering Certifier</option> <option>Assistant Biosafety Certifier</option> <option>Assistant Engineering Certifier</option>");
            // $("#s1").css('display','block');
            // $("#s2").css('display','block');
        }
    });
</script>
