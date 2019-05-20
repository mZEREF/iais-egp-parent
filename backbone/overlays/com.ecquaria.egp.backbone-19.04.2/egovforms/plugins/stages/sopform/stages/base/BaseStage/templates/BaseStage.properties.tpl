<div class="edit-page-property">
	<div class="page-property-header-bg">
        <span class="property-header-icon"></span>                   
        <h5 class="page-property-header">Flow Properties</h5>
           	<ul>
           		<li class="edit-page-li single-properties-close"><a href="#"><span class="properties-close">close</span></a></li>
            </ul>
     </div>
	<div class="edit-page-content">
		<div class="page-content-section">
		{for i in stageDef.stageProperties}
			{if i.special == 'true'}
					{if i.type =='separator'}
			     		<hr class="separator"/>
					{/if}
					{if i.type == 'taskAssignment'}
						<div class="properties---taskAssignmentType">
			     			<label class="page-edit-label">Task Type</label>
			     			<select name="taskAssignmentType" class="toolboxLabel">
								<option value="direct">Direct</option>
								<option value="roundrobin">Round Robin</option>
								<option value="pickup">Pickup</option>
							</select>
			     		</div>
			     		<div class="page-content-spacer"></div>
			     		
			     		<div class="properties---taskAssignmentUserDomain">
			     			<label class="page-edit-label">User Domain</label>
			     			<select name="taskAssignmentUserDomain" class="toolboxLabel">
							</select>
			     		</div>
			     		<div class="page-content-spacer"></div>
			     		
			     		<div class="properties---taskAssignmentUserId">
			     			<label class="page-edit-label">User Id</label>
			     			<input type="text" name="taskAssignmentUserId" class="toolboxLabel"/>
			     		</div>
			     		<div class="page-content-spacer"></div>
			     		
			     		<div class="properties---taskAssignmentRoleId">
			     			<label class="page-edit-label">Role Id</label>
			     			<select name="taskAssignmentRoleId" class="toolboxLabel">
							</select>
			     		</div>
			     	{/if}
			     	
			     	{if i.type == 'processSelection'}	
			     		<div class="properties---processSelectionProject">
			     			<label class="page-edit-label">Project</label>
			     			<select name="processSelectionProject" class="toolboxLabel">
							</select>
			     		</div>
			     		<div class="page-content-spacer"></div>
			     		
			     		<div class="properties---processSelectionProcess">
			     			<label class="page-edit-label">Process</label>
			     			<input type="text" name="processSelectionProcess" class="toolboxLabel"/>
			     		</div>
			     		<div class="page-content-spacer"></div>
			     		
			     		<div class="properties---processSelectionVersion">
			     			<label class="page-edit-label">Version</label>
			     			<input type="text" name="processSelectionVersion" class="toolboxLabel"/>
			     		</div>
			     		<div class="page-content-spacer"></div>
			     		
			     		<div class="properties---processSelectionComponent">
			     			<label class="page-edit-label">Component</label>
			     			<input type="text" name="processSelectionComponent" class="toolboxLabel"/>
			     		</div>
			     	{/if}
			     	
			     	{if i.type == 'processSelectionOPL'}
			     		<div class="properties---processSelectionOPLProject">
			     			<label class="page-edit-label">Project</label>
			     			<select name="processSelectionOPLProject" class="toolboxLabel">
							</select>
			     		</div>
			     		<div class="page-content-spacer"></div>
			     		
			     		<div class="properties---processSelectionOPLProcess">
			     			<label class="page-edit-label">Process</label>
			     			<input type="text" name="processSelectionOPLProcess" class="toolboxLabel"/>
			     		</div>
			     		<div class="page-content-spacer"></div>
			     		
			     		<div class="properties---processSelectionOPLVersion">
			     			<label class="page-edit-label">Version</label>
			     			<input type="text" name="processSelectionOPLVersion" class="toolboxLabel"/>
			     		</div>
			     		<div class="page-content-spacer"></div>
			     		
			     		<div class="properties---processSelectionOPLComponent">
			     			<label class="page-edit-label">Component</label>
			     			<input type="text" name="processSelectionOPLComponent" class="toolboxLabel"/>
			     		</div>
			     	{/if}
			     	
			     	{if i.type == 'processSelectionOPS'}
			     		<div class="properties---processSelectionOPSProject">
			     			<label class="page-edit-label">Project</label>
			     			<select name="processSelectionOPSProject" class="toolboxLabel">
							</select>
			     		</div>
			     		<div class="page-content-spacer"></div>
			     		
			     		<div class="properties---processSelectionOPSProcess">
			     			<label class="page-edit-label">Process</label>
			     			<input type="text" name="processSelectionOPSProcess" class="toolboxLabel"/>
			     		</div>
			     		<div class="page-content-spacer"></div>
			     		
			     		<div class="properties---processSelectionOPSVersion">
			     			<label class="page-edit-label">Version</label>
			     			<input type="text" name="processSelectionOPSVersion" class="toolboxLabel"/>
			     		</div>
			     		<div class="page-content-spacer"></div>
			     		
			     		<div class="properties---processSelectionOPSComponent">
			     			<label class="page-edit-label">Component</label>
			     			<input type="text" name="processSelectionOPSComponent" class="toolboxLabel"/>
			     		</div>
			     	{/if}
			     	{if i.type == 'processSelectionAltStage'}
			     		<div class="properties---processSelectionAltStageProject">
			     			<label class="page-edit-label">Project</label>
			     			<select name="processSelectionAltStageProject" class="toolboxLabel">
								</select>
			     		</div>
			     		<div class="page-content-spacer"></div>
			     		
			     		<div class="properties---processSelectionAltStageProcess">
			     			<label class="page-edit-label">Process</label>
			     			
							<input type="text" name="processSelectionAltStageProcess" class="toolboxLabel"/>
			     		</div>
			     		<div class="page-content-spacer"></div>
			     		
			     		<div class="properties---processSelectionAltStageVersion">
			     			<label class="page-edit-label">Version</label>
			     			<input type="text" name="processSelectionAltStageVersion" class="toolboxLabel"/>
			     		</div>
			     	{/if}
			     	{if i.type == 'customizedAckPage'}
			     		<div class="properties---customizedAckPageSelection">
			     			<label class="page-edit-label">Customize Ack Page</label>
			     			<select name="customizedAckPageSelection" class="toolboxLabel">
									<option value="">-</option>
									<option value="message">Message</option>
									<option value="url">URL</option>
								</select>
			     		</div>
			     		<div class="page-content-spacer"></div>
			     		
			     		<div class="properties---customizedAckPageMessage">
			     			<label class="page-edit-label">Ack Message</label>
			     			<input type="text" name="customizedAckPageMessage" class="toolboxLabel"/>
			     		</div>
			     		<div class="page-content-spacer"></div>
			     		
			     		<div class="properties---customizedAckPageURL">
			     			<label class="page-edit-label">Ack URL</label>
			     			<input type="text" name="customizedAckPageURL" class="toolboxLabel"/>
			     		</div>
			     	{/if}
			     	{if i.type == 'allowsDiscussion'}
			     		<div class="properties---allowsDiscussionSelection">
			     			<label class="page-edit-label">Allows Discussion</label>
			     			<input type="checkbox" name="allowsDiscussionSelection" class="toolboxLabel"/>
			     		</div>
			     		<div class="page-content-spacer"></div>
			     		
			     		<div class="properties---allowsDiscussions">
			     			<label class="page-edit-label"></label>
			     			<fieldset>
									<legend>Discussion Group</legend>
									<div id="allowsDiscussions_div" class="buttons">
										<table>
											<tr>
												<td>Role Id</td>
												<td><select name="allowDiscussionsRoleId" class="toolboxLabel"></select></td>
											</tr>
										</table>
									</div>
								</fieldset>
			     		</div>
			     	{/if}
			     	{if i.type == 'customizedButtons'}
			     		<div class="properties---customizedButtonsSelection">
			     			<label class="page-edit-label">Customize Buttons</label>
			     			<input type="checkbox" name="customizedButtonsSelection" class="toolboxLabel"/>
			     		</div>
			     		<div class="page-content-spacer"></div>
			     		
			     		<div class="properties---customizedButtons">
			     			<fieldset>
								<legend>Buttons</legend>
								<div id="customizedButtons_div" class="buttons">
									<table>
										<tr>
											<th class="toolboxLabel">Button Label</th>
											<th class="toolboxLabel">Return Value</th>
											<th class="toolboxLabel"></th>
										</tr>
										<tr class="customButton">
											<td><input size="10" type="text" name="customizedButton_label" class="toolboxLabel"/></td>
											<td><input size="10" type="text" name="customizedButton_value" class="toolboxLabel"/></td>
											<td>
												<button type="button" class="customizedButton-remove" style="padding-left: 0; padding-right: 0; padding-top: 0; height: 20px;">
													<img src="${sopFormTheme}images/general/icon-delete.png" alt=""/>
												</button>
											</td>
										</tr>
										<tr>
											<td colspan="3">
												<button type="button" class="customizedButton-add">
													<img src="${sopFormTheme}images/forms/add.png" alt=""/>
												</button>
											</td>
										</tr>
									</table>
								</div>
								</fieldset>
			     		</div>
			     	{/if}
			     	{if i.type == 'approverRoleOPL'}
			     		<div class="properties---approverRoleId">
			     			<label class="page-edit-label">Role</label>
			     			<select name="approverRoleId" class="toolboxLabel">
								</select>
			     		</div>
			     	{/if}
			    {else}
			    	<div class="properties---${i.name}">
		     			<label class="page-edit-label">${i.label}</label>
		     			{if i.type == 'label'}
							<input type="text" name="${i.name}" class="toolboxLabel" disabled="disabled"
							{if i.value != ""}
							 	 value="${i.value}"
							{/if}
							 />
						{/if}
	
						{if i.type == 'text'}
							<input type="text" name="${i.name}" class="toolboxLabel"
							{if i.defaultValue != ""}
							 	 value="${i.defaultValue}"
							{/if}
							 />
						{/if}
	
						{if i.type == 'password'}
							<input type="password" name="${i.name}" class="toolboxLabel"
							{if i.defaultValue != ""}
							 	 value="${i.defaultValue}"
							{/if}
							/>
						{/if}
	
						{if i.type == 'checkbox'}
							<input type="checkbox" name="${i.name}" class="toolboxLabel"
							{if i.defaultValue == 'checked'}
								checked
							{/if}
							/>
						{/if}
	
						{if i.type == 'radio'}
							{if i.options != null}
								{var index = 0}
								{for option in i.options}
									{if option_index != 'toString'}
										<div><input id="${i.name}---${index}" type="radio" name="${i.name}" value="${option_index}"
											{if i.defaultValue == option_index}
												checked
											{/if}
										/><label for="${i.name}_${index}" class="toolboxLabel">${option}</label></div>
										{eval}index++{/eval}
									{/if}
								{/for}
							{/if}
						{/if}
	
						{if i.type == 'dropdown'}
							<select name="${i.name}" class="toolboxLabel">
								{if i.options != null}
									{for option in i.options}
										{if option_index != 'toString'}
											<option value="${option_index}"
											{if i.defaultValue == option_index}
												selected
											{/if}
											>${option}</option>
										{/if}
									{/for}
								{/if}
							</select>
						{/if}
	
						{if i.type == 'list'}
							<select name="${i.name}" multiple="true" size="5" class="toolboxLabel">
								{if i.options != null}
									{for option in i.options}
										{if option_index != 'toString'}
											<option value="${option_index}"
											{if i.defaultValue == option_index}
												selected
											{/if}
											>${option}</option>
										{/if}
									{/for}
								{/if}
							</select>
						{/if}
	
						{if i.type == 'textarea'}
							<textarea cols="20" rows="3" name="${i.name}">{if i.defaultValue != ""}${i.defaultValue}{/if}</textarea>
						{/if}
	
						{if i.type == 'button'}
							<input type="button" name="${i.name}" value="${i.value}" id="${stageInstId}---${i.name}" class="toolboxLabel"/>
						{/if}
		     		</div>
		     		<div class="page-content-spacer"></div>
		     	{/if}
			{/for}
</div>
</div>
</div>