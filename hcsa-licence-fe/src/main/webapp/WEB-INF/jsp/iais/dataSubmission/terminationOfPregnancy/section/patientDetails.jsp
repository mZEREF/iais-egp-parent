<c:set var="terminationOfPregnancyDto" value="${topSuperDataSubmissionDto.terminationOfPregnancyDto}" />
<c:set var="patientInformationDto" value="${terminationOfPregnancyDto.patientInformationDto}" />
<div class="form-horizontal patientPatails">
    <iais:row>
        <iais:value width="6" cssClass="col-md-6">
            <%--<strong class="app-font-size-22 premHeader">title</strong>--%>
        </iais:value>
        <iais:value width="6" cssClass="col-md-6 text-right editDiv">
            <c:if test="${canEdit}">
            <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
            <a id="edit" class="text-right app-font-size-16">
                <em class="fa fa-pencil-square-o">&nbsp;</em> Edit
            </a>
            </c:if>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Name Of Patient" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="66" type="text" name="patientName" value="${patientInformationDto.patientName}" />
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="ID No." mandatory="true"/>
        <iais:value width="3" cssClass="col-md-3">
            <iais:select name="idType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                         value="${patientInformationDto.idType}" cssClass="idType"/>
        </iais:value>
        <iais:value width="4" cssClass="col-md-4">
            <iais:input maxLength="15" type="text" name="idNumber" value="${patientInformationDto.idNumber}" />
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Date of Birth" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker name="birthData" value="${patientInformationDto.birthData}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Nationality" mandatory="false"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="nationality" firstOption="Please Select" codeCategory="CATE_ID_NATIONALITY"
                         value="${patientInformationDto.nationality}" cssClass="nationality"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Date Commenced Residence In Singapore"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker name="commResidenceInSgDate" value="${patientInformationDto.commResidenceInSgDate}"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_commResidenceInSgDate"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Residence Status"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select cssClass="residenceStatus"  name="residenceStatus" firstOption="Please Select" codeCategory="TOP_RESIDENCE_STATUS" value="${patientInformationDto.residenceStatus}"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_residenceStatus"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Ethnic Group" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select cssClass="ethnicGroup"  name="ethnicGroup" firstOption="Please Select" codeCategory="CATE_ID_ETHNIC_GROUP" value="${patientInformationDto.ethnicGroup}"/>
        </iais:value>
    </iais:row>
    <div id="otherEthnicGroup" <c:if test="${patientInformationDto.ethnicGroup!='ETHG005'}">style="display: none"</c:if>>
        <iais:row>
            <iais:field width="5" value="Other Ethnic Group" mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="200" type="text" name="otherEthnicGroup" value="${patientInformationDto.otherEthnicGroup}"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_otherEthnicGroup"></span>
            </iais:value>
        </iais:row>
    </div>
    <iais:row>
        <iais:field width="5" value="Marital Status" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select cssClass="maritalStatus"  name="maritalStatus" firstOption="Please Select" codeCategory="TOP_MARITAL_STATUS" value="${patientInformationDto.maritalStatus}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Education Level" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select cssClass="educationLevel"  name="educationLevel" firstOption="Please Select" codeCategory="VSS_EDUCATION_LEVEL" value="${patientInformationDto.educationLevel}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="No. of Living Children" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input id="childrenNum" maxLength="2" type="text" name="livingChildrenNo" value="${patientInformationDto.livingChildrenNo}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Activity Status" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select cssClass="activityStatus"  name="activityStatus" firstOption="Please Select" codeCategory="TOP_ACTIVITY_STATUS" value="${patientInformationDto.activityStatus}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Occupation" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select cssClass="occupation"  name="occupation" firstOption="Please Select" codeCategory="TOP_OCCUPATION" value="${patientInformationDto.occupation}"/>
        </iais:value>
    </iais:row>
    <div id="otherOccupation" <c:if test="${patientInformationDto.occupation!='TOPOCC014'}">style="display: none"</c:if>>
        <iais:row>
            <iais:field width="5" value="Other Occupation" mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="20" type="text" name="otherOccupation" value="${patientInformationDto.otherOccupation}"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_otherOccupation"></span>
            </iais:value>
        </iais:row>
    </div>
    <iais:row>
        <iais:field width="5" value="gender"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="2" type="text" name="gender" value="${patientInformationDto.gender}"/>
        </iais:value>
    </iais:row>

        <iais:row>
            <iais:field width="5" value="Gender of Living Children (by Order)"/>
            <iais:value width="7" cssClass="col-md-7">
                <%--<iais:select cssClass="livingChildrenGenders"  name="livingChildrenGenders" firstOption="Please Select" codeCategory=""
                        value="${patientInformationDto.livingChildrenGenders}"/>--%>
                <div id="genders">
                    <iais:input maxLength="2" type="text" name="gender" value="${patientInformationDto.livingChildrenGenders}"/>
                </div>
            </iais:value>
        </iais:row>
</div>

<script>
    $(document).ready(function() {
        $('#ethnicGroup').change(function () {

            var ethnicGroup= $('#ethnicGroup option:selected').val();

            if(ethnicGroup == "ETHG005"){
                $('#otherEthnicGroup').attr("style","display: block");
            }else {
                $('#otherEthnicGroup').attr("style","display: none");
            }
        });
    });
    $(document).ready(function() {
        $('#occupation').change(function () {

            var occupation= $('#occupation option:selected').val();

            if(occupation == "TOPOCC014"){
                $('#otherOccupation').attr("style","display: block");
            }else {
                $('#otherOccupation').attr("style","display: none");
            }
        });
    });
    $(function(){
        $("#childrenNum").keyup(function() {
            var value = $(this).val();
            $("#genders").empty();
            for (var i = 0; i < value; i++) {
                var input = "<input type='text'" + i + " />";
                $("#genders").append(input);
            }
            $("#genders").mouseenter(function () {
                $(this).find("input")
            });
        });
    });
</script>