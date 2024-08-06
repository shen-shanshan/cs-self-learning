import torch
import torch.nn.functional as F
import numpy as np
import matplotlib.pyplot as plt

# 1.prepare dataset
x_data = torch.Tensor([[1.0], [2.0], [3.0]])
y_data = torch.Tensor([[0], [0], [1]])


# 2.design model
class LogisticRegressionModel(torch.nn.Module):
    def __init__(self):
        super(LogisticRegressionModel, self).__init__()
        self.linear = torch.nn.Linear(1, 1)

    def forward(self, x):
        y_pred = F.sigmoid(self.linear(x))
        return y_pred


model = LogisticRegressionModel()

# 3.construct loss and optimizer
criterion = torch.nn.BCELoss()
optimizer = torch.optim.SGD(model.parameters(), lr=0.01)

# 4.train
for epoch in range(500):
    y_pred = model(x_data)
    loss = criterion(y_pred, y_data)
    if epoch % 100 == 0:
        print(epoch, loss.item())

    optimizer.zero_grad()
    loss.backward()
    optimizer.step()

# 5.predict
x = np.linspace(0, 10, 200)
x_t = torch.Tensor(x).view((200, 1))
y_t = model(x_t)
y = y_t.data.numpy()

plt.plot(x, y)
plt.plot([0, 10], [0.5, 0.5], c='r')
plt.xlabel('Hours')
plt.ylabel('Probility of Pass')
plt.grid()
plt.show()
