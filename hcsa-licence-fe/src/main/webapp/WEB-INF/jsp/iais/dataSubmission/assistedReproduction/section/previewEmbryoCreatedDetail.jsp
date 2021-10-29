<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="panel panel-default">
    <div class="panel-heading completed ">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#">
                Embryo Created
            </a>
        </h4>
    </div>
    <div id="embryoCreatedDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <iais:row>
                    <label class="col-xs-6 col-md-6 ">No. Transferrable embryos created from fresh oocyte(s)</label>
                    <label class="col-xs-6 col-md-6">
                        <c:out value="${arSuperDataSubmissionDto.embryoCreatedStageDto.transEmbrFreshOccNum}"/>
                    </label>
                </iais:row>
                <iais:row>
                    <label class="col-xs-6 col-md-6 ">No. of Poor Quality / Unhealthy / Abnormally / Developed created from fresh oocyte(s)</label>
                    <label class="col-xs-6 col-md-6">
                        <c:out value="${arSuperDataSubmissionDto.embryoCreatedStageDto.poorDevFreshOccNum}"/>
                    </label>
                </iais:row>
                <iais:row>
                    <label class="col-xs-6 col-md-6 ">No. Transferrable embryos created from thawed oocyte(s)</label>
                    <label class="col-xs-6 col-md-6">
                        <c:out value="${arSuperDataSubmissionDto.embryoCreatedStageDto.transEmbrThawOccNum}"/>
                    </label>
                </iais:row>
                <iais:row>
                    <label class="col-xs-6 col-md-6 ">No. of Poor Quality / Unhealthy / Abnormally / Developed created from thawed oocyte(s)</label>
                    <label class="col-xs-6 col-md-6">
                        <c:out value="${arSuperDataSubmissionDto.embryoCreatedStageDto.poorDevThawOccNum}"/>
                    </label>
                </iais:row>
                <iais:row>
                    <label class="col-xs-6 col-md-6 ">Total No. Created</label>
                    <label class="col-xs-6 col-md-6">
                        <c:out value="${arSuperDataSubmissionDto.embryoCreatedStageDto.totalNum}"/>
                    </label>
                </iais:row>

            </div>
        </div>
    </div>
</div>