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

<style>
.btn
{
    display:inline;
}
</style>

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

            <iais:field value="regulationId" required="false"></iais:field>
            <iais:value width="7">
                <input type="text" name="regulationId" value="${itemRequestDto.regulationId}" />
            </iais:value>


            <iais:field value="Regulation Clause Number" required="false"></iais:field>
            <iais:value width="7">
                <iais:select name="regulationClause" id="regulationClause" options="clauseSelect" firstOption="Select Clause Number"  value="${itemRequestDto.regulationClause}"></iais:select>
            </iais:value>

            <iais:field value="Regulation" required="false"></iais:field>
            <iais:value width="7">
                <iais:value width="7">
                    <input type="text" name="regulationDesc"  value="${itemRequestDto.regulationDesc}" />
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



        <iais:action>
            <button type="button" id="addBtn" class="btn btn-lg btn-login-submit" onclick="javascript:doSubmit();">Add</button>
        </iais:action>

        <iais:action>
            <button type="button" id="updateBtn" class="btn btn-lg btn-login-submit" onclick="javascript:doUpdate();">Update</button>
        </iais:action>

        <iais:action>
            <button type="button" id="cancelBtn" class="btn btn-lg btn-login-submit" onclick="javascript:doCancel();">Cancel</button>
        </iais:action>
    </iais:section>

</iais:body>

</form>


<script type="text/javascript">
    window.onload = function(){
        var url = location.href;
        if(url.indexOf("?") == -1)
        {
            return;
        }

        var paramStr = url.substring(url.indexOf("?") + 1, url.length - 1);
        var parameters = paramStr.split("&");

        var action;
        var pos;
        for(var i = 0; i < parameters.length; i ++){
            var param = parameters[i];
            pos = param.indexOf("=");
            if(pos == -1){
                continue;
            }

            paramName = param.substring(0, pos);
            if(param.substring(0, pos) == ""){
                continue;
            }

            paramValue = param.substring(pos + 1);

            if($("input[name='paramName']".length > 0)){
                $("input[name='paramName']").val(paramValue);
            }

            if($("select[name='paramName']".length > 0)){
                $("select[name='paramName']").val(paramValue)
            }

            $(paramName).change(function(){
                this.value = paramValue;
            })

            if("crud_action_type" == paramName){
                action = paramValue;
                if(action == "doEdit"){
                   $("addBtn").hide();
                    $("updateBtn").show();
                }else if(action = "doCreate"){
                    $("addBtn").show();
                    $("updateBtn").hide();
                }
            }
        }
    }


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