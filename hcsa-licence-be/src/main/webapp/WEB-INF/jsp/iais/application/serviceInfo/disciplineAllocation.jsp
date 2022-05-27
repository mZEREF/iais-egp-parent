<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>

<style>
  .padding-left{
    padding-left: 0px;
  }
</style>

<div class="discipline-allocation">
  <h2>${currStepName}</h2>
  <p>${CURR_STEP_NAME_LABLE}.</p>
  <div class="table-gp">
    <c:if test="${AppSubmissionDto.needEditController}">
      <c:forEach var="clickEditPage" items="${AppSubmissionDto.clickEditPage}">
        <c:if test="${'APPSPN03' == clickEditPage}">
          <c:set var="isClickEdit" value="true"/>
        </c:if>
      </c:forEach>
      <c:choose>
        <c:when test="${'true' != isClickEdit}">
          <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
        </c:when>
        <c:otherwise>
          <input id="isEditHiddenVal" type="hidden" name="isEdit" value="1"/>
        </c:otherwise>
      </c:choose>
      <c:if test="${('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType) && requestInformationConfig == null}">
        <div class="text-right app-font-size-16">
          <a class="back" id="RfcSkip" href="javascript:void(0);">
            Skip<span>&nbsp;</span><em class="fa fa-angle-right"></em>
          </a>
        </div>
      </c:if>
      <c:if test="${'true' != isClickEdit}">
        <c:set var="locking" value="true"/>
        <div id="edit-content">
          <c:choose>
            <c:when test="${AppSubmissionDto.appEditSelectDto.serviceEdit}">
              <div class="text-right app-font-size-16">
                <a id="edit" class="allocationEdit" href="javascript:void(0);">
                  <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
                </a>
              </div>
            </c:when>
            <c:otherwise>

            </c:otherwise>
          </c:choose>
        </div>
      </c:if>
    </c:if>
    <table aria-describedby="" class="table discipline-table">
      <thead>
      <tr>
        <th scope="col" >Mode of Service Delivery</th>
        <th scope="col" >${svcScopePageName}</th>
        <th scope="col" style="width:30%">Clinical Governance Officers</th>
        <th scope="col" style="width:30%">Section Leader</th>
      </tr>
      </thead>
      <c:set var="row_count" value="0"></c:set>
      <c:forEach var="premisesAndChkLst" items="${PremisesAndChkLst}" varStatus="sta">
        <c:set value="${premisesAndChkLst.premiseVal}" var="premisesIndexNo"/>
        <tbody>
        <c:forEach var="chkLst" items="${premisesAndChkLst.appSvcChckListDtoList}" varStatus="status"  >
          <c:set value="${premisesIndexNo}${status.index}" var="cgoName"/>
          <c:set value="cgo${premisesIndexNo}${status.index}" var="cgoSelName"/>
          <c:set value="sl${premisesIndexNo}${status.index}" var="slSelName"/>
          <tr>
            <c:if test="${status.first}">
              <td rowspan="${premisesAndChkLst.appSvcChckListDtoList.size()}" style="width:38%" class="premAddress">
                <p class="visible-xs visible-sm table-row-title">Mode of Service Delivery</p>
                <input type="hidden" name="${premisesIndexNo}" value="${premisesIndexNo}" />
                <p>${premisesAndChkLst.premiseGetAddress} </p>
              </td>
            </c:if>
            <td>
              <p class="visible-xs visible-sm table-row-title">Laboratory Disciplines</p>
              <input type="hidden" name="${cgoName}" value="${chkLst.chkLstConfId}"/>
              <c:choose>
                <c:when test="${chkLst.chkName=='Please indicate'}"><p>${chkLst.otherScopeName}</p></c:when>
                <c:otherwise><p>${chkLst.chkName}</p></c:otherwise>
              </c:choose>
            </td>
            <td>
              <p class="visible-xs visible-sm table-row-title">Clinical Governance Officers</p>
              <c:set var="cgoSelKey" value="cgo${premisesIndexNo}${chkLst.chkLstConfId}"/>
              <iais:select cssClass="cgo ${premisesIndexNo}" name="${cgoSelName}" firstOption="Please Select"
                            options="CgoSelect" value="${ReloadAllocationMap[cgoSelKey]}" />
              <div class="col-sm-10 padding-left" style="margin-top: 2%">
                <span  class="error-msg" name="iaisErrorMsg" id="error_disciplineAllocation${row_count}"></span>
              </div>
            </td>
            <td>
              <p class="visible-xs visible-sm table-row-title">Section Leader</p>
              <c:set var="slSelKey" value="sl${premisesIndexNo}${chkLst.chkLstConfId}"/>
              <iais:select cssClass="sl ${premisesIndexNo}" name="${slSelName}" firstOption="Please Select"
                            options="slSelectOpts" value="${ReloadAllocationMap[slSelKey]}" />
              <div class="col-sm-10 padding-left" style="margin-top: 2%">
                <span  class="error-msg" name="iaisErrorMsg" id="error_disciplineAllocationSl${row_count}"></span>
                <c:set var="row_count" value="${row_count+1}" />
              </div>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </c:forEach>


    </table>
    <span class="error-msg" name="iaisErrorMsg" id="error_CGO"></span>
    <br>
    <span class="error-msg" name="iaisErrorMsg" id="error_SL"></span>
  </div>
</div>

<script type="text/javascript">
    $(document).ready(function() {
        if(${AppSubmissionDto.needEditController && !isClickEdit}){
            $('div.nice-select').addClass('disabled');
        }

        $('div.discipline-allocation').find('.nice-select').css('width','100%');

        //Binding method
        $('#disciplineAllocationBack').click(function(){
            var controlFormLi = $('#controlFormLi').val();
            submitForms('governanceOfficers',null,null,controlFormLi);
        });
        $('#disciplineAllocationSaveDraft').click(function(){
            submitForms('principalOfficers','saveDraft',null,'clinical');
        });
        $('#disciplineAllocationNext').click(function(){
            var controlFormLi = $('#controlFormLi').val();
            submitForms('principalOfficers',null,'next',controlFormLi);
        });

        doEdit();

        $('.premAddress').css('vertical-align','top');
        if($("#errorMapIs").val()=='error'){
            $('#edit').trigger('click');
        }

    });

    var doEdit = function () {
        $('#edit').click(function () {
            $('#edit-content').addClass('hidden');
            $('#isEditHiddenVal').val('1');
            $('div.${RfiPremiseId}').removeClass('disabled');
        });
    }
</script>