<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="panel panel-default">
    <div class="panel-heading  ">
        <h4 class="panel-title">
            <a  data-toggle="collapse" href="#donationDetails">
                Donation
            </a>
        </h4>
    </div>
    <div id="donationDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <%@include file="comPart.jsp" %>
                <iais:row>
                    <iais:field width="5" value="Is the sample donated locally or from overseas?" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:if test="${arSuperDataSubmissionDto.donationStageDto.localOrOversea == 1}">Local</c:if>
                        <c:if test="${arSuperDataSubmissionDto.donationStageDto.localOrOversea == 0}">Oversea</c:if>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <label class="col-xs-4 col-md-4 control-label">What was Donated?
                        <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"
                           title="<span style='font-size: 1.5rem;'>${MessageUtil.getMessageDesc("DS_MSG013")}</span>" style="z-index: 10"
                           data-original-title="">i</a>
                    </label>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <iais:code code="${arSuperDataSubmissionDto.donationStageDto.donatedType}"/>
                    </iais:value>
                </iais:row>
                <c:set var="donatedType" value="${arSuperDataSubmissionDto.donationStageDto.donatedType}"/>
                <div id="displayOocyteDonorPatient" <c:if test="${donatedType != 'DONTY001' && donatedType != 'DONTY002' && donatedType != 'DONTY003'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Is the Oocyte Donor the Patient?" mandatory="false"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:if test="${arSuperDataSubmissionDto.donationStageDto.isOocyteDonorPatient == 1}">Yes</c:if>
                            <c:if test="${arSuperDataSubmissionDto.donationStageDto.isOocyteDonorPatient == 0}">No</c:if>
                        </iais:value>
                    </iais:row>
                    <div id="displayIsFemaleIdentityKnown" <c:if test="${arSuperDataSubmissionDto.donationStageDto.isOocyteDonorPatient != 1}">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="5"  value="Is the Female Donor's Identity Known" mandatory="false"/>
                            <iais:value width="7" cssClass="col-md-7" display="true">
                                <c:if test="${arSuperDataSubmissionDto.donationStageDto.isFemaleIdentityKnown == 1}">Yes</c:if>
                                <c:if test="${arSuperDataSubmissionDto.donationStageDto.isFemaleIdentityKnown == 0}">No</c:if>
                            </iais:value>
                        </iais:row>
                    </div>
                    <div id="displayFemaleHaveNricFin" <c:if test="${arSuperDataSubmissionDto.donationStageDto.isFemaleIdentityKnown != 1}">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="5" value="Does the Female Donor have a NRIC/FIN number?" mandatory="false"/>
                            <iais:value width="7" cssClass="col-md-7" display="true">
                                <c:if test="${arSuperDataSubmissionDto.donationStageDto.femaleIdType == 1}">Yes</c:if>
                                <c:if test="${arSuperDataSubmissionDto.donationStageDto.femaleIdType == 0}">No</c:if>
                            </iais:value>
                        </iais:row>
                    </div>
                    <div id="displayFemaleNricFinNumber" <c:if test="${arSuperDataSubmissionDto.donationStageDto.femaleIdType != 1}">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="5" value="Female Donor's NRIC/FIN Number" mandatory="false"/>
                            <iais:value width="7" cssClass="col-md-7" display="true">
                                <c:out value="${arSuperDataSubmissionDto.donationStageDto.femaleIdNumber}"/>
                            </iais:value>
                        </iais:row>
                    </div>
                    <div id="displayPassportNumber" <c:if test="${arSuperDataSubmissionDto.donationStageDto.femaleIdType != 0}">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="5" value="Female Donor's Passport Number" mandatory="false"/>
                            <iais:value width="7" cssClass="col-md-7" display="true">
                                <c:out value="${arSuperDataSubmissionDto.donationStageDto.femaleIdNumber}"/>
                            </iais:value>
                        </iais:row>
                    </div>
                    <iais:row>
                        <iais:field width="5" value="Female Donor Sample Code" mandatory="false"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${arSuperDataSubmissionDto.donationStageDto.femaleDonorSampleCode}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5"  value="Age of Female Donor at the Point of Donation" mandatory="false"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${arSuperDataSubmissionDto.donationStageDto.femaleDonorAge}"/>
                        </iais:value>
                    </iais:row>
                </div>

                <div id="displaySpermDonorPatient" <c:if test="${donatedType != 'DONTY003' && donatedType != 'DONTY004'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Is the Sperm Donor the Patient's Husband?" mandatory="false"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:if test="${arSuperDataSubmissionDto.donationStageDto.isSpermDonorPatient == 1}">Yes</c:if>
                            <c:if test="${arSuperDataSubmissionDto.donationStageDto.isSpermDonorPatient == 0}">No</c:if>
                        </iais:value>
                    </iais:row>
                    <div id="displayIsMaleIdentityKnown" <c:if test="${arSuperDataSubmissionDto.donationStageDto.isSpermDonorPatient != 1}">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="5"  value="Is the Male Donor's Identity Known" mandatory="false"/>
                            <iais:value width="7" cssClass="col-md-7" display="true">
                                <c:if test="${arSuperDataSubmissionDto.donationStageDto.isMaleIdentityKnown == 1}">Yes</c:if>
                                <c:if test="${arSuperDataSubmissionDto.donationStageDto.isMaleIdentityKnown == 0}">No</c:if>
                            </iais:value>
                        </iais:row>
                    </div>
                    <div id="displayMaleHaveNricFin" <c:if test="${arSuperDataSubmissionDto.donationStageDto.isMaleIdentityKnown != 1}">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="5" value="Does the Male Donor have a NRIC/FIN number?" mandatory="false"/>
                            <iais:value width="7" cssClass="col-md-7" display="true">
                                <c:if test="${arSuperDataSubmissionDto.donationStageDto.maleIdType == 1}">Yes</c:if>
                                <c:if test="${arSuperDataSubmissionDto.donationStageDto.maleIdType == 0}">No</c:if>
                            </iais:value>
                        </iais:row>
                    </div>
                    <div id="displayMaleNricFinNumber" <c:if test="${arSuperDataSubmissionDto.donationStageDto.maleIdType != 1}">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="5" value="Male Donor's NRIC/FIN Number" mandatory="false"/>
                            <iais:value width="7" cssClass="col-md-7" display="true">
                                <c:out value="${arSuperDataSubmissionDto.donationStageDto.maleIdNumber}"/>
                            </iais:value>
                        </iais:row>
                    </div>
                    <div id="displayMalePassportNumber" <c:if test="${arSuperDataSubmissionDto.donationStageDto.maleIdType != 0}">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="5" value="Male Donor's Passport Number" mandatory="false"/>
                            <iais:value width="7" cssClass="col-md-7" display="true">
                                <c:out value="${arSuperDataSubmissionDto.donationStageDto.maleIdNumber}"/>
                            </iais:value>
                        </iais:row>
                    </div>
                    <iais:row>
                        <iais:field width="5" value="Male Donor Sample Code" mandatory="false"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${arSuperDataSubmissionDto.donationStageDto.maleDonorSampleCode}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5"  value="Age of Male Donor at the Point of Donation" mandatory="false"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${arSuperDataSubmissionDto.donationStageDto.maleDonorAge}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <iais:row>
                    <iais:field width="5" value="Which AR Centre was Gamete(s)/Embryo(s) Donated to?" id="donatedCentreField" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="${arSuperDataSubmissionDto.donationStageDto.donatedCentreAddress}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Reason for Donation" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <iais:code code="${arSuperDataSubmissionDto.donationStageDto.donationReason}"/>
                    </iais:value>
                </iais:row>
                <div id="otherDonationReasonDisplay" <c:if test="${arSuperDataSubmissionDto.donationStageDto.donationReason!='DONRES004'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Other Reason for Donation" mandatory="false"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${arSuperDataSubmissionDto.donationStageDto.otherDonationReason}"/>
                        </iais:value>
                    </iais:row>
                </div>

                <iais:row>
                    <iais:field width="5" value="Purpose of Donation"  />
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForResearch ==1 }">Research<br></c:if>
                        <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForTraining ==1 }">Training<br></c:if>
                        <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForTreatment ==1 }">Treatment</c:if>
                    </iais:value>
                </iais:row>

                <div id="researchDisplay" <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForResearch !=1 }">style="display: none"</c:if>>

                    <iais:row>
                        <iais:field width="5" value="No. Donated for Research (Usable for Treatment)" mandatory="false"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${arSuperDataSubmissionDto.donationStageDto.donResForTreatNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="No. Donated to current AR centre for Research (Not Usable for Treatment)" mandatory="false"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${arSuperDataSubmissionDto.donationStageDto.donResForCurCenNotTreatNum}"/>
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field width="5" value="Type of Research for Which Gamete(s) was Donated"  />
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForResearchHescr ==1 }">Human Embryonic Stem Cell Research<br></c:if>
                            <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForResearchRrar ==1 }">Research Related to Assisted Reproduction<br></c:if>
                            <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForResearchOther ==1 }">Other Type of Research</c:if>
                        </iais:value>
                    </iais:row>
                    <div id="donatedForResearchOtherDisplay" <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForResearchOther !=1 }">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="5" value="Please Indicate the Other Type of Research" />
                            <iais:value width="7" cssClass="col-md-7" display="true">
                                <c:out value="${arSuperDataSubmissionDto.donationStageDto.donatedForResearchOtherType}"/>
                            </iais:value>
                        </iais:row>
                    </div>
                </div>

                <div id="trainingDisplay" <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForTraining !=1 }">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="No. Used for Training" mandatory="false"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${arSuperDataSubmissionDto.donationStageDto.trainingNum}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="treatmentDisplay" <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForTreatment !=1 }">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Is the Sample from a Directed Donation?" mandatory="false"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:if test="${ arSuperDataSubmissionDto.donationStageDto.isDirectedDonation ==1 }">Yes</c:if>
                            <c:if test="${ arSuperDataSubmissionDto.donationStageDto.isDirectedDonation !=1 }">No</c:if>
                        </iais:value>
                    </iais:row>
                    <div id="treatmentDisplay" <c:if test="${ arSuperDataSubmissionDto.donationStageDto.isDirectedDonation !=1 }">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="5" value="ID No. of Donation Recipient" mandatory="false"/>
                            <iais:value width="7" cssClass="col-md-7" display="true">
                                <c:out value="${arSuperDataSubmissionDto.donationStageDto.recipientNo}"/>
                            </iais:value>
                        </iais:row>
                    </div>
                    <iais:row>
                        <iais:field width="5" value="No. Donated For Treatment" mandatory="false"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${arSuperDataSubmissionDto.donationStageDto.treatNum}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <iais:row>
                    <iais:field width="5" value="Total No. Donated" />
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <div id="totalNum" name="totalNum">${arSuperDataSubmissionDto.donationStageDto.totalNum}</div>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="ID of Donated Recipient" />
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="${arSuperDataSubmissionDto.donationStageDto.donatedRecipientNum}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>