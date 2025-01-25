from torch.utils.data import DataLoader
from name_dataset import NameDataset


class MyDataLoader:
    def __init__(self, batch_size):
        self.batch_size = batch_size
        self.train_dataset = NameDataset(is_train_set=True)
        self.test_dataset = NameDataset(is_train_set=False)

    def getTrainSet(self):
        return self.train_dataset

    def getTestSet(self):
        return self.test_dataset

    def getTrainLoader(self):
        return DataLoader(self.train_dataset, batch_size=self.batch_size, shuffle=True)

    def getTestLoader(self):
        return DataLoader(self.test_dataset, batch_size=self.batch_size, shuffle=False)

    def getCountryNum(self):
        return self.train_dataset.getCountryNum()  # output size of our model
