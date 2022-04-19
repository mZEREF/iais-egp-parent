<c:set var="patientInformationDto" value="${terminationOfPregnancyDto.patientInformationDto}"/>
<c:set var="terminationOfPregnancyDto" value="${topSuperDataSubmissionDto.terminationOfPregnancyDto}"/>
            <div class="panel-main-content form-horizontal">
               <%-- <iais:row>
                    <iais:value width="6" cssClass="col-md-6">
                        &lt;%&ndash;<strong class="app-font-size-22 premHeader">title</strong>&ndash;%&gt;
                    </iais:value>
                    <iais:value width="6" cssClass="col-md-6 text-right editDiv">
                        <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
                        <p class="print" style="font-size: 16px;">
                            <a onclick="printData()" href="javascript:void(0);"> <em class="fa fa-print"></em>Print</a>
                        </p>
                    </iais:value>
                </iais:row>--%>
                    <iais:row>
                        <c:set var="toolMsg"><iais:message key="DS_MSG014" paramKeys="1" paramValues="patient"/></c:set>
                        <iais:field width="5" value="ID No." mandatory="true" info="${toolMsg}"/>
                        <iais:value width="3" cssClass="col-md-3">
                            <iais:select name="idType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                                         value="${patientInformationDto.idType}" onchange="clearSelection()" cssClass="idType"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" style="width: 180px;">
                            <iais:input maxLength="20" type="text" name="idNumber" value="${patientInformationDto.idNumber}" onchange="clearSelection()"/>
                        </iais:value>
                        <iais:value width="3" cssClass="col-md-3 patientData" display="true" id="retrieveDataDiv">
                            <a class="retrieveIdentification" onclick="retrieveValidateTop()">
                                Validate Patient
                            </a>
                            <span class="error-msg col-md-12" name="iaisErrorMsg" id="error_retrieveData"></span>
                        </iais:value>
                    </iais:row>
                   <iais:row>
                       <iais:field width="5" value="Name of Patient" mandatory="true"/>
                       <iais:value width="7" cssClass="col-md-7">
                           <iais:input maxLength="66" type="text" name="patientName" id="patientName" value="${patientInformationDto.patientName}"/>
                       </iais:value>
                   </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Date of Birth" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:datePicker name="birthData" id="birthData" value="${patientInformationDto.birthData}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Nationality" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:select name="nationality" firstOption="Please Select" id="nationality" codeCategory="CATE_ID_NATIONALITY"
                                         value="${patientInformationDto.nationality}"  cssClass="nationality"/>
                            <span class="error-msg" name="iaisErrorMsg" id="error_nationality"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <label class="col-xs-5 col-md-4 control-label">Date Commenced Residence In Singapore
                            <span id="commResidenceInSgDate" class="mandatory">
                                <c:if test="${patientInformationDto.nationality!=null && patientInformationDto.nationality !='NAT0001'}">*</c:if>
                            </span>
                        </label>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:datePicker name="commResidenceInSgDate" id="commResidenceInSgDate" value="${patientInformationDto.commResidenceInSgDate}"/>
                            <span class="error-msg" name="iaisErrorMsg" id="error_commResidenceInSgDate"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <label class="col-xs-5 col-md-4 control-label">Residence Status
                            <span id="residenceStatus" class="mandatory">
                                <c:if test="${patientInformationDto.nationality!=null && patientInformationDto.nationality !='NAT0001'}">*</c:if>
                            </span>
                        </label>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:select cssClass="residenceStatus" name="residenceStatus" firstOption="Please Select"
                                         codeCategory="TOP_RESIDENCE_STATUS" id="residenceStatus" value="${patientInformationDto.residenceStatus}"/>
                            <span class="error-msg" name="iaisErrorMsg" id="error_residenceStatus"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Ethnic Group" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:select cssClass="ethnicGroup" name="ethnicGroup" id="ethnicGroups" firstOption="Please Select"
                                         codeCategory="VSS_ETHNIC_GROUP" value="${patientInformationDto.ethnicGroup}"/>
                        </iais:value>
                    </iais:row>
                    <div id="otherEthnicGroups" <c:if test="${patientInformationDto.ethnicGroup!='ECGP004'}">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="5" value="Ethnic Group (Others)" mandatory="true"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <iais:input maxLength="200" type="text" id="otherEthnicGroup" name="otherEthnicGroup"
                                            value="${patientInformationDto.otherEthnicGroup}"/>
                                <span class="error-msg" name="iaisErrorMsg" id="error_otherEthnicGroup"></span>
                            </iais:value>
                        </iais:row>
                    </div>
                    <iais:row>
                        <iais:field width="5" value="Marital Status" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:select cssClass="maritalStatus" name="maritalStatus" id="maritalStatus" firstOption="Please Select"
                                         codeCategory="TOP_MARITAL_STATUS" value="${patientInformationDto.maritalStatus}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Education Level" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:select cssClass="educationLevel" name="educationLevel" id="educationLevel" firstOption="Please Select"
                                         codeCategory="VSS_EDUCATION_LEVEL" value="${patientInformationDto.educationLevel}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="No. of Living Children" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:input id="childrenNum" maxLength="2" type="text" name="livingChildrenNo"
                                        value="${patientInformationDto.livingChildrenNo}"/>
                            <span class="error-msg" name="iaisErrorMsg" id="error_livingChildrenNo"></span>
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field width="5" value="Activity Status" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:select cssClass="activityStatus" name="activityStatus" id="activityStatus" firstOption="Please Select"
                                         codeCategory="TOP_ACTIVITY_STATUS" value="${patientInformationDto.activityStatus}"/>
                        </iais:value>
                    </iais:row>
                    <div id="occupations"
                         <c:if test="${patientInformationDto.activityStatus!='TOPAS001'}">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="5" value="Occupation" mandatory="true"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <iais:select cssClass="occupation" name="occupation" id="occupation" firstOption="Please Select"
                                             codeCategory="TOP_OCCUPATION" value="${patientInformationDto.occupation}"/>
                                <span class="error-msg" name="iaisErrorMsg" id="error_occupation"></span>
                            </iais:value>
                        </iais:row>
                    </div>
                    <div id="otherOccupation"
                         <c:if test="${patientInformationDto.occupation!='TOPOCC014'}">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="5" value="Other Occupation" mandatory="true"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <iais:input maxLength="20" type="text" id="otherOccupation" name="otherOccupation"
                                            value="${patientInformationDto.otherOccupation}"/>
                                <span class="error-msg" name="iaisErrorMsg" id="error_otherOccupation"></span>
                            </iais:value>
                        </iais:row>
                    </div>
                    <iais:row>
                        <iais:field width="5" value="Gender of Living Children (By Order)"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <div id="genders">
                               <c:forEach items="${patientInformationDto.livingChildrenGenders}" var="livingChildrenGenders" begin="0"
                                           varStatus="idxStatus">
                                    <iais:row>
                                        <iais:value cssClass="col-sm-7 col-md-5 col-xs-7 col-md-12">
                                            <div class="form-group" id="genders" style="padding-left: 15px;padding-right: 15px;">
                                                    <iais:select name="livingChildrenGenders${idxStatus.index}" firstOption="Please Select" id="livingChildrenGenders" codeCategory="TOP_GENDER_OF_PREGNANT_CHILDREN"
                                                                 value="${livingChildrenGenders}" cssClass="livingChildrenGenders"/>
                                                    <span id="error_livingChildrenGenders${idxStatus.index}" name="iaisErrorMsg" class="error-msg"></span>
                                            </div>
                                        </iais:value>
                                    </iais:row>
                                </c:forEach>
                            </div>
                        </iais:value>
                    </iais:row>
                </div>
<c:if test="${hasDraft}">
    <iais:confirm
            msg="DS_MSG010"
            callBack="submit('resume');" popupOrder="_draftModal" yesBtnDesc="Resume from draft"
            cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" needFungDuoJi="false"
            cancelBtnDesc="Continue" cancelFunc="submit('delete')"/>
</c:if>
<input type="hidden" id="genderCount" value="${genderCount}"/>
<script>
    $(document).ready(function () {
        $('#ethnicGroups').change(function () {

            var ethnicGroup = $('#ethnicGroups').val();

            if (ethnicGroup == "ECGP004") {
                $('#otherEthnicGroups').show();
            } else {
                $('#otherEthnicGroups').hide();
            }
        });
    });
    $(document).ready(function () {
        $('#activityStatus').change(function () {

            var activityStatus = $('#activityStatus option:selected').val();

            if (activityStatus == "TOPAS001") {
                $('#occupations').attr("style", "display: block");
            } else {
                $('#occupations').attr("style", "display: none");
            }
        });
    });
    $(document).ready(function () {
        $('#occupation').change(function () {

            var occupation = $('#occupation option:selected').val();

            if (occupation == "TOPOCC014") {
                $('#otherOccupation').attr("style", "display: block");
            } else {
                $('#otherOccupation').attr("style", "display: none");
            }
        });
    });
    $(function () {
        $("#childrenNum").keyup(function () {
            var value = $(this).val();
            $("#genders").empty();
            for (var i = 0; i < value; i++) {
                var input = "<div class=\"col-sm-7 col-md-5 col-xs-7 col-md-12\" style=\"padding-left: 0px;padding-right: 0px;\">\n" +
                    "<select name=\"livingChildrenGenders\" id=\"livingChildrenGenders\" style=\"display: none;\">\n" +
                    "     <option value=\"\">Please Select</option>\n" +
                    "     <option value=\"TOPGAW001\">Male</option>\n" +
                    "     <option value=\"TOPGAW002\">Female</option>\n" +
                    "     </select>\n" +
                    "   <div class=\"nice-select livingChildrenGenders\" name=\"living\" tabindex=\"0"+ i +"\">\n" +
                    "       <span class=\"current\">Please Select</span>\n" +
                    "           <ul class=\"list\">\n" +
                    "               <li data-value=\"\" class=\"option selected\">Please Select</li>\n" +
                    "               <li data-value=\"TOPGAW001\" class=\"option\">Male</li>\n" +
                    "               <li data-value=\"TOPGAW002\" class=\"option\">Female</li>\n" +
                    "           </ul>" +
                    "       <span id=\"error_livingChildrenGenders\" name=\"iaisErrorMsg\" class=\"error-msg\"></span>\n" +
                    "   </div>\n"+
                    "</div>\n";
                $("#genders").append(input);
            }
        });
    });
    $(document).ready(function () {
        $('#nationality').change(function () {

            var nationality = $('#nationality').val();
            if (nationality != "NAT0001") {
                $('#commResidenceInSgDate').text('*');
            } else if(nationality==null || nationality == "NAT0001"){
                $('#commResidenceInSgDate').text('');
            }
        });
        $('#nationality').change(function () {

            var nationality = $('#nationality').val();
            if (nationality != "NAT0001") {
                $('#residenceStatus').text('*');
            } else if(nationality==null || nationality == "NAT0001"){
                $('#residenceStatus').text('');
            }
        });
    });

    $(document).ready(function() {
        console.log("showValidatePT!")
        if ("1" == $('#showValidatePT').val()) {
            console.log("PT!")
            $('#validatePT').modal('show');
        } else if ("1" == $('#showValidatePT').val()) {
            console.log("no!")
            $('#noFoundDiv').modal('show');
        }
    });

    function retrieveValidateTop() {
        showWaiting();
        var idType = $('#idType').val();
        var idNo = $('input[name="idNumber"]').val();
        var url = $('#_contextPath').val() + '/top/retrieve-identification';
        var options = {
            idType: idType,
            idNo: idNo,
            url: url
        }
        callCommonAjax(options, validatePatientName);
    }
    function validatePatientName(data){
        clearErrorMsg();
        clearSelection();
        if (isEmpty(data) || isEmpty(data.selection) || isEmpty(data.selection.patientName)
            || isEmpty(data.selection.birthData)
            || isEmpty(data.selection.nationality)
            /*|| isEmpty(data.selection.commResidenceInSgDate)
            || isEmpty(data.selection.residenceStatus)*/
            || isEmpty(data.selection.ethnicGroup)
            /*|| isEmpty(data.selection.otherEthnicGroup)*/
            || isEmpty(data.selection.maritalStatus)
            || isEmpty(data.selection.educationLevel)
            || isEmpty(data.selection.livingChildrenNo)
            || isEmpty(data.selection.activityStatus)
            || isEmpty(data.selection.occupation)
            /*|| isEmpty(data.selection.otherOccupation)*/ || !isEmpty(data.errorMsg)) {
            if (!isEmpty(data.errorMsg)) {
                doValidationParse(data.errorMsg);
            } else {
                $('#noFoundDiv').modal('show');
            }
            return;
        }
        clearSelection();
        $('#patientName').find('p').text(data.selection.patientName);
        $('[name="patientName"]').val('1');
        $('#patientName').val(data.selection.patientName);

        $('#birthData').find('p').text(data.selection.birthData);
        $('[name="birthData"]').val('1');
        $('#birthData').val(data.selection.birthData);

        $('#nationality').find('p').text(data.selection.nationality);
        $('[name="nationality"]').val('1');
        $('.current').val(data.selection.nationality);

        /*$('#commResidenceInSgDate').find('p').text(data.selection.commResidenceInSgDate);
        $('[name="commResidenceInSgDate"]').val('1');
        $('#commResidenceInSgDate').val(data.selection.commResidenceInSgDate);

        $('#residenceStatus').find('p').text(data.selection.residenceStatus);
        $('[name="residenceStatus"]').val('1');
        $('#residenceStatus').val(data.selection.residenceStatus);*/

        $('#ethnicGroup').find('p').text(data.selection.ethnicGroup);
        $('[name="ethnicGroup"]').val('1');
        $('#ethnicGroup').val(data.selection.ethnicGroup);

        /*$('#otherEthnicGroup').find('p').text(data.selection.otherEthnicGroup);
        $('[name="otherEthnicGroup"]').val('1');
        $('#otherEthnicGroup').val(data.selection.otherEthnicGroup);*/

        $('#maritalStatus').find('p').text(data.selection.maritalStatus);
        $('[name="maritalStatus"]').val('1');
        $('#maritalStatus').val(data.selection.maritalStatus);

        $('#educationLevel').find('p').text(data.selection.educationLevel);
        $('[name="educationLevel"]').val('1');
        $('#educationLevel').val(data.selection.educationLevel);

        $('#childrenNum').find('p').text(data.selection.livingChildrenNo);
        $('[name="livingChildrenNo"]').val('1');
        $('#childrenNum').val(data.selection.livingChildrenNo);

        $('#activityStatus').find('p').text(data.selection.activityStatus);
        $('[name="activityStatus"]').val('1');
        $('#activityStatus').val(data.selection.activityStatus);

        $('#occupation').find('p').text(data.selection.occupation);
        $('[name="occupation"]').val('1');
        $('#occupation').val(data.selection.occupation);

        /*$('#otherOccupation').find('p').text(data.selection.otherOccupation);
        $('[name="otherOccupation"]').val('1');
        $('#otherOccupation').val(data.selection.otherOccupation);*/
    }
    function clearSelection(){
        console.log("clearSelection!")
        clearErrorMsg();
        $('#name').find('p').text('');
        clearFields('.selectionHidden');
    }
</script>