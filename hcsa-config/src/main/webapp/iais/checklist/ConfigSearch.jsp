<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 11/8/2019
  Time: 10:27 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>


<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>


<webui:setAttribute name="header-ext">
  <%
    /* You can add additional content (SCRIPT, STYLE elements)
     * which need to be placed inside HEAD element here.
     */
  %>
</webui:setAttribute>


<webui:setAttribute name="title">
  <%
    /* You can set your page title here. */
  %>

  <%=process.runtime.getCurrentComponentName()%>

</webui:setAttribute>
<!-- START: CSS -->

<!-- END: CSS -->

<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
  <input type="hidden" name="crud_action_type" value="">
  <input type="hidden" name="crud_action_value" value="">
  <input type="hidden" name="crud_action_additional" value="">


<iais:body>
  <iais:section title="Checklist Item View" id="checklistItemConfigSection">
    <iais:row>
      <iais:field value="Common" required="true"></iais:field>
      <iais:value width="7">
          <input type="radio" name="common" value="General Regulation"/> General Regulation
      </iais:value>

      <br>

      <iais:field value="Module" required="false"></iais:field>
      <iais:value width="7">
        <input name="moduleCheckBox" id="moduleCheckBox" type="checkbox" value="New Application" />New Application <br>
        <input name="moduleCheckBox" id="moduleCheckBox" type="checkbox" value="Renewal" /> Renewal<br>
        <input name="moduleCheckBox" id="moduleCheckBox" type="checkbox" value="Amendment" />Amendment<br>
        <input name="moduleCheckBox" id="moduleCheckBox" type="checkbox" value="Audit" />Audit<br>
        <input name="moduleCheckBox" id="moduleCheckBox" type="checkbox" value="Reinstatement" />Reinstatement<br>
      </iais:value>

      <br>
      <br>
      <br>
      <br>
      <br>
      ===========>>>>>>>>>>>>>>>>>>>>>
      <iais:field value="Type" required="false"></iais:field>
      <iais:value width="7">
        <input name="moduleCheckBox" id="moduleCheckBox" type="checkbox" value="Self-Assessment" />Self-Assessment <br>
        <input name="moduleCheckBox" id="moduleCheckBox" type="checkbox" value="Inspection" /> Inspection<br>
        <input name="moduleCheckBox" id="moduleCheckBox" type="checkbox" value="Audit Inspection" />Audit Inspection<br>
      </iais:value>

      <br>

      <iais:field value="Service Name" required="false"></iais:field>
      <iais:value width="7">
        <iais:select name="serviceName" id="serviceName" codeCategory="CATE_ID_COMMON_STATUS" firstOption="Select Service Name"></iais:select>
      </iais:value>


    </iais:row>


  </iais:section>


</iais:body>



</form>



<script type="text/javascript">
  function doSearch(){
  SOP.Crud.cfxSubmit("mainForm", "doSearch");
  }

  function prepareAddItem(){
  SOP.Crud.cfxSubmit("mainForm", "prepareAddItem");
  }

  function prepareClone(){

  console.log("==========1=>>>>>>>>>" + $("#itemCheckbox").val());
  console.log("==========2=>>>>>>>>>" +document.getElementsByName("itemCheckbox").values());


  SOP.Crud.cfxSubmit("mainForm", "viewCloneData");
  }

  function prepareEditItem(id){
  SOP.Crud.cfxSubmit("mainForm", "prepareEditItem", id);
  }

  function doCancel(){
  SOP.Crud.cfxSubmit("mainForm","doCancel");
  }
</script>