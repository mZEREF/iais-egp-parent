<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 10/14/2019
  Time: 1:46 PM
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
    <iais:section title="Checklist Item Add" id="items">
        <iais:row>
            <%--<iais:mask name="itemId" value="${itemRequestDto.itemId}"></iais:mask>--%>
            <iais:field value="Item Id" required="false"></iais:field>
            <iais:value width="7">

                <input type="text" name="itemId" value="${itemRequestDto.itemId}" />
            </iais:value>

            <iais:field value="Regulation Clause Number" required="false"></iais:field>
            <iais:value width="7">
                <iais:select name="regulationClauseNo"  options="clauseSelect" firstOption="Select Clause Number"  value="${itemRequestDto.regulationClauseNo}"></iais:select>
            </iais:value>

            <iais:field value="Regulation" required="false"></iais:field>
            <iais:value width="7">
                <iais:value width="7">
                    <input type="text" name="regulationClause"  value="${itemRequestDto.regulationClause}" />
                </iais:value>
            </iais:value>

            <iais:field value="Checklist Item" required="false"></iais:field>
            <iais:value width="7">
                <iais:value width="7">
                    <input type="text" name="checklistItem" value="${itemRequestDto.checklistItem}" />
                </iais:value>
            </iais:value>

            <iais:field value="Status" required="false"></iais:field>
            <iais:value width="7">
                <iais:select name="status" id="status" codeCategory="CATE_ID_COMMON_STATUS" firstOption="Select Status" value="${itemRequestDto.status}"></iais:select>
            </iais:value>

            <iais:field value="Risk Level" required="false"></iais:field>
            <iais:value width="7">
                <iais:select name="riskLevel" id="riskLevel" codeCategory="CATE_ID_RISK_LEVEL" firstOption="Select Risk Level" value="${itemRequestDto.riskLevel}"></iais:select>
            </iais:value>

            <iais:field value="Answer Type" required="false"></iais:field>
            <iais:value width="7">
                <iais:select name="answerType" id="answerType" codeCategory="CATE_ID_ANSWER_TYPE" firstOption="Select Answer Type" value="${itemRequestDto.answerType}"></iais:select>
            </iais:value>

        </iais:row>


        <c:choose>
            <c:when test="${btnTag eq 'SubmitButton'}">
                <iais:action>
                    <button type="button" class="btn btn-lg btn-login-submit" onclick="javascript:doSubmit();">Submit</button>
                </iais:action>
            </c:when>
            <c:when test="${btnTag eq 'UpdataButton'}">
                <iais:action>
                    <button type="button" class="btn btn-lg btn-login-submit" onclick="javascript:doUpdate();">Update</button>
                </iais:action>

            </c:when>
        </c:choose>

        <iais:action>
            <button type="button" class="btn btn-lg btn-login-submit" onclick="javascript:doCancel();">Cancel</button>
        </iais:action>
    </iais:section>

</iais:body>

</form>


<script type="text/javascript">
    function doSubmit(){
        SOP.Crud.cfxSubmit("mainForm", "saveChecklistItem");
    }

    function doUpdate(){
        SOP.Crud.cfxSubmit("mainForm", "saveChecklistItem");
    }

    function doCancel(){
        SOP.Crud.cfxSubmit("mainForm", "doCancel");
    }

</script>