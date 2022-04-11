<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="panel panel-default">
    <div class="panel-heading  ">
        <h4 class="panel-title">
            <a  data-toggle="collapse" href="#">
                IUI Treatment Co-funding
            </a>
        </h4>
    </div>
    <div id="efoDetails" class="panel-collapse collapse in">
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
                <c:set var="iuiTreatmentSubsidiesDto" value="${arSuperDataSubmissionDto.iuiTreatmentSubsidiesDto}" />
                <c:set var="iuiTreatmentSubsidiesDtoVersion" value="${arSuperDataSubmissionDtoVersion.iuiTreatmentSubsidiesDto}" />
                <iais:row>
                    <iais:field width="5" value="Please indicate IUI Co-funding"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <iais:code code="${iuiTreatmentSubsidiesDto.artCoFunding}"/>
                    </iais:value>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <iais:code code="${iuiTreatmentSubsidiesDtoVersion.artCoFunding}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Is there an Appeal?" />
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:if test="${iuiTreatmentSubsidiesDto.thereAppeal == true }">Yes</c:if>
                        <c:if test="${iuiTreatmentSubsidiesDto.thereAppeal == false }">No</c:if>
                    </iais:value>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:if test="${iuiTreatmentSubsidiesDtoVersion.thereAppeal == true }">Yes</c:if>
                        <c:if test="${iuiTreatmentSubsidiesDtoVersion.thereAppeal == false }">No</c:if>
                    </iais:value>
                </iais:row>
            </div>

        </div>
    </div>
</div>
