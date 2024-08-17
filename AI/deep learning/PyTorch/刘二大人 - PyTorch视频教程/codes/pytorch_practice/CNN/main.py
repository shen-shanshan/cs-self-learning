import torch
from data_loader import MyDataLoader
from cnn_net import CnnNet


def init():
    model = CnnNet()
    criterion = torch.nn.CrossEntropyLoss()
    optimizer = torch.optim.SGD(model.parameters(), lr=0.01, momentum=0.5)
    dataset = MyDataLoader(64)
    train_loader = dataset.get_train_loader()
    test_loader = dataset.get_test_loader()
    return model, criterion, optimizer, train_loader, test_loader


def train(epoch):
    running_loss = 0.0

    for batch_idx, data in enumerate(train_loader, 0):
        # 每次循环加载一个批量（64）的数据
        inputs, target = data
        optimizer.zero_grad()

        # forward + backward + update
        outputs = model(inputs)
        loss = criterion(outputs, target)
        loss.backward()
        optimizer.step()

        # 每300批（300*64个数据）计算一次loss
        running_loss += loss.item()
        if batch_idx % 300 == 299:
            print('[%d, %5d] loss: %.3f' % (epoch + 1, batch_idx + 1, running_loss / 300))
            running_loss = 0.0


def test():
    correct = 0
    total = 0

    with torch.no_grad():
        for data in test_loader:
            images, labels = data
            outputs = model(images)
            # 返回每行（每条数据）的最大值（预测概率）及其索引值（该类别的标签值）
            _, predicted = torch.max(outputs.data, dim=1)
            total += labels.size(0)  # 样本总数
            correct += (predicted == labels).sum().item()

    print('Accuracy on test set: %d %%' % (100 * correct / total))


if __name__ == '__main__':
    model, criterion, optimizer, train_loader, test_loader = init()

    for epoch in range(10):
        train(epoch)
        test()
