<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="panel panel-default">
    <div class="panel-heading completed ">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#">
                Egg Freezing Only Cycle
            </a>
        </h4>
    </div>
    <div id="efoDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <h3>
                    <p><label style="font-family:'Arial Negreta', 'Arial Normal', 'Arial';font-weight:700;"><c:out value="${AR_DATA_SUBMISSION.patientInfoDto.patient.name}"/></label><label style="font-family:'Arial Normal', 'Arial';font-weight:400;"><c:out value="(${AR_DATA_SUBMISSION.patientInfoDto.patient.idNumber})"/></label></p>
                </h3>
                <iais:row>
                    <label class="col-xs-6 col-md-6 ">Premises where IUI is Performed</label>
                    <label class="col-xs-6 col-md-6 "><c:out value="${AR_DATA_SUBMISSION.efoCycleStageDto.performed}"/></label>
                </iais:row>
                <iais:row>
                    <label class="col-xs-6 col-md-6 ">Date Started</label>
                    <label class="col-xs-6 col-md-6">
                        <fmt:formatDate value="${AR_DATA_SUBMISSION.efoCycleStageDto.startDate}" pattern="dd/MM/yyyy"></fmt:formatDate>
                    </label>
                </iais:row>
                <iais:row>
                    <label class="col-xs-6 col-md-6 ">Patient's Age as of This Treatment</label>
                    <label class="col-xs-6 col-md-6">
                        <c:out value="${AR_DATA_SUBMISSION.efoCycleStageDto.yearNum} Years and ${AR_DATA_SUBMISSION.efoCycleStageDto.monthNum} Months"/>
                    </label>
                </iais:row>
                <iais:row>
                    <label class="col-xs-6 col-md-6 ">Is it Medically Indicated?</label>
                    <label class="col-xs-6 col-md-6">
                        <c:if test="${AR_DATA_SUBMISSION.efoCycleStageDto.isMedicallyIndicated ==1 }">
                            <label class="form-check-label" >Yes</label></c:if>
                        <c:if test="${AR_DATA_SUBMISSION.efoCycleStageDto.isMedicallyIndicated ==0 }">
                            <label class="form-check-label" >No</label></c:if>
                    </label>
                </iais:row>
                <iais:row>
                    <label class="col-xs-6 col-md-6 ">Reasons</label>
                    <label class="col-xs-6 col-md-6">
                        <iais:code code="${AR_DATA_SUBMISSION.efoCycleStageDto.reason}"/>
                    </label>
                </iais:row>
                <iais:row>
                    <label class="col-xs-6 col-md-6 "></label>
                    <label class="col-xs-6 col-md-6">
                        <div id="othersReason" <c:if test="${AR_DATA_SUBMISSION.efoCycleStageDto.reason=='EFOR004'}">style="display: none"</c:if> >
                            <c:out value="${AR_DATA_SUBMISSION.efoCycleStageDto.othersReason}"/>
                        </div>
                    </label>
                </iais:row>

            </div>
        </div>
    </div>
</div>