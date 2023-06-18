library(data.table)

umls_data_path <- "../data"

read_rrf <- function(fname, select_cols) {
  fread(
    file = file.path(umls_data_path, fname),
    colClasses = "character",
    select = select_cols,
    header = FALSE,
    sep = "|",
    quote = ""
  )
}

my_write_csv <- function(df, fname) {
  fname <- paste(fname, ".csv", sep="")
  fwrite(
    df,
    file = file.path(umls_data_path, fname),
  )
}

conc <- read_rrf("MRCONSO.RRF", c(1, 2, 3, 5, 12, 15))
setnames(conc, c("CUI", "LAT", "TS", "STT", "SAB", "STR"))

rel <- read_rrf("MRREL.RRF", c(1, 4, 5))
setnames(rel, c("CUI1", "REL", "CUI2"))

def <- read_rrf("MRDEF.RRF", c(1, 5, 6))
setnames(def, c("CUI", "SAB", "DEF"))

sty <- read_rrf("MRSTY.RRF", c(1, 4))
setnames(sty, c("CUI", "STY"))

# 只留下英文的
conc <- conc[conc$LAT == "ENG", ]
print(paste("ENG列共有：", conc[, .N], "個"))

# 除了avail_cuis以外的CUI皆需排除，避免fk not found
# avail_cuis <- intersect(conc$CUI, def$CUI)
# avail_cuis <- intersect(avail_cuis, sty$CUI)
avail_cuis <- unique(conc$CUI)
print(paste("available CUI共有", length(avail_cuis), "個"))

# 根據mmlite的原始碼（ExtractMrconsoPreferredNames）
# 擷取preferred name的方式是TS==P & STT=PF
conc_pn <- conc[TS == "P" & STT == "PF", ]
conc_pn <- conc_pn[CUI %in% avail_cuis, ]
print(paste("TS = P & STT = PF可擷取出preferred name的列有", conc_pn[, .N], "個"))

conc_pn <- unique(conc_pn, by = "CUI")
my_write_csv(conc_pn[, .(CUI, STR)], "concepts")
print(paste("concepts共有", conc_pn[, .N], "個"))

conc <- conc[CUI %in% avail_cuis, ]
my_write_csv(conc[, .(CUI, STR, SAB)], "synonyms")
print(paste("synonyms共有：", conc[, .N], "個"))

rel <- rel[CUI1 %in% avail_cuis & CUI2 %in% avail_cuis, ]
rel <- rel[REL %in% c("RB", "RN")]
rel$REL <- replace(rel$REL, rel$REL == "RB", "BROADER")
rel$REL <- replace(rel$REL, rel$REL == "RN", "NARROWER")
rel <- unique(rel, by = c("CUI1", "REL", "CUI2"))
my_write_csv(rel, "relationships")
print(paste("relationships共有：", rel[, .N], "個"))

def <- def[CUI %in% avail_cuis, ]
my_write_csv(def, "definitions")
print(paste("definitions共有：", def[, .N], "個"))

sty <- sty[CUI %in% avail_cuis, ]
my_write_csv(sty, "semantic_types")
print(paste("semantic_types共有：", sty[, .N], "個"))
