import numpy as np
import torch
from torch.utils.data import Dataset, DataLoader


# 1.prepare data
class DiabetesDataset(Dataset):
    def __init__(self, file_path):
        xy = np.loadtxt(file_path, delimiter=',', dtype=np.float32)
        self.len = xy.shape[0]
        self.x_data = torch.from_numpy(xy[:, :-1])
        self.y_data = torch.from_numpy(xy[:, [-1]])

    def __getitem__(self, index):
        return self.x_data[index], self.y_data[index]

    def __len__(self):
        return self.len


dataset = DiabetesDataset('data/diabetes.csv.gz')
train_loader = DataLoader(dataset=dataset, batch_size=32, shuffle=True, num_workers=5)


# 2.design model
class Model(torch.nn.Module):
    def __init__(self):
        super(Model, self).__init__()
        self.linear_1 = torch.nn.Linear(8, 6)
        self.linear_2 = torch.nn.Linear(6, 4)
        self.linear_3 = torch.nn.Linear(4, 1)
        self.sigmoid = torch.nn.Sigmoid()

    def forward(self, x):
        x = self.sigmoid(self.linear_1(x))
        x = self.sigmoid(self.linear_2(x))
        x = self.sigmoid(self.linear_3(x))
        return x


model = Model()

# 3.construct loss and optimizer
criterion = torch.nn.BCELoss()
optimizer = torch.optim.SGD(model.parameters(), lr=0.01)

# 4.training cycle
if __name__ == '__main__':
    print('data size: ', len(dataset))
    print('data size/batch: ', len(dataset) / 32)
    for epoch in range(100):
        for i, data in enumerate(train_loader, 0):
            # prepare data
            inputs, labels = data

            # forward
            y_pred = model(inputs)
            loss = criterion(y_pred, labels)
            print(epoch, i, loss.item())

            # backward
            optimizer.zero_grad()
            loss.backward()

            # update
            optimizer.step()
