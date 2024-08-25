from torchvision import transforms
from torchvision import datasets
from torch.utils.data import DataLoader


class MyDataLoader:
    def __init__(self, batch_size):
        self.batch_size = batch_size
        self.transform = transforms.Compose([transforms.ToTensor(), transforms.Normalize((0.1307,), (0.3081,))])
        self.train_dataset = datasets.MNIST(root='../data/mnist/', train=True, download=True, transform=self.transform)
        self.test_dataset = datasets.MNIST(root='../data/mnist/', train=False, download=True, transform=self.transform)

    def get_train_loader(self):
        return DataLoader(self.train_dataset, shuffle=True, batch_size=self.batch_size)

    def get_test_loader(self):
        return DataLoader(self.test_dataset, shuffle=False, batch_size=self.batch_size)
