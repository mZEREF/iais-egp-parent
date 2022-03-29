<input type="hidden" name="pgtCount" id="pgtCount" value="${count}"/>

<div class="panel panel-default">
    <div class="panel-heading" style="padding-left: 90px;">
        <h4 class="panel-title">
            <strong>
                Preimplantation Genetic Testing
            </strong>
        </h4>
    </div>
    <div id="pftDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <h3>
                    <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                </h3>
                <iais:row>
                    <iais:field width="5" value="Types of Preimplantation Genetic Testing" mandatory="true"/>
                    <div class="col-md-6">
                        <iais:value width="6" cssClass="col-md-6">
                            <div class="form-check">
                                <input class="form-check-input"
                                       type="checkbox"
                                       name="isPgtMCom"
                                       id="isPgtMCom"
                                       <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMCom ==1 }">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="isPgtMCom"><span
                                        class="check-square"></span><iais:code code="PGTTP001"/></label>
                            </div>
                        </iais:value>
                        <iais:value width="6" cssClass="col-md-6">
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox"
                                       name="isPgtMRare" id="isPgtMRare"
                                       <c:if test="${arSuperDataSubmissionDto.pgtStageDto.isPgtMRare == 1}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="isPgtMRare"><span
                                        class="check-square"></span><iais:code code="PGTTP002"/></label>
                            </div>
                        </iais:value>
                        <iais:value width="6" cssClass="col-md-6">
                            <div class="form-check">
                                <input class="form-check-input"
                                       type="checkbox"
                                       name="isPgtMEbt"
                                       id="isPgtMEbt"
                                       <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMEbt ==1 }">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="isPgtMEbt"><span
                                        class="check-square"></span><iais:code code="PGTTP003"/></label>
                            </div>
                        </iais:value>
                        <iais:value width="6" cssClass="col-md-6">
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox"
                                       name="isPgtSr" id="isPgtSr"
                                       <c:if test="${arSuperDataSubmissionDto.pgtStageDto.isPgtSr == 1}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="isPgtSr"><span
                                        class="check-square"></span><iais:code code="PGTTP004"/></label>
                            </div>
                        </iais:value>
                        <iais:value width="6" cssClass="col-md-6">
                            <div class="form-check">
                                <input class="form-check-input"
                                       type="checkbox"
                                       name="isPgtA"
                                       id="isPgtA"
                                       <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtA ==1 }">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="isPgtA"><span
                                        class="check-square"></span><iais:code code="PGTTP005"/></label>
                            </div>
                        </iais:value>
                        <iais:value width="6" cssClass="col-md-6">
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox"
                                       name="isPtt" id="isPtt"
                                       <c:if test="${arSuperDataSubmissionDto.pgtStageDto.isPtt == 1}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="isPtt"><span
                                        class="check-square"></span><iais:code code="PGTTP006"/></label>
                            </div>
                        </iais:value>
                        <iais:value width="6" cssClass="col-md-6">
                            <div class="form-check">
                                <input class="form-check-input"
                                       type="checkbox"
                                       name="isOtherPgt"
                                       id="isOtherPgt"
                                       <c:if test="${  arSuperDataSubmissionDto.pgtStageDto.isOtherPgt ==1 }">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="isOtherPgt"><span
                                        class="check-square"></span><iais:code code="PGTTP007"/></label>
                            </div>
                            <span class="error-msg" name="iaisErrorMsg" id="error_pgt_type"></span>
                        </iais:value>
                    </div>

                </iais:row>
                <div id="pgtOthersDisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isOtherPgt !=1 }">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Others" style="font-size: 2.0rem;" />
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Other Types of Genetic Testing" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <input type="text" maxlength="100"   name="otherPgt" value="${arSuperDataSubmissionDto.pgtStageDto.otherPgt}" >
                            <span class="error-msg" name="iaisErrorMsg" id="error_otherPgt"></span>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="pgtMDisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMCom !=1 && arSuperDataSubmissionDto.pgtStageDto.isPgtMRare !=1 && arSuperDataSubmissionDto.pgtStageDto.isPgtMEbt !=1 }">style="display: none"</c:if> >
                    <iais:row>
                        <iais:field width="5" value="PGT-M" style="font-size: 2.0rem;" />
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="PGT-M Performed" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <div class="form-check">
                                <input class="form-check-input"
                                       type="checkbox"
                                       name="isPgtMDsld"
                                       id="isPgtMDsld"
                                       <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMDsld ==1 }">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="isPgtMDsld"><span
                                        class="check-square"></span>To detect Sex-linked Disease</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input"
                                       type="checkbox"
                                       name="isPgtMWithHla"
                                       id="isPgtMWithHla"
                                       <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMWithHla ==1 }">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="isPgtMWithHla"><span
                                        class="check-square"></span>Together with HLA Matching</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input"
                                       type="checkbox"
                                       name="isPgtMNon"
                                       id="isPgtMNon"
                                       <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMNon ==1 }">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="isPgtMNon"><span
                                        class="check-square"></span>None of the above</label>
                            </div>
                            <span class="error-msg" name="iaisErrorMsg" id="error_pgt_m_performed"></span>
                        </iais:value>
                    </iais:row>
                    <div id="pgtMRefNoDisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMDsld !=1 }">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="5" value="PGT-M Appeal Reference No." mandatory="true"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <input type="text" maxlength="19"   name="pgtMRefNo" value="${arSuperDataSubmissionDto.pgtStageDto.pgtMRefNo}" >
                                <span class="error-msg" name="iaisErrorMsg" id="error_pgt_m_ref_no"></span>
                            </iais:value>
                        </iais:row>
                    </div>

                    <iais:row>
                        <iais:field width="5" value="What Condition and Gene was PGT-M Performed to Detect For?" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <input type="text" maxlength="100"   name="pgtMCondition" value="${arSuperDataSubmissionDto.pgtStageDto.pgtMCondition}" >
                            <span class="error-msg" name="iaisErrorMsg" id="error_PgtMCondition"></span>
                        </iais:value>
                    </iais:row>
                </div>

                <div id="pgtSrDisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtSr !=1 }">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="PGT-SR" style="font-size: 2.0rem;" />
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="What Condition and Structural Rearrangement was PGT-SR Performed For?" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <input type="text" maxlength="100"   name="pgtSrCondition" value="${arSuperDataSubmissionDto.pgtStageDto.pgtSrCondition}" >
                            <span class="error-msg" name="iaisErrorMsg" id="error_PgtSrCondition"></span>
                        </iais:value>
                    </iais:row>
                </div>

                <div id="pgtADisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtA !=1 }">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="PGT-A" style="font-size: 2.0rem;" />
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="PGT-A Performed Because of" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <div class="form-check">
                                <input class="form-check-input"
                                       type="checkbox"
                                       name="isPgtAAma"
                                       id="isPgtAAma"
                                       <c:if test="${arSuperDataSubmissionDto.pgtStageDto.isPgtAAma ==1 }">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="isPgtAAma"><span
                                        class="check-square"></span>Advanced Maternal Age</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input"
                                       type="checkbox"
                                       name="isPgtATomrif"
                                       id="isPgtATomrif"
                                       <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtATomrif ==1 }">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="isPgtATomrif"><span
                                        class="check-square"></span>Two Or More Recurrent Implantation Failure</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input"
                                       type="checkbox"
                                       name="isPgtATomrpl"
                                       id="isPgtATomrpl"
                                       <c:if test="${arSuperDataSubmissionDto.pgtStageDto.isPgtATomrpl ==1 }">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="isPgtATomrpl"><span
                                        class="check-square"></span>Two Or More Repeated Pregnancy Losses</label>
                            </div>
                            <span class="error-msg" name="iaisErrorMsg" id="error_PerformedBecause"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="What was the Result of the PGT-A Test?" mandatory="true"/>
                        <div class="col-md-6">
                            <iais:value width="6" cssClass="col-md-6">
                                <div class="form-check">
                                    <input class="form-check-input"
                                           type="radio"
                                           name="pgtAResult"
                                           value="Normal"
                                           id="radioNormal"
                                           <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.pgtAResult =='Normal' }">checked</c:if>
                                           aria-invalid="false">
                                    <label class="form-check-label"
                                           for="radioNormal"><span
                                            class="check-circle"></span>Normal</label>
                                </div>
                            </iais:value>
                            <iais:value width="6" cssClass="col-md-6">
                                <div class="form-check">
                                    <input class="form-check-input" type="radio"
                                           name="pgtAResult" value="Abnormal" id="radioAbnormal"
                                           <c:if test="${arSuperDataSubmissionDto.pgtStageDto.pgtAResult == 'Abnormal'}">checked</c:if>
                                           aria-invalid="false">
                                    <label class="form-check-label"
                                           for="radioAbnormal"><span
                                            class="check-circle"></span>Abnormal</label>
                                </div>
                            </iais:value>
                            <span class="error-msg" name="iaisErrorMsg" id="error_PGTAResult"></span>
                        </div>

                    </iais:row>
                    <div id="AbnormalDisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.pgtAResult !='Abnormal' }">style="display: none"</c:if> >
                        <iais:row>
                            <iais:field width="5" value="What Abnormal Condition was Found for the PGT-A Test?" mandatory="true"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <input type="text" maxlength="100"   name="pgtACondition" value="${arSuperDataSubmissionDto.pgtStageDto.pgtACondition}" >
                                <span class="error-msg" name="iaisErrorMsg" id="error_PgtACondition"></span>
                            </iais:value>
                        </iais:row>
                    </div>
                </div>

                <div id="pttDisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPtt !=1 }">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="PTT" style="font-size: 2.0rem;" />
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="What Condition was PTT Perfomed to Detect" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <input type="text" maxlength="20"   name="pttCondition" value="${arSuperDataSubmissionDto.pgtStageDto.pttCondition}" >
                            <span class="error-msg" name="iaisErrorMsg" id="error_pttCondition"></span>
                        </iais:value>
                    </iais:row>

                </div>

                <iais:row>
                    <iais:field width="5" value="Please indicate Preimplantation Genetic Testing Co-funding?" mandatory="true"/>
                    <div class="col-md-6">
                        <iais:value width="3" cssClass="col-md-3">
                            <div class="form-check">
                                <input class="form-check-input"
                                       type="radio"
                                       name="isPgtCoFunding"
                                       value="1"
                                       id="radioYes"
                                       <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtCoFunding ==1 }">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="radioYes"><span
                                        class="check-circle"></span>Yes</label>
                            </div>
                        </iais:value>
                        <iais:value width="3" cssClass="col-md-3">
                            <div class="form-check">
                                <input class="form-check-input" type="radio"
                                       name="isPgtCoFunding" value="0" id="radioNo"
                                       <c:if test="${arSuperDataSubmissionDto.pgtStageDto.isPgtCoFunding == 0}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="radioNo"><span
                                        class="check-circle"></span>No</label>
                            </div>
                        </iais:value>
                        <span class="error-msg" name="iaisErrorMsg" id="error_isPgtCoFunding"></span>
                    </div>
                </iais:row>
                <div id="appealDisplay" <c:if test="${ count<6 || arSuperDataSubmissionDto.pgtStageDto.isPgtMCom !=1 && arSuperDataSubmissionDto.pgtStageDto.isPgtMRare !=1 && arSuperDataSubmissionDto.pgtStageDto.isPgtMEbt !=1 && arSuperDataSubmissionDto.pgtStageDto.isPgtSr !=1 || arSuperDataSubmissionDto.pgtStageDto.isPgtCoFunding == 0}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Is there an Appeal?" mandatory="true"/>
                        <div class="col-md-6">
                            <iais:value width="3" cssClass="col-md-3">
                                <div class="form-check">
                                    <input class="form-check-input"
                                           type="radio"
                                           name="isThereAppeal"
                                           value="1"
                                           id="appealYes"
                                           <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isThereAppeal ==1 }">checked</c:if>
                                           aria-invalid="false">
                                    <label class="form-check-label"
                                           for="appealYes"><span
                                            class="check-circle"></span>Yes</label>
                                </div>
                            </iais:value>
                            <iais:value width="3" cssClass="col-md-3">
                                <div class="form-check">
                                    <input class="form-check-input" type="radio"
                                           name="isThereAppeal" value="0" id="appealNo"
                                           <c:if test="${arSuperDataSubmissionDto.pgtStageDto.isThereAppeal == 0}">checked</c:if>
                                           aria-invalid="false">
                                    <label class="form-check-label"
                                           for="appealNo"><span
                                            class="check-circle"></span>No</label>
                                </div>
                            </iais:value>
                            <span class="error-msg" name="iaisErrorMsg" id="error_isThereAppeal"></span>
                        </div>
                    </iais:row>
                </div>

                <iais:row>
                    <iais:field width="5" value="Embryos were Biospied At" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <select name="isEmbryosBiopsiedLocal" id="isEmbryosBiopsiedLocal">
                            <c:forEach items="${embryosBiopsiedLocalSelectOption}" var="selectOption">
                                <option value="${selectOption.value}" <c:if test="${arSuperDataSubmissionDto.pgtStageDto.isEmbryosBiopsiedLocal ==selectOption.value}">selected="selected"</c:if>>${selectOption.text}</option>
                            </c:forEach>
                            <option value="Others" <c:if test="${arSuperDataSubmissionDto.pgtStageDto.isEmbryosBiopsiedLocal =='Others'}">selected="selected"</c:if>>Others</option>
                        </select>
                        <span class="error-msg" name="iaisErrorMsg" id="error_isEmbryosBiopsiedLocal"></span>
                    </iais:value>
                </iais:row>
                <div id="othersEmbryosBiopsyDisplay" <c:if test="${arSuperDataSubmissionDto.pgtStageDto.isEmbryosBiopsiedLocal!='Others'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Other Centre where Embryos were Biospied At" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <input type="text" maxlength="20"   name="otherEmbryosBiopsiedAddr" value="${arSuperDataSubmissionDto.pgtStageDto.otherEmbryosBiopsiedAddr}" >
                            <span class="error-msg" name="iaisErrorMsg" id="error_otherEmbryosBiopsiedAddr"></span>
                        </iais:value>
                    </iais:row>
                </div>

                <iais:row>
                    <iais:field width="5" value="Biopsy Done By" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <select name="isBiopsyLocal" id="isBiopsyLocal">
                            <option value="" <c:if test="${empty arSuperDataSubmissionDto.pgtStageDto.isBiopsyLocal}">selected="selected"</c:if>>Please Select</option>
                            <c:forEach items="${biopsyLocalSelectOption}" var="selectOption">
                                <option value="${selectOption.value}" <c:if test="${arSuperDataSubmissionDto.pgtStageDto.isBiopsyLocal ==selectOption.value}">selected="selected"</c:if>>${selectOption.text}</option>
                            </c:forEach>
                            <option value="Others" <c:if test="${arSuperDataSubmissionDto.pgtStageDto.isBiopsyLocal =='Others'}">selected="selected"</c:if>>Others</option>
                        </select>
                        <span class="error-msg" name="iaisErrorMsg" id="error_isBiopsyLocal"></span>
                    </iais:value>
                </iais:row>
                <div id="othersBiopsyDisplay" <c:if test="${arSuperDataSubmissionDto.pgtStageDto.isBiopsyLocal!='Others'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Biopsy Done By (Others)" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <input type="text" maxlength="20"   name="otherBiopsyAddr" value="${arSuperDataSubmissionDto.pgtStageDto.otherBiopsyAddr}" >
                            <span class="error-msg" name="iaisErrorMsg" id="error_otherBiopsyAddr"></span>
                        </iais:value>
                    </iais:row>
                </div>

            </div>
        </div>
    </div>
</div>

<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/pgtSection.js"></script>
