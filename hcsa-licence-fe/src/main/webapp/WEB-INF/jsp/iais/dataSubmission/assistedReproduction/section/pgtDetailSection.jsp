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
                    <div class="col-md-6" style="padding-right: 0;padding-left: 0;">
                        <iais:value width="6" cssClass="col-md-6" style="padding-right: 0;padding-left: 0;">
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
                        <iais:value width="6" cssClass="col-md-6" style="padding-right: 0;padding-left: 0;">
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

                        <iais:value width="6" cssClass="col-md-6" style="padding-right: 0;padding-left: 0;">
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
                        <iais:value width="6" cssClass="col-md-6" style="padding-right: 0;padding-left: 0;">
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
                        <iais:value width="6" cssClass="col-md-6" style="padding-right: 0;padding-left: 0;">
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
                        <iais:value width="6" cssClass="col-md-6" style="padding-right: 0;padding-left: 0;">
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
                            <span class="error-msg" name="iaisErrorMsg" id="error_pgt_type" style="padding-right: 15px;padding-left: 15px;"></span>
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
                <div id="pgtMDisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMCom !=1 && arSuperDataSubmissionDto.pgtStageDto.isPgtMRare !=1}">style="display: none"</c:if> >
                    <iais:row>
                        <iais:field width="5" value="PGT-M" style="font-size: 2.0rem;" />
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="PGT-M Performed" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7" style="padding-right: 0;padding-left: 0;">
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
                            <span class="error-msg" name="iaisErrorMsg" id="error_pgt_m_performed" style="padding-right: 15px;padding-left: 15px;"></span>
                        </iais:value>
                    </iais:row>
                    <div id="pgtMRefNoDisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMDsld !=1 }">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="5" value="PGT-M Appeal Reference No." />
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
                <div id="pgtMComSubsidies" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMCom !=1 }">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Please indicate if co-funding was provided for PGT-M (Common)" mandatory="true"/>
                        <div class="col-md-6" style="padding-right: 0;padding-left: 0;">
                            <iais:value width="4" cssClass="col-md-4" style="padding-right: 0;padding-left: 0;">
                                <div class="form-check">
                                    <input class="form-check-input"
                                           type="radio"
                                           name="isPgtMComCoFunding"
                                           value="1"
                                           id="radioYes"
                                           <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtCoFunding ==1 }">checked</c:if>
                                           aria-invalid="false">
                                    <label class="form-check-label"
                                           for="radioYes"><span
                                            class="check-circle"></span>Yes</label>
                                </div>
                            </iais:value>
                            <iais:value width="4" cssClass="col-md-4" style="padding-right: 0;padding-left: 0;">
                                <div class="form-check">
                                    <input class="form-check-input" type="radio"
                                           name="isPgtMComCoFunding" value="0" id="radioNo"
                                           <c:if test="${arSuperDataSubmissionDto.pgtStageDto.isPgtCoFunding == 0}">checked</c:if>
                                           aria-invalid="false">
                                    <label class="form-check-label"
                                           for="radioNo"><span
                                            class="check-circle"></span>No</label>
                                </div>
                            </iais:value>
                            <iais:value width="4" cssClass="col-md-4" style="padding-right: 0;padding-left: 0;">
                                <div class="form-check">
                                    <input class="form-check-input" type="radio"
                                           name="isPgtMComCoFunding" value="2" id="radioNA"
                                           <c:if test="${arSuperDataSubmissionDto.pgtStageDto.isPgtCoFunding == 2}">checked</c:if>
                                           aria-invalid="false">
                                    <label class="form-check-label"
                                           for="radioNA"><span
                                            class="check-circle"></span>N/A</label>
                                </div>
                            </iais:value>
                            <span class="error-msg" name="iaisErrorMsg" id="error_isPgtCoFunding" style="padding-right: 15px;padding-left: 15px;"></span>
                        </div>
                    </iais:row>
                </div>
                <div id="pgtMRareSubsidies" <c:if test="${arSuperDataSubmissionDto.pgtStageDto.isPgtMRare ne 1}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Please indicate if co-funding was provided for PGT-M (Rare)" mandatory="true"/>
                        <div class="col-md-6" style="padding-right: 0;padding-left: 0;">
                            <iais:value width="4" cssClass="col-md-4" style="padding-right: 0;padding-left: 0;">
                                <div class="form-check">
                                    <input class="form-check-input"
                                           type="radio"
                                           name="isPgtMRareCoFunding"
                                           value="1"
                                           id="radioRareYes"
                                           <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtMRareCoFunding ==1 }">checked</c:if>
                                           aria-invalid="false">
                                    <label class="form-check-label"
                                           for="radioRareYes"><span
                                            class="check-circle"></span>Yes</label>
                                </div>
                            </iais:value>
                            <iais:value width="4" cssClass="col-md-4" style="padding-right: 0;padding-left: 0;">
                                <div class="form-check">
                                    <input class="form-check-input" type="radio"
                                           name="isPgtMRareCoFunding" value="0" id="radioRareNo"
                                           <c:if test="${arSuperDataSubmissionDto.pgtStageDto.isPgtMRareCoFunding == 0}">checked</c:if>
                                           aria-invalid="false">
                                    <label class="form-check-label"
                                           for="radioRareNo"><span
                                            class="check-circle"></span>No</label>
                                </div>
                            </iais:value>
                            <iais:value width="4" cssClass="col-md-4" style="padding-right: 0;padding-left: 0;">
                                <div class="form-check">
                                    <input class="form-check-input" type="radio"
                                           name="isPgtMRareCoFunding" value="2" id="radioRareNA"
                                           <c:if test="${arSuperDataSubmissionDto.pgtStageDto.isPgtMRareCoFunding == 2}">checked</c:if>
                                           aria-invalid="false">
                                    <label class="form-check-label"
                                           for="radioRareNA"><span
                                            class="check-circle"></span>N/A</label>
                                </div>
                            </iais:value>
                            <span class="error-msg" name="iaisErrorMsg" id="error_isPgtMRareCoFunding" style="padding-right: 15px;padding-left: 15px;"></span>
                        </div>
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
                    <iais:row>
                        <iais:field width="5" value="Please indicate if co-funding was provided for PGT-SR" mandatory="true"/>
                        <div class="col-md-6" style="padding-right: 0;padding-left: 0;">
                            <iais:value width="4" cssClass="col-md-4" style="padding-right: 0;padding-left: 0;">
                                <div class="form-check">
                                    <input class="form-check-input"
                                           type="radio"
                                           name="isPgtSrCoFunding"
                                           value="1"
                                           id="radioSrYes"
                                           <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtSrCoFunding ==1 }">checked</c:if>
                                           aria-invalid="false">
                                    <label class="form-check-label"
                                           for="radioSrYes"><span
                                            class="check-circle"></span>Yes</label>
                                </div>
                            </iais:value>
                            <iais:value width="4" cssClass="col-md-4" style="padding-right: 0;padding-left: 0;">
                                <div class="form-check">
                                    <input class="form-check-input" type="radio"
                                           name="isPgtSrCoFunding" value="0" id="radioSrNo"
                                           <c:if test="${arSuperDataSubmissionDto.pgtStageDto.isPgtSrCoFunding == 0}">checked</c:if>
                                           aria-invalid="false">
                                    <label class="form-check-label"
                                           for="radioSrNo"><span
                                            class="check-circle"></span>No</label>
                                </div>
                            </iais:value>
                            <iais:value width="4" cssClass="col-md-4" style="padding-right: 0;padding-left: 0;">
                                <div class="form-check">
                                    <input class="form-check-input" type="radio"
                                           name="isPgtSrCoFunding" value="2" id="radioSrNA"
                                           <c:if test="${arSuperDataSubmissionDto.pgtStageDto.isPgtSrCoFunding == 2}">checked</c:if>
                                           aria-invalid="false">
                                    <label class="form-check-label"
                                           for="radioSrNA"><span
                                            class="check-circle"></span>N/A</label>
                                </div>
                            </iais:value>
                            <span class="error-msg" name="iaisErrorMsg" id="error_isPgtSrCoFunding" style="padding-right: 15px;padding-left: 15px;"></span>
                        </div>
                    </iais:row>
                </div>

                <div id="pgtADisplay" <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtA !=1 }">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="PGT-A" style="font-size: 2.0rem;" />
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="PGT-A Performed Because of" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7" style="padding-right: 0;padding-left: 0;">
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
                            <span class="error-msg" name="iaisErrorMsg" id="error_PerformedBecause" style="padding-right: 15px;padding-left: 15px;"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="What was the Result of the PGT-A Test?" mandatory="true"/>
                        <div class="col-md-6" style="padding-right: 0;padding-left: 0;">
                            <iais:value width="6" cssClass="col-md-6" style="padding-right: 0;padding-left: 0;">
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
                            <iais:value width="6" cssClass="col-md-6" style="padding-right: 0;padding-left: 0;">
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
                            <span class="error-msg" name="iaisErrorMsg" id="error_PGTAResult" style="padding-right: 15px;padding-left: 15px;"></span>
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
                    <iais:row>
                        <iais:field width="5" value="Please indicate if co-funding was provided for PGT-A" mandatory="true"/>
                        <div class="col-md-6" style="padding-right: 0;padding-left: 0;">
                            <iais:value width="4" cssClass="col-md-4" style="padding-right: 0;padding-left: 0;">
                                <div class="form-check">
                                    <input class="form-check-input"
                                           type="radio"
                                           name="isPgtACoFunding"
                                           value="1"
                                           id="radioAYes"
                                           <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtACoFunding ==1 }">checked</c:if>
                                           aria-invalid="false">
                                    <label class="form-check-label"
                                           for="radioAYes"><span
                                            class="check-circle"></span>Yes</label>
                                </div>
                            </iais:value>
                            <iais:value width="4" cssClass="col-md-4" style="padding-right: 0;padding-left: 0;">
                                <div class="form-check">
                                    <input class="form-check-input" type="radio"
                                           name="isPgtACoFunding" value="0" id="radioANo"
                                           <c:if test="${arSuperDataSubmissionDto.pgtStageDto.isPgtACoFunding == 0}">checked</c:if>
                                           aria-invalid="false">
                                    <label class="form-check-label"
                                           for="radioANo"><span
                                            class="check-circle"></span>No</label>
                                </div>
                            </iais:value>
                            <iais:value width="4" cssClass="col-md-4" style="padding-right: 0;padding-left: 0;">
                                <div class="form-check">
                                    <input class="form-check-input" type="radio"
                                           name="isPgtACoFunding" value="2" id="radioANA"
                                           <c:if test="${arSuperDataSubmissionDto.pgtStageDto.isPgtACoFunding == 2}">checked</c:if>
                                           aria-invalid="false">
                                    <label class="form-check-label"
                                           for="radioANA"><span
                                            class="check-circle"></span>N/A</label>
                                </div>
                            </iais:value>
                            <span class="error-msg" name="iaisErrorMsg" id="error_isPgtACoFunding" style="padding-right: 15px;padding-left: 15px;"></span>
                        </div>
                    </iais:row>
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
                    <iais:row>
                        <iais:field width="5" value="Please indicate if co-funding was provided for PTT" mandatory="true"/>
                        <div class="col-md-6" style="padding-right: 0;padding-left: 0;">
                            <iais:value width="4" cssClass="col-md-4" style="padding-right: 0;padding-left: 0;">
                                <div class="form-check">
                                    <input class="form-check-input"
                                           type="radio"
                                           name="isPttCoFunding"
                                           value="1"
                                           id="radioPttYes"
                                           <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPttCoFunding ==1 }">checked</c:if>
                                           aria-invalid="false">
                                    <label class="form-check-label"
                                           for="radioPttYes"><span
                                            class="check-circle"></span>Yes</label>
                                </div>
                            </iais:value>
                            <iais:value width="4" cssClass="col-md-4" style="padding-right: 0;padding-left: 0;">
                                <div class="form-check">
                                    <input class="form-check-input" type="radio"
                                           name="isPttCoFunding" value="0" id="radioPttNo"
                                           <c:if test="${arSuperDataSubmissionDto.pgtStageDto.isPttCoFunding == 0}">checked</c:if>
                                           aria-invalid="false">
                                    <label class="form-check-label"
                                           for="radioPttNo"><span
                                            class="check-circle"></span>No</label>
                                </div>
                            </iais:value>
                            <iais:value width="4" cssClass="col-md-4" style="padding-right: 0;padding-left: 0;">
                                <div class="form-check">
                                    <input class="form-check-input" type="radio"
                                           name="isPttCoFunding" value="2" id="radioPttNA"
                                           <c:if test="${arSuperDataSubmissionDto.pgtStageDto.isPttCoFunding == 2}">checked</c:if>
                                           aria-invalid="false">
                                    <label class="form-check-label"
                                           for="radioPttNA"><span
                                            class="check-circle"></span>N/A</label>
                                </div>
                            </iais:value>
                            <span class="error-msg" name="iaisErrorMsg" id="error_isPttCoFunding" style="padding-right: 15px;padding-left: 15px;"></span>
                        </div>
                    </iais:row>
                </div>

                <div id="appealDisplay" <c:if test="${ count<6 || arSuperDataSubmissionDto.pgtStageDto.isPgtMCom !=1 && arSuperDataSubmissionDto.pgtStageDto.isPgtMRare !=1 && arSuperDataSubmissionDto.pgtStageDto.isPgtSr !=1 || arSuperDataSubmissionDto.pgtStageDto.isPgtCoFunding == 0}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Is there an Appeal?" mandatory="true"/>
                        <div class="col-md-6" style="padding-right: 0;padding-left: 0;">
                            <iais:value width="6" cssClass="col-md-6" style="padding-right: 0;padding-left: 0;">
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
                            <iais:value width="6" cssClass="col-md-6" style="padding-right: 0;padding-left: 0;">
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
                            <span class="error-msg" name="iaisErrorMsg" id="error_isThereAppeal" style="padding-right: 15px;padding-left: 15px;"></span>
                        </div>
                    </iais:row>
                </div>

                <iais:row>
                    <iais:field width="5" value="Embryos were Biospied At" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <select name="isEmbryosBiopsiedLocal" id="isEmbryosBiopsiedLocal" class="isEmbryosBiopsiedLocalSel">
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
                        <select name="isBiopsyLocal" id="isBiopsyLocal" class="isBiopsyLocalSel">
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

                <%@include file="hasDisposalRow.jsp"%>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/pgtSection.js"></script>
