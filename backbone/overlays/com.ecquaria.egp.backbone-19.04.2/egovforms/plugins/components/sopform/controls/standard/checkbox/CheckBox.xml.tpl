{var optionLookup}
{var condition}
{var validation}
<Control type="${control.FQN}" ref="${control.properties.name}"
		dataType="multi-value"
		{if (control.position === 0) || ((control.position || -1) >= 0) } position="${control.position}" {/if}>
	{eval}
		optionLookup = optionLookupManager.getDataForTemplate({
			control: control
		});
		condition = conditionManager.getDataForTemplate({
			control: control
		});
		validation = validationManager.getDataForTemplate({
			control: control
		});
	{/eval}
	<Properties>
		${serializer.serializeControlId(control, includeId)}
		<Entry name="caption"><String>${control.properties.caption|escape}</String></Entry>
		<Entry name="width"><Integer>${control.properties.width}</Integer></Entry>
		<Entry name="cols"><Integer>${control.properties.cols}</Integer></Entry>
		<Entry name="alignment"><String>${control.properties.alignment|escape}</String></Entry>
		<Entry name="hint"><String>${control.properties.hint|escape}</String></Entry>
		<Entry name="help"><String>${control.properties.help|escape}</String></Entry>

		{if control.properties.componentLock != undefined && control.properties.componentLock.length > 0}
			<Entry name="componentLock">
				<List>
					{for cLock in control.properties.componentLock}
					<String>${cLock|escape}</String>
					{/for}
				</List>
			</Entry>
		{/if}
		{if control.properties.enable_file_attachments != undefined && control.properties.enable_file_attachments.length > 0}
			<Entry name="enable_file_attachments">
				<List>
					{for fa in control.properties.enable_file_attachments}
					<String>${fa|escape}</String>
					{/for}
				</List>
			</Entry>
		{/if}
		{if control.properties.onchangescript !== undefined}
			<Entry name="onchangescript"><String>${control.properties.onchangescript|escape}</String></Entry>
		{/if}
		{if control.properties.developerclass !== undefined}
			<Entry name="developerclass"><String>${control.properties.developerclass|escape}</String></Entry>
		{/if}

		<Entry name="selected">
			<List>
				{for item in control.properties.selected}
				<String>${item}</String>
				{/for}
			</List>
		</Entry>

		<Entry name="options">
			<List>
				{for row in control.properties.options}
				<Map>
					<Entry name="code"><String>${row.code|escape}</String></Entry>
					<Entry name="description"><String>${row.description|escape}</String></Entry>
				</Map>
				{/for}
			</List>
		</Entry>

		<Entry name="source_type"><String>${(optionLookup.source_type||'')|escape}</String></Entry>
		<Entry name="supportEscapeOption"><String>${(optionLookup.supportEscapeOption||'')|escape}</String></Entry>
		<Entry name="server_lookup_chosen"><String>${(optionLookup.server_lookup_chosen||'')|escape}</String></Entry>
		<Entry name="reference_chosen"><String>${(optionLookup.reference_chosen||'')|escape}</String></Entry>

		<Entry name="conditionModelPersistentMap">
			<Map>
				{for row in condition.persistentMap}
					<Entry name="${row.key|escape}"><String>${row.val|escape}</String></Entry>
				{/for}
			</Map>
		</Entry>
		
		<Entry name="validationModelPersistentMap">
			<Map>
				{for row in validation.validationModelPersistentMap}
					<Entry name="${row.key|escape}"><String>${row.val|escape}</String></Entry>
				{/for}
			</Map>
		</Entry>

		${serializer.serializeFontProperties(control)}

		${serializer.serializeAclProperties(control)}
	</Properties>
	{if children}
	<Children>
		${children}
	</Children>
	{/if}
</Control>