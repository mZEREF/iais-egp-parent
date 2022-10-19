<%@ page import="com.ecquaria.cloud.moh.iais.helper.MessageUtil" %>

<div class="panel panel-default">
    <div class="panel-heading" style="padding-left: 90px;">
        <h4 class="panel-title">
            <strong>
                Donation
            </strong>
        </h4>
    </div>
    <div id="donationDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <h3>
                    <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                </h3>
                <iais:row>
                    <label class="col-xs-4 col-md-4 control-label">What was Donated? <span class="mandatory">*</span>
                        <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"
                           title="<span style='font-size: 1.5rem;'>${MessageUtil.getMessageDesc("DS_MSG013")}</span>"
                           style="z-index: 10"
                           data-original-title="">i</a>
                    </label>
                    <div class="col-md-7" style="padding-right: 0;padding-left: 0;">
                        <iais:value width="6" cssClass="col-md-6" style="padding-right: 0;padding-left: 0;">
                            <div class="form-check">
                                <input class="form-check-input"
                                       type="radio"
                                       name="donatedType"
                                       value="DONTY001"
                                       id="donatedType1"
                                       <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedType =='DONTY001' }">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="donatedType1"><span
                                        class="check-circle"></span><iais:code code="DONTY001"/></label>
                            </div>
                        </iais:value>
                        <iais:value width="6" cssClass="col-md-6" style="padding-right: 0;padding-left: 0;">
                            <div class="form-check">
                                <input class="form-check-input" type="radio"
                                       name="donatedType" value="DONTY002" id="donatedType2"
                                       <c:if test="${arSuperDataSubmissionDto.donationStageDto.donatedType == 'DONTY002'}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="donatedType2"><span
                                        class="check-circle"></span><iais:code code="DONTY002"/></label>
                            </div>
                        </iais:value>
                        <iais:value width="6" cssClass="col-md-6" style="padding-right: 0;padding-left: 0;">
                            <div class="form-check">
                                <input class="form-check-input"
                                       type="radio"
                                       name="donatedType"
                                       value="DONTY003"
                                       id="donatedType3"
                                       <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedType =='DONTY003' }">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="donatedType3"><span
                                        class="check-circle"></span><iais:code code="DONTY003"/></label>
                            </div>
                        </iais:value>
                        <iais:value width="6" cssClass="col-md-6" style="padding-right: 0;padding-left: 0;">
                            <div class="form-check">
                                <input class="form-check-input" type="radio"
                                       name="donatedType" value="DONTY004" id="donatedType4"
                                       <c:if test="${arSuperDataSubmissionDto.donationStageDto.donatedType == 'DONTY004'}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="donatedType4"><span
                                        class="check-circle"></span><iais:code code="DONTY004"/></label>
                            </div>
                        </iais:value>
                        <span class="error-msg" name="iaisErrorMsg" id="error_donatedType" style="padding-right: 15px;padding-left: 15px;"></span>
                    </div>
                </iais:row>
                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Which AR Centre was Gamete(s)/Embryo(s) Donated to?" id="donatedCentreField" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <select name="donatedCentre" id="donatedCentre" class="donatedCentreSel">
                            <c:forEach items="${curCenDonatedSelectOption}" var="selectOption">
                                <option value="${selectOption.value}" <c:if test="${arSuperDataSubmissionDto.donationStageDto.donatedCentre ==selectOption.value}">selected="selected"</c:if>>${selectOption.text}</option>
                            </c:forEach>
                        </select>
                        <span class="error-msg" name="iaisErrorMsg" id="error_donatedCentre"></span>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Reason for Donation" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:select cssClass="donationReason"  name="donationReason" firstOption="Please Select" options="donationReasonSelectOption" value="${arSuperDataSubmissionDto.donationStageDto.donationReason}"></iais:select>
                        <span class="error-msg" name="iaisErrorMsg" id="error_donationReason"></span>
                    </iais:value>
                </iais:row>
                <div id="otherDonationReasonDisplay" <c:if test="${arSuperDataSubmissionDto.donationStageDto.donationReason!='DONRES004'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="6" cssClass="col-md-6" value="Other Reason for Donation" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <input type="text" maxlength="100"   name="otherDonationReason" value="${arSuperDataSubmissionDto.donationStageDto.otherDonationReason}" >
                            <span class="error-msg" name="iaisErrorMsg" id="error_otherDonationReason"></span>
                        </iais:value>
                    </iais:row>
                </div>


                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Purpose of Donation" mandatory="true"/>
                    <div class="col-md-6" style="padding-right: 0;padding-left: 0;">
                        <iais:value width="12" cssClass="col-md-12" style="padding-right: 0;padding-left: 0;">
                            <div class="form-check">
                                <input class="form-check-input"
                                       type="checkbox"
                                       name="donatedForResearch"
                                       id="donatedForResearch"
                                       <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForResearch ==1 }">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="donatedForResearch"><span
                                        class="check-square"></span>Research</label>
                            </div>
                        </iais:value>
                        <iais:value width="12" cssClass="col-md-12" style="padding-right: 0;padding-left: 0;">
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox"
                                       name="donatedForTraining" id="donatedForTraining"
                                       <c:if test="${arSuperDataSubmissionDto.donationStageDto.donatedForTraining == 1}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="donatedForTraining"><span
                                        class="check-square"></span>Training</label>
                            </div>
                        </iais:value>
                        <iais:value width="12" cssClass="col-md-12" style="padding-right: 0;padding-left: 0;">
                            <div class="form-check">
                                <input class="form-check-input"
                                       type="checkbox"
                                       name="donatedForTreatment"
                                       id="donatedForTreatment"
                                       <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForTreatment ==1 }">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="donatedForTreatment"><span
                                        class="check-square"></span>Treatment</label>
                            </div>
                        </iais:value>
                        <span class="error-msg" name="iaisErrorMsg" id="error_donatedFor" style="padding-right: 15px;padding-left: 15px;"></span>
                    </div>

                </iais:row>
                <div id="researchDisplay" <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForResearch !=1 }">style="display: none"</c:if>>

                    <iais:row>
                        <iais:field width="6" cssClass="col-md-6" value="No. Donated for Research (Usable for Treatment)" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:input maxLength="2" type="text"  id="donResForTreatNum" name="donResForTreatNum" value="${arSuperDataSubmissionDto.donationStageDto.donResForTreatNumStr==null?arSuperDataSubmissionDto.donationStageDto.donResForTreatNum:arSuperDataSubmissionDto.donationStageDto.donResForTreatNumStr}" />
                            <span class="error-msg" name="iaisErrorMsg" id="error_donResForTreatNum"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" cssClass="col-md-6" value="No. Donated to current AR centre for Research (Not Usable for Treatment)" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:input maxLength="2" type="text"   name="donResForCurCenNotTreatNum" id="donResForCurCenNotTreatNum" value="${arSuperDataSubmissionDto.donationStageDto.donResForCurCenNotTreatNumStr==null?arSuperDataSubmissionDto.donationStageDto.donResForCurCenNotTreatNum:arSuperDataSubmissionDto.donationStageDto.donResForCurCenNotTreatNumStr}" />
                            <span class="error-msg" name="iaisErrorMsg" id="error_donResForCurCenNotTreatNum"></span>
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field width="6" cssClass="col-md-6" value="Type of Research for Which Donated" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6" style="padding-right: 0;padding-left: 0;">
                            <div class="form-check">
                                <input class="form-check-input"
                                       type="checkbox"
                                       name="donatedForResearchHescr"
                                       id="donatedForResearchHescr"
                                       <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForResearchHescr ==1 }">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="donatedForResearchHescr"><span
                                        class="check-square"></span>Human Embryonic Stem Cell Research</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input"
                                       type="checkbox"
                                       name="donatedForResearchRrar"
                                       id="donatedForResearchRrar"
                                       <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForResearchRrar ==1 }">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="donatedForResearchRrar"><span
                                        class="check-square"></span>Research Related to Assisted Reproduction</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input"
                                       type="checkbox"
                                       name="donatedForResearchOther"
                                       id="donatedForResearchOther"
                                       <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForResearchOther ==1 }">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="donatedForResearchOther"><span
                                        class="check-square"></span>Other Type of Research</label>
                            </div>
                            <span class="error-msg" name="iaisErrorMsg" id="error_donatedForResearchBox" style="padding-right: 15px;padding-left: 15px;"></span>
                        </iais:value>
                    </iais:row>
                    <div id="donatedForResearchOtherDisplay" <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForResearchOther !=1 }">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="6" cssClass="col-md-6" value="Other Type of Research Donated for"  mandatory="true"/>
                            <iais:value width="6" cssClass="col-md-6">
                                <input type="text" maxlength="100"   name="donatedForResearchOtherType" value="${arSuperDataSubmissionDto.donationStageDto.donatedForResearchOtherType}" >
                                <span class="error-msg" name="iaisErrorMsg" id="error_donatedForResearchOtherType"></span>
                            </iais:value>
                        </iais:row>
                    </div>
                </div>

                <div id="trainingDisplay" <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForTraining !=1 }">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="6" cssClass="col-md-6" value="No. Used for Training" mandatory="false"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:input maxLength="2" type="text"  id="trainingNum" name="trainingNum" value="${arSuperDataSubmissionDto.donationStageDto.trainingNumStr==null?arSuperDataSubmissionDto.donationStageDto.trainingNum:arSuperDataSubmissionDto.donationStageDto.trainingNumStr}" />
                            <span class="error-msg" name="iaisErrorMsg" id="error_trainingNum"></span>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="treatmentDisplay" <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForTreatment !=1 }">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="6" cssClass="col-md-6" value="No. Donated For Treatment" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:input maxLength="2" type="text"  id="treatNum" name="treatNum" value="${arSuperDataSubmissionDto.donationStageDto.treatNumStr==null?arSuperDataSubmissionDto.donationStageDto.treatNum:arSuperDataSubmissionDto.donationStageDto.treatNumStr}" />
                            <span class="error-msg" name="iaisErrorMsg" id="error_treatNum"></span>
                        </iais:value>
                    </iais:row>
                </div>
                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Total No. Donated" mandatory="false"/>
                    <iais:value width="6" cssClass="col-md-6" display="true">
                        <div id="totalNum" name="totalNum">${arSuperDataSubmissionDto.donationStageDto.totalNum}</div>
                        <span class="error-msg" name="iaisErrorMsg" id="error_totalNum"></span>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="ID of Donated Recipient" />
                    <iais:value width="6" cssClass="col-md-6">
                        <input type="text" maxlength="9"   name="donatedRecipientNum" value="${arSuperDataSubmissionDto.donationStageDto.donatedRecipientNum}" >
                        <span class="error-msg" name="iaisErrorMsg" id="error_donatedRecipientNum"></span>
                    </iais:value>
                </iais:row>
                <%@include file="hasDisposalRow.jsp"%>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/donationSection.js"></script>