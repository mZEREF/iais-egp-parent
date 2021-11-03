<%--<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/efoSection.js"></script>--%>

<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                Donation
            </strong>
        </h4>
    </div>
    <div id="donationDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">

                <iais:row>
                    <iais:field width="6" value="What was Donated?" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:select cssClass="donatedType"  name="donatedType" firstOption="Please Select" options="donatedTypeSelectOption" value="${arSuperDataSubmissionDto.donationStageDto.donatedType}"></iais:select>
                        <span class="error-msg" name="iaisErrorMsg" id="error_donatedType"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. Donated to Current AR Centre for treatment" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <input type="text" maxlength="2" onkeypress="keyNumericPress()" id="curCenDonatedNum" name="curCenDonatedNum" value="${arSuperDataSubmissionDto.donationStageDto.curCenDonatedNum}" >
                        <span class="error-msg" name="iaisErrorMsg" id="error_curCenDonatedNum"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. Donated to Other AR Centre for treatment" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <input type="text" maxlength="2"  onkeypress="keyNumericPress()" name="otherCenDonatedNum" id="otherCenDonatedNum" value="${arSuperDataSubmissionDto.donationStageDto.otherCenDonatedNum}" >
                        <span class="error-msg" name="iaisErrorMsg" id="error_otherCenDonatedNum"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Which AR Centre was Gamete(s)/Embryo(s) Donated to?" id="isCurCenDonatedNumField" mandatory="false"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <select name="isCurCenDonatedNum" id="isCurCenDonatedNum">
                            <option value="" <c:if test="${empty arSuperDataSubmissionDto.donationStageDto.isCurCenDonatedNum}">selected="selected"</c:if>>Please Select</option>
                            <option value="1" <c:if test="${arSuperDataSubmissionDto.donationStageDto.isCurCenDonatedNum ==1}">selected="selected"</c:if>>AR Centres with active AR licence</option>
                            <option value="0" <c:if test="${arSuperDataSubmissionDto.donationStageDto.isCurCenDonatedNum ==0}">selected="selected"</c:if>>Others</option>
                        </select>
                        <span class="error-msg" name="iaisErrorMsg" id="error_isCurCenDonatedNum"></span>
                    </iais:value>
                </iais:row>
                <div id="otherDonatedCenDisplay" <c:if test="${arSuperDataSubmissionDto.donationStageDto.isCurCenDonatedNum!=0}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="Other Centre where Embryos were Biospied At" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <input type="text" maxlength="100"   name="otherDonatedCen" value="${arSuperDataSubmissionDto.donationStageDto.otherDonatedCen}" >
                            <span class="error-msg" name="iaisErrorMsg" id="error_otherDonatedCen"></span>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="directedDonorIdDisplay" <c:if test="${arSuperDataSubmissionDto.donationStageDto.isCurCenDonatedNum==0}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="ID of Directed Donor (if applicable)" mandatory="false"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <input type="text" maxlength="20"   name="directedDonorId" value="${arSuperDataSubmissionDto.donationStageDto.directedDonorId}" >
                            <span class="error-msg" name="iaisErrorMsg" id="error_directedDonorId"></span>
                        </iais:value>
                    </iais:row>
                </div>

                <iais:row>
                    <iais:field width="6" value="No. Donated for Research (Usable for Treatment)" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <input type="text" maxlength="2" onkeypress="keyNumericPress()"  name="resDonarNum" id="resDonarNum" value="${arSuperDataSubmissionDto.donationStageDto.resDonarNum}" >
                        <span class="error-msg" name="iaisErrorMsg" id="error_resDonarNum"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. Donated to current AR centre for Research (Not Usable for Treatment) " mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <input type="text" maxlength="2" onkeypress="keyNumericPress()" name="curCenResDonatedNum" id="curCenResDonatedNum" value="${arSuperDataSubmissionDto.donationStageDto.curCenResDonatedNum}" >
                        <span class="error-msg" name="iaisErrorMsg" id="error_curCenResDonatedNum"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" id="isCurCenResTypeField"  value="Type of Research for Which Donated" mandatory="false" />
                    <iais:value width="6" cssClass="col-md-6">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="checkbox"
                                   name="isCurCenResTypeHescr"
                                   id="isCurCenResTypeHescr"
                                   <c:if test="${ arSuperDataSubmissionDto.donationStageDto.isCurCenResTypeHescr ==1 }">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="isCurCenResTypeHescr"><span
                                    class="check-square"></span>Human Embryonic Stem Cell Research</label>
                        </div>
                    </iais:value>
                    <iais:value width="6" cssClass="col-md-6">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="checkbox"
                                   name="isCurCenResTypeRrar"
                                   id="isCurCenResTypeRrar"
                                   <c:if test="${ arSuperDataSubmissionDto.donationStageDto.isCurCenResTypeRrar ==1 }">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="isCurCenResTypeRrar"><span
                                    class="check-square"></span>Research Related to Assisted Reproduction</label>
                        </div>
                    </iais:value>
                    <iais:field width="6" value="" />
                    <iais:value width="6" cssClass="col-md-6">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="checkbox"
                                   name="isCurCenResTypeOther"
                                   id="isCurCenResTypeOther"
                                   <c:if test="${ arSuperDataSubmissionDto.donationStageDto.isCurCenResTypeOther ==1 }">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="isCurCenResTypeOther"><span
                                    class="check-square"></span>Other Type of Research</label>
                        </div>
                        <span class="error-msg" name="iaisErrorMsg" id="error_curCenResType"></span>
                    </iais:value>
                </iais:row>
                <div id="curCenResTypeOtherDisplay" <c:if test="${arSuperDataSubmissionDto.donationStageDto.isCurCenResTypeOther!=1}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="Other Type of Research Donated for" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <input type="text" maxlength="20"   name="curCenOtherResType" value="${arSuperDataSubmissionDto.donationStageDto.curCenOtherResType}" >
                            <span class="error-msg" name="iaisErrorMsg" id="error_curCenOtherResType"></span>
                        </iais:value>
                    </iais:row>
                </div>

                <iais:row>
                    <iais:field width="6" value="No. Donated to Other Centres / Institutions for Research" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <input type="text" maxlength="2"  onkeypress="keyNumericPress()" name="otherCenResDonarNum" id="otherCenResDonarNum" value="${arSuperDataSubmissionDto.donationStageDto.otherCenResDonarNum}" >
                        <span class="error-msg" name="iaisErrorMsg" id="error_otherCenResDonarNum"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Other AR Centre / Institution Sent to" id="isInsSentToCurField" mandatory="false"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <select name="isInsSentToCur" id="isInsSentToCur">
                            <option value="" <c:if test="${empty arSuperDataSubmissionDto.donationStageDto.isInsSentToCur}">selected="selected"</c:if>>Please Select</option>
                            <option value="1" <c:if test="${arSuperDataSubmissionDto.donationStageDto.isInsSentToCur ==1}">selected="selected"</c:if>>AR Centres with active AR licence</option>
                            <option value="0" <c:if test="${arSuperDataSubmissionDto.donationStageDto.isInsSentToCur ==0}">selected="selected"</c:if>>Others</option>
                        </select>
                        <span class="error-msg" name="iaisErrorMsg" id="error_isInsSentToCur"></span>
                    </iais:value>
                </iais:row>
                <div id="insSentToOtherCenDisplay" <c:if test="${arSuperDataSubmissionDto.donationStageDto.isInsSentToCur!=0}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="Other Type of Research Donated for" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <input type="text" maxlength="100"   name="insSentToOtherCen" value="${arSuperDataSubmissionDto.donationStageDto.insSentToOtherCen}" >
                            <span class="error-msg" name="iaisErrorMsg" id="error_insSentToOtherCen"></span>
                        </iais:value>
                    </iais:row>
                </div>


                <iais:row>
                    <iais:field width="6" value="No. Used for Training" mandatory="false"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <input type="text" maxlength="2" onkeypress="keyNumericPress()" id="trainingNum" name="trainingNum" value="${arSuperDataSubmissionDto.donationStageDto.trainingNum}" >
                        <span class="error-msg" name="iaisErrorMsg" id="error_trainingNum"></span>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="6" value="Reason for Donation" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:select cssClass="donationReason"  name="donationReason" firstOption="Please Select" options="donationReasonSelectOption" value="${arSuperDataSubmissionDto.donationStageDto.donationReason}"></iais:select>
                        <span class="error-msg" name="iaisErrorMsg" id="error_donationReason"></span>
                    </iais:value>
                </iais:row>
                <div id="otherDonationReasonDisplay" <c:if test="${arSuperDataSubmissionDto.donationStageDto.donationReason!='DONRES004'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="Other Type of Research Donated for" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <input type="text" maxlength="20"   name="otherDonationReason" value="${arSuperDataSubmissionDto.donationStageDto.otherDonationReason}" >
                            <span class="error-msg" name="iaisErrorMsg" id="error_otherDonationReason"></span>
                        </iais:value>
                    </iais:row>
                </div>

                <iais:row>
                    <iais:field width="6" value="Total No. Donated" mandatory="false"/>
                    <iais:value width="6" display="true">
                        <div id="totalNum" name="totalNum">${arSuperDataSubmissionDto.donationStageDto.totalNum}</div>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" >

    $('input[type="text"]').blur(function () {

        var curCenDonatedNum = $('#curCenDonatedNum').val();
        var otherCenDonatedNum = $('#otherCenDonatedNum').val();
        var resDonarNum = $('#resDonarNum').val();
        var curCenResDonatedNum = $('#curCenResDonatedNum').val();
        var otherCenResDonarNum = $('#otherCenResDonarNum').val();
        var trainingNum = $('#trainingNum').val();
        var totalNum = Number(curCenDonatedNum)+Number(otherCenDonatedNum)+Number(resDonarNum)+Number(curCenResDonatedNum)+Number(otherCenResDonarNum)+Number(trainingNum);


        $('#totalNum').html(totalNum);

        if(Number(curCenDonatedNum)>0){
            $('#isCurCenDonatedNumField').html('Which AR Centre was Gamete(s)/Embryo(s) Donated to? <span class="mandatory">*</span>')
        }else {
            $('#isCurCenDonatedNumField').html('Which AR Centre was Gamete(s)/Embryo(s) Donated to?')
        }

        if(Number(resDonarNum)>0||Number(curCenResDonatedNum)>0){
            $('#isCurCenResTypeField').html('Type of Research for Which Donated <span class="mandatory">*</span>')
        }else {
            $('#isCurCenResTypeField').html('Type of Research for Which Donated')
        }

        if(Number(otherCenResDonarNum)>0){
            $('#isInsSentToCurField').html('Other AR Centre / Institution Sent to <span class="mandatory">*</span>')
        }else {
            $('#isInsSentToCurField').html('Other AR Centre / Institution Sent to')
        }
    });


    function keyNumericPress() {
        var keyCode = event.keyCode;
        event.returnValue = keyCode >= 48 && keyCode <= 57;
    }
    $('#isCurCenDonatedNum').change(function () {

        var reason= $('#isCurCenDonatedNum option:selected').val();

        if("0"==reason){
            $('#otherDonatedCenDisplay').attr("style","display: block");
            $('#directedDonorIdDisplay').attr("style","display: none");

        }else {
            $('#directedDonorIdDisplay').attr("style","display: block");
            $('#otherDonatedCenDisplay').attr("style","display: none");
        }

    });
    $('#isCurCenResTypeOther').change(function () {
        if($(this).is(':checked')){
            $('#curCenResTypeOtherDisplay').attr("style","display: block");
        }else {
            $('#curCenResTypeOtherDisplay').attr("style","display: none");
        }
    });

    $('#isInsSentToCurField').change(function () {

        var reason= $('#isInsSentToCurField option:selected').val();

        if("0"==reason){
            $('#insSentToOtherCenDisplay').attr("style","display: block");
        }else {
            $('#insSentToOtherCenDisplay').attr("style","display: none");
        }

    });

    $('#donationReason').change(function () {

        var reason= $('#donationReason option:selected').val();

        if("DONRES004"==reason){
            $('#otherDonationReasonDisplay').attr("style","display: block");
        }else {
            $('#otherDonationReasonDisplay').attr("style","display: none");
        }

    });
</script>