from torch.utils.data import Dataset

import gzip
import csv


class NameDataset(Dataset):
    def __init__(self, is_train_set=True):
        filename = '../data/names_train.csv.gz' if is_train_set else '../data/names_test.csv.gz'
        with gzip.open(filename, 'rt') as f:
            reader = csv.reader(f)
            rows = list(reader)
        # 将姓名和国家分别存入两个list
        self.names = [row[0] for row in rows]
        self.countries = [row[1] for row in rows]
        self.len = len(self.names)
        self.country_list = list(sorted(set(self.countries)))  # 去重+排序
        # 记录国家名称及其index的映射：{English: 4}
        self.country_dict = self.getCountryDict()
        self.country_num = len(self.country_list)

    def __getitem__(self, index):
        return self.names[index], self.country_dict[self.countries[index]]  # 返回示例：(Maclean, 4)

    def __len__(self):
        return self.len

    def getCountryDict(self):
        country_dict = dict()
        for idx, country_name in enumerate(self.country_list, 0):
            country_dict[country_name] = idx
        return country_dict

    def idx2country(self, index):
        return self.country_list[index]

    def getCountryNum(self):
        return self.country_num
