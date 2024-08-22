import math

from data_loader import MyDataLoader
from rnn_classifier import RNNClassifier
from converter import Converter

import torch
import numpy as np
import matplotlib.pyplot as plt

import time

# Parameters
HIDDEN_SIZE = 100
BATCH_SIZE = 256
N_LAYER = 2
N_EPOCHS = 100
N_CHARS = 128
N_COUNTRY = 0
USE_GPU = False


def init():
    loader = MyDataLoader(BATCH_SIZE)
    train_loader = loader.getTrainLoader()
    test_loader = loader.getTestLoader()
    N_COUNTRY = loader.getCountryNum()

    classifier = RNNClassifier(N_CHARS, HIDDEN_SIZE, N_COUNTRY, N_LAYER)
    if USE_GPU:
        device = torch.device("cuda:0")
        classifier.to(device)

    criterion = torch.nn.CrossEntropyLoss()
    optimizer = torch.optim.Adam(classifier.parameters(), lr=0.001)

    return loader, train_loader, test_loader, classifier, criterion, optimizer, Converter(USE_GPU)


def trainModel():
    total_loss = 0
    for i, (names, countries) in enumerate(train_loader, 1):
        inputs, seq_lengths, target = converter.make_tensor(names, countries)
        # forward
        output = classifier(inputs, seq_lengths)
        loss = criterion(output, target)
        optimizer.zero_grad()

        # backward
        loss.backward()
        optimizer.step()

        total_loss += loss.item()
        if i % 10 == 0:
            print(f'[{time_since(start)}] Epoch {epoch}', end='')
            print(f'[{i * len(inputs)} / {len(loader.getTrainSet())}]')
            print(f'loss={total_loss / (i * len(inputs))}')
        return total_loss


def testModel():
    correct = 0
    total = len(loader.getTestSet())
    print("evaluating trained model ...")

    with torch.no_grad():
        for i, (names, countries) in enumerate(test_loader, 1):
            inputs, seq_lengths, target = converter.make_tensor(names, countries)
            output = classifier(inputs, seq_lengths)
            pred = output.max(dim=1, keepdim=True)[1]
            correct += pred.eq(target.view_as(pred)).sum().item()

        percent = '%.2f' % (100 * correct / total)
        print(f'Test set: Accuracy {correct} / {total} {percent}%')
        return correct / total


def time_since(since):
    sec = time.time() - since
    min = math.floor(sec / 60)
    sec -= min * 60
    return '%dm %ds' % (min, sec)


def view(acc_list):
    epoch = np.arange(1, len(acc_list) + 1, 1)
    acc_list = np.array(acc_list)

    plt.plot(epoch, acc_list)
    plt.xlabel('Epoch')
    plt.ylabel('Accuracy')
    plt.show()


if __name__ == '__main__':
    start = time.time()
    print("start ...")

    loader, train_loader, test_loader, classifier, criterion, optimizer, converter = init()

    acc_list = []
    for epoch in range(1, N_EPOCHS + 1):
        trainModel()
        acc = testModel()
        acc_list.append(acc)

    view(acc_list)
    print("end ...")
