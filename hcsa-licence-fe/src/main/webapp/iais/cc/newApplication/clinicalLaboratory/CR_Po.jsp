<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="row">
  <%--  <div class="col-xs-12 col-md-10">
      <p>Please note that you will not be able to pay for this application if you have not provided the mandatory information and documents.</p>
    </div>--%>
  <%--<div class="col-xs-12 col-md-2 text-right">
    <p class="print"><a href="#"> <i class="fa fa-print"></i>Print</a></p>
  </div>--%>
</div>
<div class="row">
  <div class="col-xs-12">
    <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">

      <div class="panel panel-default">
        <div class="panel-heading completed" id="headingPrincipal" role="tab">
          <h4 class="panel-title"><a role="button" data-toggle="collapse" href="#collapsePrincipal" aria-expanded="true" aria-controls="collapsePrincipal">Principal Officer</a></h4>
        </div>
        <c:forEach items="${AppSvcPrincipalOfficersDto}" var="appSvcPrincipalOfficersDto">
        <div class="panel-collapse collapse" id="collapsePrincipal" role="tabpanel" aria-labelledby="headingPremise">
          <div class="panel-body">
            <%--<p class="text-right"><a href="application-premises.html"><i class="fa fa-pencil-square-o"></i>Edit</a></p>--%>
            <div class="panel-main-content">
              <div class="" style="height: auto">
                <h2>Principal Officer</h2>
                <p><h4>A Principal Officer is responsible for overseeing the day-to-day operations of medical service</h4></p>
                <div class="row">
                  <div class="control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                      <div class="col-sm-6 control-label formtext ">
                        <label id="control--runtime--2--label" class="control-label control-set-font control-font-label">Assign a Clinical Governance Officer</label>
                        <span class="upload_controls"></span>
                      </div>
                      <div class="col-sm-5">
                        <div class="">
                          <select name="assign" id="cgoSelect" class="form-control control-input control-set-font control-font-normal">
                            <option>Select Personnel</option>
                            <option value="newOfficer">I'd like to add a new personnel</option>
                            <option>Deng Jin, XXX675 (NRIC)</option>
                          </select>
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
                      <div class="col-sm-2">
                        <select name="salutation" class="form-control control-input control-set-font control-font-normal">
                          <option>Mr</option>
                          <option>Mrs</option>
                          <option>Miss</option>
                          <option>Madam</option>
                        </select>
                      </div>
                      <div class="col-sm-4">
                        <input name="name" id="cr-po-name" type="text"  class="form-control control-input control-set-font control-font-normal" value="${appSvcPrincipalOfficersDto.name}"  size="30">
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
                      <div class="col-sm-2">
                        <div class="">
                          <select name="idType" class="form-control control-input control-set-font control-font-normal" id="selectType">
                            <c:if test="${appSvcPrincipalOfficersDto.idType=='nric'}">
                              <option value="nric" selected="selected">NRIC</option>
                            </c:if> <c:if test="${appSvcPrincipalOfficersDto.idType=='fin'}">
                            <option  value="fin" selected="selected">FIN</option>
                          </c:if>
                            <option value="nric">NRIC</option>
                            <option  value="fin">FIN</option>
                          </select>
                        </div>
                      </div>
                      <div class="col-sm-4">
                        <input id="idType-idNo" name="idNo" type="text"  class="form-control control-input control-set-font control-font-normal" value="${appSvcPrincipalOfficersDto.idNo}" size="30">
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
                      <div class="col-sm-2">
                        <select name="designation" class="form-control control-input control-set-font control-font-normal">\
                          <c:if test="${appSvcPrincipalOfficersDto.designation=='ceo'}">
                            <option value="ceo" selected="selected">CEO</option>
                          </c:if>
                          <c:if test="${appSvcPrincipalOfficersDto.designation=='cfo'}">
                            <option value="ceo" selected="selected">CEO</option>
                          </c:if>
                          <c:if test="${appSvcPrincipalOfficersDto.designation=='coo'}">
                            <option value="coo" selected="selected">COO</option>
                          </c:if>
                          <c:if test="${appSvcPrincipalOfficersDto.designation=='others'}">
                            <option value="others" selected="selected">Others</option>
                          </c:if>
                          <option value="ceo">CEO</option>
                          <option value="cfo">CFO</option>
                          <option value="coo">COO</option>
                          <option value="others">Others</option>
                        </select>
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
                        <input name="mobileNo" type="text"   maxlength="8" class="form-control control-input control-set-font control-font-normal" value="${appSvcPrincipalOfficersDto.mobileNo}" size="30">
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
                        <input name="officeTelNo" type="text"  id="officeTelNo" maxlength="8" class="form-control control-input control-set-font control-font-normal" value="${appSvcPrincipalOfficersDto.officeTelNo}" size="30">
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
                        <input name="emailAddress" type="text" id="emailAdress" class="form-control control-input control-set-font control-font-normal" value="${appSvcPrincipalOfficersDto.emailAddr}" size="30">
                        <span class="error-msg" name="iaisErrorMsg" id="error_emailAddr" >${error.emailAddr}</span>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="row">
                  <div class="col-sm-4">
                    Deputy Principal Officer(Optional):
                  </div>
                  <div class="col-sm-4" >
                    <select name="deputySelect" class="form-control control-input control-set-font control-font-normal">
                      <option>Please Select</option>
                      <option value="0">N</option>
                      <option value="1">Y</option>
                    </select>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        </c:forEach>
      </div>
      <div class="panel panel-default">
        <div class="panel-heading completed" id="headingDeputy" role="tab">
          <h4 class="panel-title"><a role="button" data-toggle="collapse" href="#deputyContent" aria-expanded="true" aria-controls="deputyContent">Deputy Principal Officer(Optional)</a></h4>
        </div>
        <div class="panel-collapse collapse" id="deputyContent" role="tabpanel" aria-labelledby="headingDeputy">
          <div class="panel-body">
            <%--<p class="text-right"><a href="application-premises.html"><i class="fa fa-pencil-square-o"></i>Edit</a></p>--%>
            <div class="panel-main-content">
              <div class="principalOfficers">
                <h2>Deputy Principal Officer</h2>
                <div class="row"></div>
                <div class="row"></div>
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
    $('#cgoSelect').change(function () {
        var selectVal = $(this).val();
        $('.principalOfficers').removeClass('hidden');
    });

</script>