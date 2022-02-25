<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="panel panel-default">
    <div class="panel-heading  ">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#donationDetails">
                Donation
            </a>
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
                    <iais:field width="4" cssClass="col-md-4"  value="" />
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="Current Version"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <select id="oldDsSelect" name="oldDsSelect">
                            <c:forEach items="${arSuperDataSubmissionDto.oldArSuperDataSubmissionDto}" var="oldDs" varStatus="index">
                                <option   <c:if test="${oldDs.dataSubmissionDto.id == arSuperDataSubmissionDtoVersion.dataSubmissionDto.id}">checked</c:if> value ="${oldDs.dataSubmissionDto.id}">V ${oldDs.dataSubmissionDto.version}</option>
                            </c:forEach>
                        </select>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="What was Donated?" mandatory="false"/>
                    <iais:value width="4" display="true">
                        <iais:code code="${arSuperDataSubmissionDto.donationStageDto.donatedType}"/>
                    </iais:value>
                    <iais:value width="4" display="true">
                        <iais:code code="${arSuperDataSubmissionDtoVersion.donationStageDto.donatedType}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="Which AR Centre was Gamete(s)/Embryo(s) Donated to?" id="donatedCentreField" mandatory="false"/>
                    <iais:value width="4" display="true">
                        <c:out value="${arSuperDataSubmissionDto.donationStageDto.donatedCentreAddress}"/>
                    </iais:value>
                    <iais:value width="4" display="true">
                        <c:out value="${arSuperDataSubmissionDtoVersion.donationStageDto.donatedCentreAddress}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="Reason for Donation" mandatory="false"/>
                    <iais:value width="4" display="true">
                        <iais:code code="${arSuperDataSubmissionDto.donationStageDto.donationReason}"/>
                    </iais:value>
                    <iais:value width="4" display="true">
                        <iais:code code="${arSuperDataSubmissionDtoVersion.donationStageDto.donationReason}"/>
                    </iais:value>
                </iais:row>
                <div id="otherDonationReasonDisplay" <c:if test="${arSuperDataSubmissionDto.donationStageDto.donationReason!='DONRES004' && arSuperDataSubmissionDtoVersion.donationStageDto.donationReason!='DONRES004'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="4" value="Other Reason for Donation" mandatory="false"/>
                        <iais:value width="4" display="true">
                            <c:out value="${arSuperDataSubmissionDto.donationStageDto.otherDonationReason}"/>
                        </iais:value>
                        <iais:value width="4" display="true">
                            <c:out value="${arSuperDataSubmissionDtoVersion.donationStageDto.otherDonationReason}"/>
                        </iais:value>
                    </iais:row>
                </div>

                <iais:row>
                    <iais:field width="4" value="Donated For"  />
                    <iais:value width="4" display="true">
                        <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForResearch ==1 }">Research<br></c:if>
                        <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForTraining ==1 }">Training<br></c:if>
                        <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForTreatment ==1 }">Treatment</c:if>
                    </iais:value>
                    <iais:value width="4" display="true">
                        <c:if test="${ arSuperDataSubmissionDtoVersion.donationStageDto.donatedForResearch ==1 }">Research<br></c:if>
                        <c:if test="${ arSuperDataSubmissionDtoVersion.donationStageDto.donatedForTraining ==1 }">Training<br></c:if>
                        <c:if test="${ arSuperDataSubmissionDtoVersion.donationStageDto.donatedForTreatment ==1 }">Treatment</c:if>
                    </iais:value>
                </iais:row>

                <div id="researchDisplay" <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForResearch !=1 && arSuperDataSubmissionDtoVersion.donationStageDto.donatedForResearch !=1 }">style="display: none"</c:if>>

                    <iais:row>
                        <iais:field width="4" value="No. Donated for Research (Usable for Treatment)" mandatory="false"/>
                        <iais:value width="4" display="true">
                            <c:out value="${arSuperDataSubmissionDto.donationStageDto.donResForTreatNum}"/>
                        </iais:value>
                        <iais:value width="4" display="true">
                            <c:out value="${arSuperDataSubmissionDtoVersion.donationStageDto.donResForTreatNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="4" value="No. Donated to current AR centre for Research (Not Usable for Treatment)" mandatory="false"/>
                        <iais:value width="4" display="true">
                            <c:out value="${arSuperDataSubmissionDto.donationStageDto.donResForCurCenNotTreatNum}"/>
                        </iais:value>
                        <iais:value width="4" display="true">
                            <c:out value="${arSuperDataSubmissionDtoVersion.donationStageDto.donResForCurCenNotTreatNum}"/>
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field width="4" value="Type of Research for Which Donated"  />
                        <iais:value width="4" display="true">
                            <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForResearchHescr ==1 }">Human Embryonic Stem Cell Research<br></c:if>
                            <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForResearchRrar ==1 }">Research Related to Assisted Reproduction<br></c:if>
                            <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForResearchOther ==1 }">Other Type of Research</c:if>
                        </iais:value>
                        <iais:value width="4" display="true">
                            <c:if test="${ arSuperDataSubmissionDtoVersion.donationStageDto.donatedForResearchHescr ==1 }">Human Embryonic Stem Cell Research<br></c:if>
                            <c:if test="${ arSuperDataSubmissionDtoVersion.donationStageDto.donatedForResearchRrar ==1 }">Research Related to Assisted Reproduction<br></c:if>
                            <c:if test="${ arSuperDataSubmissionDtoVersion.donationStageDto.donatedForResearchOther ==1 }">Other Type of Research</c:if>
                        </iais:value>
                    </iais:row>
                    <div id="donatedForResearchOtherDisplay" <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForResearchOther !=1 && arSuperDataSubmissionDtoVersion.donationStageDto.donatedForResearchOther !=1 }">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="4" value="Other Type of Research Donated for" />
                            <iais:value width="4" display="true">
                                <c:out value="${arSuperDataSubmissionDto.donationStageDto.donatedForResearchOtherType}"/>
                            </iais:value>
                            <iais:value width="4" display="true">
                                <c:out value="${arSuperDataSubmissionDtoVersion.donationStageDto.donatedForResearchOtherType}"/>
                            </iais:value>
                        </iais:row>
                    </div>
                </div>

                <div id="trainingDisplay" <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForTraining !=1 &&  arSuperDataSubmissionDtoVersion.donationStageDto.donatedForTraining !=1 }">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="4" value="No. Used for Training" mandatory="false"/>
                        <iais:value width="4" display="true">
                            <c:out value="${arSuperDataSubmissionDto.donationStageDto.trainingNum}"/>
                        </iais:value>
                        <iais:value width="4" display="true">
                            <c:out value="${arSuperDataSubmissionDtoVersion.donationStageDto.trainingNum}"/>
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field width="4" value="No. Donated For Treatment" mandatory="false"/>
                        <iais:value width="4" display="true">
                            <c:out value="${arSuperDataSubmissionDto.donationStageDto.treatNum}"/>
                        </iais:value>
                        <iais:value width="4" display="true">
                            <c:out value="${arSuperDataSubmissionDtoVersion.donationStageDto.treatNum}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <iais:row>
                    <iais:field width="4" value="Total No. Donated" />
                    <iais:value width="4" display="true">
                        <div >${arSuperDataSubmissionDto.donationStageDto.totalNum}</div>
                    </iais:value>
                    <iais:value width="4" display="true">
                        <div >${arSuperDataSubmissionDtoVersion.donationStageDto.totalNum}</div>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="ID of Donated Recipient" />
                    <iais:value width="4" display="true">
                        <c:out value="${arSuperDataSubmissionDto.donationStageDto.donatedRecipientNum}"/>
                    </iais:value>
                    <iais:value width="4" display="true">
                        <c:out value="${arSuperDataSubmissionDtoVersion.donationStageDto.donatedRecipientNum}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>