# Claude Code 配置

## 在远程服务器上配置 Claude Code

```bash
# Install node.js
curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -
sudo apt-get install -y nodejs
# Verify
node -v
which node  # /usr/bin/node

# Install npm
mkdir ~/.npm-global
npm config set prefix '~/.npm-global'
echo 'export PATH=$HOME/.npm-global/bin:$PATH' >> ~/.bashrc
source ~/.bashrc
which npm  # /usr/bin/npm

# Install claude code
# npm install -g @anthropic-ai/claude-code
npm install -g @anthropic-ai/claude-code@2.1.78
claude --version
which claude  # /home/sss/.npm-global/bin/claude

echo 'export PATH=$(npm prefix -g)/bin:$PATH' >> ~/.bashrc
source ~/.bashrc

# 灵芽 API
mkdir -p ~/.claude
vim ~/.claude/settings.json
# {
#   "env": {
#     "ANTHROPIC_AUTH_TOKEN": "sk-VDW5xLgL3FDti2408N85PTIOYC5U27cwQm2MrafmNNOB7hVD",
#     "ANTHROPIC_BASE_URL": "https://api.lingyaai.cn",
#     "DISABLE_AUTOUPDATER": "1"
#   }
# }
cd ~/your-project
claude

# Configure network
# export HTTP_PROXY=http://127.0.0.1:10808
# export HTTPS_PROXY=http://127.0.0.1:10808

# Install claude-hud (https://github.com/jarrodwatts/claude-hud)
claude
/plugin marketplace add jarrodwatts/claude-hud
# Linux users:
# mkdir -p ~/.cache/tmp && TMPDIR=~/.cache/tmp claude
/plugin install claude-hud
/reload-plugins
/claude-hud:setup
# Please restart Claude Code now — quit and run claude again in your terminal.
# Once restarted, run /claude-hud:setup again to complete Step 4 and verify the HUD is working.
exit
# Add more configs
vim ~/.claude/plugins/claude-hud/config.json
# See https://github.com/jarrodwatts/claude-hud?tab=readme-ov-file#example-configuration
claude
/claude-hud:setup
```
