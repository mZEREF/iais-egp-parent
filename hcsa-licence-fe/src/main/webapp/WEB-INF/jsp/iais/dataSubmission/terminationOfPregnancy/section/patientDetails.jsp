<c:set var="patientInformationDto" value="${topSuperDataSubmissionDto.patientInformationDto}"/>
<div class="panel panel-default">
    <div class="panel-heading" style="padding-left: 95px";>
        <h4 class="panel-title">
            <strong>
                Patient Information
            </strong>
        </h4>
    </div>
    <div class="panel-collapse collapse in">
        <div class="panel-body">
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
                <div class="cleanpage">
                    <iais:row>
                        <iais:field width="5" value="Name of Patient" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:input maxLength="66" type="text" name="patientName" value="${patientInformationDto.patientName}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <c:set var="toolMsg"><iais:message key="DS_MSG014" paramKeys="1" paramValues="patient"/></c:set>
                        <iais:field width="5" value="ID No." mandatory="true" info="${toolMsg}"/>
                        <iais:value width="3" cssClass="col-md-3">
                            <iais:select name="idType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                                         value="${patientInformationDto.idType}" cssClass="idType"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4">
                            <iais:input maxLength="20" type="text" name="idNumber" value="${patientInformationDto.idNumber}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Date of Birth" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:datePicker name="birthData" value="${patientInformationDto.birthData}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Nationality" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:select name="nationality" firstOption="Please Select" id="nationality" codeCategory="CATE_ID_NATIONALITY"
                                         value="${patientInformationDto.nationality}" cssClass="nationality"/>
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
                            <iais:datePicker name="commResidenceInSgDate" value="${patientInformationDto.commResidenceInSgDate}"/>
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
                                         codeCategory="TOP_RESIDENCE_STATUS" value="${patientInformationDto.residenceStatus}"/>
                            <span class="error-msg" name="iaisErrorMsg" id="error_residenceStatus"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Ethnic Group" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:select cssClass="ethnicGroup" name="ethnicGroup" id="ethnicGroup" firstOption="Please Select"
                                         codeCategory="VSS_ETHNIC_GROUP" value="${patientInformationDto.ethnicGroup}"/>
                        </iais:value>
                    </iais:row>
                    <div id="otherEthnicGroups"
                         <c:if test="${patientInformationDto.ethnicGroup!='ETHG005'}">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="5" value="Other Ethnic Group" mandatory="true"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <iais:input maxLength="200" type="text" name="otherEthnicGroup"
                                            value="${patientInformationDto.otherEthnicGroup}"/>
                                <span class="error-msg" name="iaisErrorMsg" id="error_otherEthnicGroup"></span>
                            </iais:value>
                        </iais:row>
                    </div>
                    <iais:row>
                        <iais:field width="5" value="Marital Status" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:select cssClass="maritalStatus" name="maritalStatus" firstOption="Please Select"
                                         codeCategory="TOP_MARITAL_STATUS" value="${patientInformationDto.maritalStatus}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Education Level" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:select cssClass="educationLevel" name="educationLevel" firstOption="Please Select"
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
                            <iais:select cssClass="activityStatus" name="activityStatus" firstOption="Please Select"
                                         codeCategory="TOP_ACTIVITY_STATUS" value="${patientInformationDto.activityStatus}"/>
                        </iais:value>
                    </iais:row>
                    <div id="occupations"
                         <c:if test="${patientInformationDto.activityStatus!='TOPAS001'}">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="5" value="Occupation" mandatory="true"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <iais:select cssClass="occupation" name="occupation" firstOption="Please Select"
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
                                <iais:input maxLength="20" type="text" name="otherOccupation"
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
                                                    <iais:select name="livingChildrenGenders${idxStatus.index}" firstOption="Please Select" codeCategory="TOP_GENDER_OF_PREGNANT_CHILDREN"
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
            </div>
            </div>
        </div>
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
        $('#ethnicGroup').change(function () {

            var ethnicGroup = $('#ethnicGroup').val();

            if (ethnicGroup == "ETHG005") {
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
</script>