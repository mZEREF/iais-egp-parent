<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="collapsed" href="#embryoCreatedDetails" data-toggle="collapse">
                Embryo Created
            </a>
        </h4>
    </div>
    <div id="embryoCreatedDetails" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <h3>
                    <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                </h3>
                <iais:row>
                    <iais:field width="5" value="No. Transferrable embryos created from fresh oocyte(s)" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="${arSuperDataSubmissionDto.embryoCreatedStageDto.transEmbrFreshOccNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Poor Quality / Unhealthy / Abnormally / Developed created from fresh oocyte(s)" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="${arSuperDataSubmissionDto.embryoCreatedStageDto.poorDevFreshOccNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. Transferrable embryos created from thawed oocyte(s)" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="${arSuperDataSubmissionDto.embryoCreatedStageDto.transEmbrThawOccNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Poor Quality / Unhealthy / Abnormally / Developed created from thawed oocyte(s)" mandatory="false"/>
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
                <%@include file="../common/patientInventoryTable.jsp" %>
            </div>
        </div>
    </div>
</div>