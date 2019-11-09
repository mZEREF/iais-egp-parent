<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 11/7/2019
  Time: 3:10 PM
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
    <iais:section title="Checklist Item Clone" id="chklItemClone"/>
    ============>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
  <iais:searchSection title="" onclick="">
  <div class="table-responsive" id="no-more-tables">
    <table class="table table-bordered table-condensed cf alignctr shadow" id="tableId">
      <colgroup>
        <col style="width: 10%;"/>
        <col style="width: 20%;"/>
        <col style="width: 20%;"/>
        <col style="width: 20%;"/>
        <col style="width: 20%;"/>
        <col style="width: 10%;"/>
      </colgroup>
      <thead>
      <tr>
        <iais:sortableHeader needSort="false"  field="" value="No."></iais:sortableHeader>
        <td></td>
        <iais:sortableHeader needSort="true"   field="regulationClauseNo" value="Regulation Clause Number"></iais:sortableHeader>
        <iais:sortableHeader needSort="true"   field="regulationClause" value="Regulations"></iais:sortableHeader>
        <iais:sortableHeader needSort="true"   field="checklistItem" value="Checklist Item"></iais:sortableHeader>
        <iais:sortableHeader needSort="true"   field="riskLevel" value="Rusk Level"></iais:sortableHeader>
        <iais:sortableHeader needSort="false"   field="status" value="Status"></iais:sortableHeader>
        <iais:sortableHeader needSort="false"   field="action" value="Action"></iais:sortableHeader>
      </tr>
      </thead>

      <tbody style="text-align: center">
      <c:choose>
        <c:when test="${empty cloneItems}">
          <tr>
            <td colspan="6">
              No Record!!
            </td>
          </tr>
        </c:when>
        <c:otherwise>
          <%-- message entity--%>

          <c:forEach var = "cloneItem" items = "${cloneItems}" varStatus="status">
            <tr>
              <td></td>
              <td></td>
              <td>${cloneItem.regulationClauseNo}</td>
              <td>${cloneItem.regulationClause}</td>
              <td>${cloneItem.checklistItem}</td>
              <td>${cloneItem.riskLevel}</td>
              <td>${cloneItem.status}</td>
              <td>
                <iais:link icon="form_edit" title="Edit" onclick="javascript:prepareCloneItem('${cloneItem.itemId}');"/>
              </td>
            </tr>
          </c:forEach>
        </c:otherwise>
      </c:choose>
      </tbody>


    </table>

    </iais:searchSection>


    <iais:action>
      <button type="button" class="btn btn-lg btn-login-submit" onclick="javascript:submitCloneItem();">Submit</button>
    </iais:action>

    <iais:action>
      <button type="button" class="btn btn-lg btn-login-submit" onclick="javascript:cancelClone();">Cancel</button>
    </iais:action>


  </iais:body>



</form>

<script type="text/javascript">

    function prepareCloneItem(itemId){
        SOP.Crud.cfxSubmit("mainForm", "prepareCloneItem", itemId);
    }

    function submitCloneItem(){
        SOP.Crud.cfxSubmit("mainForm", "submitCloneItem");
    }

    function cancelClone(){
        SOP.Crud.cfxSubmit("mainForm","cancelClone");
    }

</script>