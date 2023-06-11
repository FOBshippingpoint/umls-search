import pandas as pd
from pathlib import Path

cwd = Path.cwd()
path_to_files = cwd / "umls" / "data"

# Define the files and the columns to keep
target_cols = {
    "MRREL.RRF": [0, 3, 4],                 # CUI1, REL, CUI2
    "MRCONSO.RRF": [0, 1, 6, 11, 12, 14],   # CUI, LAT, ISPREF, SAB, TTY, STR
    "MRDEF.RRF": [0, 4, 5],                 # CUI, SAB, DEF
    "MRSTY.RRF": [0, 3],                    # CUI, STY
}

def slurp(file):
    cur_file = path_to_files / file
    print(f'reading {cur_file}...')
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
    output_file = path_to_files / (name + '.csv')
    df.to_csv(output_file, index=False, header=True)

print('transforming RRF to CSV...')

# ============================================================================
df = slurp('MRCONSO.RRF')
df.columns = ['CUI', 'LAT', 'ISPREF', 'SAB', 'TTY', 'STR']
df = df[df['LAT'] == 'ENG']  # Only keep rows where the language is English

# collecting preferred names
df_pn = df[df['TTY'] == 'PN'][['CUI', 'STR']]
spurt(df_pn, 'preferred_names')
        
# collecting synonyms
df_syn = df[df['ISPREF'] == 'Y'][['CUI', 'STR', 'SAB']]
spurt(df_syn, 'synonyms')

# ============================================================================
df = slurp('MRREL.RRF')
df.columns = ['CUI1', 'REL', 'CUI2']       
df = df[df['REL'].isin(['RB', 'RN'])]
spurt(df, 'relationships')

# ============================================================================
df = slurp('MRDEF.RRF')
df.columns = ['CUI', 'SAB', 'DEF']       
spurt(df, 'definitions')

# ============================================================================
df = slurp('MRSTY.RRF')
df_sty = df.copy()
df_sty.columns = ['CUI', 'STY']
spurt(df_sty, 'semantic_types')

# ============================================================================
df_cuis = pd.merge(df_pn, df_sty, on='CUI')
spurt(df_cuis, 'cuis')

print(f"\noutput at {path_to_files}:\npreferred_names.csv\nsynonyms.csv\nrelationships.csv\ndefinitions.csv\nsemantic_types.csv\ncuis.csv")
