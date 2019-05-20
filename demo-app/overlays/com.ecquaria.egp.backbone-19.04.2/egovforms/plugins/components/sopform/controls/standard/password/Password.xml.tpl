{var condition}
{var validation}
<Control type="${control.FQN}" ref="${control.properties.name}"
		dataType="single-value"
		{if (control.position === 0) || ((control.position || -1) >= 0) } position="${control.position}" {/if}>
	{eval}
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
		<Entry name="text"><String>${control.properties.text|escape}</String></Entry>
		<Entry name="alignment"><String>${control.properties.alignment|escape}</String></Entry>
		<Entry name="hint"><String>${control.properties.hint|escape}</String></Entry>
		<Entry name="help"><String>${control.properties.help|escape}</String></Entry>
		{if control.properties.size}
		<Entry name="size"><Integer>${control.properties.size}</Integer></Entry>
		{/if}
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
		{if control.properties.maxLength}
		<Entry name="maxLength"><Integer>${control.properties.maxLength}</Integer></Entry>
		{/if}
		{if control.properties.onchangescript !== undefined}
			<Entry name="onchangescript"><String>${control.properties.onchangescript|escape}</String></Entry>
		{/if}
		{if control.properties.developerclass !== undefined}
			<Entry name="developerclass"><String>${control.properties.developerclass|escape}</String></Entry>
		{/if}

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
</Control>