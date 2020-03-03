<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>

<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");

%>
<webui:setLayout name="iais-internet"/>
<%@ include file="../dashboard.jsp" %>
<form method="post" id="mainForm" class="__egovform" action=<%=process.runtime.continueURL()%>>
  <%@ include file="/include/formHidden.jsp" %>
  <input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator"/>
  <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.ApplicationValidateDto"/>
  <input type="hidden" name="valProfiles" id="valProfiles" value=""/>
  <div class="main-content">
    <div class="container">
      <div class="row">
        <div class="col-xs-12">
          <div class="tab-gp steps-tab">
            <%@ include file="../navTabs.jsp" %>
            <div class="tab-content">
              <div class="tab-pane active" id="serviceInformationTab" role="tabpanel">
                <div class="multiservice">
                  <div class="tab-gp side-tab clearfix">
                    <%@ include file="../formTabs.jsp" %>
                    <div class="tab-content">
                      <div class="tab-pane active" id="clinicalLab" role="tabpanel">
                        <%@ include file="step.jsp" %>
                        <div class="application-service-steps">
                          <div class="discipline-allocation">
                            <h2>Discipline Allocation</h2>
                            <p>Please ensure that each laboratory discipline is assigned to a clinical governance officer.</p>
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
                                <c:if test="${'APTY005' ==AppSubmissionDto.appType && requestInformationConfig == null}">
                                  <p class="text-right"><a class="back" id="RfcSkip">Skip<em class="fa fa-angle-right"></em></a></p>
                                </c:if>
                                <c:if test="${'true' != isClickEdit}">
                                  <c:set var="locking" value="true"/>
                                  <div id="edit-content">
                                    <c:choose>
                                      <c:when test="${AppSubmissionDto.appEditSelectDto.serviceEdit}">
                                        <p class="text-right"><a id="edit"><i class="fa fa-pencil-square-o"></i>Edit</a></p>
                                      </c:when>
                                      <c:otherwise>

                                      </c:otherwise>
                                    </c:choose>
                                  </div>
                                </c:if>
                              </c:if>
                              <table class="table discipline-table">
                                <thead>
                                <tr>
                                  <th>Premises</th>
                                  <th>Laboratory Disciplines</th>
                                  <th>Clinical Governance Officers</th>
                                </tr>
                                </thead>
                                <c:forEach var="premisesAndChkLst" items="${PremisesAndChkLst}" >
                                  <c:set value="${premisesAndChkLst.premiseVal}" var="premisesIndexNo"/>
                                  <tbody>
                                  <c:forEach var="chkLst" items="${premisesAndChkLst.appSvcChckListDtoList}" varStatus="status"  >
                                    <c:set value="${premisesIndexNo}${status.index}" var="cgoName"/>
                                    <tr>
                                      <c:if test="${status.first}">
                                        <td rowspan="${premisesAndChkLst.appSvcChckListDtoList.size()}">
                                          <p class="visible-xs visible-sm table-row-title">Premises</p>
                                          <input type="hidden" name="${premisesIndexNo}" value="${premisesIndexNo}" />
                                          <p>${premisesAndChkLst.premiseGetAddress} </p>
                                        </td>
                                      </c:if>
                                      <td>
                                        <p class="visible-xs visible-sm table-row-title">Laboratory Disciplines</p>
                                        <input type="hidden" name="${cgoName}" value="${chkLst.chkLstConfId}"/>
                                        <p>${chkLst.chkName}</p>
                                      </td>
                                      <td>
                                        <p class="visible-xs visible-sm table-row-title">Clinical Governance Officers</p>
                                        <c:set var="cgoSelKey" value="${premisesIndexNo}${chkLst.chkLstConfId}"/>

                                        <iais:select cssClass="${premisesIndexNo}" name="${cgoName}" firstOption="select cgo"  options="CgoSelect"  value="${ReloadAllocationMap[cgoSelKey]}"></iais:select>
                                        <span class="error-msg" name="iaisErrorMsg" id="error_disciplineAllocation${status.index}"></span>
                                      </td>
                                    </tr>
                                  </c:forEach>
                                  </tbody>
                                </c:forEach>


                              </table>
                              <p>Click <a href="#">here</a> to assign a laboratory discipline to multiple clinical governance officers.</p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <%@ include file="next.jsp" %>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <%@ include file="/include/validation.jsp" %>
</form>

<script type="text/javascript">
    $(document).ready(function() {
        if(${AppSubmissionDto.needEditController && AppSubmissionDto.appEditSelectDto.serviceEdit && !isClickEdit}){
            $('div.nice-select').addClass('disabled');
        }

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

    });

    var doEdit = function () {
        $('#edit').click(function () {
            $('#edit-content').addClass('hidden');
            $('#isEditHiddenVal').val('1');
            $('div.nice-select').removeClass('disabled');
        });
    }
</script>