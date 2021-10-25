<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                Egg Freezing Only Cycle
            </strong>
        </h4>
    </div>
    <div id="efoDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <h3>
                    <p><label style="font-family:'Arial Negreta', 'Arial Normal', 'Arial';font-weight:700;"><c:out value="${arSuperDataSubmissionDto.patientDto.patientName}"/></label><label style="font-family:'Arial Normal', 'Arial';font-weight:400;"><c:out value="(${arSuperDataSubmissionDto.patientDto.patientIdNO})"/></label></p>
                </h3>
                <iais:row>
                    <iais:field width="5" value="Premises where egg freezing only cycle is performed" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7" label="true">
                        <c:out value="${arSuperDataSubmissionDto.efoCycleStageDto.performed}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Date Started" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <fmt:formatDate value="${arSuperDataSubmissionDto.efoCycleStageDto.startDate}" pattern="dd/MM/yyyy"></fmt:formatDate>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Patient's Age as of This Treatment" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7" label="true">
                        <c:out value="${arSuperDataSubmissionDto.efoCycleStageDto.yearNum} Years and ${arSuperDataSubmissionDto.efoCycleStageDto.monthNum} Months"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Is it Medically Indicated?" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <c:if test="${arSuperDataSubmissionDto.efoCycleStageDto.isMedicallyIndicated ==1 }">
                            <label class="form-check-label" >Yes</label></c:if>
                        <c:if test="${arSuperDataSubmissionDto.efoCycleStageDto.isMedicallyIndicated ==0 }">
                            <label class="form-check-label" >No</label></c:if>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Reasons" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:code code="${arSuperDataSubmissionDto.efoCycleStageDto.reason}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <div id="othersReason" style="display: none" >
                            <input type="text" maxlength="20"   name="othersReason" value="${arSuperDataSubmissionDto.efoCycleStageDto.otherReason}" >
                            <span class="error-msg" name="iaisErrorMsg" id="error_otherReason"></span>
                        </div>
                    </iais:value>
                </iais:row>

            </div>
        </div>
    </div>
</div>