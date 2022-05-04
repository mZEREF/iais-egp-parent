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
                    <label class="col-xs-4 col-md-4 control-label">What was Donated?
                        <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"
                           title="<span style='font-size: 1.5rem;'>${MessageUtil.getMessageDesc("DS_MSG013")}</span>"
                           style="z-index: 10"
                           data-original-title="">i</a>
                    </label>
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
            </div>
        </div>
    </div>
</div>