<%@ page import="com.ecquaria.cloud.moh.iais.helper.MessageUtil" %>
<div class="panel panel-default usedDonorOocyteControlClass">
    <div class="panel-heading">
        <h4  class="panel-title" >
            <a href="#arDonorSampleDetails" data-toggle="collapse" >
                Donor Sample
            </a>
        </h4>
    </div>

    <div id="arDonorSampleDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <c:set var="donorSampleDto" value="${arSuperDataSubmissionDto.donorSampleDto}"/>
                <div class="panel-main-content form-horizontal">
                    <div class="donorSample">
                    <iais:row>
                        <iais:field width="5" value="Is Sample from a Directed Donation?" mandatory="true"/>
                        <iais:value width="3" cssClass="col-md-3">
                            <div class="form-check" onclick="showDonationYes()">
                                <input class="form-check-input"
                                       type="radio"
                                       name="directedDonation"
                                       value="1"
                                       id="directedDonationRadioYes"
                                       <c:if test="${donorSampleDto.directedDonation}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="directedDonationRadioYes"><span
                                        class="check-circle"></span>Yes</label>
                            </div>
                        </iais:value>
                        <iais:value width="3" cssClass="col-md-3">
                            <div class="form-check" onclick="showDonationNo()">
                                <input class="form-check-input" type="radio"
                                       name="directedDonation"
                                       value="0"
                                       id="directedDonationRadioNo"
                                       <c:if test="${!donorSampleDto.directedDonation}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="directedDonationRadioNo" ><span
                                        class="check-circle"></span>No</label>
                            </div>
                            <span id="error_directedDonation" name="iaisErrorMsg" class="error-msg"></span>
                        </iais:value>
                    </iais:row>
                    <div id="directedDonationYes" style="${!donorSampleDto.directedDonation ? 'display: none;' : ''}">
                        <iais:row id="idNoRow" >
                            <iais:field width="5" value="Donor's ID Type" mandatory="true"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <iais:select name="idType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE" value="${donorSampleDto.idType}"
                                             cssClass="idTypeSel"/>
                            </iais:value>
                        </iais:row>
                        <iais:row  >
                            <iais:field width="5" value="Donor's ID No." mandatory="true"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <iais:input maxLength="20" type="text" name="idNumber" value="${donorSampleDto.idNumber}" />
                            </iais:value>
                            <span id="error_directedDonationYesDonorLive" name="iaisErrorMsg" class="error-msg"></span>
                        </iais:row>
                        <iais:row  >
                            <iais:field width="5" value="Donor's Name" mandatory="true"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <iais:input maxLength="66" type="text" name="donorName" value="${donorSampleDto.donorName}" />
                            </iais:value>
                        </iais:row>

                    </div>
                    <div id="directedDonationNo" style="${donorSampleDto.directedDonation ? 'display: none;' : ''}">
                        <iais:row id="sampleType" >
                            <iais:field width="5" value="Sample Type" mandatory="true"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <iais:select name="sampleType" id ="sampleTypeId" firstOption="Please Select" codeCategory="AR_DONOR_SAMPLE_TYPE" value="${donorSampleDto.sampleType}"
                                             cssClass="sampleType"/>
                            </iais:value>
                        </iais:row>
                        <iais:row >
                            <iais:field width="5" value="Is Donor's Identity Known?" mandatory="true"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <iais:select name="donorIdentityKnown" id = "donorIdentityKnownId" firstOption="Please Select" codeCategory="CATE_DIK" value="${donorSampleDto.donorIdentityKnown}"
                                             cssClass="donorIdentityKnown"/>
                            </iais:value>
                        </iais:row>
                        <div id="donorSampleCodeRow"  style="${donorSampleDto.donorIdentityKnown =='DIK001' ? 'display: none;' : ''}">
                            <iais:row   >
                                <iais:field width="5" value="Donor Sample Code" mandatory="true"/>
                                <iais:value width="7" cssClass="col-md-7">
                                    <iais:input maxLength="20" type="text" name="donorSampleCode" id="donorSampleCode" value="${donorSampleDto.donorSampleCode}" />
                                </iais:value>
                                <span id="error_donorSampleCodeRowDonorLive" name="iaisErrorMsg" class="error-msg"></span>
                            </iais:row>

                        </div>

                        <div id ="donorDetail" style="${donorSampleDto.donorIdentityKnown == 'DIK001'? '' : 'display: none;'}">
                            <iais:row >
                                <iais:field width="5" value="Donor's ID Type" mandatory="true"/>
                                <iais:value width="7" cssClass="col-md-7">
                                    <iais:select name="knownIdType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE" value="${donorSampleDto.knownIdType}"
                                                 cssClass="idTypeSel"/>
                                </iais:value>
                            </iais:row>
                            <iais:row  >
                                <iais:field width="5" value="Donor's ID No." mandatory="true"/>
                                <iais:value width="7" cssClass="col-md-7">
                                    <iais:input maxLength="20" type="text" name="knownIdNumber" value="${donorSampleDto.knownIdNumber}" />
                                </iais:value>
                                <span id="error_donorDetailDonorLive" name="iaisErrorMsg" class="error-msg"></span>
                            </iais:row>
                            <iais:row  >
                                <iais:field width="5" value="Donor's Name" mandatory="true"/>
                                <iais:value width="7" cssClass="col-md-7">
                                    <iais:input maxLength="66" type="text" name="knownDonorName" value="${donorSampleDto.knownDonorName}" />
                                </iais:value>
                            </iais:row>
                        </div>
                        <iais:row>
                            <iais:field width="5" value="Name of Bank / AR Centre where Sample is from" mandatory="true"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <iais:select name="sampleFromHciCode" id ="sampleFromHciCode"  options="SampleFromHciCode"   value="${donorSampleDto.sampleFromHciCode}"
                                             cssClass="sampleFromOthers"/>
                            </iais:value>
                        </iais:row>
                        <iais:row id ="sampleFromOthers" style="display:none">
                            <label class="col-xs-5 col-md-4 control-label"></label>
                            <iais:value width="7" cssClass="col-md-7">
                                <iais:input maxLength="100" type="text" name="sampleFromOthers"  value="${donorSampleDto.sampleFromOthers}" />
                            </iais:value>
                        </iais:row>
                    </div>
                    </div>
                    <iais:row >
                        <iais:field width="5" value="Donor\'s Age when Sample was Collected"/>
                        <iais:field width="4" value="Donor\'s Age" />
                        <iais:field width="3" value="Available" mandatory="true"/>
                        <span id="error_nullAges" name="iaisErrorMsg" class="error-msg"></span>
                    </iais:row>
                    <c:choose>
                        <c:when test="${donorSampleDto.donorSampleAgeDtos != null}">
                            <c:forEach items="${donorSampleDto.donorSampleAgeDtos}" var="donorSampleAgeDto"  begin="0" varStatus="idxStatus">
                                <iais:row id = "donorAge0">
                                    <label class="col-xs-5 col-md-4 control-label">
                                    </label>
                                    <iais:field width="4" value="${donorSampleAgeDto.age}" />
                                    <iais:value width="3" cssClass="col-md-3">
                                        <input type="checkbox" name ="ageCheckName" value = "${donorSampleAgeDto.id}"
                                            <c:choose>
                                        <c:when test="${donorSampleAgeDto.available}">
                                              checked
                                        </c:when>
                                        <c:otherwise>
                                               disabled =true
                                        </c:otherwise>
                                        </c:choose>
                                        >
                                       </input>
                                    </iais:value>
                                </iais:row>
                            </c:forEach>
                        </c:when>
                    </c:choose>

                    <c:choose>
                        <c:when test="${donorSampleDto.ages != null}">
                            <div class="donorSampleAdd">
                                <c:forEach items="${donorSampleDto.ages}" var="age"  begin="0" varStatus="idxStatus">
                                    <iais:row id = "donorAge${idxStatus.index+1}">
                                        <label class="col-xs-5 col-md-4 control-label">
                                        </label>
                                        <iais:value width="4" cssClass="col-md-4">
                                            <iais:input maxLength="2" type="text" name="ages" value="${age}" onblur='checkAge(this)'/>
                                            <span id="error_ages${idxStatus.index}" name="iaisErrorMsg" class="error-msg"></span>
                                        </iais:value>
                                        <iais:value width="3" cssClass="col-md-3">
                                            <input type="checkbox" name ="ageCheckNameNew" value = "" checked=""  disabled ="true"></input>
                                        </iais:value>
                                        <div class="col-sm-2 col-md-1 col-xs-1 col-md-1">
                                            <h4 class="text-danger">
                                                <em class="fa fa-times-circle del-size-36 removeBtn cursorPointer" class="deleteDonor"  onclick="deleteDonorAge('${idxStatus.index+1}')"></em>
                                            </h4>
                                        </div>
                                    </iais:row>
                                </c:forEach>
                            </div>
                        </c:when>
                    </c:choose>


                    <div id ="donorAge" class="donorSampleAdd">

                    </div>
                </div>
                <iais:row >
                    <iais:value width="5" cssClass="col-md-5" display="true">
                        <a class="addDonor"   onclick="addDonorAge()"style="text-decoration:none;">+ Add Sample of Different Donor's Age</a>
                    </iais:value>
                </iais:row>
        </div>
    </div>
</div>
<iais:confirm msg="Donor\'s age when sperm(s) are collected is outside the range of 21 to 40 yrs old."
              callBack="$('#ageMsgDiv1').hide();" popupOrder="ageMsgDiv1" needCancel="false"
              yesBtnCls="btn btn-secondary" yesBtnDesc="Close"
              needFungDuoJi="false" />
<iais:confirm msg="Donor\'s age when oocyte(s) or embryo(s) are collected is outside the range of 21 to 35 yrs old."
              callBack="$('#ageMsgDiv2').hide();" popupOrder="ageMsgDiv2" needCancel="false"
              yesBtnCls="btn btn-secondary" yesBtnDesc="Close"
              needFungDuoJi="false" />
<input type="hidden" id ="ageCount" value="${ageCount}"/>
<script  type="text/javascript">
    $(document).ready(function(){
       $("#donorIdentityKnownId").change(function(){
           dikChange();
       });
        $("#sampleFromHciCode").change(function(){
            arCentreChange();
        });
        dikChange();
        arCentreChange();
        <c:if test="${arSuperDataSubmissionDto.appType eq 'DSTY_005'}">
        disableContent('div.donorSample');
        //unDisableContent('div.donorSampleAdd');
        </c:if>
    });
    function showDonationYes(){
     $("#directedDonationYes").show();
     $("#directedDonationNo").hide();
        clearFields($("#directedDonationNo"));
        dikChange();
        arCentreChange();
    }
    function showDonationNo(){
        $("#directedDonationNo").show();
        $("#directedDonationYes").hide();
        clearFields($("#directedDonationYes"));
    }
    function addDonorAge(){
        var ageCount =  $("#ageCount").val();
       $("#donorAge").append(getStr(ageCount));
        $("#ageCount").val(Number(ageCount)+1);
    }
    function deleteDonorAge(index){
        $("#donorAge"+index).remove();
    }

    function getStr(index){
        var str = "<div class=\"form-group\" id =\"donorAge" +index +
            "\">\n" +
            "                            <label class=\"col-xs-5 col-md-4 control-label\"></label>\n" +
            "                            <div class=\"col-sm-4 col-md-2 col-xs-4 col-md-4\">\n" +
            "                                <input type=\"text\" name=\"ages\" maxlength=\"2\" onblur='checkAge(this)' autocomplete=\"off\">\n" +
            "                                <span id=\"error_donorAge\" name=\"iaisErrorMsg\" class=\"error-msg\"></span>\n" +
            "                            </div>\n" +
            "<div class=\"col-sm-4 col-md-2 col-xs-3 col-md-3\">\n" +
            "                                            <input type=\"checkbox\" name=\"ageCheckNameNew\" value=\"\" checked=\"\" disabled =\"true\">\n" +
            "                                        </div>"+
            "                            <div class=\"col-sm-2 col-md-1 col-xs-1 col-md-1\">\n" +
            "                               <h4 class=\"text-danger\">"+
            "<em class=\"fa fa-times-circle del-size-36 removeBtn cursorPointer\" class=\"deleteDonor\"  onclick=\"deleteDonorAge('" +index+ "')\"></em>"+
            "</h4>" +
            "                            </div>\n" +
            "                            <div class=\"clear\"></div>\n" +
            "                        </div>";
        return str;
    }

    function arCentreChange(){
        if($("#sampleFromHciCode").val()== 'AR_SC_001'){
            $("#sampleFromOthers").show();
        }else{
            $("#sampleFromOthers").hide();
            clearFields($("#sampleFromOthers"));
        }
    }

    function dikChange(){
        if($("#donorIdentityKnownId").val() == 'DIK001'){
           $("#donorDetail").show();
           $("#donorSampleCodeRow").hide();
            clearFields($("#donorSampleCodeRow"));
        }else{
            $("#donorDetail").hide();
            $("#donorSampleCodeRow").show();
            clearFields($("#donorDetail"));
        }
    }
    function checkAge(t){
        var directedDonation=$('input:radio[name="directedDonation"]:checked').val();
        var age = $(t).val();
        if(age != '') {
            if (directedDonation == 0) {
                var sampleType = $('#sampleTypeId').val();
                if (sampleType == 'DST003') {
                    if(age<21 || age>40 ){
                       $("#ageMsgDiv1").show();
                    }
                } else if (sampleType == 'DST001' || sampleType == 'DST002') {
                    if(age<21 || age>35 ){
                        $("#ageMsgDiv2").show();
                    }
                }
            }
        }
    }
</script>