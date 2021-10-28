<%--<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/pgtSection.js"></script>--%>

<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                Preimplantation Genetic Testing
            </strong>
        </h4>
    </div>
    <div id="pftDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <iais:row>
                    <iais:field width="6" value="Types of Preimplantation Genetic Testing" mandatory="true"/>
                    <iais:value width="3" cssClass="col-md-3">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="checkbox"
                                   name="isPgtM"
                                   id="isPgtM"
                                   <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtM ==1 }">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="isPgtM"><span
                                    class="check-square"></span>PGT-M</label>
                        </div>
                    </iais:value>
                    <iais:value width="3" cssClass="col-md-3">
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox"
                                   name="isPgtSr" id="isPgtSr"
                                   <c:if test="${arSuperDataSubmissionDto.pgtStageDto.isPgtSr == 1}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="isPgtSr"><span
                                    class="check-square"></span>PGT-SR</label>
                        </div>
                    </iais:value>
                    <iais:value width="3" cssClass="col-md-3">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="checkbox"
                                   name="isPgtA"
                                   id="isPgtA"
                                   <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPgtA ==1 }">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="isPgtA"><span
                                    class="check-square"></span>PGT-A</label>
                        </div>
                    </iais:value>
                    <iais:value width="3" cssClass="col-md-3">
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox"
                                   name="isPtt" id="isPtt"
                                   <c:if test="${arSuperDataSubmissionDto.pgtStageDto.isPtt == 1}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="isPtt"><span
                                    class="check-square"></span>PTT</label>
                        </div>
                    </iais:value>
                    <iais:field width="6" value="" />
                    <iais:value width="3" cssClass="col-md-3">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="checkbox"
                                   name="isOtherPgt"
                                   id="isOtherPgt"
                                   <c:if test="${  arSuperDataSubmissionDto.pgtStageDto.isOtherPgt ==1 }">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="isOtherPgt"><span
                                    class="check-square"></span>Others</label>
                        </div>
                    </iais:value>
                </iais:row>

                <div id="pgtMDisplay" style="display: none">
                    <iais:row>
                        <iais:field width="6" value="PGT-M" />
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="PGT-M Performed" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
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
                        </iais:value>
                        <iais:field width="6" value="" />
                        <iais:value width="6" cssClass="col-md-6">
                            <div class="form-check">
                                <input class="form-check-input"
                                       type="checkbox"
                                       name="isPftMWithHla"
                                       id="isPftMWithHla"
                                       <c:if test="${ arSuperDataSubmissionDto.pgtStageDto.isPftMWithHla ==1 }">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="isPftMWithHla"><span
                                        class="check-square"></span>Together with HLA Matching</label>
                            </div>
                        </iais:value>
                        <iais:field width="6" value="" />
                        <iais:value width="6" cssClass="col-md-6">
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
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="PGT-M Appeal Reference No." mandatory="false"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <input type="text" maxlength="20"   name="pgtMRefNo" value="${arSuperDataSubmissionDto.pgtStageDto.pgtMRefNo}" >

                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="What Condition was PGT-M Performed to Detect?" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <input type="text" maxlength="20"   name="pgtMCondition" value="${arSuperDataSubmissionDto.pgtStageDto.pgtMCondition}" >
                        </iais:value>
                    </iais:row>
                </div>

                <div id="pgtSrDisplay" style="display: none">
                    <iais:row>
                        <iais:field width="6" value="What Condition and Structural Rearrangement was PGT-SR Performed?" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <input type="text" maxlength="20"   name="pgtSrCondition" value="${arSuperDataSubmissionDto.pgtStageDto.pgtSrCondition}" >
                        </iais:value>
                    </iais:row>
                </div>

                <div id="pgtADisplay" style="display: none">
                    <iais:row>
                        <iais:field width="6" value="PGT-A Performed Because of" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
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
                        </iais:value>
                        <iais:field width="6" value="" />
                        <iais:value width="6" cssClass="col-md-6">
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
                        </iais:value>
                        <iais:field width="6" value="" />
                        <iais:value width="6" cssClass="col-md-6">
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
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="What was the Result of the PGT-A Test?" mandatory="true"/>
                        <iais:value width="3" cssClass="col-md-3">
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
                        <iais:value width="3" cssClass="col-md-3">
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
                    </iais:row>
                    <div id="AbnormalDisplay" style="display: none">
                        <iais:row>
                            <iais:field width="6" value="What Abnormal Condition was Found for the PGT-A Test?" mandatory="true"/>
                            <iais:value width="6" cssClass="col-md-6">
                                <input type="text" maxlength="20"   name="pgtACondition" value="${arSuperDataSubmissionDto.pgtStageDto.pgtACondition}" >
                            </iais:value>
                        </iais:row>
                    </div>
                </div>

                <div id="pttDisplay" style="display: none">
                    <iais:row>
                        <iais:field width="6" value="What Condition was PTT Perfomed to Detect" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <input type="text" maxlength="20"   name="pttCondition" value="${arSuperDataSubmissionDto.pgtStageDto.pttCondition}" >
                        </iais:value>
                    </iais:row>

                </div>

                <iais:row>
                    <iais:field width="6" value="Please indicate Preimplantation Genetic Testing Co-funding?" mandatory="true"/>
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
                </iais:row>

                <iais:row>
                    <iais:field width="6" value="Embryos were Biospied At" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:select cssClass="isEmbryosBiopsiedLocal"  name="isEmbryosBiopsiedLocal" firstOption="Please Select" options="efoReasonSelectOption" value="${arSuperDataSubmissionDto.pgtStageDto.isEmbryosBiopsiedLocal}"></iais:select>
                    </iais:value>
                </iais:row>
                <div id="othersBiopsyDisplay" <c:if test="${arSuperDataSubmissionDto.pgtStageDto.isEmbryosBiopsiedLocal!='EFOR004'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="Other Centre where Embryos were Biospied At" mandatory="false"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <div id="otherEmbryosBiopsiedAddr"  >
                                <input type="text" maxlength="20"   name="otherEmbryosBiopsiedAddr" value="${arSuperDataSubmissionDto.pgtStageDto.otherEmbryosBiopsiedAddr}" >
                                <span class="error-msg" name="iaisErrorMsg" id="error_otherEmbryosBiopsiedAddr"></span>
                            </div>
                        </iais:value>
                    </iais:row>
                </div>

                <iais:row>
                    <iais:field width="6" value="Biopsy Done By" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:select cssClass="isBiopsyLocal"  name="isBiopsyLocal" firstOption="Please Select" options="efoReasonSelectOption" value="${arSuperDataSubmissionDto.pgtStageDto.isBiopsyLocal}"></iais:select>
                        <span class="error-msg" name="iaisErrorMsg" id="error_reason"></span>
                    </iais:value>
                </iais:row>
                <div id="othersBiopsyDisplay" <c:if test="${arSuperDataSubmissionDto.pgtStageDto.isBiopsyLocal!='EFOR004'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="Biopsy Done By (Others)" mandatory="false"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <div id="otherBiopsyAddr"  >
                                <input type="text" maxlength="20"   name="otherBiopsyAddr" value="${arSuperDataSubmissionDto.pgtStageDto.otherBiopsyAddr}" >
                                <span class="error-msg" name="iaisErrorMsg" id="error_otherBiopsyAddr"></span>
                            </div>
                        </iais:value>
                    </iais:row>
                </div>

            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        $('#isPgtM').change(function () {
            if($(this).is(':checked')){
                $('#pgtMDisplay').attr("style","display: block");
            }else {
                $('#pgtMDisplay').attr("style","display: none");
            }
        });

        $('#isPgtSr').change(function () {
            if($(this).is(':checked')){
                $('#pgtSrDisplay').attr("style","display: block");
            }else {
                $('#pgtSrDisplay').attr("style","display: none");
            }
        });

        $('#isPgtA').change(function () {
            if($(this).is(':checked')){
                $('#pgtADisplay').attr("style","display: block");
            }else {
                $('#pgtADisplay').attr("style","display: none");
            }
        });

        $('#isPtt').change(function () {
            if($(this).is(':checked')){
                $('#pttDisplay').attr("style","display: block");
            }else {
                $('#pttDisplay').attr("style","display: none");
            }
        });

        $("[name='pgtAResult']").change(function () {

            if($(this).val()=='Abnormal'){
                $('#AbnormalDisplay').attr("style","display: block");
            }else {
                $('#AbnormalDisplay').attr("style","display: none");
            }
        });
    });

</script>