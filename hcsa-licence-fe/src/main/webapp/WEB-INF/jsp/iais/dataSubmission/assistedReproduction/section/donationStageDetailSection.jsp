<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/donationSection.js"></script>

<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                Donation
            </strong>
        </h4>
    </div>
    <div id="donationDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">

                <iais:row>
                    <iais:field width="6" value="What was Donated?" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:select cssClass="donatedType"  name="donatedType" firstOption="Please Select" options="donatedTypeSelectOption" value="${arSuperDataSubmissionDto.donationStageDto.donatedType}"></iais:select>
                        <span class="error-msg" name="iaisErrorMsg" id="error_donatedType"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. Donated to Current AR Centre for treatment" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <input type="text" maxlength="2"  id="curCenDonatedNum" name="curCenDonatedNum" value="${arSuperDataSubmissionDto.donationStageDto.curCenDonatedNum}" >
                        <span class="error-msg" name="iaisErrorMsg" id="error_curCenDonatedNum"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. Donated to Other AR Centre for treatment" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <input type="text" maxlength="2"   name="otherCenDonatedNum" id="otherCenDonatedNum" value="${arSuperDataSubmissionDto.donationStageDto.otherCenDonatedNum}" >
                        <span class="error-msg" name="iaisErrorMsg" id="error_otherCenDonatedNum"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Which AR Centre was Gamete(s)/Embryo(s) Donated to?" id="isCurCenDonatedField" mandatory="false"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <select name="isCurCenDonated" id="isCurCenDonated">
                            <option value="" <c:if test="${empty arSuperDataSubmissionDto.donationStageDto.isCurCenDonated}">selected="selected"</c:if>>Please Select</option>
                            <c:forEach items="${curCenDonatedSelectOption}" var="selectOption">
                                <option value="${selectOption.value}" <c:if test="${arSuperDataSubmissionDto.donationStageDto.isCurCenDonated ==selectOption.value}">selected="selected"</c:if>>${selectOption.text}</option>
                            </c:forEach>
                            <option value="Others" <c:if test="${arSuperDataSubmissionDto.donationStageDto.isCurCenDonated =='Others'}">selected="selected"</c:if>>Others</option>
                        </select>
                        <span class="error-msg" name="iaisErrorMsg" id="error_isCurCenDonated"></span>
                    </iais:value>
                </iais:row>
                <div id="otherDonatedCenDisplay" <c:if test="${arSuperDataSubmissionDto.donationStageDto.isCurCenDonated!='Others'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="Other Centre where Embryos were Biospied At" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <input type="text" maxlength="100"   name="otherDonatedCen" value="${arSuperDataSubmissionDto.donationStageDto.otherDonatedCen}" >
                            <span class="error-msg" name="iaisErrorMsg" id="error_otherDonatedCen"></span>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="directedDonorIdDisplay" <c:if test="${arSuperDataSubmissionDto.donationStageDto.isCurCenDonated=='Others'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="ID of Directed Donor (if applicable)" mandatory="false"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <input type="text" maxlength="20"   name="directedDonorId" value="${arSuperDataSubmissionDto.donationStageDto.directedDonorId}" >
                            <span class="error-msg" name="iaisErrorMsg" id="error_directedDonorId"></span>
                        </iais:value>
                    </iais:row>
                </div>

                <iais:row>
                    <iais:field width="6" value="No. Donated for Research (Usable for Treatment)" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <input type="text" maxlength="2"   name="resDonarNum" id="resDonarNum" value="${arSuperDataSubmissionDto.donationStageDto.resDonarNum}" >
                        <span class="error-msg" name="iaisErrorMsg" id="error_resDonarNum"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. Donated to current AR centre for Research (Not Usable for Treatment) " mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <input type="text" maxlength="2"  name="curCenResDonatedNum" id="curCenResDonatedNum" value="${arSuperDataSubmissionDto.donationStageDto.curCenResDonatedNum}" >
                        <span class="error-msg" name="iaisErrorMsg" id="error_curCenResDonatedNum"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" id="isCurCenResTypeField"  value="Type of Research for Which Donated" mandatory="false" />
                    <iais:value width="6" cssClass="col-md-6">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="checkbox"
                                   name="isCurCenResTypeHescr"
                                   id="isCurCenResTypeHescr"
                                   <c:if test="${ arSuperDataSubmissionDto.donationStageDto.isCurCenResTypeHescr ==1 }">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="isCurCenResTypeHescr"><span
                                    class="check-square"></span>Human Embryonic Stem Cell Research</label>
                        </div>
                    </iais:value>
                    <iais:value width="6" cssClass="col-md-6">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="checkbox"
                                   name="isCurCenResTypeRrar"
                                   id="isCurCenResTypeRrar"
                                   <c:if test="${ arSuperDataSubmissionDto.donationStageDto.isCurCenResTypeRrar ==1 }">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="isCurCenResTypeRrar"><span
                                    class="check-square"></span>Research Related to Assisted Reproduction</label>
                        </div>
                    </iais:value>
                    <iais:field width="6" value="" />
                    <iais:value width="6" cssClass="col-md-6">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="checkbox"
                                   name="isCurCenResTypeOther"
                                   id="isCurCenResTypeOther"
                                   <c:if test="${ arSuperDataSubmissionDto.donationStageDto.isCurCenResTypeOther ==1 }">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="isCurCenResTypeOther"><span
                                    class="check-square"></span>Other Type of Research</label>
                        </div>
                        <span class="error-msg" name="iaisErrorMsg" id="error_curCenResType"></span>
                    </iais:value>
                </iais:row>
                <div id="curCenResTypeOtherDisplay" <c:if test="${arSuperDataSubmissionDto.donationStageDto.isCurCenResTypeOther!=1}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="Other Type of Research Donated for" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <input type="text" maxlength="20"   name="curCenOtherResType" value="${arSuperDataSubmissionDto.donationStageDto.curCenOtherResType}" >
                            <span class="error-msg" name="iaisErrorMsg" id="error_curCenOtherResType"></span>
                        </iais:value>
                    </iais:row>
                </div>

                <iais:row>
                    <iais:field width="6" value="No. Donated to Other Centres / Institutions for Research" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <input type="text" maxlength="2"   name="otherCenResDonarNum" id="otherCenResDonarNum" value="${arSuperDataSubmissionDto.donationStageDto.otherCenResDonarNum}" >
                        <span class="error-msg" name="iaisErrorMsg" id="error_otherCenResDonarNum"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Other AR Centre / Institution Sent to" id="isInsSentToCurField" mandatory="false"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <select name="isInsSentToCur" id="isInsSentToCur">
                            <option value="" <c:if test="${empty arSuperDataSubmissionDto.donationStageDto.isInsSentToCur}">selected="selected"</c:if>>Please Select</option>
                            <c:forEach items="${insSentToCurSelectOption}" var="selectOption">
                                <option value="${selectOption.value}" <c:if test="${arSuperDataSubmissionDto.donationStageDto.isInsSentToCur ==selectOption.value}">selected="selected"</c:if>>${selectOption.text}</option>
                            </c:forEach>
                            <option value="Others" <c:if test="${arSuperDataSubmissionDto.donationStageDto.isInsSentToCur =='Others'}">selected="selected"</c:if>>Others</option>
                        </select>
                        <span class="error-msg" name="iaisErrorMsg" id="error_isInsSentToCur"></span>
                    </iais:value>
                </iais:row>
                <div id="insSentToOtherCenDisplay" <c:if test="${arSuperDataSubmissionDto.donationStageDto.isInsSentToCur!='Others'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="Other Type of Research Donated for" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <input type="text" maxlength="100"   name="insSentToOtherCen" value="${arSuperDataSubmissionDto.donationStageDto.insSentToOtherCen}" >
                            <span class="error-msg" name="iaisErrorMsg" id="error_insSentToOtherCen"></span>
                        </iais:value>
                    </iais:row>
                </div>


                <iais:row>
                    <iais:field width="6" value="No. Used for Training" mandatory="false"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <input type="text" maxlength="2"  id="trainingNum" name="trainingNum" value="${arSuperDataSubmissionDto.donationStageDto.trainingNum}" >
                        <span class="error-msg" name="iaisErrorMsg" id="error_trainingNum"></span>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="6" value="Reason for Donation" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:select cssClass="donationReason"  name="donationReason" firstOption="Please Select" options="donationReasonSelectOption" value="${arSuperDataSubmissionDto.donationStageDto.donationReason}"></iais:select>
                        <span class="error-msg" name="iaisErrorMsg" id="error_donationReason"></span>
                    </iais:value>
                </iais:row>
                <div id="otherDonationReasonDisplay" <c:if test="${arSuperDataSubmissionDto.donationStageDto.donationReason!='DONRES004'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="Other Type of Research Donated for" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <input type="text" maxlength="20"   name="otherDonationReason" value="${arSuperDataSubmissionDto.donationStageDto.otherDonationReason}" >
                            <span class="error-msg" name="iaisErrorMsg" id="error_otherDonationReason"></span>
                        </iais:value>
                    </iais:row>
                </div>

                <iais:row>
                    <iais:field width="6" value="Total No. Donated" mandatory="false"/>
                    <iais:value width="6" display="true">
                        <div id="totalNum" name="totalNum">${arSuperDataSubmissionDto.donationStageDto.totalNum}</div>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>
