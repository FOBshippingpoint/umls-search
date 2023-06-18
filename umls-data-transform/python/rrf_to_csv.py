from os import error
import pandas as pd
from pathlib import Path

cwd = Path.cwd()
path_to_files = cwd / "umls-data-transform" / "data"
if not path_to_files.exists():
    print(path_to_files, "<---data路徑不存在")
    path_to_files = cwd.parent / "data"
    if path_to_files.exists():
        print(path_to_files, "<---改用此data路徑")
    else:
        path_to_files = cwd / "data"
        if path_to_files.exists():
            print(path_to_files, "<---改用此data路徑")
        else:
            error("找不到合適的data路徑")
            exit(1)

# Define the files and columns to keep
rrf_info = {
    "MRCONSO.RRF": {
        "col_nums": [0, 1, 2, 4, 11, 14],
        "col_names": ["CUI", "LAT", "TS", "STT", "SAB", "STR"],
    },
    "MRREL.RRF": {"col_nums": [0, 3, 4], "col_names": ["CUI1", "REL", "CUI2"]},
    "MRDEF.RRF": {"col_nums": [0, 4, 5], "col_names": ["CUI", "SAB", "DEF"]},
    "MRSTY.RRF": {"col_nums": [0, 3], "col_names": ["CUI", "STY"]},
}


def slurp(file: str) -> pd.DataFrame:
    cur_file = path_to_files / file
    print(f"reading {cur_file}...")
    df = pd.read_csv(
        cur_file,
        sep="|",
        header=None,
        usecols=rrf_info[file]["col_nums"],
        dtype=str,
        index_col=False,
    )
    df.columns = rrf_info[file]["col_names"]
    return df

def spurt(df: pd.DataFrame, name: str):
    output_file = path_to_files / (name + ".csv")
    df.to_csv(output_file, index=False, header=True)


print("transforming RRF to CSV...")

# Reading RRF files
conc = slurp("MRCONSO.RRF")
rel = slurp("MRREL.RRF")
defn = slurp("MRDEF.RRF")
sty = slurp("MRSTY.RRF")

# 只留下英文
conc = conc[conc.LAT == "ENG"]
print(f"ENG列共有：{conc.shape[0]}筆")

# avail_cuis是拿掉英文後非重複的MRCONSO CUI集合
# 其他table中，除了avail_cuis以外的CUI皆需排除，避免fk not found
avail_cuis = conc.CUI.unique()
print(f"available CUIs共有{avail_cuis.shape[0]}筆")


# 根據mmlite的原始碼（ExtractMrconsoPreferredNames）
# 擷取preferred name的方式是TS==P & STT=PF
conc_pn = conc[(conc.TS == "P") & (conc.STT == "PF")]
conc_pn = conc_pn[conc_pn.CUI.isin(avail_cuis)]
print(f"TS = P & STT = PF可擷取出preferred name的列有{conc_pn.shape[0]}筆")

conc_pn = conc_pn.drop_duplicates(subset=["CUI"])

spurt(conc_pn[["CUI", "STR"]], "concepts")
print(f"concepts共有 {conc_pn.shape[0]} 筆")

conc = conc[conc["CUI"].isin(avail_cuis)]
spurt(conc[["CUI", "STR", "SAB"]], "synonyms")
print(f"synonyms共有： {conc.shape[0]} 筆")

rel = rel[(rel["CUI1"].isin(avail_cuis)) & (rel["CUI2"].isin(avail_cuis))]
rel = rel[rel["REL"].isin(["RB", "RN"])]
rel = rel.drop_duplicates(subset=["CUI1", "REL", "CUI2"])
rel["REL"] = rel["REL"].replace({"RB": "BROADER", "RN": "NARROWER"})
spurt(rel, "relationships")
print(f"relationships共有： {rel.shape[0]} 筆")

defn = defn[defn["CUI"].isin(avail_cuis)]
spurt(defn, "definitions")
print(f"definitions共有： {defn.shape[0]} 筆")

sty = sty[sty["CUI"].isin(avail_cuis)]
spurt(sty, "semantic_types")
print(f"semantic_types共有： {sty.shape[0]} 筆")

print("轉換完成")
print(
    f"\noutput at {path_to_files}:\ncuis.csv\nsynonyms.csv\nrelationships.csv\ndefinitions.csv\nsemantic_types.csv"
)

# create table concepts (cui varchar(255) not null, preferred_name TEXT, primary key (cui));
# create table definitions (definition_id bigserial not null, meaning TEXT, source_name varchar(255), concept varchar(255), primary key (definition_id));
# create table relationships (relationship_id bigserial not null, rel_type varchar(255), concept1 varchar(255), concept2 varchar(255), primary key (relationship_id));
# create table semantic_types (semantic_type_id bigserial not null, type TEXT, concept varchar(255), primary key (semantic_type_id));
# create table synonyms (synonym_id bigserial not null, source_name varchar(255), term TEXT, concept varchar(255), primary key (synonym_id));
# create table umls_terms (id bigint not null, cui varchar(255), definition varchar(255), primary key (id));
# alter table if exists definitions add constraint FK1pi777hgvp3471m8m9oa7qew5 foreign key (concept) references concepts;
# alter table if exists relationships add constraint FKtjqdai8rava8qin4w9lgs5j8u foreign key (concept1) references concepts;
# alter table if exists relationships add constraint FKljpoh4gyhx45peg7yh3l3j1v5 foreign key (concept2) references concepts;
# alter table if exists semantic_types add constraint FKq9238djde3kt88550r5v6ovbk foreign key (concept) references concepts;
# alter table if exists synonyms add constraint FKiq2nko2f23nqk6l6gm1uhadh9 foreign key (concept) references concepts

# \copy concepts(cui, preferred_name) from 'C:\Users\blueb\Repo\umls-search\umls\data\concepts.csv' with (format csv, delimiter ',', encoding 'UTF-8', header);
# \copy definitions(concept, source_name, meaning) from 'C:\Users\blueb\Repo\umls-search\umls\data\definitions.csv' with (format csv, delimiter ',', encoding 'UTF-8', header);
# \copy relationships(concept1, rel_type, concept2) from 'C:\Users\blueb\Repo\umls-search\umls\data\relationships.csv' with (format csv, delimiter ',', encoding 'UTF-8', header);
# \copy synonyms(concept, term, source_name) from 'C:\Users\blueb\Repo\umls-search\umls\data\synonyms.csv' with (format csv, delimiter ',', encoding 'UTF-8', header);
# \copy semantic_types(concept, type) from 'C:\Users\blueb\Repo\umls-search\umls\data\synonyms.csv' with (format csv, delimiter ',', encoding 'UTF-8', header);
