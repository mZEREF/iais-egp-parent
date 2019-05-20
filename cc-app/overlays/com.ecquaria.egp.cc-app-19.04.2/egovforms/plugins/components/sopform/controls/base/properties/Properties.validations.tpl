{eval}
	validationManager.doSetup({ id: id });
{/eval}
<div class="property-container">
	<div id="validationPanel">
		{if errorMessagePositionOptions.length != 0}
			<div>
				<label class="page-edit-label">Message Position</label>
				<select id="errMsgPosSelect" class="property-input">
					{for row in errorMessagePositionOptions}
						<option value="${row.code|escape}">${row.description|escape}</option>
					{/for}
				</select>
			</div>
			<div class="page-content-spacer"></div>
		{/if}
		<div>
			<label class="page-edit-label">Custom Error Message</label>
			<input id="customErrorMessage" type="text" value="${customErrorMessage|default:''|escape}" class="property-input" />
		</div>
		<div class="page-content-spacer"></div>
		{for row in toggleOptions}
			<div>
				<label class="page-edit-label">${row.description|escape}</label>
				<input id="${row.code|escape}" type="checkbox" value="${row.code|escape}"></input>
			</div>
			<div class="page-content-spacer"></div>
		{/for}
		
		{if dataTypeOptions.length != 0}
			<div>
				<label class="page-edit-label">Category</label>
				{if dataTypeOptions.length == 1}
					{for row in dataTypeOptions}
						<input id="category_select" type="hidden" value="${row.code|escape}" class="property-input">
						${row.description|escape}
					{/for}
				{elseif dataTypeOptions.length > 1}
					<select id="category_select" class="property-input">
						{for row in dataTypeOptions}
							<option value="${row.code|escape}">${row.description|escape}</option>
						{/for}
					</select>
				{/if}
			</div>
			<div class="page-content-spacer"></div>
			<div id="validationOptionDiv" style="display:none">
				<label class="page-edit-label"></label>
				<div>
					<fieldset>
						<legend>Options</legend>
						
						<div id="forDataType_alphabet" style="display:none">
							<div>
								<label class="page-edit-label">Min Length</label>
								<input id="alphabet-min-length" type="text" value="" class="property-input">
							</div>
							<div class="page-content-spacer"></div>
							<div>
								<label class="page-edit-label">Max Length</label>
								<input id="alphabet-max-length" type="text" value="" class="property-input">
							</div>
							
						</div>
						
						<div id="forDataType_alpha_numeric" style="display:none">
							<div>
								<label class="page-edit-label">Min Length</label>
								<input id="alpha_numeric-min-length" type="text" value="" class="property-input">
							</div>
							<div class="page-content-spacer"></div>
							<div>
								<label class="page-edit-label">Max Length</label>
								<input id="alpha_numeric-max-length" type="text" value="" class="property-input">
							</div>
							
						</div>
						
						<div id="forDataType_decimal" style="display:none">
							<div>
								<label class="page-edit-label">Min Value</label>
								<input id="decimal-min-value" type="text" value="" class="property-input">
							</div>
							<div class="page-content-spacer"></div>
							<div>
								<label class="page-edit-label">Max Value</label>
								<input id="decimal-max-value" type="text" value="" class="property-input">
							</div>
							
						</div>

						<div id="forDataType_number" style="display:none">
							<div>
								<label class="page-edit-label">Min Value</label>
								<input id="number-min-value" type="text" value="">
							</div>
							<div class="page-content-spacer"></div>
							<div>
								<label class="page-edit-label">Max Value</label>
								<input id="number-max-value" type="text" value="">
							</div>
							
						</div>
						
						<div id="forDataType_date" style="display:none">
							<div>
								<label class="page-edit-label">Type</label>
								<select id="date_type_option_select">
									{for row in dateTypeOptions}
										<option value="${row.code|escape}">${row.description|escape}</option>
									{/for}
								</select>
							</div>
							<div class="page-content-spacer"></div>
							<div>
								<label class="page-edit-label">Start</label>
								<input id="date-start" type="text" value="">
							</div>
							<div class="page-content-spacer"></div>
							<div id="dateEndRow" style="display:none">
								<label class="page-edit-label">End</label>
								<input id="date-end" type="text" value="">
							</div>
						</div>
						<div id="forDataType_value_count" style="display:none">
							<div>
								<label class="page-edit-label">Min Count</label>
								<input id="value_count-min-count" type="text" value="">
							</div>
							<div class="page-content-spacer"></div>
							<div>
								<label class="page-edit-label">Max Count</label>
								<input id="value_count-max-count" type="text" value="">
							</div>
						</div>
					</fieldset>
				</div>
			</div>
			<div class="page-content-spacer"></div>
		{/if}
			
		 {if maxPredRules != 0}
			{var r = Form.Util.range(1, maxPredRules)}
			{for index in r}
				<div>
					<label class="page-edit-label">Rule (${index})</label>
					<select id="select_pred_vald_${index}">
						{for row in validationPredefinedtype}
							<option value="${row.code|escape}">${row.description|escape}</option>
						{/for}
					</select>
				</div>
				<div class="page-content-spacer"></div>
				<div id="pred_parameters_${index}" style="display: none">
					<label class="page-edit-label"></label>
					<div>

					</div>
				</div>
				<div class="page-content-spacer"></div>
			{/for}
		{/if}
		
		<div>
			<label class="page-edit-label"><input type="checkbox" id="enable-blur-func"/> Blur:</label>
			<input id="blur-func" type="text" value="">
		</div>
		<div class="page-content-spacer"></div>
		<div>
			<label class="page-edit-label"><input type="checkbox" id="enable-change-func"/> Value Change:</label>
			<input id="change-func" type="text" value="">
		</div>
		<div class="page-content-spacer"></div>
		<div>
			<label class="page-edit-label"><input type="checkbox" id="enable-submit-func"/> Submit:</label>
			<input id="submit-func" type="text" value="">
		</div>
		
	</div>
</div>