import torch


class Converter:
    def __init__(self, use_gpu=False):
        self.use_gpu = use_gpu

    def create_tensor(self, tensor):
        if self.use_gpu:
            device = torch.device("cuda:0")
            tensor = tensor.to(device)
        return tensor

    def make_tensor(self, names, countries):
        seq_and_len = [self.name2list(name) for name in names]
        name_seqs = [sl[0] for sl in seq_and_len]
        seq_lens = torch.LongTensor([sl[1] for sl in seq_and_len])
        countries = countries.long()

        # make tensor of name: (batch_size, seq_len)
        seq_tensor = torch.zeros(len(name_seqs), seq_lens.max()).long()
        for idx, (seq, seq_len) in enumerate(zip(name_seqs, seq_lens), 0):
            seq_tensor[idx, :seq_len] = torch.LongTensor(seq)

        # sort by length to use pack_padded_sequence
        seq_lens, perm_idx = seq_lens.sort(dim=0, descending=True)
        seq_tensor = seq_tensor[perm_idx]
        countries = countries[perm_idx]

        return self.create_tensor(seq_tensor), self.create_tensor(seq_lens), self.create_tensor(countries)

    def name2list(self, name):
        arr = [ord(c) for c in name]
        return arr, len(arr)
