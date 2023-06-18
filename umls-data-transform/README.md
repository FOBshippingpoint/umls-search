# Usage

Transform UMLS dataset to csv files.

1. Download UMLS Metathesaurus Level 0 Subset from [here](https://www.nlm.nih.gov/research/umls/licensedcontent/umlsknowledgesources.html).
2. Extract MRCONSO.RRF, MRDEF.RRF, MRREL.RRF, and MRSTY.RRF into `/data`.
3. run script

```sh
# You could (or not) setup virtual environment first
cd umls-data-transform
pip install -r requirements.txt
python python/rrf_to_csv.py
```

4. The script will output csv files at `/data` folder.

---

## R

1. follow step 1 and 2 from Python section
2. run script

```sh
cd umls-data-transform/r
Rscript rrf_to_csv.R
```

---

More details see [UMLS CUI 階層架構實作構想](https://docs.google.com/document/d/1W-qFIMDEFvPpuVgeoDFXM6k0Wj3VUI-xnhumPm01W0A/edit#heading=h.lkgxkwgbrfsc).
