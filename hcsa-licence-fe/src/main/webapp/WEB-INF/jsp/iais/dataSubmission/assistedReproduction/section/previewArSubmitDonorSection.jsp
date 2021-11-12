<c:set var="headingSign" value="completed"/>
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
                    <iais:field width="5" value="Is Sample from a Directed Donation?" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="${donorSampleDto.directedDonation ? 'Yes' : 'No'}"/>
                    </iais:value>
                </iais:row>
                <div id="directedDonationYes" style="${!donorSampleDto.directedDonation ? 'display: none;' : ''}">
                    <iais:row id="idNoRow" >
                        <iais:field width="5" value="Donor's ID Type" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:code code="${donorSampleDto.idType}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row  >
                        <iais:field width="5" value="Donor's ID No." mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <c:out value="${donorSampleDto.idNumber}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row  >
                        <iais:field width="5" value="Donor's Name" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <c:out value="${donorSampleDto.donorName}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Donor relation to patient" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${donorSampleDto.donarRelation == 'F' ? 'Friend' : 'Relative'}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="directedDonationNo" style="${donorSampleDto.directedDonation ? 'display: none;' : ''}">
                    <iais:row id="sampleType" >
                        <iais:field width="5" value="Sample Type" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:code code="${donorSampleDto.sampleType}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row >
                        <iais:field width="5" value="Is Donor's Identity Known?" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:code code="${donorSampleDto.donorIdentityKnown}"/>
                        </iais:value>
                    </iais:row>
                    <div id="donorSampleCodeRow" >
                        <iais:row   >
                            <iais:field width="5" value="Donor Sample Code / ID" mandatory="true"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <c:out value="${donorSampleDto.donorSampleCode}"/>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field width="5" value="Name of Bank / AR Centre where Sample is from" mandatory="true"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <iais:code code="${donorSampleDto.sampleFromHciCode}"/>
                            </iais:value>
                        </iais:row>
                        <iais:row id ="sampleFromOthers" style="display:none">
                            <label class="col-xs-5 col-md-4 control-label"></label>
                            <iais:value width="7" cssClass="col-md-7">
                                <c:out value="${donorSampleDto.sampleFromOthers}"/>
                            </iais:value>
                        </iais:row>
                    </div>

                    <div id ="donorDetail">
                        <iais:row >
                            <iais:field width="5" value="Donor's ID Type" mandatory="true"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <iais:code code="${donorSampleDto.idType}"/>
                            </iais:value>
                        </iais:row>
                        <iais:row  >
                            <iais:field width="5" value="Donor's ID No." mandatory="true"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <c:out value="${donorSampleDto.idNumber}"/>
                            </iais:value>
                        </iais:row>
                        <iais:row  >
                            <iais:field width="5" value="Donor's Name" mandatory="true"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <c:out value="${donorSampleDto.donorName}"/>
                            </iais:value>
                        </iais:row>
                    </div>
                </div>

                <iais:row>
                    <iais:field width="5" value="Donor's Age when Sample was Collected" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${donorSampleDto.age}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <label class="col-xs-5 col-md-4 control-label"></label>
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${donorSampleDto.age}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>
<%@include file="previewDonorSection.jsp"%>
