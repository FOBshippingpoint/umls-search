import pandas as pd
from pathlib import Path

cwd = Path.cwd()
path_to_files = cwd / "umls" / "data"

# Define the files and the columns to keep
files = {
    "MRREL.RRF": [0, 3, 4],                 # CUI1, REL, CUI2
    "MRCONSO.RRF": [0, 1, 6, 11, 12, 14],   # CUI, LAT, ISPREF, SAB, TTY, STR
    "MRDEF.RRF": [0, 4, 5],                 # CUI, SAB, DEF
    "MRSTY.RRF": [0, 3],                    # CUI, STY
}

print('transforming RRF to CSV...')
for file, cols in files.items():
    cur_file = path_to_files / file
    print(cur_file)

    df = pd.read_csv(
        cur_file,
        sep="|",
        header=None,
        usecols=cols,
        dtype=str,
        index_col=False,
    )

    if file == 'MRCONSO.RRF':
        df.columns = ['CUI', 'LAT', 'ISPREF', 'SAB', 'TTY', 'STR']
        df = df[df['LAT'] == 'ENG']  # Only keep rows where the language is English

        # collecting preferred names
        df_pn = df[df['TTY'] == 'PN'][['CUI', 'STR']]
        df_pn.to_csv(path_to_files / 'preferred_names.csv', index=False, header=True)
        
        # collecting synonyms
        df_syn = df[df['ISPREF'] == 'Y'][['CUI', 'STR', 'SAB']]
        df_syn.to_csv(path_to_files / 'synonyms.csv', index=False, header=True)

    elif file == 'MRREL.RRF':
        df.columns = ['CUI1', 'REL', 'CUI2']       
        df = df[df['REL'].isin(['RB', 'RN'])]
        df.to_csv(path_to_files / 'relationships.csv', index=False, header=True)

    elif file == 'MRDEF.RRF':
        df.columns = ['CUI', 'SAB', 'DEF']       
        df.to_csv(path_to_files / 'definitions.csv', index=False, header=True)

    elif file == 'MRSTY.RRF':
        df_sty = df.copy()
        df_sty.columns = ['CUI', 'STY']
        df_sty.to_csv(path_to_files / 'semantic_types.csv', index=False, header=True)

df_cuis = pd.merge(df_pn, df_sty, on='CUI')
df_cuis.to_csv(path_to_files / 'cuis.csv', index=False, header=True)

print(f"\noutput at {path_to_files}:\npreferred_names.csv\nsynonyms.csv\nrelationships.csv\ndefinitions.csv\nsemantic_types.csv\ncuis.csv")
