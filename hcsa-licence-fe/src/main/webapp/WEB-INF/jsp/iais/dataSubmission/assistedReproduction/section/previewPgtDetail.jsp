<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="panel panel-default">
    <div class="panel-heading completed ">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#">
                Preimplantation Genetic Testing
            </a>
        </h4>
    </div>
    <div id="pftDetailsPreview" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">

                <iais:row>
                    <label class="col-xs-6 col-md-6 ">Types of Preimplantation Genetic Testing</label>
                    <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtM ==1 }"><label class="col-xs-2 col-md-2">PGT-M</label></c:if>
                    <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtSr ==1 }"><label class="col-xs-2 col-md-2 ">PGT-SR</label></c:if>
                    <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtA ==1 }"><label class="col-xs-2 col-md-2">PGT-A</label></c:if>
                    <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPtt ==1 }"><label class="col-xs-2 col-md-2">PTT</label></c:if>
                    <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isOtherPgt ==1 }"><label class="col-xs-2 col-md-2">Others</label></c:if>
                </iais:row>
                <div id="pgtMDisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtM !=1 }">style="display: none"</c:if> >
                    <iais:row>
                        <iais:field width="6" value="PGT-M" />
                    </iais:row>
                    <iais:row>
                        <label class="col-xs-6 col-md-6 ">PGT-M Performed</label>
                        <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMDsld ==1 }"><label class="col-xs-6 col-md-6">To detect Sex-linked Disease</label></c:if>
                        <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMWithHla ==1 }"><label class="col-xs-6 col-md-6 ">Together with HLA Matching</label></c:if>
                        <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMNon ==1 }"><label class="col-xs-6 col-md-6">None of the above</label></c:if>
                    </iais:row>

                    <iais:row>
                        <label class="col-xs-6 col-md-6 ">PGT-M Appeal Reference No.</label>
                        <label class="col-xs-6 col-md-6">
                            <c:out value="${arSuperDataSubmissionDto.pgtStageDto.pgtMRefNo}"/>
                        </label>
                    </iais:row>
                    <iais:row>
                        <label class="col-xs-6 col-md-6 ">What Condition was PGT-M Performed to Detect?</label>
                        <label class="col-xs-6 col-md-6">
                            <c:out value="${arSuperDataSubmissionDto.pgtStageDto.pgtMCondition}"/>
                        </label>
                    </iais:row>
                </div>
                <div id="pgtSrDisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtSr !=1 }">style="display: none"</c:if> >
                    <iais:row>
                        <label class="col-xs-6 col-md-6 ">What Condition and Structural Rearrangement was PGT-SR Performed?</label>
                        <label class="col-xs-6 col-md-6">
                            <c:out value="${arSuperDataSubmissionDto.pgtStageDto.pgtSrCondition}"/>
                        </label>
                    </iais:row>
                </div>
                <div id="pgtADisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtA !=1 }">style="display: none"</c:if> >
                    <iais:row>
                        <label class="col-xs-6 col-md-6 ">PGT-A Performed Because of</label>
                        <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtAAma ==1 }"><label class="col-xs-6 col-md-6">Advanced Maternal Age</label></c:if>
                        <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtATomrif ==1 }"><label class="col-xs-6 col-md-6 ">Two Or More Recurrent Implantation Failure</label></c:if>
                        <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtATomrpl ==1 }"><label class="col-xs-6 col-md-6">Two Or More Repeated Pregnancy Losses</label></c:if>
                    </iais:row>
                    <iais:row>
                        <label class="col-xs-6 col-md-6 ">What was the Result of the PGT-A Test?</label>
                        <label class="col-xs-6 col-md-6">
                            <c:out value="${arSuperDataSubmissionDto.pgtStageDto.pgtAResult}"/>
                        </label>
                    </iais:row>
                    <div id="AbnormalDisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.pgtAResult !='Abnormal' }">style="display: none"</c:if> >
                        <iais:row>
                            <label class="col-xs-6 col-md-6 ">What Abnormal Condition was Found for the PGT-A Test?</label>
                            <label class="col-xs-6 col-md-6">
                                <c:out value="${arSuperDataSubmissionDto.pgtStageDto.pgtACondition}"/>
                            </label>
                        </iais:row>
                    </div>
                </div>
                <div id="pttDisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPtt !=1 }">style="display: none"</c:if> >
                    <iais:row>
                        <label class="col-xs-6 col-md-6 ">What Condition was PTT Perfomed to Detect</label>
                        <label class="col-xs-6 col-md-6">
                            <c:out value="${arSuperDataSubmissionDto.pgtStageDto.pttCondition}"/>
                        </label>
                    </iais:row>
                </div>
                <iais:row>
                    <label class="col-xs-6 col-md-6 ">Please indicate Preimplantation Genetic Testing Co-funding?</label>
                    <label class="col-xs-6 col-md-6">
                        <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtCoFunding ==1 }"><label class="col-xs-6 col-md-6">Yes</label></c:if>
                        <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtCoFunding ==0 }"><label class="col-xs-6 col-md-6 ">No</label></c:if>
                    </label>
                </iais:row>
                <iais:row>
                    <label class="col-xs-6 col-md-6 ">Embryos were Biospied At</label>
                    <label class="col-xs-6 col-md-6">
                        <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtCoFunding ==1 }"><label class="col-xs-6 col-md-6">HCI name tagged to the active AR licence</label></c:if>
                        <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtCoFunding ==0 }"><label class="col-xs-6 col-md-6 ">Others</label></c:if>
                    </label>
                </iais:row>
                <div id="othersEmbryosBiopsyDisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isEmbryosBiopsiedLocal !=1 }">style="display: none"</c:if> >
                    <iais:row>
                        <label class="col-xs-6 col-md-6 ">Other Centre where Embryos were Biospied At</label>
                        <label class="col-xs-6 col-md-6">
                            <c:out value="${arSuperDataSubmissionDto.pgtStageDto.otherEmbryosBiopsiedAddr}"/>
                        </label>
                    </iais:row>
                </div>

                <iais:row>
                    <label class="col-xs-6 col-md-6 ">Biopsy Done By</label>
                    <label class="col-xs-6 col-md-6">
                        <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isBiopsyLocal ==1 }"><label class="col-xs-6 col-md-6">List of Embryologists tagged to the active AR licence</label></c:if>
                        <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isBiopsyLocal ==0 }"><label class="col-xs-6 col-md-6 ">Others</label></c:if>
                    </label>
                </iais:row>
                <div id="othersBiopsyDisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isBiopsyLocal !=1 }">style="display: none"</c:if> >
                    <iais:row>
                        <label class="col-xs-6 col-md-6 ">Biopsy Done By (Others)</label>
                        <label class="col-xs-6 col-md-6">
                            <c:out value="${arSuperDataSubmissionDto.pgtStageDto.otherBiopsyAddr}"/>
                        </label>
                    </iais:row>
                </div>

            </div>
        </div>
    </div>
</div>