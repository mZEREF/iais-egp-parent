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
                    <iais:field width="5"   value="" />
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="Current Version"/>
                    </iais:value>
                </iais:row>
                <c:set var="iuiTreatmentSubsidiesDto" value="${arSuperDataSubmissionDto.iuiTreatmentSubsidiesDto}" />
                <iais:row>
                    <iais:field width="5" value="Please indicate IUI Co-funding"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <iais:code code="${iuiTreatmentSubsidiesDto.artCoFunding}"/>
                    </iais:value>
                </iais:row>
            <c:if test="${isDisplayAppeal}">
                <iais:row>
                    <iais:field width="5" value="Is there an Appeal?" />
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:if test="${iuiTreatmentSubsidiesDto.thereAppeal == true }">Yes</c:if>
                        <c:if test="${iuiTreatmentSubsidiesDto.thereAppeal == false }">No</c:if>
                    </iais:value>
                </iais:row>
            </c:if>
            </div>

        </div>
    </div>
</div>
