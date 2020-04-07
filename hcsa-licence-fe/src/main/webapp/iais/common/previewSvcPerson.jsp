<div class="amended-service-info-gp">
  <label style="font-size: 2.2rem">SERVICE PERSONNEL</label>
  <div class="amend-preview-info">
    <c:forEach items="${currentPreviewSvcInfo.appSvcPersonnelDtoList}" var="appSvcPersonnelDtoList" varStatus="status">
      <p>Service Personnel ${status.index+1}:</p>
      <div class="form-check-gp">
        <div class="row">
          <div class="col-xs-12">
            <table class="col-xs-8">
              <c:choose>
                <c:when test="${currentPreviewSvcInfo.serviceCode=='BLB'}">
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Designation :</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.designation}</p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name :</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.name}</p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Professional Registration No.:</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.profRegNo}</p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Relevant working experience (Years) :</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.wrkExpYear}</p>
                    </td>
                  </tr>
                </c:when>
                <c:when test="${currentPreviewSvcInfo.serviceCode=='TCB'}">
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name :</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.name}</p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Qualification :</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.quaification}</p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Relevant working experience (Years) :</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.wrkExpYear}</p>
                    </td>
                  </tr>

                </c:when>
                <c:when test="${appSvcPersonnelDtoList.personnelType=='SPPT001'}">
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Select Service Personnel :</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Radiology Professional</p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name :</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.name}</p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Designation :</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.designation}</p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Qualification :</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.quaification}</p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Relevant working experience (Years) :</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.wrkExpYear}</p>
                    </td>
                  </tr>
                </c:when>
                <c:when test="${appSvcPersonnelDtoList.personnelType=='SPPT002'}">
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Select Service Personnel :</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Medical Physicist</p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name :</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.name}</p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Qualification :</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.quaification}</p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Relevant working experience (Years) :</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.wrkExpYear}</p>
                    </td>
                  </tr>
                </c:when>
                <c:when test="${appSvcPersonnelDtoList.personnelType=='SPPT003'}">
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Select Service Personnel :</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Radiation Safety Officer</p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name :</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.name}</p>
                    </td>
                  </tr>
                </c:when>
                <c:when test="${appSvcPersonnelDtoList.personnelType=='SPPT004'}">
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Select Service Personnel :</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Registered Nurse</p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name :</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.name}</p>
                    </td>
                  </tr>
                  <tr>
                    <td class="col-xs-8">
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Professional Registration No :</p>
                    </td>
                    <td>
                      <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.profRegNo}</p>
                    </td>
                  </tr>
                </c:when>
              </c:choose>
            </table>
          </div>
        </div>

      </div>

    </c:forEach>

  </div>

</div>