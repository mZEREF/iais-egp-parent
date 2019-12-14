<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
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
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <iais:section title="" id = "supPoolList">
                        <div class="bg-title">
                            <h2>Basic Search Criteria</h2>
                        </div>
                        <iais:row>
                            <iais:value width="18">
                                <label>
                                    <input type="text" name="search_no"  />
                                </label>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:value width="18">
                                <input type="radio" name="select_search" value="application" />Application No
                            </iais:value>
                            <iais:value width="18">
                                <input type="radio" name="select_search" value="licence" />Licence No
                            </iais:value>
                        </iais:row>

                        <iais:action style="text-align:center;">
                            <button type="button" class="search btn" onclick="javascript:doSearch();">Advanced Search</button>
                            <button type="button" class="search btn" onclick="javascript:doClear();">Clear</button>
                        </iais:action>
                    </iais:section>

                </div>
            </div>
        </div>
    </div>
</form>
<script type="text/javascript">


    function doClear(){
    }

    function doSearch(){
        var radios=document.getElementsByName("select_search");
        for (var i = 0, length = radios.length; i < length; i++) {
            if (radios[i].checked) {
                SOP.Crud.cfxSubmit("mainForm", radios[i].value);
                break;
            }
        }
    }
</script>