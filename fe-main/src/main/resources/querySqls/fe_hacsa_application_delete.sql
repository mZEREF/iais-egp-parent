/**
 * Y
 */
DELETE FROM hcsala.dbo.app_svc_key_personnel
WHERE APPLICATION_ID in (SELECT ID FROM hcsala.dbo.application
WHERE APPLICATION_NO like 'AN200512000809C%');

DELETE FROM hcsala.dbo.app_grp_personnel_ext
WHERE APP_GRP_PSN_ID in (SELECT ID FROM hcsala.dbo.app_grp_personnel
WHERE APP_GRP_ID in (SELECT APP_GRP_ID FROM hcsala.dbo.application
WHERE APPLICATION_NO like 'AN200512000809C%'));

DELETE FROM hcsala.dbo.app_grp_personnel
WHERE APP_GRP_ID in (SELECT APP_GRP_ID FROM hcsala.dbo.application
WHERE APPLICATION_NO like 'AN200512000809C%');

/**
 * Y
 */

DELETE FROM hcsala.dbo.app_svc_premises_scope_allocation
WHERE APP_SVC_PREM_SCOPE_ID in (SELECT ID
FROM hcsala.dbo.app_svc_premises_scope
WHERE APP_PREM_CORRE_ID in (SELECT ID FROM hcsala.dbo.app_premises_correlation
WHERE APPLICATION_ID in (SELECT ID FROM hcsala.dbo.application
WHERE APPLICATION_NO like 'AN200512000809C%')));

/**
 * Y  APP_PREM_CORRE_ID
 */

DELETE FROM hcsala.dbo.app_svc_premises_scope
WHERE APP_PREM_CORRE_ID in (SELECT ID FROM hcsala.dbo.app_premises_correlation
WHERE APPLICATION_ID in (SELECT ID FROM hcsala.dbo.application
WHERE APPLICATION_NO like 'AN200512000809C%'));

/**
 * Y
 */
DELETE FROM hcsala.dbo.app_change_field
WHERE APP_ID in (SELECT ID FROM hcsala.dbo.application
WHERE APPLICATION_NO like 'AN200512000809C%');

/**
 * Y
 */
DELETE FROM hcsala.dbo.app_svc_personnel
WHERE APPLICATION_ID in (SELECT ID FROM hcsala.dbo.application
WHERE APPLICATION_NO like 'AN200512000809C%');

/**
 * Y
 */
DELETE FROM hcsala.dbo.app_base_correlation
WHERE BASE_APP_ID in (SELECT ID FROM hcsala.dbo.application 
WHERE APPLICATION_NO like 'AN200512000809C%');

/**
 * Y
 */
DELETE FROM hcsala.dbo.app_svc_doc
WHERE APP_PREM_CORRE_ID in (SELECT ID FROM hcsala.dbo.app_premises_correlation
WHERE APPLICATION_ID in (SELECT ID FROM hcsala.dbo.application
WHERE APPLICATION_NO like 'AN200512000809C%'));

/**
 * 935375CE-A98A-EA11-BE79-000C298A32C2
 * SELECT ID FROM hcsala.dbo.app_premises_correlation
WHERE APPLICATION_ID in (SELECT ID FROM hcsala.dbo.application
WHERE APPLICATION_NO like 'AN200512000809C%')
 */

/**
 * Y
 */
DELETE FROM hcsala.dbo.app_premises_special_doc
WHERE APP_PREM_CORRE_ID in (SELECT ID FROM hcsala.dbo.app_premises_correlation
WHERE APPLICATION_ID in (SELECT ID FROM hcsala.dbo.application
WHERE APPLICATION_NO like 'AN200512000809C%'));

/**
 * Y
 */
DELETE FROM hcsala.dbo.app_premises_self_decl_chkl
WHERE APP_PREM_CORRE_ID in (SELECT ID FROM hcsala.dbo.app_premises_correlation
WHERE APPLICATION_ID in (SELECT ID FROM hcsala.dbo.application
WHERE APPLICATION_NO like 'AN200512000809C%'));

/**
 * Y
 */
DELETE FROM hcsala.dbo.app_premises_pre_inspect_nc_item
WHERE PRE_NC_ID in (SELECT ID
FROM hcsala.dbo.app_premises_pre_inspect_nc
WHERE APP_PREM_CORRE_ID in (SELECT ID FROM hcsala.dbo.app_premises_correlation
WHERE APPLICATION_ID in (SELECT ID FROM hcsala.dbo.application
WHERE APPLICATION_NO like 'AN200512000809C%')));


/**
 * Y
 */
DELETE FROM hcsala.dbo.app_premises_pre_inspect_nc_doc
WHERE NC_ITEM_ID in (SELECT ID FROM hcsala.dbo.app_premises_pre_inspect_nc
WHERE APP_PREM_CORRE_ID in (SELECT ID FROM hcsala.dbo.app_premises_correlation
WHERE APPLICATION_ID in (SELECT ID FROM hcsala.dbo.application
WHERE APPLICATION_NO like 'AN200512000809C%')));


/**
 * Y
 */
DELETE FROM hcsala.dbo.app_premises_pre_inspect_nc
WHERE APP_PREM_CORRE_ID in (SELECT ID FROM hcsala.dbo.app_premises_correlation
WHERE APPLICATION_ID in (SELECT ID FROM hcsala.dbo.application
WHERE APPLICATION_NO like 'AN200512000809C%'));

/**
 * Y
 */
DELETE FROM hcsala.dbo.app_premises_misc
WHERE APP_PREM_CORRE_ID in (SELECT ID FROM hcsala.dbo.app_premises_correlation
WHERE APPLICATION_ID in (SELECT ID FROM hcsala.dbo.application
WHERE APPLICATION_NO like 'AN200512000809C%'));

/**
 * Y
 */
DELETE FROM hcsala.dbo.app_premises_inspection_appt
WHERE APP_PREM_CORRE_ID in (SELECT ID FROM hcsala.dbo.app_premises_correlation
WHERE APPLICATION_ID in (SELECT ID FROM hcsala.dbo.application
WHERE APPLICATION_NO like 'AN200512000809C%'));

/**
 * Y
 */
DELETE FROM hcsala.dbo.app_premises_adhoc_chkl_item
WHERE ADHOC_CONF_ID in (SELECT ID
FROM hcsala.dbo.app_premises_adhoc_chkl_config
WHERE APP_PREM_CORRE_ID in (SELECT ID FROM hcsala.dbo.app_premises_correlation 
WHERE APPLICATION_ID in (SELECT ID FROM hcsala.dbo.application
WHERE APPLICATION_NO like 'AN200512000809C%')));

/**
 * Y
 */
DELETE FROM hcsala.dbo.app_premises_adhoc_chkl_config
WHERE APP_PREM_CORRE_ID in (SELECT ID FROM hcsala.dbo.app_premises_correlation
WHERE APPLICATION_ID in (SELECT ID FROM hcsala.dbo.application
WHERE APPLICATION_NO like 'AN200512000809C%'));

/**
 * Y
 */
DELETE FROM hcsala.dbo.app_premises_correlation
WHERE APPLICATION_ID in (SELECT ID FROM hcsala.dbo.application
WHERE APPLICATION_NO like 'AN200512000809C%');


/**
 * app group premises Y
 */

DELETE FROM hcsala.dbo.app_prem_ph_open_period
WHERE PREM_ID in (SELECT ID
FROM hcsala.dbo.app_grp_premises
WHERE APP_GRP_ID in (SELECT APP_GRP_ID FROM hcsala.dbo.application
WHERE APPLICATION_NO like 'AN200512000809C%'));

/**
 * Y
 */
DELETE FROM hcsala.dbo.app_premises_operational_unit
WHERE PREMISES_ID in (SELECT ID
FROM hcsala.dbo.app_grp_premises
WHERE APP_GRP_ID in (SELECT APP_GRP_ID FROM hcsala.dbo.application
WHERE APPLICATION_NO like 'AN200512000809C%'));

/**
 * Y
 */
DELETE FROM hcsala.dbo.app_grp_primary_doc
WHERE APP_GRP_PREM_ID in (SELECT ID
FROM hcsala.dbo.app_grp_premises
WHERE APP_GRP_ID in (SELECT APP_GRP_ID FROM hcsala.dbo.application
WHERE APPLICATION_NO like 'AN200512000809C%'));


/**
 * Y
 */
DELETE FROM hcsala.dbo.app_grp_premises
WHERE APP_GRP_ID in (SELECT APP_GRP_ID FROM hcsala.dbo.application
WHERE APPLICATION_NO like 'AN200512000809C%');

/**
 * app group Y
 */

DELETE FROM hcsala.dbo.app_group_misc
WHERE APP_GRP_ID in (SELECT APP_GRP_ID FROM hcsala.dbo.application
WHERE APPLICATION_NO like 'AN200512000809C%');

/**
 * Y
 */
DELETE FROM hcsala.dbo.app_grp_submission_draft
WHERE APP_GRP_ID in (SELECT APP_GRP_ID FROM hcsala.dbo.application
WHERE APPLICATION_NO like 'AN200512000809C%');


/**
 * other Y
 */
DELETE FROM hcsala.dbo.app_fee_details
WHERE APPLICATION_NO like 'AN200512000809C%';



BEGIN
	DECLARE @groupId  uniqueidentifier
	DECLARE @appId uniqueidentifier
	DECLARE csr_app CURSOR 
	FOR 
		SELECT a.ID, a.APP_GRP_ID FROM hcsala.dbo.application a WHERE a.APPLICATION_NO like 'AN200512000809C%'
	OPEN csr_app
	FETCH NEXT FROM csr_app INTO @appId, @groupId
	
	WHILE (@@fetch_status = 0)
	BEGIN TRY
		BEGIN TRANSACTION
		BEGIN
			DELETE FROM hcsala.dbo.application where id = @appId
			FETCH NEXT FROM csr_app INTO @appId, @groupId
		END
				
		IF (@@fetch_status = -1)
			BEGIN
				DELETE FROM hcsala.dbo.app_grp_submission_draft where APP_GRP_ID = @groupId
				DELETE FROM hcsala.dbo.application_group where id = @groupId
			END
		
		COMMIT TRANSACTION 
	END TRY
	BEGIN CATCH
		PRINT 'TRANSACTION ERROR' 
		ROLLBACK TRANSACTION
		FETCH NEXT FROM csr_app INTO @appId, @groupId
	END CATCH
	
	CLOSE csr_app
	DEALLOCATE csr_app
END












