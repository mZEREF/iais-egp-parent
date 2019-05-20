{var condition}
<Control type="${control.FQN}" ref="${control.properties.name}" {if (control.position === 0) || ((control.position || -1) >= 0) } position="${control.position}" {/if}>
	{eval}
		condition = conditionManager.getDataForTemplate({
			control: control
		});
	{/eval}
	<Properties>
		${serializer.serializeControlId(control, includeId)}
		{if control.properties.source !== undefined}
		<Entry name="source"><String>${control.properties.source|escape}</String></Entry>
		{/if}
		{if control.properties.displayOnly !== undefined}
		<Entry name="displayOnly"><String>${control.properties.displayOnly|escape}</String></Entry>
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