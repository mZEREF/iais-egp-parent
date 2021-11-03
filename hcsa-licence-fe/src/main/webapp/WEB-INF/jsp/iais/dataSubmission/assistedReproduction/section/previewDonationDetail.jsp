<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="panel panel-default">
    <div class="panel-heading completed ">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#">
                Donation
            </a>
        </h4>
    </div>
    <div id="donationDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <iais:row>
                    <iais:field width="6" value="What was Donated?" mandatory="false"/>
                    <iais:value width="6" display="true">
                        <iais:code code="${arSuperDataSubmissionDto.donationStageDto.donatedType}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. Donated to Current AR Centre for treatment" mandatory="false"/>
                    <iais:value width="6" display="true">
                        <c:out value="${arSuperDataSubmissionDto.donationStageDto.curCenDonatedNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. Donated to Other AR Centre for treatment" mandatory="false"/>
                    <iais:value width="6" display="true">
                        <c:out value="${arSuperDataSubmissionDto.donationStageDto.otherCenDonatedNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Which AR Centre was Gamete(s)/Embryo(s) Donated to?" mandatory="false"/>
                    <iais:value width="6" display="true">
                        <c:out value="${arSuperDataSubmissionDto.donationStageDto.isCurCenDonated}"/>
                    </iais:value>
                </iais:row>
                <div id="otherDonatedCenDisplay" <c:if test="${arSuperDataSubmissionDto.donationStageDto.isCurCenDonated!='Others'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="Other Centre where Embryos were Biospied At" />
                        <iais:value width="6" display="true">
                            <c:out value="${arSuperDataSubmissionDto.donationStageDto.otherDonatedCen}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="directedDonorIdDisplay" <c:if test="${arSuperDataSubmissionDto.donationStageDto.isCurCenDonated=='Others'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="ID of Directed Donor (if applicable)"/>
                        <iais:value width="6" display="true">
                            <c:out value="${arSuperDataSubmissionDto.donationStageDto.directedDonorId}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <iais:row>
                    <iais:field width="6" value="No. Donated for Research (Usable for Treatment)" />
                    <iais:value width="6" display="true">
                        <c:out value="${arSuperDataSubmissionDto.donationStageDto.resDonarNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. Donated to current AR centre for Research (Not Usable for Treatment) " />
                    <iais:value width="6" display="true">
                        <c:out value="${arSuperDataSubmissionDto.donationStageDto.curCenResDonatedNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Type of Research for Which Donated"  />
                    <iais:value width="6" display="true">
                        <c:if test="${ arSuperDataSubmissionDto.donationStageDto.isCurCenResTypeHescr ==1 }">Human Embryonic Stem Cell Research<br></c:if>
                        <c:if test="${ arSuperDataSubmissionDto.donationStageDto.isCurCenResTypeRrar ==1 }">Research Related to Assisted Reproduction<br></c:if>
                        <c:if test="${ arSuperDataSubmissionDto.donationStageDto.isCurCenResTypeOther ==1 }">Other Type of Research</c:if>
                    </iais:value>
                </iais:row>
                <div id="curCenResTypeOtherDisplay" <c:if test="${arSuperDataSubmissionDto.donationStageDto.isCurCenResTypeOther!=1}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="Other Type of Research Donated for" />
                        <iais:value width="6" display="true">
                            <c:out value="${arSuperDataSubmissionDto.donationStageDto.curCenOtherResType}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <iais:row>
                    <iais:field width="6" value="No. Donated to Other Centres / Institutions for Research" />
                    <iais:value width="6" display="true">
                        <c:out value="${arSuperDataSubmissionDto.donationStageDto.otherCenResDonarNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Other AR Centre / Institution Sent to" id="isInsSentToCurField" />
                    <iais:value width="6" display="true">
                        <c:out value="${arSuperDataSubmissionDto.donationStageDto.isInsSentToCur}"/>
                    </iais:value>
                </iais:row>
                <div id="insSentToOtherCenDisplay" <c:if test="${arSuperDataSubmissionDto.donationStageDto.isInsSentToCur!='Others'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="Other Type of Research Donated for" />
                        <iais:value width="6" display="true">
                            <c:out value="${arSuperDataSubmissionDto.donationStageDto.insSentToOtherCen}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <iais:row>
                    <iais:field width="6" value="No. Used for Training"/>
                    <iais:value width="6"  display="true">
                        <c:out value="${arSuperDataSubmissionDto.donationStageDto.trainingNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Reason for Donation" />
                    <iais:value width="6" display="true">
                        <iais:code code="${arSuperDataSubmissionDto.donationStageDto.donationReason}"/>
                    </iais:value>
                </iais:row>
                <div id="otherDonationReasonDisplay" <c:if test="${arSuperDataSubmissionDto.donationStageDto.donationReason!='DONRES004'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="Other Type of Research Donated for" />
                        <iais:value width="6" display="true">
                            <c:out value="${arSuperDataSubmissionDto.donationStageDto.otherDonationReason}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <iais:row>
                    <iais:field width="6" value="Total No. Donated" />
                    <iais:value width="6" display="true">
                        <div id="totalNum" name="totalNum">${arSuperDataSubmissionDto.donationStageDto.totalNum}</div>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>