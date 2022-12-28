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
                    <iais:field width="4" value="Is the sample donated locally or from overseas?" mandatory="false"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:if test="${arSuperDataSubmissionDto.donationStageDto.localOrOversea == 1}">Local</c:if>
                        <c:if test="${arSuperDataSubmissionDto.donationStageDto.localOrOversea == 0}">Overseas</c:if>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:if test="${arSuperDataSubmissionDtoVersion.donationStageDto.localOrOversea == 1}">Local</c:if>
                        <c:if test="${arSuperDataSubmissionDtoVersion.donationStageDto.localOrOversea == 0}">Overseas</c:if>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <label class="col-xs-4 col-md-4 control-label">Type of Sample
                        <c:if test="${arSuperDataSubmissionDto.donationStageDto.donatedType == 'DONTY004' || arSuperDataSubmissionDtoVersion.donationStageDto.donatedType == 'DONTY004'}">
                        <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"
                           title="<span style='font-size: 1.5rem;'>${MessageUtil.getMessageDesc("DS_MSG013")}</span>"
                           style="z-index: 10"
                           data-original-title="">i</a>
                        </c:if>
                    </label>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <iais:code code="${arSuperDataSubmissionDto.donationStageDto.donatedType}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <iais:code code="${arSuperDataSubmissionDtoVersion.donationStageDto.donatedType}"/>
                    </iais:value>
                </iais:row>
                <c:set var="donatedType" value="${arSuperDataSubmissionDto.donationStageDto.donatedType}"/>
                <c:set var="donatedTypeVersion" value="${arSuperDataSubmissionDtoVersion.donationStageDto.donatedType}"/>
                <div id="displayOocyteDonorPatient" <c:if test="${donatedType != 'DONTY001' && donatedType != 'DONTY002' && donatedType != 'DONTY003' && donatedTypeVersion != 'DONTY001' && donatedTypeVersion != 'DONTY002' && donatedTypeVersion != 'DONTY003'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="4" value="Is the Oocyte Donor the Patient?" mandatory="false"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:if test="${arSuperDataSubmissionDto.donationStageDto.isOocyteDonorPatient == 1}">Yes</c:if>
                            <c:if test="${arSuperDataSubmissionDto.donationStageDto.isOocyteDonorPatient == 0}">No</c:if>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:if test="${arSuperDataSubmissionDtoVersion.donationStageDto.isOocyteDonorPatient == 1}">Yes</c:if>
                            <c:if test="${arSuperDataSubmissionDtoVersion.donationStageDto.isOocyteDonorPatient == 0}">No</c:if>
                        </iais:value>
                    </iais:row>
                    <div id="displayIsFemaleIdentityKnown" <c:if test="${arSuperDataSubmissionDto.donationStageDto.isOocyteDonorPatient != 1 && arSuperDataSubmissionDtoVersion.donationStageDto.isOocyteDonorPatient != 1}">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="4"  value="Is the Female Donor's Identity Known" mandatory="false"/>
                            <iais:value width="4" cssClass="col-md-4" display="true">
                                <c:if test="${arSuperDataSubmissionDto.donationStageDto.isFemaleIdentityKnown == 1}">Yes</c:if>
                                <c:if test="${arSuperDataSubmissionDto.donationStageDto.isFemaleIdentityKnown == 0}">No</c:if>
                            </iais:value>
                            <iais:value width="4" cssClass="col-md-4" display="true">
                                <c:if test="${arSuperDataSubmissionDtoVersion.donationStageDto.isFemaleIdentityKnown == 1}">Yes</c:if>
                                <c:if test="${arSuperDataSubmissionDtoVersion.donationStageDto.isFemaleIdentityKnown == 0}">No</c:if>
                            </iais:value>
                        </iais:row>
                    </div>
                    <div id="displayFemaleHaveNricFin" <c:if test="${arSuperDataSubmissionDto.donationStageDto.isFemaleIdentityKnown != 0 && arSuperDataSubmissionDtoVersion.donationStageDto.isFemaleIdentityKnown != 0}">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="4" value="Does the Female Donor have a NRIC/FIN number?" mandatory="false"/>
                            <iais:value width="4" cssClass="col-md-4" display="true">
                                <c:if test="${arSuperDataSubmissionDto.donationStageDto.femaleIdType == 1}">Yes</c:if>
                                <c:if test="${arSuperDataSubmissionDto.donationStageDto.femaleIdType == 0}">No</c:if>
                            </iais:value>
                            <iais:value width="4" cssClass="col-md-4" display="true">
                                <c:if test="${arSuperDataSubmissionDtoVersion.donationStageDto.femaleIdType == 1}">Yes</c:if>
                                <c:if test="${arSuperDataSubmissionDtoVersion.donationStageDto.femaleIdType == 0}">No</c:if>
                            </iais:value>
                        </iais:row>
                    </div>
                    <div id="displayFemaleNricFinNumber" <c:if test="${arSuperDataSubmissionDto.donationStageDto.femaleIdType != 1 && arSuperDataSubmissionDtoVersion.donationStageDto.femaleIdType != 1}">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="4" value="Female Donor's NRIC/FIN Number" mandatory="false"/>
                            <iais:value width="4" cssClass="col-md-4" display="true">
                                <c:out value="${arSuperDataSubmissionDto.donationStageDto.femaleIdNumber}"/>
                            </iais:value>
                            <iais:value width="4" cssClass="col-md-4" display="true">
                                <c:out value="${arSuperDataSubmissionDtoVersion.donationStageDto.femaleIdNumber}"/>
                            </iais:value>
                        </iais:row>
                    </div>
                    <div id="displayPassportNumber" <c:if test="${arSuperDataSubmissionDto.donationStageDto.femaleIdType != 0 && arSuperDataSubmissionDtoVersion.donationStageDto.femaleIdType != 0}">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="4" value="Female Donor's Passport Number" mandatory="false"/>
                            <iais:value width="4" cssClass="col-md-4" display="true">
                                <c:out value="${arSuperDataSubmissionDto.donationStageDto.femaleIdNumber}"/>
                            </iais:value>
                            <iais:value width="4" cssClass="col-md-4" display="true">
                                <c:out value="${arSuperDataSubmissionDtoVersion.donationStageDto.femaleIdNumber}"/>
                            </iais:value>
                        </iais:row>
                    </div>
                    <iais:row>
                        <iais:field width="4" value="Female Donor Sample Code" mandatory="false"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${arSuperDataSubmissionDto.donationStageDto.femaleDonorSampleCode}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${arSuperDataSubmissionDtoVersion.donationStageDto.femaleDonorSampleCode}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="4"  value="Age of Female Donor at the Point of Donation" mandatory="false"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${arSuperDataSubmissionDto.donationStageDto.femaleDonorAge}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${arSuperDataSubmissionDtoVersion.donationStageDto.femaleDonorAge}"/>
                        </iais:value>
                    </iais:row>
                </div>

                <div id="displaySpermDonorPatient" <c:if test="${donatedType != 'DONTY003'&& donatedType != 'DONTY004'&& donatedTypeVersion != 'DONTY003'&& donatedTypeVersion != 'DONTY004'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="4" value="Is the Sperm Donor the Patient's Husband?" mandatory="false"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:if test="${arSuperDataSubmissionDto.donationStageDto.isSpermDonorPatient == 1}">Yes</c:if>
                            <c:if test="${arSuperDataSubmissionDto.donationStageDto.isSpermDonorPatient == 0}">No</c:if>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:if test="${arSuperDataSubmissionDtoVersion.donationStageDto.isSpermDonorPatient == 1}">Yes</c:if>
                            <c:if test="${arSuperDataSubmissionDtoVersion.donationStageDto.isSpermDonorPatient == 0}">No</c:if>
                        </iais:value>
                    </iais:row>
                    <div id="displayIsMaleIdentityKnown" <c:if test="${arSuperDataSubmissionDto.donationStageDto.isSpermDonorPatient != 1 && arSuperDataSubmissionDtoVersion.donationStageDto.isSpermDonorPatient != 1}">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="4"  value="Is the Male Donor's Identity Known" mandatory="false"/>
                            <iais:value width="4" cssClass="col-md-4" display="true">
                                <c:if test="${arSuperDataSubmissionDto.donationStageDto.isMaleIdentityKnown == 1}">Yes</c:if>
                                <c:if test="${arSuperDataSubmissionDto.donationStageDto.isMaleIdentityKnown == 0}">No</c:if>
                            </iais:value>
                            <iais:value width="4" cssClass="col-md-4" display="true">
                                <c:if test="${arSuperDataSubmissionDtoVersion.donationStageDto.isMaleIdentityKnown == 1}">Yes</c:if>
                                <c:if test="${arSuperDataSubmissionDtoVersion.donationStageDto.isMaleIdentityKnown == 0}">No</c:if>
                            </iais:value>
                        </iais:row>
                    </div>
                    <div id="displayMaleHaveNricFin" <c:if test="${arSuperDataSubmissionDto.donationStageDto.isMaleIdentityKnown != 0 && arSuperDataSubmissionDtoVersion.donationStageDto.isMaleIdentityKnown != 0}">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="4" value="Does the Male Donor have a NRIC/FIN number?" mandatory="false"/>
                            <iais:value width="4" cssClass="col-md-4" display="true">
                                <c:if test="${arSuperDataSubmissionDto.donationStageDto.maleIdType == 1}">Yes</c:if>
                                <c:if test="${arSuperDataSubmissionDto.donationStageDto.maleIdType == 0}">No</c:if>
                            </iais:value>
                            <iais:value width="4" cssClass="col-md-4" display="true">
                                <c:if test="${arSuperDataSubmissionDtoVersion.donationStageDto.maleIdType == 1}">Yes</c:if>
                                <c:if test="${arSuperDataSubmissionDtoVersion.donationStageDto.maleIdType == 0}">No</c:if>
                            </iais:value>
                        </iais:row>
                    </div>
                    <div id="displayMaleNricFinNumber" <c:if test="${arSuperDataSubmissionDto.donationStageDto.maleIdType != 1 && arSuperDataSubmissionDtoVersion.donationStageDto.maleIdType != 1}">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="4" value="Male Donor's NRIC/FIN Number" mandatory="false"/>
                            <iais:value width="4" cssClass="col-md-4" display="true">
                                <c:out value="${arSuperDataSubmissionDto.donationStageDto.maleIdNumber}"/>
                            </iais:value>
                            <iais:value width="4" cssClass="col-md-4" display="true">
                                <c:out value="${arSuperDataSubmissionDtoVersion.donationStageDto.maleIdNumber}"/>
                            </iais:value>
                        </iais:row>
                    </div>
                    <div id="displayMalePassportNumber" <c:if test="${arSuperDataSubmissionDto.donationStageDto.maleIdType != 0 && arSuperDataSubmissionDtoVersion.donationStageDto.maleIdType != 0}">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="4" value="Male Donor's Passport Number" mandatory="false"/>
                            <iais:value width="4" cssClass="col-md-4" display="true">
                                <c:out value="${arSuperDataSubmissionDto.donationStageDto.maleIdNumber}"/>
                            </iais:value>
                            <iais:value width="4" cssClass="col-md-4" display="true">
                                <c:out value="${arSuperDataSubmissionDtoVersion.donationStageDto.maleIdNumber}"/>
                            </iais:value>
                        </iais:row>
                    </div>
                    <iais:row>
                        <iais:field width="4" value="Male Donor Sample Code" mandatory="false"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${arSuperDataSubmissionDto.donationStageDto.maleDonorSampleCode}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${arSuperDataSubmissionDtoVersion.donationStageDto.maleDonorSampleCode}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="4"  value="Age of Male Donor at the Point of Donation" mandatory="false"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${arSuperDataSubmissionDto.donationStageDto.maleDonorAge}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${arSuperDataSubmissionDtoVersion.donationStageDto.maleDonorAge}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="sampleFromLocal" <c:if test="${arSuperDataSubmissionDto.donationStageDto.localOrOversea != 1 && arSuperDataSubmissionDtoVersion.donationStageDto.localOrOversea != 1}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="4" value="Donated to" id="donatedCentreField" mandatory="false"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${arSuperDataSubmissionDto.donationStageDto.donatedCentreAddress}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${arSuperDataSubmissionDtoVersion.donationStageDto.donatedCentreAddress}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="sampleFromOversea" <c:if test="${arSuperDataSubmissionDto.donationStageDto.localOrOversea != 0 && arSuperDataSubmissionDtoVersion.donationStageDto.localOrOversea != 0}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="4" value="Which Institution was the Sample Donated From?" mandatory="false"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${arSuperDataSubmissionDto.donationStageDto.overseaDonatedCentre}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${arSuperDataSubmissionDtoVersion.donationStageDto.overseaDonatedCentre}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <iais:row>
                    <iais:field width="4" value="Reason for Donation" mandatory="false"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <iais:code code="${arSuperDataSubmissionDto.donationStageDto.donationReason}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <iais:code code="${arSuperDataSubmissionDtoVersion.donationStageDto.donationReason}"/>
                    </iais:value>
                </iais:row>
                <div id="otherDonationReasonDisplay" <c:if test="${arSuperDataSubmissionDto.donationStageDto.donationReason!='DONRES004' && arSuperDataSubmissionDtoVersion.donationStageDto.donationReason!='DONRES004'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="4" value="Other Reason for Donation" mandatory="false"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${arSuperDataSubmissionDto.donationStageDto.otherDonationReason}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${arSuperDataSubmissionDtoVersion.donationStageDto.otherDonationReason}"/>
                        </iais:value>
                    </iais:row>
                </div>

                <iais:row>
                    <iais:field width="4" value="Purpose of Donation"  />
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForTreatment ==1 }">Treatment</c:if>
                        <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForResearch ==1 }">Research<br></c:if>
                        <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForTraining ==1 }">Training<br></c:if>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:if test="${ arSuperDataSubmissionDtoVersion.donationStageDto.donatedForTreatment ==1 }">Treatment</c:if>
                        <c:if test="${ arSuperDataSubmissionDtoVersion.donationStageDto.donatedForResearch ==1 }">Research<br></c:if>
                        <c:if test="${ arSuperDataSubmissionDtoVersion.donationStageDto.donatedForTraining ==1 }">Training<br></c:if>
                    </iais:value>
                </iais:row>

                <div id="treatmentDisplay" <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForTreatment !=1 &&  arSuperDataSubmissionDtoVersion.donationStageDto.donatedForTreatment !=1}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="4" value="Is the Sample from a Directed Donation?" mandatory="false"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:if test="${ arSuperDataSubmissionDto.donationStageDto.isDirectedDonation ==1 }">Yes</c:if>
                            <c:if test="${ arSuperDataSubmissionDto.donationStageDto.isDirectedDonation !=1 }">No</c:if>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:if test="${ arSuperDataSubmissionDtoVersion.donationStageDto.isDirectedDonation ==1 }">Yes</c:if>
                            <c:if test="${ arSuperDataSubmissionDtoVersion.donationStageDto.isDirectedDonation !=1 }">No</c:if>
                        </iais:value>
                    </iais:row>
                    <div id="treatmentDisplay" <c:if test="${ arSuperDataSubmissionDto.donationStageDto.isDirectedDonation !=1 && arSuperDataSubmissionDtoVersion.donationStageDto.isDirectedDonation !=1}">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="4" value="ID No. of Donation Recipient" mandatory="false"/>
                            <iais:value width="4" cssClass="col-md-4" display="true">
                                <c:out value="${arSuperDataSubmissionDto.donationStageDto.recipientNo}"/>
                            </iais:value>
                            <iais:value width="4" cssClass="col-md-4" display="true">
                                <c:out value="${arSuperDataSubmissionDtoVersion.donationStageDto.recipientNo}"/>
                            </iais:value>
                        </iais:row>
                    </div>
                    <iais:row>
                        <iais:field width="4" value="No. Donated For Treatment" mandatory="false"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${arSuperDataSubmissionDto.donationStageDto.treatNum}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${arSuperDataSubmissionDtoVersion.donationStageDto.treatNum}"/>
                        </iais:value>
                    </iais:row>
                </div>

                <div id="researchDisplay" <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForResearch !=1 && arSuperDataSubmissionDtoVersion.donationStageDto.donatedForResearch !=1 }">style="display: none"</c:if>>

                    <iais:row>
                        <iais:field width="4" value="No. Donated for Research (Usable for Treatment)" mandatory="false"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${arSuperDataSubmissionDto.donationStageDto.donResForTreatNum}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${arSuperDataSubmissionDtoVersion.donationStageDto.donResForTreatNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="4" value="No. Donated to current AR centre for Research (Not Usable for Treatment)" mandatory="false"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${arSuperDataSubmissionDto.donationStageDto.donResForCurCenNotTreatNum}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${arSuperDataSubmissionDtoVersion.donationStageDto.donResForCurCenNotTreatNum}"/>
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field width="4" value="Type of Research for Which Gamete(s) was Donated"  />
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForResearchHescr ==1 }">Human Embryonic Stem Cell Research<br></c:if>
                            <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForResearchRrar ==1 }">Research Related to Assisted Reproduction<br></c:if>
                            <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForResearchOther ==1 }">Other Type of Research</c:if>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:if test="${ arSuperDataSubmissionDtoVersion.donationStageDto.donatedForResearchHescr ==1 }">Human Embryonic Stem Cell Research<br></c:if>
                            <c:if test="${ arSuperDataSubmissionDtoVersion.donationStageDto.donatedForResearchRrar ==1 }">Research Related to Assisted Reproduction<br></c:if>
                            <c:if test="${ arSuperDataSubmissionDtoVersion.donationStageDto.donatedForResearchOther ==1 }">Other Type of Research</c:if>
                        </iais:value>
                    </iais:row>
                    <div id="donatedForResearchOtherDisplay" <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForResearchOther !=1 && arSuperDataSubmissionDtoVersion.donationStageDto.donatedForResearchOther !=1 }">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="4" value="Please Indicate the Other Type of Research" />
                            <iais:value width="4" cssClass="col-md-4" display="true">
                                <c:out value="${arSuperDataSubmissionDto.donationStageDto.donatedForResearchOtherType}"/>
                            </iais:value>
                            <iais:value width="4" cssClass="col-md-4" display="true">
                                <c:out value="${arSuperDataSubmissionDtoVersion.donationStageDto.donatedForResearchOtherType}"/>
                            </iais:value>
                        </iais:row>
                    </div>
                </div>

                <div id="trainingDisplay" <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForTraining !=1 &&  arSuperDataSubmissionDtoVersion.donationStageDto.donatedForTraining !=1 }">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="4" value="No. Used for Training" mandatory="false"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${arSuperDataSubmissionDto.donationStageDto.trainingNum}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${arSuperDataSubmissionDtoVersion.donationStageDto.trainingNum}"/>
                        </iais:value>
                    </iais:row>
                </div>

                <iais:row>
                    <iais:field width="4" value="Total No. Donated" />
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <div >${arSuperDataSubmissionDto.donationStageDto.totalNum}</div>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <div >${arSuperDataSubmissionDtoVersion.donationStageDto.totalNum}</div>
                    </iais:value>
                </iais:row>

            </div>
        </div>
    </div>
</div>