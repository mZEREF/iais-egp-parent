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
                        <iais:row>
                            <iais:field width="5" value="Donor relation to patient" mandatory="true"/>
                            <iais:value width="3" cssClass="col-md-3">
                                <div class="form-check" >
                                    <input class="form-check-input"
                                           type="radio"
                                           name="donorRelation"
                                           value="F"
                                           id = "donorRelationF"
                                           <c:if test="${donorSampleDto.donorRelation eq 'F'}">checked</c:if>
                                           aria-invalid="false">
                                    <label class="form-check-label"
                                           for="donorRelationF"><span
                                            class="check-circle"></span>Friend</label>
                                </div>
                                <span id="error_donorRelation" name="iaisErrorMsg" class="error-msg"></span>
                            </iais:value>
                            <iais:value width="3" cssClass="col-md-3">
                                <div class="form-check" >
                                    <input class="form-check-input" type="radio"
                                           name="donorRelation"
                                           value="R"
                                           id = "donorRelationR"
                                           <c:if test="${donorSampleDto.donorRelation eq 'R'}">checked</c:if>
                                           aria-invalid="false">
                                    <label class="form-check-label"
                                           for="donorRelationR" ><span
                                            class="check-circle"></span>Relative</label>
                                </div>
                            </iais:value>
                        </iais:row>
                    </div>
                    <div id="directedDonationNo" style="${donorSampleDto.directedDonation ? 'display: none;' : ''}">
                        <iais:row id="sampleType" >
                            <iais:field width="5" value="Sample Type" mandatory="true"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <iais:select name="sampleType" firstOption="Please Select" codeCategory="AR_DONOR_SAMPLE_TYPE" value="${donorSampleDto.sampleType}"
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

                    <c:choose>
                        <c:when test="${donorSampleDto.donorSampleAgeDtos != null}">
                            <c:forEach items="${donorSampleDto.donorSampleAgeDtos}" var="donorSampleAgeDto"  begin="0" varStatus="idxStatus">
                                <iais:row id = "donorAge0">
                                    <label class="col-xs-5 col-md-4 control-label">
                                        <c:if test="${idxStatus.first==true}">
                                            Donor's Age when Sample was Collected
                                            <span class="mandatory">*</span>
                                        </c:if>
                                    </label>
                                    <iais:value width="7" cssClass="col-md-7">
                                        <input type="text" name="oldAges" value="${donorSampleAgeDto.age}" maxlength="2" autocomplete="off" disabled =true>
                                        <span id="error_oldAges" name="iaisErrorMsg" class="error-msg"></span>
                                    </iais:value>
                                </iais:row>
                            </c:forEach>
                        </c:when>
                    </c:choose>

                    <c:choose>
                        <c:when test="${donorSampleDto.ages == null}">
                            <c:if test="${donorSampleDto.donorSampleAgeDtos == null}">
                                <iais:row  id = "donorAge0">
                                    <iais:field width="5" value="Donor's Age when Sample was Collected" mandatory="true"/>
                                    <iais:value width="7" cssClass="col-md-7">
                                        <iais:input maxLength="2" type="text" name="ages" value="" />
                                        <span id="error_ages0" name="iaisErrorMsg" class="error-msg"></span>
                                    </iais:value>
                                </iais:row>
                            </c:if>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${donorSampleDto.ages}" var="age"  begin="0" varStatus="idxStatus">
                                <iais:row id = "donorAge${idxStatus.index}">
                                    <label class="col-xs-5 col-md-4 control-label">
                                        <c:if test="${idxStatus.first==true && donorSampleDto.donorSampleAgeDtos == null}">
                                            Donor's Age when Sample was Collected
                                            <span class="mandatory">*</span>
                                        </c:if>
                                    </label>
                                    <iais:value width="7" cssClass="col-md-7">
                                        <iais:input maxLength="2" type="text" name="ages" value="${age}" />
                                        <span id="error_ages${idxStatus.index}" name="iaisErrorMsg" class="error-msg"></span>
                                    </iais:value>
                                    <c:if test="${idxStatus.first!=true}">
                                        <div class="col-sm-2 col-md-1 col-xs-1 col-md-1">
                                            <h4 class="text-danger">
                                                <em class="fa fa-times-circle del-size-36 removeBtn cursorPointer" class="deleteDonor"  onclick="deleteDonorAge('${idxStatus.index}')"></em>
                                            </h4>
                                        </div>
                                    </c:if>
                                </iais:row>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>


                    <div id ="donorAge">

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
            "                            <div class=\"col-sm-7 col-md-5 col-xs-7 col-md-7\">\n" +
            "                                <input type=\"text\" name=\"ages" +
            "\" maxlength=\"2\" autocomplete=\"off\">\n" +
            "                                <span id=\"error_donorAge\" name=\"iaisErrorMsg\" class=\"error-msg\"></span>\n" +
            "                            </div>\n" +
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
</script>