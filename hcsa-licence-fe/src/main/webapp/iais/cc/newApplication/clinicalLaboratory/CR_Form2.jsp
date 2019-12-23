<style>
  /* label in the component */

  div.capital-info div.control div.control-label-span {
    display: none;
  }
  /* label component */

  div.capital-info div.control div.label-control {
    display: block;
  }

  div.capital-info div.info-header div.label-control {
    padding-left: 16px;
    text-align: left;
  }

  div.capital-info td.first div.control div.label-control {
    width: 100%;
  }

  div.capital-info td.first {
    width: 350px;
  }

  div.capital-info div.no-label div.control-input-span {
    width: 90%;
  }

  div.capital-info .control .control-input-span input[type="text"] {
    max-width: none;
    width: 90%;
  }

  div.capital-info table.control-grid tr td td {
    width: auto;
    padding-right: 25px;
  }


</style>
<%--<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>--%>
<div id="formPanel" class="sopform ui-tabs ui-widget ui-widget-content ui-corner-all" style="display: block;">
  <div id="wizard-page-title">Clinical Governance Officer</div>
  <div class="form-tab-panel ui-tabs-panel ui-widget-content ui-corner-bottom" id="tab_page_0">
    <div id="control--runtime--0" class="page control control-area  container-p-1">
      <div id="control--runtime--0--errorMsg_page_top" class="error_placements"></div>
      <table class="control-grid columns1">
        <tbody>
        <tr height="1">
          <td class="first last" style="width: 100%;">
            <div id="control--runtime--85" class="control control-caption-horizontal">
            </div>
          </td>
        </tr>
        <tr>
        </tr>
        <tr height="1">
          <td class="first last" style="width: 100%;">
            <div id="control--runtime--1" class="section control  container-s-1">
              <div class="control-set-font control-font-header section-header"><label>A Clinical Governance Officer is responsible for the clinical and technical oversight of a medical service.</label></div>

              <span class="upload_controls"></span>
              <div id="control--runtime--1--errorMsg_section_top" class="error_placements"></div>

              <c:if test="${CgoMandatoryCount >0}">
              <c:forEach  begin="0" end="${CgoMandatoryCount-1}"  step="1" varStatus="status" >
                <c:set value="${GovernanceOfficersList}" var="cgoList"/>
                <c:set value="cgo-${status.index}-" var="cgoIndeNo"/>
                <c:set value="${cgoList[status.index]}" var="currentCgo"/>
                <c:set value="${errorMap_governanceOfficers[status.index]}" var="errorMap"/>
                <table class="assignContent control-grid">
                  <tbody>
                  <tr height="1">
                    <td class="first last" style="width: 100%;">
                      <div id="control--runtime--2" class="control control-caption-horizontal">
                        <div class=" form-group form-horizontal formgap">
                          <div class="col-sm-4 control-label formtext ">
                            <label id="control--runtime--2--label" class="control-label control-set-font control-font-label">Assign a Clinical Governance Officer</label>
                            <span class="upload_controls"></span>
                          </div>
                          <div class="col-sm-5">
                            <div class="">
                              <iais:select cssClass="assignSel"  name="assignSelect"  options="CgoSelectList" value="${currentCgo.assignSelect}"></iais:select>
                              <span class="error-msg" name="iaisErrorMsg" id="error_assignSelect${status.index}"></span>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div class="profile-info-gp hidden">
                        <div class="officer-info">
                          <h3>Linda Tan</h3>
                          <div class="row profile-line">
                            <div class="col-xs-6 col-md-3">
                              <p>Name</p>
                            </div>
                            <div class="col-xs-6 col-md-7">
                              <p>Linda Tan</p>
                            </div>
                          </div>
                          <div class="row profile-line">
                            <div class="col-xs-6 col-md-3">
                              <p>ID No.</p>
                            </div>
                            <div class="col-xs-6 col-md-7">
                              <p>XXXX422 (NRIC)</p>
                            </div>
                          </div>
                          <div class="row profile-line">
                            <div class="col-xs-6 col-md-3">
                              <p>Mobile No.</p>
                            </div>
                            <div class="col-xs-6 col-md-7">
                              <p>+65 90009098</p>
                            </div>
                          </div>
                          <div class="row profile-line">
                            <div class="col-xs-6 col-md-3">
                              <p>Email Address</p>
                            </div>
                            <div class="col-xs-6 col-md-7">
                              <p>lindatan@hotmail.com</p>
                            </div>
                          </div>
                          <div class="row">
                            <div class="col-xs-12">
                              <p>To edit these details, you will need to <a data-toggle="modal" data-target="#officerAmend">amend</a> it first before proceeding with your application.</p>
                            </div>
                          </div>
                        </div>
                      </div>

                      <div id="newOfficer" class="new-officer-form ${cgoIndeNo}-new hidden">
                        <table class="control-grid">
                          <tbody>
                          <tr height="1">
                            <td class="first last" style="width: 100%;">
                              <div  class="control control-caption-horizontal">
                                <div class=" form-group form-horizontal formgap">
                                  <div class="col-sm-4 control-label formtext ">
                                    <label  class="control-label control-set-font control-font-label">Salutation</label>
                                    <span class="mandatory">*</span>
                                    <span class="upload_controls"></span>
                                  </div>
                                  <div class="col-sm-5">
                                    <iais:select  name="salutation" codeCategory="CATE_ID_SALUTATION" value="${currentCgo.salutation}" firstOption="Select Salution"></iais:select>
                                    <span class="error-msg" name="iaisErrorMsg" id="error_salutation${status.index}"></span>
                                  </div>
                                </div>
                              </div>
                            </td>
                          </tr>
                          <tr height="1">
                            <td class="first last" style="width: 100%;">
                              <div  class="control control-caption-horizontal">
                                <div class=" form-group form-horizontal formgap">
                                  <div class="col-sm-4 control-label formtext ">
                                    <label  class="control-label control-set-font control-font-label">
                                      Name
                                    </label>
                                    <span class="mandatory">*</span>
                                    <span class="upload_controls"></span>
                                  </div>
                                  <div class="col-sm-5">
                                    <div class="">
                                      <iais:input maxLength="66" type="text" name="name" value="${currentCgo.name}"></iais:input>
                                      <span class="error-msg" name="iaisErrorMsg" id="error_name${status.index}"></span>
                                    </div>
                                  </div>
                                </div>
                              </div>
                            </td>
                          </tr>
                          <tr height="1">
                            <td class="first last" style="width: 100%;">
                              <div id="control--runtime--3" class="control control-caption-horizontal">
                                <div class=" form-group form-horizontal formgap">
                                  <div class="col-sm-4 control-label formtext ">
                                    <label id="control--runtime--3--label" class="control-label control-set-font control-font-label">ID Type</label>
                                    <span class="mandatory">*</span>
                                    <span class="upload_controls"></span>
                                  </div>
                                  <div class="col-sm-5">
                                    <div class="">
                                      <iais:select  name="idType" value="${currentCgo.idType}" options="IdTypeSelect"></iais:select>
                                      <span class="error-msg" name="iaisErrorMsg" id="error_idTyp${status.index}"></span>
                                    </div>
                                  </div>
                                </div>
                              </div>
                            </td>
                          </tr>
                          <tr height="1">
                            <td class="first last" style="width: 100%;">
                              <div id="control--runtime--28" class="control control-caption-horizontal">
                                <div class=" form-group form-horizontal formgap">
                                  <div class="col-sm-4 control-label formtext ">
                                    <label id="control--runtime--28--label" class="control-label control-set-font control-font-label">
                                      ID No.
                                    </label>
                                    <span class="mandatory">*</span>
                                    <span class="upload_controls"></span>
                                  </div>
                                  <div class="col-sm-5">
                                    <div class="">
                                      <iais:input maxLength="9" type="text" name="idNo" value="${currentCgo.idNo}"></iais:input>
                                      <span class="error-msg" name="iaisErrorMSg" id="error_idNo${status.index}"></span>
                                    </div>
                                  </div>
                                </div>
                              </div>
                            </td>
                          </tr>
                          <tr height="1">
                            <td class="first last" style="width: 100%;">
                              <div  class="control control-caption-horizontal">
                                <div class="form-group form-horizontal formgap">
                                  <div class="col-sm-4 control-label formtext">
                                    <label class="control-label control-set-font control-font-label">Designation</label>
                                    <span class="mandatory">*</span>
                                    <span class="upload_controls"></span>
                                  </div>
                                  <div class="col-sm-5">
                                    <div class="">
                                      <iais:select name="designation" codeCategory="CATE_ID_DESIGNATION" value="${currentCgo.designation}" firstOption="Select Designation"></iais:select>
                                      <span class="error-msg" name="iaisErrorMsg" id="error_designation${status.index}"></span>
                                    </div>
                                  </div>
                                </div>
                              </div>
                            </td>
                          </tr>
                          <tr height="1">
                            <td class="first last" style="width: 100%;">
                              <div  class="control control-caption-horizontal">
                                <div class="form-group form-horizontal formgap">
                                  <div class="col-sm-4 control-label formtext">
                                    <label  class="control-label control-set-font control-font-label">Professional Regn Type</label>
                                    <span class="mandatory">*</span>
                                    <span class="upload_controls"></span>
                                  </div>
                                  <div class="col-sm-5">
                                    <div class="professionRegoType">
                                      <iais:select name="professionRegoType" codeCategory="CATE_ID_PROFESSIONAL_TYPE" value="${currentCgo.professionRegoType}" firstOption="Select professionRegoType"></iais:select>
                                      <span class="error-msg" name="iaisErrorMsg" id="error_professionRegoType${status.index}"></span>
                                    </div>
                                  </div>
                                </div>
                              </div>
                            </td>
                          </tr>
                          <tr height="1">
                            <td class="first last" style="width: 100%;">
                              <div id="control--runtime--31" class="control control-caption-horizontal">
                                <div class=" form-group form-horizontal formgap">
                                  <div class="col-sm-4 control-label formtext ">
                                    <label id="control--runtime--31--label" class="control-label control-set-font control-font-label">
                                      Professional Regn No.
                                    </label>
                                    <span class="mandatory">*</span>
                                    <span class="upload_controls"></span>
                                  </div>
                                  <div class="col-sm-5">
                                    <div class="">
                                      <iais:input maxLength="20" type="text" name="professionRegoNo" value="${currentCgo.professionRegoNo}"></iais:input>
                                      <span class="error-msg" name="iaisErrorMsg" id="error_professionRegoNo${status.index}"></span>
                                    </div>
                                  </div>
                                </div>
                              </div>
                            </td>
                          </tr>
                          <tr height="1">
                            <td class="first last" style="width: 100%;">
                              <div id="control--runtime--29" class="control control-caption-horizontal">
                                <div class="form-group form-horizontal formgap">
                                  <div class="col-sm-4 control-label formtext">
                                    <label id="control--runtime--29--label" class="control-label control-set-font control-font-label">Specialty</label>
                                    <span class="mandatory">*</span>
                                  </div>
                                  <div class="col-sm-5">
                                    <div class="specialtyContent">
                                      <iais:select cssClass="specialty" name="specialty" options="SpecialtySelectList" value="${currentCgo.speciality}" ></iais:select>
                                      <span class="error-msg" name="iaisErrorMsg" id="error_speciality${status.index}"></span>
                                      <c:choose>
                                        <c:when test="${currentCgo.speciality eq 'other'}">
                                          <input name="specialtyOther" type="text"  class="form-control control-input control-set-font control-font-normal" value="${currentCgo.specialityOther}" size="30">
                                        </c:when>
                                        <c:otherwise>
                                          <input name="specialtyOther" type="text"  class="form-control control-input control-set-font control-font-normal hidden" value="" size="30">
                                        </c:otherwise>
                                      </c:choose>

                                    </div>
                                  </div>
                                </div>
                              </div>
                            </td>
                          </tr>
                          <tr height="1">
                            <td class="first last" style="width: 100%;">
                              <div id="control--runtime--32" class="control control-caption-horizontal">
                                <div class=" form-group form-horizontal formgap">
                                  <div class="col-sm-4 control-label formtext ">
                                    <label id="control--runtime--32--label" class="control-label control-set-font control-font-label">
                                      Subspeciality or relevant qualification
                                    </label>
                                  </div>
                                  <div class="col-sm-5">
                                    <div class="">
                                      <iais:input maxLength="100" type="text" name="qualification" value="${currentCgo.qualification}"></iais:input>
                                      <span class="error-msg" name="iaisErrorMsg" id="error_qualification${status.index}"><c:out value="${errorMap.qualification}"></c:out></span>
                                    </div>
                                  </div>
                                </div>
                              </div>
                            </td>
                          </tr>
                          <tr height="1">
                            <td class="first last" style="width: 100%;">
                              <div  class="control control-caption-horizontal">
                                <div class=" form-group form-horizontal formgap">
                                  <div class="col-sm-4 control-label formtext ">
                                    <label  class="control-label control-set-font control-font-label">Mobile No.
                                    </label>                                                                                                                                        <span class="mandatory">*</span>
                                    <span class="upload_controls"></span>
                                  </div>
                                  <div class="col-sm-5">
                                    <div class="">
                                      <iais:input maxLength="8" type="text" name="mobileNo" value="${currentCgo.mobileNo}"></iais:input>
                                      <span class="error-msg" name="iaisErrorMsg" id="error_mobileNo${status.index}"></span>
                                    </div>
                                  </div>
                                </div>
                              </div>
                            </td>
                          </tr>
                          <tr height="1">
                            <td class="first last" style="">
                              <div id="control--runtime--33" class="control control-caption-horizontal">
                                <div class=" form-group form-horizontal formgap">
                                  <div class="col-sm-4 control-label formtext ">
                                    <label id="control--runtime--33--label" class="control-label control-set-font control-font-label">Email address
                                    </label>                                                                                                                                                        <span class="mandatory">*</span>
                                    <span class="upload_controls"></span>
                                  </div>
                                  <div class="col-sm-4">
                                    <div class="">
                                      <iais:input maxLength="66" type="text" name="emailAddress" value="${currentCgo.emailAddr}"></iais:input>
                                      <span class="error-msg" name="iaisErrorMsg" id="error_emailAddr${status.index}"></span>
                                    </div>
                                  </div>
                                </div>
                              </div>
                            </td>
                          </tr>
                          </tbody>
                        </table>
                      </div>
                    </td>
                  </tr>
                  </tbody>
                </table>
                <c:if test="${!status.last}">
                  <hr/>
                </c:if>
              </c:forEach>
              </c:if>
            </div>
          </td>
        </tr>
        <hr/>
        <c:if test="${'BLB'!=currentSvcCode || 'RDS'!=currentSvcCode}">
        <tr id="addInfo">
          <td>
            <span class="addListBtn" style="color:deepskyblue;cursor:pointer;">+ Add Another Clinical Governance Officer</span>
          </td>
        </tr>
        </c:if>
        </tbody>
      </table>
    </div>
  </div>
</div>

<script>
    $(document).ready(function () {
        //coverage  cpl_custom_form_script init css
        $('.new-officer-form > table> tbody> tr:first-child > td >div.control > div.form-group > div:nth-child(2)').removeClass('col-sm-2');
        $('.new-officer-form > table> tbody> tr:first-child > td >div.control > div.form-group > div:nth-child(2)').addClass('col-sm-3');

        $('.new-officer-form > table> tbody> tr:nth-child(3) > td >div.control > div.form-group > div:nth-child(2)').removeClass('col-sm-2');
        $('.new-officer-form > table> tbody> tr:nth-child(3) > td >div.control > div.form-group > div:nth-child(2)').addClass('col-sm-3');

        $('.new-officer-form > table> tbody> tr:nth-child(5) > td >div.control > div.form-group > div:nth-child(2)').removeClass('col-sm-3');
        $('.new-officer-form > table> tbody> tr:nth-child(5) > td >div.control > div.form-group > div:nth-child(2)').addClass('col-sm-4');
        //get from cpl_custom_form_script
        $('select.assignSel').change(function () {
            $parentEle = $(this).closest('td.first');
            if ($(this).val() == "newOfficer") {
                $parentEle.find('> .new-officer-form').removeClass('hidden');
                $parentEle.find('> .profile-info-gp').addClass('hidden');
            } else {
                $parentEle.find('> .profile-info-gp').removeClass('hidden');
                $parentEle.find('> .new-officer-form').addClass('hidden');
            }
        });

        reLoadChange();

        showSpecialty();
    });

    var showSpecialty = function () {
        $('.specialty').change(function () {
            $specialtyEle = $(this).closest('.specialtyContent');
            var val = $(this).val();

            if('other' == val){
                $specialtyEle.find('input[name="specialtyOther"]').removeClass('hidden');
            }else{
                $specialtyEle.find('input[name="specialtyOther"]').addClass('hidden');
            }
        });
    }

    var reLoadChange = function () {
        var i=0;
        $('select.assignSel').each(function (k,v) {
            if("newOfficer" ==$(this).val()){
                var removeClass = '.cgo-'+i+'--new';
                console.log("removeClass"+removeClass);
                $(removeClass).removeClass('hidden');
                i++;
            }
        });
    }

    $('.addListBtn').click(function () {
        /*var assignContent = $('.assignContent:last').html();
        var appendHtml = '<hr/> <table class="testTable">'+ assignContent+'</table>';
        $('.assignContent:last').after(appendHtml);*/
        $.ajax({
            'url':'${pageContext.request.contextPath}/governance-officer-html',
            'dataType':'text',
            'type':'GET',
            'success':function (data) {
                console.log("suc");
                $('.assignContent:last').after(data);
                showSpecialty();

                $('select.assignSel').change(function () {
                    $parentEle = $(this).closest('td.first');
                    if ($(this).val() == "newOfficer") {
                        $parentEle.find('> .new-officer-form').removeClass('hidden');
                        $parentEle.find('> .profile-info-gp').addClass('hidden');
                    } else {
                        $parentEle.find('> .profile-info-gp').removeClass('hidden');
                        $parentEle.find('> .new-officer-form').addClass('hidden');
                    }
                });


            },
            'error':function (data) {
                console.log("err");
            }
        });
    });

</script>