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
    <iais:field width="4" cssClass="col-md-4"  value="" />
    <iais:value width="4" cssClass="col-md-4" display="true">
        <c:out value="Current Version"/>
    </iais:value>
    <c:if test="${not empty arSuperDataSubmissionDto.oldArSuperDataSubmissionDto}">
        <iais:value width="4" cssClass="col-md-4" display="true">
            <iais:select  id="oldDsSelect" name="oldDsSelect" options="versionOptions" value="${arSuperDataSubmissionDtoVersion.dataSubmissionDto.id}"/>
        </iais:value>
    </c:if>
</iais:row>