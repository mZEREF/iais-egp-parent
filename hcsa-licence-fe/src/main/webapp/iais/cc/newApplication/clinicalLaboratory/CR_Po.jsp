<div class="row">
</div>
<div class="row">
  <div class="col-xs-12">
    <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
      <div class="panel panel-default">
        <div class="panel-heading " id="headingPrincipal" role="tab">
          <h4 class="panel-title"><a role="button" data-toggle="collapse" href="#collapsePrincipal" aria-expanded="true" aria-controls="collapsePrincipal">Principal Officer</a></h4>
        </div>
        <div class="panel-collapse collapse <c:if test="${ReloadPrincipalOfficers != null && ReloadPrincipalOfficers.size()>0}" >
          in
        </c:if>  " id="collapsePrincipal" role="tabpanel" aria-labelledby="headingPremise">

          <div class="panel-body">
            <%--<p class="text-right"><a href="application-premises.html"><i class="fa fa-pencil-square-o"></i>Edit</a></p>--%>
            <div class="panel-main-content">
              <div class="" style="height: auto">
                <h2>Principal Officer</h2>
                <p><h4>A Principal Officer is responsible for overseeing the day-to-day operations of medical service</h4></p>
                <div class="row"></div>
              </div>
              <c:if test="${PrincipalOfficersMandatory>0}">
              <c:forEach begin="0" end="${PrincipalOfficersMandatory-1}" step="1" varStatus="status">
                <c:if test="${ReloadPrincipalOfficers != null && ReloadPrincipalOfficers.size()>0}" >
                  <c:set var="principalOfficer" value="${ReloadPrincipalOfficers[status.index]}"/>
                </c:if>
                <div class="po-content">
              <div class="">
                <div class="row">
                  <div class="control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                      <div class="col-sm-6 control-label formtext ">
                        <label id="control--runtime--2--label" class="control-label control-set-font control-font-label">Assign a Clinical Governance Officer</label>
                        <span class="upload_controls"></span>
                      </div>
                      <div class="col-sm-5">
                        <div class="">
                          <iais:select cssClass="poSelect"  name="assignSelect" options="PrincipalOfficersAssignSelect"  value="${principalOfficer.assignSelect}" ></iais:select>
                          <div id="control--runtime--2--errorMsg_right" style="display: none;" class="error_placements"></div>
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
                      <div class="col-sm-3 control-label formtext ">
                        <label  class="control-label control-set-font control-font-label">Name</label>
                        <span class="mandatory">*</span>
                      </div>
                      <div class="col-sm-4">
                        <iais:select  name="salutation" codeCategory="CATE_ID_SALUTATION" value="${principalOfficer.salutation}" firstOption="Please Select"></iais:select>
                      </div>
                      <div class="col-sm-4">
                        <input name="name" id="cr-po-name" type="text"  class="form-control control-input control-set-font control-font-normal" value="${principalOfficer.name}" >
                        <span class="error-msg" name="iaisErrorMsg" id="error_name"></span>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="row">
                  <div class="control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                      <div class="col-sm-3 control-label formtext ">
                        <label id="control--runtime--33--label" class="control-label control-set-font control-font-label">ID No.
                          <span class="mandatory">*</span>
                        </label>
                      </div>
                      <div class="col-sm-4">
                        <div class="">
                          <iais:select  name="idType" value="${principalOfficer.idType}" options="IdTypeSelect"></iais:select>
                          <span class="error-msg" name="iaisErrorMsg" id="error_idType"></span>
                        </div>
                      </div>
                      <div class="col-sm-4">
                        <input id="idType-idNo" name="idNo" type="text"  class="form-control control-input control-set-font control-font-normal" value="${principalOfficer.idNo}" >
                        <span class="error-msg" id="error_NRICFIN" name="iaisErrorMsg"></span>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="row">
                  <div class="control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                      <div class="col-sm-3 control-label formtext ">
                        <label  class="control-label control-set-font control-font-label">Designation</label>
                        <span class="mandatory">*</span>
                      </div>
                      <div class="col-sm-5">
                        <iais:select name="designation" codeCategory="CATE_ID_DESIGNATION" value="${principalOfficer.designation}" firstOption="Please Select"></iais:select>
                      </div>

                    </div>
                  </div>
                </div>
                <div class="row">
                  <div class="control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                      <div class="col-sm-3 control-label formtext ">
                        <label  class="control-label control-set-font control-font-label">Mobile No.</label>
                        <span class="mandatory">*</span>
                      </div>
                      <div class="col-sm-4">
                        <input name="mobileNo" type="text"   maxlength="8" class="form-control control-input control-set-font control-font-normal" value="${principalOfficer.mobileNo}" >
                        <span class="error-msg"  name="iaisErrorMsg" id="error_mobileNo"></span>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="row">
                  <div class="control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                      <div class="col-sm-3 control-label formtext ">
                        <label  class="control-label control-set-font control-font-label">Office Telephone.</label>
                        <span class="mandatory">*</span>
                      </div>
                      <div class="col-sm-4">
                        <input name="officeTelNo" type="text"  id="officeTelNo" maxlength="8" class="form-control control-input control-set-font control-font-normal" value="${principalOfficer.officeTelNo}" >
                        <span class="error-msg" name="iaisErrorMsg" id="error_officeTelNo" ></span>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="row">
                  <div class="control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                      <div class="col-sm-3 control-label formtext ">
                        <label  class="control-label control-set-font control-font-label">Email Address</label>
                        <span class="mandatory">*</span>
                      </div>
                      <div class="col-sm-4">
                        <input name="emailAddress" type="text" id="emailAdress" class="form-control control-input control-set-font control-font-normal" value="${principalOfficer.emailAddr}" >
                        <span class="error-msg" name="iaisErrorMsg" id="error_emailAddr" ></span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
                </div>
              </c:forEach>
              </c:if>
              <div class="row">
                <div class="col-sm-4">
                  Deputy Principal Officer(Optional):
                </div>
                <div class="col-sm-4" >
                  <iais:select cssClass="deputySelect"  name="deputyPrincipalOfficer" options="DeputyFlagSelect"  value="" ></iais:select>
                  <br/>
                  <br/>
                  <br/>
                  <br/>
                  <br/>
                  <br/>
                  <br/>
                </div>
              </div>

            </div>

          </div>
        </div>
      </div>
      <div class="deputy-content panel panel-default hidden">
        <div class="panel-heading " id="headingDeputy" role="tab">
          <h4 class="panel-title"><a role="button" data-toggle="collapse" href="#deputyContent" aria-expanded="true" aria-controls="deputyContent">Deputy Principal Officer(Optional)</a></h4>
        </div>
        <div class="deputy-content panel-collapse collapse" id="deputyContent" role="tabpanel" aria-labelledby="headingDeputy">
          <div class="panel-body">
            <%--<p class="text-right"><a href="application-premises.html"><i class="fa fa-pencil-square-o"></i>Edit</a></p>--%>
            <div class="panel-main-content">
              <div class="deputyPrincipalOfficers">
                <h2>Deputy Principal Officer</h2>
                <div class="row">
                  <div class="control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                      <div class="col-sm-3 control-label formtext ">
                        <label  class="control-label control-set-font control-font-label">Name</label>
                        <span class="mandatory">*</span>
                      </div>
                      <div class="col-sm-4">
                        <iais:select  name="deputySalutation" codeCategory="CATE_ID_SALUTATION" value="" firstOption="Select Salution"></iais:select>
                      </div>
                      <div class="col-sm-4">
                        <input name="deputyName"  type="text"  class="form-control control-input control-set-font control-font-normal" value=""  size="30">
                        <span class="error-msg" name="iaisErrorMsg" id="error_deputyName"></span>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="row">
                  <div class="control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                      <div class="col-sm-3 control-label formtext ">
                        <label  class="control-label control-set-font control-font-label">ID No.
                          <span class="mandatory">*</span>
                        </label>

                      </div>
                      <div class="col-sm-4">
                        <div class="">
                          <iais:select  name="deputyIdType" value="" options="IdTypeSelect"></iais:select>
                          <span name="iaisErrorMsg" class="error-msg" id=""></span>
                        </div>
                      </div>
                      <div class="col-sm-4">
                        <input  name="deputyIdNo" type="text"  class="form-control control-input control-set-font control-font-normal" value="" size="30">
                        <span class="error-msg"  name="iaisErrorMsg" id="error_deputyIdNo"></span>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="row">
                  <div class="control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                      <div class="col-sm-3 control-label formtext ">
                        <label  class="control-label control-set-font control-font-label">Designation</label>
                        <span class="mandatory">*</span>
                      </div>
                      <div class="col-sm-5">
                        <iais:select name="deputyDesignation" codeCategory="CATE_ID_DESIGNATION" value="" firstOption="Select Designation"></iais:select>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="row">
                  <div class="control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                      <div class="col-sm-3 control-label formtext ">
                        <label  class="control-label control-set-font control-font-label">Mobile No.</label>
                        <span class="mandatory">*</span>
                      </div>
                      <div class="col-sm-4">
                        <input name="deputyMobileNo" type="text"   maxlength="8" class="form-control control-input control-set-font control-font-normal" value="" size="30">
                        <span class="error-msg"  name="iaisErrorMsg"  id="error_deputyMobileNo"></span>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="row">
                  <div class="control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                      <div class="col-sm-3 control-label formtext ">
                        <label  class="control-label control-set-font control-font-label">Email Address</label>
                        <span class="mandatory">*</span>
                      </div>
                      <div class="col-sm-4">
                        <input name="deputyEmailAddr" type="text" class="form-control control-input control-set-font control-font-normal" value="" size="30">
                        <span class="error-msg" name="iaisErrorMsg"  id="error_deputyEmailAddr" ></span>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="row ">
                  <div class="control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                      <div class="col-sm-3 control-label formtext ">
                        <label  class="control-label control-set-font control-font-label">Preferred Mode of Receiving MedAlert</label>
                        <span class="mandatory">*</span>
                      </div>
                      <div class="col-sm-4">
                        <iais:select  name="modeOfMedAlert" value="" options="MedAlertSelect"></iais:select>
                        <span class="error-msg" name="iaisErrorMsg" id="error_modeOfMedAlert" ></span>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="row"></div>
                <div class="row"></div>
                <div class="row"></div>
                <div class="row"></div>
              </div>
            </div>
          </div>
        </div>
      </div>

    </div>
  </div>
</div>

<script>
  $(document).ready(function () {
      poSelect();
      $('.poSelect').trigger('change');

  });

  var poSelect = function(){
      $('.poSelect').change(function () {
          $poContentEle = $(this).closest('div.po-content');
          var selectVal = $(this).val();
          if("newOfficer" == selectVal){
              $poContentEle.find('div.principalOfficers').removeClass('hidden');
          }
      });
  };


    $('.deputySelect').change(function () {
        var deputyFlag = $(this).val();
        $poContentEle = $(this).closest('div.panel-group');
        if("1" == deputyFlag){
            $poContentEle.find('div.deputy-content ').removeClass('hidden');
        }else{
            $poContentEle.find('div.deputy-content ').addClass('hidden');
        }

    });


</script>