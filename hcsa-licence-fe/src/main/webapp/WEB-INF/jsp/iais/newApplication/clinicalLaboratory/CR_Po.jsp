<input type="hidden" name="applicationType" value="${AppSubmissionDto.appType}"/>
<input type="hidden" name="rfiObj" value="<c:if test="${requestInformationConfig == null}">0</c:if><c:if test="${requestInformationConfig != null}">1</c:if>"/>
<div class="row">
</div>
<div class="row">
  <div class="col-xs-12">
    <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
      <div class="panel panel-default">
        <div class="panel-heading" id="headingPrincipal" role="tab">
          <h4 class="panel-title"><a role="button" class="" data-toggle="collapse" href="#collapsePrincipal" aria-expanded="true" aria-controls="collapsePrincipal">Principal Officer</a></h4>
        </div>
        <div class="panel-collapse collapse in" id="collapsePrincipal" role="tabpanel" aria-labelledby="headingPremise">

          <div class="panel-body">
            <div class="panel-main-content">
              <div class="" style="height: auto">
                <h2>Principal Officer</h2>
                <p><h4>A Principal Officer is responsible for overseeing the day-to-day operations of medical service</h4></p>
                <div class="row"></div>
              </div>
              <div class="po-content">
                <c:if test="${AppSubmissionDto.needEditController}">
                  <c:forEach var="clickEditPage" items="${AppSubmissionDto.clickEditPage}">
                    <c:if test="${'APPSPN04' == clickEditPage}">
                      <c:set var="isClickEdit" value="true"/>
                    </c:if>
                  </c:forEach>
                  <c:choose>
                    <c:when test="${!isClickEdit}">
                      <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
                    </c:when>
                    <c:otherwise>
                      <input id="isEditHiddenVal" type="hidden" name="isEdit" value="1"/>
                    </c:otherwise>
                  </c:choose>
                  <c:if test="${('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType) && requestInformationConfig == null}">
                      <p><div class="text-right app-font-size-16"><a class="back" id="RfcSkip">Skip<span>&nbsp;</span><em class="fa fa-angle-right"></em></a></div></p>
                  </c:if>
                  <c:if test="${!isClickEdit}">
                    <c:set var="showPreview" value="true"/>
                    <c:set var="canEdit" value="${AppSubmissionDto.appEditSelectDto.poEdit || AppSubmissionDto.appEditSelectDto.serviceEdit}"/>
                    <div class="<c:if test="${'true' != showPreview}">hidden</c:if>">
                      <c:choose>
                        <c:when test="${canEdit}">
                            <p><div class="text-right app-font-size-16"><a id="edit"><em class="fa fa-pencil-square-o"></em>Edit</a></div></p>
                        </c:when>
                        <c:otherwise>

                        </c:otherwise>
                      </c:choose>
                    </div>
                  </c:if>
                </c:if>
              </div>
              <c:if test="${PrincipalOfficersMandatory>0}">
                <c:set value="${poHcsaSvcPersonnelDto.mandatoryCount}" var="poMandatoryCount"/>
                <c:forEach begin="0" end="${PrincipalOfficersMandatory-1}" step="1" varStatus="status">
                  <c:if test="${ReloadPrincipalOfficers != null && ReloadPrincipalOfficers.size()>0}" >
                    <c:set var="principalOfficer" value="${ReloadPrincipalOfficers[status.index]}"/>
                  </c:if>
                  <c:set var="suffix" value="${status.index}" />
                  <div class="po-content">
                    <c:choose>
                      <c:when test="${principalOfficer.licPerson}">
                        <input type="hidden" name="poLicPerson" value="1"/>
                      </c:when>
                      <c:otherwise>
                        <input type="hidden" name="poLicPerson" value="0"/>
                      </c:otherwise>
                    </c:choose>
                    <input type="hidden" name="poExistingPsn" value="0"/>
                    <div class="row">
                      <div class="control control-caption-horizontal">
                        <div class=" form-group form-horizontal formgap">
                          <div class="col-sm-6 control-label formtext col-md-4">
                            <div class="cgo-header" style="font-size: 18px;">
                              <strong>Principal Officer <label class="assign-psn-item">${status.index+1}</label></strong>
                            </div>
                          </div>
                          <div class="col-sm-5 col-md-8 text-right" >
                            <c:if test="${status.index - poMandatoryCount >=0}">
                              <h4 class="text-danger"><em class="fa fa-times-circle removePoBtn cursorPointer"></em></h4>
                            </c:if>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div class="">
                      <div class="row">
                        <div class="control control-caption-horizontal">
                          <div class=" form-group form-horizontal formgap">
                            <div class="col-sm-6 control-label formtext col-md-4">
                              <label id="control--runtime--2--label" class="control-label control-set-font control-font-label">Assign a Principal Officer</label>
                              <span class="upload_controls"></span>
                            </div>
                            <div class="col-sm-5 col-md-8" id="assignSelect${suffix}">
                              <div class="">
                                <iais:select cssClass="poSelect"  name="poSelect" options="PrincipalOfficersAssignSelect"  value="${principalOfficer.assignSelect}" ></iais:select>
                                <div id="control--runtime--2--errorMsg_right" style="display: none;" class="error_placements"></div>
                                <span id="error_assignSelect${suffix}" name="iaisErrorMsg" class="error-msg"></span>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div class="principalOfficers hidden">
                      <div class="row">
                        <div class="control control-caption-horizontal">
                          <div class=" form-group form-horizontal formgap">
                            <div class="col-sm-3 control-label formtext col-md-4">
                              <label  class="control-label control-set-font control-font-label">Name</label>
                              <span class="mandatory">*</span>
                            </div>
                            <div class="col-sm-4" id="salutation${suffix}">
                              <iais:select cssClass="salutation"  name="salutation" codeCategory="CATE_ID_SALUTATION" value="${principalOfficer.salutation}" firstOption="Please Select"></iais:select>
                              <span class="error-msg" id="error_salutation${suffix}" name="iaisErrorMsg"></span>
                            </div>

                            <div class="col-sm-4">
                              <input autocomplete="off" name="name" maxlength="66" id="cr-po-name" type="text"  class="form-control control-input control-set-font control-font-normal" value="${principalOfficer.name}" >
                              <span class="error-msg" name="iaisErrorMsg" id="error_name${status.index}"></span>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div class="row">
                        <div class="control control-caption-horizontal">
                          <div class=" form-group form-horizontal formgap">
                            <div class="col-sm-3 control-label formtext col-md-4">
                              <label id="control--runtime--33--label" class="control-label control-set-font control-font-label">ID No.
                                <span class="mandatory">*</span>
                              </label>
                            </div>
                            <div class="col-sm-4">
                              <div class="" id="idType${suffix}">
                                <iais:select cssClass="idType"  name="idType"  value="${principalOfficer.idType}" options="IdTypeSelect"></iais:select>
                                <span class="error-msg" name="iaisErrorMsg" id="error_idType${status.index}"></span>
                              </div>
                            </div>
                            <div class="col-sm-4">
                              <input autocomplete="off" id="idType-idNo" name="idNo" type="text" maxlength="9"  class="idNoVal form-control control-input control-set-font control-font-normal" value="${principalOfficer.idNo}" >
                              <span class="error-msg" id="error_poNRICFIN${status.index}" name="iaisErrorMsg"></span>
                              <span class="error-msg" id="error_NRICFIN" name="iaisErrorMsg"></span>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div class="row">
                        <div class="control control-caption-horizontal">
                          <div class=" form-group form-horizontal formgap">
                            <div class="col-sm-3 control-label formtext col-md-4">
                            </div>
                            <div class="col-sm-8">
                              <div class="">
                                <span class="error-msg" name="iaisErrorMsg" id="error_poIdTypeNo${status.index}"></span>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div class="row">
                        <div class="control control-caption-horizontal">
                          <div class=" form-group form-horizontal formgap">
                            <div class="col-sm-3 control-label formtext col-md-4">
                              <label  class="control-label control-set-font control-font-label">Designation</label>
                              <span class="mandatory">*</span>
                            </div>
                            <div class="col-sm-5 col-md-8" id="designation${suffix}">
                              <iais:select cssClass="designation" name="designation" codeCategory="CATE_ID_DESIGNATION" value="${principalOfficer.designation}" firstOption="Please Select"></iais:select>
                            </div>
                            <div  class="col-sm-3  col-md-4"></div>
                            <div class="col-sm-5 col-md-8">
                              <span class="error-msg" name="iaisErrorMsg" id="error_designation${suffix}"></span>
                            </div>

                          </div>
                        </div>
                      </div>
                      <div class="row">
                        <div class="control control-caption-horizontal">
                          <div class=" form-group form-horizontal formgap">
                            <div class="col-sm-3 control-label formtext col-md-4">
                              <label  class="control-label control-set-font control-font-label">Mobile No.</label>
                              <span class="mandatory">*</span>
                            </div>
                            <div class="col-sm-4 col-md-8">
                              <input autocomplete="off" name="mobileNo" type="text"   maxlength="8" class="form-control control-input control-set-font control-font-normal" value="${principalOfficer.mobileNo}" >
                              <span class="error-msg"  name="iaisErrorMsg" id="error_mobileNo${status.index}"></span>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div class="row">
                        <div class="control control-caption-horizontal">
                          <div class=" form-group form-horizontal formgap">
                            <div class="col-sm-3 control-label formtext col-md-4">
                              <label  class="control-label control-set-font control-font-label">Office Telephone No.</label>
                              <span class="mandatory">*</span>
                            </div>
                            <div class="col-sm-4 col-md-8">
                              <input autocomplete="off" name="officeTelNo" type="text"  id="officeTelNo" maxlength="8" class="form-control control-input control-set-font control-font-normal" value="${principalOfficer.officeTelNo}" >
                              <span class="error-msg" name="iaisErrorMsg" id="error_officeTelNo${status.index}" ></span>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div class="row">
                        <div class="control control-caption-horizontal">
                          <div class=" form-group form-horizontal formgap">
                            <div class="col-sm-3 control-label formtext col-md-4">
                              <label  class="control-label control-set-font control-font-label">Email Address</label>
                              <span class="mandatory">*</span>
                            </div>
                            <div class="col-sm-4 col-md-8">
                              <input autocomplete="off" name="emailAddress" maxlength="66" type="text" id="emailAdress" class="form-control control-input control-set-font control-font-normal" value="${principalOfficer.emailAddr}" >
                              <span class="error-msg" name="iaisErrorMsg" id="error_emailAddr${status.index}" ></span>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </c:forEach>
              </c:if>
              <c:if test="${requestInformationConfig==null}">
                <c:set var="poDtoLength" value="${ReloadPrincipalOfficers.size()}"/>
                <c:if test="${poDtoLength == '0'}">
                  <c:set var="poDtoLength" value="1"/>
                </c:if>
                <c:set var="needAddPsn" value="true"/>
                <c:choose>
                  <c:when test="${poHcsaSvcPersonnelDto.status =='CMSTAT003'}">
                    <c:set var="needAddPsn" value="false"/>
                  </c:when>
                  <c:when test="${poDtoLength >= poHcsaSvcPersonnelDto.maximumCount}">
                    <c:set var="needAddPsn" value="false"/>
                  </c:when>
                </c:choose>
                <div class="row <c:if test="${!needAddPsn}">hidden</c:if>" id="addPsnDiv-po">
                  <div class="col-sm-4">
                    <span id="addPoBtn" style="color:deepskyblue;cursor:pointer;">+ Add Another Principal Officer</span>
                  </div>
                  <div  class="col-sm-5 col-md-5"><span class="poErrorMsg" style="color: red;"></span></div>
                </div>
              </c:if>
              <br/>
              <br/>
              <c:if test="${AppSubmissionDto.needEditController }">
                <c:forEach var="clickEditPage" items="${AppSubmissionDto.clickEditPage}">
                  <c:if test="${'APPSPN05' == clickEditPage}">
                    <c:set var="isClickEditDpo" value="true"/>
                  </c:if>
                </c:forEach>
                <c:choose>
                  <c:when test="${!isClickEditDpo}">
                    <input id="isEditDpoHiddenVal" type="hidden" name="isEditDpo" value="0"/>
                  </c:when>
                  <c:otherwise>
                    <input id="isEditDpoHiddenVal" type="hidden" name="isEditDpo" value="1"/>
                  </c:otherwise>
                </c:choose>
                <c:if test="${!isClickEditDpo}">
                  <c:set var="showPreview" value="true"/>
                  <c:set var="canEditDpoEdit" value="${AppSubmissionDto.appEditSelectDto.dpoEdit || AppSubmissionDto.appEditSelectDto.serviceEdit}"/>
                  <div class=" <c:if test="${'true' != showPreview}">hidden</c:if>">
                    <c:choose>
                      <c:when test="${canEditDpoEdit}">
                          <p><div class="text-right app-font-size-16"><a id="edit-dpo"><em class="fa fa-pencil-square-o"></em>Edit</a></div></p>
                      </c:when>
                      <c:otherwise>

                      </c:otherwise>
                    </c:choose>
                  </div>
                </c:if>
              </c:if>
              <c:set var="needDpoDrop" value="N"/>
              <c:if test="${dpoHcsaSvcPersonnelDto.maximumCount > 0}">
                  <c:set var="needDpoDrop" value="Y"/>
              </c:if>
              <c:if test="${needDpoDrop == 'Y'}">
              <div class="row">
                <div class="form-group form-horizontal formgap">
                  <div class="col-sm-6 col-md-4" style="font-size: 1.6rem;">
                    Deputy Principal Officer (Optional)
                  </div>
                  <c:if test="${DeputyPrincipalOfficersMandatory> 0}">
                  <div class="col-sm-5 col-md-8" >
                      <%--<c:if test="${ReloadPrincipalOfficers ==null ||ReloadPrincipalOfficers.size()==0}">
                        <c:set var="flag" value="-1"/>
                      </c:if>
                      <c:if test="${ReloadPrincipalOfficers !=null && ReloadPrincipalOfficers.size()>0}">
                        <c:set var="flag" value="0"/>
                      </c:if>
                      <c:if test="${ReloadDeputyPrincipalOfficers != null && ReloadDeputyPrincipalOfficers.size()>0}" >
                        <c:set var="flag" value="1"/>
                      </c:if>--%>
                    <iais:select cssClass="deputySelect"  name="deputyPrincipalOfficer" options="DeputyFlagSelect"  value="${DeputyPoFlag}" ></iais:select>
                    <br/>
                    <br/>
                    <br/>
                    <br/>
                    <br/>
                    <br/>
                    <br/>
                  </div>
                </div>
                </c:if>
              </div>
              </c:if>
            </div>

          </div>
        </div>
      </div>
      <div class="deputy-content panel panel-default hidden">
        <div class="panel-heading " id="headingDeputy" role="tab">
          <h4 class="panel-title"><a role="button" data-toggle="collapse" href="#deputyContent" aria-expanded="true" aria-controls="deputyContent">Deputy Principal Officer (Optional)</a></h4>
        </div>
        <div class="deputy-content panel-collapse collapse <c:if test="${DeputyPrincipalOfficersMandatory> 0}">in</c:if>" id="deputyContent" role="tabpanel" aria-labelledby="headingDeputy">
          <div class="panel-body">
            <div class="panel-main-content">
              <h2>Deputy Principal Officer</h2>
              <div class="dpo-content">
              </div>
              <c:if test="${DeputyPrincipalOfficersMandatory>0}">
                <c:set value="${dpoHcsaSvcPersonnelDto.mandatoryCount}" var="dpoMandatoryCount"/>
                <c:forEach begin="0" end="${DeputyPrincipalOfficersMandatory-1}" step="1" varStatus="status">
                  <c:if test="${ReloadDeputyPrincipalOfficers != null && ReloadDeputyPrincipalOfficers.size()>0}" >
                    <c:set var="deputy" value="${ReloadDeputyPrincipalOfficers[status.index]}"/>
                  </c:if>
                  <div class="dpo-content">
                    <c:choose>
                      <c:when test="${deputy.licPerson}">
                        <input type="hidden" name="dpoLicPerson" value="1"/>
                      </c:when>
                      <c:otherwise>
                        <input type="hidden" name="dpoLicPerson" value="0"/>
                      </c:otherwise>
                    </c:choose>
                    <input type="hidden" name="dpoExistingPsn" value="0"/>
                    <div class="row" <c:if test="${status.first}">style="margin-top:-4%;"</c:if> >
                      <div class="control control-caption-horizontal">
                        <div class=" form-group form-horizontal formgap">
                          <div class="col-sm-6 control-label formtext col-md-4">
                            <div class="cgo-header" style="font-size: 18px;">
                              <strong>Deputy Principal Officer <label class="assign-psn-item">${status.index+1}</label></strong>
                            </div>
                          </div>
                          <div class="col-sm-5 col-md-8 text-right" >
                            <c:if test="${status.index - dpoMandatoryCount >=0}">
                              <h4 class="text-danger"><em class="fa fa-times-circle removeDpoBtn cursorPointer"></em></h4>
                            </c:if>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div class="row">
                      <div class="control control-caption-horizontal">
                        <div class=" form-group form-horizontal formgap">
                          <div class="col-sm-6 control-label formtext col-md-4" style="font-size: 1.6rem;">
                            Assign a Deputy Principal Officer
                          </div>
                          <div class="col-sm-5 col-md-8" id="assignSelect${suffix}">
                            <iais:select cssClass="deputyPoSelect"  name="deputyPoSelect" options="DeputyPrincipalOfficersAssignSelect"  value="${deputy.assignSelect}" ></iais:select>
                            <span id="error_deputyAssignSelect${status.index}" name="iaisErrorMsg" class="error-msg"></span>
                          </div>
                        </div>
                      </div>
                    </div>

                    <div class="deputyPrincipalOfficers hidden">
                      <div class="row">
                        <div class="control control-caption-horizontal">
                          <div class=" form-group form-horizontal formgap">
                            <div class="col-sm-3 control-label formtext col-md-4">
                              <label  class="control-label control-set-font control-font-label">Name</label>
                              <span class="mandatory">*</span>
                            </div>
                            <div class="col-sm-4 " id="deputySalutation${suffix}">
                              <iais:select cssClass="deputySalutation"  name="deputySalutation" codeCategory="CATE_ID_SALUTATION" value="${deputy.salutation}" firstOption="Please Select"></iais:select>
                              <span name="iaisErrorMsg" class="error-msg" id="error_deputySalutation${status.index}"></span>
                            </div>
                            <div class="col-sm-4">
                              <input autocomplete="off" name="deputyName" maxlength="66" type="text"  class="form-control control-input control-set-font control-font-normal" value="${deputy.name}"  size="30">
                              <span class="error-msg" name="iaisErrorMsg" id="error_deputyName${status.index}"></span>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div class="row">
                        <div class="control control-caption-horizontal">
                          <div class=" form-group form-horizontal formgap">
                            <div class="col-sm-3 control-label formtext col-md-4">
                              <label  class="control-label control-set-font control-font-label">ID No.
                                <span class="mandatory">*</span>
                              </label>

                            </div>
                            <div class="col-sm-4" id="deputyIdType${suffix}">
                              <div class="">
                                <iais:select cssClass="deputyIdType"  name="deputyIdType" value="${deputy.idType}" options="IdTypeSelect"></iais:select>
                                <span name="iaisErrorMsg" class="error-msg" id="error_deputyIdType${status.index}"></span>
                              </div>
                            </div>
                            <div class="col-sm-4">
                              <input autocomplete="off"  name="deputyIdNo" maxlength="9" type="text"  class=" form-control control-input control-set-font control-font-normal" value="${deputy.idNo}" size="30">
                              <span class="error-msg"  name="iaisErrorMsg" id="error_deputyIdNo${status.index}"></span>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div class="row">
                        <div class="control control-caption-horizontal">
                          <div class=" form-group form-horizontal formgap">
                            <div class="col-sm-3 control-label formtext col-md-4">
                            </div>
                            <div class="col-sm-8">
                              <div class="">
                                <span class="error-msg" name="iaisErrorMsg" id="error_dpoIdTypeNo${status.index}"></span>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div class="row">
                        <div class="control control-caption-horizontal">
                          <div class=" form-group form-horizontal formgap">
                            <div class="col-sm-3 control-label formtext col-md-4">
                              <label  class="control-label control-set-font control-font-label">Designation</label>
                              <span class="mandatory">*</span>
                            </div>
                            <div class="col-sm-5 col-md-8" id="deputyDesignation${suffix}">
                              <iais:select cssClass="deputyDesignation" name="deputyDesignation" codeCategory="CATE_ID_DESIGNATION" value="${deputy.designation}" firstOption="Please Select"></iais:select>
                              <span class="error-msg" id="error_deputyDesignation${status.index}" name="iaisErrorMsg"></span>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div class="row">
                        <div class="control control-caption-horizontal">
                          <div class=" form-group form-horizontal formgap">
                            <div class="col-sm-3 control-label formtext col-md-4">
                              <label  class="control-label control-set-font control-font-label">Mobile No.</label>
                              <span class="mandatory">*</span>
                            </div>
                            <div class="col-sm-4 col-md-8">
                              <input autocomplete="off" name="deputyMobileNo" type="text"   maxlength="8" class="form-control control-input control-set-font control-font-normal" value="${deputy.mobileNo}" size="30">
                              <span class="error-msg"  name="iaisErrorMsg"  id="error_deputyMobileNo${status.index}"></span>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div class="row">
                        <div class="control control-caption-horizontal">
                          <div class=" form-group form-horizontal formgap">
                            <div class="col-sm-3 control-label formtext col-md-4">
                              <label  class="control-label control-set-font control-font-label">Office Telephone No.</label>
                              <span class="mandatory">*</span>
                            </div>
                            <div class="col-sm-4 col-md-8">
                              <input autocomplete="off" name="deputyOfficeTelNo" type="text"  id="deputyOfficeTelNo" maxlength="8" class="form-control control-input control-set-font control-font-normal" value="${deputy.officeTelNo}" >
                              <span name="iaisErrorMsg" id="error_deputyofficeTelNo${status.index}" class="error-msg"></span>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div class="row">
                        <div class="control control-caption-horizontal">
                          <div class=" form-group form-horizontal formgap">
                            <div class="col-sm-3 control-label formtext col-md-4">
                              <label  class="control-label control-set-font control-font-label">Email Address</label>
                              <span class="mandatory">*</span>
                            </div>
                            <div class="col-sm-4 col-md-8">
                              <input autocomplete="off" name="deputyEmailAddr" maxlength="66" type="text" class="form-control control-input control-set-font control-font-normal" value="${deputy.emailAddr}" size="30">
                              <span class="error-msg" name="iaisErrorMsg"  id="error_deputyEmailAddr${status.index}" ></span>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </c:forEach>
              </c:if>
            </div>
            <c:if test="${requestInformationConfig==null}">
              <c:set var="dpoDtoLength" value="${ReloadDeputyPrincipalOfficers.size()}"/>
              <c:if test="${dpoDtoLength == '0'}">
                <c:set var="dpoDtoLength" value="1"/>
              </c:if>
              <c:set var="needAddPsn" value="true"/>
              <c:choose>
                <c:when test="${dpoHcsaSvcPersonnelDto.status =='CMSTAT003'}">
                  <c:set var="needAddPsn" value="false"/>
                </c:when>
                <c:when test="${dpoDtoLength >= dpoHcsaSvcPersonnelDto.maximumCount}">
                  <c:set var="needAddPsn" value="false"/>
                </c:when>
              </c:choose>
              <div class="row <c:if test="${!needAddPsn}">hidden</c:if>" id="addPsnDiv-dpo">
                <div class="col-sm-5">
                  <span id="addDpoBtn" style="color:deepskyblue;cursor:pointer;">+ Add Another Deputy Principal Officer</span>
                </div>
                <div  class="col-sm-5 col-md-5">
                  <span class="dpoErrorMsg" style="color: red;margin-left: -75px;"></span>
                </div>
              </div>
            </c:if>
            <br/>
            <br/>
            <br/>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<script>
    var init;
    $(document).ready(function () {
        <!-- init start-->
        init = 0;
        poSelect();

        dpoSelect();

        //retrieveData();

        addPo();

        addDpo();

        removePo();

        removeDpo();

        doEdit();

        doEditDpo();

        $('select.poSelect').trigger('change');
        $('select.deputySelect').trigger('change');
        $('select.deputyPoSelect').trigger('change');

        $('input[name="poLicPerson"]').each(function () {
            if('1' == $(this).val()){
                var $currentPsn = $(this).closest('.po-content').find('div.principalOfficers');
                disabledPartPage($currentPsn);
            }
        });
        $('input[name="dpoLicPerson"]').each(function () {
            if('1' == $(this).val()){
                var $currentPsn = $(this).closest('.dpo-content').find('div.deputyPrincipalOfficers');
                disabledPartPage($currentPsn);
            }
        });

        //disabled
        if(${AppSubmissionDto.needEditController && !isClickEdit}){
            $('.po-content input[type="text"]').prop('disabled',true);
            $('.po-content div.nice-select').addClass('disabled');
            $('.po-content input[type="text"]').css('border-color','#ededed');
            $('.po-content input[type="text"]').css('color','#999');
            $('#addPoBtn').addClass('hidden');
            $('#addPoBtn').unbind('click');
        }
        if(${AppSubmissionDto.needEditController && !isClickEditDpo}){
            $('.deputySelect').addClass('disabled');
            $('.deputy-content input[type="text"]').prop('disabled',true);
            $('.deputy-content div.nice-select').addClass('disabled');
            $('.deputy-content input[type="text"]').css('border-color','#ededed');
            $('.deputy-content input[type="text"]').css('color','#999');
            $('#addDpoBtn').unbind('click');
        }

        var appType = $('input[name="applicationType"]').val();
        var rfiObj = $('input[name="rfiObj"]').val();
        //new and not rfi
        if('APTY002' == appType && '0' == rfiObj){
            //po
            <c:choose>
              <c:when test="${!empty ReloadPrincipalOfficers}">
                console.log('po true');
                <c:set var="psnLength" value="${ReloadPrincipalOfficers.size()-1}"/>
              </c:when>
              <c:otherwise>
                <c:set var="psnLength" value="0"/>
              </c:otherwise>
            </c:choose>
            console.log('psnLength:'+${psnLength});
            <c:forEach begin="0" end="${psnLength}" step="1" varStatus="stat">
              var $currentPsn = $('.po-content').eq(${stat.index+1});
              //remove dis style
              $currentPsn.find('input[type="text"]').css('border-color','');
              $currentPsn.find('input[type="text"]').css('color','');
              //add edit and set style
              setPoPsnDisabled($currentPsn,${ReloadPrincipalOfficers[stat.index].psnEditFieldStr});
            </c:forEach>

            //dpo
            <c:choose>
              <c:when test="${!empty ReloadDeputyPrincipalOfficers}">
            console.log('dpo true');
                <c:set var="psnLengthDpo" value="${ReloadDeputyPrincipalOfficers.size()-1}"/>
              </c:when>
              <c:otherwise>
                <c:set var="psnLengthDpo" value="0"/>
              </c:otherwise>
            </c:choose>
            console.log('psnLengthDpo:'+${psnLengthDpo});
            <c:forEach begin="0" end="${psnLengthDpo}" step="1" varStatus="stat">
              var $currentPsn = $('.dpo-content').eq(${stat.index+1});
              //remove dis style
              $currentPsn.find('input[type="text"]').css('border-color','');
              $currentPsn.find('input[type="text"]').css('color','');
              //add edit and set style
              setDpoPsnDisabled($currentPsn,${ReloadDeputyPrincipalOfficers[stat.index].psnEditFieldStr});
            </c:forEach>
        }

        <!-- init end-->
        init = 1;
    });

    var poLoadByAjax =  function ($poContentEle,selectVal) {
        var data = {};
        fillPoData($poContentEle,data);
        var arr = selectVal.split(',');
        var idType = arr[0];
        var idNo = arr[1];
        var jsonData = {
            'idType':idType,
            'idNo':idNo
        };
        $.ajax({
            'url':'${pageContext.request.contextPath}/psn-select-info',
            'dataType':'json',
            'data':jsonData,
            'type':'GET',
            'success':function (data) {
                if(data == null){
                    return;
                }
                fillPoData($poContentEle,data);
            },
            'error':function () {

            }
        });
    }

    var dpoLoadByAjax =  function ($poContentEle,selectVal) {
        var data = {};
        fillDpoData($poContentEle,data);
        var arr = selectVal.split(',');
        var idType = arr[0];
        var idNo = arr[1];
        var jsonData = {
            'idType':idType,
            'idNo':idNo
        };
        $.ajax({
            'url':'${pageContext.request.contextPath}/psn-select-info',
            'dataType':'json',
            'data':jsonData,
            'type':'GET',
            'success':function (data) {
                if(data == null){
                    return;
                }
                fillDpoData($poContentEle,data);

            },
            'error':function () {

            }
        });
    }


    var poSelect = function(){
        $('.poSelect').change(function () {
            var $poContentEle = $(this).closest('div.po-content');
            var selectVal = $(this).val();
            var data= {};
            if("newOfficer" == selectVal){
                $poContentEle.find('div.principalOfficers').removeClass('hidden');
                unDisabledPartPage($poContentEle);
                if(0 != init) {
                    fillPoData($poContentEle, data);
                }
                $poContentEle.find('input[name="poLicPerson"]').val('0');
                $poContentEle.find('input[name="poExistingPsn"]').val('0');
            }else if('-1' == selectVal){
                $poContentEle.find('div.principalOfficers').addClass('hidden');
                if(0 != init) {
                    fillPoData($poContentEle, data);
                }
                $poContentEle.find('input[name="poLicPerson"]').val('0');
                $poContentEle.find('input[name="poExistingPsn"]').val('0');
            }else{
                $poContentEle.find('div.principalOfficers').removeClass('hidden');
                if(init == 0){
                    return;
                }
                poLoadByAjax($poContentEle,selectVal);
            }
        });
    };

    var dpoSelect = function(){
        $('.deputyPoSelect').change(function () {
            var $dpoContentEle = $(this).closest('div.dpo-content');
            var selectVal = $(this).val();
            var data= {};
            if("newOfficer" == selectVal){
                $dpoContentEle.find('div.deputyPrincipalOfficers').removeClass('hidden');
                unDisabledPartPage($dpoContentEle);
                if(0 != init) {
                    fillDpoData($dpoContentEle, data);
                }
                $dpoContentEle.find('input[name="poLicPerson"]').val('0');
                $dpoContentEle.find('input[name="dpoExistingPsn"]').val('0');
            }else if('-1' == selectVal){
                $dpoContentEle.find('div.deputyPrincipalOfficers').addClass('hidden');
                if(0 != init) {
                    fillDpoData($dpoContentEle, data);
                }
                $dpoContentEle.find('input[name="poLicPerson"]').val('0');
                $dpoContentEle.find('input[name="dpoExistingPsn"]').val('0');
            }else{
                $dpoContentEle.find('div.deputyPrincipalOfficers').removeClass('hidden');
                if(init == 0){
                    return;
                }
                dpoLoadByAjax($dpoContentEle,selectVal);
            }
        });
    };


    $('.deputySelect').change(function () {
        var deputyFlag = $(this).val();
        var $poContentEle = $(this).closest('div.panel-group');
        if("1" == deputyFlag){
            $poContentEle.find('div.deputy-content ').removeClass('hidden');
        }else{
            $poContentEle.find('div.deputy-content ').addClass('hidden');
        }

    });

    var addPo = function(){
        $('#addPoBtn').click(function () {
            var hasNumber = $('div.po-content').size() - 1;
            console.log("hasNumber" + hasNumber);
            $.ajax({
                url:'${pageContext.request.contextPath}/principal-officer-html',
                dataType:'json',
                type:'POST',
                data:{
                    'HasNumber':hasNumber
                },
                success:function (data) {
                    if ('success' == data.res) {
                        console.log(data.res);
                        $('.po-content:last').after(data.sucInfo);
                        poSelect();
                        removePo();
                        //retrieveData();
                        removePo();
                        <!--set Scrollbar -->
                        $("div.poSelect->ul").mCustomScrollbar({
                                advanced:{
                                    updateOnContentResize: true
                                }
                            }
                        );
                        //hidden add more
                        var psnLength = $('.po-content').length-1;
                        if(psnLength >='${poHcsaSvcPersonnelDto.maximumCount}'){
                            $('#addPsnDiv-po').addClass('hidden');
                        }
                    }else{
                        $('.poErrorMsg').html(data.errInfo);
                    }
                },
                error:function (data) {
                    console.log("err");
                }
            });

        });
    }


    var addDpo = function(){
        $('#addDpoBtn').click(function () {
            var hasNumber = $('.dpo-content').size() - 1;
            console.log("hasNumber" + hasNumber);
            $.ajax({
                url:'${pageContext.request.contextPath}/deputy-principal-officer-html',
                dataType:'json',
                type:'POST',
                data:{
                    'HasNumber':hasNumber
                },
                'success':function (data) {
                    if ('success' == data.res) {
                        console.log("suc");
                        $('.dpo-content:last').after(data.sucInfo);
                        $('.deputyPoSelect').unbind();
                        dpoSelect();
                        removeDpo();
                        <!--set Scrollbar -->
                        $("div.deputyPoSelect->ul").mCustomScrollbar({
                                advanced:{
                                    updateOnContentResize: true
                                }
                            }
                        );
                        //hidden add more
                        var psnLength = $('.dpo-content').length-1;
                        if(psnLength >='${dpoHcsaSvcPersonnelDto.maximumCount}'){
                            $('#addPsnDiv-dpo').addClass('hidden');
                        }
                    }else{
                        $('.dpoErrorMsg').html(data.errInfo);
                    }
                },
                error:function (data) {
                    console.log("err");
                }
            });
        });
    }



    var doEdit = function () {
        $('#edit').click(function () {
            $('.po-content input[type="text"]').prop('disabled',false);
            $('.po-content div.nice-select').removeClass('disabled');
            $('.po-content input[type="text"]').css('border-color','');
            $('.po-content input[type="text"]').css('color','');
            $('#isEditHiddenVal').val('1');
            $('#addPoBtn').removeClass('hidden');
            addPo();
            $('#edit').addClass('hidden');
            $('input[name="poExistingPsn"]').val('0');
        });
    }


    var doEditDpo = function () {
        $('#edit-dpo').click(function () {
            $('.deputySelect').removeClass('disabled');
            $('.deputy-content input[type="text"]').prop('disabled',false);
            $('.deputy-content div.nice-select').removeClass('disabled');
            $('.deputy-content input[type="text"]').css('border-color','');
            $('.deputy-content input[type="text"]').css('color','');
            $('#isEditDpoHiddenVal').val('1');
            addDpo();
            $('#edit-dpo').addClass('hidden');
            $('input[name="dpoExistingPsn"]').val('0');
        });
    }
    var fillPoData = function ($poContentEle,data) {
        $poContentEle.find('input[name="idNo"]').val(data.idNo);
        $poContentEle.find('input[name="name"]').val(data.name);
        $poContentEle.find('input[name="mobileNo"]').val(data.mobileNo);
        $poContentEle.find('input[name="officeTelNo"]').val(data.officeTelNo);
        $poContentEle.find('input[name="emailAddress"]').val(data.emailAddr);
        <!--salutation-->
        var salutation = data.salutation;
        if(salutation == null || salutation =='undefined' || salutation == ''){
            salutation = '';
        }
        $poContentEle.find('select[name="salutation"]').val(salutation);
        var salutationVal = $poContentEle.find('option[value="' + salutation + '"]').html();
        $poContentEle.find('select[name="salutation"]').next().find('.current').html(salutationVal);
        <!-- idType-->
        var idType = data.idType;
        if(idType == null || idType =='undefined' || idType == ''){
            idType = '';
        }
        $poContentEle.find('select[name="idType"]').val(idType);
        var idTypeVal = $poContentEle.find('option[value="' + idType + '"]').html();
        $poContentEle.find('select[name="idType"]').next().find('.current').html(idTypeVal);
        <!--Designation  -->
        var designation = data.designation;
        if(designation == null || designation =='undefined' || designation == ''){
            designation = '';
        }
        $poContentEle.find('select[name="designation"]').val(designation);
        var designationVal = $poContentEle.find('option[value="' + designation + '"]').html();
        $poContentEle.find('select[name="designation"]').next().find('.current').html(designationVal);

        var isLicPerson = data.licPerson;
        if('1' == isLicPerson){
            var $psnContentEle = $poContentEle.find('div.principalOfficers');
            //add disabled not add input disabled style
            personDisable($psnContentEle,'','Y');
            var psnEditDto = data.psnEditDto;
            setPoPsnDisabled($psnContentEle,psnEditDto);
            $poContentEle.find('input[name="poLicPerson"]').val('1');
            $poContentEle.find('input[name="poExistingPsn"]').val('1');
        }else{
            unDisabledPartPage($poContentEle.find('div.principalOfficers'));
            $poContentEle.find('input[name="poLicPerson"]').val('0');
            $poContentEle.find('input[name="poExistingPsn"]').val('0');
        }
    }

    var fillDpoData = function ($poContentEle,data) {
        $poContentEle.find('input[name="deputyIdNo"]').val(data.idNo);
        $poContentEle.find('input[name="deputyName"]').val(data.name);
        $poContentEle.find('input[name="deputyMobileNo"]').val(data.mobileNo);
        $poContentEle.find('input[name="deputyOfficeTelNo"]').val(data.officeTelNo);
        $poContentEle.find('input[name="deputyEmailAddr"]').val(data.emailAddr);
        <!--salutation-->
        var salutation = data.salutation;
        if(salutation == null || salutation =='undefined' || salutation == ''){
            salutation = '';
        }
        $poContentEle.find('select[name="deputySalutation"]').val(salutation);
        var salutationVal = $poContentEle.find('option[value="' + salutation + '"]').html();
        $poContentEle.find('select[name="deputySalutation"]').next().find('.current').html(salutationVal);
        <!-- idType-->
        var idType = data.idType;
        if(idType == null || idType =='undefined' || idType == ''){
            idType = '';
        }
        $poContentEle.find('select[name="deputyIdType"]').val(idType);
        var idTypeVal = $poContentEle.find('option[value="' + idType + '"]').html();
        $poContentEle.find('select[name="deputyIdType"]').next().find('.current').html(idTypeVal);
        <!--Designation  -->
        var designation = data.designation;
        if(designation == null || designation =='undefined' || designation == ''){
            designation = '';
        }
        $poContentEle.find('select[name="deputyDesignation"]').val(designation);
        var designationVal = $poContentEle.find('option[value="' + designation + '"]').html();
        $poContentEle.find('select[name="deputyDesignation"]').next().find('.current').html(designationVal);

        var isLicPerson = data.licPerson;
        if('1' == isLicPerson){
            var $psnContentEle = $poContentEle.find('div.deputyPrincipalOfficers');
            //add disabled not add input disabled style
            personDisable($psnContentEle,'','Y');
            var psnEditDto = data.psnEditDto;
            setDpoPsnDisabled($psnContentEle,psnEditDto);
            $poContentEle.find('input[name="dpoLicPerson"]').val('1');
            $poContentEle.find('input[name="dpoExistingPsn"]').val('1');
        }else{
            unDisabledPartPage($poContentEle.find('div.deputyPrincipalOfficers'));
            $poContentEle.find('input[name="dpoLicPerson"]').val('0');
            $poContentEle.find('input[name="dpoExistingPsn"]').val('0');
        }
    }

    var removePo = function () {
        $('.removePoBtn').click(function () {
            var $premContentEle= $(this).closest('div.po-content');
            $premContentEle.remove();
            //reset number
            $('.po-content').each(function (k,v) {
                $(this).find('.assign-psn-item').html(k);
            });
            //show add more
            var psnLength = $('.po-content').length-1;
            if(psnLength <'${poHcsaSvcPersonnelDto.maximumCount}'){
                $('#addPsnDiv-po').removeClass('hidden');
            }
        });

    }
    var removeDpo = function () {
        $('.removeDpoBtn').click(function () {
            var $premContentEle= $(this).closest('div.dpo-content');
            $premContentEle.remove();
            //reset number
            $('.dpo-content').each(function (k,v) {
                $(this).find('.assign-psn-item').html(k);
            });
            //show add more
            var psnLength = $('.dpo-content').length-1;
            if(psnLength <'${dpoHcsaSvcPersonnelDto.maximumCount}'){
                $('#addPsnDiv-dpo').removeClass('hidden');
            }
        });

    }

    var setPoPsnDisabled = function ($cgoPsnEle,psnEditDto) {
        console.log("setPsnDisabled start...");
        console.log("psnEditDto:"+psnEditDto);
        if(psnEditDto == 'undefined' || psnEditDto == '' || psnEditDto == null){
            console.log('psnEditDto is empty or undefind');
            return;
        }
        //dropdown
        if(psnEditDto.salutation){
            $cgoPsnEle.find('div.salutation').removeClass('disabled');
        }
        if(psnEditDto.idType){
            $cgoPsnEle.find('div.idType').removeClass('disabled');
        }
        if(psnEditDto.designation){
            $cgoPsnEle.find('div.designation').removeClass('disabled');
        }
        //input text
        if(psnEditDto.name){
            $cgoPsnEle.find('input[name="name"]').prop('disabled',false);
        }
        if(psnEditDto.idNo){
            $cgoPsnEle.find('input[name="idNo"]').prop('disabled',false);
        }
        if(psnEditDto.mobileNo){
            $cgoPsnEle.find('input[name="mobileNo"]').prop('disabled',false);
        }
        if(psnEditDto.officeTelNo){
            $cgoPsnEle.find('input[name="officeTelNo"]').prop('disabled',false);
        }
        if(psnEditDto.emailAddr){
            $cgoPsnEle.find('input[name="emailAddress"]').prop('disabled',false);
        }
        //for disabled add style
        $cgoPsnEle.find('input[type="text"]').each(function () {
            if($(this).prop('disabled')){
                $(this).css('border-color','#ededed');
                $(this).css('color','#999');
            }else{
                $(this).css('border-color','');
                $(this).css('color','');
            }
        });
        console.log("setPsnDisabled end...");
    }


    var setDpoPsnDisabled = function ($cgoPsnEle,psnEditDto) {
        console.log("setPsnDisabled start...");
        console.log(psnEditDto);
        if(psnEditDto == 'undefined' || psnEditDto == '' || psnEditDto == null){
            console.log('psnEditDto is empty or undefind');
            return;
        }
        //dropdown
        if(psnEditDto.salutation){
            $cgoPsnEle.find('div.deputySalutation').removeClass('disabled');
        }
        if(psnEditDto.idType){
            $cgoPsnEle.find('div.deputyIdType').removeClass('disabled');
        }
        if(psnEditDto.designation){
            $cgoPsnEle.find('div.deputyDesignation').removeClass('disabled');
        }
        //input text
        if(psnEditDto.name){
            $cgoPsnEle.find('input[name="deputyName"]').prop('disabled',false);
        }
        if(psnEditDto.idNo){
            $cgoPsnEle.find('input[name="deputyIdNo"]').prop('disabled',false);
        }
        if(psnEditDto.mobileNo){
            $cgoPsnEle.find('input[name="deputyMobileNo"]').prop('disabled',false);
        }
        if(psnEditDto.officeTelNo){
            $cgoPsnEle.find('input[name="deputyOfficeTelNo"]').prop('disabled',false);
        }
        if(psnEditDto.emailAddr){
            $cgoPsnEle.find('input[name="deputyEmailAddress"]').prop('disabled',false);
        }
        //for disabled add style
        $cgoPsnEle.find('input[type="text"]').each(function () {
            if($(this).prop('disabled')){
                $(this).css('border-color','#ededed');
                $(this).css('color','#999');
            }else{
                $(this).css('border-color','');
                $(this).css('color','');
            }
        });
        console.log("setPsnDisabled end...");
    }
</script>