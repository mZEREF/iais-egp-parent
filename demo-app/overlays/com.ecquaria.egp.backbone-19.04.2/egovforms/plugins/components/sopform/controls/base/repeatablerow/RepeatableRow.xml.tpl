{var condition}
<Control type="${control.FQN}" ref="${control.properties.name}"
		dataType="multi-row"
		{if (control.position === 0) || ((control.position || -1) >= 0) } position="${control.position}" {/if}>
	{eval}
		condition = conditionManager.getDataForTemplate({
			control: control
		});
		
		aggMap = control.getDataForTemplate();
	{/eval}
	<Properties>
		${serializer.serializeControlId(control, includeId)}
		<Entry name="border"><Boolean>${control.properties.border}</Boolean></Entry>
		<Entry name="title"><String>${control.properties.title|escape}</String></Entry>
		<Entry name="controlButton"><String>${control.properties.controlButton|escape}</String></Entry>
		<Entry name="hint"><String>${control.properties.hint|escape}</String></Entry>
		<Entry name="tooltip"><String>${control.properties.tooltip|escape}</String></Entry>
		<Entry name="help"><String>${control.properties.help|escape}</String></Entry>
		<Entry name="prompt"><String>${control.properties.prompt|escape}</String></Entry>
		{if control.properties.aggregation != undefined && control.properties.aggregation.length > 0}
			<Entry name="aggregation">
				<List>
					{for agg in control.properties.aggregation}
					<String>${agg|escape}</String>
					{/for}
				</List>
			</Entry>
		{/if}
		<Entry name="aggregation_map">
			<Map>
				{for row in aggMap.persistentMap}
					<Entry name="${row.key|escape}"><String>${row.val|escape}</String></Entry>
				{/for}
			</Map>
		</Entry>
		{if control.properties.componentLock != undefined && control.properties.componentLock.length > 0}
			<Entry name="componentLock">
				<List>
					{for cLock in control.properties.componentLock}
					<String>${cLock|escape}</String>
					{/for}
				</List>
			</Entry>
		{/if}
		{if control.properties.onclickscript !== undefined}
			<Entry name="onclickscript"><String>${control.properties.onclickscript|escape}</String></Entry>
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
		{if control.properties.rows >=0}
		<Entry name="rows"><Integer>${control.properties.rows}</Integer></Entry>
        <Entry name="min_rows"><Integer>${control.properties.min_rows}</Integer></Entry>
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