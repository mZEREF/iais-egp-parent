<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="collapsed" href="#donationDetails" data-toggle="collapse">
                Donation
            </a>
        </h4>
    </div>
    <div id="donationDetails" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <h3>
                    <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                </h3>
                <iais:row>
                    <iais:field width="5" value="What was Donated?" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <iais:code code="${arSuperDataSubmissionDto.donationStageDto.donatedType}"/>
                    </iais:value>
                </iais:row>
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
                    <iais:field width="5" value="Donated For"  />
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
                        <iais:field width="5" value="Type of Research for Which Donated"  />
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForResearchHescr ==1 }">Human Embryonic Stem Cell Research<br></c:if>
                            <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForResearchRrar ==1 }">Research Related to Assisted Reproduction<br></c:if>
                            <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForResearchOther ==1 }">Other Type of Research</c:if>
                        </iais:value>
                    </iais:row>
                    <div id="donatedForResearchOtherDisplay" <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForResearchOther !=1 }">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="5" value="Other Type of Research Donated for" />
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
                <%@include file="../common/patientInventoryTable.jsp" %>
            </div>
        </div>
    </div>
</div>