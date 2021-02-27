<c:forEach var="stepSchem" items="${currentPreviewSvcInfo.hcsaServiceStepSchemeDtos}">
    <c:if test="${stepSchem.stepCode == 'SVST007'}">
        <c:set var="currStepName" value="${stepSchem.stepName}"/>
    </c:if>
</c:forEach>
<div class="amended-service-info-gp">
  <label style="font-size: 2.2rem">${currStepName}</label>
  <div class="amend-preview-info">
    <div class="form-check-gp">
      <div class="row">
        <div class="col-xs-12">
          <c:forEach items="${currentPreviewSvcInfo.appSvcMedAlertPersonList}" var="appSvcMedAlertPerson" varStatus="status">

            <div  class="col-xs-12" style="margin-bottom: 1%;margin-top: 1%">
              <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><strong>MedAlert Person<c:if test="${currentPreviewSvcInfo.appSvcMedAlertPersonList.size() > 1}"> ${status.index+1}</c:if>: </strong></p>
            </div>
            <table class="col-xs-8">

              <tr>
                <td class="col-xs-8">
                  <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Salutation</p>
                </td>
                <td>
                  <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${appSvcMedAlertPerson.salutation}"/></p>
                </td>
              </tr>

              <tr>
                <td class="col-xs-8">
                  <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name</p>
                </td>
                <td>
                  <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${appSvcMedAlertPerson.name}"/></p>
                </td>
              </tr>

              <tr>
                <td class="col-xs-8">
                  <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID Type</p>
                </td>
                <td>
                  <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${appSvcMedAlertPerson.idType}"/></p>
                </td>
              </tr>

              <tr>
                <td class="col-xs-8">
                  <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID No.</p>
                </td>
                <td>
                  <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${appSvcMedAlertPerson.idNo}"/></p>
                </td>
              </tr>

              <tr>
                <td class="col-xs-8">
                  <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Mobile No.</p>
                </td>
                <td>
                  <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${appSvcMedAlertPerson.mobileNo}"/></p>
                </td>
              </tr>

              <tr>
                <td class="col-xs-8">
                  <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Email Address</p>
                </td>
                <td>
                  <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${appSvcMedAlertPerson.emailAddr}"/></p>
                </td>
              </tr>

            </table>
          </c:forEach>
        </div>
      </div>
    </div>

  </div>

</div>

