<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="panel panel-default">
    <div class="panel-heading  ">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#embryoCreatedDetails">
                Embryo Created
            </a>
        </h4>
    </div>
    <div id="embryoCreatedDetails" class="panel-collapse collapse in">
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
                    <c:if test="${not empty arSuperDataSubmissionDto.oldArSuperDataSubmissionDto}">
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <select id="oldDsSelect" name="oldDsSelect">
                                <c:forEach items="${arSuperDataSubmissionDto.oldArSuperDataSubmissionDto}" var="oldDs" varStatus="index">
                                    <option  <c:if test="${oldDs.dataSubmissionDto.id == arSuperDataSubmissionDtoVersion.dataSubmissionDto.id}">checked</c:if> value ="${oldDs.dataSubmissionDto.id}">V ${oldDs.dataSubmissionDto.version}</option>
                                </c:forEach>
                            </select>
                        </iais:value>
                    </c:if>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="No. Transferrable embryos created from fresh oocyte(s)" mandatory="false"/>
                    <iais:value width="4" display="true">
                        <c:out value="${arSuperDataSubmissionDto.embryoCreatedStageDto.transEmbrFreshOccNum}"/>
                    </iais:value>
                    <iais:value width="4" display="true">
                        <c:out value="${arSuperDataSubmissionDtoVersion.embryoCreatedStageDto.transEmbrFreshOccNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="No. of Poor Quality / Unhealthy / Abnormally / Developed created from fresh oocyte(s)" mandatory="false"/>
                    <iais:value width="4" display="true">
                        <c:out value="${arSuperDataSubmissionDto.embryoCreatedStageDto.poorDevFreshOccNum}"/>
                    </iais:value>
                    <iais:value width="4" display="true">
                        <c:out value="${arSuperDataSubmissionDtoVersion.embryoCreatedStageDto.poorDevFreshOccNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="No. Transferrable embryos created from thawed oocyte(s)" mandatory="false"/>
                    <iais:value width="4" display="true">
                        <c:out value="${arSuperDataSubmissionDto.embryoCreatedStageDto.transEmbrThawOccNum}"/>
                    </iais:value>
                    <iais:value width="4" display="true">
                        <c:out value="${arSuperDataSubmissionDtoVersion.embryoCreatedStageDto.transEmbrThawOccNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="No. of Poor Quality / Unhealthy / Abnormally / Developed created from thawed oocyte(s)" mandatory="false"/>
                    <iais:value width="4" display="true">
                        <c:out value="${arSuperDataSubmissionDto.embryoCreatedStageDto.poorDevThawOccNum}"/>
                    </iais:value>
                    <iais:value width="4" display="true">
                        <c:out value="${arSuperDataSubmissionDtoVersion.embryoCreatedStageDto.poorDevThawOccNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="Total No. Created" mandatory="false"/>
                    <iais:value width="4" display="true">
                        <c:out value="${arSuperDataSubmissionDto.embryoCreatedStageDto.totalNum}"/>
                    </iais:value>
                    <iais:value width="4" display="true">
                        <c:out value="${arSuperDataSubmissionDtoVersion.embryoCreatedStageDto.totalNum}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>