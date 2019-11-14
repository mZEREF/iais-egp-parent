<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%
    /*
      You can customize this default file:
      /D:/Users/ecquaria/Desktop/eclipse/plugins/com.ecquaria.eclipse.sit_6.1.1/WebPage.jsp.default
    */

//handle to the Engine APIs
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

<form method="post" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <h1>This is Bank!!!</h1>
    <a href="https://egp.sit.inter.iais.com/payment/eservice/INTERNET/Payment?amount=1&reqRefNo=5252&invoiceNo=888">ToPayment</a>
</form>
