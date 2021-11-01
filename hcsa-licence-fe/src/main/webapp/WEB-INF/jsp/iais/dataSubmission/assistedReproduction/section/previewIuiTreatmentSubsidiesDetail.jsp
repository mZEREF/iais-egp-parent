<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="panel panel-default">
    <div class="panel-heading completed ">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#">
                IUI Treatment Subsidies
            </a>
        </h4>
    </div>
    <div id="efoDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <iais:row>
                    <label class="col-xs-6 col-md-6 ">Please indicate IUI Co-funding</label>
                    <label class="col-xs-6 col-md-6">
                        <c:if test="${arSuperDataSubmissionDto.iuiTreatmentSubsidiesDto.artCoFunding ==1 }">
                            <label class="form-check-label" >No Co-funding</label></c:if>
                        <c:if test="${arSuperDataSubmissionDto.iuiTreatmentSubsidiesDto.artCoFunding ==0 }">
                            <label class="form-check-label" >IUI Cycle Subsidy</label></c:if>
                    </label>
                </iais:row>
            </div>
        </div>
    </div>
</div>
