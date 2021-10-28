<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="panel panel-default">
    <div class="panel-heading completed ">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#">
                Preimplantation Genetic Testing
            </a>
        </h4>
    </div>
    <div id="efoDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">

                <iais:row>
                    <label class="col-xs-6 col-md-6 ">Premises where IUI is Performed</label>
                    <label class="col-xs-6 col-md-6 "><c:out value="${arSuperDataSubmissionDto.efoCycleStageDto.performed}"/></label>
                </iais:row>
                <iais:row>
                    <label class="col-xs-6 col-md-6 ">Date Started</label>
                    <label class="col-xs-6 col-md-6">
                        <fmt:formatDate value="${arSuperDataSubmissionDto.efoCycleStageDto.startDate}" pattern="dd/MM/yyyy"></fmt:formatDate>
                    </label>
                </iais:row>
                <iais:row>
                    <label class="col-xs-6 col-md-6 ">Patient's Age as of This Treatment</label>
                    <label class="col-xs-6 col-md-6">
                        <c:out value="${arSuperDataSubmissionDto.efoCycleStageDto.yearNum} Years and ${arSuperDataSubmissionDto.efoCycleStageDto.monthNum} Months"/>
                    </label>
                </iais:row>
                <iais:row>
                    <label class="col-xs-6 col-md-6 ">Is it Medically Indicated?</label>
                    <label class="col-xs-6 col-md-6">
                        <c:if test="${arSuperDataSubmissionDto.efoCycleStageDto.isMedicallyIndicated ==1 }">
                            <label class="form-check-label" >Yes</label></c:if>
                        <c:if test="${arSuperDataSubmissionDto.efoCycleStageDto.isMedicallyIndicated ==0 }">
                            <label class="form-check-label" >No</label></c:if>
                    </label>
                </iais:row>
                <iais:row>
                    <label class="col-xs-6 col-md-6 ">Reasons</label>
                    <label class="col-xs-6 col-md-6">
                        <iais:code code="${arSuperDataSubmissionDto.efoCycleStageDto.reason}"/>
                    </label>
                </iais:row>
                <iais:row>
                    <label class="col-xs-6 col-md-6 "></label>
                    <label class="col-xs-6 col-md-6">
                        <div id="othersReason" <c:if test="${arSuperDataSubmissionDto.efoCycleStageDto.reason=='EFOR004'}">style="display: none"</c:if> >
                            <c:out value="${arSuperDataSubmissionDto.efoCycleStageDto.othersReason}"/>
                        </div>
                    </label>
                </iais:row>

            </div>
        </div>
    </div>
</div>