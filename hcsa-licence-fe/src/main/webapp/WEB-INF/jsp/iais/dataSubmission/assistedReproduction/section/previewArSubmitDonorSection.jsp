<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
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
                    <iais:field width="5" value="Is Sample from a Directed Donation?" />
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="${donorSampleDto.directedDonation ? 'Yes' : 'No'}"/>
                    </iais:value>
                </iais:row>
                <div id="directedDonationYes" style="${!donorSampleDto.directedDonation ? 'display: none;' : ''}">
                    <iais:row id="idNoRow" >
                        <iais:field width="5" value="Donor's ID Type" />
                        <iais:value width="7" cssClass="col-md-7"  display="true">
                            <iais:code code="${donorSampleDto.idType}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row  >
                        <iais:field width="5" value="Donor's ID No." />
                        <iais:value width="7" cssClass="col-md-7"  display="true">
                            <c:out value="${donorSampleDto.idNumber}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row  >
                        <iais:field width="5" value="Donor's Name" />
                        <iais:value width="7" cssClass="col-md-7"  display="true">
                            <c:out value="${donorSampleDto.donorName}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Donor relation to patient" />
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${donorSampleDto.donorRelation == 'F' ? 'Friend' : 'Relative'}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="directedDonationNo" style="${donorSampleDto.directedDonation ? 'display: none;' : ''}">
                    <iais:row id="sampleType" >
                        <iais:field width="5" value="Sample Type" />
                        <iais:value width="7" cssClass="col-md-7"  display="true">
                            <iais:code code="${donorSampleDto.sampleType}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row >
                        <iais:field width="5" value="Is Donor's Identity Known?" />
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <iais:code code="${donorSampleDto.donorIdentityKnown}"/>
                        </iais:value>
                    </iais:row>
                    <div id="donorSampleCodeRow" style="${donorSampleDto.donorIdentityKnown =='DIK001' ? 'display: none;' : ''}">
                        <iais:row   >
                            <iais:field width="5" value="Donor Sample Code" />
                            <iais:value width="7" cssClass="col-md-7"  display="true">
                                <c:out value="${donorSampleDto.donorSampleCode}"/>
                            </iais:value>
                        </iais:row>

                    </div>

                    <div id ="donorDetail" style="${donorSampleDto.donorIdentityKnown == 'DIK001'? '' : 'display: none;'}">
                        <iais:row >
                            <iais:field width="5" value="Donor's ID Type" />
                            <iais:value width="7" cssClass="col-md-7"  display="true">
                                <iais:code code="${donorSampleDto.knownIdType}"/>
                            </iais:value>
                        </iais:row>
                        <iais:row  >
                            <iais:field width="5" value="Donor's ID No." />
                            <iais:value width="7" cssClass="col-md-7"  display="true">
                                <c:out value="${donorSampleDto.knownIdNumber}"/>
                            </iais:value>
                        </iais:row>
                        <iais:row  >
                            <iais:field width="5" value="Donor's Name" />
                            <iais:value width="7" cssClass="col-md-7"  display="true">
                                <c:out value="${donorSampleDto.knownDonorName}"/>
                            </iais:value>
                        </iais:row>
                    </div>
                    <iais:row>
                        <iais:field width="5" value="Name of Bank / AR Centre where Sample is from" />
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <iais:optionText value="${donorSampleDto.sampleFromHciCode}" selectionOptions="SampleFromHciCode"/>
                        </iais:value>
                    </iais:row>
                    <iais:row id ="sampleFromOthers" style="${donorSampleDto.sampleFromHciCode =='AR_SC_001' ? '' : 'display: none;'}">
                        <label class="col-xs-5 col-md-4 control-label"></label>
                        <iais:value width="7" cssClass="col-md-7"  display="true">
                            <c:out value="${donorSampleDto.sampleFromOthers}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <c:choose>
                    <c:when test="${donorSampleDto.donorSampleAgeDtos != null}">
                        <c:forEach items="${donorSampleDto.donorSampleAgeDtos}" var="donorSampleAgeDto"  begin="0" varStatus="idxStatus">
                            <iais:row id = "donorAge0">
                                <label class="col-xs-5 col-md-4 control-label">
                                    <c:if test="${idxStatus.first==true}">
                                        Donor's Age when Sample was Collected
                                    </c:if>
                                </label>
                                <iais:value width="7" cssClass="col-md-7"  display="true">
                                    <c:out value="${donorSampleAgeDto.age}"/>
                                </iais:value>
                            </iais:row>
                        </c:forEach>
                    </c:when>
                </c:choose>

               <c:forEach items="${donorSampleDto.ages}" var="age"  begin="0" varStatus="index">
                <iais:row>
                    <label class="col-xs-5 col-md-4 control-label">
                        <c:if test="${index.first==true && donorSampleDto.donorSampleAgeDtos == null}">
                            Donor's Age when Sample was Collected
                            <span class="mandatory">*</span>
                        </c:if>
                    </label>
                    <iais:value width="7" cssClass="col-md-7"  display="true">
                        <c:out value="${age}"/>
                    </iais:value>
                </iais:row>
               </c:forEach>
            </div>
        </div>
    </div>
</div>


