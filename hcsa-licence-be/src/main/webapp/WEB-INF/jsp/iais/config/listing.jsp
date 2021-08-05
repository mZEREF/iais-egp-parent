<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<webui:setLayout name="iais-intranet"/>

<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>

<div class="main-content">
  <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="action_id_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <div class="col-lg-12 col-xs-10">
    <div class="bg-title" style="text-align: center">

      <h2>  HCSA Configurator Module</h2>

    </div>
    </div>
    <div class="components">
      <a class="btn btn-secondary" onclick="back()" > Back</a>
    </div>
    <div>
<br>
    <div  class="col-lg-12 col-xs-10">
      <table aria-describedby="" border="1px">
        <tr>
          <th scope="col" style="width: 25%;text-align: center"><strong>Service Name</strong></th>
          <th scope="col" style="width: 15%;text-align: center"><strong>Usage</strong></th>
          <th scope="col" style="width: 20%;text-align: center"><strong>Effective Start Date</strong></th>
          <th scope="col" style="width: 20%;text-align: center"><strong>Effective End Date</strong></th>
          <th scope="col" style="width: 5%;text-align: center"><strong>Version</strong></th>
          <th scope="col" style="width: 30%;text-align: center"><strong>Actions</strong></th>
        </tr>
        <c:forEach items="${hcsaServiceDtos}"  var="hcsaServiceDto">
      <tr>
        <td  style="text-align: center">${hcsaServiceDto.svcName}</td>
        <td  style="text-align: center"><c:if test="${hcsaServiceDto.status=='CMSTAT001'}">Active</c:if>
          <c:if test="${hcsaServiceDto.status=='CMSTAT003'}">Inactive</c:if></td>

        <td  style="text-align: center">${hcsaServiceDto.effectiveDate}</td>
        <td  style="text-align: center"><fmt:formatDate value="${hcsaServiceDto.endDate}" pattern="dd/MM/yyyy"/></td>
        <td  style="text-align: center">${hcsaServiceDto.version}</td>
        <td  style="text-align: center">
          <button onclick="edit(this)"  class="btn btn-default btn-sm" value="<iais:mask name="crud_action_value"  value="${hcsaServiceDto.id}"/>">update</button>
          <button value="<iais:mask name="crud_action_value"  value="${hcsaServiceDto.id}"/>" class="btn btn-default btn-sm" onclick="del(this)">delete</button>
        </td>

      </tr>
        </c:forEach>
      </table>

    </div>
</div>
  <input type="hidden" id="reasult" value="${delete}" >
  </form>
</div>
<iais:confirm msg="${deleteFile}" needCancel="false"  callBack="cancel()" popupOrder="deleteFile"></iais:confirm>
<script type="text/javascript">
  $(document).ready(function () {
      if($('#reasult').val()=='fail'){
          $('#deleteFile').modal("show");
      }else if($('#reasult').val()=='success'){
        $('#deleteFile').modal("show");
      }

  });
   function edit(obj) {
       SOP.Crud.cfxSubmit("mainForm","edit",$(obj).val(),"");
   }
   function del(obj) {
       SOP.Crud.cfxSubmit("mainForm","delete",$(obj).val(),"");
   }

   function  back() {
       SOP.Crud.cfxSubmit("mainForm","back");
   }
  function cancel() {
    $('#deleteFile').modal("hide");
  }

</script>
</>
