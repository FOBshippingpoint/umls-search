import pandas as pd
from pathlib import Path

cwd = Path.cwd()
path_to_files = cwd / "umls" / "data"

# Define the files and the columns to keep
target_cols = {
    "MRREL.RRF": [0, 3, 4],  # CUI1, REL, CUI2
    "MRCONSO.RRF": [0, 1, 6, 11, 12, 14],  # CUI, LAT, ISPREF, SAB, TTY, STR
    "MRDEF.RRF": [0, 4, 5],  # CUI, SAB, DEF
    "MRSTY.RRF": [0, 3],  # CUI, STY
}


def slurp(file):
    cur_file = path_to_files / file
    print(f"reading {cur_file}...")
    df = pd.read_csv(
        cur_file,
        sep="|",
        header=None,
        usecols=target_cols[file],
        dtype=str,
        index_col=False,
    )
    return df


def spurt(df, name):
    output_file = path_to_files / (name + ".csv")
    df.to_csv(output_file, index=False, header=True)


print("transforming RRF to CSV...")

# ============================================================================
df = slurp("MRCONSO.RRF")
# C5392097 this thing doesn't have PN, only PEP
df.columns = ["CUI", "LAT", "ISPREF", "SAB", "TTY", "STR"]
df = df[df["LAT"] == "ENG"]  # Only keep rows where the language is English

# collecting preferred names
df_pn = df[df["TTY"] == "PN"][["CUI", "STR"]]
spurt(df_pn, "cuis")

# collecting synonyms
df_syn = df[df["ISPREF"] == "Y"][["CUI", "STR", "SAB"]]
df_syn = df_syn[df_syn["CUI"].isin(df_pn["CUI"])]
spurt(df_syn, "synonyms")

# ============================================================================
df_rel = slurp("MRREL.RRF")
df_rel.columns = ["CUI1", "REL", "CUI2"]
df_rel = df_rel[df_rel["REL"].isin(["RB", "RN"])]
df_rel["REL"] = df_rel["REL"].replace({"RB": "BROADER", "RN": "NARROWER"})
df_rel = df_rel[df_rel["CUI1"].isin(df_pn["CUI"]) & df_rel["CUI2"].isin(df_pn["CUI"])]
spurt(df_rel, "relationships")

# ============================================================================
df_def = slurp("MRDEF.RRF")
df_def.columns = ["CUI", "SAB", "DEF"]
df_def = df_def[df_def["CUI"].isin(df_pn["CUI"])]
spurt(df_def, "definitions")

# ============================================================================
df_sty = slurp("MRSTY.RRF")
df_sty.columns = ["CUI", "STY"]
df_sty = df_sty[df_sty["CUI"].isin(df_pn["CUI"])]
spurt(df_sty, "semantic_types")

print(
    f"\noutput at {path_to_files}:\ncuis.csv\nsynonyms.csv\nrelationships.csv\ndefinitions.csv\nsemantic_types.csv"
)

# \copy cuis(cui, preferred_name) from 'C:\Users\blueb\Repo\umls-search\umls\data\cuis.csv' with (format csv, delimiter ',', header);
# \copy definitions(cui, source_name, meaning) from 'C:\Users\blueb\Repo\umls-search\umls\data\definitions.csv' with (format csv, delimiter ',', header);
# \copy relationships(cui1, rel_type, cui2) from 'C:\Users\blueb\Repo\umls-search\umls\data\relationships.csv' with (format csv, delimiter ',', header);
# \copy synonyms(cui, term, source_name) from 'C:\Users\blueb\Repo\umls-search\umls\data\synonyms.csv' with (format csv, delimiter ',', header);
# \copy semantic_types(cui, type) from 'C:\Users\blueb\Repo\umls-search\umls\data\synonyms.csv' with (format csv, delimiter ',', header);
