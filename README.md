# Unified Medical Language System (UMLS) Mock

- [Unified Medical Language System (UMLS) Mock](#unified-medical-language-system-umls-mock)
  - [Features](#features)
    - [遭遇問題](#遭遇問題)
  - [Requirements](#requirements)
  - [Working...](#working)
    - [1. 找到定義檔案以及其格式](#1-找到定義檔案以及其格式)
    - [2. 使用 Python 撰寫腳本將 .RRF 轉換成 .csv](#2-使用-python-撰寫腳本將-rrf-轉換成-csv)
    - [3. 將 .csv 匯入 Postgres](#3-將-csv-匯入-postgres)
    - [4. 後端 Entity, Repository, Service, Controller](#4-後端-entity-repository-service-controller)
    - [5. 前端介面](#5-前端介面)

## Features

**後端 API**

- [x] 透過 CUI 搜尋其定義
      測試: `http://localhost:8080/api/v1/umls/search/text/renal tubular acidosis`
- [x] 透過輸入文字搜尋其 CUIs 以及其定義
      測試: `http://localhost:8080/api/v1/umls/search/cui/C0000039`

**前端**

- [ ] 搜尋介面

### 遭遇問題

MetaMapLite 的資料集似乎比較少，有些輸入字串只能找到有限的 CUIs，然而 UMLS 的資料集中有更多的 CUIs，因此需要找到一個方法將 UMLS 的資料集轉換成 MetaMapLite 的資料集。

## Requirements

- UMLS Dataset
- MetaMapLite

## Working...

為了實現這個 UMLS 搜索系統，需要以下工具和技術：

- MetaMapLite：用於將用戶輸入的詞彙映射到相應的 CUI。
- Java Spring Boot 3：後端服務器框架。
- PostgreSQL：用於存儲 UMLS 數據的數據庫。

在 Java Spring Boot 3 中，需要以下依賴項(Dependencies)：

- Spring Boot Starter Data JPA：用於簡化數據庫操作。
- Spring Boot Starter Web：用於構建 Web 服務。
- PostgreSQL JDBC Driver：用於連接到 PostgreSQL 數據庫。

開始動工!

### 1. 找到定義檔案以及其格式

到官網[UMLS Knowledge Sources: File Downloads](https://www.nlm.nih.gov/research/umls/licensedcontent/umlsknowledgesources.html)下載 **UMLS Metathesaurus Level 0 Subset(NEW as of 2022AB)** 或其他版本。

看一下表格定義 [Definitions (File = MRDEF.RRF)](https://www.ncbi.nlm.nih.gov/books/NBK9685/table/ch03.T.definitions_file_mrdef_rrf/?report=objectonly)

| Col.     | Description                                                                                                                                                               |
| -------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| CUI      | Unique identifier for concept                                                                                                                                             |
| AUI      | Unique identifier for atom - variable length field, 8 or 9 characters                                                                                                     |
| ATUI     | Unique identifier for attribute                                                                                                                                           |
| SATUI    | Source asserted attribute identifier [optional-present if it exists]                                                                                                      |
| SAB      | Abbreviated source name (SAB) of the source of the definition。                                                                                                           |
| DEF      | Definition                                                                                                                                                                |
| SUPPRESS | Suppressible flag. Values = O, E, Y, or N. Reflects the suppressible status of the attribute; not yet in use. See also SUPPRESS in MRCONSO.RRF, MRREL.RRF, and MRSAT.RRF. |
| CVF      | Content View Flag. Bit field used to flag rows included in Content View. This field is a varchar field to maximize the number of bits available for use.                  |

看起來我們需要提取以下字段：

- `CUI`：概念的唯一標識符。
- `DEF`：定義。

### 2. 使用 Python 撰寫腳本將 .RRF 轉換成 .csv

因為對於 Postgres 來說 .csv 比較好匯入，所以寫個腳本轉個檔。

```python
import csv

def convert_rrf_to_csv(rrf_file, csv_file):
    with open(rrf_file, 'r', encoding='utf-8') as rrf, open(csv_file, 'w', encoding='utf-8', newline='') as csv_out:
        csv_writer = csv.writer(csv_out, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL)

        for line in rrf:
            fields = line.strip().split('|')

            # 只選擇cui和definition欄位，即索引0和5
            selected_fields = [fields[0], fields[5]]
            csv_writer.writerow(selected_fields)

if __name__ == "__main__":
    rrf_file = '../MRDEF.RRF'  # 替換為RRF文件路徑
    csv_file = '../MRDEF.csv'  # 希望保存的CSV文件路徑
    convert_rrf_to_csv(rrf_file, csv_file)
```

### 3. 將 .csv 匯入 Postgres

導入時發現一個問題: 一個 CUI 可能會對應到多個 Definitions。如 CUI=C0000097 就有兩個 Definitions

因此需要添加一個自動遞增的 id 作為主鍵。

這邊使用 PgAdmin 中的 Query Tools 建立一個表格

```sql
CREATE TABLE umls_terms (
    id SERIAL PRIMARY KEY,
    cui VARCHAR(255) NOT NULL,
    definition TEXT NOT NULL
);
```

右鍵單擊 `umls_terms` 表，然後選擇**Import/Export**。在彈出的窗口中，選擇以下選項：

- 選擇**導入**。
- 文件名：選擇剛才生成的 CSV 文件。
- 格式：選擇 **csv**。
- 編碼：選擇“**UTF8**”。
- 分隔符：選擇逗號 `,`。
- 引用字符：選擇雙引號 `"`。
- 跳脫字元: 無。
- **Columns to import**: 選擇 `cui` 和 `definition`就好。
  選擇“開始”。

資料庫部分完成!

### 4. 後端 Entity, Repository, Service, Controller

隨意寫

### 5. 前端介面

- 前端框架：React
- 狀態管理：Redux 或 React Context API
- UI 組件庫：Material-UI 或 Ant Design
- HTTP 客戶端庫：Axios
- 錯誤追踪和監控：Sentry
- 防抖(Debounce)和節流：[use-debounce](https://www.npmjs.com/package/use-debounce)

處理問題

- CORS
- .ENV
