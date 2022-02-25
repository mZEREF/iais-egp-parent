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
                <h3>
                    <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                </h3>
                <iais:row>
                    <iais:field width="4" cssClass="col-md-4"  value="" />
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="Current Version"/>
                    </iais:value>
                    <c:if test="${not empty arSuperDataSubmissionDto.oldArSuperDataSubmissionDto}">
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <select id="oldDsSelect" name="oldDsSelect">
                                <c:forEach items="${arSuperDataSubmissionDto.oldArSuperDataSubmissionDto}" var="oldDs" varStatus="index">
                                    <option  <c:if test="${oldDs.dataSubmissionDto.id == arSuperDataSubmissionDtoVersion.dataSubmissionDto.id}">checked</c:if> value ="${oldDs.dataSubmissionDto.id}">V ${oldDs.dataSubmissionDto.version}</option>
                                </c:forEach>
                            </select>
                        </iais:value>
                    </c:if>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="Types of Preimplantation Genetic Testing" />
                    <iais:value width="4" display="true">
                        <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMCom ==1 }"><iais:code code="PGTTP001"/><br></c:if>
                        <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMRare ==1 }"><iais:code code="PGTTP002"/><br></c:if>
                        <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMEbt ==1 }"><iais:code code="PGTTP003"/><br></c:if>
                        <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtSr ==1 }"><iais:code code="PGTTP004"/><br></c:if>
                        <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtA ==1 }"><iais:code code="PGTTP005"/><br></c:if>
                        <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPtt ==1 }"><iais:code code="PGTTP006"/><br></c:if>
                        <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isOtherPgt ==1 }"><iais:code code="PGTTP007"/></c:if>
                    </iais:value>
                    <iais:value width="4" display="true">
                        <c:if test="${ arSuperDataSubmissionDtoVersion.pgtStageDto.isPgtMCom ==1 }"><iais:code code="PGTTP001"/><br></c:if>
                        <c:if test="${ arSuperDataSubmissionDtoVersion.pgtStageDto.isPgtMRare ==1 }"><iais:code code="PGTTP002"/><br></c:if>
                        <c:if test="${ arSuperDataSubmissionDtoVersion.pgtStageDto.isPgtMEbt ==1 }"><iais:code code="PGTTP003"/><br></c:if>
                        <c:if test="${ arSuperDataSubmissionDtoVersion.pgtStageDto.isPgtSr ==1 }"><iais:code code="PGTTP004"/><br></c:if>
                        <c:if test="${ arSuperDataSubmissionDtoVersion.pgtStageDto.isPgtA ==1 }"><iais:code code="PGTTP005"/><br></c:if>
                        <c:if test="${ arSuperDataSubmissionDtoVersion.pgtStageDto.isPtt ==1 }"><iais:code code="PGTTP006"/><br></c:if>
                        <c:if test="${ arSuperDataSubmissionDtoVersion.pgtStageDto.isOtherPgt ==1 }"><iais:code code="PGTTP007"/></c:if>
                    </iais:value>

                </iais:row>

                <div id="pgtOthersDisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isOtherPgt !=1 && arSuperDataSubmissionDtoVersion.pgtStageDto.isOtherPgt !=1 }">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="4" value="Others" style="font-size: 2.0rem;" />
                    </iais:row>
                    <iais:row>
                        <iais:field width="4" value="Other Types of Genetic Testing" />
                        <iais:value width="4" display="true">
                            <c:out value="${arSuperDataSubmissionDto.pgtStageDto.otherPgt}"/>
                        </iais:value>
                        <iais:value width="4" display="true">
                            <c:out value="${arSuperDataSubmissionDtoVersion.pgtStageDto.otherPgt}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="pgtMDisplay" <c:if test="${  (arSuperDataSubmissionDto.pgtStageDto.isPgtMCom !=1 || arSuperDataSubmissionDto.pgtStageDto.isPgtMRare !=1 || arSuperDataSubmissionDto.pgtStageDto.isPgtMEbt !=1 )
                 && (arSuperDataSubmissionDtoVersion.pgtStageDto.isPgtMCom !=1 || arSuperDataSubmissionDtoVersion.pgtStageDto.isPgtMRare !=1 || arSuperDataSubmissionDtoVersion.pgtStageDto.isPgtMEbt !=1 )
                 }">style="display: none"</c:if> >
                    <iais:row>
                        <iais:field width="4" value="PGT-M" style="font-size: 2.0rem;" />
                    </iais:row>
                    <iais:row>
                        <iais:field width="4" value="PGT-M Performed" />
                        <iais:value width="4" display="true">
                            <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMDsld ==1 }">To detect Sex-linked Disease<br></c:if>
                            <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMWithHla ==1 }">Together with HLA Matching<br></c:if>
                            <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMNon ==1 }">None of the above</c:if>
                        </iais:value>
                        <iais:value width="4" display="true">
                            <c:if test="${ arSuperDataSubmissionDtoVersion.pgtStageDto.isPgtMDsld ==1 }">To detect Sex-linked Disease<br></c:if>
                            <c:if test="${ arSuperDataSubmissionDtoVersion.pgtStageDto.isPgtMWithHla ==1 }">Together with HLA Matching<br></c:if>
                            <c:if test="${ arSuperDataSubmissionDtoVersion.pgtStageDto.isPgtMNon ==1 }">None of the above</c:if>
                        </iais:value>

                    </iais:row>

                    <div id="pgtMRefNoDisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMDsld !=1 }">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="4" value="PGT-M Appeal Reference No." />
                            <iais:value width="4" display="true">
                                <c:out value="${arSuperDataSubmissionDto.pgtStageDto.pgtMRefNo}"/>
                            </iais:value>
                            <iais:value width="4" display="true">
                                <c:out value="${arSuperDataSubmissionDtoVersion.pgtStageDto.pgtMRefNo}"/>
                            </iais:value>
                        </iais:row>
                    </div>
                    <iais:row>
                        <iais:field width="4" value="What Condition was PGT-M Performed to Detect?"/>
                        <iais:value width="4" display="true">
                            <c:out value="${arSuperDataSubmissionDto.pgtStageDto.pgtMCondition}"/>
                        </iais:value>
                        <iais:value width="4" display="true">
                            <c:out value="${arSuperDataSubmissionDtoVersion.pgtStageDto.pgtMCondition}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="pgtSrDisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtSr !=1 && arSuperDataSubmissionDtoVersion.pgtStageDto.isPgtSr !=1 }">style="display: none"</c:if> >
                    <iais:row>
                        <iais:field width="4" value="PGT-SR" style="font-size: 2.0rem;" />
                    </iais:row>
                    <iais:row>
                        <iais:field width="4" value="What Condition and Structural Rearrangement was PGT-SR Performed?"/>
                        <iais:value width="4" display="true">
                            <c:out value="${arSuperDataSubmissionDto.pgtStageDto.pgtSrCondition}"/>
                        </iais:value>
                        <iais:value width="4" display="true">
                            <c:out value="${arSuperDataSubmissionDtoVersion.pgtStageDto.pgtSrCondition}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="pgtADisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtA !=1 && arSuperDataSubmissionDtoVersion.pgtStageDto.isPgtA !=1 }">style="display: none"</c:if> >
                    <iais:row>
                        <iais:field width="4" value="PGT-A" style="font-size: 2.0rem;" />
                    </iais:row>
                    <iais:row>
                        <iais:field width="4" value="PGT-A Performed Because of"/>
                        <iais:value width="4" display="true">
                            <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtAAma ==1 }">Advanced Maternal Age<br></c:if>
                            <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtATomrif ==1 }">Two Or More Recurrent Implantation Failure<br></c:if>
                            <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtATomrpl ==1 }">Two Or More Repeated Pregnancy Losses</c:if>
                        </iais:value>
                        <iais:value width="4" display="true">
                            <c:if test="${ arSuperDataSubmissionDtoVersion.pgtStageDto.isPgtAAma ==1 }">Advanced Maternal Age<br></c:if>
                            <c:if test="${ arSuperDataSubmissionDtoVersion.pgtStageDto.isPgtATomrif ==1 }">Two Or More Recurrent Implantation Failure<br></c:if>
                            <c:if test="${ arSuperDataSubmissionDtoVersion.pgtStageDto.isPgtATomrpl ==1 }">Two Or More Repeated Pregnancy Losses</c:if>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="4" value="What was the Result of the PGT-A Test?"/>
                        <iais:value width="4" display="true">
                            <c:out value="${arSuperDataSubmissionDto.pgtStageDto.pgtAResult}"/>
                        </iais:value>
                        <iais:value width="4" display="true">
                            <c:out value="${arSuperDataSubmissionDtoVersion.pgtStageDto.pgtAResult}"/>
                        </iais:value>
                    </iais:row>
                    <div id="AbnormalDisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.pgtAResult !='Abnormal' }">style="display: none"</c:if> >
                        <iais:row>
                            <iais:field width="4" value="What Abnormal Condition was Found for the PGT-A Test?"/>
                            <iais:value width="4" display="true">
                                <c:out value="${arSuperDataSubmissionDto.pgtStageDto.pgtACondition}"/>
                            </iais:value>
                            <iais:value width="4" display="true">
                                <c:out value="${arSuperDataSubmissionDtoVersion.pgtStageDto.pgtACondition}"/>
                            </iais:value>
                        </iais:row>
                    </div>
                </div>
                <div id="pttDisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPtt !=1 && arSuperDataSubmissionDtoVersion.pgtStageDto.isPtt !=1 }">style="display: none"</c:if> >
                    <iais:row>
                        <iais:field width="4" value="PTT" style="font-size: 2.0rem;" />
                    </iais:row>
                    <iais:row>
                        <iais:field width="4" value="What Condition was PTT Perfomed to Detect"/>
                        <iais:value width="4" display="true">
                            <c:out value="${arSuperDataSubmissionDto.pgtStageDto.pttCondition}"/>
                        </iais:value>
                        <iais:value width="4" display="true">
                            <c:out value="${arSuperDataSubmissionDtoVersion.pgtStageDto.pttCondition}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <iais:row>
                    <iais:field width="4" value="Please indicate Preimplantation Genetic Testing Co-funding?"/>
                    <iais:value width="4" display="true">
                        <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtCoFunding ==1 }">
                            Yes
                        </c:if>
                        <c:if test="${ arSuperDataSubmissionDtoVersion.pgtStageDto.isPgtCoFunding ==0 }">
                            No
                        </c:if>
                    </iais:value>
                    <iais:value width="4" display="true">
                        <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtCoFunding ==1 }">
                            Yes
                        </c:if>
                        <c:if test="${ arSuperDataSubmissionDtoVersion.pgtStageDto.isPgtCoFunding ==0 }">
                            No
                        </c:if>
                    </iais:value>
                </iais:row>
                <div <c:if test="${ count<6 }">style="display: none"</c:if>>
                    <iais:row >
                        <iais:field width="4" value="Is there an Appeal?"/>
                        <iais:value width="4" display="true">
                            <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isThereAppeal ==1 }">
                                Yes
                            </c:if>
                            <c:if test="${ arSuperDataSubmissionDtoVersion.pgtStageDto.isThereAppeal ==0 }">
                                No
                            </c:if>
                        </iais:value>
                        <iais:value width="4" display="true">
                            <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isThereAppeal ==1 }">
                                Yes
                            </c:if>
                            <c:if test="${ arSuperDataSubmissionDtoVersion.pgtStageDto.isThereAppeal ==0 }">
                                No
                            </c:if>
                        </iais:value>
                    </iais:row>
                </div>
                <iais:row>
                    <iais:field width="4" value="Embryos were Biospied At"/>
                    <iais:value width="4" display="true">
                        <c:out value="${arSuperDataSubmissionDto.pgtStageDto.embryosBiopsiedLocal}"/>
                    </iais:value>
                    <iais:value width="4" display="true">
                        <c:out value="${arSuperDataSubmissionDtoVersion.pgtStageDto.embryosBiopsiedLocal}"/>
                    </iais:value>
                </iais:row>
                <div id="othersEmbryosBiopsyDisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isEmbryosBiopsiedLocal !='Others' && arSuperDataSubmissionDtoVersion.pgtStageDto.isEmbryosBiopsiedLocal !='Others' }">style="display: none"</c:if> >
                    <iais:row>
                        <iais:field width="4" value="Other Centre where Embryos were Biospied At"/>
                        <iais:value width="4" display="true">
                            <c:out value="${arSuperDataSubmissionDto.pgtStageDto.otherEmbryosBiopsiedAddr}"/>
                        </iais:value>
                        <iais:value width="4" display="true">
                            <c:out value="${arSuperDataSubmissionDtoVersion.pgtStageDto.otherEmbryosBiopsiedAddr}"/>
                        </iais:value>
                    </iais:row>
                </div>

                <iais:row>
                    <iais:field width="4" value="Biopsy Done By"/>
                    <iais:value width="4" display="true">
                        <c:out value="${arSuperDataSubmissionDto.pgtStageDto.isBiopsyLocal}"/>
                    </iais:value>
                    <iais:value width="4" display="true">
                        <c:out value="${arSuperDataSubmissionDtoVersion.pgtStageDto.isBiopsyLocal}"/>
                    </iais:value>
                </iais:row>
                <div id="othersBiopsyDisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isBiopsyLocal !='Others' && arSuperDataSubmissionDtoVersion.pgtStageDto.isBiopsyLocal !='Others' }">style="display: none"</c:if> >
                    <iais:row>
                        <iais:field width="4" value="Biopsy Done By (Others)"/>
                        <iais:value width="4" display="true">
                            <c:out value="${arSuperDataSubmissionDto.pgtStageDto.otherBiopsyAddr}"/>
                        </iais:value>
                        <iais:value width="4" display="true">
                            <c:out value="${arSuperDataSubmissionDtoVersion.pgtStageDto.otherBiopsyAddr}"/>
                        </iais:value>
                    </iais:row>
                </div>

            </div>
        </div>
    </div>
</div>