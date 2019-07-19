<%@ page import="com.ecquaria.cloud.moh.iais.test.entity.OrgUserAccount" %>
<%@ page import="java.util.List" %>
<%@ page import="com.ecquaria.cloud.moh.iais.test.entity.Organization" %>

<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>

<%

    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
    List<OrgUserAccount> lists =  request.getAttribute("lists")==null?null:(List<OrgUserAccount>) request.getAttribute("lists");


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
<!-- START: CSS -->
<link href="/sop/_statics/css/jquery-ui/smoothness/jquery-ui-custom.css" rel="stylesheet" type="text/css" media="all" />
<link href="/sop/_statics/css/core/core.css" rel="stylesheet" type="text/css" media="all" />
<link href="/sop/_themes/sop6/css/template.css" rel="stylesheet" type="text/css" media="all" />
<link href="/sop/_themes/sop6/css/custom.css" rel="stylesheet" type="text/css" media="all" />
<link href="/sop/_themes/sop6/css/jquery.treeview.css" rel="stylesheet" type="text/css" media="all" />
<link href="/sop/_themes/sop6/css/jquery.autocomplete.css" rel="stylesheet" type="text/css" media="all" />
<!-- END: CSS -->
<h1>Customer Listing</h1>
<div class="onecolumn">
    <div class="header">
        <div class="switch">
            <div class="search-bg">
                <div class="search-icon">
                    <img title="search" alt="search" src="/sop/_themes/sop6/images/general/search.png">
                </div>
                <button name="search-panel-control-1" id="search-panel-control-1" type="button" class="search-button">Search</button>
            </div>
        </div>
        <script type="text/javascript">
            SOP.Common.load(function(){SOP.Common.toggleSearchPanel('search-panel-control-1', 'search-container','searchPanelStatus');});</script>
    </div>
<form method="post" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <div class="search-container"/>
        <input type="hidden" name="searchPanelStatus" value="true">
        <br class='clear'/>
        <div class="search-label"/>
            <label>Customer Id:</label>
        </div>
        <div class="search-input">
            <input type="hidden" name="crud.filters" value="@@78ab0d4e:::customerId_____901">
            <input type="text" name="customerId_____901_number" value="">
        </div>
        <br class='clear'/>
        <div class="action-buttons-panel">
            <button onclick="SOP.Crud.cfxSubmit('mainForm', 'search')" type="button">Search</button>
            <button onclick="SOP.Common.clearSearchForm(this)" type="button">Clear</button>
        </div>
        <br class='clear'/>
        <br class='clear'/>
    </div>
    <div class="content">
        <div class="pagination-wrapper">
            <div class="pag-lft-element">
                <select id="crud.recordsPerPage" name="@@78ab0d4e:::crud.recordsPerPage" onchange="javascript:SOP.Crud.cfxSubmit('mainForm', 'changePage', '1', '@@75972815', 'mainForm');">
                    <optgroup label="Display Per Page"><option value="10" selected >10</option>
                        <option value="20">20</option>
                        <option value="50">50</option>
                        <option value="100">100</option>
                    </optgroup>
                </select>
            </div>
            <div class="pag-rgh-total-item">Page 1 of 1 Displaying 1 - 1 of 1</div>
            <div class="pagination">
                <a class="disabled" href="javascript:void(0);">First</a>&nbsp;&nbsp;
                <a class="disabled" href="javascript:void(0);">Previous</a>&nbsp;&nbsp;
                <a class="disabled" href="javascript:void(0);">1</a>&nbsp;&nbsp;
                <a class="disabled" href="javascript:void(0);">Next</a>&nbsp;&nbsp;
                <a class="disabled" href="javascript:void(0);">Last</a>
            </div>
        </div>
        <div class="blank"></div>
        <table class="table2">
            <script type="text/javascript">
                sopCrud1= new SOP.Crud.SOPCrud('Customer', "");
                sopCrud1.entityUidsVar = 'customerUids';sopCrud1.allEntityUidsVar = 'allEntityUids1';
                sopCrud1.allEntitiesCbVar = 'allEntitiesCb1';
            </script>
            <thead>
                <tr>
                    <th width="1%">
                        <input type="checkbox"  id="allEntitiesCb1" name="allEntitiesCb1" onclick="sopCrud1.setAllCheckBox(this.checked)"/>
                    </th>
                    <th width="1%">
                        No.
                    </th>
                    <th>
                        <span class="column-sort">
                            <a class="sort-up " title="Sort up" href="javascript:SOP.Crud.cfxSubmit('mainForm', 'sortRecords', 'id', 'ascending')" >
                            </a>
                            <a class="sort-down " title="Sort down" href="javascript:SOP.Crud.cfxSubmit('mainForm', 'sortRecords', 'id', 'descending')" >
                            </a>
                        </span>
                        <div class="sort-label">Account Id</div>
                    </th>
                    <th>
                    <span class="column-sort">
                        <a class="sort-up " title="Sort up" href="javascript:SOP.Crud.cfxSubmit('mainForm', 'sortRecords', 'customerName', 'ascending')" >
                        </a>
                        <a class="sort-down " title="Sort down" href="javascript:SOP.Crud.cfxSubmit('mainForm', 'sortRecords', 'customerName', 'descending')" >
                        </a>
                    </span>
                    <div class="sort-label">Name</div>
                    </th>
                    <th>
                        <span class="column-sort">
                            <a class="sort-up " title="Sort up" href="javascript:SOP.Crud.cfxSubmit('mainForm', 'sortRecords', 'customerEmail', 'ascending')" >
                            </a>
                            <a class="sort-down " title="Sort down" href="javascript:SOP.Crud.cfxSubmit('mainForm', 'sortRecords', 'customerEmail', 'descending')" ></a>
                        </span>
                        <div class="sort-label">Nirc No</div>
                    </th>
                    <th>
                        <span class="column-sort">
                            <a class="sort-up " title="Sort up" href="javascript:SOP.Crud.cfxSubmit('mainForm', 'sortRecords', 'customerEmail', 'ascending')" >
                            </a>
                            <a class="sort-down " title="Sort down" href="javascript:SOP.Crud.cfxSubmit('mainForm', 'sortRecords', 'customerEmail', 'descending')" ></a>
                        </span>
                        <div class="sort-label">UEN No</div>
                    </th>
                    <th>
                        <span class="column-sort">
                            <a class="sort-up " title="Sort up" href="javascript:SOP.Crud.cfxSubmit('mainForm', 'sortRecords', 'orderLimit', 'ascending')" ></a>
                            <a class="sort-down " title="Sort down" href="javascript:SOP.Crud.cfxSubmit('mainForm', 'sortRecords', 'orderLimit', 'descending')" ></a>
                        </span>
                        <div class="sort-label">Corp Pass Id
                        </div>
                    </th>
                    <th>
                        <span class="column-sort">
                            <a class="sort-up " title="Sort up" href="javascript:SOP.Crud.cfxSubmit('mainForm', 'sortRecords', 'createDate', 'ascending')" ></a>
                            <a class="sort-down " title="Sort down" href="javascript:SOP.Crud.cfxSubmit('mainForm', 'sortRecords', 'createDate', 'descending')" ></a>
                        </span>
                        <div class="sort-label">Status</div>
                    </th>
                    <th>Actions
                    </th>
                </tr>
            </thead>
            <tbody>


                <%
                if(lists==null ||lists.size() ==0 ){
                 %>
                <tr class="odd-row">
                  <td colspan="9"> No Record</td>
                </tr>
                <%
                }else{

                for(int i = 0;i<lists.size();i++){
                    OrgUserAccount orgUserAccount = lists.get(i);
                   int id =  orgUserAccount.getId();
                   String name = orgUserAccount.getName();
                   String nircNo = orgUserAccount.getNircNo();
                   String corpPassId = orgUserAccount.getCorpPassId();
                   String status = orgUserAccount.getStatus();
                   Organization organization = orgUserAccount.getOrganization();
                   String uneNo = organization.getUenNo();
                %>
                <tr class="odd-row">
                <td><input type="checkbox" onclick="sopCrud1.updateAllCheckBox()" value="1" name="customerUids" id="entityUids_1"></td>
                <td><%=i+1%></td>
                <td><%=id%></td>
                <td><%=name%></td>
                <td><%=nircNo%></td>
                 <td><%=uneNo%></td>
                <td><%=corpPassId%></td>
                <td><%=status%></td>
                <td>
                    <a onclick="javascript:SOP.Crud.cfxSubmit('mainForm','view','1');" class="img-action">
                        <img class="img-action-view" src="/sop/_statics/images/empty.gif" title="View">
                    </a>
                    <script type="text/javascript">$(function(){SOP.Common.setupTooltip($('.img-action>img'),{position: "center right"});});</script>
                    <a onclick="javascript:SOP.Crud.cfxSubmit('mainForm','edit','1');" class="img-action"><img class="img-action-edit" src="/sop/_statics/images/empty.gif" title="Edit">
                    </a>
                </td>
                </tr>
                <%
                }
                }
                %>


            </tbody>
        </table>
        <div class="blank"></div>
        <div class="pagination-wrapper">
            <div class="pag-lft-element">
                <select id="crud.recordsPerPage" name="@@78ab0d4e:::crud.recordsPerPage" onchange="javascript:SOP.Crud.cfxSubmit('mainForm', 'changePage', '1', '@@75972815', 'mainForm');">
                    <optgroup label="Display Per Page"><option value="10" selected >10</option>
                        <option value="20">20</option>
                        <option value="50">50</option>
                        <option value="100">100</option>
                    </optgroup>
                </select>
            </div>
            <div class="pag-rgh-total-item">Page 1 of 1 Displaying 1 - 1 of 1</div>
            <div class="pagination">
                <a class="disabled" href="javascript:void(0);">First</a>&nbsp;&nbsp;
                <a class="disabled" href="javascript:void(0);">Previous</a>&nbsp;&nbsp;
                <a class="disabled" href="javascript:void(0);">1</a>&nbsp;&nbsp;
                <a class="disabled" href="javascript:void(0);">Next</a>&nbsp;&nbsp;
                <a class="disabled" href="javascript:void(0);">Last</a>
            </div>
        </div>
        <div class="blank">
        </div>
    </div>
    <div class="action-buttons-panel">
        <button type="button" onclick="javascript:SOP.Crud.cfxSubmit(&#39;mainForm&#39;, &#39;create&#39;)">Create</button>
        <button type="button" onclick="javascript:deleteEntities();">Delete</button>
    </div>
        <br class='clear'/>
    </div>
</form>
</div>

<script type="text/javascript">
    function deleteEntities(){
        if(confirm('Are you sure you want to delete?')){
            SOP.Crud.cfxSubmit('mainForm', 'delete', '');
        }
    }

    $('#mainForm').keypress(
        function(event){
            submitOnKeyPress(event);
        }

    );

    function submitOnKeyPress(e) {

        if (e.keyCode == 13){ //when hit enter
            SOP.Crud.cfxSubmit('mainForm', 'search');
        }

    }
</script>