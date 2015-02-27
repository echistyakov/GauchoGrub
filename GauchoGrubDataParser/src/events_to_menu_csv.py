import sys
import csv
import datetime


def process_file(file_path, date):
    item_list = []
    with open(file_path, mode="rt") as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            new_date = date + datetime.timedelta(days=int(row["DayOfWeek"]) - 1)
            item_list.append({"EventId": row["Id"], "Date": str(new_date)})

    with open(file_path + ".csv", 'w') as csvfile:
        writer = csv.DictWriter(csvfile, ["EventId", "Date"], lineterminator='\n')
        writer.writeheader()
        writer.writerows(item_list)


if __name__ == "__main__":
    if len(sys.argv) == 3:
        split_date = sys.argv[2].split("/")
        date = datetime.date(int(split_date[2]), int(split_date[0]), int(split_date[1]))
        process_file(sys.argv[1], date)
    else:
        print("Usage: " + sys.argv[0] + " file_path mm/dd/yyyy")