import torch
import torch.nn.functional as F
from residual_block import ResidualBlock


class CnnNet(torch.nn.Module):
    def __init__(self):
        super(CnnNet, self).__init__()
        # 卷积层
        self.conv1 = torch.nn.Conv2d(1, 16, kernel_size=5)
        self.conv2 = torch.nn.Conv2d(16, 32, kernel_size=5)
        # 池化层
        self.mp = torch.nn.MaxPool2d(2)

        # 残差模块
        self.r_block1 = ResidualBlock(16)
        self.r_block2 = ResidualBlock(32)

        # 全连接层
        self.fc = torch.nn.Linear(512, 10)

    def forward(self, x):
        in_size = x.size(0)  # 输入数据的样本数量

        # 卷积->Relu->池化->残差
        y = self.mp(F.relu(self.conv1(x)))
        y = self.r_block1(y)
        y = self.mp(F.relu(self.conv2(y)))
        y = self.r_block2(y)

        y = y.view(in_size, -1)  # flatten，将每条数据展开为一维向量
        return self.fc(y)
