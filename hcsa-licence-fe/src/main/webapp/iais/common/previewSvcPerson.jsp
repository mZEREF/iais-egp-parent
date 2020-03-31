<div class="amended-service-info-gp">
  <h2>SERVICE PERSON</h2>
<c:forEach items="${currentPreviewSvcInfo.appSvcPersonnelDtoList}" var="appSvcPersonnelDtoList">
  <c:if test="{appSvcPersonnelDtoList.personnelType=='SPPT003'}" >
    <label>PersonnelType: </label>Medical Physicist
    <label>Name</label>${appSvcPersonnelDtoList.name}
  </c:if>

    <label>PersonnelType:</label>Radiation Safety Officer
    <label>Name:</label>${appSvcPersonnelDtoList.name}
    <label>Qualification:</label>${appSvcPersonnelDtoList.quaification}
    <label>Relevant working experience (Years):</label>${appSvcPersonnelDtoList.wrkExpYear}


</c:forEach>

</div>