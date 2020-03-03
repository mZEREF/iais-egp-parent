<iais:section title="" id="demoList">
    <iais:row>
        <iais:field value="User ID:"/>
        <iais:value width="18">
            <input id="userId" name="userId" type="text" value="${userId}" maxlength="20">
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field value="Email address:"/>
        <iais:value width="18">
            <input id="emailAddress" name="emailAddress" type="text" value="${email}">
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field value="Display Name:"/>
        <iais:value width="18">
            <input id="displayName" type="text" name="displayName" value="${displayName}">
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field value="Account Status:"/>
        <iais:value width="18">
            <iais:select name="accountStatus" options="statusOption" firstOption="Please Select"
                         value="${status}"></iais:select>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field value="Roles Assigned:"/>
        <iais:value width="18">
            <input id="roleAssigned" type="text" name="roleAssigned">
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field value="Privileges Assigned:"/>
        <iais:value width="18">
            <input id="privilegeAssigned" type="text" name="privilegeAssigned">
        </iais:value>
    </iais:row>
    <iais:action style="text-align:center;">
        <button class="btn btn-lg btn-login-search" type="button" id="IU_Search"
                style="background:#2199E8; color: white ">Search
        </button>
        <button class="btn btn-lg btn-login-clear" id="IU_Clear" type="button" style="background:#2199E8; color: white">
            Clear
        </button>
    </iais:action>
</iais:section>
<br/><br/>
<div class="col-xs-3">
    <div class="components">
        <a class="btn btn-primary" onclick="doCreate()">Create</a>
    </div>
</div>
<div class="col-xs-3">
    <div class="components">
        <a class="btn btn-primary" onclick="doStatus()">Change Status</a>
    </div>
</div>
<div class="col-xs-3">
    <div class="components">
        <a class="btn btn-primary" onclick="doExport()">Export</a>
    </div>
</div>
<div class="col-xs-3">
    <div class="components">
        <input type="file" id="inputFile" name="xmlFile" style="display:none" onchange="javascirpt:doImport();"/>
        <input type="button" class="btn btn-primary" onclick="document.getElementById('inputFile').click()"
               value="Import"/>
    </div>
</div>

