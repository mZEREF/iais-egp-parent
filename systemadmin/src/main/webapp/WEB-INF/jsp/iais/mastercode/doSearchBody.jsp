<form method="post" id="MasterCodeFileForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <div class="row">
        <div class="col-xs-3 col-md-3">
            <div class="components">
                <a class="btn btn-primary" onclick="doCreate()">Create</a>
            </div>
        </div>
        <div class="col-xs-3 col-md-3">
            <div class="components">
                <a class="btn btn-primary" href="${pageContext.request.contextPath}/master-code-file">Download</a>
            </div>
        </div>
        <div class="col-xs-3 col-md-3">
            <div class="file-upload-gp">
                <input id="selectedFile" name="selectedFile" type="file" style="display: none;"
                       aria-label="selectedFile1">
                <a class="btn btn-file-upload btn-secondary" href="#">Upload</a>
            </div>
        </div>
        <div class="filter-box form-horizontal">
            <div class="form-group">
                <iais:value>
                    <%String codeKey = request.getParameter("codeCategory")==null?"":request.getParameter("codeCategory");%>
                    <label class="col-xs-12 col-md-4 control-label" for="codeCategory">Code Category.</label>
                    <div class="col-xs-8 col-sm-6 col-md-5">
                        <input id="codeCategory" type="text" name="codeCategory" value="<%=codeKey%>" maxlength="25">
                    </div>
                </iais:value>
            </div>
            <div class="form-group">
                <iais:value>
                    <%String codeDescription = request.getParameter("codeDescription")==null?"":request.getParameter("codeDescription");%>
                    <label class="col-xs-12 col-md-4 control-label">Code Description.</label>
                    <div class="col-xs-8 col-sm-6 col-md-5">
                        <input id="codeDescription" type="text" name="codeDescription" value="<%=codeDescription%>" maxlength="255">
                    </div>
                </iais:value>
            </div>
            <div class="form-group">
                <iais:value>
                    <%String esd = request.getParameter("esd")==null?"":request.getParameter("esd");%>
                    <label class="col-xs-12 col-md-4 control-label" for="esd">Effective Start Date.</label>
                    <div class="col-xs-8 col-sm-6 col-md-5">
                        <iais:datePicker id="esd" name="esd" value="<%=esd%>"/>
                    </div>
                </iais:value>
            </div>
            <div class="form-group">
                <iais:value>
                    <%String eed = request.getParameter("eed")==null?"":request.getParameter("eed");%>
                    <label class="col-xs-12 col-md-4 control-label" for="eed">Effective End Date.</label>
                    <div class="col-xs-8 col-sm-6 col-md-5">
                        <iais:datePicker id="eed" name="eed" value="<%=eed%>"/>
                    </div>
                </iais:value>
            </div>
            <div class="form-group">
                <iais:value>
                    <%String codeStatus = request.getParameter("codeStatus")==null?"":request.getParameter("codeStatus");%>
                    <label class="col-xs-12 col-md-4 control-label" for="codeStatus">Status.</label>
                    <div class="col-xs-8 col-sm-6 col-md-5">
                        <iais:select name="codeStatus" id="codeStatus" options="codeStatus" value="<%=codeStatus%>"></iais:select>
                    </div>
                </iais:value>
            </div>
            <div class="form-group">
                <div class="row">
                    <div class="col-xs-12 col-md-12">
                        <div class="text-right"><a class="btn btn-primary" id="MC_Search">Search</a></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <iais:pagination param="MasterCodeSearchParam" result="MasterCodeSearchResult"/>
</form>
