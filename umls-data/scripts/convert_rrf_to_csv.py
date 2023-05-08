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