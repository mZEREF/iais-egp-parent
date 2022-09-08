<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto" %>
<%@ page import="com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorSampleDto" %>
<%--@elvariable id="headingSign" type="java.lang.String"--%>
<%
    ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
    DonorSampleDto donorSampleDto = arSuperDataSubmissionDto.getDonorSampleDto();
%>
<c:set var="donorSampleDto" value="${arSuperDataSubmissionDto.donorSampleDto}"/>
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="collapsed" href="#cycleDetails" data-toggle="collapse">
                Donor Sample
            </a>
        </h4>
    </div>
    <div id="cycleDetails" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal ">
                <iais:row>
                    <iais:field width="6" value="Is the sample donated from overseas or locally? " cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6" display="true">
                        <c:if test="${donorSampleDto.directedDonation}">Yes</c:if>
                        <c:if test="${not donorSampleDto.directedDonation}">No</c:if>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="6" value="Type of Sample " cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6" display="true">
                        <iais:code code="${donorSampleDto.sampleType}"/>
                    </iais:value>
                </iais:row>

                <div style="${donorSampleDto.sampleType eq 'DONTY001' or donorSampleDto.sampleType eq 'DONTY002' or donorSampleDto.sampleType eq 'DONTY003'?'':'display: none'}">
                    <iais:row>
                        <iais:field width="6" value="Is the Female Donor's Identity Known? " cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            ${donorSampleDto.donorIdentityKnown eq 'DIK002'?'No':'Yes'}
                        </iais:value>
                    </iais:row>

                    <div style="${donorSampleDto.donorIdentityKnown eq 'DIK001'?'':'display: none'}">
                        <iais:row>
                            <iais:field width="6" value="Does the Female Donor have a NRIC/FIN number? "
                                        cssClass="col-md-6"/>
                            <iais:value width="6" cssClass="col-md-6" display="true">
                                ${donorSampleDto.idType eq 'DTV_IT003'?'No':'Yes'}
                            </iais:value>
                        </iais:row>

                        <iais:row>
                            <c:choose>
                                <c:when test="${donorSampleDto.idType eq 'DTV_IT003'}">
                                    <iais:field width="6" value="Female Donor's Passport Number " cssClass="col-md-6"/>
                                </c:when>
                                <c:otherwise>
                                    <iais:field width="6" value="Female Donor's NRIC/FIN Number " cssClass="col-md-6"/>
                                </c:otherwise>
                            </c:choose>
                            <iais:value width="6" cssClass="col-md-6" display="true">
                                <c:out value="${donorSampleDto.idNumber}"/>
                            </iais:value>
                        </iais:row>
                    </div>

                    <iais:row>
                        <iais:field width="6" value="Female Donor Sample Code " cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            <c:out value="${donorSampleDto.donorSampleCode}"/>
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <label class="col-xs-6 col-md-4 control-label col-md-6">Age of Female Donor at the Point of
                            Donation
                            <a class="btn-tooltip styleguide-tooltip" href="javascript:void(0);" data-toggle="tooltip"
                               data-html="true" title=""
                               data-original-title="<p>Donor's age when oocyte(s) or embryo(s) are collected is outside the range of 21 to 35 yrs old.</p>"
                               style="${donorSampleDto.donorSampleAge<21 or donorSampleDto.donorSampleAge>35 ?'':'display: none;'}">i</a>
                        </label>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            <c:out value="${donorSampleDto.donorSampleAge}"/>
                        </iais:value>
                    </iais:row>
                </div>

                <div style="${donorSampleDto.sampleType eq 'DONTY004' or donorSampleDto.sampleType eq 'DONTY003'?'':'display: none'}">
                    <iais:row>
                        <iais:field width="6" value="Is the Male Donor's Identity Known? " cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            ${donorSampleDto.maleDonorIdentityKnow eq false?'No':'Yes'}
                        </iais:value>
                    </iais:row>

                    <div style="${donorSampleDto.maleDonorIdentityKnow eq true?'':'display: none'}">
                        <iais:row>
                            <iais:field width="6" value="Does the Male Donor have a NRIC/FIN number? "
                                        cssClass="col-md-6"/>
                            <iais:value width="6" cssClass="col-md-6" display="true">
                                ${donorSampleDto.idTypeMale eq 'DTV_IT003'?'No':'Yes'}
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
                            <iais:value width="6" cssClass="col-md-6" display="true">
                                <c:out value="${donorSampleDto.idNumberMale}"/>
                            </iais:value>
                        </iais:row>
                    </div>

                    <iais:row>
                        <iais:field width="6" value="Male Donor Sample Code " cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <c:out value="${donorSampleDto.maleDonorSampleCode}"/>
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <label class="col-xs-6 control-label col-md-6">Age of Male Donor at the Point of Donation
                            <a class="btn-tooltip styleguide-tooltip" href="javascript:void(0);" data-toggle="tooltip"
                               data-html="true" title=""
                               data-original-title="<p>Donor's age when sperm(s) are collected is outside the range of 21 to 40 yrs old.</p>"
                               style="${donorSampleDto.maleDonorSampleAge<21 or donorSampleDto.maleDonorSampleAge>40 ?'':'display: none;'}">i</a>
                        </label>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            <c:out value="${donorSampleDto.maleDonorSampleAge}"/>
                        </iais:value>
                    </iais:row>
                </div>

                <iais:row>
                    <iais:field width="6" value="Which Institution was the Sample Donated From? " cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6" display="true"
                                style="${donorSampleDto.directedDonation?'':'dispaly: none;'}">
                        <iais:code code="${donorSampleDto.sampleFromHciCode}"/>
                    </iais:value>
                    <iais:value width="6" cssClass="col-md-6" display="true"
                                style="${donorSampleDto.directedDonation?'dispaly: none;':''}">
                        <c:out value="${donorSampleDto.sampleFromOthers}"/>
                    </iais:value>
                </iais:row>

                <iais:row style="${donorSampleDto.sampleFromHciCode eq 'AR_SC_001'?'':'dispaly: none;'}">
                    <iais:field width="6" value="If 'Others', Please Specify the Name of the Institution "
                                cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6" display="true">
                        <c:out value="${donorSampleDto.sampleFromOthers}"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="6" value="Reason(s) for Donation " cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6" display="true">
                        <iais:code code="${donorSampleDto.donationReason}"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="6" value="If 'Others', please specify the reason for donation "
                                cssClass="col-md-6"
                                style="${donorSampleDto.donationReason eq 'DONRES004'?'':'display: none;'}"/>
                    <iais:value width="6" cssClass="col-md-6" display="true">
                        <iais:code code="${donorSampleDto.otherDonationReason}"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="6" value="Purpose of Donation" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6" display="true">
                        <c:if test="${donorSampleDto.donatedForResearch}"><p>Research</p></c:if>
                        <c:if test="${donorSampleDto.donatedForTraining}"><p>Training</p></c:if>
                        <c:if test="${donorSampleDto.donatedForTreatment}"><p>Treatment</p></c:if>
                    </iais:value>
                </iais:row>

                <div style="${donorSampleDto.donatedForResearch?'':'display: none;'}">
                    <iais:row>
                        <iais:field width="6" value="No. Donated for Research (Usable for Treatment) "
                                    cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            <c:out value="${donorSampleDto.donResForTreatNum}"/>
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field width="6" value="No. Donated for Research (Not Usable for Treatment) "
                                    cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            <c:out value="${donorSampleDto.donResForCurCenNotTreatNum}"/>
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field width="6" value="Type of Research for Which Gamete(s) Was Donated"
                                    cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            <c:if test="${donorSampleDto.donatedForResearchHescr}"><p>Human Embryonic Stem Cell
                                Research</p></c:if>
                            <c:if test="${donorSampleDto.donatedForResearchRrar}"><p>Research Related to Assisted
                                Reproduction</p></c:if>
                            <c:if test="${donorSampleDto.donatedForResearchOther}"><p>Other Type of Research</p></c:if>
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field width="6" value="Please indicate the Other Type of Research" cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            <c:out value="${donorSampleDto.donatedForResearchOtherType}"/>
                        </iais:value>
                    </iais:row>
                </div>

                <iais:row>
                    <iais:field width="6" value="No. Donated for Training " cssClass="col-md-6"
                                style="${donorSampleDto.donatedForTraining?'':'display: none;'}"/>
                    <iais:value width="6" cssClass="col-md-6" display="true">
                        <iais:code code="${donorSampleDto.trainingNum}"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="6" value="No. Donated for Treatment " cssClass="col-md-6"
                                style="${donorSampleDto.donatedForTreatment?'':'display: none;'}"/>
                    <iais:value width="6" cssClass="col-md-6" display="true">
                        <iais:code code="${donorSampleDto.treatNum}"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="6" value="Total No. Donated " cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6" display="true">
                        <p id="donatedNum"><%=donorSampleDto.calTotalNum()%>
                        </p>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>