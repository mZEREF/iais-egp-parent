<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 9/5/2019
  Time: 5:30 PM
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
<form id = "spform" method = "post" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">


    <iais:body>
        <iais:section title="System Parameter List" id="msgList">
            <iais:error>
                <c:if test = "${not empty errorMap}">
                    <div class="error">
                        <c:forEach items="${errorMap}" var="map">
                            ${map.key}  ${map.value} <br/>
                        </c:forEach>
                    </div>
                </c:if>
            </iais:error>

            <iais:row>
                <iais:field value="Domain Type" required="true"></iais:field>
                <iais:value width="7">
                    <iais:select name="domainType" options="domainTypeSelect" firstOption="Please select" value="${parameterRequestDto.domainType}" ></iais:select>
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field value="Module" required="true"></iais:field>
                <iais:value width="7">
                    <iais:select name="module" options="moduleTypeSelect" firstOption="Please select" value="${parameterRequestDto.module}"></iais:select>
                </iais:value>
            </iais:row>


            <iais:row>
                <iais:field value="Description" required="false"></iais:field>
                <iais:value width="7">
                    <input type="text" name="description" value="${parameterRequestDto.description}" />
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field value="Value" required="false"></iais:field>
                <iais:value width="7">
                    <input type="text" name="value" value="${parameterRequestDto.value}" />
                </iais:value>
            </iais:row>

        </iais:section>
    </iais:body>

    <tr>
        <iais:action>
            <button type="button" class="btn" onclick="javascript:doEdit('${parameterRequestDto.rowguid}');">Update</button>
            <button type="button" class="btn" onclick="javascript:doCancel();">Cancel</button>
        </iais:action>
    </tr>



</form>

<script type="text/javascript">


    function doEdit(rowguid){
        if(confirm('are sure you want to edit ? ')){
            SOP.Crud.cfxSubmit("spform", "doEdit", rowguid);
        }
    }

    function doCancel(){
        SOP.Crud.cfxSubmit("mainForm","cancel");
    }

</script>

