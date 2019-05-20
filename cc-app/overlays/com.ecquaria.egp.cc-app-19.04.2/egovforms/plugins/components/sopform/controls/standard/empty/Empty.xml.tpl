{var condition}
<Control type="${control.FQN}" {if (control.position === 0) || ((control.position || -1) >= 0) } position="${control.position}" {/if}>
	{eval}
		condition = conditionManager.getDataForTemplate({
			control: control
		});
	{/eval}
	<Properties>
		${serializer.serializeControlId(control, includeId)}
		<Entry name="height"><Integer>${control.properties.height}</Integer></Entry>

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
		<Entry name="conditionModelPersistentMap">
			<Map>
				{for row in condition.persistentMap}
					<Entry name="${row.key|escape}"><String>${row.val|escape}</String></Entry>
				{/for}
			</Map>
		</Entry>

		${serializer.serializeAclProperties(control)}
	</Properties>
</Control>