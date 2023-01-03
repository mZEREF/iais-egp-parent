<div class="panel panel-default">
    <div class="panel-heading ">
        <h4 class="panel-title" >
            <a href="#viewArDonorSampleDetails" data-toggle="collapse" >
                Donor Sample
            </a>
        </h4>
    </div>
    <div id="viewArDonorSampleDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <c:set var="donorSampleDto" value="${arSuperDataSubmissionDto.donorSampleDto}"/>
            <div class="panel-main-content form-horizontal">
                <iais:row>
                    <iais:field width="6" value="Is the sample donated from overseas or locally? " cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6" display="true">
                        <c:if test="${donorSampleDto.localOrOversea}">Local</c:if>
                        <c:if test="${not donorSampleDto.localOrOversea}">Overseas</c:if>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <label class="col-xs-4 col-md-4 control-label">Type of Sample
                        <c:if test="${arSuperDataSubmissionDto.donationStageDto.donatedType eq 'DONTY004'}">
                            <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"
                               title="<span style='font-size: 1.5rem;'>${MessageUtil.getMessageDesc("DS_MSG013")}</span>" style="z-index: 10"
                               data-original-title="">i</a>
                        </c:if>
                    </label>
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

                    <iais:row >
                        <iais:field width="6" value="Age of Female Donor at the Point of Donation" cssClass="col-md-6" info = "${donorSampleDto.ageErrorMsg}"/>
                        <label class="col-xs-2 col-md-3 control-label">Donor's Age</label>
                        <label class="col-xs-2 col-md-3 control-label">Available</label>
                    </iais:row>
                    <c:choose>
                        <c:when test="${donorSampleDto.donorSampleAgeDtos != null}">
                            <c:forEach items="${donorSampleDto.donorSampleAgeDtos}" var="donorSampleAgeDto"  begin="0" varStatus="idxStatus">
                                <iais:row id = "donorAge0">
                                    <label class="col-xs-5 col-md-6 control-label"></label>
                                    <iais:value width="3" cssClass="col-md-3"  display="true">
                                        <c:out value="${donorSampleAgeDto.age}"/>
                                    </iais:value>
                                    <iais:value width="3" cssClass="col-md-3"  display="true">
                                        <input type="checkbox" name ="ageCheckName" value = "${donorSampleAgeDto.id}" disabled ="true"
                                        <c:choose>
                                        <c:when test="${donorSampleAgeDto.available}">
                                               checked
                                        </c:when>
                                        </c:choose>
                                        >
                                    </iais:value>
                                </iais:row>
                            </c:forEach>
                        </c:when>
                    </c:choose>

                    <c:forEach items="${donorSampleDto.ages}" var="age"  begin="0" varStatus="index">
                        <iais:row>
                            <label class="col-xs-5 col-md-3 control-label"></label>
                            <iais:value width="3" cssClass="col-md-3"  display="true">
                                <c:out value="${age}"/>
                            </iais:value>
                            <iais:value width="3" cssClass="col-md-3"  display="true">
                                <input type="checkbox" name ="ageCheckName" value = "" disabled ="true" checked ="">
                                </input>
                            </iais:value>
                        </iais:row>
                    </c:forEach>
                </div>



                <div style="${donorSampleDto.sampleType eq 'DONTY005' or donorSampleDto.sampleType eq 'DONTY004' or donorSampleDto.sampleType eq 'DONTY003'?'':'display: none'}">
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

                    <iais:row >
                        <iais:field width="6" value="Age of Male Donor at the Point of Donation" cssClass="col-md-6" info = "${donorSampleDto.ageErrorMsg}"/>
                        <label class="col-xs-2 col-md-3 control-label">Donor's Age</label>
                        <label class="col-xs-2 col-md-3 control-label">Available</label>
                    </iais:row>
                    <c:choose>
                        <c:when test="${donorSampleDto.maleDonorSampleAgeDtos != null}">
                            <c:forEach items="${donorSampleDto.maleDonorSampleAgeDtos}" var="donorSampleAgeDto"  begin="0" varStatus="idxStatus">
                                <iais:row id = "donorAge0">
                                    <label class="col-xs-5 col-md-6 control-label"></label>
                                    <iais:value width="3" cssClass="col-md-3"  display="true">
                                        <c:out value="${donorSampleAgeDto.age}"/>
                                    </iais:value>
                                    <iais:value width="3" cssClass="col-md-3"  display="true">
                                        <input type="checkbox" name ="ageCheckName" value = "${donorSampleAgeDto.id}" disabled ="true"
                                        <c:choose>
                                        <c:when test="${donorSampleAgeDto.available}">
                                               checked
                                        </c:when>
                                        </c:choose>
                                        >
                                    </iais:value>
                                </iais:row>
                            </c:forEach>
                        </c:when>
                    </c:choose>
                    <c:forEach items="${donorSampleDto.ages}" var="age"  begin="0" varStatus="index">
                        <iais:row>
                            <label class="col-xs-5 col-md-3 control-label"></label>
                            <iais:value width="3" cssClass="col-md-3"  display="true">
                                <c:out value="${age}"/>
                            </iais:value>
                            <iais:value width="3" cssClass="col-md-3"  display="true">
                                <input type="checkbox" name ="ageCheckName" value = "" disabled ="true" checked ="">
                                </input>
                            </iais:value>
                        </iais:row>
                    </c:forEach>
                </div>

                <iais:row>
                    <iais:field width="6" value="Which Institution was the Sample Donated From? " cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6" display="true"
                                style="${donorSampleDto.localOrOversea?'':'display: none;'}">
                        <iais:code code="${donorSampleDto.sampleFromHciCode}"/>
                    </iais:value>
                    <iais:value width="6" cssClass="col-md-6" display="true"
                                style="${donorSampleDto.localOrOversea?'display: none;':''}">
                        <c:out value="${donorSampleDto.sampleFromOthers}"/>
                    </iais:value>
                </iais:row>

                <iais:row style="${donorSampleDto.sampleFromHciCode eq 'AR_SC_001'?'':'display: none;'}">
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
                        <c:if test="${donorSampleDto.donatedForResearch}">Research</c:if>
                        <c:if test="${donorSampleDto.donatedForTraining}">Training</c:if>
                        <c:if test="${donorSampleDto.donatedForTreatment}">Treatment</c:if>
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
                            <c:if test="${donorSampleDto.donatedForResearchHescr}">Human Embryonic Stem Cell Research</c:if>
                            <c:if test="${donorSampleDto.donatedForResearchRrar}">Research Related to Assisted Reproduction</c:if>
                            <c:if test="${donorSampleDto.donatedForResearchOther}">Other Type of Research</c:if>
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field width="6" value="Please indicate the Other Type of Research" cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            <c:out value="${donorSampleDto.donatedForResearchOtherType}"/>
                        </iais:value>
                    </iais:row>
                </div>

                <iais:row style="${donorSampleDto.donatedForTraining?'':'display: none;'}">
                    <iais:field width="6" value="No. Donated for Training " cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6" display="true">
                        <iais:code code="${donorSampleDto.trainingNum}"/>
                    </iais:value>
                </iais:row>

                <div style="${donorSampleDto.donatedForTreatment?'':'display: none;'}">
                    <iais:row>
                        <iais:field width="6" value="Is the sample from a directed donation?" cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            <c:if test="${donorSampleDto.directedDonation}">Yes</c:if>
                            <c:if test="${not donorSampleDto.directedDonation}">No</c:if>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="No. Donated for Treatment " cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            <iais:code code="${donorSampleDto.treatNum}"/>
                        </iais:value>
                    </iais:row>
                </div>
            </div>
        </div>
    </div>
</div>


