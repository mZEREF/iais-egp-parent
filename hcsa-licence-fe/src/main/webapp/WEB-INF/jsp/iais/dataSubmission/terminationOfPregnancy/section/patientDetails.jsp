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
                   <c:set var="patientInformationDto" value="${terminationOfPregnancyDto.patientInformationDto}"/>
                   <c:set var="terminationOfPregnancyDto" value="${topSuperDataSubmissionDto.terminationOfPregnancyDto}"/>
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
                            <iais:datePicker name="commResidenceInSgDate" id="commResidenceInSgDates" value="${patientInformationDto.commResidenceInSgDate}"/>
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
                                         codeCategory="TOP_RESIDENCE_STATUS" id="residenceStatu" value="${patientInformationDto.residenceStatus}"/>
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
                                <iais:input maxLength="20" type="text" id="otherEthnicGroup" name="otherEthnicGroup"
                                            value="${patientInformationDto.otherEthnicGroup}"/>
                                <span class="error-msg" name="iaisErrorMsg" id="error_otherEthnicGroup"></span>
                            </iais:value>
                        </iais:row>
                    </div>
                    <iais:row>
                        <iais:field width="5" value="Marital Status" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:select cssClass="maritalStatus" name="maritalStatus" id="maritalStatus" firstOption="Please Select" codeCategory="TOP_MARITAL_STATUS" value="${patientInformationDto.maritalStatus}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Education Level" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:select cssClass="educationLevel" name="educationLevel" id="educationLevel" firstOption="Please Select"
                                         codeCategory="TOP_EDUCATION_LEVEL" value="${patientInformationDto.educationLevel}"/>
                        </iais:value>
                    </iais:row>
                   <iais:row>
                       <iais:field width="5" value="Employment Status" mandatory="true"/>
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
                   <div id="otherOccupations"
                        <c:if test="${patientInformationDto.occupation!='TOPOCC011'}">style="display: none"</c:if>>
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
                        <iais:field width="5" value="No. of Living Children" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:input id="childrenNum" maxLength="2" type="text" name="livingChildrenNo" value="${patientInformationDto.livingChildrenNo}"/>
                            <span class="error-msg" name="iaisErrorMsg" id="error_livingChildrenNo"></span>
                            <span id="childrenNumMsg" name="iaisErrorMsg" class="error-msg"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row id="numMax">
                        <div id="gender" <c:if test="${!(patientInformationDto.livingChildrenNo).matches('[0-9]+') || patientInformationDto.livingChildrenNo<=0 || patientInformationDto.livingChildrenNo ==null }">style="display: none"</c:if>>
                                <iais:field width="5" value="Gender of Living Children (By Order)" mandatory="true"/>
                        </div>
                        <iais:value width="7" cssClass="col-md-7">
                            <div id="genders">
                               <c:forEach items="${patientInformationDto.livingChildrenGenders}" var="livingChildrenGenders" begin="0"
                                           varStatus="idxStatus">
                                    <iais:row>
                                        <iais:value cssClass="col-sm-7 col-md-5 col-xs-7 col-md-12">
                                            <div class="form-group" id="genders" style="padding-left: 15px;padding-right: 15px;">
                                                    <iais:select name="livingChildrenGenders" firstOption="Please Select" id="livingChildrenGenders${idxStatus.index}" codeCategory="TOP_GENDER_OF_PREGNANT_CHILDREN"
                                                                 value="${livingChildrenGenders}" cssClass="livingChildrenGenders${idxStatus.index}"/>
                                                    <span id="error_livingChildrenGenders${idxStatus.index}" name="iaisErrorMsg" class="error-msg"></span>
                                            </div>
                                        </iais:value>
                                    </iais:row>
                                </c:forEach>
                            </div>
                        </iais:value>
                    </iais:row>
                </div>
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
                $('#occupations').show();
            } else {
                $('#occupations').hide();
                $('#otherOccupations').hide();
                fillValue($('#occupations'),null);
                $('#otherOccupations').val(null);
            }
        });
    });
    $(document).ready(function () {
        $('#occupation').change(function () {

            var occupation = $('#occupation option:selected').val();

            if (occupation == "TOPOCC011") {
                $('#otherOccupations').show();
            } else {
                $('#otherOccupations').hide();
            }
        });

        $('#childrenNum').keyup(function () {
            var childrenNum = $('#childrenNum').val();
            if (childrenNum!=null && childrenNum>0 && childrenNum<=10) {
                $('#gender').show();
            } else {
                $('#gender').hide();
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
            refreshId($('#genders').find('select'));
        });
    });
    $(document).ready(function () {
        $('#nationality').change(function () {

            var nationality = $('#nationality').val();
            if (nationality != "NAT0001" && nationality !=null && nationality !='') {
                $('#commResidenceInSgDate').text('*');
            } else {
                $('#commResidenceInSgDate').text('');
            }
        });
        $('#nationality').change(function () {

            var nationality = $('#nationality').val();
            if (nationality != "NAT0001" && nationality !=null && nationality !='') {
                $('#residenceStatus').text('*');
            } else {
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
    function refreshId(targetSelector) {
        $(targetSelector).each(function (k,v) {
            var $input = $(v);
            var orgId = $input.attr('id');
            var result = /([a-zA-Z_]*)/g.exec(orgId);
            var id = !isEmpty(result) && result.length > 0 ? result[0] : orgId;
            $input.prop('id', id + k);
        });
    }
    function validatePatientName(data){
        console.log("validatePatientName!")
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
            /*|| isEmpty(data.selection.occupation)*/
            /*|| isEmpty(data.selection.otherOccupation)*/ || !isEmpty(data.errorMsg)) {
            if (!isEmpty(data.errorMsg)) {
                doValidationParse(data.errorMsg);
            } else {
                console.log("shibai")
                $('#noFoundDiv').modal('show');
            }
            return;
        }
        clearSelection();
        $('#patientName').val(data.selection.patientName);

        $('#birthData').val(data.selection.birthData);

        fillValue($('#nationality'),data.selection.nationality);
        if(!isEmpty(data.selection.commResidenceInSgDate)){
            $('#commResidenceInSgDates').val(data.selection.commResidenceInSgDate);
        }
        if(!isEmpty(data.selection.residenceStatus)){
            console.log("residenceStatus!")
            fillValue($('#residenceStatu'),data.selection.residenceStatus);
        }
        fillValue($('#ethnicGroups'),data.selection.ethnicGroup);
        if(!isEmpty(data.selection.otherEthnicGroup)){
            $('#otherEthnicGroups').show();
            fillValue($('#otherEthnicGroup'),data.selection.otherEthnicGroup);
        }
        fillValue($('#maritalStatus'),data.selection.maritalStatus);
        fillValue($('#educationLevel'),data.selection.educationLevel);
        $('#childrenNum').val(data.selection.livingChildrenNo);
        fillValue($('#activityStatus'),data.selection.activityStatus);
        if(!isEmpty(data.selection.occupation)){
            $('#occupations').show();
            fillValue($('#occupation'),data.selection.occupation);
        }
        if(!isEmpty(data.selection.otherOccupation)){
            $('#otherOccupations').show();
            fillValue($('#otherOccupation'),data.selection.otherOccupation);
        }
        if(data.selection.livingChildrenNo>0){
            console.log("qingchu")
            $('#childrenNum').trigger('keyup');
            $('#genders').show();
            var livingChildrenGenders=data.selection.livingChildrenGenders;
            for(var i=0;i<livingChildrenGenders.length;i++){
                console.log(i)
                fillValue($('#livingChildrenGenders'+i),livingChildrenGenders[i]);
            }
        }else {
            $('#gender').hide();
            $('#genders').hide();
        }
    }
    function clearSelection(){
        console.log("clearSelection!")
        clearErrorMsg();
    }
    //Judge whether the input number is greater than 10
    $(function(){
        var preValue = '';
        $("body").on("keyup", "input[name=livingChildrenNo]", function(){
            var value = $(this).val();
            var reg=/^[1-9][0-9]+$/gi;
            var data=$('#childrenNum').val();
            if(!!value && !$(this).val().match(/^(?:10|[0-9])$/) && reg.test(data)) {
                /*$(this).val(preValue);*/
                /*$('#childrenNum').val(null);*/
                clearErrorMsg();
                $("#childrenNumMsg").text('cannot enter more than 10.');
                /*$('#childrenNum').trigger('keyup');*/
                $('#numMax').hide();
            }
            preValue = $(this).val();
            return true;
        });
    });
</script>