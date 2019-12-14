<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <div class="main-content">
        <div id="base" class="">

            <!-- Unnamed (Rectangle) -->
            <div id="u2637" class="ax_default label">
                <div id="u2637_div" class=""></div>
                <div id="u2637_text" class="text ">
                    <p><span>&nbsp; Basic Search Criteria</span></p>
                </div>
            </div>

            <!-- Unnamed (Text Field) -->
            <div id="u2638" class="ax_default text_field">
                <input id="u2638_input" type="text" value=""/>
            </div>



            <!-- Unnamed (Radio Button) -->
            <div id="u2641" class="ax_default radio_button">
                <label for="u2641_input" style="position: absolute; left: 0px;">
                    <div id="u2641_text" class="text ">
                        <p><span>Application No</span></p>
                    </div>
                </label>
                <input id="u2641_input" type="radio" value="radio" name="u2641"/>
            </div>

            <!-- Unnamed (Radio Button) -->
            <div id="u2642" class="ax_default radio_button">
                <label for="u2642_input" style="position: absolute; left: 0px;">
                    <div id="u2642_text" class="text ">
                        <p><span>Licence No</span></p>
                    </div>
                </label>
                <input id="u2642_input" type="radio" value="radio" name="u2642"/>
            </div>


            <!-- Unnamed (Rectangle) -->
            <div id="u2644" class="ax_default primary_button">
                <div id="u2644_div" class=""></div>
                <div id="u2644_text" class="text ">
                    <p><span>Clear</span></p>
                </div>
            </div>

            <!-- Unnamed (Rectangle) -->
            <div id="u2645" class="ax_default primary_button">
                <div id="u2645_div" class=""></div>
                <div id="u2645_text" class="text ">
                    <p><span>Advanced Search</span></p>
                </div>
            </div>

        </div>
    </div>
</form>
