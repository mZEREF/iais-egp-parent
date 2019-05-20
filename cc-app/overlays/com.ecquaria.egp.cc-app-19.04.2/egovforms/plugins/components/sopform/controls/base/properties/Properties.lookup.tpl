{eval}
	optionLookupManager.doSetup({ id: id });
{/eval}
<div class="property-container">
<div id="lookupPanel">
	<div>
		<label class="page-edit-label">Sources</label>
		<select id="source_selection">
			{for row in sourceOptions}
				<option value="${row.code|escape}">${row.description|escape}</option>
			{/for}
		</select>
	</div>
	<div class="page-content-spacer"></div>
	<div>
		<label class="page-edit-label">Escape</label>
		<input id="support_EscapeOption" type="checkbox" value="support"></input>
	</div>
	<div class="page-content-spacer"></div>
	<div>
		<label class="page-edit-label">Options</label>
		<div id="sources_options_default_div" style="margin: 0pt 0pt 0pt 132px;">
			{for option in options}
			<div class="property-container">
				<input type="text" class="property-input-description" value="${option.description|escape}" size="12"
					style="vertical-align: top;" /><button class="property-remove-option">
					<img src="${sopFormTheme}images/general/icon-delete.png" alt=""/>
				</button><button class="property-default-option">
					<img src="${sopFormTheme}images/general/icon-accept.png" alt=""/>
				</button><br/>
				<input name="optionValue" type="text" class="property-input-code" value="${option.code|escape}" size="12"
					style="vertical-align: top; display: none"/>
			</div>
			{/for}
			<div id="showValue">
				<input id="showOptionValue" type="checkbox" value="show"></input>advance options
			</div>
			<button type="button" class="property-add-option">
				<img src="${sopFormTheme}images/forms/add.png" alt=""/>
			</button>
			<button type="button" class="property-save-option" onclick="optionLookupManager.doSaveOptionDialog();">
				<img src="${sopFormTheme}images/forms/database_add.png" alt=""/>
			</button>
		</div>
		<div id="sources_options_lookup_div">
			<select id="option_code_selection">
				{for row in lookupOptions}
					<option value="${row.description|escape}">${row.description|escape}</option>
				{/for}
			</select>
		</div>
	</div>
	<div class="page-content-spacer"></div>
	<div id="reference_row">
		<label class="page-edit-label">References</label>
		<div id="sources_options_lookup_reference">
			<select id="reference_code_selection">
				{for row in referenceOptions}
					<option value="${row.code|escape}">${row.description|escape}</option>
				{/for}
			</select>
		</div>
	</div>
</div>
</div>