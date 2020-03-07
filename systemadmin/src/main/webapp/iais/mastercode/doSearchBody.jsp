<form method="post" id="MasterCodeFileForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
</form>
<div class="row">
    <div class="col-xs-2 col-md-2">
        <div class="components">
            <a class="btn btn-secondary" data-toggle="collapse" data-target="#advfilter">Filter</a>
        </div>
    </div>
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
            <input id="selectedFile" name="selectedFile" type="file" style="display: none;" aria-label="selectedFile1">
            <a class="btn btn-file-upload btn-secondary" href="#">Upload</a>
        </div>
    </div>
    <div id="advfilter" class="collapse">
        <div class="filter-box">
            <h3>Master Code Search</h3>
            <div class="form-group">
                <iais:value>
                    <label class="col-xs-12 col-md-4 control-label" for="codeCategory">Code Category.</label>
                    <div class="col-xs-8 col-sm-6 col-md-5">
                        <input id="codeCategory" type="text" name="codeCategory" maxlength="25">
                    </div>
                </iais:value>
            </div>
            <div class="form-group">
                <iais:value>
                    <label class="col-xs-12 col-md-4 control-label">Code Description.</label>
                    <div class="col-xs-8 col-sm-6 col-md-5">
                        <input id="codeDescription" type="text" name="codeDescription" maxlength="255">
                    </div>
                </iais:value>
            </div>
            <div class="form-group">
                <iais:value>
                    <label class="col-xs-12 col-md-4 control-label" for="esd">Effective Start Date.</label>
                    <div class="col-xs-8 col-sm-6 col-md-5">
                        <iais:datePicker id="esd" name="esd"/>
                    </div>
                </iais:value>
            </div>
            <div class="form-group">
                <iais:value>
                    <label class="col-xs-12 col-md-4 control-label" for="eed">Effective End Date.</label>
                    <div class="col-xs-8 col-sm-6 col-md-5">
                        <iais:datePicker id="eed" name="eed"/>
                    </div>
                </iais:value>
            </div>
            <div class="form-group">
                <iais:value>
                    <label class="col-xs-12 col-md-4 control-label" for="status">Status.</label>
                    <div class="col-xs-8 col-sm-6 col-md-5">
                        <input id="status" type="text" name="codeStatus">
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

</div>
