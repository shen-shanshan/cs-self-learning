# Convolutional layers for mel-spectrogram processing
self.conv2d1 = nn.Conv2d(1, config.downsample_hidden_size, 3, 2, padding=1)
self.conv2d2 = nn.Conv2d(
    config.downsample_hidden_size,
    config.downsample_hidden_size,
    3,
    2,
    padding=1,
)
self.conv2d3 = nn.Conv2d(
    config.downsample_hidden_size,
    config.downsample_hidden_size,
    3,
    2,
    padding=1,
)

# Apply convolutional layers (chunk if needed to avoid OOM)
if padded_feature.size(0) <= self.conv_chunksize:
    # Fast path: no chunking needed
    padded_embed = F.gelu(self.conv2d1(padded_feature))
    padded_embed = F.gelu(self.conv2d2(padded_embed))
    padded_embed = F.gelu(self.conv2d3(padded_embed))
else:
    # Chunked processing to avoid OOM
    padded_embeds = []
    for chunk in padded_feature.split(self.conv_chunksize, dim=0):
        padded_embed = F.gelu(self.conv2d1(chunk))
        padded_embed = F.gelu(self.conv2d2(padded_embed))
        padded_embed = F.gelu(self.conv2d3(padded_embed))
        padded_embeds.append(padded_embed)
    padded_embed = torch.cat(padded_embeds, dim=0)
