BEGIN;
CREATE TABLE [Entry] (
    [ent_seq] INTEGER  PRIMARY KEY NOT NULL
);
CREATE INDEX entry_ent_seq_idx ON Entry (ent_seq);
COMMIT;

BEGIN;
CREATE TABLE [r_ele] (
    [ID_r_ele] INTEGER  NOT NULL  PRIMARY KEY AUTOINCREMENT,
	[reb] TEXT, 
	[reb_romaji] TEXT,
    [ent_seq] INTEGER  NOT NULL,
    [re_nokanji] BOOLEAN NOT NULL DEFAULT 0,
    FOREIGN KEY(ent_seq) REFERENCES Entry(ent_seq)
);

CREATE INDEX r_ele_ent_seq_idx ON r_ele (ent_seq);
CREATE INDEX r_ele_reb_idx ON r_ele (reb);
CREATE INDEX r_ele_reb_romaji_idx ON r_ele (reb_romaji);
COMMIT;


BEGIN;
CREATE TABLE [re_restr] (
	[ID_re_restr] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	[ID_r_ele] INTEGER NOT NULL,
	[re_restr] TEXT,
	[ent_seq] INTEGER  NOT NULL,
	FOREIGN KEY(ID_r_ele) REFERENCES r_ele(ID_r_ele),
	FOREIGN KEY(ent_seq) REFERENCES Entry(ent_seq)
);

CREATE INDEX re_restr_re_restr_idx ON re_restr (re_restr);
CREATE INDEX re_restr_r_ele_idx ON re_restr (ID_r_ele);
CREATE INDEX re_restr_ent_seq_idx ON re_restr (ent_seq);
COMMIT;

BEGIN;
CREATE TABLE [re_inf] (
	[ID_re_inf] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	[ID_r_ele] INTEGER NOT NULL,
	[re_inf] TEXT,
	[ent_seq] INTEGER  NOT NULL,
	FOREIGN KEY(ID_r_ele) REFERENCES r_ele(ID_r_ele),
	FOREIGN KEY(ent_seq) REFERENCES Entry(ent_seq)
);
CREATE INDEX re_inf_r_ele_idx ON re_inf (ID_r_ele);
CREATE INDEX re_inf_ent_seq_idx ON re_inf (ent_seq);
CREATE INDEX re_inf_re_inf_idx ON re_inf (re_inf);

COMMIT;

BEGIN;
CREATE TABLE [re_pri] (
	[ID_re_pri] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	[ID_r_ele] INTEGER NOT NULL,
	[re_pri] TEXT,
	[ent_seq] INTEGER  NOT NULL,
	FOREIGN KEY(ID_r_ele) REFERENCES r_ele(ID_r_ele),
	FOREIGN KEY(ent_seq) REFERENCES Entry(ent_seq)
);
CREATE INDEX re_pri_r_ele_idx ON re_pri (ID_r_ele);
CREATE INDEX re_pri_ent_seq_idx ON re_pri (ent_seq);
COMMIT;

BEGIN;
CREATE TABLE [k_ele] (
	[ID_k_ele] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	[keb] TEXT,
	[ent_seq] INTEGER NOT NULL,
	FOREIGN KEY(ent_seq) REFERENCES Entry(ent_seq)
);
CREATE INDEX k_ele_ent_seq_idx ON k_ele (ent_seq);
CREATE INDEX k_ele_keb_idx ON k_ele (keb);
COMMIT;

BEGIN;
CREATE TABLE [ke_inf] (
	[ID_ke_inf] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	[ID_k_ele] INTEGER NOT NULL,
	[ke_inf] TEXT,
	[ent_seq] INTEGER  NOT NULL,
	FOREIGN KEY(ID_k_ele) REFERENCES k_ele(ID_k_ele),
	FOREIGN KEY(ent_seq) REFERENCES Entry(ent_seq)
);
CREATE INDEX ke_inf_k_ele_idx ON ke_inf (ID_k_ele);
CREATE INDEX ke_inf_ent_seq_idx ON ke_inf (ent_seq);
COMMIT;

BEGIN;
CREATE TABLE [ke_pri] (
	[ID_ke_pri] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	[ID_k_ele] INTEGER NOT NULL,
	[ke_pri] TEXT,
	[ent_seq] INTEGER  NOT NULL,
	FOREIGN KEY(ID_k_ele) REFERENCES k_ele(ID_k_ele),
	FOREIGN KEY(ent_seq) REFERENCES Entry(ent_seq)
);
CREATE INDEX ke_pri_k_ele_idx ON ke_pri (ID_k_ele);
CREATE INDEX ke_pri_ent_seq_idx ON ke_pri (ent_seq);
COMMIT;

BEGIN;
CREATE TABLE [info] (
	[ID_info] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	[ent_seq] INTEGER NOT NULL,
	FOREIGN KEY(ent_seq) REFERENCES Entry(ent_seq)
);
CREATE INDEX info_ent_seq_idx ON info (ent_seq);
COMMIT;

BEGIN;
CREATE TABLE [links] (
	[ID_links] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	[ID_info] INTEGER NOT NULL,
	[link_tag] TEXT,
	[link_desc] TEXT,
	[link_uri] TEXT,
	[ent_seq] INTEGER NOT NULL,
	FOREIGN KEY(ent_seq) REFERENCES Entry(ent_seq),
	FOREIGN KEY (ID_info) REFERENCES info(ID_info)
);
CREATE INDEX links_info_idx ON links (ID_info);
CREATE INDEX links_ent_seq_idx ON links (ent_seq);
COMMIT;

BEGIN;
CREATE TABLE [bibl] (
	[ID_bibl] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	[ID_info] INTEGER NOT NULL,
	[bib_tag] TEXT,
	[bib_txt] TEXT,
	[ent_seq] INTEGER NOT NULL,
	FOREIGN KEY(ent_seq) REFERENCES Entry(ent_seq),
	FOREIGN KEY (ID_info) REFERENCES info(ID_info)
);
CREATE INDEX bibl_ent_seq_idx ON bibl (ent_seq);
CREATE INDEX bibl_info_idx ON bibl (ID_info);
COMMIT;

BEGIN;
CREATE TABLE [etym] (
	[ID_etym] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	[ID_info] INTEGER NOT NULL,
	[etym] TEXT,
	[ent_seq] INTEGER NOT NULL,
	FOREIGN KEY(ent_seq) REFERENCES Entry(ent_seq),
	FOREIGN KEY (ID_info) REFERENCES info(ID_info)
);
CREATE INDEX etym_info_idx ON etym (ID_info);
CREATE INDEX etym_ent_seq_idx ON etym (ent_seq);
COMMIT;

BEGIN;
CREATE TABLE [audit] (
	[ID_audit] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	[ID_info] INTEGER NOT NULL,
	[upd_date] DATE,
	[upd_detl] TEXT,
	[ent_seq] INTEGER NOT NULL,
	FOREIGN KEY(ent_seq) REFERENCES Entry(ent_seq),
	FOREIGN KEY (ID_info) REFERENCES info(ID_info)
);
CREATE INDEX audit_info_idx ON audit (ID_info);
CREATE INDEX audit_ent_seq_idx ON audit (ent_seq);
COMMIT;

BEGIN;
CREATE TABLE [sense] (
	[ID_sense] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	[ent_seq] INTEGER NOT NULL,
	FOREIGN KEY(ent_seq) REFERENCES Entry(ent_seq)
);
CREATE INDEX sense_ent_seq_idx ON sense (ent_seq);
COMMIT;

BEGIN;
CREATE TABLE [stagk] (
	[ID_stagk] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	[ID_sense] INTEGER NOT NULL,
	[stagk] TEXT,
	[ent_seq] INTEGER NOT NULL,
	FOREIGN KEY(ent_seq) REFERENCES Entry(ent_seq),
	FOREIGN KEY (ID_sense) REFERENCES sense(ID_sense)
);
CREATE INDEX stagk_sense_idx ON stagk (ID_sense);
CREATE INDEX stagk_ent_seq_idx ON stagk (ent_seq);
COMMIT;

BEGIN;
CREATE TABLE [stagr] (
	[ID_stagr] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	[ID_sense] INTEGER NOT NULL,
	[stagr] TEXT,
	[ent_seq] INTEGER NOT NULL,
	FOREIGN KEY(ent_seq) REFERENCES Entry(ent_seq),
	FOREIGN KEY (ID_sense) REFERENCES sense(ID_sense)
);
CREATE INDEX stagr_sense_idx ON stagr (ID_sense);
CREATE INDEX stagr_ent_seq_idx ON stagr (ent_seq);
COMMIT;

BEGIN;
CREATE TABLE [xref] (
	[ID_xref] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	[ID_sense] INTEGER NOT NULL,
	[xref] TEXT,
	[ent_seq] INTEGER NOT NULL,
	FOREIGN KEY(ent_seq) REFERENCES Entry(ent_seq),
	FOREIGN KEY (ID_sense) REFERENCES sense(ID_sense)
);
CREATE INDEX xref_sense_idx ON xref (ID_sense);
CREATE INDEX xref_ent_seq_idx ON xref (ent_seq);
COMMIT;

BEGIN;
CREATE TABLE [ant] (
	[ID_ant] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	[ID_sense] INTEGER NOT NULL,
	[ant] TEXT,
	[ent_seq] INTEGER NOT NULL,
	FOREIGN KEY(ent_seq) REFERENCES Entry(ent_seq),
	FOREIGN KEY (ID_sense) REFERENCES sense(ID_sense)
);
CREATE INDEX ant_sense_idx ON ant (ID_sense);
CREATE INDEX ant_ent_seq_idx ON ant (ent_seq);
COMMIT;

BEGIN;
CREATE TABLE [pos] (
	[ID_pos] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	[ID_sense] INTEGER NOT NULL,
	[pos] TEXT,
	[ent_seq] INTEGER NOT NULL,
	FOREIGN KEY(ent_seq) REFERENCES Entry(ent_seq),
	FOREIGN KEY (ID_sense) REFERENCES sense(ID_sense)
);
CREATE INDEX pos_sense_idx ON pos (ID_sense);
CREATE INDEX pos_ent_seq_idx ON pos (ent_seq);
COMMIT;

BEGIN;
CREATE TABLE [field] (
	[ID_field] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	[ID_sense] INTEGER NOT NULL,
	[field] TEXT,
	[ent_seq] INTEGER NOT NULL,
	FOREIGN KEY(ent_seq) REFERENCES Entry(ent_seq),
	FOREIGN KEY (ID_sense) REFERENCES sense(ID_sense)
);
CREATE INDEX field_sense_idx ON field (ID_sense);
CREATE INDEX field_ent_seq_idx ON field (ent_seq);
COMMIT;

BEGIN;
CREATE TABLE [misc] (
	[ID_misc] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	[ID_sense] INTEGER NOT NULL,
	[misc] TEXT,
	[ent_seq] INTEGER NOT NULL,
	FOREIGN KEY(ent_seq) REFERENCES Entry(ent_seq),
	FOREIGN KEY (ID_sense) REFERENCES sense(ID_sense)
);
CREATE INDEX misc_sense_idx ON misc (ID_sense);
CREATE INDEX misc_ent_seq_idx ON misc (ent_seq);
COMMIT;

BEGIN;
CREATE TABLE [lsource] (
	[ID_lsource] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	[ID_sense] INTEGER NOT NULL,
	[lsource] TEXT,
	[lang] TEXT,
	[ent_seq] INTEGER NOT NULL,
	FOREIGN KEY(ent_seq) REFERENCES Entry(ent_seq),
	FOREIGN KEY (ID_sense) REFERENCES sense(ID_sense)
);
CREATE INDEX lsource_sense_idx ON lsource (ID_sense);
CREATE INDEX lsource_ent_seq_idx ON lsource (ent_seq);
COMMIT;

BEGIN;
CREATE TABLE [dial] (
	[ID_dial] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	[ID_sense] INTEGER NOT NULL,
	[dial] TEXT,
	[ent_seq] INTEGER NOT NULL,
	FOREIGN KEY(ent_seq) REFERENCES Entry(ent_seq),
	FOREIGN KEY (ID_sense) REFERENCES sense(ID_sense)
);
CREATE INDEX dial_sense_idx ON dial (ID_sense);
CREATE INDEX dial_ent_seq_idx ON dial (ent_seq);
COMMIT;

BEGIN;
CREATE TABLE [gloss] (
	[ID_gloss] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	[ID_sense] INTEGER NOT NULL,
	[gloss] TEXT,
	[lang] TEXT,
	[ent_seq] INTEGER NOT NULL,
	FOREIGN KEY(ent_seq) REFERENCES Entry(ent_seq),
	FOREIGN KEY (ID_sense) REFERENCES sense(ID_sense)
);

CREATE INDEX gloss_gloss_idx ON gloss (gloss);
CREATE INDEX gloss_ID_sense_idx ON gloss (ID_sense);
CREATE INDEX gloss_ent_seq_idx ON gloss (ent_seq);
COMMIT;

BEGIN;
CREATE TABLE [example] (
	[ID_example] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	[ID_sense] INTEGER NOT NULL,
	[example] TEXT,
	[ent_seq] INTEGER NOT NULL,
	FOREIGN KEY(ent_seq) REFERENCES Entry(ent_seq),
	FOREIGN KEY (ID_sense) REFERENCES sense(ID_sense)
);
CREATE INDEX example_ID_sense_idx ON example (ID_sense);
CREATE INDEX example_ent_seq_idx ON example (ent_seq);
COMMIT;

BEGIN;
CREATE TABLE [s_inf] (
	[ID_s_inf] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	[ID_sense] INTEGER NOT NULL,
	[s_inf] TEXT,
	[ent_seq] INTEGER NOT NULL,
	FOREIGN KEY(ent_seq) REFERENCES Entry(ent_seq),
	FOREIGN KEY (ID_sense) REFERENCES sense(ID_sense)
);
CREATE INDEX s_inf_ID_sense_idx ON s_inf (ID_sense);
CREATE INDEX s_inf_ent_seq_idx ON s_inf (ent_seq);
COMMIT;
