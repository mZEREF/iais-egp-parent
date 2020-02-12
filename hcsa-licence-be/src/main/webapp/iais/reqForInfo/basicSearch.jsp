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
    <input type="hidden" name="crud_action_type" value=""/>
    <input type="hidden" name="crud_action_value" value=""/>
    <input type="hidden" name="crud_action_additional" value=""/>
    <iais:body >
        <div class="container">
            <div class="col-xs-12">
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <br><br><br><br>
                    <h2>Basic Search Criteria</h2>
                    <div class="panel panel-default">
                        <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                            <div class="panel-body">
                                <div class="panel-main-content">
                                    <iais:section title="" id = "supPoolList">
                                        <iais:row>
                                            <iais:field value="search no"/>
                                            <iais:value width="18">
                                                <label>
                                                    <input type="text" name="search_no"  />
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:value width="18">
                                                <input type="radio" name="select_search" value="application" checked />Application No
                                            </iais:value>
                                            <iais:value width="18">
                                                <input type="radio" name="select_search" value="licence" />Licence No
                                            </iais:value>
                                        </iais:row>
                                        <iais:action style="text-align:center;">
                                            <button type="button" class="btn btn-lg btn-login-submit" type="button"
                                                    style="background:#2199E8; color: white" onclick="javascript:doSearch();">Search</button>
                                            <button type="button" class="btn btn-lg btn-login-submit" type="button"
                                                    style="background:#2199E8; color: white" onclick="javascript:doAdvancedSearch();">Advanced</button>
                                            <button type="button" class="btn btn-lg btn-login-clear" type="button"
                                                    style="background:#2199E8; color: white" onclick="javascript:doClear();">Clear</button>
                                        </iais:action>
                                    </iais:section>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </iais:body>
</form>
<script type="text/javascript">


    function doClear(){
        $('input[name="search_no"]').val("");
    }

    function doAdvancedSearch(){
        var radios=document.getElementsByName("select_search");
        for (var i = 0, length = radios.length; i < length; i++) {
            if (radios[i].checked) {
                SOP.Crud.cfxSubmit("mainForm", radios[i].value);
                break;
            }
        }
    }
    function doSearch(){
        var radios=document.getElementsByName("select_search");
        for (var i = 0, length = radios.length; i < length; i++) {
            if (radios[i].checked) {
                SOP.Crud.cfxSubmit("mainForm", radios[i].value+"Adv");
                break;
            }
        }
    }

    function sortRecords(sortFieldName,sortType){
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        submit('licSort');
    }
</script>