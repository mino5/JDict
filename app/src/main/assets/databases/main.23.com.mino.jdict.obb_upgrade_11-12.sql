INSERT INTO Setting(SettingKey, SettingValue)
SELECT 'KanaAsRomaji', 'False'
WHERE NOT EXISTS(SELECT 1 FROM Setting WHERE SettingKey = 'KanaAsRomaji');