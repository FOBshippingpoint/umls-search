import pandas as pd
import os

# Get the current working directory
cwd = os.getcwd()

# Define the path to the files
path_to_files = cwd + "/umls/data/"

# Define the files and the columns to keep
files = {
    "MRCONSO.RRF": [0, 1, 11, 12, 14],
    "MRDEF.RRF": [0, 5],
    "MRREL.RRF": [0, 3],
    "MRSTY.RRF": [0, 1, 3],
    # "MRHIER.RRF": [0, 5, 7],
}

# Loop through the files
for file, cols in files.items():
    print(path_to_files + file)

    # Read the RRF file with pandas
    df = pd.read_csv(
        path_to_files + file,
        sep="|",
        header=None,
        usecols=cols,
        dtype=str,
        low_memory=False,
        index_col=False,
    )

    if file == "MRCONSO.RRF":
        df = df[df[1] == "ENG"]  # Only keep rows where the language is English

    # Write to csv
    df.to_csv(path_to_files + file.replace(".RRF", ".csv"), index=False, header=False)
