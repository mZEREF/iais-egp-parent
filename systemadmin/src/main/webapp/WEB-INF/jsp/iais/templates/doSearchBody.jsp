<style>
    .dropdown-menu-right{
        width: 100% !important;
    }
</style>
<div class="row">
    <div class="filter-box form-horizontal clearTep">
        <div class="form-group">
            <iais:value>
                <label class="col-xs-4 col-md-4 control-label" for="msgType">Process</label>
                <div class="col-xs-6 col-sm-9 col-md-6">
                    <%String tepProcess = request.getParameter("tepProcess");%>
                    <iais:select name="tepProcess" id="tepProcess" options="tepProcess" value="<%=tepProcess%>" firstOption="Please Select"/>
                </div>
            </iais:value>
        </div>
        <div class="form-group">
            <iais:value>
                <label class="col-xs-4 col-md-4 control-label" for="msgType">Message Type</label>
                <div class="col-xs-6 col-sm-6 col-md-6">
                    <%String msgType = request.getParameter("msgType");%>
                    <iais:select name="msgType" id="msgType" options="msgType" value="<%=msgType%>" firstOption="Please Select"/>
                </div>
            </iais:value>
        </div>
        <div class="form-group">
            <iais:value>
                <label class="col-xs-4 col-md-4 control-label" for="templateName">Template Name</label>
                <div class="col-xs-6 col-sm-6 col-md-6">
                    <form><input id="templateName" type="text" name="templateName"
                                 maxlength="500" value="${MsgTemplateSearchParam.getFilters().get("templateName").replaceAll("\"","&quot;").replaceAll("%","")}"/>
                        <div class="input-group-btn">
                            <ul class="dropdown-menu dropdown-menu-right" role="menu"></ul>
                        </div>
                    </form>
                </div>
            </iais:value>
        </div>
        <div class="form-group" id="deliveryMode">
            <iais:value>
                <label class="col-xs-4 col-md-4 control-label" for="deliveryMode">Delivery Mode</label>
                <div class="col-xs-6 col-sm-6 col-md-6">
                    <%String deliveryMode = request.getParameter("deliveryMode");%>
                    <iais:select name="deliveryMode" id="deliveryMode" options="deliveryMode"  value="<%=deliveryMode%>"  firstOption="Please Select"/>
                </div>
            </iais:value>
        </div>
        <div class="form-group">
            <iais:value>
                <label class="col-xs-4 col-md-4 control-label" for="esd">Effective Start Date</label>
                <div class="col-xs-6 col-sm-6 col-md-6">
                    <%String esd = request.getParameter("esd");%>
                    <iais:datePicker id="esd" name="esd" value="<%=esd%>"/>
                </div>
            </iais:value>
        </div>
        <div class="form-group">
            <iais:value>
                <label class="col-xs-4 col-md-4 control-label"></label>
                <div class="col-xs-6 col-sm-6 col-md-6">
                    <span class="error-msg" style="width: 150%;position: absolute;">${TDEM}</span>
                </div>
            </iais:value>
        </div>
        <div class="form-group">
            <iais:value>
                <label class="col-xs-4 col-md-4 control-label" for="eed">Effective End Date</label>
                <div class="col-xs-6 col-sm-6 col-md-6">
                    <%String eed = request.getParameter("eed");%>
                    <iais:datePicker id="eed" name="eed" value="<%=eed%>"/>
                </div>
            </iais:value>

        </div>
        <div class="row">
            <div class="col-xs-12 col-md-12">
                <div class="text-right">
                    <a class="btn btn-secondary" id="ANT_Clearn">Clear</a>
                    <a class="btn btn-primary" id="ANT_Search">Search</a>
                </div>
            </div>
        </div>
    </div>
</div>