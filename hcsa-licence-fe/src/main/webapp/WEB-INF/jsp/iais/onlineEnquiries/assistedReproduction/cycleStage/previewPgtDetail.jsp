<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="panel panel-default">
    <div class="panel-heading  ">
        <h4 class="panel-title">
            <a href="#pgtDetailsPreview" data-toggle="collapse">
                Preimplantation Genetic Testing
            </a>
        </h4>
    </div>
    <div id="pgtDetailsPreview" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <%@include file="comPart.jsp" %>
                <iais:row>
                    <iais:field width="5" value="Types of Preimplantation Genetic Testing" />
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMCom ==1 }"><iais:code code="PGTTP001"/><br></c:if>
                        <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMRare ==1 }"><iais:code code="PGTTP002"/><br></c:if>
                        <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtSr ==1 }"><iais:code code="PGTTP004"/><br></c:if>
                        <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtA ==1 }"><iais:code code="PGTTP005"/><br></c:if>
                        <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPtt ==1 }"><iais:code code="PGTTP006"/><br></c:if>
                        <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isOtherPgt ==1 }"><iais:code code="PGTTP007"/></c:if>
                    </iais:value>

                </iais:row>
                <div id="pgtOthersDisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isOtherPgt !=1 }">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Others" style="font-size: 2.0rem;" />
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Other Types of Genetic Testing" />
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${arSuperDataSubmissionDto.pgtStageDto.otherPgt}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="pgtMDisplay" <c:if test="${  arSuperDataSubmissionDto.pgtStageDto.isPgtMCom !=1 && arSuperDataSubmissionDto.pgtStageDto.isPgtMRare !=1 }">style="display: none"</c:if> >
                    <iais:row>
                        <iais:field width="5" value="PGT-M" style="font-size: 2.0rem;" />
                    </iais:row>
                    <div id="pgtMcomStage" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMCom !=1 }">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="5" value="Please check all PGT-M (Common) stages done in current cycle" />
                            <iais:value width="7" cssClass="col-md-7" display="true">
                                <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.workUpCom == 1}"><iais:code code="PGT001"/><br></c:if>
                                <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.ebtCom == 1}"><iais:code code="PGT002"/><br></c:if>
                            </iais:value>
                        </iais:row>
                    </div>
                    <div id="pgtMRareStage" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMRare !=1 }">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="5" value="Please check all PGT-M (Rare) stages done in current cycle" />
                            <iais:value width="7" cssClass="col-md-7" display="true">
                                <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.workUpRare == 1}"><iais:code code="PGT001"/><br></c:if>
                                <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.ebtRare == 1}"><iais:code code="PGT002"/><br></c:if>
                            </iais:value>
                        </iais:row>
                    </div>
                    <iais:row>
                        <iais:field width="5" value="Date Started for PGT-M" info="${MessageUtil.getMessageDesc('DS_MSG039')}"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <fmt:formatDate value='${arSuperDataSubmissionDto.pgtStageDto.pgtMDate}' pattern='dd/MM/yyyy' />
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="PGT-M Performed" />
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMDsld ==1 }">To detect Sex-linked Disease<br></c:if>
                            <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMWithHla ==1 }">Together with HLA Matching<br></c:if>
                            <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMNon ==1 }">None of the above</c:if>
                        </iais:value>

                    </iais:row>
                    <div id="pgtMRefNoDisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMDsld !=1 }">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="5" value="PGT-M Appeal Reference No." />
                            <iais:value width="7" cssClass="col-md-7" display="true">
                                <c:out value="${arSuperDataSubmissionDto.pgtStageDto.pgtMRefNo}"/>
                            </iais:value>
                        </iais:row>
                    </div>

                    <iais:row>
                        <iais:field width="5" value="What Condition and Gene was PGT-M Performed to Detect?"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${arSuperDataSubmissionDto.pgtStageDto.pgtMCondition}"/>
                        </iais:value>
                    </iais:row>
                    <div id="pgtMComSubsidies" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMCom !=1 }">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Please indicate if co-funding was provided for PGT-M (Common)"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtCoFunding == 'Y'}">
                                Yes
                            </c:if>
                            <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtCoFunding == 'N'}">
                                No
                            </c:if>
                            <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtCoFunding == 'NA'}">
                                N/A
                            </c:if>
                        </iais:value>
                    </iais:row>
                    </div>
                    <div id="pgtMRareSubsidies" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMRare !=1 }">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="5" value="Please indicate if co-funding was provided for PGT-M (Rare)"/>
                            <iais:value width="7" cssClass="col-md-7" display="true">
                                <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMRareCoFunding == 'Y'}">
                                    Yes
                                </c:if>
                                <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMRareCoFunding == 'N'}">
                                    No
                                </c:if>
                                <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMRareCoFunding == 'NA'}">
                                    N/A
                                </c:if>
                            </iais:value>
                        </iais:row>
                    </div>
                </div>
                <div id="pgtSrDisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtSr !=1 }">style="display: none"</c:if> >
                    <iais:row>
                        <iais:field width="5" value="PGT-SR" style="font-size: 2.0rem;" />
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Date Started for PGT-SR"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <fmt:formatDate value='${arSuperDataSubmissionDto.pgtStageDto.pgtSrDate}' pattern='dd/MM/yyyy' />
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="PGT-SR Appeal Reference No. (If Applicable)"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${arSuperDataSubmissionDto.pgtStageDto.pgtSrRefNo}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="What was PGT-SR Performed For?"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${arSuperDataSubmissionDto.pgtStageDto.pgtSrCondition}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Please indicate if co-funding was provided for PGT-SR"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtSrCoFunding == 'Y'}">
                                Yes
                            </c:if>
                            <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtSrCoFunding == 'N'}">
                                No
                            </c:if>
                            <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtSrCoFunding == 'NA'}">
                                N/A
                            </c:if>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="pgtADisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtA !=1 }">style="display: none"</c:if> >
                    <iais:row>
                        <iais:field width="5" value="PGT-A" style="font-size: 2.0rem;" />
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="PGT-A Performed Because of"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtAAma ==1 }">Advanced Maternal Age<br></c:if>
                            <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtATomrif ==1 }">Two Or More Recurrent Implantation Failure<br></c:if>
                            <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtATomrpl ==1 }">Two Or More Repeated Pregnancy Losses</c:if>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="What was the Result of the PGT-A Test?"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${arSuperDataSubmissionDto.pgtStageDto.pgtAResult}"/>
                        </iais:value>
                    </iais:row>
                    <div id="AbnormalDisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.pgtAResult !='Abnormal' }">style="display: none"</c:if> >
                        <iais:row>
                            <iais:field width="5" value="What Abnormal Condition was Found for the PGT-A Test?"/>
                            <iais:value width="7" cssClass="col-md-7" display="true">
                                <c:out value="${arSuperDataSubmissionDto.pgtStageDto.pgtACondition}"/>
                            </iais:value>
                        </iais:row>
                    </div>
                    <iais:row>
                        <iais:field width="5" value="Please indicate if co-funding was provided for PGT-A"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtACoFunding == 'Y'}">
                                Yes
                            </c:if>
                            <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtACoFunding == 'N'}">
                                No
                            </c:if>
                            <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtACoFunding == 'NA'}">
                                N/A
                            </c:if>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="pttDisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPtt !=1 }">style="display: none"</c:if> >
                    <iais:row>
                        <iais:field width="5" value="PTT" style="font-size: 2.0rem;" />
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="What Condition was PTT Perfomed to Detect"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${arSuperDataSubmissionDto.pgtStageDto.pttCondition}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Please indicate if co-funding was provided for PTT"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPttCoFunding == 'Y'}">
                                Yes
                            </c:if>
                            <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPttCoFunding == 'N'}">
                                No
                            </c:if>
                            <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPttCoFunding == 'NA'}">
                                N/A
                            </c:if>
                        </iais:value>
                    </iais:row>
                </div>
                <div <c:if test="${ appealDisplayShow!=true}">style="display: none"</c:if>>
                    <iais:row >
                        <iais:field width="5" value="Is there an Appeal?"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isThereAppeal ==1 }">
                                Yes
                            </c:if>
                            <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isThereAppeal ==0 }">
                                No
                            </c:if>
                        </iais:value>
                    </iais:row>
                </div>
                <iais:row>
                    <iais:field width="5" value="Embryos were Biospied At"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <iais:optionText value="${arSuperDataSubmissionDto.pgtStageDto.embryosBiopsiedLocal}"/>
                    </iais:value>
                </iais:row>
                <div id="othersEmbryosBiopsyDisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isEmbryosBiopsiedLocal !='Others' }">style="display: none"</c:if> >
                    <iais:row>
                        <iais:field width="5" value="Other Centre where Embryos were Biospied At"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <iais:optionText value="${arSuperDataSubmissionDto.pgtStageDto.otherEmbryosBiopsiedAddr}"/>
                        </iais:value>
                    </iais:row>
                </div>

                <iais:row>
                    <iais:field width="5" value="Biopsy Done By"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="${arSuperDataSubmissionDto.pgtStageDto.isBiopsyLocal}"/>
                    </iais:value>
                </iais:row>
                <div id="othersBiopsyDisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isBiopsyLocal !='Others' }">style="display: none"</c:if> >
                    <iais:row>
                        <iais:field width="5" value="Biopsy Done By (Others)"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${arSuperDataSubmissionDto.pgtStageDto.otherBiopsyAddr}"/>
                        </iais:value>
                    </iais:row>
                </div>

            </div>
        </div>
    </div>
</div>