{var condition}
<Control type="${control.FQN}" {if (control.position === 0) || ((control.position || -1) >= 0) } position="${control.position}" {/if}
		ref="${control.properties.name}"
		dataType="multi-row">
	{eval}
		condition = conditionManager.getDataForTemplate({
			control: control
		});
	{/eval}
	<Properties>
		${serializer.serializeControlId(control, includeId)}
		<Entry name="cols"><Integer>${control.properties.cols}</Integer></Entry>
		<Entry name="repeat"><Integer>${control.properties.repeat}</Integer></Entry>
		<Entry name="controlButton"><String>${control.properties.controlButton|escape}</String></Entry>
		<Entry name="captionOrientation"><String>${control.properties.captionOrientation|escape}</String></Entry>
		<Entry name="type"><String>${control.properties.type|escape}</String></Entry>
		<Entry name="border"><Boolean>${control.properties.border}</Boolean></Entry>
		<Entry name="width"><String>${control.properties.width|escape}</String></Entry>
		<Entry name="title"><String>${control.properties.title|escape}</String></Entry>
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
		{if control.properties.developerclass !== undefined}
			<Entry name="developerclass"><String>${control.properties.developerclass|escape}</String></Entry>
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
		<Entry name="conditionModelPersistentMap">
			<Map>
				{for row in condition.persistentMap}
					<Entry name="${row.key|escape}"><String>${row.val|escape}</String></Entry>
				{/for}
			</Map>
		</Entry>

		${serializer.serializeFontProperties(control)}

		${serializer.serializeAclProperties(control)}
	</Properties>
	{if defined('children') && children}
	<Children>
		${children}
	</Children>
	{/if}
</Control>
