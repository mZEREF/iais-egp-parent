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

            <h2>   HCSA Configurator Module</h2>


    </div>
      <div class="components">
       <a class="btn btn-secondary">Back</a>
      </div>
    <div>
    <br>

    <svg width="500" height="500" onclick="add()" style="background-color: #E2E2E2" version="1.1" xmlns="http://www.w3.org/2000/svg">
     Add a new HCSA Module

    </svg>
    <svg width="500" height="500" onclick="list()" style="background-color: #E2E2E2" version="1.1" xmlns="http://www.w3.org/2000/svg">
      List HCSA Modules
    </svg>
  </div>

    </div>


  </form>

</div>
<script type="text/javascript">

    function add() {
        SOP.Crud.cfxSubmit("mainForm","addNewService","addNewService","");
    }
    function list() {
    SOP.Crud.cfxSubmit("mainForm","list");
    }

</script>
</>
