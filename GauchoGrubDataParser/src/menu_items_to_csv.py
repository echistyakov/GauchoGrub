import sys
import csv


def process_file(file_path):
    item_list = []
    with open(file_path, mode="rt") as file:
        lines = file.readlines()
        for line in lines:
            line = line.strip()
            menu_item_type = 1
            if "(v)" in line:
                menu_item_type = 2
            elif "(vgn)" in line:
                menu_item_type = 3
            line = line.replace("(v)", "").replace("(vgn)", "").strip()
            if line in [item["Title"] for item in item_list]:
                continue
            elif len(line) == 0:
                continue
            else:
                item_list.append({"Title": line, "MenuItemTypeId": menu_item_type})

    with open(file_path + ".csv", 'w') as csvfile:
        writer = csv.DictWriter(csvfile, ["Title", "MenuItemTypeId"], lineterminator='\n')
        writer.writeheader()
        writer.writerows(item_list)


if __name__ == "__main__":
    if len(sys.argv) == 2:
        process_file(sys.argv[1])
    else:
        print("Usage: " + sys.argv[0] + " file_path")