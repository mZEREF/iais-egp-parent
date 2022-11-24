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
                            <iais:field width="6" value="Is the sample donated from overseas or locally?"
                                        cssClass="col-md-6" mandatory="true"/>
                            <iais:value width="6" cssClass="col-md-6">
                                <iais:value width="6" cssClass="col-md-6">
                                    <div class="form-check" style="padding: 0;">
                                        <input class="form-check-input" id="localDonate" type="radio" name="localOrOversea"
                                               <c:if test="${donorSampleDto.localOrOversea}">checked</c:if>
                                               value="1"/>
                                        <label class="form-check-label" for="localDonate">
                                            <span class="check-circle"></span>Local
                                        </label>
                                    </div>
                                </iais:value>
                                <iais:value width="6" cssClass="col-md-6">
                                    <div class="form-check">
                                        <input class="form-check-input" id="overseasDonate" type="radio" name="localOrOversea"
                                               <c:if test="${donorSampleDto.localOrOversea eq false}">checked</c:if>
                                               value="0"/>
                                        <label class="form-check-label" for="overseasDonate">
                                            <span class="check-circle"></span>Overseas
                                        </label>
                                    </div>
                                </iais:value>
                                <span class="error-msg" name="iaisErrorMsg" id="error_localOrOversea"></span>
                            </iais:value>
                        </iais:row>

                        <iais:row>
                            <iais:field width="6" value="Type of Sample " cssClass="col-md-6" mandatory="true"/>
                            <iais:value width="6" cssClass="col-md-6">
                                <iais:select name="sampleType" id="sampleType" firstOption="Please Select" codeCategory="CATE_ID_DONATED_TYPE"
                                             value="${donorSampleDto.sampleType}"/>
                            </iais:value>
                        </iais:row>

                        <div id="femaleDonorDiv" style="${donorSampleDto.sampleType eq 'DONTY001' or donorSampleDto.sampleType eq 'DONTY002' or donorSampleDto.sampleType eq 'DONTY003'?'':'display: none'}">
                            <iais:row>
                                <iais:field width="6" value="Is the Female Donor's Identity Known? " cssClass="col-md-6"
                                            mandatory="true"/>
                                <iais:value width="6" cssClass="col-md-6">
                                    <iais:value width="6" cssClass="col-md-6">
                                        <div class="form-check" style="padding: 0;">
                                            <input class="form-check-input" type="radio" id="knownF" name="donorIdentityKnown"
                                                   <c:if test="${donorSampleDto.donorIdentityKnown eq 'DIK002'}">checked</c:if>
                                                   value="DIK002"/>
                                            <label class="form-check-label" for="knownF">
                                                <span class="check-circle"></span>No
                                            </label>
                                        </div>
                                    </iais:value>
                                    <iais:value width="6" cssClass="col-md-6">
                                        <div class="form-check">
                                            <input class="form-check-input" id="unKnownF" type="radio" name="donorIdentityKnown"
                                                   <c:if test="${donorSampleDto.donorIdentityKnown eq 'DIK001'}">checked</c:if>
                                                   value="DIK001"/>
                                            <label class="form-check-label" for="unKnownF">
                                                <span class="check-circle"></span>Yes
                                            </label>
                                        </div>
                                    </iais:value>
                                    <span class="error-msg" name="iaisErrorMsg" id="error_donorIdentityKnown"></span>
                                </iais:value>
                            </iais:row>

                            <div style="${donorSampleDto.donorIdentityKnown eq 'DIK001'?'':'display: none'}">
                                <iais:row>
                                    <iais:field width="6" value="Does the Female Donor have a NRIC/FIN number? "
                                                cssClass="col-md-6"
                                                mandatory="true"/>
                                    <iais:value width="6" cssClass="col-md-6">
                                        <iais:value width="6" cssClass="col-md-6">
                                            <div class="form-check" style="padding: 0;">
                                                <input class="form-check-input" id="noIdNUmberF" type="radio" name="hasIdNumberF"
                                                       <c:if test="${donorSampleDto.idType eq 'DTV_IT003'}">checked</c:if>
                                                       value="0"/>
                                                <label class="form-check-label" for="noIdNUmberF">
                                                    <span class="check-circle"></span>No
                                                </label>
                                            </div>
                                        </iais:value>
                                        <iais:value width="6" cssClass="col-md-6">
                                            <div class="form-check">
                                                <input class="form-check-input" id="hasIdNumberF" type="radio" name="hasIdNumberF"
                                                       <c:if test="${donorSampleDto.idType eq 'DTV_IT001' || donorSampleDto.idType eq 'DTV_IT002'}">checked</c:if>
                                                       value="1"/>
                                                <label class="form-check-label" for="hasIdNumberF">
                                                    <span class="check-circle"></span>Yes
                                                </label>
                                            </div>
                                        </iais:value>
                                        <span class="error-msg" name="iaisErrorMsg" id="error_hasIdNumberF"></span>
                                    </iais:value>
                                </iais:row>

                                <iais:row style="${donorSampleDto.donorIdentityKnown eq 'DIK001'?'':'display: none'}">
                                    <c:choose>
                                        <c:when test="${donorSampleDto.idType eq 'DTV_IT003'}">
                                            <iais:field width="6" value="Female Donor's Passport Number " cssClass="col-md-6"/>
                                        </c:when>
                                        <c:otherwise>
                                            <iais:field width="6" value="Female Donor's NRIC/FIN Number " cssClass="col-md-6"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <iais:value width="6" cssClass="col-md-6">
                                        <iais:input maxLength="20" type="text" name="idNumber" value="${donorSampleDto.idNumber}"/>
                                    </iais:value>
                                </iais:row>
                            </div>

                            <iais:row>
                                <iais:field width="6" value="Female Donor Sample Code " cssClass="col-md-6"
                                            mandatory="true"/>
                                <iais:value width="6" cssClass="col-md-6">
                                    <iais:input maxLength="20" type="text" name="donorSampleCode" value="${donorSampleDto.donorSampleCode}"/>
                                </iais:value>
                            </iais:row>
                        </div>

                        <div id="maleDonorDiv" style="${donorSampleDto.sampleType eq 'DONTY004' or donorSampleDto.sampleType eq 'DONTY003'?'':'display: none'}">
                            <iais:row>
                                <iais:field width="6" value="Is the Male Donor's Identity Known? " cssClass="col-md-6"
                                            mandatory="true"/>
                                <iais:value width="6" cssClass="col-md-6">
                                    <iais:value width="6" cssClass="col-md-6">
                                        <div class="form-check" style="padding: 0;">
                                            <input class="form-check-input" id="knownM" type="radio" name="maleDonorIdentityKnow"
                                                   <c:if test="${donorSampleDto.maleDonorIdentityKnow eq false}">checked</c:if>
                                                   value="0"/>
                                            <label class="form-check-label" for="knownM">
                                                <span class="check-circle"></span>No
                                            </label>
                                        </div>
                                    </iais:value>
                                    <iais:value width="6" cssClass="col-md-6">
                                        <div class="form-check">
                                            <input class="form-check-input" id="unKnownM" type="radio" name="maleDonorIdentityKnow"
                                                   <c:if test="${donorSampleDto.maleDonorIdentityKnow}">checked</c:if>
                                                   value="1"/>
                                            <label class="form-check-label" for="unKnownM">
                                                <span class="check-circle"></span>Yes
                                            </label>
                                        </div>
                                    </iais:value>
                                    <span class="error-msg" name="iaisErrorMsg" id="error_maleDonorIdentityKnow"></span>
                                </iais:value>
                            </iais:row>

                            <div style="${donorSampleDto.maleDonorIdentityKnow eq true?'':'display: none'}">
                                <iais:row>
                                    <iais:field width="6" value="Does the Male Donor have a NRIC/FIN number? "
                                                cssClass="col-md-6"
                                                mandatory="true"/>
                                    <iais:value width="6" cssClass="col-md-6">
                                        <iais:value width="6" cssClass="col-md-6">
                                            <div class="form-check" style="padding: 0;">
                                                <input class="form-check-input" id="noIdNUmberM" type="radio" name="hasIdNumberM"
                                                       <c:if test="${donorSampleDto.idTypeMale eq 'DTV_IT003'}">checked</c:if>
                                                       value="0"/>
                                                <label class="form-check-label" for="noIdNUmberM">
                                                    <span class="check-circle"></span>No
                                                </label>
                                            </div>
                                        </iais:value>
                                        <iais:value width="6" cssClass="col-md-6">
                                            <div class="form-check">
                                                <input class="form-check-input" id="hasIdNumberM" type="radio" name="hasIdNumberM"
                                                       <c:if test="${donorSampleDto.idTypeMale eq 'DTV_IT001' || donorSampleDto.idTypeMale eq 'DTV_IT002'}">checked</c:if>
                                                       value="1"/>
                                                <label class="form-check-label" for="hasIdNumberM">
                                                    <span class="check-circle"></span>Yes
                                                </label>
                                            </div>
                                        </iais:value>
                                        <span class="error-msg" name="iaisErrorMsg" id="error_hasIdNumberM"></span>
                                    </iais:value>
                                </iais:row>

                                <iais:row>
                                    <c:choose>
                                        <c:when test="${donorSampleDto.idTypeMale eq 'DTV_IT003'}">
                                            <iais:field width="6" value="Male Donor's Passport Number " cssClass="col-md-6"/>
                                        </c:when>
                                        <c:otherwise>
                                            <iais:field width="6" value="Male Donor's NRIC/FIN Number " cssClass="col-md-6"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <iais:value width="6" cssClass="col-md-6">
                                        <iais:input maxLength="20" type="text" name="idNumberMale" id="donorPassportM"
                                                    value="${donorSampleDto.idNumberMale}"/>
                                    </iais:value>
                                </iais:row>
                            </div>

                            <iais:row>
                                <iais:field width="6" value="Male Donor Sample Code " mandatory="true"
                                            cssClass="col-md-6"/>
                                <iais:value width="6" cssClass="col-md-6">
                                    <iais:input maxLength="20" type="text" name="maleDonorSampleCode" id="maleDonorSampleCode"
                                                value="${donorSampleDto.maleDonorSampleCode}"/>
                                </iais:value>
                            </iais:row>

                        </div>
                        <iais:row style="${donorSampleDto.localOrOversea?'':'display: none;'}">
                            <iais:field width="6" value="Donated to" cssClass="col-md-6"/>
                            <iais:value width="6" cssClass="col-md-6" display="true">
                                <iais:optionText value="${arSuperDataSubmissionDto.premisesDto.premiseLabel}"/>
                            </iais:value>
                        </iais:row>

                        <iais:row style="${donorSampleDto.localOrOversea?'display: none;':''}">
                            <iais:field width="6" value="Which Institution was the Sample Donated From?" cssClass="col-md-6"/>
                            <iais:value width="6" cssClass="col-md-6" display="true">
                                <c:out value="${donorSampleDto.sampleFromOthers}"/>
                            </iais:value>
                        </iais:row>

                        <iais:row>
                            <iais:field width="6" value="Reason(s) for Donation " cssClass="col-md-6"/>
                            <iais:value width="6" cssClass="col-md-6" display="true">
                                <iais:select name="donationReason" id="donationReason" firstOption="Please Select"
                                             codeCategory="CATE_ID_DONATION_REASON"
                                             value="${donorSampleDto.donationReason}"/>
                            </iais:value>
                        </iais:row>

                        <iais:row style="${donorSampleDto.donationReason eq 'DONRES004'?'':'display: none;'}">
                            <iais:field width="6" value="If 'Others', please specify the reason for donation " cssClass="col-md-6"/>
                            <iais:value width="6" cssClass="col-md-6" display="true">
                                <iais:input maxLength="100" type="text" name="otherDonationReason"
                                            value="${donorSampleDto.otherDonationReason}"/>
                            </iais:value>
                        </iais:row>

                        <iais:row>
                            <iais:field width="6" value="Purpose of Donation" cssClass="col-md-6"/>
                            <iais:value width="6" cssClass="col-md-6" display="true">
                                <div class="form-check col-xs-12" style="padding: 0;">
                                    <input class="form-check-input" type="checkbox"
                                           name="donatedForResearch"
                                           value="1"
                                           id="donatedForResearch"
                                           <c:if test="${donorSampleDto.donatedForResearch}">checked</c:if>
                                           aria-invalid="false">
                                    <label class="form-check-label"
                                           for="donatedForResearch"><span
                                            class="check-square"></span>Research</label>
                                </div>
                                <div class="form-check col-xs-12" style="padding: 0;">
                                    <input class="form-check-input" type="checkbox"
                                           name="donatedForTraining"
                                           value="1"
                                           id="donatedForTraining"
                                           <c:if test="${donorSampleDto.donatedForTraining}">checked</c:if>
                                           aria-invalid="false">
                                    <label class="form-check-label"
                                           for="donatedForTraining"><span
                                            class="check-square"></span>Training</label>
                                </div>
                                <div class="form-check col-xs-12" style="padding: 0;">
                                    <input class="form-check-input" type="checkbox"
                                           name="donatedForTreatment"
                                           value="1"
                                           id="donatedForTreatment"
                                           <c:if test="${donorSampleDto.donatedForTreatment}">checked</c:if>
                                           aria-invalid="false">
                                    <label class="form-check-label"
                                           for="donatedForTreatment"><span
                                            class="check-square"></span>Treatment</label>
                                </div>
                                <span class="error-msg" name="iaisErrorMsg" id="error_donationPurpose"></span>
                            </iais:value>
                        </iais:row>

                        <div style="${donorSampleDto.donatedForResearch?'':'display: none;'}">
                            <iais:row>
                                <iais:field width="6" value="No. Donated for Research (Usable for Treatment) "
                                            cssClass="col-md-6"/>
                                <iais:value width="6" cssClass="col-md-6" display="true">
                                    <iais:input maxLength="3" type="text" name="donResForTreatNum" value="${donorSampleDto.donResForTreatNum}"/>
                                </iais:value>
                            </iais:row>

                            <iais:row>
                                <iais:field width="6" value="No. Donated for Research (Not Usable for Treatment) "
                                            cssClass="col-md-6"/>
                                <iais:value width="6" cssClass="col-md-6" display="true">
                                    <iais:input maxLength="3" type="text" name="donResForCurCenNotTreatNum"
                                                value="${donorSampleDto.donResForCurCenNotTreatNum}"/>
                                </iais:value>
                            </iais:row>

                            <iais:row>
                                <iais:field width="6" value="Type of Research for Which Gamete(s) Was Donated"
                                            cssClass="col-md-6"/>
                                <iais:value width="6" cssClass="col-md-6" display="true">
                                    <div class="form-check" style="padding-left:0">
                                        <input class="form-check-input" type="checkbox" value="1" id="donatedForResearchHescr"
                                               <c:if test="${donorSampleDto.donatedForResearchHescr}">checked</c:if>
                                               name="donatedForResearchHescr">
                                        <label class="form-check-label" for="donatedForResearchHescr">
                                            <span class="check-square"></span>Human Embryonic Stem Cell Research
                                        </label>
                                    </div>
                                    <div class="form-check" style="padding-left:0">
                                        <input class="form-check-input" type="checkbox" value="1" id="donatedForResearchRrar"
                                               <c:if test="${donorSampleDto.donatedForResearchRrar}">checked</c:if>
                                               name="donatedForResearchRrar">
                                        <label class="form-check-label" for="donatedForResearchRrar">
                                            <span class="check-square"></span>Research Related to Assisted Reproduction
                                        </label>
                                    </div>
                                    <div class="form-check" style="padding-left:0">
                                        <input class="form-check-input" type="checkbox" value="1" id="donatedForResearchOther"
                                               <c:if test="${donorSampleDto.donatedForResearchOther}">checked</c:if>
                                               name="donatedForResearchOther">
                                        <label class="form-check-label" for="donatedForResearchOther">
                                            <span class="check-square"></span>Other Type of Research
                                        </label>
                                    </div>
                                    <span class="error-msg" name="iaisErrorMsg" id="error_gameteResType"></span>
                                </iais:value>
                            </iais:row>

                            <iais:row>
                                <iais:field width="6" value="Please indicate the Other Type of Research" cssClass="col-md-6"/>
                                <iais:value width="6" cssClass="col-md-6" display="true">
                                    <iais:input maxLength="3" type="text" name="donatedForResearchOtherType"
                                                value="${donorSampleDto.donatedForResearchOtherType}"/>
                                </iais:value>
                            </iais:row>
                        </div>

                        <iais:row style="${donorSampleDto.donatedForTraining?'':'display: none;'}">
                            <iais:field width="6" value="No. Donated for Training " cssClass="col-md-6"/>
                            <iais:value width="6" cssClass="col-md-6" display="true">
                                <iais:input maxLength="3" type="text" name="trainingNum" value="${donorSampleDto.trainingNum}"/>
                            </iais:value>
                        </iais:row>

                        <div style="${donorSampleDto.donatedForTreatment?'':'display: none;'}">
                            <iais:row>
                                <iais:field width="6" value="Is the sample from a directed donation?" cssClass="col-md-6"/>
                                <iais:value width="6" cssClass="col-md-6">
                                    <iais:value width="6" cssClass="col-md-6" style="padding-left:0">
                                        <div class="form-check" style="padding: 0;">
                                            <input class="form-check-input" id="directedDonationY" type="radio" name="directedDonation"
                                                   <c:if test="${donorSampleDto.directedDonation}">checked</c:if>
                                                   value="1"/>
                                            <label class="form-check-label" for="directedDonationY">
                                                <span class="check-circle"></span>Yes
                                            </label>
                                        </div>
                                    </iais:value>
                                    <iais:value width="6" cssClass="col-md-6">
                                        <div class="form-check">
                                            <input class="form-check-input" id="directedDonationN" type="radio" name="directedDonation"
                                                   <c:if test="${donorSampleDto.directedDonation eq false}">checked</c:if>
                                                   value="0"/>
                                            <label class="form-check-label" for="directedDonationN">
                                                <span class="check-circle"></span>No
                                            </label>
                                        </div>
                                    </iais:value>
                                    <span class="error-msg" name="iaisErrorMsg" id="error_directedDonation"></span>
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="6" value="No. Donated for Treatment " cssClass="col-md-6"/>
                                <iais:value width="6" cssClass="col-md-6" display="true">
                                    <iais:input maxLength="3" type="text" name="treatNum" value="${donorSampleDto.treatNum}"/>
                                </iais:value>
                            </iais:row>
                        </div>
                    </div>
                    <div style="${donorSampleDto.sampleType eq 'DONTY001' or donorSampleDto.sampleType eq 'DONTY002' or donorSampleDto.sampleType eq 'DONTY003'?'':'display: none'}">
                    <iais:row >
                        <iais:field width="5" value="Age of Donor at the Point of Donation"/>
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
                                    <iais:value width="3" cssClass="col-md-3" display="true">
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
</div>
<iais:confirm msg="DS_ERR044"
              callBack="$('#ageMsgDiv1').hide();" popupOrder="ageMsgDiv1" needCancel="false"
              yesBtnCls="btn btn-secondary" yesBtnDesc="Close"
              needFungDuoJi="false" />
<iais:confirm msg="DS_ERR045"
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