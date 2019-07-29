<%--
  ~   This file is generated by ECQ project skeleton automatically.
  ~
  ~   Copyright 2019-2049, Ecquaria Technologies Pte Ltd. All rights reserved.
  ~
  ~   No part of this material may be copied, reproduced, transmitted,
  ~   stored in a retrieval system, reverse engineered, decompiled,
  ~   disassembled, localised, ported, adapted, varied, modified, reused,
  ~   customised or translated into any language in any form or by any means,
  ~   electronic, mechanical, photocopying, recording or otherwise,
  ~   without the prior written permission of Ecquaria Technologies Pte Ltd.
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



<form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">

<iais:body >

    <br/>
    <h2>Org Account Create</h2>
    <iais:error>
        <c:if test = "${not empty errorMap}">
             <div class="error">
                 <c:forEach items="${errorMap}" var="map">
                     ${map.key}  ${map.value} <br/>
                 </c:forEach>
             </div>
        </c:if>
    </iais:error>
    <iais:section title="Org Account Create" id="orgAccountCreate">
        <iais:row>
          <iais:field value="name" required="true"></iais:field>
          <iais:value width="7">
              <input type="text" name="name" value="" />
          </iais:value>
        </iais:row>
        <iais:row>
            <iais:field value="nirc No" required="true"></iais:field>
            <iais:value width="7">
                <input type="text" name="nircNo" value="" />
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field value="corp Pass Id No" required="true"></iais:field>
            <iais:value width="7">
                <input type="text" name="corpPassId" value="" />
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field value="Status" required="true"></iais:field>
            <iais:value width="7">
                <select name="status" id="">
                    <option value="pending">pending</option>
                    <option value="procing">Procing</option>
                </select>
            </iais:value>
        </iais:row>
        <iais:action>
            <button type="button"  class="btn btn-default" onclick="javascript:doCancel();">Cancel</button>
            <button type="button" class="btn btn-lg btn-login-submit" onclick="javascript:doSave('${orgId}');">Submit</button>
        </iais:action>
    </iais:section>

</iais:body>


</form>

<script type="text/javascript">
function doCancel(){
SOP.Crud.cfxSubmit("mainForm","cancel");
}
function doSave(orgId){
SOP.Crud.cfxSubmit("mainForm","save",orgId);
}

</script>



























