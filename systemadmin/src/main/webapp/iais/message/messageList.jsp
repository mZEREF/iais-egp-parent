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
<iais:body>
    <h2>Message List Page</h2>

        <iais:section title="Message List" id="msgList">
        <iais:row>
            <iais:field value="Domain Type"></iais:field>
            <iais:value width="7">
                <input type="checkbox" name="domainType" value="internet">Internet</input>
                <input type="checkbox" name="domainType" value="intranet">Intranet</input>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field value="Msg Type"></iais:field>

        </iais:row>

        <iais:row>
            <iais:field value="Module"></iais:field>
        </iais:row>

        <iais:row>
            <iais:field value="Description"></iais:field>
        </iais:row>

        <iais:row>
            <iais:field value="Status"></iais:field>
        </iais:row>
    </iais:section>
</iais:body>
