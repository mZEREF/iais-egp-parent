<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
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
    <input type="hidden" name="crud_action_additional" value="">
    <div class="col-lg-12 col-xs-10">
    <div class="bg-title" style="text-align: center">

      <h2>  HCSA Configurator Module</h2>

    </div>
    </div>
    <div class="components">
      <a class="btn btn-secondary">Back</a>
    </div>
    <div>
<br>
    <div  class="col-lg-12 col-xs-10">
      <table border="1px">
        <tr>
          <td style="width: 25%;text-align: center">Service Name</td>
          <td style="width: 20%;text-align: center">Usage</td>
          <td style="width: 25%;text-align: center">Effective Start Date</td>
          <td style="width: 20%;text-align: center">Effective End Date</td>
          <td style="width: 25%;text-align: center">Actions</td>
        </tr>
      <tr>
        <td  style="text-align: center">Medical Clinic</td>
        <td  style="text-align: center">Active</td>
        <td  style="text-align: center">1 Jan 2018</td>
        <td  style="text-align: center">1 Jan 2020</td>
        <td  style="text-align: center"><button onclick="edit()">edit</button><button onclick="del()">delete</button></td>

      </tr>
      </table>

    </div>

</div>
  </form>
</div>

<script type="text/javascript">
   function edit() {
       SOP.Crud.cfxSubmit("mainForm","edit");
   }
   function del() {
       SOP.Crud.cfxSubmit("mainForm","delete");
   }

</script>
</>
