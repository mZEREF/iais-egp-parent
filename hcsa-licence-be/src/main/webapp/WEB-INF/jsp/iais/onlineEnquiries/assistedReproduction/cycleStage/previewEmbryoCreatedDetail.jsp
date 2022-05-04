<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="panel panel-default">
    <div class="panel-heading  ">
        <h4 class="panel-title">
            <a  data-toggle="collapse" href="#embryoCreatedDetails">
                Embryo Created
            </a>
        </h4>
    </div>
    <div id="embryoCreatedDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <iais:row style="margin-bottom: 0;">
                    <label class="col-xs-4 col-md-4 control-label"><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/>
                        <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                    </label>
                    <label class="col-xs-8 col-md-8 control-label">Submission ID : <span style="font-weight:normal"><c:out value="${arSuperDataSubmissionDto.dataSubmissionDto.submissionNo}"/></span>
                    </label>
                </iais:row>
                <hr/>
                <iais:row>
                    <iais:field width="5"  value="" />
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="Current Version"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Transferrable embryos created from fresh oocyte(s)" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="${arSuperDataSubmissionDto.embryoCreatedStageDto.transEmbrFreshOccNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Poor Quality / Unhealthy / Abnormally Developed embryos created from fresh oocyte(s)" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="${arSuperDataSubmissionDto.embryoCreatedStageDto.poorDevFreshOccNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Transferrable embryos created from thawed oocyte(s)" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="${arSuperDataSubmissionDto.embryoCreatedStageDto.transEmbrThawOccNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Poor Quality / Unhealthy / Abnormally Developed embryos created from thawed oocyte(s)" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="${arSuperDataSubmissionDto.embryoCreatedStageDto.poorDevThawOccNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Total No. Created" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="${arSuperDataSubmissionDto.embryoCreatedStageDto.totalNum}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>