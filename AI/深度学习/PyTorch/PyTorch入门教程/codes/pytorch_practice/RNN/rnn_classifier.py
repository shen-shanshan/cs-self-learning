from converter import Converter

import torch
from torch.nn.utils.rnn import pack_padded_sequence

USE_GPU = False


class RNNClassifier(torch.nn.Module):
    def __init__(self, input_size, hidden_size, output_size, n_layers=1, bidirectional=True):
        super(RNNClassifier, self).__init__()
        # GRU layer's params
        self.hidden_size = hidden_size
        self.n_layers = n_layers
        self.n_directions = 2 if bidirectional else 1

        """
        Embedding layer:
        input shape: (seqLen, batchSize)
        output shape: (seqLen, batchSize, hiddenSize)
        """
        self.embedding = torch.nn.Embedding(input_size, hidden_size)

        """
        GRU layer:
        input shape:
            input: (seqLen, batchSize, hiddenSize)
            hidden: (n_layers * n_directions, batchSize, hiddenSize)
        output shape:
            output: (seqLen, batchSize, hiddenSize * n_directions)
            hidden: (n_layers * n_directions, batchSize, hiddenSize)
        """
        self.gru = torch.nn.GRU(hidden_size, hidden_size, n_layers, bidirectional=bidirectional)

        """
        全连接层
        """
        self.fc = torch.nn.Linear(hidden_size * self.n_directions, output_size)

    # 以单个下划线开头的变量或方法仅供内部使用，相当于Java中的private方法
    def _init_hidden(self, batch_size):
        hidden = torch.zeros(self.n_layers * self.n_directions, batch_size, self.hidden_size)
        converter = Converter(USE_GPU)
        return converter.create_tensor(hidden)

    def forward(self, input, seq_lengths):
        # 将输入转置：batch_size * seq_len -> seq_len * batch_size
        input = input.t()
        batch_size = input.size(1)

        """
        initial hidden:
        shape: (n_layers * n_directions, batchSize, hiddenSize)
        """
        hidden = self._init_hidden(batch_size)

        embedding = self.embedding(input)

        """
        embedding shape: (seqLen, batchSize, hiddenSize)
        seq_lengths: a list of seqLen of each batch element
        
        return: a PackedSequence object
        PackedSequence: holds the data and list of batch_sizes (not seqLen) of a packed sequence
        for example:
            data: a x
                  b
                  c
            batch_size shape: (2, 1, 1)
        """
        gru_input = pack_padded_sequence(embedding, seq_lengths)

        output, hidden = self.gru(gru_input, hidden)

        if self.n_directions == 2:
            # 沿纵轴拼接，拼接后的shape: (2, batchSize, hiddenSize)
            hidden_cat = torch.cat([hidden[-1], hidden[-2]], dim=1)
        else:
            # shape: (1, batchSize, hiddenSize)
            hidden_cat = hidden[-1]

        fc_output = self.fc(hidden_cat)
        return fc_output
