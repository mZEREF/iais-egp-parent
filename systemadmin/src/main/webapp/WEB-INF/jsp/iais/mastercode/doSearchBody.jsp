<form method="post" id="MasterCodeFileForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <div class="row clearMC">
        <div class="filter-box form-horizontal">
            <div class="form-group">
                <iais:value>
                    <%String codeKey = request.getParameter("codeCategory")==null?"":request.getParameter("codeCategory");%>
                    <label class="col-xs-4 col-md-4 control-label" for="codeCategory">Master Code Category</label>
                    <div class="col-xs-6 col-sm-6 col-md-6">
                        <iais:select name="codeCategory" options="allCodeCategory"
                                     value="<%=codeKey%>"/>
                    </div>
                </iais:value>
            </div>
            <div class="form-group">
                <iais:value>
                    <%String codeValue = request.getParameter("codeValue")==null?"":request.getParameter("codeValue");%>
                    <label class="col-xs-4 col-md-4 control-label">Code Value</label>
                    <div class="col-xs-6 col-sm-6 col-md-6">
                        <input id="codeValue" type="text" name="codeValue" value="<%=codeValue%>" maxlength="25">
                    </div>
                </iais:value>
            </div>
            <div class="form-group">
                <iais:value>
                    <%String codeDescription = request.getParameter("codeDescription")==null?"":request.getParameter("codeDescription");%>
                    <label class="col-xs-4 col-md-4 control-label">Code Description</label>
                    <div class="col-xs-6 col-sm-6 col-md-6">
                        <form><textarea cols="86" rows="6" id="description" name="codeDescription"
                                        maxlength="255"><%=codeDescription%></textarea></form>
                    </div>
                </iais:value>
            </div>
            <div class="form-group">
                <iais:value>
                    <%String filterValue = request.getParameter("filterValue")==null?"":request.getParameter("filterValue");%>
                    <label class="col-xs-4 col-md-4 control-label">Filter Value</label>
                    <div class="col-xs-6 col-sm-6 col-md-6">
                        <input id="filterValue" type="text" name="filterValue" value="<%=filterValue%>" maxlength="50">
                    </div>
                </iais:value>
            </div>
            <div class="form-group">
                <iais:value>
                    <%String codeStatus = request.getParameter("codeStatus")==null?"":request.getParameter("codeStatus");%>
                    <label class="col-xs-4 col-md-4 control-label" for="codeStatus">Status</label>
                    <div class="col-xs-6 col-sm-6 col-md-6">
                        <iais:select name="codeStatus" id="codeStatus" options="codeStatus" value="<%=codeStatus%>"/>
                    </div>
                </iais:value>
            </div>
            <div class="form-group">
                <iais:value>
                    <%String esd = request.getParameter("esd")==null?"":request.getParameter("esd");%>
                    <label class="col-xs-4 col-md-4 control-label" for="esd">Effective Start Date</label>
                    <div class="col-xs-6 col-sm-6 col-md-6">
                        <iais:datePicker id="esd" name="esd" value="<%=esd%>"/>
                    </div>
                </iais:value>
            </div>
            <div class="form-group">
                <iais:value>
                    <%String eed = request.getParameter("eed")==null?"":request.getParameter("eed");%>
                    <label class="col-xs-4 col-md-4 control-label" for="eed">Effective End Date</label>
                    <div class="col-xs-6 col-sm-6 col-md-6">
                        <iais:datePicker id="eed" name="eed" value="<%=eed%>"/>
                    </div>
                </iais:value>
            </div>
            <div class="form-group">
                <iais:value>
                    <label class="col-xs-4 col-md-4 control-label"></label>
                    <div class="col-xs-6 col-sm-6 col-md-6">
                        <span class="error-msg" style="width: 150%;position: absolute;">${ERR_EED}</span>
                    </div>
                </iais:value>
            </div>

            <div class="form-group">
                <div class="row">
                    <div class="col-xs-12 col-md-12">
                        <div class="text-right">
                            <a class="btn btn-secondary" id="MC_Clear">Clear</a>
                            <a class="btn btn-primary" id="MC_Search">Search</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <iais:pagination param="MasterCodeSearchParam" result="MasterCodeSearchResult"/>
</form>
