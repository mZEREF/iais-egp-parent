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
                    <iais:field width="6" value="Types of Preimplantation Genetic Testing" />
                    <iais:value width="6" display="true">
                        <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtM ==1 }">PGT-M<br></c:if>
                        <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtSr ==1 }">PGT-SR<br></c:if>
                        <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtA ==1 }">PGT-A<br></c:if>
                        <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPtt ==1 }">PTT<br></c:if>
                        <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isOtherPgt ==1 }">Others</c:if>
                    </iais:value>

                </iais:row>
                <div id="pgtOthersDisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isOtherPgt !=1 }">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="Others" />
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="Other Types of Genetic Testing" />
                        <iais:value width="6" display="true">
                            <c:out value="${arSuperDataSubmissionDto.pgtStageDto.otherPgt}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="pgtMDisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtM !=1 }">style="display: none"</c:if> >
                    <iais:row>
                        <iais:field width="6" value="PGT-M" />
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="PGT-M Performed" />
                        <iais:value width="6" display="true">
                            <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMDsld ==1 }">To detect Sex-linked Disease<br></c:if>
                            <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMWithHla ==1 }">Together with HLA Matching<br></c:if>
                            <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMNon ==1 }">None of the above</c:if>
                        </iais:value>

                    </iais:row>

                    <iais:row>
                        <iais:field width="6" value="PGT-M Appeal Reference No." />
                        <iais:value width="6" display="true">
                            <c:out value="${arSuperDataSubmissionDto.pgtStageDto.pgtMRefNo}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="What Condition was PGT-M Performed to Detect?"/>
                        <iais:value width="6" display="true">
                            <c:out value="${arSuperDataSubmissionDto.pgtStageDto.pgtMCondition}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="pgtSrDisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtSr !=1 }">style="display: none"</c:if> >
                    <iais:row>
                        <iais:field width="6" value="PGT-SR" />
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="What Condition and Structural Rearrangement was PGT-SR Performed?"/>
                        <iais:value width="6" display="true">
                            <c:out value="${arSuperDataSubmissionDto.pgtStageDto.pgtSrCondition}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="pgtADisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtA !=1 }">style="display: none"</c:if> >
                    <iais:row>
                        <iais:field width="6" value="PGT-A" />
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="PGT-A Performed Because of"/>
                        <iais:value width="6" display="true">
                            <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtAAma ==1 }">Advanced Maternal Age<br></c:if>
                            <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtATomrif ==1 }">Two Or More Recurrent Implantation Failure<br></c:if>
                            <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtATomrpl ==1 }">Two Or More Repeated Pregnancy Losses</c:if>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="What was the Result of the PGT-A Test?"/>
                        <iais:value width="6" display="true">
                            <c:out value="${arSuperDataSubmissionDto.pgtStageDto.pgtAResult}"/>
                        </iais:value>
                    </iais:row>
                    <div id="AbnormalDisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.pgtAResult !='Abnormal' }">style="display: none"</c:if> >
                        <iais:row>
                            <iais:field width="6" value="What Abnormal Condition was Found for the PGT-A Test?"/>
                            <iais:value width="6" display="true">
                                <c:out value="${arSuperDataSubmissionDto.pgtStageDto.pgtACondition}"/>
                            </iais:value>
                        </iais:row>
                    </div>
                </div>
                <div id="pttDisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPtt !=1 }">style="display: none"</c:if> >
                    <iais:row>
                        <iais:field width="6" value="PTT" />
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="What Condition was PTT Perfomed to Detect"/>
                        <iais:value width="6" display="true">
                            <c:out value="${arSuperDataSubmissionDto.pgtStageDto.pttCondition}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <iais:row>
                    <iais:field width="6" value="Please indicate Preimplantation Genetic Testing Co-funding?"/>
                    <iais:value width="6" display="true">
                        <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtCoFunding ==1 }">
                            Yes
                        </c:if>
                        <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtCoFunding ==0 }">
                            No
                        </c:if>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Embryos were Biospied At"/>
                    <iais:value width="6" display="true">
                        <c:out value="${arSuperDataSubmissionDto.pgtStageDto.isEmbryosBiopsiedLocal}"/>
                    </iais:value>
                </iais:row>
                <div id="othersEmbryosBiopsyDisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isEmbryosBiopsiedLocal !='Others' }">style="display: none"</c:if> >
                    <iais:row>
                        <iais:field width="6" value="Other Centre where Embryos were Biospied At"/>
                        <iais:value width="6" display="true">
                            <c:out value="${arSuperDataSubmissionDto.pgtStageDto.otherEmbryosBiopsiedAddr}"/>
                        </iais:value>
                    </iais:row>
                </div>

                <iais:row>
                    <iais:field width="6" value="Biopsy Done By"/>
                    <iais:value width="6" display="true">
                        <c:out value="${arSuperDataSubmissionDto.pgtStageDto.isBiopsyLocal}"/>
                    </iais:value>
                </iais:row>
                <div id="othersBiopsyDisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isBiopsyLocal !='Others' }">style="display: none"</c:if> >
                    <iais:row>
                        <iais:field width="6" value="Biopsy Done By (Others)"/>
                        <iais:value width="6" display="true">
                            <c:out value="${arSuperDataSubmissionDto.pgtStageDto.otherBiopsyAddr}"/>
                        </iais:value>
                    </iais:row>
                </div>
            </div>
        </div>
    </div>
</div>