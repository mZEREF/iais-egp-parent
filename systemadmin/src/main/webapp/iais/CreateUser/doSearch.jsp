<%--
  Created by IntelliJ IDEA.
  User: ecquaria
  Date: 2019/12/24
  Time: 18:59
  To change this template use File | Settings | File Templates.
--%>
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
    <div id="advfilter" class="collapse">
        <div class="filter-box">
            <h3>Master Code Search</h3>
            <div class="form-group">
                <iais:value>
                    <label class="col-xs-12 col-md-4 control-label" for="userId">User ID.</label>
                    <div class="col-xs-8 col-sm-6 col-md-5">
                        <input id="userId" name="userId" type="text">
                    </div>
                </iais:value>
            </div>
            <div class="form-group">
                <iais:value>
                    <label class="col-xs-12 col-md-4 control-label" for="emailAddress">Email address.</label>
                    <div class="col-xs-8 col-sm-6 col-md-5">
                        <input id="emailAddress" name="emailAddress" type="text">
                    </div>
                </iais:value>
            </div>
            <div class="form-group">
                <iais:value>
                    <label class="col-xs-12 col-md-4 control-label" for="displayName">Display Name.</label>
                    <div class="col-xs-8 col-sm-6 col-md-5">
                        <input id="displayName" type="text" name="displayName">
                    </div>
                </iais:value>
            </div>
            <div class="form-group">
                <iais:value>
                    <label class="col-xs-12 col-md-4 control-label" for="accountStatus">Account Status.</label>
                    <div class="col-xs-8 col-sm-6 col-md-5">
                        <input id="accountStatus" type="text" name="accountStatus">
                    </div>
                </iais:value>
            </div>
            <div class="form-group">
                <iais:value>
                    <label class="col-xs-12 col-md-4 control-label" for="roleAssigned">Roles Assigned.</label>
                    <div class="col-xs-8 col-sm-6 col-md-5">
                        <input id="roleAssigned" type="text" name="roleAssigned">
                    </div>
                </iais:value>
            </div>

            <div class="form-group">
                <iais:value>
                    <label class="col-xs-12 col-md-4 control-label" for="privilegeAssigned">Privileges Assigned.</label>
                    <div class="col-xs-8 col-sm-6 col-md-5">
                        <input id="privilegeAssigned" type="text" name="privilegeAssigned">
                    </div>
                </iais:value>
            </div>

            <div class="application-tab-footer">
                <div class="row">
                    <div class="col-xs-12 col-md-12">
                        <div class="text-right"><a class="btn btn-primary" id="IU_Search">Search</a></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

