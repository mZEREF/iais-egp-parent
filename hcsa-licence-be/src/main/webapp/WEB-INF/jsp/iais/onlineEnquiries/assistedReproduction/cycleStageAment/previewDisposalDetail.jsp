<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="panel panel-default">
    <div class="panel-heading  ">
        <h4 class="panel-title">
            <a  data-toggle="collapse" href="#disposalDetails">
                Disposal
            </a>
        </h4>
    </div>
    <div id="disposalDetails" class="panel-collapse collapse in">
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
                        <iais:select  id="oldDsSelect" name="oldDsSelect" options="versionOptions" value="${arSuperDataSubmissionDtoVersion.dataSubmissionDto.id}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="What was disposed?" mandatory="false"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <iais:code code="${arSuperDataSubmissionDto.disposalStageDto.disposedType}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <iais:code code="${arSuperDataSubmissionDtoVersion.disposalStageDto.disposedType}"/>
                    </iais:value>
                </iais:row>
                <div id="oocyteDisplay" <c:if test="${arSuperDataSubmissionDto.disposalStageDto.disposedTypeDisplay!=1 && arSuperDataSubmissionDtoVersion.disposalStageDto.disposedTypeDisplay!=1 }">style="display: none"</c:if>>

                    <iais:row>
                        <iais:field width="4" value="Immature" mandatory="false"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${arSuperDataSubmissionDto.disposalStageDto.immature}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${arSuperDataSubmissionDtoVersion.disposalStageDto.immature}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="4" value="Abnormally Fertilised" mandatory="false"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${arSuperDataSubmissionDto.disposalStageDto.abnormallyFertilised}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${arSuperDataSubmissionDtoVersion.disposalStageDto.abnormallyFertilised}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="4" value="Unfertilised" mandatory="false"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${arSuperDataSubmissionDto.disposalStageDto.unfertilised}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${arSuperDataSubmissionDtoVersion.disposalStageDto.unfertilised}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="4" value="Atretic" mandatory="false"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${arSuperDataSubmissionDto.disposalStageDto.atretic}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${arSuperDataSubmissionDtoVersion.disposalStageDto.atretic}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="4" value="Damaged" mandatory="false"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${arSuperDataSubmissionDto.disposalStageDto.damaged}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${arSuperDataSubmissionDtoVersion.disposalStageDto.damaged}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="4" value="Lysed / Degenerated" mandatory="false"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${arSuperDataSubmissionDto.disposalStageDto.lysedOrDegenerated}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${arSuperDataSubmissionDtoVersion.disposalStageDto.lysedOrDegenerated}"/>
                        </iais:value>
                    </iais:row>
                </div>

                <div id="embryoDisplay" <c:if test="${arSuperDataSubmissionDto.disposalStageDto.disposedTypeDisplay!=2 && arSuperDataSubmissionDtoVersion.disposalStageDto.disposedTypeDisplay!=2}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="4" value="No. of Poor Quality / Unhealthy / Abnormal Discarded" mandatory="false"/>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${arSuperDataSubmissionDto.disposalStageDto.unhealthyNum}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <c:out value="${arSuperDataSubmissionDtoVersion.disposalStageDto.unhealthyNum}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <iais:row>
                    <iais:field width="4" value="Discarded for Other Reasons" mandatory="false"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arSuperDataSubmissionDto.disposalStageDto.otherDiscardedNum}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arSuperDataSubmissionDtoVersion.disposalStageDto.otherDiscardedNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="Other Reasons for Discarding" mandatory="false"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arSuperDataSubmissionDto.disposalStageDto.otherDiscardedReason}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arSuperDataSubmissionDtoVersion.disposalStageDto.otherDiscardedReason}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="Total No. Disposed Of" mandatory="false"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arSuperDataSubmissionDto.disposalStageDto.totalNum}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arSuperDataSubmissionDtoVersion.disposalStageDto.totalNum}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>