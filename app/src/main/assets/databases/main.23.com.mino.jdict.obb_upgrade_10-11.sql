INSERT INTO Setting(SettingKey, SettingValue)
SELECT 'SearchSettingsGlobal', 'False'
WHERE NOT EXISTS(SELECT 1 FROM Setting WHERE SettingKey = 'SearchSettingsGlobal');