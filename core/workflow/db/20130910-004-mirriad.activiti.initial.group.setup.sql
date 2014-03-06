INSERT INTO ACT_ID_GROUP VALUES ('admin', 1, 'System Administrator', 'security-role');
INSERT INTO ACT_ID_GROUP VALUES ('user', 1, 'User', 'security-role');

INSERT INTO ACT_ID_USER VALUES ('sysadmin', 1, 'System', 'Administrator', 'james.mcilroy@mirriad.com', 'sysadmin', NULL);
INSERT INTO ACT_ID_MEMBERSHIP VALUES ('sysadmin', 'admin');
INSERT INTO ACT_ID_MEMBERSHIP VALUES ('sysadmin', 'user');