<h4>
    <br>
    <br>
    <strong>
        ${(empty inter_user_attr.displayName ||  inter_user_attr.displayName == '-') ? 'Licensee' : inter_user_attr.displayName}
    </strong>(${inter_user_attr.identityNo})
</h4>
<br>
<br>
<div class="form-group">
    <div class="col-xs-12 col-md-10" >
      <ul>   <a style="float: right" id="reLoadMyInfo" onclick="javascript:reLoadMyInfoTodo()"> <img src="<%=webroot1%>img/MyinfoBtn.png" alt="Ministry of Health" width="235" height="64"></a>
       <li>
        You may retrieve your address and contact information from your <strong>MyInfo profile</strong>.
       </li>
      <li>
        To update your MyInfo profile, please visit myinfo.gov.sg.
    </li>
    </ul>
    </div>
</div>
<br>
